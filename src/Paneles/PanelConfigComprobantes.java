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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PanelConfigComprobantes extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JComboBox<String> cboTipoComprobante;

    private JTextField txtPuntoVenta;
    private JTextField txtProximoNumero;

    private JCheckBox chkNombreComercial;
    private JCheckBox chkCuit;
    private JCheckBox chkDireccion;
    private JCheckBox chkTelefono;
    private JCheckBox chkVendedor;
    private JCheckBox chkFechaHora;

    private JTextArea txtPieComprobante;

    private JCheckBox chkImprimirAutomaticamente;
    private JCheckBox chkVistaPrevia;
    private JCheckBox chkPermitirReimpresion;

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
    public PanelConfigComprobantes() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();
         EstiloBotones.corregirBotones(this);

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        cboTipoComprobante = new JComboBox<>();

        cboTipoComprobante.addItem("Ticket");
        cboTipoComprobante.addItem("Factura A");
        cboTipoComprobante.addItem("Factura B");
        cboTipoComprobante.addItem("Factura C");
        cboTipoComprobante.addItem("Presupuesto");
        cboTipoComprobante.addItem("Nota de Crédito");

        txtPuntoVenta = new JTextField();
        txtProximoNumero = new JTextField();

        chkNombreComercial =
                new JCheckBox("Mostrar nombre comercial");

        chkCuit =
                new JCheckBox("Mostrar CUIT");

        chkDireccion =
                new JCheckBox("Mostrar dirección");

        chkTelefono =
                new JCheckBox("Mostrar teléfono");

        chkVendedor =
                new JCheckBox("Mostrar vendedor");

        chkFechaHora =
                new JCheckBox("Mostrar fecha y hora");

        txtPieComprobante =
                new JTextArea(4, 30);

        txtPieComprobante.setLineWrap(true);
        txtPieComprobante.setWrapStyleWord(true);

        chkImprimirAutomaticamente =
                new JCheckBox(
                        "Imprimir automáticamente al finalizar la venta"
                );

        chkVistaPrevia =
                new JCheckBox(
                        "Mostrar vista previa antes de imprimir"
                );

        chkPermitirReimpresion =
                new JCheckBox(
                        "Permitir reimpresión de comprobantes"
                );

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

        JCheckBox[] checks = {
            chkNombreComercial,
            chkCuit,
            chkDireccion,
            chkTelefono,
            chkVendedor,
            chkFechaHora,
            chkImprimirAutomaticamente,
            chkVistaPrevia,
            chkPermitirReimpresion
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

    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        JPanel contenedor =
                new JPanel(
                        new BorderLayout(0, 15)
                );

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel("COMPROBANTES");

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
                        "Numeración, datos visibles y comportamiento de los comprobantes"
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

        contenedor.add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel centro =
                new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new javax.swing.BoxLayout(
                        centro,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        10,
                        0,
                        15,
                        0
                )
        );

        JPanel panelGeneral =
                crearPanelGeneral();

        JPanel panelDatos =
                crearPanelDatosVisibles();

        JPanel panelPie =
                crearPanelPie();

        JPanel panelOpciones =
                crearPanelOpciones();

        panelGeneral.setAlignmentX(LEFT_ALIGNMENT);
        panelDatos.setAlignmentX(LEFT_ALIGNMENT);
        panelPie.setAlignmentX(LEFT_ALIGNMENT);
        panelOpciones.setAlignmentX(LEFT_ALIGNMENT);

        centro.add(panelGeneral);

        centro.add(
                javax.swing.Box.createVerticalStrut(15)
        );

        centro.add(panelDatos);

        centro.add(
                javax.swing.Box.createVerticalStrut(15)
        );

        centro.add(panelPie);

        centro.add(
                javax.swing.Box.createVerticalStrut(15)
        );

        centro.add(panelOpciones);

        centro.add(
                javax.swing.Box.createVerticalStrut(15)
        );

        //======================================================
        // SCROLL
        //======================================================
        JScrollPane scroll =
                new JScrollPane(centro);

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
                .setBackground(Color.WHITE);

        contenedor.add(
                scroll,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES
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

        botones.add(btnRestaurar);
        botones.add(btnGuardar);

        contenedor.add(
                botones,
                BorderLayout.SOUTH
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );

    }

    //==========================================================
    // CONFIGURACION GENERAL
    //==========================================================
    private JPanel crearPanelGeneral() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Configuración General"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // TIPO COMPROBANTE
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Tipo predeterminado"
                ),
                c
        );

        c.gridx = 1;

        cboTipoComprobante.setPreferredSize(
                new Dimension(
                        220,
                        32
                )
        );

        panel.add(
                cboTipoComprobante,
                c
        );

        //======================================================
        // PUNTO VENTA
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Punto de Venta"
                ),
                c
        );

        c.gridx = 3;

        txtPuntoVenta.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        panel.add(
                txtPuntoVenta,
                c
        );

        //======================================================
        // NUMERO
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel(
                        "Próximo Número"
                ),
                c
        );

        c.gridx = 1;

        txtProximoNumero.setPreferredSize(
                new Dimension(
                        220,
                        32
                )
        );

        panel.add(
                txtProximoNumero,
                c
        );

        return panel;

    }

    //==========================================================
    // DATOS VISIBLES
    //==========================================================
    private JPanel crearPanelDatosVisibles() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Datos visibles en el comprobante"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                chkNombreComercial,
                c
        );

        c.gridx = 1;

        panel.add(
                chkCuit,
                c
        );

        c.gridx = 2;

        panel.add(
                chkDireccion,
                c
        );

        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                chkTelefono,
                c
        );

        c.gridx = 1;

        panel.add(
                chkVendedor,
                c
        );

        c.gridx = 2;

        panel.add(
                chkFechaHora,
                c
        );

        return panel;

    }

    //==========================================================
    // PIE COMPROBANTE
    //==========================================================
    private JPanel crearPanelPie() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Pie del Comprobante"
                )
        );

        JLabel info =
                new JLabel(
                        "Texto que aparecerá al final del ticket o comprobante:"
                );

        info.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        panel.add(
                info,
                BorderLayout.NORTH
        );

        JScrollPane scroll =
                new JScrollPane(
                        txtPieComprobante
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
    // OPCIONES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new javax.swing.BoxLayout(
                        panel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Opciones de Impresión"
                )
        );

        chkImprimirAutomaticamente.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkVistaPrevia.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkPermitirReimpresion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panel.add(
                chkImprimirAutomaticamente
        );

        panel.add(
                javax.swing.Box
                        .createVerticalStrut(8)
        );

        panel.add(
                chkVistaPrevia
        );

        panel.add(
                javax.swing.Box
                        .createVerticalStrut(8)
        );

        panel.add(
                chkPermitirReimpresion
        );

        return panel;

    }

    //==========================================================
    // BORDER TITULO
    //==========================================================
    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE),
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

        boton.setFocusPainted(false);

        boton.setBackground(color);

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

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        cboTipoComprobante.setSelectedItem(
                "Ticket"
        );

        txtPuntoVenta.setText(
                "0001"
        );

        txtProximoNumero.setText(
                "00000001"
        );

        chkNombreComercial.setSelected(true);
        chkCuit.setSelected(true);
        chkDireccion.setSelected(true);
        chkTelefono.setSelected(true);
        chkVendedor.setSelected(true);
        chkFechaHora.setSelected(true);

        txtPieComprobante.setText(
                "Gracias por su compra."
        );

        chkImprimirAutomaticamente.setSelected(
                true
        );

        chkVistaPrevia.setSelected(
                false
        );

        chkPermitirReimpresion.setSelected(
                true
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Configuración de comprobantes guardada.",
                    "Comprobantes",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

    }

}
