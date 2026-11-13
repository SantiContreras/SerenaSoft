package services.comprobantes;

/** Falla de forma segura: nunca simula un CAE ni imprime una factura fiscal. */
public final class EmisorFiscalNoDisponible implements EmisorFiscal {
    @Override public ResultadoFiscal autorizar(SolicitudFiscal solicitud) {
        throw new UnsupportedOperationException(
            "Facturación ARCA no instalada. Emití el comprobante fiscal desde un medio autorizado. " +
            "Serena Soft puede emitir únicamente un ticket interno no fiscal.");
    }
}
