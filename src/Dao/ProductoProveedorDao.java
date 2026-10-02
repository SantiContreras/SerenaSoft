package Dao;

import Configuracion.conexion;

import model.Producto;
import model.ProductoProveedor;
import model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class ProductoProveedorDao {

    // =========================================================
    // DAOS RELACIONADOS
    // =========================================================

    private final ProductoDao productoDao;
    private final ProveedorDao proveedorDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProductoProveedorDao() {

        this.productoDao =
                new ProductoDao();

        this.proveedorDao =
                new ProveedorDao();
    }


    // =========================================================
    // GUARDAR
    // =========================================================

    /**
     * Crea la relación entre un producto y un proveedor.
     *
     * La clave primaria está formada por:
     *
     * id_producto + id_proveedor
     */
    public boolean guardar(
            ProductoProveedor productoProveedor)
            throws SQLException {

        String sql =
                "INSERT INTO producto_proveedor "
                + "(id_producto, "
                + "id_proveedor, "
                + "codigo_proveedor, "
                + "costo_ultimo, "
                + "proveedor_principal, "
                + "fecha_ultima_compra) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    productoProveedor
                            .getProducto()
                            .getIdProducto()
            );

            ps.setInt(
                    2,
                    productoProveedor
                            .getProveedor()
                            .getIdProveedor()
            );

            ps.setString(
                    3,
                    productoProveedor
                            .getCodigoProveedor()
            );

            ps.setBigDecimal(
                    4,
                    productoProveedor
                            .getCostoUltimo()
            );

            ps.setBoolean(
                    5,
                    productoProveedor
                            .isProveedorPrincipal()
            );

            if (productoProveedor
                    .getFechaUltimaCompra() != null) {

                ps.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                productoProveedor
                                        .getFechaUltimaCompra()
                        )
                );

            } else {

                ps.setTimestamp(
                        6,
                        null
                );
            }

            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    /**
     * Actualiza los datos de una relación existente.
     *
     * No cambia producto ni proveedor porque ambos forman
     * la clave primaria de la relación.
     */
    public boolean actualizar(
            ProductoProveedor productoProveedor)
            throws SQLException {

        String sql =
                "UPDATE producto_proveedor "
                + "SET codigo_proveedor = ?, "
                + "costo_ultimo = ?, "
                + "proveedor_principal = ?, "
                + "fecha_ultima_compra = ? "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    productoProveedor
                            .getCodigoProveedor()
            );

            ps.setBigDecimal(
                    2,
                    productoProveedor
                            .getCostoUltimo()
            );

            ps.setBoolean(
                    3,
                    productoProveedor
                            .isProveedorPrincipal()
            );

            if (productoProveedor
                    .getFechaUltimaCompra() != null) {

                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(
                                productoProveedor
                                        .getFechaUltimaCompra()
                        )
                );

            } else {

                ps.setTimestamp(
                        4,
                        null
                );
            }

            ps.setInt(
                    5,
                    productoProveedor
                            .getProducto()
                            .getIdProducto()
            );

            ps.setInt(
                    6,
                    productoProveedor
                            .getProveedor()
                            .getIdProveedor()
            );

            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // BUSCAR POR PRODUCTO Y PROVEEDOR
    // =========================================================

    public ProductoProveedor buscar(
            int idProducto,
            int idProveedor)
            throws SQLException {

        String sql =
                "SELECT * "
                + "FROM producto_proveedor "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idProveedor
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapear(
                            rs
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // VERIFICAR EXISTENCIA
    // =========================================================

    public boolean existe(
            int idProducto,
            int idProveedor)
            throws SQLException {

        String sql =
                "SELECT 1 "
                + "FROM producto_proveedor "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idProveedor
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // =========================================================
    // LISTAR POR PRODUCTO
    // =========================================================

    /**
     * Devuelve todos los proveedores asociados a un producto.
     *
     * El proveedor principal aparece primero.
     */
    public List<ProductoProveedor> listarPorProducto(
            int idProducto)
            throws SQLException {

        List<ProductoProveedor> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM producto_proveedor "
                + "WHERE id_producto = ? "
                + "ORDER BY proveedor_principal DESC, "
                + "id_proveedor";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapear(rs)
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR POR PROVEEDOR
    // =========================================================

    /**
     * Devuelve todos los productos asociados a un proveedor.
     */
    public List<ProductoProveedor> listarPorProveedor(
            int idProveedor)
            throws SQLException {

        List<ProductoProveedor> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM producto_proveedor "
                + "WHERE id_proveedor = ? "
                + "ORDER BY id_producto";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProveedor
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapear(rs)
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<ProductoProveedor> listarTodos()
            throws SQLException {

        List<ProductoProveedor> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM producto_proveedor "
                + "ORDER BY id_producto, "
                + "proveedor_principal DESC, "
                + "id_proveedor";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapear(rs)
                );
            }
        }

        return lista;
    }


    // =========================================================
    // BUSCAR PROVEEDOR PRINCIPAL
    // =========================================================

    public ProductoProveedor buscarPrincipal(
            int idProducto)
            throws SQLException {

        String sql =
                "SELECT * "
                + "FROM producto_proveedor "
                + "WHERE id_producto = ? "
                + "AND proveedor_principal = TRUE "
                + "LIMIT 1";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapear(
                            rs
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // QUITAR PROVEEDOR PRINCIPAL
    // =========================================================

    /**
     * Quita la condición de principal a todos los proveedores
     * asociados al producto.
     *
     * Este método será utilizado por el Service antes de marcar
     * un nuevo proveedor como principal.
     */
    public boolean quitarPrincipales(
            int idProducto,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE producto_proveedor "
                + "SET proveedor_principal = FALSE "
                + "WHERE id_producto = ? "
                + "AND proveedor_principal = TRUE";

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.executeUpdate();

            return true;
        }
    }


    // =========================================================
    // MARCAR COMO PRINCIPAL
    // =========================================================

    /**
     * Marca una relación concreta como proveedor principal.
     *
     * Se utiliza dentro de una transacción controlada
     * por el Service.
     */
    public boolean marcarPrincipal(
            int idProducto,
            int idProveedor,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE producto_proveedor "
                + "SET proveedor_principal = TRUE "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idProveedor
            );

            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // ACTUALIZAR ÚLTIMO COSTO
    // =========================================================

    /**
     * Actualiza costo y fecha de última compra.
     *
     * Más adelante CompraService podrá utilizar este método
     * cuando confirmemos una compra.
     */
    public boolean actualizarUltimoCosto(
            int idProducto,
            int idProveedor,
            java.math.BigDecimal costoUltimo,
            java.time.LocalDateTime fechaUltimaCompra)
            throws SQLException {

        String sql =
                "UPDATE producto_proveedor "
                + "SET costo_ultimo = ?, "
                + "fecha_ultima_compra = ? "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    costoUltimo
            );

            if (fechaUltimaCompra != null) {

                ps.setTimestamp(
                        2,
                        Timestamp.valueOf(
                                fechaUltimaCompra
                        )
                );

            } else {

                ps.setTimestamp(
                        2,
                        null
                );
            }

            ps.setInt(
                    3,
                    idProducto
            );

            ps.setInt(
                    4,
                    idProveedor
            );

            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // ELIMINAR RELACIÓN
    // =========================================================

    /**
     * Elimina solamente la relación Producto-Proveedor.
     *
     * NO elimina ni el producto ni el proveedor.
     */
    public boolean eliminar(
            int idProducto,
            int idProveedor)
            throws SQLException {

        String sql =
                "DELETE FROM producto_proveedor "
                + "WHERE id_producto = ? "
                + "AND id_proveedor = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idProveedor
            );

            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // MAPEAR RESULTSET
    // =========================================================

    private ProductoProveedor mapear(
            ResultSet rs)
            throws SQLException {

        ProductoProveedor relacion =
                new ProductoProveedor();


        // -----------------------------------------------------
        // PRODUCTO
        // -----------------------------------------------------

        int idProducto =
                rs.getInt(
                        "id_producto"
                );

        Producto producto =
                productoDao.buscarPorId(
                        idProducto
                );

        relacion.setProducto(
                producto
        );


        // -----------------------------------------------------
        // PROVEEDOR
        // -----------------------------------------------------

        int idProveedor =
                rs.getInt(
                        "id_proveedor"
                );

        Proveedor proveedor =
                proveedorDao.buscarPorId(
                        idProveedor
                );

        relacion.setProveedor(
                proveedor
        );


        // -----------------------------------------------------
        // DATOS DE LA RELACIÓN
        // -----------------------------------------------------

        relacion.setCodigoProveedor(
                rs.getString(
                        "codigo_proveedor"
                )
        );

        relacion.setCostoUltimo(
                rs.getBigDecimal(
                        "costo_ultimo"
                )
        );

        relacion.setProveedorPrincipal(
                rs.getBoolean(
                        "proveedor_principal"
                )
        );


        // -----------------------------------------------------
        // FECHA ÚLTIMA COMPRA
        // -----------------------------------------------------

        Timestamp fecha =
                rs.getTimestamp(
                        "fecha_ultima_compra"
                );

        if (fecha != null) {

            relacion.setFechaUltimaCompra(
                    fecha.toLocalDateTime()
            );
        }

        return relacion;
    }
}