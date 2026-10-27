package Paneles;

import Dao.HistorialComprasDao;
import Dao.HistorialComprasDao.CompraFila;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import rojeru_san.rsbutton.RSButtonRound;

/** Historial y detalle de compras. Solo lectura, salvo la carga explícita de adjuntos. */
public class PanelHistorialCompras extends JPanel {
    private static final Color AZUL=new Color(8,32,82), AZUL_BOTON=new Color(25,70,150), FONDO=new Color(245,247,250), TEXTO=new Color(35,49,72);
    private static final Color BORDE=new Color(216,226,239), FRANJA=new Color(247,250,254);
    private final HistorialComprasDao dao=new HistorialComprasDao();
    private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"ID","Fecha","Proveedor","Comprobante","Letra","Estado","Total"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(modelo);
    private final JTextField buscar=new JTextField();
    private final JCheckBox confirmadas=new JCheckBox("Solo confirmadas",true);
    private final JLabel resumen=new JLabel("Cargando compras...");
    private List<CompraFila> compras=new ArrayList<>();
    private final DecimalFormat dinero=new DecimalFormat("$ #,##0.00",DecimalFormatSymbols.getInstance(new Locale("es","AR")));
    private final SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public PanelHistorialCompras(){
        setLayout(new BorderLayout(0,14));setBackground(FONDO);setBorder(new EmptyBorder(22,24,22,24));
        JPanel arriba=new JPanel(new BorderLayout(0,12));arriba.setOpaque(false);
        JLabel titulo=new JLabel("Historial de compras");titulo.setFont(new Font("Segoe UI",Font.BOLD,26));titulo.setForeground(AZUL);
        JLabel subtitulo=new JLabel("Compras por proveedor, comprobantes, productos e impuestos");subtitulo.setFont(new Font("Segoe UI",Font.PLAIN,13));subtitulo.setForeground(new Color(100,115,135));
        JPanel titulos=new JPanel(new GridLayout(2,1,0,3));titulos.setOpaque(false);titulos.add(titulo);titulos.add(subtitulo);arriba.add(titulos,BorderLayout.NORTH);
        JPanel filtros=new JPanel(new BorderLayout(10,0));filtros.setOpaque(false);
        buscar.setFont(new Font("Segoe UI",Font.PLAIN,14));buscar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(212,221,233)),new EmptyBorder(9,12,9,12)));
        buscar.setToolTipText("Buscar proveedor, comprobante o ID de compra");
        buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){cargar();}public void removeUpdate(javax.swing.event.DocumentEvent e){cargar();}public void changedUpdate(javax.swing.event.DocumentEvent e){cargar();}});
        filtros.add(buscar,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));acciones.setOpaque(false);
        confirmadas.setOpaque(false);confirmadas.setFont(new Font("Segoe UI",Font.PLAIN,12));confirmadas.addActionListener(e->cargar());acciones.add(confirmadas);
        RSButtonRound refrescar=boton("ACTUALIZAR",120);refrescar.addActionListener(e->cargar());acciones.add(refrescar);
        RSButtonRound ver=boton("VER DETALLE",135);ver.addActionListener(e->abrirDetalle());acciones.add(ver);
        filtros.add(acciones,BorderLayout.EAST);arriba.add(filtros,BorderLayout.SOUTH);add(arriba,BorderLayout.NORTH);
        estilizar(tabla);tabla.setAutoCreateRowSorter(true);tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.addMouseListener(new MouseAdapter(){@Override public void mouseClicked(MouseEvent e){if(e.getClickCount()==2)abrirDetalle();}});
        tabla.getColumnModel().getColumn(0).setPreferredWidth(45);tabla.getColumnModel().getColumn(2).setPreferredWidth(240);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(180);
        JScrollPane scroll=scrollTabla(tabla);add(scroll,BorderLayout.CENTER);
        resumen.setForeground(TEXTO);resumen.setFont(new Font("Segoe UI",Font.BOLD,13));add(resumen,BorderLayout.SOUTH);
        cargar();
    }
    private static RSButtonRound boton(String texto,int ancho){
        RSButtonRound b=new RSButtonRound();b.setText(texto);b.setBackground(AZUL_BOTON);b.setColorHover(new Color(55,115,220));b.setColorText(Color.WHITE);b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,11));b.setPreferredSize(new Dimension(ancho,38));b.setFocusable(false);return b;
    }
    private static void estilizar(JTable t){
        t.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t.setForeground(TEXTO);t.setBackground(Color.WHITE);
        t.setRowHeight(38);t.setFillsViewportHeight(true);
        t.setShowVerticalLines(false);t.setShowHorizontalLines(true);
        t.setGridColor(new Color(233,239,247));
        t.setSelectionBackground(new Color(217,232,253));t.setSelectionForeground(AZUL);
        t.setIntercellSpacing(new Dimension(0,1));
        JTableHeader h=t.getTableHeader();
        h.setReorderingAllowed(false);h.setResizingAllowed(true);
        h.setPreferredSize(new Dimension(0,42));
        h.setOpaque(true);h.setBackground(AZUL);h.setForeground(Color.WHITE);
        h.setFont(new Font("Segoe UI",Font.BOLD,12));
        // Nimbus ignora a veces setBackground() en JTableHeader.
        // Un renderer propio garantiza fondo azul y titulos blancos.
        h.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focus,int row,int column){
                JLabel l=(JLabel)super.getTableCellRendererComponent(table,value,selected,focus,row,column);
                l.setOpaque(true);l.setBackground(AZUL);l.setForeground(Color.WHITE);
                l.setFont(new Font("Segoe UI",Font.BOLD,12));
                l.setHorizontalAlignment(SwingConstants.LEFT);
                l.setBorder(new EmptyBorder(0,12,0,6));
                return l;
            }
        });
        t.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focus,int row,int column){
                JLabel l=(JLabel)super.getTableCellRendererComponent(table,value,selected,focus,row,column);
                l.setOpaque(true);l.setFont(new Font("Segoe UI",Font.PLAIN,13));
                l.setForeground(selected?AZUL:TEXTO);
                l.setBackground(selected?new Color(217,232,253):(row%2==0?Color.WHITE:FRANJA));
                l.setBorder(new EmptyBorder(0,10,0,8));
                l.setHorizontalAlignment(value instanceof Number?SwingConstants.RIGHT:SwingConstants.LEFT);
                return l;
            }
        });
    }
    private static JScrollPane scrollTabla(JTable tabla){
        JScrollPane s=new JScrollPane(tabla);
        s.setBorder(BorderFactory.createLineBorder(BORDE));
        s.getViewport().setBackground(Color.WHITE);
        return s;
    }
    private static String numero(Object o){
        if(o==null)return "-";
        if(o instanceof java.math.BigDecimal){
            return new DecimalFormat("#,##0.###",DecimalFormatSymbols.getInstance(new Locale("es","AR"))).format(o);
        }
        return String.valueOf(o);
    }
    private static String importe(Object o){
        if(o==null)return "-";
        return new DecimalFormat("$ #,##0.00",DecimalFormatSymbols.getInstance(new Locale("es","AR"))).format(o);
    }
    private static String comprobante(String s){return s==null||s.isBlank()?"Sin numero":s;}
    private static void anchos(JTable t,int... anchos){
        t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i=0;i<anchos.length&&i<t.getColumnCount();i++){
            t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
    private void cargar(){
        String filtro=buscar.getText();boolean solo=confirmadas.isSelected();
        new SwingWorker<List<CompraFila>,Void>(){
            @Override protected List<CompraFila> doInBackground() throws Exception{return dao.listar(filtro,solo);}
            @Override protected void done(){try{
                compras=get();modelo.setRowCount(0);java.math.BigDecimal total=java.math.BigDecimal.ZERO;
                for(CompraFila c:compras){modelo.addRow(new Object[]{c.id,c.fecha==null?"":fecha.format(c.fecha),c.proveedor,c.comprobante==null?"Sin número":c.comprobante,c.letra,c.estado,dinero.format(c.total)});total=total.add(c.total);}
                resumen.setText(compras.size()+" compras encontradas  |  Total listado: "+dinero.format(total));
            }catch(Exception ex){resumen.setText("No se pudo consultar el historial");error(ex);}}
        }.execute();
    }
    private void abrirDetalle(){
        int fila=tabla.getSelectedRow();if(fila<0){JOptionPane.showMessageDialog(this,"Seleccioná una compra de la tabla.");return;}
        long id=((Number)modelo.getValueAt(tabla.convertRowIndexToModel(fila),0)).longValue();
        CompraFila seleccion=null;for(CompraFila c:compras)if(c.id==id){seleccion=c;break;}
        if(seleccion!=null)new DialogoDetalleCompra(SwingUtilities.getWindowAncestor(this),seleccion,dao).setVisible(true);
    }
    private void error(Exception ex){Throwable t=ex;while(t.getCause()!=null)t=t.getCause();JOptionPane.showMessageDialog(this,t.getMessage(),"Error de compras",JOptionPane.ERROR_MESSAGE);}
    private static class DialogoDetalleCompra extends JDialog{
        private final CompraFila compra;private final HistorialComprasDao dao;
        private final DefaultTableModel adjuntos=new DefaultTableModel(new Object[]{"ID","Archivo","Fecha","Bytes"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
        private final JTable tablaAdjuntos=new JTable(adjuntos);
        DialogoDetalleCompra(Window owner,CompraFila compra,HistorialComprasDao dao){
            super(owner,"Detalle de compra #"+compra.id,ModalityType.APPLICATION_MODAL);this.compra=compra;this.dao=dao;
            setSize(1040,660);setMinimumSize(new Dimension(820,520));setLocationRelativeTo(owner);
            JPanel root=new JPanel(new BorderLayout(0,14));root.setBackground(FONDO);root.setBorder(new EmptyBorder(18,20,18,20));setContentPane(root);
            JLabel titulo=new JLabel("Compra #"+compra.id+"  ·  "+compra.proveedor);titulo.setFont(new Font("Segoe UI",Font.BOLD,21));titulo.setForeground(AZUL);
            JLabel datos=new JLabel("Comprobante: "+comprobante(compra.comprobante)+"   |   Estado: "+compra.estado+"   |   Total: "+new DecimalFormat("$ #,##0.00",DecimalFormatSymbols.getInstance(new Locale("es","AR"))).format(compra.total));
            datos.setFont(new Font("Segoe UI",Font.PLAIN,13));datos.setForeground(TEXTO);
            JPanel cab=new JPanel(new GridLayout(2,1,0,5));cab.setOpaque(false);cab.add(titulo);cab.add(datos);root.add(cab,BorderLayout.NORTH);
            JTabbedPane tabs=new JTabbedPane();tabs.setFont(new Font("Segoe UI",Font.BOLD,13));tabs.setBackground(Color.WHITE);tabs.setForeground(AZUL);tabs.setOpaque(true);
            DefaultTableModel detalles=new DefaultTableModel(new Object[]{"Código","Producto","Presentación","Cantidad","Factor","Unidades stock","Costo","Subtotal"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
            JTable tablaDetalle=new JTable(detalles);estilizar(tablaDetalle);anchos(tablaDetalle,110,270,120,110,100,145,145,155);tabs.addTab("Productos comprados",scrollTabla(tablaDetalle));
            DefaultTableModel impuestos=new DefaultTableModel(new Object[]{"Alícuota %","Base imponible","IVA","Crédito computable"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
            JTable tablaIva=new JTable(impuestos);estilizar(tablaIva);anchos(tablaIva,160,210,190,210);tabs.addTab("Impuestos",scrollTabla(tablaIva));
            estilizar(tablaAdjuntos);tablaAdjuntos.setAutoCreateRowSorter(true);
            JPanel panelAdjuntos=new JPanel(new BorderLayout(0,10));panelAdjuntos.setBackground(Color.WHITE);anchos(tablaAdjuntos,75,550,185,120);panelAdjuntos.add(scrollTabla(tablaAdjuntos),BorderLayout.CENTER);
            JPanel botones=new JPanel(new FlowLayout(FlowLayout.RIGHT));botones.setOpaque(false);
            RSButtonRound agregar=boton("ADJUNTAR PDF / IMAGEN",200);agregar.addActionListener(e->adjuntar());botones.add(agregar);
            RSButtonRound abrir=boton("ABRIR ADJUNTO",150);abrir.addActionListener(e->abrir());botones.add(abrir);
            panelAdjuntos.add(botones,BorderLayout.SOUTH);tabs.addTab("Facturas adjuntas",panelAdjuntos);root.add(tabs,BorderLayout.CENTER);
            RSButtonRound cerrar=boton("CERRAR",120);cerrar.addActionListener(e->dispose());JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT));pie.setOpaque(false);pie.add(cerrar);root.add(pie,BorderLayout.SOUTH);
            try{for(Object[] r:dao.detalles(compra.id)){
                detalles.addRow(new Object[]{r[0],r[1],r[2],numero(r[3]),numero(r[4]),numero(r[5]),importe(r[6]),importe(r[7])});
            }
            for(Object[] r:dao.impuestos(compra.id)){
                impuestos.addRow(new Object[]{numero(r[0]),importe(r[1]),importe(r[2]),importe(r[3])});
            }
            cargarAdjuntos();}
            catch(SQLException ex){JOptionPane.showMessageDialog(this,"Error consultando compra: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
        }
        private void cargarAdjuntos(){try{adjuntos.setRowCount(0);for(Object[] r:dao.listarAdjuntos(compra.id))adjuntos.addRow(new Object[]{r[0],r[1],r[2],r[3]});}catch(SQLException ex){JOptionPane.showMessageDialog(this,"No se pudieron leer los adjuntos. Ejecutá primero el SQL de instalación.\n"+ex.getMessage());}}
        private void adjuntar(){
            JFileChooser chooser=new JFileChooser();chooser.setDialogTitle("Seleccionar factura PDF o imagen");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF, JPG, JPEG, PNG","pdf","jpg","jpeg","png"));
            if(chooser.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;
            File f=chooser.getSelectedFile();String n=f.getName().toLowerCase(Locale.ROOT);
            if(!(n.endsWith(".pdf")||n.endsWith(".jpg")||n.endsWith(".jpeg")||n.endsWith(".png"))){JOptionPane.showMessageDialog(this,"Solo PDF, JPG o PNG.");return;}
            if(f.length()>10L*1024*1024){JOptionPane.showMessageDialog(this,"Máximo 10 MB por archivo.");return;}
            try{String mime=n.endsWith(".pdf")?"application/pdf":n.endsWith(".png")?"image/png":"image/jpeg";
                dao.guardarAdjunto(compra.id,f.getName(),mime,Files.readAllBytes(f.toPath()));cargarAdjuntos();
                JOptionPane.showMessageDialog(this,"Factura adjuntada a la compra #"+compra.id);
            }catch(Exception ex){JOptionPane.showMessageDialog(this,"No se pudo adjuntar: "+ex.getMessage());}
        }
        private void abrir(){
            int fila=tablaAdjuntos.getSelectedRow();if(fila<0){JOptionPane.showMessageDialog(this,"Seleccioná un archivo.");return;}
            int i=tablaAdjuntos.convertRowIndexToModel(fila);long id=((Number)adjuntos.getValueAt(i,0)).longValue();String nombre=String.valueOf(adjuntos.getValueAt(i,1));
            String extension=nombre.toLowerCase(Locale.ROOT).endsWith(".pdf")?".pdf":nombre.toLowerCase(Locale.ROOT).endsWith(".png")?".png":".jpg";
            try{File temp=File.createTempFile("serena_factura_"+id+"_",extension);temp.deleteOnExit();Files.write(temp.toPath(),dao.leerAdjunto(id,compra.id));
                if(!Desktop.isDesktopSupported())throw new IllegalStateException("No hay visor de archivos disponible.");
                try{Desktop.getDesktop().open(temp);}
                catch(Exception visor){
                    // El problema puede ser Adobe Acrobat, no el archivo almacenado.
                    JFileChooser destino=new JFileChooser();
                    destino.setDialogTitle("El visor predeterminado fallo. Guardar una copia para abrir con otro programa");
                    destino.setSelectedFile(new File(nombre));
                    if(destino.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
                        File elegido=destino.getSelectedFile();
                        Files.copy(temp.toPath(),elegido.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(this,"Archivo guardado en:\n"+elegido.getAbsolutePath()+"\nAbrilo con Chrome, Edge u otro visor PDF.");
                    }else{
                        JOptionPane.showMessageDialog(this,"No se pudo abrir con el visor predeterminado: "+visor.getMessage());
                    }
                }
            }catch(Exception ex){JOptionPane.showMessageDialog(this,"No se pudo abrir: "+ex.getMessage());}
        }
    }
}
