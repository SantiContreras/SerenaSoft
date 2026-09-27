package Dialogos;

import Diseños.EstiloBotones;
import Paneles.PanelConfiguracion;
import Paneles.PanelHistorialVentas;
import Paneles.PanelMiPerfil;
import Principal.PanelPrincipal;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Menú desplegable del usuario.
 *
 * Permite acceder rápidamente a: - Mi Perfil - Configuración - Usuarios -
 * Cambiar Contraseña - Copias de Seguridad - Acerca del Sistema - Cerrar Sesión
 *
 * @author Santiago
 */
public class MenuUsuarioPopup extends JPopupMenu {

    //====================================================
    // COLORES
    //====================================================
    private static final Color COLOR_FONDO = new Color(244, 246, 250);
    private static final Color COLOR_TEXTO = new Color(25, 55, 110);
    private static final Color COLOR_HOVER = new Color(25, 70, 145);
    private static final Color COLOR_SEPARADOR = new Color(220, 225, 235);
    private static final Color COLOR_BORDE = new Color(200, 210, 225);

    //====================================================
    // DIMENSIONES
    //====================================================
    private static final int ANCHO_MENU = 280;
    private static final int ALTO_BOTON = 46;
    private static final int TAMANO_ICONO = 24;

    //====================================================
    // BOTONES
    //====================================================
    private JButton btnPerfil;
    private JButton btnConfiguracion;
    private JButton btnUsuarios;
    private JButton btnCambiarClave;
    private JButton btnBackup;
    private JButton btnAcerca;
    private JButton btnCerrarSesion;
    private JButton btnHistorialVentas;

    //====================================================
    // REFERENCIA AL PANEL PRINCIPAL
    //====================================================
    private final PanelPrincipal panelPrincipal;

    //====================================================
    // CONSTRUCTOR
    //====================================================
    public MenuUsuarioPopup(PanelPrincipal panelPrincipal) {

        this.panelPrincipal = panelPrincipal;

        inicializarComponentes();
        construirMenu();
        configurarEventos();
  
    }

    //====================================================
    // INICIALIZAR COMPONENTES
    //====================================================
    private void inicializarComponentes() {

        btnPerfil = crearBoton(
                "Mi Perfil",
                "clientes4.png"
        );

        btnConfiguracion = crearBoton(
                "Configuración",
                "config1.png"
        );

        btnUsuarios = crearBoton(
                "Usuarios",
                "employees1.png"
        );

        btnCambiarClave = crearBoton(
                "Cambiar Contraseña",
                "editar6.png"
        );

        btnHistorialVentas = crearBoton(
                "Historial de Ventas",
                "sales2.png"
        );

        btnAcerca = crearBoton(
                "Acerca del Sistema",
                "manual2.png"
        );

        btnCerrarSesion = crearBoton(
                "Cerrar Sesión",
                "eliminar3.png"
        );
    }

    //====================================================
    // CONSTRUIR MENU
    //====================================================
    private void construirMenu() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDE
                        ),
                        new EmptyBorder(
                                5,
                                5,
                                5,
                                5
                        )
                )
        );

        setBackground(COLOR_FONDO);

        //================================================
        // MI PERFIL
        //================================================
        add(btnPerfil);

        agregarSeparador();

        //================================================
        // CONFIGURACIÓN
        //================================================
        add(btnConfiguracion);

        agregarSeparador();

        //================================================
        // USUARIOS
        //================================================
        add(btnUsuarios);

        agregarSeparador();

        //================================================
        // CAMBIAR CONTRASEÑA
        //================================================
        add(btnCambiarClave);

        agregarSeparador();

        //================================================
// HISTORIAL DE VENTAS
//================================================
        add(btnHistorialVentas);

        agregarSeparador();

        //================================================
        // ACERCA
        //================================================
        add(btnAcerca);

        agregarSeparador();

        //================================================
        // CERRAR SESIÓN
        //================================================
        add(btnCerrarSesion);

        //================================================
        // TAMAÑO DEL POPUP
        //================================================
        setPreferredSize(
                new Dimension(
                        ANCHO_MENU,
                        calcularAltoMenu()
                )
        );
    }

    //====================================================
    // CREAR SEPARADOR
    //====================================================
    private void agregarSeparador() {

        JSeparator separador = new JSeparator();

        separador.setForeground(COLOR_SEPARADOR);

        separador.setBackground(COLOR_SEPARADOR);

        separador.setMaximumSize(
                new Dimension(
                        ANCHO_MENU - 10,
                        1
                )
        );

        add(separador);
    }

    //====================================================
    // CREAR BOTÓN
    //====================================================
    private JButton crearBoton(
            String texto,
            String nombreIcono
    ) {

        JButton boton = new JButton(texto);

        //................................................
        // DIMENSIONES
        //................................................
        Dimension dimension = new Dimension(
                ANCHO_MENU - 10,
                ALTO_BOTON
        );

        boton.setPreferredSize(dimension);
        boton.setMinimumSize(dimension);
        boton.setMaximumSize(dimension);

        //................................................
        // ALINEACIÓN
        //................................................
        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setVerticalAlignment(
                SwingConstants.CENTER
        );

        //................................................
        // ESTILO
        //................................................
        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setContentAreaFilled(false);

        boton.setOpaque(true);

        boton.setBackground(COLOR_FONDO);

        boton.setForeground(COLOR_TEXTO);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        boton.setBorder(
                new EmptyBorder(
                        0,
                        15,
                        0,
                        10
                )
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        //................................................
        // ICONO
        //................................................
        ImageIcon icono = cargarIcono(
                nombreIcono
        );

        if (icono != null) {

            boton.setIcon(icono);

            boton.setIconTextGap(12);
        }

        //................................................
        // EFECTO HOVER
        //................................................
        boton.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent e
            ) {

                boton.setBackground(
                        COLOR_HOVER
                );

                boton.setForeground(
                        Color.WHITE
                );
            }

            @Override
            public void mouseExited(
                    MouseEvent e
            ) {

                boton.setBackground(
                        COLOR_FONDO
                );

                boton.setForeground(
                        COLOR_TEXTO
                );
            }
        }
        );

        return boton;
    }

    //====================================================
    // CARGAR ICONO
    //====================================================
    private ImageIcon cargarIcono(
            String nombre
    ) {

        java.net.URL url
                = getClass().getResource(
                        "/img/" + nombre
                );

        if (url == null) {

            System.out.println(
                    "No se encontró el icono: /img/"
                    + nombre
            );

            return null;
        }

        ImageIcon iconoOriginal
                = new ImageIcon(url);

        Image imagen
                = iconoOriginal.getImage();

        Image imagenEscalada
                = imagen.getScaledInstance(
                        TAMANO_ICONO,
                        TAMANO_ICONO,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(
                imagenEscalada
        );
    }

    //====================================================
    // ALTO DEL MENU
    //====================================================
    private int calcularAltoMenu() {

        /*
         * 7 botones
         * 6 separadores
         * márgenes internos
         */
        int botones
                = 7 * ALTO_BOTON;

        int separadores
                = 6 * 1;

        int margenes
                = 10;

        return botones
                + separadores
                + margenes;
    }

    //====================================================
    // EVENTOS
    //====================================================
    private void configurarEventos() {

        //================================================
        // MI PERFIL
        //================================================
        btnPerfil.addActionListener(e -> {

            setVisible(false);

            abrirMiPerfil();
        });

        //================================================
        // CONFIGURACIÓN
        //================================================
        btnConfiguracion.addActionListener(e -> {

            // Cerramos el menú desplegable
            setVisible(false);

            // Mostramos el panel Configuración
            panelPrincipal.mostrarPanel(
                    new PanelConfiguracion()
            );

        });

        //================================================
        // USUARIOS
        //================================================
        btnUsuarios.addActionListener(e -> {

            setVisible(false);

            abrirUsuarios();
        });

        //================================================
        // CAMBIAR CONTRASEÑA
        //================================================
        btnCambiarClave.addActionListener(e -> {

            setVisible(false);

            abrirCambiarClave();
        });

      

        //================================================
        // ACERCA DEL SISTEMA
        //================================================
        btnAcerca.addActionListener(e -> {

            setVisible(false);

            mostrarAcerca();
        });

        //================================================
        // CERRAR SESIÓN
        //================================================
        btnCerrarSesion.addActionListener(e -> {

            cerrarSesion();
        });

        //================================================
// HISTORIAL DE VENTAS
//================================================
        btnHistorialVentas.addActionListener(e -> {

            // Cerramos el menú desplegable
            setVisible(false);

            // Abrimos el historial dentro de PanelPrincipal
            panelPrincipal.mostrarPanel(
                    new PanelHistorialVentas()
            );
        });
    }

    //====================================================
    // MI PERFIL
    //====================================================
    private void abrirMiPerfil() {

        PanelMiPerfil panel
                = new PanelMiPerfil(panelPrincipal);

        mostrarPanel(panel);
    }

    //====================================================
    // CONFIGURACIÓN
    //====================================================
    private void abrirConfiguracion() {

        mostrarMensaje(
                "El módulo de Configuración todavía no está disponible.",
                "Configuración",
                JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Cuando tengas PanelConfiguracion:
         *
         * PanelConfiguracion panel =
         *         new PanelConfiguracion();
         *
         * mostrarPanel(panel);
         */
    }

    //====================================================
    // USUARIOS
    //====================================================
    private void abrirUsuarios() {

        mostrarMensaje(
                "El módulo de Administración de Usuarios todavía no está disponible.",
                "Usuarios",
                JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Cuando tengas PanelUsuarios:
         *
         * PanelUsuarios panel =
         *         new PanelUsuarios();
         *
         * mostrarPanel(panel);
         */
    }

    //====================================================
    // CAMBIAR CONTRASEÑA
    //====================================================
    private void abrirCambiarClave() {

        mostrarMensaje(
                "El módulo de cambio de contraseña todavía no está disponible.",
                "Seguridad",
                JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Cuando tengas el diálogo:
         *
         * DialogCambiarClave dialog =
         *         new DialogCambiarClave(...);
         *
         * dialog.setVisible(true);
         */
    }

    //====================================================
    // BACKUP
    //====================================================
    private void abrirBackup() {

        mostrarMensaje(
                "El módulo de Copias de Seguridad todavía no está disponible.",
                "Copias de Seguridad",
                JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Cuando tengas PanelBackup:
         *
         * PanelBackup panel =
         *         new PanelBackup();
         *
         * mostrarPanel(panel);
         */
    }

    //====================================================
    // ACERCA DEL SISTEMA
    //====================================================
    private void mostrarAcerca() {

        JOptionPane.showMessageDialog(
                panelPrincipal,
                """
                Serena Soft
                
                Sistema de Gestión Comercial
                
                Versión 1.0
                
                © 2026 Serena Soft
                """,
                "Acerca del Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //====================================================
    // CERRAR SESIÓN
    //====================================================
    private void cerrarSesion() {

        int opcion
                = JOptionPane.showConfirmDialog(
                        panelPrincipal,
                        "¿Está seguro de que desea cerrar la sesión?",
                        "Cerrar Sesión",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion != JOptionPane.YES_OPTION) {

            return;
        }

        setVisible(false);

        /*
         * Acá debería ir la lógica real de logout.
         *
         * Por ejemplo:
         *
         * panelPrincipal.cerrarSesion();
         *
         * o:
         *
         * Login login = new Login();
         * login.setVisible(true);
         *
         * panelPrincipal.dispose();
         */
    }

    //====================================================
    // MOSTRAR PANEL
    //====================================================
    private void mostrarPanel(
            JPanel panel
    ) {

        if (panelPrincipal == null) {

            mostrarMensaje(
                    "No se encontró el PanelPrincipal.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        /*
         * IMPORTANTE:
         *
         * Si tu PanelPrincipal ya tiene un método
         * específico para cambiar de panel,
         * utilizá ese método acá.
         *
         * Ejemplo:
         *
         * panelPrincipal.mostrarPanel(panel);
         *
         * Si el método se llama diferente,
         * solamente hay que cambiar esta parte.
         */
        try {

            java.lang.reflect.Method metodo
                    = panelPrincipal.getClass()
                            .getMethod(
                                    "mostrarPanel",
                                    JPanel.class
                            );

            metodo.invoke(
                    panelPrincipal,
                    panel
            );

        } catch (Exception ex) {

            /*
             * Si todavía no existe mostrarPanel(),
             * avisamos para no romper el programa.
             */
            mostrarMensajeError(
                    "PanelPrincipal todavía no tiene configurado "
                    + "el método mostrarPanel(JPanel).\n\n"
                    + "El módulo fue creado correctamente, "
                    + "pero falta conectar la navegación.",
                    ex
            );
        }
    }

    //====================================================
    // MOSTRAR MENSAJE
    //====================================================
    private void mostrarMensaje(
            String mensaje,
            String titulo,
            int tipo
    ) {

        JOptionPane.showMessageDialog(
                panelPrincipal,
                mensaje,
                titulo,
                tipo
        );
    }

    //====================================================
    // MOSTRAR ERROR
    //====================================================
    private void mostrarMensajeError(
            String mensaje,
            Exception ex
    ) {

        System.err.println(
                "Error en MenuUsuarioPopup:"
        );

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                panelPrincipal,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
