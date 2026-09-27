package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoEgresoCaja extends JDialog {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private static final Color AZUL_SERENA =
            new Color(25, 70, 145);

    private static final Color FONDO =
            new Color(232, 236, 242);

    private static final Color BORDE =
            new Color(210, 218, 228);

    private static final Color TEXTO =
            new Color(45, 52, 62);

    private static final Color TEXTO_SECUNDARIO =
            new Color(100, 108, 120);

    private static final Color ROJO =
            new Color(185, 55, 65);

    private static final Color ROJO_HOVER =
            new Color(205, 70, 80);

    private static final Color GRIS_METAL =
            new Color(75, 83, 93);

    private static final Color GRIS_METAL_HOVER =
            new Color(95, 104, 116);

    //==========================================================
    // DATOS DEL MOVIMIENTO
    //==========================================================
    private JLabel lblUsuario;
    private JLabel lblFecha;
    private JLabel lblHora;

    //==========================================================
    // DATOS DEL EGRESO
    //==========================================================
    private JTextField txtMonto;

    private JComboBox<String> comboMedioPago;
    private JComboBox<String> comboMotivo;

    private JTextArea txtObservaciones;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCancelar;
    private JButton btnRegistrar;

    //==========================================================
    // DATOS INTERNOS
    //==========================================================
    private String usuario;

    private boolean confirmado = false;

    private double monto = 0;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoEgresoCaja(
            Window parent,
            String usuario) {

        super(parent);

        this.usuario = usuario;

        inicializarComponentes();

        construirDialogo();

        cargarDatos();

        configurarEventos();

        setTitle("Egreso de Caja");

        setModal(true);

        setSize(
                new Dimension(
                        680,
                        590
                )
        );

        setMinimumSize(
                new Dimension(
                        620,
                        550
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

        //======================================================
        // MONTO
        //======================================================
        txtMonto =
                new JTextField();

        txtMonto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        txtMonto.setHorizontalAlignment(
                JTextField.RIGHT
        );

        txtMonto.setPreferredSize(
                new Dimension(
                        280,
                        44
                )
        );

        //======================================================
        // MEDIO DE PAGO
        //======================================================
        comboMedioPago =
                new JComboBox<>();

        comboMedioPago.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        comboMedioPago.setPreferredSize(
                new Dimension(
                        280,
                        36
                )
        );

        //======================================================
        // MOTIVO
        //======================================================
        comboMotivo =
                new JComboBox<>();

        comboMotivo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        comboMotivo.setPreferredSize(
                new Dimension(
                        280,
                        36
                )
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

        btnRegistrar =
                crearBotonRojo(
                        "REGISTRAR EGRESO",
                        175
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

        //======================================================
        // HEADER
        //======================================================
        add(
                crearHeader(),
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
                        18,
                        22,
                        18,
                        22
                )
        );

        JPanel datos =
                crearPanelDatos();

        JPanel egreso =
                crearPanelEgreso();

        JPanel observaciones =
                crearPanelObservaciones();

        datos.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        egreso.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        observaciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(datos);

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(egreso);

        contenido.add(
                Box.createVerticalStrut(12)
        );

        contenido.add(observaciones);

        //======================================================
        // SCROLL
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

        scroll.getViewport()
                .setBackground(FONDO);

        add(
                scroll,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER
        //======================================================
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
                        "EGRESO DE CAJA"
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
                        "Registre una salida de dinero de la caja actual"
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
                        "DATOS DEL MOVIMIENTO"
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
                        145
                )
        );

        return panel;
    }

    //==========================================================
    // PANEL EGRESO
    //==========================================================
    private JPanel crearPanelEgreso() {

        JPanel panel =
                crearTarjeta(
                        "DETALLE DEL EGRESO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // MONTO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;

        panel.add(
                crearLabel("Monto"),
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

        JLabel simbolo =
                new JLabel("$");

        simbolo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        simbolo.setForeground(
                AZUL_OSCURO
        );

        panelMonto.add(
                simbolo,
                BorderLayout.WEST
        );

        panelMonto.add(
                txtMonto,
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

        panel.add(
                comboMedioPago,
                c
        );

        //======================================================
        // MOTIVO
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Motivo"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                comboMotivo,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        185
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
                        85
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        135
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
                btnRegistrar
        );

        return footer;
    }

    //==========================================================
    // CARGAR DATOS
    //==========================================================
    private void cargarDatos() {

        //======================================================
        // USUARIO
        //======================================================
        lblUsuario.setText(
                usuario == null
                        || usuario.trim().isEmpty()
                        ? "Usuario"
                        : usuario
        );

        //======================================================
        // FECHA Y HORA
        //======================================================
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

        //======================================================
        // MEDIOS DE PAGO
        //======================================================
        comboMedioPago.removeAllItems();

        comboMedioPago.addItem(
                "Seleccione..."
        );

        comboMedioPago.addItem(
                "Efectivo"
        );

        comboMedioPago.addItem(
                "Transferencia"
        );

        comboMedioPago.addItem(
                "Tarjeta"
        );

        comboMedioPago.addItem(
                "Mercado Pago"
        );

        comboMedioPago.addItem(
                "Otro"
        );

        //======================================================
        // MOTIVOS
        //======================================================
        comboMotivo.removeAllItems();

        comboMotivo.addItem(
                "Seleccione..."
        );

        comboMotivo.addItem(
                "Retiro de efectivo"
        );

        comboMotivo.addItem(
                "Pago a proveedor"
        );

        comboMotivo.addItem(
                "Compra de insumos"
        );

        comboMotivo.addItem(
                "Gastos de transporte"
        );

        comboMotivo.addItem(
                "Gastos de limpieza"
        );

        comboMotivo.addItem(
                "Servicios"
        );

        comboMotivo.addItem(
                "Devolución a cliente"
        );

        comboMotivo.addItem(
                "Ajuste autorizado"
        );

        comboMotivo.addItem(
                "Otro"
        );

        txtMonto.setText("");

        txtObservaciones.setText("");
    }

    //==========================================================
    // EVENTOS
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
        btnRegistrar.addActionListener(e -> {

            registrarEgreso();
        });

        //======================================================
        // ENTER EN MONTO
        //======================================================
        txtMonto.addActionListener(e -> {

            comboMedioPago.requestFocus();
        });
    }

    //==========================================================
    // REGISTRAR EGRESO
    //==========================================================
    private void registrarEgreso() {

        //======================================================
        // VALIDAR MONTO
        //======================================================
        Double valorMonto =
                obtenerMontoCampo();

        if (valorMonto == null
                || valorMonto <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un monto válido mayor a cero.",
                    "Egreso de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            txtMonto.requestFocus();

            return;
        }

        //======================================================
        // VALIDAR MEDIO DE PAGO
        //======================================================
        if (comboMedioPago.getSelectedIndex()
                == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el medio de pago.",
                    "Egreso de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            comboMedioPago.requestFocus();

            return;
        }

        //======================================================
        // VALIDAR MOTIVO
        //======================================================
        if (comboMotivo.getSelectedIndex()
                == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el motivo del egreso.",
                    "Egreso de Caja",
                    JOptionPane.WARNING_MESSAGE
            );

            comboMotivo.requestFocus();

            return;
        }

        //======================================================
        // DATOS
        //======================================================
        String medioPago =
                comboMedioPago
                        .getSelectedItem()
                        .toString();

        String motivo =
                comboMotivo
                        .getSelectedItem()
                        .toString();

        NumberFormat moneda =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "es",
                                "AR"
                        )
                );

        //======================================================
        // CONFIRMACION
        //======================================================
        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar este egreso?\n\n"
                        + "Monto: "
                        + moneda.format(valorMonto)
                        + "\n"
                        + "Medio de pago: "
                        + medioPago
                        + "\n"
                        + "Motivo: "
                        + motivo
                        + "\n"
                        + "Usuario: "
                        + lblUsuario.getText(),
                        "Confirmar Egreso",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        //======================================================
        // GUARDAMOS RESULTADO
        //======================================================
        monto =
                valorMonto;

        confirmado =
                true;

        //======================================================
        // TODAVIA NO GUARDA EN MYSQL
        //======================================================
        JOptionPane.showMessageDialog(
                this,
                "Egreso registrado correctamente.\n\n"
                + "Monto: "
                + moneda.format(monto)
                + "\n"
                + "Medio de pago: "
                + medioPago
                + "\n"
                + "Motivo: "
                + motivo,
                "Egreso Registrado",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // OBTENER MONTO
    //==========================================================
    private Double obtenerMontoCampo() {

        try {

            String texto =
                    txtMonto
                            .getText()
                            .trim();

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

        } catch (NumberFormatException e) {

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

        JButton boton =
                crearBotonBase(
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
                        ROJO_HOVER
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

    public double getMonto() {

        return monto;
    }

    public String getMedioPago() {

        Object seleccionado =
                comboMedioPago
                        .getSelectedItem();

        return seleccionado == null
                ? ""
                : seleccionado.toString();
    }

    public String getMotivo() {

        Object seleccionado =
                comboMotivo
                        .getSelectedItem();

        return seleccionado == null
                ? ""
                : seleccionado.toString();
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }

    public String getUsuario() {

        return usuario;
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
