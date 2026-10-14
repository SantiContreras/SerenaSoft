package Dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Registro inmutable de variaciones de inventario. Debe llamarse con la MISMA
 * conexión y transacción que actualiza stock_producto.
 */
public class MovimientoStockDao {

    public void registrar(Connection cn, int idProducto, int idDeposito,
            int idUsuario, String tipo, long idDetalleOrigen,
            BigDecimal cantidad, BigDecimal anterior, BigDecimal posterior,
            String observaciones) throws SQLException {
        if (cn == null || cn.getAutoCommit()) {
            throw new SQLException("El movimiento requiere una transacción activa.");
        }
        if (idProducto <= 0 || idDeposito <= 0 || idUsuario <= 0 || idDetalleOrigen <= 0) {
            throw new SQLException("Identificadores de movimiento inválidos.");
        }
        if (!"COMPRA".equals(tipo) && !"SALIDA".equals(tipo) && !"AJUSTE".equals(tipo)) {
            throw new SQLException("Tipo de movimiento inválido.");
        }
        if (cantidad == null || anterior == null || posterior == null
                || posterior.compareTo(anterior.add(cantidad)) != 0) {
            throw new SQLException("Los saldos del movimiento no coinciden.");
        }

        String sql = "INSERT INTO movimiento_stock "
                + "(id_producto,id_deposito,id_usuario,tipo_movimiento,id_detalle_origen,"
                + "cantidad,stock_anterior,stock_posterior,observaciones) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idDeposito);
            ps.setInt(3, idUsuario);
            ps.setString(4, tipo);
            ps.setLong(5, idDetalleOrigen);
            ps.setBigDecimal(6, cantidad);
            ps.setBigDecimal(7, anterior);
            ps.setBigDecimal(8, posterior);
            ps.setString(9, observaciones);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No se registró el movimiento de stock.");
            }
        }
    }
}
