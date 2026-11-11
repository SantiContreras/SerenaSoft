package Paneles;

import Configuracion.conexion;
import Dialogos.DialogoNuevoArticulo;
import Dialogos.DialogoEditarArticulo;
import Dialogos.DialogoPromociones;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

/** Catálogo de artículos real, con edición y baja lógica. */
public class PanelArticulos extends JPanel {
    private static final Color AZUL=new Color(20,57,117), FONDO=new Color(245,248,253), VERDE=new Color(22,132,91);
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"ID","Código","Código de barras","Artículo","Categoría","Costo","Precio","Stock","Estado"},0){
        @Override public boolean isCellEditable(int r,int c){return false;}
        @Override public Class<?> getColumnClass(int c){return c==0?Integer.class:Object.class;}
    };
    private final JTable tabla=new JTable(modelo);
    private final JTextField buscar=new JTextField();
    private final JComboBox<String> categoria=new JComboBox<>(),estado=new JComboBox<>(new String[]{"Todos","Activos","Inactivos"});
    private final Map<String,Integer> categorias=new HashMap<>();
    private final JLabel resumen=new JLabel("Cargando artículos...");
    private final JButton editar=boton("EDITAR",AZUL), cambiar=boton("DESACTIVAR",new Color(177,68,66));
    private final NumberFormat moneda=NumberFormat.getCurrencyInstance(new Locale("es","AR"));
    public PanelArticulos(){
        setLayout(new BorderLayout(0,10));setBackground(FONDO);setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        JPanel cab=new JPanel(new BorderLayout(0,14));cab.setBackground(Color.WHITE);cab.setBorder(BorderFactory.createEmptyBorder(18,20,18,20));
        JLabel titulo=new JLabel("GESTIÓN DE ARTÍCULOS");titulo.setFont(new Font("Segoe UI",Font.BOLD,23));titulo.setForeground(AZUL);
        JLabel subtitulo=new JLabel("Catálogo, precios, existencias y promociones de Serena Soft");subtitulo.setForeground(new Color(95,110,130));
        JPanel encabezado=new JPanel(new GridLayout(2,1,0,3));encabezado.setOpaque(false);encabezado.add(titulo);encabezado.add(subtitulo);cab.add(encabezado,BorderLayout.NORTH);
        JPanel filtros=new JPanel(new GridBagLayout());filtros.setOpaque(false);GridBagConstraints g=new GridBagConstraints();g.gridy=0;g.insets=new Insets(0,0,0,10);g.fill=GridBagConstraints.HORIZONTAL;
        g.gridx=0;g.weightx=0;filtros.add(etiqueta("BUSCAR"),g);g.gridx=1;g.weightx=1;buscar.setPreferredSize(new Dimension(210,38));buscar.setFont(new Font("Segoe UI",Font.PLAIN,13));buscar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(170,190,218)),BorderFactory.createEmptyBorder(5,10,5,10)));buscar.setToolTipText("Código, nombre o código de barras");filtros.add(buscar,g);
        g.gridx=2;g.weightx=0;filtros.add(etiqueta("CATEGORÍA"),g);g.gridx=3;categoria.setPreferredSize(new Dimension(155,38));categoria.setFont(new Font("Segoe UI",Font.PLAIN,13));filtros.add(categoria,g);
        g.gridx=4;filtros.add(etiqueta("ESTADO"),g);g.gridx=5;estado.setPreferredSize(new Dimension(110,38));estado.setFont(new Font("Segoe UI",Font.PLAIN,13));filtros.add(estado,g);
        JButton limpiar=boton("LIMPIAR",new Color(91,105,124));g.gridx=6;filtros.add(limpiar,g);cab.add(filtros,BorderLayout.CENTER);add(cab,BorderLayout.NORTH);
        tabla.setRowHeight(38);tabla.setFont(new Font("Segoe UI",Font.PLAIN,13));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);tabla.setSelectionBackground(new Color(215,231,255));tabla.setSelectionForeground(AZUL);tabla.setShowVerticalLines(false);tabla.setGridColor(new Color(228,234,243));
        JTableHeader h=tabla.getTableHeader();h.setFont(new Font("Segoe UI",Font.BOLD,12));h.setPreferredSize(new Dimension(0,43));h.setDefaultRenderer(new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){super.getTableCellRendererComponent(t,v,selected,focus,row,col);setBackground(AZUL);setForeground(Color.WHITE);setOpaque(true);setFont(new Font("Segoe UI",Font.BOLD,12));setBorder(BorderFactory.createEmptyBorder(0,8,0,0));return this;}});
        tabla.getColumnModel().getColumn(0).setMinWidth(0);tabla.getColumnModel().getColumn(0).setMaxWidth(0);tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(270);
        tabla.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){super.getTableCellRendererComponent(t,v,selected,focus,row,col);setBorder(BorderFactory.createEmptyBorder(0,8,0,8));if(!selected){setBackground(row%2==0?Color.WHITE:new Color(248,250,254));setForeground(col==8? ("Activo".equals(v)?VERDE:new Color(181,67,67)):new Color(39,53,76));}return this;}});
        JScrollPane sc=new JScrollPane(tabla);sc.setBorder(BorderFactory.createLineBorder(new Color(219,227,239)));add(sc,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout(8,0));pie.setBackground(Color.WHITE);pie.setBorder(BorderFactory.createEmptyBorder(13,16,13,16));resumen.setForeground(AZUL);pie.add(resumen,BorderLayout.WEST);
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));acciones.setOpaque(false);
        JButton nuevo=boton("+ NUEVO",AZUL),promos=boton("PROMOCIONES 2 × 1",new Color(115,73,164)),actualizar=boton("ACTUALIZAR",new Color(84,100,121));
        acciones.add(promos);acciones.add(nuevo);acciones.add(editar);acciones.add(cambiar);acciones.add(actualizar);pie.add(acciones,BorderLayout.EAST);add(pie,BorderLayout.SOUTH);
        nuevo.addActionListener(e->{Window w=SwingUtilities.getWindowAncestor(this);DialogoNuevoArticulo d=new DialogoNuevoArticulo(w);d.setLocationRelativeTo(w);d.setVisible(true);if(d.isGuardado())recargar();});
        editar.addActionListener(e->{Integer id=idSeleccionado();if(id==null)return;DialogoEditarArticulo d=new DialogoEditarArticulo(SwingUtilities.getWindowAncestor(this),id);d.setVisible(true);if(d.isGuardado())recargar();});
        cambiar.addActionListener(e->cambiarEstado());actualizar.addActionListener(e->recargar());promos.addActionListener(e->{new DialogoPromociones(SwingUtilities.getWindowAncestor(this)).setVisible(true);recargar();});
        limpiar.addActionListener(e->{buscar.setText("");categoria.setSelectedIndex(0);estado.setSelectedIndex(0);});
        buscar.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){filtrar();}public void removeUpdate(DocumentEvent e){filtrar();}public void changedUpdate(DocumentEvent e){filtrar();}});
        categoria.addActionListener(e->filtrar());estado.addActionListener(e->filtrar());tabla.getSelectionModel().addListSelectionListener(e->actualizarBotones());
        tabla.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)editar.doClick();}});
        recargar();
    }
    private static JLabel etiqueta(String s){JLabel l=new JLabel(s);l.setForeground(AZUL);l.setFont(new Font("Segoe UI",Font.BOLD,11));return l;}
    private static JButton boton(String txt,Color color){
        JButton b=new JButton(txt){@Override protected void paintComponent(Graphics g){Graphics2D d=(Graphics2D)g.create();d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);Color c=isEnabled()?color:new Color(160,172,187);if(getModel().isPressed())c=c.darker();else if(getModel().isRollover())c=c.brighter();d.setColor(c);d.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);d.dispose();super.paintComponent(g);}};
        b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setContentAreaFilled(false);b.setOpaque(false);b.setBorder(BorderFactory.createEmptyBorder(10,13,10,13));b.setFocusPainted(false);b.setPreferredSize(new Dimension(txt.contains("PROMOCIONES")?177:txt.equals("ACTUALIZAR")?118:110,40));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;
    }
    private Integer idSeleccionado(){int r=tabla.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Seleccioná primero un artículo.");return null;}return (Integer)modelo.getValueAt(tabla.convertRowIndexToModel(r),0);}
    private void actualizarBotones(){int r=tabla.getSelectedRow();editar.setEnabled(r>=0);cambiar.setEnabled(r>=0);if(r>=0){String v=String.valueOf(tabla.getValueAt(r,8));cambiar.setText("Activo".equals(v)?"DESACTIVAR":"REACTIVAR");}else cambiar.setText("DESACTIVAR");}
    public final void recargar(){Integer previo=null;int sr=tabla.getSelectedRow();if(sr>=0)previo=(Integer)tabla.getValueAt(sr,0);
        try(Connection cn=conexion.getConexion()){
            categorias.clear();categoria.removeAllItems();categoria.addItem("Todas");
            try(PreparedStatement ps=cn.prepareStatement("SELECT id_categoria,nombre FROM categoria ORDER BY nombre");ResultSet rs=ps.executeQuery()){while(rs.next()){String n=rs.getString(2);categoria.addItem(n);categorias.put(n,rs.getInt(1));}}
            modelo.setRowCount(0);
            String sql="SELECT p.id_producto,p.codigo,p.codigo_barra,p.nombre,COALESCE(c.nombre,'Sin categoría') categoria,p.precio_compra,p.precio_venta,COALESCE(SUM(s.cantidad),0) stock,p.activo FROM producto p LEFT JOIN categoria c ON c.id_categoria=p.id_categoria LEFT JOIN stock_producto s ON s.id_producto=p.id_producto GROUP BY p.id_producto,p.codigo,p.codigo_barra,p.nombre,c.nombre,p.precio_compra,p.precio_venta,p.activo ORDER BY p.nombre";
            try(PreparedStatement ps=cn.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){modelo.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),moneda.format(rs.getBigDecimal(6)),moneda.format(rs.getBigDecimal(7)),rs.getBigDecimal(8).stripTrailingZeros().toPlainString(),rs.getBoolean(9)?"Activo":"Inactivo"});}}
            if(previo!=null)for(int i=0;i<modelo.getRowCount();i++)if(previo.equals(modelo.getValueAt(i,0))){int vista=tabla.convertRowIndexToView(i);if(vista>=0)tabla.setRowSelectionInterval(vista,vista);break;}
            filtrar();
        }catch(SQLException ex){resumen.setText("Error de conexión");JOptionPane.showMessageDialog(this,"No se pudo cargar el catálogo:\n"+ex.getMessage(),"Serena Soft",JOptionPane.ERROR_MESSAGE);}actualizarBotones();
    }
    private void filtrar(){if(tabla==null)return;String q=buscar.getText().trim().toLowerCase(Locale.ROOT);String cat=String.valueOf(categoria.getSelectedItem());String est=String.valueOf(estado.getSelectedItem());TableRowSorter<DefaultTableModel> sorter=new TableRowSorter<>(modelo);tabla.setRowSorter(sorter);sorter.setRowFilter(new RowFilter<DefaultTableModel,Integer>(){@Override public boolean include(Entry<? extends DefaultTableModel,? extends Integer> e){int r=e.getIdentifier();boolean coincide=q.isEmpty();for(int i:new int[]{1,2,3})if(String.valueOf(modelo.getValueAt(r,i)).toLowerCase(Locale.ROOT).contains(q))coincide=true;return coincide&&("Todas".equals(cat)||cat.equals(modelo.getValueAt(r,4)))&&("Todos".equals(est)||("Activos".equals(est)?"Activo":"Inactivo").equals(modelo.getValueAt(r,8)));}});resumen.setText("Artículos visibles: "+tabla.getRowCount()+" / "+modelo.getRowCount());actualizarBotones();}
    private void cambiarEstado(){Integer id=idSeleccionado();if(id==null)return;int row=tabla.getSelectedRow();boolean activo="Activo".equals(tabla.getValueAt(row,8));String accion=activo?"desactivar":"reactivar";if(JOptionPane.showConfirmDialog(this,"¿Querés "+accion+" este artículo?\nNo se borrarán ventas ni movimientos.","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("UPDATE producto SET activo=? WHERE id_producto=? AND activo=?")){ps.setBoolean(1,!activo);ps.setInt(2,id);ps.setBoolean(3,activo);if(ps.executeUpdate()!=1)throw new SQLException("El estado del artículo cambió. Actualizá la lista.");recargar();}catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}}
}
