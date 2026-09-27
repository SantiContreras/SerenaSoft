/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Principal;

import Dialogos.MenuUsuarioPopup;
import Paneles.PanelVentas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import rojeru_san.rsbutton.RSButtonRound;
import Paneles.PanelArticulos;
import Paneles.PanelCaja;
import Paneles.PanelDashboard;
import Paneles.PanelProveedores;
import Paneles.PanelStock;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import model.Usuario;
import sesion.SesionUsuario;

/**
 *
 * @author santi
 */
public class PanelPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PanelPrincipal.class.getName());

    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    
    private String usuarioLogueado;

    private JLabel lblUsuarioLogueado;
    
    

    private JLabel lblFecha;

    private JPanel panelBotones;

    private JPopupMenu menuUsuario;

    private JMenuItem itemPerfil;
    private JMenuItem itemConfiguracion;
    private JMenuItem itemClave;
    private JMenuItem itemSalir;

    //botones 
    private RSButtonRound btnVentas;
    private RSButtonRound btnArticulos;
    private RSButtonRound btnClientes;
    private RSButtonRound btnStock;
    private RSButtonRound btnCaja;
    private RSButtonRound btnAdministrador;
    private RSButtonRound btnProveedores;
    private RSButtonRound btnInicio;

   //==========================================================
// CONSTRUCTOR PRINCIPAL
//==========================================================
public PanelPrincipal(String usuario) {

    initComponents();

    this.usuarioLogueado = usuario;

    setBackground(new Color(236, 240, 245));

    setSize(1200, 700);

    setLocationRelativeTo(null);

    //======================================================
    // HEADER
    //======================================================
    construirHeader();

    panelHeader.setColorPrimario(
            new Color(8, 32, 82)
    );

    panelHeader.setColorSecundario(
            new Color(18, 55, 125)
    );

    //======================================================
    // MENU USUARIO
    //======================================================
    crearMenuUsuario();

    menuUsuario =
            new MenuUsuarioPopup(this);

    configurarEventos();

    //======================================================
    // COLORES
    //======================================================
    panelContenido.setBackground(
            new Color(245, 247, 250)
    );

    panelBody.setBackground(
            new Color(245, 247, 250)
    );

    panelContenido.setLayout(
            new BorderLayout()
    );


    //======================================================
    // PANTALLA INICIAL
    //======================================================
    mostrarPanel(
            new PanelDashboard()
    );

    panelDerecho.setVisible(false);
    
    cargarUsuarioLogueado();
}

// =========================================================
// CARGAR DATOS DEL USUARIO LOGUEADO
// =========================================================

// =========================================================
// CARGAR DATOS DEL USUARIO LOGUEADO
// =========================================================

private void cargarUsuarioLogueado() {

    Usuario usuario =
            SesionUsuario.getUsuarioActual();


    // =====================================================
    // SIN USUARIO
    // =====================================================

    if (usuario == null) {

        lblUsuarioLogueado.setText(
                "Sin usuario"
        );

        btnAdministrador.setText(
                "Sin rol ▼"
        );

        return;
    }


    // =====================================================
    // NOMBRE DEL USUARIO
    // =====================================================

    lblUsuarioLogueado.setText(
            usuario.getNombreCompleto()
    );


    // =====================================================
    // ROL DEL USUARIO
    // =====================================================

    if (usuario.getRol() != null) {

        btnAdministrador.setText(
                usuario.getRol().getNombre()
                + " ▼"
        );

    } else {

        btnAdministrador.setText(
                "Sin rol ▼"
        );
    }
}


//==========================================================
// CONSTRUCTOR SIN PARAMETROS
// SOLO PARA COMPATIBILIDAD / PRUEBAS
//==========================================================
public PanelPrincipal() {

    this("Usuario");
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new rojeru_san.rspanel.RSPanelGradiente();
        panelBody = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        panelContenido = new javax.swing.JPanel();
        panelDerecho = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelHeader.setBackground(new java.awt.Color(0, 0, 153));
        panelHeader.setPreferredSize(new java.awt.Dimension(0, 70));
        panelHeader.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelBody.setBackground(new java.awt.Color(0, 0, 0));
        panelBody.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setResizeWeight(0.7);
        jSplitPane1.setLeftComponent(panelContenido);

        panelDerecho.setPreferredSize(new java.awt.Dimension(300, 0));
        jSplitPane1.setRightComponent(panelDerecho);

        panelBody.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PanelPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelDerecho;
    private rojeru_san.rspanel.RSPanelGradiente panelHeader;
    // End of variables declaration//GEN-END:variables

    private void construirHeader() {

        panelHeader.removeAll();

        panelHeader.setLayout(
                new BorderLayout()
        );

        panelHeader.setPreferredSize(
                new Dimension(
                        0,
                        90
                )
        );

        panelHeader.setColorPrimario(
                new Color(
                        10,
                        45,
                        120
                )
        );

        panelHeader.setColorSecundario(
                new Color(
                        20,
                        75,
                        170
                )
        );

        //==========================================================
        // IZQUIERDA - MARCA
        //==========================================================
        JPanel izquierda
                = new JPanel();

        izquierda.setOpaque(false);

        izquierda.setLayout(
                new BoxLayout(
                        izquierda,
                        BoxLayout.Y_AXIS
                )
        );

        izquierda.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        10,
                        15
                )
        );

        izquierda.setPreferredSize(
                new Dimension(
                        215,
                        90
                )
        );

        //==========================================================
        // TITULO
        //==========================================================
        lblTitulo
                = new JLabel(
                        "SERENA SOFT"
                );

        lblTitulo.setForeground(
                Color.WHITE
        );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        lblTitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        //==========================================================
        // SUBTITULO
        //==========================================================
        lblSubtitulo
                = new JLabel(
                        "Sistema de Gestión Comercial"
                );

        lblSubtitulo.setForeground(
                new Color(
                        220,
                        230,
                        250
                )
        );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblSubtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        izquierda.add(
                Box.createVerticalGlue()
        );

        izquierda.add(
                lblTitulo
        );

        izquierda.add(
                Box.createVerticalStrut(2)
        );

        izquierda.add(
                lblSubtitulo
        );

        izquierda.add(
                Box.createVerticalGlue()
        );

        //==========================================================
        // CENTRO - MENU PRINCIPAL
        //==========================================================
        panelBotones
                = new JPanel();

        panelBotones.setOpaque(false);

        /*
     * IMPORTANTE:
     *
     * No usamos FlowLayout porque cuando no entra
     * manda los botones a una segunda fila.
     *
     * BoxLayout mantiene SIEMPRE una sola línea.
         */
        panelBotones.setLayout(
                new BoxLayout(
                        panelBotones,
                        BoxLayout.X_AXIS
                )
        );

        panelBotones.setBorder(
                new EmptyBorder(
                        20,
                        5,
                        20,
                        5
                )
        );

        //==========================================================
        // BOTONES
        //==========================================================
        btnInicio
                = crearBotonMenu(
                        "INICIO",
                        "/img/inicio1.png",
                        100
                );

        btnVentas
                = crearBotonMenu(
                        "VENTAS",
                        "/img/sales2.png",
                        105
                );

        btnArticulos
                = crearBotonMenu(
                        "ARTICULOS",
                        "/img/producto4.png",
                        110
                );

        btnClientes
                = crearBotonMenu(
                        "CLIENTES",
                        "/img/clientes5.png",
                        110
                );

        btnStock
                = crearBotonMenu(
                        "STOCK",
                        "/img/stock1.png",
                        100
                );

        btnCaja
                = crearBotonMenu(
                        "CAJA",
                        "/img/caja1.png",
                        95
                );

        btnProveedores
                = crearBotonMenu(
                        "PROVEEDORES",
                        "/img/clientes4.png",
                        125
                );

        //==========================================================
        // AGREGAR BOTONES
        //==========================================================
        panelBotones.add(
                Box.createHorizontalGlue()
        );

        agregarBotonMenu(
                panelBotones,
                btnInicio
        );

        agregarBotonMenu(
                panelBotones,
                btnVentas
        );

        agregarBotonMenu(
                panelBotones,
                btnArticulos
        );

        agregarBotonMenu(
                panelBotones,
                btnClientes
        );

        agregarBotonMenu(
                panelBotones,
                btnStock
        );

        agregarBotonMenu(
                panelBotones,
                btnCaja
        );

        agregarBotonMenu(
                panelBotones,
                btnProveedores
        );

        panelBotones.add(
                Box.createHorizontalGlue()
        );

        //==========================================================
        // EVENTOS
        //==========================================================
        btnInicio.addActionListener(e -> {

            mostrarPanel(
                    new PanelDashboard()
            );

        });

        btnVentas.addActionListener(e -> {

            mostrarVentas();

        });

        btnArticulos.addActionListener(e -> {

            mostrarArticulos();

        });

        btnClientes.addActionListener(e -> {

            // Después conectamos PanelClientes
            // mostrarPanel(new PanelClientes());
        });

        btnStock.addActionListener(e -> {

            abrirPanelStock();

        });

        btnCaja.addActionListener(e -> {

            mostrarPanel(
                    new PanelCaja()
            );

        });

        btnProveedores.addActionListener(e -> {

            mostrarPanel(
                    new PanelProveedores()
            );

        });

        //==========================================================
        // DERECHA - USUARIO
        //==========================================================
        JPanel derecha
                = new JPanel(
                        new GridBagLayout()
                );

        derecha.setOpaque(false);

        derecha.setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        15
                )
        );

        derecha.setPreferredSize(
                new Dimension(
                        175,
                        90
                )
        );

        GridBagConstraints gbc
                = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.anchor
                = GridBagConstraints.CENTER;

        gbc.insets
                = new Insets(
                        2,
                        0,
                        2,
                        0
                );

        //==========================================================
        // FECHA
        //==========================================================
        lblFecha
                = new JLabel(
                        "24/06/2026 11:30",
                        SwingConstants.CENTER
                );

        lblFecha.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblFecha.setForeground(
                Color.WHITE
        );

        //==========================================================
        // USUARIO
        //==========================================================
        lblUsuarioLogueado
                = new JLabel(
                        "Usuario: Santiago",
                        SwingConstants.CENTER
                );

        lblUsuarioLogueado.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblUsuarioLogueado.setForeground(
                new Color(
                        215,
                        225,
                        245
                )
        );

        //==========================================================
        // ADMINISTRADOR
        //==========================================================
        btnAdministrador
                = new RSButtonRound();

        btnAdministrador.setText(
                "Administrador ▼"
        );

        btnAdministrador.setPreferredSize(
                new Dimension(
                        155,
                        30
                )
        );

        btnAdministrador.setBackground(
                new Color(
                        33,
                        70,
                        150
                )
        );

        btnAdministrador.setColorHover(
                new Color(
                        52,
                        98,
                        190
                )
        );

        btnAdministrador.setColorText(
                Color.WHITE
        );

        btnAdministrador.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        btnAdministrador.setFocusable(
                false
        );

        gbc.gridy = 0;

        derecha.add(
                lblFecha,
                gbc
        );

        gbc.gridy = 1;

        derecha.add(
                lblUsuarioLogueado,
                gbc
        );

        gbc.gridy = 2;

        gbc.insets
                = new Insets(
                        7,
                        0,
                        0,
                        0
                );

        derecha.add(
                btnAdministrador,
                gbc
        );

        //==========================================================
        // AGREGAR AL HEADER
        //==========================================================
        panelHeader.add(
                izquierda,
                BorderLayout.WEST
        );

        panelHeader.add(
                panelBotones,
                BorderLayout.CENTER
        );

        panelHeader.add(
                derecha,
                BorderLayout.EAST
        );

        panelHeader.revalidate();
        panelHeader.repaint();
    }

    private void crearMenuUsuario() {

        menuUsuario = new JPopupMenu();

        menuUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        itemPerfil = new JMenuItem("👤 Mi Perfil");

        itemConfiguracion = new JMenuItem("⚙ Configuración");

        itemClave = new JMenuItem("🔒 Cambiar contraseña");

        itemSalir = new JMenuItem("🚪 Cerrar sesión");

        menuUsuario.add(itemPerfil);

        menuUsuario.add(itemConfiguracion);

        menuUsuario.add(itemClave);

        menuUsuario.addSeparator();

        menuUsuario.add(itemSalir);

    }

    private RSButtonRound crearBotonInicio(String texto, String icono) {

        RSButtonRound b = new RSButtonRound();

        b.setText(texto);

        b.setIcon(new ImageIcon(getClass().getResource(icono)));

        b.setFont(new Font("Segoe UI", Font.BOLD, 13));

        b.setForeground(Color.WHITE);

        b.setBackground(new Color(25, 70, 150));
        b.setColorHover(new Color(55, 115, 220));

        b.setColorText(Color.WHITE);

        b.setPreferredSize(new Dimension(118, 44));
        return b;
    }

    private RSButtonRound crearBotonMenu(
            String texto,
            String icono,
            int ancho) {

        RSButtonRound boton
                = new RSButtonRound();

        boton.setText(
                texto
        );

        boton.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                icono
                        )
                )
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setColorText(
                Color.WHITE
        );

        boton.setBackground(
                new Color(
                        25,
                        70,
                        150
                )
        );

        boton.setColorHover(
                new Color(
                        55,
                        115,
                        220
                )
        );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        44
                )
        );

        boton.setMinimumSize(
                new Dimension(
                        ancho,
                        44
                )
        );

        boton.setMaximumSize(
                new Dimension(
                        ancho,
                        44
                )
        );

        boton.setFocusable(
                false
        );

        return boton;
    }
    
    private void agregarBotonMenu(
        JPanel panel,
        RSButtonRound boton) {

    panel.add(
            boton
    );

    panel.add(
            Box.createHorizontalStrut(
                    6
            )
    );
}

    private RSButtonRound crearBotonProveedores(String texto, String icono) {
        RSButtonRound b = new RSButtonRound();

        b.setText(texto);

        b.setIcon(new ImageIcon(getClass().getResource(icono)));

        b.setFont(new Font("Segoe UI", Font.BOLD, 13));

        b.setForeground(Color.WHITE);

        b.setBackground(new Color(25, 70, 150));
        b.setColorHover(new Color(55, 115, 220));

        b.setColorText(Color.WHITE);

        b.setPreferredSize(new Dimension(118, 44));
        return b;
    }

    public void mostrarVentas() {
        panelContenido.removeAll();
        panelContenido.add(new PanelVentas(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarArticulos() {
        panelContenido.removeAll();
        panelContenido.add(new PanelArticulos(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirPanelStock() {

        panelContenido.removeAll();

        PanelStock panel = new PanelStock();

        panel.setSize(panelContenido.getSize());

        panel.setLocation(0, 0);

        panelContenido.setLayout(new BorderLayout());

        panelContenido.add(panel, BorderLayout.CENTER);

        panelContenido.revalidate();

        panelContenido.repaint();

    }

    public void mostrarPanel(JPanel panel) {

        panelContenido.removeAll();

        panelContenido.setLayout(new BorderLayout());

        panelContenido.add(panel, BorderLayout.CENTER);

        panelContenido.revalidate();

        panelContenido.repaint();
    }

    private void configurarEventos() {

        btnAdministrador.addActionListener(e -> {

            menuUsuario.show(
                    btnAdministrador,
                    0,
                    btnAdministrador.getHeight() + 5);

        });
    }
}
