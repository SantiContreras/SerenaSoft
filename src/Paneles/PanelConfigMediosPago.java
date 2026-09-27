package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelConfigMediosPago extends JPanel {

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaMediosPago;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnActivarDesactivar;
    private JButton btnEliminar;

    private JButton btnRestaurar;
    private JButton btnGuardar;

    //==========================================================
    // OPCIONES
    //==========================================================
    private JCheckBox chkCombinarMedios;
    private JCheckBox chkReferenciaTransferencia;
    private JCheckBox chkCalcularCambio;

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

    private final Color ROJO =
            new Color(200, 55, 55);

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
    public PanelConfigMediosPago() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        inicializarTabla();

        btnNuevo =
                crearBoton(
                        "Nuevo",
                        VERDE,
                        110
                );

        btnEditar =
                crearBoton(
                        "Editar",
                        AZUL,
                        110
                );

        btnActivarDesactivar =
                crearBoton(
                        "Activar / Desactivar",
                        GRIS,
                        160
                );

        btnEliminar =
                crearBoton(
                        "Eliminar",
                        ROJO,
                        110
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

        chkCombinarMedios =
                new JCheckBox(
                        "Permitir combinar distintos medios de pago"
                );

        chkReferenciaTransferencia =
                new JCheckBox(
                        "Solicitar referencia o comprobante en transferencias"
                );

        chkCalcularCambio =
                new JCheckBox(
                        "Calcular y mostrar cambio en pagos en efectivo"
                );

        JCheckBox[] checks = {
            chkCombinarMedios,
            chkReferenciaTransferencia,
            chkCalcularCambio
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
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Medio de Pago",
                            "Estado",
                            "Recargo / Descuento",
                            "Predeterminado"
                        },
                        0
                ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tablaMediosPago =
                new JTable(modeloTabla);

        tablaMediosPago.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMediosPago.setRowHeight(30);

        tablaMediosPago.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaMediosPago.setBackground(
                Color.WHITE
        );

        tablaMediosPago.setForeground(
                new Color(30, 30, 30)
        );

        tablaMediosPago.setGridColor(
                new Color(
                        225,
                        230,
                        238
                )
        );

        tablaMediosPago.setSelectionBackground(
                new Color(
                        215,
                        230,
                        250
                )
        );

        tablaMediosPago.setSelectionForeground(
                new Color(
                        20,
                        40,
                        80
                )
        );

        tablaMediosPago
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                36
                        )
                );

        tablaMediosPago
                .getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul();

    }

    //==========================================================
    // HEADER AZUL
    //==========================================================
    private void aplicarHeaderAzul() {

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel label =
                        (JLabel)
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                label.setBackground(
                        AZUL_OSCURO
                );

                label.setForeground(
                        Color.WHITE
                );

                label.setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

                label.setHorizontalAlignment(
                        JLabel.CENTER
                );

                label.setOpaque(true);

                label.setBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                0,
                                1,
                                new Color(
                                        40,
                                        85,
                                        150
                                )
                        )
                );

                return label;

            }
        };

        for (int i = 0;
             i < tablaMediosPago.getColumnCount();
             i++) {

            tablaMediosPago
                    .getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(renderer);

        }

    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(FONDO);

        JPanel contenedor =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        contenedor.setBackground(
                BLANCO
        );

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
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
                new JLabel(
                        "MEDIOS DE PAGO"
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
                        "Configure las formas de pago disponibles para las ventas"
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
                javax.swing.Box
                        .createVerticalStrut(5)
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

        JPanel panelTabla =
                crearPanelTabla();

        panelTabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JPanel panelOpciones =
                crearPanelOpciones();

        panelOpciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        centro.add(panelTabla);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(20)
        );

        centro.add(panelOpciones);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(20)
        );

        //======================================================
        // SCROLL
        //======================================================
        JScrollPane scrollContenido =
                new JScrollPane(
                        centro
                );

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

        scrollContenido
                .getViewport()
                .setBackground(
                        Color.WHITE
                );

        contenedor.add(
                scrollContenido,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES FINALES
        //======================================================
        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        panelBotones.setOpaque(
                false
        );

        panelBotones.add(
                btnRestaurar
        );

        panelBotones.add(
                btnGuardar
        );

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
    // PANEL TABLA
    //==========================================================
    private JPanel crearPanelTabla() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        panel.setOpaque(
                false
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        "Medios habilitados",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaMediosPago
                );

        scroll.setPreferredSize(
                new Dimension(
                        750,
                        220
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES CRUD
        //======================================================
        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                8
                        )
                );

        botones.setOpaque(
                false
        );

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(
                btnActivarDesactivar
        );
        botones.add(btnEliminar);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        return panel;

    }

    //==========================================================
    // OPCIONES GENERALES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(
                false
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        "Configuración General",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

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

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                chkCombinarMedios,
                c
        );

        c.gridy = 1;

        panel.add(
                chkReferenciaTransferencia,
                c
        );

        c.gridy = 2;

        panel.add(
                chkCalcularCambio,
                c
        );

        return panel;

    }

    //==========================================================
    // CREAR BOTON
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

        boton.setFocusPainted(
                false
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

        return boton;

    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloTabla.setRowCount(0);

        modeloTabla.addRow(
                new Object[]{
                    1,
                    "Efectivo",
                    "Activo",
                    "0%",
                    "Sí"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    2,
                    "Tarjeta Débito",
                    "Activo",
                    "0%",
                    "No"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    3,
                    "Tarjeta Crédito",
                    "Activo",
                    "10%",
                    "No"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    4,
                    "Transferencia",
                    "Activo",
                    "0%",
                    "No"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    5,
                    "Mercado Pago",
                    "Activo",
                    "0%",
                    "No"
                }
        );

        chkCombinarMedios.setSelected(
                true
        );

        chkReferenciaTransferencia
                .setSelected(
                        true
                );

        chkCalcularCambio.setSelected(
                true
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnNuevo.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Nuevo medio de pago.",
                            "Medios de Pago",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnEditar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Editar medio de pago.",
                            "Medios de Pago",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnActivarDesactivar
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Activar / Desactivar medio de pago.",
                            "Medios de Pago",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnEliminar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Eliminar medio de pago.",
                            "Medios de Pago",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración de medios de pago guardada.",
                            "Medios de Pago",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

        });

    }

}
