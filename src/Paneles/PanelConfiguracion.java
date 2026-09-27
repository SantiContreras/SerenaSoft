package Paneles;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import Paneles.PanelConfiguracionEmpresa;
import Paneles.PanelConfigProductos;
import Paneles.PanelConfigUsuarios;
import Paneles.PanelConfigComprobantes;
import Paneles.PanelConfigMediosPago;


public class PanelConfiguracion extends JPanel {

    //==========================================================
    // PANELES
    //==========================================================
    private JPanel panelMenu;
    private JPanel panelContenido;
    private JPanel panelHeader;

    //==========================================================
    // CARD LAYOUT
    //==========================================================
    private CardLayout cardLayout;

    //==========================================================
    // BOTONES MENU
    //==========================================================
    private JButton btnEmpresa;
    private JButton btnUsuarios;
    private JButton btnSeguridad;
    private JButton btnComprobantes;
    private JButton btnMediosPago;
    private JButton btnProductos;
    private JButton btnImpresion;
    private JButton btnBackup;
    private JButton btnApariencia;
    private JButton btnSistema;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO_GENERAL
            = new Color(245, 247, 250);

    private final Color FONDO_MENU
            = new Color(238, 242, 248);

    private final Color AZUL_PRINCIPAL
            = new Color(25, 70, 145);

    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color AZUL_HOVER
            = new Color(220, 232, 250);

    private final Color TEXTO
            = new Color(50, 60, 75);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfiguracion() {

        inicializarComponentes();

        construirLayout();

        registrarPaneles();

        configurarEventos();
        
         EstiloBotones.corregirBotones(this);

        mostrarPanel("EMPRESA");

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        panelMenu = new JPanel();

        panelContenido = new JPanel();

        panelHeader = new JPanel();

        cardLayout = new CardLayout();

        btnEmpresa
                = crearBotonMenu("Empresa");

        btnUsuarios
                = crearBotonMenu("Usuarios");

        btnSeguridad
                = crearBotonMenu("Seguridad");

        btnComprobantes
                = crearBotonMenu("Comprobantes");

        btnMediosPago
                = crearBotonMenu("Medios de Pago");

        btnProductos
                = crearBotonMenu("Productos");

        btnImpresion
                = crearBotonMenu("Impresión");

        btnBackup
                = crearBotonMenu("Copias de Seguridad");

        btnApariencia
                = crearBotonMenu("Apariencia");

        btnSistema
                = crearBotonMenu("Sistema");

    }

    //==========================================================
    // CONSTRUIR LAYOUT
    //==========================================================
    private void construirLayout() {

        removeAll();

        setLayout(new BorderLayout());

        setBackground(FONDO_GENERAL);

        //------------------------------------------------------
        // HEADER
        //------------------------------------------------------
        panelHeader.setLayout(
                new BoxLayout(
                        panelHeader,
                        BoxLayout.Y_AXIS
                )
        );

        panelHeader.setBackground(Color.WHITE);

        panelHeader.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        18,
                        25
                )
        );

        JLabel lblTitulo
                = new JLabel("CONFIGURACIÓN");

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        lblTitulo.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JLabel lblSubtitulo
                = new JLabel(
                        "Administración y parámetros generales del sistema"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblSubtitulo.setForeground(
                new Color(105, 115, 130)
        );

        lblSubtitulo.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelHeader.add(lblTitulo);

        panelHeader.add(
                Box.createVerticalStrut(4)
        );

        panelHeader.add(lblSubtitulo);

        add(
                panelHeader,
                BorderLayout.NORTH
        );

        //------------------------------------------------------
        // MENU LATERAL
        //------------------------------------------------------
        panelMenu.setLayout(
                new BoxLayout(
                        panelMenu,
                        BoxLayout.Y_AXIS
                )
        );

        panelMenu.setBackground(
                FONDO_MENU
        );

        panelMenu.setPreferredSize(
                new Dimension(230, 0)
        );

        panelMenu.setBorder(
                new EmptyBorder(
                        18,
                        12,
                        18,
                        12
                )
        );

        agregarBotonMenu(btnEmpresa);

        agregarBotonMenu(btnUsuarios);

        agregarBotonMenu(btnSeguridad);

        agregarBotonMenu(btnComprobantes);

        agregarBotonMenu(btnMediosPago);

        agregarBotonMenu(btnProductos);

        agregarBotonMenu(btnImpresion);

        agregarBotonMenu(btnBackup);

        agregarBotonMenu(btnApariencia);

        agregarBotonMenu(btnSistema);

        add(
                panelMenu,
                BorderLayout.WEST
        );

        //------------------------------------------------------
        // CONTENIDO
        //------------------------------------------------------
        panelContenido.setLayout(
                cardLayout
        );

        panelContenido.setBackground(
                FONDO_GENERAL
        );

        panelContenido.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        add(
                panelContenido,
                BorderLayout.CENTER
        );

    }

    //==========================================================
    // REGISTRAR PANELES
    //==========================================================
    private void registrarPaneles() {

        panelContenido.add(
                new PanelConfiguracionEmpresa(),
                "EMPRESA"
        );

        panelContenido.add(
                new PanelConfigUsuarios(),
                "USUARIOS"
        );

        panelContenido.add(
        new PanelConfigSeguridad(),
        "SEGURIDAD"
);

        panelContenido.add(
                new PanelConfigComprobantes(),
                "COMPROBANTES"
        );

        panelContenido.add(
                new PanelConfigMediosPago(),
                "MEDIOS_PAGO"
        );

        panelContenido.add(
                new PanelConfigProductos(),
                "PRODUCTOS"
        );

        panelContenido.add(
                new PanelConfigImpresion(),
                "IMPRESION"
        );

        panelContenido.add(
                new PanelConfigBackup(),
                "BACKUP"
        );

        panelContenido.add(
                new PanelConfigApariencia(),
                "APARIENCIA"
        );

        panelContenido.add(
                new PanelConfigSistema(),
                "SISTEMA"
        );

    }

    //==========================================================
    // PANEL TEMPORAL
    //==========================================================
    private JPanel crearPanelTemporal(
            String titulo,
            String descripcion) {

        JPanel panel
                = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 235)
                        ),
                        new EmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
                )
        );

        JLabel lblTitulo
                = new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        lblTitulo.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JLabel lblDescripcion
                = new JLabel(descripcion);

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblDescripcion.setForeground(
                new Color(100, 105, 115)
        );

        lblDescripcion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panel.add(lblTitulo);

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(lblDescripcion);

        panel.add(
                Box.createVerticalGlue()
        );

        return panel;

    }

    //==========================================================
    // CREAR BOTON MENU
    //==========================================================
    private JButton crearBotonMenu(
            String texto) {

        JButton boton
                = new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        205,
                        44
                )
        );

        boton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        boton.setMinimumSize(
                new Dimension(
                        205,
                        44
                )
        );

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        boton.setForeground(
                TEXTO
        );

        boton.setBackground(
                FONDO_MENU
        );

        boton.setBorder(
                new EmptyBorder(
                        0,
                        15,
                        0,
                        10
                )
        );

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setContentAreaFilled(false);

        boton.setOpaque(true);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                if (!boton.getBackground()
                        .equals(AZUL_PRINCIPAL)) {

                    boton.setBackground(
                            AZUL_HOVER
                    );

                }

            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                if (!boton.getBackground()
                        .equals(AZUL_PRINCIPAL)) {

                    boton.setBackground(
                            FONDO_MENU
                    );

                }

            }

        });

        return boton;

    }

    //==========================================================
    // AGREGAR BOTON MENU
    //==========================================================
    private void agregarBotonMenu(
            JButton boton) {

        panelMenu.add(boton);

        panelMenu.add(
                Box.createVerticalStrut(5)
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnEmpresa.addActionListener(
                e -> mostrarPanel("EMPRESA")
        );

        btnUsuarios.addActionListener(
                e -> mostrarPanel("USUARIOS")
        );

        btnSeguridad.addActionListener(
                e -> mostrarPanel("SEGURIDAD")
        );

        btnComprobantes.addActionListener(
                e -> mostrarPanel("COMPROBANTES")
        );

        btnMediosPago.addActionListener(
                e -> mostrarPanel("MEDIOS_PAGO")
        );
        btnProductos.addActionListener(
                e -> mostrarPanel("PRODUCTOS")
        );

        btnImpresion.addActionListener(
                e -> mostrarPanel("IMPRESION")
        );

        btnBackup.addActionListener(
                e -> mostrarPanel("BACKUP")
        );

        btnApariencia.addActionListener(
                e -> mostrarPanel("APARIENCIA")
        );

        btnSistema.addActionListener(
                e -> mostrarPanel("SISTEMA")
        );

    }

    //==========================================================
    // MOSTRAR PANEL
    //==========================================================
    private void mostrarPanel(
            String nombrePanel) {

        cardLayout.show(
                panelContenido,
                nombrePanel
        );

        limpiarSeleccion();

        switch (nombrePanel) {

            case "EMPRESA":
                marcarSeleccionado(
                        btnEmpresa
                );
                break;

            case "USUARIOS":
                marcarSeleccionado(
                        btnUsuarios
                );
                break;

            case "SEGURIDAD":
                marcarSeleccionado(
                        btnSeguridad
                );
                break;

            case "COMPROBANTES":
                marcarSeleccionado(
                        btnComprobantes
                );
                break;

            case "MEDIOS_PAGO":
                marcarSeleccionado(
                        btnMediosPago
                );
                break;

            case "PRODUCTOS":
                marcarSeleccionado(
                        btnProductos
                );
                break;

            case "IMPRESION":
                marcarSeleccionado(
                        btnImpresion
                );
                break;

            case "BACKUP":
                marcarSeleccionado(
                        btnBackup
                );
                break;

            case "APARIENCIA":
                marcarSeleccionado(
                        btnApariencia
                );
                break;

            case "SISTEMA":
                marcarSeleccionado(
                        btnSistema
                );
                break;

        }

    }

    //==========================================================
    // LIMPIAR SELECCION
    //==========================================================
    private void limpiarSeleccion() {

        JButton[] botones = {
            btnEmpresa,
            btnUsuarios,
            btnSeguridad,
            btnComprobantes,
            btnMediosPago,
            btnProductos,
            btnImpresion,
            btnBackup,
            btnApariencia,
            btnSistema

        };

        for (JButton boton : botones) {

            boton.setBackground(
                    FONDO_MENU
            );

            boton.setForeground(
                    TEXTO
            );

        }

    }

    //==========================================================
    // MARCAR SELECCIONADO
    //==========================================================
    private void marcarSeleccionado(
            JButton boton) {

        boton.setBackground(
                AZUL_PRINCIPAL
        );

        boton.setForeground(
                Color.WHITE
        );

    }

}
