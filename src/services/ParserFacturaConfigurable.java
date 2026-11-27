package services;
import model.FacturaPdf;
import java.math.*;import java.time.*;import java.util.regex.*;
/** Perfil manual: expresiones con grupos nombrados; nunca habilita escritura. */
public final class ParserFacturaConfigurable {
 public FacturaPdf parse(FacturaPdf f, java.util.Properties p){
  f.demo=false; f.lineas.clear(); f.alertas.clear();
  f.proveedor=campo(p,"proveedor",f.texto); f.cuit=campo(p,"cuit",f.texto); f.letra=campo(p,"letra",f.texto); f.numero=campo(p,"numero",f.texto);
  f.fecha=LocalDate.parse(campo(p,"fecha",f.texto),java.time.format.DateTimeFormatter.ofPattern(p.getProperty("fecha.formato","dd/MM/uuuu")));
  f.subtotal=num(campo(p,"subtotal",f.texto)); f.total=num(campo(p,"total",f.texto)); f.iva=num(campo(p,"iva",f.texto)); f.otrosTributos=num(campo(p,"otros",f.texto)); f.descuento=num(campo(p,"descuento",f.texto));
  Matcher m=Pattern.compile(p.getProperty("fila"),Pattern.MULTILINE|Pattern.CASE_INSENSITIVE).matcher(f.texto); BigDecimal suma=BigDecimal.ZERO;
  while(m.find()){FacturaPdf.Linea l=new FacturaPdf.Linea();l.codigo=m.group("codigo");l.descripcion=m.group("descripcion");l.unidad=m.group("unidad");l.factor=num(m.group("factor"));l.cantidad=num(m.group("cantidad"));l.costo=num(m.group("costo"));l.importe=num(m.group("importe"));l.revisarFactor=true;if(l.factor.signum()<=0||l.cantidad.signum()<=0||l.costo.signum()<0)throw new IllegalArgumentException("Cantidad/costo/factor inválido");if(l.cantidad.multiply(l.costo).setScale(2,RoundingMode.HALF_UP).compareTo(l.importe)!=0)f.alertas.add("Importe inconsistente: "+l.codigo);suma=suma.add(l.importe);f.lineas.add(l);}
  if(f.lineas.isEmpty())throw new IllegalArgumentException("Perfil sin coincidencias de artículos");if(suma.compareTo(f.subtotal)!=0)f.alertas.add("Detalle no concilia con subtotal");if(f.subtotal.subtract(f.descuento).add(f.iva).add(f.otrosTributos).compareTo(f.total)!=0)f.alertas.add("Total no conciliado");f.alertas.add("Perfil manual: verificar cobertura completa y datos fiscales");return f;
 }
 private String campo(java.util.Properties p,String k,String t){String regex=p.getProperty(k);if(regex==null)throw new IllegalArgumentException("Falta configuración: "+k);if(regex.startsWith("literal:"))return regex.substring(8);Matcher m=Pattern.compile(regex,Pattern.MULTILINE|Pattern.CASE_INSENSITIVE).matcher(t);if(!m.find())throw new IllegalArgumentException("No se encontró: "+k);return m.group(1);}
 private BigDecimal num(String s){return new BigDecimal(s.trim().replace(".","").replace(',','.'));}
}
