package Dialogos;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/** Tema claro corporativo para los dialogos de venta. Solo modifica presentacion. */
public final class EstiloDialogosVentas {
 private EstiloDialogosVentas(){}
 public static final Color FONDO=new Color(231,239,250), TARJETA=Color.WHITE,
  AZUL=new Color(17,57,117), TEXTO=new Color(34,51,75), BORDE=new Color(205,216,232);
 public static void aplicar(JDialog d){
   d.getContentPane().setBackground(FONDO);
   recorrer(d.getContentPane());
   d.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(88,133,190),1));
   d.repaint();
 }
 private static void recorrer(Component c){
   if(c instanceof JPanel){JPanel p=(JPanel)c;p.setOpaque(true);if(!Boolean.TRUE.equals(p.getClientProperty("serena.headerBar")))p.setBackground(FONDO);}
   if(c instanceof JLabel){JLabel l=(JLabel)c;if(!Boolean.TRUE.equals(l.getClientProperty("serena.header")))l.setForeground(AZUL);l.setFont(new Font("Segoe UI",Font.BOLD,Math.max(14,l.getFont().getSize())));}
   if(c instanceof JTextField){
     JTextField t=(JTextField)c;t.setOpaque(true);t.setBackground(new Color(255,255,255));t.setForeground(TEXTO);t.setCaretColor(AZUL);
     t.addFocusListener(new FocusAdapter(){public void focusGained(FocusEvent e){t.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(13,new Color(18,116,207)),BorderFactory.createEmptyBorder(4,14,4,14)));}public void focusLost(FocusEvent e){t.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(13,new Color(104,143,194)),BorderFactory.createEmptyBorder(4,14,4,14)));}});t.setSelectionColor(new Color(186,214,248));t.setSelectedTextColor(TEXTO);
     t.setFont(new Font("Segoe UI",Font.PLAIN,17));
     t.setPreferredSize(new Dimension(Math.max(210,t.getPreferredSize().width),46));
     t.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(13,new Color(104,143,194)),BorderFactory.createEmptyBorder(4,14,4,14)));
   }
   if(c instanceof JSpinner){JSpinner s=(JSpinner)c;if(s.getEditor() instanceof JSpinner.DefaultEditor)recorrer(((JSpinner.DefaultEditor)s.getEditor()).getTextField());}
   if(c instanceof JComboBox){
     JComboBox<?> x=(JComboBox<?>)c;x.setBackground(Color.WHITE);x.setForeground(TEXTO);
     x.setFont(new Font("Segoe UI",Font.PLAIN,16));x.setPreferredSize(new Dimension(Math.max(210,x.getPreferredSize().width),46));
     x.setRenderer(new DefaultListCellRenderer(){@Override public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean selected,boolean focused){
       JLabel l=(JLabel)super.getListCellRendererComponent(list,value,index,selected,focused);
       l.setFont(new Font("Segoe UI",Font.PLAIN,16));l.setBorder(BorderFactory.createEmptyBorder(6,12,6,8));
       l.setOpaque(true);l.setBackground(selected?new Color(216,234,255):Color.WHITE);l.setForeground(TEXTO);return l;
     }});
   }
   if(c instanceof JButton){
     JButton b=(JButton)c;
     String raw=b.getText()==null?"":b.getText();
     String normalized=raw.replaceFirst("^[^\\p{L}\\p{N}]+\\s*","").trim();
     if(!normalized.isEmpty())b.setText(normalized);
     String name=normalized.toLowerCase(java.util.Locale.ROOT);
     Color base=name.contains("guardar")||name.contains("confirmar")||name.contains("cobrar")||name.contains("aceptar")?new Color(18,137,91):
       name.contains("cancelar")||name.contains("cerrar")?new Color(105,119,139):new Color(18,92,169);
     b.setUI(new BotonClaroUI(base));b.setBackground(base);b.setForeground(Color.WHITE);
     b.setFont(new Font("Segoe UI",Font.BOLD,14));b.setFocusPainted(false);b.setContentAreaFilled(false);b.setOpaque(false);
     b.setBorder(BorderFactory.createEmptyBorder(11,16,11,16));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
     b.setRolloverEnabled(true);b.setIcon(new Icono(name,Color.WHITE,17));b.setIconTextGap(9);
   }
   if(c instanceof Container)for(Component child:((Container)c).getComponents())recorrer(child);
 }
 public static JPanel encabezado(String icono,String titulo,String descripcion){
   JPanel p=new JPanel(new BorderLayout());p.setOpaque(false);
   JPanel barra=new JPanel(new BorderLayout(12,0));barra.setBackground(new Color(17,57,117));barra.putClientProperty("serena.headerBar",Boolean.TRUE);
   barra.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
   JLabel t=new JLabel(titulo,new Icono(titulo.toLowerCase(java.util.Locale.ROOT),new Color(96,207,255),25),SwingConstants.LEFT);
   t.setIconTextGap(13);t.setFont(new Font("Segoe UI",Font.BOLD,22));t.setForeground(Color.WHITE);t.putClientProperty("serena.header",Boolean.TRUE);barra.add(t,BorderLayout.CENTER);
   p.add(barra,BorderLayout.NORTH);
   JLabel d=new JLabel(descripcion);d.setForeground(new Color(35,68,112));d.setFont(new Font("Segoe UI",Font.BOLD,14));
   d.setBorder(BorderFactory.createEmptyBorder(16,19,11,10));p.add(d,BorderLayout.SOUTH);
   return p;
 }
 private static final class BotonClaroUI extends javax.swing.plaf.basic.BasicButtonUI{
   private final Color base;BotonClaroUI(Color c){base=c;}
   @Override public void paint(Graphics g,JComponent c){
     AbstractButton b=(AbstractButton)c;ButtonModel m=b.getModel();Graphics2D gg=(Graphics2D)g.create();
     try{gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       Color fill=!b.isEnabled()?new Color(166,175,188):m.isPressed()?base.darker().darker():m.isRollover()?base.brighter():base;
       gg.setColor(fill);gg.fillRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,15,15);
       gg.setColor(m.isRollover()?base.darker():fill);gg.drawRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,15,15);
     }finally{gg.dispose();}super.paint(g,c);
   }
 }
 /** Iconos vectoriales compatibles con cualquier fuente instalada. */
 private static final class Icono implements Icon{
   private final String name;private final Color color;private final int size;
   Icono(String n,Color c,int s){name=n;color=c;size=s;}
   public int getIconWidth(){return size;}public int getIconHeight(){return size;}
   public void paintIcon(Component c,Graphics g,int x,int y){
     Graphics2D a=(Graphics2D)g.create();try{
       a.translate(x,y);a.scale(size/24.0,size/24.0);a.setColor(color);a.setStroke(new BasicStroke(2.1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
       if(name.contains("buscar")||name.contains("cliente")){a.drawOval(3,3,13,13);a.drawLine(15,15,22,22);}
       else if(name.contains("cancel")||name.contains("cerrar")){a.drawLine(5,5,19,19);a.drawLine(19,5,5,19);}
       else if(name.contains("guardar")||name.contains("acept")||name.contains("confirm")||name.contains("cobrar")){a.drawLine(3,12,9,18);a.drawLine(9,18,21,5);}
       else if(name.contains("peso")||name.contains("pesable")){a.drawRoundRect(3,5,18,16,4,4);a.drawArc(8,8,8,8,0,180);}
       else if(name.contains("venta")||name.contains("factur")||name.contains("comprobante")){a.drawRoundRect(5,2,14,20,2,2);a.drawLine(8,8,16,8);a.drawLine(8,12,16,12);a.drawLine(8,16,14,16);}
       else {a.drawRoundRect(3,3,18,18,4,4);a.drawLine(8,12,16,12);a.drawLine(12,8,12,16);}
     }finally{a.dispose();}
   }
 }
 public static final class RoundedBorder extends AbstractBorder{
   private final int radio;private final Color color;
   public RoundedBorder(int r,Color c){radio=r;color=c;}
   @Override public void paintBorder(Component c,Graphics g,int x,int y,int w,int h){Graphics2D gg=(Graphics2D)g.create();gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);gg.setColor(color);gg.drawRoundRect(x,y,w-1,h-1,radio,radio);gg.dispose();}
   @Override public Insets getBorderInsets(Component c){return new Insets(8,12,8,12);}
 }
}
