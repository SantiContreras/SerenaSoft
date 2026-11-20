package Dao;

import Configuracion.conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/** Transferencias en dos fases, con bloqueo de filas y auditoría atómica. */
public class TransferenciaStockDao {
    public static final class Item {
        public final int producto;
        public final BigDecimal cantidad;
        public Item(int producto, BigDecimal cantidad) { this.producto=producto; this.cantidad=cantidad; }
    }
    public static final class Pendiente {
        public final long id;
        public final String origen, destino, fecha, responsable;
        public Pendiente(long id,String origen,String destino,String fecha,String responsable) {
            this.id=id;this.origen=origen;this.destino=destino;this.fecha=fecha;this.responsable=responsable;
        }
        @Override public String toString(){return "TR-"+String.format("%06d",id)+" | "+origen+" → "+destino+" | "+fecha+" | "+responsable;}
    }
    private static void validar(int origen,int destino,int usuario,List<Item> items) throws SQLException {
        if(origen<=0 || destino<=0 || origen==destino || usuario<=0 || items==null || items.isEmpty())
            throw new SQLException("Depósitos, usuario o productos inválidos.");
        Set<Integer> ids=new HashSet<>();
        for(Item i:items) if(i.producto<=0 || i.cantidad==null || i.cantidad.signum()<=0 || i.cantidad.scale()>3 || !ids.add(i.producto))
            throw new SQLException("Producto repetido o cantidad inválida (máximo 3 decimales).");
    }
    private static BigDecimal bloquearStock(Connection c,int producto,int deposito) throws SQLException {
        try(PreparedStatement p=c.prepareStatement("SELECT cantidad FROM stock_producto WHERE id_producto=? AND id_deposito=? FOR UPDATE")) {
            p.setInt(1,producto);p.setInt(2,deposito);
            try(ResultSet r=p.executeQuery()) {return r.next()?r.getBigDecimal(1):BigDecimal.ZERO;}
        }
    }
    private static void modificarStock(Connection c,int producto,int deposito,BigDecimal nuevo) throws SQLException {
        try(PreparedStatement p=c.prepareStatement("INSERT INTO stock_producto(id_producto,id_deposito,cantidad) VALUES(?,?,?) ON DUPLICATE KEY UPDATE cantidad=VALUES(cantidad)")) {
            p.setInt(1,producto);p.setInt(2,deposito);p.setBigDecimal(3,nuevo);p.executeUpdate();
        }
    }
    private static void movimiento(Connection c,int producto,int deposito,int usuario,String tipo,long detalle,BigDecimal delta,BigDecimal antes,String nota) throws SQLException {
        try(PreparedStatement p=c.prepareStatement("INSERT INTO movimiento_stock(id_producto,id_deposito,id_usuario,tipo_movimiento,id_detalle_origen,cantidad,stock_anterior,stock_posterior,observaciones) VALUES(?,?,?,?,?,?,?,?,?)")) {
            p.setInt(1,producto);p.setInt(2,deposito);p.setInt(3,usuario);p.setString(4,tipo);p.setLong(5,detalle);
            p.setBigDecimal(6,delta);p.setBigDecimal(7,antes);p.setBigDecimal(8,antes.add(delta));p.setString(9,nota);p.executeUpdate();
        }
    }
    private static void validarDepositos(Connection c,int origen,int destino) throws SQLException {
        try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM deposito WHERE id_deposito IN (?,?) AND activo=1")) {
            p.setInt(1,origen);p.setInt(2,destino);
            try(ResultSet r=p.executeQuery()) {r.next();if(r.getInt(1)!=2)throw new SQLException("Ambos depósitos deben existir y estar activos.");}
        }
    }
    /** Registra y despacha de manera atómica. No acredita destino. */
    public long enviar(int origen,int destino,int usuario,String motivo,String observaciones,List<Item> items) throws SQLException {
        validar(origen,destino,usuario,items);
        if(motivo==null || motivo.trim().isEmpty())throw new SQLException("Ingresá un motivo.");
        List<Item> orden=new ArrayList<>(items);orden.sort(Comparator.comparingInt(i->i.producto));
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                validarDepositos(c,origen,destino);
                // Bloqueo ordenado evita que dos transferencias gasten simultáneamente el mismo stock.
                Map<Integer,BigDecimal> anteriores=new LinkedHashMap<>();
                for(Item i:orden) {
                    BigDecimal anterior=bloquearStock(c,i.producto,origen);
                    if(anterior.compareTo(i.cantidad)<0)throw new SQLException("Stock insuficiente para producto ID "+i.producto+". Disponible: "+anterior);
                    anteriores.put(i.producto,anterior);
                }
                long id;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO transferencia_stock(id_origen,id_destino,id_usuario_creador,id_usuario_envia,fecha_envio,estado,motivo,observaciones) VALUES(?,?,?,?,NOW(),'EN_TRANSITO',?,?)",Statement.RETURN_GENERATED_KEYS)) {
                    p.setInt(1,origen);p.setInt(2,destino);p.setInt(3,usuario);p.setInt(4,usuario);p.setString(5,motivo.trim());p.setString(6,observaciones);
                    p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("No se generó transferencia.");id=r.getLong(1);}
                }
                for(Item i:orden) {
                    long detalle;
                    try(PreparedStatement p=c.prepareStatement("INSERT INTO transferencia_stock_detalle(id_transferencia,id_producto,cantidad) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                        p.setLong(1,id);p.setInt(2,i.producto);p.setBigDecimal(3,i.cantidad);p.executeUpdate();
                        try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("No se generó detalle.");detalle=r.getLong(1);}
                    }
                    BigDecimal antes=anteriores.get(i.producto), delta=i.cantidad.negate();
                    modificarStock(c,i.producto,origen,antes.add(delta));
                    movimiento(c,i.producto,origen,usuario,"TRANSFER_OUT",detalle,delta,antes,"Transferencia TR-"+id+" hacia depósito "+destino);
                }
                c.commit();return id;
            } catch(Exception e) {c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}
        }
    }
    public List<Pendiente> listarEnTransito() throws SQLException {
        List<Pendiente> lista=new ArrayList<>();
        String sql="SELECT t.id_transferencia,o.nombre origen,d.nombre destino,DATE_FORMAT(t.fecha_envio,'%d/%m/%Y %H:%i') fecha,u.nombre_completo responsable FROM transferencia_stock t JOIN deposito o ON o.id_deposito=t.id_origen JOIN deposito d ON d.id_deposito=t.id_destino JOIN usuario u ON u.id_usuario=t.id_usuario_envia WHERE t.estado='EN_TRANSITO' ORDER BY t.id_transferencia DESC";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next())lista.add(new Pendiente(r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)));
        }
        return lista;
    }
    public List<String> detalle(long id) throws SQLException {
        List<String> lista=new ArrayList<>();
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement("SELECT p.codigo,p.nombre,d.cantidad FROM transferencia_stock_detalle d JOIN producto p ON p.id_producto=d.id_producto WHERE d.id_transferencia=? ORDER BY p.nombre")) {
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){while(r.next())lista.add(r.getString(1)+" | "+r.getString(2)+" | "+r.getBigDecimal(3).stripTrailingZeros().toPlainString());}
        }
        return lista;
    }
    /** Confirma recepción completa. Si hay faltantes, no confirmar: requiere resolución de diferencia. */
    public void recibir(long id,int usuario,String observaciones) throws SQLException {
        if(id<=0 || usuario<=0)throw new SQLException("Transferencia o usuario inválidos.");
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                int destino;
                try(PreparedStatement p=c.prepareStatement("SELECT id_destino,estado FROM transferencia_stock WHERE id_transferencia=? FOR UPDATE")) {
                    p.setLong(1,id);try(ResultSet r=p.executeQuery()){
                        if(!r.next())throw new SQLException("Transferencia inexistente.");
                        destino=r.getInt(1);if(!"EN_TRANSITO".equals(r.getString(2)))throw new SQLException("La transferencia ya fue recibida o no está en tránsito.");
                    }
                }
                List<long[]> detalle=new ArrayList<>();List<BigDecimal> cantidades=new ArrayList<>();
                try(PreparedStatement p=c.prepareStatement("SELECT id_detalle,id_producto,cantidad FROM transferencia_stock_detalle WHERE id_transferencia=? ORDER BY id_producto")) {
                    p.setLong(1,id);try(ResultSet r=p.executeQuery()){while(r.next()){detalle.add(new long[]{r.getLong(1),r.getLong(2)});cantidades.add(r.getBigDecimal(3));}}
                }
                if(detalle.isEmpty())throw new SQLException("Transferencia sin artículos.");
                for(int n=0;n<detalle.size();n++) {
                    int producto=(int)detalle.get(n)[1];long idDetalle=detalle.get(n)[0];BigDecimal cantidad=cantidades.get(n);
                    // Garantiza que exista la fila del destino, incluso si nunca tuvo ese producto.
                    try(PreparedStatement p=c.prepareStatement("INSERT INTO stock_producto(id_producto,id_deposito,cantidad) VALUES(?,?,0) ON DUPLICATE KEY UPDATE id_stock=id_stock")) {
                        p.setInt(1,producto);p.setInt(2,destino);p.executeUpdate();
                    }
                    BigDecimal antes=bloquearStock(c,producto,destino);
                    modificarStock(c,producto,destino,antes.add(cantidad));
                    movimiento(c,producto,destino,usuario,"TRANSFER_IN",idDetalle,cantidad,antes,"Recepción TR-"+id);
                }
                try(PreparedStatement p=c.prepareStatement("UPDATE transferencia_stock SET estado='RECIBIDA',id_usuario_recibe=?,fecha_recepcion=NOW(),observaciones_recepcion=? WHERE id_transferencia=? AND estado='EN_TRANSITO'")) {
                    p.setInt(1,usuario);p.setString(2,observaciones);p.setLong(3,id);
                    if(p.executeUpdate()!=1)throw new SQLException("No se pudo confirmar recepción.");
                }
                c.commit();
            }catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}
        }
    }
}
