package Login;

import Principal.PanelPrincipal;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.security.Principal;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import services.LoginService;
import services.ResultadoLogin;
import sesion.SesionUsuario;



public class Login extends JFrame {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO
            = new Color(10, 38, 90);

    private static final Color AZUL_PRINCIPAL
            = new Color(20, 75, 165);

    private static final Color AZUL_HOVER
            = new Color(35, 95, 195);

    private static final Color FONDO
            = new Color(242, 245, 249);

    private static final Color BORDE
            = new Color(210, 218, 228);

    private static final Color TEXTO
            = new Color(40, 48, 60);

    private static final Color TEXTO_SECUNDARIO
            = new Color(105, 113, 125);

    private static final Color ROJO
            = new Color(190, 55, 65);

    private static final Color VERDE
            = new Color(25, 135, 84);

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    private JCheckBox chkMostrarPassword;

    private JButton btnIniciarSesion;

    private JLabel lblMensaje;

    private char echoOriginal;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public Login() {

        inicializarVentana();

        inicializarComponentes();

        construirInterfaz();

        configurarEventos();

        SwingUtilities.invokeLater(() -> {
            txtUsuario.requestFocusInWindow();
        });
    }

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        iniciarSesion();
    }

    private void iniciarSesion() {

        String username
                = txtUsuario.getText().trim();

        String password
                = new String(
                        txtPassword.getPassword()
                );

        LoginService loginService
                = new LoginService();

        ResultadoLogin resultado
                = loginService.login(
                        username,
                        password
                );

        if (!resultado.isCorrecto()) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Serena Soft",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            txtPassword.setText("");
            txtPassword.requestFocus();

            return;
        }

        SesionUsuario.iniciarSesion(
                resultado.getUsuario()
        );

        abrirSistemaPrincipal();
    }

   private void abrirSistemaPrincipal() {

    PanelPrincipal principal = new PanelPrincipal();

    principal.setLocationRelativeTo(null);
    principal.setVisible(true);

    this.dispose();
}

    //==========================================================
    // VENTANA
    //==========================================================
    private void inicializarVentana() {

        setTitle(
                "Serena Soft - Iniciar Sesión"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(
                1100,
                680
        );

        setMinimumSize(
                new Dimension(
                        980,
                        620
                )
        );

        setLocationRelativeTo(null);

        setResizable(true);

        getContentPane().setBackground(
                FONDO
        );
    }

    //==========================================================
    // COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // USUARIO
        //======================================================
        txtUsuario
                = new JTextField();

        configurarCampo(
                txtUsuario
        );

        //======================================================
        // PASSWORD
        //======================================================
        txtPassword
                = new JPasswordField();

        configurarCampo(
                txtPassword
        );

        echoOriginal
                = txtPassword.getEchoChar();

        //======================================================
        // MOSTRAR PASSWORD
        //======================================================
        chkMostrarPassword
                = new JCheckBox(
                        "Mostrar contraseña"
                );

        chkMostrarPassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        chkMostrarPassword.setForeground(
                TEXTO_SECUNDARIO
        );

        chkMostrarPassword.setOpaque(
                false
        );

        chkMostrarPassword.setFocusPainted(
                false
        );

        chkMostrarPassword.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        //======================================================
        // BOTON LOGIN
        //======================================================
        btnIniciarSesion
                = new JButton(
                        "INICIAR SESIÓN"
                );

        btnIniciarSesion.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btnIniciarSesion.setForeground(
                Color.WHITE
        );

        btnIniciarSesion.setBackground(
                AZUL_PRINCIPAL
        );

        btnIniciarSesion.setFocusPainted(
                false
        );

        btnIniciarSesion.setBorderPainted(
                false
        );

        btnIniciarSesion.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnIniciarSesion.setPreferredSize(
                new Dimension(
                        390,
                        50
                )
        );

        btnIniciarSesion.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        //======================================================
        // MENSAJE
        //======================================================
        lblMensaje
                = new JLabel(" ");

        lblMensaje.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblMensaje.setForeground(
                ROJO
        );
    }

    //==========================================================
    // CONFIGURAR CAMPO
    //==========================================================
    private void configurarCampo(
            JTextField campo) {

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        campo.setForeground(
                TEXTO
        );

        campo.setBackground(
                Color.WHITE
        );

        campo.setCaretColor(
                AZUL_PRINCIPAL
        );

        campo.setPreferredSize(
                new Dimension(
                        390,
                        48
                )
        );

        campo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        campo.setBorder(
                new EmptyBorder(
                        0,
                        4,
                        0,
                        10
                )
        );
    }

    //==========================================================
    // CONSTRUIR INTERFAZ
    //==========================================================
    private void construirInterfaz() {

        setLayout(
                new BorderLayout()
        );

        add(
                crearPanelMarca(),
                BorderLayout.WEST
        );

        add(
                crearPanelLogin(),
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // PANEL IZQUIERDO
    //==========================================================
    private JPanel crearPanelMarca() {

        PanelFondoLogin panel
                = new PanelFondoLogin(
                        "/img/login_serena.png"
                );

        panel.setPreferredSize(
                new Dimension(
                        470,
                        680
                )
        );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c
                = new GridBagConstraints();

        c.gridx = 0;

        c.weightx = 1;

        c.anchor
                = GridBagConstraints.WEST;

        c.fill
                = GridBagConstraints.HORIZONTAL;

        //======================================================
        // TARJETA MARCA
        //======================================================
        JPanel tarjetaMarca
                = crearTarjetaMarca();

        c.gridy = 0;

        c.insets
                = new Insets(
                        30,
                        45,
                        12,
                        45
                );

        panel.add(
                tarjetaMarca,
                c
        );

        //======================================================
        // SEPARADOR
        //======================================================
        JSeparator separador
                = new JSeparator();

        separador.setForeground(
                new Color(
                        80,
                        160,
                        245
                )
        );

        c.gridy = 1;

        c.insets
                = new Insets(
                        8,
                        45,
                        20,
                        45
                );

        panel.add(
                separador,
                c
        );

        //======================================================
        // DESCRIPCION
        //======================================================
        JLabel descripcion
                = new JLabel(
                        "<html>"
                        + "Administrá tu negocio de manera<br>"
                        + "simple, rápida y segura."
                        + "</html>"
                );

        descripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        descripcion.setForeground(
                Color.WHITE
        );

        c.gridy = 2;

        c.insets
                = new Insets(
                        0,
                        45,
                        22,
                        45
                );

        panel.add(
                descripcion,
                c
        );

        //======================================================
        // MODULOS
        //======================================================
        JPanel panelModulos
                = new JPanel();

        panelModulos.setOpaque(
                false
        );

        panelModulos.setLayout(
                new BoxLayout(
                        panelModulos,
                        BoxLayout.Y_AXIS
                )
        );

        panelModulos.add(
                crearItemModulo(
                        "/img/logo2.png",
                        "Ventas y facturación"
                )
        );

        panelModulos.add(
                Box.createVerticalStrut(10)
        );

        panelModulos.add(
                crearItemModulo(
                        "/img/stock1.png",
                        "Control de stock"
                )
        );

        panelModulos.add(
                Box.createVerticalStrut(10)
        );

        panelModulos.add(
                crearItemModulo(
                        "/img/clientes5.png",
                        "Clientes y proveedores"
                )
        );

        panelModulos.add(
                Box.createVerticalStrut(10)
        );

        panelModulos.add(
                crearItemModulo(
                        "/img/caja1.png",
                        "Gestión de caja"
                )
        );

        panelModulos.add(
                Box.createVerticalStrut(10)
        );

        panelModulos.add(
                crearItemModulo(
                        "/img/inicio1.png",
                        "Reportes comerciales"
                )
        );

        c.gridy = 3;

        c.weighty = 1;

        c.anchor
                = GridBagConstraints.NORTHWEST;

        c.insets
                = new Insets(
                        0,
                        45,
                        10,
                        45
                );

        panel.add(
                panelModulos,
                c
        );

        //======================================================
        // FRASE INFERIOR
        //======================================================
        JPanel frase
                = crearFraseInferior();

        c.gridy = 4;

        c.weighty = 0;

        c.fill
                = GridBagConstraints.HORIZONTAL;

        c.anchor
                = GridBagConstraints.SOUTHWEST;

        c.insets
                = new Insets(
                        5,
                        45,
                        30,
                        45
                );

        panel.add(
                frase,
                c
        );

        return panel;
    }

    //==========================================================
    // TARJETA SUPERIOR SERENA SOFT
    //==========================================================
    private JPanel crearTarjetaMarca() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        22,
                        new Color(
                                5,
                                45,
                                105,
                                220
                        )
                );

        tarjeta.setLayout(
                new BorderLayout(
                        16,
                        0
                )
        );

        tarjeta.setBorder(
                new EmptyBorder(
                        14,
                        14,
                        14,
                        22
                )
        );

        tarjeta.setPreferredSize(
                new Dimension(
                        380,
                        100
                )
        );

        //======================================================
        // ICONO
        //======================================================
        JLabel icono
                = new JLabel();

        icono.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icono.setVerticalAlignment(
                SwingConstants.CENTER
        );

        icono.setPreferredSize(
                new Dimension(
                        70,
                        70
                )
        );

        icono.setOpaque(
                true
        );

        icono.setBackground(
                new Color(
                        25,
                        95,
                        210
                )
        );

        ImageIcon img
                = cargarIcono(
                        "/img/logo4.png",
                        42,
                        42
                );

        if (img != null) {

            icono.setIcon(
                    img
            );

        } else {

            icono.setText(
                    "S"
            );

            icono.setForeground(
                    Color.WHITE
            );

            icono.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            32
                    )
            );
        }

        //======================================================
        // TEXTOS
        //======================================================
        JPanel textos
                = new JPanel();

        textos.setOpaque(
                false
        );

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo
                = new JLabel(
                        "SERENA SOFT"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        titulo.setForeground(
                Color.WHITE
        );

        JLabel subtitulo
                = new JLabel(
                        "Gestión Comercial"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        subtitulo.setForeground(
                new Color(
                        185,
                        205,
                        235
                )
        );

        textos.add(
                Box.createVerticalGlue()
        );

        textos.add(
                titulo
        );

        textos.add(
                Box.createVerticalStrut(2)
        );

        textos.add(
                subtitulo
        );

        textos.add(
                Box.createVerticalGlue()
        );

        tarjeta.add(
                icono,
                BorderLayout.WEST
        );

        tarjeta.add(
                textos,
                BorderLayout.CENTER
        );

        return tarjeta;
    }

    //==========================================================
    // ITEM MODULO
    //==========================================================
    private JPanel crearItemModulo(
            String rutaIcono,
            String texto) {

        JPanel item
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        item.setOpaque(
                false
        );

        JLabel fondoIcono
                = new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        fondoIcono.setPreferredSize(
                new Dimension(
                        38,
                        38
                )
        );

        fondoIcono.setOpaque(
                true
        );

        fondoIcono.setBackground(
                new Color(
                        15,
                        80,
                        170
                )
        );

        ImageIcon icono
                = cargarIcono(
                        rutaIcono,
                        22,
                        22
                );

        if (icono != null) {

            fondoIcono.setIcon(
                    icono
            );
        }

        JLabel lblTexto
                = new JLabel(
                        texto
                );

        lblTexto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblTexto.setForeground(
                Color.WHITE
        );

        item.add(
                fondoIcono
        );

        item.add(
                Box.createHorizontalStrut(
                        12
                )
        );

        item.add(
                lblTexto
        );

        return item;
    }

    //==========================================================
    // FRASE INFERIOR
    //==========================================================
    private JPanel crearFraseInferior() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(
                false
        );

        panel.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        3,
                        0,
                        0,
                        new Color(
                                35,
                                160,
                                255
                        )
                )
        );

        JLabel texto
                = new JLabel(
                        "<html>"
                        + "<b>Todo lo que tu negocio necesita,</b><br>"
                        + "<font color='#62B5FF'>"
                        + "en un solo sistema."
                        + "</font>"
                        + "</html>"
                );

        texto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        texto.setForeground(
                Color.WHITE
        );

        texto.setBorder(
                new EmptyBorder(
                        3,
                        14,
                        3,
                        0
                )
        );

        panel.add(
                texto,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // PANEL LOGIN DERECHO
    //==========================================================
    private JPanel crearPanelLogin() {

        JPanel fondo
                = new JPanel(
                        new GridBagLayout()
                );

        fondo.setBackground(
                FONDO
        );

        fondo.add(
                crearTarjetaLogin()
        );

        return fondo;
    }

    //==========================================================
    // TARJETA LOGIN
    //==========================================================
    private JPanel crearTarjetaLogin() {

        JPanel tarjeta
                = new JPanel();

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta,
                        BoxLayout.Y_AXIS
                )
        );

        tarjeta.setBackground(
                Color.WHITE
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                42,
                                50,
                                30,
                                50
                        )
                )
        );

        tarjeta.setPreferredSize(
                new Dimension(
                        500,
                        545
                )
        );

        //======================================================
        // TITULO
        //======================================================
        JLabel titulo
                = new JLabel(
                        "Iniciar sesión"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        //======================================================
        // SUBTITULO
        //======================================================
        JLabel subtitulo
                = new JLabel(
                        "Ingresá tus credenciales para continuar"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        subtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        //======================================================
        // LABEL USUARIO
        //======================================================
        JLabel lblUsuario
                = crearLabelCampo(
                        "Usuario"
                );

        //======================================================
        // LABEL PASSWORD
        //======================================================
        JLabel lblPassword
                = crearLabelCampo(
                        "Contraseña"
                );

        //======================================================
        // PANEL USUARIO
        //======================================================
        JPanel panelUsuario
                = crearPanelCampo(
                        "/img/clientes5.png",
                        txtUsuario
                );

        //======================================================
        // PANEL PASSWORD
        //======================================================
        JPanel panelPassword
                = crearPanelCampoPassword(
                        txtPassword
                );

        panelUsuario.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panelPassword.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        chkMostrarPassword.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        lblMensaje.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        btnIniciarSesion.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        //======================================================
        // OLVIDASTE PASSWORD
        //======================================================
        JButton btnOlvidePassword
                = new JButton(
                        "¿Olvidaste tu contraseña?"
                );

        btnOlvidePassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        btnOlvidePassword.setForeground(
                AZUL_PRINCIPAL
        );

        btnOlvidePassword.setContentAreaFilled(
                false
        );

        btnOlvidePassword.setBorderPainted(
                false
        );

        btnOlvidePassword.setFocusPainted(
                false
        );

        btnOlvidePassword.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnOlvidePassword.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnOlvidePassword.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "La recuperación de contraseña "
                    + "se configurará cuando conectemos "
                    + "los usuarios con MySQL.",
                    "Recuperar Contraseña",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        //======================================================
        // PIE
        //======================================================
        JLabel pie
                = new JLabel(
                        "SERENA SOFT  •  Sistema de Gestión Comercial"
                );

        pie.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        pie.setForeground(
                new Color(
                        145,
                        150,
                        160
                )
        );

        pie.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //======================================================
        // AGREGAR COMPONENTES
        //======================================================
        tarjeta.add(
                titulo
        );

        tarjeta.add(
                Box.createVerticalStrut(6)
        );

        tarjeta.add(
                subtitulo
        );

        tarjeta.add(
                Box.createVerticalStrut(30)
        );

        tarjeta.add(
                lblUsuario
        );

        tarjeta.add(
                Box.createVerticalStrut(7)
        );

        tarjeta.add(
                panelUsuario
        );

        tarjeta.add(
                Box.createVerticalStrut(18)
        );

        tarjeta.add(
                lblPassword
        );

        tarjeta.add(
                Box.createVerticalStrut(7)
        );

        tarjeta.add(
                panelPassword
        );

        tarjeta.add(
                Box.createVerticalStrut(10)
        );

        tarjeta.add(
                chkMostrarPassword
        );

        tarjeta.add(
                Box.createVerticalStrut(8)
        );

        tarjeta.add(
                lblMensaje
        );

        tarjeta.add(
                Box.createVerticalStrut(10)
        );

        tarjeta.add(
                btnIniciarSesion
        );

        tarjeta.add(
                Box.createVerticalStrut(18)
        );

        tarjeta.add(
                crearSeparadorConTexto()
        );

        tarjeta.add(
                Box.createVerticalStrut(8)
        );

        tarjeta.add(
                btnOlvidePassword
        );

        tarjeta.add(
                Box.createVerticalGlue()
        );

        tarjeta.add(
                pie
        );

        return tarjeta;
    }

    //==========================================================
    // CAMPO USUARIO
    //==========================================================
    private JPanel crearPanelCampo(
            String rutaIcono,
            JTextField campo) {

        JPanel panel
                = crearContenedorCampo();

        JLabel icono
                = new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        icono.setPreferredSize(
                new Dimension(
                        45,
                        48
                )
        );

        ImageIcon img
                = cargarIcono(
                        rutaIcono,
                        18,
                        18
                );

        if (img != null) {

            icono.setIcon(
                    img
            );
        }

        panel.add(
                icono,
                BorderLayout.WEST
        );

        panel.add(
                campo,
                BorderLayout.CENTER
        );

        campo.putClientProperty(
                "panelContenedor",
                panel
        );

        return panel;
    }

    //==========================================================
    // CAMPO PASSWORD
    //==========================================================
    private JPanel crearPanelCampoPassword(
            JPasswordField campo) {

        JPanel panel
                = crearContenedorCampo();

        JLabel icono
                = new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        icono.setPreferredSize(
                new Dimension(
                        45,
                        48
                )
        );

        ImageIcon imgCandado
                = cargarIcono(
                        "/img/candado1.png",
                        18,
                        18
                );

        if (imgCandado != null) {

            icono.setIcon(
                    imgCandado
            );

        } else {

            icono.setText(
                    "●"
            );

            icono.setForeground(
                    AZUL_OSCURO
            );
        }
        panel.add(
                icono,
                BorderLayout.WEST
        );

        panel.add(
                campo,
                BorderLayout.CENTER
        );

        campo.putClientProperty(
                "panelContenedor",
                panel
        );

        return panel;
    }

    //==========================================================
    // CONTENEDOR CAMPO
    //==========================================================
    private JPanel crearContenedorCampo() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setPreferredSize(
                new Dimension(
                        390,
                        50
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        return panel;
    }

    //==========================================================
    // LABEL CAMPO
    //==========================================================
    private JLabel crearLabelCampo(
            String texto) {

        JLabel label
                = new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                TEXTO
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    //==========================================================
    // SEPARADOR
    //==========================================================
    private JPanel crearSeparadorConTexto() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(
                false
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        25
                )
        );

        GridBagConstraints c
                = new GridBagConstraints();

        c.gridy = 0;

        c.fill
                = GridBagConstraints.HORIZONTAL;

        JSeparator izquierda
                = new JSeparator();

        JSeparator derecha
                = new JSeparator();

        JLabel centro
                = new JLabel(
                        "o"
                );

        centro.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        centro.setForeground(
                TEXTO_SECUNDARIO
        );

        c.gridx = 0;

        c.weightx = 1;

        panel.add(
                izquierda,
                c
        );

        c.gridx = 1;

        c.weightx = 0;

        c.insets
                = new Insets(
                        0,
                        12,
                        0,
                        12
                );

        panel.add(
                centro,
                c
        );

        c.gridx = 2;

        c.weightx = 1;

        c.insets
                = new Insets(
                        0,
                        0,
                        0,
                        0
                );

        panel.add(
                derecha,
                c
        );

        return panel;
    }

    //==========================================================
    // CARGAR ICONO
    //==========================================================
    private ImageIcon cargarIcono(
            String ruta,
            int ancho,
            int alto) {

        try {

            URL url
                    = getClass()
                            .getResource(
                                    ruta
                            );

            if (url == null) {

                return null;
            }

            ImageIcon original
                    = new ImageIcon(
                            url
                    );

            Image imagen
                    = original
                            .getImage()
                            .getScaledInstance(
                                    ancho,
                                    alto,
                                    Image.SCALE_SMOOTH
                            );

            return new ImageIcon(
                    imagen
            );

        } catch (Exception e) {

            return null;
        }
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // MOSTRAR PASSWORD
        //======================================================
        chkMostrarPassword.addActionListener(e -> {

            if (chkMostrarPassword.isSelected()) {

                txtPassword.setEchoChar(
                        (char) 0
                );

            } else {

                txtPassword.setEchoChar(
                        echoOriginal
                );
            }
        });

        //======================================================
        // HOVER LOGIN
        //======================================================
        btnIniciarSesion.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                btnIniciarSesion.setBackground(
                        AZUL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                btnIniciarSesion.setBackground(
                        AZUL_PRINCIPAL
                );
            }
        });

        //======================================================
        // FOCUS USUARIO
        //======================================================
        txtUsuario.addFocusListener(
                new FocusAdapter() {

            @Override
            public void focusGained(
                    FocusEvent e) {

                cambiarBordeContenedor(
                        txtUsuario,
                        true
                );
            }

            @Override
            public void focusLost(
                    FocusEvent e) {

                cambiarBordeContenedor(
                        txtUsuario,
                        false
                );
            }
        });

        //======================================================
        // FOCUS PASSWORD
        //======================================================
        txtPassword.addFocusListener(
                new FocusAdapter() {

            @Override
            public void focusGained(
                    FocusEvent e) {

                cambiarBordeContenedor(
                        txtPassword,
                        true
                );
            }

            @Override
            public void focusLost(
                    FocusEvent e) {

                cambiarBordeContenedor(
                        txtPassword,
                        false
                );
            }
        });

        //======================================================
        // BOTON LOGIN
        //======================================================
        btnIniciarSesion.addActionListener(e -> {

            iniciarSesion();
        });

        //======================================================
        // ENTER USUARIO
        //======================================================
        txtUsuario.addActionListener(e -> {

            txtPassword.requestFocus();
        });

        //======================================================
        // ENTER PASSWORD
        //======================================================
        txtPassword.addActionListener(e -> {

            iniciarSesion();
        });
    }

    //==========================================================
    // CAMBIAR BORDE
    //==========================================================
    private void cambiarBordeContenedor(
            JTextField campo,
            boolean focus) {

        Object objeto
                = campo.getClientProperty(
                        "panelContenedor"
                );

        if (objeto instanceof JPanel) {

            JPanel panel
                    = (JPanel) objeto;

            if (focus) {

                panel.setBorder(
                        BorderFactory.createLineBorder(
                                AZUL_PRINCIPAL,
                                2
                        )
                );

            } else {

                panel.setBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        )
                );
            }
        }
    }

    //==========================================================
    // LOGIN
    //==========================================================
    

    //==========================================================
    // ERROR
    //==========================================================
    private void mostrarError(
            String mensaje) {

        lblMensaje.setForeground(
                ROJO
        );

        lblMensaje.setText(
                mensaje
        );
    }

    //==========================================================
    // PANEL REDONDEADO
    //==========================================================
    private class PanelRedondeado
            extends JPanel {

        private final int radio;
        private final Color color;

        public PanelRedondeado(
                int radio,
                Color color) {

            this.radio
                    = radio;

            this.color
                    = color;

            setOpaque(
                    false
            );
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            Graphics2D g2
                    = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    color
            );

            g2.fill(
                    new RoundRectangle2D.Double(
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            radio,
                            radio
                    )
            );

            g2.dispose();

            super.paintComponent(
                    g
            );
        }
    }

    //==========================================================
    // PANEL DE FONDO
    //==========================================================
    private class PanelFondoLogin
            extends JPanel {

        private Image imagen;

        public PanelFondoLogin(
                String rutaImagen) {

            setOpaque(
                    true
            );

            URL recurso
                    = getClass()
                            .getResource(
                                    rutaImagen
                            );

            if (recurso != null) {

                imagen
                        = new ImageIcon(
                                recurso
                        ).getImage();
            }
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(
                    g
            );

            Graphics2D g2
                    = (Graphics2D) g.create();

            int ancho
                    = getWidth();

            int alto
                    = getHeight();

            //==================================================
            // IMAGEN
            //==================================================
            if (imagen != null) {

                int anchoImagen
                        = imagen.getWidth(
                                this
                        );

                int altoImagen
                        = imagen.getHeight(
                                this
                        );

                double escala
                        = Math.max(
                                (double) ancho
                                / anchoImagen,
                                (double) alto
                                / altoImagen
                        );

                int nuevoAncho
                        = (int) (anchoImagen
                        * escala);

                int nuevoAlto
                        = (int) (altoImagen
                        * escala);

                int x
                        = (ancho
                        - nuevoAncho) / 2;

                int y
                        = (alto
                        - nuevoAlto) / 2;

                g2.drawImage(
                        imagen,
                        x,
                        y,
                        nuevoAncho,
                        nuevoAlto,
                        this
                );

            } else {

                g2.setColor(
                        AZUL_OSCURO
                );

                g2.fillRect(
                        0,
                        0,
                        ancho,
                        alto
                );
            }

            //==================================================
            // OVERLAY AZUL
            //
            // 0.48 deja ver bastante más la imagen
            //==================================================
            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            0.48f
                    )
            );

            g2.setColor(
                    new Color(
                            6,
                            38,
                            90
                    )
            );

            g2.fillRect(
                    0,
                    0,
                    ancho,
                    alto
            );

            //==================================================
            // GRADIENTE
            //==================================================
            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            0.15f
                    )
            );

            GradientPaint gradient
                    = new GradientPaint(
                            0,
                            0,
                            new Color(
                                    15,
                                    65,
                                    150
                            ),
                            0,
                            alto,
                            new Color(
                                    3,
                                    20,
                                    55
                            )
                    );

            g2.setPaint(
                    gradient
            );

            g2.fillRect(
                    0,
                    0,
                    ancho,
                    alto
            );

            g2.dispose();
        }
    }

    //==========================================================
    // MAIN
    //==========================================================
    public static void main(
            String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager
                            .getSystemLookAndFeelClassName()
            );

        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException e) {

            System.out.println(
                    "No se pudo cargar el LookAndFeel."
            );
        }

        SwingUtilities.invokeLater(() -> {

            new Login().setVisible(
                    true
            );
        });
    }
}
