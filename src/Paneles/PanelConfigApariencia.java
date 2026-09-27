package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class PanelConfigApariencia extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JComboBox<String> cboTema;
    private JComboBox<String> cboColorPrincipal;
    private JComboBox<String> cboTamanoFuente;

    private JCheckBox chkAnimaciones;
    private JCheckBox chkIconos;
    private JCheckBox chkBordesRedondeados;
    private JCheckBox chkMenuCompacto;

    private JButton btnRestaurar;
    private JButton btnGuardar;

    //==========================================================
    // VISTA PREVIA
    //==========================================================
    private JPanel panelVistaPrevia;
    private JLabel lblVistaTitulo;
    private JButton btnVistaVentas;
    private JButton btnVistaStock;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BLANCO
            = Color.WHITE;

    private final Color AZUL
            = new Color(25, 70, 145);

    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color VERDE
            = new Color(25, 135, 84);

    private final Color GRIS
            = new Color(110, 120, 135);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigApariencia() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        cboTema = new JComboBox<>();

        cboTema.addItem("Claro");
        cboTema.addItem("Oscuro");
        cboTema.addItem("Sistema");

        cboColorPrincipal = new JComboBox<>();

        cboColorPrincipal.addItem("Azul Serena");
        cboColorPrincipal.addItem("Azul Oscuro");
        cboColorPrincipal.addItem("Verde");
        cboColorPrincipal.addItem("Gris");

        cboTamanoFuente = new JComboBox<>();

        cboTamanoFuente.addItem("Pequeño");
        cboTamanoFuente.addItem("Mediano");
        cboTamanoFuente.addItem("Grande");

        chkAnimaciones
                = new JCheckBox(
                        "Usar animaciones y transiciones"
                );

        chkIconos
                = new JCheckBox(
                        "Mostrar iconos en botones y menús"
                );

        chkBordesRedondeados
                = new JCheckBox(
                        "Utilizar bordes redondeados"
                );

        chkMenuCompacto
                = new JCheckBox(
                        "Usar menú compacto"
                );

        JCheckBox[] checks = {
            chkAnimaciones,
            chkIconos,
            chkBordesRedondeados,
            chkMenuCompacto
        };

        for (JCheckBox check : checks) {

            check.setOpaque(false);

            check.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );
        }

        btnRestaurar
                = crearBoton(
                        "Restaurar",
                        GRIS,
                        140
                );

        btnGuardar
                = crearBoton(
                        "Guardar Cambios",
                        VERDE,
                        160
                );

        inicializarVistaPrevia();
    }

    //==========================================================
    // VISTA PREVIA
    //==========================================================
    private void inicializarVistaPrevia() {

        panelVistaPrevia
                = new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        lblVistaTitulo
                = new JLabel(
                        "SERENA SOFT"
                );

        btnVistaVentas
                = new JButton(
                        "Ventas"
                );

        btnVistaStock
                = new JButton(
                        "Stock"
                );
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
            new BorderLayout(0, 12)
    );

    contenedor.setBackground(BLANCO);

    contenedor.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE),
                    new EmptyBorder(
                            25,
                            30,
                            20,
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


    JLabel lblTitulo = new JLabel(
            "APARIENCIA"
    );

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


    JLabel lblSubtitulo = new JLabel(
            "Personalización visual de la interfaz del sistema"
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
    // AGREGAMOS HEADER
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
                    12,
                    0,
                    5,
                    0
            )
    );


    //==========================================================
    // CONFIGURACIÓN VISUAL
    //==========================================================
    JPanel panelGeneral =
            crearPanelGeneral();

    panelGeneral.setAlignmentX(
            LEFT_ALIGNMENT
    );

    panelGeneral.setPreferredSize(
            new Dimension(
                    800,
                    120
            )
    );

    panelGeneral.setMinimumSize(
            new Dimension(
                    600,
                    120
            )
    );

    panelGeneral.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    120
            )
    );


    //==========================================================
    // OPCIONES
    //==========================================================
    JPanel panelOpciones =
            crearPanelOpciones();

    panelOpciones.setAlignmentX(
            LEFT_ALIGNMENT
    );

    panelOpciones.setPreferredSize(
            new Dimension(
                    800,
                    115
            )
    );

    panelOpciones.setMinimumSize(
            new Dimension(
                    600,
                    115
            )
    );

    panelOpciones.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    115
            )
    );


    //==========================================================
    // VISTA PREVIA
    //==========================================================
    JPanel panelPreview =
            crearPanelVistaPrevia();

    panelPreview.setAlignmentX(
            LEFT_ALIGNMENT
    );

    panelPreview.setPreferredSize(
            new Dimension(
                    800,
                    120
            )
    );

    panelPreview.setMinimumSize(
            new Dimension(
                    600,
                    120
            )
    );

    panelPreview.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    120
            )
    );


    //==========================================================
    // AGREGAMOS LOS PANELES
    //==========================================================
    centro.add(
            panelGeneral
    );

    centro.add(
            Box.createVerticalStrut(12)
    );

    centro.add(
            panelOpciones
    );

    centro.add(
            Box.createVerticalStrut(12)
    );

    centro.add(
            panelPreview
    );

    // El sobrante queda abajo.
    centro.add(
            Box.createVerticalGlue()
    );


    //==========================================================
    // AGREGAMOS CONTENIDO CENTRAL
    //==========================================================
    contenedor.add(
            centro,
            BorderLayout.CENTER
    );


    //==========================================================
    // BOTONES INFERIORES
    //==========================================================
    JPanel botones = new JPanel(
            new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    5
            )
    );

    botones.setOpaque(false);

    botones.setBorder(
            new EmptyBorder(
                    5,
                    0,
                    0,
                    0
            )
    );

    botones.add(
            btnRestaurar
    );

    botones.add(
            btnGuardar
    );


    //==========================================================
    // AGREGAMOS BOTONES ABAJO
    //==========================================================
    contenedor.add(
            botones,
            BorderLayout.SOUTH
    );


    //==========================================================
    // CONTENEDOR FINAL
    //==========================================================
    add(
            contenedor,
            BorderLayout.CENTER
    );
}

    //==========================================================
    // GENERAL
    //==========================================================
    private JPanel crearPanelGeneral() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo("Configuración Visual")
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        panel.setPreferredSize(
                new Dimension(
                        800,
                        130
                )
        );

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;

        //==================================================
        // FILA 1
        //==================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Tema"),
                c
        );

        c.gridx = 1;

        cboTema.setPreferredSize(
                new Dimension(160, 30)
        );

        panel.add(
                cboTema,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel("Color principal"),
                c
        );

        c.gridx = 3;

        cboColorPrincipal.setPreferredSize(
                new Dimension(180, 30)
        );

        panel.add(
                cboColorPrincipal,
                c
        );

        //==================================================
        // FILA 2
        //==================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel("Tamaño de fuente"),
                c
        );

        c.gridx = 1;

        cboTamanoFuente.setPreferredSize(
                new Dimension(160, 30)
        );

        panel.add(
                cboTamanoFuente,
                c
        );

        // Esto absorbe el espacio horizontal sobrante
        c.gridx = 4;
        c.weightx = 1.0;

        panel.add(
                Box.createHorizontalGlue(),
                c
        );

        return panel;
    }

    //==========================================================
    // OPCIONES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel
                = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Opciones de Interfaz"
                )
        );

        chkAnimaciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkIconos.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkBordesRedondeados.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkMenuCompacto.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panel.add(
                chkAnimaciones
        );

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(
                chkIconos
        );

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(
                chkBordesRedondeados
        );

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(
                chkMenuCompacto
        );

        return panel;
    }

    //==========================================================
    // VISTA PREVIA
    //==========================================================
    private JPanel crearPanelVistaPrevia() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Vista Previa"
                )
        );

        panelVistaPrevia.setPreferredSize(
                new Dimension(
                        700,
                        85
                )
        );

        panelVistaPrevia.setMinimumSize(
                new Dimension(
                        500,
                        85
                )
        );

        panelVistaPrevia.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        85
                )
        );

        panelVistaPrevia.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        panelVistaPrevia.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        lblVistaTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblVistaTitulo.setForeground(
                AZUL_OSCURO
        );

        lblVistaTitulo.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        panelVistaPrevia.add(
                lblVistaTitulo,
                BorderLayout.NORTH
        );

        JPanel botones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        botones.setOpaque(false);

        aplicarEstiloVista(
                btnVistaVentas
        );

        aplicarEstiloVista(
                btnVistaStock
        );

        botones.add(
                btnVistaVentas
        );

        botones.add(
                btnVistaStock
        );

        panelVistaPrevia.add(
                botones,
                BorderLayout.CENTER
        );

        panel.add(
                panelVistaPrevia,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // VISTA
    //==========================================================
    private void aplicarEstiloVista(
            JButton boton) {

        boton.setPreferredSize(
                new Dimension(
                        110,
                        36
                )
        );

        boton.setFocusPainted(false);

        boton.setBackground(
                AZUL
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        cboTema.setSelectedItem(
                "Claro"
        );

        cboColorPrincipal.setSelectedItem(
                "Azul Serena"
        );

        cboTamanoFuente.setSelectedItem(
                "Mediano"
        );

        chkAnimaciones.setSelected(
                true
        );

        chkIconos.setSelected(
                true
        );

        chkBordesRedondeados.setSelected(
                true
        );

        chkMenuCompacto.setSelected(
                false
        );

        actualizarVistaPrevia();
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        cboTema.addActionListener(e -> {
            actualizarVistaPrevia();
        });

        cboColorPrincipal.addActionListener(e -> {
            actualizarVistaPrevia();
        });

        cboTamanoFuente.addActionListener(e -> {
            actualizarVistaPrevia();
        });

        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración de apariencia guardada.",
                            "Apariencia",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

        });
    }

    //==========================================================
    // ACTUALIZAR VISTA PREVIA
    //==========================================================
    private void actualizarVistaPrevia() {

        String color
                = (String) cboColorPrincipal
                        .getSelectedItem();

        Color colorSeleccionado
                = AZUL;

        if ("Azul Oscuro".equals(color)) {

            colorSeleccionado
                    = AZUL_OSCURO;

        } else if ("Verde".equals(color)) {

            colorSeleccionado
                    = new Color(
                            25,
                            135,
                            84
                    );

        } else if ("Gris".equals(color)) {

            colorSeleccionado
                    = new Color(
                            90,
                            100,
                            115
                    );
        }

        btnVistaVentas.setBackground(
                colorSeleccionado
        );

        btnVistaStock.setBackground(
                colorSeleccionado
        );

        lblVistaTitulo.setForeground(
                colorSeleccionado
        );

        String tema
                = (String) cboTema.getSelectedItem();

        if ("Oscuro".equals(tema)) {

            panelVistaPrevia.setBackground(
                    new Color(
                            45,
                            48,
                            55
                    )
            );

            lblVistaTitulo.setForeground(
                    Color.WHITE
            );

        } else {

            panelVistaPrevia.setBackground(
                    new Color(
                            245,
                            247,
                            250
                    )
            );
        }

        String fuente
                = (String) cboTamanoFuente
                        .getSelectedItem();

        int tamano = 18;

        if ("Pequeño".equals(fuente)) {

            tamano = 15;

        } else if ("Grande".equals(fuente)) {

            tamano = 21;
        }

        lblVistaTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        tamano
                )
        );

        panelVistaPrevia.revalidate();
        panelVistaPrevia.repaint();
    }

    //==========================================================
    // BORDER
    //==========================================================
    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(
                        BORDE
                ),
                titulo,
                0,
                0,
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                ),
                AZUL_OSCURO
        );
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        10,
                        10,
                        10,
                        10
                );

        c.anchor
                = GridBagConstraints.WEST;

        return c;
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
                        36
                )
        );

        boton.setFocusPainted(false);

        boton.setBackground(color);

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        return boton;
    }
}
