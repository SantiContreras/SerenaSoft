package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Información fiscal de una compra existente. No modifica el stock. */
public class CompraFiscal {
    public long idCompra;
    public String tipoComprobante; // FACTURA, NOTA_CREDITO, NOTA_DEBITO, REMITO, SIN_COMPROBANTE
    public String letra; // A, B, C, M, etc. según corresponda
    public String puntoVenta, numero, cuitEmisor, condicionIvaEmisor;
    public LocalDate fechaEmision, vencimientoCae;
    public String moneda = "ARS", cae, observacionesFiscales;
    public BigDecimal cotizacion = BigDecimal.ONE;
    public BigDecimal netoNoGravado = BigDecimal.ZERO, importeExento = BigDecimal.ZERO;
    public BigDecimal percepcionesIva = BigDecimal.ZERO, percepcionesIibb = BigDecimal.ZERO;
    public BigDecimal otrosTributos = BigDecimal.ZERO, totalComprobante = BigDecimal.ZERO;
    public final List<DetalleIva> alicuotas = new ArrayList<>();

    public static class DetalleIva {
        public BigDecimal alicuota, baseImponible, importeIva, creditoComputable;
        public DetalleIva(BigDecimal alicuota, BigDecimal baseImponible, BigDecimal importeIva, BigDecimal creditoComputable) {
            this.alicuota = alicuota;
            this.baseImponible = baseImponible;
            this.importeIva = importeIva;
            this.creditoComputable = creditoComputable;
        }
    }
}
