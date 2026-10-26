package Dao;

import Configuracion.conexion;
import model.CompraFiscal;
import services.CompraFiscalService;
import java.sql.*;
import java.math.BigDecimal;

/** Registra datos fiscales de una compra ya confirmada, sin tocar stock. */
public class CompraFiscalDao {
    /** Uso independiente: maneja su propia transacción. */
    public void guardar(CompraFiscal f) throws SQLException {
        try (Connection cn = conexion.getConexion()) {
            boolean original = cn.getAutoCommit();
            try {
                cn.setAutoCommit(false);
                guardar(f, cn);
                cn.commit();
            } catch (SQLException | RuntimeException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(original);
            }
        }
    }

    /** Uso desde CompraService: NO hace commit, rollback ni cierra cn. */
    public void guardar(CompraFiscal f, Connection cn) throws SQLException {
        if (cn == null) throw new SQLException("Conexión de compra no disponible");
        CompraFiscalService.validar(f);
        String cab = "INSERT INTO compra_fiscal (id_compra,tipo_comprobante,letra,punto_venta,numero,fecha_emision,cuit_emisor,condicion_iva_emisor,moneda,cotizacion,neto_gravado,neto_no_gravado,importe_exento,iva_discriminado,iva_credito_computable,percepciones_iva,percepciones_iibb,otros_tributos,total_comprobante,diferencia_con_compra,cae,vencimiento_cae,observaciones_fiscales) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String det = "INSERT INTO compra_fiscal_iva (id_compra,alicuota,base_imponible,importe_iva,credito_computable) VALUES (?,?,?,?,?)";
                BigDecimal totalCompra;
                try (PreparedStatement p = cn.prepareStatement("SELECT total FROM compra WHERE id_compra=? FOR UPDATE")) {
                    p.setLong(1, f.idCompra);
                    try (ResultSet r = p.executeQuery()) {
                        if (!r.next()) throw new SQLException("No existe la compra " + f.idCompra);
                        totalCompra = r.getBigDecimal(1);
                    }
                }
                if (f.totalComprobante.subtract(totalCompra).abs()
                        .compareTo(new BigDecimal("0.02")) > 0) {
                    throw new SQLException("El total fiscal no coincide con el total de la compra");
                }
                // Evita reemplazar silenciosamente un registro fiscal ya asentado.
                try (PreparedStatement p = cn.prepareStatement(cab)) {
                    int i=1;
                    p.setLong(i++,f.idCompra); p.setString(i++,f.tipoComprobante); p.setString(i++,f.letra);
                    p.setString(i++,f.puntoVenta); p.setString(i++,f.numero);
                    p.setDate(i++,f.fechaEmision==null?null:Date.valueOf(f.fechaEmision));
                    p.setString(i++,f.cuitEmisor); p.setString(i++,f.condicionIvaEmisor);
                    p.setString(i++,f.moneda); p.setBigDecimal(i++,f.cotizacion);
                    p.setBigDecimal(i++,CompraFiscalService.netoGravado(f));
                    p.setBigDecimal(i++,f.netoNoGravado); p.setBigDecimal(i++,f.importeExento);
                    p.setBigDecimal(i++,CompraFiscalService.ivaDiscriminado(f));
                    p.setBigDecimal(i++,CompraFiscalService.creditoFiscal(f));
                    p.setBigDecimal(i++,f.percepcionesIva); p.setBigDecimal(i++,f.percepcionesIibb);
                    p.setBigDecimal(i++,f.otrosTributos); p.setBigDecimal(i++,f.totalComprobante);
                    p.setBigDecimal(i++,f.totalComprobante.subtract(totalCompra));
                    p.setString(i++,f.cae);
                    p.setDate(i++,f.vencimientoCae==null?null:Date.valueOf(f.vencimientoCae));
                    p.setString(i++,f.observacionesFiscales);
                    p.executeUpdate();
                }
                try (PreparedStatement p = cn.prepareStatement(det)) {
                    for (CompraFiscal.DetalleIva d : f.alicuotas) {
                        p.setLong(1,f.idCompra); p.setBigDecimal(2,d.alicuota);
                        p.setBigDecimal(3,d.baseImponible); p.setBigDecimal(4,d.importeIva);
                        p.setBigDecimal(5,d.creditoComputable); p.addBatch();
                    }
                    p.executeBatch();
                }
    }
}
