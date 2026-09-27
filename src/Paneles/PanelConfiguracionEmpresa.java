package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PanelConfiguracionEmpresa extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtNombreComercial;
    private JTextField txtRazonSocial;
    private JTextField txtCuit;

    private JComboBox<String> cboCondicionIva;

    private JTextField txtDireccion;
    private JTextField txtLocalidad;
    private JTextField txtProvincia;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JLabel lblLogo;

    private JButton btnSeleccionarLogo;
    private JButton btnGuardar;
    private JButton btnRestaurar;

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

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfiguracionEmpresa() {

        inicializarComponentes();

        construirPanel();

        configurarEventos();

        cargarDatosPrueba();

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        txtNombreComercial = new JTextField();
        txtRazonSocial = new JTextField();
        txtCuit = new JTextField();

        cboCondicionIva = new JComboBox<>();

        cboCondicionIva.addItem("Responsable Inscripto");
        cboCondicionIva.addItem("Monotributista");
        cboCondicionIva.addItem("Exento");
        cboCondicionIva.addItem("Consumidor Final");

        txtDireccion = new JTextField();
        txtLocalidad = new JTextField();
        txtProvincia = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();

        lblLogo = new JLabel(
                "LOGO",
                SwingConstants.CENTER
        );

        btnSeleccionarLogo =
                new JButton("Seleccionar Logo");

        btnGuardar =
                new JButton("Guardar Cambios");

        btnRestaurar =
                new JButton("Restaurar");

    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        setBorder(
                new EmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        //------------------------------------------------------
        // CONTENEDOR PRINCIPAL
        //------------------------------------------------------
        JPanel contenedor =
                new JPanel(
                        new BorderLayout()
                );

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDE
                        ),

                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        //------------------------------------------------------
        // HEADER
        //------------------------------------------------------
        JPanel panelHeader =
                new JPanel(
                        new BorderLayout()
                );

        panelHeader.setOpaque(false);

        JPanel panelTitulos =
                new JPanel();

        panelTitulos.setOpaque(false);

        panelTitulos.setLayout(
                new javax.swing.BoxLayout(
                        panelTitulos,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "EMPRESA"
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

        JLabel lblSubtitulo =
                new JLabel(
                        "Datos generales y fiscales del comercio"
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

        panelTitulos.add(lblTitulo);

        panelTitulos.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        panelTitulos.add(lblSubtitulo);

        panelHeader.add(
                panelTitulos,
                BorderLayout.WEST
        );

        contenedor.add(
                panelHeader,
                BorderLayout.NORTH
        );

        //------------------------------------------------------
        // FORMULARIO
        //------------------------------------------------------
        JPanel formulario =
                new JPanel(
                        new GridBagLayout()
                );

        formulario.setOpaque(false);

        formulario.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        15,
                        0
                )
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        c.anchor =
                GridBagConstraints.WEST;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        //------------------------------------------------------
        // FILA 1
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Nombre Comercial"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtNombreComercial,
                260
        );

        formulario.add(
                txtNombreComercial,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Razón Social"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtRazonSocial,
                260
        );

        formulario.add(
                txtRazonSocial,
                c
        );

        //------------------------------------------------------
        // FILA 2
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;

        formulario.add(
                crearLabel("CUIT"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtCuit,
                220
        );

        formulario.add(
                txtCuit,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Condición IVA"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        cboCondicionIva.setPreferredSize(
                new Dimension(
                        260,
                        32
                )
        );

        formulario.add(
                cboCondicionIva,
                c
        );

        //------------------------------------------------------
        // FILA 3
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Dirección"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;

        configurarCampo(
                txtDireccion,
                600
        );

        formulario.add(
                txtDireccion,
                c
        );

        c.gridwidth = 1;

        //------------------------------------------------------
        // FILA 4
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Localidad"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtLocalidad,
                220
        );

        formulario.add(
                txtLocalidad,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Provincia"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtProvincia,
                220
        );

        formulario.add(
                txtProvincia,
                c
        );

        //------------------------------------------------------
        // FILA 5
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Teléfono"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtTelefono,
                220
        );

        formulario.add(
                txtTelefono,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Email"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtEmail,
                260
        );

        formulario.add(
                txtEmail,
                c
        );

        //------------------------------------------------------
        // LOGO
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Logo"
                ),
                c
        );

        c.gridx = 1;

        lblLogo.setPreferredSize(
                new Dimension(
                        120,
                        90
                )
        );

        lblLogo.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        lblLogo.setForeground(
                TEXTO_SECUNDARIO
        );

        formulario.add(
                lblLogo,
                c
        );

        c.gridx = 2;
        c.gridwidth = 2;

        btnSeleccionarLogo.setPreferredSize(
                new Dimension(
                        170,
                        36
                )
        );

        formulario.add(
                btnSeleccionarLogo,
                c
        );

        c.gridwidth = 1;

        contenedor.add(
                formulario,
                BorderLayout.CENTER
        );

        //------------------------------------------------------
        // BOTONES
        //------------------------------------------------------
        JPanel panelBotones =
                new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        aplicarEstiloBoton(
                btnRestaurar,
                new Color(
                        110,
                        120,
                        135
                )
        );

        aplicarEstiloBoton(
                btnGuardar,
                new Color(
                        25,
                        135,
                        84
                )
        );

        panelBotones.add(
                btnRestaurar
        );

        panelBotones.add(
                btnGuardar
        );

        contenedor.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );

    }

    //==========================================================
    // CONFIGURAR CAMPO
    //==========================================================
    private void configurarCampo(
            JTextField campo,
            int ancho) {

        campo.setPreferredSize(
                new Dimension(
                        ancho,
                        32
                )
        );

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

    }

    //==========================================================
    // CREAR LABEL
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
    // BOTONES
    //==========================================================
    private void aplicarEstiloBoton(
            JButton boton,
            Color color) {

        boton.setPreferredSize(
                new Dimension(
                        160,
                        38
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
                        13
                )
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnSeleccionarLogo.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int resultado =
                    chooser.showOpenDialog(
                            this
                    );

            if (resultado
                    == JFileChooser.APPROVE_OPTION) {

                lblLogo.setText(
                        chooser
                                .getSelectedFile()
                                .getName()
                );

            }

        });

        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Datos de la empresa guardados.",
                            "Configuración",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        txtNombreComercial.setText(
                "Serena Soft"
        );

        txtRazonSocial.setText(
                "Serena Soft"
        );

        txtCuit.setText(
                "20-00000000-0"
        );

        cboCondicionIva
                .setSelectedItem(
                        "Responsable Inscripto"
                );

        txtDireccion.setText(
                "Av. Principal 123"
        );

        txtLocalidad.setText(
                "Resistencia"
        );

        txtProvincia.setText(
                "Chaco"
        );

        txtTelefono.setText(
                "3624-000000"
        );

        txtEmail.setText(
                "contacto@serenasoft.com"
        );

    }

}
