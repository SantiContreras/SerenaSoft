package services;

import Configuracion.conexion;
import Dao.VentaDao;
import Dao.VentaDetalleDao;
import Dao.VentaPagoDao;
import model.Venta;
import model.VentaDetalle;
import model.PagoVenta;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

/** Motor transaccional POS. Los importes de venta incluyen IVA. */
public final class VentaService {
    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final int MONEY = 2;
    public static final class Item {
        public final int productoId;
        public final BigDecimal cantidad, descuentoPorcentaje;
        public Item(int productoId, BigDecimal cantidad, BigDecimal descuentoPorcentaje) {
            this.productoId=productoId; this.cantidad=cantidad; this.descuentoPorcentaje=descuentoPorcentaje;
        }
    }
    public static final class Pago {
        public final int medioPagoId;
        public final BigDecimal monto, recibido;
        public final String referencia;
        public Pago(int medioPagoId, BigDecimal monto, BigDecimal recibido, String referencia) {
            this.medioPagoId=medioPagoId; this.monto=monto; this.recibido=recibido; this.referencia=referencia;
        }
    }
    public static final class Solicitud {
        public int usuarioId, depositoId;
        public Integer clienteId, cajaSesionId;
        public BigDecimal descuentoGeneral=BigDecimal.ZERO;
        public String observaciones="";
        public final List<Item> items=new ArrayList<>();
        public final List<Pago> pagos=new ArrayList<>();
    }
    public static final class Resultado {
        public final long ventaId, numero;
        public final BigDecimal subtotal, descuento, total, cambio;
        Resultado(long id,long numero,BigDecimal sub,BigDecimal desc,BigDecimal total,BigDecimal cambio){
            this.ventaId=id;this.numero=numero;this.subtotal=sub;this.descuento=desc;this.total=total;this.cambio=cambio;
        }
    }
    private static final class Linea {
        int id; String nombre; BigDecimal cantidad, precio, costo, iva, descuentoPorcentaje, descuentoImporte, subtotal, ivaImporte;
        boolean controla, sinStock;
    }
    private static BigDecimal money(BigDecimal v){return v.setScale(MONEY,RoundingMode.HALF_UP);}
    private static void validar(boolean ok,String msg){if(!ok)throw new IllegalArgumentException(msg);}
    private static long insertId(Connection c,String sql,Object... args)throws SQLException{
        try(PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            bind(ps,args);ps.executeUpdate();try(ResultSet rs=ps.getGeneratedKeys()){
                if(!rs.next())throw new SQLException("No se generó identificador");return rs.getLong(1);
            }
        }
    }
    private static void update(Connection c,String sql,Object... args)throws SQLException{
        try(PreparedStatement ps=c.prepareStatement(sql)){bind(ps,args);ps.executeUpdate();}
    }
    private static void bind(PreparedStatement ps,Object... args)throws SQLException{
        for(int i=0;i<args.length;i++)ps.setObject(i+1,args[i]);
    }
    private static BigDecimal nz(BigDecimal b){return b==null?BigDecimal.ZERO:b;}

    /** Se debe invocar sólo tras confirmar el pago. Hace COMMIT o ROLLBACK completo. */
    public Resultado confirmar(Solicitud s)throws SQLException{
        validar(s!=null && s.usuarioId>0 && s.depositoId>0,"Usuario y depósito obligatorios");
        validar(!s.items.isEmpty(),"No hay productos");
        validar(s.descuentoGeneral!=null && s.descuentoGeneral.signum()>=0,"Descuento inválido");
        try(Connection c=conexion.getConexion()){
            c.setAutoCommit(false);
            try {
                List<Linea> lineas=new ArrayList<>();
                BigDecimal bruto=BigDecimal.ZERO;
                for(Item item:s.items){
                    validar(item.cantidad!=null && item.cantidad.signum()>0,"Cantidad inválida");
                    validar(item.descuentoPorcentaje!=null && item.descuentoPorcentaje.signum()>=0 && item.descuentoPorcentaje.compareTo(CIEN)<=0,"Descuento de línea inválido");
                    Linea l=new Linea();l.id=item.productoId;l.cantidad=item.cantidad;l.descuentoPorcentaje=item.descuentoPorcentaje;
                    String q="SELECT p.nombre,p.precio_venta,p.precio_compra,p.controla_stock,p.permite_venta_sin_stock,p.permite_descuento,COALESCE(t.porcentaje,0) iva FROM producto p LEFT JOIN tipo_iva t ON t.id_iva=p.id_iva WHERE p.id_producto=? AND p.activo=1";
                    try(PreparedStatement ps=c.prepareStatement(q)){
                        ps.setInt(1,l.id);try(ResultSet rs=ps.executeQuery()){
                            validar(rs.next(),"Producto inexistente/inactivo: "+l.id);
                            l.nombre=rs.getString("nombre");l.precio=rs.getBigDecimal("precio_venta");l.costo=nz(rs.getBigDecimal("precio_compra"));l.iva=nz(rs.getBigDecimal("iva"));
                            l.controla=rs.getBoolean("controla_stock");l.sinStock=rs.getBoolean("permite_venta_sin_stock");
                            validar(item.descuentoPorcentaje.signum()==0 || rs.getBoolean("permite_descuento"),"Producto sin permiso de descuento: "+l.nombre);
                        }
                    }
                    BigDecimal base=money(l.precio.multiply(l.cantidad));
                    l.descuentoImporte=money(base.multiply(l.descuentoPorcentaje).divide(CIEN,8,RoundingMode.HALF_UP));
                    l.subtotal=base.subtract(l.descuentoImporte);
                    bruto=bruto.add(base);lineas.add(l);
                }
                BigDecimal descuentosLineas=BigDecimal.ZERO;
                for(Linea l:lineas)descuentosLineas=descuentosLineas.add(l.descuentoImporte);
                BigDecimal netoLineas=bruto.subtract(descuentosLineas);
                validar(s.descuentoGeneral.compareTo(netoLineas)<=0,"Descuento general mayor al subtotal");
                BigDecimal general=money(s.descuentoGeneral);
                BigDecimal total=netoLineas.subtract(general);
                validar(total.signum()>0,"Total debe ser mayor a cero");
                BigDecimal totalPagos=BigDecimal.ZERO,cambio=BigDecimal.ZERO;
                validar(!s.pagos.isEmpty(),"Debe indicar pagos");
                for(Pago p:s.pagos){
                    validar(p.monto!=null && p.monto.signum()>0,"Monto de pago inválido");
                    totalPagos=totalPagos.add(money(p.monto));
                    BigDecimal recibido=p.recibido==null?p.monto:p.recibido;
                    validar(recibido.compareTo(p.monto)>=0,"Importe recibido insuficiente");
                    cambio=cambio.add(recibido.subtract(p.monto));
                }
                validar(totalPagos.compareTo(total)==0,"Pagos no coinciden con total: "+totalPagos+" vs "+total);
                VentaDao ventaDao=new VentaDao();
                VentaDetalleDao detalleDao=new VentaDetalleDao();
                VentaPagoDao pagoDao=new VentaPagoDao();
                long numero=ventaDao.reservarNumero(c);
                Venta venta=new Venta();
                venta.numeroVenta=numero;venta.idUsuario=s.usuarioId;venta.idDeposito=s.depositoId;
                venta.idCliente=s.clienteId;venta.idCajaSesion=s.cajaSesionId;
                venta.subtotal=bruto;venta.descuentoTotal=descuentosLineas.add(general);
                venta.total=total;venta.observaciones=s.observaciones;
                long id=ventaDao.insertar(c,venta);
                BigDecimal distribuido=BigDecimal.ZERO;
                for(int i=0;i<lineas.size();i++){
                    Linea l=lineas.get(i);
                    BigDecimal parte=(i==lineas.size()-1)?general.subtract(distribuido):money(general.multiply(l.subtotal).divide(netoLineas,8,RoundingMode.HALF_UP));
                    distribuido=distribuido.add(parte);
                    l.descuentoImporte=l.descuentoImporte.add(parte);l.subtotal=l.subtotal.subtract(parte);
                    l.ivaImporte=l.iva.signum()==0?BigDecimal.ZERO:money(l.subtotal.multiply(l.iva).divide(CIEN.add(l.iva),8,RoundingMode.HALF_UP));
                    VentaDetalle detalle=new VentaDetalle();
                    detalle.idVenta=id;detalle.idProducto=l.id;detalle.descripcionProducto=l.nombre;
                    detalle.cantidad=l.cantidad;detalle.precioUnitario=l.precio;detalle.costoUnitario=l.costo;
                    detalle.descuentoPorcentaje=l.descuentoPorcentaje;detalle.descuentoImporte=l.descuentoImporte;
                    detalle.ivaPorcentaje=l.iva;detalle.ivaImporte=l.ivaImporte;detalle.subtotal=l.subtotal;
                    detalleDao.insertar(c,detalle);
                    if(l.controla){
                        BigDecimal anterior;
                        try(PreparedStatement ps=c.prepareStatement("SELECT cantidad FROM stock_producto WHERE id_producto=? AND id_deposito=? FOR UPDATE")){
                            ps.setInt(1,l.id);ps.setInt(2,s.depositoId);
                            try(ResultSet rs=ps.executeQuery()){anterior=rs.next()?rs.getBigDecimal(1):BigDecimal.ZERO;}
                        }
                        BigDecimal posterior=anterior.subtract(l.cantidad);
                        validar(l.sinStock || posterior.signum()>=0,"Stock insuficiente: "+l.nombre);
                        int actualizados;
                        try(PreparedStatement ps=c.prepareStatement("UPDATE stock_producto SET cantidad=? WHERE id_producto=? AND id_deposito=?")){
                            bind(ps,posterior,l.id,s.depositoId);actualizados=ps.executeUpdate();
                        }
                        if(actualizados==0){
                            update(c,"INSERT INTO stock_producto(id_producto,id_deposito,cantidad) VALUES(?,?,?)",l.id,s.depositoId,posterior);
                        }
                        update(c,"INSERT INTO movimiento_stock(id_producto,id_deposito,id_usuario,tipo_movimiento,motivo,cantidad,stock_anterior,stock_posterior,costo_unitario,tipo_origen,id_origen) VALUES(?,?,?,'VENTA','Venta POS',?,?,?,?, 'VENTA',?)",
                            l.id,s.depositoId,s.usuarioId,l.cantidad.negate(),anterior,posterior,l.costo,id);
                    }
                }
                for(Pago p:s.pagos){
                    BigDecimal recibido=p.recibido==null?p.monto:p.recibido;
                    PagoVenta pago=new PagoVenta();
                    pago.idVenta=id;pago.idMedioPago=p.medioPagoId;pago.monto=money(p.monto);
                    pago.importeRecibido=money(recibido);pago.cambio=money(recibido.subtract(p.monto));
                    pago.referencia=p.referencia;
                    pagoDao.insertar(c,pago);
                }
                ventaDao.registrarFinalizacion(c,id,s.usuarioId);
                c.commit();return new Resultado(id,numero,bruto,descuentosLineas.add(general),total,money(cambio));
            }catch(Exception ex){
                c.rollback();if(ex instanceof SQLException)throw (SQLException)ex;
                throw new SQLException("Venta revertida: "+ex.getMessage(),ex);
            }finally{c.setAutoCommit(true);}
        }
    }
}
