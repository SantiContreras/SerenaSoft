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
import Dialogos.DialogoDetalleMovimiento;
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
import Dao.ProductoDao;
import Dao.AjusteStockDao;
import model.AjusteStock;
import model.AjusteStockDetalle;
import java.time.format.DateTimeFormatter;
import javax.swing.Icon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
    private javax.swing.JTextField txtBuscar;
    private RSButtonRound btnBuscar;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblTotalArticulos;
    private JLabel lblTotalStock;
    private JLabel lblActivos;
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
    private final ProductoDao productoDao = new ProductoDao();
    private final AjusteStockDao ajusteStockDao = new AjusteStockDao();
    private final java.util.Map<String,Integer> idsPorCodigo = new java.util.HashMap<>();

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

        configurarEventos();

        actualizarStock();
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // BUSQUEDA
        //======================================================
        txtBuscar
                = new javax.swing.JTextField();

        txtBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        txtBuscar.setPreferredSize(
                new Dimension(
                        310,
                        44
                )
        );

        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setForeground(AZUL_OSCURO);
        txtBuscar.setCaretColor(AZUL_PRINCIPAL);
        txtBuscar.setSelectionColor(new Color(210, 228, 252));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(174, 193, 218), 1, true),
                new EmptyBorder(8, 15, 8, 15)));
        txtBuscar.putClientProperty("JTextField.placeholderText", "Código, nombre, marca o categoría...");
        txtBuscar.setToolTipText("Buscá por código, producto, marca, categoría o depósito");

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
                        "0",
                        AZUL_OSCURO
                );

        lblActivos = crearValorKPI("0", AZUL_PRINCIPAL);

        lblTotalStock
                = crearValorKPI(
                        "0",
                        AZUL_OSCURO
                );

        lblValorInventario
                = crearValorKPI(
                        "$ 0",
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

        btnEntrada.setIcon(icono("plus", Color.WHITE));
        btnSalida.setIcon(icono("minus", Color.WHITE));
        btnAjuste.setIcon(icono("tools", Color.WHITE));
        btnModificar.setIcon(icono("edit", Color.WHITE));
        btnDarBaja.setIcon(icono("cross", Color.WHITE));
        btnActualizar.setIcon(icono("refresh", Color.WHITE));
        btnBuscar.setIcon(icono("search", Color.WHITE));

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
                            "Estado",
                            "Compra",
                            "Venta",
                            "Mínimo"
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

        tablaStock.setRowHeight(36);
        tablaStock.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaStock.setIntercellSpacing(new Dimension(0, 1));

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

        DefaultTableCellRenderer rendererFilas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean selected, boolean focused, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, selected, focused, row, column);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                label.setBorder(new EmptyBorder(0, 9, 0, 9));
                label.setBackground(selected ? new Color(211, 228, 250) :
                        row % 2 == 0 ? Color.WHITE : new Color(247, 250, 254));
                label.setForeground(AZUL_OSCURO);
                label.setHorizontalAlignment((column == 5 || (column >= 7 && column <= 9))
                        ? SwingConstants.RIGHT : SwingConstants.LEFT);
                return label;
            }
        };
        for (int columna = 0; columna < 10; columna++) {
            if (columna != 6) {
                tablaStock.getColumnModel().getColumn(columna).setCellRenderer(rendererFilas);
            }
        }

        //======================================================
        // RENDER ESTADO
        //======================================================
        tablaStock
                .getColumnModel()
                .getColumn(6)
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
                .setPreferredWidth(290);

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
                .setPreferredWidth(145);

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

        tablaStock.getColumnModel().getColumn(9).setPreferredWidth(100);

        scrollStock
                = new JScrollPane(
                        tablaStock
                );

        scrollStock.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        scrollStock.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollStock.getHorizontalScrollBar().setUnitIncrement(18);

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

        // El detalle y el historial se muestran en un diálogo independiente.

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
                        620
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

        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

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

        titulo.setForeground(AZUL_OSCURO);
        titulo.setIcon(icono("inventory", AZUL_PRINCIPAL));
        titulo.setIconTextGap(12);

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
                        4,
                        0,
                        0
                )
        );

        panel.add(
                crearKPI(
                        "PRODUCTOS REGISTRADOS",
                        lblTotalArticulos,
                        "Incluye productos sin existencias"
                )
        );

        panel.add(crearKPI("PRODUCTOS ACTIVOS", lblActivos, "Disponibles para operar"));

        panel.add(
                crearKPI(
                        "STOCK TOTAL",
                        lblTotalStock,
                        "Existencias (distintas unidades)"
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
                        20,
                        13,
                        12
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

        lblTitulo.setIcon(icono(titulo.contains("ACTIVOS") ? "check" :
                titulo.contains("VALOR") ? "money" :
                titulo.contains("TOTAL") ? "layers" : "inventory", AZUL_PRINCIPAL));
        lblTitulo.setIconTextGap(6);
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
                        "LISTADO DE STOCK  ·  DOBLE CLIC PARA VER DETALLE E HISTORIAL"
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
                        "HISTORIAL DE AJUSTES CONFIRMADOS"
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
        idsPorCodigo.clear();
        List<Producto> productos = productoDao.listarTodos();
        stockCargado = stockProductoDao.listarTodos();
        if (productos == null) productos = java.util.Collections.emptyList();
        if (stockCargado == null) stockCargado = java.util.Collections.emptyList();

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal valor = BigDecimal.ZERO;
        int activos = 0;
        java.util.Set<Integer> productosConFila = new java.util.HashSet<>();
        for (Producto p : productos) {
            if (p != null) {
                idsPorCodigo.put(p.getCodigo(), p.getIdProducto());
                if (p.isActivo()) activos++;
            }
        }
        for (StockProducto stock : stockCargado) {
            if (stock == null || stock.getProducto() == null) continue;
            Producto p = stock.getProducto();
            Deposito d = stock.getDeposito();
            productosConFila.add(p.getIdProducto());
            BigDecimal cantidad = stock.getCantidad() == null ? BigDecimal.ZERO : stock.getCantidad();
            total = total.add(cantidad);
            valor = valor.add(cantidad.multiply(p.getPrecioCompra() == null ? BigDecimal.ZERO : p.getPrecioCompra()));
            agregarFilaStock(p, d == null ? "Sin depósito" : d.getNombre(), cantidad);
        }
        // Productos sin registro de stock también deben aparecer.
        for (Producto p : productos) {
            if (p != null && !productosConFila.contains(p.getIdProducto())) {
                agregarFilaStock(p, "Sin depósito", BigDecimal.ZERO);
            }
        }
        lblTotalArticulos.setText(String.valueOf(productos.size()));
        lblActivos.setText(String.valueOf(activos));
        lblTotalStock.setText(formatearCantidad(total));
        lblValorInventario.setText(formatearDinero(valor));
        limpiarDetalle();
    }

    private void agregarFilaStock(Producto p, String deposito, BigDecimal cantidad) {
        BigDecimal minimo = p.getStockMinimo() == null ? BigDecimal.ZERO : p.getStockMinimo();
        BigDecimal compra = p.getPrecioCompra() == null ? BigDecimal.ZERO : p.getPrecioCompra();
        BigDecimal venta = p.getPrecioVenta() == null ? BigDecimal.ZERO : p.getPrecioVenta();
        modeloStock.addRow(new Object[]{
            p.getCodigo(), p.getNombre(),
            p.getMarca() == null ? "-" : p.getMarca().getNombre(),
            p.getCategoria() == null ? "-" : p.getCategoria().getNombre(),
            deposito, formatearCantidad(cantidad),
            p.isActivo() ? calcularEstadoStock(cantidad, minimo) : "INACTIVO",
            formatearDinero(compra), formatearDinero(venta), formatearCantidad(minimo)
        });
    }

    // Historial de ajustes confirmados, asociado al producto y depósito seleccionados.
    private void cargarHistorialAjustes() {
        modeloHistorial.setRowCount(0);
        int fila = tablaStock.getSelectedRow();
        if (fila < 0) return;
        Integer idProducto = idsPorCodigo.get(valorTabla(fila, 0));
        String deposito = valorTabla(fila, 4);
        if (idProducto == null) return;
        try {
            List<AjusteStock> ajustes = ajusteStockDao.listarPorEstado("CONFIRMADO");
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (AjusteStock a : ajustes) {
                if (a.getDeposito() == null || !deposito.equals(a.getDeposito().getNombre())) continue;
                if (a.getDetalles() == null) continue;
                for (AjusteStockDetalle d : a.getDetalles()) {
                    if (d.getProducto() == null || d.getProducto().getIdProducto() != idProducto) continue;
                    BigDecimal dif = d.getDiferencia() == null ? BigDecimal.ZERO : d.getDiferencia();
                    String usuario = a.getUsuario() == null ? "-" : a.getUsuario().getNombreCompleto();
                    modeloHistorial.addRow(new Object[]{
                        a.getFecha() == null ? "-" : a.getFecha().format(formato),
                        "AJUSTE", (dif.signum() > 0 ? "+" : "") + formatearCantidad(dif),
                        a.getMotivo() == null ? deposito : a.getMotivo() + " · " + deposito,
                        usuario
                    });
                }
            }
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo consultar el historial de ajustes: " + ex.getMessage(),
                    "Historial", JOptionPane.WARNING_MESSAGE);
        }
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

        return NumberFormat.getNumberInstance(new Locale("es", "AR"))
                .format(normalizada);
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

                    abrirDetalleMovimiento();
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

            Frame ventana = obtenerFrame();
            if (ventana == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la ventana principal.");
                return;
            }
            DialogoAjusteStock dialogo
                    = new DialogoAjusteStock(ventana);

            dialogo.setVisible(true);
            actualizarStock();

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
        btnActualizar.addActionListener(e -> actualizarStock());
    }

    /** Abre la ficha del producto y su historial en una ventana independiente. */
    private void abrirDetalleMovimiento() {
        int fila = tablaStock.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un producto de la tabla.");
            return;
        }
        String codigo = valorTabla(fila, 0);
        Integer id = idsPorCodigo.get(codigo);
        if (id == null) {
            JOptionPane.showMessageDialog(this, "No se pudo identificar el producto.");
            return;
        }
        DialogoDetalleMovimiento dialogo = new DialogoDetalleMovimiento(
                SwingUtilities.getWindowAncestor(this), id,
                codigo, valorTabla(fila, 1), valorTabla(fila, 2),
                valorTabla(fila, 3), valorTabla(fila, 4),
                valorTabla(fila, 5), valorTabla(fila, 9),
                valorTabla(fila, 7), valorTabla(fila, 8),
                valorTabla(fila, 6));
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
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

        actualizarStock();
    }

    //==========================================================
// SALIDA DE STOCK
//==========================================================
    private void abrirSalida() {

        Frame ventana = obtenerFrame();
        if (ventana == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la ventana principal.");
            return;
        }
        DialogoSalidaStock dialogo
                = new DialogoSalidaStock(ventana);

        dialogo.setLocationRelativeTo(
                ventana
        );

        dialogo.setVisible(
                true
        );

        actualizarStock();
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

        actualizarStock();
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

        actualizarStock();
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
                        5
                )
        );

        lblDetalleMinimo.setText(
                valorTabla(
                        fila,
                        9
                )
        );

        lblDetalleEstado.setText(
                valorTabla(
                        fila,
                        6
                )
        );

        // El historial requiere consultas de movimientos por ID de producto.
        // No mostrar operaciones inventadas ni de otros artículos.
        // El historial se consulta al abrir el diálogo.
    }

    //==========================================================
    // HISTORIAL PRUEBA
    //==========================================================
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
        if (tablaStock.getRowSorter() == null) {
            tablaStock.setAutoCreateRowSorter(true);
        }
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter =
                (javax.swing.table.TableRowSorter<DefaultTableModel>) tablaStock.getRowSorter();
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            String[] palabras = texto.split("\\s+");
            sorter.setRowFilter(new javax.swing.RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    StringBuilder fila = new StringBuilder();
                    for (int col = 0; col <= 4; col++) {
                        Object valor = entry.getValue(col);
                        if (valor != null) fila.append(valor).append(' ');
                    }
                    String contenido = fila.toString().toLowerCase(java.util.Locale.ROOT);
                    for (String palabra : palabras) {
                        if (!contenido.contains(palabra.toLowerCase(java.util.Locale.ROOT))) return false;
                    }
                    return true;
                }
            });
        }
    }

    private void actualizarStock() {
        String codigoSeleccionado = null;
        String depositoSeleccionado = null;
        int fila = tablaStock.getSelectedRow();
        if (fila >= 0) {
            codigoSeleccionado = valorTabla(fila, 0);
            depositoSeleccionado = valorTabla(fila, 4);
        }
        try {
            cargarStock();
            filtrarTabla();
            if (codigoSeleccionado != null) {
                for (int i = 0; i < tablaStock.getRowCount(); i++) {
                    if (codigoSeleccionado.equals(valorTabla(i, 0))
                            && depositoSeleccionado.equals(valorTabla(i, 4))) {
                        tablaStock.setRowSelectionInterval(i, i);
                        tablaStock.scrollRectToVisible(tablaStock.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el stock: " + ex.getMessage(),
                    "Serena Soft", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Íconos vectoriales, independientes de fuentes y archivos externos.
    private Icon icono(String tipo, Color color) {
        return new Icon() {
            @Override public int getIconWidth() { return 20; }
            @Override public int getIconHeight() { return 20; }
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D d = (Graphics2D) g.create();
                d.translate(x, y);
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                d.setColor(color);
                d.setStroke(new java.awt.BasicStroke(2.1f,
                        java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                switch (tipo) {
                    case "plus": d.drawLine(10, 3, 10, 17); d.drawLine(3, 10, 17, 10); break;
                    case "minus": d.drawLine(3, 10, 17, 10); break;
                    case "cross": d.drawLine(5, 5, 15, 15); d.drawLine(15, 5, 5, 15); break;
                    case "search": d.drawOval(2, 2, 11, 11); d.drawLine(12, 12, 18, 18); break;
                    case "edit": d.drawLine(4, 14, 13, 5); d.drawLine(7, 16, 16, 7); d.drawLine(3, 17, 8, 16); break;
                    case "refresh": d.drawArc(3, 3, 14, 14, 40, 290); d.drawLine(16, 3, 17, 9); d.drawLine(17, 9, 12, 8); break;
                    case "inventory":
                        d.drawRoundRect(2, 5, 16, 13, 2, 2); d.drawLine(2, 9, 18, 9);
                        d.drawLine(7, 5, 7, 2); d.drawLine(13, 5, 13, 2); break;
                    case "check":
                        d.drawOval(2, 2, 16, 16); d.drawLine(6, 10, 9, 13); d.drawLine(9, 13, 15, 7); break;
                    case "money":
                        d.drawOval(2, 2, 16, 16); d.drawLine(10, 4, 10, 16);
                        d.drawArc(6, 5, 8, 6, 0, 180); d.drawArc(6, 9, 8, 6, 180, 180); break;
                    case "layers":
                        d.drawLine(2, 7, 10, 3); d.drawLine(10, 3, 18, 7);
                        d.drawLine(18, 7, 10, 11); d.drawLine(10, 11, 2, 7);
                        d.drawLine(2, 12, 10, 16); d.drawLine(10, 16, 18, 12); break;
                    case "tools":
                        d.drawOval(3, 3, 5, 5); d.drawLine(8, 8, 17, 17);
                        d.drawLine(3, 17, 16, 4); break;
                    case "list":
                        for (int i = 5; i <= 15; i += 5) {
                            d.drawRect(2, i - 1, 2, 2); d.drawLine(8, i, 18, i);
                        } break;
                    case "history":
                        d.drawArc(2, 2, 16, 16, 40, 290);
                        d.drawLine(10, 5, 10, 10); d.drawLine(10, 10, 14, 12); break;
                    default: d.drawRoundRect(3, 3, 14, 14, 2, 2);
                }
                d.dispose();
            }
        };
    }

    //==========================================================
    // PANEL TARJETA
    //==========================================================
    private JPanel crearTarjeta() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.setColor(new Color(213, 224, 238));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
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

        label.setIcon(icono(texto.contains("ACCIONES") ? "tools" :
                texto.contains("LISTADO") ? "list" :
                texto.contains("HISTORIAL") ? "history" : "inventory", AZUL_PRINCIPAL));
        label.setIconTextGap(9);
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

        boton.setBackground(new Color(22, 112, 151));
        boton.setColorHover(new Color(17, 137, 178));

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
        boton.setBackground(ROJO);
        boton.setColorHover(new Color(215, 65, 65));

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

                case "INACTIVO":
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
