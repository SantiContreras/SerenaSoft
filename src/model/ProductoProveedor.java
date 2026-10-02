package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Relaciona un producto con uno de sus proveedores.
 *
 * Un producto puede tener varios proveedores y un proveedor
 * puede comercializar varios productos.
 */
public class ProductoProveedor {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private Producto producto;
    private Proveedor proveedor;

    private String codigoProveedor;
    private BigDecimal costoUltimo;
    private boolean proveedorPrincipal;
    private LocalDateTime fechaUltimaCompra;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public ProductoProveedor() {
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public ProductoProveedor(
            Producto producto,
            Proveedor proveedor,
            String codigoProveedor,
            BigDecimal costoUltimo,
            boolean proveedorPrincipal,
            LocalDateTime fechaUltimaCompra) {

        this.producto = producto;
        this.proveedor = proveedor;
        this.codigoProveedor = codigoProveedor;
        this.costoUltimo = costoUltimo;
        this.proveedorPrincipal = proveedorPrincipal;
        this.fechaUltimaCompra = fechaUltimaCompra;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public BigDecimal getCostoUltimo() {
        return costoUltimo;
    }

    public void setCostoUltimo(BigDecimal costoUltimo) {
        this.costoUltimo = costoUltimo;
    }

    public boolean isProveedorPrincipal() {
        return proveedorPrincipal;
    }

    public void setProveedorPrincipal(boolean proveedorPrincipal) {
        this.proveedorPrincipal = proveedorPrincipal;
    }

    public LocalDateTime getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(LocalDateTime fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    @Override
    public String toString() {

        String nombreProducto = producto != null
                ? producto.getNombre()
                : "Sin producto";

        String nombreProveedor = proveedor != null
                ? proveedor.toString()
                : "Sin proveedor";

        return nombreProducto + " - " + nombreProveedor;
    }
}