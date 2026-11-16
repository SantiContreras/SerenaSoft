package Paneles;

import Dao.ConfiguracionComprobanteDao;
import Dao.ConfiguracionComprobanteDao.Opciones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** Configuración segura de tickets internos. La emisión fiscal permanece fuera de este panel. */
public final class PanelConfigComprobantes extends JPanel {
    private static final Color AZUL=new Color(22,68,137), VERDE=new Color(22,126,78), GRIS=new Color(91,103,120);
    private final ConfiguracionComprobanteDao dao=new ConfiguracionComprobanteDao();
    private final JCheckBox nombre=check("Nombre comercial"), cuit=check("CUIT"), direccion=check("Dirección"), telefono=check("Teléfono"), vendedor=check("Vendedor"), fecha=check("Fecha y hora");
    private final JCheckBox automatico=check("Impresión automática al finalizar la venta"), previa=check("Vista previa antes de imprimir"), reimpresion=check("Permitir reimpresión (sujeto a permisos de usuario)");
    private final JTextArea pie=new JTextArea(3,36);
    private final JButton guardar=boton("GUARDAR CAMBIOS",VERDE), descartar=boton("DESCARTAR CAMBIOS",GRIS);
    private final JLabel estado=new JLabel(" ");
    private boolean disponible;

    public PanelConfigComprobantes(){
        setLayout(new BorderLayout(0,8));setBackground(new Color(245,247,251));setBorder(new EmptyBorder(14,14,14,14));
        JPanel contenido=new JPanel();contenido.setLayout(new BoxLayout(contenido,BoxLayout.Y_AXIS));contenido.setBackground(Color.WHITE);contenido.setBorder(new EmptyBorder(18,20,18,20));
        JLabel titulo=new JLabel("COMPROBANTES · TICKETS INTERNOS");titulo.setFont(new Font("Segoe UI",Font.BOLD,20));titulo.setForeground(AZUL);contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(8));
        JLabel aviso=new JLabel("Ticket NO FISCAL. La facturación ARCA se habilitará en un módulo separado cuando esté implementada y autorizada.");aviso.setForeground(new Color(145,75,25));contenido.add(aviso);
        contenido.add(Box.createVerticalStrut(12));
        JPanel numeracion=seccion("Numeración protegida (solo lectura)");
        numeracion.add(new JLabel("El número se asigna desde Ventas / Comprobantes. No puede editarse aquí para evitar duplicados."));
        contenido.add(numeracion);contenido.add(Box.createVerticalStrut(12));
        JPanel datos=seccion("Datos visibles en el ticket");
        JPanel checks=new JPanel(new GridLayout(0,2,12,8));checks.setOpaque(false);
        for(JCheckBox c:new JCheckBox[]{nombre,cuit,direccion,telefono,vendedor,fecha})checks.add(c);
        datos.add(checks);contenido.add(datos);contenido.add(Box.createVerticalStrut(12));
        JPanel mensaje=seccion("Mensaje final (máximo 250 caracteres)");pie.setLineWrap(true);pie.setWrapStyleWord(true);pie.setFont(new Font("Segoe UI",Font.PLAIN,13));
        JScrollPane area=new JScrollPane(pie);area.setPreferredSize(new Dimension(580,82));mensaje.add(area);contenido.add(mensaje);contenido.add(Box.createVerticalStrut(12));
        JPanel opciones=seccion("Preferencias de impresión");
        for(JCheckBox c:new JCheckBox[]{automatico,previa,reimpresion}){opciones.add(c);opciones.add(Box.createVerticalStrut(8));}
        JLabel nota=new JLabel("Las preferencias se guardan; la impresión requiere integración con Ventas.");nota.setForeground(GRIS);opciones.add(nota);
        contenido.add(opciones);contenido.add(Box.createVerticalStrut(12));
        JPanel fiscal=seccion("Facturación electrónica ARCA · preparación futura");
        fiscal.add(new JLabel("No disponible en esta versión. No se solicitan credenciales ni se emiten facturas fiscales."));contenido.add(fiscal);
        JScrollPane scroll=new JScrollPane(contenido);scroll.setBorder(BorderFactory.createLineBorder(new Color(216,225,235)));scroll.getVerticalScrollBar().setUnitIncrement(18);add(scroll,BorderLayout.CENTER);
        JPanel barra=new JPanel(new BorderLayout());barra.setOpaque(false);estado.setForeground(GRIS);barra.add(estado,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));acciones.setOpaque(false);acciones.add(descartar);acciones.add(guardar);barra.add(acciones,BorderLayout.EAST);add(barra,BorderLayout.SOUTH);
        guardar.addActionListener(e->guardar());descartar.addActionListener(e->cargar());cargar();
    }
    private static JCheckBox check(String texto){JCheckBox c=new JCheckBox(texto);c.setOpaque(false);c.setFont(new Font("Segoe UI",Font.PLAIN,13));return c;}
    private static JPanel seccion(String titulo){JPanel p=new JPanel();p.setOpaque(false);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setAlignmentX(Component.LEFT_ALIGNMENT);p.setBorder(new CompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(215,224,235)),titulo,0,0,new Font("Segoe UI",Font.BOLD,14),AZUL),new EmptyBorder(10,12,10,12)));return p;}
    private static JButton boton(String texto,Color fondo){JButton b=new JButton(texto){@Override protected void paintComponent(Graphics g){Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);x.setColor(isEnabled()?fondo:new Color(160,165,172));x.fillRoundRect(0,0,getWidth(),getHeight(),12,12);x.setColor(Color.WHITE);x.setFont(getFont());FontMetrics fm=x.getFontMetrics();x.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);x.dispose();}};b.setPreferredSize(new Dimension(texto.length()>16?195:185,40));b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setOpaque(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private void aplicar(Opciones o){nombre.setSelected(o.nombre);cuit.setSelected(o.cuit);direccion.setSelected(o.direccion);telefono.setSelected(o.telefono);vendedor.setSelected(o.vendedor);fecha.setSelected(o.fecha);automatico.setSelected(o.automatico);previa.setSelected(o.vistaPrevia);reimpresion.setSelected(o.reimpresion);pie.setText(o.pie==null?"":o.pie);}
    private Opciones leer(){Opciones o=new Opciones();o.nombre=nombre.isSelected();o.cuit=cuit.isSelected();o.direccion=direccion.isSelected();o.telefono=telefono.isSelected();o.vendedor=vendedor.isSelected();o.fecha=fecha.isSelected();o.automatico=automatico.isSelected();o.vistaPrevia=previa.isSelected();o.reimpresion=reimpresion.isSelected();o.pie=pie.getText().trim();return o;}
    private void cargar(){try{aplicar(dao.cargar());disponible=true;estado.setText("Configuración cargada desde MariaDB.");}catch(Exception ex){disponible=false;estado.setText("ERROR: no se pudo cargar la configuración.");JOptionPane.showMessageDialog(this,ex.getMessage(),"Error de configuración",JOptionPane.ERROR_MESSAGE);}guardar.setEnabled(disponible);}
    private void guardar(){if(!disponible)return;try{Opciones o=leer();if(o.automatico&&o.vistaPrevia){JOptionPane.showMessageDialog(this,"Elegí impresión automática o vista previa, no ambas.","Validación",JOptionPane.WARNING_MESSAGE);return;}dao.guardar(o);estado.setText("Cambios guardados correctamente en MariaDB.");JOptionPane.showMessageDialog(this,"Configuración guardada. Para aplicar las opciones al ticket falta integrar JasperReports.");}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"No se guardó",JOptionPane.ERROR_MESSAGE);}}
}
