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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoEntradaManual extends JDialog {

    //==========================================================
    // DATOS DE LA COMPRA
    //==========================================================
    private JTextField txtProveedor;
    private JButton btnBuscarProveedor;

    private int idProveedorSeleccionado = -1;
    private JTextField txtNumeroFactura;
    private JTextField txtFecha;

    //==========================================================
    // PRODUCTO
    //==========================================================
    private JTextField txtBuscarProducto;
    private JButton btnBuscarProducto;

    private JLabel lblCodigoProducto;
    private JLabel lblProductoSeleccionado;
    private JLabel lblStockActual;

    private JSpinner spnCantidad;
    private JTextField txtCosto;

    private JLabel lblSubtotalItem;

    private JButton btnAgregarProducto;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // ACCIONES
    //==========================================================
    private JButton btnEliminarItem;
    private JButton btnLimpiar;

    private JButton btnCancelar;
    private JButton btnConfirmar;

    private JLabel lblTotalCompra;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color AZUL
            = new Color(25, 70, 145);

    private final Color VERDE
            = new Color(25, 135, 84);

    private final Color ROJO
            = new Color(200, 55, 55);

    private final Color GRIS
            = new Color(110, 120, 135);

    private final Color NARANJA
            = new Color(235, 145, 20);

    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoEntradaManual(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        cargarDatosPrueba();

        configurarEventos();
        
         EstiloBotones.corregirBotones(
            getContentPane()
    );

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

        setResizable(true);
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        txtProveedor
                = new JTextField();

        txtProveedor.setEditable(false);

        btnBuscarProveedor
                = crearBoton(
                        "Buscar",
                        AZUL,
                        100
                );

        txtNumeroFactura = new JTextField();

        txtFecha = new JTextField();

        txtBuscarProducto = new JTextField();

        btnBuscarProducto
                = crearBoton(
                        "Buscar",
                        AZUL,
                        100
                );

        lblCodigoProducto
                = new JLabel("-");

        lblProductoSeleccionado
                = new JLabel("Seleccione un producto");

        lblStockActual
                = new JLabel("0");

        spnCantidad
                = new JSpinner(
                        new SpinnerNumberModel(
                                1.0,
                                0.001,
                                999999.0,
                                1.0
                        )
                );

        txtCosto
                = new JTextField();

        lblSubtotalItem
                = new JLabel("$ 0,00");

        lblSubtotalItem.setForeground(
                VERDE
        );

        lblSubtotalItem.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        btnAgregarProducto
                = crearBoton(
                        "Agregar Producto",
                        VERDE,
                        160
                );

        btnEliminarItem
                = crearBoton(
                        "Eliminar Item",
                        ROJO,
                        140
                );

        btnLimpiar
                = crearBoton(
                        "Limpiar",
                        GRIS,
                        110
                );

        btnCancelar
                = crearBoton(
                        "Cancelar",
                        GRIS,
                        130
                );

        btnConfirmar
                = crearBoton(
                        "Confirmar Entrada",
                        VERDE,
                        180
                );

        lblTotalCompra
                = new JLabel("$ 0,00");

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
                new Color(30, 30, 30)
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
        // FORZAR HEADER AZUL
        //==========================================================
        DefaultTableCellRenderer headerRenderer
                = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel label
                        = (JLabel) super.getTableCellRendererComponent(
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
        // APLICAR RENDERER A TODAS LAS COLUMNAS
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
        // ANCHOS
        //==========================================================
        tablaDetalle
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(110);

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
                "Entrada Manual de Mercadería"
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
        JPanel header
                = new JPanel(
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

        JPanel titulos
                = new JPanel();

        titulos.setOpaque(false);

        titulos.setLayout(
                new javax.swing.BoxLayout(
                        titulos,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        "NUEVA ENTRADA - CARGA MANUAL"
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

        JLabel lblSubtitulo
                = new JLabel(
                        "Registre una compra o ingreso de mercadería"
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
        // CONTENIDO
        //======================================================
        JPanel centro
                = new JPanel();

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

        JPanel panelCompra
                = crearPanelDatosCompra();

        JPanel panelProducto
                = crearPanelProducto();

        JPanel panelTabla
                = crearPanelTabla();

        panelCompra.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelProducto.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelTabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelCompra.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        panelProducto.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        panelTabla.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        270
                )
        );

        centro.add(panelCompra);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelProducto);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(12)
        );

        centro.add(panelTabla);

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scrollContenido
                = new JScrollPane(
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

        //======================================================
        // FOOTER
        //======================================================
        add(
                crearFooter(),
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // DATOS DE COMPRA
    //==========================================================
   //==========================================================
// DATOS DE COMPRA
//==========================================================
private JPanel crearPanelDatosCompra() {

    JPanel panel =
            new JPanel(
                    new GridBagLayout()
            );

    panel.setBackground(
            Color.WHITE
    );

    panel.setBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(
                            BORDE
                    ),
                    "Datos de la Compra",
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
            crearConstraints();


    //======================================================
    // PROVEEDOR
    //======================================================
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;

    panel.add(
            crearLabel("Proveedor"),
            c
    );


    //======================================================
    // CAMPO PROVEEDOR
    //======================================================
    c.gridx = 1;
    c.weightx = 1;
    c.fill = GridBagConstraints.HORIZONTAL;

    txtProveedor.setPreferredSize(
            new Dimension(
                    280,
                    32
            )
    );

    panel.add(
            txtProveedor,
            c
    );


    //======================================================
    // BUSCAR PROVEEDOR
    //======================================================
    c.gridx = 2;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;

    panel.add(
            btnBuscarProveedor,
            c
    );


    //======================================================
    // FACTURA
    //======================================================
    c.gridx = 3;

    panel.add(
            crearLabel("Factura N°"),
            c
    );


    c.gridx = 4;

    txtNumeroFactura.setPreferredSize(
            new Dimension(
                    160,
                    32
            )
    );

    panel.add(
            txtNumeroFactura,
            c
    );


    //======================================================
    // FECHA
    //======================================================
    c.gridx = 5;

    panel.add(
            crearLabel("Fecha"),
            c
    );


    c.gridx = 6;

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
    // PRODUCTO
    //==========================================================
    private JPanel crearPanelProducto() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        "Agregar Producto",
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

        GridBagConstraints c
                = crearConstraints();

        //======================================================
        // BUSCAR
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Producto / Código"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        txtBuscarProducto.setPreferredSize(
                new Dimension(
                        450,
                        32
                )
        );

        panel.add(
                txtBuscarProducto,
                c
        );

        c.gridx = 4;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill
                = GridBagConstraints.NONE;

        panel.add(
                btnBuscarProducto,
                c
        );

        //======================================================
        // CODIGO
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel("Código"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblCodigoProducto,
                c
        );

        //======================================================
        // PRODUCTO
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel("Producto"),
                c
        );

        c.gridx = 3;

        lblProductoSeleccionado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                lblProductoSeleccionado,
                c
        );

        //======================================================
        // STOCK
        //======================================================
        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Stock actual"
                ),
                c
        );

        c.gridx = 5;

        lblStockActual.setForeground(
                AZUL
        );

        lblStockActual.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        panel.add(
                lblStockActual,
                c
        );

        //======================================================
        // CANTIDAD
        //======================================================
        c.gridx = 0;
        c.gridy = 2;

        panel.add(
                crearLabel(
                        "Cantidad"
                ),
                c
        );

        c.gridx = 1;

        spnCantidad.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        panel.add(
                spnCantidad,
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

        txtCosto.setPreferredSize(
                new Dimension(
                        140,
                        32
                )
        );

        panel.add(
                txtCosto,
                c
        );

        //======================================================
        // SUBTOTAL
        //======================================================
        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Subtotal"
                ),
                c
        );

        c.gridx = 5;

        panel.add(
                lblSubtotalItem,
                c
        );

        //======================================================
        // AGREGAR
        //======================================================
        c.gridx = 5;
        c.gridy = 3;

        panel.add(
                btnAgregarProducto,
                c
        );

        return panel;
    }

    //==========================================================
    // TABLA DETALLE
    //==========================================================
    private JPanel crearPanelTabla() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        "Detalle de la Entrada",
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

        JScrollPane scroll
                = new JScrollPane(
                        tablaDetalle
                );

        scroll.setPreferredSize(
                new Dimension(
                        900,
                        170
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel acciones
                = new JPanel(
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

        JPanel footer
                = new JPanel(
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
        // TOTAL
        //======================================================
        JPanel total
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        total.setOpaque(false);

        JLabel lblTotal
                = new JLabel(
                        "TOTAL COMPRA:"
                );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblTotal.setForeground(
                AZUL_OSCURO
        );

        total.add(lblTotal);

        total.add(
                lblTotalCompra
        );

        footer.add(
                total,
                BorderLayout.WEST
        );

        //======================================================
        // BOTONES
        //======================================================
        JPanel botones
                = new JPanel(
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
   //==========================================================
// DATOS PRUEBA
//==========================================================
private void cargarDatosPrueba() {

    //======================================================
    // PROVEEDOR
    //======================================================
    // No seleccionamos proveedor automáticamente.
    // El usuario deberá buscarlo con btnBuscarProveedor.

    idProveedorSeleccionado = -1;

    txtProveedor.setText("");

    //======================================================
    // DATOS DE LA COMPRA
    //======================================================
    txtNumeroFactura.setText("");

    txtFecha.setText(
            "24/08/2026"
    );

    //======================================================
    // PRODUCTO TEMPORAL
    //======================================================
    lblCodigoProducto.setText(
            "A0001"
    );

    lblProductoSeleccionado.setText(
            "Coca Cola 2.25L"
    );

    lblStockActual.setText(
            "120"
    );

    txtCosto.setText(
            "2300"
    );

    spnCantidad.setValue(
            1.0
    );

    //======================================================
    // TOTALES
    //======================================================
    lblTotalCompra.setText(
            "$ 0,00"
    );

    actualizarSubtotalItem();
}

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnBuscarProveedor.addActionListener(e -> {

            DialogoBuscarProveedor dialogo
                    = new DialogoBuscarProveedor(
                            this
                    );

            dialogo.setVisible(true);

            if (dialogo.isSeleccionado()) {

                idProveedorSeleccionado
                        = dialogo.getIdSeleccionado();

                txtProveedor.setText(
                        dialogo.getRazonSocialSeleccionada()
                );

                System.out.println(
                        "Proveedor seleccionado ID: "
                        + idProveedorSeleccionado
                );
            }
        });

        //======================================================
        // BUSCAR PRODUCTO
        //======================================================
        btnBuscarProducto
                .addActionListener(e -> {

                    // Más adelante:
                    // buscar producto real en la BD.
                    lblCodigoProducto.setText(
                            "A0001"
                    );

                    lblProductoSeleccionado.setText(
                            "Coca Cola 2.25L"
                    );

                    lblStockActual.setText(
                            "120"
                    );

                    txtCosto.setText(
                            "2300"
                    );

                    actualizarSubtotalItem();

                });

        //======================================================
        // CAMBIO CANTIDAD
        //======================================================
        spnCantidad.addChangeListener(e -> {

            actualizarSubtotalItem();

        });

        //======================================================
        // COSTO
        //======================================================
        txtCosto.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                    }
                });

        //======================================================
        // AGREGAR PRODUCTO
        //======================================================
        btnAgregarProducto
                .addActionListener(e -> {

                    agregarProductoTabla();

                });

        //======================================================
        // ELIMINAR ITEM
        //======================================================
        btnEliminarItem
                .addActionListener(e -> {

                    int fila
                            = tablaDetalle
                                    .getSelectedRow();

                    if (fila != -1) {

                        modeloTabla.removeRow(
                                fila
                        );

                        actualizarTotalCompra();
                    }

                });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar.addActionListener(e -> {

            modeloTabla.setRowCount(0);

            actualizarTotalCompra();

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

                javax.swing.JOptionPane
                        .showMessageDialog(
                                this,
                                "Debe agregar al menos un producto.",
                                "Entrada de Mercadería",
                                javax.swing.JOptionPane.WARNING_MESSAGE
                        );

                return;
            }

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Entrada preparada para registrar.\n"
                            + "Más adelante se conectará con la base de datos.",
                            "Entrada de Mercadería",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

        });
    }

    //==========================================================
    // AGREGAR PRODUCTO
    //==========================================================
    private void agregarProductoTabla() {

        String codigo
                = lblCodigoProducto.getText();

        String producto
                = lblProductoSeleccionado.getText();

        double cantidad
                = ((Number) spnCantidad.getValue())
                        .doubleValue();

        double costo;

        try {

            costo
                    = Double.parseDouble(
                            txtCosto
                                    .getText()
                                    .trim()
                                    .replace(",", ".")
                    );

        } catch (NumberFormatException ex) {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Ingrese un costo válido.",
                            "Costo inválido",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        if (cantidad <= 0) {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "La cantidad debe ser mayor a cero.",
                            "Cantidad inválida",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        if (costo < 0) {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "El costo no puede ser negativo.",
                            "Costo inválido",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        double subtotal
                = cantidad * costo;

        modeloTabla.addRow(
                new Object[]{
                    codigo,
                    producto,
                    cantidad,
                    costo,
                    subtotal
                }
        );

        actualizarTotalCompra();

        // Dejamos preparado el siguiente ingreso.
        spnCantidad.setValue(
                1.0
        );

        txtBuscarProducto.setText(
                ""
        );

        txtBuscarProducto.requestFocusInWindow();
    }

    //==========================================================
    // SUBTOTAL ITEM
    //==========================================================
    private void actualizarSubtotalItem() {

        double cantidad
                = ((Number) spnCantidad.getValue())
                        .doubleValue();

        double costo = 0;

        try {

            String texto
                    = txtCosto
                            .getText()
                            .trim()
                            .replace(",", ".");

            if (!texto.isEmpty()) {

                costo
                        = Double.parseDouble(
                                texto
                        );
            }

        } catch (NumberFormatException ex) {

            costo = 0;
        }

        double subtotal
                = cantidad * costo;

        lblSubtotalItem.setText(
                String.format(
                        "$ %,.2f",
                        subtotal
                )
        );
    }

    //==========================================================
    // TOTAL COMPRA
    //==========================================================
    private void actualizarTotalCompra() {

        double total = 0;

        for (int i = 0;
                i < modeloTabla.getRowCount();
                i++) {

            Object valor
                    = modeloTabla.getValueAt(
                            i,
                            4
                    );

            if (valor instanceof Number) {

                total
                        += ((Number) valor)
                                .doubleValue();
            }
        }

        lblTotalCompra.setText(
                String.format(
                        "$ %,.2f",
                        total
                )
        );
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        8,
                        10,
                        8,
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

        JButton boton
                = new JButton(texto);

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
