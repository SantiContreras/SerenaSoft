package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoVentaFinalizada extends JDialog {

    //==========================================================
    // DATOS DE LA VENTA
    //==========================================================
    private String numeroVenta;
    private String comprobante;
    private String formaPago;
    private String usuario;

    private double total;
    private double recibido;
    private double cambio;

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JLabel lblNumeroVenta;
    private JLabel lblComprobante;
    private JLabel lblFormaPago;

    private JLabel lblTotal;
    private JLabel lblRecibido;
    private JLabel lblCambio;

    private JLabel lblUsuario;
    private JLabel lblFecha;

    private JButton btnImprimir;
    private JButton btnNuevaVenta;
    private JButton btnCerrar;

    //==========================================================
    // RESULTADOS
    //==========================================================
    private boolean nuevaVenta = false;
    private boolean imprimir = false;

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color AZUL =
            new Color(30, 85, 170);

    private final Color VERDE =
            new Color(20, 145, 85);

    private final Color GRIS =
            new Color(105, 115, 130);

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BORDE =
            new Color(218, 224, 232);

    private final Color TEXTO =
            new Color(50, 55, 65);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoVentaFinalizada(
            Window parent,
            String numeroVenta,
            String comprobante,
            String formaPago,
            double total,
            double recibido,
            double cambio,
            String usuario) {

        super(parent);

        this.numeroVenta = numeroVenta;
        this.comprobante = comprobante;
        this.formaPago = formaPago;

        this.total = total;
        this.recibido = recibido;
        this.cambio = cambio;

        this.usuario = usuario;

        inicializarComponentes();
        construirDialogo();
        cargarDatos();
        configurarEventos();

        setTitle("Venta Finalizada");

        setModal(true);

        setSize(
                new Dimension(
                        620,
                        650
                )
        );

        setMinimumSize(
                new Dimension(
                        580,
                        600
                )
        );

        setResizable(false);

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setLocationRelativeTo(parent);
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        lblNumeroVenta = crearValor("-");
        lblComprobante = crearValor("-");
        lblFormaPago = crearValor("-");

        lblTotal =
                new JLabel(
                        "$ 0,00",
                        SwingConstants.CENTER
                );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        lblTotal.setForeground(
                VERDE
        );

        lblRecibido = crearImporte("$ 0,00");
        lblCambio = crearImporte("$ 0,00");

        lblCambio.setForeground(
                VERDE
        );

        lblUsuario = crearTextoSecundario("-");
        lblFecha = crearTextoSecundario("-");

        btnImprimir =
                crearBoton(
                        "IMPRIMIR COMPROBANTE",
                        AZUL,
                        190
                );

        btnNuevaVenta =
                crearBoton(
                        "NUEVA VENTA",
                        VERDE,
                        160
                );

        btnCerrar =
                crearBoton(
                        "Cerrar",
                        GRIS,
                        100
                );
    }

    //==========================================================
    // CONSTRUIR DIALOGO
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
                        22,
                        25,
                        20,
                        25
                )
        );

        //======================================================
        // CIRCULO VERDE
        //======================================================
        JLabel icono =
                new JLabel(
                        "✓",
                        SwingConstants.CENTER
                );

        icono.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        icono.setForeground(
                Color.WHITE
        );

        icono.setOpaque(true);

        icono.setBackground(
                VERDE
        );

        icono.setPreferredSize(
                new Dimension(
                        60,
                        60
                )
        );

        icono.setMinimumSize(
                new Dimension(
                        60,
                        60
                )
        );

        icono.setMaximumSize(
                new Dimension(
                        60,
                        60
                )
        );

        icono.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //======================================================
        // TITULO
        //======================================================
        JLabel titulo =
                new JLabel(
                        "VENTA REALIZADA"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitulo =
                new JLabel(
                        "La venta se registró correctamente"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                GRIS
        );

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        header.add(icono);

        header.add(
                Box.createVerticalStrut(12)
        );

        header.add(titulo);

        header.add(
                Box.createVerticalStrut(5)
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
                        25,
                        15,
                        25
                )
        );

        JPanel panelOperacion =
                crearPanelOperacion();

        JPanel panelTotal =
                crearPanelTotal();

        JPanel panelPago =
                crearPanelPago();

        JPanel panelAuditoria =
                crearPanelAuditoria();

        panelOperacion.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panelTotal.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panelPago.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panelAuditoria.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(panelOperacion);

        contenido.add(
                Box.createVerticalStrut(10)
        );

        contenido.add(panelTotal);

        contenido.add(
                Box.createVerticalStrut(10)
        );

        contenido.add(panelPago);

        contenido.add(
                Box.createVerticalStrut(10)
        );

        contenido.add(panelAuditoria);

        add(
                contenido,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES
        //======================================================
        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                15
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

        footer.add(btnCerrar);
        footer.add(btnImprimir);
        footer.add(btnNuevaVenta);

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // PANEL DATOS OPERACION
    //==========================================================
    private JPanel crearPanelOperacion() {

        JPanel panel =
                crearPanelTarjeta();

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        agregarFila(
                panel,
                c,
                0,
                "Venta N°",
                lblNumeroVenta
        );

        agregarFila(
                panel,
                c,
                1,
                "Comprobante",
                lblComprobante
        );

        agregarFila(
                panel,
                c,
                2,
                "Forma de pago",
                lblFormaPago
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        return panel;
    }

    //==========================================================
    // PANEL TOTAL
    //==========================================================
    private JPanel crearPanelTotal() {

        JPanel panel =
                crearPanelTarjeta();

        panel.setLayout(
                new BorderLayout()
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        115
                )
        );

        JLabel titulo =
                new JLabel(
                        "TOTAL",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        titulo.setBorder(
                new EmptyBorder(
                        8,
                        0,
                        0,
                        0
                )
        );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        panel.add(
                lblTotal,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // PANEL PAGO
    //==========================================================
    private JPanel crearPanelPago() {

        JPanel panel =
                crearPanelTarjeta();

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        agregarFila(
                panel,
                c,
                0,
                "Recibido",
                lblRecibido
        );

        agregarFila(
                panel,
                c,
                1,
                "Cambio",
                lblCambio
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        return panel;
    }

    //==========================================================
    // PANEL AUDITORIA
    //==========================================================
    private JPanel crearPanelAuditoria() {

        JPanel panel =
                crearPanelTarjeta();

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        agregarFila(
                panel,
                c,
                0,
                "Usuario",
                lblUsuario
        );

        agregarFila(
                panel,
                c,
                1,
                "Fecha",
                lblFecha
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        90
                )
        );

        return panel;
    }

    //==========================================================
    // CARGAR DATOS
    //==========================================================
    private void cargarDatos() {

        lblNumeroVenta.setText(
                valorSeguro(
                        numeroVenta,
                        "-"
                )
        );

        lblComprobante.setText(
                valorSeguro(
                        comprobante,
                        "Ticket"
                )
        );

        lblFormaPago.setText(
                valorSeguro(
                        formaPago,
                        "-"
                )
        );

        lblTotal.setText(
                formatearImporte(
                        total
                )
        );

        lblRecibido.setText(
                formatearImporte(
                        recibido
                )
        );

        lblCambio.setText(
                formatearImporte(
                        cambio
                )
        );

        lblUsuario.setText(
                valorSeguro(
                        usuario,
                        "-"
                )
        );

        LocalDateTime ahora =
                LocalDateTime.now();

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy - HH:mm"
                );

        lblFecha.setText(
                ahora.format(
                        formato
                )
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCerrar.addActionListener(e -> {

            nuevaVenta = false;

            dispose();
        });

        btnNuevaVenta.addActionListener(e -> {

            nuevaVenta = true;

            dispose();
        });

        btnImprimir.addActionListener(e -> {

            imprimir = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Más adelante aquí imprimiremos "
                    + "el comprobante de la venta.",
                    "Imprimir comprobante",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    //==========================================================
    // PANEL TARJETA
    //==========================================================
    private JPanel crearPanelTarjeta() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDE
                        ),

                        new EmptyBorder(
                                8,
                                15,
                                8,
                                15
                        )
                )
        );

        return panel;
    }

    //==========================================================
    // AGREGAR FILA
    //==========================================================
    private void agregarFila(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            String titulo,
            JLabel valor) {

        JLabel lblTitulo =
                new JLabel(
                        titulo
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblTitulo.setForeground(
                GRIS
        );

        c.gridx = 0;
        c.gridy = fila;

        c.weightx = 0;

        c.anchor =
                GridBagConstraints.WEST;

        panel.add(
                lblTitulo,
                c
        );

        c.gridx = 1;

        c.weightx = 1;

        c.anchor =
                GridBagConstraints.EAST;

        panel.add(
                valor,
                c
        );
    }

    //==========================================================
    // GRIDBAG
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        6,
                        5,
                        6,
                        5
                );

        c.fill =
                GridBagConstraints.HORIZONTAL;

        return c;
    }

    //==========================================================
    // LABEL VALOR
    //==========================================================
    private JLabel crearValor(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // LABEL IMPORTE
    //==========================================================
    private JLabel crearImporte(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // TEXTO SECUNDARIO
    //==========================================================
    private JLabel crearTextoSecundario(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                new Color(
                        90,
                        95,
                        105
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
                new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        40
                )
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

        boton.setFocusPainted(
                false
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    //==========================================================
    // FORMATEAR IMPORTE
    //==========================================================
    private String formatearImporte(
            double importe) {

        DecimalFormat formato =
                new DecimalFormat(
                        "#,##0.00"
                );

        return "$ "
                + formato.format(
                        importe
                );
    }

    //==========================================================
    // VALOR SEGURO
    //==========================================================
    private String valorSeguro(
            String valor,
            String defecto) {

        if (valor == null
                || valor.trim().isEmpty()) {

            return defecto;
        }

        return valor;
    }

    //==========================================================
    // GETTERS
    //==========================================================
    public boolean isNuevaVenta() {

        return nuevaVenta;
    }

    public boolean isImprimir() {

        return imprimir;
    }
}
