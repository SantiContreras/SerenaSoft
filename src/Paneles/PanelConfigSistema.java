package Paneles;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PanelConfigSistema extends JPanel {

    //==========================================================
    // CONFIGURACION REGIONAL
    //==========================================================
    private JComboBox<String> cboMoneda;
    private JTextField txtSimboloMoneda;
    private JComboBox<String> cboFormatoFecha;
    private JComboBox<String> cboSeparadorDecimal;
    private JSpinner spnDecimales;

    //==========================================================
    // COMPORTAMIENTO
    //==========================================================
    private JCheckBox chkConfirmarEliminar;
    private JCheckBox chkAbrirVentasInicio;
    private JCheckBox chkRecordarUsuario;
    private JCheckBox chkMostrarConfirmaciones;
    private JCheckBox chkBuscarActualizaciones;

    //==========================================================
    // INICIO Y SESION
    //==========================================================
    private JComboBox<String> cboPanelInicial;
    private JSpinner spnTiempoSesion;
    private JCheckBox chkRecordarUltimoModulo;

    //==========================================================
    // INFORMACION DEL SISTEMA
    //==========================================================
    private JLabel lblVersion;
    private JLabel lblBaseDatos;
    private JLabel lblJava;
    private JLabel lblModo;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnRestaurar;
    private JButton btnGuardar;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BLANCO =
            Color.WHITE;

    private final Color AZUL =
            new Color(25, 70, 145);

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigSistema() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();
        
         EstiloBotones.corregirBotones(this);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // REGIONAL
        //======================================================
        cboMoneda = new JComboBox<>();

        cboMoneda.addItem("ARS - Peso Argentino");
        cboMoneda.addItem("USD - Dólar");
        cboMoneda.addItem("PYG - Guaraní");

        txtSimboloMoneda = new JTextField();

        cboFormatoFecha = new JComboBox<>();

        cboFormatoFecha.addItem("dd/MM/yyyy");
        cboFormatoFecha.addItem("dd-MM-yyyy");
        cboFormatoFecha.addItem("yyyy-MM-dd");

        cboSeparadorDecimal = new JComboBox<>();

        cboSeparadorDecimal.addItem(",");
        cboSeparadorDecimal.addItem(".");

        spnDecimales =
                new JSpinner(
                        new SpinnerNumberModel(
                                2,
                                0,
                                4,
                                1
                        )
                );

        //======================================================
        // COMPORTAMIENTO
        //======================================================
        chkConfirmarEliminar =
                new JCheckBox(
                        "Confirmar antes de eliminar o dar de baja registros"
                );

        chkAbrirVentasInicio =
                new JCheckBox(
                        "Abrir el módulo Ventas al iniciar el sistema"
                );

        chkRecordarUsuario =
                new JCheckBox(
                        "Recordar el último usuario utilizado"
                );

        chkMostrarConfirmaciones =
                new JCheckBox(
                        "Mostrar mensajes de confirmación después de guardar"
                );

        chkBuscarActualizaciones =
                new JCheckBox(
                        "Buscar actualizaciones del sistema al iniciar"
                );

        JCheckBox[] checks = {
            chkConfirmarEliminar,
            chkAbrirVentasInicio,
            chkRecordarUsuario,
            chkMostrarConfirmaciones,
            chkBuscarActualizaciones
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

        //======================================================
        // INICIO Y SESION
        //======================================================
        cboPanelInicial = new JComboBox<>();

        cboPanelInicial.addItem("Ventas");
        cboPanelInicial.addItem("Artículos");
        cboPanelInicial.addItem("Clientes");
        cboPanelInicial.addItem("Stock");
        cboPanelInicial.addItem("Caja");

        spnTiempoSesion =
                new JSpinner(
                        new SpinnerNumberModel(
                                30,
                                5,
                                480,
                                5
                        )
                );

        chkRecordarUltimoModulo =
                new JCheckBox(
                        "Recordar el último módulo abierto"
                );

        chkRecordarUltimoModulo.setOpaque(false);

        chkRecordarUltimoModulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        //======================================================
        // INFO
        //======================================================
        lblVersion = new JLabel("1.0");
        lblBaseDatos = new JLabel("MySQL");
        lblJava = new JLabel("Java 21");
        lblModo = new JLabel("Desarrollo");

        //======================================================
        // BOTONES
        //======================================================
        btnRestaurar =
                crearBoton(
                        "Restaurar",
                        GRIS,
                        140
                );

        btnGuardar =
                crearBoton(
                        "Guardar Cambios",
                        VERDE,
                        160
                );
    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        //======================================================
        // CONTENEDOR GENERAL
        //======================================================
        JPanel contenedor =
                new JPanel(
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

        //======================================================
        // HEADER
        //======================================================
        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel("SISTEMA");

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

        JLabel lblSubtitulo =
                new JLabel(
                        "Parámetros generales del funcionamiento de Serena Soft"
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

        header.add(lblTitulo);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(lblSubtitulo);

        contenedor.add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO CENTRAL
        //======================================================
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
                        15,
                        0,
                        20,
                        0
                )
        );

        centro.setPreferredSize(
                new Dimension(
                        850,
                        650
                )
        );

        centro.setMinimumSize(
                new Dimension(
                        700,
                        650
                )
        );

        //======================================================
        // SECCIONES
        //======================================================
        JPanel panelRegional =
                crearPanelRegional();

        JPanel panelComportamiento =
                crearPanelComportamiento();

        JPanel panelInicioSesion =
                crearPanelInicioSesion();

        JPanel panelInformacion =
                crearPanelInformacion();

        //======================================================
        // TAMAÑOS CONTROLADOS
        //======================================================
        configurarTamanoPanel(
                panelRegional,
                145
        );

        configurarTamanoPanel(
                panelComportamiento,
                175
        );

        configurarTamanoPanel(
                panelInicioSesion,
                135
        );

        configurarTamanoPanel(
                panelInformacion,
                130
        );

        panelRegional.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelComportamiento.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelInicioSesion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelInformacion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        //======================================================
        // AGREGAMOS SECCIONES
        //======================================================
        centro.add(
                panelRegional
        );

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(
                panelComportamiento
        );

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(
                panelInicioSesion
        );

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(
                panelInformacion
        );

        centro.add(
                Box.createVerticalStrut(15)
        );

        //======================================================
        // SCROLL CENTRAL
        //======================================================
        JScrollPane scrollContenido =
                new JScrollPane(
                        centro
                );

        scrollContenido.setBorder(null);

        scrollContenido.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollContenido.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollContenido
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollContenido
                .getViewport()
                .setBackground(BLANCO);

        contenedor.add(
                scrollContenido,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES INFERIORES
        //======================================================
        JPanel botones =
                new JPanel(
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

        contenedor.add(
                botones,
                BorderLayout.SOUTH
        );

        //======================================================
        // CONTENEDOR FINAL
        //======================================================
        add(
                contenedor,
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // CONFIGURACION REGIONAL
    //==========================================================
    private JPanel crearPanelRegional() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Configuración Regional"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // MONEDA
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Moneda"),
                c
        );

        c.gridx = 1;

        cboMoneda.setPreferredSize(
                new Dimension(
                        220,
                        32
                )
        );

        panel.add(
                cboMoneda,
                c
        );

        //======================================================
        // SIMBOLO
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel("Símbolo"),
                c
        );

        c.gridx = 3;

        txtSimboloMoneda.setPreferredSize(
                new Dimension(
                        80,
                        32
                )
        );

        panel.add(
                txtSimboloMoneda,
                c
        );

        //======================================================
        // FECHA
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel(
                        "Formato de fecha"
                ),
                c
        );

        c.gridx = 1;

        cboFormatoFecha.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        panel.add(
                cboFormatoFecha,
                c
        );

        //======================================================
        // SEPARADOR
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Separador decimal"
                ),
                c
        );

        c.gridx = 3;

        cboSeparadorDecimal.setPreferredSize(
                new Dimension(
                        80,
                        32
                )
        );

        panel.add(
                cboSeparadorDecimal,
                c
        );

        //======================================================
        // DECIMALES
        //======================================================
        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Decimales"
                ),
                c
        );

        c.gridx = 5;

        spnDecimales.setPreferredSize(
                new Dimension(
                        80,
                        32
                )
        );

        panel.add(
                spnDecimales,
                c
        );

        return panel;
    }

    //==========================================================
    // COMPORTAMIENTO
    //==========================================================
    private JPanel crearPanelComportamiento() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Comportamiento del Sistema"
                )
        );

        JCheckBox[] checks = {
            chkConfirmarEliminar,
            chkAbrirVentasInicio,
            chkRecordarUsuario,
            chkMostrarConfirmaciones,
            chkBuscarActualizaciones
        };

        for (JCheckBox check : checks) {

            check.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            panel.add(check);

            panel.add(
                    Box.createVerticalStrut(7)
            );
        }

        return panel;
    }

    //==========================================================
    // INICIO Y SESION
    //==========================================================
    private JPanel crearPanelInicioSesion() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Inicio y Sesión"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // PANEL INICIAL
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Módulo inicial"
                ),
                c
        );

        c.gridx = 1;

        cboPanelInicial.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        panel.add(
                cboPanelInicial,
                c
        );

        //======================================================
        // TIEMPO SESION
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Tiempo de sesión"
                ),
                c
        );

        c.gridx = 3;

        spnTiempoSesion.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnTiempoSesion,
                c
        );

        c.gridx = 4;

        panel.add(
                new JLabel("minutos"),
                c
        );

        //======================================================
        // RECORDAR MODULO
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 5;

        panel.add(
                chkRecordarUltimoModulo,
                c
        );

        return panel;
    }

    //==========================================================
    // INFORMACION DEL SISTEMA
    //==========================================================
    private JPanel crearPanelInformacion() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Información del Sistema"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // VERSION
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Versión"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblVersion,
                c
        );

        //======================================================
        // BD
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Base de Datos"
                ),
                c
        );

        c.gridx = 3;

        panel.add(
                lblBaseDatos,
                c
        );

        //======================================================
        // JAVA
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel("Java"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblJava,
                c
        );

        //======================================================
        // MODO
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel("Modo"),
                c
        );

        c.gridx = 3;

        panel.add(
                lblModo,
                c
        );

        return panel;
    }

    //==========================================================
    // TAMANO PANEL
    //==========================================================
    private void configurarTamanoPanel(
            JPanel panel,
            int alto) {

        panel.setPreferredSize(
                new Dimension(
                        800,
                        alto
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        600,
                        alto
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        alto
                )
        );
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        cboMoneda.setSelectedItem(
                "ARS - Peso Argentino"
        );

        txtSimboloMoneda.setText(
                "$"
        );

        cboFormatoFecha.setSelectedItem(
                "dd/MM/yyyy"
        );

        cboSeparadorDecimal.setSelectedItem(
                ","
        );

        spnDecimales.setValue(
                2
        );

        chkConfirmarEliminar.setSelected(
                true
        );

        chkAbrirVentasInicio.setSelected(
                true
        );

        chkRecordarUsuario.setSelected(
                true
        );

        chkMostrarConfirmaciones.setSelected(
                true
        );

        chkBuscarActualizaciones.setSelected(
                false
        );

        cboPanelInicial.setSelectedItem(
                "Ventas"
        );

        spnTiempoSesion.setValue(
                30
        );

        chkRecordarUltimoModulo.setSelected(
                false
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // CAMBIAR MONEDA
        //======================================================
        cboMoneda.addActionListener(e -> {

            String moneda =
                    (String)
                            cboMoneda
                                    .getSelectedItem();

            if ("ARS - Peso Argentino".equals(moneda)) {

                txtSimboloMoneda.setText("$");

            } else if ("USD - Dólar".equals(moneda)) {

                txtSimboloMoneda.setText("US$");

            } else if ("PYG - Guaraní".equals(moneda)) {

                txtSimboloMoneda.setText("₲");
            }

        });

        //======================================================
        // RESTAURAR
        //======================================================
        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

        //======================================================
        // GUARDAR
        //======================================================
        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración del sistema guardada.",
                            "Sistema",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });
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

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        c.anchor =
                GridBagConstraints.WEST;

        return c;
    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label =
                new JLabel(texto);

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

        JButton boton =
                new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        36
                )
        );

        boton.setFocusPainted(
                false
        );

        boton.setBackground(
                color
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

        return boton;
    }
}