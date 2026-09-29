package model;

import java.math.BigDecimal;

public class AjusteStockDetalle {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private long idDetalle;

    private long idAjuste;

    private Producto producto;

    private BigDecimal stockSistema;

    private BigDecimal stockFisico;

    private BigDecimal diferencia;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public AjusteStockDetalle() {

        this.stockSistema =
                BigDecimal.ZERO;

        this.stockFisico =
                BigDecimal.ZERO;

        this.diferencia =
                BigDecimal.ZERO;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public AjusteStockDetalle(
            long idDetalle,
            long idAjuste,
            Producto producto,
            BigDecimal stockSistema,
            BigDecimal stockFisico,
            BigDecimal diferencia) {

        this.idDetalle =
                idDetalle;

        this.idAjuste =
                idAjuste;

        this.producto =
                producto;

        this.stockSistema =
                stockSistema;

        this.stockFisico =
                stockFisico;

        this.diferencia =
                diferencia;
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


    public long getIdAjuste() {

        return idAjuste;
    }

    public void setIdAjuste(
            long idAjuste) {

        this.idAjuste =
                idAjuste;
    }


    public Producto getProducto() {

        return producto;
    }

    public void setProducto(
            Producto producto) {

        this.producto =
                producto;
    }


    public BigDecimal getStockSistema() {

        return stockSistema;
    }

    public void setStockSistema(
            BigDecimal stockSistema) {

        this.stockSistema =
                stockSistema;
    }


    public BigDecimal getStockFisico() {

        return stockFisico;
    }

    public void setStockFisico(
            BigDecimal stockFisico) {

        this.stockFisico =
                stockFisico;
    }


    public BigDecimal getDiferencia() {

        return diferencia;
    }

    public void setDiferencia(
            BigDecimal diferencia) {

        this.diferencia =
                diferencia;
    }


    // =========================================================
    // CALCULAR DIFERENCIA
    // =========================================================

    /**
     * diferencia = stock físico - stock sistema
     *
     * Ejemplo:
     *
     * sistema = 70
     * físico  = 67
     * diferencia = -3
     *
     * sistema = 70
     * físico  = 74
     * diferencia = +4
     */
    public void calcularDiferencia() {

        if (stockSistema == null
                || stockFisico == null) {

            diferencia =
                    BigDecimal.ZERO;

            return;
        }


        diferencia =
                stockFisico.subtract(
                        stockSistema
                );
    }


    // =========================================================
    // VERIFICAR SI EXISTE DIFERENCIA
    // =========================================================

    public boolean tieneDiferencia() {

        if (diferencia == null) {

            return false;
        }

        return diferencia.compareTo(
                BigDecimal.ZERO
        ) != 0;
    }


    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {

        if (producto == null) {

            return "Producto sin asignar";
        }

        return producto.getNombre();
    }
}