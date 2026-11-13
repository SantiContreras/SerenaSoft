package services.comprobantes;

/** Validación independiente del menú y de los controles Swing. */
public final class PoliticaComprobantes {
    private PoliticaComprobantes() {}
    public static void validarModo(ModoComprobante modo, boolean licenciaArca,
            boolean integracionArcaActiva) {
        if (modo == null) throw new IllegalArgumentException("Modo requerido");
        if (modo == ModoComprobante.FACTURA_ELECTRONICA_ARCA &&
                (!licenciaArca || !integracionArcaActiva)) {
            throw new IllegalStateException("ARCA no está habilitado y validado para esta instalación");
        }
    }
}
