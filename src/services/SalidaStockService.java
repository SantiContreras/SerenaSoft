package services;
import Configuracion.conexion;
import Dao.StockProductoDao;
import model.StockProducto;

import java.sql.Connection;
import Dao.SalidaStockDao;
import Dao.DepositoDao;
import Dao.ProductoDao;

import model.Deposito;
import model.Producto;
import model.SalidaStock;
import model.SalidaStockDetalle;
import model.Usuario;

import sesion.SesionUsuario;

import java.math.BigDecimal;
import java.sql.SQLException;

import java.util.List;

public class SalidaStockService {

    // =========================================================
    // DAO
    // =========================================================

    private final SalidaStockDao salidaStockDao;
    private final DepositoDao depositoDao;
    private final ProductoDao productoDao;
    private final StockProductoDao stockProductoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SalidaStockService() {

        this.salidaStockDao =
                new SalidaStockDao();

        this.depositoDao =
                new DepositoDao();

        this.productoDao =
                new ProductoDao();
        
        this.stockProductoDao =
        new StockProductoDao();
    }


    // =========================================================
    // CREAR SALIDA
    // =========================================================

    /**
     * Crea solamente la cabecera.
     *
     * La salida siempre nace como BORRADOR.
     * Todavía NO modifica stock.
     */
    public ResultadoOperacion crearSalida(
            int idDeposito,
            String motivo,
            String destino,
            String observaciones) {

        // -----------------------------------------------------
        // VALIDAR SESIÓN
        // -----------------------------------------------------

        if (!SesionUsuario.haySesion()) {

            return ResultadoOperacion.error(
                    "No hay un usuario con sesión iniciada."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DEPÓSITO
        // -----------------------------------------------------

        if (idDeposito <= 0) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un depósito."
            );
        }


        // -----------------------------------------------------
        // VALIDAR MOTIVO
        // -----------------------------------------------------

        motivo = limpiar(motivo);

        if (motivo == null) {

            return ResultadoOperacion.error(
                    "Debe indicar el motivo de la salida."
            );
        }

        if (motivo.length() > 120) {

            return ResultadoOperacion.error(
                    "El motivo no puede superar "
                    + "los 120 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DESTINO
        // -----------------------------------------------------

        destino = limpiar(destino);

        if (destino != null
                && destino.length() > 150) {

            return ResultadoOperacion.error(
                    "El destino no puede superar "
                    + "los 150 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR OBSERVACIONES
        // -----------------------------------------------------

        observaciones =
                limpiar(observaciones);

        if (observaciones != null
                && observaciones.length() > 500) {

            return ResultadoOperacion.error(
                    "Las observaciones no pueden superar "
                    + "los 500 caracteres."
            );
        }


        try {

            Deposito deposito =
                    depositoDao.buscarPorId(
                            idDeposito
                    );

            if (deposito == null) {

                return ResultadoOperacion.error(
                        "El depósito seleccionado no existe."
                );
            }

            if (!deposito.isActivo()) {

                return ResultadoOperacion.error(
                        "El depósito seleccionado "
                        + "se encuentra inactivo."
                );
            }


            // -------------------------------------------------
            // USUARIO DE LA SESIÓN
            // -------------------------------------------------

            Usuario usuario =
                    SesionUsuario.getUsuarioActual();

            if (usuario == null) {

                return ResultadoOperacion.error(
                        "No se pudo obtener "
                        + "el usuario de la sesión."
                );
            }


            // -------------------------------------------------
            // CREAR SALIDA
            // -------------------------------------------------

            SalidaStock salida =
                    new SalidaStock();

            salida.setDeposito(
                    deposito
            );

            salida.setUsuario(
                    usuario
            );

            salida.setMotivo(
                    motivo
            );

            salida.setDestino(
                    destino
            );

            salida.setObservaciones(
                    observaciones
            );

            salida.setEstado(
                    "BORRADOR"
            );


            boolean guardado =
                    salidaStockDao.guardar(
                            salida
                    );

            if (!guardado) {

                return ResultadoOperacion.error(
                        "No se pudo crear "
                        + "la salida de stock."
                );
            }


            return ResultadoOperacion.ok(
                    "Salida de stock creada correctamente. "
                    + "ID: "
                    + salida.getIdSalida()
            );

        } catch (SQLException e) {

            return ResultadoOperacion.error(
                    "Error al crear la salida de stock: "
                    + e.getMessage()
            );
        }
    }
    
    
    // =========================================================
// CONFIRMAR SALIDA
// =========================================================

/**
 * Confirma una salida de stock.
 *
 * La operación completa se ejecuta dentro de una única
 * transacción.
 *
 * Flujo:
 *
 * 1. Bloquea la salida.
 * 2. Verifica que siga en BORRADOR.
 * 3. Verifica que tenga detalles.
 * 4. Bloquea el stock de cada producto.
 * 5. Valida existencia suficiente.
 * 6. Descuenta el stock.
 * 7. Cambia BORRADOR -> CONFIRMADA.
 * 8. COMMIT.
 *
 * Si cualquier paso falla:
 *
 * ROLLBACK.
 */
public ResultadoOperacion confirmarSalida(
        long idSalida) {

    // =====================================================
    // VALIDACIONES INICIALES
    // =====================================================

    if (idSalida <= 0) {

        return ResultadoOperacion.error(
                "La salida de stock no es válida."
        );
    }

    if (!SesionUsuario.haySesion()) {

        return ResultadoOperacion.error(
                "No hay un usuario con sesión iniciada."
        );
    }


    Connection cn = null;

    try {

        // =================================================
        // ABRIR CONEXIÓN
        // =================================================

        cn = conexion.getConexion();


        // =================================================
        // INICIAR TRANSACCIÓN
        // =================================================

        cn.setAutoCommit(false);


        // =================================================
        // 1. BLOQUEAR SALIDA
        // =================================================

        SalidaStock salida =
                salidaStockDao
                        .buscarPorIdParaActualizar(
                                idSalida,
                                cn
                        );

        if (salida == null) {

            cn.rollback();

            return ResultadoOperacion.error(
                    "La salida de stock no existe."
            );
        }


        // =================================================
        // 2. VALIDAR ESTADO
        // =================================================

        if (!salida.estaEnBorrador()) {

            cn.rollback();

            return ResultadoOperacion.error(
                    "La salida no puede confirmarse "
                    + "porque su estado actual es "
                    + salida.getEstado()
                    + "."
            );
        }


        // =================================================
        // 3. VALIDAR DETALLES
        // =================================================

        List<SalidaStockDetalle> detalles =
                salida.getDetalles();

        if (detalles == null
                || detalles.isEmpty()) {

            cn.rollback();

            return ResultadoOperacion.error(
                    "La salida no tiene productos."
            );
        }


        // =================================================
        // 4. VALIDAR TODO EL STOCK PRIMERO
        // =================================================
        //
        // IMPORTANTE:
        //
        // Primero bloqueamos y validamos TODOS los productos.
        // Todavía no descontamos nada.
        //
        // Esto hace el flujo más claro y seguro.
        // =================================================

        for (SalidaStockDetalle detalle
                : detalles) {

            if (detalle.getProducto() == null) {

                throw new SQLException(
                        "Existe un detalle sin producto."
                );
            }

            int idProducto =
                    detalle
                            .getProducto()
                            .getIdProducto();

            BigDecimal cantidadSalida =
                    detalle.getCantidad();


            // ---------------------------------------------
            // VALIDAR CANTIDAD
            // ---------------------------------------------

            if (cantidadSalida == null
                    || cantidadSalida.compareTo(
                            BigDecimal.ZERO) <= 0) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Existe un producto con "
                        + "una cantidad inválida."
                );
            }


            // ---------------------------------------------
            // BLOQUEAR STOCK
            // ---------------------------------------------

            StockProducto stock =
                    stockProductoDao
                            .buscarParaActualizar(
                                    cn,
                                    idProducto,
                                    salida
                                            .getDeposito()
                                            .getIdDeposito()
                            );


            // ---------------------------------------------
            // NO EXISTE STOCK PARA PRODUCTO/DEPÓSITO
            // ---------------------------------------------

            if (stock == null) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "No existe stock registrado para "
                        + detalle
                                .getProducto()
                                .getNombre()
                        + " en el depósito "
                        + salida
                                .getDeposito()
                                .getNombre()
                        + "."
                );
            }


            BigDecimal disponible =
                    stock.getCantidad();


            // ---------------------------------------------
            // VALIDACIÓN DEFENSIVA
            // ---------------------------------------------

            if (disponible == null) {

                disponible =
                        BigDecimal.ZERO;
            }


            // ---------------------------------------------
            // STOCK INSUFICIENTE
            // ---------------------------------------------

            if (disponible.compareTo(
                    cantidadSalida) < 0) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Stock insuficiente para "
                        + detalle
                                .getProducto()
                                .getNombre()
                        + ". Disponible: "
                        + disponible.stripTrailingZeros()
                                .toPlainString()
                        + " | Solicitado: "
                        + cantidadSalida
                                .stripTrailingZeros()
                                .toPlainString()
                );
            }
        }


        // =================================================
        // 5. DESCONTAR STOCK
        // =================================================
        //
        // Llegamos acá solamente si TODOS los productos
        // tienen stock suficiente.
        // =================================================

        for (SalidaStockDetalle detalle
                : detalles) {

            int idProducto =
                    detalle
                            .getProducto()
                            .getIdProducto();

            BigDecimal cantidadSalida =
                    detalle.getCantidad();


            // ---------------------------------------------
            // LA FILA YA ESTÁ BLOQUEADA POR FOR UPDATE
            // ---------------------------------------------

            StockProducto stock =
                    stockProductoDao
                            .buscarParaActualizar(
                                    cn,
                                    idProducto,
                                    salida
                                            .getDeposito()
                                            .getIdDeposito()
                            );

            if (stock == null) {

                throw new SQLException(
                        "El stock dejó de estar disponible "
                        + "durante la confirmación."
                );
            }


            // ---------------------------------------------
            // DESCONTAR
            // ---------------------------------------------

            boolean descontado =
                    stockProductoDao
                            .descontarStock(
                                    cn,
                                    stock.getIdStock(),
                                    cantidadSalida
                            );


            if (!descontado) {

                throw new SQLException(
                        "No se pudo descontar stock de "
                        + detalle
                                .getProducto()
                                .getNombre()
                        + "."
                );
            }
        }


        // =================================================
        // 6. CAMBIAR ESTADO
        // =================================================

        boolean confirmada =
                salidaStockDao
                        .cambiarEstadoSiCoincide(
                                idSalida,
                                "BORRADOR",
                                "CONFIRMADA",
                                cn
                        );


        if (!confirmada) {

            throw new SQLException(
                    "No se pudo cambiar el estado "
                    + "de la salida a CONFIRMADA."
            );
        }


        // =================================================
        // 7. COMMIT
        // =================================================

        cn.commit();


        return ResultadoOperacion.ok(
                "Salida de stock confirmada correctamente."
        );


    } catch (SQLException e) {

        // =================================================
        // ROLLBACK
        // =================================================

        if (cn != null) {

            try {

                cn.rollback();

            } catch (SQLException rollbackException) {

                System.err.println(
                        "Error al realizar rollback: "
                        + rollbackException.getMessage()
                );
            }
        }


        return ResultadoOperacion.error(
                "Error al confirmar la salida: "
                + e.getMessage()
        );


    } finally {

        // =================================================
        // RESTAURAR Y CERRAR CONNECTION
        // =================================================

        if (cn != null) {

            try {

                cn.setAutoCommit(true);

            } catch (SQLException e) {

                System.err.println(
                        "No se pudo restaurar AutoCommit: "
                        + e.getMessage()
                );
            }


            try {

                cn.close();

            } catch (SQLException e) {

                System.err.println(
                        "No se pudo cerrar la conexión: "
                        + e.getMessage()
                );
            }
        }
    }
}


    // =========================================================
    // AGREGAR PRODUCTO
    // =========================================================

    /**
     * Agrega un producto a una salida BORRADOR.
     *
     * Importante:
     * este método NO descuenta stock.
     */
    public ResultadoOperacion agregarProducto(
            long idSalida,
            int idProducto,
            BigDecimal cantidad) {

        if (idSalida <= 0) {

            return ResultadoOperacion.error(
                    "La salida no es válida."
            );
        }

        if (idProducto <= 0) {

            return ResultadoOperacion.error(
                    "El producto no es válido."
            );
        }

        if (cantidad == null) {

            return ResultadoOperacion.error(
                    "Debe indicar una cantidad."
            );
        }

        if (cantidad.compareTo(
                BigDecimal.ZERO) <= 0) {

            return ResultadoOperacion.error(
                    "La cantidad debe ser mayor a cero."
            );
        }


        try {

            // -------------------------------------------------
            // BUSCAR SALIDA
            // -------------------------------------------------

            SalidaStock salida =
                    salidaStockDao.buscarPorId(
                            idSalida
                    );

            if (salida == null) {

                return ResultadoOperacion.error(
                        "La salida de stock no existe."
                );
            }


            // -------------------------------------------------
            // SOLO BORRADOR
            // -------------------------------------------------

            if (!salida.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "Solo se pueden modificar "
                        + "salidas en estado BORRADOR."
                );
            }


            // -------------------------------------------------
            // BUSCAR PRODUCTO
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

            if (!producto.isActivo()) {

                return ResultadoOperacion.error(
                        "El producto seleccionado "
                        + "se encuentra inactivo."
                );
            }


            // -------------------------------------------------
            // VALIDAR DECIMALES
            // -------------------------------------------------

            if (producto.getUnidadVenta() != null) {

                boolean permiteDecimales =
                        producto
                                .getUnidadVenta()
                                .isPermiteDecimales();

                if (!permiteDecimales
                        && tieneDecimales(cantidad)) {

                    return ResultadoOperacion.error(
                            "La unidad "
                            + producto
                                    .getUnidadVenta()
                                    .getCodigo()
                            + " no permite "
                            + "cantidades decimales."
                    );
                }
            }


            // -------------------------------------------------
            // EVITAR PRODUCTO DUPLICADO
            // -------------------------------------------------

            List<SalidaStockDetalle> detalles =
                    salida.getDetalles();

            if (detalles != null) {

                for (SalidaStockDetalle detalle
                        : detalles) {

                    if (detalle.getProducto() != null
                            && detalle
                                    .getProducto()
                                    .getIdProducto()
                            == idProducto) {

                        return ResultadoOperacion.error(
                                "El producto ya fue agregado "
                                + "a esta salida."
                        );
                    }
                }
            }


            // -------------------------------------------------
            // CREAR DETALLE
            // -------------------------------------------------

            SalidaStockDetalle detalle =
                    new SalidaStockDetalle();

            detalle.setIdSalida(
                    idSalida
            );

            detalle.setProducto(
                    producto
            );

            detalle.setCantidad(
                    cantidad
            );


            boolean guardado =
                    salidaStockDao.guardarDetalle(
                            detalle
                    );

            if (!guardado) {

                return ResultadoOperacion.error(
                        "No se pudo agregar "
                        + "el producto a la salida."
                );
            }


            return ResultadoOperacion.ok(
                    "Producto agregado "
                    + "a la salida correctamente."
            );

        } catch (SQLException e) {

            return ResultadoOperacion.error(
                    "Error al agregar el producto: "
                    + e.getMessage()
            );
        }
    }


    // =========================================================
    // ELIMINAR DETALLE
    // =========================================================

    public ResultadoOperacion eliminarDetalle(
            long idSalida,
            long idDetalle) {

        if (idSalida <= 0
                || idDetalle <= 0) {

            return ResultadoOperacion.error(
                    "Los datos del detalle no son válidos."
            );
        }


        try {

            SalidaStock salida =
                    salidaStockDao.buscarPorId(
                            idSalida
                    );

            if (salida == null) {

                return ResultadoOperacion.error(
                        "La salida no existe."
                );
            }

            if (!salida.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "No se pueden eliminar productos "
                        + "de una salida que ya fue procesada."
                );
            }


            // -------------------------------------------------
            // VERIFICAR QUE EL DETALLE PERTENEZCA A LA SALIDA
            // -------------------------------------------------

            boolean pertenece = false;

            for (SalidaStockDetalle detalle
                    : salida.getDetalles()) {

                if (detalle.getIdDetalle()
                        == idDetalle) {

                    pertenece = true;
                    break;
                }
            }

            if (!pertenece) {

                return ResultadoOperacion.error(
                        "El detalle no pertenece "
                        + "a la salida indicada."
                );
            }


            boolean eliminado =
                    salidaStockDao.eliminarDetalle(
                            idDetalle
                    );

            if (!eliminado) {

                return ResultadoOperacion.error(
                        "No se pudo eliminar el detalle."
                );
            }


            return ResultadoOperacion.ok(
                    "Producto eliminado "
                    + "de la salida correctamente."
            );

        } catch (SQLException e) {

            return ResultadoOperacion.error(
                    "Error al eliminar el detalle: "
                    + e.getMessage()
            );
        }
    }


    // =========================================================
    // ACTUALIZAR CANTIDAD
    // =========================================================

    public ResultadoOperacion actualizarCantidad(
            long idSalida,
            long idDetalle,
            BigDecimal cantidad) {

        if (cantidad == null
                || cantidad.compareTo(
                        BigDecimal.ZERO) <= 0) {

            return ResultadoOperacion.error(
                    "La cantidad debe ser mayor a cero."
            );
        }


        try {

            SalidaStock salida =
                    salidaStockDao.buscarPorId(
                            idSalida
                    );

            if (salida == null) {

                return ResultadoOperacion.error(
                        "La salida no existe."
                );
            }

            if (!salida.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "Solo se pueden modificar "
                        + "salidas en estado BORRADOR."
                );
            }


            SalidaStockDetalle encontrado =
                    null;

            for (SalidaStockDetalle detalle
                    : salida.getDetalles()) {

                if (detalle.getIdDetalle()
                        == idDetalle) {

                    encontrado = detalle;
                    break;
                }
            }


            if (encontrado == null) {

                return ResultadoOperacion.error(
                        "El detalle no pertenece "
                        + "a esta salida."
                );
            }


            // -------------------------------------------------
            // VALIDAR DECIMALES
            // -------------------------------------------------

            Producto producto =
                    encontrado.getProducto();

            if (producto != null
                    && producto.getUnidadVenta() != null
                    && !producto
                            .getUnidadVenta()
                            .isPermiteDecimales()
                    && tieneDecimales(cantidad)) {

                return ResultadoOperacion.error(
                        "La unidad "
                        + producto
                                .getUnidadVenta()
                                .getCodigo()
                        + " no permite "
                        + "cantidades decimales."
                );
            }


            boolean actualizado =
                    salidaStockDao
                            .actualizarCantidadDetalle(
                                    idDetalle,
                                    cantidad
                            );

            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo actualizar "
                        + "la cantidad."
                );
            }


            return ResultadoOperacion.ok(
                    "Cantidad actualizada correctamente."
            );

        } catch (SQLException e) {

            return ResultadoOperacion.error(
                    "Error al actualizar la cantidad: "
                    + e.getMessage()
            );
        }
    }


    // =========================================================
    // BUSCAR SALIDA
    // =========================================================

    public SalidaStock buscarPorId(
            long idSalida) {

        try {

            return salidaStockDao.buscarPorId(
                    idSalida
            );

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar salida: "
                    + e.getMessage()
            );

            return null;
        }
    }


    // =========================================================
    // LISTAR TODAS
    // =========================================================

    public List<SalidaStock> listarTodas() {

        try {

            return salidaStockDao.listarTodas();

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar salidas: "
                    + e.getMessage()
            );

            return List.of();
        }
    }


    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================

    public List<SalidaStock> listarPorEstado(
            String estado) {

        try {

            return salidaStockDao.listarPorEstado(
                    estado
            );

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar salidas: "
                    + e.getMessage()
            );

            return List.of();
        }
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    /**
     * Determina si un BigDecimal posee una parte decimal
     * distinta de cero.
     *
     * 75.000 -> false
     * 75.500 -> true
     */
    private boolean tieneDecimales(
            BigDecimal cantidad) {

        return cantidad
                .stripTrailingZeros()
                .scale() > 0;
    }


    private String limpiar(
            String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        return texto.isEmpty()
                ? null
                : texto;
    }

    // =========================================================
    // REGISTRAR SALIDA COMPLETA - UNA SOLA TRANSACCIÓN
    // =========================================================
    public ResultadoOperacion registrarSalidaCompleta(
            int idDeposito, String motivo, String destino,
            String observaciones, List<SalidaStockDetalle> items) {

        if (!SesionUsuario.haySesion() || SesionUsuario.getUsuarioActual() == null)
            return ResultadoOperacion.error("Debe iniciar sesión.");
        if (idDeposito <= 0 || motivo == null || motivo.isBlank())
            return ResultadoOperacion.error("Depósito o motivo inválido.");
        if (items == null || items.isEmpty())
            return ResultadoOperacion.error("Agregue productos a la salida.");
        if (motivo.length() > 120 || (destino != null && destino.length() > 150)
                || (observaciones != null && observaciones.length() > 500))
            return ResultadoOperacion.error("Los datos de la salida superan la longitud permitida.");
        if ("Traslado".equalsIgnoreCase(motivo.trim())
                || "Venta / despacho".equalsIgnoreCase(motivo.trim()))
            return ResultadoOperacion.error("Las transferencias y ventas deben registrarse en sus módulos correspondientes.");

        Deposito deposito = depositoDao.buscarPorId(idDeposito);
        if (deposito == null || !deposito.isActivo())
            return ResultadoOperacion.error("El depósito no está activo.");

        // Validaciones previas y orden de bloqueo consistente.
        java.util.TreeMap<Integer, SalidaStockDetalle> ordenados = new java.util.TreeMap<>();
        for (SalidaStockDetalle item : items) {
            if (item == null || item.getProducto() == null || item.getCantidad() == null)
                return ResultadoOperacion.error("Hay un detalle incompleto.");
            int id = item.getProducto().getIdProducto();
            BigDecimal cantidad = item.getCantidad();
            if (id <= 0 || cantidad.signum() <= 0 || cantidad.scale() > 3)
                return ResultadoOperacion.error("Cantidad inválida para el producto " + id);
            if (ordenados.putIfAbsent(id, item) != null)
                return ResultadoOperacion.error("Hay productos duplicados en la salida.");
            Producto producto = productoDao.buscarPorId(id);
            if (producto == null || !producto.isActivo() || !producto.isControlaStock())
                return ResultadoOperacion.error("Producto inactivo o sin control de stock: " + id);
            if (producto.getUnidadVenta() != null
                    && "UN".equalsIgnoreCase(producto.getUnidadVenta().getCodigo())
                    && cantidad.stripTrailingZeros().scale() > 0)
                return ResultadoOperacion.error("El producto " + producto.getNombre() + " requiere unidades enteras.");
        }

        try (Connection cn = conexion.getConexion()) {
            cn.setAutoCommit(false);
            try {
                SalidaStock salida = new SalidaStock();
                salida.setDeposito(deposito);
                salida.setUsuario(SesionUsuario.getUsuarioActual());
                salida.setMotivo(motivo.trim());
                salida.setDestino(destino == null ? null : destino.trim());
                salida.setObservaciones(observaciones == null ? null : observaciones.trim());
                salida.setEstado("BORRADOR");
                if (!salidaStockDao.guardar(salida, cn) || salida.getIdSalida() <= 0)
                    throw new SQLException("No se pudo crear la salida.");

                for (SalidaStockDetalle item : ordenados.values()) {
                    int id = item.getProducto().getIdProducto();
                    StockProducto stock = stockProductoDao.buscarParaActualizar(cn, id, idDeposito);
                    if (stock == null || stock.getCantidad() == null
                            || stock.getCantidad().compareTo(item.getCantidad()) < 0)
                        throw new SQLException("Stock insuficiente para producto ID " + id);
                }
                for (SalidaStockDetalle item : ordenados.values()) {
                    int id = item.getProducto().getIdProducto();
                    StockProducto stock = stockProductoDao.buscarParaActualizar(cn, id, idDeposito);
                    item.setIdSalida(salida.getIdSalida());
                    if (!salidaStockDao.guardarDetalle(item, cn))
                        throw new SQLException("No se pudo guardar el detalle de " + id);
                    if (!stockProductoDao.descontarStock(cn, stock.getIdStock(), item.getCantidad()))
                        throw new SQLException("No se pudo descontar el stock de " + id);
                }
                if (!salidaStockDao.cambiarEstadoSiCoincide(
                        salida.getIdSalida(), "BORRADOR", "CONFIRMADA", cn))
                    throw new SQLException("No se pudo confirmar el estado.");
                cn.commit();
                return ResultadoOperacion.ok("Salida N.º " + salida.getIdSalida() + " confirmada. Stock descontado.");
            } catch (Exception ex) {
                cn.rollback();
                return ResultadoOperacion.error("No se registró la salida: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            return ResultadoOperacion.error("Error de conexión: " + ex.getMessage());
        }
    }

}
