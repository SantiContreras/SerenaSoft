package model;
import java.math.*; import java.time.*; import java.util.*;
public class FacturaPdf {
 public final List<String> alertas=new ArrayList<>(); public BigDecimal otrosTributos=BigDecimal.ZERO; public boolean demo; public BigDecimal ivaIncluido=BigDecimal.ZERO; public String proveedor,cuit,numero,letra,texto,nombreArchivo; public LocalDate fecha; public byte[] original;
 public BigDecimal subtotal,descuento,iva,total; public final List<Linea> lineas=new ArrayList<>();
 public static class Linea { public BigDecimal alicuota=BigDecimal.ZERO,totalConIva=BigDecimal.ZERO; public String codigo,descripcion,unidad; public BigDecimal factor,cantidad,costo,importe; public Producto producto; public BigDecimal bonificacion=BigDecimal.ZERO; public boolean revisarFactor;
 public BigDecimal stock(){return cantidad.multiply(factor);} public BigDecimal costoBase(){return costo.divide(factor,2,RoundingMode.HALF_UP);} }
}
