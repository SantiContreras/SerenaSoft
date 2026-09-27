package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PanelConfigImpresion extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JComboBox<String> cboImpresora;
    private JComboBox<String> cboTamanoPapel;
    private JComboBox<String> cboOrientacion;

    private JSpinner spnCopias;

    private JCheckBox chkImprimirAutomaticamente;
    private JCheckBox chkVistaPrevia;
    private JCheckBox chkCorteAutomatico;
    private JCheckBox chkImprimirLogo;
    private JCheckBox chkImprimirQR;

    private JButton btnActualizarImpresoras;
    private JButton btnPrueba;
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

    private final Color NARANJA =
            new Color(235, 145, 20);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigImpresion() {

        inicializarComponentes();

        construirPanel();

        cargarImpresoras();

        cargarDatosPrueba();

        configurarEventos();
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        cboImpresora = new JComboBox<>();

        cboTamanoPapel = new JComboBox<>();
        cboTamanoPapel.addItem("80 mm");
        cboTamanoPapel.addItem("58 mm");
        cboTamanoPapel.addItem("A4");

        cboOrientacion = new JComboBox<>();
        cboOrientacion.addItem("Vertical");
        cboOrientacion.addItem("Horizontal");

        spnCopias = new JSpinner(
                new SpinnerNumberModel(
                        1,
                        1,
                        10,
                        1
                )
        );

        chkImprimirAutomaticamente =
                new JCheckBox(
                        "Imprimir comprobante automáticamente al cobrar"
                );

        chkVistaPrevia =
                new JCheckBox(
                        "Mostrar vista previa antes de imprimir"
                );

        chkCorteAutomatico =
                new JCheckBox(
                        "Cortar papel automáticamente"
                );

        chkImprimirLogo =
                new JCheckBox(
                        "Imprimir logo de la empresa"
                );

        chkImprimirQR =
                new JCheckBox(
                        "Imprimir código QR cuando corresponda"
                );

        JCheckBox[] checks = {
            chkImprimirAutomaticamente,
            chkVistaPrevia,
            chkCorteAutomatico,
            chkImprimirLogo,
            chkImprimirQR
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

        btnActualizarImpresoras =
                crearBoton(
                        "Actualizar",
                        AZUL,
                        120
                );

        btnPrueba =
                crearBoton(
                        "Imprimir Prueba",
                        NARANJA,
                        150
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
        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel("IMPRESIÓN");

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
                        "Configuración de impresoras y comprobantes"
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
        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new javax.swing.BoxLayout(
                        centro,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        15,
                        0
                )
        );

        JPanel panelImpresora =
                crearPanelImpresora();

        JPanel panelFormato =
                crearPanelFormato();

        JPanel panelOpciones =
                crearPanelOpciones();

        panelImpresora.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelFormato.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelOpciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        centro.add(panelImpresora);

        centro.add(
                javax.swing.Box.createVerticalStrut(18)
        );

        centro.add(panelFormato);

        centro.add(
                javax.swing.Box.createVerticalStrut(18)
        );

        centro.add(panelOpciones);

        centro.add(
                javax.swing.Box.createVerticalStrut(20)
        );

        //======================================================
        // SCROLL
        //======================================================
        javax.swing.JScrollPane scroll =
                new javax.swing.JScrollPane(
                        centro
                );

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
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
        // BOTONES INFERIORES
        //======================================================
        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        panelBotones.setOpaque(false);

        panelBotones.add(btnPrueba);
        panelBotones.add(btnRestaurar);
        panelBotones.add(btnGuardar);

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
    // PANEL IMPRESORA
    //==========================================================
    private JPanel crearPanelImpresora() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Impresora"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // IMPRESORA PREDETERMINADA
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Impresora predeterminada"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        cboImpresora.setPreferredSize(
                new Dimension(
                        350,
                        32
                )
        );

        panel.add(
                cboImpresora,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        panel.add(
                btnActualizarImpresoras,
                c
        );

        return panel;
    }

    //==========================================================
    // PANEL FORMATO
    //==========================================================
    private JPanel crearPanelFormato() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Formato de Impresión"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // TAMAÑO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Tamaño de papel"
                ),
                c
        );

        c.gridx = 1;

        cboTamanoPapel.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        panel.add(
                cboTamanoPapel,
                c
        );

        //======================================================
        // ORIENTACION
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Orientación"
                ),
                c
        );

        c.gridx = 3;

        cboOrientacion.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        panel.add(
                cboOrientacion,
                c
        );

        //======================================================
        // COPIAS
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel(
                        "Cantidad de copias"
                ),
                c
        );

        c.gridx = 1;

        spnCopias.setPreferredSize(
                new Dimension(
                        100,
                        32
                )
        );

        panel.add(
                spnCopias,
                c
        );

        return panel;
    }

    //==========================================================
    // PANEL OPCIONES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new javax.swing.BoxLayout(
                        panel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Opciones"
                )
        );

        chkImprimirAutomaticamente.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkVistaPrevia.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkCorteAutomatico.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkImprimirLogo.setAlignmentX(
                LEFT_ALIGNMENT
        );

        chkImprimirQR.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panel.add(
                chkImprimirAutomaticamente
        );

        panel.add(
                javax.swing.Box.createVerticalStrut(8)
        );

        panel.add(
                chkVistaPrevia
        );

        panel.add(
                javax.swing.Box.createVerticalStrut(8)
        );

        panel.add(
                chkCorteAutomatico
        );

        panel.add(
                javax.swing.Box.createVerticalStrut(8)
        );

        panel.add(
                chkImprimirLogo
        );

        panel.add(
                javax.swing.Box.createVerticalStrut(8)
        );

        panel.add(
                chkImprimirQR
        );

        return panel;
    }

    //==========================================================
    // CARGAR IMPRESORAS DEL SISTEMA
    //==========================================================
    private void cargarImpresoras() {

        cboImpresora.removeAllItems();

        PrintService[] servicios =
                PrintServiceLookup
                        .lookupPrintServices(
                                null,
                                null
                        );

        for (PrintService servicio : servicios) {

            cboImpresora.addItem(
                    servicio.getName()
            );
        }

        if (cboImpresora.getItemCount() == 0) {

            cboImpresora.addItem(
                    "No se encontraron impresoras"
            );
        }
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        cboTamanoPapel.setSelectedItem(
                "80 mm"
        );

        cboOrientacion.setSelectedItem(
                "Vertical"
        );

        spnCopias.setValue(1);

        chkImprimirAutomaticamente
                .setSelected(true);

        chkVistaPrevia
                .setSelected(false);

        chkCorteAutomatico
                .setSelected(true);

        chkImprimirLogo
                .setSelected(true);

        chkImprimirQR
                .setSelected(false);
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // ACTUALIZAR IMPRESORAS
        //======================================================
        btnActualizarImpresoras
                .addActionListener(e -> {

            cargarImpresoras();

        });

        //======================================================
        // PRUEBA
        //======================================================
        btnPrueba.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Prueba de impresión.\n"
                            + "La funcionalidad real se conectará después.",
                            "Impresión",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

        });

        //======================================================
        // RESTAURAR
        //======================================================
        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

        //======================================================
        // GUARDAR
        //======================================================
        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración de impresión guardada.",
                            "Impresión",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

        });
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
}