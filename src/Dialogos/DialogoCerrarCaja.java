package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogoCerrarCaja extends JDialog {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO = new Color(15, 50, 110);
    private static final Color AZUL_SERENA = new Color(25, 70, 145);
    private static final Color FONDO = new Color(232, 236, 242);
    private static final Color BORDE = new Color(210, 218, 228);
    private static final Color TEXTO = new Color(45, 52, 62);
    private static final Color TEXTO_SECUNDARIO = new Color(100, 108, 120);

    private static final Color VERDE = new Color(25, 135, 84);
    private static final Color ROJO = new Color(190, 55, 65);

    private static final Color GRIS_METAL = new Color(75, 83, 93);
    private static final Color GRIS_METAL_HOVER = new Color(95, 104, 116);

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JLabel lblUsuario;
    private JLabel lblFecha;
    private JLabel lblHora;

    //==========================================================
    // RESUMEN EFECTIVO
    //==========================================================
    private JLabel lblSaldoInicial;
    private JLabel lblVentasEfectivo;
    private JLabel lblIngresosEfectivo;
    private JLabel lblEgresosEfectivo;
    private JLabel lblEfectivoEsperado;

    //==========================================================
    // OTROS MEDIOS
    //==========================================================
    private JLabel lblTarjeta;
    private JLabel lblTransferencia;
    private JLabel lblMercadoPago;

    //==========================================================
    // CONTEO FINAL
    //==========================================================
    private JTextField txtEfectivoContado;

    private JLabel lblDiferencia;
    private JLabel lblEstadoDiferencia;

    //==========================================================
    // OBSERVACIONES
    //==========================================================
    private JTextArea txtObservaciones;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCancelar;
    private JButton btnCerrarCaja;

    //==========================================================
    // VARIABLES
    //==========================================================
    private String usuario;

    private double saldoInicial;
    private double ventasEfectivo;
    private double ingresosEfectivo;
    private double egresosEfectivo;

    private double tarjeta;
    private double transferencia;
    private double mercadoPago;

    private double efectivoEsperado;
    private double efectivoContado;
    private double diferencia;

    private boolean confirmado = false;

    //==========================================================
    // FORMATO MONEDA
    //==========================================================
    private final NumberFormat formatoMoneda
            = NumberFormat.getCurrencyInstance(
                    new Locale("es", "AR")
            );

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoCerrarCaja(
            Window parent,
            String usuario,
            double saldoInicial,
            double ventasEfectivo,
            double ingresosEfectivo,
            double egresosEfectivo,
            double tarjeta,
            double transferencia,
            double mercadoPago) {

        super(parent);

        this.usuario = usuario;

        this.saldoInicial = saldoInicial;
        this.ventasEfectivo = ventasEfectivo;
        this.ingresosEfectivo = ingresosEfectivo;
        this.egresosEfectivo = egresosEfectivo;

        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
        this.mercadoPago = mercadoPago;

        calcularEfectivoEsperado();

        inicializarComponentes();
        construirDialogo();
        cargarDatos();
        configurarEventos();

        setTitle("Cierre de Caja");
        setModal(true);

        setSize(760, 790);
        setMinimumSize(new Dimension(700, 700));

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
    // CALCULAR EFECTIVO ESPERADO
    //==========================================================
    private void calcularEfectivoEsperado() {

        efectivoEsperado
                = saldoInicial
                + ventasEfectivo
                + ingresosEfectivo
                - egresosEfectivo;
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        lblUsuario = crearValor("-");
        lblFecha = crearValor("-");
        lblHora = crearValor("-");

        lblSaldoInicial = crearValorMoneda();
        lblVentasEfectivo = crearValorMoneda();
        lblIngresosEfectivo = crearValorMoneda();
        lblEgresosEfectivo = crearValorMoneda();

        lblEfectivoEsperado = crearValorMonedaGrande();

        lblTarjeta = crearValorMoneda();
        lblTransferencia = crearValorMoneda();
        lblMercadoPago = crearValorMoneda();

        //======================================================
        // EFECTIVO CONTADO
        //======================================================
        txtEfectivoContado = new JTextField();

        txtEfectivoContado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        txtEfectivoContado.setHorizontalAlignment(
                JTextField.RIGHT
        );

        txtEfectivoContado.setPreferredSize(
                new Dimension(280, 46)
        );

        //======================================================
        // DIFERENCIA
        //======================================================
        lblDiferencia
                = new JLabel(
                        formatoMoneda.format(0)
                );

        lblDiferencia.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        lblDiferencia.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblEstadoDiferencia
                = new JLabel(
                        "Ingrese el efectivo contado"
                );

        lblEstadoDiferencia.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblEstadoDiferencia.setForeground(
                TEXTO_SECUNDARIO
        );

        //======================================================
        // OBSERVACIONES
        //======================================================
        txtObservaciones = new JTextArea();

        txtObservaciones.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        //======================================================
        // BOTONES
        //======================================================
        btnCancelar
                = crearBotonMetal(
                        "CANCELAR",
                        120
                );

        btnCerrarCaja
                = crearBotonRojo(
                        "CERRAR CAJA",
                        160
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

        add(
                crearHeader(),
                BorderLayout.NORTH
        );

        JPanel contenido = new JPanel();

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
                        18,
                        22,
                        18,
                        22
                )
        );

        agregarPanel(
                contenido,
                crearPanelDatos()
        );

        agregarPanel(
                contenido,
                crearPanelResumenEfectivo()
        );

        agregarPanel(
                contenido,
                crearPanelOtrosMedios()
        );

        agregarPanel(
                contenido,
                crearPanelConteo()
        );

        agregarPanel(
                contenido,
                crearPanelObservaciones()
        );

        JScrollPane scroll
                = new JScrollPane(contenido);

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        scroll.getViewport()
                .setBackground(FONDO);

        add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                crearFooter(),
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // AGREGAR PANEL
    //==========================================================
    private void agregarPanel(
            JPanel contenido,
            JPanel panel) {

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(panel);

        contenido.add(
                Box.createVerticalStrut(12)
        );
    }

    //==========================================================
    // HEADER
    //==========================================================
    private JPanel crearHeader() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        16,
                        25
                )
        );

        JLabel titulo
                = new JLabel(
                        "CIERRE DE CAJA"
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

        JLabel subtitulo
                = new JLabel(
                        "Realice el conteo final y confirme el cierre de la caja actual"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(subtitulo);

        return panel;
    }

    //==========================================================
    // DATOS DEL CIERRE
    //==========================================================
    private JPanel crearPanelDatos() {

        JPanel panel
                = crearTarjeta(
                        "DATOS DEL CIERRE"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c
                = crearConstraints();

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

        agregarFila(
                panel,
                c,
                2,
                "Hora",
                lblHora
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        return panel;
    }

    //==========================================================
    // RESUMEN EFECTIVO
    //==========================================================
    private JPanel crearPanelResumenEfectivo() {

        JPanel panel
                = crearTarjeta(
                        "RESUMEN DE EFECTIVO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c
                = crearConstraints();

        agregarFila(
                panel,
                c,
                0,
                "Saldo inicial",
                lblSaldoInicial
        );

        agregarFila(
                panel,
                c,
                1,
                "Ventas en efectivo",
                lblVentasEfectivo
        );

        agregarFila(
                panel,
                c,
                2,
                "Ingresos en efectivo",
                lblIngresosEfectivo
        );

        agregarFila(
                panel,
                c,
                3,
                "Egresos en efectivo",
                lblEgresosEfectivo
        );

        agregarFila(
                panel,
                c,
                4,
                "EFECTIVO ESPERADO",
                lblEfectivoEsperado
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        230
                )
        );

        return panel;
    }

    //==========================================================
    // OTROS MEDIOS DE PAGO
    //==========================================================
    private JPanel crearPanelOtrosMedios() {

        JPanel panel
                = crearTarjeta(
                        "OTROS MEDIOS DE PAGO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c
                = crearConstraints();

        agregarFila(
                panel,
                c,
                0,
                "Tarjeta",
                lblTarjeta
        );

        agregarFila(
                panel,
                c,
                1,
                "Transferencia",
                lblTransferencia
        );

        agregarFila(
                panel,
                c,
                2,
                "Mercado Pago",
                lblMercadoPago
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        return panel;
    }

    //==========================================================
    // CONTEO FINAL
    //==========================================================
    private JPanel crearPanelConteo() {

        JPanel panel
                = crearTarjeta(
                        "CONTEO FINAL"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c
                = crearConstraints();

        //======================================================
        // EFECTIVO CONTADO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Efectivo contado"
                ),
                c
        );

        JPanel panelMonto
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        panelMonto.setOpaque(false);

        JLabel signo
                = new JLabel("$");

        signo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        signo.setForeground(
                AZUL_OSCURO
        );

        panelMonto.add(
                signo,
                BorderLayout.WEST
        );

        panelMonto.add(
                txtEfectivoContado,
                BorderLayout.CENTER
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(
                panelMonto,
                c
        );

        //======================================================
        // DIFERENCIA
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Diferencia"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(
                lblDiferencia,
                c
        );

        //======================================================
        // ESTADO
        //======================================================
        c.gridx = 1;
        c.gridy = 2;

        panel.add(
                lblEstadoDiferencia,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        165
                )
        );

        return panel;
    }

    //==========================================================
    // OBSERVACIONES
    //==========================================================
    private JPanel crearPanelObservaciones() {

        JPanel panel
                = crearTarjeta(
                        "OBSERVACIONES DEL CIERRE"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JScrollPane scroll
                = new JScrollPane(
                        txtObservaciones
                );

        scroll.setPreferredSize(
                new Dimension(
                        560,
                        80
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        return panel;
    }

    //==========================================================
    // FOOTER
    //==========================================================
    private JPanel crearFooter() {

        JPanel footer
                = new JPanel(
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
                btnCerrarCaja
        );

        return footer;
    }

    //==========================================================
    // AGREGAR FILA
    //==========================================================
    private void agregarFila(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            String texto,
            JComponent componente) {

        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        c.fill
                = GridBagConstraints.NONE;

        panel.add(
                crearLabel(texto),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(
                componente,
                c
        );
    }

    //==========================================================
    // CARGAR DATOS
    //==========================================================
    private void cargarDatos() {

        lblUsuario.setText(
                usuario == null
                || usuario.trim().isEmpty()
                ? "Usuario"
                : usuario
        );

        LocalDateTime ahora
                = LocalDateTime.now();

        DateTimeFormatter fecha
                = DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        DateTimeFormatter hora
                = DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                );

        lblFecha.setText(
                ahora.format(fecha)
        );

        lblHora.setText(
                ahora.format(hora)
        );

        //======================================================
        // EFECTIVO
        //======================================================
        lblSaldoInicial.setText(
                formatoMoneda.format(
                        saldoInicial
                )
        );

        lblVentasEfectivo.setText(
                formatoMoneda.format(
                        ventasEfectivo
                )
        );

        lblIngresosEfectivo.setText(
                formatoMoneda.format(
                        ingresosEfectivo
                )
        );

        lblEgresosEfectivo.setText(
                "- "
                + formatoMoneda.format(
                        egresosEfectivo
                )
        );

        lblEfectivoEsperado.setText(
                formatoMoneda.format(
                        efectivoEsperado
                )
        );

        lblEfectivoEsperado.setForeground(
                AZUL_SERENA
        );

        //======================================================
        // OTROS MEDIOS
        //======================================================
        lblTarjeta.setText(
                formatoMoneda.format(
                        tarjeta
                )
        );

        lblTransferencia.setText(
                formatoMoneda.format(
                        transferencia
                )
        );

        lblMercadoPago.setText(
                formatoMoneda.format(
                        mercadoPago
                )
        );

        txtEfectivoContado.setText("");

        actualizarDiferenciaVisual(null);
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            confirmado = false;

            dispose();
        });

        btnCerrarCaja.addActionListener(e -> {

            cerrarCaja();
        });

        //======================================================
        // CALCULO AUTOMATICO
        //======================================================
        txtEfectivoContado
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                    @Override
                    public void insertUpdate(
                            DocumentEvent e) {

                        calcularDiferencia();
                    }

                    @Override
                    public void removeUpdate(
                            DocumentEvent e) {

                        calcularDiferencia();
                    }

                    @Override
                    public void changedUpdate(
                            DocumentEvent e) {

                        calcularDiferencia();
                    }
                });
    }

    //==========================================================
    // CALCULAR DIFERENCIA
    //==========================================================
    private void calcularDiferencia() {

        Double contado
                = obtenerMonto(
                        txtEfectivoContado
                                .getText()
                );

        actualizarDiferenciaVisual(
                contado
        );
    }

    //==========================================================
    // ACTUALIZAR DIFERENCIA VISUAL
    //==========================================================
    private void actualizarDiferenciaVisual(
            Double contado) {

        if (contado == null) {

            diferencia = 0;

            lblDiferencia.setText(
                    formatoMoneda.format(0)
            );

            lblDiferencia.setForeground(
                    TEXTO
            );

            lblEstadoDiferencia.setText(
                    "Ingrese el efectivo contado"
            );

            lblEstadoDiferencia.setForeground(
                    TEXTO_SECUNDARIO
            );

            return;
        }

        diferencia
                = contado
                - efectivoEsperado;

        lblDiferencia.setText(
                formatoMoneda.format(
                        diferencia
                )
        );

        //======================================================
        // EXACTA
        //======================================================
        if (Math.abs(diferencia) < 0.01) {

            lblDiferencia.setForeground(
                    VERDE
            );

            lblEstadoDiferencia.setText(
                    "Caja exacta"
            );

            lblEstadoDiferencia.setForeground(
                    VERDE
            );

            //======================================================
            // SOBRANTE
            //======================================================
        } else if (diferencia > 0) {

            lblDiferencia.setForeground(
                    AZUL_SERENA
            );

            lblEstadoDiferencia.setText(
                    "Sobrante de efectivo"
            );

            lblEstadoDiferencia.setForeground(
                    AZUL_SERENA
            );

            //======================================================
            // FALTANTE
            //======================================================
        } else {

            lblDiferencia.setForeground(
                    ROJO
            );

            lblEstadoDiferencia.setText(
                    "Faltante de efectivo"
            );

            lblEstadoDiferencia.setForeground(
                    ROJO
            );
        }
    }

    //==========================================================
    // CERRAR CAJA
    //==========================================================
    private void cerrarCaja() {

        Double contado
                = obtenerMonto(
                        txtEfectivoContado
                                .getText()
                );

        if (contado == null
                || contado < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un monto válido para el efectivo contado.",
                    "Cierre de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            txtEfectivoContado.requestFocus();

            return;
        }

        efectivoContado
                = contado;

        diferencia
                = efectivoContado
                - efectivoEsperado;

        String estado
                = obtenerEstado();

        //======================================================
        // SI EXISTE DIFERENCIA Y NO HAY OBSERVACION
        //======================================================
        if (Math.abs(diferencia) >= 0.01
                && txtObservaciones
                        .getText()
                        .trim()
                        .isEmpty()) {

            int continuar
                    = JOptionPane.showConfirmDialog(
                            this,
                            "Existe una diferencia de "
                            + formatoMoneda.format(
                                    diferencia
                            )
                            + ".\n\n"
                            + "No ingresó una observación.\n"
                            + "¿Desea continuar igualmente?",
                            "Diferencia de Caja",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (continuar
                    != JOptionPane.YES_OPTION) {

                txtObservaciones
                        .requestFocus();

                return;
            }
        }

        //======================================================
        // CONFIRMACION FINAL
        //======================================================
        int opcion
                = JOptionPane.showConfirmDialog(
                        this,
                        "¿Confirma el cierre de caja?\n\n"
                        + "Efectivo esperado: "
                        + formatoMoneda.format(
                                efectivoEsperado
                        )
                        + "\n"
                        + "Efectivo contado: "
                        + formatoMoneda.format(
                                efectivoContado
                        )
                        + "\n"
                        + "Diferencia: "
                        + formatoMoneda.format(
                                diferencia
                        )
                        + "\n"
                        + "Resultado: "
                        + estado
                        + "\n\n"
                        + "Una vez cerrada la caja no podrán "
                        + "registrarse nuevos movimientos.",
                        "Confirmar Cierre",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        confirmado = true;

        //======================================================
        // POR AHORA NO MODIFICAMOS MYSQL
        //======================================================
        JOptionPane.showMessageDialog(
                this,
                "Caja cerrada correctamente.\n\n"
                + "Efectivo esperado: "
                + formatoMoneda.format(
                        efectivoEsperado
                )
                + "\n"
                + "Efectivo contado: "
                + formatoMoneda.format(
                        efectivoContado
                )
                + "\n"
                + "Diferencia: "
                + formatoMoneda.format(
                        diferencia
                )
                + "\n"
                + "Resultado: "
                + estado,
                "Caja Cerrada",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // ESTADO
    //==========================================================
    private String obtenerEstado() {

        if (Math.abs(diferencia) < 0.01) {

            return "CAJA EXACTA";
        }

        if (diferencia > 0) {

            return "SOBRANTE";
        }

        return "FALTANTE";
    }

    //==========================================================
    // OBTENER MONTO
    //==========================================================
    private Double obtenerMonto(
            String texto) {

        try {

            if (texto == null
                    || texto.trim().isEmpty()) {

                return null;
            }

            texto
                    = texto
                            .trim()
                            .replace("$", "")
                            .replace(" ", "")
                            .replace(".", "")
                            .replace(",", ".");

            return Double.parseDouble(
                    texto
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    //==========================================================
    // TARJETA
    //==========================================================
    private JPanel crearTarjeta(
            String titulo) {

        JPanel panel
                = new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(),
                                titulo,
                                0,
                                0,
                                new Font(
                                        "Segoe UI",
                                        Font.BOLD,
                                        13
                                ),
                                AZUL_OSCURO
                        )
                )
        );

        return panel;
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        7,
                        10,
                        7,
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
                        12
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // VALOR
    //==========================================================
    private JLabel crearValor(
            String texto) {

        JLabel label
                = new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                AZUL_OSCURO
        );

        return label;
    }

    //==========================================================
    // VALOR MONEDA
    //==========================================================
    private JLabel crearValorMoneda() {

        JLabel label
                = new JLabel(
                        formatoMoneda.format(0)
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                AZUL_OSCURO
        );

        label.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        return label;
    }

    //==========================================================
    // VALOR MONEDA GRANDE
    //==========================================================
    private JLabel crearValorMonedaGrande() {

        JLabel label
                = new JLabel(
                        formatoMoneda.format(0)
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        label.setForeground(
                AZUL_SERENA
        );

        label.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        return label;
    }

    //==========================================================
    // BOTON BASE
    //==========================================================
    private JButton crearBotonBase(
            String texto,
            int ancho) {

        JButton boton
                = new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        40
                )
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    //==========================================================
    // BOTON ROJO
    //==========================================================
    private JButton crearBotonRojo(
            String texto,
            int ancho) {

        JButton boton
                = crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                ROJO
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        new Color(
                                210,
                                65,
                                75
                        )
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        ROJO
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON METAL
    //==========================================================
    private JButton crearBotonMetal(
            String texto,
            int ancho) {

        JButton boton
                = crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_METAL
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL
                );
            }
        });

        return boton;
    }

    //==========================================================
    // GETTERS
    //==========================================================
    public boolean isConfirmado() {

        return confirmado;
    }

    public double getSaldoInicial() {

        return saldoInicial;
    }

    public double getVentasEfectivo() {

        return ventasEfectivo;
    }

    public double getIngresosEfectivo() {

        return ingresosEfectivo;
    }

    public double getEgresosEfectivo() {

        return egresosEfectivo;
    }

    public double getEfectivoEsperado() {

        return efectivoEsperado;
    }

    public double getEfectivoContado() {

        return efectivoContado;
    }

    public double getDiferencia() {

        return diferencia;
    }

    public double getTarjeta() {

        return tarjeta;
    }

    public double getTransferencia() {

        return transferencia;
    }

    public double getMercadoPago() {

        return mercadoPago;
    }

    public String getEstadoCierre() {

        return obtenerEstado();
    }

    public String getUsuario() {

        return usuario;
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }

    public String getFecha() {

        return lblFecha.getText();
    }

    public String getHora() {

        return lblHora.getText();
    }
}
