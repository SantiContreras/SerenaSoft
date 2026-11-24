package Dialogos;

import Configuracion.conexion;
import sesion.SesionUsuario;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

/** Configuración local. No escribe existencias ni movimientos. */
public class DialogoAdministrarDepositos extends JDialog {
    private static final Color AZUL = new Color(20,49,101);
    private final DefaultTableModel depositos = modelo("ID", "Código", "Depósito", "Tipo", "Estado");
    private final DefaultTableModel stock = modelo("Código", "Producto", "Existencia", "Mínimo", "Estado");
    private final JTable tabla = new JTable(depositos), detalle = new JTable(stock);
    private final JLabel tituloStock = new JLabel("Seleccioná un depósito");
    private final JTextField buscar = new JTextField();
    private final JLabel resumen = new JLabel(" ");
    private static DefaultTableModel modelo(String... cols) {
        return new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
    }
    public DialogoAdministrarDepositos(Window owner) {
        super(owner,"Serena Soft | Administración de depósitos",ModalityType.APPLICATION_MODAL);
        if (!SesionUsuario.esAdministrador()) throw new SecurityException("Acceso exclusivo del administrador.");
        JPanel root = new JPanel(new BorderLayout(10,10)); root.setBorder(new EmptyBorder(12,12,12,12));
        root.setBackground(new Color(239,244,250)); setContentPane(root);
        JLabel encabezado = new JLabel("ADMINISTRACIÓN DE DEPÓSITOS"); encabezado.setOpaque(true); encabezado.setBackground(AZUL); encabezado.setForeground(Color.WHITE); encabezado.setFont(new Font("Segoe UI",Font.BOLD,20)); encabezado.setBorder(new EmptyBorder(15,15,15,15)); root.add(encabezado,BorderLayout.NORTH);
        estilizar(tabla); estilizar(detalle); tabla.removeColumn(tabla.getColumnModel().getColumn(0)); int[] anchos={110,410,140,130}; for(int i=0;i<anchos.length;i++)tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]); int[] anchosStock={120,420,110,100,140}; for(int i=0;i<anchosStock.length;i++)detalle.getColumnModel().getColumn(i).setPreferredWidth(anchosStock[i]);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel superior = new JPanel(new BorderLayout(8,8)); superior.setOpaque(false);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); acciones.setOpaque(false);
        JButton nuevo = boton("+ NUEVO DEPÓSITO",new Color(20,125,91)), editar=boton("MODIFICAR",AZUL), refresh=boton("ACTUALIZAR",AZUL);
        acciones.add(nuevo); acciones.add(editar); acciones.add(refresh); superior.add(acciones,BorderLayout.NORTH); superior.add(new JScrollPane(tabla),BorderLayout.CENTER);
        JPanel inferior = new JPanel(new BorderLayout(6,6)); inferior.setOpaque(false);
        JPanel filtros = new JPanel(new GridLayout(2,1,6,6)); filtros.setOpaque(false); tituloStock.setFont(new Font("Segoe UI",Font.BOLD,16)); tituloStock.setForeground(AZUL); filtros.add(tituloStock);
        JPanel entrada = new JPanel(new BorderLayout(8,0)); entrada.setOpaque(false); JLabel lupa=new JLabel("Buscar artículo"); lupa.setFont(new Font("Segoe UI",Font.BOLD,13)); lupa.setForeground(AZUL); entrada.add(lupa,BorderLayout.WEST); campo(buscar); buscar.setToolTipText("Buscar por código o nombre"); entrada.add(buscar); JButton limpiar=boton("LIMPIAR",AZUL); limpiar.addActionListener(e->buscar.setText("")); entrada.add(limpiar,BorderLayout.EAST); filtros.add(entrada); inferior.add(filtros,BorderLayout.NORTH); inferior.add(new JScrollPane(detalle)); inferior.add(resumen,BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,superior,inferior); split.setResizeWeight(.28); split.setBorder(null); root.add(split);
        JPanel pie = new JPanel(new BorderLayout()); pie.setOpaque(false); pie.add(new JLabel("Stock por depósito · El alta no genera existencias.")); JButton cerrar=boton("CERRAR",AZUL); pie.add(cerrar,BorderLayout.EAST); root.add(pie,BorderLayout.SOUTH);
        tabla.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()) cargarStock();});
        nuevo.addActionListener(e->editar(false)); editar.addActionListener(e->editar(true)); refresh.addActionListener(e->cargar()); cerrar.addActionListener(e->dispose());
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stock); detalle.setRowSorter(sorter);
        buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){filtrar();} public void removeUpdate(javax.swing.event.DocumentEvent e){filtrar();} public void changedUpdate(javax.swing.event.DocumentEvent e){filtrar();} private void filtrar(){sorter.setRowFilter(RowFilter.regexFilter("(?i)"+java.util.regex.Pattern.quote(buscar.getText()),0,1));}});
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); setSize(1000,680); split.setDividerLocation(180); setLocationRelativeTo(owner); cargar();
    }
    private static JButton boton(String texto,Color color){
        JButton b=new JButton(texto){protected void paintComponent(Graphics g){Graphics2D d=(Graphics2D)g.create(); d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); d.setColor(getModel().isPressed()?color.darker():color); d.fillRoundRect(0,0,getWidth(),getHeight(),12,12); d.dispose(); super.paintComponent(g);}};
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI",Font.BOLD,12)); b.setBorder(new EmptyBorder(10,16,10,16)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    private static void campo(JTextField f){
        f.setFont(new Font("Segoe UI",Font.PLAIN,14)); f.setForeground(AZUL);
        f.setBackground(Color.WHITE); f.setCaretColor(AZUL);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(183,201,225)),new EmptyBorder(7,10,7,10)));
        f.setPreferredSize(new Dimension(240,38));
    }
    private static void estilizar(JTable t){
        t.setFont(new Font("Segoe UI",Font.PLAIN,13)); t.setRowHeight(32);
        t.setForeground(AZUL); t.setSelectionForeground(AZUL); t.setSelectionBackground(new Color(209,225,246));
        t.setShowVerticalLines(false); t.setShowHorizontalLines(true); t.setGridColor(new Color(220,228,239)); t.setFillsViewportHeight(true); t.setBackground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false); t.getTableHeader().setPreferredSize(new Dimension(0,36));
        t.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){public Component getTableCellRendererComponent(JTable a,Object v,boolean s,boolean f,int r,int c){super.getTableCellRendererComponent(a,v,false,false,r,c);setOpaque(true);setBackground(AZUL);setForeground(Color.WHITE);setFont(new Font("Segoe UI",Font.BOLD,13));setHorizontalAlignment(CENTER);setBorder(new EmptyBorder(0,8,0,8));return this;}});
        t.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){public Component getTableCellRendererComponent(JTable a,Object v,boolean s,boolean f,int r,int c){
            super.getTableCellRendererComponent(a,v,s,f,r,c); setOpaque(true);setForeground(AZUL);setFont(a.getFont());setBorder(new EmptyBorder(0,9,0,9));
            String col=a.getColumnName(c); setHorizontalAlignment("Estado".equals(col)?CENTER:("Existencia".equals(col)||"Mínimo".equals(col)?RIGHT:LEFT));
            setBackground(s?new Color(209,225,246):r%2==0?Color.WHITE:new Color(243,247,252));
            if("Estado".equals(col)&&v!=null){switch(v.toString().toUpperCase(java.util.Locale.ROOT)){
                case "ACTIVO": case "DISPONIBLE": setBackground(new Color(134,211,162));break;
                case "BAJO STOCK": setBackground(new Color(244,204,112));break;
                case "SIN STOCK": setBackground(new Color(242,155,155));break;
                case "NEGATIVO": setBackground(new Color(228,119,119));break;
                case "INACTIVO": setBackground(new Color(185,192,202));break;
            } setFont(a.getFont().deriveFont(Font.BOLD)); if(s)setBorder(BorderFactory.createMatteBorder(1,0,1,0,AZUL));}
            return this;
        }});
    }
    private boolean formulario(boolean existente,JTextField codigo,JTextField nombre,JTextField desc){
        JDialog d=new JDialog(this,existente?"Modificar depósito":"Nuevo depósito",ModalityType.APPLICATION_MODAL);
        JPanel root=new JPanel(new BorderLayout(12,12));root.setBackground(new Color(239,244,250));root.setBorder(new EmptyBorder(14,14,14,14));d.setContentPane(root);
        JLabel h=new JLabel(existente?"MODIFICAR DEPÓSITO":"NUEVO DEPÓSITO");h.setOpaque(true);h.setBackground(AZUL);h.setForeground(Color.WHITE);h.setFont(new Font("Segoe UI",Font.BOLD,19));h.setBorder(new EmptyBorder(16,14,16,14));root.add(h,BorderLayout.NORTH);
        JPanel form=new JPanel(new GridBagLayout());form.setBackground(Color.WHITE);form.setBorder(new EmptyBorder(12,16,12,16));
        String[] labels={"Código único (ej. DEP-02)","Nombre del depósito","Descripción / ubicación (opcional)"};JTextField[] fields={codigo,nombre,desc};
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.weightx=1;g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;
        for(int i=0;i<fields.length;i++){JLabel l=new JLabel(labels[i]);l.setFont(new Font("Segoe UI",Font.BOLD,13));l.setForeground(AZUL);g.gridy=i*2;g.insets=new Insets(5,0,4,0);form.add(l,g);campo(fields[i]);g.gridy++;g.insets=new Insets(0,0,8,0);form.add(fields[i],g);}
        if(existente)codigo.setBackground(new Color(235,240,247));root.add(form,BorderLayout.CENTER);
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));pie.setOpaque(false);JButton cancelar=boton("CANCELAR",new Color(75,85,99)),guardar=boton("GUARDAR DEPÓSITO",new Color(20,125,91));pie.add(cancelar);pie.add(guardar);root.add(pie,BorderLayout.SOUTH);
        boolean[] ok={false};cancelar.addActionListener(e->d.dispose());guardar.addActionListener(e->{String co=codigo.getText().trim(),no=nombre.getText().trim(),de=desc.getText().trim();if(co.isEmpty()||co.length()>20||no.isEmpty()||no.length()>100||de.length()>250){JOptionPane.showMessageDialog(d,"Revisá los campos: código hasta 20, nombre hasta 100 y descripción hasta 250 caracteres.");return;}ok[0]=true;d.dispose();});
        d.getRootPane().setDefaultButton(guardar);d.getRootPane().registerKeyboardAction(e->d.dispose(),KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW);d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);d.setSize(510,410);d.setLocationRelativeTo(this);SwingUtilities.invokeLater(()->(existente?nombre:codigo).requestFocusInWindow());d.setVisible(true);return ok[0];
    }
    private int seleccionado(){int r=tabla.getSelectedRow();return r<0?-1:(Integer)depositos.getValueAt(tabla.convertRowIndexToModel(r),0);}
    private void cargar(){int anterior=seleccionado(); depositos.setRowCount(0); stock.setRowCount(0); try(Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement("SELECT * FROM deposito ORDER BY es_principal DESC,nombre"); ResultSet r=p.executeQuery()){while(r.next())depositos.addRow(new Object[]{r.getInt("id_deposito"),r.getString("codigo"),r.getString("nombre"),r.getBoolean("es_principal")?"Principal":"Secundario",r.getBoolean("activo")?"Activo":"Inactivo"}); if(depositos.getRowCount()>0){int sel=0;for(int i=0;i<depositos.getRowCount();i++)if((Integer)depositos.getValueAt(i,0)==anterior)sel=i;tabla.setRowSelectionInterval(sel,sel);}}catch(SQLException e){error(e);}}
    private void cargarStock(){stock.setRowCount(0);int id=seleccionado();tituloStock.setText("Seleccioná un depósito");resumen.setText(" ");if(id<0)return; tituloStock.setText("EXISTENCIAS | "+tabla.getValueAt(tabla.getSelectedRow(),1));
        String sql="SELECT p.codigo,p.nombre,COALESCE(s.cantidad,0) cantidad,p.stock_minimo,p.activo FROM producto p JOIN stock_producto s ON s.id_producto=p.id_producto AND s.id_deposito=? WHERE p.activo=1 OR s.cantidad<>0 ORDER BY p.nombre";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,id);try(ResultSet r=p.executeQuery()){int positivos=0,negativos=0;while(r.next()){BigDecimal n=r.getBigDecimal("cantidad"),m=r.getBigDecimal("stock_minimo");if(n.signum()>0)positivos++;if(n.signum()<0)negativos++;stock.addRow(new Object[]{r.getString("codigo"),r.getString("nombre"),n.stripTrailingZeros().toPlainString(),m.stripTrailingZeros().toPlainString(),!r.getBoolean("activo")?"INACTIVO":n.signum()<0?"NEGATIVO":n.signum()==0?"SIN STOCK":n.compareTo(m)<0?"BAJO STOCK":"DISPONIBLE"});}resumen.setText("Artículos con existencia: "+positivos+"  |  Stock negativo: "+negativos+"  |  Artículos asignados: "+stock.getRowCount());}}catch(SQLException e){stock.setRowCount(0);resumen.setText("No se pudo consultar el stock.");error(e);}}
    private void editar(boolean existente){if(!SesionUsuario.esAdministrador())return;int id=seleccionado();if(existente&&id<0)return; JTextField codigo=new JTextField(),nombre=new JTextField(),desc=new JTextField();
        if(existente){try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement("SELECT codigo,nombre,descripcion FROM deposito WHERE id_deposito=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(!r.next())return;codigo.setText(r.getString(1));nombre.setText(r.getString(2));desc.setText(r.getString(3));}}catch(SQLException e){error(e);return;}codigo.setEditable(false);}
        if(!formulario(existente,codigo,nombre,desc))return;
        String co=codigo.getText().trim(),no=nombre.getText().trim(),de=desc.getText().trim();if(co.isEmpty()||co.length()>20||no.isEmpty()||no.length()>100||de.length()>250){JOptionPane.showMessageDialog(this,"Revisá los campos: código hasta 20, nombre hasta 100 y descripción hasta 250 caracteres.");return;}
        try(Connection c=conexion.getConexion()){try(PreparedStatement lock=c.prepareStatement("SELECT GET_LOCK('serenasoft_deposito_central',10)");ResultSet r=lock.executeQuery()){if(!r.next()||r.getInt(1)!=1)throw new SQLException("Configuración ocupada. Intentá nuevamente.");}try{try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM deposito WHERE nombre=? AND id_deposito<>?")){p.setString(1,no);p.setInt(2,existente?id:-1);try(ResultSet r=p.executeQuery()){r.next();if(r.getInt(1)>0)throw new SQLException("Ya existe un depósito con ese nombre.");}}
          String sql=existente?"UPDATE deposito SET nombre=?,descripcion=? WHERE id_deposito=?":"INSERT INTO deposito(nombre,descripcion,codigo,es_principal,activo) VALUES (?,?,?,0,1)";
          try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,no);p.setString(2,de);if(existente)p.setInt(3,id);else p.setString(3,co);p.executeUpdate();}
        }finally{try(PreparedStatement p=c.prepareStatement("SELECT RELEASE_LOCK('serenasoft_deposito_central')")){p.executeQuery().close();}} cargar(); JOptionPane.showMessageDialog(this,"Depósito guardado correctamente. No se modificó el stock.");}catch(SQLException e){error(e);}}
    private void error(SQLException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Depósitos",JOptionPane.ERROR_MESSAGE);}
}
