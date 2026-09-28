package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalidaStock {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idSalida;

    private Deposito deposito;

    private Usuario usuario;

    private LocalDateTime fecha;

    private String motivo;

    private String destino;

    private String observaciones;

    private String estado;

    private List<SalidaStockDetalle> detalles;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public SalidaStock() {

        this.estado = "BORRADOR";

        this.detalles =
                new ArrayList<>();
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public SalidaStock(
            long idSalida,
            Deposito deposito,
            Usuario usuario,
            LocalDateTime fecha,
            String motivo,
            String destino,
            String observaciones,
            String estado,
            List<SalidaStockDetalle> detalles) {

        this.idSalida = idSalida;
        this.deposito = deposito;
        this.usuario = usuario;
        this.fecha = fecha;
        this.motivo = motivo;
        this.destino = destino;
        this.observaciones = observaciones;
        this.estado = estado;

        this.detalles =
                detalles != null
                ? detalles
                : new ArrayList<>();
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(long idSalida) {
        this.idSalida = idSalida;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<SalidaStockDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(
            List<SalidaStockDetalle> detalles) {

        this.detalles =
                detalles != null
                ? detalles
                : new ArrayList<>();
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    public void agregarDetalle(
            SalidaStockDetalle detalle) {

        if (detalle != null) {

            detalles.add(detalle);
        }
    }

    public boolean estaEnBorrador() {

        return "BORRADOR".equalsIgnoreCase(
                estado
        );
    }

    public boolean estaConfirmada() {

        return "CONFIRMADA".equalsIgnoreCase(
                estado
        );
    }

    public boolean estaAnulada() {

        return "ANULADA".equalsIgnoreCase(
                estado
        );
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Salida #" + idSalida;
    }
}