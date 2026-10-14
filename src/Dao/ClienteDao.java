package Dao;
import Configuracion.conexion;
import model.Cliente;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
public class ClienteDao {
 private static final String COLS="tipo_documento,nro_documento,razon_social,nombre,apellido,condicion_iva,tipo_cliente,telefono,email,direccion,localidad,provincia,permite_cuenta_corriente,limite_credito,activo";
 private Cliente map(ResultSet r)throws SQLException {
  Cliente c=new Cliente(); c.setIdCliente(r.getInt("id_cliente"));
  c.setTipoDocumento(r.getString("tipo_documento"));c.setNroDocumento(r.getString("nro_documento"));c.setRazonSocial(r.getString("razon_social"));c.setNombre(r.getString("nombre"));c.setApellido(r.getString("apellido"));c.setCondicionIva(r.getString("condicion_iva"));c.setTipoCliente(r.getString("tipo_cliente"));c.setTelefono(r.getString("telefono"));c.setEmail(r.getString("email"));c.setDireccion(r.getString("direccion"));c.setLocalidad(r.getString("localidad"));c.setProvincia(r.getString("provincia"));c.setPermiteCuentaCorriente(r.getBoolean("permite_cuenta_corriente"));c.setLimiteCredito(r.getBigDecimal("limite_credito"));c.setActivo(r.getBoolean("activo"));
  Timestamp a=r.getTimestamp("fecha_registro"),b=r.getTimestamp("fecha_modificacion");if(a!=null)c.setFechaRegistro(a.toLocalDateTime());if(b!=null)c.setFechaModificacion(b.toLocalDateTime());return c;
 }
 private void bind(PreparedStatement p,Cliente c)throws SQLException {
  String[] v={c.getTipoDocumento(),c.getNroDocumento(),c.getRazonSocial(),c.getNombre(),c.getApellido(),c.getCondicionIva(),c.getTipoCliente(),c.getTelefono(),c.getEmail(),c.getDireccion(),c.getLocalidad(),c.getProvincia()};
  for(int i=0;i<v.length;i++){if(v[i]==null||v[i].trim().isEmpty())p.setNull(i+1,Types.VARCHAR);else p.setString(i+1,v[i].trim());}
  p.setBoolean(13,c.isPermiteCuentaCorriente());p.setBigDecimal(14,c.getLimiteCredito());p.setBoolean(15,c.isActivo());
 }
 public int guardar(Cliente c)throws SQLException {
  String sql="INSERT INTO cliente ("+COLS+") VALUES ("+String.join(",",Collections.nCopies(15,"?"))+")";
  try(Connection cn=conexion.getConexion();PreparedStatement p=cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){bind(p,c);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(r.next()){c.setIdCliente(r.getInt(1));return c.getIdCliente();}}throw new SQLException("No se generó ID de cliente");}
 }
 public void actualizar(Cliente c)throws SQLException {
  String[] columns=COLS.split(",");StringJoiner j=new StringJoiner(",");for(String s:columns)j.add(s+"=?");
  try(Connection cn=conexion.getConexion();PreparedStatement p=cn.prepareStatement("UPDATE cliente SET "+j+" WHERE id_cliente=?")){bind(p,c);p.setInt(16,c.getIdCliente());if(p.executeUpdate()!=1)throw new SQLException("Cliente inexistente");}
 }
 public void cambiarActivo(int id,boolean activo)throws SQLException {
  try(Connection cn=conexion.getConexion();PreparedStatement p=cn.prepareStatement("UPDATE cliente SET activo=? WHERE id_cliente=?")){p.setBoolean(1,activo);p.setInt(2,id);if(p.executeUpdate()!=1)throw new SQLException("Cliente inexistente");}
 }
 public Cliente buscarPorId(int id)throws SQLException {
  try(Connection cn=conexion.getConexion();PreparedStatement p=cn.prepareStatement("SELECT * FROM cliente WHERE id_cliente=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?map(r):null;}}
 }
 public List<Cliente> listar(String filtro,boolean incluirInactivos)throws SQLException {
  List<Cliente> out=new ArrayList<>();String sql="SELECT * FROM cliente WHERE (?=1 OR activo=1) AND (CONCAT_WS(' ',nombre,apellido,razon_social,nro_documento,telefono,email) LIKE ?) ORDER BY id_cliente DESC LIMIT 2000";
  try(Connection cn=conexion.getConexion();PreparedStatement p=cn.prepareStatement(sql)){p.setInt(1,incluirInactivos?1:0);p.setString(2,"%"+(filtro==null?"":filtro.trim())+"%");try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}}return out;
 }
}
