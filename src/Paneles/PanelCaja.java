/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Dialogos.DialogoAperturaCaja;
import Dialogos.DialogoArqueoCaja;
import Dialogos.DialogoCerrarCaja;
import Dialogos.DialogoEgresoCaja;
import Dialogos.DialogoIngresoCaja;
import services.CajaService;
import sesion.SesionUsuario;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import java.sql.SQLException;
import javax.swing.Icon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.geom.RoundRectangle2D;
import rojerusan.RSComboBox;

/**
 *
 * @author santi
 */
public class PanelCaja extends javax.swing.JPanel {

    private final CajaService cajaService = new CajaService();
    private CajaService.Resumen resumenActual;
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));

    //==========================================================
    // ESTADO DE CAJA
    //==========================================================
    private JLabel lblEstadoCaja;
    private JLabel lblCajero;
    private JLabel lblFechaApertura;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblSaldoInicial;
    private JLabel lblVentas;
    private JLabel lblIngresos;
    private JLabel lblEgresos;
    private JLabel lblSaldoActual;

    //==========================================================
    // FILTROS
    //==========================================================
    private RSComboBox comboTipoMovimiento;
    private RSComboBox comboFecha;
    private JButton btnBuscar;
    private JTextField txtBuscarMovimiento;
    private TableRowSorter<DefaultTableModel> filtroTabla;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnAbrirCaja;
    private JButton btnIngreso;
    private JButton btnEgreso;
    private JButton btnArqueo;
    private JButton btnCerrarCaja;

    //==========================================================
    // MEDIOS DE PAGO
    //==========================================================
    private JLabel lblEfectivo;
    private JLabel lblTarjeta;
    private JLabel lblTransferencia;
    private JLabel lblMercadoPago;

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

    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelCaja() {

        inicializarComponentes();

        construirPanel();

        refrescarCaja();

        configurarEventos();
        
  
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        lblEstadoCaja = crearValor("ABIERTA", VERDE);
        lblCajero = crearValor("Santiago", AZUL_OSCURO);
        lblFechaApertura = crearValor("26/08/2026 08:30", AZUL_OSCURO);

        lblSaldoInicial
                = crearValorResumen(
                        "$ 50.000",
                        AZUL
                );

        lblVentas
                = crearValorResumen(
                        "$ 185.400",
                        VERDE
                );

        lblIngresos
                = crearValorResumen(
                        "$ 20.000",
                        VERDE
                );

        lblEgresos
                = crearValorResumen(
                        "$ 15.500",
                        ROJO
                );

        lblSaldoActual
                = crearValorResumen(
                        "$ 239.900",
                        AZUL_OSCURO
                );

        comboTipoMovimiento
                = new RSComboBox();

        comboTipoMovimiento.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Todos",
                            "Venta",
                            "Ingreso",
                            "Egreso",
                            "Pago Proveedor",
                            "Retiro"
                        }
                )
        );

        comboFecha
                = new RSComboBox();

        comboFecha.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Hoy",
                            "Ayer",
                            "Últimos 7 días",
                            "Este mes"
                        }
                )
        );

        btnBuscar
                = crearBotonSimple(
                        "Buscar",
                        AZUL,
                        100
                );

        btnAbrirCaja
                = crearBotonAccion(
                        "ABRIR CAJA",
                        VERDE
                );

        btnIngreso
                = crearBotonAccion(
                        "INGRESO",
                        AZUL
                );

        btnEgreso
                = crearBotonAccion(
                        "EGRESO",
                        NARANJA
                );

        btnArqueo
                = crearBotonAccion(
                        "ARQUEO",
                        AZUL_OSCURO
                );

        btnCerrarCaja
                = crearBotonAccion(
                        "CERRAR CAJA",
                        ROJO
                );

        lblEfectivo
                = crearValorResumen(
                        "$ 98.000",
                        VERDE
                );

        lblTarjeta
                = crearValorResumen(
                        "$ 64.500",
                        AZUL
                );

        lblTransferencia
                = crearValorResumen(
                        "$ 22.900",
                        AZUL_OSCURO
                );

        lblMercadoPago
                = crearValorResumen(
                        "$ 0",
                        GRIS
                );

        inicializarTabla();
        txtBuscarMovimiento = new JTextField();
        txtBuscarMovimiento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscarMovimiento.setToolTipText("Buscar por fecha, tipo, descripción, medio de pago o usuario");
        txtBuscarMovimiento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE), new EmptyBorder(5, 10, 5, 10)));
    }

    //==========================================================
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "Fecha",
                            "Hora",
                            "Tipo",
                            "Descripción",
                            "Medio de Pago",
                            "Ingreso",
                            "Egreso",
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

        tablaMovimientos
                = new JTable(
                        modeloTabla
                );

        tablaMovimientos.setRowHeight(
                30
        );

        tablaMovimientos.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaMovimientos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMovimientos.setBackground(
                Color.WHITE
        );

        tablaMovimientos.setForeground(
                new Color(
                        35,
                        35,
                        35
                )
        );

        tablaMovimientos.setGridColor(
                new Color(
                        225,
                        230,
                        238
                )
        );

        tablaMovimientos.setShowHorizontalLines(true);
        tablaMovimientos.setShowVerticalLines(false);

        tablaMovimientos.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaMovimientos.setSelectionForeground(
                Color.BLACK
        );

        tablaMovimientos.setFillsViewportHeight(
                true
        );

        tablaMovimientos
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaMovimientos
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        aplicarHeaderAzul();
        filtroTabla = new TableRowSorter<>(modeloTabla);
        tablaMovimientos.setRowSorter(filtroTabla);

        tablaMovimientos
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(90);

        tablaMovimientos
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(70);

        tablaMovimientos
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(110);

        tablaMovimientos
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(260);

        tablaMovimientos
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        tablaMovimientos
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        tablaMovimientos
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(110);

        tablaMovimientos
                .getColumnModel()
                .getColumn(7)
                .setPreferredWidth(110);
    }

    //==========================================================
    // HEADER AZUL
    //==========================================================
    private void aplicarHeaderAzul() {

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
                                13
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
                i < tablaMovimientos.getColumnCount();
                i++) {

            tablaMovimientos
                    .getColumnModel()
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
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(FONDO);
        contenedor.setBorder(new EmptyBorder(12, 18, 12, 18));

        JPanel header = crearHeader();
        agregarBloque(contenedor, header, 55, 8);

        JPanel bloquePrincipal = new JPanel(new GridLayout(1, 2, 12, 0));
        bloquePrincipal.setOpaque(false);
        bloquePrincipal.add(crearTarjetaSaldoPrincipal());
        bloquePrincipal.add(crearPanelEstadoCaja());
        agregarBloque(contenedor, bloquePrincipal, 108, 10);

        agregarBloque(contenedor, crearPanelResumen(), 75, 10);
        agregarBloque(contenedor, crearPanelFiltros(), 64, 10);
        agregarBloque(contenedor, crearPanelMovimientos(), 250, 10);

        JLabel subtituloMedios = new JLabel("RESUMEN POR MEDIO DE PAGO");
        subtituloMedios.setForeground(AZUL_OSCURO);
        subtituloMedios.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtituloMedios.setAlignmentX(LEFT_ALIGNMENT);
        contenedor.add(subtituloMedios);
        contenedor.add(Box.createVerticalStrut(6));
        agregarBloque(contenedor, crearPanelMediosPago(), 70, 0);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE),
            new EmptyBorder(5, 14, 5, 14)));
        barra.add(crearPanelAcciones(), BorderLayout.CENTER);
        add(barra, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(FONDO);
        add(scroll, BorderLayout.CENTER);
    }

    private void agregarBloque(JPanel contenedor, JPanel bloque, int alto, int espacio) {
        bloque.setAlignmentX(LEFT_ALIGNMENT);
        bloque.setPreferredSize(new Dimension(1000, alto));
        bloque.setMinimumSize(new Dimension(0, alto));
        bloque.setMaximumSize(new Dimension(Integer.MAX_VALUE, alto));
        contenedor.add(bloque);
        if (espacio > 0) contenedor.add(Box.createVerticalStrut(espacio));
    }

    private JPanel crearTarjetaSaldoPrincipal() {
        JPanel p = new JPanel(new BorderLayout(8, 6));
        p.setBackground(AZUL_OSCURO);
        p.setBorder(new EmptyBorder(13, 20, 13, 20));
        JLabel titulo = new JLabel("SALDO DISPONIBLE EN CAJA");
        titulo.setForeground(new Color(202, 222, 255));
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(titulo, BorderLayout.NORTH);
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setFont(new Font("Segoe UI", Font.BOLD, 29));
        p.add(lblSaldoActual, BorderLayout.CENTER);
        JLabel nota = new JLabel("Efectivo esperado según movimientos registrados");
        nota.setForeground(new Color(202, 222, 255));
        nota.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        p.add(nota, BorderLayout.SOUTH);
        return p;
    }

    //==========================================================
    // HEADER
    //==========================================================
    private JPanel crearHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel titulo = new JLabel("Caja y movimientos");
        titulo.setForeground(AZUL_OSCURO);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        JLabel detalle = new JLabel("Control financiero  |  Aperturas, movimientos, arqueos y cierres");
        detalle.setForeground(TEXTO_SECUNDARIO);
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(titulo, BorderLayout.NORTH);
        p.add(detalle, BorderLayout.SOUTH);
        return p;
    }

    //==========================================================
    // ESTADO CAJA
    //==========================================================
    private JPanel crearPanelEstadoCaja() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                3,
                                1,
                                0,
                                5
                        )
                );

        panel.setOpaque(
                false
        );

        panel.add(
                crearTarjetaDato(
                        "Estado de Caja",
                        lblEstadoCaja
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Cajero",
                        lblCajero
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Apertura",
                        lblFechaApertura
                )
        );

        return panel;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                4,
                                10,
                                0
                        )
                );

        panel.setOpaque(
                false
        );

        panel.add(
                crearTarjetaDato(
                        "Saldo Inicial",
                        lblSaldoInicial
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Ventas",
                        lblVentas
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Ingresos",
                        lblIngresos
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Egresos",
                        lblEgresos
                )
        );

        // El saldo actual se destaca en la tarjeta principal superior.

        return panel;
    }

    //==========================================================
    // FILTROS
    //==========================================================
    private JPanel crearPanelFiltros() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE), new EmptyBorder(10, 12, 10, 12)));
        JPanel izquierda = new JPanel(new BorderLayout(7, 0));
        izquierda.setOpaque(false);
        JLabel lupa = new JLabel("BUSCAR");
        lupa.setForeground(AZUL_OSCURO);
        lupa.setFont(new Font("Segoe UI", Font.BOLD, 11));
        izquierda.add(lupa, BorderLayout.WEST);
        izquierda.add(txtBuscarMovimiento, BorderLayout.CENTER);
        p.add(izquierda, BorderLayout.CENTER);
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        derecha.setOpaque(false);
        derecha.add(crearLabel("Tipo"));
        comboTipoMovimiento.setPreferredSize(new Dimension(145, 35));
        derecha.add(comboTipoMovimiento);
        derecha.add(crearLabel("Período"));
        comboFecha.setPreferredSize(new Dimension(145, 35));
        derecha.add(comboFecha);
        btnBuscar.setPreferredSize(new Dimension(92, 35));
        derecha.add(btnBuscar);
        p.add(derecha, BorderLayout.EAST);
        return p;
    }

    //==========================================================
    // MOVIMIENTOS
    //==========================================================
    private JPanel crearPanelMovimientos() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(BorderFactory.createLineBorder(BORDE));
        panel.add(crearEncabezadoSeccion("MOVIMIENTOS DE CAJA"), BorderLayout.NORTH);

        JScrollPane scroll
                = new JScrollPane(
                        tablaMovimientos
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
    // MEDIOS DE PAGO
    //==========================================================
    private JPanel crearPanelMediosPago() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                0
                        )
                );

        panel.setOpaque(
                false
        );

        panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        panel.add(
                crearTarjetaDato(
                        "Efectivo",
                        lblEfectivo
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Tarjeta",
                        lblTarjeta
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Transferencia",
                        lblTransferencia
                )
        );

        panel.add(
                crearTarjetaDato(
                        "Mercado Pago",
                        lblMercadoPago
                )
        );

        return panel;
    }

    //==========================================================
    // ACCIONES
    //==========================================================
    private JPanel crearPanelAcciones() {

        JPanel panel
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                8
                        )
                );

        panel.setOpaque(
                false
        );

        panel.add(
                btnAbrirCaja
        );

        panel.add(
                btnIngreso
        );

        panel.add(
                btnEgreso
        );

        panel.add(
                btnArqueo
        );

        panel.add(
                btnCerrarCaja
        );
        if (SesionUsuario.esAdministrador()) {
            JButton auditoria = crearBotonAccion("HISTORIAL", AZUL_OSCURO);
            auditoria.addActionListener(e -> {
                try { new Dialogos.DialogoAuditoriaCajas(SwingUtilities.getWindowAncestor(this)).setVisible(true); }
                catch(Exception ex) { error(ex); }
            });
            panel.add(auditoria);
        }

        return panel;
    }

    //==========================================================
    // TARJETA
    //==========================================================
    private JPanel crearTarjetaDato(String titulo, JLabel valor) {
        Color fondoTarjeta = new Color(238, 244, 255);
        Color bordeTarjeta = new Color(206, 221, 246);
        Color acento = AZUL;
        if (titulo.equals("Ventas") || titulo.equals("Ingresos") || titulo.equals("Efectivo")) {
            fondoTarjeta = new Color(235, 250, 243);
            bordeTarjeta = new Color(192, 230, 211);
            acento = VERDE;
        } else if (titulo.equals("Egresos")) {
            fondoTarjeta = new Color(255, 241, 239);
            bordeTarjeta = new Color(244, 210, 205);
            acento = ROJO;
        } else if (titulo.equals("Saldo Actual") || titulo.equals("Estado de Caja")) {
            fondoTarjeta = new Color(227, 238, 255);
            bordeTarjeta = new Color(177, 202, 241);
            acento = AZUL_OSCURO;
        } else if (titulo.equals("Apertura") || titulo.equals("Transferencia")) {
            fondoTarjeta = new Color(245, 241, 255);
            bordeTarjeta = new Color(222, 210, 247);
            acento = new Color(106, 75, 160);
        }
        final Color bg = fondoTarjeta, edge = bordeTarjeta, line = acento;
        JPanel panel = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(1, 1, getWidth()-3, getHeight()-3, 16, 16);
                g2.setColor(edge);
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 16, 16);
                g2.setColor(line);
                g2.fillRoundRect(1, 9, 4, Math.max(12, getHeight()-18), 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        boolean estadoMini = titulo.equals("Estado de Caja") || titulo.equals("Cajero") || titulo.equals("Apertura");
        panel.setBorder(new EmptyBorder(estadoMini ? 3 : 8, 13, estadoMini ? 3 : 7, 10));
        JLabel lblTitulo = new JLabel(titulo.toUpperCase(java.util.Locale.ROOT));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(new Color(73, 91, 119));
        if (estadoMini) {
            panel.setLayout(new BorderLayout(8, 0));
            lblTitulo.setPreferredSize(new Dimension(112, 20));
            valor.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panel.add(lblTitulo, BorderLayout.WEST);
            panel.add(valor, BorderLayout.CENTER);
        } else {
            valor.setFont(new Font("Segoe UI", Font.BOLD, 18));
            panel.add(lblTitulo, BorderLayout.NORTH);
            panel.add(valor, BorderLayout.CENTER);
        }
        return panel;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private String dinero(BigDecimal n) { return moneda.format(n == null ? BigDecimal.ZERO : n); }

    private void refrescarCaja() {
        try {
            resumenActual = cajaService.obtenerResumen();
            CajaService.Resumen r = resumenActual;
            boolean abierta = r.idSesion > 0;
            lblEstadoCaja.setText(abierta ? "ABIERTA  #" + r.idSesion : "SIN APERTURA");
            lblEstadoCaja.setForeground(abierta ? VERDE : ROJO);
            lblCajero.setText(abierta ? r.cajero : "Sin sesión abierta");
            lblFechaApertura.setText(abierta ? r.apertura : "—");
            lblSaldoInicial.setText(dinero(r.inicial));
            lblVentas.setText(dinero(r.ventas));
            lblIngresos.setText(dinero(r.ingresos));
            lblEgresos.setText(dinero(r.egresos));
            lblSaldoActual.setText(dinero(r.efectivoEsperado));
            lblEfectivo.setText(dinero(r.efectivoEsperado));
            lblTarjeta.setText(dinero(r.tarjeta));
            lblTransferencia.setText(dinero(r.transferencia));
            lblMercadoPago.setText(dinero(r.mercadoPago));
            btnAbrirCaja.setEnabled(!abierta);
            btnIngreso.setEnabled(abierta);
            btnEgreso.setEnabled(abierta);
            btnArqueo.setEnabled(abierta);
            btnCerrarCaja.setEnabled(abierta);
            modeloTabla.setRowCount(0);
            if (abierta) {
                for (CajaService.Movimiento m : cajaService.movimientos(r.idSesion,
                        String.valueOf(comboTipoMovimiento.getSelectedItem()),
                        String.valueOf(comboFecha.getSelectedItem()))) {
                    modeloTabla.addRow(new Object[]{m.fecha, m.hora, m.tipo, m.descripcion,
                        m.medio, m.ingreso.signum() == 0 ? "" : dinero(m.ingreso),
                        m.egreso.signum() == 0 ? "" : dinero(m.egreso), m.usuario});
                }
            }
            aplicarBusquedaLocal();
        } catch (SQLException ex) {
            lblEstadoCaja.setText("ERROR DE CONEXIÓN / ESQUEMA");
            lblCajero.setText("Revisar configuración SQL");
            modeloTabla.setRowCount(0);
            btnAbrirCaja.setEnabled(false);
            btnIngreso.setEnabled(false);
            btnEgreso.setEnabled(false);
            btnArqueo.setEnabled(false);
            btnCerrarCaja.setEnabled(false);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Caja - MariaDB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarBusquedaLocal() {
        String q = txtBuscarMovimiento.getText().trim();
        if (q.isEmpty()) filtroTabla.setRowFilter(null);
        else filtroTabla.setRowFilter(RowFilter.regexFilter("(?iu)" + java.util.regex.Pattern.quote(q)));
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Caja - Operación no realizada", JOptionPane.ERROR_MESSAGE);
    }

    private int usuarioId() {
        int id = SesionUsuario.getIdUsuario();
        if (id <= 0) throw new IllegalStateException("Iniciá sesión para operar la caja.");
        return id;
    }

    //==========================================================
    // EVENTOS TEMPORALES
    //==========================================================
    private void configurarEventos() {
        btnBuscar.addActionListener(e -> refrescarCaja());
        comboTipoMovimiento.addActionListener(e -> refrescarCaja());
        comboFecha.addActionListener(e -> refrescarCaja());
        txtBuscarMovimiento.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarBusquedaLocal(); }
            public void removeUpdate(DocumentEvent e) { aplicarBusquedaLocal(); }
            public void changedUpdate(DocumentEvent e) { aplicarBusquedaLocal(); }
        });
        btnAbrirCaja.addActionListener(e -> {
            try {
                Window w = SwingUtilities.getWindowAncestor(this);
                DialogoAperturaCaja d = new DialogoAperturaCaja(w, SesionUsuario.getNombreUsuario());
                d.setVisible(true);
                if (d.isConfirmado()) {
                    cajaService.abrir(usuarioId(), BigDecimal.valueOf(d.getSaldoInicial()), d.getObservaciones());
                    refrescarCaja();
                }
            } catch (Exception ex) { error(ex); }
        });
        btnIngreso.addActionListener(e -> registrarMovimiento(true));
        btnEgreso.addActionListener(e -> registrarMovimiento(false));
        btnArqueo.addActionListener(e -> {
            try {
                if (resumenActual == null || resumenActual.idSesion <= 0) return;
                CajaService.Resumen r = resumenActual;
                DialogoArqueoCaja d = new DialogoArqueoCaja(SwingUtilities.getWindowAncestor(this),
                    SesionUsuario.getNombreUsuario(), r.efectivoEsperado.doubleValue(),
                    r.tarjeta.doubleValue(), r.transferencia.doubleValue(), r.mercadoPago.doubleValue());
                d.setVisible(true);
                if (d.isConfirmado()) {
                    cajaService.arquear(r.idSesion, usuarioId(), BigDecimal.valueOf(d.getEfectivoContado()), d.getObservaciones());
                    refrescarCaja();
                }
            } catch (Exception ex) { error(ex); }
        });
        btnCerrarCaja.addActionListener(e -> {
            try {
                if (resumenActual == null || resumenActual.idSesion <= 0) return;
                CajaService.Resumen r = resumenActual;
                DialogoCerrarCaja d = new DialogoCerrarCaja(SwingUtilities.getWindowAncestor(this),
                    SesionUsuario.getNombreUsuario(), r.inicial.doubleValue(), r.ventasEfectivo.doubleValue(),
                    r.ingresosEfectivo.doubleValue(), r.egresosEfectivo.doubleValue(),
                    r.tarjeta.doubleValue(), r.transferencia.doubleValue(), r.mercadoPago.doubleValue());
                d.setVisible(true);
                if (d.isConfirmado()) {
                    cajaService.cerrar(r.idSesion, usuarioId(), BigDecimal.valueOf(d.getEfectivoContado()), d.getObservaciones());
                    refrescarCaja();
                }
            } catch (Exception ex) { error(ex); }
        });
    }

    private void registrarMovimiento(boolean ingreso) {
        try {
            if (resumenActual == null || resumenActual.idSesion <= 0) return;
            Window w = SwingUtilities.getWindowAncestor(this);
            if (ingreso) {
                DialogoIngresoCaja d = new DialogoIngresoCaja(w, SesionUsuario.getNombreUsuario());
                d.setVisible(true);
                if (d.isConfirmado()) {
                    cajaService.registrar(resumenActual.idSesion, usuarioId(), true,
                        BigDecimal.valueOf(d.getMonto()), d.getMedioPago(), d.getMotivo(), d.getObservaciones());
                    refrescarCaja();
                }
            } else {
                DialogoEgresoCaja d = new DialogoEgresoCaja(w, SesionUsuario.getNombreUsuario());
                d.setVisible(true);
                if (d.isConfirmado()) {
                    cajaService.registrar(resumenActual.idSesion, usuarioId(), false,
                        BigDecimal.valueOf(d.getMonto()), d.getMedioPago(), d.getMotivo(), d.getObservaciones());
                    refrescarCaja();
                }
            }
        } catch (Exception ex) { error(ex); }
    }

    //==========================================================
    // VALORES
    //==========================================================
    private JLabel crearValor(
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
                        16
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    private JLabel crearValorResumen(
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
                        19
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label
                = new JLabel(
                        texto
                );

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
    // BOTONES
    //==========================================================
    private JButton crearBotonSimple(
            String texto,
            Color color,
            int ancho) {

        JButton boton
                = new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        34
                )
        );

        boton.setBackground(
                color
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFocusPainted(
                false
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

    /** Botón dibujado directamente: evita que el Look&Feel o RSButtonRound
     * pinte texto gris/blanco invisible al deshabilitar acciones. */
    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    Color fondo = isEnabled() ? color : new Color(125, 140, 161);
                    if (getModel().isPressed() && isEnabled()) fondo = fondo.darker();
                    else if (getModel().isRollover() && isEnabled()) fondo = fondo.brighter();
                    g2.setColor(fondo);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 13, 13));
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    java.awt.FontMetrics fm = g2.getFontMetrics();
                    int textoAncho = fm.stringWidth(getText());
                    int iconoAncho = 19, separacion = 9;
                    int total = iconoAncho + separacion + textoAncho;
                    int inicio = Math.max(8, (getWidth()-total)/2);
                    new IconoCaja(getText()).paintIcon(this, g2, inicio,
                            (getHeight()-19)/2);
                    g2.drawString(getText(), inicio+iconoAncho+separacion,
                            (getHeight()-fm.getHeight())/2+fm.getAscent());
                } finally { g2.dispose(); }
            }
        };
        boton.setPreferredSize(new Dimension(142, 42));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setRolloverEnabled(true);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boton.getAccessibleContext().setAccessibleName(texto);
        return boton;
    }

    /** Iconos vectoriales, sin depender de emojis ni fuentes externas. */
    private static final class IconoCaja implements Icon {
        private final String tipo;
        IconoCaja(String tipo) { this.tipo=tipo; }
        @Override public int getIconWidth() { return 19; }
        @Override public int getIconHeight() { return 19; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D a=(Graphics2D)g.create();
            try {
                a.translate(x,y);a.setColor(Color.WHITE);
                a.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if(tipo.contains("ABRIR")) {
                    a.drawRoundRect(2,6,15,11,3,3);a.drawLine(5,6,5,3);a.drawLine(5,3,14,3);
                    a.drawLine(9,9,9,14);a.drawLine(7,11,11,11);
                } else if(tipo.contains("CERRAR")) {
                    a.drawRoundRect(2,7,15,10,3,3);a.drawRoundRect(5,2,9,8,3,3);
                    a.drawLine(8,12,11,12);
                } else if(tipo.contains("ARQUEO")) {
                    a.drawRoundRect(3,2,13,16,3,3);a.drawLine(6,6,13,6);
                    a.drawLine(6,10,8,12);a.drawLine(8,12,13,8);
                } else {
                    a.drawOval(4,4,11,11);a.drawLine(9,7,9,12);
                    if(tipo.contains("INGRESO")) {a.drawLine(6,10,9,7);a.drawLine(9,7,12,10);}
                    else {a.drawLine(6,10,9,13);a.drawLine(9,13,12,10);}
                }
            } finally { a.dispose(); }
        }
    }

    //==========================================================
    // BORDE
    //==========================================================
    private JPanel crearEncabezadoSeccion(String titulo) {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(new Color(28, 68, 133));
        cabecera.setBorder(new EmptyBorder(9, 13, 9, 13));
        JLabel texto = new JLabel(titulo);
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cabecera.add(texto, BorderLayout.WEST);
        return cabecera;
    }

    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory.createTitledBorder(
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
        );
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
