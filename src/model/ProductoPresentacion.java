package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Presentación de compra. El factor convierte a la unidad de stock del producto. */
public class ProductoPresentacion {
    private int idPresentacion;
    private int idProducto;
    private String nombre;
    private UnidadMedida unidadCompra;
    private BigDecimal factorConversion = BigDecimal.ONE;
    private boolean predeterminada;
    private boolean activo = true;
    private LocalDateTime fechaCreacion;

    public ProductoPresentacion() {}

    public int getIdPresentacion() { return idPresentacion; }
    public void setIdPresentacion(int v) { idPresentacion = v; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int v) { idProducto = v; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { nombre = v; }
    public UnidadMedida getUnidadCompra() { return unidadCompra; }
    public void setUnidadCompra(UnidadMedida v) { unidadCompra = v; }
    public BigDecimal getFactorConversion() { return factorConversion; }
    public void setFactorConversion(BigDecimal v) { factorConversion = v; }
    public boolean isPredeterminada() { return predeterminada; }
    public void setPredeterminada(boolean v) { predeterminada = v; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean v) { activo = v; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v) { fechaCreacion = v; }

    public BigDecimal convertirAStock(BigDecimal cantidadCompra) {
        if (cantidadCompra == null || cantidadCompra.signum() <= 0
                || factorConversion == null || factorConversion.signum() <= 0) {
            throw new IllegalArgumentException("Cantidad o factor inválidos.");
        }
        return cantidadCompra.multiply(factorConversion).setScale(3, java.math.RoundingMode.UNNECESSARY);
    }

    @Override public String toString() {
        return nombre + " (x" + factorConversion.stripTrailingZeros().toPlainString() + ")";
    }
}
