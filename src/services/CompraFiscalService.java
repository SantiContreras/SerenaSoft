package services;

import model.CompraFiscal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class CompraFiscalService {
    private static final Set<String> TIPOS = Set.of("FACTURA", "NOTA_CREDITO", "NOTA_DEBITO", "REMITO", "SIN_COMPROBANTE");
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
    private static BigDecimal dos(BigDecimal v) { return nz(v).setScale(2, RoundingMode.HALF_UP); }

    /** Desglosa precio FINAL con IVA incluido; nunca lo suma dos veces. */
    public static CompraFiscal.DetalleIva desdePrecioFinal(BigDecimal totalFinal, BigDecimal tasaPorcentaje, boolean creditoComputable) {
        if (totalFinal == null || tasaPorcentaje == null || totalFinal.signum() < 0 || tasaPorcentaje.signum() < 0)
            throw new IllegalArgumentException("Precio o tasa inválidos");
        BigDecimal base = totalFinal.divide(BigDecimal.ONE.add(tasaPorcentaje.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);
        BigDecimal iva = dos(totalFinal).subtract(base);
        return new CompraFiscal.DetalleIva(tasaPorcentaje, base, iva, creditoComputable ? iva : BigDecimal.ZERO);
    }

    public static BigDecimal netoGravado(CompraFiscal f) {
        return f.alicuotas.stream().map(d -> nz(d.baseImponible)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public static BigDecimal ivaDiscriminado(CompraFiscal f) {
        return f.alicuotas.stream().map(d -> nz(d.importeIva)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public static BigDecimal creditoFiscal(CompraFiscal f) {
        return f.alicuotas.stream().map(d -> nz(d.creditoComputable)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public static BigDecimal totalCalculado(CompraFiscal f) {
        return netoGravado(f).add(nz(f.netoNoGravado)).add(nz(f.importeExento))
            .add(ivaDiscriminado(f)).add(nz(f.percepcionesIva)).add(nz(f.percepcionesIibb)).add(nz(f.otrosTributos)).setScale(2, RoundingMode.HALF_UP);
    }
    public static void validar(CompraFiscal f) {
        if (f == null || f.idCompra <= 0 || f.tipoComprobante == null || !TIPOS.contains(f.tipoComprobante))
            throw new IllegalArgumentException("Compra o tipo de comprobante inválido");
        if (f.totalComprobante == null || f.totalComprobante.signum() < 0)
            throw new IllegalArgumentException("Total de comprobante inválido");
        if (f.cotizacion == null || f.cotizacion.signum() <= 0) throw new IllegalArgumentException("Cotización inválida");
        for (BigDecimal importe : new BigDecimal[]{f.netoNoGravado, f.importeExento, f.percepcionesIva, f.percepcionesIibb, f.otrosTributos})
            if (importe == null || importe.signum() < 0) throw new IllegalArgumentException("Importe fiscal inválido");
        for (CompraFiscal.DetalleIva d : f.alicuotas) {
            if (d.alicuota == null || d.baseImponible == null || d.importeIva == null || d.creditoComputable == null || d.alicuota.signum() < 0 || d.baseImponible.signum() < 0 || d.importeIva.signum() < 0 || d.creditoComputable.signum() < 0 || d.creditoComputable.compareTo(d.importeIva) > 0)
                throw new IllegalArgumentException("Detalle de IVA inválido");
        }
        if (Set.of("REMITO", "SIN_COMPROBANTE").contains(f.tipoComprobante) && creditoFiscal(f).signum() != 0)
            throw new IllegalArgumentException("No corresponde crédito fiscal automático sin factura válida");
        if ("C".equals(f.letra) && (ivaDiscriminado(f).signum() != 0 || creditoFiscal(f).signum() != 0))
            throw new IllegalArgumentException("Factura C no discrimina IVA");
        if (totalCalculado(f).subtract(f.totalComprobante).abs().compareTo(new BigDecimal("0.02")) > 0)
            throw new IllegalArgumentException("Los importes fiscales no concilian con el total del comprobante");
    }
}
