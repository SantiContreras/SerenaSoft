package services;
import model.*; import Dao.*; import Configuracion.conexion; import java.sql.*; import java.math.*; import java.util.*;
public class ImportacionCompraService {
 private void ok(ResultadoOperacion r){if(!r.isExitoso())throw new IllegalStateException(r.getMensaje());}
 public long confirmar(FacturaPdf f,Proveedor proveedor,Deposito deposito)throws Exception{
 if(f==null||proveedor==null||deposito==null)throw new IllegalArgumentException("Seleccione proveedor y depósito");
 if(!f.demo)throw new IllegalStateException("Formato comercial: solo revisión hasta integrar confirmación fiscal y transaccional");
 Set<Integer> ids=new HashSet<>();for(FacturaPdf.Linea l:f.lineas){if(l.producto==null||!l.producto.isActivo()||!ids.add(l.producto.getIdProducto()))throw new IllegalArgumentException("Vincule cada artículo a un producto activo distinto"); if(l.producto.getUnidadVenta()==null|| (l.unidad.equals("KG")&&!l.producto.getUnidadVenta().isPermiteDecimales()))throw new IllegalArgumentException("Revise unidad de stock: "+l.codigo);}
 String numero="DEMO "+f.letra+" "+f.numero; long id=0;
 try(Connection lock=conexion.getConexion()){
 try(PreparedStatement p=lock.prepareStatement("SELECT GET_LOCK('serena_importacion_pdf', 10)" );ResultSet r=p.executeQuery()){if(!r.next()||r.getInt(1)!=1)throw new IllegalStateException("Otra importación está en curso");}
 try{
 for(FacturaPdf.Linea l:f.lineas){if(!l.producto.isControlaStock() || !new ProductoPresentacionDao().existeActiva(l.producto.getIdProducto(),l.unidad,l.factor,lock))throw new IllegalArgumentException("Configure presentación activa "+l.unidad+" x"+l.factor+" para "+l.codigo+" antes de importar");}
 try(PreparedStatement p=lock.prepareStatement("SELECT id_compra FROM compra WHERE numero_comprobante=? AND estado<>'ANULADA'")){p.setString(1,numero);try(ResultSet r=p.executeQuery()){if(r.next())throw new IllegalStateException("Factura DEMO ya registrada o en borrador: "+r.getLong(1));}}
 Compra c=new Compra();c.setProveedor(proveedor);c.setDeposito(deposito);c.setNumeroComprobante(numero);c.setOrigenCarga("PDF");c.setObservaciones("DEMO sin validez fiscal | "+f.proveedor+" | "+f.cuit+" | "+f.fecha+" | Total PDF "+f.total+" | IVA "+f.iva);
 CompraService s=new CompraService();ok(s.crearCompra(c));id=c.getIdCompra();
 for(FacturaPdf.Linea l:f.lineas)ok(s.agregarProducto(id,l.producto.getIdProducto(),l.unidad,l.factor,l.cantidad,l.costo));
 ok(s.aplicarDescuento(id,f.descuento));
 try(PreparedStatement p=lock.prepareStatement("INSERT INTO compra_adjunto(id_compra,nombre_archivo,tipo_mime,contenido) VALUES(?,?,?,?)")){p.setLong(1,id);p.setString(2,f.nombreArchivo);p.setString(3,"application/pdf");p.setBytes(4,f.original);p.executeUpdate();}
 ok(s.confirmarCompra(id));return id;
 }catch(Exception e){throw new IllegalStateException((id>0?"Compra "+id+": revisar estado antes de reintentar. ":"")+e.getMessage(),e);}finally{try(Statement p=lock.createStatement()){p.execute("SELECT RELEASE_LOCK('serena_importacion_pdf')");}}
 }
 }
}
