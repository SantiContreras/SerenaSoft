package Dao;
import java.sql.*;
import model.PagoVenta;
public class VentaPagoDao {
 public void insertar(Connection c,PagoVenta v)throws SQLException{
  try(PreparedStatement p=c.prepareStatement("INSERT INTO venta_pago(id_venta,id_medio_pago,monto,importe_recibido,cambio,referencia) VALUES(?,?,?,?,?,?)")){
   p.setLong(1,v.idVenta);p.setInt(2,v.idMedioPago);p.setBigDecimal(3,v.monto);
   p.setBigDecimal(4,v.importeRecibido);p.setBigDecimal(5,v.cambio);p.setString(6,v.referencia);
   if(p.executeUpdate()!=1)throw new SQLException("No se inserto pago");
  }
 }
}
