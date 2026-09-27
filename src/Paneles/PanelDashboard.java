/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import rojeru_san.rsbutton.RSButtonRound;
import sesion.SesionUsuario;

/**
 *
 * @author santi
 */
public class PanelDashboard extends javax.swing.JPanel {

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblVentasHoy;
    private JLabel lblCantidadVentas;
    private JLabel lblCajaActual;
    private JLabel lblEstadoCaja;
    private JLabel lblStockBajo;
    private JLabel lblSinStock;
    private JLabel lblVentasMes;
    private JLabel lblCantidadMes;

    //==========================================================
    // TABLAS
    //==========================================================
    private JTable tablaUltimasVentas;
    private DefaultTableModel modeloUltimasVentas;

    private JTable tablaMasVendidos;
    private DefaultTableModel modeloMasVendidos;

    //==========================================================
    // ALERTAS
    //==========================================================
    private JPanel panelAlertas;

    //==========================================================
    // ACCESOS RAPIDOS
    //==========================================================
    private RSButtonRound btnNuevaVenta;
    private RSButtonRound btnEntradaMercaderia;
    private RSButtonRound btnNuevoCliente;
    private RSButtonRound btnVerCaja;

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

    private final Color NARANJA
            = new Color(235, 145, 20);

    private final Color GRIS
            = new Color(105, 115, 130);
   private final Color FONDO =
        new Color(205, 216, 230);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelDashboard() {

        inicializarComponentes();
        construirPanel();
        cargarDatosPrueba();
        configurarEventos();
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        lblVentasHoy
                = crearValorPrincipal(
                        "$ 185.400",
                        VERDE
                );

        lblCantidadVentas
                = crearTextoSecundario(
                        "12 ventas realizadas"
                );

        lblCajaActual
                = crearValorPrincipal(
                        "$ 239.900",
                        AZUL_OSCURO
                );

        lblEstadoCaja
                = crearTextoEstado(
                        "● CAJA ABIERTA",
                        VERDE
                );

        lblStockBajo
                = crearValorPrincipal(
                        "5",
                        NARANJA
                );

        lblSinStock
                = crearTextoSecundario(
                        "2 productos sin stock"
                );

        lblVentasMes
                = crearValorPrincipal(
                        "$ 3.850.000",
                        AZUL
                );

        lblCantidadMes
                = crearTextoSecundario(
                        "284 ventas este mes"
                );

        btnNuevaVenta
                = crearBotonRapido(
                        "NUEVA VENTA",
                        VERDE
                );

        btnEntradaMercaderia
                = crearBotonRapido(
                        "ENTRADA MERCADERÍA",
                        AZUL
                );

        btnNuevoCliente
                = crearBotonRapido(
                        "NUEVO CLIENTE",
                        AZUL_OSCURO
                );

        btnVerCaja
                = crearBotonRapido(
                        "VER CAJA",
                        NARANJA
                );

        inicializarTablaUltimasVentas();
        inicializarTablaMasVendidos();
    }

    //==========================================================
    // TABLA ULTIMAS VENTAS
    //==========================================================
    private void inicializarTablaUltimasVentas() {

        modeloUltimasVentas
                = new DefaultTableModel(
                        new Object[]{
                            "Venta",
                            "Hora",
                            "Cliente",
                            "Pago",
                            "Total"
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

        tablaUltimasVentas
                = crearTabla(
                        modeloUltimasVentas
                );

        tablaUltimasVentas
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(85);

        tablaUltimasVentas
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(65);

        tablaUltimasVentas
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(220);

        tablaUltimasVentas
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        tablaUltimasVentas
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);
    }

    //==========================================================
    // TABLA MAS VENDIDOS
    //==========================================================
    private void inicializarTablaMasVendidos() {

        modeloMasVendidos
                = new DefaultTableModel(
                        new Object[]{
                            "Producto",
                            "Unidades",
                            "Total"
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

        tablaMasVendidos
                = crearTabla(
                        modeloMasVendidos
                );

        tablaMasVendidos
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(260);

        tablaMasVendidos
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(80);

        tablaMasVendidos
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(110);
    }

    //==========================================================
    // CREAR TABLA
    //==========================================================
    private JTable crearTabla(
            DefaultTableModel modelo) {

        JTable tabla
                = new JTable(modelo);

        tabla.setRowHeight(29);

        tabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tabla.setBackground(
                Color.WHITE
        );

        tabla.setForeground(
                new Color(
                        35,
                        35,
                        35
                )
        );

        tabla.setGridColor(
                new Color(
                        225,
                        230,
                        238
                )
        );

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        tabla.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tabla.setSelectionForeground(
                Color.BLACK
        );

        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                36
                        )
                );

        tabla.getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul(tabla);

        return tabla;
    }

    //==========================================================
    // HEADER TABLA
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

                label.setOpaque(true);

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
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                FONDO
        );

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
                        18,
                        22,
                        18,
                        22
                )
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header
                = crearHeader();

        header.setAlignmentX(
                LEFT_ALIGNMENT
        );

        contenido.add(header);

        contenido.add(
                Box.createVerticalStrut(15)
        );

        //======================================================
        // TARJETAS
        //======================================================
        JPanel tarjetas
                = crearPanelTarjetas();

        tarjetas.setAlignmentX(
                LEFT_ALIGNMENT
        );

        tarjetas.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        contenido.add(tarjetas);

        contenido.add(
                Box.createVerticalStrut(15)
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel zonaCentral
                = crearZonaCentral();

        zonaCentral.setAlignmentX(
                LEFT_ALIGNMENT
        );

        zonaCentral.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        320
                )
        );

        contenido.add(zonaCentral);

        contenido.add(
                Box.createVerticalStrut(15)
        );

        //======================================================
        // ZONA INFERIOR
        //======================================================
        JPanel zonaInferior
                = crearZonaInferior();

        zonaInferior.setAlignmentX(
                LEFT_ALIGNMENT
        );

        zonaInferior.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        260
                )
        );

        contenido.add(zonaInferior);

        contenido.add(
                Box.createVerticalStrut(10)
        );

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scroll
                = new JScrollPane(
                        contenido
                );

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        scroll.getViewport()
                .setBackground(
                        FONDO
                );

        add(
                scroll,
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // HEADER
    //==========================================================
   private JPanel crearHeader() {

    JPanel panel
            = new JPanel();

    panel.setOpaque(false);

    panel.setLayout(
            new BoxLayout(
                    panel,
                    BoxLayout.Y_AXIS
            )
    );


    // =========================================================
    // TITULO
    // =========================================================

    JLabel titulo
            = new JLabel(
                    "INICIO"
            );

    titulo.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    24
            )
    );

    titulo.setForeground(
            AZUL_OSCURO
    );


    // =========================================================
    // OBTENER NOMBRE DEL USUARIO LOGUEADO
    // =========================================================

    String nombreCompleto
            = SesionUsuario.getNombreUsuario();

    String primerNombre
            = "";


    if (nombreCompleto != null
            && !nombreCompleto.isBlank()) {

        primerNombre
                = nombreCompleto
                        .trim()
                        .split("\\s+")[0];
    }


    // =========================================================
    // BIENVENIDA
    // =========================================================

    String textoBienvenida;

    if (primerNombre.isBlank()) {

        textoBienvenida
                = "Buenos días";

    } else {

        textoBienvenida
                = "Buenos días, "
                + primerNombre;
    }


    JLabel bienvenida
            = new JLabel(
                    textoBienvenida
            );

    bienvenida.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    17
            )
    );

    bienvenida.setForeground(
            new Color(
                    55,
                    65,
                    80
            )
    );


    // =========================================================
    // SUBTITULO
    // =========================================================

    JLabel subtitulo
            = new JLabel(
                    "Resumen general de la actividad del comercio"
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


    // =========================================================
    // AGREGAR COMPONENTES
    // =========================================================

    panel.add(
            titulo
    );

    panel.add(
            Box.createVerticalStrut(5)
    );

    panel.add(
            bienvenida
    );

    panel.add(
            Box.createVerticalStrut(2)
    );

    panel.add(
            subtitulo
    );


    return panel;
}

    //==========================================================
    // TARJETAS
    //==========================================================
    private JPanel crearPanelTarjetas() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                4,
                                14,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearTarjetaResumen(
                        "VENTAS DE HOY",
                        lblVentasHoy,
                        lblCantidadVentas
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "CAJA ACTUAL",
                        lblCajaActual,
                        lblEstadoCaja
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "STOCK BAJO",
                        lblStockBajo,
                        lblSinStock
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "VENTAS DEL MES",
                        lblVentasMes,
                        lblCantidadMes
                )
        );

        return panel;
    }

    //==========================================================
    // ZONA CENTRAL
    //==========================================================
    private JPanel crearZonaCentral() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearPanelUltimasVentas()
        );

        panel.add(
                crearPanelAlertas()
        );

        return panel;
    }

    //==========================================================
    // ZONA INFERIOR
    //==========================================================
    private JPanel crearZonaInferior() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearPanelMasVendidos()
        );

        panel.add(
                crearPanelAccesosRapidos()
        );

        return panel;
    }

    //==========================================================
    // ULTIMAS VENTAS
    //==========================================================
    private JPanel crearPanelUltimasVentas() {

        JPanel panel
                = crearPanelSeccion(
                        "ÚLTIMAS VENTAS"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JScrollPane scroll
                = new JScrollPane(
                        tablaUltimasVentas
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // ALERTAS
    //==========================================================
    private JPanel crearPanelAlertas() {

        JPanel exterior
                = crearPanelSeccion(
                        "ALERTAS"
                );

        exterior.setLayout(
                new BorderLayout()
        );

        panelAlertas
                = new JPanel();

        panelAlertas.setBackground(
                Color.WHITE
        );

        panelAlertas.setLayout(
                new BoxLayout(
                        panelAlertas,
                        BoxLayout.Y_AXIS
                )
        );

        panelAlertas.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        panelAlertas.add(
                crearAlerta(
                        "Stock bajo",
                        "5 productos necesitan reposición",
                        NARANJA
                )
        );

        panelAlertas.add(
                Box.createVerticalStrut(8)
        );

        panelAlertas.add(
                crearAlerta(
                        "Productos sin stock",
                        "2 artículos no tienen existencias",
                        ROJO
                )
        );

        panelAlertas.add(
                Box.createVerticalStrut(8)
        );

        panelAlertas.add(
                crearAlerta(
                        "Caja abierta",
                        "Caja iniciada a las 08:30",
                        VERDE
                )
        );

        panelAlertas.add(
                Box.createVerticalStrut(8)
        );

        panelAlertas.add(
                crearAlerta(
                        "Copia de seguridad",
                        "Último backup: ayer 19:30",
                        AZUL
                )
        );

        exterior.add(
                panelAlertas,
                BorderLayout.CENTER
        );

        return exterior;
    }

    //==========================================================
    // MAS VENDIDOS
    //==========================================================
    private JPanel crearPanelMasVendidos() {

        JPanel panel
                = crearPanelSeccion(
                        "PRODUCTOS MÁS VENDIDOS"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JScrollPane scroll
                = new JScrollPane(
                        tablaMasVendidos
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // ACCESOS RAPIDOS
    //==========================================================
    private JPanel crearPanelAccesosRapidos() {

        JPanel exterior
                = crearPanelSeccion(
                        "ACCESOS RÁPIDOS"
                );

        exterior.setLayout(
                new BorderLayout()
        );

        JPanel botones
                = new JPanel(
                        new GridLayout(
                                2,
                                2,
                                12,
                                12
                        )
                );

        botones.setBackground(
                Color.WHITE
        );

        botones.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        botones.add(
                btnNuevaVenta
        );

        botones.add(
                btnEntradaMercaderia
        );

        botones.add(
                btnNuevoCliente
        );

        botones.add(
                btnVerCaja
        );

        exterior.add(
                botones,
                BorderLayout.CENTER
        );

        return exterior;
    }

    //==========================================================
    // TARJETA RESUMEN
    //==========================================================
    private JPanel crearTarjetaResumen(
            String titulo,
            JLabel valor,
            JLabel descripcion) {

        JPanel panel
                = new JPanel();

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
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
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
                new Color(
                        70,
                        75,
                        85
                )
        );

        panel.add(lblTitulo);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(valor);

        panel.add(
                Box.createVerticalStrut(3)
        );

        panel.add(descripcion);

        return panel;
    }

    //==========================================================
    // SECCION
    //==========================================================
    private JPanel crearPanelSeccion(
            String titulo) {

        JPanel panel
                = new JPanel();

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
    // ALERTA
    //==========================================================
    private JPanel crearAlerta(
            String titulo,
            String descripcion,
            Color color) {

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        panel.setBackground(
                new Color(
                        250,
                        251,
                        253
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                4,
                                0,
                                0,
                                color
                        ),
                        new EmptyBorder(
                                6,
                                10,
                                6,
                                10
                        )
                )
        );

        JLabel punto
                = new JLabel("●");

        punto.setForeground(
                color
        );

        punto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JPanel textos
                = new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
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
                        13
                )
        );

        JLabel lblDescripcion
                = new JLabel(
                        descripcion
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblDescripcion.setForeground(
                TEXTO_SECUNDARIO
        );

        textos.add(lblTitulo);
        textos.add(lblDescripcion);

        panel.add(
                punto,
                BorderLayout.WEST
        );

        panel.add(
                textos,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloUltimasVentas.setRowCount(
                0
        );

        modeloUltimasVentas.addRow(
                new Object[]{
                    "00000125",
                    "10:42",
                    "Consumidor Final",
                    "Efectivo",
                    "$ 38.800"
                }
        );

        modeloUltimasVentas.addRow(
                new Object[]{
                    "00000124",
                    "10:35",
                    "Juan Pérez",
                    "Tarjeta",
                    "$ 25.600"
                }
        );

        modeloUltimasVentas.addRow(
                new Object[]{
                    "00000123",
                    "10:17",
                    "María López",
                    "Transferencia",
                    "$ 18.900"
                }
        );

        modeloUltimasVentas.addRow(
                new Object[]{
                    "00000122",
                    "09:58",
                    "Consumidor Final",
                    "Efectivo",
                    "$ 12.500"
                }
        );

        modeloUltimasVentas.addRow(
                new Object[]{
                    "00000121",
                    "09:40",
                    "Comercial NEA",
                    "Cuenta corriente",
                    "$ 42.700"
                }
        );

        //======================================================
        // PRODUCTOS MAS VENDIDOS
        //======================================================
        modeloMasVendidos.setRowCount(
                0
        );

        modeloMasVendidos.addRow(
                new Object[]{
                    "Coca Cola 2.25 L",
                    "42",
                    "$ 109.200"
                }
        );

        modeloMasVendidos.addRow(
                new Object[]{
                    "Aceite Natura 900 ml",
                    "27",
                    "$ 86.400"
                }
        );

        modeloMasVendidos.addRow(
                new Object[]{
                    "Yerba Playadito 1 Kg",
                    "19",
                    "$ 93.100"
                }
        );

        modeloMasVendidos.addRow(
                new Object[]{
                    "Papel Higiénico Elite x4",
                    "16",
                    "$ 56.000"
                }
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnNuevaVenta.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aquí navegaremos directamente a Ventas.",
                    "Nueva Venta",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnEntradaMercaderia.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Abrirá Entrada de Mercadería.",
                    "Stock",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnNuevoCliente.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Abrirá Nuevo Cliente.",
                    "Clientes",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnVerCaja.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Navegará al módulo Caja.",
                    "Caja",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    //==========================================================
    // LABELS
    //==========================================================
    private JLabel crearValorPrincipal(
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
                        21
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    private JLabel crearTextoSecundario(
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
                TEXTO_SECUNDARIO
        );

        return label;
    }

    private JLabel crearTextoEstado(
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
                        12
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    //==========================================================
    // BOTON RAPIDO
    //==========================================================
    private RSButtonRound crearBotonRapido(
            String texto,
            Color color) {

        RSButtonRound boton
                = new RSButtonRound();

        boton.setText(
                texto
        );

        boton.setBackground(
                color
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setColorText(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        boton.setFocusable(
                false
        );

        return boton;
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
