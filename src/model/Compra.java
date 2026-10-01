package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idCompra;

    private Proveedor proveedor;
    private Usuario usuario;
    private Deposito deposito;

    private String numeroComprobante;
    private String origenCarga;

    private LocalDateTime fecha;

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;

    private String estado;
    private String observaciones;

    private List<CompraDetalle> detalles;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Compra() {

        this.origenCarga = "MANUAL";

        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;

        this.estado = "BORRADOR";

        this.detalles = new ArrayList<>();
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Compra(
            long idCompra,
            Proveedor proveedor,
            Usuario usuario,
            Deposito deposito,
            String numeroComprobante,
            String origenCarga,
            LocalDateTime fecha,
            BigDecimal subtotal,
            BigDecimal descuento,
            BigDecimal total,
            String estado,
            String observaciones,
            List<CompraDetalle> detalles) {

        this.idCompra = idCompra;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.deposito = deposito;
        this.numeroComprobante = numeroComprobante;
        this.origenCarga = origenCarga;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;
        this.detalles = detalles;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
    }


    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }


    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }


    public String getOrigenCarga() {
        return origenCarga;
    }

    public void setOrigenCarga(String origenCarga) {
        this.origenCarga = origenCarga;
    }


    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public List<CompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }


    // =========================================================
    // AGREGAR DETALLE
    // =========================================================

    public void agregarDetalle(CompraDetalle detalle) {

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        detalles.add(detalle);
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
    // ESTADOS
    // =========================================================

    public boolean estaEnBorrador() {
        return "BORRADOR".equals(estado);
    }

    public boolean estaConfirmada() {
        return "CONFIRMADA".equals(estado);
    }

    public boolean estaAnulada() {
        return "ANULADA".equals(estado);
    }


    // =========================================================
    // ORIGEN DE CARGA
    // =========================================================

    public boolean esCargaManual() {
        return "MANUAL".equals(origenCarga);
    }

    public boolean esCargaCodigoBarras() {
        return "CODIGO_BARRAS".equals(origenCarga);
    }

    public boolean esCargaPdf() {
        return "PDF".equals(origenCarga);
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        if (idCompra > 0) {
            return "Compra #" + idCompra;
        }

        return "Nueva compra";
    }
}