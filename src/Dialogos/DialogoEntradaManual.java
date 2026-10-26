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
import Dao.ProductoDao;
import model.TipoIva;
import Dao.ProveedorDao;
import model.Compra;
import model.CompraFiscal;
import services.CompraFiscalService;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.UnidadMedida;
import services.CompraService;
import services.ResultadoOperacion;
import Dao.StockProductoDao;
import model.ProductoPresentacion;
import services.ProductoPresentacionService;
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
    private final ProductoDao productoDaoFiscal = new ProductoDao();
    private static final java.util.prefs.Preferences PREFERENCIAS_FISCALES =
            java.util.prefs.Preferences.userNodeForPackage(DialogoEntradaManual.class);
    private final ProveedorDao proveedorDao = new ProveedorDao();
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoDao stockProductoDao = new StockProductoDao();
    private final ProductoPresentacionService presentacionService = new ProductoPresentacionService();
    private final List<ProductoPresentacion> presentacionesDisponibles = new ArrayList<>();
    private boolean cargandoPresentaciones = false;
    //==========================================================
    // DATOS DE LA COMPRA
    //==========================================================
    private JTextField txtProveedor;
    private JButton btnBuscarProveedor;

    private int idProveedorSeleccionado = -1;
    private JTextField txtNumeroFactura;
    private JComboBox<String> cmbTipoFactura;
    private JButton btnDetalleFiscal;
    private JTextField txtTotalFactura;
    private JLabel lblResumenFiscal;
    private boolean totalFacturaEditado = false;
    private String condicionFiscal = PREFERENCIAS_FISCALES.get("condicionFiscalComercio", "MONOTRIBUTISTA");
    private BigDecimal ajusteIvaManual = null;
    // La primera integración fiscal admite Factura A sin percepciones.
    // El crédito fiscal requiere validación contable y se registra en cero.
    private boolean preciosConIvaIncluido = true;
    private String motivoAjusteIva = "";
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
    private JTextField txtPrecioVenta;
    private JLabel lblCostoStock;
    private JLabel lblMargenVenta;
    private final java.util.Map<Integer, BigDecimal> preciosVentaPendientes = new java.util.HashMap<>();

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
                        1100,
                        700
                )
        );

        setMinimumSize(
                new Dimension(
                        960,
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
        cmbFormaCompra.setPreferredSize(new Dimension(190, 37));
        cmbFormaCompra.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbFormaCompra.setMaximumRowCount(16);
        cmbFormaCompra.setToolTipText("Seleccioná la presentación exacta: PACK x6, CAJA x8, BULTO x12, PALLET x480, etc.");
        cmbFormaCompra.setBackground(Color.WHITE);
        cmbFormaCompra.setForeground(new Color(35, 54, 82));
        cmbFormaCompra.setBorder(BorderFactory.createLineBorder(new Color(199, 212, 230), 1));
        cmbFormaCompra.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list,
                    Object value, int index, boolean selected, boolean focus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, focus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 10));
                label.setOpaque(true);
                label.setBackground(selected ? new Color(225, 237, 252) : Color.WHITE);
                label.setForeground(new Color(28, 54, 91));
                return label;
            }
        });

        lblFactorCompraTitulo = crearLabel("Unidades por presentación");

        spnFactorCompra = new JSpinner(
                new SpinnerNumberModel(1.000, 0.001, 999999.000, 1.000)
        );
        spnFactorCompra.setPreferredSize(new Dimension(125, 38));
        spnFactorCompra.setEditor(
                new JSpinner.NumberEditor(spnFactorCompra, "0.###")
        );
        estilizarSpinner(spnFactorCompra, true);

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
        txtPrecioVenta = new JTextField();
        lblCostoStock = new JLabel("$ 0,00");
        lblMargenVenta = new JLabel("—");

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
        aplicarEstiloCampo(txtPrecioVenta);
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

        tablaDetalle.setRowHeight(32);

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
        tablaDetalle.setShowVerticalLines(false);

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
                Color.WHITE
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

        // La cabecera fiscal ocupa dos filas: no limitarla a 110 px.
        // BoxLayout necesita una altura máxima suficiente para mostrar
        // todos los campos y sus bordes sin recortarlos.
        panelCompra.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 190)
        );

        panelProducto.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        540
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
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
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
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), new EmptyBorder(14, 18, 16, 18)));

        JLabel titulo = new JLabel("DATOS DEL COMPROBANTE DE COMPRA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(AZUL_OSCURO);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel filas = new JPanel(new GridBagLayout());
        filas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 7);
        c.anchor = GridBagConstraints.WEST;

        // Fila 1: proveedor, buscar y tipo de comprobante.
        c.gridy = 0; c.gridx = 0; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        filas.add(crearLabel("Proveedor"), c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        txtProveedor.setPreferredSize(new Dimension(280, 38));
        txtProveedor.setMinimumSize(new Dimension(120, 38));
        filas.add(txtProveedor, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        filas.add(btnBuscarProveedor, c);
        c.gridx = 3;
        filas.add(crearLabel("Tipo"), c);
        c.gridx = 4;
        cmbTipoFactura = new JComboBox<>(new String[]{"Factura A", "Factura B", "Factura C", "Remito", "Sin comprobante"});
        estilizarComboFiscal(cmbTipoFactura);
        cmbTipoFactura.setPreferredSize(new Dimension(162, 38));
        filas.add(cmbTipoFactura, c);
        cmbTipoFactura.addActionListener(e -> {
            ajusteIvaManual = null;
            if (txtNumeroFactura != null) {
                boolean sinComprobante = "Sin comprobante".equals(cmbTipoFactura.getSelectedItem());
                txtNumeroFactura.setEnabled(!sinComprobante);
                if (sinComprobante) txtNumeroFactura.setText("");
            }
        });

        // Fila 2: número completo, fecha y botón de conciliación.
        c.gridy = 1; c.gridx = 0;
        filas.add(crearLabel("Comprobante N°"), c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        txtNumeroFactura.setPreferredSize(new Dimension(280, 38));
        txtNumeroFactura.setMinimumSize(new Dimension(120, 38));
        txtNumeroFactura.setToolTipText("Ejemplo: 0003-00001258");
        filas.add(txtNumeroFactura, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        filas.add(crearLabel("Fecha"), c);
        c.gridx = 3; c.gridwidth = 1;
        txtFecha.setPreferredSize(new Dimension(116, 38));
        txtFecha.setMinimumSize(new Dimension(105, 38));
        filas.add(txtFecha, c);
        c.gridx = 4;
        btnDetalleFiscal = new JButton("Ver impuestos");
        btnDetalleFiscal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalleFiscal.setBackground(new Color(25, 75, 153));
        btnDetalleFiscal.setForeground(Color.WHITE);
        btnDetalleFiscal.setOpaque(true);
        btnDetalleFiscal.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnDetalleFiscal.setFocusPainted(false);
        btnDetalleFiscal.setPreferredSize(new Dimension(162, 38));
        btnDetalleFiscal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalleFiscal.addActionListener(e -> abrirDetalleFiscal());
        filas.add(btnDetalleFiscal, c);

        // Fila 3: total de factura requerido para conciliar antes de guardar.
        c.gridy = 2; c.gridx = 0; c.gridwidth = 1;
        c.weightx = 0; c.fill = GridBagConstraints.NONE;
        filas.add(crearLabel("Total factura ($)"), c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        txtTotalFactura = new JTextField();
        txtTotalFactura.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTotalFactura.setForeground(AZUL_OSCURO);
        txtTotalFactura.setBackground(Color.WHITE);
        txtTotalFactura.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), new EmptyBorder(6, 12, 6, 12)));
        txtTotalFactura.setPreferredSize(new Dimension(280, 38));
        txtTotalFactura.setToolTipText("Ingresá el total final del comprobante. Ejemplo: 106000,00");
        filas.add(txtTotalFactura, c);
        c.gridx = 2; c.gridwidth = 3; c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        lblResumenFiscal = new JLabel("El sistema verifica el total antes de confirmar");
        lblResumenFiscal.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblResumenFiscal.setForeground(AZUL_OSCURO);
        filas.add(lblResumenFiscal, c);
        cmbTipoFactura.addActionListener(e -> {
            boolean requiereTotal = !"Sin comprobante".equals(cmbTipoFactura.getSelectedItem())
                    && !"Remito".equals(cmbTipoFactura.getSelectedItem());
            txtTotalFactura.setEnabled(requiereTotal);
            if (!requiereTotal) txtTotalFactura.setText("");
        });

        panel.add(filas, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(1010, 218));
        panel.setMinimumSize(new Dimension(820, 218));
        return panel;
    }

    private void estilizarComboFiscal(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(AZUL_OSCURO);
        combo.setFocusable(false);
        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list,
                    Object value, int index, boolean selected, boolean focused) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, selected, focused);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(new EmptyBorder(7, 10, 7, 7));
                label.setBackground(selected ? new Color(225, 236, 251) : Color.WHITE);
                label.setForeground(AZUL_OSCURO);
                return label;
            }
        });
    }

    private BigDecimal totalDetalleCompra() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleEntradaTemporal d : detalles) total = total.add(d.getSubtotal());
        return total;
    }

    // Carga rápida: el precio ingresado es el precio FINAL que cobra el proveedor.
    // El IVA se desglosa sólo informativamente en Factura A y no se suma de nuevo.
    private void actualizarResumenFiscal() {
        if (lblResumenFiscal == null || cmbTipoFactura == null) return;
        BigDecimal total = totalDetalleCompra();
        String tipo = (String) cmbTipoFactura.getSelectedItem();
        BigDecimal iva = calcularIvaEstimado();
        String mensaje = "Calculado: " + formatoFiscal(total);
        if ("Factura A".equals(tipo)) {
            mensaje += "  |  IVA incluido estimado: " + formatoFiscal(iva);
        }
        String ingresado = txtTotalFactura == null ? "" : txtTotalFactura.getText().trim();
        if (!ingresado.isEmpty()) {
            try {
                BigDecimal declarado = parsearImporteFiscal(ingresado);
                BigDecimal diferencia = declarado.subtract(total).setScale(2, RoundingMode.HALF_UP);
                mensaje += "  |  Diferencia: " + formatoFiscal(diferencia);
                lblResumenFiscal.setForeground(diferencia.signum() == 0 ? new Color(0, 125, 78) : new Color(178, 75, 24));
            } catch (NumberFormatException ex) {
                mensaje = "Revisá el formato del total de factura";
                lblResumenFiscal.setForeground(new Color(178, 75, 24));
            }
        } else {
            lblResumenFiscal.setForeground(AZUL_OSCURO);
        }
        lblResumenFiscal.setText(mensaje);
    }

    private BigDecimal parsearImporteFiscal(String texto) {
        String valor = texto.trim().replace("$", "").replace(" ", "");
        if (valor.contains(",")) valor = valor.replace(".", "").replace(',', '.');
        BigDecimal numero = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
        if (numero.signum() < 0) throw new NumberFormatException("Importe negativo");
        return numero;
    }

    private boolean validarConciliacionFactura() {
        String tipo = (String) cmbTipoFactura.getSelectedItem();
        if ("Sin comprobante".equals(tipo) || "Remito".equals(tipo)) return true;
        if (txtNumeroFactura.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresá el número de la factura del proveedor.",
                    "Comprobante", JOptionPane.WARNING_MESSAGE);
            txtNumeroFactura.requestFocusInWindow();
            return false;
        }
        if (txtTotalFactura == null) {
            JOptionPane.showMessageDialog(this, "No se inicializó el campo Total factura. Cerrá y volvé a abrir la entrada.",
                    "Error del formulario", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String texto = txtTotalFactura.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresá el total final que figura en la factura.",
                    "Conciliación", JOptionPane.WARNING_MESSAGE);
            txtTotalFactura.requestFocusInWindow();
            return false;
        }
        try {
            BigDecimal diferencia = parsearImporteFiscal(texto).subtract(totalDetalleCompra());
            if (diferencia.abs().compareTo(new BigDecimal("0.01")) > 0) {
                JOptionPane.showMessageDialog(this,
                        "La factura y los productos no coinciden.\nDiferencia: " + formatoFiscal(diferencia)
                        + "\nRevisá cantidades, precios, descuentos, percepciones o impuestos."
                        + "\nNo se modificó el stock.",
                        "Revisar diferencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El total de factura no es válido.",
                    "Conciliación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void abrirDetalleFiscal() {
        String tipo = (String) cmbTipoFactura.getSelectedItem();
        boolean discrimina = "Factura A".equals(tipo);
        JDialog dialogo = new JDialog(this, "Serena Soft | IVA y conciliación", true);
        dialogo.setSize(690, 580);
        dialogo.setMinimumSize(new Dimension(610, 520));
        dialogo.setLocationRelativeTo(this);
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(new EmptyBorder(18, 22, 18, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 5, 7, 5);
        JLabel titulo = new JLabel("DATOS FISCALES DE LA COMPRA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titulo.setForeground(new Color(19, 51, 105));
        contenido.add(titulo, g);
        g.gridy++;
        JLabel info = new JLabel("" + tipo + "  |  El IVA recuperable depende de la condición fiscal.");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contenido.add(info, g);
        g.gridwidth = 1;
        g.gridy++; g.gridx = 0;
        contenido.add(crearLabel("Condición fiscal del comercio"), g);
        g.gridx = 1;
        JComboBox<String> condicion = new JComboBox<>(new String[]{"MONOTRIBUTISTA", "RESPONSABLE_INSCRIPTO"});
        condicion.setSelectedItem(condicionFiscal);
        condicion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contenido.add(condicion, g);
        g.gridy++; g.gridx = 0;
        contenido.add(crearLabel("Importe cargado en productos"), g);
        g.gridx = 1;
        JLabel totalProductos = new JLabel(formatoFiscal(totalDetalleCompra()));
        contenido.add(totalProductos, g);
        g.gridy++; g.gridx = 0;
        contenido.add(crearLabel("IVA estimado en Factura A"), g);
        g.gridx = 1;
        JLabel ivaEstimado = new JLabel(formatoFiscal(calcularIvaEstimado()));
        contenido.add(ivaEstimado, g);
        g.gridy++; g.gridx = 0;
        contenido.add(crearLabel("IVA según factura ($)"), g);
        g.gridx = 1;
        JTextField campoIva = new JTextField(ajusteIvaManual == null ? calcularIvaEstimado().toPlainString() : ajusteIvaManual.toPlainString());
        campoIva.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoIva.setEnabled(discrimina);
        contenido.add(campoIva, g);
        g.gridy++; g.gridx = 0;
        contenido.add(crearLabel("Motivo del ajuste"), g);
        g.gridx = 1;
        JTextField motivo = new JTextField(motivoAjusteIva);
        motivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        motivo.setEnabled(discrimina);
        contenido.add(motivo, g);
        g.gridy++; g.gridx = 0; g.gridwidth = 2;
        JLabel aviso = new JLabel("<html>Etapa inicial: Factura A con precios finales (IVA incluido), sin percepciones.<br>El crédito fiscal se guarda en cero hasta su validación contable.<br>Si la factura contiene descuentos no reflejados en los precios, percepciones u otros tributos, no la confirmes todavía.</html>");
        aviso.setForeground(new Color(135, 83, 15));
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contenido.add(aviso, g);
        g.gridy++;
        JButton aplicar = new JButton("Aplicar revisión");
        aplicar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        aplicar.setForeground(Color.WHITE);
        aplicar.setBackground(new Color(0, 145, 97));
        aplicar.addActionListener(e -> {
            try {
                BigDecimal nuevo = new BigDecimal(campoIva.getText().trim().replace(",", "."));
                if (nuevo.signum() < 0) throw new NumberFormatException();
                BigDecimal calculado = calcularIvaEstimado();
                if (discrimina && nuevo.compareTo(calculado) != 0 && motivo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Indicá el motivo de la corrección.");
                    return;
                }
                condicionFiscal = (String) condicion.getSelectedItem();
                PREFERENCIAS_FISCALES.put("condicionFiscalComercio", condicionFiscal);
                ajusteIvaManual = discrimina ? nuevo : null;
                motivoAjusteIva = motivo.getText().trim();
                dialogo.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El IVA debe ser un importe válido y no negativo.");
            }
        });
        contenido.add(aplicar, g);
        JScrollPane scrollFiscal = new JScrollPane(contenido);
        scrollFiscal.setBorder(BorderFactory.createEmptyBorder());
        scrollFiscal.getVerticalScrollBar().setUnitIncrement(16);
        dialogo.setContentPane(scrollFiscal);
        dialogo.setVisible(true);
    }

    private String formatoFiscal(BigDecimal importe) {
        return java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "AR")).format(importe);
    }

    private BigDecimal calcularIvaEstimado() {
        if (cmbTipoFactura == null || !"Factura A".equals(cmbTipoFactura.getSelectedItem())) return BigDecimal.ZERO;
        BigDecimal resultado = BigDecimal.ZERO;
        for (DetalleEntradaTemporal d : detalles) {
            if (d.getProducto().getTipoIva() == null || d.getProducto().getTipoIva().getPorcentaje() == null) continue;
            BigDecimal tasa = d.getProducto().getTipoIva().getPorcentaje();
            if (tasa.signum() <= 0) continue;
            // Los costos actuales se cargan como importes finales (IVA incluido).
            BigDecimal divisor = BigDecimal.ONE.add(tasa.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
            BigDecimal neto = d.getSubtotal().divide(divisor, 2, RoundingMode.HALF_UP);
            resultado = resultado.add(d.getSubtotal().subtract(neto));
        }
        return resultado.setScale(2, RoundingMode.HALF_UP);
    }

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
                        2,
                        2,
                        14,
                        10
                )
        );

        panelCampos.setOpaque(false);

        //======================================================
        // PRESENTACION
        //======================================================
        JPanel bloquePresentacion
                = crearBloqueVerticalProducto(
                        "PRESENTACIÓN",
                        crearSelectorPresentaciones()
                );

        cmbFormaCompra.setPreferredSize(new Dimension(190, 37));

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
                        105,
                        37
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
                        105,
                        37
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

        // PRECIOS: fila compacta, sin ampliar la ventana ni tapar la tabla.
        JPanel panelPrecios = new JPanel(new java.awt.GridLayout(1, 3, 14, 0));
        panelPrecios.setOpaque(false);
        txtPrecioVenta.setPreferredSize(new Dimension(130, 34));
        lblCostoStock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMargenVenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelPrecios.add(crearBloqueVerticalProducto(
                "PRECIO DE VENTA / UNIDAD STOCK", txtPrecioVenta));
        panelPrecios.add(crearBloqueVerticalProducto(
                "COSTO / UNIDAD STOCK", lblCostoStock));
        panelPrecios.add(crearBloqueVerticalProducto(
                "MARGEN BRUTO SOBRE VENTA", lblMargenVenta));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 5, 5, 5);
        panel.add(panelPrecios, c);

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
        c.gridy = 6;
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
        // Recalcular inmediatamente al escribir, sin esperar Tab.
        javax.swing.JFormattedTextField editorCantidad =
                ((JSpinner.DefaultEditor) spnCantidad.getEditor()).getTextField();
        // El editor del spinner se recrea al cambiar presentación; se conecta
        // también desde configurarSpinnerSegunUnidad().
        conectarEditorCantidad(editorCantidad);
        txtPrecioVenta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarPrecios(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarPrecios(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarPrecios(); }
        });
        txtCosto.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                        actualizarPrecios();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                        actualizarPrecios();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {

                        actualizarSubtotalItem();
                        actualizarPrecios();
                    }
                });

        //======================================================
        // FORMA DE COMPRA
        //======================================================
        cmbFormaCompra.addActionListener(e -> {

            if (productoSeleccionado != null && !cargandoPresentaciones) {
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
                        DetalleEntradaTemporal eliminado = detalles.remove(fila);
                        preciosVentaPendientes.remove(eliminado.getProducto().getIdProducto());
                        actualizarTotalCompra();
                    }

                });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar.addActionListener(e -> {

            modeloTabla.setRowCount(0);
            detalles.clear();
            preciosVentaPendientes.clear();
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
    private JPanel crearSelectorPresentaciones() {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        panel.setOpaque(false);
        panel.add(cmbFormaCompra, BorderLayout.CENTER);
        JButton agregar = new JButton("+ NUEVA");
        agregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        agregar.setPreferredSize(new Dimension(115, 50));
        agregar.setBackground(new Color(0, 151, 92));
        agregar.setForeground(Color.WHITE);
        agregar.addActionListener(e -> {
            if (productoSeleccionado == null || productoSeleccionado.getUnidadVenta() == null) {
                JOptionPane.showMessageDialog(this, "Seleccioná primero un artículo.");
                return;
            }
            Producto actual = productoSeleccionado;
            DialogoPresentacionesProducto dlg = new DialogoPresentacionesProducto(
                this, actual.getIdProducto(), actual.getNombre(),
                actual.getUnidadVenta().getIdUnidad(),
                actual.getUnidadVenta().getCodigo());
            dlg.setVisible(true);
            if (dlg.tieneCambios()) cargarProductoSeleccionado(actual);
        });
        panel.add(agregar, BorderLayout.EAST);
        return panel;
    }

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

        // Presentaciones reales de producto_presentacion (no solo unidad predeterminada).
        cargandoPresentaciones = true;
        presentacionesDisponibles.clear();
        cmbFormaCompra.removeAllItems();
        try {
            presentacionesDisponibles.addAll(
                    presentacionService.listarActivasPorProducto(producto.getIdProducto()));
            for (ProductoPresentacion presentacion : presentacionesDisponibles) {
                cmbFormaCompra.addItem(presentacion.getNombre()
                        + " (x" + formatearCantidad(presentacion.getFactorConversion()) + ")");
            }
        } catch (java.sql.SQLException ex) {
            cargandoPresentaciones = false;
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las presentaciones del producto:\n" + ex.getMessage(),
                    "Presentaciones", JOptionPane.ERROR_MESSAGE);
            limpiarProductoSeleccionado();
            return;
        }
        cargandoPresentaciones = false;
        if (presentacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Este producto no tiene presentaciones activas.\n"
                    + "Configurá sus presentaciones antes de ingresarlo al stock.",
                    "Sin presentaciones", JOptionPane.WARNING_MESSAGE);
            // Conservar el producto seleccionado para habilitar "+ NUEVA".
            return;
        }
        cmbFormaCompra.setSelectedIndex(0);
        aplicarFormaCompraSeleccionada();
        txtPrecioVenta.setText(producto.getPrecioVenta() == null
                ? "0" : producto.getPrecioVenta().stripTrailingZeros().toPlainString());
        actualizarIngresoStock();
        actualizarSubtotalItem();
        actualizarPrecios();
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
            conectarEditorCantidad(((JSpinner.DefaultEditor) spnCantidad.getEditor()).getTextField());
            estilizarSpinner(spnCantidad, false);
        } else {
            spnCantidad.setModel(
                    new SpinnerNumberModel(1, 1, 999999, 1)
            );
            spnCantidad.setEditor(
                    new JSpinner.NumberEditor(spnCantidad, "0")
            );
            conectarEditorCantidad(((JSpinner.DefaultEditor) spnCantidad.getEditor()).getTextField());
            estilizarSpinner(spnCantidad, false);
        }
    }

    //==========================================================
    // AGREGAR PRODUCTO
    //==========================================================
    private void conectarEditorCantidad(javax.swing.JFormattedTextField editor) {
        editor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void actualizar() {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        spnCantidad.commitEdit();
                        actualizarIngresoStock();
                        actualizarSubtotalItem();
                    } catch (java.text.ParseException | IllegalArgumentException ex) {
                        // Mientras el usuario escribe, el valor puede estar incompleto.
                    }
                });
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        });
    }

    private void actualizarPrecios() {
        if (productoSeleccionado == null) {
            lblCostoStock.setText("—");
            lblMargenVenta.setText("—");
            return;
        }
        BigDecimal costo = obtenerCosto();
        BigDecimal factor = obtenerFactorCompraActual();
        BigDecimal venta = obtenerPrecioVenta();
        if (costo == null || factor == null || factor.signum() <= 0) {
            lblCostoStock.setText("—");
            lblMargenVenta.setText("—");
            return;
        }
        BigDecimal unitario = costo.divide(factor, 2, RoundingMode.HALF_UP);
        lblCostoStock.setText(formatearMoneda(unitario));
        if (venta == null || venta.signum() <= 0) {
            lblMargenVenta.setText("—");
        } else {
            BigDecimal margen = venta.subtract(unitario).multiply(new BigDecimal("100"))
                    .divide(venta, 2, RoundingMode.HALF_UP);
            lblMargenVenta.setText(margen.toPlainString().replace('.', ',') + " %");
            lblMargenVenta.setForeground(margen.signum() < 0 ? ROJO : VERDE);
        }
    }

    private BigDecimal obtenerPrecioVenta() {
        try {
            String texto = txtPrecioVenta.getText().trim().replace(" ", "");
            if (texto.contains(",") && !texto.contains(".")) texto = texto.replace(',', '.');
            return new BigDecimal(texto);
        } catch (Exception ex) { return null; }
    }

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

        ProductoPresentacion presentacionElegida = obtenerPresentacionSeleccionada();
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

        if (presentacionElegida == null || presentacionElegida.getUnidadCompra() == null) {
            JOptionPane.showMessageDialog(this,
                    "Elegí una presentación válida del artículo antes de agregarlo.",
                    "Presentación obligatoria", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UnidadMedida unidadUsada = presentacionElegida.getUnidadCompra();

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

        try {
            cantidadStock = cantidadStock.setScale(3, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException ex) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad de stock supera los 3 decimales permitidos.",
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UnidadMedida unidadStock = productoSeleccionado.getUnidadVenta();
        if (unidadStock != null && !unidadStock.isPermiteDecimales()
                && cantidadStock.stripTrailingZeros().scale() > 0) {
            JOptionPane.showMessageDialog(this,
                    "El stock en " + codigoUnidad(unidadStock) + " debe ser entero.",
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (DetalleEntradaTemporal previo : detalles) {
            if (previo.getProducto().getIdProducto() == productoSeleccionado.getIdProducto()) {
                JOptionPane.showMessageDialog(this,
                        "Este producto ya está en el detalle.\n"
                        + "El servicio de compras actual admite una sola línea por producto.\n"
                        + "Eliminá la línea anterior antes de cargar otra presentación.",
                        "Producto repetido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

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

        BigDecimal nuevoPrecio = obtenerPrecioVenta();
        if (nuevoPrecio == null || nuevoPrecio.signum() < 0
                || nuevoPrecio.scale() > 2) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá un precio de venta válido (hasta 2 decimales).",
                    "Precio de venta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        BigDecimal precioAnterior = productoSeleccionado.getPrecioVenta();
        if (precioAnterior == null || nuevoPrecio.compareTo(precioAnterior) != 0) {
            preciosVentaPendientes.put(productoSeleccionado.getIdProducto(), nuevoPrecio);
        } else {
            preciosVentaPendientes.remove(productoSeleccionado.getIdProducto());
        }
        detalles.add(
                detalle
        );

        modeloTabla.addRow(
                new Object[]{
                    productoSeleccionado.getCodigo(),
                    productoSeleccionado.getNombre(),
                    formatearCantidad(cantidadCompra),
                    presentacionElegida.getNombre() + " [" + unidadCompraCodigo + "]",
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
        actualizarResumenFiscal();
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
        txtPrecioVenta.setText("");
        actualizarPrecios();

        cargandoPresentaciones = true;
        cmbFormaCompra.removeAllItems();
        presentacionesDisponibles.clear();
        cargandoPresentaciones = false;

        spnFactorCompra.setModel(
                new SpinnerNumberModel(
                        1.000,
                        0.001,
                        999999.000,
                        1.000
                )
        );

        spnFactorCompra.setEditor(
                new JSpinner.NumberEditor(spnFactorCompra, "0.###")
        );
        estilizarSpinner(spnFactorCompra, true);

        spnFactorCompra.setEnabled(false);
        spnFactorCompra.setVisible(false);
        lblFactorCompraTitulo.setVisible(false);

        spnCantidad.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
        spnCantidad.setEditor(new JSpinner.NumberEditor(spnCantidad, "0"));
        estilizarSpinner(spnCantidad, false);
        lblSubtotalItem.setText("$ 0,00");
    }

    // Ajuste visual: el número queda cómodo para seleccionar y editar.
    private void estilizarSpinner(JSpinner spinner, boolean soloLectura) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setBorder(BorderFactory.createLineBorder(new Color(199, 212, 230)));
        if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            javax.swing.JFormattedTextField campo =
                    ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
            campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            campo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            campo.setMargin(new Insets(3, 13, 3, 12));
            campo.setBackground(soloLectura ? new Color(246, 248, 252) : Color.WHITE);
            campo.setForeground(new Color(28, 54, 91));
            campo.setCaretColor(new Color(28, 54, 91));
        }
    }

    //==========================================================
    // FORMA DE COMPRA
    //==========================================================
    private void aplicarFormaCompraSeleccionada() {
        if (productoSeleccionado == null || cargandoPresentaciones) return;
        ProductoPresentacion presentacion = obtenerPresentacionSeleccionada();
        if (presentacion == null) return;

        String forma = codigoUnidad(presentacion.getUnidadCompra());
        BigDecimal factor = presentacion.getFactorConversion();
        spnFactorCompra.setValue(factor.doubleValue());
        // El factor procede de la BD: no debe poder cambiarse libremente.
        spnFactorCompra.setEnabled(false);
        boolean mostrarFactor = true;
        spnFactorCompra.setVisible(mostrarFactor);
        lblFactorCompraTitulo.setVisible(mostrarFactor);
        lblFactorCompraTitulo.setText("CONTENIDO POR " + presentacion.getNombre());
        lblFactorConversion.setText("1 " + presentacion.getNombre() + " = " + factor.stripTrailingZeros().toPlainString() + " " + codigoUnidad(productoSeleccionado.getUnidadVenta()));
        lblUnidadCantidad.setText(forma);
        lblCostoUnidad.setText("Costo por " + presentacion.getNombre());
        configurarSpinnerSegunUnidad(presentacion.getUnidadCompra());

        // producto.precio_compra representa costo por unidad de STOCK.
        // El costo de la presentación se sugiere multiplicando por su factor.
        BigDecimal costoBase = productoSeleccionado.getPrecioCompra();
        if (costoBase != null && costoBase.signum() >= 0) {
            BigDecimal sugerido = costoBase.multiply(factor)
                    .setScale(2, RoundingMode.HALF_UP);
            txtCosto.setText(sugerido.stripTrailingZeros().toPlainString());
        } else {
            txtCosto.setText("0");
        }
        txtCosto.setToolTipText("Costo sugerido según presentación predeterminada. "
                + "Reemplazalo por el costo real de la factura.");
        actualizarIngresoStock();
        actualizarSubtotalItem();
    }

    private ProductoPresentacion obtenerPresentacionSeleccionada() {
        int indice = cmbFormaCompra.getSelectedIndex();
        if (indice < 0 || indice >= presentacionesDisponibles.size()) return null;
        return presentacionesDisponibles.get(indice);
    }

    private String obtenerFormaCompraSeleccionada() {
        ProductoPresentacion presentacion = obtenerPresentacionSeleccionada();
        return presentacion == null ? null : codigoUnidad(presentacion.getUnidadCompra());
    }

    private BigDecimal obtenerFactorCompraActual() {
        ProductoPresentacion seleccionada = obtenerPresentacionSeleccionada();
        if (seleccionada == null) return null;
        BigDecimal factor = seleccionada.getFactorConversion();
        return factor != null && factor.signum() > 0 ? factor : null;
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

        // Conciliar antes de crear la compra o modificar existencias.
        if (!validarConciliacionFactura()) return;
        if ("Factura A".equals(cmbTipoFactura.getSelectedItem())
                && !validarFacturaAParaPersistir()) return;

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

            compra.setObservaciones("Entrada manual de mercadería | Tipo: "
                    + cmbTipoFactura.getSelectedItem());

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
            CompraFiscal fiscal = null;
            if ("Factura A".equals(cmbTipoFactura.getSelectedItem())) {
                fiscal = construirFiscalFacturaA(idCompra, proveedor);
            }
            ResultadoOperacion confirmar
                    = compraService.confirmarCompra(idCompra,
                            new java.util.HashMap<>(preciosVentaPendientes), fiscal);

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

    /** Primera integración: sólo Factura A, precios finales con IVA incluido.
     *  No se inventa crédito fiscal ni se asignan percepciones desconocidas. */
    private boolean validarFacturaAParaPersistir() {
        if (!"RESPONSABLE_INSCRIPTO".equals(condicionFiscal)) {
            JOptionPane.showMessageDialog(this,
                    "Para registrar esta Factura A, elegí RESPONSABLE_INSCRIPTO en Ver impuestos.\n"
                    + "La condición fiscal debe corresponder a tu empresa.",
                    "Configuración fiscal", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for (DetalleEntradaTemporal d : detalles) {
            if (d.getProducto().getTipoIva() == null
                    || d.getProducto().getTipoIva().getPorcentaje() == null) {
                if (!solicitarIvaFaltante(d.getProducto())) return false;
            }
        }
        // Al configurar una alícuota se recalcula el IVA antes de confirmar.
        if (ajusteIvaManual != null && ajusteIvaManual.subtract(calcularIvaEstimado()).abs()
                .compareTo(new BigDecimal("0.01")) > 0) {
            JOptionPane.showMessageDialog(this,
                    "Se actualizaron alícuotas. Revisá el IVA en 'Ver impuestos' antes de confirmar.",
                    "Revisar IVA", JOptionPane.WARNING_MESSAGE);
            ajusteIvaManual = null;
            return false;
        }
        return true;
    }

    /** Pide la alícuota real al operador y la guarda en el catálogo. */
    private boolean solicitarIvaFaltante(Producto producto) {
        try {
            java.util.List<TipoIva> opciones = productoDaoFiscal.listarIvasActivos();
            if (opciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay alícuotas activas en tipo_iva.",
                        "IVA", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            String[] etiquetas = new String[opciones.size()];
            for (int i = 0; i < opciones.size(); i++) {
                TipoIva t = opciones.get(i);
                etiquetas[i] = t.getNombre() + " (" + t.getPorcentaje().stripTrailingZeros().toPlainString() + " %)";
            }
            String elegida = (String) JOptionPane.showInputDialog(this,
                    "El producto '" + producto.getNombre() + "' no tiene IVA configurado.\n"
                    + "Seleccioná la alícuota que corresponde legalmente al producto.\n"
                    + "La selección quedará guardada para futuras compras.",
                    "Configurar IVA del producto", JOptionPane.QUESTION_MESSAGE,
                    null, etiquetas, etiquetas[0]);
            if (elegida == null) return false;
            int indice = java.util.Arrays.asList(etiquetas).indexOf(elegida);
            if (indice < 0) return false;
            TipoIva iva = opciones.get(indice);
            if (!productoDaoFiscal.asignarIvaSiFalta(producto.getIdProducto(), iva.getIdIva())) {
                JOptionPane.showMessageDialog(this,
                        "No se asignó el IVA: el producto pudo haber cambiado. Volvé a buscarlo.",
                        "IVA", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            producto.setTipoIva(iva);
            ajusteIvaManual = null; // Evita mantener el cálculo anterior.
            actualizarResumenFiscal();
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo configurar el IVA: " + ex.getMessage(),
                    "IVA", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private CompraFiscal construirFiscalFacturaA(long idCompra, Proveedor proveedor) {
        CompraFiscal f = new CompraFiscal();
        f.idCompra = idCompra;
        f.tipoComprobante = "FACTURA";
        f.letra = "A";
        String nro = txtNumeroFactura.getText().trim();
        String[] partes = nro.split("-", 2);
        f.puntoVenta = partes.length == 2 ? partes[0].trim() : null;
        f.numero = partes.length == 2 ? partes[1].trim() : nro;
        f.fechaEmision = LocalDate.parse(txtFecha.getText().trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        f.totalComprobante = parsearImporteFiscal(txtTotalFactura.getText());
        f.observacionesFiscales = "Carga desde entrada manual. IVA estimado por producto; "
                + "crédito fiscal pendiente de validación contable.";
        java.util.Map<BigDecimal, BigDecimal> totalesPorTasa = new java.util.TreeMap<>();
        for (DetalleEntradaTemporal d : detalles) {
            BigDecimal tasa = d.getProducto().getTipoIva().getPorcentaje();
            totalesPorTasa.merge(tasa, d.getSubtotal(), BigDecimal::add);
        }
        for (java.util.Map.Entry<BigDecimal, BigDecimal> e : totalesPorTasa.entrySet()) {
            BigDecimal tasa = e.getKey();
            BigDecimal total = e.getValue();
            if (tasa.signum() == 0) {
                f.importeExento = f.importeExento.add(total);
            } else {
                f.alicuotas.add(CompraFiscalService.desdePrecioFinal(total, tasa, false));
            }
        }
        CompraFiscalService.validar(f);
        return f;
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
