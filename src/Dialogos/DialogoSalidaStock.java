package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import Dao.DepositoDao;
import Dao.StockProductoDao;
import model.Deposito;
import model.Producto;
import model.SalidaStockDetalle;
import services.SalidaStockService;
import services.ResultadoOperacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoSalidaStock extends JDialog {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private static final Color AZUL_SERENA =
            new Color(25, 70, 145);

    private static final Color AZUL_HOVER =
            new Color(45, 95, 180);

    private static final Color FONDO =
            new Color(225, 232, 241);

    private static final Color BORDE =
            new Color(205, 215, 228);

    private static final Color TEXTO =
            new Color(45, 52, 62);

    private static final Color TEXTO_SECUNDARIO =
            new Color(100, 108, 120);

    private static final Color VERDE =
            new Color(25, 135, 84);

    private static final Color ROJO =
            new Color(190, 65, 65);

    private static final Color ROJO_HOVER =
            new Color(215, 75, 75);

    private static final Color GRIS_METAL =
            new Color(70, 78, 88);

    private static final Color GRIS_METAL_HOVER =
            new Color(88, 98, 110);

    private static final Color GRIS_CLARO =
            new Color(222, 227, 233);

    private static final Color GRIS_CLARO_HOVER =
            new Color(205, 212, 220);

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JComboBox<String> comboMotivo;
    private JTextField txtDestino;
    private JTextArea txtObservaciones;

    //==========================================================
    // PRODUCTO
    //==========================================================
    private JTextField txtProducto;
    private JButton btnBuscarProducto;

    private JLabel lblCodigo;
    private JLabel lblNombreProducto;
    private JLabel lblStockActual;

    private JSpinner spnCantidad;

    private JButton btnAgregarProducto;

    //==========================================================
    // PRODUCTO SELECCIONADO
    //==========================================================
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoDao stockDao = new StockProductoDao();
    private final SalidaStockService salidaService = new SalidaStockService();
    private final Map<String, Producto> productosDetalle = new LinkedHashMap<>();
    private Producto objetoSeleccionado;
    private Deposito depositoPrincipal;
    private String codigoSeleccionado;
    private String productoSeleccionado;

    private double stockActualSeleccionado;
    private double precioSeleccionado;

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
    private JButton btnRegistrarSalida;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblProductos;
    private JLabel lblUnidades;

    //==========================================================
    // RESULTADO
    //==========================================================
    private boolean confirmado = false;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoSalidaStock(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        cargarDatosPrueba();
        depositoPrincipal = depositoDao.buscarPrincipal();

        configurarEventos();

        setTitle(
                "Salida de Stock"
        );

        setModal(true);

        setSize(
                new Dimension(
                        920,
                        690
                )
        );

        setMinimumSize(
                new Dimension(
                        780,
                        580
                )
        );

        setResizable(true);
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        aplicarEstiloVisualSerena();
        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(
                parent
        );
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // DATOS GENERALES
        //======================================================
        comboMotivo =
                new JComboBox<>();

        comboMotivo.setPreferredSize(
                new Dimension(
                        230,
                        34
                )
        );

        comboMotivo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        txtDestino =
                crearCampoTexto();

        txtObservaciones =
                new JTextArea();

        txtObservaciones.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        txtObservaciones.setLineWrap(
                true
        );

        txtObservaciones.setWrapStyleWord(
                true
        );

        //======================================================
        // PRODUCTO
        //======================================================
        txtProducto =
                crearCampoTexto();

        txtProducto.setEditable(
                false
        );

        txtProducto.setBackground(
                new Color(
                        248,
                        249,
                        251
                )
        );

        btnBuscarProducto =
                crearBotonPrimario(
                        "BUSCAR",
                        105
                );

        lblCodigo =
                crearValorDetalle("-");

        lblNombreProducto =
                crearValorDetalle("-");

        lblStockActual =
                crearValorDetalle("-");

        lblStockActual.setForeground(
                VERDE
        );

        lblStockActual.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        spnCantidad =
                new JSpinner(
                        new SpinnerNumberModel(
                                1.0,
                                0.001,
                                999999.0,
                                1.0
                        )
                );

        spnCantidad.setPreferredSize(
                new Dimension(
                        140,
                        34
                )
        );

        btnAgregarProducto =
                crearBotonPrimario(
                        "AGREGAR PRODUCTO",
                        175
                );

        //======================================================
        // TABLA
        //======================================================
        inicializarTabla();

        //======================================================
        // ACCIONES
        //======================================================
        btnEliminarItem =
                crearBotonPeligro(
                        "ELIMINAR ITEM",
                        145
                );

        btnLimpiar =
                crearBotonClaro(
                        "LIMPIAR",
                        110
                );

        btnCancelar =
                crearBotonMetal(
                        "CANCELAR",
                        120
                );

        btnRegistrarSalida =
                crearBotonPrimario(
                        "REGISTRAR SALIDA",
                        175
                );

        //======================================================
        // RESUMEN
        //======================================================
        lblProductos =
                new JLabel(
                        "0"
                );

        lblProductos.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblProductos.setForeground(
                AZUL_OSCURO
        );

        lblUnidades =
                new JLabel(
                        "0"
                );

        lblUnidades.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblUnidades.setForeground(
                VERDE
        );
    }

    //==========================================================
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "Código",
                            "Producto",
                            "Cantidad",
                            "Stock Actual"
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

        tablaDetalle =
                new JTable(
                        modeloTabla
                );

        tablaDetalle.setRowHeight(
                30
        );

        tablaDetalle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaDetalle.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaDetalle.setSelectionBackground(
                new Color(
                        215,
                        228,
                        245
                )
        );

        tablaDetalle.setSelectionForeground(
                Color.BLACK
        );

        tablaDetalle.setGridColor(
                new Color(
                        225,
                        230,
                        237
                )
        );

        tablaDetalle.setShowHorizontalLines(
                true
        );

        tablaDetalle.setShowVerticalLines(
                false
        );

        tablaDetalle.setFillsViewportHeight(
                true
        );

        tablaDetalle
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaDetalle
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        aplicarHeaderAzul();

        tablaDetalle
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(
                        110
                );

        tablaDetalle
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(
                        360
                );

        tablaDetalle
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(
                        120
                );

        tablaDetalle
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(
                        130
                );
    }

    //==========================================================
    // CONSTRUIR DIALOGO
    //==========================================================
    private void construirDialogo() {

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
        add(
                crearHeader(),
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel contenido =
                new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setBackground(
                FONDO
        );

        contenido.setBorder(
                new EmptyBorder(
                        15,
                        18,
                        15,
                        18
                )
        );

        JPanel datos =
                crearPanelDatosSalida();

        JPanel producto =
                crearPanelProducto();

        JPanel detalle =
                crearPanelDetalle();

        JPanel resumen =
                crearPanelResumen();

        datos.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        producto.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        detalle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        resumen.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(
                datos
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                producto
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                detalle
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                resumen
        );

        contenido.add(
                Box.createVerticalStrut(
                        8
                )
        );

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scrollGeneral =
                new JScrollPane(
                        contenido
                );

        scrollGeneral.setBorder(
                null
        );

        scrollGeneral.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollGeneral.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollGeneral
                .getVerticalScrollBar()
                .setUnitIncrement(
                        18
                );

        scrollGeneral
                .getViewport()
                .setBackground(
                        FONDO
                );

        add(
                scrollGeneral,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER FIJO
        //======================================================
        add(
                crearFooter(),
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // HEADER
    //==========================================================
    private JPanel crearHeader() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, AZUL_SERENA),
                new EmptyBorder(17, 25, 15, 25)
        ));

        JLabel titulo =
                new JLabel(
                        "SALIDA DE STOCK"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        titulo.setForeground(
                AZUL_SERENA
        );
        titulo.setIcon(new IconoSerena("salida", AZUL_SERENA, 27));
        titulo.setIconTextGap(12);

        JLabel subtitulo =
                new JLabel(
                        "Registre uno o varios productos que egresan del inventario"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(
                titulo
        );

        panel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    //==========================================================
    // DATOS DE LA SALIDA
    //==========================================================
    private JPanel crearPanelDatosSalida() {

        JPanel panel =
                crearTarjetaSeccion(
                        "DATOS DE LA SALIDA"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // MOTIVO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Motivo"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                comboMotivo,
                c
        );

        //======================================================
        // DESTINO
        //======================================================
        c.gridx = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Destino"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                txtDestino,
                c
        );

        //======================================================
        // OBSERVACIONES
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Observaciones"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.BOTH;

        JScrollPane scroll =
                new JScrollPane(
                        txtObservaciones
                );

        scroll.setPreferredSize(
                new Dimension(
                        600,
                        70
                )
        );

        panel.add(
                scroll,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        175
                )
        );

        return panel;
    }

    //==========================================================
    // PRODUCTO
    //==========================================================
    private JPanel crearPanelProducto() {

        JPanel panel =
                crearTarjetaSeccion(
                        "AGREGAR PRODUCTO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // BUSCADOR
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Producto"
                ),
                c
        );

        JPanel panelBuscar =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        panelBuscar.setOpaque(
                false
        );

        panelBuscar.add(
                txtProducto,
                BorderLayout.CENTER
        );

        panelBuscar.add(
                btnBuscarProducto,
                BorderLayout.EAST
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                panelBuscar,
                c
        );

        //======================================================
        // CODIGO
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Código"
                ),
                c
        );

        c.gridx = 1;

        panel.add(
                lblCodigo,
                c
        );

        //======================================================
        // NOMBRE
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Producto"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblNombreProducto,
                c
        );

        //======================================================
        // STOCK
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

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
        // CANTIDAD
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Cantidad"
                ),
                c
        );

        c.gridx = 3;

        panel.add(
                spnCantidad,
                c
        );

        //======================================================
        // BOTON
        //======================================================
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.anchor =
                GridBagConstraints.EAST;

        panel.add(
                btnAgregarProducto,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        210
                )
        );

        return panel;
    }

    //==========================================================
    // DETALLE
    //==========================================================
    private JPanel crearPanelDetalle() {

        JPanel panel =
                crearTarjetaSeccion(
                        "DETALLE DE LA SALIDA"
                );

        panel.setLayout(
                new BorderLayout(
                        0,
                        8
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaDetalle
                );

        scroll.setPreferredSize(
                new Dimension(
                        700,
                        190
                )
        );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel acciones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                4
                        )
                );

        acciones.setOpaque(
                false
        );

        acciones.add(
                btnEliminarItem
        );

        panel.add(
                acciones,
                BorderLayout.SOUTH
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        285
                )
        );

        return panel;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel =
                crearTarjetaSeccion(
                        "RESUMEN"
                );

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        30,
                        10
                )
        );

        JLabel tituloProductos =
                crearLabel(
                        "Productos:"
                );

        JLabel tituloUnidades =
                crearLabel(
                        "Unidades:"
                );

        panel.add(
                tituloProductos
        );

        panel.add(
                lblProductos
        );

        panel.add(
                tituloUnidades
        );

        panel.add(
                lblUnidades
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        80
                )
        );

        return panel;
    }

    //==========================================================
    // FOOTER
    //==========================================================
    private JPanel crearFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                11
                        )
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDE
                )
        );

        footer.add(
                btnLimpiar
        );

        footer.add(
                btnCancelar
        );

        footer.add(
                btnRegistrarSalida
        );

        return footer;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        comboMotivo.removeAllItems();

        comboMotivo.addItem(
                "Seleccione..."
        );

        comboMotivo.addItem(
                "Venta / despacho"
        );

        comboMotivo.addItem(
                "Consumo interno"
        );

        comboMotivo.addItem(
                "Rotura"
        );

        comboMotivo.addItem(
                "Vencimiento"
        );

        comboMotivo.addItem(
                "Traslado"
        );

        comboMotivo.addItem(
                "Donación"
        );

        comboMotivo.addItem(
                "Otro"
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // BUSCAR PRODUCTO
        //======================================================
        btnBuscarProducto.addActionListener(e -> {

            DialogoBuscarProducto dialogo =
                    new DialogoBuscarProducto(
                            this
                    );

            dialogo.setVisible(
                    true
            );

            if (!dialogo.isSeleccionado()) {

                return;
            }

            objetoSeleccionado = dialogo.getObjetoProductoSeleccionado();
            if (objetoSeleccionado == null || depositoPrincipal == null) {
                JOptionPane.showMessageDialog(this, "Producto o depósito no disponible.");
                return;
            }
            codigoSeleccionado = objetoSeleccionado.getCodigo();
            productoSeleccionado = objetoSeleccionado.getNombre();
            BigDecimal disponible = stockDao.obtenerCantidad(
                    objetoSeleccionado.getIdProducto(), depositoPrincipal.getIdDeposito());
            stockActualSeleccionado = disponible.doubleValue();
            txtProducto.setText(productoSeleccionado);
            lblCodigo.setText(codigoSeleccionado);
            lblNombreProducto.setText(productoSeleccionado);
            String unidad = objetoSeleccionado.getUnidadVenta() == null
                    ? "" : objetoSeleccionado.getUnidadVenta().getCodigo();
            lblStockActual.setText(disponible.stripTrailingZeros().toPlainString() + " " + unidad);
            spnCantidad.setValue(1.0);
        });

        //======================================================
        // AGREGAR
        //======================================================
        btnAgregarProducto.addActionListener(e -> {

            agregarProducto();
        });

        //======================================================
        // ELIMINAR
        //======================================================
        btnEliminarItem.addActionListener(e -> {

            eliminarItem();
        });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar.addActionListener(e -> {

            limpiarSalida();
        });

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar.addActionListener(e -> {

            confirmado =
                    false;

            dispose();
        });

        //======================================================
        // REGISTRAR
        //======================================================
        btnRegistrarSalida.addActionListener(e -> {

            registrarSalida();
        });
    }

    //==========================================================
    // AGREGAR PRODUCTO
    //==========================================================
    private void agregarProducto() {
        if (objetoSeleccionado == null || depositoPrincipal == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto y verifique el depósito.");
            return;
        }
        BigDecimal cantidad = new BigDecimal(spnCantidad.getValue().toString());
        if (cantidad.signum() <= 0 || cantidad.scale() > 3) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida (máximo 3 decimales).");
            return;
        }
        String unidad = objetoSeleccionado.getUnidadVenta() == null
                ? "" : objetoSeleccionado.getUnidadVenta().getCodigo();
        if ("UN".equalsIgnoreCase(unidad) && cantidad.stripTrailingZeros().scale() > 0) {
            JOptionPane.showMessageDialog(this, "Este producto requiere unidades enteras.");
            return;
        }
        BigDecimal disponible = stockDao.obtenerCantidad(
                objetoSeleccionado.getIdProducto(), depositoPrincipal.getIdDeposito());
        int fila = buscarFilaProducto(codigoSeleccionado);
        BigDecimal anterior = fila < 0 ? BigDecimal.ZERO
                : new BigDecimal(modeloTabla.getValueAt(fila, 2).toString());
        BigDecimal total = anterior.add(cantidad);
        if (total.compareTo(disponible) > 0) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: "
                    + disponible.stripTrailingZeros().toPlainString() + " " + unidad);
            return;
        }
        productosDetalle.put(codigoSeleccionado, objetoSeleccionado);
        if (fila >= 0) {
            modeloTabla.setValueAt(total, fila, 2);
            modeloTabla.setValueAt(disponible, fila, 3);
        } else {
            modeloTabla.addRow(new Object[]{codigoSeleccionado, productoSeleccionado,
                    cantidad, disponible});
        }
        actualizarResumen();
        limpiarProductoSeleccionado();
    }

    //==========================================================
    // BUSCAR FILA
    //==========================================================
    private int buscarFilaProducto(
            String codigo) {

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

            if (codigoTabla.equals(
                    codigo
            )) {

                return i;
            }
        }

        return -1;
    }

    //==========================================================
    // CANTIDAD YA AGREGADA
    //==========================================================
    private double obtenerCantidadYaAgregada(
            String codigo) {

        int fila =
                buscarFilaProducto(
                        codigo
                );

        if (fila == -1) {

            return 0;
        }

        return Double.parseDouble(
                modeloTabla
                        .getValueAt(
                                fila,
                                2
                        )
                        .toString()
        );
    }

    //==========================================================
    // ELIMINAR ITEM
    //==========================================================
    private void eliminarItem() {

        int fila =
                tablaDetalle
                        .getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto de la tabla.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String producto =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea quitar de la salida:\n\n"
                        + producto
                        + "?",
                        "Eliminar Producto",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion
                == JOptionPane.YES_OPTION) {

            productosDetalle.remove(modeloTabla.getValueAt(fila, 0).toString());
            modeloTabla.removeRow(
                    fila
            );

            actualizarResumen();
        }
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private void actualizarResumen() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total = total.add(new BigDecimal(modeloTabla.getValueAt(i, 2).toString()));
        }
        lblProductos.setText(String.valueOf(modeloTabla.getRowCount()));
        lblUnidades.setText(total.stripTrailingZeros().toPlainString());
    }

    //==========================================================
    // REGISTRAR SALIDA
    //==========================================================
    private void registrarSalida() {
        if (depositoPrincipal == null) {
            JOptionPane.showMessageDialog(this, "No existe depósito principal activo.");
            return;
        }
        if (comboMotivo.getSelectedIndex() <= 0 || modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un motivo y agregue productos.");
            return;
        }
        String motivo = comboMotivo.getSelectedItem().toString();
        if ("Traslado".equalsIgnoreCase(motivo)
                || "Venta / despacho".equalsIgnoreCase(motivo)) {
            JOptionPane.showMessageDialog(this,
                    "Los traslados y las ventas deben registrarse desde sus módulos específicos.");
            return;
        }
        java.util.List<SalidaStockDetalle> items = new java.util.ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String codigo = modeloTabla.getValueAt(i, 0).toString();
            Producto producto = productosDetalle.get(codigo);
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "No se pudo identificar: " + codigo);
                return;
            }
            SalidaStockDetalle item = new SalidaStockDetalle();
            item.setProducto(producto);
            item.setCantidad(new BigDecimal(modeloTabla.getValueAt(i, 2).toString()));
            items.add(item);
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Confirmar salida de " + items.size() + " productos del depósito "
                + depositoPrincipal.getNombre() + "?\nEl stock se descontará definitivamente.",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        btnRegistrarSalida.setEnabled(false);
        try {
            ResultadoOperacion resultado = salidaService.registrarSalidaCompleta(
                    depositoPrincipal.getIdDeposito(), motivo,
                    txtDestino.getText(), txtObservaciones.getText(), items);
            if (!resultado.isExitoso()) {
                JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                        "No se registró la salida", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmado = true;
            JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                    "Salida confirmada", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar. Verifique el historial antes de reintentar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnRegistrarSalida.setEnabled(true);
        }
    }

    //==========================================================
    // LIMPIAR SALIDA
    //==========================================================
    private void limpiarSalida() {
        productosDetalle.clear();

        modeloTabla.setRowCount(
                0
        );

        comboMotivo.setSelectedIndex(
                0
        );

        txtDestino.setText(
                ""
        );

        txtObservaciones.setText(
                ""
        );

        limpiarProductoSeleccionado();

        actualizarResumen();
    }

    //==========================================================
    // LIMPIAR PRODUCTO
    //==========================================================
    private void limpiarProductoSeleccionado() {
        objetoSeleccionado = null;

        codigoSeleccionado =
                null;

        productoSeleccionado =
                null;

        stockActualSeleccionado =
                0;

        precioSeleccionado =
                0;

        txtProducto.setText(
                ""
        );

        lblCodigo.setText(
                "-"
        );

        lblNombreProducto.setText(
                "-"
        );

        lblStockActual.setText(
                "-"
        );

        spnCantidad.setValue(
                1.0
        );
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
                                12
                        )
                );

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                label.setOpaque(
                        true
                );

                return label;
            }
        };

        for (int i = 0;
             i < tablaDetalle.getColumnCount();
             i++) {

            tablaDetalle
                    .getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(
                            renderer
                    );
        }
    }

    //==========================================================
    // TARJETA
    //==========================================================
    private JPanel crearTarjetaSeccion(
            String titulo) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        titulo,
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                14
                        ),
                        AZUL_OSCURO
                )
        );

        return panel;
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        7,
                        9,
                        7,
                        9
                );

        c.anchor =
                GridBagConstraints.WEST;

        return c;
    }

    //==========================================================
    // CAMPO
    //==========================================================
    private JTextField crearCampoTexto() {

        JTextField campo =
                new JTextField();

        campo.setPreferredSize(
                new Dimension(
                        240,
                        34
                )
        );

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        return campo;
    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // VALOR
    //==========================================================
    private JLabel crearValorDetalle(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // BOTON PRIMARIO
    //==========================================================
    private JButton crearBotonPrimario(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                AZUL_SERENA
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        AZUL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        AZUL_SERENA
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON METAL
    //==========================================================
    private JButton crearBotonMetal(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_METAL
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON CLARO
    //==========================================================
    private JButton crearBotonClaro(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_CLARO
        );

        boton.setForeground(
                TEXTO
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_CLARO_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_CLARO
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON PELIGRO
    //==========================================================
    private JButton crearBotonPeligro(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_METAL
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        ROJO_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON BASE
    //==========================================================
    private JButton crearBotonBase(
            String texto,
            int ancho) {

        JButton boton =
                new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        38
                )
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        boton.setFocusPainted(
                false
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    //==========================================================
    // FORMATO CANTIDAD
    //==========================================================
    private String formatearCantidad(
            double valor) {

        DecimalFormat formato =
                new DecimalFormat(
                        "#,##0.###"
                );

        return formato.format(
                valor
        );
    }

    //==========================================================
    // GETTERS
    //==========================================================
    public boolean isConfirmado() {

        return confirmado;
    }

    public String getMotivo() {

        Object valor =
                comboMotivo
                        .getSelectedItem();

        return valor == null
                ? ""
                : valor.toString();
    }

    public String getDestino() {

        return txtDestino
                .getText()
                .trim();
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }

    public DefaultTableModel getModeloDetalle() {

        return modeloTabla;
    }

    // =========================================================
    // RETOQUES VISUALES - NO MODIFICAN LA LOGICA DE STOCK
    // =========================================================
    private void aplicarEstiloVisualSerena() {
        personalizarBoton(btnBuscarProducto, "buscar");
        personalizarBoton(btnAgregarProducto, "agregar");
        personalizarBoton(btnEliminarItem, "eliminar");
        personalizarBoton(btnLimpiar, "limpiar");
        personalizarBoton(btnCancelar, "cancelar");
        personalizarBoton(btnRegistrarSalida, "confirmar");

        btnRegistrarSalida.setBackground(VERDE);
        btnRegistrarSalida.setForeground(Color.WHITE);
        btnRegistrarSalida.setPreferredSize(new Dimension(195, 40));
        btnAgregarProducto.setPreferredSize(new Dimension(195, 40));
        btnBuscarProducto.setPreferredSize(new Dimension(118, 40));

        tablaDetalle.setRowHeight(33);
        tablaDetalle.setShowVerticalLines(false);
        tablaDetalle.setGridColor(new Color(232, 237, 245));
        tablaDetalle.setSelectionBackground(new Color(211, 227, 249));
        tablaDetalle.setSelectionForeground(AZUL_OSCURO);
        tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDetalle.getTableHeader().setPreferredSize(new Dimension(0, 38));
        tablaDetalle.getTableHeader().setReorderingAllowed(false);
        aplicarHeaderAzul();
    }

    private void personalizarBoton(JButton boton, String tipo) {
        if (boton == null) return;
        boton.setIcon(new IconoSerena(tipo, Color.WHITE, 15));
        boton.setIconTextGap(8);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(7, 10, 7, 10));
    }

    /** Iconos vectoriales: no requieren archivos PNG ni dependencias. */
    private static final class IconoSerena implements Icon {
        private final String tipo;
        private final Color color;
        private final int tam;

        IconoSerena(String tipo, Color color, int tam) {
            this.tipo = tipo;
            this.color = color;
            this.tam = tam;
        }

        @Override public int getIconWidth() { return tam; }
        @Override public int getIconHeight() { return tam; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.translate(x, y);
                g2.scale(tam / 24.0, tam / 24.0);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                switch (tipo) {
                    case "buscar":
                        g2.drawOval(3, 3, 13, 13);
                        g2.drawLine(15, 15, 22, 22);
                        break;
                    case "agregar":
                        g2.drawRoundRect(3, 3, 18, 18, 4, 4);
                        g2.drawLine(12, 7, 12, 17);
                        g2.drawLine(7, 12, 17, 12);
                        break;
                    case "eliminar":
                        g2.drawLine(5, 6, 19, 6);
                        g2.drawLine(9, 3, 15, 3);
                        g2.drawRoundRect(7, 7, 10, 14, 2, 2);
                        g2.drawLine(10, 11, 10, 17);
                        g2.drawLine(14, 11, 14, 17);
                        break;
                    case "limpiar":
                        g2.drawArc(4, 4, 16, 16, 45, 280);
                        g2.drawLine(19, 4, 20, 10);
                        g2.drawLine(20, 10, 14, 9);
                        break;
                    case "cancelar":
                        g2.drawLine(5, 5, 19, 19);
                        g2.drawLine(19, 5, 5, 19);
                        break;
                    case "confirmar":
                        g2.drawOval(2, 2, 20, 20);
                        g2.drawLine(6, 12, 10, 16);
                        g2.drawLine(10, 16, 18, 8);
                        break;
                    case "salida":
                        g2.drawRoundRect(2, 5, 13, 15, 2, 2);
                        g2.drawLine(10, 12, 22, 12);
                        g2.drawLine(17, 7, 22, 12);
                        g2.drawLine(22, 12, 17, 17);
                        break;
                    default: break;
                }
            } finally {
                g2.dispose();
            }
        }
    }

}