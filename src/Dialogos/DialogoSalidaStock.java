package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;

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

        panel.setBorder(
                new EmptyBorder(
                        17,
                        25,
                        15,
                        25
                )
        );

        JLabel titulo =
                new JLabel(
                        "SALIDA DE STOCK"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

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

            codigoSeleccionado =
                    dialogo.getCodigoSeleccionado();

            productoSeleccionado =
                    dialogo.getProductoSeleccionado();

            stockActualSeleccionado =
                    dialogo.getStockSeleccionado();

            precioSeleccionado =
                    dialogo.getPrecioSeleccionado();

            txtProducto.setText(
                    productoSeleccionado
            );

            lblCodigo.setText(
                    codigoSeleccionado
            );

            lblNombreProducto.setText(
                    productoSeleccionado
            );

            lblStockActual.setText(
                    formatearCantidad(
                            stockActualSeleccionado
                    )
                    + " unidades"
            );

            spnCantidad.setValue(
                    1.0
            );
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

        if (codigoSeleccionado == null
                || productoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto.",
                    "Salida de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double cantidad =
                ((Number)
                spnCantidad.getValue())
                        .doubleValue();

        if (cantidad <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad debe ser mayor a cero.",
                    "Cantidad inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        //======================================================
        // VER CUANTO YA AGREGAMOS DEL MISMO PRODUCTO
        //======================================================
        double yaAgregado =
                obtenerCantidadYaAgregada(
                        codigoSeleccionado
                );

        if ((yaAgregado + cantidad)
                > stockActualSeleccionado) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad supera el stock disponible.\n\n"
                    + "Stock actual: "
                    + formatearCantidad(
                            stockActualSeleccionado
                    )
                    + "\n"
                    + "Ya agregado: "
                    + formatearCantidad(
                            yaAgregado
                    )
                    + "\n"
                    + "Intentando agregar: "
                    + formatearCantidad(
                            cantidad
                    ),
                    "Stock insuficiente",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        //======================================================
        // SI YA EXISTE, SUMAMOS
        //======================================================
        int filaExistente =
                buscarFilaProducto(
                        codigoSeleccionado
                );

        if (filaExistente != -1) {

            double cantidadAnterior =
                    Double.parseDouble(
                            modeloTabla
                                    .getValueAt(
                                            filaExistente,
                                            2
                                    )
                                    .toString()
                    );

            modeloTabla.setValueAt(
                    cantidadAnterior
                    + cantidad,
                    filaExistente,
                    2
            );

        } else {

            modeloTabla.addRow(
                    new Object[]{
                        codigoSeleccionado,
                        productoSeleccionado,
                        cantidad,
                        stockActualSeleccionado
                    }
            );
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

        int productos =
                modeloTabla.getRowCount();

        double unidades =
                0;

        for (int i = 0;
             i < modeloTabla.getRowCount();
             i++) {

            unidades +=
                    Double.parseDouble(
                            modeloTabla
                                    .getValueAt(
                                            i,
                                            2
                                    )
                                    .toString()
                    );
        }

        lblProductos.setText(
                String.valueOf(
                        productos
                )
        );

        lblUnidades.setText(
                formatearCantidad(
                        unidades
                )
        );
    }

    //==========================================================
    // REGISTRAR SALIDA
    //==========================================================
    private void registrarSalida() {

        if (comboMotivo.getSelectedIndex()
                == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el motivo de la salida.",
                    "Salida de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            comboMotivo.requestFocus();

            return;
        }

        if (modeloTabla.getRowCount()
                == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Agregue al menos un producto.",
                    "Salida de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar la salida de stock?\n\n"
                        + "Productos: "
                        + lblProductos.getText()
                        + "\n"
                        + "Unidades: "
                        + lblUnidades.getText(),
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        confirmado =
                true;

        JOptionPane.showMessageDialog(
                this,
                "Salida preparada correctamente.\n"
                + "Más adelante se descontará el stock en MySQL.",
                "Salida Registrada",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // LIMPIAR SALIDA
    //==========================================================
    private void limpiarSalida() {

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
}