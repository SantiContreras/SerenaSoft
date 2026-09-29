package model;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class AjusteStock {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idAjuste;

    private Deposito deposito;

    private Usuario usuario;

    private LocalDateTime fecha;

    private String motivo;

    private String observaciones;

    private String estado;

    private List<AjusteStockDetalle> detalles;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public AjusteStock() {

        this.estado =
                "BORRADOR";

        this.detalles =
                new ArrayList<>();
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public AjusteStock(
            long idAjuste,
            Deposito deposito,
            Usuario usuario,
            LocalDateTime fecha,
            String motivo,
            String observaciones,
            String estado,
            List<AjusteStockDetalle> detalles) {

        this.idAjuste =
                idAjuste;

        this.deposito =
                deposito;

        this.usuario =
                usuario;

        this.fecha =
                fecha;

        this.motivo =
                motivo;

        this.observaciones =
                observaciones;

        this.estado =
                estado;

        this.detalles =
                detalles != null
                        ? detalles
                        : new ArrayList<>();
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdAjuste() {

        return idAjuste;
    }

    public void setIdAjuste(
            long idAjuste) {

        this.idAjuste =
                idAjuste;
    }


    public Deposito getDeposito() {

        return deposito;
    }

    public void setDeposito(
            Deposito deposito) {

        this.deposito =
                deposito;
    }


    public Usuario getUsuario() {

        return usuario;
    }

    public void setUsuario(
            Usuario usuario) {

        this.usuario =
                usuario;
    }


    public LocalDateTime getFecha() {

        return fecha;
    }

    public void setFecha(
            LocalDateTime fecha) {

        this.fecha =
                fecha;
    }


    public String getMotivo() {

        return motivo;
    }

    public void setMotivo(
            String motivo) {

        this.motivo =
                motivo;
    }


    public String getObservaciones() {

        return observaciones;
    }

    public void setObservaciones(
            String observaciones) {

        this.observaciones =
                observaciones;
    }


    public String getEstado() {

        return estado;
    }

    public void setEstado(
            String estado) {

        this.estado =
                estado;
    }


    public List<AjusteStockDetalle> getDetalles() {

        return detalles;
    }

    public void setDetalles(
            List<AjusteStockDetalle> detalles) {

        this.detalles =
                detalles != null
                        ? detalles
                        : new ArrayList<>();
    }


    // =========================================================
    // AGREGAR DETALLE
    // =========================================================

    public void agregarDetalle(
            AjusteStockDetalle detalle) {

        if (detalle == null) {

            return;
        }

        if (detalles == null) {

            detalles =
                    new ArrayList<>();
        }

        detalles.add(
                detalle
        );
    }


    // =========================================================
    // VERIFICAR ESTADOS
    // =========================================================

    public boolean estaEnBorrador() {

        return "BORRADOR".equalsIgnoreCase(
                estado
        );
    }


    public boolean estaConfirmado() {

        return "CONFIRMADO".equalsIgnoreCase(
                estado
        );
    }


    public boolean estaAnulado() {

        return "ANULADO".equalsIgnoreCase(
                estado
        );
    }


    // =========================================================
    // CANTIDAD DE DETALLES
    // =========================================================

    public int cantidadDetalles() {

        if (detalles == null) {

            return 0;
        }

        return detalles.size();
    }


    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {

        return "Ajuste #"
                + idAjuste;
    }
}