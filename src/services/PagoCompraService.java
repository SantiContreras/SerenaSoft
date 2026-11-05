package services;

import Configuracion.conexion;
import java.math.BigDecimal;
import java.sql.*;

/** Pagos aplicados a compras confirmadas. Cada pago y su egreso se guardan en una transacción. */
public final class PagoCompraService {
    public static final class Estado {
        public final BigDecimal total, pagado, pendiente;
        public final String estado;
        public Estado(BigDecimal t,BigDecimal p) {
            total=t;pagado=p;pendiente=t.subtract(p);
            estado=p.signum()==0?"PENDIENTE":pendiente.signum()==0?"PAGADA":"PARCIAL";
        }
    }
    public Estado consultar(long idCompra) throws SQLException {
        try(Connection c=conexion.getConexion()) {return estado(c,idCompra,false);}
    }
    private Estado estado(Connection c,long id,boolean bloquear) throws SQLException {
        String sql="SELECT total,estado FROM compra WHERE id_compra=?"+(bloquear?" FOR UPDATE":"");
        BigDecimal total;
        try(PreparedStatement p=c.prepareStatement(sql)) {
            p.setLong(1,id);try(ResultSet r=p.executeQuery()) {
                if(!r.next())throw new SQLException("Compra inexistente: "+id);
                if(!"CONFIRMADA".equals(r.getString(2)))throw new SQLException("Solo se pagan compras CONFIRMADAS.");
                total=r.getBigDecimal(1);
            }
        }
        try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(monto),0) FROM compra_pago WHERE id_compra=?")) {
            p.setLong(1,id);try(ResultSet r=p.executeQuery()) {r.next();return new Estado(total,r.getBigDecimal(1));}
        }
    }
    public long registrar(long compra,int usuario,int medio,BigDecimal monto,String referencia,String observaciones) throws SQLException {
        if(compra<=0||usuario<=0||medio<=0||monto==null||monto.scale()>2||monto.signum()<=0)throw new SQLException("Datos de pago inválidos.");
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                Estado e=estado(c,compra,true);
                if(monto.compareTo(e.pendiente)>0)throw new SQLException("El pago supera el saldo pendiente: $"+e.pendiente);
                String tipo;boolean efectivo;
                try(PreparedStatement p=c.prepareStatement("SELECT tipo,afecta_efectivo_fisico FROM medio_pago WHERE id_medio_pago=? AND activo=1")) {
                    p.setInt(1,medio);try(ResultSet r=p.executeQuery()) {
                        if(!r.next())throw new SQLException("Medio de pago inexistente o inactivo.");
                        tipo=r.getString(1);efectivo=r.getBoolean(2);
                    }
                }
                // Asociar el movimiento a la caja ABIERTA del usuario que paga.
                // No asignar automáticamente a la caja de otro cajero.
                Long sesion=null,movimiento=null;
                try(PreparedStatement p=c.prepareStatement(
                    "SELECT id_sesion FROM caja_sesion WHERE estado='ABIERTA' AND id_usuario_apertura=? ORDER BY id_sesion DESC LIMIT 1 FOR UPDATE")) {
                    p.setInt(1,usuario);
                    try(ResultSet r=p.executeQuery()){if(r.next())sesion=r.getLong(1);}
                }
                if(efectivo && sesion==null)
                    throw new SQLException("Para pagar en efectivo debés tener tu propia caja ABIERTA.");
                if(sesion!=null) {
                    String medioCaja=efectivo?"EFECTIVO":
                        tipo!=null&&tipo.toUpperCase().contains("TRANSFER")?"TRANSFERENCIA":
                        tipo!=null&&(tipo.toUpperCase().contains("TARJETA")||tipo.toUpperCase().contains("DEBITO")||tipo.toUpperCase().contains("CREDITO"))?"TARJETA":"MERCADO_PAGO";
                    if(efectivo) {
                        BigDecimal inicial=BigDecimal.ZERO,ventas=BigDecimal.ZERO,otros=BigDecimal.ZERO;
                        try(PreparedStatement p=c.prepareStatement("SELECT monto_inicial FROM caja_sesion WHERE id_sesion=?")) {
                            p.setLong(1,sesion);try(ResultSet r=p.executeQuery()){if(r.next())inicial=r.getBigDecimal(1);}
                        }
                        try(PreparedStatement p=c.prepareStatement(
                            "SELECT COALESCE(SUM(vp.monto),0) FROM venta_pago vp JOIN venta v ON v.id_venta=vp.id_venta JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE v.id_caja_sesion=? AND mp.afecta_efectivo_fisico=1")) {
                            p.setLong(1,sesion);try(ResultSet r=p.executeQuery()){if(r.next())ventas=r.getBigDecimal(1);}
                        }
                        try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(CASE WHEN tipo='INGRESO' THEN monto ELSE -monto END),0) FROM caja_movimiento WHERE id_sesion=? AND medio_pago='EFECTIVO'")) {
                            p.setLong(1,sesion);try(ResultSet r=p.executeQuery()){if(r.next())otros=r.getBigDecimal(1);}
                        }
                        if(inicial.add(ventas).add(otros).compareTo(monto)<0)
                            throw new SQLException("Fondos insuficientes en caja para este pago en efectivo.");
                    }
                    try(PreparedStatement p=c.prepareStatement(
                        "INSERT INTO caja_movimiento(id_sesion,id_usuario,tipo,medio_pago,monto,motivo,observaciones) VALUES (?,?,'EGRESO',?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                        p.setLong(1,sesion);p.setInt(2,usuario);p.setString(3,medioCaja);p.setBigDecimal(4,monto);
                        p.setString(5,"Pago proveedor - compra #"+compra);p.setString(6,referencia);
                        p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(r.next())movimiento=r.getLong(1);}
                    }
                }
                long idPago;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO compra_pago(id_compra,id_usuario,id_sesion,id_medio_pago,monto,referencia,observaciones,id_movimiento_caja) VALUES (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                    p.setLong(1,compra);p.setInt(2,usuario);
                    if(sesion==null)p.setNull(3,Types.BIGINT);else p.setLong(3,sesion);
                    p.setInt(4,medio);p.setBigDecimal(5,monto);p.setString(6,referencia);p.setString(7,observaciones);
                    if(movimiento==null)p.setNull(8,Types.BIGINT);else p.setLong(8,movimiento);
                    p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("No se generó ID de pago.");idPago=r.getLong(1);}
                }
                c.commit();return idPago;
            }catch(SQLException|RuntimeException ex){c.rollback();throw ex;}
        }
    }
}
