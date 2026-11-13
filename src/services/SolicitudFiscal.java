package services.comprobantes;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos mínimos; ampliar con alícuotas, tributos y documentos antes de conectar ARCA. */
public record SolicitudFiscal(long idVenta, long cuitEmisor, int puntoVenta,
        int tipoComprobante, long numeroDocumento, int tipoDocumento,
        BigDecimal importeTotal, LocalDate fecha) {
    public SolicitudFiscal {
        if (idVenta <= 0 || puntoVenta <= 0 || tipoComprobante <= 0 ||
                importeTotal == null || importeTotal.signum() < 0 || fecha == null)
            throw new IllegalArgumentException("Solicitud fiscal incompleta");
    }
}
