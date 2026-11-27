package services;
import model.FacturaPdf; import java.math.*; import java.nio.file.*; import java.time.*; import java.time.format.*; import java.util.regex.*; import com.lowagie.text.pdf.*; import com.lowagie.text.pdf.parser.*;
public class LectorFacturaPDF {
 private static final String N="[0-9]+(?:\\.[0-9]{3})*(?:,[0-9]+)?";
 private static BigDecimal n(String s){return new BigDecimal(s.replace(".","").replace(',','.'));}
 private static Matcher m(String regex,String t){Matcher x=Pattern.compile(regex).matcher(t); if(!x.find())throw new IllegalArgumentException("Campo no reconocido: "+regex);return x;}
 public FacturaPdf extraer(Path path)throws Exception{
 if(Files.size(path)>10_000_000)throw new IllegalArgumentException("PDF supera 10 MB");
 FacturaPdf f=new FacturaPdf(); f.original=Files.readAllBytes(path); f.nombreArchivo=path.getFileName().toString(); PdfReader r=new PdfReader(f.original); StringBuilder b=new StringBuilder(); try{if(r.getNumberOfPages()>50)throw new IllegalArgumentException("Máximo 50 páginas"); PdfTextExtractor e=new PdfTextExtractor(r); for(int i=1;i<=r.getNumberOfPages();i++)b.append(e.getTextFromPage(i)).append('\n');}finally{r.close();}
 f.texto=b.toString(); return f;
 }
 public FacturaPdf leer(Path path)throws Exception{ FacturaPdf f=extraer(path); String t=f.texto; if(t.isBlank())throw new IllegalArgumentException("PDF sin texto: requiere OCR"); if(!t.contains("DOCUMENTO DE PRUEBA - SIN VALIDEZ FISCAL")) return new FacturaFormatoComercialParser().parse(f); f.demo=true;
 Matcher h=m("SIN VALIDEZ FISCAL\\s*(.*?)\\s*Factura ([AB]) simulada \\| Nro\\. (\\d{4}-\\d{8})",t); f.proveedor=h.group(1); f.letra=h.group(2);f.numero=h.group(3);f.cuit=m("CUIT ficticio: ([0-9-]+)",t).group(1);f.fecha=LocalDate.parse(m("Fecha: (\\d{2}/\\d{2}/\\d{4})",t).group(1),DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));
 f.subtotal=n(m("Subtotal: \\$ ("+N+")",t).group(1));f.descuento=n(m("Descuento: \\$ ("+N+")",t).group(1));f.total=n(m("TOTAL: \\$ ("+N+")",t).group(1));f.iva=f.letra.equals("A")?n(m("IVA 21%: \\$ ("+N+")",t).group(1)):BigDecimal.ZERO;
 Matcher l=Pattern.compile("(?m)^([A-Z0-9-]+) +(.+?) +(PACK|CAJA|BULTO|UN|KG)(?: +x("+N+"))? +("+N+") +("+N+") +("+N+") *$").matcher(t); BigDecimal sum=BigDecimal.ZERO;while(l.find()){FacturaPdf.Linea a=new FacturaPdf.Linea();a.codigo=l.group(1);a.descripcion=l.group(2);a.unidad=l.group(3);a.factor=l.group(4)==null?BigDecimal.ONE:n(l.group(4));a.cantidad=n(l.group(5));a.costo=n(l.group(6));a.importe=n(l.group(7));if(a.factor.signum()<=0||a.cantidad.signum()<=0||a.costo.signum()<=0||a.cantidad.multiply(a.costo).setScale(2,RoundingMode.HALF_UP).compareTo(a.importe)!=0)throw new IllegalArgumentException("Importe inválido: "+a.codigo);sum=sum.add(a.importe);f.lineas.add(a);}
 if(f.lineas.isEmpty()||sum.compareTo(f.subtotal)!=0||f.descuento.compareTo(sum)>0||sum.subtract(f.descuento).add(f.iva).compareTo(f.total)!=0)throw new IllegalArgumentException("Factura no concilia; no se puede importar");return f;
 }
 public static void main(String[] args)throws Exception{for(String p:args){FacturaPdf f=new LectorFacturaPDF().leer(Path.of(p));System.out.println(f.numero+" | "+f.lineas.size()+" | "+f.total);}}
}
