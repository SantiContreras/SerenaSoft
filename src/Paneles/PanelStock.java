/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Dialogos.DialogoAjusteStock;
import Dialogos.DialogoDarDeBajaProducto;
import Dialogos.DialogoEntradaManual;
import Dialogos.DialogoEntradaStock;
import Dialogos.DialogoModificarStock;
import Dialogos.DialogoSalidaStock;
import Dialogos.DialogoTipoEntrada;
import Diseños.BotonAccion;
import Diseños.PanelRedondeado;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import rojeru_san.rsbutton.RSButtonRound;
import Diseños.TablaEstilo;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import rojeru_san.rsbutton.RSButtonMetro;

import Dao.StockProductoDao;

import model.StockProducto;
import model.Producto;
import model.Deposito;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author santi
 */
public class PanelStock extends javax.swing.JPanel {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private static final Color AZUL_PRINCIPAL
            = new Color(24, 72, 145);

    private static final Color AZUL_HOVER
            = new Color(35, 92, 180);

    private static final Color GRIS_METAL
            = new Color(70, 78, 88);

    private static final Color GRIS_METAL_HOVER
            = new Color(88, 98, 110);

    private static final Color GRIS_METAL_2
            = new Color(86, 96, 108);

    private static final Color GRIS_CLARO
            = new Color(225, 229, 234);

    private static final Color GRIS_CLARO_HOVER
            = new Color(208, 214, 221);

    private static final Color TEXTO_OSCURO
            = new Color(45, 52, 60);

    private static final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    private static final Color FONDO
            = new Color(238, 242, 247);

    private static final Color BORDE
            = new Color(210, 218, 228);

    private static final Color VERDE
            = new Color(25, 135, 84);

    private static final Color NARANJA
            = new Color(220, 140, 15);

    private static final Color ROJO
            = new Color(190, 65, 65);

    //==========================================================
    // BUSQUEDA
    //==========================================================
    private TextFieldRedondeado txtBuscar;
    private RSButtonRound btnBuscar;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblTotalArticulos;
    private JLabel lblTotalStock;
    private JLabel lblValorInventario;

    //==========================================================
    // ACCIONES
    //==========================================================
    private RSButtonRound btnEntrada;
    private RSButtonRound btnSalida;
    private RSButtonRound btnAjuste;
    private RSButtonRound btnModificar;
    private RSButtonRound btnDarBaja;
    private RSButtonRound btnActualizar;

    //==========================================================
    // TABLA STOCK
    //==========================================================
    private JTable tablaStock;
    private DefaultTableModel modeloStock;
    private JScrollPane scrollStock;

    //==========================================================
    // DETALLE
    //==========================================================
    private JLabel lblDetalleCodigo;
    private JLabel lblDetalleProducto;
    private JLabel lblDetalleMarca;
    private JLabel lblDetalleCategoria;
    private JLabel lblDetalleStock;
    private JLabel lblDetalleMinimo;
    private JLabel lblDetalleEstado;

    //==========================================================
    // HISTORIAL
    //==========================================================
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;

    //==========================================================
// DATOS STOCK
//==========================================================
    private final StockProductoDao stockProductoDao
            = new StockProductoDao();

    private List<StockProducto> stockCargado;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelStock() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                FONDO
        );

        inicializarComponentes();

        construirPanel();

        cargarStock();

        configurarEventos();
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // BUSQUEDA
        //======================================================
        txtBuscar
                = new TextFieldRedondeado();

        txtBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        txtBuscar.setPreferredSize(
                new Dimension(
                        300,
                        36
                )
        );

        btnBuscar
                = crearBotonPrimario(
                        "BUSCAR",
                        120
                );

        //======================================================
        // KPI
        //======================================================
        lblTotalArticulos
                = crearValorKPI(
                        "156",
                        AZUL_OSCURO
                );

        lblTotalStock
                = crearValorKPI(
                        "1.258",
                        AZUL_OSCURO
                );

        lblValorInventario
                = crearValorKPI(
                        "$ 2.456.780",
                        VERDE
                );

        //======================================================
        // BOTONES
        //======================================================
        btnEntrada
                = crearBotonMetal(
                        "ENTRADA",
                        155
                );

        btnSalida
                = crearBotonMetal(
                        "SALIDA",
                        150
                );

        btnAjuste
                = crearBotonMetal(
                        "AJUSTE",
                        150
                );

        btnModificar
                = crearBotonMetal(
                        "MODIFICAR",
                        165
                );

        btnDarBaja
                = crearBotonPeligro(
                        "DAR DE BAJA",
                        170
                );

        btnActualizar
                = crearBotonPrimario(
                        "ACTUALIZAR",
                        165
                );

        inicializarTablaStock();

        inicializarTablaHistorial();

        inicializarDetalle();
    }

    //==========================================================
    // DETALLE
    //==========================================================
    private void inicializarDetalle() {

        lblDetalleCodigo
                = crearValorDetalle("-");

        lblDetalleProducto
                = crearValorDetalle("-");

        lblDetalleMarca
                = crearValorDetalle("-");

        lblDetalleCategoria
                = crearValorDetalle("-");

        lblDetalleStock
                = crearValorDetalle("-");

        lblDetalleMinimo
                = crearValorDetalle("-");

        lblDetalleEstado
                = crearValorDetalle("-");
    }

    //==========================================================
    // TABLA STOCK
    //==========================================================
    private void inicializarTablaStock() {

        modeloStock
                = new DefaultTableModel(
                        new Object[]{
                            "Código",
                            "Producto",
                            "Marca",
                            "Categoría",
                            "Depósito",
                            "Stock",
                            "Mínimo",
                            "Compra",
                            "Venta",
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

        tablaStock
                = new JTable(
                        modeloStock
                );

        tablaStock.setRowHeight(
                31
        );

        tablaStock.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaStock.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaStock.setGridColor(
                new Color(
                        225,
                        230,
                        237
                )
        );

        tablaStock.setShowVerticalLines(
                false
        );

        tablaStock.setShowHorizontalLines(
                true
        );

        tablaStock.setSelectionBackground(
                new Color(
                        215,
                        228,
                        245
                )
        );

        tablaStock.setSelectionForeground(
                Color.BLACK
        );

        tablaStock.setFillsViewportHeight(
                true
        );

        tablaStock
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        tablaStock
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        aplicarHeaderAzul(
                tablaStock
        );

        //======================================================
        // RENDER ESTADO
        //======================================================
        tablaStock
                .getColumnModel()
                .getColumn(8)
                .setCellRenderer(
                        new RendererEstado()
                );

        //======================================================
        // ANCHOS
        //======================================================
        tablaStock
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(90);

        tablaStock
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(260);

        tablaStock
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(140);

        tablaStock
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(140);

        tablaStock
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        tablaStock
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);

        tablaStock
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(110);

        tablaStock
                .getColumnModel()
                .getColumn(7)
                .setPreferredWidth(110);

        tablaStock
                .getColumnModel()
                .getColumn(8)
                .setPreferredWidth(120);

        scrollStock
                = new JScrollPane(
                        tablaStock
                );

        scrollStock.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollStock
                .getViewport()
                .setBackground(
                        Color.WHITE
                );
    }

    //==========================================================
    // HISTORIAL
    //==========================================================
    private void inicializarTablaHistorial() {

        modeloHistorial
                = new DefaultTableModel(
                        new Object[]{
                            "Fecha",
                            "Tipo",
                            "Cantidad",
                            "Origen / Destino",
                            "Usuario"
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

        tablaHistorial
                = new JTable(
                        modeloHistorial
                );

        tablaHistorial.setRowHeight(
                29
        );

        tablaHistorial.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        tablaHistorial.setGridColor(
                new Color(
                        225,
                        230,
                        237
                )
        );

        tablaHistorial.setShowVerticalLines(
                false
        );

        tablaHistorial.setSelectionBackground(
                new Color(
                        215,
                        228,
                        245
                )
        );

        tablaHistorial.setSelectionForeground(
                Color.BLACK
        );

        tablaHistorial
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                36
                        )
                );

        tablaHistorial
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        aplicarHeaderAzul(
                tablaHistorial
        );
    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        JPanel contenido
                = new JPanel();

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
                        14,
                        16,
                        16,
                        16
                )
        );

        JPanel header
                = crearPanelHeader();

        JPanel resumen
                = crearPanelResumen();

        JPanel acciones
                = crearPanelAcciones();

        JPanel listado
                = crearPanelListado();

        JPanel inferior
                = crearPanelInferior();

        //======================================================
        // ALINEACION
        //======================================================
        header.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        resumen.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        acciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        listado.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        inferior.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        //======================================================
        // TAMAÑOS
        //======================================================
        header.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        105
                )
        );

        resumen.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        acciones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        105
                )
        );

        listado.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        450
                )
        );

        inferior.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        330
                )
        );

        //======================================================
        // AGREGAR
        //======================================================
        contenido.add(
                header
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
                        12
                )
        );

        contenido.add(
                acciones
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                listado
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                inferior
        );

        contenido.add(
                Box.createVerticalStrut(
                        10
                )
        );

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scrollGeneral
                = new JScrollPane(
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
    }

    //==========================================================
    // HEADER
    //==========================================================
    private JPanel crearPanelHeader() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new BorderLayout(
                        20,
                        0
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                14,
                                20,
                                14,
                                20
                        )
                )
        );

        //======================================================
        // TITULOS
        //======================================================
        JPanel titulos
                = new JPanel();

        titulos.setOpaque(
                false
        );

        titulos.setLayout(
                new BoxLayout(
                        titulos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo
                = new JLabel(
                        "CONTROL DE STOCK"
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

        JLabel subtitulo
                = new JLabel(
                        "Consulta y gestión del inventario del negocio"
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

        titulos.add(
                titulo
        );

        titulos.add(
                Box.createVerticalStrut(
                        3
                )
        );

        titulos.add(
                subtitulo
        );

        //======================================================
        // BUSCADOR
        //======================================================
        JPanel buscador
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                13
                        )
                );

        buscador.setOpaque(
                false
        );

        JLabel lblBuscar
                = new JLabel(
                        "Buscar artículo"
                );

        lblBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblBuscar.setForeground(
                TEXTO_OSCURO
        );

        buscador.add(
                lblBuscar
        );

        buscador.add(
                txtBuscar
        );

        buscador.add(
                btnBuscar
        );

        panel.add(
                titulos,
                BorderLayout.WEST
        );

        panel.add(
                buscador,
                BorderLayout.EAST
        );

        return panel;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new GridLayout(
                        1,
                        3,
                        0,
                        0
                )
        );

        panel.add(
                crearKPI(
                        "ARTÍCULOS",
                        lblTotalArticulos,
                        "Total registrados"
                )
        );

        panel.add(
                crearKPI(
                        "STOCK TOTAL",
                        lblTotalStock,
                        "Unidades disponibles"
                )
        );

        panel.add(
                crearKPI(
                        "VALOR INVENTARIO",
                        lblValorInventario,
                        "Valor total del inventario"
                )
        );

        return panel;
    }

    //==========================================================
    // KPI
    //==========================================================
    private JPanel crearKPI(
            String titulo,
            JLabel valor,
            String descripcion) {

        JPanel panel
                = new JPanel();

        panel.setOpaque(
                false
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        15,
                        28,
                        13,
                        20
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        titulo
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        JLabel lblDescripcion
                = new JLabel(
                        descripcion
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblDescripcion.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(
                lblTitulo
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                valor
        );

        panel.add(
                Box.createVerticalStrut(
                        3
                )
        );

        panel.add(
                lblDescripcion
        );

        return panel;
    }

    //==========================================================
    // ACCIONES
    //==========================================================
    private JPanel crearPanelAcciones() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel titulo
                = crearTituloSeccion(
                        "ACCIONES"
                );

        titulo.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        3,
                        18
                )
        );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel botones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                8
                        )
                );

        botones.setOpaque(
                false
        );

        botones.setBorder(
                new EmptyBorder(
                        0,
                        8,
                        8,
                        8
                )
        );

        botones.add(
                btnEntrada
        );

        botones.add(
                btnSalida
        );

        botones.add(
                btnAjuste
        );

        botones.add(
                btnModificar
        );

        botones.add(
                btnDarBaja
        );

        botones.add(
                btnActualizar
        );

        panel.add(
                botones,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // LISTADO
    //==========================================================
    private JPanel crearPanelListado() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel titulo
                = crearTituloSeccion(
                        "LISTADO DE STOCK"
                );

        titulo.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        10,
                        18
                )
        );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        panel.add(
                scrollStock,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // INFERIOR
    //==========================================================
    private JPanel crearPanelInferior() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                2,
                                12,
                                0
                        )
                );

        panel.setOpaque(
                false
        );

        panel.add(
                crearPanelDetalle()
        );

        panel.add(
                crearPanelHistorial()
        );

        return panel;
    }

    //==========================================================
    // DETALLE
    //==========================================================
    private JPanel crearPanelDetalle() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel titulo
                = crearTituloSeccion(
                        "DETALLE DEL ARTÍCULO"
                );

        titulo.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        5,
                        18
                )
        );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO
        //======================================================
        JPanel contenido
                = new JPanel(
                        new GridBagLayout()
                );

        contenido.setOpaque(
                false
        );

        contenido.setBorder(
                new EmptyBorder(
                        8,
                        20,
                        15,
                        20
                )
        );

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        5,
                        5,
                        5,
                        12
                );

        c.anchor
                = GridBagConstraints.WEST;

        c.fill
                = GridBagConstraints.HORIZONTAL;

        agregarDetalle(
                contenido,
                c,
                0,
                "Código:",
                lblDetalleCodigo
        );

        agregarDetalle(
                contenido,
                c,
                1,
                "Producto:",
                lblDetalleProducto
        );

        agregarDetalle(
                contenido,
                c,
                2,
                "Marca:",
                lblDetalleMarca
        );

        agregarDetalle(
                contenido,
                c,
                3,
                "Categoría:",
                lblDetalleCategoria
        );

        agregarDetalle(
                contenido,
                c,
                4,
                "Stock actual:",
                lblDetalleStock
        );

        agregarDetalle(
                contenido,
                c,
                5,
                "Stock mínimo:",
                lblDetalleMinimo
        );

        agregarDetalle(
                contenido,
                c,
                6,
                "Estado:",
                lblDetalleEstado
        );

        panel.add(
                contenido,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // AGREGAR DETALLE
    //==========================================================
    private void agregarDetalle(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            String titulo,
            JLabel valor) {

        JLabel etiqueta
                = new JLabel(
                        titulo
                );

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        etiqueta.setForeground(
                AZUL_OSCURO
        );

        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;

        panel.add(
                etiqueta,
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        panel.add(
                valor,
                c
        );
    }

    //==========================================================
    // HISTORIAL
    //==========================================================
    private JPanel crearPanelHistorial() {

        JPanel panel
                = crearTarjeta();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel titulo
                = crearTituloSeccion(
                        "HISTORIAL DE MOVIMIENTOS"
                );

        titulo.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        8,
                        18
                )
        );

        JScrollPane scroll
                = new JScrollPane(
                        tablaHistorial
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        12,
                        12,
                        12
                )
        );

        scroll
                .getViewport()
                .setBackground(
                        Color.WHITE
                );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    //==========================================================
// CARGAR STOCK REAL
//==========================================================
    private void cargarStock() {

        modeloStock.setRowCount(0);

        stockCargado
                = stockProductoDao.listarTodos();

        BigDecimal stockTotal
                = BigDecimal.ZERO;

        BigDecimal valorInventario
                = BigDecimal.ZERO;

        int articulos
                = 0;

        for (StockProducto stock : stockCargado) {

            if (stock == null
                    || stock.getProducto() == null
                    || stock.getDeposito() == null) {

                continue;
            }

            Producto producto
                    = stock.getProducto();

            Deposito deposito
                    = stock.getDeposito();

            BigDecimal cantidad
                    = stock.getCantidad() != null
                    ? stock.getCantidad()
                    : BigDecimal.ZERO;

            BigDecimal minimo
                    = producto.getStockMinimo() != null
                    ? producto.getStockMinimo()
                    : BigDecimal.ZERO;

            BigDecimal precioCompra
                    = producto.getPrecioCompra() != null
                    ? producto.getPrecioCompra()
                    : BigDecimal.ZERO;

            BigDecimal precioVenta
                    = producto.getPrecioVenta() != null
                    ? producto.getPrecioVenta()
                    : BigDecimal.ZERO;

            String marca
                    = producto.getMarca() != null
                    ? producto.getMarca().getNombre()
                    : "-";

            String categoria
                    = producto.getCategoria() != null
                    ? producto.getCategoria().getNombre()
                    : "-";

            String estado
                    = calcularEstadoStock(
                            cantidad,
                            minimo
                    );

            modeloStock.addRow(
                    new Object[]{
                        producto.getCodigo(),
                        producto.getNombre(),
                        marca,
                        categoria,
                        deposito.getNombre(),
                        formatearCantidad(cantidad),
                        formatearCantidad(minimo),
                        formatearDinero(precioCompra),
                        formatearDinero(precioVenta),
                        estado
                    }
            );

            articulos++;

            stockTotal
                    = stockTotal.add(
                            cantidad
                    );

            valorInventario
                    = valorInventario.add(
                            cantidad.multiply(
                                    precioCompra
                            )
                    );
        }

        //======================================================
        // KPI
        //======================================================
        lblTotalArticulos.setText(
                String.valueOf(
                        articulos
                )
        );

        lblTotalStock.setText(
                formatearCantidad(
                        stockTotal
                )
        );

        lblValorInventario.setText(
                formatearDinero(
                        valorInventario
                )
        );

        limpiarDetalle();
    }

//==========================================================
// FORMATEAR DINERO
//==========================================================
    private String formatearDinero(
            BigDecimal valor) {

        if (valor == null) {

            valor = BigDecimal.ZERO;
        }

        NumberFormat formato
                = NumberFormat.getCurrencyInstance(
                        new Locale(
                                "es",
                                "AR"
                        )
                );

        formato.setMaximumFractionDigits(
                2
        );

        formato.setMinimumFractionDigits(
                0
        );

        return formato.format(
                valor
        );
    }

//==========================================================
// FORMATEAR CANTIDAD
//==========================================================
    private String formatearCantidad(
            BigDecimal cantidad) {

        if (cantidad == null) {

            return "0";
        }

        BigDecimal normalizada
                = cantidad.stripTrailingZeros();

        return normalizada.toPlainString();
    }

//==========================================================
// CALCULAR ESTADO STOCK
//==========================================================
    private String calcularEstadoStock(
            BigDecimal cantidad,
            BigDecimal minimo) {

        if (cantidad == null
                || cantidad.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            return "Sin Stock";
        }

        if (minimo != null
                && minimo.compareTo(
                        BigDecimal.ZERO
                ) > 0
                && cantidad.compareTo(
                        minimo
                ) <= 0) {

            return "Bajo Stock";
        }

        return "Disponible";
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // SELECCION TABLA
        //======================================================
        tablaStock
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (e.getValueIsAdjusting()) {

                        return;
                    }

                    cargarDetalleSeleccionado();
                });

        //======================================================
        // DOBLE CLICK
        //======================================================
        tablaStock.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseClicked(
                    MouseEvent e) {

                if (e.getClickCount() == 2
                        && tablaStock.getSelectedRow() != -1) {

                    abrirModificar();
                }
            }
        });

        //======================================================
// BUSCADOR EN TIEMPO REAL
//======================================================
        txtBuscar.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarTabla();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarTabla();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarTabla();
            }
        });

        //======================================================
        // BUSCAR
        //======================================================
        btnBuscar.addActionListener(e -> {

            filtrarTabla();
        });

        txtBuscar.addActionListener(e -> {

            filtrarTabla();
        });

        //======================================================
        // ENTRADA
        //======================================================
        btnEntrada.addActionListener(e -> {

            abrirEntrada();
        });

        //======================================================
        // SALIDA
        //======================================================
        btnSalida.addActionListener(e -> {

            abrirSalida();

        });

        //======================================================
        // AJUSTE
        //======================================================
        btnAjuste.addActionListener(e -> {

            Window ventana = SwingUtilities.getWindowAncestor(this);

            DialogoAjusteStock dialogo
                    = new DialogoAjusteStock((Frame) ventana);

            dialogo.setVisible(true);

        });
        //======================================================
        // MODIFICAR
        //======================================================
        btnModificar.addActionListener(e -> {

            abrirModificar();
        });

        //======================================================
        // DAR DE BAJA
        //======================================================
        btnDarBaja.addActionListener(e -> {

            abrirDarDeBaja();
        });

        //======================================================
        // ACTUALIZAR
        //======================================================
        btnActualizar.addActionListener(e -> {

            cargarStock();

            lblTotalArticulos.setText(
                    "156"
            );

            lblTotalStock.setText(
                    "1.258"
            );

            lblValorInventario.setText(
                    "$ 2.456.780"
            );
        });
    }

    //==========================================================
    // ENTRADA
    //==========================================================
    private void abrirEntrada() {

        Window ventana
                = SwingUtilities.getWindowAncestor(
                        this
                );

        DialogoTipoEntrada dialogo
                = new DialogoTipoEntrada(
                        ventana
                );

        dialogo.setLocationRelativeTo(
                ventana
        );

        dialogo.setVisible(
                true
        );
    }

    //==========================================================
// SALIDA DE STOCK
//==========================================================
    private void abrirSalida() {

        Window ventana
                = SwingUtilities.getWindowAncestor(
                        this
                );

        DialogoSalidaStock dialogo
                = new DialogoSalidaStock(
                        (Frame) ventana);

        dialogo.setLocationRelativeTo(
                ventana
        );

        dialogo.setVisible(
                true
        );
    }

    //==========================================================
    // MODIFICAR
    //==========================================================
    private void abrirModificar() {

        Frame frame
                = obtenerFrame();

        if (frame == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo encontrar la ventana principal.",
                    "Modificar Stock",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        DialogoModificarStock dialogo
                = new DialogoModificarStock(
                        frame
                );

        dialogo.setLocationRelativeTo(
                frame
        );

        dialogo.setVisible(
                true
        );
    }

    //==========================================================
    // DAR DE BAJA
    //==========================================================
    private void abrirDarDeBaja() {

        Frame frame
                = obtenerFrame();

        if (frame == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo encontrar la ventana principal.",
                    "Dar de Baja",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        DialogoDarDeBajaProducto dialogo
                = new DialogoDarDeBajaProducto(
                        frame
                );

        dialogo.setLocationRelativeTo(
                frame
        );

        dialogo.setVisible(
                true
        );
    }

    //==========================================================
    // OBTENER FRAME
    //==========================================================
    private Frame obtenerFrame() {

        Window ventana
                = SwingUtilities
                        .getWindowAncestor(
                                this
                        );

        if (ventana instanceof Frame) {

            return (Frame) ventana;
        }

        return null;
    }

    //==========================================================
    // DETALLE SELECCIONADO
    //==========================================================
    private void cargarDetalleSeleccionado() {

        int fila
                = tablaStock.getSelectedRow();

        if (fila == -1) {

            limpiarDetalle();

            return;
        }

        lblDetalleCodigo.setText(
                valorTabla(
                        fila,
                        0
                )
        );

        lblDetalleProducto.setText(
                valorTabla(
                        fila,
                        1
                )
        );

        lblDetalleMarca.setText(
                valorTabla(
                        fila,
                        2
                )
        );

        lblDetalleCategoria.setText(
                valorTabla(
                        fila,
                        3
                )
        );

        lblDetalleStock.setText(
                valorTabla(
                        fila,
                        4
                )
        );

        lblDetalleMinimo.setText(
                valorTabla(
                        fila,
                        5
                )
        );

        lblDetalleEstado.setText(
                valorTabla(
                        fila,
                        8
                )
        );

        cargarHistorialPrueba(
                valorTabla(
                        fila,
                        1
                )
        );
    }

    //==========================================================
    // HISTORIAL PRUEBA
    //==========================================================
    private void cargarHistorialPrueba(
            String producto) {

        modeloHistorial.setRowCount(
                0
        );

        modeloHistorial.addRow(
                new Object[]{
                    "28/08/2026",
                    "Entrada",
                    "+20",
                    "Compra proveedor",
                    "Santiago"
                }
        );

        modeloHistorial.addRow(
                new Object[]{
                    "28/08/2026",
                    "Venta",
                    "-2",
                    "Venta N° 00000125",
                    "Santiago"
                }
        );

        modeloHistorial.addRow(
                new Object[]{
                    "27/08/2026",
                    "Ajuste",
                    "+1",
                    "Corrección inventario",
                    "Administrador"
                }
        );
    }

    //==========================================================
    // LIMPIAR DETALLE
    //==========================================================
    private void limpiarDetalle() {

        lblDetalleCodigo.setText(
                "-"
        );

        lblDetalleProducto.setText(
                "-"
        );

        lblDetalleMarca.setText(
                "-"
        );

        lblDetalleCategoria.setText(
                "-"
        );

        lblDetalleStock.setText(
                "-"
        );

        lblDetalleMinimo.setText(
                "-"
        );

        lblDetalleEstado.setText(
                "-"
        );

        modeloHistorial.setRowCount(
                0
        );
    }

    //==========================================================
    // VALOR TABLA
    //==========================================================
    private String valorTabla(
            int fila,
            int columna) {

        Object valor
                = tablaStock
                        .getValueAt(
                                fila,
                                columna
                        );

        return valor == null
                ? ""
                : valor.toString();
    }

    //==========================================================
    // FILTRO VISUAL DE PRUEBA
    //==========================================================
    //==========================================================
// FILTRAR TABLA
//==========================================================
    //==========================================================
// FILTRAR TABLA
//==========================================================
    private void filtrarTabla() {

        String buscar
                = txtBuscar
                        .getText()
                        .trim()
                        .toLowerCase();

        //======================================================
        // SI BORRÓ LA BÚSQUEDA, VOLVER A MOSTRAR TODO
        //======================================================
        if (buscar.isEmpty()) {

            cargarStock();

            return;
        }

        //======================================================
        // RECARGAR DATOS COMPLETOS
        //======================================================
        cargarStock();

        //======================================================
        // SEPARAR PALABRAS
        // Ejemplo: "jamon crudo"
        //          -> "jamon" + "crudo"
        //======================================================
        String[] palabras
                = buscar.split("\\s+");

        //======================================================
        // RECORRER DESDE ABAJO
        //======================================================
        for (int i = modeloStock.getRowCount() - 1;
                i >= 0;
                i--) {

            String codigo
                    = valorModelo(i, 0);

            String producto
                    = valorModelo(i, 1);

            String marca
                    = valorModelo(i, 2);

            String categoria
                    = valorModelo(i, 3);

            String deposito
                    = valorModelo(i, 4);

            //==================================================
            // UNIFICAR INFORMACIÓN DE LA FILA
            //==================================================
            String contenidoFila
                    = codigo
                    + " "
                    + producto
                    + " "
                    + marca
                    + " "
                    + categoria
                    + " "
                    + deposito;

            //==================================================
            // TODAS LAS PALABRAS DEBEN APARECER
            //==================================================
            boolean coincide
                    = true;

            for (String palabra : palabras) {

                if (!contenidoFila.contains(palabra)) {

                    coincide
                            = false;

                    break;
                }
            }

            //==================================================
            // SI NO COINCIDE, SACARLA DEL MODELO
            //==================================================
            if (!coincide) {

                modeloStock.removeRow(i);
            }
        }
    }

//==========================================================
// OBTENER VALOR DEL MODELO COMO TEXTO
//==========================================================
    private String valorModelo(
            int fila,
            int columna) {

        Object valor
                = modeloStock.getValueAt(
                        fila,
                        columna
                );

        if (valor == null) {

            return "";
        }

        return valor
                .toString()
                .trim()
                .toLowerCase();
    }

    //==========================================================
    // PANEL TARJETA
    //==========================================================
    private JPanel crearTarjeta() {

        JPanel panel
                = new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        return panel;
    }

    //==========================================================
    // TITULO SECCION
    //==========================================================
    private JLabel crearTituloSeccion(
            String texto) {

        JLabel label
                = new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                AZUL_OSCURO
        );

        return label;
    }

    //==========================================================
    // KPI
    //==========================================================
    private JLabel crearValorKPI(
            String texto,
            Color color) {

        JLabel label
                = new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    //==========================================================
    // VALOR DETALLE
    //==========================================================
    private JLabel crearValorDetalle(
            String texto) {

        JLabel label
                = new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        label.setForeground(
                TEXTO_OSCURO
        );

        return label;
    }

    //==========================================================
    // BOTON PRIMARIO
    //==========================================================
    private RSButtonRound crearBotonPrimario(
            String texto,
            int ancho) {

        RSButtonRound boton
                = new RSButtonRound();

        boton.setText(
                texto
        );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        40
                )
        );

        boton.setBackground(
                AZUL_PRINCIPAL
        );

        boton.setColorHover(
                AZUL_HOVER
        );

        boton.setColorText(
                Color.WHITE
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        boton.setFocusable(
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
    // BOTON METAL
    //==========================================================
    private RSButtonRound crearBotonMetal(
            String texto,
            int ancho) {

        RSButtonRound boton
                = new RSButtonRound();

        boton.setText(
                texto
        );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        40
                )
        );

        boton.setBackground(
                GRIS_METAL
        );

        boton.setColorHover(
                GRIS_METAL_HOVER
        );

        boton.setColorText(
                Color.WHITE
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        boton.setFocusable(
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
    // BOTON PELIGRO
    //==========================================================
    private RSButtonRound crearBotonPeligro(
            String texto,
            int ancho) {

        RSButtonRound boton
                = crearBotonMetal(
                        texto,
                        ancho
                );

        /*
         * Normalmente queda gris metálico.
         * Recién al pasar el mouse mostramos rojo.
         */
        boton.setColorHover(
                ROJO
        );

        return boton;
    }

    //==========================================================
    // HEADER AZUL
    //==========================================================
    private void aplicarHeaderAzul(
            JTable tabla) {

        DefaultTableCellRenderer renderer
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
                                12
                        )
                );

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                label.setOpaque(
                        true
                );

                label.setBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                0,
                                1,
                                new Color(
                                        40,
                                        75,
                                        130
                                )
                        )
                );

                return label;
            }
        };

        for (int i = 0;
                i < tabla.getColumnCount();
                i++) {

            tabla.getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(
                            renderer
                    );
        }
    }

    //==========================================================
    // RENDER ESTADO
    //==========================================================
    private class RendererEstado
            extends DefaultTableCellRenderer {

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

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            if (isSelected) {

                label.setBackground(
                        new Color(
                                215,
                                228,
                                245
                        )
                );

                label.setForeground(
                        Color.BLACK
                );

                return label;
            }

            String estado
                    = value == null
                            ? ""
                            : value.toString();

            switch (estado) {

                case "Disponible":

                    label.setBackground(
                            new Color(
                                    226,
                                    243,
                                    233
                            )
                    );

                    label.setForeground(
                            VERDE
                    );

                    break;

                case "Bajo Stock":

                    label.setBackground(
                            new Color(
                                    255,
                                    244,
                                    213
                            )
                    );

                    label.setForeground(
                            NARANJA
                    );

                    break;

                case "Sin Stock":

                    label.setBackground(
                            new Color(
                                    250,
                                    226,
                                    226
                            )
                    );

                    label.setForeground(
                            ROJO
                    );

                    break;

                default:

                    label.setBackground(
                            Color.WHITE
                    );

                    label.setForeground(
                            TEXTO_OSCURO
                    );

                    break;
            }

            return label;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
