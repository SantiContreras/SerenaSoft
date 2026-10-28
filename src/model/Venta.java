package model;
import java.math.BigDecimal;
public class Venta {
 public long idVenta, numeroVenta;
 public int idUsuario, idDeposito;
 public Integer idCliente, idCajaSesion;
 public BigDecimal subtotal=BigDecimal.ZERO, descuentoTotal=BigDecimal.ZERO, total=BigDecimal.ZERO;
 public String estado="FINALIZADA", observaciones="";
}
