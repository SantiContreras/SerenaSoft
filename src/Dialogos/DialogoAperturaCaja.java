package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoAperturaCaja extends JDialog {

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

    private static final Color VERDE_HOVER =
            new Color(35, 155, 98);

    private static final Color GRIS_METAL =
            new Color(75, 83, 93);

    private static final Color GRIS_METAL_HOVER =
            new Color(95, 104, 116);

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JLabel lblUsuario;
    private JLabel lblFecha;
    private JLabel lblHora;

    private JTextField txtSaldoInicial;
    private JTextArea txtObservaciones;

    private JButton btnCancelar;
    private JButton btnAbrirCaja;

    //==========================================================
    // DATOS
    //==========================================================
    private String usuario;

    private boolean confirmado = false;

    private double saldoInicial = 0;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoAperturaCaja(
            Window parent,
            String usuario) {

        super(parent);

        this.usuario = usuario;

        inicializarComponentes();

        construirDialogo();

        cargarDatos();

        configurarEventos();

        setTitle("Apertura de Caja");

        setModal(true);

        setSize(
                new Dimension(
                        650,
                        510
                )
        );

        setMinimumSize(
                new Dimension(
                        600,
                        480
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

        lblUsuario =
                crearValor("-");

        lblFecha =
                crearValor("-");

        lblHora =
                crearValor("-");

        txtSaldoInicial =
                new JTextField();

        txtSaldoInicial.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        txtSaldoInicial.setHorizontalAlignment(
                JTextField.RIGHT
        );

        txtSaldoInicial.setPreferredSize(
                new Dimension(
                        250,
                        45
                )
        );

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

        btnCancelar =
                crearBotonMetal(
                        "CANCELAR",
                        120
                );

        btnAbrirCaja =
                crearBotonVerde(
                        "ABRIR CAJA",
                        150
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

        JPanel tarjetaDatos =
                crearPanelDatos();

        JPanel tarjetaSaldo =
                crearPanelSaldo();

        JPanel tarjetaObservaciones =
                crearPanelObservaciones();

        tarjetaDatos.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        tarjetaSaldo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        tarjetaObservaciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(
                tarjetaDatos
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                tarjetaSaldo
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                tarjetaObservaciones
        );

        add(
                contenido,
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
                        "APERTURA DE CAJA"
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
                        "Ingrese el fondo inicial con el que comenzará la jornada"
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

        panel.add(
                titulo
        );

        panel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    //==========================================================
    // PANEL DATOS
    //==========================================================
    private JPanel crearPanelDatos() {

        JPanel panel =
                crearTarjeta(
                        "DATOS DE APERTURA"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // USUARIO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Cajero"),
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

        //======================================================
        // FECHA
        //======================================================
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

        //======================================================
        // HORA
        //======================================================
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
                        150
                )
        );

        return panel;
    }

    //==========================================================
    // PANEL SALDO
    //==========================================================
    private JPanel crearPanelSaldo() {

        JPanel panel =
                crearTarjeta(
                        "SALDO INICIAL"
                );

        panel.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        JLabel lblTexto =
                new JLabel(
                        "Fondo inicial disponible"
                );

        lblTexto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblTexto.setForeground(
                TEXTO
        );

        JPanel campo =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        campo.setOpaque(false);

        JLabel simbolo =
                new JLabel("$");

        simbolo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        simbolo.setForeground(
                AZUL_OSCURO
        );

        campo.add(
                simbolo,
                BorderLayout.WEST
        );

        campo.add(
                txtSaldoInicial,
                BorderLayout.CENTER
        );

        panel.add(
                lblTexto,
                BorderLayout.WEST
        );

        panel.add(
                campo,
                BorderLayout.CENTER
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
                        550,
                        90
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
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
                btnAbrirCaja
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

        txtSaldoInicial.setText(
                "0"
        );

        txtSaldoInicial.selectAll();
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            confirmado = false;

            dispose();
        });

        btnAbrirCaja.addActionListener(e -> {

            abrirCaja();
        });

        txtSaldoInicial.addActionListener(e -> {

            abrirCaja();
        });
    }

    //==========================================================
    // ABRIR CAJA
    //==========================================================
    private void abrirCaja() {

        Double saldo =
                obtenerSaldoInicial();

        if (saldo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un saldo inicial válido.",
                    "Apertura de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            txtSaldoInicial.requestFocus();

            return;
        }

        if (saldo < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El saldo inicial no puede ser negativo.",
                    "Apertura de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            txtSaldoInicial.requestFocus();

            return;
        }

        NumberFormat formatoMoneda =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "es",
                                "AR"
                        )
                );

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea abrir la caja?\n\n"
                        + "Cajero: "
                        + lblUsuario.getText()
                        + "\n"
                        + "Fecha: "
                        + lblFecha.getText()
                        + "\n"
                        + "Hora: "
                        + lblHora.getText()
                        + "\n"
                        + "Saldo inicial: "
                        + formatoMoneda.format(
                                saldo
                        ),
                        "Confirmar Apertura",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        saldoInicial = saldo;

        confirmado = true;

        JOptionPane.showMessageDialog(
                this,
                "Caja abierta correctamente.\n\n"
                + "Saldo inicial: "
                + formatoMoneda.format(
                        saldoInicial
                ),
                "Caja Abierta",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // OBTENER SALDO
    //==========================================================
    private Double obtenerSaldoInicial() {

        try {

            String texto =
                    txtSaldoInicial
                            .getText()
                            .trim();

            if (texto.isEmpty()) {

                return null;
            }

            texto =
                    texto
                            .replace(
                                    "$",
                                    ""
                            )
                            .replace(
                                    " ",
                                    ""
                            )
                            .replace(
                                    ".",
                                    ""
                            )
                            .replace(
                                    ",",
                                    "."
                            );

            return Double.parseDouble(
                    texto
            );

        } catch (
                NumberFormatException e) {

            return null;
        }
    }

    //==========================================================
    // TARJETA
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
    // BOTON VERDE
    //==========================================================
    private JButton crearBotonVerde(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                VERDE
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
                        VERDE_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        VERDE
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

    public double getSaldoInicial() {

        return saldoInicial;
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
}
