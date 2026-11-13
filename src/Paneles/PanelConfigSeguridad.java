package Paneles;
import Dao.SeguridadConfigDao;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** Configuración efectiva de acceso y claves. Otras políticas requieren integración adicional. */
public class PanelConfigSeguridad extends JPanel {
 private static final Color NAVY=new Color(19,53,111), BLUE=new Color(28,76,155), GREEN=new Color(25,135,84),BG=new Color(241,245,250),INK=new Color(25,47,85);
 private final JCheckBox bloqueo=check("Bloquear temporalmente después de intentos fallidos");
 private final JSpinner intentos=new JSpinner(new SpinnerNumberModel(5,1,20,1));
 private final JSpinner minutos=new JSpinner(new SpinnerNumberModel(15,1,1440,1));
 private final JSpinner longitud=new JSpinner(new SpinnerNumberModel(8,8,64,1));
 private final JCheckBox mayus=check("Exigir mayúscula"),minus=check("Exigir minúscula"),numero=check("Exigir número"),especial=check("Exigir carácter especial");
 private final JButton guardar=button("GUARDAR CAMBIOS",GREEN),restaurar=button("DESCARTAR CAMBIOS",new Color(98,112,133));
 private final JLabel estado=new JLabel(" ");
 private final SeguridadConfigDao dao=new SeguridadConfigDao();
 public PanelConfigSeguridad(){
  setLayout(new BorderLayout());setBackground(BG);
  JPanel body=new JPanel();body.setOpaque(false);body.setLayout(new BoxLayout(body,BoxLayout.Y_AXIS));body.setBorder(new EmptyBorder(14,18,14,18));
  JPanel title=new JPanel(new BorderLayout());title.setOpaque(false);
  JLabel h=new JLabel("SEGURIDAD DEL SISTEMA");h.setFont(new Font("Segoe UI",Font.BOLD,22));h.setForeground(NAVY);
  JLabel sub=new JLabel("Políticas de acceso y contraseñas aplicadas a todos los usuarios");sub.setForeground(new Color(92,107,128));
  JPanel ht=new JPanel();ht.setOpaque(false);ht.setLayout(new BoxLayout(ht,BoxLayout.Y_AXIS));ht.add(h);ht.add(Box.createVerticalStrut(4));ht.add(sub);title.add(ht);body.add(title);body.add(Box.createVerticalStrut(15));
  JPanel acceso=section("01  ACCESO Y BLOQUEO","Protección frente a contraseñas incorrectas");
  acceso.add(line(bloqueo));acceso.add(line(new JLabel("Intentos permitidos"),intentos,new JLabel("Minutos de bloqueo"),minutos));body.add(acceso);body.add(Box.createVerticalStrut(12));
  JPanel claves=section("02  CONTRASEÑAS","Requisitos para crear o restablecer contraseñas");
  claves.add(line(new JLabel("Longitud mínima"),longitud));
  claves.add(line(mayus,minus));claves.add(line(numero,especial));body.add(claves);body.add(Box.createVerticalStrut(12));
  JPanel nota=section("03  PROTECCIONES ADICIONALES","Funciones planificadas para la siguiente etapa técnica");
  JLabel l=new JLabel("Auditoría detallada, vencimiento de claves y sesiones concurrentes requieren servicios propios.");l.setForeground(new Color(93,103,117));l.setFont(new Font("Segoe UI",Font.PLAIN,12));nota.add(line(l));body.add(nota);body.add(Box.createVerticalStrut(10));
  estado.setForeground(new Color(95,108,126));body.add(estado);
  JScrollPane scroll=new JScrollPane(body);scroll.setBorder(null);scroll.getVerticalScrollBar().setUnitIncrement(18);scroll.getViewport().setBackground(BG);add(scroll,BorderLayout.CENTER);
  JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,10));footer.setBackground(Color.WHITE);footer.setBorder(new MatteBorder(1,0,0,0,new Color(220,227,237)));footer.add(restaurar);footer.add(guardar);add(footer,BorderLayout.SOUTH);
  bloqueo.addActionListener(e->habilitar());restaurar.addActionListener(e->cargar());guardar.addActionListener(e->guardar());cargar();
 }
 private static JCheckBox check(String text){JCheckBox c=new JCheckBox(text);c.setFont(new Font("Segoe UI",Font.PLAIN,13));c.setForeground(INK);c.setBackground(Color.WHITE);c.setFocusPainted(false);c.setOpaque(true);return c;}
 private static JButton button(String text, Color color) {
  JButton b = new JButton(text) {
   @Override protected void paintComponent(Graphics g) {
    Graphics2D d = (Graphics2D) g.create();
    try {
     d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     Color fondo = !isEnabled() ? new Color(145,151,162)
       : getModel().isPressed() ? color.darker().darker()
       : getModel().isRollover() ? color.brighter() : color;
     d.setColor(fondo);
     d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
     d.setColor(Color.WHITE);
     d.setFont(getFont());
     FontMetrics fm = d.getFontMetrics();
     String texto = getText();
     int x = Math.max(8, (getWidth()-fm.stringWidth(texto))/2);
     int y = (getHeight()-fm.getHeight())/2 + fm.getAscent();
     d.drawString(texto, x, y);
    } finally { d.dispose(); }
    // No llamar a super.paintComponent(): el Look and Feel puede tapar el fondo.
   }
  };
  b.setFont(new Font("Segoe UI", Font.BOLD, 12));
  b.setForeground(Color.WHITE);
  b.setContentAreaFilled(false);
  b.setOpaque(false);
  b.setBorderPainted(false);
  b.setFocusPainted(false);
  b.setRolloverEnabled(true);
  b.setPreferredSize(new Dimension(185, 40));
  b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  return b;
 }
 private static JPanel section(String title,String subtitle){JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBackground(Color.WHITE);p.setBorder(new CompoundBorder(new LineBorder(new Color(211,221,234)),new EmptyBorder(13,17,12,17)));p.setAlignmentX(LEFT_ALIGNMENT);JLabel a=new JLabel(title);a.setForeground(NAVY);a.setFont(new Font("Segoe UI",Font.BOLD,15));JLabel b=new JLabel(subtitle);b.setForeground(new Color(95,109,128));b.setFont(new Font("Segoe UI",Font.PLAIN,12));a.setAlignmentX(LEFT_ALIGNMENT);b.setAlignmentX(LEFT_ALIGNMENT);p.add(a);p.add(Box.createVerticalStrut(3));p.add(b);p.add(Box.createVerticalStrut(10));return p;}
 private static JPanel line(Component... comps){JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,15,4));p.setOpaque(false);p.setAlignmentX(LEFT_ALIGNMENT);p.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));for(Component c:comps){if(c instanceof JSpinner) c.setPreferredSize(new Dimension(76,29));if(c instanceof JLabel)((JLabel)c).setForeground(INK);p.add(c);}return p;}
 private void habilitar(){intentos.setEnabled(bloqueo.isSelected());minutos.setEnabled(bloqueo.isSelected());}
 private void cargar(){try{SeguridadConfigDao.Datos d=dao.leer();bloqueo.setSelected(d.bloquear);intentos.setValue(d.intentos);minutos.setValue(d.minutos);longitud.setValue(d.longitud);mayus.setSelected(d.mayus);minus.setSelected(d.minus);numero.setSelected(d.numero);especial.setSelected(d.especial);estado.setText("Configuración cargada desde MariaDB.");habilitar();}catch(Exception ex){estado.setText("No se pudo cargar la configuración.");JOptionPane.showMessageDialog(this,ex.getMessage(),"Seguridad",JOptionPane.ERROR_MESSAGE);}}
 private void guardar(){try{SeguridadConfigDao.Datos d=new SeguridadConfigDao.Datos();d.bloquear=bloqueo.isSelected();d.intentos=(Integer)intentos.getValue();d.minutos=(Integer)minutos.getValue();d.longitud=(Integer)longitud.getValue();d.mayus=mayus.isSelected();d.minus=minus.isSelected();d.numero=numero.isSelected();d.especial=especial.isSelected();dao.guardar(d);estado.setText("Cambios guardados en MariaDB. Se aplican en próximos accesos y nuevas contraseñas.");JOptionPane.showMessageDialog(this,"Configuración de seguridad guardada correctamente.","Serena Soft",JOptionPane.INFORMATION_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"No se guardaron los cambios: "+ex.getMessage(),"Seguridad",JOptionPane.ERROR_MESSAGE);}}
}
