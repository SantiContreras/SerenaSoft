package model;
import java.math.BigDecimal;
public class VentaDetalle {
 public long idVenta;
 public int idProducto;
 public String descripcionProducto;
 public BigDecimal cantidad, precioUnitario, costoUnitario, descuentoPorcentaje, descuentoImporte, ivaPorcentaje, ivaImporte, subtotal;
}
