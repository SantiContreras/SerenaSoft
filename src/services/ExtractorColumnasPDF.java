package services;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.parser.*;
import java.util.*;
import java.util.regex.*;

/** Reconstruye filas por proximidad vertical de las celdas de descripción al código. */
public final class ExtractorColumnasPDF {
 record Celda(float x,float y,String texto) {}
 static final Pattern COD=Pattern.compile("\\d{8,14}");
 public static String detalle(byte[] bytes, boolean fiscal) throws Exception {
  PdfReader reader=new PdfReader(bytes); StringBuilder out=new StringBuilder();
  try { for(int p=1;p<=reader.getNumberOfPages();p++) {
   List<Celda> c=new ArrayList<>();
   PdfContentStreamProcessor proc=new PdfContentStreamProcessor(){float lastEnd; public void displayText(String s,Matrix end){Matrix m=getCurrentTextMatrix();if(!s.isEmpty()){float x=m.get(Matrix.I31), y=m.get(Matrix.I32); if(!c.isEmpty()){Celda prev=c.get(c.size()-1);if(Math.abs(prev.y()-y)<.1 && Math.abs(lastEnd-x)<2){c.set(c.size()-1,new Celda(prev.x(),y,prev.texto()+s));lastEnd=end.get(Matrix.I31);return;}}c.add(new Celda(x,y,s));lastEnd=end.get(Matrix.I31);}}};
   proc.reset(); proc.processContent(reader.getPageContent(p),reader.getPageN(p).getAsDict(PdfName.RESOURCES));
   List<Celda> codes=c.stream().filter(a->COD.matcher(a.texto().trim()).matches()).filter(a->a.x()<reader.getPageSize(1).getWidth()*.22).sorted(Comparator.comparingDouble(a->a.y())).toList();
   if(codes.isEmpty())continue;
   float left=codes.get(0).x();
   for(Celda code:codes){
    List<Celda> same=c.stream().filter(a->Math.abs(a.y()-code.y())<1.5 && a.x()>left+15).sorted(Comparator.comparingDouble(Celda::x)).toList();
    int count=fiscal?7:5; if(same.size()<count)throw new IllegalArgumentException("Columnas incompletas: "+code.texto());
    List<Celda> nums=same.subList(same.size()-count,same.size()); float limit=nums.get(0).x()-3;
    List<Celda> desc=c.stream().filter(a->a.x()>left+15&&a.x()<limit&&Math.abs(a.y()-code.y())<=12).filter(a->codes.stream().min(Comparator.comparingDouble(k->Math.abs(k.y()-a.y()))).orElseThrow()==code).filter(a->!a.texto().matches("(?i)Producto.*|Descripci.n|Cant\\.|U\\. Med.*|%|Bonif|Subtotal|IVA|Precio Unit.*")).sorted(Comparator.comparingDouble((Celda a)->a.y()).thenComparingDouble(Celda::x)).toList();
    out.append(code.texto()); for(Celda a:desc)out.append(' ').append(a.texto()); for(Celda a:nums)out.append(' ').append(a.texto());out.append('\n');
   }
  }}finally{reader.close();} return out.toString();
 }
}
