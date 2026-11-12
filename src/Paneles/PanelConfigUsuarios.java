package Paneles;

import model.Rol;
import model.Usuario;
import services.ResultadoOperacion;
import services.UsuarioService;
import sesion.SesionUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Administración real de usuarios. Los permisos por usuario se implementan en una etapa posterior. */
public class PanelConfigUsuarios extends JPanel {
    private static final Color AZUL = new Color(25,70,145);
    private static final Color OSCURO = new Color(15,50,110);
    private static final Color FONDO = new Color(245,247,250);
    private static final Color VERDE = new Color(25,135,84);
    private static final Color BORDE = new Color(215,222,232);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final UsuarioService servicio = new UsuarioService();
    private final JTextField buscar = new JTextField(22);
    private final JComboBox<String> filtroRol = new JComboBox<>();
    private final JLabel total = new JLabel("0"), activos = new JLabel("0"), inactivos = new JLabel("0");
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID","Usuario","Nombre","Rol","Estado","Último acceso"},0) {
        @Override public boolean isCellEditable(int r,int c){return false;}
        @Override public Class<?> getColumnClass(int c){return c==0?Integer.class:String.class;}
    };
    private final JTable tabla = new JTable(modelo);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
    private final List<Usuario> usuarios = new ArrayList<>();
    private JButton nuevo, editar, estado, clave, roles;

    public PanelConfigUsuarios(){
        setLayout(new BorderLayout()); setBackground(FONDO);
        JPanel contenido = new JPanel(new BorderLayout(0,14));
        contenido.setBackground(Color.WHITE); contenido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(22,24,20,24)));
        JPanel superior = new JPanel(); superior.setOpaque(false); superior.setLayout(new BoxLayout(superior,BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("USUARIOS"); titulo.setFont(new Font("Segoe UI",Font.BOLD,23)); titulo.setForeground(OSCURO); titulo.setAlignmentX(LEFT_ALIGNMENT);
        JLabel subtitulo = new JLabel("Administración de usuarios y accesos al sistema"); subtitulo.setFont(new Font("Segoe UI",Font.PLAIN,13)); subtitulo.setForeground(new Color(100,110,125)); subtitulo.setAlignmentX(LEFT_ALIGNMENT);
        superior.add(titulo); superior.add(Box.createVerticalStrut(4)); superior.add(subtitulo);
        contenido.add(superior,BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0,13)); cuerpo.setOpaque(false);
        JPanel filtros = new JPanel(new BorderLayout(12,0)); filtros.setOpaque(false);
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT,8,2)); izquierda.setOpaque(false);
        izquierda.add(new JLabel("Buscar")); buscar.setPreferredSize(new Dimension(260,32)); izquierda.add(buscar);
        izquierda.add(new JLabel("Rol")); filtroRol.setPreferredSize(new Dimension(155,32)); izquierda.add(filtroRol);
        JButton actualizar = boton("ACTUALIZAR",AZUL,115); izquierda.add(actualizar);
        filtros.add(izquierda,BorderLayout.CENTER); cuerpo.add(filtros,BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0,12)); centro.setOpaque(false);
        JPanel tarjetas = new JPanel(new GridLayout(1,3,12,0)); tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("TOTAL USUARIOS",total,AZUL)); tarjetas.add(tarjeta("ACTIVOS",activos,VERDE)); tarjetas.add(tarjeta("INACTIVOS / BLOQUEADOS",inactivos,new Color(195,55,55)));
        centro.add(tarjetas,BorderLayout.NORTH);
        tabla.setRowHeight(31); tabla.setFont(new Font("Segoe UI",Font.PLAIN,13)); tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(new Color(215,230,250)); tabla.setSelectionForeground(OSCURO); tabla.setGridColor(new Color(229,233,240));
        tabla.setAutoCreateRowSorter(false); tabla.setRowSorter(sorter); sorter.setComparator(0,(a,b)->Integer.compare((Integer)a,(Integer)b));
        tabla.getTableHeader().setPreferredSize(new Dimension(0,36)); tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,selected,focus,row,col);l.setBackground(OSCURO);l.setForeground(Color.WHITE);l.setOpaque(true);l.setHorizontalAlignment(SwingConstants.CENTER);l.setFont(new Font("Segoe UI",Font.BOLD,12));return l;
            }
        });
        JScrollPane scroll = new JScrollPane(tabla); scroll.setPreferredSize(new Dimension(720,270)); scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),"Listado de usuarios"));
        centro.add(scroll,BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,5)); acciones.setOpaque(false);
        nuevo=boton("NUEVO USUARIO",VERDE,145); editar=boton("EDITAR",AZUL,105); estado=boton("ACTIVAR / DESACTIVAR",new Color(105,115,130),180); clave=boton("RESTABLECER CLAVE",new Color(225,140,20),165);
        acciones.add(nuevo);acciones.add(editar);acciones.add(estado);acciones.add(clave);
        centro.add(acciones,BorderLayout.SOUTH); cuerpo.add(centro,BorderLayout.CENTER);
        JPanel inferior=new JPanel(new BorderLayout(8,0)); inferior.setOpaque(false); inferior.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),"Roles y permisos"));
        JLabel aviso=new JLabel("Los permisos por rol y por empleado se configurarán en la siguiente etapa.");aviso.setForeground(new Color(95,105,120));aviso.setFont(new Font("Segoe UI",Font.PLAIN,12));inferior.add(aviso,BorderLayout.CENTER);
        roles=boton("GESTIONAR ROLES",AZUL,160);inferior.add(roles,BorderLayout.EAST);cuerpo.add(inferior,BorderLayout.SOUTH);
        contenido.add(cuerpo,BorderLayout.CENTER);add(contenido,BorderLayout.CENTER);
        buscar.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){filtrar();}public void removeUpdate(DocumentEvent e){filtrar();}public void changedUpdate(DocumentEvent e){filtrar();}});
        filtroRol.addActionListener(e->filtrar()); actualizar.addActionListener(e->recargar());
        nuevo.addActionListener(e->formulario(null));editar.addActionListener(e->{Usuario u=seleccionado();if(u!=null)formulario(u);});
        estado.addActionListener(e->cambiarEstado());clave.addActionListener(e->restablecerClave());
        roles.addActionListener(e->JOptionPane.showMessageDialog(this,"La administración de roles y permisos se incorporará en la siguiente etapa.\nTodavía no se modifican permisos desde esta pantalla.","Roles y permisos",JOptionPane.INFORMATION_MESSAGE));
        recargar();
    }
    private JPanel tarjeta(String nombre,JLabel valor,Color color){JPanel p=new JPanel(new BorderLayout(0,5));p.setBackground(new Color(249,250,253));p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(10,13,10,13)));JLabel l=new JLabel(nombre);l.setForeground(OSCURO);l.setFont(new Font("Segoe UI",Font.BOLD,11));valor.setForeground(color);valor.setFont(new Font("Segoe UI",Font.BOLD,22));p.add(l,BorderLayout.NORTH);p.add(valor,BorderLayout.CENTER);return p;}
    private JButton boton(String texto,Color color,int ancho){JButton b=new JButton(texto);b.setPreferredSize(new Dimension(ancho,36));b.setBackground(color);b.setForeground(Color.WHITE);b.setFocusPainted(false);b.setFont(new Font("Segoe UI",Font.BOLD,11));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    public final void recargar(){
        if(!SesionUsuario.esAdministrador()){modelo.setRowCount(0);usuarios.clear();total.setText("-");activos.setText("-");inactivos.setText("-");setAcciones(false);return;}
        try{
            List<Usuario> lista=servicio.listarUsuarios();List<Rol> listaRoles=servicio.listarRoles();
            usuarios.clear();modelo.setRowCount(0);int a=0,i=0;
            if(lista!=null)for(Usuario u:lista){if(u==null)continue;usuarios.add(u);String est=u.getEstado()==null?"SIN ESTADO":u.getEstado();if(u.estaActivo())a++;else i++;modelo.addRow(new Object[]{u.getIdUsuario(),u.getUsername(),u.getNombreCompleto(),u.getRol()==null?"Sin rol":u.getRol().getNombre(),est,u.getUltimoAcceso()==null?"Sin acceso":u.getUltimoAcceso().format(FECHA)});}
            String anterior=(String)filtroRol.getSelectedItem();filtroRol.removeAllItems();filtroRol.addItem("Todos");if(listaRoles!=null)for(Rol r:listaRoles)filtroRol.addItem(r.getNombre());
            if(anterior!=null)filtroRol.setSelectedItem(anterior);if(filtroRol.getSelectedIndex()<0)filtroRol.setSelectedIndex(0);
            total.setText(String.valueOf(usuarios.size()));activos.setText(String.valueOf(a));inactivos.setText(String.valueOf(i));filtrar();setAcciones(true);
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"No se pudieron cargar usuarios desde MariaDB:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    private void setAcciones(boolean habilitar){nuevo.setEnabled(habilitar);editar.setEnabled(habilitar);estado.setEnabled(habilitar);clave.setEnabled(habilitar);roles.setEnabled(habilitar);}
    private void filtrar(){if(sorter==null)return;String q=buscar.getText().trim();String rol=(String)filtroRol.getSelectedItem();List<RowFilter<Object,Object>> f=new ArrayList<>();if(!q.isEmpty())f.add(RowFilter.regexFilter("(?i)"+Pattern.quote(q),1,2));if(rol!=null&&!"Todos".equals(rol))f.add(RowFilter.regexFilter("(?i)^"+Pattern.quote(rol)+"$",3));sorter.setRowFilter(RowFilter.andFilter(f));}
    private Usuario seleccionado(){int v=tabla.getSelectedRow();if(v<0){JOptionPane.showMessageDialog(this,"Seleccione un usuario de la tabla.");return null;}int m=tabla.convertRowIndexToModel(v);Integer id=(Integer)modelo.getValueAt(m,0);for(Usuario u:usuarios)if(id.equals(u.getIdUsuario()))return u;return null;}
    private boolean autorizado(){if(SesionUsuario.esAdministrador())return true;JOptionPane.showMessageDialog(this,"Solo el administrador puede gestionar usuarios.","Acceso denegado",JOptionPane.WARNING_MESSAGE);return false;}
    private void formulario(Usuario original){
        if(!autorizado())return;
        JTextField nombre=new JTextField(original==null?"":original.getNombreCompleto(),22),user=new JTextField(original==null?"":original.getUsername(),22),email=new JTextField(original==null||original.getEmail()==null?"":original.getEmail(),22);
        JPasswordField pass=new JPasswordField(22),confirmar=new JPasswordField(22);
        JComboBox<Rol> combo=new JComboBox<>();for(Rol r:servicio.listarRoles())combo.addItem(r);
        if(original!=null&&original.getRol()!=null)for(int x=0;x<combo.getItemCount();x++)if(combo.getItemAt(x).getIdRol()==original.getRol().getIdRol())combo.setSelectedIndex(x);
        JPanel p=new JPanel(new GridBagLayout());GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(5,6,5,6);c.fill=GridBagConstraints.HORIZONTAL;
        agregarCampo(p,c,0,"Nombre completo",nombre);agregarCampo(p,c,1,"Usuario",user);agregarCampo(p,c,2,"Email",email);agregarCampo(p,c,3,"Rol",combo);
        if(original==null){agregarCampo(p,c,4,"Contraseña",pass);agregarCampo(p,c,5,"Confirmar contraseña",confirmar);}
        int op=JOptionPane.showConfirmDialog(this,p,original==null?"Nuevo usuario":"Editar usuario",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(op!=JOptionPane.OK_OPTION)return;
        Rol rol=(Rol)combo.getSelectedItem();ResultadoOperacion r;
        if(original==null)r=servicio.crearUsuario(nombre.getText(),user.getText(),new String(pass.getPassword()),new String(confirmar.getPassword()),email.getText(),rol);
        else r=servicio.editarUsuario(original.getIdUsuario(),nombre.getText(),user.getText(),email.getText(),rol);
        resultado(r);
    }
    private void agregarCampo(JPanel p,GridBagConstraints c,int fila,String etiqueta,JComponent comp){c.gridy=fila;c.gridx=0;c.weightx=0;p.add(new JLabel(etiqueta),c);c.gridx=1;c.weightx=1;p.add(comp,c);}
    private void cambiarEstado(){if(!autorizado())return;Usuario u=seleccionado();if(u==null)return;boolean desactivar=u.estaActivo();String accion=desactivar?"desactivar":"activar";if(JOptionPane.showConfirmDialog(this,"¿Desea "+accion+" al usuario "+u.getUsername()+"?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;resultado(desactivar?servicio.desactivarUsuario(u.getIdUsuario()):servicio.activarUsuario(u.getIdUsuario()));}
    private void restablecerClave(){if(!autorizado())return;Usuario u=seleccionado();if(u==null)return;JPasswordField p1=new JPasswordField(20),p2=new JPasswordField(20);JPanel p=new JPanel(new GridLayout(4,1,4,5));p.add(new JLabel("Nueva contraseña para "+u.getUsername()));p.add(p1);p.add(new JLabel("Confirmar contraseña"));p.add(p2);if(JOptionPane.showConfirmDialog(this,p,"Restablecer clave",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;resultado(servicio.cambiarPassword(u.getIdUsuario(),new String(p1.getPassword()),new String(p2.getPassword())));}
    private void resultado(ResultadoOperacion r){JOptionPane.showMessageDialog(this,r.getMensaje(),"Usuarios",r.isExitoso()?JOptionPane.INFORMATION_MESSAGE:JOptionPane.WARNING_MESSAGE);if(r.isExitoso())recargar();}
}
