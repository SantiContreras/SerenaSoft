package Dialogos;

import services.CajaService;
import com.toedter.calendar.JDateChooser;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.table.*;

/** Auditoría de cajas, sin escritura en base de datos. */
public final class DialogoAuditoriaCajas extends JDialog {
    private final CajaService servicio = new CajaService();
    private final JDateChooser desde = new JDateChooser(), hasta = new JDateChooser();
    private final JTextField cajero = new JTextField("0",4);
    private final JComboBox<String> estado = new JComboBox<>(new String[]{"TODAS","ABIERTA","CERRADA"});
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Caja","Cajero","Apertura","Cierre","Estado","Fondo inicial","Esperado","Contado","Diferencia"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla = new JTable(modelo);
    private final List<CajaService.SesionAuditada> resultados = new ArrayList<>();
    private final Color azul = new Color(19,50,105), fondo = new Color(244,247,252);
    private final DecimalFormat moneda = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("es","AR")));
    private final SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");

    public DialogoAuditoriaCajas(Window owner){
        super(owner,"Serena Soft | Historial y auditoría de cajas",ModalityType.APPLICATION_MODAL);
        setSize(1170,660);setMinimumSize(new Dimension(950,540));setLocationRelativeTo(owner);
        JPanel root = new JPanel(new BorderLayout(0,12));root.setBackground(fondo);root.setBorder(BorderFactory.createEmptyBorder(16,18,16,18));setContentPane(root);
        JPanel top=new JPanel(new BorderLayout());top.setBackground(azul);top.setBorder(BorderFactory.createEmptyBorder(17,20,17,20));
        JLabel titulo=new JLabel("HISTORIAL Y AUDITORÍA DE CAJAS");titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,21));top.add(titulo);root.add(top,BorderLayout.NORTH);
        JPanel central=new JPanel(new BorderLayout(0,12));central.setOpaque(false);
        JPanel filtros=new JPanel(new FlowLayout(FlowLayout.LEFT,12,9));filtros.setBackground(Color.WHITE);filtros.setBorder(BorderFactory.createLineBorder(new Color(212,223,238)));
        Calendar inicio=Calendar.getInstance();inicio.add(Calendar.DAY_OF_MONTH,-30);
        desde.setDate(inicio.getTime());hasta.setDate(new java.util.Date());
        for(JDateChooser d:new JDateChooser[]{desde,hasta}){d.setDateFormatString("dd/MM/yyyy");d.setPreferredSize(new Dimension(150,36));d.setFont(new Font("Segoe UI",Font.PLAIN,13));}
        filtros.add(etiqueta("DESDE"));filtros.add(desde);filtros.add(etiqueta("HASTA"));filtros.add(hasta);
        filtros.add(etiqueta("ID CAJERO"));cajero.setPreferredSize(new Dimension(55,36));filtros.add(cajero);
        filtros.add(etiqueta("ESTADO"));estado.setPreferredSize(new Dimension(112,36));filtros.add(estado);
        JButton consultar=boton("CONSULTAR",new Color(27,82,165),115);consultar.addActionListener(e->consultar());filtros.add(consultar);
        central.add(filtros,BorderLayout.NORTH);
        tabla.setRowHeight(35);tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowVerticalLines(false);tabla.setGridColor(new Color(221,229,239));tabla.setSelectionBackground(new Color(209,226,250));tabla.setSelectionForeground(azul);
        tabla.setBackground(Color.WHITE);tabla.setFillsViewportHeight(true);tabla.setAutoCreateRowSorter(true);
        JTableHeader h=tabla.getTableHeader();h.setFont(new Font("Segoe UI",Font.BOLD,12));h.setPreferredSize(new Dimension(0,43));h.setReorderingAllowed(false);
        h.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object value,boolean selected,boolean focus,int row,int column){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,value,selected,focus,row,column);
                l.setOpaque(true);l.setBackground(azul);l.setForeground(Color.WHITE);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));l.setHorizontalAlignment(SwingConstants.LEFT);return l;
            }
        });
        tabla.getColumnModel().getColumn(0).setPreferredWidth(52);tabla.getColumnModel().getColumn(1).setPreferredWidth(155);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(155);tabla.getColumnModel().getColumn(3).setPreferredWidth(155);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);
        DefaultTableCellRenderer valores=new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int r,int c){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,sel,focus,r,c);l.setHorizontalAlignment(SwingConstants.RIGHT);l.setBorder(BorderFactory.createEmptyBorder(0,3,0,10));
                if(!sel && c==8 && v!=null && v.toString().startsWith("-$"))l.setForeground(new Color(190,39,47));else if(!sel)l.setForeground(azul);return l;
            }
        };
        for(int i=5;i<=8;i++)tabla.getColumnModel().getColumn(i).setCellRenderer(valores);
        JScrollPane scroll=new JScrollPane(tabla);scroll.setBorder(BorderFactory.createLineBorder(new Color(212,223,238)));central.add(scroll,BorderLayout.CENTER);root.add(central,BorderLayout.CENTER);
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,5));footer.setOpaque(false);
        JButton detalle=boton("VER DETALLE",new Color(27,82,165),145);detalle.addActionListener(e->detalle());footer.add(detalle);
        JButton pdf=boton("EXPORTAR PDF",new Color(22,105,87),145);pdf.addActionListener(e->exportarPDF());footer.add(pdf);
        JButton excel=boton("EXPORTAR EXCEL",new Color(18,125,77),150);excel.addActionListener(e->exportarExcel());footer.add(excel);
        JButton cerrar=boton("CERRAR",new Color(95,105,121),105);cerrar.addActionListener(e->dispose());footer.add(cerrar);
        root.add(footer,BorderLayout.SOUTH);consultar();
    }
    private JLabel etiqueta(String t){JLabel l=new JLabel(t);l.setForeground(azul);l.setFont(new Font("Segoe UI",Font.BOLD,11));return l;}
    private JButton boton(String texto,Color color,int ancho){JButton b=new JButton(texto){@Override protected void paintComponent(Graphics g){Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);x.setColor(getModel().isPressed()?color.darker():color);x.fillRoundRect(0,0,getWidth(),getHeight(),10,10);x.dispose();super.paintComponent(g);}};b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(Color.WHITE);b.setContentAreaFilled(false);b.setOpaque(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setPreferredSize(new Dimension(ancho,40));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private String dinero(BigDecimal n){return "$ "+moneda.format(n==null?BigDecimal.ZERO:n);}
    private void consultar(){try{
        if(desde.getDate()==null||hasta.getDate()==null)throw new IllegalArgumentException("Seleccioná ambas fechas en el calendario.");
        java.sql.Date a=new java.sql.Date(desde.getDate().getTime()),b=new java.sql.Date(hasta.getDate().getTime());
        if(a.after(b))throw new IllegalArgumentException("La fecha DESDE no puede ser posterior a HASTA.");
        int id=Integer.parseInt(cajero.getText().trim());if(id<0)throw new IllegalArgumentException("El ID del cajero debe ser cero o positivo.");
        List<CajaService.SesionAuditada> lista=servicio.historial(a,b,id,(String)estado.getSelectedItem());resultados.clear();resultados.addAll(lista);modelo.setRowCount(0);
        for(CajaService.SesionAuditada s:lista)modelo.addRow(new Object[]{s.id,s.cajero,s.apertura,s.cierre==null?"—":s.cierre,s.estado,dinero(s.inicial),"CERRADA".equals(s.estado)?dinero(s.esperado):"—","CERRADA".equals(s.estado)?dinero(s.contado):"—","CERRADA".equals(s.estado)?dinero(s.diferencia):"—"});
    }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Consulta de cajas",JOptionPane.ERROR_MESSAGE);}}
    private CajaService.SesionAuditada seleccion(){int fila=tabla.getSelectedRow();return fila<0?null:resultados.get(tabla.convertRowIndexToModel(fila));}
    private void detalle(){CajaService.SesionAuditada s=seleccion();if(s==null){JOptionPane.showMessageDialog(this,"Seleccioná una caja en la tabla.");return;}try{
        StringBuilder b=new StringBuilder("CAJA #"+s.id+"\nCajero: "+s.cajero+"\nApertura: "+s.apertura+"\nCierre: "+(s.cierre==null?"Abierta":s.cierre)+"\nFondo: "+dinero(s.inicial));
        if("CERRADA".equals(s.estado))b.append("\nEsperado: ").append(dinero(s.esperado)).append("\nContado: ").append(dinero(s.contado)).append("\nDiferencia: ").append(dinero(s.diferencia)).append("\nObservación cierre: ").append(s.observacion==null?"":s.observacion);
        b.append("\n\nARQUEOS REGISTRADOS\n");List<CajaService.ArqueoAuditado> arqueos=servicio.arqueosHistoricos(s.id);if(arqueos.isEmpty())b.append("Sin arqueos intermedios\n");
        for(CajaService.ArqueoAuditado a:arqueos)b.append(a.fecha).append(" | ").append(a.usuario).append("\nEsperado: ").append(dinero(a.esperado)).append(" | Contado: ").append(dinero(a.contado)).append(" | Diferencia: ").append(dinero(a.diferencia)).append("\nObservación: ").append(a.observacion==null?"":a.observacion).append("\n\n");
        JTextArea area=new JTextArea(b.toString(),20,65);area.setEditable(false);area.setFont(new Font("Consolas",Font.PLAIN,13));area.setCaretPosition(0);JOptionPane.showMessageDialog(this,new JScrollPane(area),"Detalle de caja #"+s.id,JOptionPane.PLAIN_MESSAGE);
    }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}}
    private List<CajaService.SesionAuditada> exportables(){
        CajaService.SesionAuditada s=seleccion();Object[] opciones={"TODAS LAS CAJAS FILTRADAS","SOLO CAJA SELECCIONADA","CANCELAR"};
        int o=JOptionPane.showOptionDialog(this,"¿Qué cierres querés exportar?\nSe incluyen únicamente sesiones CERRADAS.","Alcance del reporte",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
        if(o==2||o==JOptionPane.CLOSED_OPTION)return null;
        List<CajaService.SesionAuditada> out=new ArrayList<>();
        if(o==1 && s==null){JOptionPane.showMessageDialog(this,"Seleccioná primero una caja.");return null;}
        for(CajaService.SesionAuditada x:resultados)if("CERRADA".equals(x.estado)&&(o==0||x==s))out.add(x);
        if(out.isEmpty()){JOptionPane.showMessageDialog(this,"No hay cajas cerradas para exportar.");return null;}return out;
    }
    private File archivo(String ext){JFileChooser f=new JFileChooser();f.setSelectedFile(new File("SerenaSoft_Auditoria_Cajas_"+new SimpleDateFormat("yyyyMMdd").format(new java.util.Date())+"."+ext));return f.showSaveDialog(this)==JFileChooser.APPROVE_OPTION?f.getSelectedFile():null;}
    private String[] columnas={"Caja","Cajero","Apertura","Cierre","Estado","Inicial","Esperado","Contado","Diferencia","Observación"};
    private String[] datos(CajaService.SesionAuditada s){return new String[]{String.valueOf(s.id),s.cajero,s.apertura,s.cierre,s.estado,dinero(s.inicial),dinero(s.esperado),dinero(s.contado),dinero(s.diferencia),s.observacion==null?"":s.observacion};}
    private void exportarPDF(){List<CajaService.SesionAuditada> lista=exportables();if(lista==null)return;File f=archivo("pdf");if(f==null)return;
        try{Document doc=new Document(PageSize.A4.rotate(),30,30,35,35);PdfWriter.getInstance(doc,new FileOutputStream(f));doc.open();
            com.lowagie.text.Font blanco=new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,16,com.lowagie.text.Font.BOLD,Color.WHITE), chico=new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,8,com.lowagie.text.Font.NORMAL), cab=new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,8,com.lowagie.text.Font.BOLD,Color.WHITE);
            PdfPTable banner=new PdfPTable(1);banner.setWidthPercentage(100);PdfPCell head=new PdfPCell(new Phrase("SERENA SOFT  |  AUDITORÍA DE CIERRES DE CAJA",blanco));head.setBackgroundColor(azul);head.setPadding(15);head.setBorder(0);banner.addCell(head);doc.add(banner);
            doc.add(new Paragraph("Período: "+fecha.format(desde.getDate())+" al "+fecha.format(hasta.getDate())+"  |  Cierres: "+lista.size(),new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,10,com.lowagie.text.Font.NORMAL)));doc.add(new Paragraph(" "));
            PdfPTable t=new PdfPTable(new float[]{.6f,1.7f,1.35f,1.35f,.8f,1.2f,1.2f,1.2f,1.2f,2.1f});t.setWidthPercentage(100);t.setHeaderRows(1);
            for(String col:columnas){PdfPCell c=new PdfPCell(new Phrase(col,cab));c.setBackgroundColor(azul);c.setPadding(6);t.addCell(c);}
            int n=0;for(CajaService.SesionAuditada s:lista){for(String v:datos(s)){PdfPCell c=new PdfPCell(new Phrase(v==null?"":v,chico));c.setPadding(5);if(n%2==0)c.setBackgroundColor(new Color(235,242,251));t.addCell(c);}n++;}
            doc.add(t);doc.add(new Paragraph("Documento de consulta. Los importes reflejan registros guardados en Serena Soft.",new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,8,com.lowagie.text.Font.ITALIC)));doc.close();
            JOptionPane.showMessageDialog(this,"PDF generado:\n"+f.getAbsolutePath());
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error al generar PDF",JOptionPane.ERROR_MESSAGE);}}
    private void exportarExcel(){List<CajaService.SesionAuditada> lista=exportables();if(lista==null)return;File f=archivo("xlsx");if(f==null)return;
        try(ZipOutputStream z=new ZipOutputStream(new FileOutputStream(f))){
            zip(z,"[Content_Types].xml","<?xml version=\"1.0\" encoding=\"UTF-8\"?><Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"><Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/><Default Extension=\"xml\" ContentType=\"application/xml\"/><Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/><Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/><Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/></Types>");
            zip(z,"_rels/.rels","<?xml version=\"1.0\" encoding=\"UTF-8\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/></Relationships>");
            zip(z,"xl/workbook.xml","<?xml version=\"1.0\" encoding=\"UTF-8\"?><workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"><sheets><sheet name=\"Cierres de caja\" sheetId=\"1\" r:id=\"rId1\"/></sheets></workbook>");
            zip(z,"xl/_rels/workbook.xml.rels","<?xml version=\"1.0\" encoding=\"UTF-8\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/><Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/></Relationships>");
            zip(z,"xl/styles.xml","<?xml version=\"1.0\" encoding=\"UTF-8\"?><styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"><fonts count=\"2\"><font/><font><b/><color rgb=\"FFFFFFFF\"/></font></fonts><fills count=\"3\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF133269\"/><bgColor indexed=\"64\"/></patternFill></fill></fills><borders count=\"1\"><border/></borders><cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs><cellXfs count=\"2\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/><xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\"/></cellXfs></styleSheet>");
            StringBuilder b=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"><sheetViews><sheetView workbookViewId=\"0\"><pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/></sheetView></sheetViews><cols><col min=\"1\" max=\"1\" width=\"10\" customWidth=\"1\"/><col min=\"2\" max=\"5\" width=\"23\" customWidth=\"1\"/><col min=\"6\" max=\"9\" width=\"19\" customWidth=\"1\"/><col min=\"10\" max=\"10\" width=\"65\" customWidth=\"1\"/></cols><sheetData>");
            b.append("<row r=\"1\">");for(int i=0;i<columnas.length;i++)celda(b,1,i,columnas[i],true);b.append("</row>");
            int r=2;for(CajaService.SesionAuditada s:lista){b.append("<row r=\"").append(r).append("\">");String[] d=datos(s);for(int i=0;i<d.length;i++)celda(b,r,i,d[i],false);b.append("</row>");r++;}
            b.append("</sheetData><autoFilter ref=\"A1:J").append(r-1).append("\"/></worksheet>");zip(z,"xl/worksheets/sheet1.xml",b.toString());
            JOptionPane.showMessageDialog(this,"Excel generado:\n"+f.getAbsolutePath());
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error al generar Excel",JOptionPane.ERROR_MESSAGE);}}
    private void zip(ZipOutputStream z,String ruta,String contenido)throws IOException{z.putNextEntry(new ZipEntry(ruta));z.write(contenido.getBytes("UTF-8"));z.closeEntry();}
    private void celda(StringBuilder b,int r,int i,String valor,boolean cab){String col=String.valueOf((char)('A'+i));b.append("<c r=\"").append(col).append(r).append("\" t=\"inlineStr\"");if(cab)b.append(" s=\"1\"");b.append("><is><t>").append(xml(valor==null?"":valor)).append("</t></is></c>");}
    private String xml(String s){return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&apos;");}
}
