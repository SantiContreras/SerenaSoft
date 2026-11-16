package Paneles;

import Configuracion.conexion;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/** Catálogos seguros: no altera precios, stock, IVA ni conversiones. */
public class PanelConfigProductos extends JPanel {
    private static final Color AZUL = new Color(27,76,155), VERDE = new Color(22,137,87), GRIS = new Color(93,106,125);
    private final JTabbedPane tabs = new JTabbedPane();
    private static final Color FONDO = new Color(245,248,252), BORDE = new Color(215,226,240);
    private final JTable[] tablas = new JTable[3];
    private final DefaultTableModel[] modelos = new DefaultTableModel[3];
    private final String[] tablasSql = {"categoria", "marca", "unidad_medida"};
    private final String[] claves = {"id_categoria", "id_marca", "id_unidad"};
    private final JLabel estado = new JLabel("Listo para consultar los catálogos.");

    public PanelConfigProductos() {
        setLayout(new BorderLayout(12,12)); setBackground(new Color(245,247,251));
        setBorder(BorderFactory.createEmptyBorder(22,24,18,24));
        JPanel arriba = new JPanel(new GridLayout(2,1,0,4)); arriba.setOpaque(false);
        JLabel titulo = new JLabel("PRODUCTOS · CONFIGURACIÓN SEGURA");
        titulo.setFont(new Font("Segoe UI",Font.BOLD,23)); titulo.setForeground(AZUL);
        JLabel ayuda = new JLabel("Administración de catálogos. No modifica precios, IVA, stock ni ventas existentes.");
        ayuda.setFont(new Font("Segoe UI",Font.PLAIN,13)); ayuda.setForeground(GRIS);
        arriba.add(titulo); arriba.add(ayuda); add(arriba,BorderLayout.NORTH);
        tabs.setFont(new Font("Segoe UI",Font.BOLD,14));
        tabs.setBackground(Color.WHITE); tabs.setForeground(AZUL);
        String[] nombres = {"Categorías", "Marcas", "Unidades (solo consulta)"};
        for(int i=0;i<3;i++) { final int idx=i;
            modelos[i] = new DefaultTableModel(new String[]{"ID","Nombre / código","Descripción","Estado"},0) {
                @Override public boolean isCellEditable(int r,int c){return false;}
            };
            JTable t = new JTable(modelos[i]); tablas[i]=t;
            t.setFont(new Font("Segoe UI",Font.PLAIN,14)); t.setRowHeight(35);
            t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); t.setSelectionBackground(new Color(216,232,252));
            t.getTableHeader().setOpaque(false);
            t.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
                @Override public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focus,int row,int col){
                    JLabel l=(JLabel)super.getTableCellRendererComponent(table,value,selected,focus,row,col);
                    l.setOpaque(true); l.setBackground(AZUL);l.setForeground(Color.WHITE);
                    l.setFont(new Font("Segoe UI",Font.BOLD,14));
                    l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(85,128,191)),BorderFactory.createEmptyBorder(0,12,0,8)));
                    return l;
                }
            });
            t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));t.getTableHeader().setPreferredSize(new Dimension(0,38));
            t.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
                @Override public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focus,int row,int col){
                    super.getTableCellRendererComponent(table,value,selected,focus,row,col);
                    if(!selected) setBackground(row%2==0?Color.WHITE:new Color(242,246,251));
                    setForeground(col==3?("Activo".equals(value)?VERDE:new Color(190,55,55)):new Color(38,48,65));
                    setBorder(BorderFactory.createEmptyBorder(0,9,0,6));return this;
                }
            });
            JPanel cont = new JPanel(new BorderLayout(0,12)); cont.setBackground(Color.WHITE);
            cont.setBorder(BorderFactory.createEmptyBorder(15,12,12,12));
            JScrollPane sc=new JScrollPane(t);
            sc.setBorder(BorderFactory.createLineBorder(BORDE));
            sc.getViewport().setBackground(Color.WHITE);
            t.setGridColor(new Color(228,235,244));
            t.setShowVerticalLines(false);
            cont.add(sc,BorderLayout.CENTER);
            JPanel acciones=new JPanel(new FlowLayout(FlowLayout.LEFT,9,3));acciones.setOpaque(false);
            if(i<2){acciones.add(boton("Nuevo",VERDE,()->editar(idx,false)));
                acciones.add(boton("Editar",AZUL,()->editar(idx,true)));
                acciones.add(boton("Activar / Desactivar",GRIS,()->cambiarEstado(idx)));}
            acciones.add(boton("Actualizar",AZUL,()->cargar(idx)));
            if(i==2){JLabel aviso=new JLabel("Protegido: los cambios de unidades y conversiones se realizan desde un proceso controlado.");
                aviso.setForeground(GRIS); acciones.add(aviso);}
            cont.add(acciones,BorderLayout.SOUTH);tabs.addTab(nombres[i],cont);
        }
        add(tabs,BorderLayout.CENTER);estado.setFont(new Font("Segoe UI",Font.PLAIN,13));estado.setForeground(GRIS);
        add(estado,BorderLayout.SOUTH);
        for(int i=0;i<3;i++)cargar(i);
    }
    private JButton boton(String texto,Color color,Runnable accion){
        JButton b=new JButton(texto){@Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled()?color:GRIS);g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
            g2.setFont(getFont());FontMetrics fm=g2.getFontMetrics();g2.setColor(Color.WHITE);
            g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);g2.dispose();
        }};
        b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setPreferredSize(new Dimension(Math.max(120,texto.length()*10+25),39));
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e->accion.run());return b;
    }
    private void cargar(int i){
        String sql=i==0?"SELECT id_categoria,nombre,COALESCE(descripcion,''),activo FROM categoria ORDER BY nombre":
                i==1?"SELECT id_marca,nombre,COALESCE(descripcion,''),activo FROM marca ORDER BY nombre":
                "SELECT id_unidad,CONCAT(codigo,' - ',nombre),IF(permite_decimales=1,'Permite decimales','Solo enteros'),activo FROM unidad_medida ORDER BY nombre";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            DefaultTableModel m=modelos[i];m.setRowCount(0);
            while(r.next())m.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getBoolean(4)?"Activo":"Inactivo"});
            estado.setText("Catálogo actualizado desde MariaDB.");
        }catch(SQLException ex){estado.setText("Error al consultar: "+ex.getMessage());
            JOptionPane.showMessageDialog(this,"No se pudo cargar el catálogo.\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    private int seleccionado(int i){int fila=tablas[i].getSelectedRow();
        return fila<0?-1:((Number)modelos[i].getValueAt(tablas[i].convertRowIndexToModel(fila),0)).intValue();}
    private void editar(int i,boolean existente){
        int id=existente?seleccionado(i):-1;if(existente&&id<0){aviso("Seleccioná primero un registro.");return;}
        String nombre="",descripcion="";int fila=tablas[i].getSelectedRow();
        if(existente){int r=tablas[i].convertRowIndexToModel(fila);nombre=String.valueOf(modelos[i].getValueAt(r,1));descripcion=String.valueOf(modelos[i].getValueAt(r,2));}
        JTextField n=campo(nombre),d=campo(descripcion);
        if(!mostrarDialogoRegistro(existente,n,d))return;
        nombre=n.getText().trim();descripcion=d.getText().trim();
        if(nombre.isEmpty()||nombre.length()>100||descripcion.length()>250){aviso("Nombre obligatorio (máx. 100) y descripción de hasta 250 caracteres.");return;}
        String tabla=tablasSql[i],clave=claves[i];
        String sql=existente?"UPDATE "+tabla+" SET nombre=?,descripcion=? WHERE "+clave+"=?":
            "INSERT INTO "+tabla+" (nombre,descripcion,activo) VALUES (?,?,1)";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,nombre);p.setString(2,descripcion);if(existente)p.setInt(3,id);
            if(p.executeUpdate()!=1)throw new SQLException("No se modificó exactamente un registro.");
            cargar(i);JOptionPane.showMessageDialog(this,"Guardado correctamente.");
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,"No se guardó. Revisá nombres duplicados o la conexión.\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    private JTextField campo(String valor){
        JTextField t=new JTextField(valor);
        t.setFont(new Font("Segoe UI",Font.PLAIN,15));
        t.setPreferredSize(new Dimension(400,42));
        t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE,1,true),BorderFactory.createEmptyBorder(7,12,7,12)));
        t.setBackground(Color.WHITE);t.setForeground(new Color(35,49,68));
        return t;
    }
    private boolean mostrarDialogoRegistro(boolean existente,JTextField nombre,JTextField descripcion){
        Window owner=SwingUtilities.getWindowAncestor(this);
        final JDialog dlg=new JDialog(owner,existente?"Editar registro":"Nuevo registro",Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel raiz=new JPanel(new BorderLayout());raiz.setBackground(Color.WHITE);
        JPanel encabezado=new JPanel(new BorderLayout(0,5));encabezado.setBackground(AZUL);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18,23,18,23));
        JLabel titulo=new JLabel(existente?"EDITAR REGISTRO":"NUEVO REGISTRO");
        titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,20));
        JLabel subtitulo=new JLabel("Catálogo de productos · Serena Soft");
        subtitulo.setForeground(new Color(219,232,255));subtitulo.setFont(new Font("Segoe UI",Font.PLAIN,12));
        encabezado.add(titulo,BorderLayout.NORTH);encabezado.add(subtitulo,BorderLayout.SOUTH);
        raiz.add(encabezado,BorderLayout.NORTH);
        JPanel campos=new JPanel(new GridBagLayout());campos.setBackground(Color.WHITE);
        campos.setBorder(BorderFactory.createEmptyBorder(23,24,16,24));
        GridBagConstraints gc=new GridBagConstraints();gc.gridx=0;gc.weightx=1;gc.fill=GridBagConstraints.HORIZONTAL;
        gc.gridy=0;gc.insets=new Insets(0,0,7,0);campos.add(etiqueta("Nombre *"),gc);
        gc.gridy=1;gc.insets=new Insets(0,0,18,0);campos.add(nombre,gc);
        gc.gridy=2;gc.insets=new Insets(0,0,7,0);campos.add(etiqueta("Descripción"),gc);
        gc.gridy=3;gc.insets=new Insets(0,0,8,0);campos.add(descripcion,gc);
        gc.gridy=4;campos.add(new JLabel("* Campo obligatorio. No modifica ventas ni stock."),gc);
        raiz.add(campos,BorderLayout.CENTER);
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,14));pie.setBackground(FONDO);
        pie.setBorder(BorderFactory.createMatteBorder(1,0,0,0,BORDE));
        final boolean[] confirmado={false};
        JButton cancelar=boton("CANCELAR",GRIS,()->dlg.dispose());
        JButton guardar=boton(existente?"GUARDAR CAMBIOS":"GUARDAR",VERDE,()->{confirmado[0]=true;dlg.dispose();});
        cancelar.setPreferredSize(new Dimension(132,43));guardar.setPreferredSize(new Dimension(174,43));
        pie.add(cancelar);pie.add(guardar);raiz.add(pie,BorderLayout.SOUTH);
        dlg.setContentPane(raiz);dlg.setSize(500,385);dlg.setMinimumSize(new Dimension(450,370));
        dlg.setLocationRelativeTo(this);dlg.getRootPane().setDefaultButton(guardar);
        dlg.setVisible(true);return confirmado[0];
    }
    private JLabel etiqueta(String texto){
        JLabel l=new JLabel(texto);l.setForeground(AZUL);
        l.setFont(new Font("Segoe UI",Font.BOLD,14));return l;
    }
    private void cambiarEstado(int i){int id=seleccionado(i);if(id<0){aviso("Seleccioná primero un registro.");return;}
        int fila=tablas[i].convertRowIndexToModel(tablas[i].getSelectedRow());boolean activo="Activo".equals(modelos[i].getValueAt(fila,3));
        if(JOptionPane.showConfirmDialog(this,(activo?"Desactivar":"Activar")+" este registro?\nNo se borrarán referencias históricas.","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        String sql="UPDATE "+tablasSql[i]+" SET activo=? WHERE "+claves[i]+"=? AND activo=?";
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(sql)){
            p.setBoolean(1,!activo);p.setInt(2,id);p.setBoolean(3,activo);
            if(p.executeUpdate()!=1)throw new SQLException("El estado cambió en otra sesión. Actualizá la tabla.");
            cargar(i);
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"No se pudo cambiar el estado",JOptionPane.ERROR_MESSAGE);}
    }
    private void aviso(String mensaje){JOptionPane.showMessageDialog(this,mensaje,"Serena Soft",JOptionPane.INFORMATION_MESSAGE);}
}
