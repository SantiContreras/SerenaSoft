package services.comprobantes;

/** Contrato futuro para implementar WSAA + WSFEv1 sin acoplarlo a Swing. */
public interface EmisorFiscal {
    ResultadoFiscal autorizar(SolicitudFiscal solicitud) throws Exception;
}
