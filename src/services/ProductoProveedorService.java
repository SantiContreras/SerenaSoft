package services;

import Configuracion.conexion;

import Dao.ProductoDao;
import Dao.ProductoProveedorDao;
import Dao.ProveedorDao;

import model.Producto;
import model.ProductoProveedor;
import model.Proveedor;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;


public class ProductoProveedorService {

    // =========================================================
    // DAOS
    // =========================================================

    private final ProductoProveedorDao productoProveedorDao;
    private final ProductoDao productoDao;
    private final ProveedorDao proveedorDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProductoProveedorService() {

        this.productoProveedorDao =
                new ProductoProveedorDao();

        this.productoDao =
                new ProductoDao();

        this.proveedorDao =
                new ProveedorDao();
    }


    // =========================================================
    // ASOCIAR PRODUCTO CON PROVEEDOR
    // =========================================================

    /**
     * Crea una nueva relación Producto-Proveedor.
     *
     * Un producto puede tener varios proveedores.
     * La misma combinación Producto-Proveedor no puede repetirse.
     */
    public ResultadoOperacion asociar(
            int idProducto,
            int idProveedor,
            String codigoProveedor,
            BigDecimal costoUltimo,
            boolean proveedorPrincipal) {

        // -----------------------------------------------------
        // VALIDACIONES BÁSICAS
        // -----------------------------------------------------

        if (idProducto <= 0) {

            return ResultadoOperacion.error(
                    "El producto indicado no es válido."
            );
        }

        if (idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "El proveedor indicado no es válido."
            );
        }


        // -----------------------------------------------------
        // LIMPIAR CÓDIGO DEL PROVEEDOR
        // -----------------------------------------------------

        codigoProveedor =
                limpiar(
                        codigoProveedor
                );

        if (codigoProveedor != null
                && codigoProveedor.length() > 100) {

            return ResultadoOperacion.error(
                    "El código del proveedor no puede "
                    + "superar los 100 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR COSTO
        // -----------------------------------------------------

        if (costoUltimo != null
                && costoUltimo.compareTo(
                        BigDecimal.ZERO
                ) < 0) {

            return ResultadoOperacion.error(
                    "El último costo no puede ser negativo."
            );
        }


        try {

            // -------------------------------------------------
            // VALIDAR PRODUCTO
            // -------------------------------------------------

            Producto producto =
                    productoDao.buscarPorId(
                            idProducto
                    );

            if (producto == null) {

                return ResultadoOperacion.error(
                        "El producto seleccionado no existe."
                );
            }


            // -------------------------------------------------
            // VALIDAR PROVEEDOR
            // -------------------------------------------------

            Proveedor proveedor =
                    proveedorDao.buscarPorId(
                            idProveedor
                    );

            if (proveedor == null) {

                return ResultadoOperacion.error(
                        "El proveedor seleccionado no existe."
                );
            }


            // -------------------------------------------------
            // EVITAR RELACIÓN DUPLICADA
            // -------------------------------------------------

            if (productoProveedorDao.existe(
                    idProducto,
                    idProveedor)) {

                return ResultadoOperacion.error(
                        "El proveedor ya se encuentra "
                        + "asociado a este producto."
                );
            }


            // -------------------------------------------------
            // CREAR RELACIÓN
            // -------------------------------------------------

            ProductoProveedor relacion =
                    new ProductoProveedor();

            relacion.setProducto(
                    producto
            );

            relacion.setProveedor(
                    proveedor
            );

            relacion.setCodigoProveedor(
                    codigoProveedor
            );

            relacion.setCostoUltimo(
                    costoUltimo
            );

            /*
             * Primero guardamos la relación SIN marcarla
             * como principal.
             *
             * Si debe ser principal, lo hacemos después
             * mediante la operación transaccional.
             */
            relacion.setProveedorPrincipal(
                    false
            );

            relacion.setFechaUltimaCompra(
                    null
            );


            boolean guardado =
                    productoProveedorDao.guardar(
                            relacion
                    );

            if (!guardado) {

                return ResultadoOperacion.error(
                        "No se pudo asociar el proveedor "
                        + "al producto."
                );
            }


            // -------------------------------------------------
            // MARCAR COMO PRINCIPAL SI CORRESPONDE
            // -------------------------------------------------

            if (proveedorPrincipal) {

                ResultadoOperacion resultadoPrincipal =
                        establecerPrincipal(
                                idProducto,
                                idProveedor
                        );

                if (!resultadoPrincipal.isExitoso()) {

                    /*
                     * La asociación ya existe.
                     * No la eliminamos automáticamente porque
                     * los datos fueron guardados correctamente.
                     */
                    return ResultadoOperacion.error(
                            "El proveedor fue asociado, "
                            + "pero no pudo establecerse "
                            + "como principal. "
                            + resultadoPrincipal.getMensaje()
                    );
                }
            }


            return ResultadoOperacion.ok(
                    "Proveedor asociado al producto "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al asociar proveedor y producto: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ACTUALIZAR RELACIÓN
    // =========================================================

    /**
     * Modifica código y costo de una relación existente.
     *
     * El proveedor principal se administra mediante
     * establecerPrincipal().
     */
    public ResultadoOperacion actualizar(
            int idProducto,
            int idProveedor,
            String codigoProveedor,
            BigDecimal costoUltimo) {

        if (idProducto <= 0
                || idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "La relación Producto-Proveedor "
                    + "no es válida."
            );
        }


        codigoProveedor =
                limpiar(
                        codigoProveedor
                );

        if (codigoProveedor != null
                && codigoProveedor.length() > 100) {

            return ResultadoOperacion.error(
                    "El código del proveedor no puede "
                    + "superar los 100 caracteres."
            );
        }


        if (costoUltimo != null
                && costoUltimo.compareTo(
                        BigDecimal.ZERO
                ) < 0) {

            return ResultadoOperacion.error(
                    "El último costo no puede ser negativo."
            );
        }


        try {

            ProductoProveedor relacion =
                    productoProveedorDao.buscar(
                            idProducto,
                            idProveedor
                    );

            if (relacion == null) {

                return ResultadoOperacion.error(
                        "La relación Producto-Proveedor "
                        + "no existe."
                );
            }


            relacion.setCodigoProveedor(
                    codigoProveedor
            );

            relacion.setCostoUltimo(
                    costoUltimo
            );


            boolean actualizado =
                    productoProveedorDao.actualizar(
                            relacion
                    );


            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo actualizar "
                        + "la relación Producto-Proveedor."
                );
            }


            return ResultadoOperacion.ok(
                    "Relación Producto-Proveedor "
                    + "actualizada correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al actualizar la relación: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ESTABLECER PROVEEDOR PRINCIPAL
    // =========================================================

    /**
     * Deja un único proveedor principal para el producto.
     *
     * Operación transaccional:
     *
     * 1. Comprueba que la relación exista.
     * 2. Quita cualquier principal anterior.
     * 3. Marca el nuevo proveedor.
     * 4. COMMIT.
     *
     * Ante cualquier error se ejecuta ROLLBACK.
     */
    public ResultadoOperacion establecerPrincipal(
            int idProducto,
            int idProveedor) {

        if (idProducto <= 0
                || idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "Producto o proveedor no válido."
            );
        }


        try {

            if (!productoProveedorDao.existe(
                    idProducto,
                    idProveedor)) {

                return ResultadoOperacion.error(
                        "El proveedor no está asociado "
                        + "al producto."
                );
            }

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "No se pudo verificar la relación: "
                    + ex.getMessage()
            );
        }


        Connection cn = null;

        try {

            cn =
                    conexion.getConexion();

            cn.setAutoCommit(
                    false
            );


            // -------------------------------------------------
            // QUITAR PRINCIPAL ANTERIOR
            // -------------------------------------------------

            productoProveedorDao.quitarPrincipales(
                    idProducto,
                    cn
            );


            // -------------------------------------------------
            // MARCAR NUEVO PRINCIPAL
            // -------------------------------------------------

            boolean marcado =
                    productoProveedorDao.marcarPrincipal(
                            idProducto,
                            idProveedor,
                            cn
                    );


            if (!marcado) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "No se pudo establecer "
                        + "el proveedor principal."
                );
            }


            // -------------------------------------------------
            // CONFIRMAR
            // -------------------------------------------------

            cn.commit();


            return ResultadoOperacion.ok(
                    "Proveedor principal actualizado "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            if (cn != null) {

                try {

                    cn.rollback();

                } catch (SQLException ignored) {
                }
            }


            return ResultadoOperacion.error(
                    "Error al establecer proveedor principal: "
                    + ex.getMessage()
            );


        } finally {

            if (cn != null) {

                try {

                    cn.setAutoCommit(
                            true
                    );

                } catch (SQLException ignored) {
                }


                try {

                    cn.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }


    // =========================================================
    // ACTUALIZAR ÚLTIMO COSTO
    // =========================================================

    /**
     * Actualiza el último costo conocido del producto
     * para ese proveedor.
     *
     * También registra la fecha de la compra.
     *
     * Más adelante CompraService podrá llamar a este método
     * automáticamente al confirmar una compra.
     */
    public ResultadoOperacion actualizarUltimoCosto(
            int idProducto,
            int idProveedor,
            BigDecimal costoUltimo,
            LocalDateTime fechaUltimaCompra) {

        if (idProducto <= 0
                || idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "Producto o proveedor no válido."
            );
        }


        if (costoUltimo == null) {

            return ResultadoOperacion.error(
                    "Debe indicar el costo."
            );
        }


        if (costoUltimo.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            return ResultadoOperacion.error(
                    "El costo no puede ser negativo."
            );
        }


        if (fechaUltimaCompra == null) {

            fechaUltimaCompra =
                    LocalDateTime.now();
        }


        try {

            if (!productoProveedorDao.existe(
                    idProducto,
                    idProveedor)) {

                return ResultadoOperacion.error(
                        "El proveedor no está asociado "
                        + "al producto."
                );
            }


            boolean actualizado =
                    productoProveedorDao.actualizarUltimoCosto(
                            idProducto,
                            idProveedor,
                            costoUltimo,
                            fechaUltimaCompra
                    );


            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo actualizar "
                        + "el último costo."
                );
            }


            return ResultadoOperacion.ok(
                    "Último costo actualizado "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al actualizar el último costo: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // BUSCAR RELACIÓN
    // =========================================================

    public ProductoProveedor buscar(
            int idProducto,
            int idProveedor) {

        if (idProducto <= 0
                || idProveedor <= 0) {

            return null;
        }


        try {

            return productoProveedorDao.buscar(
                    idProducto,
                    idProveedor
            );

        } catch (SQLException ex) {

            return null;
        }
    }


    // =========================================================
    // BUSCAR PROVEEDOR PRINCIPAL
    // =========================================================

    public ProductoProveedor buscarPrincipal(
            int idProducto) {

        if (idProducto <= 0) {

            return null;
        }


        try {

            return productoProveedorDao.buscarPrincipal(
                    idProducto
            );

        } catch (SQLException ex) {

            return null;
        }
    }


    // =========================================================
    // LISTAR POR PRODUCTO
    // =========================================================

    public List<ProductoProveedor> listarPorProducto(
            int idProducto) {

        if (idProducto <= 0) {

            return Collections.emptyList();
        }


        try {

            return productoProveedorDao.listarPorProducto(
                    idProducto
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // LISTAR POR PROVEEDOR
    // =========================================================

    public List<ProductoProveedor> listarPorProveedor(
            int idProveedor) {

        if (idProveedor <= 0) {

            return Collections.emptyList();
        }


        try {

            return productoProveedorDao.listarPorProveedor(
                    idProveedor
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<ProductoProveedor> listarTodos() {

        try {

            return productoProveedorDao.listarTodos();

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // ELIMINAR ASOCIACIÓN
    // =========================================================

    /**
     * Elimina únicamente la relación.
     *
     * No elimina el Producto ni el Proveedor.
     */
    public ResultadoOperacion eliminar(
            int idProducto,
            int idProveedor) {

        if (idProducto <= 0
                || idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "La relación Producto-Proveedor "
                    + "no es válida."
            );
        }


        try {

            ProductoProveedor relacion =
                    productoProveedorDao.buscar(
                            idProducto,
                            idProveedor
                    );


            if (relacion == null) {

                return ResultadoOperacion.error(
                        "La relación Producto-Proveedor "
                        + "no existe."
                );
            }


            /*
             * Por ahora permitimos eliminar incluso al principal.
             *
             * Si quedan otros proveedores, simplemente el producto
             * quedará momentáneamente sin proveedor principal.
             *
             * Después podemos decidir desde la interfaz si queremos
             * obligar al usuario a elegir otro.
             */
            boolean eliminado =
                    productoProveedorDao.eliminar(
                            idProducto,
                            idProveedor
                    );


            if (!eliminado) {

                return ResultadoOperacion.error(
                        "No se pudo eliminar "
                        + "la asociación."
                );
            }


            return ResultadoOperacion.ok(
                    "Proveedor desvinculado del producto "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al eliminar la asociación: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(
            String texto) {

        if (texto == null) {

            return null;
        }


        texto =
                texto.trim();


        if (texto.isEmpty()) {

            return null;
        }


        return texto;
    }
}