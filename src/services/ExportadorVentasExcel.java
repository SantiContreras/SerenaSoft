package services;

import Configuracion.conexion;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** Exportador OOXML (.xlsx) sin librerías adicionales. No modifica datos. */
public final class ExportadorVentasExcel {
    private ExportadorVentasExcel() {}
    private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final class Venta {
        String numero,fecha,hora,cliente,pago,estado;
        BigDecimal total;
    }
    private static final class Resumen {
        int ventas, anuladas;
        BigDecimal total=BigDecimal.ZERO;
    }
    public static int exportar(File destino, LocalDate desde, LocalDate hasta, String numero, String cliente, String pago, String estado) throws Exception {
        if(desde==null || hasta==null || desde.isAfter(hasta)) throw new IllegalArgumentException("Rango de fechas inválido");
        List<Venta> ventas=new ArrayList<>();
        StringBuilder sql=new StringBuilder("SELECT v.numero_venta,v.fecha,v.total,v.estado, "
            + "COALESCE(NULLIF(TRIM(c.razon_social),''),NULLIF(TRIM(CONCAT_WS(' ',c.nombre,c.apellido)),''),'Consumidor Final') cliente, "
            + "COALESCE((SELECT GROUP_CONCAT(DISTINCT mp.nombre ORDER BY mp.nombre SEPARATOR ', ') FROM venta_pago vp JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE vp.id_venta=v.id_venta),'Sin pago') pago "
            + "FROM venta v LEFT JOIN cliente c ON c.id_cliente=v.id_cliente WHERE v.fecha>=? AND v.fecha<?");
        List<Object> args=new ArrayList<>();
        args.add(Timestamp.valueOf(desde.atStartOfDay()));args.add(Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));
        if(numero!=null&&!numero.trim().isEmpty()) {
            try {args.add(Long.parseLong(numero.trim()));}catch(NumberFormatException ex){throw new IllegalArgumentException("Número de venta inválido");}
            sql.append(" AND v.numero_venta=?");
        }
        if(cliente!=null&&!cliente.trim().isEmpty()) {
            sql.append(" AND (c.razon_social LIKE ? OR c.nombre LIKE ? OR c.apellido LIKE ? OR c.nro_documento LIKE ?)");
            for(int i=0;i<4;i++)args.add("%"+cliente.trim()+"%");
        }
        if(pago!=null&&!"Todos".equals(pago)) {
            sql.append(" AND EXISTS(SELECT 1 FROM venta_pago vp2 JOIN medio_pago mp2 ON mp2.id_medio_pago=vp2.id_medio_pago WHERE vp2.id_venta=v.id_venta AND mp2.nombre=?)");args.add(pago);
        }
        if(estado!=null&&!"Todos".equals(estado)) {sql.append(" AND v.estado=?");args.add(estado);}
        sql.append(" ORDER BY v.fecha,v.id_venta");
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql.toString())) {
            for(int i=0;i<args.size();i++)ps.setObject(i+1,args.get(i));
            try(ResultSet rs=ps.executeQuery()) {
                while(rs.next()) {
                    Venta v=new Venta();Timestamp t=rs.getTimestamp("fecha");
                    v.numero=String.format("%08d",rs.getLong("numero_venta"));
                    v.fecha=t.toLocalDateTime().toLocalDate().format(DIA);
                    v.hora=t.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    v.cliente=rs.getString("cliente");v.pago=rs.getString("pago");
                    v.estado=rs.getString("estado");v.total=rs.getBigDecimal("total");
                    ventas.add(v);
                }
            }
        }
        Map<String,Resumen> meses=new TreeMap<>();
        BigDecimal total=BigDecimal.ZERO;int finalizadas=0,anuladas=0;
        for(Venta v:ventas) {
            String mes=LocalDate.parse(v.fecha,DIA).format(MES);
            Resumen r=meses.computeIfAbsent(mes,k->new Resumen());r.ventas++;
            if("ANULADA".equals(v.estado)){r.anuladas++;anuladas++;}
            if("FINALIZADA".equals(v.estado)||v.estado.startsWith("DEVUELTA_")) {
                r.total=r.total.add(v.total);total=total.add(v.total);finalizadas++;
            }
        }
        try(ZipOutputStream zip=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destino)))) {
            put(zip,"[Content_Types].xml","<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                +"<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"><Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/><Default Extension=\"xml\" ContentType=\"application/xml\"/><Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/><Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/><Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/><Override PartName=\"/xl/worksheets/sheet2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/></Types>");
            put(zip,"_rels/.rels","<?xml version=\"1.0\" encoding=\"UTF-8\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/></Relationships>");
            put(zip,"xl/workbook.xml","<?xml version=\"1.0\" encoding=\"UTF-8\"?><workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"><sheets><sheet name=\"Resumen mensual\" sheetId=\"1\" r:id=\"rId1\"/><sheet name=\"Detalle ventas\" sheetId=\"2\" r:id=\"rId2\"/></sheets></workbook>");
            put(zip,"xl/_rels/workbook.xml.rels","<?xml version=\"1.0\" encoding=\"UTF-8\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/><Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet2.xml\"/><Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/></Relationships>");
            put(zip,"xl/styles.xml",styles());
            String rango="PERÍODO: "+desde.format(DIA)+"  AL  "+hasta.format(DIA);
            StringBuilder a=new StringBuilder();start(a,new int[]{22,23,21,27,18,18,18},7,8);
            banner(a,"RESUMEN MENSUAL DE VENTAS",rango,7);
            fila(a,5,ESTILO_CABECERA,"MES","VENTAS","ANULADAS","VENTAS VÁLIDAS","TOTAL VENDIDO","PROMEDIO","ESTADO");
            int n=6;for(Map.Entry<String,Resumen> e:meses.entrySet()) {
                Resumen r=e.getValue();int validas=r.ventas-r.anuladas;
                fila(a,n++,((n%2)==0?ESTILO_PAR:ESTILO_IMPAR),e.getKey(),r.ventas,r.anuladas,validas,dinero(r.total),dinero(validas==0?BigDecimal.ZERO:r.total.divide(BigDecimal.valueOf(validas),2,java.math.RoundingMode.HALF_UP)),"CONSOLIDADO");
            }
            int totalRow=n+1;
            fila(a,totalRow,ESTILO_TOTAL,"TOTAL DEL RANGO",ventas.size(),anuladas,finalizadas,dinero(total),dinero(finalizadas==0?BigDecimal.ZERO:total.divide(BigDecimal.valueOf(finalizadas),2,java.math.RoundingMode.HALF_UP)),"RESUMEN");
            celda(a,1,totalRow+3,"Nota: ventas devueltas incluidas por su valor original; los reintegros aún no se descuentan.",ESTILO_NOTA);
            end(a,5,Math.max(5,n-1),7,"A1:G1","A2:G2","A3:G3","A"+(totalRow+3)+":G"+(totalRow+3));
            put(zip,"xl/worksheets/sheet1.xml",a.toString());
            StringBuilder b=new StringBuilder();start(b,new int[]{19,17,13,35,29,20,22},7,8);
            banner(b,"DETALLE DE VENTAS",rango,7);
            fila(b,5,ESTILO_CABECERA,"N° VENTA","FECHA","HORA","CLIENTE","MEDIO DE PAGO","ESTADO","TOTAL ($)");
            int i=6;for(Venta v:ventas){
                int estilo=(i%2==0?ESTILO_PAR:ESTILO_IMPAR);
                fila(b,i++,estilo,v.numero,v.fecha,v.hora,v.cliente,v.pago,v.estado,dinero(v.total));
            }
            int filaTotal=i+1;
            fila(b,filaTotal,ESTILO_TOTAL,"TOTAL VENDIDO", "", "", "", "", "",dinero(total));
            celda(b,1,filaTotal+3,"El total excluye anuladas; las devoluciones se muestran por el importe original.",ESTILO_NOTA);
            end(b,5,Math.max(5,i-1),7,"A1:G1","A2:G2","A3:G3","A"+(filaTotal+3)+":G"+(filaTotal+3));
            put(zip,"xl/worksheets/sheet2.xml",b.toString());
        }
        return ventas.size();
    }
    private static final int ESTILO_CABECERA=3,ESTILO_PAR=4,ESTILO_IMPAR=5,ESTILO_TOTAL=6,ESTILO_NOTA=7;
    private static final class Moneda {final BigDecimal n;Moneda(BigDecimal n){this.n=n==null?BigDecimal.ZERO:n;}}
    private static Moneda dinero(BigDecimal n){return new Moneda(n);}
    private static String styles(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
            +"<numFmts count=\"1\"><numFmt numFmtId=\"164\" formatCode=\"&quot;$&quot; #,##0.00;[Red](&quot;$&quot; #,##0.00)\"/></numFmts>"
            +"<fonts count=\"5\"><font><sz val=\"11\"/><name val=\"Calibri\"/></font><font><b/><sz val=\"19\"/><color rgb=\"FFFFFFFF\"/><name val=\"Calibri\"/></font><font><b/><sz val=\"11\"/><color rgb=\"FFFFFFFF\"/></font><font><sz val=\"10\"/><color rgb=\"FF47617D\"/></font><font><b/><sz val=\"11\"/><color rgb=\"FF0E3470\"/></font></fonts>"
            +"<fills count=\"6\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF102E63\"/></patternFill></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF1D4F9A\"/></patternFill></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFF0F5FB\"/></patternFill></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFDCE8F6\"/></patternFill></fill></fills>"
            +"<borders count=\"2\"><border><left/><right/><top/><bottom/><diagonal/></border><border><left/><right/><top/><bottom style=\"hair\"><color rgb=\"FFD9E3F0\"/></bottom><diagonal/></border></borders>"
            +"<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>"
            +"<cellXfs count=\"11\">"
            +"<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
            +"<xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"3\" fillId=\"4\" borderId=\"0\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"2\" fillId=\"3\" borderId=\"0\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"0\" fillId=\"4\" borderId=\"1\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"4\" fillId=\"5\" borderId=\"1\" xfId=\"0\" applyAlignment=\"1\"><alignment vertical=\"center\" indent=\"1\"/></xf>"
            +"<xf numFmtId=\"0\" fontId=\"3\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
            +"<xf numFmtId=\"164\" fontId=\"0\" fillId=\"4\" borderId=\"1\" xfId=\"0\" applyNumberFormat=\"1\"/>"
            +"<xf numFmtId=\"164\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyNumberFormat=\"1\"/>"
            +"<xf numFmtId=\"164\" fontId=\"4\" fillId=\"5\" borderId=\"1\" xfId=\"0\" applyNumberFormat=\"1\"/>"
            +"</cellXfs></styleSheet>";
    }
    private static void put(ZipOutputStream zip,String name,String value)throws IOException{
        zip.putNextEntry(new ZipEntry(name));zip.write(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));zip.closeEntry();
    }
    private static String esc(String s){
        if(s==null)return "";
        StringBuilder b=new StringBuilder();for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            if(c=='&')b.append("&amp;");else if(c=='<')b.append("&lt;");else if(c=='>')b.append("&gt;");
            else if(c=='\"')b.append("&quot;");else if(c=='\'')b.append("&apos;");
            else if(c>=32 || c=='\n'||c=='\t'||c=='\r')b.append(c);
        }return b.toString();
    }
    private static void start(StringBuilder b,int[] widths,int columns,int frozen){
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        b.append("<sheetViews><sheetView workbookViewId=\"0\" showGridLines=\"0\"><pane ySplit=\"5\" topLeftCell=\"A6\" activePane=\"bottomLeft\" state=\"frozen\"/></sheetView></sheetViews>");
        b.append("<sheetFormatPr defaultRowHeight=\"17\"/><cols>");
        for(int i=0;i<widths.length;i++)b.append("<col min=\"").append(i+1).append("\" max=\"").append(i+1).append("\" width=\"").append(widths[i]).append("\" customWidth=\"1\"/>");
        b.append("</cols><sheetData>");
    }
    private static void banner(StringBuilder b,String titulo,String rango,int cols){
        fila(b,1,1,"SERENA SOFT  |  GESTIÓN DE VENTAS");
        fila(b,2,1,titulo);
        fila(b,3,2,rango);
    }
    private static void fila(StringBuilder b,int num,int estilo,Object... valores){
        b.append("<row r=\"").append(num).append("\" ht=\"").append(num<=2?32:(num==5?27:23)).append("\" customHeight=\"1\">");
        for(int i=0;i<valores.length;i++)celdaInterna(b,i,num,valores[i],estilo);
        b.append("</row>");
    }
    private static void celda(StringBuilder b,int columna,int fila,Object valor,int estilo){
        b.append("<row r=\"").append(fila).append("\"><c r=\"").append(col(columna-1)).append(fila).append("\" s=\"").append(estilo).append("\" t=\"inlineStr\"><is><t>").append(esc(String.valueOf(valor))).append("</t></is></c></row>");
    }
    private static void celdaInterna(StringBuilder b,int i,int num,Object valor,int estilo){
        int s=valor instanceof Moneda ? (estilo==ESTILO_PAR?8:estilo==ESTILO_IMPAR?9:estilo==ESTILO_TOTAL?10:estilo) : estilo;
        b.append("<c r=\"").append(col(i)).append(num).append("\" s=\"").append(s).append("\"");
        if(valor instanceof Moneda){b.append("><v>").append(((Moneda)valor).n.toPlainString()).append("</v></c>");}
        else if(valor instanceof Number){b.append("><v>").append(valor).append("</v></c>");}
        else {b.append(" t=\"inlineStr\"><is><t>").append(esc(String.valueOf(valor))).append("</t></is></c>");}
    }
    private static void end(StringBuilder b,int filtroInicio,int filtroFin,int cols,String... merges){
        b.append("</sheetData><autoFilter ref=\"A").append(filtroInicio).append(":").append(col(cols-1)).append(filtroFin).append("\"/>");
        b.append("<mergeCells count=\"").append(merges.length).append("\">");for(String m:merges)b.append("<mergeCell ref=\"").append(m).append("\"/>");
        b.append("</mergeCells><pageMargins left=\"0.25\" right=\"0.25\" top=\"0.5\" bottom=\"0.5\" header=\"0.2\" footer=\"0.2\"/></worksheet>");
    }
    private static String col(int i){StringBuilder b=new StringBuilder();do{b.insert(0,(char)('A'+i%26));i=i/26-1;}while(i>=0);return b.toString();}
}
