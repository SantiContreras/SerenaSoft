package Dao;
import java.sql.*;
import model.VentaDetalle;
public class VentaDetalleDao {
 public long insertar(Connection c,VentaDetalle d)throws SQLException {
  String sql="INSERT INTO venta_detalle(id_venta,id_producto,descripcion_producto,cantidad,precio_unitario,costo_unitario,descuento_porcentaje,descuento_importe,iva_porcentaje,iva_importe,subtotal) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
  try(PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
   p.setLong(1,d.idVenta);p.setInt(2,d.idProducto);p.setString(3,d.descripcionProducto);
   p.setBigDecimal(4,d.cantidad);p.setBigDecimal(5,d.precioUnitario);p.setBigDecimal(6,d.costoUnitario);
   p.setBigDecimal(7,d.descuentoPorcentaje);p.setBigDecimal(8,d.descuentoImporte);
   p.setBigDecimal(9,d.ivaPorcentaje);p.setBigDecimal(10,d.ivaImporte);p.setBigDecimal(11,d.subtotal);
   if(p.executeUpdate()!=1)throw new SQLException("No se inserto detalle");
   try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Sin ID detalle");return r.getLong(1);}
  }
 }
}
