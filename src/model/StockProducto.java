package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockProducto {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idStock;

    private Producto producto;
    private Deposito deposito;

    private BigDecimal cantidad;

    private LocalDateTime fechaActualizacion;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public StockProducto() {

        this.cantidad =
                BigDecimal.ZERO;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public StockProducto(
            long idStock,
            Producto producto,
            Deposito deposito,
            BigDecimal cantidad,
            LocalDateTime fechaActualizacion) {

        this.idStock = idStock;
        this.producto = producto;
        this.deposito = deposito;
        this.cantidad = cantidad;
        this.fechaActualizacion = fechaActualizacion;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public long getIdStock() {
        return idStock;
    }

    public void setIdStock(long idStock) {
        this.idStock = idStock;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(
            Producto producto) {

        this.producto = producto;
    }


    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(
            Deposito deposito) {

        this.deposito = deposito;
    }


    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(
            BigDecimal cantidad) {

        this.cantidad = cantidad;
    }


    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(
            LocalDateTime fechaActualizacion) {

        this.fechaActualizacion =
                fechaActualizacion;
    }


    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {

        String nombreProducto =
                producto != null
                        ? producto.getNombre()
                        : "";

        String nombreDeposito =
                deposito != null
                        ? deposito.getNombre()
                        : "";

        return nombreProducto
                + " - "
                + nombreDeposito
                + " - Stock: "
                + cantidad;
    }
}