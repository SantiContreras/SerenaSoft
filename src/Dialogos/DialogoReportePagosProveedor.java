package Dialogos;

import Configuracion.conexion;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Reporte de pagos reales por proveedor; consulta MariaDB por rango inclusivo. */
public class DialogoReportePagosProveedor extends JDialog {
    private static final Color NAVY=new Color(15,48,105), BLUE=new Color(28,76,150), PALE=new Color(237,244,254);
    private static final DateTimeFormatter DATE=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MONEY=NumberFormat.getCurrencyInstance(new Locale("es","AR"));
    private final int proveedorId;
    private LocalDate desde=LocalDate.now().withDayOfMonth(1), hasta=LocalDate.now();
    private final JButton inicio=new JButton(), fin=new JButton();
    private final JLabel aviso=new JLabel("Seleccioná el período que querés imprimir");
    private String nombre="", cuit="", direccion="", localidad="", provincia="", telefono="", email="", contacto="", iva="";
    private final List<Pago> pagos=new ArrayList<>();
    private BigDecimal total=BigDecimal.ZERO;

    private static class Pago {
        LocalDate fecha; String factura, medio, referencia; BigDecimal importe;
        Pago(LocalDate f,String n,String m,String r,BigDecimal i){fecha=f;factura=n;medio=m;referencia=r;importe=i;}
    }
    public DialogoReportePagosProveedor(Window owner,int proveedorId){
        super(owner,"Reporte de pagos del proveedor",ModalityType.APPLICATION_MODAL);
        this.proveedorId=proveedorId;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760,395);setMinimumSize(new Dimension(690,370));setLocationRelativeTo(owner);
        JPanel root=new JPanel(new BorderLayout());root.setBackground(new Color(247,249,253));setContentPane(root);
        JPanel head=new JPanel(new BorderLayout());head.setBackground(NAVY);head.setBorder(new EmptyBorder(17,25,17,25));
        JLabel h=new JLabel("REPORTE DE PAGOS A PROVEEDOR");h.setForeground(Color.WHITE);h.setFont(new Font("Segoe UI",Font.BOLD,22));head.add(h);
        root.add(head,BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,18));center.setOpaque(false);center.setBorder(new EmptyBorder(17,25,14,25));root.add(center,BorderLayout.CENTER);
        JLabel instr=new JLabel("Elegí las fechas. Podés navegar por meses y seleccionar un día en el calendario.");instr.setFont(new Font("Segoe UI",Font.PLAIN,14));instr.setForeground(NAVY);center.add(instr,BorderLayout.NORTH);
        JPanel fechas=new JPanel(new GridLayout(1,2,12,0));fechas.setOpaque(false);
        fechas.add(campoFecha("DESDE",inicio,true));fechas.add(campoFecha("HASTA",fin,false));center.add(fechas,BorderLayout.CENTER);
        aviso.setFont(new Font("Segoe UI",Font.PLAIN,12));aviso.setForeground(BLUE);center.add(aviso,BorderLayout.SOUTH);
        JPanel foot=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,12));foot.setBackground(Color.WHITE);
        JButton cancelar=boton("CANCELAR",new Color(83,96,116));cancelar.addActionListener(e->dispose());
        JButton generar=boton("GENERAR Y VER REPORTE",BLUE);generar.addActionListener(e->generar());
        foot.add(cancelar);foot.add(generar);root.add(foot,BorderLayout.SOUTH);
        actualizar();
    }
    private JPanel campoFecha(String titulo,JButton b,boolean first){
        JPanel p=new JPanel(new BorderLayout(0,8));p.setBackground(PALE);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(201,219,245)),new EmptyBorder(12,14,12,14)));
        JLabel l=new JLabel(titulo);l.setFont(new Font("Segoe UI",Font.BOLD,15));l.setForeground(NAVY);p.add(l,BorderLayout.NORTH);
        b.setFont(new Font("Segoe UI",Font.BOLD,17));b.setForeground(BLUE);b.setBackground(Color.WHITE);b.setFocusPainted(false);b.setPreferredSize(new Dimension(270,55));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e->{LocalDate elegido=calendario(first?desde:hasta);if(elegido!=null){if(first)desde=elegido;else hasta=elegido;actualizar();}});
        p.add(b,BorderLayout.CENTER);return p;
    }
    private void actualizar(){inicio.setText(desde.format(DATE)+"   |   ELEGIR FECHA");fin.setText(hasta.format(DATE)+"   |   ELEGIR FECHA");}
    private static JButton boton(String t,Color c){
        JButton b=new JButton(t){
            @Override protected void paintComponent(Graphics g){
                Graphics2D a=(Graphics2D)g.create();
                a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                a.setColor(isEnabled()? (getModel().isRollover()?c.brighter():c):new Color(166,176,191));
                a.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                a.dispose();super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);b.setOpaque(false);b.setBorderPainted(false);
        b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(t.length()>15?220:125,42));return b;
    }
    private LocalDate calendario(LocalDate inicial){
        final JDialog d=new JDialog(this,"Seleccionar fecha",true);d.setSize(440,445);d.setLocationRelativeTo(this);
        JPanel base=new JPanel(new BorderLayout(0,10));base.setBackground(Color.WHITE);base.setBorder(new EmptyBorder(14,16,16,16));d.setContentPane(base);
        final YearMonth[] mes={YearMonth.from(inicial)};final LocalDate[] seleccionado={null};
        JPanel cab=new JPanel(new BorderLayout());cab.setOpaque(false);JLabel titulo=new JLabel("",SwingConstants.CENTER);titulo.setFont(new Font("Segoe UI",Font.BOLD,18));titulo.setForeground(NAVY);
        JButton ant=new JButton("<"),sig=new JButton(">");cab.add(ant,BorderLayout.WEST);cab.add(titulo,BorderLayout.CENTER);cab.add(sig,BorderLayout.EAST);base.add(cab,BorderLayout.NORTH);
        JPanel grid=new JPanel(new GridLayout(7,7,4,4));grid.setBackground(Color.WHITE);base.add(grid,BorderLayout.CENTER);
        Runnable render=()->{
            titulo.setText(mes[0].getMonth().getDisplayName(java.time.format.TextStyle.FULL,new Locale("es","AR")).toUpperCase()+" "+mes[0].getYear());grid.removeAll();
            for(String dia:new String[]{"Lu","Ma","Mi","Ju","Vi","Sa","Do"}){JLabel l=new JLabel(dia,SwingConstants.CENTER);l.setForeground(NAVY);l.setFont(new Font("Segoe UI",Font.BOLD,12));grid.add(l);}
            int offset=mes[0].atDay(1).getDayOfWeek().getValue()-1;
            for(int i=0;i<42;i++){int num=i-offset+1;if(num<1||num>mes[0].lengthOfMonth()){grid.add(new JLabel(""));continue;}
                LocalDate fecha=mes[0].atDay(num);JButton b=new JButton(String.valueOf(num));b.setFont(new Font("Segoe UI",Font.PLAIN,14));b.setFocusPainted(false);b.setBorderPainted(false);
                boolean activo=fecha.equals(inicial);b.setBackground(activo?BLUE:Color.WHITE);b.setForeground(activo?Color.WHITE:NAVY);
                b.addActionListener(e->{seleccionado[0]=fecha;d.dispose();});grid.add(b);
            }grid.revalidate();grid.repaint();
        };
        ant.addActionListener(e->{mes[0]=mes[0].minusMonths(1);render.run();});sig.addActionListener(e->{mes[0]=mes[0].plusMonths(1);render.run();});render.run();
        JButton hoy=boton("HOY",BLUE);hoy.addActionListener(e->{seleccionado[0]=LocalDate.now();d.dispose();});JPanel sur=new JPanel(new FlowLayout(FlowLayout.RIGHT));sur.setOpaque(false);sur.add(hoy);base.add(sur,BorderLayout.SOUTH);
        d.setVisible(true);return seleccionado[0];
    }
    private void generar(){
        if(desde.isAfter(hasta)){JOptionPane.showMessageDialog(this,"La fecha DESDE no puede ser posterior a HASTA.");return;}
        pagos.clear();total=BigDecimal.ZERO;
        try(Connection c=conexion.getConexion()){
            try(PreparedStatement p=c.prepareStatement("SELECT razon_social,cuit,direccion,localidad,provincia,telefono,email,persona_contacto,condicion_iva FROM proveedor WHERE id_proveedor=?")){
                p.setInt(1,proveedorId);try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("Proveedor inexistente");
                    nombre=s(r.getString(1));cuit=s(r.getString(2));direccion=s(r.getString(3));localidad=s(r.getString(4));provincia=s(r.getString(5));telefono=s(r.getString(6));email=s(r.getString(7));contacto=s(r.getString(8));iva=s(r.getString(9));}}
            String sql="SELECT cp.fecha,cp.id_compra,COALESCE(c.numero_comprobante,''),mp.nombre,COALESCE(cp.referencia,''),cp.monto FROM compra_pago cp JOIN compra c ON c.id_compra=cp.id_compra JOIN medio_pago mp ON mp.id_medio_pago=cp.id_medio_pago WHERE c.id_proveedor=? AND cp.fecha>=? AND cp.fecha<? ORDER BY cp.fecha,cp.id_pago";
            try(PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,proveedorId);p.setDate(2,java.sql.Date.valueOf(desde));p.setDate(3,java.sql.Date.valueOf(hasta.plusDays(1)));
                try(ResultSet r=p.executeQuery()){while(r.next()){
                    String n=r.getString(3);String fact="Compra #"+r.getLong(2)+(n.isBlank()?"":" / "+n);
                    BigDecimal monto=r.getBigDecimal(6);pagos.add(new Pago(r.getTimestamp(1).toLocalDateTime().toLocalDate(),fact,s(r.getString(4)),s(r.getString(5)),monto));total=total.add(monto);
                }}}
            new VistaPrevia(this).setVisible(true);
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"No se pudo generar el reporte:\n"+ex.getMessage(),"Reporte",JOptionPane.ERROR_MESSAGE);}
    }
    private static String s(String s){return s==null?"":s;}
    private class VistaPrevia extends JDialog {
        VistaPrevia(Window parent){super(parent,"Vista previa - Reporte de pagos",ModalityType.APPLICATION_MODAL);
            setSize(950,690);setLocationRelativeTo(parent);setLayout(new BorderLayout());
            JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,8));acciones.setBackground(Color.WHITE);
            JButton imprimir=boton("IMPRIMIR",BLUE),cerrar=boton("CERRAR",new Color(83,96,116));
            imprimir.addActionListener(e->imprimir());cerrar.addActionListener(e->dispose());acciones.add(imprimir);acciones.add(cerrar);add(acciones,BorderLayout.NORTH);
            JPanel paginas=new JPanel();paginas.setLayout(new BoxLayout(paginas,BoxLayout.Y_AXIS));paginas.setBackground(new Color(225,231,240));
            int n=Math.max(1,(pagos.size()+17)/18);
            for(int i=0;i<n;i++){final int pagina=i;JPanel hoja=new JPanel(){protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g.create();g2.scale(getWidth()/595.0,getHeight()/842.0);dibujar(g2,pagina,n);g2.dispose();}};
                hoja.setBackground(Color.WHITE);hoja.setPreferredSize(new Dimension(595,842));hoja.setMaximumSize(new Dimension(595,842));hoja.setMinimumSize(new Dimension(595,842));hoja.setBorder(BorderFactory.createLineBorder(new Color(190,200,214)));paginas.add(Box.createVerticalStrut(12));paginas.add(hoja);
            }JScrollPane sc=new JScrollPane(paginas);sc.getVerticalScrollBar().setUnitIncrement(22);add(sc,BorderLayout.CENTER);
        }
        private void imprimir(){PrinterJob job=PrinterJob.getPrinterJob();job.setJobName("Serena Soft - Pagos "+nombre);
            int paginas=Math.max(1,(pagos.size()+17)/18);
            job.setPrintable((g,format,index)->{if(index>=paginas)return Printable.NO_SUCH_PAGE;
                Graphics2D a=(Graphics2D)g.create();double scale=Math.min(format.getImageableWidth()/595.0,format.getImageableHeight()/842.0);
                a.translate(format.getImageableX(),format.getImageableY());a.scale(scale,scale);dibujar(a,index,paginas);a.dispose();return Printable.PAGE_EXISTS;});
            if(job.printDialog()){try{job.print();}catch(PrinterException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Impresión",JOptionPane.ERROR_MESSAGE);}}
        }
    }
    private void dibujar(Graphics2D g,int pagina,int paginas){
        g.setColor(Color.WHITE);g.fillRect(0,0,595,842);
        g.setColor(NAVY);g.fillRoundRect(24,24,547,96,10,10);
        texto(g,"SERENA SOFT",42,60,21,Font.BOLD,Color.WHITE);
        texto(g,"Sistema de Gestión Comercial",42,80,10,Font.PLAIN,Color.WHITE);
        texto(g,"REPORTE DE PAGOS",300,57,19,Font.BOLD,Color.WHITE);
        texto(g,"Período: "+desde.format(DATE)+" al "+hasta.format(DATE),300,79,11,Font.PLAIN,Color.WHITE);
        texto(g,"PROVEEDOR  "+nombre.toUpperCase(),35,155,16,Font.BOLD,NAVY);
        texto(g,"CUIT: "+cuit+"   |   Condición IVA: "+iva,35,178,10,Font.PLAIN,NAVY);
        texto(g,"Dirección: "+direccion+" - "+localidad+", "+provincia,35,196,10,Font.PLAIN,NAVY);
        texto(g,"Teléfono: "+telefono+"   |   Contacto: "+contacto,35,214,10,Font.PLAIN,NAVY);
        texto(g,"Email: "+email,35,232,10,Font.PLAIN,NAVY);
        g.setColor(NAVY);g.fillRect(25,254,545,36);
        String[] heads={"FECHA","COMPRA / FACTURA","MEDIO","REFERENCIA","IMPORTE"};int[] xs={34,103,278,375,475};
        for(int i=0;i<heads.length;i++)texto(g,heads[i],xs[i],276,9,Font.BOLD,Color.WHITE);
        int inicio=pagina*18,fin=Math.min(pagos.size(),inicio+18),y=290;
        for(int i=inicio;i<fin;i++){Pago p=pagos.get(i);if(i%2==0){g.setColor(PALE);g.fillRect(25,y,545,25);}g.setColor(new Color(210,220,235));g.drawLine(25,y+25,570,y+25);
            texto(g,p.fecha.format(DATE),34,y+17,9,Font.PLAIN,NAVY);textoCortado(g,p.factura,103,y+17,170,9);textoCortado(g,p.medio,278,y+17,95,9);textoCortado(g,p.referencia,375,y+17,92,9);
            textoDerecha(g,MONEY.format(p.importe),562,y+17,9,Font.BOLD,NAVY);y+=25;}
        if(pagos.isEmpty()){texto(g,"No se registraron pagos en el período seleccionado.",42,330,12,Font.PLAIN,NAVY);}
        if(pagina==paginas-1){g.setColor(NAVY);g.fillRoundRect(25,770,545,39,7,7);texto(g,"TOTAL PAGADO EN EL PERÍODO",39,795,12,Font.BOLD,Color.WHITE);textoDerecha(g,MONEY.format(total),557,795,15,Font.BOLD,Color.WHITE);}
        texto(g,"Emitido: "+LocalDate.now().format(DATE)+"  |  Página "+(pagina+1)+" de "+paginas,30,827,9,Font.PLAIN,new Color(95,110,130));
    }
    private static void texto(Graphics2D g,String s,int x,int y,int size,int style,Color c){g.setFont(new Font("SansSerif",style,size));g.setColor(c);g.drawString(s==null?"":s,x,y);}
    private static void textoDerecha(Graphics2D g,String s,int right,int y,int size,int style,Color c){g.setFont(new Font("SansSerif",style,size));texto(g,s,right-g.getFontMetrics().stringWidth(s),y,size,style,c);}
    private static void textoCortado(Graphics2D g,String s,int x,int y,int max,int size){g.setFont(new Font("SansSerif",Font.PLAIN,size));String t=s;while(t.length()>3&&g.getFontMetrics().stringWidth(t)>max)t=t.substring(0,t.length()-2)+"…";texto(g,t,x,y,size,Font.PLAIN,NAVY);}
}
