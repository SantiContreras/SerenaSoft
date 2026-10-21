package Dialogos;

import Configuracion.conexion;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Gestiona presentaciones de compra/venta sin modificar el stock existente. */
public class DialogoPresentacionesProducto extends JDialog {
    private final int idProducto;
    private final int idUnidadBase;
    private final String codigoBase;
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{"UNIDAD", "PACK", "CAJA", "BULTO", "BOLSA", "BANDEJA", "DISPLAY", "PALLET", "OTRA"});
    private final JTextField contenido = new JTextField("12");
    private final JTextField codigo = new JTextField();
    private final JTextField precio = new JTextField();
    private final JLabel conversion = new JLabel();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Presentación", "Contiene", "Código de barras", "Precio venta"},0) {
        @Override public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable tabla = new JTable(modelo);
    private boolean cambios;
    public boolean tieneCambios(){return cambios;}

    public DialogoPresentacionesProducto(Window owner,int idProducto,String nombreProducto,int idUnidadBase,String codigoBase){
        super(owner,"Presentaciones · Serena Soft",ModalityType.APPLICATION_MODAL);
        this.idProducto=idProducto;this.idUnidadBase=idUnidadBase;this.codigoBase=codigoBase;
        setSize(950,610);setMinimumSize(new Dimension(850,540));setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));getContentPane().setBackground(new Color(240,245,252));
        JPanel cab=new JPanel(new GridLayout(2,1));cab.setBackground(new Color(17,49,105));cab.setBorder(BorderFactory.createEmptyBorder(14,20,14,20));
        JLabel titulo=new JLabel("PRESENTACIONES DEL ARTÍCULO");titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,19));cab.add(titulo);
        JLabel sub=new JLabel(nombreProducto+"  ·  Stock base: "+codigoBase);sub.setForeground(new Color(207,226,251));cab.add(sub);add(cab,BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout(10,10));centro.setOpaque(false);centro.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        JPanel form=new JPanel(new GridLayout(2,4,12,8));form.setOpaque(false);
        form.add(new JLabel("Presentación de compra"));form.add(new JLabel("Unidades de stock ("+codigoBase+")"));form.add(new JLabel("Código barras (opcional)"));form.add(new JLabel("Precio venta (opcional)"));
        for (JComponent campo : new JComponent[]{tipo,contenido,codigo,precio}) {
            campo.setFont(new Font("Segoe UI",Font.PLAIN,15));
            campo.setPreferredSize(new Dimension(160,38));
        }
        tipo.setMaximumRowCount(12);
        tipo.setBackground(Color.WHITE);
        instalarComboModerno(tipo);
        contenido.setText("1");
        tipo.addActionListener(e->{ if("UNIDAD".equals(tipo.getSelectedItem())) contenido.setText("1"); });
        tipo.setToolTipText("Elegí el embalaje; el contenido lo definís vos para cada producto.");
        form.add(tipo);form.add(contenido);form.add(codigo);form.add(precio);
        JPanel superior=new JPanel(new BorderLayout(5,8));superior.setOpaque(false);superior.add(form,BorderLayout.CENTER);
        conversion.setForeground(new Color(12,83,160));conversion.setFont(new Font("Segoe UI",Font.BOLD,13));superior.add(conversion,BorderLayout.SOUTH);
        centro.add(superior,BorderLayout.NORTH);
        tabla.setRowHeight(36);tabla.setFont(new Font("Segoe UI",Font.PLAIN,14));tabla.setShowVerticalLines(false);tabla.setShowHorizontalLines(true);tabla.setGridColor(new Color(226,233,243));tabla.setSelectionBackground(new Color(30,116,220));tabla.setSelectionForeground(Color.WHITE);tabla.setFillsViewportHeight(true);tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,13));tabla.getTableHeader().setBackground(new Color(17,49,105));tabla.getTableHeader().setForeground(Color.WHITE);tabla.getTableHeader().setPreferredSize(new Dimension(0,38));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);tabla.setAutoCreateRowSorter(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean selected, boolean focused, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                cell.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                cell.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));
                cell.setBackground(selected ? new Color(29, 118, 225)
                        : (row % 2 == 0 ? Color.WHITE : new Color(238, 245, 253)));
                cell.setForeground(selected ? Color.WHITE : new Color(35, 53, 80));
                return cell;
            }
        });
        javax.swing.table.JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean selected, boolean focused, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                label.setOpaque(true);
                label.setBackground(new Color(17, 49, 105));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 6));
                return label;
            }
        });
        JScrollPane desplazamiento = new JScrollPane(tabla); desplazamiento.setBorder(BorderFactory.createLineBorder(new Color(215,226,240)));
        centro.add(desplazamiento,BorderLayout.CENTER);add(centro,BorderLayout.CENTER);
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,12));pie.setBackground(new Color(248,250,253));
        pie.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(216,225,237)));
        JButton nuevo=boton("AGREGAR PRESENTACIÓN",new Color(0,151,92));
        JButton desactivar=boton("DESACTIVAR",new Color(91,105,126));
        JButton modificar=boton("CAMBIAR CONTENIDO",new Color(34, 100, 174));
        JButton cerrar=boton("CERRAR",new Color(28,75,145));
        pie.add(desactivar);pie.add(modificar);pie.add(nuevo);pie.add(cerrar);add(pie,BorderLayout.SOUTH);
        nuevo.addActionListener(e->guardar());modificar.addActionListener(e->modificarSeleccionada());desactivar.addActionListener(e->desactivar());cerrar.addActionListener(e->dispose());
        tipo.addActionListener(e->previsualizar());contenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){previsualizar();}public void removeUpdate(javax.swing.event.DocumentEvent e){previsualizar();}public void changedUpdate(javax.swing.event.DocumentEvent e){previsualizar();}});
        previsualizar();recargar();
    }
    private JButton boton(String texto,Color color){
        JButton b=new JButton(texto){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo=!isEnabled()?new Color(166,175,188):(getModel().isPressed()?color.darker():color);
                g2.setColor(fondo);g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose();super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);b.setFont(new Font("Segoe UI",Font.PLAIN,14));
        b.setContentAreaFilled(false);b.setOpaque(false);b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10,17,10,17));
        b.setPreferredSize(new Dimension(texto.startsWith("AGREGAR")?205:(texto.startsWith("CAMBIAR")?190:135),42));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    /** Combo moderno, compatible con Swing sin dependencias externas. */
    private void instalarComboModerno(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(new Color(37, 57, 83));
        combo.setBorder(BorderFactory.createLineBorder(new Color(195, 211, 230)));
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override protected JButton createArrowButton() {
                JButton flecha = new JButton("⌄");
                flecha.setFont(new Font("Segoe UI", Font.PLAIN, 19));
                flecha.setForeground(new Color(48, 87, 145));
                flecha.setBackground(new Color(239, 245, 252));
                flecha.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 9));
                flecha.setFocusPainted(false);
                return flecha;
            }
        });
        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean selected, boolean focused) {
                JLabel item = (JLabel) super.getListCellRendererComponent(list, value, index, selected, focused);
                item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                item.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 8));
                if (selected) { item.setBackground(new Color(221, 234, 250)); item.setForeground(new Color(17, 49, 105)); }
                else { item.setBackground(Color.WHITE); item.setForeground(new Color(37, 57, 83)); }
                return item;
            }
        });
    }
    private void previsualizar(){try{BigDecimal f=new BigDecimal(contenido.getText().trim().replace(',','.'));conversion.setText("1 "+tipo.getSelectedItem()+" = "+f.stripTrailingZeros().toPlainString()+" "+codigoBase);}catch(Exception ex){conversion.setText("Ingresá una cantidad válida mayor que cero.");}}
    private void recargar(){modelo.setRowCount(0);String sql="SELECT id_presentacion,nombre,factor_conversion,codigo_barra,precio_venta FROM producto_presentacion WHERE id_producto=? AND activo=1 ORDER BY es_predeterminada DESC,factor_conversion,nombre";
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement(sql)){ps.setInt(1,idProducto);try(ResultSet rs=ps.executeQuery()){while(rs.next())modelo.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getBigDecimal(3).stripTrailingZeros().toPlainString(),rs.getString(4),rs.getBigDecimal(5)});}}
        catch(SQLException e){mensaje("Error al consultar presentaciones: "+e.getMessage(),true);}}
    private void guardar(){try{
        String t=tipo.getSelectedItem().toString();BigDecimal f=new BigDecimal(contenido.getText().trim().replace(',','.'));
        if(f.signum()<=0||f.stripTrailingZeros().scale()>4)throw new IllegalArgumentException("El contenido debe ser positivo y admitir hasta 4 decimales.");
        if(!codigoBase.equalsIgnoreCase("KG")&&!codigoBase.equalsIgnoreCase("LT")&&f.stripTrailingZeros().scale()>0)throw new IllegalArgumentException("Para unidades enteras, el contenido debe ser entero.");
        if ("UNIDAD".equals(t) && f.compareTo(BigDecimal.ONE)!=0) throw new IllegalArgumentException("Una UNIDAD equivale a 1 "+codigoBase+". Elegí PACK, CAJA o BULTO para cantidades mayores.");
        if (java.util.Arrays.asList("CAJA","PACK","BULTO","PALLET","DISPLAY","BANDEJA","BOLSA").contains(t) && f.compareTo(BigDecimal.ONE)==0) throw new IllegalArgumentException("Indicá el contenido real del embalaje. Ej.: CAJA x8, BULTO x12, PALLET x480.");
        String bar=codigo.getText().trim();if(bar.length()>100)throw new IllegalArgumentException("Código de barras demasiado largo.");
        BigDecimal pv=null;if(!precio.getText().isBlank()){pv=new BigDecimal(precio.getText().trim().replace(',','.'));if(pv.signum()<0||pv.scale()>2)throw new IllegalArgumentException("Precio inválido: máximo 2 decimales.");}
        try(Connection cn=conexion.getConexion()){
            cn.setAutoCommit(false);
            try {
                // El índice único es (id_producto,nombre), INCLUSO cuando activo=0.
                // Se reutiliza la presentación desactivada si coincide su factor;
                // para otro factor se crea una versión con nombre distinto.
                String nombreDestino = t;
                Integer idExistente = null;
                BigDecimal factorExistente = null;
                boolean activoExistente = false;
                try(PreparedStatement ps=cn.prepareStatement(
                        "SELECT id_presentacion,factor_conversion,activo FROM producto_presentacion WHERE id_producto=? AND nombre=? LIMIT 1")) {
                    ps.setInt(1,idProducto);ps.setString(2,t);
                    try(ResultSet rs=ps.executeQuery()) {
                        if(rs.next()) {idExistente=rs.getInt(1);factorExistente=rs.getBigDecimal(2);activoExistente=rs.getBoolean(3);}
                    }
                }
                if(idExistente!=null && factorExistente.compareTo(f)==0) {
                    if(activoExistente) throw new IllegalArgumentException("Esta presentación ya existe y está activa.");
                    try(PreparedStatement ps=cn.prepareStatement(
                            "UPDATE producto_presentacion SET activo=1 WHERE id_presentacion=?")) {
                        ps.setInt(1,idExistente);ps.executeUpdate();
                    }
                } else {
                    if(idExistente!=null) nombreDestino=t+" x"+f.stripTrailingZeros().toPlainString();
                    // También contempla una presentación versionada creada anteriormente.
                    Integer idVersion=null;boolean versionActiva=false;BigDecimal factorVersion=null;
                    try(PreparedStatement ps=cn.prepareStatement(
                            "SELECT id_presentacion,activo,factor_conversion FROM producto_presentacion WHERE id_producto=? AND nombre=? LIMIT 1")) {
                        ps.setInt(1,idProducto);ps.setString(2,nombreDestino);
                        try(ResultSet rs=ps.executeQuery()) {
                            if(rs.next()) {idVersion=rs.getInt(1);versionActiva=rs.getBoolean(2);factorVersion=rs.getBigDecimal(3);}
                        }
                    }
                    if(idVersion!=null) {
                        if(versionActiva) throw new IllegalArgumentException("La presentación "+nombreDestino+" ya existe y está activa.");
                        if(factorVersion.compareTo(f)!=0) throw new IllegalArgumentException("Ya existe el nombre "+nombreDestino+" con otro factor.");
                        try(PreparedStatement ps=cn.prepareStatement("UPDATE producto_presentacion SET activo=1 WHERE id_presentacion=?")) {
                            ps.setInt(1,idVersion);ps.executeUpdate();
                        }
                    } else {
                        String sql="INSERT INTO producto_presentacion(id_producto,nombre,id_unidad_compra,factor_conversion,es_predeterminada,activo,codigo_barra,precio_venta) VALUES(?,?,?,?,0,1,?,?)";
                        try(PreparedStatement ps=cn.prepareStatement(sql)) {
                            ps.setInt(1,idProducto);ps.setString(2,nombreDestino);
                            ps.setInt(3,resolverUnidadPresentacion(cn,t));ps.setBigDecimal(4,f);
                            if(bar.isEmpty())ps.setNull(5,Types.VARCHAR);else ps.setString(5,bar);
                            if(pv==null)ps.setNull(6,Types.DECIMAL);else ps.setBigDecimal(6,pv);
                            ps.executeUpdate();
                        }
                    }
                }
                cn.commit();
            } catch(Exception ex) {cn.rollback();throw ex;}
            finally {cn.setAutoCommit(true);}
        }
        cambios=true;recargar();codigo.setText("");precio.setText("");mensaje("Presentación guardada correctamente.",false);
    }catch(Exception e){mensaje(e.getMessage(),true);}}
    /** Crear una versión nueva en lugar de reescribir conversiones usadas en compras anteriores. */
    private void modificarSeleccionada() {
        int row=tabla.getSelectedRow();
        if(row<0) {mensaje("Seleccioná una presentación de la tabla.",true);return;}
        int modelRow=tabla.convertRowIndexToModel(row);
        String nombre=String.valueOf(modelo.getValueAt(modelRow,1));
        String t=nombre.replaceFirst("(?i)\\s+x[0-9]+([.,][0-9]+)?$","").trim().toUpperCase();
        if(t.startsWith("ORIGINAL")) {
            mensaje("La presentación original no se modifica desde aquí. Podés agregar otra presentación.",true);return;
        }
        tipo.setSelectedItem(t);
        contenido.setText(String.valueOf(modelo.getValueAt(modelRow,2)));
        JOptionPane.showMessageDialog(this,
                "Cambiá el contenido y presioná AGREGAR PRESENTACIÓN.\n"
                + "Se creará una presentación nueva para conservar el historial.\n"
                + "Después podés desactivar la anterior.",
                "Cambiar contenido sin alterar compras anteriores",JOptionPane.INFORMATION_MESSAGE);
    }
    /** El código de unidad guardado debe describir la presentación, no la unidad base. */
    private int resolverUnidadPresentacion(Connection cn, String tipoPresentacion) throws SQLException {
        String codigoUnidad = "UNIDAD".equalsIgnoreCase(tipoPresentacion)
                ? codigoBase : tipoPresentacion;
        if ("OTRA".equalsIgnoreCase(tipoPresentacion)) {
            throw new SQLException("Para OTRA, creá primero la unidad correspondiente en Unidades de medida.");
        }
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_unidad FROM unidad_medida WHERE UPPER(codigo)=UPPER(?) AND activo=1 LIMIT 1")) {
            ps.setString(1, codigoUnidad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No existe la unidad " + codigoUnidad
                + " en el catálogo Unidad de medida. Creala antes de agregar la presentación.");
    }

    private void desactivar(){int row=tabla.getSelectedRow();if(row<0){mensaje("Seleccioná una presentación.",true);return;}int id=(Integer)modelo.getValueAt(tabla.convertRowIndexToModel(row),0);
        if(JOptionPane.showConfirmDialog(this,"¿Desactivar esta presentación? Los movimientos anteriores se conservarán.","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("UPDATE producto_presentacion SET activo=0 WHERE id_presentacion=? AND id_producto=? AND es_predeterminada=0")){ps.setInt(1,id);ps.setInt(2,idProducto);if(ps.executeUpdate()!=1)throw new SQLException("No se puede desactivar la presentación original.");cambios=true;recargar();}catch(SQLException e){mensaje(e.getMessage(),true);}}
    private void mensaje(String m,boolean error){JOptionPane.showMessageDialog(this,m,"Serena Soft",error?JOptionPane.ERROR_MESSAGE:JOptionPane.INFORMATION_MESSAGE);}
}
