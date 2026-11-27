package services;
import Configuracion.conexion;
import Dao.*;
import model.*;
import java.sql.*;
import java.math.*;
import java.util.*;
public class MemoriaFacturaPdfService {
 public static String cuit(String s){return s==null?"":s.replaceAll("[^0-9]", "");}
 public static boolean mismoProveedor(FacturaPdf f,Proveedor p){return f!=null&&p!=null&&cuit(f.cuit).length()==11&&cuit(f.cuit).equals(cuit(p.getCuit()));}
 private String unidad(Producto p){return p.getUnidadVenta()==null?"":p.getUnidadVenta().getCodigo();}
 private boolean valida(Producto p,FacturaPdf.Linea l,BigDecimal factor,Connection cn)throws SQLException{
 return p!=null&&p.isActivo()&&p.isControlaStock()&&factor!=null&&factor.signum()>0&&p.getUnidadVenta()!=null&&(!"KG".equalsIgnoreCase(l.unidad)||"KG".equalsIgnoreCase(unidad(p)))&&(p.getUnidadVenta().isPermiteDecimales()||l.cantidad.multiply(factor).stripTrailingZeros().scale()<=0)&&new ProductoPresentacionDao().existeActiva(p.getIdProducto(),l.unidad,factor,cn);
 }
 public void recuperar(FacturaPdf f,Proveedor p)throws SQLException{
 if(!mismoProveedor(f,p))return;
 try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("SELECT * FROM factura_pdf_equivalencia WHERE id_proveedor=? AND codigo_pdf=? AND unidad_pdf=?")){
 for(FacturaPdf.Linea l:f.lineas){if(l.codigo==null||l.codigo.trim().isEmpty()||l.unidad==null)continue;ps.setInt(1,p.getIdProveedor());ps.setString(2,l.codigo);ps.setString(3,l.unidad);try(ResultSet r=ps.executeQuery()){if(!r.next())continue;Producto prod=new ProductoDao().buscarPorId(r.getInt("id_producto"));BigDecimal factor=r.getBigDecimal("factor");if(!Objects.equals(l.descripcion,r.getString("descripcion_pdf"))||!valida(prod,l,factor,cn)||!unidad(prod).equals(r.getString("unidad_stock")))continue;l.producto=prod;/* No resolver automáticamente factores ambiguos. */if(l.factor!=null&&l.factor.compareTo(factor)==0&&!l.revisarFactor){l.factor=factor;}}}
 }}
 public void guardar(FacturaPdf f,Proveedor p,FacturaPdf.Linea l,Producto prod,BigDecimal factor)throws SQLException{
 if(!mismoProveedor(f,p))throw new IllegalArgumentException("El CUIT del proveedor debe coincidir con el PDF");
 if(l.codigo==null||l.codigo.trim().isEmpty()||l.descripcion==null||l.unidad==null)throw new IllegalArgumentException("Artículo sin identificación suficiente");
 try(Connection cn=conexion.getConexion()){
 if(!valida(prod,l,factor,cn))throw new IllegalArgumentException("Configure primero una presentación activa compatible con la unidad de stock");
 try(PreparedStatement ps=cn.prepareStatement("INSERT INTO factura_pdf_equivalencia(id_proveedor,codigo_pdf,unidad_pdf,descripcion_pdf,id_producto,factor,unidad_stock) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE descripcion_pdf=VALUES(descripcion_pdf),id_producto=VALUES(id_producto),factor=VALUES(factor),unidad_stock=VALUES(unidad_stock)")){
 ps.setInt(1,p.getIdProveedor());ps.setString(2,l.codigo);ps.setString(3,l.unidad);ps.setString(4,l.descripcion);ps.setInt(5,prod.getIdProducto());ps.setBigDecimal(6,factor);ps.setString(7,unidad(prod));ps.executeUpdate();}}
 }
 public void validar(FacturaPdf.Linea l,Producto p,BigDecimal factor)throws SQLException{try(Connection cn=conexion.getConexion()){if(!valida(p,l,factor,cn))throw new IllegalArgumentException("Presentación incompatible o no configurada en el producto");}}
}
