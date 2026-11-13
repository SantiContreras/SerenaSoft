package Dao;
import Configuracion.conexion;
import java.sql.*;

/** Configuración global; si la tabla no está instalada, no permite guardar. */
public class SeguridadConfigDao {
 public static class Datos {
  public boolean bloquear=true, mayus=false, minus=false, numero=false, especial=false;
  public int intentos=5, minutos=15, longitud=8;
 }
 public Datos leer() throws SQLException {
  try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement("SELECT * FROM configuracion_seguridad WHERE id=1");ResultSet r=p.executeQuery()) {
   if(!r.next()) throw new SQLException("No hay configuración de seguridad. Ejecute sql/01_configuracion_seguridad.sql");
   Datos d=new Datos();d.bloquear=r.getBoolean("bloquear_intentos");d.intentos=r.getInt("max_intentos");d.minutos=r.getInt("minutos_bloqueo");d.longitud=r.getInt("longitud_minima");d.mayus=r.getBoolean("exigir_mayuscula");d.minus=r.getBoolean("exigir_minuscula");d.numero=r.getBoolean("exigir_numero");d.especial=r.getBoolean("exigir_especial");return d;
  }
 }
 public void guardar(Datos d) throws SQLException {
  String q="UPDATE configuracion_seguridad SET bloquear_intentos=?,max_intentos=?,minutos_bloqueo=?,longitud_minima=?,exigir_mayuscula=?,exigir_minuscula=?,exigir_numero=?,exigir_especial=? WHERE id=1";
  try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(q)) {
   p.setBoolean(1,d.bloquear);p.setInt(2,d.intentos);p.setInt(3,d.minutos);p.setInt(4,d.longitud);p.setBoolean(5,d.mayus);p.setBoolean(6,d.minus);p.setBoolean(7,d.numero);p.setBoolean(8,d.especial);
   if(p.executeUpdate()!=1)throw new SQLException("No se encontró la fila de configuración");
  }
 }
 public static Datos leerOValoresSeguros(){try{return new SeguridadConfigDao().leer();}catch(SQLException e){System.err.println("Seguridad: usando valores seguros predeterminados: "+e.getMessage());return new Datos();}}
 public static String validarClave(String clave,Datos d){
  if(clave==null||clave.length()<d.longitud)return "La contraseña debe tener al menos "+d.longitud+" caracteres.";
  if(d.mayus&&!clave.matches("(?s).*[A-ZÁÉÍÓÚÑ].*"))return "Debe incluir una letra mayúscula.";
  if(d.minus&&!clave.matches("(?s).*[a-záéíóúñ].*"))return "Debe incluir una letra minúscula.";
  if(d.numero&&!clave.matches("(?s).*\\d.*"))return "Debe incluir un número.";
  if(d.especial&&!clave.matches("(?s).*[\\p{Punct}].*"))return "Debe incluir un carácter especial.";
  return null;
 }
}
