package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Point;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableCellRenderer;

public class PanelConfigUsuarios extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtBuscar;

    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnBloquear;
    private JButton btnRestablecerClave;
    private JButton btnGestionarRoles;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cboRol;

    private JLabel lblTotalUsuarios;
    private JLabel lblUsuariosActivos;
    private JLabel lblUsuariosBloqueados;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO = new Color(245, 247, 250);
    private final Color BLANCO = Color.WHITE;

    private final Color AZUL = new Color(25, 70, 145);
    private final Color AZUL_OSCURO = new Color(15, 50, 110);

    private final Color VERDE = new Color(25, 135, 84);
    private final Color ROJO = new Color(200, 55, 55);
    private final Color GRIS = new Color(110, 120, 135);

    private final Color BORDE = new Color(215, 222, 232);
    private final Color TEXTO_SECUNDARIO = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigUsuarios() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        txtBuscar = new JTextField();

        btnBuscar = crearBoton(
                "Buscar",
                AZUL,
                100
        );

        btnNuevo = crearBoton(
                "Nuevo Usuario",
                VERDE,
                140
        );

        btnEditar = crearBoton(
                "Editar",
                AZUL,
                110
        );

        btnBloquear = crearBoton(
                "Activar / Bloquear",
                GRIS,
                150
        );

        btnRestablecerClave = crearBoton(
                "Restablecer Clave",
                new Color(235, 145, 20),
                160
        );

        btnGestionarRoles = crearBoton(
                "Gestionar Roles",
                AZUL,
                150
        );

        cboRol = new JComboBox<>();

        cboRol.addItem("Todos");
        cboRol.addItem("Administrador");
        cboRol.addItem("Vendedor");
        cboRol.addItem("Cajero");
        cboRol.addItem("Depósito");

        lblTotalUsuarios = new JLabel("0");
        lblUsuariosActivos = new JLabel("0");
        lblUsuariosBloqueados = new JLabel("0");

        inicializarTabla();

    }

    //==========================================================
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Usuario",
                    "Nombre",
                    "Rol",
                    "Estado",
                    "Último Acceso"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);

        //==================================================
        // CONFIGURACIÓN GENERAL
        //==================================================
        tablaUsuarios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaUsuarios.setRowHeight(30);

        tablaUsuarios.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        tablaUsuarios.setForeground(
                new Color(30, 30, 30)
        );

        tablaUsuarios.setBackground(Color.WHITE);

        tablaUsuarios.setGridColor(
                new Color(225, 230, 238)
        );

        tablaUsuarios.setShowVerticalLines(true);
        tablaUsuarios.setShowHorizontalLines(true);

        tablaUsuarios.setSelectionBackground(
                new Color(215, 230, 250)
        );

        tablaUsuarios.setSelectionForeground(
                new Color(20, 40, 80)
        );

        //==================================================
        // HEADER
        //==================================================
        tablaUsuarios.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tablaUsuarios.getTableHeader().setBackground(
                AZUL_OSCURO
        );

        tablaUsuarios.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaUsuarios.getTableHeader().setPreferredSize(
                new Dimension(0, 36)
        );

        tablaUsuarios.getTableHeader().setReorderingAllowed(false);

        tablaUsuarios.getTableHeader().setOpaque(true);

        //==================================================
        // ANCHO COLUMNAS
        //==================================================
        tablaUsuarios.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        tablaUsuarios.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(110);

        tablaUsuarios.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(190);

        tablaUsuarios.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        tablaUsuarios.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);

        tablaUsuarios.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(160);

        DefaultTableCellRenderer headerRenderer
                = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                label.setBackground(AZUL_OSCURO);
                label.setForeground(Color.WHITE);

                label.setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

                label.setHorizontalAlignment(
                        JLabel.CENTER
                );

                label.setOpaque(true);

                label.setBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                0,
                                1,
                                new Color(40, 85, 150)
                        )
                );

                return label;
            }
        };

        for (int i = 0;
                i < tablaUsuarios.getColumnCount();
                i++) {

            tablaUsuarios.getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(headerRenderer);
        }
    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
   private void construirPanel() {

    //==========================================================
    // PANEL PRINCIPAL
    //==========================================================
    setLayout(new BorderLayout());

    setBackground(FONDO);


    //==========================================================
    // CONTENEDOR GENERAL
    //==========================================================
    JPanel contenedor = new JPanel(
            new BorderLayout(0, 15)
    );

    contenedor.setBackground(BLANCO);

    contenedor.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE),
                    new EmptyBorder(
                            25,
                            30,
                            25,
                            30
                    )
            )
    );


    //==========================================================
    // HEADER
    //==========================================================
    JPanel header = new JPanel();

    header.setOpaque(false);

    header.setLayout(
            new BoxLayout(
                    header,
                    BoxLayout.Y_AXIS
            )
    );


    JLabel lblTitulo =
            new JLabel("USUARIOS");

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


    JLabel lblSubtitulo =
            new JLabel(
                    "Administración de usuarios, roles y accesos al sistema"
            );

    lblSubtitulo.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    14
            )
    );

    lblSubtitulo.setForeground(
            TEXTO_SECUNDARIO
    );

    lblSubtitulo.setAlignmentX(
            LEFT_ALIGNMENT
    );


    header.add(lblTitulo);

    header.add(
            Box.createVerticalStrut(5)
    );

    header.add(lblSubtitulo);


    //==========================================================
    // HEADER → CONTENEDOR
    //==========================================================
    contenedor.add(
            header,
            BorderLayout.NORTH
    );


    //==========================================================
    // PANEL CENTRAL
    //==========================================================
    JPanel centro = new JPanel();

    centro.setOpaque(false);

    centro.setLayout(
            new BoxLayout(
                    centro,
                    BoxLayout.Y_AXIS
            )
    );

    centro.setBorder(
            new EmptyBorder(
                    10,
                    0,
                    20,
                    0
            )
    );


    //==========================================================
    // TAMAÑO DEL CONTENIDO
    //==========================================================
    centro.setPreferredSize(
            new Dimension(
                    850,
                    680
            )
    );

    centro.setMinimumSize(
            new Dimension(
                    700,
                    680
            )
    );


    //==========================================================
    // BUSCADOR
    //==========================================================
    JPanel panelBusqueda =
            crearPanelBusqueda();

    panelBusqueda.setAlignmentX(
            LEFT_ALIGNMENT
    );

    panelBusqueda.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    75
            )
    );

    centro.add(
            panelBusqueda
    );


    // ESPACIO
    centro.add(
            Box.createVerticalStrut(20)
    );


    //==========================================================
    // RESUMEN
    //==========================================================
    JPanel panelResumen =
            crearPanelResumen();

    panelResumen.setAlignmentX(
            LEFT_ALIGNMENT
    );

    panelResumen.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    85
            )
    );

    centro.add(
            panelResumen
    );


    // ESPACIO
    centro.add(
            Box.createVerticalStrut(20)
    );


    //==========================================================
    // TABLA + BOTONES
    //==========================================================
    JPanel panelTabla =
            crearPanelTabla();

    panelTabla.setAlignmentX(
            LEFT_ALIGNMENT
    );

    centro.add(
            panelTabla
    );


    // MÁS ESPACIO ENTRE TABLA Y ROLES
    centro.add(
            Box.createVerticalStrut(25)
    );


    //==========================================================
    // ROLES Y PERMISOS
    //==========================================================
    JPanel panelRoles =
            crearPanelRoles();

    panelRoles.setAlignmentX(
            LEFT_ALIGNMENT
    );

    centro.add(
            panelRoles
    );


    centro.add(
            Box.createVerticalStrut(20)
    );


    //==========================================================
    // SCROLL CONTENIDO
    //==========================================================
    JScrollPane scrollContenido =
            new JScrollPane(centro);

    scrollContenido.setBorder(null);

    scrollContenido.setOpaque(false);

    scrollContenido
            .getViewport()
            .setOpaque(false);


    // SOLO SCROLL VERTICAL
    scrollContenido.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scrollContenido.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );


    // VELOCIDAD DE RUEDA DEL MOUSE
    scrollContenido
            .getVerticalScrollBar()
            .setUnitIncrement(16);


    //==========================================================
    // FORZAR INICIO ARRIBA
    //==========================================================
    javax.swing.SwingUtilities.invokeLater(() -> {

        scrollContenido
                .getViewport()
                .setViewPosition(
                        new Point(
                                0,
                                0
                        )
                );

    });


    //==========================================================
    // SCROLL → CONTENEDOR
    //==========================================================
    contenedor.add(
            scrollContenido,
            BorderLayout.CENTER
    );


    //==========================================================
    // CONTENEDOR → PANELCONFIGUSUARIOS
    //==========================================================
    add(
            contenedor,
            BorderLayout.CENTER
    );

}

    //==========================================================
    // PANEL BUSQUEDA
    //==========================================================
    private JPanel crearPanelBusqueda() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDE),
                        "Buscar Usuario",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        8,
                        8,
                        8,
                        8
                );

        c.anchor
                = GridBagConstraints.WEST;

        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Usuario o nombre"
                ),
                c
        );

        //------------------------------------------------------
        //======================================================
// LABEL BUSCAR
//======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        panel.add(
                crearLabel("Usuario o nombre"),
                c
        );

//======================================================
// CAMPO BUSCAR
//======================================================
        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;

        txtBuscar.setPreferredSize(
                new Dimension(280, 32)
        );

        txtBuscar.setMinimumSize(
                new Dimension(180, 32)
        );

        panel.add(
                txtBuscar,
                c
        );

//======================================================
// LABEL ROL
//======================================================
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        panel.add(
                crearLabel("Rol"),
                c
        );

//======================================================
// COMBO ROL
//======================================================
        c.gridx = 3;

        cboRol.setPreferredSize(
                new Dimension(170, 32)
        );

        panel.add(
                cboRol,
                c
        );

//======================================================
// BOTON BUSCAR
//======================================================
        c.gridx = 4;

        panel.add(
                btnBuscar,
                c
        );

        return panel;

    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel
                = new JPanel(
                        new java.awt.GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearTarjetaResumen(
                        "Usuarios",
                        lblTotalUsuarios,
                        AZUL
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Activos",
                        lblUsuariosActivos,
                        VERDE
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Bloqueados",
                        lblUsuariosBloqueados,
                        ROJO
                )
        );

        return panel;

    }

    private JPanel crearTarjetaResumen(
            String titulo,
            JLabel valor,
            Color color) {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                new Color(
                        250,
                        251,
                        253
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        JLabel lblTitulo
                = new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        valor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        valor.setForeground(color);

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                valor,
                BorderLayout.CENTER
        );

        return panel;

    }

    //==========================================================
    // TABLA
    //==========================================================
    private JPanel crearPanelTabla() {

        JPanel panel = new JPanel(
                new BorderLayout(0, 15)
        );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDE),
                        "Listado de Usuarios",
                        0,
                        0,
                        new Font("Segoe UI", Font.BOLD, 15),
                        AZUL_OSCURO
                )
        );

        //==========================================================
        // TABLA
        //==========================================================
        JScrollPane scroll = new JScrollPane(tablaUsuarios);

        scroll.setPreferredSize(
                new Dimension(750, 170)
        );

        scroll.setMinimumSize(
                new Dimension(500, 170)
        );

        // Evitamos scroll horizontal innecesario
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        //==========================================================
        // BOTONES
        //==========================================================
        JPanel botones = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        12,
                        12
                )
        );

        botones.setOpaque(false);

        botones.setPreferredSize(
                new Dimension(750, 60)
        );

        botones.setMinimumSize(
                new Dimension(500, 52)
        );

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnBloquear);
        botones.add(btnRestablecerClave);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        //==========================================================
        // TAMAÑO DEL BLOQUE COMPLETO
        //==========================================================
        panel.setPreferredSize(
                new Dimension(750, 250)
        );

        panel.setMinimumSize(
                new Dimension(500, 250)
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        250
                )
        );

        return panel;
    }

    //==========================================================
    // ROLES
    //==========================================================
    private JPanel crearPanelRoles() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDE),
                        "Roles y Permisos",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

        panel.setPreferredSize(
                new Dimension(750, 75)
        );

        panel.setMinimumSize(
                new Dimension(500, 75)
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        75
                )
        );

        JLabel lblInfo
                = new JLabel(
                        "Configure los roles y permisos disponibles para los usuarios."
                );

        lblInfo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblInfo.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(
                lblInfo,
                BorderLayout.CENTER
        );

        JPanel botones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        botones.setOpaque(false);

        botones.add(
                btnGestionarRoles
        );

        panel.add(
                botones,
                BorderLayout.EAST
        );

        return panel;

    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label
                = new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(
                        60,
                        70,
                        85
                )
        );

        return label;

    }

    //==========================================================
    // BOTON
    //==========================================================
    private JButton crearBoton(
            String texto,
            Color color,
            int ancho) {

        JButton boton
                = new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        34
                )
        );

        boton.setFocusPainted(false);

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        return boton;

    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloTabla.setRowCount(0);

        modeloTabla.addRow(
                new Object[]{
                    1,
                    "santiago",
                    "Santiago Contreras",
                    "Administrador",
                    "Activo",
                    "18/08/2026 11:45"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    2,
                    "juan",
                    "Juan Pérez",
                    "Vendedor",
                    "Activo",
                    "18/08/2026 09:20"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    3,
                    "maria",
                    "María López",
                    "Cajero",
                    "Bloqueado",
                    "17/08/2026 18:10"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    4,
                    "pedro",
                    "Pedro Gómez",
                    "Depósito",
                    "Activo",
                    "17/08/2026 16:40"
                }
        );

        actualizarResumen();

    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private void actualizarResumen() {

        int total
                = modeloTabla.getRowCount();

        int activos = 0;
        int bloqueados = 0;

        for (int i = 0;
                i < modeloTabla.getRowCount();
                i++) {

            String estado
                    = modeloTabla
                            .getValueAt(
                                    i,
                                    4
                            )
                            .toString();

            if (estado.equalsIgnoreCase(
                    "Activo"
            )) {

                activos++;

            } else if (estado.equalsIgnoreCase(
                    "Bloqueado"
            )) {

                bloqueados++;

            }

        }

        lblTotalUsuarios.setText(
                String.valueOf(total)
        );

        lblUsuariosActivos.setText(
                String.valueOf(activos)
        );

        lblUsuariosBloqueados.setText(
                String.valueOf(bloqueados)
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnBuscar.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Búsqueda de usuarios.",
                    "Usuarios",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnNuevo.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Nuevo usuario.",
                    "Usuarios",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEditar.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Editar usuario.",
                    "Usuarios",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnBloquear.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Activar / Bloquear usuario.",
                    "Usuarios",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnRestablecerClave.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Restablecer contraseña del usuario.",
                    "Seguridad",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnGestionarRoles.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Gestión de roles y permisos.",
                    "Roles",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

    }

}
