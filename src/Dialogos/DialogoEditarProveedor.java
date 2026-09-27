package Dialogos;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DialogoEditarProveedor extends JDialog {

    //==========================================================
    // DATOS DEL PROVEEDOR
    //==========================================================
    private int idProveedor;

    //==========================================================
    // DATOS COMERCIALES
    //==========================================================
    private JTextField txtRazonSocial;
    private JTextField txtNombreComercial;
    private JTextField txtCuit;

    private JComboBox<String> cboCondicionIva;

    //==========================================================
    // DOMICILIO
    //==========================================================
    private JTextField txtDireccion;
    private JTextField txtLocalidad;
    private JTextField txtProvincia;

    //==========================================================
    // CONTACTO
    //==========================================================
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JTextField txtPersonaContacto;
    private JTextField txtTelefonoContacto;

    //==========================================================
    // OTROS
    //==========================================================
    private JTextArea txtObservaciones;

    private JCheckBox chkActivo;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCancelar;
    private JButton btnGuardar;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoEditarProveedor(
            Window parent,
            int idProveedor,
            String razonSocial,
            String cuit,
            String telefono,
            String estado) {

        super(parent);

        this.idProveedor = idProveedor;

        inicializarComponentes();

        construirDialogo();

        cargarDatosProveedor(
                razonSocial,
                cuit,
                telefono,
                estado
        );

        configurarEventos();

        setModal(true);

        setTitle("Editar Proveedor");

        setSize(
                new Dimension(
                        850,
                        650
                )
        );

        setMinimumSize(
                new Dimension(
                        780,
                        600
                )
        );

        setLocationRelativeTo(parent);
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setResizable(true);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        txtRazonSocial = new JTextField();
        txtNombreComercial = new JTextField();
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

        txtPersonaContacto = new JTextField();
        txtTelefonoContacto = new JTextField();

        txtObservaciones = new JTextArea();

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        chkActivo = new JCheckBox("Proveedor activo");

        chkActivo.setOpaque(false);

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnGuardar =
                crearBoton(
                        "Guardar Cambios",
                        VERDE,
                        170
                );
    }

    //==========================================================
    // CONSTRUIR DIALOGO
    //==========================================================
    private void construirDialogo() {

        setLayout(new BorderLayout());

        getContentPane().setBackground(FONDO);

        //======================================================
        // HEADER
        //======================================================
        JPanel header = new JPanel();

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        header.setBackground(Color.WHITE);

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        18,
                        25
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "EDITAR PROVEEDOR"
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
                        "Modifique los datos comerciales y de contacto del proveedor"
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
                javax.swing.Box.createVerticalStrut(5)
        );

        header.add(lblSubtitulo);

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel centro = new JPanel();

        centro.setBackground(FONDO);

        centro.setLayout(
                new javax.swing.BoxLayout(
                        centro,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JPanel panelComercial =
                crearPanelComercial();

        JPanel panelDomicilio =
                crearPanelDomicilio();

        JPanel panelContacto =
                crearPanelContacto();

        JPanel panelObservaciones =
                crearPanelObservaciones();

        panelComercial.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelDomicilio.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelContacto.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelObservaciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelComercial.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        panelDomicilio.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        panelContacto.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        panelObservaciones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        centro.add(panelComercial);

        centro.add(
                javax.swing.Box.createVerticalStrut(12)
        );

        centro.add(panelDomicilio);

        centro.add(
                javax.swing.Box.createVerticalStrut(12)
        );

        centro.add(panelContacto);

        centro.add(
                javax.swing.Box.createVerticalStrut(12)
        );

        centro.add(panelObservaciones);

        centro.add(
                javax.swing.Box.createVerticalStrut(15)
        );

        //======================================================
        // SCROLL
        //======================================================
        JScrollPane scrollContenido =
                new JScrollPane(centro);

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

        add(
                scrollContenido,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER
        //======================================================
        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(Color.WHITE);

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                BORDE
                        ),
                        new EmptyBorder(
                                12,
                                20,
                                12,
                                20
                        )
                )
        );

        JPanel estado =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                0
                        )
                );

        estado.setOpaque(false);

        estado.add(
                chkActivo
        );

        footer.add(
                estado,
                BorderLayout.WEST
        );

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        botones.setOpaque(false);

        botones.add(btnCancelar);
        botones.add(btnGuardar);

        footer.add(
                botones,
                BorderLayout.EAST
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // DATOS COMERCIALES
    //==========================================================
    private JPanel crearPanelComercial() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                crearBordeTitulo(
                        "Datos Comerciales"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        // RAZÓN SOCIAL
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Razón Social *"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtRazonSocial,
                250
        );

        panel.add(
                txtRazonSocial,
                c
        );

        // NOMBRE COMERCIAL
        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Nombre Comercial"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtNombreComercial,
                220
        );

        panel.add(
                txtNombreComercial,
                c
        );

        // CUIT
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("CUIT *"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtCuit,
                180
        );

        panel.add(
                txtCuit,
                c
        );

        // IVA
        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Condición IVA"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        cboCondicionIva.setPreferredSize(
                new Dimension(
                        220,
                        32
                )
        );

        panel.add(
                cboCondicionIva,
                c
        );

        return panel;
    }

    //==========================================================
    // DOMICILIO
    //==========================================================
    private JPanel crearPanelDomicilio() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                crearBordeTitulo(
                        "Domicilio"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Dirección"),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtDireccion,
                500
        );

        panel.add(
                txtDireccion,
                c
        );

        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Localidad"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtLocalidad,
                220
        );

        panel.add(
                txtLocalidad,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Provincia"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtProvincia,
                220
        );

        panel.add(
                txtProvincia,
                c
        );

        return panel;
    }

    //==========================================================
    // CONTACTO
    //==========================================================
    private JPanel crearPanelContacto() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                crearBordeTitulo(
                        "Contacto"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Teléfono"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtTelefono,
                200
        );

        panel.add(
                txtTelefono,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Email"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtEmail,
                250
        );

        panel.add(
                txtEmail,
                c
        );

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Persona de Contacto"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtPersonaContacto,
                220
        );

        panel.add(
                txtPersonaContacto,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Tel. Contacto"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtTelefonoContacto,
                200
        );

        panel.add(
                txtTelefonoContacto,
                c
        );

        return panel;
    }

    //==========================================================
    // OBSERVACIONES
    //==========================================================
    private JPanel crearPanelObservaciones() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                crearBordeTitulo(
                        "Observaciones"
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        txtObservaciones
                );

        scroll.setPreferredSize(
                new Dimension(
                        700,
                        90
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // CARGAR DATOS DEL PROVEEDOR
    //==========================================================
    private void cargarDatosProveedor(
            String razonSocial,
            String cuit,
            String telefono,
            String estado) {

        txtRazonSocial.setText(
                razonSocial
        );

        txtCuit.setText(
                cuit
        );

        txtTelefono.setText(
                telefono
        );

        chkActivo.setSelected(
                "Activo".equalsIgnoreCase(
                        estado
                )
        );

        /*
         * Por ahora estos datos son simulados.
         * Cuando conectemos MySQL vendrán del objeto Proveedor.
         */
        txtNombreComercial.setText(
                razonSocial
        );

        cboCondicionIva.setSelectedItem(
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

        txtEmail.setText(
                "contacto@proveedor.com"
        );

        txtPersonaContacto.setText(
                ""
        );

        txtTelefonoContacto.setText(
                ""
        );

        txtObservaciones.setText(
                ""
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            dispose();

        });

        btnGuardar.addActionListener(e -> {

            if (!validarCampos()) {
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Cambios preparados para guardar.\n"
                    + "Proveedor ID: "
                    + idProveedor,
                    "Editar Proveedor",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Más adelante:
            //
            // Proveedor proveedor = obtenerProveedorFormulario();
            // proveedorDao.actualizar(proveedor);

            dispose();

        });
    }

    //==========================================================
    // VALIDACIONES
    //==========================================================
    private boolean validarCampos() {

        if (txtRazonSocial
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la razón social.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtRazonSocial.requestFocus();

            return false;
        }

        if (txtCuit
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el CUIT.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCuit.requestFocus();

            return false;
        }

        return true;
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
                        13
                )
        );
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
                        8,
                        10,
                        8,
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
}
