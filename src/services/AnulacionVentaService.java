package services;

import Configuracion.conexion;
import sesion.SesionUsuario;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Anulación total y transaccional de ventas no devueltas. */
public final class AnulacionVentaService {
    private AnulacionVentaService() {}

    public static final class Resultado {
        public final long numeroVenta;
        public final BigDecimal total;
        public final int productosReintegrados;
        public final boolean reintegroPendiente;
        Resultado(long numero, BigDecimal total, int productos, boolean pendiente) {
            this.numeroVenta = numero; this.total = total;
            this.productosReintegrados = productos; this.reintegroPendiente = pendiente;
        }
    }

    private static final class Item {
        long id; int producto; BigDecimal cantidad; boolean controla;
        Item(long id, int producto, BigDecimal cantidad, boolean controla) {
            this.id=id; this.producto=producto; this.cantidad=cantidad; this.controla=controla;
        }
    }

    private static long insertar(Connection c, String sql, Object... valores) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i=0;i<valores.length;i++) ps.setObject(i+1,valores[i]);
            ps.executeUpdate();
            try (ResultSet rs=ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("No se obtuvo el identificador del movimiento.");
                return rs.getLong(1);
            }
        }
    }
    private static void ejecutar(Connection c, String sql, Object... valores) throws SQLException {
        try (PreparedStatement ps=c.prepareStatement(sql)) {
            for(int i=0;i<valores.length;i++) ps.setObject(i+1,valores[i]);
            ps.executeUpdate();
        }
    }
    private static void exigir(boolean ok, String mensaje) throws SQLException {
        if (!ok) throw new SQLException(mensaje);
    }

    /** Método anterior deshabilitado: nunca reponer stock sin evaluación física por producto. */
    @Deprecated
    public static Resultado anular(long idVenta, String motivo) throws SQLException {
        throw new SQLException("Usá DevolucionVentaService.anular con evaluación de destino por producto.");
    }
    /** Registrar solo después de haber efectuado y verificado el reintegro fuera del sistema. */
    public static void confirmarReintegro(long idVenta, String referencia) throws SQLException {
        int usuario=SesionUsuario.getIdUsuario();
        exigir(usuario>0 && SesionUsuario.tienePermiso("VENTA_ANULAR"),"Sin permiso de anulación.");
        referencia=referencia==null?"":referencia.trim();
        exigir(referencia.length()>=5 && referencia.length()<=150,
            "Ingresá una referencia verificable del reintegro (5 a 150 caracteres).");
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                try(PreparedStatement ps=c.prepareStatement(
                    "SELECT 1 FROM usuario u JOIN rol_permiso rp ON rp.id_rol=u.id_rol " +
                    "JOIN permiso p ON p.id_permiso=rp.id_permiso " +
                    "WHERE u.id_usuario=? AND u.estado='ACTIVO' AND p.codigo='VENTA_ANULAR' AND p.activo=1")) {
                    ps.setInt(1,usuario);
                    try(ResultSet rs=ps.executeQuery()) { exigir(rs.next(),"Sin autorización vigente."); }
                }
                int actualizados;
                try(PreparedStatement ps=c.prepareStatement(
                    "UPDATE venta_anulacion_reintegro r JOIN venta v ON v.id_venta=r.id_venta " +
                    "SET r.estado='CONFIRMADO',r.fecha_confirmacion=NOW(),r.id_usuario_confirmacion=?,r.referencia=? " +
                    "WHERE r.id_venta=? AND r.estado='PENDIENTE' AND v.estado='ANULADA'")) {
                    ps.setInt(1,usuario);ps.setString(2,referencia);ps.setLong(3,idVenta);
                    actualizados=ps.executeUpdate();
                }
                exigir(actualizados==1,"No existe un reintegro pendiente para esta venta.");
                c.commit();
            } catch(SQLException | RuntimeException ex) {
                try {c.rollback();} catch(SQLException rb) {ex.addSuppressed(rb);}
                throw ex;
            }
        }
    }

}
