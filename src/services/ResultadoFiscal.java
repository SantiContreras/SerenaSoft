package services.comprobantes;

import java.time.LocalDate;

public record ResultadoFiscal(EstadoFiscal estado, String cae, LocalDate vencimientoCae,
        String observaciones, Long numeroAutorizado) {
    public ResultadoFiscal {
        if (estado == null) throw new IllegalArgumentException("Estado obligatorio");
    }
}
