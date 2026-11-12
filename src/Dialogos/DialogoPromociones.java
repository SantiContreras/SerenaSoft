package Dialogos;

import Configuracion.conexion;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

/** Administración visual de 2x1. No altera precios ni aplica descuentos en caja. */
public class DialogoPromociones extends JDialog {
    private static final Color NAVY=new Color(19,53,110), BLUE=new Color(22,101,195), BG=new Color(245,248,253), GREEN=new Color(19,139,91);
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","PRODUCTO","DESDE","HASTA","ESTADO"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable table=new JTable(model);
    private final DefaultListModel<Product> results=new DefaultListModel<>();
    private final JList<Product> productList=new JList<>(results);
    private final JTextField search=new JTextField();
    private final JLabel stockInfo=new JLabel("Seleccioná un producto para consultar existencias");
    private final JLabel pairsInfo=new JLabel("Pares 2 x 1 posibles: —");
    private final java.util.Map<Integer,java.math.BigDecimal> stocks=new HashMap<>();
    private boolean stockConsultado=false;
    private final JButton start=button("Elegir fecha",BLUE), end=button("Elegir fecha",BLUE);
    private LocalDate startDate=LocalDate.now(), endDate=LocalDate.now().plusDays(7);
    private final java.util.List<Product> catalog=new ArrayList<>();
    private static class Product {final int id;final String name,code,barcode;Product(int i,String n,String c,String b){id=i;name=n;code=c;barcode=b;}public String toString(){return name+"  ·  "+code;}}
    public DialogoPromociones(Window owner){super(owner,"Promociones 2 × 1 | Serena Soft",ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());getContentPane().setBackground(BG);
        JPanel header=new JPanel(new BorderLayout(0,4));header.setBackground(NAVY);header.setBorder(new EmptyBorder(18,24,18,24));
        JLabel title=new JLabel("PROMOCIONES 2 × 1");title.setForeground(Color.WHITE);title.setFont(new Font("Segoe UI",Font.BOLD,23));header.add(title,BorderLayout.NORTH);
        JLabel subtitle=new JLabel("Buscá un producto, elegí la vigencia y administrá sus ofertas");subtitle.setForeground(new Color(209,226,250));subtitle.setFont(new Font("Segoe UI",Font.PLAIN,13));header.add(subtitle,BorderLayout.SOUTH);add(header,BorderLayout.NORTH);
        JPanel content=new JPanel(new BorderLayout(0,12));content.setBackground(BG);content.setBorder(new EmptyBorder(16,18,12,18));
        JPanel editor=new JPanel(new BorderLayout(12,10));editor.setBackground(Color.WHITE);editor.setBorder(new CompoundBorder(new LineBorder(new Color(211,222,239)),new EmptyBorder(15,17,15,17)));
        JPanel searchPane=new JPanel(new BorderLayout(0,6));searchPane.setOpaque(false);searchPane.add(label("BUSCAR PRODUCTO POR NOMBRE, CÓDIGO O BARRAS"),BorderLayout.NORTH);
        search.setFont(new Font("Segoe UI",Font.PLAIN,14));search.setPreferredSize(new Dimension(300,38));search.setToolTipText("Escribí para filtrar el catálogo; no necesitás recorrer miles de productos");search.setBorder(new CompoundBorder(new LineBorder(new Color(165,187,219)),new EmptyBorder(6,11,6,11)));searchPane.add(search,BorderLayout.CENTER);
        editor.add(searchPane,BorderLayout.NORTH);
        productList.setFont(new Font("Segoe UI",Font.PLAIN,13));productList.setFixedCellHeight(32);productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);productList.setSelectionBackground(new Color(214,232,255));productList.setSelectionForeground(NAVY);
        JScrollPane candidates=new JScrollPane(productList);candidates.setPreferredSize(new Dimension(300,115));candidates.setBorder(new LineBorder(new Color(216,226,241)));JPanel mid=new JPanel(new BorderLayout(0,7));mid.setOpaque(false);mid.add(candidates,BorderLayout.CENTER);
        JPanel stockPanel=new JPanel(new GridLayout(2,1,0,3));stockPanel.setBackground(new Color(235,245,255));stockPanel.setBorder(new EmptyBorder(9,12,9,12));
        stockInfo.setFont(new Font("Segoe UI",Font.BOLD,13));stockInfo.setForeground(NAVY);
        pairsInfo.setFont(new Font("Segoe UI",Font.PLAIN,12));pairsInfo.setForeground(GREEN);
        stockPanel.add(stockInfo);stockPanel.add(pairsInfo);mid.add(stockPanel,BorderLayout.SOUTH);
        editor.add(mid,BorderLayout.CENTER);
        productList.addListSelectionListener(e->{if(!e.getValueIsAdjusting())updateStockInfo();});
        JPanel dates=new JPanel(new GridLayout(1,2,12,0));dates.setOpaque(false);dates.add(dateCell("FECHA DE INICIO",start,()->chooseDate(startDate,d->{startDate=d;updateDates();})));dates.add(dateCell("FECHA DE FIN",end,()->chooseDate(endDate,d->{endDate=d;updateDates();})));editor.add(dates,BorderLayout.SOUTH);
        content.add(editor,BorderLayout.NORTH);
        table.setRowHeight(35);table.setFont(new Font("Segoe UI",Font.PLAIN,13));table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);table.setShowVerticalLines(false);table.setGridColor(new Color(231,237,245));table.setSelectionBackground(new Color(217,233,255));table.setSelectionForeground(NAVY);
        JTableHeader th=table.getTableHeader();th.setDefaultRenderer(new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){super.getTableCellRendererComponent(t,v,s,f,r,c);setBackground(NAVY);setForeground(Color.WHITE);setFont(new Font("Segoe UI",Font.BOLD,12));setBorder(new EmptyBorder(0,10,0,0));setOpaque(true);return this;}});th.setPreferredSize(new Dimension(0,38));
        table.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){super.getTableCellRendererComponent(t,v,s,f,r,c);setBorder(new EmptyBorder(0,9,0,9));if(!s){setBackground(r%2==0?Color.WHITE:new Color(248,250,254));setForeground(c==4?("Activa".equals(v)?GREEN:new Color(178,73,73)):NAVY);}return this;}});
        JScrollPane tableScroll=new JScrollPane(table);tableScroll.setBorder(new LineBorder(new Color(211,222,239)));content.add(tableScroll,BorderLayout.CENTER);add(content,BorderLayout.CENTER);
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,9,11));footer.setBackground(Color.WHITE);footer.setBorder(new MatteBorder(1,0,0,0,new Color(218,228,241)));
        JButton create=button("+ CREAR PROMOCIÓN",GREEN), toggle=button("ACTIVAR / DESACTIVAR",BLUE), close=button("CERRAR",new Color(91,106,125));footer.add(create);footer.add(toggle);footer.add(close);add(footer,BorderLayout.SOUTH);
        create.addActionListener(e->create());toggle.addActionListener(e->toggle());close.addActionListener(e->dispose());
        search.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){filter();}public void removeUpdate(DocumentEvent e){filter();}public void changedUpdate(DocumentEvent e){filter();}});
        updateDates();setSize(940,720);setMinimumSize(new Dimension(720,580));setLocationRelativeTo(owner);reload();SwingUtilities.invokeLater(()->search.requestFocusInWindow());
    }
    private static JLabel label(String s){JLabel l=new JLabel(s);l.setForeground(NAVY);l.setFont(new Font("Segoe UI",Font.BOLD,12));return l;}
    private static JButton button(String text,Color color){JButton b=new JButton(text){@Override protected void paintComponent(Graphics g){Graphics2D d=(Graphics2D)g.create();d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);Color c=isEnabled()?color:new Color(158,169,184);if(getModel().isPressed())c=c.darker();else if(getModel().isRollover())c=c.brighter();d.setColor(c);d.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);d.dispose();super.paintComponent(g);}};b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setContentAreaFilled(false);b.setOpaque(false);b.setBorder(new EmptyBorder(12,17,12,17));b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private JPanel dateCell(String name,JButton b,Runnable action){JPanel p=new JPanel(new BorderLayout(0,6));p.setOpaque(false);p.add(label(name),BorderLayout.NORTH);b.addActionListener(e->action.run());p.add(b,BorderLayout.CENTER);return p;}
    private void updateDates(){DateTimeFormatter f=DateTimeFormatter.ofPattern("dd/MM/yyyy");start.setText("INICIO  ·  "+startDate.format(f)+"   ▾");end.setText("FIN  ·  "+endDate.format(f)+"   ▾");}
    /** Calendario propio con Java Swing: no requiere JCalendar ni librerías adicionales. */
    private void chooseDate(LocalDate initial,java.util.function.Consumer<LocalDate> chosen){JDialog dlg=new JDialog(this,"Seleccionar fecha",true);dlg.setLayout(new BorderLayout(8,8));dlg.getContentPane().setBackground(Color.WHITE);
        final YearMonth[] month={YearMonth.from(initial)};JPanel top=new JPanel(new BorderLayout());top.setBackground(Color.WHITE);top.setBorder(new EmptyBorder(12,12,5,12));JLabel monthLabel=label("");JButton prev=button("‹",BLUE),next=button("›",BLUE);top.add(prev,BorderLayout.WEST);top.add(monthLabel,BorderLayout.CENTER);top.add(next,BorderLayout.EAST);monthLabel.setHorizontalAlignment(SwingConstants.CENTER);dlg.add(top,BorderLayout.NORTH);
        JPanel grid=new JPanel(new GridLayout(0,7,4,4));grid.setBackground(Color.WHITE);grid.setBorder(new EmptyBorder(8,12,12,12));dlg.add(grid,BorderLayout.CENTER);
        Runnable redraw=()->{grid.removeAll();monthLabel.setText(month[0].getMonth().getDisplayName(java.time.format.TextStyle.FULL,new Locale("es","AR")).toUpperCase(new Locale("es","AR"))+" "+month[0].getYear());for(String day:new String[]{"L","M","X","J","V","S","D"}){JLabel l=label(day);l.setHorizontalAlignment(SwingConstants.CENTER);grid.add(l);}int offset=month[0].atDay(1).getDayOfWeek().getValue()-1;for(int i=0;i<offset;i++)grid.add(new JLabel());for(int day=1;day<=month[0].lengthOfMonth();day++){final LocalDate date=month[0].atDay(day);JButton d=new JButton(String.valueOf(day));d.setFont(new Font("Segoe UI",Font.PLAIN,13));d.setBackground(date.equals(initial)?new Color(207,230,255):Color.WHITE);d.setForeground(NAVY);d.setFocusPainted(false);d.addActionListener(e->{chosen.accept(date);dlg.dispose();});grid.add(d);}grid.revalidate();grid.repaint();};prev.addActionListener(e->{month[0]=month[0].minusMonths(1);redraw.run();});next.addActionListener(e->{month[0]=month[0].plusMonths(1);redraw.run();});redraw.run();dlg.setSize(390,360);dlg.setLocationRelativeTo(this);dlg.setVisible(true);}
    private void filter(){Product selected=productList.getSelectedValue();results.clear();String q=search.getText().trim().toLowerCase(Locale.ROOT);int added=0;for(Product p:catalog){if(p.name.toLowerCase(Locale.ROOT).contains(q)||p.code.toLowerCase(Locale.ROOT).contains(q)||p.barcode.toLowerCase(Locale.ROOT).contains(q)){results.addElement(p);if(++added>=100)break;}}if(selected!=null){for(int i=0;i<results.size();i++)if(results.get(i).id==selected.id){productList.setSelectedIndex(i);break;}}if(productList.getSelectedIndex()<0&&!results.isEmpty())productList.setSelectedIndex(0);}
    private void updateStockInfo(){
        Product p=productList.getSelectedValue();
        if(p==null){stockInfo.setText("Seleccioná un producto para consultar existencias");pairsInfo.setText("Pares 2 x 1 posibles: —");return;}
        if(!stockConsultado){stockInfo.setText("Stock no disponible (verificá depósito)");pairsInfo.setText("Pares 2 x 1 posibles: —");return;}
        java.math.BigDecimal qty=stocks.getOrDefault(p.id,java.math.BigDecimal.ZERO);
        stockInfo.setText("Stock disponible: " + qty.stripTrailingZeros().toPlainString() + " unidades");
        pairsInfo.setText("Pares 2 x 1 posibles: " + qty.divideToIntegralValue(new java.math.BigDecimal("2")).toPlainString());
        stockInfo.setForeground(qty.signum()<=0?new Color(185,45,45):NAVY);
    }
    private void cargarStock(Connection cn){
        stocks.clear();stockConsultado=false;
        // Suma las existencias de depósitos activos; no inventa disponibilidad si falla la consulta.
        String sql="SELECT sp.id_producto, COALESCE(SUM(sp.cantidad),0) FROM stock_producto sp JOIN deposito d ON d.id_deposito=sp.id_deposito WHERE d.activo=1 GROUP BY sp.id_producto";
        try(PreparedStatement ps=cn.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()) stocks.put(rs.getInt(1),rs.getBigDecimal(2));
            stockConsultado=true;
        }catch(SQLException ex){System.err.println("No se pudo consultar stock de promociones: "+ex.getMessage());}
    }
    private void reload(){catalog.clear();model.setRowCount(0);try(Connection cn=conexion.getConexion()){
        cargarStock(cn);
        try(PreparedStatement ps=cn.prepareStatement("SELECT id_producto,nombre,codigo,COALESCE(codigo_barra,'') FROM producto WHERE activo=1 AND es_pesable=0 ORDER BY nombre");ResultSet rs=ps.executeQuery()){while(rs.next())catalog.add(new Product(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)));}
        try(PreparedStatement ps=cn.prepareStatement("SELECT pr.id_promocion,p.nombre,pr.fecha_inicio,pr.fecha_fin,pr.activo FROM promocion_2x1 pr JOIN producto p ON p.id_producto=pr.id_producto ORDER BY pr.id_promocion DESC");ResultSet rs=ps.executeQuery()){while(rs.next())model.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5)?"Activa":"Inactiva"});}
        filter();updateStockInfo();}catch(SQLException ex){JOptionPane.showMessageDialog(this,"No se pudieron cargar las promociones:\n"+ex.getMessage(),"Serena Soft",JOptionPane.ERROR_MESSAGE);}}
    private void create(){Product p=productList.getSelectedValue();if(p==null){JOptionPane.showMessageDialog(this,"Buscá y seleccioná un producto.");return;}if(!stockConsultado){JOptionPane.showMessageDialog(this,"No se pudo consultar el stock. Verificá los depósitos antes de crear la promoción.");return;}
        if(stocks.getOrDefault(p.id,java.math.BigDecimal.ZERO).compareTo(new java.math.BigDecimal("2"))<0){
            int confirm=JOptionPane.showConfirmDialog(this,"El producto tiene menos de 2 unidades disponibles. ¿Querés registrar la promoción de todos modos?","Stock insuficiente",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(confirm!=JOptionPane.YES_OPTION)return;
        }
        if(endDate.isBefore(startDate)){JOptionPane.showMessageDialog(this,"La fecha de fin debe ser igual o posterior a la de inicio.");return;}
        try(Connection cn=conexion.getConexion()){try(PreparedStatement ps=cn.prepareStatement("SELECT COUNT(*) FROM promocion_2x1 WHERE id_producto=? AND activo=1 AND fecha_inicio<=? AND fecha_fin>=?")){ps.setInt(1,p.id);ps.setDate(2,java.sql.Date.valueOf(endDate));ps.setDate(3,java.sql.Date.valueOf(startDate));try(ResultSet r=ps.executeQuery()){r.next();if(r.getInt(1)>0){JOptionPane.showMessageDialog(this,"Ya existe una promoción activa superpuesta para este producto.");return;}}}try(PreparedStatement ps=cn.prepareStatement("INSERT INTO promocion_2x1(id_producto,fecha_inicio,fecha_fin,activo) VALUES(?,?,?,1)")){ps.setInt(1,p.id);ps.setDate(2,java.sql.Date.valueOf(startDate));ps.setDate(3,java.sql.Date.valueOf(endDate));ps.executeUpdate();}reload();JOptionPane.showMessageDialog(this,"Promoción registrada. Su aplicación automática en caja requiere la integración con VentaService.");}catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Serena Soft",JOptionPane.ERROR_MESSAGE);}}
    private void toggle(){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Seleccioná una promoción.");return;}int id=(Integer)table.getValueAt(row,0);boolean active="Activa".equals(table.getValueAt(row,4));try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("UPDATE promocion_2x1 SET activo=? WHERE id_promocion=?")){ps.setBoolean(1,!active);ps.setInt(2,id);ps.executeUpdate();reload();}catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Serena Soft",JOptionPane.ERROR_MESSAGE);}}
}
