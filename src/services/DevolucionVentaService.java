package services;

import Configuracion.conexion;
import sesion.SesionUsuario;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

/** Devoluciones parciales/totales, inventario y reintegro pendientes en una transacción. */
public final class DevolucionVentaService {
    private DevolucionVentaService() {}
    public enum Destino { APTO, REVISION, MERMA, NO_RECIBIDO }
    public static final class Linea {
        public final long idDetalle;
        public final String producto;
        public final BigDecimal vendido, disponible, precio;
        public final boolean controlaStock;
        public BigDecimal cantidad = BigDecimal.ZERO;
        public Destino destino = Destino.REVISION;
        public String observacion = "";
        public Linea(long id,String nombre,BigDecimal vend,BigDecimal disp,BigDecimal precio,boolean controla){
            this.idDetalle=id;this.producto=nombre;this.vendido=vend;this.disponible=disp;
            this.precio=precio;this.controlaStock=controla;
        }
    }
    public static final class Resultado {
        public final long idDevolucion;
        public final BigDecimal monto;
        public final int reingresados;
        Resultado(long id,BigDecimal monto,int reingresados){this.idDevolucion=id;this.monto=monto;this.reingresados=reingresados;}
    }
    private static void exigir(boolean condicion,String mensaje)throws SQLException {if(!condicion)throw new SQLException(mensaje);}
    private static void exec(Connection c,String sql,Object...args)throws SQLException{
        try(PreparedStatement p=c.prepareStatement(sql)){for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);exigir(p.executeUpdate()==1,"No se pudo registrar el movimiento.");}
    }
    private static long insert(Connection c,String sql,Object...args)throws SQLException{
        try(PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);exigir(p.executeUpdate()==1,"No se insertó el registro.");
            try(ResultSet r=p.getGeneratedKeys()){exigir(r.next(),"No se obtuvo el ID.");return r.getLong(1);}
        }
    }
    private static void permiso(Connection c,int usuario,String codigo)throws SQLException{
        exigir(usuario>0 && SesionUsuario.tienePermiso(codigo),"Sin permiso "+codigo+".");
        try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM usuario u JOIN rol_permiso rp ON rp.id_rol=u.id_rol JOIN permiso pe ON pe.id_permiso=rp.id_permiso WHERE u.id_usuario=? AND u.estado='ACTIVO' AND pe.codigo=? AND pe.activo=1")){
            p.setInt(1,usuario);p.setString(2,codigo);try(ResultSet r=p.executeQuery()){exigir(r.next(),"El permiso no está vigente.");}
        }
    }
    private static final class Venta {
        long numero;int deposito;BigDecimal total;String estado;
    }
    private static Venta bloquearVenta(Connection c,long id)throws SQLException{
        Venta v=new Venta();
        try(PreparedStatement p=c.prepareStatement("SELECT numero_venta,id_deposito,total,estado FROM venta WHERE id_venta=? FOR UPDATE")){
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){
                exigir(r.next(),"No existe la venta.");v.numero=r.getLong(1);v.deposito=r.getInt(2);v.total=r.getBigDecimal(3);v.estado=r.getString(4);
            }
        }
        return v;
    }
    private static void comprobarFiscal(Connection c,long id)throws SQLException{
        try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM comprobante WHERE id_venta=? AND estado='EMITIDO' LIMIT 1")){
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){exigir(!r.next(),"Hay un comprobante fiscal emitido. Resolverlo por el circuito fiscal antes de devolver.");}
        }
        try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM cliente_cuenta_movimiento WHERE origen='VENTA' AND id_referencia=? LIMIT 1")){
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){exigir(!r.next(),"Venta vinculada a cuenta corriente. Requiere conciliación previa.");}
        }
    }
    private static List<Linea> lineas(Connection c,long id)throws SQLException{
        List<Linea> out=new ArrayList<>();
        String q="SELECT d.id_detalle,d.descripcion_producto,d.cantidad,d.subtotal,p.controla_stock,COALESCE(SUM(CASE WHEN dv.estado='CONFIRMADA' THEN dd.cantidad ELSE 0 END),0) " +
            "FROM venta_detalle d JOIN producto p ON p.id_producto=d.id_producto " +
            "LEFT JOIN devolucion_venta_detalle dd ON dd.id_venta_detalle=d.id_detalle " +
            "LEFT JOIN devolucion_venta dv ON dv.id_devolucion=dd.id_devolucion " +
            "WHERE d.id_venta=? GROUP BY d.id_detalle,d.descripcion_producto,d.cantidad,d.subtotal,p.controla_stock ORDER BY d.id_detalle";
        try(PreparedStatement p=c.prepareStatement(q)){
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    BigDecimal vendido=r.getBigDecimal(3),ya=r.getBigDecimal(6);
                    out.add(new Linea(r.getLong(1),r.getString(2),vendido,vendido.subtract(ya),r.getBigDecimal(4),r.getBoolean(5)));
                }
            }
        }
        return out;
    }
    public static List<Linea> consultar(long idVenta)throws SQLException{
        try(Connection c=conexion.getConexion()){
            Venta v=bloquearVenta(c,idVenta);
            exigir(v.estado.equals("FINALIZADA")||v.estado.startsWith("DEVUELTA_"),"Venta no admite devoluciones: "+v.estado);
            return lineas(c,idVenta);
        }
    }
    private static void registrarDestino(Connection c,long idVenta,Long idDev,long idDetalle,int producto,int usuario,
                                          BigDecimal cantidad,Destino destino,String obs)throws SQLException{
        insert(c,"INSERT INTO venta_retorno_destino(id_venta,id_devolucion,id_venta_detalle,id_producto,id_usuario,cantidad,destino,observaciones) VALUES(?,?,?,?,?,?,?,?)",
            idVenta,idDev,idDetalle,producto,usuario,cantidad,destino.name(),obs);
    }
    private static void reponer(Connection c,int deposito,int usuario,int producto,BigDecimal cantidad,String nota)throws SQLException{
        // Bloqueo de fila, sin lectura/escritura perdida entre cajeros concurrentes.
        BigDecimal anterior=BigDecimal.ZERO;
        try(PreparedStatement p=c.prepareStatement("SELECT cantidad FROM stock_producto WHERE id_producto=? AND id_deposito=? FOR UPDATE")){
            p.setInt(1,producto);p.setInt(2,deposito);try(ResultSet r=p.executeQuery()){if(r.next())anterior=r.getBigDecimal(1);}
        }
        BigDecimal posterior=anterior.add(cantidad);
        try(PreparedStatement p=c.prepareStatement("INSERT INTO stock_producto(id_producto,id_deposito,cantidad) VALUES(?,?,?) ON DUPLICATE KEY UPDATE cantidad=cantidad+VALUES(cantidad)")){
            p.setInt(1,producto);p.setInt(2,deposito);p.setBigDecimal(3,cantidad);p.executeUpdate();
        }
        long ajuste=insert(c,"INSERT INTO ajuste_stock(id_deposito,id_usuario,motivo,observaciones,estado) VALUES(?,?,?,?,'CONFIRMADO')",
            deposito,usuario,"Retorno apto por devolución",nota);
        long detalle=insert(c,"INSERT INTO ajuste_stock_detalle(id_ajuste,id_producto,stock_sistema,stock_fisico,diferencia) VALUES(?,?,?,?,?)",
            ajuste,producto,anterior,posterior,cantidad);
        insert(c,"INSERT INTO movimiento_stock(id_producto,id_deposito,id_usuario,tipo_movimiento,id_detalle_origen,cantidad,stock_anterior,stock_posterior,observaciones) VALUES(?,?,?,'AJUSTE',?,?,?,?,?)",
            producto,deposito,usuario,detalle,cantidad,anterior,posterior,nota);
    }
    /** Anulación total: exige decisiones explícitas para TODAS las líneas. */
    public static Resultado anular(long idVenta,String motivo,List<Linea> seleccion)throws SQLException{
        return procesar(idVenta,motivo,seleccion,true);
    }
    /** Devolución de algunas o todas las unidades, sin borrar pagos originales. */
    public static Resultado devolver(long idVenta,String motivo,List<Linea> seleccion)throws SQLException{
        return procesar(idVenta,motivo,seleccion,false);
    }
    private static Resultado procesar(long idVenta,String motivo,List<Linea> seleccion,boolean anular)throws SQLException{
        int usuario=SesionUsuario.getIdUsuario();
        motivo=motivo==null?"":motivo.trim();
        exigir(motivo.length()>=10 && motivo.length()<=250,"Motivo: entre 10 y 250 caracteres.");
        exigir(seleccion!=null && !seleccion.isEmpty(),"Seleccioná los productos.");
        try(Connection c=conexion.getConexion()){
            c.setAutoCommit(false);
            try{
                permiso(c,usuario,anular?"VENTA_ANULAR":"VENTA_DEVOLVER");
                Venta v=bloquearVenta(c,idVenta);
                exigir(anular?v.estado.equals("FINALIZADA"):(v.estado.equals("FINALIZADA")||v.estado.startsWith("DEVUELTA_")),"Estado incompatible: "+v.estado);
                comprobarFiscal(c,idVenta);
                List<Linea> disponibles=lineas(c,idVenta);
                exigir(!disponibles.isEmpty(),"Venta sin detalles.");
                if(anular){
                    try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM devolucion_venta WHERE id_venta=? AND estado='CONFIRMADA' LIMIT 1")){
                        p.setLong(1,idVenta);try(ResultSet r=p.executeQuery()){exigir(!r.next(),"Esta venta ya tiene devoluciones. Usá devolución parcial.");}
                    }
                }
                Map<Long,Linea> mapa=new HashMap<>();for(Linea l:disponibles)mapa.put(l.idDetalle,l);
                Set<Long> usados=new HashSet<>();BigDecimal subtotalDev=BigDecimal.ZERO;
                BigDecimal subtotalOriginal=BigDecimal.ZERO;
                for(Linea l:disponibles)subtotalOriginal=subtotalOriginal.add(l.precio);
                for(Linea l:seleccion){
                    Linea db=mapa.get(l.idDetalle);
                    exigir(db!=null && usados.add(l.idDetalle),"Detalle inexistente o repetido.");
                    exigir(l.cantidad!=null && l.cantidad.signum()>0 && l.cantidad.scale()<=3,"Cantidad inválida.");
                    exigir(l.cantidad.compareTo(db.disponible)<=0,"Cantidad supera lo disponible: "+db.producto);
                    exigir(!anular || l.cantidad.compareTo(db.vendido)==0,"Anulación: se requiere la cantidad total de "+db.producto);
                    exigir(l.destino!=null,"Seleccioná el destino de "+db.producto);
                    exigir(l.observacion!=null && l.observacion.trim().length()>=5,"Explicá el estado físico de "+db.producto);
                    subtotalDev=subtotalDev.add(db.precio.multiply(l.cantidad).divide(db.vendido,6,RoundingMode.HALF_UP));
                }
                exigir(!anular || seleccion.size()==disponibles.size(),"La anulación exige evaluar todos los productos.");
                // Prorrateo del importe real cobrado (incluye descuentos y recargos de cabecera).
                exigir(subtotalOriginal.signum()>0,"Venta sin base monetaria; requiere revisión manual.");
                BigDecimal totalAnterior=BigDecimal.ZERO;
                try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(total),0) FROM devolucion_venta WHERE id_venta=? AND estado='CONFIRMADA'")){
                    p.setLong(1,idVenta);try(ResultSet r=p.executeQuery()){if(r.next())totalAnterior=r.getBigDecimal(1);}
                }
                BigDecimal importe=anular?v.total:subtotalDev.multiply(v.total).divide(subtotalOriginal,2,RoundingMode.HALF_UP);
                BigDecimal restante=v.total.subtract(totalAnterior);
                if(importe.compareTo(restante)>0)importe=restante;
                exigir(importe.signum()>=0,"Reintegro calculado inválido.");
                long idDev=0;
                if(!anular)idDev=insert(c,"INSERT INTO devolucion_venta(id_venta,id_usuario,motivo,total,estado,observaciones) VALUES(?,?,?,?,'CONFIRMADA',?)",
                    idVenta,usuario,motivo,importe,"Reintegro pendiente; destino individual de mercadería");
                int aptos=0;
                for(Linea l:seleccion){
                    Linea db=mapa.get(l.idDetalle);
                    int producto;
                    try(PreparedStatement p=c.prepareStatement("SELECT id_producto FROM venta_detalle WHERE id_detalle=?")){
                        p.setLong(1,l.idDetalle);try(ResultSet r=p.executeQuery()){exigir(r.next(),"Producto faltante.");producto=r.getInt(1);}
                    }
                    if(!anular){
                        BigDecimal valor=db.precio.multiply(l.cantidad).divide(db.vendido,2,RoundingMode.HALF_UP);
                        insert(c,"INSERT INTO devolucion_venta_detalle(id_devolucion,id_venta_detalle,id_producto,cantidad,precio_unitario,subtotal) VALUES(?,?,?,?,?,?)",
                            idDev,l.idDetalle,producto,l.cantidad,db.precio.divide(db.vendido,2,RoundingMode.HALF_UP),valor);
                    }
                    registrarDestino(c,idVenta,anular?null:idDev,l.idDetalle,producto,usuario,l.cantidad,l.destino,l.observacion.trim());
                    if(l.destino==Destino.APTO && db.controlaStock){
                        reponer(c,v.deposito,usuario,producto,l.cantidad,"Retorno APTO de venta #"+v.numero+" / "+motivo);
                        aptos++;
                    }
                }
                if(anular){
                    insert(c,"INSERT INTO venta_anulacion_reintegro(id_venta,id_usuario,monto,estado,observaciones) VALUES(?,?,?,'PENDIENTE',?)",
                        idVenta,usuario,importe,"Reintegro pendiente; stock según evaluación por producto");
                    exec(c,"UPDATE venta SET estado='ANULADA',fecha_anulacion=NOW(),id_usuario_anulacion=?,motivo_anulacion=? WHERE id_venta=?",
                        usuario,motivo,idVenta);
                    insert(c,"INSERT INTO venta_estado_historial(id_venta,id_usuario,estado_anterior,estado_nuevo,motivo) VALUES(?,?,?,?,?)",
                        idVenta,usuario,v.estado,"ANULADA",motivo);
                }else{
                    insert(c,"INSERT INTO devolucion_reintegro(id_devolucion,id_usuario,monto,estado) VALUES(?, ?, ?, 'PENDIENTE')",idDev,usuario,importe);
                    boolean completo=true;
                    for(Linea l:disponibles){
                        BigDecimal usado=BigDecimal.ZERO;
                        for(Linea s:seleccion)if(s.idDetalle==l.idDetalle)usado=s.cantidad;
                        if(usado.compareTo(l.disponible)<0)completo=false;
                    }
                    String nuevo=completo?"DEVUELTA_TOTAL":"DEVUELTA_PARCIAL";
                    exec(c,"UPDATE venta SET estado=? WHERE id_venta=?",nuevo,idVenta);
                    insert(c,"INSERT INTO venta_estado_historial(id_venta,id_usuario,estado_anterior,estado_nuevo,motivo) VALUES(?,?,?,?,?)",
                        idVenta,usuario,v.estado,nuevo,motivo);
                }
                c.commit();return new Resultado(idDev,importe,aptos);
            }catch(SQLException|RuntimeException ex){try{c.rollback();}catch(SQLException e){ex.addSuppressed(e);}throw ex;}
            finally{try{c.setAutoCommit(true);}catch(SQLException ignored){}}
        }
    }
    public static final class Reintegro {
        public final long idDevolucion;
        public final BigDecimal monto;
        Reintegro(long id,BigDecimal monto){this.idDevolucion=id;this.monto=monto;}
        @Override public String toString(){return "Devolución #"+idDevolucion+" | $ "+monto+" pendiente";}
    }
    public static List<Reintegro> reintegrosPendientes(long idVenta)throws SQLException{
        List<Reintegro> out=new ArrayList<>();
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(
            "SELECT d.id_devolucion,r.monto FROM devolucion_venta d JOIN devolucion_reintegro r ON r.id_devolucion=d.id_devolucion " +
            "WHERE d.id_venta=? AND d.estado='CONFIRMADA' AND r.estado='PENDIENTE' ORDER BY d.id_devolucion")){
            p.setLong(1,idVenta);try(ResultSet rs=p.executeQuery()){
                while(rs.next())out.add(new Reintegro(rs.getLong(1),rs.getBigDecimal(2)));
            }
        }
        return out;
    }
    public static void confirmarReintegroDevolucion(long idDevolucion,String referencia)throws SQLException{
        int usuario=SesionUsuario.getIdUsuario();referencia=referencia==null?"":referencia.trim();
        exigir(referencia.length()>=5 && referencia.length()<=150,"Referencia de reintegro inválida.");
        try(Connection c=conexion.getConexion()){
            c.setAutoCommit(false);
            try{
                permiso(c,usuario,"VENTA_DEVOLVER");
                try(PreparedStatement p=c.prepareStatement("UPDATE devolucion_reintegro SET estado='CONFIRMADO',fecha_confirmacion=NOW(),id_usuario_confirmacion=?,referencia=? WHERE id_devolucion=? AND estado='PENDIENTE'")){
                    p.setInt(1,usuario);p.setString(2,referencia);p.setLong(3,idDevolucion);
                    exigir(p.executeUpdate()==1,"No existe reintegro pendiente para esa devolución.");
                }
                c.commit();
            }catch(SQLException|RuntimeException ex){try{c.rollback();}catch(SQLException e){ex.addSuppressed(e);}throw ex;}
        }
    }
    public static final class Revision {
        public final long idRetorno;
        public final String producto;
        public final BigDecimal cantidad;
        Revision(long id,String producto,BigDecimal cantidad){this.idRetorno=id;this.producto=producto;this.cantidad=cantidad;}
        @Override public String toString(){return "#"+idRetorno+" | "+producto+" | "+cantidad+" (en revisión)";}
    }
    public static List<Revision> revisionesPendientes(long idVenta)throws SQLException{
        List<Revision> out=new ArrayList<>();
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(
            "SELECT r.id_retorno,p.nombre,r.cantidad FROM venta_retorno_destino r JOIN producto p ON p.id_producto=r.id_producto " +
            "LEFT JOIN venta_revision_resolucion x ON x.id_retorno=r.id_retorno " +
            "WHERE r.id_venta=? AND r.destino='REVISION' AND x.id_resolucion IS NULL ORDER BY r.id_retorno")){
            p.setLong(1,idVenta);try(ResultSet rs=p.executeQuery()){
                while(rs.next())out.add(new Revision(rs.getLong(1),rs.getString(2),rs.getBigDecimal(3)));
            }
        }
        return out;
    }
    public static void resolverRevision(long idRetorno,Destino resultado,String observacion)throws SQLException{
        exigir(resultado==Destino.APTO||resultado==Destino.MERMA,"Resultado de revisión inválido.");
        observacion=observacion==null?"":observacion.trim();
        exigir(observacion.length()>=10 && observacion.length()<=250,"Explicá la decisión (10 a 250 caracteres).");
        int usuario=SesionUsuario.getIdUsuario();
        try(Connection c=conexion.getConexion()){
            c.setAutoCommit(false);
            try{
                permiso(c,usuario,"VENTA_DEVOLVER");
                long idVenta;int deposito,producto;BigDecimal cantidad;boolean controla;
                try(PreparedStatement p=c.prepareStatement(
                    "SELECT r.id_venta,v.id_deposito,r.id_producto,r.cantidad,p.controla_stock " +
                    "FROM venta_retorno_destino r JOIN venta v ON v.id_venta=r.id_venta JOIN producto p ON p.id_producto=r.id_producto " +
                    "WHERE r.id_retorno=? AND r.destino='REVISION' FOR UPDATE")){
                    p.setLong(1,idRetorno);try(ResultSet rs=p.executeQuery()){
                        exigir(rs.next(),"Revisión inexistente.");
                        idVenta=rs.getLong(1);deposito=rs.getInt(2);producto=rs.getInt(3);
                        cantidad=rs.getBigDecimal(4);controla=rs.getBoolean(5);
                    }
                }
                try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM venta_revision_resolucion WHERE id_retorno=?")){
                    p.setLong(1,idRetorno);try(ResultSet rs=p.executeQuery()){exigir(!rs.next(),"La revisión ya fue resuelta.");}
                }
                insert(c,"INSERT INTO venta_revision_resolucion(id_retorno,id_usuario,resultado,observaciones) VALUES(?,?,?,?)",
                    idRetorno,usuario,resultado.name(),observacion);
                if(resultado==Destino.APTO && controla)reponer(c,deposito,usuario,producto,cantidad,
                    "Aprobado tras revisión #"+idRetorno+" de venta #"+idVenta+". "+observacion);
                c.commit();
            }catch(SQLException|RuntimeException ex){try{c.rollback();}catch(SQLException e){ex.addSuppressed(e);}throw ex;}
            finally{try{c.setAutoCommit(true);}catch(SQLException ignored){}}
        }
    }

}
