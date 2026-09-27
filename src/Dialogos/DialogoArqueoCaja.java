package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogoArqueoCaja extends JDialog {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private static final Color AZUL_SERENA =
            new Color(25, 70, 145);

    private static final Color AZUL_HOVER =
            new Color(45, 95, 180);

    private static final Color FONDO =
            new Color(232, 236, 242);

    private static final Color BORDE =
            new Color(210, 218, 228);

    private static final Color TEXTO =
            new Color(45, 52, 62);

    private static final Color TEXTO_SECUNDARIO =
            new Color(100, 108, 120);

    private static final Color VERDE =
            new Color(25, 135, 84);

    private static final Color ROJO =
            new Color(190, 55, 65);

    private static final Color NARANJA =
            new Color(205, 125, 35);

    private static final Color GRIS_METAL =
            new Color(75, 83, 93);

    private static final Color GRIS_METAL_HOVER =
            new Color(95, 104, 116);

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JLabel lblUsuario;
    private JLabel lblFecha;
    private JLabel lblHora;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblEfectivoEsperado;
    private JLabel lblTarjeta;
    private JLabel lblTransferencia;
    private JLabel lblMercadoPago;

    //==========================================================
    // CONTEO
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
    private JButton btnRegistrarArqueo;

    //==========================================================
    // VARIABLES
    //==========================================================
    private String usuario;

    private double efectivoEsperado;
    private double totalTarjeta;
    private double totalTransferencia;
    private double totalMercadoPago;

    private double efectivoContado;
    private double diferencia;

    private boolean confirmado = false;

    //==========================================================
    // FORMATO MONEDA
    //==========================================================
    private final NumberFormat formatoMoneda =
            NumberFormat.getCurrencyInstance(
                    new Locale("es", "AR")
            );

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoArqueoCaja(
            Window parent,
            String usuario,
            double efectivoEsperado,
            double totalTarjeta,
            double totalTransferencia,
            double totalMercadoPago) {

        super(parent);

        this.usuario =
                usuario;

        this.efectivoEsperado =
                efectivoEsperado;

        this.totalTarjeta =
                totalTarjeta;

        this.totalTransferencia =
                totalTransferencia;

        this.totalMercadoPago =
                totalMercadoPago;
        
         EstiloBotones.corregirBotones(
                getContentPane()
    );

        inicializarComponentes();

        construirDialogo();

        cargarDatos();

        configurarEventos();

        setTitle(
                "Arqueo de Caja"
        );

        setModal(true);

        setSize(
                new Dimension(
                        720,
                        690
                )
        );

        setMinimumSize(
                new Dimension(
                        660,
                        640
                )
        );

        setResizable(false);

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(parent);
    }

    //==========================================================
    // CONSTRUCTOR SIMPLIFICADO
    //==========================================================
    public DialogoArqueoCaja(
            Window parent,
            String usuario,
            double efectivoEsperado) {

        this(
                parent,
                usuario,
                efectivoEsperado,
                0,
                0,
                0
        );
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        lblUsuario =
                crearValor("-");

        lblFecha =
                crearValor("-");

        lblHora =
                crearValor("-");

        //======================================================
        // RESUMEN
        //======================================================
        lblEfectivoEsperado =
                crearValorMoneda();

        lblTarjeta =
                crearValorMoneda();

        lblTransferencia =
                crearValorMoneda();

        lblMercadoPago =
                crearValorMoneda();

        //======================================================
        // EFECTIVO CONTADO
        //======================================================
        txtEfectivoContado =
                new JTextField();

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
                new Dimension(
                        280,
                        46
                )
        );

        //======================================================
        // DIFERENCIA
        //======================================================
        lblDiferencia =
                new JLabel(
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

        lblEstadoDiferencia =
                new JLabel(
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
        txtObservaciones =
                new JTextArea();

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
        btnCancelar =
                crearBotonMetal(
                        "CANCELAR",
                        120
                );

        btnRegistrarArqueo =
                crearBotonPrimario(
                        "REGISTRAR ARQUEO",
                        180
                );
    }

    //==========================================================
    // CONSTRUIR DIALOGO
    //==========================================================
    private void construirDialogo() {

        setLayout(
                new BorderLayout()
        );

        getContentPane()
                .setBackground(
                        FONDO
                );

        add(
                crearHeader(),
                BorderLayout.NORTH
        );

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
                        18,
                        22,
                        18,
                        22
                )
        );

        JPanel datos =
                crearPanelDatos();

        JPanel resumen =
                crearPanelResumen();

        JPanel conteo =
                crearPanelConteo();

        JPanel observaciones =
                crearPanelObservaciones();

        datos.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        resumen.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        observaciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(datos);

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(resumen);

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(conteo);

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(observaciones);

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
    // HEADER
    //==========================================================
    private JPanel crearHeader() {

        JPanel panel =
                new JPanel();

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

        JLabel titulo =
                new JLabel(
                        "ARQUEO DE CAJA"
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

        JLabel subtitulo =
                new JLabel(
                        "Compare el efectivo esperado con el dinero contado físicamente"
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
    // PANEL DATOS
    //==========================================================
    private JPanel crearPanelDatos() {

        JPanel panel =
                crearTarjeta(
                        "DATOS DEL ARQUEO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        // USUARIO
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Usuario"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblUsuario,
                c
        );

        // FECHA
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Fecha"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblFecha,
                c
        );

        // HORA
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Hora"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblHora,
                c
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
    // PANEL RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel =
                crearTarjeta(
                        "RESUMEN DE CAJA"
                );

        panel.setLayout(
                new GridLayout(
                        4,
                        2,
                        12,
                        8
                )
        );

        panel.add(
                crearLabel(
                        "Efectivo esperado"
                )
        );

        panel.add(
                lblEfectivoEsperado
        );

        panel.add(
                crearLabel(
                        "Tarjeta"
                )
        );

        panel.add(
                lblTarjeta
        );

        panel.add(
                crearLabel(
                        "Transferencia"
                )
        );

        panel.add(
                lblTransferencia
        );

        panel.add(
                crearLabel(
                        "Mercado Pago"
                )
        );

        panel.add(
                lblMercadoPago
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        170
                )
        );

        return panel;
    }

    //==========================================================
    // PANEL CONTEO
    //==========================================================
    private JPanel crearPanelConteo() {

        JPanel panel =
                crearTarjeta(
                        "CONTEO DE EFECTIVO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // EFECTIVO CONTADO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Efectivo contado"
                ),
                c
        );

        JPanel panelMonto =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        panelMonto.setOpaque(false);

        JLabel signo =
                new JLabel("$");

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
        c.fill =
                GridBagConstraints.HORIZONTAL;

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
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Diferencia"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

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
    // PANEL OBSERVACIONES
    //==========================================================
    private JPanel crearPanelObservaciones() {

        JPanel panel =
                crearTarjeta(
                        "OBSERVACIONES"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JScrollPane scroll =
                new JScrollPane(
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
                btnRegistrarArqueo
        );

        return footer;
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

        LocalDateTime ahora =
                LocalDateTime.now();

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                );

        lblFecha.setText(
                ahora.format(
                        formatoFecha
                )
        );

        lblHora.setText(
                ahora.format(
                        formatoHora
                )
        );

        lblEfectivoEsperado.setText(
                formatoMoneda.format(
                        efectivoEsperado
                )
        );

        lblTarjeta.setText(
                formatoMoneda.format(
                        totalTarjeta
                )
        );

        lblTransferencia.setText(
                formatoMoneda.format(
                        totalTransferencia
                )
        );

        lblMercadoPago.setText(
                formatoMoneda.format(
                        totalMercadoPago
                )
        );

        txtEfectivoContado.setText("");

        lblDiferencia.setText(
                formatoMoneda.format(0)
        );

        lblEstadoDiferencia.setText(
                "Ingrese el efectivo contado"
        );

        lblEstadoDiferencia.setForeground(
                TEXTO_SECUNDARIO
        );
    }

    //==========================================================
    // CONFIGURAR EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar.addActionListener(e -> {

            confirmado = false;

            dispose();
        });

        //======================================================
        // REGISTRAR
        //======================================================
        btnRegistrarArqueo.addActionListener(e -> {

            registrarArqueo();
        });

        //======================================================
        // CALCULAR DIFERENCIA EN TIEMPO REAL
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

        Double contado =
                obtenerMonto(
                        txtEfectivoContado.getText()
                );

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

        diferencia =
                contado - efectivoEsperado;

        lblDiferencia.setText(
                formatoMoneda.format(
                        diferencia
                )
        );

        //======================================================
        // CAJA EXACTA
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
    // REGISTRAR ARQUEO
    //==========================================================
    private void registrarArqueo() {

        Double contado =
                obtenerMonto(
                        txtEfectivoContado
                                .getText()
                );

        if (contado == null
                || contado < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un monto válido para el efectivo contado.",
                    "Arqueo de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            txtEfectivoContado
                    .requestFocus();

            return;
        }

        efectivoContado =
                contado;

        diferencia =
                efectivoContado
                - efectivoEsperado;

        String estado;

        if (Math.abs(diferencia) < 0.01) {

            estado =
                    "CAJA EXACTA";

        } else if (diferencia > 0) {

            estado =
                    "SOBRANTE";

        } else {

            estado =
                    "FALTANTE";
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar este arqueo?\n\n"
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
                        "Confirmar Arqueo",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        confirmado =
                true;

        //======================================================
        // MAS ADELANTE SE GUARDA EN MYSQL
        //======================================================
        JOptionPane.showMessageDialog(
                this,
                "Arqueo registrado correctamente.\n\n"
                + "Resultado: "
                + estado
                + "\n"
                + "Diferencia: "
                + formatoMoneda.format(
                        diferencia
                ),
                "Arqueo Registrado",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // OBTENER MONTO
    //==========================================================
    private Double obtenerMonto(
            String texto) {

        try {

            if (texto == null) {

                return null;
            }

            texto =
                    texto.trim();

            if (texto.isEmpty()) {

                return null;
            }

            texto =
                    texto
                            .replace("$", "")
                            .replace(" ", "")
                            .replace(".", "")
                            .replace(",", ".");

            return Double.parseDouble(
                    texto
            );

        } catch (
                NumberFormatException e) {

            return null;
        }
    }

    //==========================================================
    // CREAR TARJETA
    //==========================================================
    private JPanel crearTarjeta(
            String titulo) {

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

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        7,
                        10,
                        7,
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
                new JLabel(
                        texto
                );

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
                AZUL_OSCURO
        );

        return label;
    }

    //==========================================================
    // VALOR MONEDA
    //==========================================================
    private JLabel crearValorMoneda() {

        JLabel label =
                new JLabel(
                        formatoMoneda.format(0)
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
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
    // BOTON BASE
    //==========================================================
    private JButton crearBotonBase(
            String texto,
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

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
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
    // BOTON PRIMARIO
    //==========================================================
    private JButton crearBotonPrimario(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                AZUL_SERENA
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
                        AZUL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        AZUL_SERENA
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

        JButton boton =
                crearBotonBase(
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

    public double getEfectivoEsperado() {

        return efectivoEsperado;
    }

    public double getEfectivoContado() {

        return efectivoContado;
    }

    public double getDiferencia() {

        return diferencia;
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

        return lblFecha
                .getText();
    }

    public String getHora() {

        return lblHora
                .getText();
    }

    public String getEstadoArqueo() {

        if (Math.abs(diferencia) < 0.01) {

            return "EXACTA";
        }

        if (diferencia > 0) {

            return "SOBRANTE";
        }

        return "FALTANTE";
    }
}
