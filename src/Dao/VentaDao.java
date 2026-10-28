package Dao;
import java.sql.*;
import model.Venta;
public class VentaDao {
 public long reservarNumero(Connection c) throws SQLException {
  long numero;
  try(PreparedStatement p=c.prepareStatement("SELECT siguiente FROM venta_numerador WHERE id=1 FOR UPDATE"); ResultSet r=p.executeQuery()){
   if(!r.next()) throw new SQLException("Falta venta_numerador. Ejecutar sql/01_numerador.sql");
   numero=r.getLong(1);
  }
  try(PreparedStatement p=c.prepareStatement("UPDATE venta_numerador SET siguiente=siguiente+1 WHERE id=1")){
   if(p.executeUpdate()!=1) throw new SQLException("No se actualizo numerador");
  }
  return numero;
 }
 public long insertar(Connection c,Venta v)throws SQLException {
  String sql="INSERT INTO venta(id_caja_sesion,id_usuario,id_cliente,id_deposito,numero_venta,subtotal,descuento_total,total,estado,observaciones,fecha_finalizacion) VALUES(?,?,?,?,?,?,?,?,?,?,NOW())";
  try(PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
   if(v.idCajaSesion==null)p.setNull(1,Types.BIGINT);else p.setInt(1,v.idCajaSesion);
   p.setInt(2,v.idUsuario);
   if(v.idCliente==null)p.setNull(3,Types.INTEGER);else p.setInt(3,v.idCliente);
   p.setInt(4,v.idDeposito);p.setLong(5,v.numeroVenta);p.setBigDecimal(6,v.subtotal);
   p.setBigDecimal(7,v.descuentoTotal);p.setBigDecimal(8,v.total);p.setString(9,v.estado);p.setString(10,v.observaciones);
   if(p.executeUpdate()!=1)throw new SQLException("No se inserto venta");
   try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Sin ID de venta");return r.getLong(1);}
  }
 }
 public void registrarFinalizacion(Connection c,long idVenta,int usuario)throws SQLException {
  try(PreparedStatement p=c.prepareStatement("INSERT INTO venta_estado_historial(id_venta,id_usuario,estado_anterior,estado_nuevo,motivo) VALUES(?,?,'PENDIENTE','FINALIZADA','Cobro confirmado')")){
   p.setLong(1,idVenta);p.setInt(2,usuario);if(p.executeUpdate()!=1)throw new SQLException("No se registro estado");
  }
 }
}
