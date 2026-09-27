package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoRegistrarPagoProveedor extends JDialog {

    //==========================================================
    // PROVEEDOR
    //==========================================================
    private int idProveedor;
    private String razonSocial;
    private double saldoActual;

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JLabel lblProveedor;
    private JLabel lblSaldoActual;

    private JTextField txtFecha;
    private JTextField txtImporte;
    private JTextField txtReferencia;

    private JComboBox<String> cboMedioPago;
    private JComboBox<String> cboComprobante;

    private JTextArea txtObservaciones;

    private JButton btnCancelar;
    private JButton btnRegistrarPago;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color ROJO =
            new Color(200, 55, 55);

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
    public DialogoRegistrarPagoProveedor(
            Window parent,
            int idProveedor,
            String razonSocial,
            double saldoActual) {

        super(parent);

        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.saldoActual = saldoActual;

        inicializarComponentes();
        construirDialogo();
        cargarDatos();
        configurarEventos();

        setTitle("Registrar Pago a Proveedor");

        setModal(true);

        setSize(
                new Dimension(
                        700,
                        620
                )
        );

        setMinimumSize(
                new Dimension(
                        650,
                        580
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

        lblProveedor =
                new JLabel("-");

        lblSaldoActual =
                new JLabel("$ 0,00");

        txtFecha =
                new JTextField();

        txtImporte =
                new JTextField();

        txtReferencia =
                new JTextField();

        cboMedioPago =
                new JComboBox<>();

        cboMedioPago.addItem("Efectivo");
        cboMedioPago.addItem("Transferencia");
        cboMedioPago.addItem("Tarjeta de Débito");
        cboMedioPago.addItem("Cheque");
        cboMedioPago.addItem("Mercado Pago");
        cboMedioPago.addItem("Otro");

        cboComprobante =
                new JComboBox<>();

        cboComprobante.addItem(
                "Aplicar a cuenta corriente"
        );

        cboComprobante.addItem(
                "Factura específica"
        );

        cboComprobante.addItem(
                "Pago a cuenta"
        );

        txtObservaciones =
                new JTextArea();

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnRegistrarPago =
                crearBoton(
                        "Registrar Pago",
                        VERDE,
                        160
                );
    }

    //==========================================================
    // CONSTRUIR
    //==========================================================
    private void construirDialogo() {

        setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                FONDO
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header =
                new JPanel();

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBackground(
                Color.WHITE
        );

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
                        "REGISTRAR PAGO"
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
                        "Registre un pago realizado al proveedor"
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

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO
        //======================================================
        JPanel contenido =
                new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setBackground(
                FONDO
        );

        contenido.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JPanel panelProveedor =
                crearPanelProveedor();

        JPanel panelPago =
                crearPanelPago();

        JPanel panelObservaciones =
                crearPanelObservaciones();

        panelProveedor.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelPago.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelObservaciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelProveedor.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        105
                )
        );

        panelPago.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        235
                )
        );

        panelObservaciones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        140
                )
        );

        contenido.add(
                panelProveedor
        );

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(
                panelPago
        );

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(
                panelObservaciones
        );

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scroll =
                new JScrollPane(
                        contenido
                );

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scroll,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER
        //======================================================
        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                12
                        )
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDE
                )
        );

        footer.add(
                btnCancelar
        );

        footer.add(
                btnRegistrarPago
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // PROVEEDOR
    //==========================================================
    private JPanel crearPanelProveedor() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Proveedor"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Proveedor"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblProveedor,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Saldo pendiente"),
                c
        );

        c.gridx = 3;

        lblSaldoActual.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblSaldoActual.setForeground(
                ROJO
        );

        panel.add(
                lblSaldoActual,
                c
        );

        return panel;
    }

    //==========================================================
    // DATOS DEL PAGO
    //==========================================================
    private JPanel crearPanelPago() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Datos del Pago"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // FECHA
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Fecha *"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtFecha
        );

        panel.add(
                txtFecha,
                c
        );

        //======================================================
        // IMPORTE
        //======================================================
        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Importe *"),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtImporte
        );

        panel.add(
                txtImporte,
                c
        );

        //======================================================
        // MEDIO DE PAGO
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Medio de Pago"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCombo(
                cboMedioPago
        );

        panel.add(
                cboMedioPago,
                c
        );

        //======================================================
        // REFERENCIA
        //======================================================
        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Referencia"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCampo(
                txtReferencia
        );

        panel.add(
                txtReferencia,
                c
        );

        //======================================================
        // APLICAR PAGO
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Aplicar Pago"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        configurarCombo(
                cboComprobante
        );

        panel.add(
                cboComprobante,
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
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

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
                        600,
                        80
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // CARGAR DATOS
    //==========================================================
    private void cargarDatos() {

        lblProveedor.setText(
                razonSocial
        );

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "es",
                                "AR"
                        )
                );

        lblSaldoActual.setText(
                formato.format(
                        saldoActual
                )
        );

        // Fecha actual
        java.time.LocalDate fecha =
                java.time.LocalDate.now();

        java.time.format.DateTimeFormatter formatoFecha =
                java.time.format.DateTimeFormatter
                        .ofPattern(
                                "dd/MM/yyyy"
                        );

        txtFecha.setText(
                fecha.format(
                        formatoFecha
                )
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            dispose();

        });

        btnRegistrarPago.addActionListener(e -> {

            if (!validarFormulario()) {
                return;
            }

            double importe =
                    obtenerImporte();

            double nuevoSaldo =
                    saldoActual - importe;

            if (nuevoSaldo < 0) {
                nuevoSaldo = 0;
            }

            NumberFormat formato =
                    NumberFormat.getCurrencyInstance(
                            new Locale(
                                    "es",
                                    "AR"
                            )
                    );

            int opcion =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Proveedor: "
                            + razonSocial
                            + "\n"
                            + "Importe: "
                            + formato.format(importe)
                            + "\n"
                            + "Medio de pago: "
                            + cboMedioPago.getSelectedItem()
                            + "\n\n"
                            + "Saldo posterior: "
                            + formato.format(nuevoSaldo)
                            + "\n\n"
                            + "¿Registrar el pago?",
                            "Confirmar Pago",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (opcion !=
                    JOptionPane.YES_OPTION) {

                return;
            }

            /*
             * MÁS ADELANTE:
             *
             * PagoProveedor pago =
             *      new PagoProveedor(...);
             *
             * pagoProveedorDao.guardar(pago);
             *
             * cuentaCorrienteDao.registrarPago(...);
             *
             * cajaDao.registrarEgreso(...);
             */

            JOptionPane.showMessageDialog(
                    this,
                    "Pago preparado correctamente.\n"
                    + "Proveedor ID: "
                    + idProveedor
                    + "\n\n"
                    + "Cuando conectemos MySQL este movimiento "
                    + "actualizará la cuenta corriente y Caja.",
                    "Pago Registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        });
    }

    //==========================================================
    // VALIDAR
    //==========================================================
    private boolean validarFormulario() {

        if (txtFecha
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la fecha del pago.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtFecha.requestFocus();

            return false;
        }

        if (txtImporte
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el importe.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtImporte.requestFocus();

            return false;
        }

        double importe;

        try {

            importe =
                    obtenerImporte();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "El importe ingresado no es válido.",
                    "Importe incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            txtImporte.requestFocus();

            return false;
        }

        if (importe <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El importe debe ser mayor a cero.",
                    "Importe incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        if (importe > saldoActual) {

            JOptionPane.showMessageDialog(
                    this,
                    "El importe ingresado supera el saldo pendiente.",
                    "Importe incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    //==========================================================
    // OBTENER IMPORTE
    //==========================================================
    private double obtenerImporte() {

        String texto =
                txtImporte
                        .getText()
                        .trim();

        texto =
                texto.replace("$", "")
                        .replace(" ", "");

        /*
         * Permite por ahora:
         *
         * 150000
         * 150000.50
         * 150000,50
         */
        if (texto.contains(",")
                && !texto.contains(".")) {

            texto =
                    texto.replace(
                            ",",
                            "."
                    );
        }

        return Double.parseDouble(
                texto
        );
    }

    //==========================================================
    // CAMPO
    //==========================================================
    private void configurarCampo(
            JTextField campo) {

        campo.setPreferredSize(
                new Dimension(
                        200,
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
    // COMBO
    //==========================================================
    private void configurarCombo(
            JComboBox<?> combo) {

        combo.setPreferredSize(
                new Dimension(
                        200,
                        32
                )
        );

        combo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );
    }

    //==========================================================
    // BORDE
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