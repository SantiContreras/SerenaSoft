package Paneles;

import Configuracion.conexion;
import Dao.ProveedorDao;
import Dialogos.DialogoEditarProveedor;
import Dialogos.DialogoFichaProveedor;
import Dialogos.DialogoNuevoProveedor;
import model.Proveedor;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Panel de proveedores: datos reales, búsqueda y acciones conservadas. */
public class PanelProveedores extends JPanel {
    private static final Color NAVY=new Color(15,48,104), BLUE=new Color(25,76,155), GREEN=new Color(12,136,76), RED=new Color(190,45,48), ORANGE=new Color(237,132,12), BORDER=new Color(216,227,240), BG=new Color(248,250,253);
    private final ProveedorDao dao=new ProveedorDao();
    private final JTextField buscar=new JTextField();
    private final JComboBox<String> estado=new JComboBox<>(new String[]{"Todos","Activos","Inactivos","Con deuda"});
    private final JLabel total=new JLabel("0"), activos=new JLabel("0"), deudores=new JLabel("0"), inactivos=new JLabel("0"), contador=new JLabel("Listado de Proveedores");
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"ID","CUIT","Razón Social / Nombre","Contacto","Teléfono","Estado","Última Compra","Saldo"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(modelo);
    private final Map<Integer,Proveedor> proveedores=new HashMap<>();
    private final Map<Integer,BigDecimal> saldos=new HashMap<>();
    private final Map<Integer,String> fechas=new HashMap<>();
    private final NumberFormat dinero=NumberFormat.getCurrencyInstance(new Locale("es","AR"));
    private final JButton nuevo=boton("NUEVO PROVEEDOR",GREEN,"plus"), editar=boton("EDITAR",BLUE,"edit"), ficha=boton("VER FICHA",ORANGE,"eye"), baja=boton("ACTIVAR / DAR DE BAJA",new Color(105,120,140),"check"), imprimir=boton("IMPRIMIR LISTADO",NAVY,"print");
    private final JButton buscarBtn=boton("BUSCAR",BLUE,"search"), actualizarBtn=boton("ACTUALIZAR",NAVY,"refresh");

    public PanelProveedores(){
        setLayout(new BorderLayout());setBackground(Color.WHITE);
        JPanel cuerpo=new JPanel(new BorderLayout(0,12));cuerpo.setBackground(Color.WHITE);cuerpo.setBorder(new EmptyBorder(14,24,12,24));
        JPanel cab=new JPanel(new BorderLayout(12,0));cab.setOpaque(false);
        JLabel icon=new JLabel(new VectorIcon("building",BLUE,30));icon.setHorizontalAlignment(SwingConstants.CENTER);icon.setPreferredSize(new Dimension(48,46));cab.add(icon,BorderLayout.WEST);
        JPanel tit=new JPanel(new GridLayout(2,1,0,2));tit.setOpaque(false);tit.add(label("Proveedores",25,NAVY,true));tit.add(label("Administración de proveedores, compras, pagos y cuentas comerciales",13,new Color(104,120,145),false));cab.add(tit,BorderLayout.CENTER);
        cuerpo.add(cab,BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout(0,12));centro.setOpaque(false);
        JPanel tarjetas=new JPanel(new GridLayout(1,4,12,0));tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("TOTAL DE PROVEEDORES",total,"building",new Color(232,242,255),BLUE));
        tarjetas.add(tarjeta("ACTIVOS",activos,"check",new Color(229,249,237),GREEN));
        tarjetas.add(tarjeta("CON DEUDA",deudores,"alert",new Color(255,238,238),RED));
        tarjetas.add(tarjeta("INACTIVOS",inactivos,"minus",new Color(239,242,247),new Color(104,116,136)));
        tarjetas.setPreferredSize(new Dimension(100,70));
        JPanel filtros=new JPanel(new BorderLayout(12,0));filtros.setBackground(Color.WHITE);filtros.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER,1,true),new EmptyBorder(9,12,9,12)));
        buscar.setFont(new Font("Segoe UI",Font.PLAIN,14));buscar.setToolTipText("Buscar por nombre, razón social, CUIT o contacto");buscar.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(176,201,235),1,true),new EmptyBorder(8,12,8,12)));
        JPanel derecha=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));derecha.setOpaque(false);derecha.add(label("Estado:",13,NAVY,true));estado.setPreferredSize(new Dimension(132,36));estado.setFont(new Font("Segoe UI",Font.PLAIN,13));estado.setBackground(Color.WHITE);estado.setForeground(NAVY);estado.setFocusable(false);derecha.add(estado);derecha.add(buscarBtn);derecha.add(actualizarBtn);
        filtros.add(buscar,BorderLayout.CENTER);filtros.add(derecha,BorderLayout.EAST);filtros.setPreferredSize(new Dimension(100,58));
        JPanel arriba=new JPanel(new BorderLayout(0,12));arriba.setOpaque(false);arriba.add(tarjetas,BorderLayout.NORTH);arriba.add(filtros,BorderLayout.SOUTH);
        centro.add(arriba,BorderLayout.NORTH);
        JPanel zonaTabla=new JPanel(new BorderLayout(0,8));zonaTabla.setOpaque(false);contador.setFont(new Font("Segoe UI",Font.BOLD,17));contador.setForeground(NAVY);zonaTabla.add(contador,BorderLayout.NORTH);
        tabla.setRowHeight(33);tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);tabla.setSelectionBackground(new Color(194,221,255));tabla.setSelectionForeground(NAVY);tabla.setShowVerticalLines(true);tabla.setGridColor(new Color(225,232,241));tabla.setFillsViewportHeight(true);tabla.setAutoCreateRowSorter(true);
        tabla.getTableHeader().setPreferredSize(new Dimension(0,39));tabla.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer header=new DefaultTableCellRenderer();header.setHorizontalAlignment(SwingConstants.CENTER);header.setOpaque(true);header.setBackground(NAVY);header.setForeground(Color.WHITE);header.setFont(new Font("Segoe UI",Font.BOLD,12));header.setBorder(new EmptyBorder(5,5,5,5));
        for(int i=0;i<tabla.getColumnCount();i++)tabla.getColumnModel().getColumn(i).setHeaderRenderer(header);
        tabla.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int row,int col){super.getTableCellRendererComponent(t,v,sel,focus,row,col);setBorder(new EmptyBorder(0,8,0,8));setBackground(sel?new Color(194,221,255):(row%2==0?Color.WHITE:new Color(246,249,253)));setForeground(NAVY);setFont(new Font("Segoe UI",Font.PLAIN,12));if(col==5){boolean activo="Activo".equals(v);setForeground(activo?GREEN:new Color(104,116,136));setFont(getFont().deriveFont(Font.BOLD));}if(col==7){BigDecimal s=(BigDecimal)v;setText(dinero.format(s));setHorizontalAlignment(RIGHT);setForeground(s.signum()>0?RED:GREEN);setFont(getFont().deriveFont(Font.BOLD));}else setHorizontalAlignment(col==0?CENTER:LEFT);return this;}});
        int[] anchos={44,140,240,145,125,85,105,145};for(int i=0;i<anchos.length;i++)tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        JScrollPane scroll=new JScrollPane(tabla);scroll.setBorder(new LineBorder(BORDER));zonaTabla.add(scroll,BorderLayout.CENTER);centro.add(zonaTabla,BorderLayout.CENTER);
        cuerpo.add(centro,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new BorderLayout());acciones.setOpaque(false);acciones.setBorder(new EmptyBorder(8,0,3,0));JPanel izq=new JPanel(new FlowLayout(FlowLayout.LEFT,9,0));izq.setOpaque(false);izq.add(nuevo);izq.add(editar);izq.add(ficha);izq.add(baja);acciones.add(izq,BorderLayout.WEST);acciones.add(imprimir,BorderLayout.EAST);acciones.setPreferredSize(new Dimension(100,55));cuerpo.add(acciones,BorderLayout.SOUTH);
        add(cuerpo,BorderLayout.CENTER);
        buscar.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e){filtrar();}
            @Override public void removeUpdate(DocumentEvent e){filtrar();}
            @Override public void changedUpdate(DocumentEvent e){filtrar();}
        });
        buscarBtn.addActionListener(e->filtrar());buscar.addActionListener(e->filtrar());estado.addActionListener(e->filtrar());actualizarBtn.addActionListener(e->cargar());
        nuevo.addActionListener(e->{DialogoNuevoProveedor d=new DialogoNuevoProveedor(SwingUtilities.getWindowAncestor(this));d.setVisible(true);cargar();});
        editar.addActionListener(e->{Proveedor p=seleccionado();if(p==null)return;DialogoEditarProveedor d=new DialogoEditarProveedor(SwingUtilities.getWindowAncestor(this),p.getIdProveedor(),p.getRazonSocial(),p.getCuit(),p.getTelefono(),p.isActivo()?"Activo":"Inactivo");d.setVisible(true);cargar();});
        ficha.addActionListener(e->abrirFicha());tabla.addMouseListener(new MouseAdapter(){@Override public void mouseClicked(MouseEvent e){if(e.getClickCount()==2)abrirFicha();}});
        baja.addActionListener(e->{Proveedor p=seleccionado();if(p==null)return;boolean activar=!p.isActivo();int r=JOptionPane.showConfirmDialog(this,(activar?"¿Activar":"¿Dar de baja")+" al proveedor "+p.getRazonSocial()+"?","Confirmar estado",JOptionPane.YES_NO_OPTION);if(r!=JOptionPane.YES_OPTION)return;try{dao.cambiarEstado(p.getIdProveedor(),activar);cargar();}catch(Exception ex){error(ex);}});
        imprimir.addActionListener(e->{try{tabla.print(JTable.PrintMode.FIT_WIDTH,new java.text.MessageFormat("SERENA SOFT - PROVEEDORES"),new java.text.MessageFormat("Página {0}"));}catch(Exception ex){error(ex);}});
        tabla.getRowSorter().addRowSorterListener(e -> contador.setText("Listado de Proveedores ("+tabla.getRowCount()+")"));
        SwingUtilities.invokeLater(this::cargar);
    }
    private void abrirFicha(){Proveedor p=seleccionado();if(p==null)return;DialogoFichaProveedor d=new DialogoFichaProveedor(SwingUtilities.getWindowAncestor(this),p.getIdProveedor(),p.getRazonSocial(),p.getCuit(),p.getTelefono(),p.isActivo()?"Activo":"Inactivo");d.setVisible(true);cargar();}
    private Proveedor seleccionado(){int row=tabla.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Seleccioná un proveedor de la tabla.");return null;}int id=((Number)modelo.getValueAt(tabla.convertRowIndexToModel(row),0)).intValue();return proveedores.get(id);}
    private void cargar(){int selected=-1;int row=tabla.getSelectedRow();if(row>=0)selected=((Number)modelo.getValueAt(tabla.convertRowIndexToModel(row),0)).intValue();try{
        List<Proveedor> lista=dao.listarTodos();proveedores.clear();saldos.clear();fechas.clear();modelo.setRowCount(0);
        try(Connection cn=conexion.getConexion()){
            // Se usan las mismas compras confirmadas y pagos que la ficha del proveedor.
            String sql="SELECT p.id_proveedor, COALESCE(c.comprado,0)-COALESCE(g.pagado,0) AS saldo, c.ultima " +
                "FROM proveedor p LEFT JOIN (SELECT id_proveedor,SUM(total) comprado,MAX(fecha) ultima FROM compra WHERE estado='CONFIRMADA' GROUP BY id_proveedor)c ON c.id_proveedor=p.id_proveedor " +
                "LEFT JOIN (SELECT id_proveedor,SUM(importe) pagado FROM pago_proveedor GROUP BY id_proveedor)g ON g.id_proveedor=p.id_proveedor";
            try(PreparedStatement ps=cn.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){int id=rs.getInt(1);BigDecimal s=rs.getBigDecimal(2);saldos.put(id,s==null?BigDecimal.ZERO:s.max(BigDecimal.ZERO));Timestamp f=rs.getTimestamp(3);fechas.put(id,f==null?"—":f.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));}}
        }
        for(Proveedor p:lista){int id=p.getIdProveedor();proveedores.put(id,p);modelo.addRow(new Object[]{id,nn(p.getCuit()),nn(p.getRazonSocial()),nn(p.getPersonaContacto()),nn(p.getTelefono()),p.isActivo()?"Activo":"Inactivo",fechas.getOrDefault(id,"—"),saldos.getOrDefault(id,BigDecimal.ZERO)});}
        int a=0,d=0;for(Proveedor p:lista){if(p.isActivo())a++;if(saldos.getOrDefault(p.getIdProveedor(),BigDecimal.ZERO).signum()>0)d++;}
        total.setText(""+lista.size());activos.setText(""+a);deudores.setText(""+d);inactivos.setText(""+(lista.size()-a));filtrar();
        if(selected>0)for(int i=0;i<modelo.getRowCount();i++)if(((Number)modelo.getValueAt(i,0)).intValue()==selected){int vr=tabla.convertRowIndexToView(i);if(vr>=0)tabla.setRowSelectionInterval(vr,vr);break;}
    }catch(Exception ex){error(ex);}}
    private void filtrar(){String q=buscar.getText().trim().toLowerCase(Locale.ROOT);String st=(String)estado.getSelectedItem();TableRowSorter<DefaultTableModel> sorter=(TableRowSorter<DefaultTableModel>)tabla.getRowSorter();sorter.setRowFilter(new RowFilter<DefaultTableModel,Integer>(){@Override public boolean include(Entry<? extends DefaultTableModel,? extends Integer> e){int r=e.getIdentifier();String datos=(""+modelo.getValueAt(r,1)+" "+modelo.getValueAt(r,2)+" "+modelo.getValueAt(r,3)+" "+modelo.getValueAt(r,4)).toLowerCase(Locale.ROOT);boolean ok=datos.contains(q);String es=""+modelo.getValueAt(r,5);BigDecimal s=(BigDecimal)modelo.getValueAt(r,7);return ok&&("Todos".equals(st)||("Activos".equals(st)&&"Activo".equals(es))||("Inactivos".equals(st)&&"Inactivo".equals(es))||("Con deuda".equals(st)&&s.signum()>0));}});contador.setText("Listado de Proveedores ("+tabla.getRowCount()+")");}
    private static String nn(String s){return s==null?"":s;}
    private void error(Exception ex){JOptionPane.showMessageDialog(this,"No se pudieron cargar o guardar los datos.\n"+ex.getMessage(),"Proveedores",JOptionPane.ERROR_MESSAGE);}
    private static JLabel label(String text,int size,Color color,boolean bold){JLabel l=new JLabel(text);l.setFont(new Font("Segoe UI",bold?Font.BOLD:Font.PLAIN,size));l.setForeground(color);return l;}
    private static JButton boton(String text,Color color,String glyph){
        JButton b=new JButton(text,new VectorIcon(glyph,Color.WHITE,17)){
            private boolean hover;
            {addMouseListener(new MouseAdapter(){@Override public void mouseEntered(MouseEvent e){hover=true;repaint();}@Override public void mouseExited(MouseEvent e){hover=false;repaint();}});}
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color c=isEnabled()?color:new Color(155,165,179);if(hover&&isEnabled())c=c.brighter();
                g2.setColor(c);g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,11,11);g2.dispose();super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(Color.WHITE);b.setBackground(color);
        b.setOpaque(false);b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);
        b.setHorizontalTextPosition(SwingConstants.RIGHT);b.setIconTextGap(8);
        b.setBorder(new EmptyBorder(0,13,0,13));b.setPreferredSize(new Dimension(Math.max(105,text.length()*7+49),38));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;
    }
    private static JPanel tarjeta(String name,JLabel value,String simbolo,Color fondo,Color color){
        JPanel p=new JPanel(new BorderLayout(9,0));p.setBackground(fondo);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(214,225,239),1,true),new EmptyBorder(7,12,7,12)));
        JLabel icon=new JLabel(new VectorIcon(simbolo,color,25));icon.setHorizontalAlignment(SwingConstants.CENTER);icon.setPreferredSize(new Dimension(35,35));p.add(icon,BorderLayout.WEST);
        JPanel t=new JPanel(new GridLayout(2,1));t.setOpaque(false);t.add(label(name,11,new Color(35,51,75),true));
        value.setFont(new Font("Segoe UI",Font.BOLD,22));value.setForeground(color);t.add(value);p.add(t,BorderLayout.CENTER);return p;
    }
    /** Iconos dibujados en Java2D: no dependen de fuentes de símbolos instaladas. */
    private static class VectorIcon implements Icon{
        private final String type;private final Color color;private final int size;
        VectorIcon(String type,Color color,int size){this.type=type;this.color=color;this.size=size;}
        @Override public int getIconWidth(){return size;}@Override public int getIconHeight(){return size;}
        @Override public void paintIcon(Component c,Graphics g,int x,int y){
            Graphics2D d=(Graphics2D)g.create();d.translate(x,y);d.scale(size/24.0,size/24.0);
            d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            d.setColor(color);d.setStroke(new BasicStroke(2.1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            switch(type){
                case "plus":d.drawLine(12,4,12,20);d.drawLine(4,12,20,12);break;
                case "edit":d.drawLine(5,18,17,6);d.drawLine(8,20,20,8);d.drawLine(16,5,19,8);d.drawLine(4,20,8,20);break;
                case "eye":d.drawOval(3,7,18,10);d.fillOval(10,10,4,4);break;
                case "check":d.drawLine(4,12,10,18);d.drawLine(10,18,21,5);break;
                case "minus":d.drawLine(5,12,19,12);break;
                case "alert":d.drawLine(12,4,12,15);d.fillOval(11,19,2,2);break;
                case "building":d.drawRoundRect(4,3,16,19,2,2);for(int r=0;r<3;r++)for(int col=0;col<2;col++)d.drawRect(7+col*6,6+r*5,2,2);break;
                case "search":d.drawOval(3,3,13,13);d.drawLine(15,15,21,21);break;
                case "refresh":d.drawArc(4,4,16,16,30,290);d.drawLine(18,4,20,10);d.drawLine(20,10,14,9);break;
                case "print":d.drawRect(6,3,12,6);d.drawRoundRect(3,9,18,10,2,2);d.drawRect(7,15,10,7);break;
            }
            d.dispose();
        }
    }
}
