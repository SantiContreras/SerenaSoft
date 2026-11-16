package Dao;

import Configuracion.conexion;
import java.sql.*;

/** Preferencias de ticket interno independientes del esquema fiscal preexistente. */
public final class ConfiguracionComprobanteDao {
    public static final class Opciones {
        public boolean nombre=true, cuit=true, direccion=true, telefono=true, vendedor=true, fecha=true;
        public boolean automatico=false, vistaPrevia=false, reimpresion=true;
        public String pie="Gracias por su compra.";
    }
    public Opciones cargar() throws SQLException {
        String sql="SELECT mostrar_nombre,mostrar_cuit,mostrar_direccion,mostrar_telefono,mostrar_vendedor,mostrar_fecha,imprimir_automatico,vista_previa,permitir_reimpresion,pie FROM serenasoft_preferencias_ticket WHERE clave=1";
        try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement(sql); ResultSet r=p.executeQuery()) {
            if(!r.next()) throw new SQLException("Falta la fila de preferencias. Ejecutá el SQL incluido.");
            Opciones o=new Opciones();
            o.nombre=r.getBoolean(1); o.cuit=r.getBoolean(2); o.direccion=r.getBoolean(3);
            o.telefono=r.getBoolean(4); o.vendedor=r.getBoolean(5); o.fecha=r.getBoolean(6);
            o.automatico=r.getBoolean(7); o.vistaPrevia=r.getBoolean(8); o.reimpresion=r.getBoolean(9);
            o.pie=r.getString(10); return o;
        }
    }
    public void guardar(Opciones o) throws SQLException {
        if(o==null || o.pie==null || o.pie.length()>250) throw new IllegalArgumentException("El mensaje final no puede superar 250 caracteres.");
        if(o.automatico && o.vistaPrevia) throw new IllegalArgumentException("No se puede activar impresión automática y vista previa a la vez.");
        String sql="UPDATE serenasoft_preferencias_ticket SET mostrar_nombre=?,mostrar_cuit=?,mostrar_direccion=?,mostrar_telefono=?,mostrar_vendedor=?,mostrar_fecha=?,imprimir_automatico=?,vista_previa=?,permitir_reimpresion=?,pie=? WHERE clave=1";
        try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setBoolean(1,o.nombre);p.setBoolean(2,o.cuit);p.setBoolean(3,o.direccion);
            p.setBoolean(4,o.telefono);p.setBoolean(5,o.vendedor);p.setBoolean(6,o.fecha);
            p.setBoolean(7,o.automatico);p.setBoolean(8,o.vistaPrevia);p.setBoolean(9,o.reimpresion);p.setString(10,o.pie);
            if(p.executeUpdate()==0) {
                try(PreparedStatement check=c.prepareStatement("SELECT clave FROM serenasoft_preferencias_ticket WHERE clave=1");ResultSet rs=check.executeQuery()) {
                    if(!rs.next()) throw new SQLException("Falta el registro de preferencias. Ejecutá el SQL incluido.");
                }
            }
        }
    }
}
