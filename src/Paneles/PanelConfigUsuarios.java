package Paneles;

import model.Rol;
import model.Permiso;
import Dao.RolDao;
import java.util.Set;
import java.util.HashSet;
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
        JPanel contenido = new JPanel(new BorderLayout(0,9));
        contenido.setBackground(Color.WHITE); contenido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(14,18,14,18)));
        JPanel superior = new JPanel(); superior.setOpaque(false); superior.setLayout(new BoxLayout(superior,BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("USUARIOS"); titulo.setFont(new Font("Segoe UI",Font.BOLD,23)); titulo.setForeground(OSCURO); titulo.setAlignmentX(LEFT_ALIGNMENT);
        JLabel subtitulo = new JLabel("Administración de usuarios y accesos al sistema"); subtitulo.setFont(new Font("Segoe UI",Font.PLAIN,13)); subtitulo.setForeground(new Color(100,110,125)); subtitulo.setAlignmentX(LEFT_ALIGNMENT);
        superior.add(titulo); superior.add(Box.createVerticalStrut(4)); superior.add(subtitulo);
        contenido.add(superior,BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0,8)); cuerpo.setOpaque(false);
        JPanel filtros = new JPanel(new BorderLayout(12,0)); filtros.setOpaque(false);
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT,8,2)); izquierda.setOpaque(false);
        izquierda.add(new JLabel("Buscar")); buscar.setPreferredSize(new Dimension(260,32)); izquierda.add(buscar);
        izquierda.add(new JLabel("Rol")); filtroRol.setPreferredSize(new Dimension(155,32)); izquierda.add(filtroRol);
        JButton actualizar = boton("ACTUALIZAR",AZUL,115); izquierda.add(actualizar);
        filtros.add(izquierda,BorderLayout.CENTER); cuerpo.add(filtros,BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0,8)); centro.setOpaque(false);
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
        JScrollPane scroll = new JScrollPane(tabla); scroll.setPreferredSize(new Dimension(720,350)); scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),"Listado de usuarios"));
        centro.add(scroll,BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,5)); acciones.setOpaque(false);
        nuevo=boton("NUEVO USUARIO",VERDE,145); editar=boton("EDITAR",AZUL,105); estado=boton("ACTIVAR / DESACTIVAR",new Color(105,115,130),180); clave=boton("RESTABLECER CLAVE",new Color(225,140,20),165);
        acciones.add(nuevo);acciones.add(editar);acciones.add(estado);acciones.add(clave);
        centro.add(acciones,BorderLayout.SOUTH); cuerpo.add(centro,BorderLayout.CENTER);
        JPanel inferior=new JPanel(new BorderLayout(8,0)); inferior.setOpaque(false); inferior.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),"Roles y permisos"));
        JLabel aviso=new JLabel("Configure los permisos por rol. Las excepciones individuales se incorporarán después.");aviso.setForeground(new Color(95,105,120));aviso.setFont(new Font("Segoe UI",Font.PLAIN,12));inferior.add(aviso,BorderLayout.CENTER);
        roles=boton("GESTIONAR ROLES",AZUL,160);inferior.add(roles,BorderLayout.EAST);cuerpo.add(inferior,BorderLayout.SOUTH);
        contenido.add(cuerpo,BorderLayout.CENTER);add(contenido,BorderLayout.CENTER);
        buscar.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){filtrar();}public void removeUpdate(DocumentEvent e){filtrar();}public void changedUpdate(DocumentEvent e){filtrar();}});
        filtroRol.addActionListener(e->filtrar()); actualizar.addActionListener(e->recargar());
        nuevo.addActionListener(e->formulario(null));editar.addActionListener(e->{Usuario u=seleccionado();if(u!=null)formulario(u);});
        estado.addActionListener(e->cambiarEstado());clave.addActionListener(e->restablecerClave());
        roles.addActionListener(e->gestionarRoles());
        recargar();
    }
    private JPanel tarjeta(String nombre,JLabel valor,Color color){JPanel p=new JPanel(new BorderLayout(0,5));p.setBackground(new Color(249,250,253));p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(10,13,10,13)));JLabel l=new JLabel(nombre);l.setForeground(OSCURO);l.setFont(new Font("Segoe UI",Font.BOLD,11));valor.setForeground(color);valor.setFont(new Font("Segoe UI",Font.BOLD,22));p.add(l,BorderLayout.NORTH);p.add(valor,BorderLayout.CENTER);return p;}
    private JButton boton(String texto,Color color,int ancho){
        JButton b=botonColorSolido(texto,color);
        b.setPreferredSize(new Dimension(ancho,36));
        b.setFont(new Font("Segoe UI",Font.BOLD,11));
        return b;
    }
    /** Botón totalmente pintado a mano: evita que Windows L&F tape el fondo y texto. */
    private JButton botonColorSolido(String texto,Color fondo){
        JButton b=new JButton(texto){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                try{
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    Color base=isEnabled()?fondo:new Color(155,163,175);
                    if(isEnabled()&&getModel().isPressed())base=base.darker();
                    else if(isEnabled()&&getModel().isRollover())base=base.brighter();
                    g2.setColor(base);
                    g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                    g2.setColor(Color.WHITE);
                    Font fuente=getFont();
                    if(fuente==null)fuente=new Font("Segoe UI",Font.BOLD,12);
                    g2.setFont(fuente);
                    FontMetrics fm=g2.getFontMetrics();
                    String etiqueta=getText();
                    int x=(getWidth()-fm.stringWidth(etiqueta))/2;
                    int y=(getHeight()-fm.getHeight())/2+fm.getAscent();
                    g2.drawString(etiqueta,Math.max(5,x),y);
                }finally{g2.dispose();}
            }
            @Override protected void paintBorder(Graphics g){}
        };
        b.setForeground(Color.WHITE);
        b.setBackground(fondo);
        b.setFont(new Font("Segoe UI",Font.BOLD,12));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
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
    private void filtrar(){if(sorter==null)return;String q=buscar.getText().trim();String rol=(String)filtroRol.getSelectedItem();List<RowFilter<Object,Object>> f=new ArrayList<>();if(!q.isEmpty())f.add(RowFilter.regexFilter("(?i)"+Pattern.quote(q),1,2,3));if(rol!=null&&!"Todos".equals(rol))f.add(RowFilter.regexFilter("(?i)^"+Pattern.quote(rol)+"$",3));sorter.setRowFilter(RowFilter.andFilter(f));}
    private Usuario seleccionado(){int v=tabla.getSelectedRow();if(v<0){JOptionPane.showMessageDialog(this,"Seleccione un usuario de la tabla.");return null;}int m=tabla.convertRowIndexToModel(v);Integer id=(Integer)modelo.getValueAt(m,0);for(Usuario u:usuarios)if(id.equals(u.getIdUsuario()))return u;return null;}
    private boolean autorizado(){if(SesionUsuario.esAdministrador())return true;JOptionPane.showMessageDialog(this,"Solo el administrador puede gestionar usuarios.","Acceso denegado",JOptionPane.WARNING_MESSAGE);return false;}
    private void formulario(Usuario original){
        if(!autorizado())return;
        JTextField nombre=new JTextField(original==null?"":original.getNombreCompleto());
        JTextField user=new JTextField(original==null?"":original.getUsername());
        JTextField email=new JTextField(original==null||original.getEmail()==null?"":original.getEmail());
        JPasswordField pass=new JPasswordField(), confirmar=new JPasswordField();
        JComboBox<Rol> combo=new JComboBox<>();
        for(Rol r:servicio.listarRoles())combo.addItem(r);
        if(original!=null&&original.getRol()!=null)for(int x=0;x<combo.getItemCount();x++)
            if(combo.getItemAt(x).getIdRol()==original.getRol().getIdRol())combo.setSelectedIndex(x);
        JPanel campos=new JPanel(new GridBagLayout());campos.setBackground(Color.WHITE);
        campos.setBorder(new EmptyBorder(16,24,8,24));
        GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(5,7,5,7);c.fill=GridBagConstraints.HORIZONTAL;
        agregarCampoModerno(campos,c,0,"Nombre completo *",nombre);
        agregarCampoModerno(campos,c,1,"Usuario *",user);
        agregarCampoModerno(campos,c,2,"Correo electrónico",email);
        agregarCampoModerno(campos,c,3,"Rol asignado *",combo);
        if(original==null){
            agregarCampoModerno(campos,c,4,"Contraseña *",pass);
            agregarCampoModerno(campos,c,5,"Confirmar contraseña *",confirmar);
            JCheckBox ver=new JCheckBox("Mostrar contraseñas");ver.setOpaque(false);
            ver.setFont(new Font("Segoe UI",Font.PLAIN,12));ver.setForeground(OSCURO);
            final char eco=pass.getEchoChar();
            ver.addActionListener(e->{char v=ver.isSelected()?(char)0:eco;pass.setEchoChar(v);confirmar.setEchoChar(v);});
            c.gridx=1;c.gridy=6;c.weightx=1;campos.add(ver,c);
        }
        JLabel nota=new JLabel(original==null?"La contraseña debe tener al menos 8 caracteres.":"La contraseña se cambia desde Restablecer clave.");
        nota.setFont(new Font("Segoe UI",Font.PLAIN,12));nota.setForeground(new Color(100,112,130));
        JPanel notaPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,24,4));notaPanel.setBackground(Color.WHITE);notaPanel.add(nota);
        JPanel contenido=new JPanel(new BorderLayout());contenido.setBackground(Color.WHITE);
        contenido.add(campos,BorderLayout.CENTER);contenido.add(notaPanel,BorderLayout.SOUTH);
        JDialog dlg=dialogoModerno(original==null?"NUEVO USUARIO":"EDITAR USUARIO",
            original==null?"Datos personales y credenciales de acceso":"Actualizar los datos y el rol del empleado",contenido,590,original==null?640:510);
        JPanel pie=(JPanel)dlg.getRootPane().getClientProperty("serena.pie");
        JButton cancelar=botonDialogo("CANCELAR",new Color(105,117,135));
        JButton guardar=botonDialogo(original==null?"GUARDAR USUARIO":"GUARDAR CAMBIOS",VERDE);
        pie.add(cancelar);pie.add(guardar);
        cancelar.addActionListener(e->dlg.dispose());
        guardar.addActionListener(e->{
            Rol rol=(Rol)combo.getSelectedItem();ResultadoOperacion r;
            if(original==null)r=servicio.crearUsuario(nombre.getText(),user.getText(),new String(pass.getPassword()),new String(confirmar.getPassword()),email.getText(),rol);
            else r=servicio.editarUsuario(original.getIdUsuario(),nombre.getText(),user.getText(),email.getText(),rol);
            if(r.isExitoso()){dlg.dispose();resultado(r);}else JOptionPane.showMessageDialog(dlg,r.getMensaje(),"Verifique los datos",JOptionPane.WARNING_MESSAGE);
        });
        dlg.getRootPane().setDefaultButton(guardar);
        mostrarDialogoCentrado(dlg);
    }
    private void agregarCampoModerno(JPanel p,GridBagConstraints c,int fila,String etiqueta,JComponent comp){
        JLabel l=new JLabel(etiqueta);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(OSCURO);
        c.gridy=fila;c.gridx=0;c.weightx=0;c.anchor=GridBagConstraints.WEST;c.ipady=0;p.add(l,c);
        comp.setFont(new Font("Segoe UI",Font.PLAIN,13));comp.setPreferredSize(new Dimension(290,35));
        if(comp instanceof JTextField)((JTextField)comp).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),new EmptyBorder(5,9,5,9)));
        c.gridx=1;c.weightx=1;p.add(comp,c);
    }
    private JButton botonDialogo(String texto,Color fondo){
        JButton b=botonColorSolido(texto,fondo);
        b.setBorder(new EmptyBorder(11,19,11,19));
        b.setPreferredSize(new Dimension(texto.length()>14?175:130,42));
        return b;
    }
    private JDialog dialogoModerno(String titulo,String subtitulo,JComponent cuerpo,int ancho,int alto){
        Window owner=SwingUtilities.getWindowAncestor(this);
        JDialog dlg=new JDialog(owner,titulo,Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel base=new JPanel(new BorderLayout());base.setBackground(Color.WHITE);
        JPanel cabecera=new JPanel(new BorderLayout(0,5));cabecera.setBackground(OSCURO);
        cabecera.setBorder(new EmptyBorder(18,24,17,24));
        JLabel t=new JLabel(titulo);t.setFont(new Font("Segoe UI",Font.BOLD,21));t.setForeground(Color.WHITE);
        JLabel sub=new JLabel(subtitulo);sub.setFont(new Font("Segoe UI",Font.PLAIN,12));sub.setForeground(new Color(215,229,249));
        cabecera.add(t,BorderLayout.NORTH);cabecera.add(sub,BorderLayout.SOUTH);
        base.add(cabecera,BorderLayout.NORTH);base.add(cuerpo,BorderLayout.CENTER);
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,13));pie.setBackground(new Color(244,247,251));
        pie.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,0,0,0,BORDE),new EmptyBorder(4,14,5,14)));base.add(pie,BorderLayout.SOUTH);
        dlg.getRootPane().putClientProperty("serena.pie",pie);
        dlg.setContentPane(base);
        // Tamaño de referencia; el tamaño definitivo se calcula después de añadir los botones.
        dlg.setSize(ancho,alto);
        return dlg;
    }
    /** Ajusta el alto al contenido real (incluyendo botones) y centra en la ventana principal. */
    private void mostrarDialogoCentrado(JDialog dlg) {
        dlg.pack();
        Dimension natural = dlg.getPreferredSize();
        Dimension actual = dlg.getSize();
        int ancho = Math.max(actual.width, natural.width + 16);
        int alto = Math.max(actual.height, natural.height + 24);
        Window owner = SwingUtilities.getWindowAncestor(this);
        Rectangle disponible = owner != null ? owner.getGraphicsConfiguration().getBounds()
                : GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
                owner != null ? owner.getGraphicsConfiguration()
                        : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        int maxAlto = disponible.height - insets.top - insets.bottom - 36;
        int maxAncho = disponible.width - insets.left - insets.right - 36;
        dlg.setSize(Math.min(ancho, maxAncho), Math.min(alto, maxAlto));
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }
    private void cambiarEstado(){if(!autorizado())return;Usuario u=seleccionado();if(u==null)return;boolean desactivar=u.estaActivo();String accion=desactivar?"desactivar":"activar";if(JOptionPane.showConfirmDialog(this,"¿Desea "+accion+" al usuario "+u.getUsername()+"?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;resultado(desactivar?servicio.desactivarUsuario(u.getIdUsuario()):servicio.activarUsuario(u.getIdUsuario()));}
    private void restablecerClave(){
        if(!autorizado())return;Usuario u=seleccionado();if(u==null)return;
        JPasswordField p1=new JPasswordField(),p2=new JPasswordField();
        JPanel campos=new JPanel(new GridBagLayout());campos.setBackground(Color.WHITE);
        campos.setBorder(new EmptyBorder(22,24,15,24));
        GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(7,7,7,7);c.fill=GridBagConstraints.HORIZONTAL;
        agregarCampoModerno(campos,c,0,"Nueva contraseña",p1);
        agregarCampoModerno(campos,c,1,"Confirmar contraseña",p2);
        JCheckBox ver=new JCheckBox("Mostrar contraseñas");ver.setOpaque(false);
        final char eco=p1.getEchoChar();
        ver.addActionListener(e->{char v=ver.isSelected()?(char)0:eco;p1.setEchoChar(v);p2.setEchoChar(v);});
        c.gridx=1;c.gridy=2;campos.add(ver,c);
        JDialog dlg=dialogoModerno("RESTABLECER CONTRASEÑA","Usuario: "+u.getUsername(),campos,570,390);
        JPanel pie=(JPanel)dlg.getRootPane().getClientProperty("serena.pie");
        JButton cancelar=botonDialogo("CANCELAR",new Color(105,117,135));
        JButton guardar=botonDialogo("GUARDAR CLAVE",VERDE);pie.add(cancelar);pie.add(guardar);
        cancelar.addActionListener(e->dlg.dispose());
        guardar.addActionListener(e->{ResultadoOperacion r=servicio.cambiarPassword(u.getIdUsuario(),new String(p1.getPassword()),new String(p2.getPassword()));
            if(r.isExitoso()){dlg.dispose();resultado(r);}else JOptionPane.showMessageDialog(dlg,r.getMensaje(),"Verifique la contraseña",JOptionPane.WARNING_MESSAGE);
        });dlg.getRootPane().setDefaultButton(guardar);mostrarDialogoCentrado(dlg);
    }
    private void resultado(ResultadoOperacion r){JOptionPane.showMessageDialog(this,r.getMensaje(),"Usuarios",r.isExitoso()?JOptionPane.INFORMATION_MESSAGE:JOptionPane.WARNING_MESSAGE);if(r.isExitoso()){buscar.setText("");filtroRol.setSelectedItem("Todos");recargar();if(!usuarios.isEmpty()){int m=usuarios.size()-1;tabla.scrollRectToVisible(tabla.getCellRect(tabla.convertRowIndexToView(m),0,true));}}}
    private void gestionarRoles() {
        if(!autorizado())return;
        RolDao dao=new RolDao();
        try {
            List<Rol> lista=dao.listarActivos();
            JComboBox<Rol> combo=new JComboBox<>();
            for(Rol r:lista)if(!"ADMINISTRADOR".equalsIgnoreCase(r.getNombre()))combo.addItem(r);
            if(combo.getItemCount()==0){JOptionPane.showMessageDialog(this,"No hay roles editables.");return;}
            List<Permiso> catalogo=dao.listarPermisos();
            if(catalogo.isEmpty()){JOptionPane.showMessageDialog(this,"No hay permisos activos en la tabla permiso.");return;}
            JPanel listaChecks=new JPanel();listaChecks.setLayout(new BoxLayout(listaChecks,BoxLayout.Y_AXIS));
            listaChecks.setBackground(Color.WHITE);
            java.util.Map<String,JPanel> grupos=new java.util.LinkedHashMap<>();
            List<JCheckBox> checks=new ArrayList<>();
            for(Permiso permiso:catalogo){
                String modulo=permiso.getModulo();
                JPanel grupo=grupos.get(modulo);
                if(grupo==null){
                    grupo=new JPanel();grupo.setLayout(new BoxLayout(grupo,BoxLayout.Y_AXIS));
                    grupo.setBackground(Color.WHITE);
                    grupo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),modulo));
                    grupo.setAlignmentX(Component.LEFT_ALIGNMENT);
                    grupos.put(modulo,grupo);
                    listaChecks.add(grupo);
                    listaChecks.add(Box.createVerticalStrut(7));
                }
                JCheckBox ch=new JCheckBox(permiso.getNombre()+"  ["+permiso.getCodigo()+"]");
                ch.setBackground(Color.WHITE);
                ch.setFont(new Font("Segoe UI",Font.PLAIN,12));
                ch.putClientProperty("id",permiso.getIdPermiso());checks.add(ch);grupo.add(ch);
            }
            Runnable cargar=()->{
                Rol r=(Rol)combo.getSelectedItem();if(r==null)return;
                try {Set<Integer> ids=dao.permisosDeRol(r.getIdRol());
                    for(JCheckBox ch:checks)ch.setSelected(ids.contains((Integer)ch.getClientProperty("id")));
                }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Error de permisos",JOptionPane.ERROR_MESSAGE);}
            };
            combo.addActionListener(e->cargar.run());cargar.run();
            JPanel panel=new JPanel(new BorderLayout(0,10));
            JPanel arriba=new JPanel(new BorderLayout(8,0));arriba.add(new JLabel("Rol:"),BorderLayout.WEST);arriba.add(combo,BorderLayout.CENTER);
            panel.add(arriba,BorderLayout.NORTH);
            JScrollPane scroll=new JScrollPane(listaChecks);scroll.setPreferredSize(new Dimension(560,360));panel.add(scroll,BorderLayout.CENTER);
            panel.add(new JLabel("Los cambios se aplicarán a los próximos inicios de sesión."),BorderLayout.SOUTH);
            int opcion=JOptionPane.showConfirmDialog(this,panel,"Gestionar permisos por rol",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(opcion!=JOptionPane.OK_OPTION)return;
            Rol elegido=(Rol)combo.getSelectedItem();if(elegido==null)return;
            Set<Integer> ids=new HashSet<>();for(JCheckBox ch:checks)if(ch.isSelected())ids.add((Integer)ch.getClientProperty("id"));
            if(JOptionPane.showConfirmDialog(this,"¿Guardar "+ids.size()+" permisos para el rol "+elegido.getNombre()+"?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            dao.guardarPermisos(elegido.getIdRol(),ids);
            JOptionPane.showMessageDialog(this,"Permisos del rol actualizados correctamente.\nLos usuarios deben volver a iniciar sesión.");
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"No se pudieron administrar los permisos:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
}
