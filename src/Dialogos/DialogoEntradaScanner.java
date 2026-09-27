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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoEntradaScanner extends JDialog {

    //==========================================================
    // DATOS COMPRA
    //==========================================================
    private JComboBox<String> cboProveedor;
    private JTextField txtNumeroFactura;
    private JTextField txtFecha;

    //==========================================================
    // SCANNER
    //==========================================================
    private JTextField txtCodigoBarras;

    private JLabel lblCodigo;
    private JLabel lblProducto;
    private JLabel lblStockActual;
    private JLabel lblCosto;

    private JButton btnAgregarManual;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblCantidadProductos;
    private JLabel lblTotalCompra;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnEliminarItem;
    private JButton btnLimpiar;
    private JButton btnCancelar;
    private JButton btnConfirmar;

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

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // PRODUCTO ACTUAL - POR AHORA DATOS SIMULADOS
    //==========================================================
    private String codigoActual;
    private String productoActual;
    private double costoActual;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoEntradaScanner(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        cargarDatosPrueba();

        configurarEventos();

        setModal(true);

        setSize(
                new Dimension(
                        1050,
                        680
                )
        );

        setMinimumSize(
                new Dimension(
                        950,
                        620
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

        cboProveedor = new JComboBox<>();

        txtNumeroFactura = new JTextField();

        txtFecha = new JTextField();

        txtCodigoBarras = new JTextField();

        txtCodigoBarras.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblCodigo =
                new JLabel("-");

        lblProducto =
                new JLabel(
                        "Esperando producto..."
                );

        lblStockActual =
                new JLabel("0");

        lblCosto =
                new JLabel("$ 0,00");

        lblProducto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblStockActual.setForeground(
                AZUL
        );

        lblCosto.setForeground(
                VERDE
        );

        btnAgregarManual =
                crearBoton(
                        "Agregar",
                        VERDE,
                        120
                );

        btnEliminarItem =
                crearBoton(
                        "Eliminar Item",
                        ROJO,
                        140
                );

        btnLimpiar =
                crearBoton(
                        "Limpiar",
                        GRIS,
                        110
                );

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnConfirmar =
                crearBoton(
                        "Confirmar Entrada",
                        VERDE,
                        180
                );

        lblCantidadProductos =
                new JLabel("0");

        lblCantidadProductos.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblCantidadProductos.setForeground(
                AZUL
        );

        lblTotalCompra =
                new JLabel("$ 0,00");

        lblTotalCompra.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTotalCompra.setForeground(
                VERDE
        );

        inicializarTabla();
    }

    //==========================================================
    // TABLA
    //==========================================================
   private void inicializarTabla() {

    //==========================================================
    // MODELO
    //==========================================================
    modeloTabla = new DefaultTableModel(
            new Object[]{
                "Código",
                "Producto",
                "Cantidad",
                "Costo",
                "Subtotal"
            },
            0
    ) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };


    //==========================================================
    // CREAR TABLA
    //==========================================================
    tablaDetalle = new JTable(modeloTabla);


    //==========================================================
    // CONFIGURACION GENERAL
    //==========================================================
    tablaDetalle.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
    );

    tablaDetalle.setRowHeight(30);

    tablaDetalle.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    13
            )
    );

    tablaDetalle.setBackground(
            Color.WHITE
    );

    tablaDetalle.setForeground(
            new Color(
                    30,
                    30,
                    30
            )
    );

    tablaDetalle.setGridColor(
            new Color(
                    220,
                    226,
                    235
            )
    );

    tablaDetalle.setShowHorizontalLines(true);
    tablaDetalle.setShowVerticalLines(true);

    tablaDetalle.setFillsViewportHeight(true);


    //==========================================================
    // SELECCION
    //==========================================================
    tablaDetalle.setSelectionBackground(
            new Color(
                    205,
                    220,
                    242
            )
    );

    tablaDetalle.setSelectionForeground(
            Color.BLACK
    );


    //==========================================================
    // HEADER
    //==========================================================
    tablaDetalle.getTableHeader().setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    13
            )
    );

    tablaDetalle.getTableHeader().setPreferredSize(
            new Dimension(
                    0,
                    38
            )
    );

    tablaDetalle.getTableHeader().setReorderingAllowed(false);

    tablaDetalle.getTableHeader().setResizingAllowed(true);


    //==========================================================
    // HEADER AZUL FORZADO
    //==========================================================
    DefaultTableCellRenderer headerRenderer =
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
                                    45,
                                    85,
                                    145
                            )
                    )
            );

            return label;
        }
    };


    //==========================================================
    // APLICAR HEADER
    //==========================================================
    for (int i = 0;
         i < tablaDetalle.getColumnCount();
         i++) {

        tablaDetalle
                .getColumnModel()
                .getColumn(i)
                .setHeaderRenderer(
                        headerRenderer
                );
    }


    //==========================================================
    // ANCHO COLUMNAS
    //==========================================================
    tablaDetalle
            .getColumnModel()
            .getColumn(0)
            .setPreferredWidth(130);

    tablaDetalle
            .getColumnModel()
            .getColumn(1)
            .setPreferredWidth(280);

    tablaDetalle
            .getColumnModel()
            .getColumn(2)
            .setPreferredWidth(100);

    tablaDetalle
            .getColumnModel()
            .getColumn(3)
            .setPreferredWidth(130);

    tablaDetalle
            .getColumnModel()
            .getColumn(4)
            .setPreferredWidth(150);
}

    //==========================================================
    // CONSTRUIR DIALOGO
    //==========================================================
    private void construirDialogo() {

        setTitle(
                "Entrada de Mercadería por Código de Barras"
        );

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
                        "ENTRADA DE MERCADERÍA - CÓDIGO DE BARRAS"
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
                        "Escanee los productos recibidos para agregarlos rápidamente"
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

        titulos.add(lblTitulo);

        titulos.add(
                javax.swing.Box
                        .createVerticalStrut(4)
        );

        titulos.add(lblSubtitulo);

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

        JPanel panelCompra =
                crearPanelCompra();

        JPanel panelScanner =
                crearPanelScanner();

        JPanel panelTabla =
                crearPanelTabla();

        panelCompra.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelScanner.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelTabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelCompra.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        100
                )
        );

        panelScanner.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        panelTabla.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        280
                )
        );

        centro.add(panelCompra);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelScanner);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelTabla);

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
    // DATOS COMPRA
    //==========================================================
    private JPanel crearPanelCompra() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Datos de la Compra"
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
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        cboProveedor.setPreferredSize(
                new Dimension(
                        300,
                        32
                )
        );

        panel.add(
                cboProveedor,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel("Factura N°"),
                c
        );

        c.gridx = 3;

        txtNumeroFactura.setPreferredSize(
                new Dimension(
                        170,
                        32
                )
        );

        panel.add(
                txtNumeroFactura,
                c
        );

        c.gridx = 4;

        panel.add(
                crearLabel("Fecha"),
                c
        );

        c.gridx = 5;

        txtFecha.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        panel.add(
                txtFecha,
                c
        );

        return panel;
    }

    //==========================================================
    // SCANNER
    //==========================================================
    private JPanel crearPanelScanner() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Escanear Producto"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // CODIGO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Código de barras"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        txtCodigoBarras.setPreferredSize(
                new Dimension(
                        500,
                        45
                )
        );

        panel.add(
                txtCodigoBarras,
                c
        );

        //======================================================
        // INFO
        //======================================================
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel("Código"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblCodigo,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel("Producto"),
                c
        );

        c.gridx = 3;

        panel.add(
                lblProducto,
                c
        );

        //======================================================
        // STOCK
        //======================================================
        c.gridx = 0;
        c.gridy = 2;

        panel.add(
                crearLabel(
                        "Stock Actual"
                ),
                c
        );

        c.gridx = 1;

        panel.add(
                lblStockActual,
                c
        );

        //======================================================
        // COSTO
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Costo Unitario"
                ),
                c
        );

        c.gridx = 3;

        panel.add(
                lblCosto,
                c
        );

        //======================================================
        // AGREGAR
        //======================================================
        c.gridx = 4;

        panel.add(
                btnAgregarManual,
                c
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
                        "Productos Escaneados"
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaDetalle
                );

        scroll.setPreferredSize(
                new Dimension(
                        900,
                        180
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
                btnEliminarItem
        );

        acciones.add(
                btnLimpiar
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

        //======================================================
        // RESUMEN
        //======================================================
        JPanel resumen =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                15,
                                0
                        )
                );

        resumen.setOpaque(false);

        JLabel lblProductos =
                crearLabel(
                        "UNIDADES:"
                );

        JLabel lblTotal =
                crearLabel(
                        "TOTAL:"
                );

        resumen.add(
                lblProductos
        );

        resumen.add(
                lblCantidadProductos
        );

        resumen.add(
                javax.swing.Box
                        .createHorizontalStrut(20)
        );

        resumen.add(
                lblTotal
        );

        resumen.add(
                lblTotalCompra
        );

        footer.add(
                resumen,
                BorderLayout.WEST
        );

        //======================================================
        // BOTONES
        //======================================================
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
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        cboProveedor.removeAllItems();

        cboProveedor.addItem(
                "Distribuidora Norte"
        );

        cboProveedor.addItem(
                "Coca Cola FEMSA"
        );

        cboProveedor.addItem(
                "Mayorista Central"
        );

        txtFecha.setText(
                "21/08/2026"
        );

        limpiarProductoActual();

        lblCantidadProductos.setText(
                "0"
        );

        lblTotalCompra.setText(
                "$ 0,00"
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // ENTER DEL SCANNER
        //======================================================
        txtCodigoBarras
                .addActionListener(e -> {

            procesarCodigoEscaneado();

        });

        //======================================================
        // AGREGAR MANUALMENTE
        //======================================================
        btnAgregarManual
                .addActionListener(e -> {

            if (codigoActual != null) {

                agregarProductoTabla(
                        codigoActual,
                        productoActual,
                        costoActual
                );

                prepararSiguienteEscaneo();
            }

        });

        //======================================================
        // ELIMINAR
        //======================================================
        btnEliminarItem
                .addActionListener(e -> {

            int fila =
                    tablaDetalle
                            .getSelectedRow();

            if (fila != -1) {

                modeloTabla.removeRow(
                        fila
                );

                actualizarResumen();
            }

        });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar
                .addActionListener(e -> {

            modeloTabla.setRowCount(0);

            actualizarResumen();

            prepararSiguienteEscaneo();

        });

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar
                .addActionListener(e -> {

            dispose();

        });

        //======================================================
        // CONFIRMAR
        //======================================================
        btnConfirmar
                .addActionListener(e -> {

            if (modeloTabla.getRowCount() == 0) {

                javax.swing.JOptionPane
                        .showMessageDialog(
                                this,
                                "Debe escanear al menos un producto.",
                                "Entrada",
                                javax.swing.JOptionPane
                                        .WARNING_MESSAGE
                        );

                return;
            }

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Entrada preparada para registrar.",
                            "Entrada de Mercadería",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        //======================================================
        // FOCO INICIAL
        //======================================================
        javax.swing.SwingUtilities
                .invokeLater(() -> {

            txtCodigoBarras
                    .requestFocusInWindow();

        });
    }

    //==========================================================
    // PROCESAR CODIGO
    //==========================================================
    private void procesarCodigoEscaneado() {

        String codigo =
                txtCodigoBarras
                        .getText()
                        .trim();

        if (codigo.isEmpty()) {

            return;
        }

        /*
         * Más adelante reemplazamos esto por:
         *
         * Producto producto =
         * productoDao.buscarPorCodigoBarras(codigo);
         */

        //======================================================
        // DATOS SIMULADOS
        //======================================================
        if (codigo.equals("779001")) {

            codigoActual = codigo;
            productoActual =
                    "Coca Cola 2.25L";
            costoActual = 2300;

            lblStockActual.setText(
                    "120"
            );

        } else if (codigo.equals("779002")) {

            codigoActual = codigo;
            productoActual =
                    "Fanta 2.25L";
            costoActual = 2100;

            lblStockActual.setText(
                    "8"
            );

        } else if (codigo.equals("779003")) {

            codigoActual = codigo;
            productoActual =
                    "Sprite 2.25L";
            costoActual = 2050;

            lblStockActual.setText(
                    "0"
            );

        } else {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Producto no encontrado.\nCódigo: "
                            + codigo,
                            "Código no registrado",
                            javax.swing.JOptionPane
                                    .WARNING_MESSAGE
                    );

            prepararSiguienteEscaneo();

            return;
        }

        //======================================================
        // MOSTRAR PRODUCTO
        //======================================================
        lblCodigo.setText(
                codigoActual
        );

        lblProducto.setText(
                productoActual
        );

        lblCosto.setText(
                String.format(
                        "$ %,.2f",
                        costoActual
                )
        );

        /*
         * Como un scanner normalmente manda ENTER después
         * del código, directamente agregamos una unidad.
         */
        agregarProductoTabla(
                codigoActual,
                productoActual,
                costoActual
        );

        prepararSiguienteEscaneo();
    }

    //==========================================================
    // AGREGAR A TABLA
    //==========================================================
    private void agregarProductoTabla(
            String codigo,
            String producto,
            double costo) {

        /*
         * Primero verificamos si el producto ya está en la tabla.
         * Si existe, aumentamos cantidad en vez de crear otra fila.
         */
        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            String codigoTabla =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    0
                            )
                            .toString();

            if (codigo.equals(codigoTabla)) {

                double cantidadActual =
                        ((Number)
                                modeloTabla
                                        .getValueAt(
                                                i,
                                                2
                                        ))
                                .doubleValue();

                cantidadActual++;

                modeloTabla.setValueAt(
                        cantidadActual,
                        i,
                        2
                );

                modeloTabla.setValueAt(
                        cantidadActual * costo,
                        i,
                        4
                );

                actualizarResumen();

                return;
            }
        }

        //======================================================
        // NUEVO PRODUCTO
        //======================================================
        modeloTabla.addRow(
                new Object[]{
                    codigo,
                    producto,
                    1.0,
                    costo,
                    costo
                }
        );

        actualizarResumen();
    }

    //==========================================================
    // ACTUALIZAR RESUMEN
    //==========================================================
    private void actualizarResumen() {

        double cantidadTotal = 0;
        double totalCompra = 0;

        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            cantidadTotal +=
                    ((Number)
                            modeloTabla
                                    .getValueAt(
                                            i,
                                            2
                                    ))
                            .doubleValue();

            totalCompra +=
                    ((Number)
                            modeloTabla
                                    .getValueAt(
                                            i,
                                            4
                                    ))
                            .doubleValue();
        }

        lblCantidadProductos.setText(
                String.format(
                        "%.0f",
                        cantidadTotal
                )
        );

        lblTotalCompra.setText(
                String.format(
                        "$ %,.2f",
                        totalCompra
                )
        );
    }

    //==========================================================
    // PREPARAR SIGUIENTE SCAN
    //==========================================================
    private void prepararSiguienteEscaneo() {

        txtCodigoBarras.setText("");

        txtCodigoBarras.requestFocusInWindow();
    }

    //==========================================================
    // LIMPIAR PRODUCTO
    //==========================================================
    private void limpiarProductoActual() {

        codigoActual = null;
        productoActual = null;
        costoActual = 0;

        lblCodigo.setText("-");
        lblProducto.setText(
                "Esperando producto..."
        );

        lblStockActual.setText("0");
        lblCosto.setText("$ 0,00");
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
}
