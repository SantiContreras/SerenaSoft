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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelConfigBackup extends JPanel {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtRutaBackup;

    private JButton btnSeleccionarRuta;
    private JButton btnCrearBackup;
    private JButton btnAbrirCarpeta;
    private JButton btnRestaurarBackup;
    private JButton btnEliminarBackup;
    private JButton btnGuardar;

    private JCheckBox chkBackupAutomatico;
    private JCheckBox chkIncluirConfiguracion;

    private JSpinner spnCantidadBackups;

    private JLabel lblUltimoBackup;
    private JLabel lblEstadoBackup;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaBackups;
    private DefaultTableModel modeloTabla;

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
    public PanelConfigBackup() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        txtRutaBackup = new JTextField();

        btnSeleccionarRuta =
                crearBoton(
                        "Seleccionar",
                        AZUL,
                        120
                );

        btnCrearBackup =
                crearBoton(
                        "Crear Backup Ahora",
                        VERDE,
                        170
                );

        btnAbrirCarpeta =
                crearBoton(
                        "Abrir Carpeta",
                        AZUL,
                        140
                );

        btnRestaurarBackup =
                crearBoton(
                        "Restaurar Backup",
                        NARANJA,
                        160
                );

        btnEliminarBackup =
                crearBoton(
                        "Eliminar Backup",
                        ROJO,
                        150
                );

        btnGuardar =
                crearBoton(
                        "Guardar Configuración",
                        VERDE,
                        190
                );

        chkBackupAutomatico =
                new JCheckBox(
                        "Crear una copia automática al cerrar el sistema"
                );

        chkIncluirConfiguracion =
                new JCheckBox(
                        "Incluir configuración del sistema en el respaldo"
                );

        chkBackupAutomatico.setOpaque(false);
        chkIncluirConfiguracion.setOpaque(false);

        chkBackupAutomatico.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        chkIncluirConfiguracion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        spnCantidadBackups =
                new JSpinner(
                        new SpinnerNumberModel(
                                10,
                                1,
                                100,
                                1
                        )
                );

        lblUltimoBackup =
                new JLabel(
                        "20/08/2026 10:05"
                );

        lblEstadoBackup =
                new JLabel(
                        "● Correcto"
                );

        lblEstadoBackup.setForeground(
                VERDE
        );

        lblEstadoBackup.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        inicializarTabla();

    }

    //==========================================================
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "Fecha",
                            "Archivo",
                            "Tamaño",
                            "Estado"
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

        tablaBackups =
                new JTable(modeloTabla);

        tablaBackups.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaBackups.setRowHeight(30);

        tablaBackups.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaBackups.setBackground(
                Color.WHITE
        );

        tablaBackups.setGridColor(
                new Color(
                        225,
                        230,
                        238
                )
        );

        tablaBackups.setSelectionBackground(
                new Color(
                        215,
                        230,
                        250
                )
        );

        tablaBackups.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                36
                        )
                );

        tablaBackups.getTableHeader()
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
                                super
                                        .getTableCellRendererComponent(
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

                return label;
            }
        };

        for (int i = 0;
             i < tablaBackups.getColumnCount();
             i++) {

            tablaBackups
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
                        "COPIAS DE SEGURIDAD"
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
                        "Administración de respaldos y restauración de datos"
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

        JPanel panelEstado =
                crearPanelEstado();

        JPanel panelUbicacion =
                crearPanelUbicacion();

        JPanel panelOpciones =
                crearPanelOpciones();

        JPanel panelHistorial =
                crearPanelHistorial();

        panelEstado.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelUbicacion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelOpciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelHistorial.setAlignmentX(
                LEFT_ALIGNMENT
        );

        centro.add(panelEstado);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelUbicacion);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelOpciones);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelHistorial);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
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
                .setBackground(Color.WHITE);

        contenedor.add(
                scrollContenido,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTON GUARDAR
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
    // ESTADO
    //==========================================================
    private JPanel crearPanelEstado() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Estado del Respaldo"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Último respaldo"
                ),
                c
        );

        c.gridx = 1;

        panel.add(
                lblUltimoBackup,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Estado"
                ),
                c
        );

        c.gridx = 3;

        panel.add(
                lblEstadoBackup,
                c
        );

        return panel;

    }

    //==========================================================
    // UBICACION
    //==========================================================
    private JPanel crearPanelUbicacion() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Ubicación de los Backups"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Carpeta"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        txtRutaBackup.setPreferredSize(
                new Dimension(
                        400,
                        32
                )
        );

        panel.add(
                txtRutaBackup,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                btnSeleccionarRuta,
                c
        );

        return panel;

    }

    //==========================================================
    // OPCIONES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Opciones de Respaldo"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;

        panel.add(
                chkBackupAutomatico,
                c
        );

        c.gridy = 1;

        panel.add(
                chkIncluirConfiguracion,
                c
        );

        c.gridy = 2;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Conservar últimas"
                ),
                c
        );

        c.gridx = 1;

        spnCantidadBackups.setPreferredSize(
                new Dimension(
                        80,
                        32
                )
        );

        panel.add(
                spnCantidadBackups,
                c
        );

        c.gridx = 2;

        panel.add(
                new JLabel(
                        "copias"
                ),
                c
        );

        return panel;

    }

    //==========================================================
    // HISTORIAL
    //==========================================================
    private JPanel crearPanelHistorial() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Historial de Backups"
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaBackups
                );

        scroll.setPreferredSize(
                new Dimension(
                        750,
                        170
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                5
                        )
                );

        botones.setOpaque(false);

        botones.add(
                btnCrearBackup
        );

        botones.add(
                btnAbrirCarpeta
        );

        botones.add(
                btnRestaurarBackup
        );

        botones.add(
                btnEliminarBackup
        );

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        return panel;

    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        txtRutaBackup.setText(
                "C:\\SerenaSoft\\Backups"
        );

        chkBackupAutomatico
                .setSelected(true);

        chkIncluirConfiguracion
                .setSelected(true);

        spnCantidadBackups
                .setValue(10);

        modeloTabla.setRowCount(0);

        modeloTabla.addRow(
                new Object[]{
                    "20/08/2026 10:05",
                    "serena_20260820_1005.sql",
                    "3.8 MB",
                    "Correcto"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "19/08/2026 18:20",
                    "serena_20260819_1820.sql",
                    "3.7 MB",
                    "Correcto"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "18/08/2026 19:00",
                    "serena_20260818_1900.sql",
                    "3.6 MB",
                    "Correcto"
                }
        );

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnSeleccionarRuta
                .addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            chooser.setFileSelectionMode(
                    JFileChooser.DIRECTORIES_ONLY
            );

            int resultado =
                    chooser.showOpenDialog(
                            this
                    );

            if (resultado ==
                    JFileChooser.APPROVE_OPTION) {

                txtRutaBackup.setText(
                        chooser
                                .getSelectedFile()
                                .getAbsolutePath()
                );
            }

        });

        btnCrearBackup
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Crear copia de seguridad.",
                            "Backup",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnAbrirCarpeta
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Abrir carpeta de respaldos.",
                            "Backup",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        btnRestaurarBackup
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Restaurar copia seleccionada.",
                            "Backup",
                            javax.swing.JOptionPane
                                    .WARNING_MESSAGE
                    );

        });

        btnEliminarBackup
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Eliminar copia seleccionada.",
                            "Backup",
                            javax.swing.JOptionPane
                                    .WARNING_MESSAGE
                    );

        });

        btnGuardar
                .addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración de respaldo guardada.",
                            "Backup",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

    }

    //==========================================================
    // BORDER
    //==========================================================
    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory
                .createTitledBorder(
                        BorderFactory
                                .createLineBorder(
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
