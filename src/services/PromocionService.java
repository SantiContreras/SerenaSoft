package services;
import java.math.*;
import java.sql.*;

/** Motor 2x1: unidades enteras, una gratis por cada par. */
public final class PromocionService {
 private PromocionService(){}
 public static boolean tieneDosPorUno(Connection cn,int idProducto)throws SQLException{
  String sql="SELECT 1 FROM promocion_2x1 WHERE id_producto=? AND activo=1 AND CURRENT_DATE BETWEEN fecha_inicio AND fecha_fin LIMIT 1";
  try(PreparedStatement p=cn.prepareStatement(sql)){p.setInt(1,idProducto);try(ResultSet r=p.executeQuery()){return r.next();}}
 }
 /** El descuento se calcula por pares completos. No aplicar a productos pesables. */
 public static BigDecimal descuentoDosPorUno(BigDecimal precio,BigDecimal cantidad){
  if(precio==null||cantidad==null||precio.signum()<0||cantidad.signum()<0)throw new IllegalArgumentException("Precio o cantidad inválidos");
  if(cantidad.stripTrailingZeros().scale()>0)throw new IllegalArgumentException("2x1 sólo admite unidades enteras");
  return precio.multiply(new BigDecimal(cantidad.toBigInteger().divide(java.math.BigInteger.valueOf(2)))).setScale(2,RoundingMode.HALF_UP);
 }
}
