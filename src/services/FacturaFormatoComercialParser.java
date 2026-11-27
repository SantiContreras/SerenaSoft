package services;
import model.FacturaPdf;
import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.regex.*;
import java.util.Locale;

/** Lector conservador de comprobantes tabulares argentinos con texto. */
public final class FacturaFormatoComercialParser {
 private static final String N="[0-9]+(?:\\.[0-9]{3})*(?:,[0-9]+)?";
 private static final String U="\\p{L}[\\p{L}.\\-]*(?:\\s+\\p{L}[\\p{L}.\\-]*){0,2}";
 private static BigDecimal n(String s){return new BigDecimal(s.replace(".","").replace(',','.'));}
 private static BigDecimal money(BigDecimal n){return n.setScale(2,RoundingMode.HALF_UP);}
 private static Matcher find(String p,String t){Matcher m=Pattern.compile(p,Pattern.CASE_INSENSITIVE|Pattern.MULTILINE).matcher(t);if(!m.find())throw new IllegalArgumentException("Formato no reconocido o campo ausente: "+p);return m;}
 public FacturaPdf parse(FacturaPdf f){
 String t=f.texto.replace('\u00a0',' ');
 f.letra=find("^\\s*([ABC])\\s*$",t).group(1).toUpperCase(Locale.ROOT);
 f.proveedor=find("^Raz.n Social:\\s*([^\\r\\n]+)",t).group(1).trim();
 f.cuit=find("^CUIT:\\s*([0-9-]+)",t).group(1);
 f.numero=find("Punto de Venta:\\s*(\\d+)",t).group(1)+"-"+find("Comp\\.\\s*Nro:\\s*(\\d+)",t).group(1);
 f.fecha=LocalDate.parse(find("Fecha de Emisi.n:\\s*(\\d{2}/\\d{2}/\\d{4})",t).group(1),DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));
 f.total=n(find("Importe Total:\\s*\\$?\\s*("+N+")",t).group(1));
 f.subtotal=n(find("^(?:Subtotal|Importe Neto Gravado):\\s*\\$?\\s*("+N+")",t).group(1));
 f.descuento=BigDecimal.ZERO;f.iva=BigDecimal.ZERO;
 Matcher iv=Pattern.compile("IVA Contenido en el Total:\\s*\\$?\\s*("+N+")",Pattern.CASE_INSENSITIVE).matcher(t);if(iv.find())f.ivaIncluido=n(iv.group(1));
 // No interpretar impuestos discriminados como IVA incluido.
 boolean aFiscal=f.letra.equals("A"); Matcher taxes=Pattern.compile("(?m)^IVA\\s+[0-9.,]+%:\\s*\\$?\\s*("+N+")").matcher(t); while(taxes.find())f.iva=f.iva.add(n(taxes.group(1))); Matcher otros=Pattern.compile("(?im)^Importe Otros Tributos:\\s*\\$?\\s*("+N+")").matcher(t); if(otros.find())f.otrosTributos=n(otros.group(1));
 String body;
 try {body=ExtractorColumnasPDF.detalle(f.original,aFiscal);} catch(Exception ex){body="";}
 if(body.isBlank()) {
 Matcher start=find("^C.digo[\\s\\S]*?Precio Unit\\.",t);int end=find("^Detalle de Otros Tributos|^R.gimen de Transparencia|^Subtotal:\\s*\\$",t.substring(start.end())).start()+start.end();
 body=t.substring(start.end(),end); }
 Pattern row=Pattern.compile("^([A-Za-z0-9][A-Za-z0-9._/-]*)\\s+(.+?)\\s+("+N+")\\s+("+U+")\\s+("+N+")\\s+(?:("+N+")\\s+)?("+N+")"+(aFiscal?"\\s+([0-9.,]+)%\\s+("+N+")":"")+"$",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
 String pending="";BigDecimal sum=BigDecimal.ZERO;
 for(String raw:body.split("\\R")){
 String line=raw.trim();if(line.isEmpty()||line.matches("(?i)%|Bonif|Subtotal|Subtotal IVA|Subt\\. c/|IVA"))continue;
 if(!pending.isBlank() && line.matches("^\\d{8,14}\\s+.*")){f.alertas.add("Renglón no interpretado: "+pending);pending="";} pending=(pending+" "+line).trim();Matcher r=row.matcher(pending);if(!r.matches())continue;
 FacturaPdf.Linea a=new FacturaPdf.Linea();a.codigo=r.group(1);a.descripcion=r.group(2);a.unidad=r.group(4).toUpperCase(Locale.ROOT);if(a.unidad.matches("UN|UNIDAD|UNIDADES"))a.unidad="UN";if(a.unidad.matches("KILO|KILOS|KILOGRAMO|KILOGRAMOS"))a.unidad="KG";
 a.cantidad=n(r.group(3));BigDecimal bruto=n(r.group(5));a.bonificacion=r.group(6)==null?BigDecimal.ZERO:n(r.group(6));a.importe=n(r.group(7));
 if(a.cantidad.signum()<=0||bruto.signum()<=0||a.bonificacion.signum()<0||a.bonificacion.compareTo(BigDecimal.valueOf(100))>=0||money(a.cantidad.multiply(bruto).multiply(BigDecimal.ONE.subtract(a.bonificacion.movePointLeft(2)))).compareTo(a.importe)!=0)throw new IllegalArgumentException("Importe inconsistente: "+a.codigo);
 if(aFiscal){a.alicuota=new BigDecimal(r.group(8).replace(',','.'));a.totalConIva=n(r.group(9));if(money(a.importe.multiply(BigDecimal.ONE.add(a.alicuota.movePointLeft(2)))).compareTo(a.totalConIva)!=0)f.alertas.add("IVA de línea inconsistente: "+a.codigo);} a.costo=a.importe.divide(a.cantidad,8,RoundingMode.HALF_UP).stripTrailingZeros();
 Matcher factor=Pattern.compile("(?iu)\\b"+a.unidad+"\\s*x\\s*(\\d+)").matcher(a.descripcion);
 a.factor=BigDecimal.ONE;a.revisarFactor=!a.unidad.equals("UN")&&!a.unidad.equals("KG");
 if(factor.find() && !a.descripcion.substring(factor.end()).matches("(?i)^\\s*(kg|g|ml|l)\\b.*"))a.factor=new BigDecimal(factor.group(1));
 if(a.factor.signum()<=0)throw new IllegalArgumentException("Factor inválido: "+a.codigo);
 f.lineas.add(a);sum=sum.add(a.importe);pending="";
 }
 if(!pending.isBlank())f.alertas.add("Texto de detalle sin interpretar: "+pending); if(f.lineas.isEmpty())throw new IllegalArgumentException("No se detectaron artículos"); if(sum.compareTo(f.subtotal)!=0)f.alertas.add("Detalle $"+sum+" distinto del neto PDF $"+f.subtotal); if(f.subtotal.add(f.iva).add(f.otrosTributos).compareTo(f.total)!=0)f.alertas.add("Total fiscal no conciliado"); if(aFiscal){BigDecimal ivaLineas=BigDecimal.ZERO;for(FacturaPdf.Linea l:f.lineas)ivaLineas=ivaLineas.add(l.totalConIva.subtract(l.importe));if(ivaLineas.compareTo(f.iva)!=0)f.alertas.add("IVA detalle $"+ivaLineas+" distinto de IVA PDF $"+f.iva);}
 return f;
 }
}
