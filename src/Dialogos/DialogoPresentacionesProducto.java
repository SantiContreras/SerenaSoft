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
        setSize(850,570);setMinimumSize(new Dimension(690,490));setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));getContentPane().setBackground(new Color(240,245,252));
        JPanel cab=new JPanel(new GridLayout(2,1));cab.setBackground(new Color(17,49,105));cab.setBorder(BorderFactory.createEmptyBorder(14,20,14,20));
        JLabel titulo=new JLabel("PRESENTACIONES DEL ARTÍCULO");titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,19));cab.add(titulo);
        JLabel sub=new JLabel(nombreProducto+"  ·  Stock base: "+codigoBase);sub.setForeground(new Color(207,226,251));cab.add(sub);add(cab,BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout(10,10));centro.setOpaque(false);centro.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        JPanel form=new JPanel(new GridLayout(2,4,10,6));form.setOpaque(false);
        form.add(new JLabel("Tipo de presentación"));form.add(new JLabel("Contenido en "+codigoBase));form.add(new JLabel("Código barras (opcional)"));form.add(new JLabel("Precio venta (opcional)"));
        form.add(tipo);form.add(contenido);form.add(codigo);form.add(precio);
        JPanel superior=new JPanel(new BorderLayout(5,8));superior.setOpaque(false);superior.add(form,BorderLayout.CENTER);
        conversion.setForeground(new Color(12,83,160));conversion.setFont(new Font("Segoe UI",Font.BOLD,13));superior.add(conversion,BorderLayout.SOUTH);
        centro.add(superior,BorderLayout.NORTH);
        tabla.setRowHeight(29);tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);tabla.setAutoCreateRowSorter(true);
        centro.add(new JScrollPane(tabla),BorderLayout.CENTER);add(centro,BorderLayout.CENTER);
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,12));pie.setBackground(Color.WHITE);
        JButton nuevo=boton("AGREGAR PRESENTACIÓN",new Color(0,151,92));
        JButton desactivar=boton("DESACTIVAR",new Color(91,105,126));
        JButton cerrar=boton("CERRAR",new Color(28,75,145));
        pie.add(desactivar);pie.add(nuevo);pie.add(cerrar);add(pie,BorderLayout.SOUTH);
        nuevo.addActionListener(e->guardar());desactivar.addActionListener(e->desactivar());cerrar.addActionListener(e->dispose());
        tipo.addActionListener(e->previsualizar());contenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){previsualizar();}public void removeUpdate(javax.swing.event.DocumentEvent e){previsualizar();}public void changedUpdate(javax.swing.event.DocumentEvent e){previsualizar();}});
        previsualizar();recargar();
    }
    private JButton boton(String texto,Color color){JButton b=new JButton(texto);b.setBackground(color);b.setForeground(Color.WHITE);b.setFocusPainted(false);b.setFont(new Font("Segoe UI",Font.BOLD,12));return b;}
    private void previsualizar(){try{BigDecimal f=new BigDecimal(contenido.getText().trim().replace(',','.'));conversion.setText("1 "+tipo.getSelectedItem()+" = "+f.stripTrailingZeros().toPlainString()+" "+codigoBase);}catch(Exception ex){conversion.setText("Ingresá una cantidad válida mayor que cero.");}}
    private void recargar(){modelo.setRowCount(0);String sql="SELECT id_presentacion,nombre,factor_conversion,codigo_barra,precio_venta FROM producto_presentacion WHERE id_producto=? AND activo=1 ORDER BY es_predeterminada DESC,factor_conversion,nombre";
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement(sql)){ps.setInt(1,idProducto);try(ResultSet rs=ps.executeQuery()){while(rs.next())modelo.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getBigDecimal(3).stripTrailingZeros().toPlainString(),rs.getString(4),rs.getBigDecimal(5)});}}
        catch(SQLException e){mensaje("Error al consultar presentaciones: "+e.getMessage(),true);}}
    private void guardar(){try{
        String t=tipo.getSelectedItem().toString();BigDecimal f=new BigDecimal(contenido.getText().trim().replace(',','.'));
        if(f.signum()<=0||f.scale()>4)throw new IllegalArgumentException("El contenido debe ser positivo y admitir hasta 4 decimales.");
        if(!codigoBase.equalsIgnoreCase("KG")&&!codigoBase.equalsIgnoreCase("LT")&&f.stripTrailingZeros().scale()>0)throw new IllegalArgumentException("Para unidades enteras, el contenido debe ser entero.");
        String bar=codigo.getText().trim();if(bar.length()>100)throw new IllegalArgumentException("Código de barras demasiado largo.");
        BigDecimal pv=null;if(!precio.getText().isBlank()){pv=new BigDecimal(precio.getText().trim().replace(',','.'));if(pv.signum()<0||pv.scale()>2)throw new IllegalArgumentException("Precio inválido: máximo 2 decimales.");}
        String sql="INSERT INTO producto_presentacion(id_producto,nombre,id_unidad_compra,factor_conversion,es_predeterminada,activo,codigo_barra,precio_venta) VALUES(?,?,?,?,0,1,?,?)";
        try(Connection cn=conexion.getConexion()){
            cn.setAutoCommit(false);
            try{
                try(PreparedStatement check=cn.prepareStatement("SELECT 1 FROM producto_presentacion WHERE id_producto=? AND nombre=? AND factor_conversion=? AND activo=1 LIMIT 1")){
                    check.setInt(1,idProducto);check.setString(2,t);check.setBigDecimal(3,f);try(ResultSet rs=check.executeQuery()){if(rs.next())throw new IllegalArgumentException("Ya existe esta presentación con el mismo contenido.");}}
                try(PreparedStatement ps=cn.prepareStatement(sql)){ps.setInt(1,idProducto);ps.setString(2,t);ps.setInt(3,resolverUnidadPresentacion(cn,t));ps.setBigDecimal(4,f);if(bar.isEmpty())ps.setNull(5,Types.VARCHAR);else ps.setString(5,bar);if(pv==null)ps.setNull(6,Types.DECIMAL);else ps.setBigDecimal(6,pv);ps.executeUpdate();}
                cn.commit();
            }catch(Exception ex){cn.rollback();throw ex;}finally{cn.setAutoCommit(true);}
        }
        cambios=true;recargar();codigo.setText("");precio.setText("");mensaje("Presentación guardada correctamente.",false);
    }catch(Exception e){mensaje(e.getMessage(),true);}}
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
