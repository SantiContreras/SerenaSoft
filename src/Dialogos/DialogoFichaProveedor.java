package Dialogos;

import Configuracion.conexion;
import java.sql.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoFichaProveedor extends JDialog {

    //==========================================================
    // PROVEEDOR
    //==========================================================
    private int idProveedor;
    private String nombreProveedorCabecera = "PROVEEDOR";

    private static final class IconoResumen implements javax.swing.Icon {
        private final String tipo;
        private final Color color;
        IconoResumen(String tipo, Color color) { this.tipo = tipo; this.color = color; }
        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }
        @Override public void paintIcon(Component c, java.awt.Graphics g, int x, int y) {
            java.awt.Graphics2D a = (java.awt.Graphics2D) g.create();
            try {
                a.translate(x, y);
                a.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                a.setColor(color);
                a.setStroke(new java.awt.BasicStroke(1.8f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                if (tipo.equals("Total Comprado")) {
                    a.drawRoundRect(2, 5, 16, 12, 3, 3); a.drawLine(5, 5, 7, 2); a.drawLine(7, 2, 15, 2);
                    a.drawLine(6, 11, 14, 11);
                } else if (tipo.equals("Total Pagado")) {
                    a.drawOval(2, 2, 16, 16); a.drawLine(6, 10, 9, 13); a.drawLine(9, 13, 15, 7);
                } else if (tipo.equals("Saldo Pendiente")) {
                    a.drawOval(2, 2, 16, 16); a.drawLine(10, 5, 10, 11); a.fillOval(9, 14, 2, 2);
                } else {
                    a.drawRoundRect(2, 4, 16, 14, 2, 2); a.drawLine(2, 8, 18, 8);
                    a.drawLine(6, 2, 6, 6); a.drawLine(14, 2, 14, 6); a.fillOval(6, 11, 3, 3);
                }
            } finally { a.dispose(); }
        }
    }

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JLabel lblRazonSocial;
    private JLabel lblNombreComercial;
    private JLabel lblCuit;
    private JLabel lblCondicionIva;
    private JLabel lblDireccion;
    private JLabel lblLocalidad;
    private JLabel lblProvincia;
    private JLabel lblTelefono;
    private JLabel lblEmail;
    private JLabel lblContacto;
    private JLabel lblEstado;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblTotalComprado;
    private JLabel lblTotalPagado;
    private JLabel lblSaldoPendiente;
    private JLabel lblUltimaCompra;

    //==========================================================
    // TABLAS
    //==========================================================
    private JPanel panelNavegacion;
    private CardLayout tarjetasGuia;
    private JPanel contenidoGuia;
    private JButton navDeudas, navCompras, navDatos;
    private JButton subMovimientos, subPagos;
    private CardLayout tarjetasDeudas;
    private JPanel contenidoDeudas;

    private JLabel ayudaPagoGuiado;
    private final java.util.List<Long> idsFacturas = new java.util.ArrayList<>();
    private JTable tablaCompras;
    private JTable tablaFacturas;
    private JTable tablaCuentaCorriente;
    private JTable tablaPagos;

    private DefaultTableModel modeloCompras;
    private DefaultTableModel modeloFacturas;
    private DefaultTableModel modeloCuenta;
    private DefaultTableModel modeloPagos;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnNuevaCompra;
    private JButton btnRegistrarPago;
    private JButton btnCerrar;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BLANCO
            = Color.WHITE;

    private final Color AZUL
            = new Color(25, 70, 145);

    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color VERDE
            = new Color(25, 135, 84);

    private final Color ROJO
            = new Color(200, 55, 55);

    private final Color GRIS
            = new Color(110, 120, 135);

    private final Color NARANJA
            = new Color(235, 145, 20);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoFichaProveedor(
            Window parent,
            int idProveedor,
            String razonSocial,
            String cuit,
            String telefono,
            String estado) {

        super(parent);

        this.idProveedor = idProveedor;
        this.nombreProveedorCabecera = (razonSocial == null || razonSocial.isBlank()) ? "PROVEEDOR" : razonSocial;

        inicializarComponentes();

        construirDialogo();

        cargarDatosProveedor(
                razonSocial,
                cuit,
                telefono,
                estado
        );

        cargarDatosReales();

        configurarEventos();

        setModal(true);

        setTitle(
                "Ficha del Proveedor"
        );

        setSize(
                new Dimension(
                        1100,
                        720
                )
        );

        setMinimumSize(
                new Dimension(
                        950,
                        650
                )
        );

        setLocationRelativeTo(parent);
         // Los botones vectoriales gestionan su propio contraste.
        setResizable(true);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        lblRazonSocial = crearValor("-");
        lblNombreComercial = crearValor("-");
        lblCuit = crearValor("-");
        lblCondicionIva = crearValor("-");
        lblDireccion = crearValor("-");
        lblLocalidad = crearValor("-");
        lblProvincia = crearValor("-");
        lblTelefono = crearValor("-");
        lblEmail = crearValor("-");
        lblContacto = crearValor("-");
        lblEstado = crearValor("-");

        lblTotalComprado
                = crearValorResumen(
                        "$ 0,00",
                        AZUL
                );

        lblTotalPagado
                = crearValorResumen(
                        "$ 0,00",
                        VERDE
                );

        lblSaldoPendiente
                = crearValorResumen(
                        "$ 0,00",
                        ROJO
                );

        lblUltimaCompra
                = crearValorResumen(
                        "-",
                        NARANJA
                );

        btnNuevaCompra
                = crearBoton(
                        "Nueva Compra",
                        AZUL,
                        150
                );

        btnRegistrarPago
                = crearBoton(
                        "Registrar Pago",
                        VERDE,
                        150
                );

        btnCerrar
                = crearBoton(
                        "Cerrar",
                        GRIS,
                        120
                );

        inicializarTablas();
    }

    //==========================================================
    // TABLAS
    //==========================================================
    private void inicializarTablas() {

        modeloCompras
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Fecha",
                            "Factura",
                            "Productos",
                            "Total",
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

        tablaCompras
                = crearTabla(
                        modeloCompras
                );

        modeloFacturas
                = new DefaultTableModel(
                        new Object[]{
                            "Factura",
                            "Fecha",
                            "Vencimiento",
                            "Importe",
                            "Pagado",
                            "Saldo",
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

        tablaFacturas
                = crearTabla(
                        modeloFacturas
                );
        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) actualizarSeleccionFactura();
        });
        tablaFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && javax.swing.SwingUtilities.isLeftMouseButton(e)) pagarFacturaSeleccionada();
            }
        });

        modeloCuenta
                = new DefaultTableModel(
                        new Object[]{
                            "Fecha",
                            "Movimiento",
                            "Referencia",
                            "Debe",
                            "Haber",
                            "Saldo"
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

        tablaCuentaCorriente
                = crearTabla(
                        modeloCuenta
                );

        modeloPagos
                = new DefaultTableModel(
                        new Object[]{
                            "Fecha",
                            "Importe",
                            "Medio de Pago",
                            "Referencia",
                            "Observación"
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

        tablaPagos
                = crearTabla(
                        modeloPagos
                );
    }

    //==========================================================
    // CREAR TABLA
    //==========================================================
    private JTable crearTabla(
            DefaultTableModel modelo) {

        JTable tabla
                = new JTable(modelo);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.setRowHeight(30);

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
                        30,
                        30,
                        30
                )
        );

        tabla.setGridColor(
                new Color(
                        220,
                        226,
                        235
                )
        );

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(true);

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
                                38
                        )
                );

        tabla.getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul(tabla);
        if (modelo == modeloFacturas) {
            tabla.getColumnModel().getColumn(6).setCellRenderer(new EstadoFacturaRenderer());
        }

        return tabla;
    }


    /** Estados de pago con contraste y colores suaves, sin fuentes de iconos externas. */
    private static final class EstadoFacturaRenderer extends DefaultTableCellRenderer {
        EstadoFacturaRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        @Override public Component getTableCellRendererComponent(JTable table, Object value,
                boolean selected, boolean focus, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focus, row, column);
            String estado = value == null ? "" : value.toString().trim().toUpperCase(Locale.ROOT);
            setText("  " + estado + "  ");
            if ("PAGADA".equals(estado)) {
                setForeground(new Color(18, 113, 67));
                setBackground(selected ? new Color(182, 223, 201) : new Color(225, 246, 234));
            } else if ("PARCIAL".equals(estado)) {
                setForeground(new Color(145, 85, 10));
                setBackground(selected ? new Color(247, 219, 158) : new Color(255, 244, 217));
            } else if ("PENDIENTE".equals(estado)) {
                setForeground(new Color(173, 44, 51));
                setBackground(selected ? new Color(248, 197, 198) : new Color(255, 232, 233));
            } else {
                setForeground(selected ? table.getSelectionForeground() : table.getForeground());
                setBackground(selected ? table.getSelectionBackground() : table.getBackground());
            }
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            return this;
        }
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
                        = (JLabel) super
                                .getTableCellRendererComponent(
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
                        BorderFactory
                                .createMatteBorder(
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
        JPanel header
                = new JPanel();

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        header.setBackground(
                AZUL_OSCURO
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        18,
                        25
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        nombreProveedorCabecera.toUpperCase(java.util.Locale.ROOT)
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
                        "FICHA DEL PROVEEDOR  |  Compras, facturas y cuenta corriente"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(
                new Color(220, 233, 252)
        );

        header.add(lblTitulo);

        header.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        header.add(lblSubtitulo);

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel centro
                = new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        centro.setBackground(
                FONDO
        );

        centro.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        //======================================================
        // RESUMEN
        //======================================================
        centro.add(
                crearPanelResumen(),
                BorderLayout.NORTH
        );

        // Navegacion guiada: tres destinos claros para el operador.
        tarjetasGuia = new CardLayout();
        contenidoGuia = new JPanel(tarjetasGuia);
        contenidoGuia.setBackground(FONDO);
        contenidoGuia.add(crearVistaDeudas(), "DEUDAS");
        contenidoGuia.add(crearPanelTabla(tablaCompras), "COMPRAS");
        contenidoGuia.add(crearPanelDatosGenerales(), "DATOS");

        panelNavegacion = new JPanel(new BorderLayout(0, 10));
        panelNavegacion.setOpaque(false);
        JPanel barra = new JPanel(new java.awt.GridLayout(1, 3, 10, 0));
        barra.setOpaque(false);
        navDeudas = crearBoton("FACTURAS Y PAGOS", AZUL_OSCURO, 190);
        navCompras = crearBoton("+  HISTORIAL DE COMPRAS", AZUL, 190);
        navDatos = crearBoton("i  DATOS DEL PROVEEDOR", AZUL, 190);
        barra.add(navDeudas); barra.add(navCompras); barra.add(navDatos);
        panelNavegacion.add(barra, BorderLayout.NORTH);
        panelNavegacion.add(contenidoGuia, BorderLayout.CENTER);
        centro.add(panelNavegacion, BorderLayout.CENTER);
        navDeudas.addActionListener(e -> { cambiarSeccion("DEUDAS"); cambiarDetalleDeuda("FACTURAS"); });
        navCompras.addActionListener(e -> cambiarSeccion("COMPRAS"));
        navDatos.addActionListener(e -> cambiarSeccion("DATOS"));
        cambiarSeccion("DEUDAS");

        add(
                centro,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER
        //======================================================
        JPanel footer
                = new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                BorderFactory
                        .createCompoundBorder(
                                BorderFactory
                                        .createMatteBorder(
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

        JPanel acciones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        acciones.setOpaque(false);

        acciones.add(
                btnNuevaCompra
        );

        btnRegistrarPago.setText("PAGAR FACTURA SELECCIONADA");
        btnRegistrarPago.setPreferredSize(new Dimension(245, 36));
        acciones.add(
                btnRegistrarPago
        );

        footer.add(
                acciones,
                BorderLayout.WEST
        );

        JPanel cerrar
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        cerrar.setOpaque(false);

        cerrar.add(
                btnCerrar
        );

        footer.add(
                cerrar,
                BorderLayout.EAST
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    private void cambiarSeccion(String seccion) {
        tarjetasGuia.show(contenidoGuia, seccion);
        navDeudas.setBackground("DEUDAS".equals(seccion) ? AZUL_OSCURO : AZUL);
        navCompras.setBackground("COMPRAS".equals(seccion) ? AZUL_OSCURO : AZUL);
        navDatos.setBackground("DATOS".equals(seccion) ? AZUL_OSCURO : AZUL);
        navDeudas.setToolTipText("Consultar deuda, facturas y registrar pagos");
        navCompras.setToolTipText("Ver todas las compras del proveedor");
        navDatos.setToolTipText("Consultar informacion fiscal y de contacto");
    }

    private JPanel crearVistaDeudas() {
        JPanel vista = new JPanel(new BorderLayout(0, 10));
        vista.setBackground(Color.WHITE);
        vista.setBorder(new EmptyBorder(12, 14, 12, 14));
        JPanel superior = new JPanel(new BorderLayout(0, 8));
        superior.setOpaque(false);
        JLabel titulo = new JLabel("FACTURAS DEL PROVEEDOR  |  Seleccioná una factura para pagar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(AZUL_OSCURO);
        superior.add(titulo, BorderLayout.NORTH);
        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        opciones.setOpaque(false);
        subMovimientos = crearBoton("VER MOVIMIENTOS", AZUL, 170);
        subPagos = crearBoton("VER PAGOS REALIZADOS", AZUL, 205);
        opciones.add(subMovimientos); opciones.add(subPagos);
        JButton reporte = crearBoton("IMPRIMIR REPORTE", AZUL_OSCURO, 190);
        reporte.setToolTipText("Reporte de pagos por rango de fechas");
        reporte.addActionListener(e -> new DialogoReportePagosProveedor(this, idProveedor).setVisible(true));
        opciones.add(reporte);
        superior.add(opciones, BorderLayout.SOUTH);
        vista.add(superior, BorderLayout.NORTH);
        tarjetasDeudas = new CardLayout();
        contenidoDeudas = new JPanel(tarjetasDeudas);
        contenidoDeudas.add(crearPanelTabla(tablaFacturas), "FACTURAS");
        contenidoDeudas.add(crearPanelTabla(tablaCuentaCorriente), "MOVIMIENTOS");
        contenidoDeudas.add(crearPanelTabla(tablaPagos), "PAGOS");
        vista.add(contenidoDeudas, BorderLayout.CENTER);
        subMovimientos.addActionListener(e -> cambiarDetalleDeuda("MOVIMIENTOS"));
        subPagos.addActionListener(e -> cambiarDetalleDeuda("PAGOS"));
        cambiarDetalleDeuda("FACTURAS");
        ayudaPagoGuiado = new JLabel("PASO 1: Seleccioná una factura pendiente de la tabla.  PASO 2: Pulsá PAGAR FACTURA SELECCIONADA.");
        ayudaPagoGuiado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ayudaPagoGuiado.setForeground(AZUL_OSCURO);
        vista.add(ayudaPagoGuiado, BorderLayout.SOUTH);
        return vista;
    }

    private void cambiarDetalleDeuda(String seccion) {
        tarjetasDeudas.show(contenidoDeudas, seccion);
        subMovimientos.setBackground("MOVIMIENTOS".equals(seccion) ? AZUL_OSCURO : AZUL);
        subPagos.setBackground("PAGOS".equals(seccion) ? AZUL_OSCURO : AZUL);
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel
                = new JPanel(
                        new java.awt.GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        15,
                        0
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Total Comprado",
                        lblTotalComprado
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Total Pagado",
                        lblTotalPagado
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Saldo Pendiente",
                        lblSaldoPendiente
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Última Compra",
                        lblUltimaCompra
                )
        );

        return panel;
    }

    private JPanel crearTarjetaResumen(
            String titulo,
            JLabel valor) {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        Color acento = titulo.equals("Total Comprado") ? AZUL
                : titulo.equals("Total Pagado") ? VERDE
                : titulo.equals("Saldo Pendiente") ? ROJO : NARANJA;
        Color suave = titulo.equals("Total Comprado") ? new Color(235, 243, 255)
                : titulo.equals("Total Pagado") ? new Color(231, 248, 238)
                : titulo.equals("Saldo Pendiente") ? new Color(255, 237, 238)
                : new Color(255, 246, 229);
        panel.setBackground(suave);

        panel.setBorder(
                BorderFactory
                        .createCompoundBorder(
                                BorderFactory
                                        .createLineBorder(
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
                = new JLabel(titulo);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JPanel encabezado = new JPanel(new BorderLayout(8, 0));
        encabezado.setOpaque(false);
        JLabel icono = new JLabel(new IconoResumen(titulo, acento));
        encabezado.add(icono, BorderLayout.WEST);
        lblTitulo.setForeground(acento);
        encabezado.add(lblTitulo, BorderLayout.CENTER);
        panel.add(encabezado, BorderLayout.NORTH);

        panel.add(valor, BorderLayout.CENTER);

        return panel;
    }

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JPanel crearPanelDatosGenerales() {

        JPanel exterior
                = new JPanel(
                        new BorderLayout()
                );

        exterior.setBackground(
                Color.WHITE
        );

        exterior.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Datos del Proveedor"
                )
        );

        GridBagConstraints c
                = crearConstraints();

        agregarDato(
                panel,
                c,
                0,
                "Razón Social",
                lblRazonSocial
        );

        agregarDato(
                panel,
                c,
                1,
                "Nombre Comercial",
                lblNombreComercial
        );

        agregarDato(
                panel,
                c,
                2,
                "CUIT",
                lblCuit
        );

        agregarDato(
                panel,
                c,
                3,
                "Condición IVA",
                lblCondicionIva
        );

        agregarDato(
                panel,
                c,
                4,
                "Dirección",
                lblDireccion
        );

        agregarDato(
                panel,
                c,
                5,
                "Localidad",
                lblLocalidad
        );

        agregarDato(
                panel,
                c,
                6,
                "Provincia",
                lblProvincia
        );

        agregarDato(
                panel,
                c,
                7,
                "Teléfono",
                lblTelefono
        );

        agregarDato(
                panel,
                c,
                8,
                "Email",
                lblEmail
        );

        agregarDato(
                panel,
                c,
                9,
                "Contacto",
                lblContacto
        );

        agregarDato(
                panel,
                c,
                10,
                "Estado",
                lblEstado
        );

        exterior.add(
                panel,
                BorderLayout.NORTH
        );

        return exterior;
    }

    //==========================================================
    // AGREGAR DATO
    //==========================================================
    private void agregarDato(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            String titulo,
            JLabel valor) {

        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        c.fill
                = GridBagConstraints.NONE;

        panel.add(
                crearLabel(titulo),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(
                valor,
                c
        );
    }

    //==========================================================
    // PANEL TABLA
    //==========================================================
    private JPanel crearPanelTabla(
            JTable tabla) {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JScrollPane scroll
                = new JScrollPane(
                        tabla
                );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // CARGAR PROVEEDOR
    //==========================================================
    private void cargarDatosProveedor(
            String razonSocial,
            String cuit,
            String telefono,
            String estado) {

        lblRazonSocial.setText(
                razonSocial
        );

        lblNombreComercial.setText(
                razonSocial
        );

        lblCuit.setText(
                cuit
        );

        lblTelefono.setText(
                telefono
        );

        lblEstado.setText(
                estado
        );

        try (Connection c = conexion.getConexion();
             PreparedStatement p = c.prepareStatement("SELECT nombre_comercial,condicion_iva,direccion,localidad,provincia,email,persona_contacto FROM proveedor WHERE id_proveedor=?")) {
            p.setInt(1, idProveedor);
            try (ResultSet r=p.executeQuery()) {
                if(r.next()) {
                    lblNombreComercial.setText(nvl(r.getString(1)));
                    lblCondicionIva.setText(nvl(r.getString(2)));
                    lblDireccion.setText(nvl(r.getString(3)));
                    lblLocalidad.setText(nvl(r.getString(4)));
                    lblProvincia.setText(nvl(r.getString(5)));
                    lblEmail.setText(nvl(r.getString(6)));
                    lblContacto.setText(nvl(r.getString(7)));
                }
            }
        } catch(SQLException ex) { JOptionPane.showMessageDialog(this,"No se pudieron cargar los datos del proveedor: " + ex.getMessage()); }

    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private static String nvl(String s) { return s==null || s.isBlank() ? "—" : s; }
    private static String moneda(BigDecimal n) {
        return NumberFormat.getCurrencyInstance(new Locale("es","AR")).format(n==null?BigDecimal.ZERO:n);
    }
    private void cargarDatosReales() {
        idsFacturas.clear();
        modeloCompras.setRowCount(0);modeloFacturas.setRowCount(0);
        modeloCuenta.setRowCount(0);modeloPagos.setRowCount(0);
        try(Connection c=conexion.getConexion()) {
            BigDecimal comprado=BigDecimal.ZERO,pagado=BigDecimal.ZERO;
            String ultima="—";
            try(PreparedStatement p=c.prepareStatement(
                "SELECT c.id_compra,DATE_FORMAT(c.fecha,'%d/%m/%Y'),COALESCE(c.numero_comprobante,''),"+
                "(SELECT COUNT(*) FROM compra_detalle d WHERE d.id_compra=c.id_compra),c.total,c.estado " +
                "FROM compra c WHERE c.id_proveedor=? ORDER BY c.fecha DESC,c.id_compra DESC")) {
                p.setInt(1,idProveedor);
                try(ResultSet r=p.executeQuery()) { while(r.next()) {
                    long id=r.getLong(1);String fecha=r.getString(2),factura=r.getString(3),estado=r.getString(6);
                    BigDecimal total=r.getBigDecimal(5);
                    modeloCompras.addRow(new Object[]{id,fecha,factura,r.getInt(4),moneda(total),estado});
                    if(!"CONFIRMADA".equals(estado)) continue;
                    if("—".equals(ultima)) ultima=fecha;
                    comprado=comprado.add(total);
                    BigDecimal aplicado=BigDecimal.ZERO;
                    try(PreparedStatement pp=c.prepareStatement("SELECT COALESCE(SUM(monto),0) FROM compra_pago WHERE id_compra=?")) {
                        pp.setLong(1,id);try(ResultSet x=pp.executeQuery()){if(x.next()) aplicado=x.getBigDecimal(1);}
                    }
                    pagado=pagado.add(aplicado);
                    BigDecimal saldo=total.subtract(aplicado);
                    String estadoPago=saldo.signum()<=0?"PAGADA":aplicado.signum()==0?"PENDIENTE":"PARCIAL";
                    idsFacturas.add(id);
                    modeloFacturas.addRow(new Object[]{"Compra #"+id+" / "+factura,fecha,"—",moneda(total),moneda(aplicado),moneda(saldo),estadoPago});
                }}
            }
            try(PreparedStatement p=c.prepareStatement(
                "SELECT DATE_FORMAT(cp.fecha,'%d/%m/%Y'),cp.monto,mp.nombre,cp.referencia,cp.observaciones,cp.id_compra " +
                "FROM compra_pago cp JOIN compra c ON c.id_compra=cp.id_compra JOIN medio_pago mp ON mp.id_medio_pago=cp.id_medio_pago " +
                "WHERE c.id_proveedor=? ORDER BY cp.fecha DESC,cp.id_pago DESC")) {
                p.setInt(1,idProveedor);try(ResultSet r=p.executeQuery()) {while(r.next()) {
                    modeloPagos.addRow(new Object[]{r.getString(1),moneda(r.getBigDecimal(2)),r.getString(3),nvl(r.getString(4)),nvl(r.getString(5))});
                }}
            }
            // Extracto por compra/pago, ordenado cronológicamente con saldo acumulado.
            try(PreparedStatement p=c.prepareStatement(
                "SELECT fecha,clase,ref,debe,haber FROM ("+
                "SELECT c.fecha fecha,'Compra' clase,CONCAT('#',c.id_compra) ref,c.total debe,0 haber FROM compra c WHERE c.id_proveedor=? AND c.estado='CONFIRMADA' " +
                "UNION ALL SELECT cp.fecha,'Pago',CONCAT('Compra #',cp.id_compra),0,cp.monto FROM compra_pago cp JOIN compra c ON c.id_compra=cp.id_compra WHERE c.id_proveedor=?"+
                ") t ORDER BY fecha,CASE WHEN clase='Compra' THEN 0 ELSE 1 END")) {
                p.setInt(1,idProveedor);p.setInt(2,idProveedor);
                BigDecimal saldo=BigDecimal.ZERO;
                try(ResultSet r=p.executeQuery()){while(r.next()) {
                    BigDecimal debe=r.getBigDecimal(4),haber=r.getBigDecimal(5);
                    saldo=saldo.add(debe).subtract(haber);
                    modeloCuenta.addRow(new Object[]{new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getTimestamp(1)),r.getString(2),r.getString(3),moneda(debe),moneda(haber),moneda(saldo)});
                }}
            }
            lblTotalComprado.setText(moneda(comprado));lblTotalPagado.setText(moneda(pagado));
            lblSaldoPendiente.setText(moneda(comprado.subtract(pagado)));
            lblUltimaCompra.setText(ultima);
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this,"Error al consultar cuenta corriente real: " + ex.getMessage(),"Proveedores",JOptionPane.ERROR_MESSAGE);
        }
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnNuevaCompra
                .addActionListener(e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "Aquí podremos abrir directamente las opciones:\n"
                            + "Carga Manual / Código de Barras / Factura PDF.",
                            "Nueva Compra",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                });

        btnRegistrarPago.addActionListener(e -> pagarFacturaSeleccionada());

        btnCerrar.addActionListener(e -> {

            dispose();

        });
    }

    private void actualizarSeleccionFactura() {
        if (ayudaPagoGuiado == null || tablaFacturas == null) return;
        int fila = tablaFacturas.getSelectedRow();
        if (fila < 0) {
            ayudaPagoGuiado.setText("PASO 1: Seleccioná una factura PENDIENTE o PARCIAL.  PASO 2: Pulsá PAGAR FACTURA SELECCIONADA.");
            return;
        }
        int indice = tablaFacturas.convertRowIndexToModel(fila);
        if (indice < 0 || indice >= idsFacturas.size()) return;
        String estado = String.valueOf(modeloFacturas.getValueAt(indice, 6));
        if ("PAGADA".equals(estado)) {
            ayudaPagoGuiado.setText("Esta factura ya está pagada. Seleccioná otra con saldo pendiente.");
        } else {
            ayudaPagoGuiado.setText("FACTURA SELECCIONADA: Compra #" + idsFacturas.get(indice)
                    + "  |  Saldo: " + modeloFacturas.getValueAt(indice, 5)
                    + "  |  Pulsá PAGAR FACTURA SELECCIONADA.");
        }
    }

    private void pagarFacturaSeleccionada() {
        cambiarSeccion("DEUDAS");
        cambiarDetalleDeuda("FACTURAS");
        int fila = tablaFacturas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccioná una factura pendiente de la tabla.\nDespués pulsá PAGAR FACTURA SELECCIONADA.",
                    "Seleccionar factura", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int indice = tablaFacturas.convertRowIndexToModel(fila);
        if (indice < 0 || indice >= idsFacturas.size()) return;
        if ("PAGADA".equals(String.valueOf(modeloFacturas.getValueAt(indice, 6)))) {
            JOptionPane.showMessageDialog(this, "Esta factura ya está pagada. Seleccioná otra con saldo pendiente.",
                    "Factura sin deuda", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        long compraId = idsFacturas.get(indice);
        DialogoRegistrarPagoProveedor dialogo = new DialogoRegistrarPagoProveedor(
                this, idProveedor, nombreProveedorCabecera, 0.0, compraId);
        dialogo.setVisible(true);
        cargarDatosReales();
        cambiarDetalleDeuda("FACTURAS");
    }

    //==========================================================
    // VALOR
    //==========================================================
    private JLabel crearValor(
            String texto) {

        JLabel label
                = new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        label.setForeground(
                new Color(
                        40,
                        50,
                        65
                )
        );

        return label;
    }

    //==========================================================
    // VALOR RESUMEN
    //==========================================================
    private JLabel crearValorResumen(
            String texto,
            Color color) {

        JLabel label
                = new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    //==========================================================
    // BORDER
    //==========================================================
    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory
                .createTitledBorder(
                        BorderFactory
                                .createLineBorder(
                                        BORDE
                                ),
                        titulo,
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
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

        String icono = texto.toLowerCase().contains("compra") ? "plus" : texto.toLowerCase().contains("pago") ? "wallet" : "close";
        JButton boton = new BotonSerena(texto, color, icono);

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
