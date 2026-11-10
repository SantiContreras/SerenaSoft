package services;

import Configuracion.conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sesion.SesionUsuario;

/** Operaciones de caja transaccionales. Nunca contabiliza reintegros pendientes como egresos. */
public final class CajaService {
    public static final class Resumen {
        public long idSesion;
        public String cajero = "", apertura = "";
        public BigDecimal inicial = BigDecimal.ZERO, ventas = BigDecimal.ZERO,
            ingresos = BigDecimal.ZERO, egresos = BigDecimal.ZERO,
            ventasEfectivo = BigDecimal.ZERO, ingresosEfectivo = BigDecimal.ZERO,
            egresosEfectivo = BigDecimal.ZERO, efectivoEsperado = BigDecimal.ZERO,
            tarjeta = BigDecimal.ZERO, transferencia = BigDecimal.ZERO, mercadoPago = BigDecimal.ZERO;
    }
    public static final class Movimiento {
        public String fecha, hora, tipo, descripcion, medio, usuario;
        public BigDecimal ingreso, egreso;
        Movimiento(String fecha, String hora, String tipo, String descripcion, String medio,
                BigDecimal ingreso, BigDecimal egreso, String usuario) {
            this.fecha=fecha; this.hora=hora; this.tipo=tipo; this.descripcion=descripcion;
            this.medio=medio; this.ingreso=ingreso; this.egreso=egreso; this.usuario=usuario;
        }
    }
    private static BigDecimal money(ResultSet rs, int index) throws SQLException {
        BigDecimal n = rs.getBigDecimal(index); return n == null ? BigDecimal.ZERO : n;
    }
    private static String medio(String raw) {
        String s = raw == null ? "" : raw.toUpperCase(java.util.Locale.ROOT);
        if (s.contains("EFECTIVO")) return "EFECTIVO";
        if (s.contains("MERCADO") || s.contains("MP")) return "MERCADO_PAGO";
        if (s.contains("TRANSFER")) return "TRANSFERENCIA";
        if (s.contains("TARJETA") || s.contains("DEBITO") || s.contains("CRÉDITO") || s.contains("CREDITO")) return "TARJETA";
        throw new IllegalArgumentException("Medio de pago no reconocido: " + raw);
    }
    private static void montoValido(BigDecimal m) {
        if (m == null || m.signum() <= 0 || m.scale() > 2) throw new IllegalArgumentException("El importe debe ser positivo y tener como máximo dos decimales.");
    }
    private static void bloquearSesion(Connection c, long id) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT estado FROM caja_sesion WHERE id_sesion=? FOR UPDATE")) {
            p.setLong(1,id);
            try(ResultSet r=p.executeQuery()) { if (!r.next() || !"ABIERTA".equals(r.getString(1))) throw new SQLException("La caja ya no está abierta. Actualizá el panel."); }
        }
    }
    private static void verificarPropietario(Connection c, long sesion, int usuario) throws SQLException {
        if (usuario <= 0 || usuario != SesionUsuario.getIdUsuario())
            throw new SQLException("El usuario activo no corresponde a esta operación de caja.");
        try (PreparedStatement p = c.prepareStatement("SELECT id_usuario_apertura FROM caja_sesion WHERE id_sesion=? FOR UPDATE")) {
            p.setLong(1, sesion);
            try (ResultSet rs = p.executeQuery()) {
                if (!rs.next() || rs.getInt(1) != usuario)
                    throw new SQLException("Esta caja pertenece a otro cajero.");
            }
        }
    }
    public Resumen obtenerResumen() throws SQLException {
        int usuarioActual = SesionUsuario.getIdUsuario();
        if (usuarioActual <= 0) throw new SQLException("Iniciá sesión para consultar tu caja.");
        Resumen r = new Resumen();
        try(Connection c=conexion.getConexion()) {
            try(PreparedStatement p=c.prepareStatement("SELECT cs.id_sesion,cs.monto_inicial,DATE_FORMAT(cs.fecha_apertura,'%d/%m/%Y %H:%i'),u.nombre_completo FROM caja_sesion cs JOIN usuario u ON u.id_usuario=cs.id_usuario_apertura WHERE cs.estado='ABIERTA' AND cs.id_usuario_apertura=? ORDER BY cs.id_sesion DESC LIMIT 1")) {
                p.setInt(1,usuarioActual);
                try(ResultSet x=p.executeQuery()) { if(x.next()) {r.idSesion=x.getLong(1);r.inicial=money(x,2);r.apertura=x.getString(3);r.cajero=x.getString(4);} }
            }
            if(r.idSesion==0) return r;
            // Ventas vinculadas explícitamente a esta sesión; no inventar movimientos de ventas anteriores.
            try(PreparedStatement p=c.prepareStatement("SELECT UPPER(mp.nombre),COALESCE(SUM(vp.monto),0) FROM venta_pago vp JOIN venta v ON v.id_venta=vp.id_venta JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE v.id_caja_sesion=? AND v.estado IN ('FINALIZADA','DEVUELTA_PARCIAL','DEVUELTA_TOTAL','ANULADA') GROUP BY UPPER(mp.nombre)")) {
                p.setLong(1,r.idSesion);
                try(ResultSet x=p.executeQuery()) {while(x.next()) { BigDecimal m=money(x,2); r.ventas=r.ventas.add(m); String name=x.getString(1); String k; try{k=medio(name);}catch(IllegalArgumentException e){continue;} switch(k){case "EFECTIVO":r.ventasEfectivo=r.ventasEfectivo.add(m);break;case "TARJETA":r.tarjeta=r.tarjeta.add(m);break;case "TRANSFERENCIA":r.transferencia=r.transferencia.add(m);break;case "MERCADO_PAGO":r.mercadoPago=r.mercadoPago.add(m);break;} }}
            }
            try(PreparedStatement p=c.prepareStatement("SELECT tipo,medio_pago,COALESCE(SUM(monto),0) FROM caja_movimiento WHERE id_sesion=? GROUP BY tipo,medio_pago")) {
                p.setLong(1,r.idSesion);
                try(ResultSet x=p.executeQuery()) {while(x.next()) {boolean in="INGRESO".equals(x.getString(1));String k=x.getString(2);BigDecimal m=money(x,3);if(in){r.ingresos=r.ingresos.add(m);if("EFECTIVO".equals(k))r.ingresosEfectivo=r.ingresosEfectivo.add(m);}else{r.egresos=r.egresos.add(m);if("EFECTIVO".equals(k))r.egresosEfectivo=r.egresosEfectivo.add(m);} }}
            }
            r.efectivoEsperado=r.inicial.add(r.ventasEfectivo).add(r.ingresosEfectivo).subtract(r.egresosEfectivo);
        }
        return r;
    }
    public List<Movimiento> movimientos(long sesion,String tipo,String periodo) throws SQLException {
        List<Movimiento> result=new ArrayList<>();
        String sql="SELECT fecha,hora,tipo,descripcion,medio,ingreso,egreso,usuario FROM ("+
            "SELECT DATE_FORMAT(vp.fecha,'%d/%m/%Y') fecha,DATE_FORMAT(vp.fecha,'%H:%i') hora,'Venta' tipo,CONCAT('Venta N° ',v.numero_venta) descripcion,mp.nombre medio,vp.monto ingreso,0 egreso,COALESCE(u.nombre_completo,'') usuario,vp.fecha orden FROM venta_pago vp JOIN venta v ON v.id_venta=vp.id_venta JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago LEFT JOIN usuario u ON u.id_usuario=v.id_usuario WHERE v.id_caja_sesion=? "+
            "UNION ALL SELECT DATE_FORMAT(m.fecha,'%d/%m/%Y'),DATE_FORMAT(m.fecha,'%H:%i'),IF(m.tipo='INGRESO','Ingreso','Egreso'),m.motivo,m.medio_pago,IF(m.tipo='INGRESO',m.monto,0),IF(m.tipo='EGRESO',m.monto,0),COALESCE(u.nombre_completo,''),m.fecha FROM caja_movimiento m LEFT JOIN usuario u ON u.id_usuario=m.id_usuario WHERE m.id_sesion=?"+
            ") q WHERE 1=1";
        if("Venta".equals(tipo))sql+=" AND tipo='Venta'";
        else if("Ingreso".equals(tipo))sql+=" AND tipo='Ingreso'";
        else if("Egreso".equals(tipo)||"Pago Proveedor".equals(tipo)||"Retiro".equals(tipo))sql+=" AND tipo='Egreso'";
        if("Hoy".equals(periodo))sql+=" AND STR_TO_DATE(fecha,'%d/%m/%Y')=CURDATE()";
        else if("Ayer".equals(periodo))sql+=" AND STR_TO_DATE(fecha,'%d/%m/%Y')=CURDATE()-INTERVAL 1 DAY";
        else if("Últimos 7 días".equals(periodo))sql+=" AND STR_TO_DATE(fecha,'%d/%m/%Y')>=CURDATE()-INTERVAL 6 DAY";
        else if("Este mes".equals(periodo))sql+=" AND YEAR(STR_TO_DATE(fecha,'%d/%m/%Y'))=YEAR(CURDATE()) AND MONTH(STR_TO_DATE(fecha,'%d/%m/%Y'))=MONTH(CURDATE())";
        sql+=" ORDER BY orden DESC LIMIT 500";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)) {
            p.setLong(1,sesion);p.setLong(2,sesion);
            try(ResultSet x=p.executeQuery()) {while(x.next())result.add(new Movimiento(x.getString(1),x.getString(2),x.getString(3),x.getString(4),x.getString(5),money(x,6),money(x,7),x.getString(8)));}
        }
        return result;
    }
    public void abrir(int usuario,BigDecimal inicial,String observacion) throws SQLException {
        if(inicial==null || inicial.signum()<0 || inicial.scale()>2)throw new IllegalArgumentException("Saldo inicial inválido.");
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                // Serializar las aperturas del mismo usuario, incluso desde dos equipos.
                try (PreparedStatement lock = c.prepareStatement("SELECT id_usuario FROM usuario WHERE id_usuario=? FOR UPDATE")) {
                    lock.setInt(1, usuario);
                    try (ResultSet rs = lock.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Usuario inexistente.");
                    }
                }
                try (PreparedStatement check = c.prepareStatement("SELECT id_sesion FROM caja_sesion WHERE id_usuario_apertura=? AND estado='ABIERTA' LIMIT 1")) {
                    check.setInt(1, usuario);
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next()) throw new SQLException("Ya tenés una caja abierta. Cerrala antes de abrir otra.");
                    }
                }
                // Caja física/lógica dedicada a cada cajero. No reutilizar CAJA-01 de otro usuario.
                String codigo = "CAJERO-" + usuario;
                long idCaja;
                try (PreparedStatement caja = c.prepareStatement("SELECT id_caja FROM caja WHERE codigo=? AND activo=1 FOR UPDATE")) {
                    caja.setString(1, codigo);
                    try (ResultSet rs = caja.executeQuery()) {
                        if (rs.next()) idCaja = rs.getLong(1);
                        else {
                            try (PreparedStatement ins = c.prepareStatement("INSERT INTO caja(codigo,nombre,activo) VALUES(?,?,1)", Statement.RETURN_GENERATED_KEYS)) {
                                ins.setString(1, codigo);
                                ins.setString(2, "Caja del usuario " + usuario);
                                ins.executeUpdate();
                                try (ResultSet keys = ins.getGeneratedKeys()) {
                                    if (!keys.next()) throw new SQLException("No se pudo crear la caja del usuario.");
                                    idCaja = keys.getLong(1);
                                }
                            }
                        }
                    }
                }
                try(PreparedStatement p=c.prepareStatement("INSERT INTO caja_sesion(id_caja,id_usuario_apertura,monto_inicial,estado,observacion_apertura) VALUES(?,?,?,'ABIERTA',?)")) {
                    p.setLong(1,idCaja);p.setInt(2,usuario);p.setBigDecimal(3,inicial);p.setString(4,observacion);p.executeUpdate();
                }
                c.commit();
            } catch(Exception e){c.rollback();if(e instanceof SQLException)throw(SQLException)e;throw new SQLException(e.getMessage(),e);}finally{c.setAutoCommit(true);}
        }
    }
    public void registrar(long sesion,int usuario,boolean ingreso,BigDecimal monto,String medioPago,String motivo,String observaciones) throws SQLException {
        montoValido(monto);String k=medio(medioPago);
        if(motivo==null || motivo.isBlank())throw new IllegalArgumentException("Indicá un motivo.");
        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                verificarPropietario(c, sesion, usuario);
                bloquearSesion(c,sesion);
                try(PreparedStatement p=c.prepareStatement("INSERT INTO caja_movimiento(id_sesion,id_usuario,tipo,medio_pago,monto,motivo,observaciones) VALUES(?,?,?,?,?,?,?)")) {
                    p.setLong(1,sesion);p.setInt(2,usuario);p.setString(3,ingreso?"INGRESO":"EGRESO");p.setString(4,k);p.setBigDecimal(5,monto);p.setString(6,motivo);p.setString(7,observaciones);p.executeUpdate();
                }
                c.commit();
            }catch(Exception e){c.rollback();if(e instanceof SQLException)throw(SQLException)e;throw new SQLException(e.getMessage(),e);}finally{c.setAutoCommit(true);}
        }
    }

    /** Calcula el efectivo dentro de la transacción que bloqueó la sesión. */
    private static BigDecimal efectivoBajoBloqueo(Connection c, long sesion) throws SQLException {
        BigDecimal inicial;
        try (PreparedStatement p=c.prepareStatement("SELECT monto_inicial FROM caja_sesion WHERE id_sesion=?")) {
            p.setLong(1,sesion);
            try(ResultSet rs=p.executeQuery()) { if(!rs.next()) throw new SQLException("Sesión inexistente."); inicial=money(rs,1); }
        }
        BigDecimal ventas=BigDecimal.ZERO;
        try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(vp.monto),0) FROM venta_pago vp JOIN venta v ON v.id_venta=vp.id_venta JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE v.id_caja_sesion=? AND UPPER(mp.nombre) LIKE '%EFECTIVO%' AND v.estado IN ('FINALIZADA','DEVUELTA_PARCIAL','DEVUELTA_TOTAL','ANULADA')")) {
            p.setLong(1,sesion);try(ResultSet rs=p.executeQuery()){if(rs.next())ventas=money(rs,1);}
        }
        BigDecimal ajuste=BigDecimal.ZERO;
        try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(CASE WHEN tipo='INGRESO' THEN monto ELSE -monto END),0) FROM caja_movimiento WHERE id_sesion=? AND medio_pago='EFECTIVO'")) {
            p.setLong(1,sesion);try(ResultSet rs=p.executeQuery()){if(rs.next())ajuste=money(rs,1);}
        }
        return inicial.add(ventas).add(ajuste);
    }
    private static void validarDiferencia(BigDecimal diferencia, String observacion) {
        if(diferencia.signum()!=0 && (observacion==null || observacion.trim().isEmpty()))
            throw new IllegalArgumentException("Para registrar un faltante o sobrante debés escribir una observación.");
    }

    public static final class SesionAuditada {
        public long id; public String cajero, apertura, cierre, estado, observacion;
        public BigDecimal inicial, esperado, contado, diferencia;
    }
    public static final class ArqueoAuditado {
        public String fecha, usuario, observacion;
        public BigDecimal esperado, contado, diferencia;
    }
    public List<SesionAuditada> historial(java.sql.Date desde,java.sql.Date hasta,int cajero,String estado) throws SQLException {
        if(!SesionUsuario.esAdministrador())throw new SQLException("Solo el administrador puede consultar todas las cajas.");
        String sql="SELECT s.id_sesion,u.nombre_completo,DATE_FORMAT(s.fecha_apertura,'%d/%m/%Y %H:%i'),"+
          "DATE_FORMAT(s.fecha_cierre,'%d/%m/%Y %H:%i'),s.estado,s.monto_inicial,s.efectivo_esperado,"+
          "s.efectivo_contado,s.diferencia,s.observacion_cierre FROM caja_sesion s JOIN usuario u ON u.id_usuario=s.id_usuario_apertura "+
          "WHERE DATE(s.fecha_apertura) BETWEEN ? AND ? AND (?=0 OR s.id_usuario_apertura=?) AND (?='TODAS' OR s.estado=?) ORDER BY s.id_sesion DESC LIMIT 1000";
        List<SesionAuditada> lista=new ArrayList<>();
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setDate(1,desde);p.setDate(2,hasta);p.setInt(3,cajero);p.setInt(4,cajero);p.setString(5,estado);p.setString(6,estado);
            try(ResultSet rs=p.executeQuery()){while(rs.next()){
                SesionAuditada a=new SesionAuditada();a.id=rs.getLong(1);a.cajero=rs.getString(2);a.apertura=rs.getString(3);
                a.cierre=rs.getString(4);a.estado=rs.getString(5);a.inicial=money(rs,6);a.esperado=money(rs,7);
                a.contado=money(rs,8);a.diferencia=money(rs,9);a.observacion=rs.getString(10);lista.add(a);
            }}
        }
        return lista;
    }
    public List<ArqueoAuditado> arqueosHistoricos(long sesion) throws SQLException {
        if(!SesionUsuario.esAdministrador())throw new SQLException("Acceso reservado al administrador.");
        List<ArqueoAuditado> lista=new ArrayList<>();
        String sql="SELECT DATE_FORMAT(a.fecha,'%d/%m/%Y %H:%i'),u.nombre_completo,a.efectivo_esperado,a.efectivo_contado,a.diferencia,a.observaciones FROM arqueo_caja a JOIN usuario u ON u.id_usuario=a.id_usuario WHERE a.id_sesion=? ORDER BY a.fecha";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setLong(1,sesion);try(ResultSet rs=p.executeQuery()){while(rs.next()){
                ArqueoAuditado a=new ArqueoAuditado();a.fecha=rs.getString(1);a.usuario=rs.getString(2);
                a.esperado=money(rs,3);a.contado=money(rs,4);a.diferencia=money(rs,5);a.observacion=rs.getString(6);lista.add(a);
            }}
        }
        return lista;
    }
    public void arquear(long sesion,int usuario,BigDecimal contado,String obs) throws SQLException {
        if(contado==null || contado.signum()<0)throw new IllegalArgumentException("Conteo inválido.");
        if (usuario != SesionUsuario.getIdUsuario()) throw new SQLException("Usuario de caja no coincide con la sesión actual.");

        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {verificarPropietario(c, sesion, usuario);bloquearSesion(c,sesion);
                BigDecimal esperado=efectivoBajoBloqueo(c,sesion);BigDecimal diferencia=contado.subtract(esperado);validarDiferencia(diferencia,obs);
                try(PreparedStatement p=c.prepareStatement("INSERT INTO arqueo_caja(id_sesion,id_usuario,efectivo_esperado,efectivo_contado,diferencia,observaciones) VALUES(?,?,?,?,?,?)")) {
                    p.setLong(1,sesion);p.setInt(2,usuario);p.setBigDecimal(3,esperado);p.setBigDecimal(4,contado);p.setBigDecimal(5,diferencia);p.setString(6,obs);p.executeUpdate();}
                c.commit();
            }catch(Exception e){c.rollback();if(e instanceof SQLException)throw(SQLException)e;throw new SQLException(e.getMessage(),e);}finally{c.setAutoCommit(true);}
        }
    }
    public void cerrar(long sesion,int usuario,BigDecimal contado,String obs) throws SQLException {
        if(contado==null || contado.signum()<0)throw new IllegalArgumentException("Conteo inválido.");
        if (usuario != SesionUsuario.getIdUsuario()) throw new SQLException("Usuario de caja no coincide con la sesión actual.");

        try(Connection c=conexion.getConexion()) {
            c.setAutoCommit(false);
            try {verificarPropietario(c, sesion, usuario);bloquearSesion(c,sesion);
                BigDecimal esperado=efectivoBajoBloqueo(c,sesion);BigDecimal diferencia=contado.subtract(esperado);validarDiferencia(diferencia,obs);
                try(PreparedStatement p=c.prepareStatement("UPDATE caja_sesion SET estado='CERRADA',id_usuario_cierre=?,fecha_cierre=NOW(),efectivo_esperado=?,efectivo_contado=?,diferencia=?,observacion_cierre=? WHERE id_sesion=? AND estado='ABIERTA'")) {
                    p.setInt(1,usuario);p.setBigDecimal(2,esperado);p.setBigDecimal(3,contado);p.setBigDecimal(4,diferencia);p.setString(5,obs);p.setLong(6,sesion);if(p.executeUpdate()!=1)throw new SQLException("No se pudo cerrar la caja.");}
                c.commit();
            }catch(Exception e){c.rollback();if(e instanceof SQLException)throw(SQLException)e;throw new SQLException(e.getMessage(),e);}finally{c.setAutoCommit(true);}
        }
    }
}
