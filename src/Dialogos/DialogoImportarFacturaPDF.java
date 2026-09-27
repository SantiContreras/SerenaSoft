package Dialogos;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoImportarFacturaPDF extends JDialog {

    //==========================================================
    // ARCHIVO
    //==========================================================
    private JTextField txtRutaPDF;

    private JButton btnSeleccionarPDF;
    private JButton btnProcesarPDF;

    //==========================================================
    // DATOS DETECTADOS
    //==========================================================
    private JLabel lblProveedor;
    private JLabel lblCuit;
    private JLabel lblNumeroFactura;
    private JLabel lblFecha;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblProductosDetectados;
    private JLabel lblProductosVinculados;
    private JLabel lblProductosPendientes;

    private JLabel lblTotalFactura;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnAsociarProducto;
    private JButton btnEliminarFila;

    private JButton btnCancelar;
    private JButton btnConfirmar;

    //==========================================================
    // ARCHIVO ACTUAL
    //==========================================================
    private File archivoPDF;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color AZUL =
            new Color(25, 70, 145);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color ROJO =
            new Color(200, 55, 55);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color NARANJA =
            new Color(235, 145, 20);

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoImportarFacturaPDF(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        configurarEventos();

        limpiarDatos();

        setModal(true);

        setSize(
                new Dimension(
                        1150,
                        720
                )
        );

        setMinimumSize(
                new Dimension(
                        1000,
                        650
                )
        );

        setLocationRelativeTo(parent);
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setResizable(true);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        txtRutaPDF =
                new JTextField();

        txtRutaPDF.setEditable(false);

        btnSeleccionarPDF =
                crearBoton(
                        "Seleccionar PDF",
                        AZUL,
                        150
                );

        btnProcesarPDF =
                crearBoton(
                        "Procesar PDF",
                        NARANJA,
                        140
                );

        lblProveedor =
                new JLabel("-");

        lblCuit =
                new JLabel("-");

        lblNumeroFactura =
                new JLabel("-");

        lblFecha =
                new JLabel("-");

        lblProductosDetectados =
                crearValorResumen(
                        "0",
                        AZUL
                );

        lblProductosVinculados =
                crearValorResumen(
                        "0",
                        VERDE
                );

        lblProductosPendientes =
                crearValorResumen(
                        "0",
                        ROJO
                );

        lblTotalFactura =
                new JLabel("$ 0,00");

        lblTotalFactura.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTotalFactura.setForeground(
                VERDE
        );

        btnAsociarProducto =
                crearBoton(
                        "Asociar Producto",
                        AZUL,
                        150
                );

        btnEliminarFila =
                crearBoton(
                        "Eliminar Fila",
                        ROJO,
                        130
                );

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnConfirmar =
                crearBoton(
                        "Confirmar Importación",
                        VERDE,
                        190
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
                            "Código",
                            "Descripción PDF",
                            "Producto del Sistema",
                            "Cantidad",
                            "Costo",
                            "Subtotal",
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

        tablaProductos =
                new JTable(modeloTabla);

        tablaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaProductos.setRowHeight(30);

        tablaProductos.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaProductos.setBackground(
                Color.WHITE
        );

        tablaProductos.setForeground(
                new Color(30, 30, 30)
        );

        tablaProductos.setGridColor(
                new Color(
                        220,
                        226,
                        235
                )
        );

        tablaProductos.setShowHorizontalLines(true);
        tablaProductos.setShowVerticalLines(true);

        tablaProductos.setFillsViewportHeight(true);

        tablaProductos.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaProductos.setSelectionForeground(
                Color.BLACK
        );

        //======================================================
        // HEADER
        //======================================================
        tablaProductos
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaProductos
                .getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul();

        //======================================================
        // ANCHOS
        //======================================================
        tablaProductos
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(90);

        tablaProductos
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        tablaProductos
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(220);

        tablaProductos
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(80);

        tablaProductos
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(100);

        tablaProductos
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        tablaProductos
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(100);
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

                label.setBorder(
                        BorderFactory
                                .createMatteBorder(
                                        0,
                                        0,
                                        0,
                                        1,
                                        new Color(
                                                45,
                                                85,
                                                145
                                        )
                                )
                );

                return label;
            }
        };

        for (int i = 0;
             i < tablaProductos.getColumnCount();
             i++) {

            tablaProductos
                    .getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(
                            renderer
                    );
        }
    }

    //==========================================================
    // CONSTRUIR
    //==========================================================
    private void construirDialogo() {

        setTitle(
                "Importar Factura PDF"
        );

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
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                Color.WHITE
        );

        header.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        JPanel titulos =
                new JPanel();

        titulos.setOpaque(false);

        titulos.setLayout(
                new javax.swing.BoxLayout(
                        titulos,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "IMPORTAR FACTURA PDF"
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
                        "Seleccione una factura de proveedor y revise los datos detectados"
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

        titulos.add(
                lblTitulo
        );

        titulos.add(
                javax.swing.Box
                        .createVerticalStrut(4)
        );

        titulos.add(
                lblSubtitulo
        );

        header.add(
                titulos,
                BorderLayout.WEST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel centro =
                new JPanel();

        centro.setBackground(
                FONDO
        );

        centro.setLayout(
                new javax.swing.BoxLayout(
                        centro,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JPanel panelArchivo =
                crearPanelArchivo();

        JPanel panelDatos =
                crearPanelDatos();

        JPanel panelResumen =
                crearPanelResumen();

        JPanel panelTabla =
                crearPanelTabla();

        panelArchivo.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelDatos.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelResumen.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelTabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelArchivo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        100
                )
        );

        panelDatos.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        120
                )
        );

        panelResumen.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        90
                )
        );

        panelTabla.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        300
                )
        );

        centro.add(panelArchivo);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelDatos);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelResumen);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelTabla);

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scrollContenido =
                new JScrollPane(
                        centro
                );

        scrollContenido.setBorder(null);

        scrollContenido
                .setHorizontalScrollBarPolicy(
                        JScrollPane
                                .HORIZONTAL_SCROLLBAR_NEVER
                );

        scrollContenido
                .setVerticalScrollBarPolicy(
                        JScrollPane
                                .VERTICAL_SCROLLBAR_AS_NEEDED
                );

        scrollContenido
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollContenido,
                BorderLayout.CENTER
        );

        add(
                crearFooter(),
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // ARCHIVO
    //==========================================================
    private JPanel crearPanelArchivo() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Factura PDF"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Archivo"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        txtRutaPDF.setPreferredSize(
                new Dimension(
                        500,
                        32
                )
        );

        panel.add(
                txtRutaPDF,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                btnSeleccionarPDF,
                c
        );

        c.gridx = 3;

        panel.add(
                btnProcesarPDF,
                c
        );

        return panel;
    }

    //==========================================================
    // DATOS DETECTADOS
    //==========================================================
    private JPanel crearPanelDatos() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Datos Detectados"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Proveedor"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblProveedor,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel("CUIT"),
                c
        );

        c.gridx = 3;

        panel.add(
                lblCuit,
                c
        );

        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel("Factura"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblNumeroFactura,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel("Fecha"),
                c
        );

        c.gridx = 3;

        panel.add(
                lblFecha,
                c
        );

        return panel;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel =
                new JPanel(
                        new java.awt.GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearTarjetaResumen(
                        "Detectados",
                        lblProductosDetectados
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Vinculados",
                        lblProductosVinculados
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Pendientes",
                        lblProductosPendientes
                )
        );

        return panel;
    }

    private JPanel crearTarjetaResumen(
            String titulo,
            JLabel valor) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        titulo
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                valor,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // TABLA
    //==========================================================
    private JPanel crearPanelTabla() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Productos Detectados"
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaProductos
                );

        scroll.setPreferredSize(
                new Dimension(
                        950,
                        200
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel acciones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                5
                        )
                );

        acciones.setOpaque(false);

        acciones.add(
                btnAsociarProducto
        );

        acciones.add(
                btnEliminarFila
        );

        panel.add(
                acciones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    //==========================================================
    // FOOTER
    //==========================================================
    private JPanel crearFooter() {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                BORDE
                        ),
                        new EmptyBorder(
                                12,
                                20,
                                12,
                                20
                        )
                )
        );

        JPanel total =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        total.setOpaque(false);

        JLabel lblTotal =
                crearLabel(
                        "TOTAL FACTURA:"
                );

        total.add(lblTotal);

        total.add(
                lblTotalFactura
        );

        footer.add(
                total,
                BorderLayout.WEST
        );

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        botones.setOpaque(false);

        botones.add(
                btnCancelar
        );

        botones.add(
                btnConfirmar
        );

        footer.add(
                botones,
                BorderLayout.EAST
        );

        return footer;
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // SELECCIONAR PDF
        //======================================================
        btnSeleccionarPDF.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            chooser.setDialogTitle(
                    "Seleccionar factura PDF"
            );

            chooser.setFileFilter(
                    new FileNameExtensionFilter(
                            "Archivos PDF",
                            "pdf"
                    )
            );

            int resultado =
                    chooser.showOpenDialog(
                            this
                    );

            if (resultado ==
                    JFileChooser.APPROVE_OPTION) {

                archivoPDF =
                        chooser
                                .getSelectedFile();

                txtRutaPDF.setText(
                        archivoPDF
                                .getAbsolutePath()
                );

                limpiarResultadoProcesamiento();
            }

        });

        //======================================================
        // PROCESAR
        //======================================================
        btnProcesarPDF.addActionListener(e -> {

            if (archivoPDF == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Primero debe seleccionar una factura PDF.",
                        "Importar PDF",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            procesarPDFSimulado();

        });

        //======================================================
        // ASOCIAR PRODUCTO
        //======================================================
        btnAsociarProducto.addActionListener(e -> {

            int fila =
                    tablaProductos
                            .getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un producto pendiente.",
                        "Asociar Producto",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            /*
             * Después abriremos un diálogo real para buscar
             * el producto correspondiente.
             */

            modeloTabla.setValueAt(
                    "Producto asociado manualmente",
                    fila,
                    2
            );

            modeloTabla.setValueAt(
                    "OK",
                    fila,
                    6
            );

            actualizarResumen();

        });

        //======================================================
        // ELIMINAR FILA
        //======================================================
        btnEliminarFila.addActionListener(e -> {

            int fila =
                    tablaProductos
                            .getSelectedRow();

            if (fila != -1) {

                modeloTabla.removeRow(
                        fila
                );

                actualizarResumen();
                actualizarTotal();
            }

        });

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar.addActionListener(e -> {

            dispose();

        });

        //======================================================
        // CONFIRMAR
        //======================================================
        btnConfirmar.addActionListener(e -> {

            if (modeloTabla.getRowCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay productos para importar.",
                        "Factura PDF",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int pendientes =
                    contarPendientes();

            if (pendientes > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Hay "
                        + pendientes
                        + " producto(s) sin asociar.\n"
                        + "Debe revisarlos antes de confirmar.",
                        "Productos Pendientes",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Factura preparada para registrar.\n"
                    + "La importación real se conectará luego con la base de datos.",
                    "Factura PDF",
                    JOptionPane.INFORMATION_MESSAGE
            );

        });

    }

    //==========================================================
    // SIMULAR PROCESAMIENTO
    //==========================================================
    private void procesarPDFSimulado() {

        modeloTabla.setRowCount(0);

        lblProveedor.setText(
                "Distribuidora Norte"
        );

        lblCuit.setText(
                "30-12345678-9"
        );

        lblNumeroFactura.setText(
                "0004-00012345"
        );

        lblFecha.setText(
                "21/08/2026"
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0001",
                    "Coca Cola 2.25",
                    "Coca Cola 2.25L",
                    12.0,
                    2300.0,
                    27600.0,
                    "OK"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0002",
                    "Fanta 2.25",
                    "Fanta 2.25L",
                    8.0,
                    2100.0,
                    16800.0,
                    "OK"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0003",
                    "Sprite 2.25",
                    "Sprite 2.25L",
                    10.0,
                    2050.0,
                    20500.0,
                    "OK"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "-",
                    "GALLET CHOC X 300",
                    "Sin asociar",
                    5.0,
                    1800.0,
                    9000.0,
                    "REVISAR"
                }
        );

        actualizarResumen();
        actualizarTotal();
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private void actualizarResumen() {

        int detectados =
                modeloTabla.getRowCount();

        int vinculados = 0;
        int pendientes = 0;

        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            String estado =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    6
                            )
                            .toString();

            if ("OK".equalsIgnoreCase(
                    estado
            )) {

                vinculados++;

            } else {

                pendientes++;
            }
        }

        lblProductosDetectados.setText(
                String.valueOf(
                        detectados
                )
        );

        lblProductosVinculados.setText(
                String.valueOf(
                        vinculados
                )
        );

        lblProductosPendientes.setText(
                String.valueOf(
                        pendientes
                )
        );
    }

    //==========================================================
    // TOTAL
    //==========================================================
    private void actualizarTotal() {

        double total = 0;

        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            Object valor =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    5
                            );

            if (valor instanceof Number) {

                total +=
                        ((Number) valor)
                                .doubleValue();
            }
        }

        lblTotalFactura.setText(
                String.format(
                        "$ %,.2f",
                        total
                )
        );
    }

    //==========================================================
    // CONTAR PENDIENTES
    //==========================================================
    private int contarPendientes() {

        int pendientes = 0;

        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            String estado =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    6
                            )
                            .toString();

            if (!"OK".equalsIgnoreCase(
                    estado
            )) {

                pendientes++;
            }
        }

        return pendientes;
    }

    //==========================================================
    // LIMPIAR
    //==========================================================
    private void limpiarDatos() {

        txtRutaPDF.setText("");

        archivoPDF = null;

        limpiarResultadoProcesamiento();
    }

    private void limpiarResultadoProcesamiento() {

        lblProveedor.setText("-");
        lblCuit.setText("-");
        lblNumeroFactura.setText("-");
        lblFecha.setText("-");

        modeloTabla.setRowCount(0);

        lblProductosDetectados.setText("0");
        lblProductosVinculados.setText("0");
        lblProductosPendientes.setText("0");

        lblTotalFactura.setText(
                "$ 0,00"
        );
    }

    //==========================================================
    // RESUMEN LABEL
    //==========================================================
    private JLabel crearValorResumen(
            String texto,
            Color color) {

        JLabel label =
                new JLabel(texto);

        label.setForeground(
                color
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        return label;
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
