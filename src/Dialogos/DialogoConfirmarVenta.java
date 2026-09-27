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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DialogoConfirmarVenta extends JDialog {

    //==========================================================
    // DATOS
    //==========================================================
    private String cliente;
    private String documento;
    private String condicionIVA;
    private String comprobante;
    private String formaPago;

    private double subtotal;
    private double descuento;
    private double total;
    private double recibe;
    private double cambio;

    //==========================================================
    // LABELS
    //==========================================================
    private JLabel lblCliente;
    private JLabel lblDocumento;
    private JLabel lblCondicionIVA;
    private JLabel lblComprobante;
    private JLabel lblFormaPago;

    private JLabel lblSubtotal;
    private JLabel lblDescuento;
    private JLabel lblTotal;

    private JLabel lblRecibe;
    private JLabel lblCambio;

    //==========================================================
    // OTROS
    //==========================================================
    private JTextArea txtObservaciones;
    private JCheckBox chkImprimir;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCancelar;
    private JButton btnConfirmar;

    //==========================================================
    // RESULTADO
    //==========================================================
    private boolean ventaConfirmada = false;

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

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoConfirmarVenta(
            Window parent,
            String cliente,
            String documento,
            String condicionIVA,
            String comprobante,
            String formaPago,
            double subtotal,
            double descuento,
            double total,
            double recibe,
            double cambio) {

        super(parent);

        this.cliente = cliente;
        this.documento = documento;
        this.condicionIVA = condicionIVA;
        this.comprobante = comprobante;
        this.formaPago = formaPago;

        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.recibe = recibe;
        this.cambio = cambio;

        inicializarComponentes();
        construirDialogo();
        cargarDatos();
        configurarEventos();

        setTitle("Confirmar Venta");

        setModal(true);

        setSize(
                new Dimension(
                        720,
                        650
                )
        );

        setMinimumSize(
                new Dimension(
                        650,
                        580
                )
        );
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setLocationRelativeTo(parent);

        setResizable(true);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        lblCliente = crearValor("-");
        lblDocumento = crearValor("-");
        lblCondicionIVA = crearValor("-");
        lblComprobante = crearValor("-");
        lblFormaPago = crearValor("-");

        lblSubtotal = crearImporte("$ 0,00", Color.BLACK);

        lblDescuento = crearImporte("$ 0,00", ROJO);

        lblTotal = crearImporte("$ 0,00", VERDE);

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        lblRecibe = crearImporte("$ 0,00", Color.BLACK);

        lblCambio = crearImporte("$ 0,00", VERDE);

        txtObservaciones =
                new JTextArea();

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        chkImprimir =
                new JCheckBox(
                        "Imprimir comprobante al finalizar"
                );

        chkImprimir.setOpaque(false);
        chkImprimir.setSelected(true);

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnConfirmar =
                crearBoton(
                        "CONFIRMAR VENTA",
                        VERDE,
                        190
                );
    }

    //==========================================================
    // CONSTRUIR
    //==========================================================
    private void construirDialogo() {

        setLayout(
                new BorderLayout()
        );

        getContentPane()
                .setBackground(
                        FONDO
                );

        //======================================================
        // HEADER
        //======================================================
        JPanel header =
                new JPanel();

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
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

        JLabel titulo =
                new JLabel(
                        "CONFIRMAR VENTA"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        JLabel subtitulo =
                new JLabel(
                        "Revise los datos antes de finalizar la operación"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                Color.GRAY
        );

        header.add(titulo);

        header.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        header.add(subtitulo);

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO
        //======================================================
        JPanel contenido =
                new JPanel();

        contenido.setBackground(
                FONDO
        );

        contenido.setLayout(
                new javax.swing.BoxLayout(
                        contenido,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        contenido.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JPanel panelCliente =
                crearPanelCliente();

        JPanel panelImportes =
                crearPanelImportes();

        JPanel panelPago =
                crearPanelPago();

        JPanel panelObservaciones =
                crearPanelObservaciones();

        panelCliente.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelImportes.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelPago.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelObservaciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelCliente.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        panelImportes.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        160
                )
        );

        panelPago.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        panelObservaciones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        contenido.add(panelCliente);

        contenido.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        contenido.add(panelImportes);

        contenido.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        contenido.add(panelPago);

        contenido.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        contenido.add(panelObservaciones);

        contenido.add(
                javax.swing.Box
                        .createVerticalStrut(10)
        );

        contenido.add(chkImprimir);

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
                btnConfirmar
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // CLIENTE
    //==========================================================
    private JPanel crearPanelCliente() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Cliente / Comprobante"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        agregarDato(
                panel,
                c,
                0,
                0,
                "Cliente",
                lblCliente
        );

        agregarDato(
                panel,
                c,
                0,
                2,
                "CUIT / DNI",
                lblDocumento
        );

        agregarDato(
                panel,
                c,
                1,
                0,
                "Condición IVA",
                lblCondicionIVA
        );

        agregarDato(
                panel,
                c,
                1,
                2,
                "Comprobante",
                lblComprobante
        );

        return panel;
    }

    //==========================================================
    // IMPORTES
    //==========================================================
    private JPanel crearPanelImportes() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Resumen de la Venta"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Subtotal"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.anchor =
                GridBagConstraints.EAST;

        panel.add(
                lblSubtotal,
                c
        );

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.anchor =
                GridBagConstraints.WEST;

        panel.add(
                crearLabel("Descuento"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.anchor =
                GridBagConstraints.EAST;

        panel.add(
                lblDescuento,
                c
        );

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.anchor =
                GridBagConstraints.WEST;

        JLabel lblTituloTotal =
                new JLabel(
                        "TOTAL"
                );

        lblTituloTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lblTituloTotal.setForeground(
                AZUL_OSCURO
        );

        panel.add(
                lblTituloTotal,
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.anchor =
                GridBagConstraints.EAST;

        panel.add(
                lblTotal,
                c
        );

        return panel;
    }

    //==========================================================
    // PAGO
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
                        "Pago"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        agregarDato(
                panel,
                c,
                0,
                0,
                "Forma de Pago",
                lblFormaPago
        );

        agregarDato(
                panel,
                c,
                0,
                2,
                "Recibe",
                lblRecibe
        );

        agregarDato(
                panel,
                c,
                1,
                0,
                "Cambio",
                lblCambio
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
                        70
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

        lblCliente.setText(
                cliente == null
                        || cliente.trim().isEmpty()
                        ? "Consumidor Final"
                        : cliente
        );

        lblDocumento.setText(
                documento == null
                        || documento.trim().isEmpty()
                        ? "-"
                        : documento
        );

        lblCondicionIVA.setText(
                condicionIVA
        );

        lblComprobante.setText(
                comprobante
        );

        lblFormaPago.setText(
                formaPago
        );

        lblSubtotal.setText(
                formatearImporte(
                        subtotal
                )
        );

        lblDescuento.setText(
                formatearImporte(
                        descuento
                )
        );

        lblTotal.setText(
                formatearImporte(
                        total
                )
        );

        lblRecibe.setText(
                formatearImporte(
                        recibe
                )
        );

        lblCambio.setText(
                formatearImporte(
                        cambio
                )
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            ventaConfirmada = false;

            dispose();

        });

        btnConfirmar.addActionListener(e -> {

            ventaConfirmada = true;

            dispose();

        });
    }

    //==========================================================
    // AGREGAR DATO
    //==========================================================
    private void agregarDato(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            int columna,
            String texto,
            JLabel valor) {

        c.gridx = columna;
        c.gridy = fila;
        c.weightx = 0;
        c.anchor =
                GridBagConstraints.WEST;

        panel.add(
                crearLabel(texto),
                c
        );

        c.gridx = columna + 1;
        c.weightx = 1;

        panel.add(
                valor,
                c
        );
    }

    //==========================================================
    // GETTERS
    //==========================================================
    public boolean isVentaConfirmada() {

        return ventaConfirmada;
    }

    public boolean isImprimirComprobante() {

        return chkImprimir.isSelected();
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }

    //==========================================================
    // FORMATOS
    //==========================================================
    private String formatearImporte(
            double importe) {

        return String.format(
                "$ %,.2f",
                importe
        );
    }

    //==========================================================
    // VALOR
    //==========================================================
    private JLabel crearValor(
            String texto) {

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        label.setForeground(
                new Color(
                        40,
                        50,
                        65
                )
        );

        return label;
    }

    //==========================================================
    // IMPORTE
    //==========================================================
    private JLabel crearImporte(
            String texto,
            Color color) {

        JLabel label =
                new JLabel(texto);

        label.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        label.setForeground(
                color
        );

        return label;
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
                        38
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
