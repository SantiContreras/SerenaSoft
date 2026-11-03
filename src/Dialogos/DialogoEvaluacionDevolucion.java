package Dialogos;

import services.DevolucionVentaService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.List;

/** Ventana visual de evaluación. Mantiene la API del historial y NO modifica transacciones SQL. */
public final class DialogoEvaluacionDevolucion extends JDialog {
    private static final Color NAVY=new Color(17,46,96), BLUE=new Color(32,91,179), PALE=new Color(240,245,252), INK=new Color(23,44,78), MUTED=new Color(94,111,135), GREEN=new Color(12,143,71), RED=new Color(201,48,57), AMBER=new Color(191,118,12);
    private final List<DevolucionVentaService.Linea> lineas;
    private final boolean anulacion;
    private final DefaultListModel<String> modelo=new DefaultListModel<>();
    private final JList<String> productos=new JList<>(modelo);
    private final JTextField cantidad=new JTextField();
    private final JTextArea evaluacion=new JTextArea(3,35), motivo=new JTextArea(2,50);
    private final JLabel nombre=new JLabel(), datos=new JLabel(), resumen=new JLabel(), aviso=new JLabel(" ");
    private final JLabel reintegro=new JLabel();
    private final JButton[] opciones=new JButton[4];
    private JButton guardar, confirmar;
    private int indice=0;
    private boolean cargando=false, confirmado=false;
    private String motivoFinal;
    private DevolucionVentaService.Destino destinoActual=DevolucionVentaService.Destino.REVISION;
    private final NumberFormat dinero=NumberFormat.getCurrencyInstance(new Locale("es","AR"));

    public DialogoEvaluacionDevolucion(Window owner,List<DevolucionVentaService.Linea> lineas,boolean anulacion){
        super(owner,anulacion?"Serena Soft | Anulación total":"Serena Soft | Devolución de mercadería",ModalityType.APPLICATION_MODAL);
        if(lineas==null||lineas.isEmpty())throw new IllegalArgumentException("No hay productos para evaluar");
        this.lineas=lineas;this.anulacion=anulacion;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);setMinimumSize(new Dimension(800,565));setSize(990,635);
        JPanel root=new JPanel(new BorderLayout());root.setBackground(PALE);setContentPane(root);
        root.add(encabezado(),BorderLayout.NORTH);
        JPanel main=new JPanel(new BorderLayout(9,0));main.setOpaque(false);main.setBorder(new EmptyBorder(10,12,7,12));
        main.add(lateral(),BorderLayout.WEST);main.add(editor(),BorderLayout.CENTER);
        root.add(main,BorderLayout.CENTER);root.add(pie(),BorderLayout.SOUTH);
        for(int i=0;i<lineas.size();i++)modelo.addElement((i+1)+".   "+lineas.get(i).producto);
        productos.addListSelectionListener(e->{if(cargando||e.getValueIsAdjusting())return;int n=productos.getSelectedIndex();if(n<0||n==indice)return;if(!guardarActual(true)){cargando=true;productos.setSelectedIndex(indice);cargando=false;return;}indice=n;cargar();});
        guardar.addActionListener(e->{if(!guardarActual(true))return;if(indice+1<lineas.size()){indice++;cargando=true;productos.setSelectedIndex(indice);cargando=false;cargar();}else{aviso.setForeground(GREEN);aviso.setText("Evaluación guardada. Ya podés confirmar la operación.");}});
        confirmar.addActionListener(e->confirmarOperacion());
        productos.setSelectedIndex(0);cargar();actualizarResumen();
        setLocationRelativeTo(owner);
    }
    private JPanel encabezado(){
        JPanel p=new JPanel(new BorderLayout(12,0));p.setBackground(NAVY);p.setBorder(new EmptyBorder(11,19,11,19));
        JLabel logo=new JLabel("S",SwingConstants.CENTER);logo.setFont(new Font("Dialog",Font.BOLD,20));logo.setForeground(new Color(154,203,255));logo.setPreferredSize(new Dimension(38,42));p.add(logo,BorderLayout.WEST);
        JPanel t=new JPanel(new GridLayout(2,1,0,4));t.setOpaque(false);
        JLabel titulo=new JLabel(anulacion?"ANULACIÓN TOTAL DE VENTA":"DEVOLUCIÓN DE MERCADERÍA");titulo.setFont(new Font("Segoe UI",Font.BOLD,20));titulo.setForeground(Color.WHITE);
        JLabel sub=new JLabel("Evaluá cada producto y definí si vuelve al stock, queda en revisión o se descarta.");sub.setFont(new Font("Segoe UI",Font.PLAIN,13));sub.setForeground(new Color(217,232,251));t.add(titulo);t.add(sub);p.add(t,BorderLayout.CENTER);return p;
    }
    private JPanel lateral(){
        JPanel p=tarjeta();p.setPreferredSize(new Dimension(225,260));p.setLayout(new BorderLayout(0,12));
        JLabel titulo=etiqueta("PRODUCTOS DE LA VENTA",14);p.add(titulo,BorderLayout.NORTH);
        productos.setFont(new Font("Segoe UI",Font.BOLD,14));productos.setFixedCellHeight(40);productos.setSelectionBackground(new Color(213,228,251));productos.setSelectionForeground(NAVY);productos.setBackground(new Color(250,252,255));productos.setBorder(new EmptyBorder(5,5,5,5));
        JScrollPane sp=new JScrollPane(productos);sp.setBorder(BorderFactory.createLineBorder(new Color(215,225,240)));p.add(sp,BorderLayout.CENTER);
        JLabel tip=new JLabel("Seleccioná un producto para evaluarlo.");tip.setForeground(MUTED);tip.setFont(new Font("Segoe UI",Font.PLAIN,11));p.add(tip,BorderLayout.SOUTH);return p;
    }
    private JPanel editor(){
        JPanel card=tarjeta();card.setLayout(new BorderLayout(0,6));
        JLabel titulo=etiqueta("EVALUACIÓN DEL PRODUCTO",15);card.add(titulo,BorderLayout.NORTH);
        JPanel content=new JPanel();content.setOpaque(false);content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        JPanel product=new JPanel(new BorderLayout(0,4));product.setBackground(new Color(232,241,253));product.setBorder(new EmptyBorder(7,10,7,10));
        nombre.setFont(new Font("Segoe UI",Font.BOLD,17));nombre.setForeground(NAVY);datos.setFont(new Font("Segoe UI",Font.PLAIN,12));datos.setForeground(MUTED);product.add(nombre,BorderLayout.NORTH);product.add(datos,BorderLayout.SOUTH);addBlock(content,product,5);
        addBlock(content,etiqueta("Cantidad a devolver *",13),3);
        cantidad.setFont(new Font("Segoe UI",Font.BOLD,16));cantidad.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(177,197,225)),new EmptyBorder(5,10,5,10)));cantidad.setPreferredSize(new Dimension(100,33));addBlock(content,cantidad,7);
        addBlock(content,etiqueta("Destino de la mercadería *",13),4);
        JPanel destinos=new JPanel(new GridLayout(1,3,6,0));destinos.setOpaque(false);destinos.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));
        String[] names={"APTO PARA REVENTA","EN REVISIÓN","MERMA / DESCARTE"};
        String[] hints={"Suma stock vendible","No suma stock","No suma stock"};
        for(int i=0;i<3;i++){final int j=i;JButton b=new JButton("<html><center><b>"+names[i]+"</b><br><font size='2'>"+hints[i]+"</font></center></html>");b.setIcon(new Icono(j==0?"check":j==1?"clock":"trash",j==0?GREEN:j==1?AMBER:RED,19));b.setIconTextGap(4);b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.setOpaque(true);b.setContentAreaFilled(true);b.addActionListener(e->{destinoActual=j==0?DevolucionVentaService.Destino.APTO:j==1?DevolucionVentaService.Destino.REVISION:DevolucionVentaService.Destino.MERMA;colorearOpciones();});opciones[i]=b;destinos.add(b);}addBlock(content,destinos,5);
        JLabel ayuda=new JLabel("Solo APTO PARA REVENTA reintegra mercadería al stock disponible.");ayuda.setForeground(AMBER);ayuda.setFont(new Font("Segoe UI",Font.PLAIN,12));addBlock(content,ayuda,7);
        addBlock(content,etiqueta("Evaluación física / observaciones *",13),4);
        configurarArea(evaluacion);JScrollPane ev=new JScrollPane(evaluacion);ev.setBorder(BorderFactory.createLineBorder(new Color(177,197,225)));ev.setPreferredSize(new Dimension(100,57));addBlock(content,ev,6);
        guardar=boton("GUARDAR PRODUCTO Y CONTINUAR",BLUE);guardar.setIcon(new Icono("check",Color.WHITE,17));guardar.setAlignmentX(Component.LEFT_ALIGNMENT);addBlock(content,guardar,3);
        JScrollPane scroll=new JScrollPane(content);scroll.setBorder(null);scroll.getVerticalScrollBar().setUnitIncrement(16);scroll.setOpaque(false);scroll.getViewport().setOpaque(false);card.add(scroll,BorderLayout.CENTER);return card;
    }
    private JPanel pie(){
        JPanel p=new JPanel(new BorderLayout(0,6));p.setBackground(Color.WHITE);p.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(215,225,239)),new EmptyBorder(8,15,9,15)));
        JPanel razon=new JPanel(new BorderLayout(0,5));razon.setOpaque(false);razon.add(etiqueta("Motivo general de la operación (10 a 250 caracteres) *",13),BorderLayout.NORTH);
        configurarArea(motivo);JScrollPane ms=new JScrollPane(motivo);ms.setBorder(BorderFactory.createLineBorder(new Color(177,197,225)));ms.setPreferredSize(new Dimension(100,44));razon.add(ms,BorderLayout.CENTER);p.add(razon,BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout());centro.setOpaque(false);resumen.setFont(new Font("Segoe UI",Font.BOLD,12));resumen.setForeground(NAVY);reintegro.setFont(new Font("Segoe UI",Font.BOLD,13));reintegro.setForeground(NAVY);centro.add(resumen,BorderLayout.WEST);centro.add(reintegro,BorderLayout.EAST);p.add(centro,BorderLayout.CENTER);
        JPanel row=new JPanel(new BorderLayout(12,0));row.setOpaque(false);JButton cancelar=boton("CANCELAR",new Color(105,118,137));cancelar.setIcon(new Icono("x",Color.WHITE,17));cancelar.addActionListener(e->dispose());row.add(cancelar,BorderLayout.WEST);
        confirmar=boton(anulacion?"CONFIRMAR ANULACIÓN":"CONFIRMAR DEVOLUCIÓN",GREEN);confirmar.setIcon(new Icono("check",Color.WHITE,17));row.add(confirmar,BorderLayout.EAST);
        JPanel foot=new JPanel(new BorderLayout(0,3));foot.setOpaque(false);aviso.setFont(new Font("Segoe UI",Font.PLAIN,12));aviso.setForeground(RED);foot.add(aviso,BorderLayout.NORTH);foot.add(row,BorderLayout.SOUTH);p.add(foot,BorderLayout.SOUTH);return p;
    }
    private JPanel tarjeta(){JPanel p=new JPanel();p.setBackground(Color.WHITE);p.setBorder(new CompoundBorder(BorderFactory.createLineBorder(new Color(215,225,241)),new EmptyBorder(11,13,11,13)));return p;}
    private JLabel etiqueta(String s,int size){JLabel l=new JLabel(s);l.setFont(new Font("Segoe UI",Font.BOLD,size));l.setForeground(NAVY);l.setAlignmentX(Component.LEFT_ALIGNMENT);return l;}
    private void configurarArea(JTextArea a){a.setFont(new Font("Segoe UI",Font.PLAIN,13));a.setLineWrap(true);a.setWrapStyleWord(true);a.setMargin(new Insets(8,10,8,10));}
    private void addBlock(JPanel p,JComponent c,int after){c.setAlignmentX(Component.LEFT_ALIGNMENT);if(!(c instanceof JScrollPane))c.setMaximumSize(new Dimension(Integer.MAX_VALUE,c.getPreferredSize().height));p.add(c);p.add(Box.createVerticalStrut(after));}
    private JButton boton(String s,Color color){
        JButton b=new JButton(s){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(!isEnabled()?new Color(166,177,190):getModel().isPressed()?color.darker():getModel().isRollover()?color.brighter():color);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);g2.dispose();super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(Color.WHITE);
        b.setOpaque(false);b.setContentAreaFilled(false);b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8,13,8,13));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(Math.max(190,b.getPreferredSize().width+15),36));return b;
    }
    /** Iconos vectoriales dibujados con Java2D: no dependen de emojis ni de fuentes externas. */
    private static final class Icono implements Icon {
        final String tipo;final Color color;final int size;
        Icono(String tipo,Color color,int size){this.tipo=tipo;this.color=color;this.size=size;}
        public int getIconWidth(){return size+3;}
        public int getIconHeight(){return size;}
        public void paintIcon(Component c,Graphics g,int x,int y){
            Graphics2D d=(Graphics2D)g.create();d.translate(x+2,y+1);d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            d.setColor(color);d.setStroke(new BasicStroke(2.1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));int w=size-4;
            switch(tipo){
                case "check":d.drawLine(1,w/2, w/3,w-2);d.drawLine(w/3,w-2,w-1,2);break;
                case "x":d.drawLine(2,2,w-2,w-2);d.drawLine(w-2,2,2,w-2);break;
                case "clock":d.drawOval(1,1,w-2,w-2);d.drawLine(w/2,w/2,w/2,3);d.drawLine(w/2,w/2,w-3,w/2);break;
                case "trash":d.drawRect(4,5,w-8,w-7);d.drawLine(2,4,w-2,4);d.drawLine(w/2-3,2,w/2+3,2);d.drawLine(w/2-2,8,w/2-2,w-4);d.drawLine(w/2+2,8,w/2+2,w-4);break;
            }d.dispose();
        }
    }
    private void colorearOpciones(){for(int i=0;i<3;i++){JButton b=opciones[i];boolean sel=(i==0&&destinoActual==DevolucionVentaService.Destino.APTO)||(i==1&&destinoActual==DevolucionVentaService.Destino.REVISION)||(i==2&&destinoActual==DevolucionVentaService.Destino.MERMA);Color c=i==0?GREEN:i==1?AMBER:RED;b.setBackground(sel?new Color(235,242,255):Color.WHITE);b.setForeground(sel?c:INK);b.setBorder(new CompoundBorder(BorderFactory.createLineBorder(sel?c:new Color(195,208,228),sel?2:1,true),new EmptyBorder(4,2,4,2)));} }
    private void cargar(){DevolucionVentaService.Linea l=lineas.get(indice);nombre.setText(l.producto);datos.setText("Vendido: "+l.vendido.toPlainString()+"   |   Disponible: "+l.disponible.toPlainString()+"   |   Precio: "+dinero.format(l.precio));cantidad.setText(l.cantidad.signum()>0?l.cantidad.toPlainString():(anulacion?l.disponible.toPlainString():"0"));destinoActual=l.destino; if(destinoActual==DevolucionVentaService.Destino.NO_RECIBIDO)destinoActual=DevolucionVentaService.Destino.REVISION;colorearOpciones();evaluacion.setText(l.observacion);aviso.setText(" ");}
    private boolean guardarActual(boolean mostrar){DevolucionVentaService.Linea l=lineas.get(indice);BigDecimal c;try{c=new BigDecimal(cantidad.getText().trim().replace(",","."));if(c.signum()<0||c.compareTo(l.disponible)>0||c.scale()>3)throw new IllegalArgumentException();}catch(Exception ex){error("La cantidad de "+l.producto+" debe estar entre 0 y "+l.disponible.toPlainString()+".");cantidad.requestFocusInWindow();return false;}
        String obs=evaluacion.getText().trim();if(c.signum()>0&&obs.length()<5){error("Describí la evaluación física de "+l.producto+" (mínimo 5 caracteres).");evaluacion.requestFocusInWindow();return false;}
        l.cantidad=c;l.destino=destinoActual;l.observacion=obs;if(mostrar){aviso.setForeground(GREEN);aviso.setText("Producto evaluado correctamente.");}actualizarResumen();return true;}
    private void error(String s){aviso.setForeground(RED);aviso.setText(s);}
    private void actualizarResumen(){int n=0,a=0,r=0,m=0;BigDecimal total=BigDecimal.ZERO;for(DevolucionVentaService.Linea l:lineas)if(l.cantidad.signum()>0){n++;total=total.add(l.precio.multiply(l.cantidad));switch(l.destino){case APTO:a++;break;case REVISION:r++;break;case MERMA:m++;break;default:break;}}resumen.setText("Productos: "+n+"    Apto: "+a+"    Revisión: "+r+"    Merma: "+m);reintegro.setText("Reintegro pendiente: "+dinero.format(total));}
    private void confirmarOperacion(){if(!guardarActual(false))return;motivoFinal=motivo.getText().trim();if(motivoFinal.length()<10||motivoFinal.length()>250){error("Ingresá un motivo general de entre 10 y 250 caracteres.");motivo.requestFocusInWindow();return;}int n=0;for(int i=0;i<lineas.size();i++){DevolucionVentaService.Linea l=lineas.get(i);if(l.cantidad.signum()>0)n++;if(anulacion&&l.cantidad.compareTo(l.disponible)!=0){indice=i;cargando=true;productos.setSelectedIndex(i);cargando=false;cargar();error("La anulación total requiere la cantidad completa de cada producto.");return;}if(l.cantidad.signum()>0&&l.observacion.trim().length()<5){indice=i;cargando=true;productos.setSelectedIndex(i);cargando=false;cargar();error("Falta evaluar el producto: "+l.producto);return;}}
        if(n==0){error("Indicá una cantidad mayor que cero en al menos un producto.");return;}
        String mensaje="<html><div style='width:340px'><b>Productos evaluados: "+n+"</b><br><br>Solo los productos APTOS regresarán al stock vendible.<br>Los productos en REVISIÓN y MERMA no aumentarán existencias.<br><br><b>El reintegro quedará PENDIENTE.</b><br>No se devolverá dinero automáticamente.</div></html>";
        Object[] opts={"✓  CONFIRMAR","VOLVER A REVISAR"};int resp=JOptionPane.showOptionDialog(this,mensaje,anulacion?"Confirmar anulación":"Confirmar devolución",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opts,opts[1]);if(resp==0){confirmado=true;dispose();}}
    public boolean fueConfirmado(){return confirmado;}
    public String getMotivo(){return motivoFinal;}
    public List<DevolucionVentaService.Linea> getSeleccionadas(){List<DevolucionVentaService.Linea> r=new ArrayList<>();for(DevolucionVentaService.Linea l:lineas)if(l.cantidad.signum()>0)r.add(l);return r;}
}
