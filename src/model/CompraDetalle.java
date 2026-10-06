package model;

import java.math.BigDecimal;

public class CompraDetalle {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idDetalle;
    private long idCompra;

    private Producto producto;

    /*
     * Fotografía de la presentación utilizada
     * en ESTA compra.
     *
     * Ejemplos:
     * CAJA
     * PACK
     * BULTO
     * UN
     * KG
     */
    private String unidadCompra;

    /*
     * Cantidad de unidades de stock contenidas
     * en una unidad de compra.
     *
     * Ejemplo:
     * 1 CAJA = 20 UN
     * factorConversion = 20
     */
    private BigDecimal factorConversion;

    /*
     * Cantidad comprada.
     *
     * Ejemplo:
     * 2 CAJAS
     * cantidad = 2
     */
    private BigDecimal cantidad;

    /*
     * Cantidad que realmente ingresa al stock.
     *
     * cantidadStock =
     * cantidad * factorConversion
     *
     * Ejemplo:
     * 2 CAJA * 20 = 40 UN
     */
    private BigDecimal cantidadStock;

    /*
     * Costo de UNA unidad de compra.
     *
     * Ejemplo:
     * $10.000 por CAJA
     */
    private BigDecimal costoUnitario;

    private BigDecimal subtotal;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public CompraDetalle() {

        this.factorConversion =
                BigDecimal.ONE;

        this.cantidad =
                BigDecimal.ZERO;

        this.cantidadStock =
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
            String unidadCompra,
            BigDecimal factorConversion,
            BigDecimal cantidad,
            BigDecimal cantidadStock,
            BigDecimal costoUnitario,
            BigDecimal subtotal) {

        this.idDetalle =
                idDetalle;

        this.idCompra =
                idCompra;

        this.producto =
                producto;

        this.unidadCompra =
                unidadCompra;

        this.factorConversion =
                factorConversion;

        this.cantidad =
                cantidad;

        this.cantidadStock =
                cantidadStock;

        this.costoUnitario =
                costoUnitario;

        this.subtotal =
                subtotal;
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


    public String getUnidadCompra() {
        return unidadCompra;
    }

    public void setUnidadCompra(
            String unidadCompra) {

        this.unidadCompra =
                unidadCompra;
    }


    public BigDecimal getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(
            BigDecimal factorConversion) {

        this.factorConversion =
                factorConversion;
    }


    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(
            BigDecimal cantidad) {

        this.cantidad =
                cantidad;
    }


    public BigDecimal getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(
            BigDecimal cantidadStock) {

        this.cantidadStock =
                cantidadStock;
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
    // CALCULAR CANTIDAD DE STOCK
    // =========================================================

    public BigDecimal calcularCantidadStock() {

        if (cantidad == null
                || factorConversion == null) {

            return BigDecimal.ZERO;
        }

        return cantidad.multiply(
                factorConversion
        );
    }


    // =========================================================
    // ACTUALIZAR CANTIDAD STOCK
    // =========================================================

    public void actualizarCantidadStock() {

        this.cantidadStock =
                calcularCantidadStock();
    }


    // =========================================================
    // CALCULAR SUBTOTAL
    //
    // cantidad comprada x costo de la presentación
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