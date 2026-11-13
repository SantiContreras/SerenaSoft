package Paneles;
import Dao.ConfiguracionComprobanteDao;
import Dao.ConfiguracionComprobanteDao.Datos;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** Configuración de tickets internos; formulario compacto y botones siempre visibles. */
public class PanelConfigComprobantes extends JPanel {
    private static final Color AZUL=new Color(20,55,113), VERDE=new Color(25,135,84), GRIS=new Color(101,114,132), BORDE=new Color(216,225,237);
    private final JTextField punto=new JTextField(8), siguiente=new JTextField(10);
    private final JCheckBox nombre=check("Nombre comercial"),cuit=check("CUIT"),direccion=check("Dirección"),telefono=check("Teléfono"),vendedor=check("Vendedor"),fecha=check("Fecha y hora");
    private final JCheckBox auto=check("Imprimir al finalizar la venta"),previa=check("Mostrar vista previa"),reimpresion=check("Permitir reimpresión");
    private final JTextArea pie=new JTextArea(3,38);
    private final JButton guardar=boton("GUARDAR CAMBIOS",VERDE),descartar=boton("DESCARTAR CAMBIOS",GRIS);
    private final JLabel estado=new JLabel(" ");
    public PanelConfigComprobantes() {
        setLayout(new BorderLayout(0,10));setBackground(Color.WHITE);setBorder(new EmptyBorder(14,18,12,18));
        JPanel cab=new JPanel(new GridLayout(2,1));cab.setOpaque(false);
        JLabel titulo=new JLabel("COMPROBANTES");titulo.setFont(new Font("Segoe UI",Font.BOLD,23));titulo.setForeground(AZUL);cab.add(titulo);
        JLabel subt=new JLabel("Configuración de tickets internos (no fiscales)");subt.setForeground(GRIS);cab.add(subt);add(cab,BorderLayout.NORTH);
        JPanel cuerpo=new JPanel();cuerpo.setLayout(new BoxLayout(cuerpo,BoxLayout.Y_AXIS));cuerpo.setBackground(Color.WHITE);
        JPanel general=seccion("01  NUMERACIÓN INTERNA");
        JPanel linea=new JPanel(new FlowLayout(FlowLayout.LEFT,14,6));linea.setOpaque(false);
        linea.add(etiqueta("Tipo:"));linea.add(etiqueta("Ticket interno / no fiscal"));linea.add(etiqueta("Punto de venta:"));punto.setPreferredSize(new Dimension(85,32));linea.add(punto);
        linea.add(etiqueta("Próxima venta:"));siguiente.setEditable(false);siguiente.setBackground(new Color(242,246,251));siguiente.setPreferredSize(new Dimension(110,32));linea.add(siguiente);general.add(linea);general.add(nota("La numeración real se consulta de venta_numerador. No se puede editar aquí."));cuerpo.add(general);cuerpo.add(Box.createVerticalStrut(10));
        JPanel datos=seccion("02  DATOS VISIBLES EN EL TICKET");JPanel checks=new JPanel(new GridLayout(2,3,12,9));checks.setOpaque(false);
        for(JCheckBox c:new JCheckBox[]{nombre,cuit,direccion,telefono,vendedor,fecha})checks.add(c);datos.add(checks);cuerpo.add(datos);cuerpo.add(Box.createVerticalStrut(10));
        JPanel mensaje=seccion("03  MENSAJE FINAL");pie.setLineWrap(true);pie.setWrapStyleWord(true);pie.setFont(new Font("Segoe UI",Font.PLAIN,13));mensaje.add(new JScrollPane(pie));cuerpo.add(mensaje);cuerpo.add(Box.createVerticalStrut(10));
        JPanel opciones=seccion("04  IMPRESIÓN Y REIMPRESIÓN");JPanel opc=new JPanel(new GridLayout(3,1,0,4));opc.setOpaque(false);for(JCheckBox c:new JCheckBox[]{auto,previa,reimpresion})opc.add(c);opciones.add(opc);opciones.add(nota("Las opciones se guardan. La automatización de impresión requiere integración con el flujo de venta."));cuerpo.add(opciones);
        JScrollPane scroll=new JScrollPane(cuerpo);scroll.setBorder(null);scroll.getVerticalScrollBar().setUnitIncrement(18);add(scroll,BorderLayout.CENTER);
        JPanel inferior=new JPanel(new BorderLayout());inferior.setBackground(Color.WHITE);estado.setForeground(GRIS);inferior.add(estado,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,9,6));acciones.setOpaque(false);acciones.add(descartar);acciones.add(guardar);inferior.add(acciones,BorderLayout.EAST);add(inferior,BorderLayout.SOUTH);
        guardar.addActionListener(e->guardar());descartar.addActionListener(e->cargar());cargar();
    }
    private static JLabel etiqueta(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(AZUL);return l;}
    private static JLabel nota(String t){JLabel l=new JLabel(t);l.setForeground(GRIS);l.setFont(new Font("Segoe UI",Font.PLAIN,11));return l;}
    private static JCheckBox check(String t){JCheckBox c=new JCheckBox(t);c.setOpaque(false);c.setForeground(new Color(31,51,83));c.setFont(new Font("Segoe UI",Font.PLAIN,13));return c;}
    private static JPanel seccion(String titulo){JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBackground(Color.WHITE);p.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(BORDE),titulo,TitledBorder.LEFT,TitledBorder.TOP,new Font("Segoe UI",Font.BOLD,13),AZUL),new EmptyBorder(9,12,12,12)));p.setAlignmentX(Component.LEFT_ALIGNMENT);return p;}
    private static JButton boton(String t,Color bg){JButton b=new JButton(t){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(isEnabled()?bg:new Color(165,174,185));g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,9,9);g2.setColor(Color.WHITE);g2.setFont(getFont());FontMetrics fm=g2.getFontMetrics();g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);g2.dispose();}};b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setPreferredSize(new Dimension(t.startsWith("GUARDAR")?178:185,39));b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setOpaque(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private void cargar(){try{Datos d=ConfiguracionComprobanteDao.cargar();punto.setText(String.format("%04d",d.puntoVenta));siguiente.setText(String.format("%08d",ConfiguracionComprobanteDao.siguienteNumeroVenta()));nombre.setSelected(d.nombre);cuit.setSelected(d.cuit);direccion.setSelected(d.direccion);telefono.setSelected(d.telefono);vendedor.setSelected(d.vendedor);fecha.setSelected(d.fecha);pie.setText(d.pie);auto.setSelected(d.auto);previa.setSelected(d.previa);reimpresion.setSelected(d.reimpresion);estado.setText("Configuración cargada desde MariaDB.");}catch(Exception ex){estado.setText("No se pudo cargar la configuración.");JOptionPane.showMessageDialog(this,"Verificá que ejecutaste el SQL de configuración.\n"+ex.getMessage(),"Comprobantes",JOptionPane.ERROR_MESSAGE);}}
    private void guardar(){try{Datos d=new Datos();d.puntoVenta=Integer.parseInt(punto.getText().trim());d.nombre=nombre.isSelected();d.cuit=cuit.isSelected();d.direccion=direccion.isSelected();d.telefono=telefono.isSelected();d.vendedor=vendedor.isSelected();d.fecha=fecha.isSelected();d.pie=pie.getText().trim();d.auto=auto.isSelected();d.previa=previa.isSelected();d.reimpresion=reimpresion.isSelected();ConfiguracionComprobanteDao.guardar(d);estado.setText("Cambios guardados correctamente.");JOptionPane.showMessageDialog(this,"Configuración guardada en MariaDB.","Comprobantes",JOptionPane.INFORMATION_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"No se guardaron los cambios:\n"+ex.getMessage(),"Comprobantes",JOptionPane.ERROR_MESSAGE);}}
}
