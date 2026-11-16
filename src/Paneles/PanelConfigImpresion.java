package Paneles;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import javax.swing.border.*;

/** Configuracion local por equipo. No modifica ventas, caja, stock ni Jasper. */
public class PanelConfigImpresion extends JPanel {
    private final Color azul = new Color(24,69,143);
    private final JComboBox<String> impresoras = new JComboBox<>();
    private final JComboBox<String> papel = new JComboBox<>(new String[]{"80 mm (reporte actual)"});
    private final JSpinner copias = new JSpinner(new SpinnerNumberModel(1,1,10,1));
    private final JCheckBox vista = new JCheckBox("Mostrar vista previa (preferencia para futura integracion)");
    private final JCheckBox automatica = new JCheckBox("Imprimir automaticamente al cobrar (pendiente de integrar)");
    private final JLabel estado = new JLabel(" ");
    private final Path ruta = Paths.get(System.getProperty("user.home"), ".serenasoft", "impresion.properties");
    private final JButton guardar = boton("Guardar cambios", new Color(23,131,83));
    private final JButton prueba = boton("Imprimir prueba", new Color(215,119,17));
    private final JButton restaurar = boton("Restaurar guardado", new Color(91,104,123));
    public PanelConfigImpresion() {
        setLayout(new BorderLayout(0,12)); setBackground(Color.WHITE); setBorder(new EmptyBorder(22,28,18,28));
        JLabel titulo = new JLabel("IMPRESION"); titulo.setFont(new Font("Segoe UI",Font.BOLD,22)); titulo.setForeground(azul);
        JLabel subtitulo = new JLabel("Preferencias locales del equipo. Los tickets de venta actuales no se alteran.");
        subtitulo.setForeground(new Color(100,110,125));
        JPanel cabecera = new JPanel(new GridLayout(2,1,0,4)); cabecera.setOpaque(false); cabecera.add(titulo);cabecera.add(subtitulo);
        add(cabecera,BorderLayout.NORTH);
        JPanel contenido = new JPanel(); contenido.setOpaque(false); contenido.setLayout(new BoxLayout(contenido,BoxLayout.Y_AXIS));
        JPanel dispositivo = seccion("Impresora de este equipo");
        dispositivo.add(new JLabel("Impresora:")); impresoras.setFont(new Font("Segoe UI",Font.PLAIN,14));impresoras.setPreferredSize(new Dimension(390,42));dispositivo.add(impresoras);
        JButton actualizar = boton("Actualizar",azul); dispositivo.add(actualizar);
        contenido.add(dispositivo); contenido.add(Box.createVerticalStrut(14));
        JPanel formato = seccion("Formato disponible");
        formato.add(new JLabel("Papel:")); papel.setFont(new Font("Segoe UI",Font.PLAIN,14));papel.setPreferredSize(new Dimension(235,42));formato.add(papel); formato.add(new JLabel("Copias:")); copias.setFont(new Font("Segoe UI",Font.PLAIN,14));copias.setPreferredSize(new Dimension(90,42));formato.add(copias);
        contenido.add(formato); contenido.add(Box.createVerticalStrut(14));
        JPanel opciones = seccion("Opciones futuras (no activas en Ventas)");
        automatica.setOpaque(false); vista.setOpaque(false);automatica.setFont(new Font("Segoe UI",Font.PLAIN,14));vista.setFont(new Font("Segoe UI",Font.PLAIN,14));
        automatica.setEnabled(false); vista.setEnabled(false);
        opciones.add(automatica); opciones.add(vista);
        contenido.add(opciones); contenido.add(Box.createVerticalStrut(12));
        JLabel aviso = new JLabel("El reporte ticket_venta.jrxml actual es de 80 mm. No se habilitan formatos sin plantilla.");
        aviso.setForeground(new Color(148,82,20)); contenido.add(aviso);
        JScrollPane scroll = new JScrollPane(contenido); scroll.setBorder(null);scroll.getViewport().setBackground(Color.WHITE); scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll,BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout()); pie.setOpaque(false); estado.setForeground(azul);estado.setFont(new Font("Segoe UI",Font.PLAIN,13));pie.add(estado,BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,8)); acciones.setOpaque(false);
        acciones.add(prueba);acciones.add(restaurar);acciones.add(guardar);pie.add(acciones,BorderLayout.EAST);add(pie,BorderLayout.SOUTH);
        actualizar.addActionListener(e->cargarImpresoras((String)impresoras.getSelectedItem()));
        restaurar.addActionListener(e->cargar()); guardar.addActionListener(e->guardar()); prueba.addActionListener(e->imprimirPrueba());
        cargarImpresoras(null); cargar();
    }
    private JPanel seccion(String nombre) {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,16,18));p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(213,222,233)),nombre,0,0,new Font("Segoe UI",Font.BOLD,16),azul));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);p.setMaximumSize(new Dimension(Integer.MAX_VALUE,145));return p;
    }
    private static JButton boton(String texto, Color color) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D d=(Graphics2D)g.create();
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                d.setColor(isEnabled()?color:new Color(190,198,210));
                d.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                d.setColor(isEnabled()?Color.WHITE:new Color(65,75,90));
                d.setFont(getFont());FontMetrics fm=d.getFontMetrics();
                d.drawString(getText(),Math.max(8,(getWidth()-fm.stringWidth(getText()))/2),(getHeight()-fm.getHeight())/2+fm.getAscent());
                d.dispose();
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);b.setOpaque(false);b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));
        b.setPreferredSize(new Dimension(Math.max(140,b.getFontMetrics(b.getFont()).stringWidth(texto)+42),43));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private void cargarImpresoras(String seleccion) {
        impresoras.removeAllItems();
        PrintService[] lista=PrintServiceLookup.lookupPrintServices(null,null);
        for(PrintService p:lista) impresoras.addItem(p.getName());
        if(seleccion!=null) impresoras.setSelectedItem(seleccion);
        if(impresoras.getSelectedIndex()<0){PrintService d=PrintServiceLookup.lookupDefaultPrintService();if(d!=null)impresoras.setSelectedItem(d.getName());}
        if(impresoras.getItemCount()==0)estado.setText("No se detectaron impresoras instaladas.");
    }
    private void cargar() {
        Properties p=new Properties();
        if(Files.exists(ruta))try(InputStream in=Files.newInputStream(ruta)){p.load(in);}catch(IOException ex){mostrarError("No se pudo leer la configuracion: "+ex.getMessage());return;}
        String nombre=p.getProperty("impresora","");
        if(!nombre.isEmpty()){boolean existe=false;for(int i=0;i<impresoras.getItemCount();i++)if(nombre.equals(impresoras.getItemAt(i)))existe=true;
            if(existe)impresoras.setSelectedItem(nombre);else estado.setText("La impresora guardada ya no esta instalada: "+nombre);}
        try{copias.setValue(Math.max(1,Math.min(10,Integer.parseInt(p.getProperty("copias","1")))));}catch(NumberFormatException ex){copias.setValue(1);}
    }
    private void guardar() {
        if(impresoras.getSelectedItem()==null){mostrarError("Selecciona una impresora instalada.");return;}
        Properties p=new Properties();p.setProperty("impresora",(String)impresoras.getSelectedItem());p.setProperty("copias",copias.getValue().toString());p.setProperty("papel","80mm");
        try{Files.createDirectories(ruta.getParent());Path temporal=Files.createTempFile(ruta.getParent(),"impresion-",".tmp");
            try(OutputStream out=Files.newOutputStream(temporal)){p.store(out,"Serena Soft - preferencias locales de impresion");}
            try{Files.move(temporal,ruta,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}catch(AtomicMoveNotSupportedException ex){Files.move(temporal,ruta,StandardCopyOption.REPLACE_EXISTING);}
            estado.setText("Preferencias locales guardadas. No cambian la impresion de Ventas todavia.");
        }catch(IOException ex){mostrarError("No se pudo guardar: "+ex.getMessage());}
    }
    private void imprimirPrueba() {
        String nombre=(String)impresoras.getSelectedItem();if(nombre==null){mostrarError("No hay impresora seleccionada.");return;}
        PrintService elegida=null;for(PrintService p:PrintServiceLookup.lookupPrintServices(null,null))if(nombre.equals(p.getName())){elegida=p;break;}
        if(elegida==null){mostrarError("La impresora ya no esta disponible.");return;}
        PrinterJob trabajo=PrinterJob.getPrinterJob();
        try{trabajo.setPrintService(elegida);trabajo.setJobName("Serena Soft - prueba de impresion");
            trabajo.setPrintable((g,f,i)->{if(i>0)return Printable.NO_SUCH_PAGE;((Graphics2D)g).translate(f.getImageableX(),f.getImageableY());g.setFont(new Font("SansSerif",Font.BOLD,11));g.drawString("SERENA SOFT - PRUEBA",10,24);g.setFont(new Font("SansSerif",Font.PLAIN,9));g.drawString("Impresora: "+nombre,10,42);g.drawString("No es un comprobante fiscal.",10,58);return Printable.PAGE_EXISTS;});
            trabajo.setCopies((Integer)copias.getValue());trabajo.print();estado.setText("Trabajo de prueba enviado a: "+nombre);
        }catch(PrinterException ex){mostrarError("Error al imprimir: "+ex.getMessage());}
    }
    private void mostrarError(String s){estado.setText(s);JOptionPane.showMessageDialog(this,s,"Impresion",JOptionPane.ERROR_MESSAGE);}
}
