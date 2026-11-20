package Dialogos;

import Dao.DepositoDao;
import Dao.TransferenciaStockDao;
import Configuracion.conexion;
import model.Deposito;
import sesion.SesionUsuario;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/** Despacho y recepción de transferencias. No permite acreditación sin recepción. */
public class DialogoTransferenciasStock extends JDialog {
    private final TransferenciaStockDao dao=new TransferenciaStockDao();
    private final JComboBox<Deposito> origen=new JComboBox<>(),destino=new JComboBox<>();
    private final JComboBox<ProductoOpcion> producto=new JComboBox<>();
    private final JTextField motivo=new JTextField("Reposición de sucursal"),cantidad=new JTextField("1");
    private final JTextArea observaciones=new JTextArea(3,25),notaRecepcion=new JTextArea(2,25);
    private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"ID","Código","Producto","Cantidad"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(modelo);
    private final JComboBox<TransferenciaStockDao.Pendiente> pendientes=new JComboBox<>();
    private final JTextArea detalle=new JTextArea(9,45);
    private static final Color AZUL=new Color(18,51,107);
    private static class ProductoOpcion {
        int id;String codigo,nombre;BigDecimal disponible;
        ProductoOpcion(int id,String codigo,String nombre,BigDecimal disponible){this.id=id;this.codigo=codigo;this.nombre=nombre;this.disponible=disponible;}
        @Override public String toString(){return codigo+" - "+nombre+" (disp. "+disponible.stripTrailingZeros().toPlainString()+")";}
    }
    private static final Color FONDO=new Color(241,245,250), BLANCO=Color.WHITE;
    private static final Color TEXTO=new Color(28,50,83), VERDE=new Color(20,135,88);
    private static final Color ROJO=new Color(185,61,65), BORDE=new Color(207,219,235);
    private JTabbedPane tabs;
    private JButton btnAgregar,btnEnviar,btnQuitar,btnRecibir,btnActualizar;
    private JLabel estadoEnvio,estadoRecepcion;

    public DialogoTransferenciasStock(Window parent) {
        super(parent,"Serena Soft | Transferencias entre depósitos",ModalityType.APPLICATION_MODAL);
        setSize(1030,740);setMinimumSize(new Dimension(870,640));setLocationRelativeTo(parent);
        tabs=new JTabbedPane();
        tabs.setFont(new Font("Segoe UI",Font.BOLD,13));
        tabs.setBackground(BLANCO);tabs.setForeground(AZUL);
        tabs.addTab("  1  DESPACHAR MERCADERÍA  ",armarEnvio());
        tabs.addTab("  2  CONFIRMAR RECEPCIÓN  ",armarRecepcion());
        add(tabs);cargarDepositos();cargarPendientes();actualizarBotones();
    }
    private JPanel base(){JPanel p=new JPanel(new BorderLayout(12,12));p.setBackground(FONDO);p.setBorder(new EmptyBorder(14,18,16,18));return p;}
    private JLabel titulo(String s){JLabel l=new JLabel(s);l.setForeground(BLANCO);l.setFont(new Font("Segoe UI",Font.BOLD,19));return l;}
    private JLabel texto(String s){JLabel l=new JLabel(s);l.setFont(new Font("Segoe UI",Font.PLAIN,13));l.setForeground(TEXTO);return l;}
    private JPanel cabecera(String t,String subtitulo){
        JPanel p=new JPanel(new BorderLayout(0,4));p.setBackground(AZUL);p.setBorder(new EmptyBorder(15,18,15,18));
        p.add(titulo(t),BorderLayout.NORTH);
        JLabel sub=new JLabel(subtitulo);sub.setForeground(new Color(217,232,252));sub.setFont(new Font("Segoe UI",Font.PLAIN,12));p.add(sub,BorderLayout.SOUTH);
        return p;
    }
    private JPanel tarjeta(String titulo){
        JPanel p=new JPanel(new BorderLayout(0,10));p.setBackground(BLANCO);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(12,14,12,14)));
        JLabel l=texto(titulo);l.setFont(new Font("Segoe UI",Font.BOLD,13));l.setForeground(AZUL);p.add(l,BorderLayout.NORTH);return p;
    }
    private JPanel fila(String etiqueta,JComponent campo){
        JPanel p=new JPanel(new BorderLayout(12,0));p.setOpaque(false);JLabel l=texto(etiqueta);
        l.setPreferredSize(new Dimension(155,34));p.add(l,BorderLayout.WEST);p.add(campo,BorderLayout.CENTER);
        campo.setFont(new Font("Segoe UI",Font.PLAIN,13));return p;
    }
    private JButton boton(String nombre,Color fondo,Runnable accion){
        JButton b=new JButton(nombre){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg=isEnabled()?fondo:new Color(217,224,234);g2.setColor(bg);g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(BLANCO);b.setBackground(fondo);
        b.setOpaque(false);b.setContentAreaFilled(false);
        b.setBorderPainted(false);b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(172,39));b.addActionListener(e->accion.run());return b;
    }
    private JPanel armarEnvio(){
        JPanel p=base();p.add(cabecera("DESPACHAR TRANSFERENCIA","La mercadería saldrá del origen y quedará en tránsito hasta confirmar la recepción"),BorderLayout.NORTH);
        JPanel cuerpo=new JPanel(new BorderLayout(0,12));cuerpo.setOpaque(false);
        JPanel form=tarjeta("DATOS DE LA TRANSFERENCIA");
        JPanel campos=new JPanel();campos.setOpaque(false);campos.setLayout(new BoxLayout(campos,BoxLayout.Y_AXIS));
        campos.add(fila("Depósito de origen",origen));campos.add(Box.createVerticalStrut(6));
        campos.add(fila("Depósito de destino",destino));campos.add(Box.createVerticalStrut(6));
        campos.add(fila("Motivo",motivo));campos.add(Box.createVerticalStrut(6));
        JScrollPane obs=new JScrollPane(observaciones);obs.setPreferredSize(new Dimension(200,58));
        campos.add(fila("Observaciones",obs));
        form.add(campos,BorderLayout.CENTER);cuerpo.add(form,BorderLayout.NORTH);
        JPanel productos=tarjeta("PRODUCTOS QUE SE ENVÍAN");
        JPanel contenido=new JPanel(new BorderLayout(0,10));contenido.setOpaque(false);
        JPanel seleccion=new JPanel(new BorderLayout(10,6));seleccion.setOpaque(false);
        seleccion.add(fila("Producto disponible",producto),BorderLayout.NORTH);
        JPanel cant=new JPanel(new BorderLayout(10,0));cant.setOpaque(false);cant.add(cantidad,BorderLayout.CENTER);
        btnAgregar=boton("+ AGREGAR",new Color(24,116,156),this::agregar);cant.add(btnAgregar,BorderLayout.EAST);
        seleccion.add(fila("Cantidad a enviar",cant),BorderLayout.SOUTH);contenido.add(seleccion,BorderLayout.NORTH);
        tabla.setRowHeight(31);tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));tabla.setGridColor(new Color(231,237,245));
        tabla.setSelectionBackground(new Color(212,226,247));tabla.setSelectionForeground(TEXTO);
        tabla.getTableHeader().setBackground(AZUL);tabla.getTableHeader().setForeground(BLANCO);
        tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));tabla.getTableHeader().setPreferredSize(new Dimension(0,34));
        tabla.setAutoCreateRowSorter(false);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);tabla.getColumnModel().getColumn(0).setMaxWidth(0);tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(125);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(115);
        JScrollPane sc=new JScrollPane(tabla);sc.setBorder(BorderFactory.createLineBorder(BORDE));contenido.add(sc,BorderLayout.CENTER);
        productos.add(contenido,BorderLayout.CENTER);cuerpo.add(productos,BorderLayout.CENTER);
        p.add(cuerpo,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout(10,0));pie.setOpaque(false);
        estadoEnvio=texto("Seleccioná dos depósitos diferentes para habilitar el envío.");
        pie.add(estadoEnvio,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));acciones.setOpaque(false);
        btnQuitar=boton("QUITAR PRODUCTO",ROJO,()->{int i=tabla.getSelectedRow();if(i>=0)modelo.removeRow(i);actualizarBotones();});
        btnEnviar=boton("CONFIRMAR DESPACHO",VERDE,this::enviar);
        acciones.add(btnQuitar);acciones.add(btnEnviar);pie.add(acciones,BorderLayout.EAST);p.add(pie,BorderLayout.SOUTH);
        origen.addActionListener(e->{cargarProductos();actualizarBotones();});
        destino.addActionListener(e->actualizarBotones());
        producto.addActionListener(e->actualizarBotones());
        tabla.getSelectionModel().addListSelectionListener(e->actualizarBotones());
        modelo.addTableModelListener(e->actualizarBotones());
        return p;
    }
    private JPanel armarRecepcion(){
        JPanel p=base();p.add(cabecera("CONFIRMAR RECEPCIÓN","Verificá las cantidades físicamente antes de acreditar mercadería al depósito destino"),BorderLayout.NORTH);
        JPanel cuerpo=new JPanel(new BorderLayout(0,12));cuerpo.setOpaque(false);
        JPanel cab=tarjeta("TRANSFERENCIAS EN TRÁNSITO");
        JPanel seleccion=new JPanel(new BorderLayout(10,0));seleccion.setOpaque(false);
        seleccion.add(pendientes,BorderLayout.CENTER);
        btnActualizar=boton("ACTUALIZAR",new Color(24,116,156),this::cargarPendientes);seleccion.add(btnActualizar,BorderLayout.EAST);
        cab.add(seleccion,BorderLayout.CENTER);cuerpo.add(cab,BorderLayout.NORTH);
        JPanel detalleCard=tarjeta("DETALLE DE LA MERCADERÍA ENVIADA");
        detalle.setEditable(false);detalle.setFont(new Font("Consolas",Font.PLAIN,13));detalle.setBackground(BLANCO);
        detalle.setForeground(TEXTO);detalle.setMargin(new Insets(12,12,12,12));
        detalleCard.add(new JScrollPane(detalle),BorderLayout.CENTER);cuerpo.add(detalleCard,BorderLayout.CENTER);
        JPanel nota=tarjeta("OBSERVACIONES DE RECEPCIÓN");
        nota.add(new JScrollPane(notaRecepcion),BorderLayout.CENTER);nota.setPreferredSize(new Dimension(0,115));
        cuerpo.add(nota,BorderLayout.SOUTH);p.add(cuerpo,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout(10,0));pie.setOpaque(false);
        estadoRecepcion=texto("No hay transferencias pendientes de recepción.");pie.add(estadoRecepcion,BorderLayout.CENTER);
        btnRecibir=boton("CONFIRMAR RECEPCIÓN",VERDE,this::recibir);
        btnRecibir.setPreferredSize(new Dimension(230,42));pie.add(btnRecibir,BorderLayout.EAST);p.add(pie,BorderLayout.SOUTH);
        pendientes.addActionListener(e->{mostrarPendiente();actualizarBotones();});return p;
    }
    private void actualizarBotones(){
        if(btnEnviar==null || btnRecibir==null)return;
        Deposito a=(Deposito)origen.getSelectedItem(),b=(Deposito)destino.getSelectedItem();
        boolean diferentes=a!=null && b!=null && a.getIdDeposito()!=b.getIdDeposito();
        boolean hayItems=modelo.getRowCount()>0;
        btnAgregar.setEnabled(diferentes && producto.getSelectedItem()!=null);
        btnQuitar.setEnabled(tabla.getSelectedRow()>=0);
        btnEnviar.setEnabled(diferentes && hayItems);
        if(!diferentes){estadoEnvio.setText(origen.getItemCount()<2?"Para transferir, primero debe existir un segundo depósito activo.":"Elegí depósitos de origen y destino diferentes.");}
        else if(!hayItems)estadoEnvio.setText("Agregá uno o más productos para habilitar el despacho.");
        else estadoEnvio.setText("Productos listos: "+modelo.getRowCount()+". Revisá cantidades antes de despachar.");
        boolean hayPendiente=pendientes.getSelectedItem()!=null;
        btnRecibir.setEnabled(hayPendiente);
        estadoRecepcion.setText(hayPendiente?"Confirmá únicamente cuando se haya recibido la totalidad de la mercadería.":"No hay transferencias pendientes de recepción.");
    }
    private void cargarDepositos(){
        origen.removeAllItems();destino.removeAllItems();
        for(Deposito d:new DepositoDao().listarActivos()){origen.addItem(d);destino.addItem(d);}
        if(destino.getItemCount()>1)destino.setSelectedIndex(1);
        cargarProductos();actualizarBotones();
    }
    private void cargarProductos(){
        producto.removeAllItems();Deposito d=(Deposito)origen.getSelectedItem();if(d==null)return;
        String sql="SELECT p.id_producto,p.codigo,p.nombre,s.cantidad FROM stock_producto s JOIN producto p ON p.id_producto=s.id_producto WHERE s.id_deposito=? AND s.cantidad>0 AND p.activo=1 ORDER BY p.nombre";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,d.getIdDeposito());try(ResultSet r=ps.executeQuery()){while(r.next())producto.addItem(new ProductoOpcion(r.getInt(1),r.getString(2),r.getString(3),r.getBigDecimal(4)));}
        }catch(SQLException ex){error(ex);}
    }
    private void agregar(){
        ProductoOpcion o=(ProductoOpcion)producto.getSelectedItem();if(o==null){aviso("Seleccioná un producto con stock.");return;}
        try{
            BigDecimal n=new BigDecimal(cantidad.getText().trim().replace(',','.'));
            if(n.signum()<=0 || n.scale()>3 || n.compareTo(o.disponible)>0)throw new NumberFormatException();
            for(int i=0;i<modelo.getRowCount();i++)if((int)modelo.getValueAt(i,0)==o.id){aviso("El producto ya está agregado. Quitalo para cambiar su cantidad.");return;}
            modelo.addRow(new Object[]{o.id,o.codigo,o.nombre,n});actualizarBotones();
        }catch(NumberFormatException e){aviso("Cantidad inválida o superior al stock mostrado (máximo 3 decimales).");}
    }
    private void enviar(){
        Deposito a=(Deposito)origen.getSelectedItem(),b=(Deposito)destino.getSelectedItem();
        if(a==null || b==null || a.getIdDeposito()==b.getIdDeposito()){aviso("Elegí dos depósitos diferentes y activos.");return;}
        if(modelo.getRowCount()==0){aviso("Agregá al menos un producto.");return;}
        List<TransferenciaStockDao.Item> items=new ArrayList<>();
        for(int i=0;i<modelo.getRowCount();i++)items.add(new TransferenciaStockDao.Item((int)modelo.getValueAt(i,0),(BigDecimal)modelo.getValueAt(i,3)));
        int confirmar=JOptionPane.showConfirmDialog(this,"¿Despachar "+items.size()+" productos desde "+a.getNombre()+" hacia "+b.getNombre()+"?\nSe descontarán del origen, pero NO se acreditarán en destino hasta la recepción.","Confirmar envío",JOptionPane.YES_NO_OPTION);
        if(confirmar!=JOptionPane.YES_OPTION)return;
        try{
            long id=dao.enviar(a.getIdDeposito(),b.getIdDeposito(),SesionUsuario.getIdUsuario(),motivo.getText(),observaciones.getText(),items);
            modelo.setRowCount(0);cargarProductos();cargarPendientes();actualizarBotones();
            JOptionPane.showMessageDialog(this,"Transferencia TR-"+String.format("%06d",id)+" despachada.\nEstado: EN TRÁNSITO.\nResponsable: "+SesionUsuario.getNombreUsuario());
        }catch(SQLException e){error(e);}
    }
    private void cargarPendientes(){
        pendientes.removeAllItems();try{for(TransferenciaStockDao.Pendiente p:dao.listarEnTransito())pendientes.addItem(p);}
        catch(SQLException e){error(e);}mostrarPendiente();actualizarBotones();
    }
    private void mostrarPendiente(){
        TransferenciaStockDao.Pendiente p=(TransferenciaStockDao.Pendiente)pendientes.getSelectedItem();
        if(p==null){detalle.setText("No hay transferencias pendientes de recepción.\n\nCuando se despache una transferencia, aparecerá aquí para su control y recepción.");return;}
        try{StringBuilder b=new StringBuilder(p.toString()).append("\n\nCÓDIGO | PRODUCTO | CANTIDAD ENVIADA\n\n");
            for(String s:dao.detalle(p.id))b.append(s).append('\n');
            b.append("\nVerificá que llegaron TODAS las unidades antes de confirmar.\nSi hay faltantes, NO confirmes: registrá la incidencia por separado.");detalle.setText(b.toString());detalle.setCaretPosition(0);
        }catch(SQLException e){error(e);}
    }
    private void recibir(){
        TransferenciaStockDao.Pendiente p=(TransferenciaStockDao.Pendiente)pendientes.getSelectedItem();if(p==null){aviso("No hay transferencias para recibir.");return;}
        int ok=JOptionPane.showConfirmDialog(this,"¿Confirmás que recibiste la totalidad de la transferencia TR-"+p.id+"?\nEsto incorporará los productos al depósito destino y dejará el registro a tu nombre.","Recepción completa",JOptionPane.YES_NO_OPTION);
        if(ok!=JOptionPane.YES_OPTION)return;
        try{dao.recibir(p.id,SesionUsuario.getIdUsuario(),notaRecepcion.getText());notaRecepcion.setText("");cargarPendientes();cargarProductos();JOptionPane.showMessageDialog(this,"Recepción confirmada. Inventario destino actualizado.");}
        catch(SQLException e){error(e);}
    }
    private void aviso(String s){JOptionPane.showMessageDialog(this,s,"Serena Soft",JOptionPane.WARNING_MESSAGE);}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error de transferencias",JOptionPane.ERROR_MESSAGE);}
}
