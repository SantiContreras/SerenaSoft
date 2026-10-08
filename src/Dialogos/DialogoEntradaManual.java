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
import Dao.StockProductoDao;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

public class DialogoEntradaManual extends JDialog {

    private final CompraService compraService = new CompraService();
    private final ProveedorDao proveedorDao = new ProveedorDao();
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoDao stockProductoDao = new StockProductoDao();
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

    private Producto productoSeleccionado;
    private final List<DetalleEntradaTemporal> detalles = new ArrayList<>();

    private JSpinner spnCantidad;
    private JTextField txtCosto;

    private JLabel lblSubtotalItem;

    private JButton btnAgregarProducto;

    //==========================================================
    // ICONOGRAFÍA DEL DIÁLOGO
    // Dibujada con Java2D: no depende de archivos PNG.
    //==========================================================
    private void configurarIconografia() {
        iconoBoton(btnBuscarProveedor, "buscar");
        iconoBoton(btnBuscarProducto, "buscar");
        iconoBoton(btnAgregarProducto, "agregar");
        iconoBoton(btnEliminarItem, "eliminar");
        iconoBoton(btnLimpiar, "limpiar");
        iconoBoton(btnCancelar, "cerrar");
        iconoBoton(btnConfirmar, "confirmar");
    }

    private void iconoBoton(JButton boton, String tipo) {
        boton.setIcon(crearIcono(tipo, Color.WHITE, 16));
        boton.setIconTextGap(9);
        boton.setHorizontalAlignment(JButton.CENTER);
    }

    private Icon crearIcono(String tipo, Color color, int lado) {
        return new Icon() {
            @Override
            public int getIconWidth() {
                return lado;
            }

            @Override
            public int getIconHeight() {
                return lado;
            }

            @Override
            public void paintIcon(Component componente, Graphics grafico, int x, int y) {
                Graphics2D g = (Graphics2D) grafico.create();
                try {
                    g.translate(x, y);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(color);
                    g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));
                    int m = lado / 2;
                    switch (tipo) {
                        case "buscar" -> {
                            g.drawOval(2, 2, lado - 8, lado - 8);
                            g.drawLine(lado - 6, lado - 6, lado - 2, lado - 2);
                        }
                        case "agregar" -> {
                            g.drawRoundRect(1, 1, lado - 3, lado - 3, 4, 4);
                            g.drawLine(m, 4, m, lado - 5);
                            g.drawLine(4, m, lado - 5, m);
                        }
                        case "eliminar" -> {
                            g.drawLine(3, 4, lado - 4, 4);
                            g.drawLine(5, 2, lado - 6, 2);
                            g.drawRoundRect(5, 6, lado - 11, lado - 9, 2, 2);
                            g.drawLine(m - 2, 8, m - 2, lado - 5);
                            g.drawLine(m + 2, 8, m + 2, lado - 5);
                        }
                        case "limpiar" -> {
                            g.drawArc(2, 2, lado - 5, lado - 5, 35, 280);
                            g.drawLine(2, 2, 2, 7);
                            g.drawLine(2, 7, 7, 7);
                        }
                        case "cerrar" -> {
                            g.drawLine(3, 3, lado - 4, lado - 4);
                            g.drawLine(lado - 4, 3, 3, lado - 4);
                        }
                        case "confirmar" -> {
                            g.drawOval(1, 1, lado - 3, lado - 3);
                            g.drawLine(4, m, m - 1, lado - 5);
                            g.drawLine(m - 1, lado - 5, lado - 4, 4);
                        }
                        case "producto" -> {
                            g.drawRect(2, 5, lado - 5, lado - 8);
                            g.drawLine(2, 5, m, 1);
                            g.drawLine(m, 1, lado - 3, 5);
                            g.drawLine(m, 1, m, lado - 3);
                        }
                        case "stock" -> {
                            g.drawRect(2, 2, lado - 5, lado - 5);
                            g.drawLine(2, m, lado - 3, m);
                            g.drawLine(m, 2, m, lado - 3);
                        }
                        case "dinero" -> {
                            g.drawOval(2, 2, lado - 5, lado - 5);
                            g.drawString("$", Math.max(3, m - 4), lado - 4);
                        }
                        case "documento" -> {
                            g.drawRoundRect(3, 1, lado - 7, lado - 3, 2, 2);
                            g.drawLine(5, 6, lado - 6, 6);
                            g.drawLine(5, 9, lado - 6, 9);
                        }
                        case "calendario" -> {
                            g.drawRect(2, 4, lado - 5, lado - 7);
                            g.drawLine(2, 7, lado - 3, 7);
                            g.drawLine(5, 1, 5, 5);
                            g.drawLine(lado - 6, 1, lado - 6, 5);
                        }
                        case "proveedor" -> {
                            g.drawOval(m - 3, 1, 6, 6);
                            g.drawArc(2, 7, lado - 5, lado - 5, 0, 180);
                        }
                        default ->
                            g.drawOval(2, 2, lado - 5, lado - 5);
                    }
                } finally {
                    g.dispose();
                }
            }
        };
    }

    private void iconoLabel(JLabel label, String tipo, Color color) {
        label.setIcon(crearIcono(tipo, color, 15));
        label.setIconTextGap(7);
    }

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

        // Íconos vectoriales: sin imágenes externas ni dependencias.
        configurarIconografia();

        setModal(true);

        // Ventana más ancha para visualizar todos los componentes
        setSize(
                new Dimension(
                        1200,
                        680
                )
        );

        setMinimumSize(
                new Dimension(
                        1100,
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

        txtProveedor.setBackground(
                new Color(248, 249, 251)
        );

        txtProveedor.setForeground(
                AZUL_OSCURO
        );

        txtProveedor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        txtProveedor.setToolTipText(
                "Proveedor seleccionado para esta compra"
        );

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
    //==========================================================
// PRODUCTO
//==========================================================
    //==========================================================
// PANEL PRODUCTO
//==========================================================
    private JPanel crearPanelProducto() {

        //======================================================
        // COLORES DEL BLOQUE
        //======================================================
        Color fondoGeneral = new Color(250, 248, 243);
        Color fondoTarjeta = new Color(255, 255, 255);
        Color fondoTitulo = new Color(239, 234, 222);
        Color bordeSuave = new Color(220, 214, 201);

        Color fondoResumen = new Color(237, 248, 242);
        Color bordeResumen = new Color(198, 226, 208);

        //======================================================
        // PANEL PRINCIPAL
        //======================================================
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(fondoGeneral);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                bordeSuave
                        ),
                        new EmptyBorder(
                                16,
                                18,
                                16,
                                18
                        )
                )
        );

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(
                5,
                5,
                5,
                5
        );

        c.anchor = GridBagConstraints.WEST;

        //======================================================
        // 1. ENCABEZADO
        //======================================================
        JPanel panelEncabezado = new JPanel();

        panelEncabezado.setOpaque(false);

        panelEncabezado.setLayout(
                new BoxLayout(
                        panelEncabezado,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel(
                "AGREGAR PRODUCTO"
        );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        JLabel subtitulo = new JLabel(
                "Seleccione el producto y cargue cómo se lo entregó el proveedor"
        );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        iconoLabel(titulo, "producto", AZUL_OSCURO);
        panelEncabezado.add(titulo);

        panelEncabezado.add(
                Box.createVerticalStrut(3)
        );

        panelEncabezado.add(subtitulo);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(
                panelEncabezado,
                c
        );

        //======================================================
        // 2. BUSCAR PRODUCTO
        //======================================================
        JPanel panelBuscar = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        panelBuscar.setOpaque(false);

        JPanel panelTextoBuscar = new JPanel();

        panelTextoBuscar.setOpaque(false);

        panelTextoBuscar.setLayout(
                new BoxLayout(
                        panelTextoBuscar,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblProducto = new JLabel(
                "Producto"
        );

        lblProducto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblProducto.setForeground(
                AZUL_OSCURO
        );

        JLabel lblBuscarAyuda = new JLabel(
                "Busque por código, nombre, marca o categoría"
        );

        lblBuscarAyuda.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblBuscarAyuda.setForeground(
                TEXTO_SECUNDARIO
        );

        iconoLabel(lblProducto, "buscar", AZUL_OSCURO);
        panelTextoBuscar.add(
                lblProducto
        );

        panelTextoBuscar.add(
                Box.createVerticalStrut(2)
        );

        panelTextoBuscar.add(
                lblBuscarAyuda
        );

        btnBuscarProducto.setText(
                "Buscar producto"
        );

        btnBuscarProducto.setPreferredSize(
                new Dimension(
                        155,
                        40
                )
        );

        panelBuscar.add(
                panelTextoBuscar,
                BorderLayout.CENTER
        );

        panelBuscar.add(
                btnBuscarProducto,
                BorderLayout.EAST
        );

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(
                panelBuscar,
                c
        );

        //======================================================
        // 3. PRODUCTO SELECCIONADO
        //======================================================
        JPanel panelProductoSeleccionado
                = new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        panelProductoSeleccionado.setBackground(
                fondoTarjeta
        );

        panelProductoSeleccionado.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                bordeSuave
                        ),
                        new EmptyBorder(
                                11,
                                14,
                                11,
                                14
                        )
                )
        );

        //======================================================
        // DATOS DEL PRODUCTO
        //======================================================
        JPanel panelDatosProducto = new JPanel();

        panelDatosProducto.setOpaque(false);

        panelDatosProducto.setLayout(
                new BoxLayout(
                        panelDatosProducto,
                        BoxLayout.Y_AXIS
                )
        );

        lblProductoSeleccionado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblProductoSeleccionado.setForeground(
                AZUL_OSCURO
        );

        lblCodigoProducto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblCodigoProducto.setForeground(
                TEXTO_SECUNDARIO
        );

        panelDatosProducto.add(
                lblProductoSeleccionado
        );

        panelDatosProducto.add(
                Box.createVerticalStrut(3)
        );

        panelDatosProducto.add(
                lblCodigoProducto
        );

        //======================================================
        // STOCK ACTUAL
        //======================================================
        JPanel panelStock = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        6,
                        5
                )
        );

        panelStock.setOpaque(false);

        JLabel lblStockTitulo = new JLabel(
                "Stock actual:"
        );

        lblStockTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblStockTitulo.setForeground(
                AZUL_OSCURO
        );

        lblStockActual.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblStockActual.setForeground(
                VERDE
        );

        iconoLabel(lblStockTitulo, "stock", AZUL_OSCURO);
        panelStock.add(
                lblStockTitulo
        );

        panelStock.add(
                lblStockActual
        );

        panelProductoSeleccionado.add(
                panelDatosProducto,
                BorderLayout.CENTER
        );

        panelProductoSeleccionado.add(
                panelStock,
                BorderLayout.EAST
        );

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(
                panelProductoSeleccionado,
                c
        );

        //======================================================
        // 4. TITULO FORMA DE COMPRA
        //======================================================
        JPanel panelTituloCompra = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        7
                )
        );

        panelTituloCompra.setBackground(
                fondoTitulo
        );

        JLabel lblTituloCompra = new JLabel(
                "¿CÓMO LO ESTÁS COMPRANDO?"
        );

        lblTituloCompra.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblTituloCompra.setForeground(
                AZUL_OSCURO
        );

        iconoLabel(lblTituloCompra, "documento", AZUL_OSCURO);
        panelTituloCompra.add(
                lblTituloCompra
        );

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(
                panelTituloCompra,
                c
        );

        //======================================================
        // 5. CAMPOS DE CARGA
        //
        // PRESENTACION | CANTIDAD | CONTENIDO | COSTO
        //======================================================
        JPanel panelCampos = new JPanel(
                new GridLayout(
                        1,
                        4,
                        18,
                        0
                )
        );

        panelCampos.setOpaque(false);

        //======================================================
        // PRESENTACION
        //======================================================
        JPanel bloquePresentacion
                = crearBloqueVerticalProducto(
                        "PRESENTACIÓN",
                        cmbFormaCompra
                );

        cmbFormaCompra.setPreferredSize(
                new Dimension(
                        160,
                        38
                )
        );

        //======================================================
        // CANTIDAD
        //======================================================
        JPanel contenidoCantidad = new JPanel(
                new BorderLayout(
                        7,
                        0
                )
        );

        contenidoCantidad.setOpaque(false);

        spnCantidad.setPreferredSize(
                new Dimension(
                        120,
                        38
                )
        );

        lblUnidadCantidad.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblUnidadCantidad.setForeground(
                AZUL_OSCURO
        );

        contenidoCantidad.add(
                spnCantidad,
                BorderLayout.CENTER
        );

        contenidoCantidad.add(
                lblUnidadCantidad,
                BorderLayout.EAST
        );

        JPanel bloqueCantidad
                = crearBloqueVerticalProducto(
                        "CANTIDAD",
                        contenidoCantidad
                );

        //======================================================
        // CONTENIDO / FACTOR
        //======================================================
        JPanel bloqueFactor = new JPanel();

        bloqueFactor.setOpaque(false);

        bloqueFactor.setLayout(
                new BoxLayout(
                        bloqueFactor,
                        BoxLayout.Y_AXIS
                )
        );

        lblFactorCompraTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        lblFactorCompraTitulo.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        lblFactorCompraTitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        spnFactorCompra.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        38
                )
        );

        spnFactorCompra.setPreferredSize(
                new Dimension(
                        140,
                        38
                )
        );

        spnFactorCompra.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        bloqueFactor.add(
                lblFactorCompraTitulo
        );

        bloqueFactor.add(
                Box.createVerticalStrut(5)
        );

        bloqueFactor.add(
                spnFactorCompra
        );

        //======================================================
        // COSTO
        //======================================================
        JPanel contenidoCosto = new JPanel(
                new BorderLayout(
                        6,
                        0
                )
        );

        contenidoCosto.setOpaque(false);

        JLabel simboloPeso = new JLabel(
                "$"
        );

        simboloPeso.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        simboloPeso.setForeground(
                VERDE
        );

        txtCosto.setPreferredSize(
                new Dimension(
                        140,
                        38
                )
        );

        contenidoCosto.add(
                simboloPeso,
                BorderLayout.WEST
        );

        contenidoCosto.add(
                txtCosto,
                BorderLayout.CENTER
        );

        JPanel bloqueCosto = new JPanel();

        bloqueCosto.setOpaque(false);

        bloqueCosto.setLayout(
                new BoxLayout(
                        bloqueCosto,
                        BoxLayout.Y_AXIS
                )
        );

        lblCostoUnidad.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        lblCostoUnidad.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        lblCostoUnidad.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenidoCosto.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        bloqueCosto.add(
                lblCostoUnidad
        );

        bloqueCosto.add(
                Box.createVerticalStrut(5)
        );

        bloqueCosto.add(
                contenidoCosto
        );

        //======================================================
        // AGREGAMOS LAS 4 COLUMNAS
        //======================================================
        panelCampos.add(
                bloquePresentacion
        );

        panelCampos.add(
                bloqueCantidad
        );

        panelCampos.add(
                bloqueFactor
        );

        panelCampos.add(
                bloqueCosto
        );

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(
                12,
                5,
                7,
                5
        );

        panel.add(
                panelCampos,
                c
        );

        //======================================================
        // 6. PARTE INFERIOR
        //
        // INGRESO + SUBTOTAL       BOTON AGREGAR
        //======================================================
        JPanel panelInferior = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        panelInferior.setOpaque(false);

        //======================================================
        // RESUMEN
        //======================================================
        JPanel panelResumen = new JPanel(
                new GridBagLayout()
        );

        panelResumen.setBackground(
                fondoResumen
        );

        panelResumen.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                bordeResumen
                        ),
                        new EmptyBorder(
                                9,
                                14,
                                9,
                                14
                        )
                )
        );

        GridBagConstraints r
                = new GridBagConstraints();

        r.anchor = GridBagConstraints.WEST;

        r.insets = new Insets(
                2,
                4,
                2,
                10
        );

        //======================================================
        // INGRESO STOCK
        //======================================================
        JLabel lblTituloIngreso = new JLabel(
                "INGRESAN AL STOCK"
        );

        lblTituloIngreso.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        lblTituloIngreso.setForeground(
                new Color(
                        65,
                        100,
                        78
                )
        );

        r.gridx = 0;
        r.gridy = 0;

        iconoLabel(lblTituloIngreso, "stock", VERDE);
        panelResumen.add(
                lblTituloIngreso,
                r
        );

        lblIngresoStock.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblIngresoStock.setForeground(
                VERDE
        );

        r.gridx = 1;
        r.weightx = 1;

        panelResumen.add(
                lblIngresoStock,
                r
        );

        //======================================================
        // SUBTOTAL
        //======================================================
        JLabel lblSubtotalTitulo = new JLabel(
                "SUBTOTAL"
        );

        lblSubtotalTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        lblSubtotalTitulo.setForeground(
                new Color(
                        65,
                        100,
                        78
                )
        );

        r.gridx = 2;
        r.weightx = 0;

        iconoLabel(lblSubtotalTitulo, "dinero", VERDE);
        panelResumen.add(
                lblSubtotalTitulo,
                r
        );

        lblSubtotalItem.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblSubtotalItem.setForeground(
                VERDE
        );

        r.gridx = 3;

        panelResumen.add(
                lblSubtotalItem,
                r
        );

        //======================================================
        // BOTON AGREGAR
        //======================================================
        btnAgregarProducto.setText(
                "Agregar Producto"
        );

        btnAgregarProducto.setPreferredSize(
                new Dimension(
                        175,
                        52
                )
        );

        panelInferior.add(
                panelResumen,
                BorderLayout.CENTER
        );

        panelInferior.add(
                btnAgregarProducto,
                BorderLayout.EAST
        );

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(
                7,
                5,
                2,
                5
        );

        panel.add(
                panelInferior,
                c
        );

        return panel;
    }

//==========================================================
// BLOQUE VERTICAL PARA CAMPOS DEL PRODUCTO
//
// Ejemplo:
//
// PRESENTACIÓN
// [ CAJA       ]
//
// CANTIDAD
// [ 2 ] CAJA
//==========================================================
    private JPanel crearBloqueVerticalProducto(
            String titulo,
            JComponent componente) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label = new JLabel(
                titulo
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        componente.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(componente);

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

        iconoLabel(lblTotal, "dinero", AZUL_OSCURO);
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

        //======================================================
// STOCK ACTUAL EN DEPÓSITO PRINCIPAL
//======================================================
        BigDecimal stockActual = BigDecimal.ZERO;

        Deposito depositoPrincipal
                = depositoDao.buscarPrincipal();

        if (depositoPrincipal != null) {

            stockActual
                    = stockProductoDao.obtenerCantidad(
                            producto.getIdProducto(),
                            depositoPrincipal.getIdDeposito()
                    );

            if (stockActual == null) {
                stockActual = BigDecimal.ZERO;
            }
        }

        lblStockActual.setText(
                formatearCantidad(stockActual)
                + " "
                + codigoVenta
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
//==========================================================
// CONFIRMAR ENTRADA REAL DE MERCADERIA
//==========================================================
    private void confirmarEntrada() {

        //======================================================
        // 1. VALIDAR PROVEEDOR
        //======================================================
        if (idProveedorSeleccionado <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un proveedor.",
                    "Entrada de Mercadería",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        //======================================================
        // 2. VALIDAR PRODUCTOS
        //======================================================
        if (detalles.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe agregar al menos un producto.",
                    "Entrada de Mercadería",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Evitar nuevas confirmaciones mientras se procesa.
        btnConfirmar.setEnabled(false);

        try {

            //==================================================
            // 3. BUSCAR PROVEEDOR
            //==================================================
            Proveedor proveedor = proveedorDao.buscarPorId(
                    idProveedorSeleccionado
            );

            if (proveedor == null) {

                mostrarError(
                        "El proveedor seleccionado ya no existe."
                );

                return;
            }

            //==================================================
            // 4. BUSCAR DEPOSITO PRINCIPAL
            //==================================================
            Deposito deposito = depositoDao.buscarPrincipal();

            if (deposito == null) {

                mostrarError(
                        "No existe un depósito principal activo."
                );

                return;
            }

            //==================================================
            // 5. PREPARAR COMPRA
            //==================================================
            Compra compra = new Compra();

            compra.setProveedor(proveedor);
            compra.setDeposito(deposito);

            String comprobante = txtNumeroFactura
                    .getText()
                    .trim();

            compra.setNumeroComprobante(
                    comprobante.isEmpty()
                    ? null
                    : comprobante
            );

            compra.setOrigenCarga("MANUAL");

            compra.setObservaciones(
                    "Entrada manual de mercadería"
            );

            //==================================================
            // 6. CREAR COMPRA EN BORRADOR
            //==================================================
            ResultadoOperacion crear
                    = compraService.crearCompra(compra);

            if (!crear.isExitoso()) {

                mostrarError(crear.getMensaje());

                return;
            }

            long idCompra = compra.getIdCompra();

            if (idCompra <= 0) {

                mostrarError(
                        "La compra fue creada, pero no se pudo "
                        + "recuperar su ID."
                );

                return;
            }

            //==================================================
            // 7. REGISTRAR DETALLES DE COMPRA
            //==================================================
            for (DetalleEntradaTemporal detalle : detalles) {

                ResultadoOperacion agregar
                        = compraService.agregarProducto(
                                idCompra,
                                detalle.getProducto()
                                        .getIdProducto(),
                                detalle.getUnidadCompra(),
                                detalle.getFactorConversion(),
                                detalle.getCantidadCompra(),
                                detalle.getCostoUnitario()
                        );

                if (!agregar.isExitoso()) {

                    mostrarError(
                            "No se pudo agregar "
                            + detalle.getProducto().getNombre()
                            + ": "
                            + agregar.getMensaje()
                            + "\n\nLa compra N.º "
                            + idCompra
                            + " quedó en borrador."
                    );

                    return;
                }
            }

            //==================================================
            // 8. CONFIRMAR COMPRA Y ACTUALIZAR STOCK
            //==================================================
            ResultadoOperacion confirmar
                    = compraService.confirmarCompra(idCompra);

            if (!confirmar.isExitoso()) {

                mostrarError(
                        "No se pudo confirmar la compra N.º "
                        + idCompra
                        + ":\n"
                        + confirmar.getMensaje()
                        + "\n\nVerificá su estado antes de "
                        + "intentar registrarla nuevamente."
                );

                return;
            }

            //==================================================
            // 9. MOSTRAR DIALOGO PERSONALIZADO
            //==================================================
            // Obtener la ventana principal si es un JFrame.
            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(this);

            java.awt.Frame propietario = null;

            if (ventana instanceof java.awt.Frame frame) {
                propietario = frame;
            }

            DialogoEntradaConfirmada dialogo
                    = new DialogoEntradaConfirmada(
                            propietario,
                            idCompra,
                            proveedor.getRazonSocial(),
                            detalles.size(),
                            deposito.getNombre(),
                            lblTotalCompra.getText()
                    );

            dialogo.setLocationRelativeTo(this);

            dialogo.setVisible(true);

            //==================================================
            // 10. CERRAR ENTRADA MANUAL
            //==================================================
            dispose();

        } catch (Exception ex) {

            mostrarError(
                    "Ocurrió un error durante el registro:\n"
                    + ex.getMessage()
                    + "\n\nSi la compra llegó a confirmarse, "
                    + "no la cargues nuevamente sin verificar "
                    + "primero la base de datos."
            );

        } finally {

            // Solo se utiliza si el diálogo permanece abierto.
            btnConfirmar.setEnabled(true);
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

        switch (texto) {
            case "Proveedor" ->
                iconoLabel(label, "proveedor", AZUL);
            case "Factura N°" ->
                iconoLabel(label, "documento", AZUL);
            case "Fecha" ->
                iconoLabel(label, "calendario", AZUL);
            default -> {
            }
        }

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
