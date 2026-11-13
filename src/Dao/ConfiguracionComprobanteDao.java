package Dao;
import Configuracion.conexion;
import java.sql.*;

/** Configuración única de tickets internos. Nunca modifica el numerador de ventas. */
public final class ConfiguracionComprobanteDao {
    private ConfiguracionComprobanteDao() {}
    public static final class Datos {
        public String tipo="TICKET_INTERNO", pie="Gracias por su compra.";
        public int puntoVenta=1;
        public boolean nombre=true,cuit=true,direccion=true,telefono=true,vendedor=true,fecha=true;
        public boolean auto=false,previa=true,reimpresion=true;
    }
    public static Datos cargar() throws SQLException {
        Datos d=new Datos();
        try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement("SELECT * FROM configuracion_comprobante WHERE id_config=1");ResultSet r=p.executeQuery()) {
            if(r.next()) { d.tipo=r.getString("tipo");d.puntoVenta=r.getInt("punto_venta");
                d.nombre=r.getBoolean("mostrar_nombre");d.cuit=r.getBoolean("mostrar_cuit");d.direccion=r.getBoolean("mostrar_direccion");d.telefono=r.getBoolean("mostrar_telefono");d.vendedor=r.getBoolean("mostrar_vendedor");d.fecha=r.getBoolean("mostrar_fecha");
                d.pie=r.getString("pie");d.auto=r.getBoolean("impresion_automatica");d.previa=r.getBoolean("vista_previa");d.reimpresion=r.getBoolean("permitir_reimpresion"); }
        }
        return d;
    }
    public static void guardar(Datos d) throws SQLException {
        if(d.puntoVenta<1 || d.puntoVenta>9999) throw new IllegalArgumentException("El punto de venta debe estar entre 1 y 9999.");
        if(d.pie==null || d.pie.length()>250) throw new IllegalArgumentException("El pie admite hasta 250 caracteres.");
        String q="INSERT INTO configuracion_comprobante (id_config,tipo,punto_venta,mostrar_nombre,mostrar_cuit,mostrar_direccion,mostrar_telefono,mostrar_vendedor,mostrar_fecha,pie,impresion_automatica,vista_previa,permitir_reimpresion) VALUES (1,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE tipo=VALUES(tipo),punto_venta=VALUES(punto_venta),mostrar_nombre=VALUES(mostrar_nombre),mostrar_cuit=VALUES(mostrar_cuit),mostrar_direccion=VALUES(mostrar_direccion),mostrar_telefono=VALUES(mostrar_telefono),mostrar_vendedor=VALUES(mostrar_vendedor),mostrar_fecha=VALUES(mostrar_fecha),pie=VALUES(pie),impresion_automatica=VALUES(impresion_automatica),vista_previa=VALUES(vista_previa),permitir_reimpresion=VALUES(permitir_reimpresion)";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,d.tipo);p.setInt(2,d.puntoVenta);p.setBoolean(3,d.nombre);p.setBoolean(4,d.cuit);p.setBoolean(5,d.direccion);p.setBoolean(6,d.telefono);p.setBoolean(7,d.vendedor);p.setBoolean(8,d.fecha);p.setString(9,d.pie);p.setBoolean(10,d.auto);p.setBoolean(11,d.previa);p.setBoolean(12,d.reimpresion);p.executeUpdate();
        }
    }
    public static long siguienteNumeroVenta() throws SQLException {
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement("SELECT siguiente FROM venta_numerador WHERE id=1");ResultSet r=p.executeQuery()) { return r.next()?r.getLong(1):1; }
    }
}
