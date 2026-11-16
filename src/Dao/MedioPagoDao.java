package Dao;

import Configuracion.conexion;
import java.sql.*;
import java.util.*;

public final class MedioPagoDao {
    public static final class Medio {
        public int id, orden;
        public String nombre, tipo;
        public boolean requiereReferencia, afectaEfectivo, activo;
        public Medio(int id,String nombre,String tipo,boolean ref,boolean efectivo,boolean activo,int orden){
            this.id=id;this.nombre=nombre;this.tipo=tipo;this.requiereReferencia=ref;
            this.afectaEfectivo=efectivo;this.activo=activo;this.orden=orden;
        }
    }
    public List<Medio> listar() throws SQLException {
        try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement("SELECT id_medio_pago,nombre,tipo,requiere_referencia,afecta_efectivo_fisico,activo,orden_visual FROM medio_pago ORDER BY orden_visual,nombre");ResultSet r=p.executeQuery()){
            List<Medio> l=new ArrayList<>();while(r.next())l.add(leer(r));return l;
        }
    }
    private static Medio leer(ResultSet r)throws SQLException{return new Medio(r.getInt(1),r.getString(2),r.getString(3),r.getBoolean(4),r.getBoolean(5),r.getBoolean(6),r.getInt(7));}
    public void guardar(Medio m)throws SQLException{
        validar(m);
        try(Connection c=conexion.getConexion()){
            if(m.id==0){
                try(PreparedStatement p=c.prepareStatement("INSERT INTO medio_pago(nombre,tipo,requiere_referencia,afecta_efectivo_fisico,activo,orden_visual) VALUES(?,?,?,?,?,?)")){
                    parametros(p,m);p.executeUpdate();
                }
            }else{
                try(PreparedStatement p=c.prepareStatement("UPDATE medio_pago SET nombre=?,tipo=?,requiere_referencia=?,afecta_efectivo_fisico=?,activo=?,orden_visual=? WHERE id_medio_pago=?")){
                    parametros(p,m);p.setInt(7,m.id);if(p.executeUpdate()!=1)throw new SQLException("No existe el medio de pago.");
                }
            }
        }
    }
    private static void parametros(PreparedStatement p,Medio m)throws SQLException{
        p.setString(1,m.nombre.trim());p.setString(2,m.tipo);p.setBoolean(3,m.requiereReferencia);
        p.setBoolean(4,m.afectaEfectivo);p.setBoolean(5,m.activo);p.setInt(6,m.orden);
    }
    private static void validar(Medio m){
        if(m.nombre==null||m.nombre.trim().isEmpty()||m.nombre.trim().length()>60)throw new IllegalArgumentException("Nombre requerido (máximo 60 caracteres).");
        if(!Arrays.asList("EFECTIVO","TRANSFERENCIA","TARJETA","QR","OTRO").contains(m.tipo))throw new IllegalArgumentException("Tipo de pago inválido.");
        if(m.afectaEfectivo != "EFECTIVO".equals(m.tipo))throw new IllegalArgumentException("Solo los pagos de tipo EFECTIVO pueden afectar la caja física.");
        if(m.orden<0)throw new IllegalArgumentException("El orden no puede ser negativo.");
    }
    public void cambiarEstado(int id,boolean activo)throws SQLException{
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement("UPDATE medio_pago SET activo=? WHERE id_medio_pago=?")){
            p.setBoolean(1,activo);p.setInt(2,id);if(p.executeUpdate()!=1)throw new SQLException("Medio no encontrado.");
        }
    }
    public static void validarCobro(Connection c,int id,java.math.BigDecimal monto,java.math.BigDecimal recibido,String referencia)throws SQLException{
        try(PreparedStatement p=c.prepareStatement("SELECT tipo,requiere_referencia,afecta_efectivo_fisico,activo FROM medio_pago WHERE id_medio_pago=? FOR UPDATE")){
            p.setInt(1,id);try(ResultSet r=p.executeQuery()){
                if(!r.next()||!r.getBoolean(4))throw new SQLException("Medio de pago inexistente o inactivo: "+id);
                boolean efectivo="EFECTIVO".equals(r.getString(1))&&r.getBoolean(3);
                if(r.getBoolean(2)&&(referencia==null||referencia.trim().isEmpty()))throw new SQLException("Este medio de pago requiere referencia de operación.");
                if(referencia!=null&&referencia.length()>150)throw new SQLException("Referencia demasiado larga (máximo 150).");
                if(!efectivo&&recibido!=null&&recibido.compareTo(monto)!=0)throw new SQLException("Los pagos electrónicos no pueden generar cambio en efectivo.");
            }
        }
    }
}
