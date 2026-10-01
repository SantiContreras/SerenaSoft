package model;

import java.math.BigDecimal;

public class CompraDetalle {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idDetalle;
    private long idCompra;
    private Producto producto;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal subtotal;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public CompraDetalle() {

        this.cantidad =
                BigDecimal.ZERO;

        this.costoUnitario =
                BigDecimal.ZERO;

        this.subtotal =
                BigDecimal.ZERO;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public CompraDetalle(
            long idDetalle,
            long idCompra,
            Producto producto,
            BigDecimal cantidad,
            BigDecimal costoUnitario,
            BigDecimal subtotal) {

        this.idDetalle = idDetalle;
        this.idCompra = idCompra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(
            long idDetalle) {

        this.idDetalle =
                idDetalle;
    }


    public long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(
            long idCompra) {

        this.idCompra =
                idCompra;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(
            Producto producto) {

        this.producto =
                producto;
    }


    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(
            BigDecimal cantidad) {

        this.cantidad =
                cantidad;
    }


    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(
            BigDecimal costoUnitario) {

        this.costoUnitario =
                costoUnitario;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal) {

        this.subtotal =
                subtotal;
    }


    // =========================================================
    // CALCULAR SUBTOTAL
    //
    // cantidad x costo_unitario
    // =========================================================

    public BigDecimal calcularSubtotal() {

        if (cantidad == null
                || costoUnitario == null) {

            return BigDecimal.ZERO;
        }

        return cantidad.multiply(
                costoUnitario
        );
    }


    // =========================================================
    // ACTUALIZAR SUBTOTAL
    // =========================================================

    public void actualizarSubtotal() {

        this.subtotal =
                calcularSubtotal();
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        if (producto == null) {

            return "Detalle de compra";
        }

        return producto.toString();
    }
}