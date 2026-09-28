package model;

import java.math.BigDecimal;

public class SalidaStockDetalle {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idDetalle;
    private long idSalida;

    private Producto producto;

    private BigDecimal cantidad;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public SalidaStockDetalle() {
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public SalidaStockDetalle(
            long idDetalle,
            long idSalida,
            Producto producto,
            BigDecimal cantidad) {

        this.idDetalle = idDetalle;
        this.idSalida = idSalida;
        this.producto = producto;
        this.cantidad = cantidad;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public long getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(long idSalida) {
        this.idSalida = idSalida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        if (producto == null) {
            return "Detalle de salida";
        }

        return producto.getNombre();
    }
}