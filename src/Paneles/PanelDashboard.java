/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Configuracion.conexion;
import services.CajaService;
import java.sql.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import javax.swing.SwingWorker;
import java.util.List;
import java.util.ArrayList;
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
    private JTable tablaReposicion;
    private DefaultTableModel modeloReposicion;
    private JLabel lblResumenReposicion;
    private JTable tablaUltimasVentas;
    private DefaultTableModel modeloUltimasVentas;

    private JTable tablaMasVendidos;
    private DefaultTableModel modeloMasVendidos;

    //==========================================================
    // ALERTAS
    //==========================================================
    private JPanel panelAlertas;
    private final GraficoVentas graficoVentas = new GraficoVentas();
    private boolean cargando = false;
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

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
        new Color(242, 246, 252);

    private final Color BORDE
            = new Color(218, 227, 240);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelDashboard() {

        inicializarComponentes();
        construirPanel();
        actualizarDatos();
        configurarEventos();
        addHierarchyListener(e -> { if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED)!=0 && isShowing()) actualizarDatos(); });
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        lblVentasHoy
                = crearValorPrincipal(
                        "$ 0,00",
                        VERDE
                );

        lblCantidadVentas
                = crearTextoSecundario(
                        "Consultando ventas..."
                );

        lblCajaActual
                = crearValorPrincipal(
                        "$ 0,00",
                        AZUL_OSCURO
                );

        lblEstadoCaja
                = crearTextoEstado(
                        "Consultando caja...",
                        VERDE
                );

        lblStockBajo
                = crearValorPrincipal(
                        "5",
                        NARANJA
                );

        lblSinStock
                = crearTextoSecundario(
                        "Consultando stock..."
                );

        lblVentasMes
                = crearValorPrincipal(
                        "$ 0,00",
                        AZUL
                );

        lblCantidadMes
                = crearTextoSecundario(
                        "Consultando ventas..."
                );

        btnNuevaVenta
                = crearBotonRapido(
                        "VENDER",
                        VERDE
                );

        btnEntradaMercaderia
                = crearBotonRapido(
                        "STOCK",
                        AZUL
                );

        btnNuevoCliente
                = crearBotonRapido(
                        "CLIENTES",
                        AZUL_OSCURO
                );

        btnVerCaja
                = crearBotonRapido(
                        "CAJA",
                        NARANJA
                );

        btnNuevaVenta.setIcon(new IconoAtajo(0));
        btnEntradaMercaderia.setIcon(new IconoAtajo(1));
        btnNuevoCliente.setIcon(new IconoAtajo(2));
        btnVerCaja.setIcon(new IconoAtajo(3));
        btnNuevaVenta.setIconTextGap(8);
        btnEntradaMercaderia.setIconTextGap(8);
        btnNuevoCliente.setIconTextGap(8);
        btnVerCaja.setIconTextGap(8);

        inicializarTablaReposicion();
        panelAlertas=new JPanel(); // Se conserva el cálculo de alertas para una futura vista.
        inicializarTablaUltimasVentas();
        inicializarTablaMasVendidos();
    }

    private void inicializarTablaReposicion() {
        modeloReposicion = new DefaultTableModel(new Object[]{"Código", "Producto", "Depósito", "Stock", "Mínimo", "Estado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaReposicion = crearTabla(modeloReposicion);
        tablaReposicion.setRowHeight(33);
        tablaReposicion.setAutoCreateRowSorter(true);
        tablaReposicion.getColumnModel().getColumn(0).setPreferredWidth(95);
        tablaReposicion.getColumnModel().getColumn(1).setPreferredWidth(270);
        tablaReposicion.getColumnModel().getColumn(2).setPreferredWidth(155);
        tablaReposicion.getColumnModel().getColumn(3).setPreferredWidth(75);
        tablaReposicion.getColumnModel().getColumn(4).setPreferredWidth(75);
        tablaReposicion.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaReposicion.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setBorder(new EmptyBorder(0, 9, 0, 9));
                if (!sel) {
                    int modelRow=t.convertRowIndexToModel(row);
                    String estado=String.valueOf(t.getModel().getValueAt(modelRow,5));
                    setBackground(col==5 ? ("SIN STOCK".equals(estado)?new Color(255,226,226):new Color(255,243,208))
                            : (row%2==0?Color.WHITE:new Color(244,248,253)));
                    setForeground(col==5 ? ("SIN STOCK".equals(estado)?ROJO:new Color(151,95,0)):AZUL_OSCURO);
                }
                setFont(new Font("Segoe UI", col==5?Font.BOLD:Font.PLAIN,12));
                setHorizontalAlignment(col==3||col==4?SwingConstants.RIGHT:SwingConstants.LEFT);
                return this;
            }
        });
        tablaReposicion.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount()==2) abrirAccesoRapido("STOCK");
            }
        });
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
        setLayout(new BorderLayout());
        setBackground(FONDO);
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido,BoxLayout.Y_AXIS));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(10,12,12,12));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL_OSCURO);
        cabecera.setBorder(new EmptyBorder(12,17,12,17));
        JLabel titulo=new JLabel("CENTRO DE CONTROL");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI",Font.BOLD,22));
        cabecera.add(titulo,BorderLayout.WEST);
        JLabel leyenda=new JLabel("Resumen general del negocio");
        leyenda.setForeground(new Color(215,232,255));
        cabecera.add(leyenda,BorderLayout.EAST);
        agregarBloque(contenido,cabecera,58,7);

        JPanel saludo=new JPanel(new BorderLayout());
        saludo.setBackground(new Color(233,242,253));
        saludo.setBorder(new EmptyBorder(9,14,9,14));
        String usuario=SesionUsuario.getNombreUsuario();
        JLabel bienvenida=new JLabel("Buenos días"+(usuario==null||usuario.trim().isEmpty()?"":", "+usuario.trim().split("\\s+")[0]));
        bienvenida.setFont(new Font("Segoe UI",Font.BOLD,16));
        bienvenida.setForeground(AZUL_OSCURO);
        saludo.add(bienvenida,BorderLayout.NORTH);
        JLabel sub=new JLabel("Gestión de ventas  |  Control de stock  |  Operaciones del día");
        sub.setForeground(TEXTO_SECUNDARIO);
        saludo.add(sub,BorderLayout.SOUTH);
        agregarBloque(contenido,saludo,58,9);

        JPanel principal=new JPanel(new BorderLayout(12,0));
        principal.setOpaque(false);
        JPanel izquierda=new JPanel(new BorderLayout(0,10));
        izquierda.setOpaque(false);
        izquierda.add(crearPanelReposicion(),BorderLayout.CENTER);
        JPanel tablasInferiores=new JPanel(new GridLayout(1,2,10,0));
        tablasInferiores.setOpaque(false);
        tablasInferiores.add(crearPanelUltimasVentas());
        tablasInferiores.add(crearPanelMasVendidos());
        tablasInferiores.setPreferredSize(new Dimension(100,210));
        izquierda.add(tablasInferiores,BorderLayout.SOUTH);
        principal.add(izquierda,BorderLayout.CENTER);

        JPanel derecha=new JPanel(new BorderLayout(0,10));
        derecha.setOpaque(false);
        derecha.setPreferredSize(new Dimension(300,100));
        JPanel accesos=crearPanelAccesosRapidos();
        accesos.setPreferredSize(new Dimension(300,164));
        derecha.add(accesos,BorderLayout.NORTH);
        derecha.add(crearPanelIndicadoresLaterales(),BorderLayout.CENTER);
        principal.add(derecha,BorderLayout.EAST);
        principal.setPreferredSize(new Dimension(900,520));
        agregarBloque(contenido,principal,520,9);

        // Los indicadores se presentan a la derecha, sin duplicar tarjetas al pie.
        JScrollPane scroll=new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll,BorderLayout.CENTER);
    }

    private void agregarBloque(JPanel destino, JPanel bloque, int alto, int separacion){
        bloque.setAlignmentX(LEFT_ALIGNMENT);
        bloque.setPreferredSize(new Dimension(900,alto));
        bloque.setMaximumSize(new Dimension(Integer.MAX_VALUE,alto));
        destino.add(bloque);
        if(separacion>0) destino.add(Box.createVerticalStrut(separacion));
    }

    private JPanel crearPanelReposicion(){
        JPanel panel=crearPanelSeccion("PRODUCTOS CON BAJO STOCK  |  Reposición prioritaria");
        panel.setLayout(new BorderLayout(0,5));
        JPanel barra=new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(new EmptyBorder(4,8,5,8));
        lblResumenReposicion=new JLabel("Consultando existencias...");
        lblResumenReposicion.setForeground(TEXTO_SECUNDARIO);
        barra.add(lblResumenReposicion,BorderLayout.WEST);
        javax.swing.JButton verStock = new javax.swing.JButton("VER STOCK COMPLETO") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()?new Color(12,39,85):
                        getModel().isRollover()?new Color(34,87,164):AZUL);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        verStock.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        verStock.setContentAreaFilled(false);
        verStock.setOpaque(false);
        verStock.setBorder(BorderFactory.createEmptyBorder(6,14,6,14));
        verStock.setForeground(Color.WHITE);
        verStock.setFont(new Font("Segoe UI",Font.BOLD,11));
        verStock.setFocusPainted(false);
        verStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        verStock.setPreferredSize(new Dimension(168,29));
        verStock.addActionListener(e->abrirAccesoRapido("STOCK"));
        barra.add(verStock,BorderLayout.EAST);
        panel.add(barra,BorderLayout.NORTH);
        JScrollPane sc=new JScrollPane(tablaReposicion);
        sc.setBorder(BorderFactory.createEmptyBorder());
        panel.add(sc,BorderLayout.CENTER);
        return panel;
    }

    /** Tres indicadores reales, compactos, en lugar de alertas duplicadas. */
    private JPanel crearPanelIndicadoresLaterales() {
        JPanel exterior=crearPanelSeccion("RESUMEN DEL NEGOCIO");
        exterior.setLayout(new BorderLayout());
        JPanel columna=new JPanel(new GridLayout(3,1,0,8));
        columna.setBackground(Color.WHITE);
        columna.setBorder(new EmptyBorder(8,10,8,10));
        columna.add(crearIndicadorLateral("VENTAS DE HOY",lblVentasHoy,lblCantidadVentas,VERDE));
        columna.add(crearIndicadorLateral("CAJA ACTUAL",lblCajaActual,lblEstadoCaja,AZUL));
        columna.add(crearIndicadorLateral("VENTAS DEL MES",lblVentasMes,lblCantidadMes,new Color(109,71,174)));
        exterior.add(columna,BorderLayout.CENTER);
        return exterior;
    }

    private JPanel crearIndicadorLateral(String titulo,JLabel valor,JLabel detalle,Color acento) {
        JPanel tarjeta=new JPanel(new BorderLayout(0,2));
        tarjeta.setBackground(new Color(247,250,255));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,4,0,0,acento),
                new EmptyBorder(5,10,5,8)));
        JLabel nombre=new JLabel(titulo);
        nombre.setForeground(AZUL_OSCURO);
        nombre.setFont(new Font("Segoe UI",Font.BOLD,11));
        valor.setFont(new Font("Segoe UI",Font.BOLD,19));
        detalle.setFont(new Font("Segoe UI",Font.PLAIN,10));
        tarjeta.add(nombre,BorderLayout.NORTH);
        tarjeta.add(valor,BorderLayout.CENTER);
        tarjeta.add(detalle,BorderLayout.SOUTH);
        return tarjeta;
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
                    "CENTRO DE CONTROL"
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
                    "INDICADORES EN VIVO  |  Información registrada en la base de datos"
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
                        "ÚLTIMAS OPERACIONES  |  Seguimiento de ventas"
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
                        "ALERTAS Y SEGUIMIENTO  |  Atención requerida"
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

        panelAlertas.add(crearAlerta("Actualizando", "Consultando indicadores de MariaDB...", AZUL));

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
                        "PRODUCTOS MÁS VENDIDOS  |  Ranking comercial"
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
                        new GridLayout(2, 2, 9, 9)
                );

        botones.setBackground(
                Color.WHITE
        );

        botones.setBorder(
                new EmptyBorder(10, 12, 10, 12)
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

        Color acento = titulo.contains("STOCK") ? NARANJA
                : titulo.contains("CAJA") ? AZUL
                : titulo.contains("MES") ? new Color(109, 71, 174) : VERDE;
        Color suave = titulo.contains("STOCK") ? new Color(255, 247, 233)
                : titulo.contains("CAJA") ? new Color(233, 241, 255)
                : titulo.contains("MES") ? new Color(245, 239, 255)
                : new Color(232, 249, 241);
        panel.setBackground(suave);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        BorderFactory.createMatteBorder(0, 5, 0, 0, acento)),
                new EmptyBorder(15, 17, 12, 12)));

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
                = new JLabel("!");

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
    private static class Datos {
        BigDecimal hoy=BigDecimal.ZERO, mes=BigDecimal.ZERO;
        int cantidadHoy, cantidadMes, bajo, sinStock;
        CajaService.Resumen caja;
        List<Object[]> ventas=new ArrayList<>(), productos=new ArrayList<>(), reposicion=new ArrayList<>();
        List<String> faltantes=new ArrayList<>();
        double[] grafico=new double[7];
        String[] dias=new String[7];
    }

    private String dinero(BigDecimal n) { return moneda.format(n==null?BigDecimal.ZERO:n); }

    public final void actualizarDatos() {
        if(cargando) return;
        cargando=true;
        new SwingWorker<Datos,Void>() {
            @Override protected Datos doInBackground() throws Exception { return consultarDatos(); }
            @Override protected void done() {
                cargando=false;
                try { aplicarDatos(get()); }
                catch(Exception e) {
                    Throwable t=e.getCause()==null?e:e.getCause();
                    lblVentasHoy.setText("—"); lblVentasMes.setText("—"); lblCajaActual.setText("—"); lblStockBajo.setText("—");
                    lblCantidadVentas.setText("Consulta pendiente"); lblCantidadMes.setText("Consulta pendiente"); lblSinStock.setText("Consulta pendiente");
                    modeloUltimasVentas.setRowCount(0); modeloMasVendidos.setRowCount(0); modeloReposicion.setRowCount(0);
                    lblResumenReposicion.setText("No se pudo consultar el stock");
                    lblEstadoCaja.setText("Error al consultar datos");
                    panelAlertas.removeAll();
                    panelAlertas.add(crearAlerta("Sin conexión o consulta fallida", t.getMessage()==null?"Revisar conexión":t.getMessage(), ROJO));
                    panelAlertas.revalidate(); panelAlertas.repaint();
                }
            }
        }.execute();
    }

    @Override public void addNotify() {
        super.addNotify();
        if(lblVentasHoy!=null) actualizarDatos();
    }

    private Datos consultarDatos() throws Exception {
        Datos d=new Datos();
        try(Connection c=conexion.getConexion()) {
            // Los totales representan ventas finalizadas sin anulaciones ni devoluciones totales.
            // Las devoluciones parciales requieren un cálculo neto específico para evitar inflar ingresos.
            String base=" FROM venta WHERE estado IN ('FINALIZADA','DEVUELTA_PARCIAL') ";
            try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(total),0),COUNT(*)"+base+"AND fecha>=CURDATE() AND fecha<CURDATE()+INTERVAL 1 DAY"); ResultSet r=p.executeQuery()) {
                if(r.next()){d.hoy=r.getBigDecimal(1);d.cantidadHoy=r.getInt(2);}
            }
            try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(total),0),COUNT(*)"+base+"AND fecha>=DATE_FORMAT(CURDATE(),'%Y-%m-01') AND fecha<DATE_FORMAT(CURDATE()+INTERVAL 1 MONTH,'%Y-%m-01')");ResultSet r=p.executeQuery()) {
                if(r.next()){d.mes=r.getBigDecimal(1);d.cantidadMes=r.getInt(2);}
            }
            String stock=" FROM producto p LEFT JOIN (SELECT id_producto,SUM(cantidad) cantidad FROM stock_producto GROUP BY id_producto) sp ON sp.id_producto=p.id_producto WHERE p.activo=1 AND p.controla_stock=1 ";
            try(PreparedStatement p=c.prepareStatement("SELECT SUM(CASE WHEN COALESCE(sp.cantidad,0)<=0 THEN 1 ELSE 0 END), SUM(CASE WHEN COALESCE(sp.cantidad,0)>0 AND COALESCE(sp.cantidad,0)<p.stock_minimo THEN 1 ELSE 0 END)"+stock);ResultSet r=p.executeQuery()) {
                if(r.next()){d.sinStock=r.getInt(1);d.bajo=r.getInt(2);}
            }
            // Stock agrupado por producto y depósito; los faltantes se priorizan por existencia.
            // El umbral mínimo corresponde al producto y se compara con la existencia consolidada.
            String sqlReposicion = "SELECT p.codigo,p.nombre,COALESCE(sp.cantidad,0),COALESCE(p.stock_minimo,0) "
                    + stock + " AND COALESCE(sp.cantidad,0) < COALESCE(p.stock_minimo,0) "
                    + "ORDER BY CASE WHEN COALESCE(sp.cantidad,0)<=0 THEN 0 ELSE 1 END, "
                    + "COALESCE(sp.cantidad,0) ASC,p.nombre ASC LIMIT 100";
            try(PreparedStatement p=c.prepareStatement(sqlReposicion);ResultSet r=p.executeQuery()) {
                while(r.next()) {
                    BigDecimal cantidad=r.getBigDecimal(3);
                    BigDecimal minimo=r.getBigDecimal(4);
                    if(cantidad==null)cantidad=BigDecimal.ZERO;
                    if(minimo==null)minimo=BigDecimal.ZERO;
                    d.reposicion.add(new Object[]{r.getString(1),r.getString(2),"Todos los depósitos",
                            cantidad.stripTrailingZeros().toPlainString(),minimo.stripTrailingZeros().toPlainString(),
                            cantidad.signum()<=0?"SIN STOCK":"STOCK BAJO"});
                }
            }
            try(PreparedStatement p=c.prepareStatement("SELECT p.nombre,COALESCE(sp.cantidad,0) cantidad"+stock+" AND COALESCE(sp.cantidad,0)<p.stock_minimo ORDER BY cantidad ASC LIMIT 3");ResultSet r=p.executeQuery()) {
                while(r.next())d.faltantes.add(r.getString(1)+" ("+r.getBigDecimal(2).stripTrailingZeros().toPlainString()+")");
            }
            try(PreparedStatement p=c.prepareStatement("SELECT v.numero_venta,DATE_FORMAT(v.fecha,'%H:%i'),COALESCE(NULLIF(TRIM(CONCAT(COALESCE(cl.nombre,''),' ',COALESCE(cl.apellido,''))),''),NULLIF(cl.razon_social,''),'Consumidor Final'),COALESCE((SELECT GROUP_CONCAT(DISTINCT mp.nombre SEPARATOR ' + ') FROM venta_pago vp JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE vp.id_venta=v.id_venta),'Sin pago'),v.total FROM venta v LEFT JOIN cliente cl ON cl.id_cliente=v.id_cliente WHERE v.estado IN ('FINALIZADA','DEVUELTA_PARCIAL','DEVUELTA_TOTAL') ORDER BY v.fecha DESC,v.id_venta DESC LIMIT 8");ResultSet r=p.executeQuery()) {
                while(r.next())d.ventas.add(new Object[]{String.format("%08d",r.getLong(1)),r.getString(2),r.getString(3),r.getString(4),dinero(r.getBigDecimal(5))});
            }
            try(PreparedStatement p=c.prepareStatement("SELECT vd.descripcion_producto,SUM(vd.cantidad),SUM(vd.subtotal) FROM venta_detalle vd JOIN venta v ON v.id_venta=vd.id_venta WHERE v.estado IN ('FINALIZADA','DEVUELTA_PARCIAL') AND v.fecha>=DATE_FORMAT(CURDATE(),'%Y-%m-01') GROUP BY vd.id_producto,vd.descripcion_producto ORDER BY SUM(vd.cantidad) DESC LIMIT 8");ResultSet r=p.executeQuery()) {
                while(r.next())d.productos.add(new Object[]{r.getString(1),r.getBigDecimal(2).stripTrailingZeros().toPlainString(),dinero(r.getBigDecimal(3))});
            }
            java.time.LocalDate today=java.time.LocalDate.now();
            for(int i=0;i<7;i++) d.dias[i]=today.minusDays(6-i).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
            try(PreparedStatement p=c.prepareStatement("SELECT DATE(fecha),SUM(total) FROM venta WHERE estado IN ('FINALIZADA','DEVUELTA_PARCIAL') AND fecha>=CURDATE()-INTERVAL 6 DAY AND fecha<CURDATE()+INTERVAL 1 DAY GROUP BY DATE(fecha)");ResultSet r=p.executeQuery()) {
                while(r.next()){int i=(int)java.time.temporal.ChronoUnit.DAYS.between(today.minusDays(6),r.getDate(1).toLocalDate());if(i>=0&&i<7)d.grafico[i]=r.getDouble(2);}
            }
        }
        try { d.caja=new CajaService().obtenerResumen(); } catch(Exception cajaError) { System.err.println("Dashboard: caja no disponible: "+cajaError.getMessage()); }
        return d;
    }

    private void aplicarDatos(Datos d) {
        lblVentasHoy.setText(dinero(d.hoy));
        lblCantidadVentas.setText(d.cantidadHoy+" ventas realizadas hoy");
        lblVentasMes.setText(dinero(d.mes));
        lblCantidadMes.setText(d.cantidadMes+" ventas este mes");
        lblStockBajo.setText(String.valueOf(d.bajo));
        lblSinStock.setText(d.sinStock+" productos sin stock");
        boolean abierta=d.caja!=null&&d.caja.idSesion>0;
        lblCajaActual.setText(abierta?dinero(d.caja.efectivoEsperado):dinero(BigDecimal.ZERO));
        lblEstadoCaja.setText(abierta?"CAJA ABIERTA #"+d.caja.idSesion:"SIN CAJA ABIERTA / NO DISPONIBLE");
        lblEstadoCaja.setForeground(abierta?VERDE:ROJO);
        modeloReposicion.setRowCount(0);
        for(Object[] fila:d.reposicion)modeloReposicion.addRow(fila);
        lblResumenReposicion.setText(d.reposicion.isEmpty()?"No hay artículos por debajo del mínimo":"Productos que necesitan reposición: "+(d.bajo+d.sinStock));
        modeloUltimasVentas.setRowCount(0);
        for(Object[] fila:d.ventas)modeloUltimasVentas.addRow(fila);
        modeloMasVendidos.setRowCount(0);
        for(Object[] fila:d.productos)modeloMasVendidos.addRow(fila);
        graficoVentas.setDatos(d.dias,d.grafico);
        panelAlertas.removeAll();
        panelAlertas.add(crearAlerta("Stock bajo",d.bajo+" productos por debajo del mínimo",NARANJA));
        panelAlertas.add(Box.createVerticalStrut(8));
        panelAlertas.add(crearAlerta("Sin stock",d.sinStock+" productos sin existencias",ROJO));
        panelAlertas.add(Box.createVerticalStrut(8));
        panelAlertas.add(crearAlerta(abierta?"Caja abierta":"Caja cerrada",abierta?"Sesión #"+d.caja.idSesion+" · "+d.caja.cajero:"No tenés una caja abierta",abierta?VERDE:GRIS));

        panelAlertas.revalidate();panelAlertas.repaint();
    }

    private static class GraficoVentas extends JPanel {
        private String[] dias=new String[7];
        private double[] valores=new double[7];
        GraficoVentas(){setBackground(Color.WHITE);setPreferredSize(new Dimension(100,145));}
        void setDatos(String[] d,double[] v){dias=d.clone();valores=v.clone();repaint();}
        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D x=(Graphics2D)g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight(),left=45,right=w-25,top=14,bottom=h-30;
            if(right<=left||bottom<=top){x.dispose();return;}
            double max=1;for(double v:valores)max=Math.max(max,v);
            x.setColor(new Color(231,236,246));
            for(int i=0;i<=3;i++){int y=top+(bottom-top)*i/3;x.drawLine(left,y,right,y);}
            Path2D line=new Path2D.Double(),fill=new Path2D.Double();
            for(int i=0;i<7;i++){
                double xx=left+(right-left)*i/6.0,yy=bottom-(bottom-top)*valores[i]/max;
                if(i==0){line.moveTo(xx,yy);fill.moveTo(xx,bottom);fill.lineTo(xx,yy);}else{line.lineTo(xx,yy);fill.lineTo(xx,yy);}
            }
            fill.lineTo(right,bottom);fill.closePath();
            x.setColor(new Color(228,241,255));x.fill(fill);
            x.setStroke(new BasicStroke(2.8f));x.setColor(new Color(24,83,171));x.draw(line);
            for(int i=0;i<7;i++){
                int xx=left+(right-left)*i/6,yy=bottom-(int)Math.round((bottom-top)*valores[i]/max);
                x.setColor(Color.WHITE);x.fillOval(xx-5,yy-5,10,10);
                x.setColor(new Color(24,83,171));x.setStroke(new BasicStroke(2));x.drawOval(xx-5,yy-5,10,10);
                x.setFont(new Font("Segoe UI",Font.PLAIN,11));x.setColor(new Color(95,110,135));
                if(dias[i]!=null)x.drawString(dias[i],xx-14,h-9);
            }
            x.dispose();
        }
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {
        btnNuevaVenta.addActionListener(e -> abrirAccesoRapido("VENTAS"));
        btnEntradaMercaderia.addActionListener(e -> abrirAccesoRapido("STOCK"));
        btnNuevoCliente.addActionListener(e -> abrirAccesoRapido("CLIENTES"));
        btnVerCaja.addActionListener(e -> abrirAccesoRapido("CAJA"));
    }

    /** Reutiliza la navegacion existente del panel principal, sin crear ventanas extras. */
    private void abrirAccesoRapido(String destino) {
        java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (!(ventana instanceof Principal.PanelPrincipal)) {
            JOptionPane.showMessageDialog(this,
                    "No se encontro la ventana principal para abrir " + destino + ".",
                    "Serena Soft", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Principal.PanelPrincipal principal = (Principal.PanelPrincipal) ventana;
        switch (destino) {
            case "VENTAS":
                principal.mostrarVentas();
                break;
            case "STOCK":
                // El boton ENTRADA ya existe en PanelStock; no se duplica su circuito.
                principal.mostrarPanel(new PanelStock());
                break;
            case "CLIENTES":
                principal.mostrarPanel(new PanelClientes());
                break;
            case "CAJA":
                principal.mostrarPanel(new PanelCaja());
                break;
            default:
                break;
        }
    }

    /** Iconos vectoriales: no dependen de fuentes Emoji ni archivos externos. */
    private static class IconoAtajo implements javax.swing.Icon {
        private final int tipo;
        IconoAtajo(int tipo) { this.tipo = tipo; }
        @Override public int getIconWidth() { return 19; }
        @Override public int getIconHeight() { return 19; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D d = (Graphics2D) g.create();
            d.translate(x, y);
            d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            d.setColor(new Color(255, 229, 125));
            if (tipo == 0) { // venta
                d.drawRoundRect(2, 2, 15, 15, 4, 4);
                d.drawLine(9, 5, 9, 14); d.drawLine(5, 9, 13, 9);
            } else if (tipo == 1) { // stock
                d.drawRect(2, 6, 15, 11); d.drawLine(2, 6, 9, 2);
                d.drawLine(9, 2, 17, 6); d.drawLine(9, 2, 9, 17);
            } else if (tipo == 2) { // cliente
                d.drawOval(6, 2, 7, 7); d.drawArc(2, 9, 15, 10, 0, 180);
            } else { // caja
                d.drawRoundRect(2, 5, 15, 11, 3, 3);
                d.drawLine(5, 2, 14, 2); d.drawOval(8, 8, 4, 4);
            }
            d.dispose();
        }
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
                        25
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
                        11
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
