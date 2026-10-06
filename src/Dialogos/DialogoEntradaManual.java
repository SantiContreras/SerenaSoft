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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import Dao.DepositoDao;
import Dao.ProveedorDao;
import model.Compra;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.UnidadMedida;
import services.CompraService;
import services.ResultadoOperacion;

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

    // Información comercial de compra / conversión
    private JLabel lblUnidadCompra;
    private JLabel lblUnidadVenta;
    private JLabel lblFactorConversion;
    private JLabel lblIngresoStock;
    private JLabel lblUnidadCantidad;
    private JLabel lblCostoUnidad;

    // Presentación utilizada en ESTA compra
    private JComboBox<String> cmbFormaCompra;
    private JSpinner spnFactorCompra;
    private JLabel lblFactorCompraTitulo;

    // Servicios / DAO necesarios para registrar la entrada real
    private final CompraService compraService = new CompraService();
    private final ProveedorDao proveedorDao = new ProveedorDao();
    private final DepositoDao depositoDao = new DepositoDao();

    private Producto productoSeleccionado;
    private final List<DetalleEntradaTemporal> detalles = new ArrayList<>();

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

        lblUnidadCompra = crearValorInformativo("-");
        lblUnidadVenta = crearValorInformativo("-");
        lblFactorConversion = crearValorInformativo("-");
        lblIngresoStock = crearValorInformativo("-");
        lblUnidadCantidad = crearValorInformativo("-");
        lblCostoUnidad = crearValorInformativo("Costo");

        cmbFormaCompra = new JComboBox<>();
        cmbFormaCompra.setPreferredSize(new Dimension(145, 38));
        cmbFormaCompra.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbFormaCompra.setBackground(Color.WHITE);

        lblFactorCompraTitulo = crearLabel("Unidades por presentación");

        spnFactorCompra = new JSpinner(
                new SpinnerNumberModel(1.000, 0.001, 999999.000, 1.000)
        );
        spnFactorCompra.setPreferredSize(new Dimension(125, 38));
        spnFactorCompra.setEditor(
                new JSpinner.NumberEditor(spnFactorCompra, "0.###")
        );

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

        aplicarEstiloCampo(txtProveedor);
        aplicarEstiloCampo(txtNumeroFactura);
        aplicarEstiloCampo(txtFecha);
        aplicarEstiloCampo(txtBuscarProducto);
        aplicarEstiloCampo(txtCosto);
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
                    "Cant. Compra",
                    "Unidad",
                    "Ingresa Stock",
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
        tablaDetalle.getColumnModel().getColumn(0).setPreferredWidth(95);
        tablaDetalle.getColumnModel().getColumn(1).setPreferredWidth(235);
        tablaDetalle.getColumnModel().getColumn(2).setPreferredWidth(95);
        tablaDetalle.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaDetalle.getColumnModel().getColumn(4).setPreferredWidth(125);
        tablaDetalle.getColumnModel().getColumn(5).setPreferredWidth(115);
        tablaDetalle.getColumnModel().getColumn(6).setPreferredWidth(125);
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
                AZUL_OSCURO
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
                Color.WHITE
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
                new Color(215, 228, 248)
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
                        360
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

        GridBagConstraints c
                = crearConstraints();

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

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(14, 16, 14, 16)
                )
        );

        GridBagConstraints c = crearConstraints();
        c.insets = new Insets(7, 7, 7, 7);

        JLabel titulo = new JLabel("AGREGAR PRODUCTO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(AZUL_OSCURO);

        JLabel ayuda = new JLabel(
                "Seleccione el producto y cargue cómo se lo entregó el proveedor"
        );
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ayuda.setForeground(TEXTO_SECUNDARIO);

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(
                new javax.swing.BoxLayout(
                        encabezado,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );
        encabezado.add(titulo);
        encabezado.add(javax.swing.Box.createVerticalStrut(2));
        encabezado.add(ayuda);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 6;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(encabezado, c);

        // BUSCADOR
        c.gridy = 1; c.gridx = 0; c.gridwidth = 1;
        c.weightx = 0; c.fill = GridBagConstraints.NONE;
        panel.add(crearLabel("Producto"), c);

        c.gridx = 1; c.gridwidth = 4; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        txtBuscarProducto.setPreferredSize(new Dimension(520, 38));
        panel.add(txtBuscarProducto, c);

        c.gridx = 5; c.gridwidth = 1; c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(btnBuscarProducto, c);

        // TARJETA DEL PRODUCTO SELECCIONADO
        JPanel tarjetaProducto = new JPanel(new BorderLayout(12, 0));
        tarjetaProducto.setBackground(new Color(247, 249, 253));
        tarjetaProducto.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(225, 231, 240)),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JPanel datosProducto = new JPanel();
        datosProducto.setOpaque(false);
        datosProducto.setLayout(
                new javax.swing.BoxLayout(
                        datosProducto,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        lblProductoSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProductoSeleccionado.setForeground(AZUL_OSCURO);
        lblCodigoProducto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCodigoProducto.setForeground(TEXTO_SECUNDARIO);

        datosProducto.add(lblProductoSeleccionado);
        datosProducto.add(javax.swing.Box.createVerticalStrut(3));
        datosProducto.add(lblCodigoProducto);

        JPanel stock = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        stock.setOpaque(false);
        stock.add(crearLabel("Stock actual:"));

        lblStockActual.setForeground(AZUL);
        lblStockActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        stock.add(lblStockActual);

        tarjetaProducto.add(datosProducto, BorderLayout.CENTER);
        tarjetaProducto.add(stock, BorderLayout.EAST);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 6;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(tarjetaProducto, c);

        // CÓMO SE COMPRA
        JLabel tituloCompra = new JLabel("¿Cómo lo estás comprando?");
        tituloCompra.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloCompra.setForeground(AZUL_OSCURO);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 6;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(tituloCompra, c);

        c.gridy = 4; c.gridx = 0; c.gridwidth = 1;
        c.weightx = 0; c.fill = GridBagConstraints.NONE;
        panel.add(crearLabel("Presentación"), c);

        c.gridx = 1;
        panel.add(cmbFormaCompra, c);

        c.gridx = 2;
        panel.add(crearLabel("Cantidad"), c);

        c.gridx = 3;
        JPanel cantidadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        cantidadPanel.setOpaque(false);
        spnCantidad.setPreferredSize(new Dimension(135, 38));
        cantidadPanel.add(spnCantidad);
        lblUnidadCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cantidadPanel.add(lblUnidadCantidad);
        panel.add(cantidadPanel, c);

        c.gridx = 4;
        panel.add(lblFactorCompraTitulo, c);

        c.gridx = 5;
        panel.add(spnFactorCompra, c);

        // RESUMEN GRANDE
        JPanel resumenIngreso = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 7)
        );
        resumenIngreso.setBackground(new Color(236, 248, 241));
        resumenIngreso.setBorder(
                BorderFactory.createLineBorder(new Color(196, 229, 209))
        );

        JLabel lblIngresan = new JLabel("INGRESAN AL STOCK:");
        lblIngresan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblIngresan.setForeground(new Color(55, 100, 75));

        lblIngresoStock.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblIngresoStock.setForeground(VERDE);

        resumenIngreso.add(lblIngresan);
        resumenIngreso.add(lblIngresoStock);

        c.gridx = 0; c.gridy = 5; c.gridwidth = 6;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(resumenIngreso, c);

        // PRECIO
        c.gridy = 6; c.gridx = 0; c.gridwidth = 1;
        c.weightx = 0; c.fill = GridBagConstraints.NONE;
        panel.add(lblCostoUnidad, c);

        c.gridx = 1;

        JPanel panelCosto = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        5,
                        0
                )
        );
        panelCosto.setOpaque(false);

        JLabel simboloMoneda = new JLabel("$");
        simboloMoneda.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );
        simboloMoneda.setForeground(VERDE);

        txtCosto.setPreferredSize(
                new Dimension(
                        145,
                        38
                )
        );

        panelCosto.add(simboloMoneda);
        panelCosto.add(txtCosto);

        panel.add(panelCosto, c);

        c.gridx = 2;
        panel.add(crearLabel("Subtotal"), c);

        c.gridx = 3;
        lblSubtotalItem.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lblSubtotalItem, c);

        c.gridx = 5;
        panel.add(btnAgregarProducto, c);

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
        // Se conserva el nombre del método para no alterar el constructor,
        // pero ya NO carga datos ficticios.
        idProveedorSeleccionado = -1;
        txtProveedor.setText("");
        txtNumeroFactura.setText("");
        txtFecha.setText(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
        );

        detalles.clear();
        modeloTabla.setRowCount(0);
        limpiarProductoSeleccionado();
        lblTotalCompra.setText("$ 0,00");
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
        //======================================================
// BUSCAR PRODUCTO
//======================================================
        btnBuscarProducto.addActionListener(e -> {

            // -------------------------------------------------
            // ABRIR DIÁLOGO DE BÚSQUEDA
            // -------------------------------------------------
            DialogoBuscarProducto dialogo
                    = new DialogoBuscarProducto(this);

            dialogo.setVisible(true);

            // -------------------------------------------------
            // SI CANCELÓ, NO HACEMOS NADA
            // -------------------------------------------------
            if (!dialogo.isSeleccionado()) {
                return;
            }

            // -------------------------------------------------
            // OBTENER PRODUCTO SELECCIONADO
            // -------------------------------------------------
            Producto producto
                    = dialogo.getObjetoProductoSeleccionado();

            if (producto == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo recuperar el producto seleccionado.",
                        "Producto",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            cargarProductoSeleccionado(producto);
        });
        //======================================================
        // CAMBIO CANTIDAD
        //======================================================
        spnCantidad.addChangeListener(e -> {

            actualizarSubtotalItem();
            actualizarIngresoStock();

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
        // FORMA DE COMPRA
        //======================================================
        cmbFormaCompra.addActionListener(e -> {

            if (productoSeleccionado != null) {
                aplicarFormaCompraSeleccionada();
            }

        });

        //======================================================
        // FACTOR DE ESTA COMPRA
        //======================================================
        spnFactorCompra.addChangeListener(e -> {

            actualizarIngresoStock();

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

                        modeloTabla.removeRow(fila);
                        detalles.remove(fila);
                        actualizarTotalCompra();
                    }

                });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar.addActionListener(e -> {

            modeloTabla.setRowCount(0);
            detalles.clear();
            actualizarTotalCompra();
            limpiarProductoSeleccionado();

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

            confirmarEntrada();

        });
    }

    //==========================================================
    // AGREGAR PRODUCTO
    //==========================================================
    private void cargarProductoSeleccionado(Producto producto) {

        productoSeleccionado = producto;

        lblCodigoProducto.setText(
                valorTexto(
                        producto.getCodigo()
                )
        );

        lblProductoSeleccionado.setText(
                valorTexto(
                        producto.getNombre()
                )
        );

        UnidadMedida unidadCompra
                = producto.getUnidadCompra();

        UnidadMedida unidadVenta
                = producto.getUnidadVenta();

        String codigoCompra
                = codigoUnidad(
                        unidadCompra
                );

        String codigoVenta
                = codigoUnidad(
                        unidadVenta
                );

        lblStockActual.setText(
                "0 " + codigoVenta
        );

        lblUnidadCompra.setText(
                descripcionUnidad(
                        unidadCompra
                )
        );

        lblUnidadVenta.setText(
                descripcionUnidad(
                        unidadVenta
                )
        );

        BigDecimal factorBase
                = obtenerFactor(
                        producto
                );

        lblFactorConversion.setText(
                "1 "
                + codigoCompra
                + " = "
                + formatearCantidad(
                        factorBase
                )
                + " "
                + codigoVenta
        );

        // Mostramos solamente opciones válidas para ESTE producto:
        // 1) su presentación habitual (CAJA / PACK / BULTO / etc.)
        // 2) su unidad directa de stock (UN / KG / LT / etc.)
        // Nunca cargamos aquí todas las unidades del sistema.
        cmbFormaCompra.removeAllItems();

        if (!"-".equals(
                codigoCompra
        )) {

            cmbFormaCompra.addItem(
                    codigoCompra
            );
        }

        if (!"-".equals(
                codigoVenta
        )
                && !codigoVenta.equalsIgnoreCase(
                        codigoCompra
                )) {

            cmbFormaCompra.addItem(
                    codigoVenta
            );
        }

        if (cmbFormaCompra.getItemCount() > 0) {

            cmbFormaCompra.setSelectedIndex(
                    0
            );
        }

        if (producto.getPrecioCompra() != null) {

            txtCosto.setText(
                    producto
                            .getPrecioCompra()
                            .stripTrailingZeros()
                            .toPlainString()
            );

        } else {

            txtCosto.setText(
                    "0"
            );
        }

        aplicarFormaCompraSeleccionada();
        actualizarIngresoStock();
        actualizarSubtotalItem();
    }

    //==========================================================
    // CONFIGURAR CANTIDAD SEGÚN UNIDAD DE COMPRA
    //==========================================================
    private void configurarSpinnerSegunUnidad(UnidadMedida unidadCompra) {
        boolean permiteDecimales
                = unidadCompra != null
                && unidadCompra.isPermiteDecimales();

        if (permiteDecimales) {
            spnCantidad.setModel(
                    new SpinnerNumberModel(1.000, 0.001, 999999.000, 0.001)
            );
            spnCantidad.setEditor(
                    new JSpinner.NumberEditor(spnCantidad, "0.000")
            );
        } else {
            spnCantidad.setModel(
                    new SpinnerNumberModel(1, 1, 999999, 1)
            );
            spnCantidad.setEditor(
                    new JSpinner.NumberEditor(spnCantidad, "0")
            );
        }
    }

    //==========================================================
    // AGREGAR PRODUCTO
    //==========================================================
    private void agregarProductoTabla() {

        if (productoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero seleccione un producto.",
                    "Producto",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String unidadCompraCodigo
                = obtenerFormaCompraSeleccionada();

        if (unidadCompraCodigo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una forma de compra.",
                    "Forma de compra",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal cantidadCompra
                = obtenerCantidadSpinner();

        if (cantidadCompra.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad debe ser mayor a cero.",
                    "Cantidad inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        UnidadMedida unidadUsada
                = obtenerUnidadSegunForma(
                        unidadCompraCodigo
                );

        if (unidadUsada != null
                && !unidadUsada.isPermiteDecimales()
                && cantidadCompra
                        .stripTrailingZeros()
                        .scale() > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La unidad "
                    + unidadCompraCodigo
                    + " no permite cantidades decimales.",
                    "Cantidad inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal factor
                = obtenerFactorCompraActual();

        if (factor == null
                || factor.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El factor de conversión debe ser mayor a cero.",
                    "Factor inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal costo
                = obtenerCosto();

        if (costo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un costo válido.",
                    "Costo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (costo.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El costo no puede ser negativo.",
                    "Costo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal cantidadStock
                = cantidadCompra.multiply(
                        factor
                );

        BigDecimal subtotal
                = cantidadCompra
                        .multiply(
                                costo
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        String unidadVentaCodigo
                = codigoUnidad(
                        productoSeleccionado
                                .getUnidadVenta()
                );

        DetalleEntradaTemporal detalle
                = new DetalleEntradaTemporal(
                        productoSeleccionado,
                        unidadCompraCodigo,
                        factor,
                        cantidadCompra,
                        cantidadStock,
                        costo,
                        subtotal
                );

        detalles.add(
                detalle
        );

        modeloTabla.addRow(
                new Object[]{
                    productoSeleccionado.getCodigo(),
                    productoSeleccionado.getNombre(),
                    formatearCantidad(cantidadCompra),
                    unidadCompraCodigo,
                    formatearCantidad(cantidadStock)
                    + " "
                    + unidadVentaCodigo,
                    formatearMoneda(costo),
                    formatearMoneda(subtotal)
                }
        );

        actualizarTotalCompra();

        limpiarProductoSeleccionado();

        txtBuscarProducto
                .requestFocusInWindow();
    }

    //==========================================================
    // INGRESO A STOCK - INFORMATIVO
    //==========================================================
    private void actualizarIngresoStock() {

        if (productoSeleccionado == null) {

            lblIngresoStock.setText(
                    "-"
            );

            return;
        }

        BigDecimal cantidadCompra
                = obtenerCantidadSpinner();

        BigDecimal factor
                = obtenerFactorCompraActual();

        if (factor == null
                || factor.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            lblIngresoStock.setText(
                    "-"
            );

            return;
        }

        BigDecimal cantidadStock
                = cantidadCompra.multiply(
                        factor
                );

        lblIngresoStock.setText(
                formatearCantidad(
                        cantidadStock
                )
                + " "
                + codigoUnidad(
                        productoSeleccionado
                                .getUnidadVenta()
                )
        );
    }

    //==========================================================
    // SUBTOTAL ITEM
    //==========================================================
    private void actualizarSubtotalItem() {
        BigDecimal cantidad = obtenerCantidadSpinner();
        BigDecimal costo = obtenerCosto();

        if (costo == null) {
            costo = BigDecimal.ZERO;
        }

        BigDecimal subtotal = cantidad.multiply(costo)
                .setScale(2, RoundingMode.HALF_UP);

        lblSubtotalItem.setText(formatearMoneda(subtotal));
    }

    //==========================================================
    // TOTAL COMPRA
    //==========================================================
    private void actualizarTotalCompra() {
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleEntradaTemporal detalle : detalles) {
            total = total.add(detalle.getSubtotal());
        }

        lblTotalCompra.setText(formatearMoneda(total));
    }

    //==========================================================
    // LIMPIAR PRODUCTO
    //==========================================================
    private void limpiarProductoSeleccionado() {
        productoSeleccionado = null;
        txtBuscarProducto.setText("");
        lblCodigoProducto.setText("-");
        lblProductoSeleccionado.setText("Seleccione un producto");
        lblStockActual.setText("0");
        lblUnidadCompra.setText("-");
        lblUnidadVenta.setText("-");
        lblFactorConversion.setText("-");
        lblIngresoStock.setText("-");
        lblUnidadCantidad.setText("-");
        lblCostoUnidad.setText("Costo");
        txtCosto.setText("");

        cmbFormaCompra.removeAllItems();

        spnFactorCompra.setModel(
                new SpinnerNumberModel(
                        1.000,
                        0.001,
                        999999.000,
                        1.000
                )
        );

        spnFactorCompra.setEditor(
                new JSpinner.NumberEditor(
                        spnFactorCompra,
                        "0.###"
                )
        );

        spnFactorCompra.setEnabled(false);
        spnFactorCompra.setVisible(false);
        lblFactorCompraTitulo.setVisible(false);

        spnCantidad.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
        spnCantidad.setEditor(new JSpinner.NumberEditor(spnCantidad, "0"));
        lblSubtotalItem.setText("$ 0,00");
    }

    //==========================================================
    // FORMA DE COMPRA
    //==========================================================
    private void aplicarFormaCompraSeleccionada() {

        if (productoSeleccionado == null) {
            return;
        }

        String forma
                = obtenerFormaCompraSeleccionada();

        if (forma == null) {
            return;
        }

        String codigoCompra
                = codigoUnidad(
                        productoSeleccionado
                                .getUnidadCompra()
                );

        String codigoVenta
                = codigoUnidad(
                        productoSeleccionado
                                .getUnidadVenta()
                );

        boolean compraDirectaStock
                = forma.equalsIgnoreCase(
                        codigoVenta
                )
                && !forma.equalsIgnoreCase(
                        codigoCompra
                );

        BigDecimal factor
                = compraDirectaStock
                ? BigDecimal.ONE
                : obtenerFactor(
                        productoSeleccionado
                );

        spnFactorCompra.setValue(
                factor.doubleValue()
        );

        // Si se compra directamente en UN/KG/LT, el factor siempre es 1.
        // Si se compra CAJA/PACK/BULTO, el operador puede cambiarlo.
        spnFactorCompra.setEnabled(
                !compraDirectaStock
        );

        boolean mostrarContenidoPresentacion
                = !compraDirectaStock;

        lblFactorCompraTitulo.setVisible(
                mostrarContenidoPresentacion
        );

        spnFactorCompra.setVisible(
                mostrarContenidoPresentacion
        );

        if (mostrarContenidoPresentacion) {

            lblFactorCompraTitulo.setText(
                    "Unidades por " + forma
            );
        }

        lblUnidadCantidad.setText(
                forma
        );

        String textoCosto;

        if ("UN".equalsIgnoreCase(forma)) {

            textoCosto = "Costo por UNIDAD";

        } else if ("KG".equalsIgnoreCase(forma)) {

            textoCosto = "Costo por KG";

        } else {

            textoCosto = "Costo por " + forma;
        }

        lblCostoUnidad.setText(
                textoCosto
        );

        configurarSpinnerSegunUnidad(
                obtenerUnidadSegunForma(
                        forma
                )
        );

        actualizarIngresoStock();
        actualizarSubtotalItem();
    }

    private String obtenerFormaCompraSeleccionada() {

        Object item
                = cmbFormaCompra
                        .getSelectedItem();

        if (item == null) {
            return null;
        }

        String valor
                = item
                        .toString()
                        .trim();

        return valor.isEmpty()
                ? null
                : valor;
    }

    private UnidadMedida obtenerUnidadSegunForma(
            String codigo) {

        if (productoSeleccionado == null
                || codigo == null) {

            return null;
        }

        UnidadMedida compra
                = productoSeleccionado
                        .getUnidadCompra();

        if (compra != null
                && compra.getCodigo() != null
                && compra.getCodigo()
                        .equalsIgnoreCase(
                                codigo
                        )) {

            return compra;
        }

        UnidadMedida venta
                = productoSeleccionado
                        .getUnidadVenta();

        if (venta != null
                && venta.getCodigo() != null
                && venta.getCodigo()
                        .equalsIgnoreCase(
                                codigo
                        )) {

            return venta;
        }

        return null;
    }

    private BigDecimal obtenerFactorCompraActual() {

        Object valor
                = spnFactorCompra
                        .getValue();

        if (valor == null) {
            return BigDecimal.ONE;
        }

        try {

            return new BigDecimal(
                    valor
                            .toString()
                            .replace(",", ".")
            ).setScale(
                    3,
                    RoundingMode.UNNECESSARY
            );

        } catch (Exception ex) {

            return null;
        }
    }

    //==========================================================
    // CONFIRMAR ENTRADA REAL
    //==========================================================
    private void confirmarEntrada() {

        if (idProveedorSeleccionado <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un proveedor.",
                    "Entrada de Mercadería",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (detalles.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe agregar al menos un producto.",
                    "Entrada de Mercadería",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Proveedor proveedor
                    = proveedorDao.buscarPorId(
                            idProveedorSeleccionado
                    );

            if (proveedor == null) {

                mostrarError(
                        "El proveedor seleccionado ya no existe."
                );

                return;
            }

            Deposito deposito
                    = depositoDao.buscarPrincipal();

            if (deposito == null) {

                mostrarError(
                        "No existe un depósito principal activo."
                );

                return;
            }

            Compra compra
                    = new Compra();

            compra.setProveedor(
                    proveedor
            );

            compra.setDeposito(
                    deposito
            );

            String comprobante
                    = txtNumeroFactura
                            .getText()
                            .trim();

            compra.setNumeroComprobante(
                    comprobante.isEmpty()
                    ? null
                    : comprobante
            );

            compra.setOrigenCarga(
                    "MANUAL"
            );

            compra.setObservaciones(
                    "Entrada manual de mercadería"
            );

            ResultadoOperacion crear
                    = compraService.crearCompra(
                            compra
                    );

            if (!crear.isExitoso()) {

                mostrarError(
                        crear.getMensaje()
                );

                return;
            }

            long idCompra
                    = compra.getIdCompra();

            if (idCompra <= 0) {

                mostrarError(
                        "La compra fue creada, pero no se pudo recuperar su ID."
                );

                return;
            }

            for (DetalleEntradaTemporal detalle
                    : detalles) {

                ResultadoOperacion agregar
                        = compraService.agregarProducto(
                                idCompra,
                                detalle
                                        .getProducto()
                                        .getIdProducto(),
                                detalle
                                        .getUnidadCompra(),
                                detalle
                                        .getFactorConversion(),
                                detalle
                                        .getCantidadCompra(),
                                detalle
                                        .getCostoUnitario()
                        );

                if (!agregar.isExitoso()) {

                    mostrarError(
                            "No se pudo agregar "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + ": "
                            + agregar.getMensaje()
                    );

                    return;
                }
            }

            ResultadoOperacion confirmar
                    = compraService.confirmarCompra(
                            idCompra
                    );

            if (!confirmar.isExitoso()) {

                mostrarError(
                        confirmar.getMensaje()
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Entrada registrada correctamente.\n"
                    + "Compra N.º "
                    + idCompra
                    + "\nEl stock fue actualizado.",
                    "Entrada de Mercadería",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (Exception ex) {

            mostrarError(
                    "No se pudo registrar la entrada: "
                    + ex.getMessage()
            );
        }
    }

    private void mostrarError(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Entrada de Mercadería",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private BigDecimal obtenerCantidadSpinner() {
        Object valor = spnCantidad.getValue();
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(valor.toString().replace(",", "."));
    }

    private BigDecimal obtenerCosto() {
        try {
            String texto = txtCosto.getText().trim().replace(" ", "");
            if (texto.isEmpty()) {
                return BigDecimal.ZERO;
            }
            if (texto.contains(",") && !texto.contains(".")) {
                texto = texto.replace(",", ".");
            }
            return new BigDecimal(texto);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private BigDecimal obtenerFactor(Producto producto) {
        if (producto == null
                || producto.getFactorConversion() == null
                || producto.getFactorConversion().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ONE;
        }
        return producto.getFactorConversion();
    }

    private String codigoUnidad(UnidadMedida unidad) {
        if (unidad == null
                || unidad.getCodigo() == null
                || unidad.getCodigo().isBlank()) {
            return "-";
        }
        return unidad.getCodigo();
    }

    private String descripcionUnidad(UnidadMedida unidad) {
        if (unidad == null) {
            return "-";
        }
        String codigo = valorTexto(unidad.getCodigo());
        String nombre = valorTexto(unidad.getNombre());
        if (codigo.equals("-")) {
            return nombre;
        }
        if (nombre.equals("-") || nombre.equalsIgnoreCase(codigo)) {
            return codigo;
        }
        return codigo + " - " + nombre;
    }

    private String formatearCantidad(BigDecimal valor) {
        if (valor == null) {
            return "0";
        }
        return valor.stripTrailingZeros().toPlainString();
    }

    private String formatearMoneda(BigDecimal valor) {
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        java.text.DecimalFormatSymbols simbolos
                = new java.text.DecimalFormatSymbols(new java.util.Locale("es", "AR"));
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        java.text.DecimalFormat formato
                = new java.text.DecimalFormat("$ #,##0.00", simbolos);
        return formato.format(valor);
    }

    private String valorTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return "-";
        }
        return texto;
    }

    public List<DetalleEntradaTemporal> getDetalles() {
        return new ArrayList<>(detalles);
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
    //==========================================================
    // ESTILO DE CAMPOS
    //==========================================================
    private void aplicarEstiloCampo(JTextField campo) {

        campo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        campo.setBackground(Color.WHITE);

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(205, 214, 226)
                        ),
                        new EmptyBorder(6, 10, 6, 10)
                )
        );
    }

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

    private JLabel crearValorInformativo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AZUL);
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

    //==========================================================
    // DETALLE TEMPORAL DE LA ENTRADA
    //==========================================================
    public static class DetalleEntradaTemporal {

        private final Producto producto;
        private final String unidadCompra;
        private final BigDecimal factorConversion;
        private final BigDecimal cantidadCompra;
        private final BigDecimal cantidadStock;
        private final BigDecimal costoUnitario;
        private final BigDecimal subtotal;

        public DetalleEntradaTemporal(
                Producto producto,
                String unidadCompra,
                BigDecimal factorConversion,
                BigDecimal cantidadCompra,
                BigDecimal cantidadStock,
                BigDecimal costoUnitario,
                BigDecimal subtotal) {

            this.producto = producto;
            this.unidadCompra = unidadCompra;
            this.factorConversion = factorConversion;
            this.cantidadCompra = cantidadCompra;
            this.cantidadStock = cantidadStock;
            this.costoUnitario = costoUnitario;
            this.subtotal = subtotal;
        }

        public Producto getProducto() {
            return producto;
        }

        public String getUnidadCompra() {
            return unidadCompra;
        }

        public BigDecimal getFactorConversion() {
            return factorConversion;
        }

        public BigDecimal getCantidadCompra() {
            return cantidadCompra;
        }

        public BigDecimal getCantidadStock() {
            return cantidadStock;
        }

        public BigDecimal getCostoUnitario() {
            return costoUnitario;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }
    }

}
