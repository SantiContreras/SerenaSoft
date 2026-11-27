package services;
import Configuracion.conexion;
import model.*;
import java.sql.*;
import java.math.*;
import java.util.*;
public class AltaMasivaPdfService {
 public record Alta(FacturaPdf.Linea linea,String nombre,String unidadStock,BigDecimal factor,BigDecimal precio,int iva){}
 public void crear(FacturaPdf f,Proveedor p,List<Alta> altas)throws SQLException{
 if(!MemoriaFacturaPdfService.mismoProveedor(f,p)||!f.alertas.isEmpty()||altas.isEmpty())throw new IllegalArgumentException("Proveedor o conciliación pendiente");
 try(Connection c=conexion.getConexion()){c.setAutoCommit(false);try{
 for(Alta a:altas){var l=a.linea();if(!Set.of("UN","KG").contains(a.unidadStock())||l.producto!=null||l.codigo==null||l.codigo.isBlank()||a.nombre().isBlank()||a.factor().signum()<=0||a.factor().scale()>4||a.precio().signum()<=0||l.cantidad.signum()<=0||l.importe.signum()<=0)throw new IllegalArgumentException("Datos inválidos: "+a.nombre());
 try(PreparedStatement q=c.prepareStatement("SELECT id_iva FROM tipo_iva WHERE id_iva=? AND activo=1")){q.setInt(1,a.iva());try(ResultSet r=q.executeQuery()){if(!r.next())throw new IllegalArgumentException("IVA inválido: "+a.nombre());}}
 int compra=unidad(c,l.unidad),venta=unidad(c,a.unidadStock());BigDecimal stock=l.cantidad.multiply(a.factor());if(stock.stripTrailingZeros().scale()>3||(!a.unidadStock().equals("KG")&&stock.stripTrailingZeros().scale()>0))throw new IllegalArgumentException("Cantidad incompatible: "+a.nombre());
 try(PreparedStatement q=c.prepareStatement("SELECT id_producto FROM producto WHERE codigo=? OR codigo_barra=? OR nombre=?")){q.setString(1,l.codigo);q.setString(2,l.codigo);q.setString(3,a.nombre());try(ResultSet r=q.executeQuery()){if(r.next())throw new IllegalArgumentException("Posible duplicado: "+a.nombre()+". Vincule manualmente.");}}
 BigDecimal costo=l.importe.divide(stock,2,RoundingMode.HALF_UP);int id;
 try(PreparedStatement q=c.prepareStatement("INSERT INTO producto(codigo,nombre,id_unidad_compra,id_unidad_venta,factor_conversion,precio_compra,precio_venta,id_iva,es_pesable,activo,observaciones,margen_porcentaje) VALUES(?,?,?,?,?,?,?,?,?,0,?,?)",Statement.RETURN_GENERATED_KEYS)){q.setString(1,l.codigo);q.setString(2,a.nombre());q.setInt(3,compra);q.setInt(4,venta);q.setBigDecimal(5,a.factor());q.setBigDecimal(6,costo);q.setBigDecimal(7,a.precio());q.setInt(8,a.iva());q.setBoolean(9,a.unidadStock().equals("KG"));q.setString(10,"Alta PDF pendiente de revisión y activación; sin ingreso de stock");q.setBigDecimal(11,a.precio().subtract(costo).multiply(BigDecimal.valueOf(100)).divide(costo,2,RoundingMode.HALF_UP));q.executeUpdate();try(ResultSet r=q.getGeneratedKeys()){if(!r.next())throw new SQLException("Sin ID");id=r.getInt(1);}}
 try(PreparedStatement q=c.prepareStatement("INSERT INTO producto_presentacion(id_producto,nombre,id_unidad_compra,factor_conversion,es_predeterminada,activo) VALUES(?,?,?,?,1,1)")){q.setInt(1,id);q.setString(2,l.unidad);q.setInt(3,compra);q.setBigDecimal(4,a.factor());q.executeUpdate();}
 }c.commit();}catch(SQLException|RuntimeException e){c.rollback();throw e;}}
 }
 private int unidad(Connection c,String codigo)throws SQLException{try(PreparedStatement q=c.prepareStatement("SELECT id_unidad FROM unidad_medida WHERE codigo=? AND activo=1")){q.setString(1,codigo);try(ResultSet r=q.executeQuery()){if(r.next())return r.getInt(1);}}throw new IllegalArgumentException("Unidad no configurada: "+codigo);}
}
