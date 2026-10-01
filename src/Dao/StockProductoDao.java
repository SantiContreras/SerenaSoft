package Dao;

import Configuracion.conexion;

import model.Deposito;
import model.Producto;
import model.StockProducto;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class StockProductoDao {

    private final ProductoDao productoDao;
    private final DepositoDao depositoDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public StockProductoDao() {

        productoDao
                = new ProductoDao();

        depositoDao
                = new DepositoDao();
    }

    // =========================================================
    // GUARDAR
    // =========================================================
    public boolean guardar(
            StockProducto stock) {

        String sql
                = "INSERT INTO stock_producto "
                + "(id_producto, id_deposito, cantidad) "
                + "VALUES (?, ?, ?)";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(
                    1,
                    stock.getProducto().getIdProducto()
            );

            ps.setInt(
                    2,
                    stock.getDeposito().getIdDeposito()
            );

            ps.setBigDecimal(
                    3,
                    stock.getCantidad()
            );

            int filas
                    = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs
                        = ps.getGeneratedKeys()) {

                    if (rs.next()) {

                        stock.setIdStock(
                                rs.getLong(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar stock:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return false;
    }

    // =========================================================
    // ACTUALIZAR CANTIDAD
    // =========================================================
    public boolean actualizarCantidad(
            long idStock,
            BigDecimal cantidad) {

        String sql
                = "UPDATE stock_producto "
                + "SET cantidad = ? "
                + "WHERE id_stock = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    cantidad
            );

            ps.setLong(
                    2,
                    idStock
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar stock:"
            );

            System.err.println(
                    e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================
    public StockProducto buscarPorId(
            long idStock) {

        String sql
                = "SELECT * "
                + "FROM stock_producto "
                + "WHERE id_stock = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idStock
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    return mapearStock(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar stock por ID:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // BUSCAR POR PRODUCTO Y DEPÓSITO
    // =========================================================
    public StockProducto buscarPorProductoDeposito(
            int idProducto,
            int idDeposito) {

        String sql
                = "SELECT * "
                + "FROM stock_producto "
                + "WHERE id_producto = ? "
                + "AND id_deposito = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    return mapearStock(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar stock por producto y depósito:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // LISTAR TODO EL STOCK
    // =========================================================
    public List<StockProducto> listarTodos() {

        List<StockProducto> lista
                = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM stock_producto "
                + "ORDER BY id_producto, id_deposito";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql); ResultSet rs
                = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapearStock(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar stock:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // LISTAR STOCK POR PRODUCTO
    // =========================================================
    public List<StockProducto> listarPorProducto(
            int idProducto) {

        List<StockProducto> lista
                = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM stock_producto "
                + "WHERE id_producto = ? "
                + "ORDER BY id_deposito";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearStock(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar stock del producto:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // LISTAR STOCK POR DEPÓSITO
    // =========================================================
    public List<StockProducto> listarPorDeposito(
            int idDeposito) {

        List<StockProducto> lista
                = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM stock_producto "
                + "WHERE id_deposito = ? "
                + "ORDER BY id_producto";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idDeposito
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearStock(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar stock del depósito:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // OBTENER CANTIDAD
    // =========================================================
    public BigDecimal obtenerCantidad(
            int idProducto,
            int idDeposito) {

        String sql
                = "SELECT cantidad "
                + "FROM stock_producto "
                + "WHERE id_producto = ? "
                + "AND id_deposito = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getBigDecimal(
                            "cantidad"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener cantidad de stock:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return BigDecimal.ZERO;
    }

    // =========================================================
// BUSCAR STOCK PARA ACTUALIZACIÓN
// TRANSACCIONAL - FOR UPDATE
// =========================================================
    /**
     * Busca el registro de stock de un producto en un depósito utilizando la
     * misma conexión de una transacción.
     *
     * FOR UPDATE bloquea el registro hasta que la transacción haga COMMIT o
     * ROLLBACK.
     *
     * Este método NO abre ni cierra la Connection.
     */
    public StockProducto buscarParaActualizar(
            Connection cn,
            int idProducto,
            int idDeposito) throws SQLException {

        String sql
                = "SELECT id_stock, id_producto, id_deposito, "
                + "cantidad, fecha_actualizacion "
                + "FROM stock_producto "
                + "WHERE id_producto = ? "
                + "AND id_deposito = ? "
                + "FOR UPDATE";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    StockProducto stock
                            = new StockProducto();

                    stock.setIdStock(
                            rs.getLong(
                                    "id_stock"
                            )
                    );

                    stock.setCantidad(
                            rs.getBigDecimal(
                                    "cantidad"
                            )
                    );

                    Timestamp timestamp
                            = rs.getTimestamp(
                                    "fecha_actualizacion"
                            );

                    if (timestamp != null) {

                        stock.setFechaActualizacion(
                                timestamp.toLocalDateTime()
                        );
                    }

                    return stock;
                }
            }
        }

        return null;
    }

// =========================================================
// ACTUALIZAR CANTIDAD
// TRANSACCIONAL
// =========================================================
    /**
     * Actualiza el stock utilizando una Connection existente.
     *
     * Este método NO hace COMMIT. Este método NO hace ROLLBACK. Este método NO
     * cierra la Connection.
     *
     * El Service controla la transacción.
     */
    public boolean actualizarCantidad(
            Connection cn,
            long idStock,
            BigDecimal cantidad) throws SQLException {

        String sql
                = "UPDATE stock_producto "
                + "SET cantidad = ? "
                + "WHERE id_stock = ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    cantidad
            );

            ps.setLong(
                    2,
                    idStock
            );

            return ps.executeUpdate() > 0;
        }
    }

// =========================================================
// DESCONTAR STOCK
// TRANSACCIONAL
// =========================================================
    /**
     * Descuenta una cantidad del stock.
     *
     * La condición cantidad >= ? agrega una segunda protección para impedir que
     * el stock quede negativo.
     *
     * Debe ejecutarse dentro de una transacción.
     */
    public boolean descontarStock(
            Connection cn,
            long idStock,
            BigDecimal cantidad) throws SQLException {

        String sql
                = "UPDATE stock_producto "
                + "SET cantidad = cantidad - ? "
                + "WHERE id_stock = ? "
                + "AND cantidad >= ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    cantidad
            );

            ps.setLong(
                    2,
                    idStock
            );

            ps.setBigDecimal(
                    3,
                    cantidad
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // MAPEAR STOCK
    // =========================================================
    private StockProducto mapearStock(
            ResultSet rs)
            throws SQLException {

        int idProducto
                = rs.getInt(
                        "id_producto"
                );

        int idDeposito
                = rs.getInt(
                        "id_deposito"
                );

        Producto producto
                = productoDao.buscarPorId(
                        idProducto
                );

        Deposito deposito
                = depositoDao.buscarPorId(
                        idDeposito
                );

        Timestamp timestamp
                = rs.getTimestamp(
                        "fecha_actualizacion"
                );

        return new StockProducto(
                rs.getLong(
                        "id_stock"
                ),
                producto,
                deposito,
                rs.getBigDecimal(
                        "cantidad"
                ),
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null
        );
    }

    // =========================================================
// SUMAR STOCK - TRANSACCIONAL
//
// Utilizado por Compra al confirmar el ingreso
// de mercadería.
//
// IMPORTANTE:
// No abre una conexión nueva.
// Utiliza la Connection recibida para formar parte
// de la misma transacción de Compra.
// =========================================================
    public boolean sumarStock(
            Connection cn,
            long idStock,
            BigDecimal cantidad)
            throws SQLException {

        String sql
                = "UPDATE stock_producto "
                + "SET cantidad = cantidad + ? "
                + "WHERE id_stock = ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    cantidad
            );

            ps.setLong(
                    2,
                    idStock
            );

            return ps.executeUpdate() > 0;
        }
    }

// =========================================================
// SUMAR O CREAR STOCK - TRANSACCIONAL
//
// Utilizado al confirmar una Compra.
//
// Si ya existe stock_producto:
//      suma la cantidad.
//
// Si todavía no existe:
//      crea la fila con esa cantidad.
//
// Todo utiliza la misma Connection de la transacción.
// =========================================================
    public boolean sumarOCrearStock(
            Connection cn,
            int idProducto,
            int idDeposito,
            BigDecimal cantidad)
            throws SQLException {

        String sql
                = "INSERT INTO stock_producto "
                + "(id_producto, id_deposito, cantidad) "
                + "VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "cantidad = cantidad + ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            ps.setBigDecimal(
                    3,
                    cantidad
            );

            ps.setBigDecimal(
                    4,
                    cantidad
            );

            return ps.executeUpdate() > 0;
        }
    }
}
