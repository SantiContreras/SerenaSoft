/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Diseños.EstiloBotones;
import Configuracion.conexion;
import services.TicketVentaJasper;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.JDialog;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author santi
 */
public class PanelHistorialVentas extends javax.swing.JPanel {

   //==========================================================
    // COLORES SERENA SOFT
    //==========================================================

    private final Color AZUL_OSCURO = new Color(15, 55, 120);
    private final Color AZUL = new Color(30, 90, 180);
    private final Color AZUL_CLARO = new Color(235, 242, 252);

    private final Color VERDE = new Color(20, 145, 85);
    private final Color ROJO = new Color(200, 55, 55);
    private final Color GRIS = new Color(105, 115, 130);

    private final Color FONDO = new Color(235, 240, 247);
    private final Color BORDE = new Color(205, 215, 228);

    //==========================================================
    // FILTROS
    //==========================================================

    private JTextField txtDesde;
    private JTextField txtHasta;
    private JTextField txtNumeroVenta;
    private JTextField txtCliente;

    private JComboBox<String> comboPago;
    private JComboBox<String> comboEstado;

    private JButton btnBuscar;
    private JButton btnLimpiar;

    //==========================================================
    // TARJETAS
    //==========================================================

    private JLabel lblCantidadVentas;
    private JLabel lblTotalVendido;
    private JLabel lblTicketPromedio;
    private JLabel lblAnuladas;

    //==========================================================
    // TABLA
    //==========================================================

    private final List<Long> idsVentas = new ArrayList<>();
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // ACCIONES
    //==========================================================

    private JButton btnVerDetalle;
    private JButton btnReimprimir;
    private JButton btnAnular;
    private JButton btnRevisar;
    private JButton btnExportar;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================

    public PanelHistorialVentas() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        construirInterfaz();

        txtDesde.setText(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtHasta.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        comboPago.removeAllItems();
        comboPago.addItem("Todos");
        cargarMediosPago();
        comboEstado.removeAllItems();
        for (String e : new String[]{"Todos", "FINALIZADA", "PENDIENTE", "ANULADA", "DEVUELTA_PARCIAL", "DEVUELTA_TOTAL"}) comboEstado.addItem(e);
        btnBuscar.addActionListener(e -> cargarVentas());
        btnLimpiar.addActionListener(e -> {
            txtDesde.setText(""); txtHasta.setText(""); txtNumeroVenta.setText(""); txtCliente.setText("");
            comboPago.setSelectedIndex(0); comboEstado.setSelectedIndex(0); cargarVentas();
        });
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnReimprimir.addActionListener(e -> reimprimir());
        btnExportar.addActionListener(e -> exportarExcel());
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { if (e.getClickCount()==2) verDetalle(); }
        });
        btnAnular.addActionListener(e -> anularVenta());
        btnRevisar.addActionListener(e -> revisarMercaderia());
        cargarVentas();
        
        
        
         EstiloBotones.corregirBotones(this);
    }

    //==========================================================
    // INTERFAZ GENERAL
    //==========================================================

    private void construirInterfaz() {

        JPanel contenido = new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setBackground(FONDO);

        contenido.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        20,
                        22
                )
        );

        contenido.add(crearTitulo());

        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(crearPanelFiltros());

        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(crearPanelResumen());

        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(crearPanelTabla());

        JPanel accionesFijas = crearBarraAcciones();
        add(accionesFijas, BorderLayout.SOUTH);

        JScrollPane scrollGeneral =
                new JScrollPane(contenido);

        scrollGeneral.setBorder(null);

        scrollGeneral.getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollGeneral.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollGeneral.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        add(
                scrollGeneral,
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // TITULO
    //==========================================================

    private JPanel crearTitulo() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(false);

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel titulo =
                new JLabel(
                        "HISTORIAL DE VENTAS"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        JLabel subtitulo =
                new JLabel(
                        "Consulte, controle y gestione las ventas realizadas"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                GRIS
        );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(3)
        );

        panel.add(subtitulo);

        return panel;
    }

    //==========================================================
    // FILTROS
    //==========================================================

    private JPanel crearPanelFiltros() {

        JPanel contenedor =
                crearPanelBlanco();

        contenedor.setLayout(
                new BorderLayout()
        );

        contenedor.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        155
                )
        );

        JLabel titulo =
                crearTituloSeccion(
                        "FILTROS DE BÚSQUEDA"
                );

        titulo.setBorder(
                new EmptyBorder(
                        12,
                        15,
                        5,
                        15
                )
        );

        contenedor.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel filtros =
                new JPanel(
                        new GridBagLayout()
                );

        filtros.setOpaque(false);

        filtros.setBorder(
                new EmptyBorder(
                        5,
                        15,
                        15,
                        15
                )
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        5,
                        6,
                        5,
                        6
                );

        c.anchor =
                GridBagConstraints.WEST;

        //======================================================
        // COMPONENTES
        //======================================================

        txtDesde =
                crearCampo(
                        "28/08/2026"
                );

        txtHasta =
                crearCampo(
                        "28/08/2026"
                );

        txtNumeroVenta =
                crearCampo("");

        txtCliente =
                crearCampo("");

        comboPago =
                crearCombo(
                        new String[]{
                            "Todos",
                            "Efectivo",
                            "Tarjeta",
                            "Transferencia",
                            "Cuenta Corriente"
                        }
                );

        comboEstado =
                crearCombo(
                        new String[]{
                            "Todos",
                            "Realizada",
                            "Anulada",
                            "Devuelta"
                        }
                );

        //======================================================
        // FILA 1
        //======================================================

        c.gridy = 0;

        c.gridx = 0;
        filtros.add(
                crearLabel("Desde"),
                c
        );

        c.gridx = 1;
        filtros.add(
                txtDesde,
                c
        );

        c.gridx = 2;
        filtros.add(
                crearLabel("Hasta"),
                c
        );

        c.gridx = 3;
        filtros.add(
                txtHasta,
                c
        );

        c.gridx = 4;
        filtros.add(
                crearLabel("Venta N°"),
                c
        );

        c.gridx = 5;
        filtros.add(
                txtNumeroVenta,
                c
        );

        //======================================================
        // FILA 2
        //======================================================

        c.gridy = 1;

        c.gridx = 0;
        filtros.add(
                crearLabel("Cliente"),
                c
        );

        c.gridx = 1;
        filtros.add(
                txtCliente,
                c
        );

        c.gridx = 2;
        filtros.add(
                crearLabel("Forma de pago"),
                c
        );

        c.gridx = 3;
        filtros.add(
                comboPago,
                c
        );

        c.gridx = 4;
        filtros.add(
                crearLabel("Estado"),
                c
        );

        c.gridx = 5;
        filtros.add(
                comboEstado,
                c
        );

        //======================================================
        // BOTONES
        //======================================================

        JPanel acciones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        acciones.setOpaque(false);

        btnLimpiar =
                crearBoton(
                        "LIMPIAR",
                        GRIS,
                        110
                );

        btnBuscar =
                crearBoton(
                        "BUSCAR",
                        AZUL,
                        120
                );

        acciones.add(btnLimpiar);
        acciones.add(btnBuscar);

        c.gridx = 6;
        c.gridy = 0;
        c.gridheight = 2;

        c.fill =
                GridBagConstraints.VERTICAL;

        filtros.add(
                acciones,
                c
        );

        contenedor.add(
                filtros,
                BorderLayout.CENTER
        );

        return contenedor;
    }

    //==========================================================
    // RESUMEN
    //==========================================================

    private JPanel crearPanelResumen() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                14,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        105
                )
        );

        lblCantidadVentas =
                new JLabel("42");

        lblTotalVendido =
                new JLabel("$ 485.200");

        lblTicketPromedio =
                new JLabel("$ 11.552");

        lblAnuladas =
                new JLabel("2");

        panel.add(
                crearTarjeta(
                        "VENTAS",
                        lblCantidadVentas,
                        "Operaciones encontradas",
                        AZUL_OSCURO
                )
        );

        panel.add(
                crearTarjeta(
                        "TOTAL VENDIDO",
                        lblTotalVendido,
                        "Importe del período",
                        VERDE
                )
        );

        panel.add(
                crearTarjeta(
                        "TICKET PROMEDIO",
                        lblTicketPromedio,
                        "Promedio por venta",
                        AZUL
                )
        );

        panel.add(
                crearTarjeta(
                        "ANULADAS",
                        lblAnuladas,
                        "Ventas anuladas",
                        ROJO
                )
        );

        return panel;
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel valor,
            String descripcion,
            Color colorValor) {

        JPanel tarjeta =
                new JPanel();

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta,
                        BoxLayout.Y_AXIS
                )
        );

        tarjeta.setBackground(
                Color.WHITE
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                10,
                                15
                        )
                )
        );

        JLabel lblTitulo =
                new JLabel(titulo);

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

        valor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        valor.setForeground(
                colorValor
        );

        JLabel lblDescripcion =
                new JLabel(descripcion);

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblDescripcion.setForeground(
                GRIS
        );

        tarjeta.add(lblTitulo);

        tarjeta.add(
                Box.createVerticalStrut(5)
        );

        tarjeta.add(valor);

        tarjeta.add(
                Box.createVerticalStrut(3)
        );

        tarjeta.add(lblDescripcion);

        return tarjeta;
    }

    //==========================================================
    // PANEL TABLA
    //==========================================================

    private JPanel crearPanelTabla() {

        JPanel panel =
                crearPanelBlanco();

        panel.setLayout(
                new BorderLayout(
                        0,
                        8
                )
        );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.setPreferredSize(
                new Dimension(
                        1100,
                        380
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        430
                )
        );

        JLabel titulo =
                crearTituloSeccion(
                        "LISTADO DE VENTAS"
                );

        titulo.setBorder(
                new EmptyBorder(
                        10,
                        14,
                        3,
                        14
                )
        );

        panel.add(
                titulo,
                BorderLayout.NORTH
        );

        inicializarTabla();

        JScrollPane scroll =
                new JScrollPane(
                        tablaVentas
                );

        scroll.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        0,
                        10
                )
        );

        scroll.setPreferredSize(
                new Dimension(
                        0,
                        250
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    /** Barra fija al pie: no es necesario desplazarse para usar los botones. */
    private JPanel crearBarraAcciones() {
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 9));
        botones.setBackground(Color.WHITE);
        botones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE));
        btnVerDetalle = crearBoton("VER DETALLE", AZUL_OSCURO, 130);
        btnReimprimir = crearBoton("REIMPRIMIR TICKET", AZUL, 165);
        btnAnular = crearBoton("ANULAR / DEVOLVER", ROJO, 160);
        btnRevisar = crearBoton("REVISAR MERCADERÍA", GRIS, 180);
        btnExportar = crearBoton("EXPORTAR EXCEL", VERDE, 155);
        botones.add(btnVerDetalle); botones.add(btnReimprimir);
        botones.add(btnAnular); botones.add(btnRevisar); botones.add(btnExportar);
        return botones;
    }

    //==========================================================
    // TABLA
    //==========================================================

    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "Venta N°",
                            "Fecha",
                            "Hora",
                            "Cliente",
                            "Pago",
                            "Comprobante",
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

        tablaVentas =
                new JTable(
                        modeloTabla
                );

        tablaVentas.setRowHeight(30);

        tablaVentas.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaVentas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaVentas.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaVentas.setSelectionForeground(
                Color.BLACK
        );

        tablaVentas.setGridColor(
                new Color(
                        225,
                        230,
                        238
                )
        );

        tablaVentas.setShowHorizontalLines(true);

        tablaVentas.setShowVerticalLines(true);

        tablaVentas.setFillsViewportHeight(true);

        tablaVentas.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaVentas.getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul();
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

                label.setOpaque(true);

                return label;
            }
        };

        for (int i = 0;
                i < tablaVentas.getColumnCount();
                i++) {

            tablaVentas
                    .getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(
                            renderer
                    );
        }
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    private static String dinero(BigDecimal n) { return MONEDA.format(n == null ? BigDecimal.ZERO : n); }
    private static String csv(String s) { return "\"" + s.replace("\"", "\"\"") + "\""; }
    private LocalDate fecha(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try { return LocalDate.parse(valor.trim(), FECHA); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha inválida: " + valor + ". Usá dd/MM/yyyy."); }
    }
    private void cargarMediosPago() {
        try (Connection c = conexion.getConexion();
             PreparedStatement ps = c.prepareStatement("SELECT nombre FROM medio_pago WHERE activo=1 ORDER BY orden_visual, nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) comboPago.addItem(rs.getString(1));
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "No se pudieron cargar los medios de pago: " + ex.getMessage()); }
    }
    private static class Registro {
        long id; Object[] columnas; BigDecimal total; String estado;
        Registro(long id, Object[] columnas, BigDecimal total, String estado) {
            this.id=id; this.columnas=columnas; this.total=total; this.estado=estado;
        }
    }
    private void cargarVentas() {
        final LocalDate desde, hasta;
        try {
            desde=fecha(txtDesde.getText()); hasta=fecha(txtHasta.getText());
            if (desde != null && hasta != null && desde.isAfter(hasta)) throw new IllegalArgumentException("La fecha Desde no puede ser posterior a Hasta.");
        } catch (IllegalArgumentException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); return; }
        final String numero=txtNumeroVenta.getText().trim(), cliente=txtCliente.getText().trim();
        final String pago=(String)comboPago.getSelectedItem(), estado=(String)comboEstado.getSelectedItem();
        btnBuscar.setEnabled(false);
        new SwingWorker<List<Registro>,Void>() {
            @Override protected List<Registro> doInBackground() throws Exception {
                StringBuilder sql=new StringBuilder("SELECT v.id_venta,v.numero_venta,v.fecha, "
                    + "COALESCE(NULLIF(TRIM(c.razon_social),''),NULLIF(TRIM(CONCAT_WS(' ',c.nombre,c.apellido)),''),'Consumidor Final') cliente, "
                    + "COALESCE((SELECT GROUP_CONCAT(DISTINCT mp.nombre ORDER BY mp.nombre SEPARATOR ', ') "
                    + "FROM venta_pago vp JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE vp.id_venta=v.id_venta),'Sin pago') pago, "
                    + "v.total,v.estado FROM venta v LEFT JOIN cliente c ON c.id_cliente=v.id_cliente WHERE 1=1");
                List<Object> args=new ArrayList<>();
                if (desde!=null) {sql.append(" AND v.fecha >= ?"); args.add(Timestamp.valueOf(desde.atStartOfDay()));}
                if (hasta!=null) {sql.append(" AND v.fecha < ?"); args.add(Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));}
                if (!numero.isEmpty()) {
                    try {sql.append(" AND v.numero_venta=?"); args.add(Long.parseLong(numero));}
                    catch(NumberFormatException ex){throw new IllegalArgumentException("El número de venta debe ser numérico.");}
                }
                if (!cliente.isEmpty()) {sql.append(" AND (c.razon_social LIKE ? OR c.nombre LIKE ? OR c.apellido LIKE ? OR c.nro_documento LIKE ?)");
                    for(int i=0;i<4;i++) args.add("%"+cliente+"%");}
                if (pago!=null && !pago.equals("Todos")) {sql.append(" AND EXISTS (SELECT 1 FROM venta_pago vp2 JOIN medio_pago mp2 ON mp2.id_medio_pago=vp2.id_medio_pago WHERE vp2.id_venta=v.id_venta AND mp2.nombre=?)");args.add(pago);}
                if (estado!=null && !estado.equals("Todos")) {sql.append(" AND v.estado=?");args.add(estado);}
                sql.append(" ORDER BY v.fecha DESC,v.id_venta DESC LIMIT 5000");
                List<Registro> resultado=new ArrayList<>();
                try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql.toString())) {
                    for(int i=0;i<args.size();i++) ps.setObject(i+1,args.get(i));
                    try(ResultSet rs=ps.executeQuery()) {while(rs.next()) {
                        Timestamp f=rs.getTimestamp("fecha"); BigDecimal t=rs.getBigDecimal("total"); String est=rs.getString("estado");
                        resultado.add(new Registro(rs.getLong("id_venta"),new Object[]{
                            String.format("%08d",rs.getLong("numero_venta")),f.toLocalDateTime().toLocalDate().format(FECHA),
                            f.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            rs.getString("cliente"),rs.getString("pago"),"Ticket",dinero(t),est},t,est));
                    }}
                }
                return resultado;
            }
            @Override protected void done() {
                btnBuscar.setEnabled(true);
                try {
                    List<Registro> filas=get(); modeloTabla.setRowCount(0);idsVentas.clear();
                    BigDecimal total=BigDecimal.ZERO;int finalizadas=0,anuladas=0;
                    for(Registro r:filas) {
                        modeloTabla.addRow(r.columnas);idsVentas.add(r.id);
                        if("FINALIZADA".equals(r.estado) || r.estado.startsWith("DEVUELTA_")) {total=total.add(r.total);finalizadas++;}
                        if("ANULADA".equals(r.estado)) anuladas++;
                    }
                    lblCantidadVentas.setText(String.valueOf(filas.size()));lblAnuladas.setText(String.valueOf(anuladas));
                    lblTotalVendido.setText(dinero(total));
                    lblTicketPromedio.setText(dinero(finalizadas==0?BigDecimal.ZERO:total.divide(BigDecimal.valueOf(finalizadas),2,java.math.RoundingMode.HALF_UP)));
                } catch(Exception ex) {JOptionPane.showMessageDialog(PanelHistorialVentas.this,"Error al consultar ventas: "+ex.getCause());}
            }
        }.execute();
    }
    private long seleccion() {
        int fila=tablaVentas.getSelectedRow();
        if(fila<0) {JOptionPane.showMessageDialog(this,"Seleccioná una venta de la tabla.");return -1;}
        return idsVentas.get(tablaVentas.convertRowIndexToModel(fila));
    }
    private void verDetalle() {
        long id=seleccion();if(id<0)return;
        StringBuilder sb=new StringBuilder();
        String q="SELECT v.numero_venta,v.fecha,v.estado,v.subtotal,v.descuento_total,v.recargo_total,v.total,v.motivo_anulacion,u.nombre_completo "
            + "FROM venta v JOIN usuario u ON u.id_usuario=v.id_usuario WHERE v.id_venta=?";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(q)) {
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()) {if(rs.next()) {
                sb.append("Venta N° ").append(rs.getLong(1)).append("  |  ").append(rs.getTimestamp(2)).append("\n")
                  .append("Estado: ").append(rs.getString(3)).append("  |  Cajero: ").append(rs.getString(9)).append("\n\n");
                if(rs.getString(8)!=null) sb.append("Motivo anulación: ").append(rs.getString(8)).append("\n");
                sb.append("Subtotal: ").append(dinero(rs.getBigDecimal(4))).append("  Descuento: ").append(dinero(rs.getBigDecimal(5)))
                  .append("  Recargo: ").append(dinero(rs.getBigDecimal(6))).append("\nTOTAL: ").append(dinero(rs.getBigDecimal(7))).append("\n\nPRODUCTOS\n");
            }}
            try(PreparedStatement det=c.prepareStatement("SELECT descripcion_producto,cantidad,precio_unitario,subtotal FROM venta_detalle WHERE id_venta=? ORDER BY id_detalle")) {
                det.setLong(1,id);try(ResultSet rs=det.executeQuery()) {while(rs.next()) sb.append(rs.getString(1)).append(" | ").append(rs.getBigDecimal(2))
                    .append(" x ").append(dinero(rs.getBigDecimal(3))).append(" = ").append(dinero(rs.getBigDecimal(4))).append("\n");}
            }
            sb.append("\nMEDIOS DE PAGO\n");
            try(PreparedStatement pagos=c.prepareStatement("SELECT mp.nombre,vp.monto,vp.importe_recibido,vp.cambio FROM venta_pago vp JOIN medio_pago mp ON mp.id_medio_pago=vp.id_medio_pago WHERE vp.id_venta=?")) {
                pagos.setLong(1,id);try(ResultSet rs=pagos.executeQuery()) {while(rs.next()) sb.append(rs.getString(1)).append(": ").append(dinero(rs.getBigDecimal(2)))
                    .append("  Recibido: ").append(dinero(rs.getBigDecimal(3))).append("  Cambio: ").append(dinero(rs.getBigDecimal(4))).append("\n");}
            }
            javax.swing.JTextArea area=new javax.swing.JTextArea(sb.toString(),19,65);area.setEditable(false);area.setFont(new Font("Monospaced",Font.PLAIN,12));
            JOptionPane.showMessageDialog(this,new JScrollPane(area),"Detalle de venta",JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception ex){JOptionPane.showMessageDialog(this,"Error al ver detalle: "+ex.getMessage());}
    }

    private void anularVenta() {
        long id=seleccion();
        if(id<0)return;
        if(!sesion.SesionUsuario.tienePermiso("VENTA_ANULAR") && !sesion.SesionUsuario.tienePermiso("VENTA_DEVOLVER")){
            JOptionPane.showMessageDialog(this,"Sin permiso VENTA_ANULAR ni VENTA_DEVOLVER.","Acceso restringido",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila=tablaVentas.getSelectedRow();
        String estado=String.valueOf(tablaVentas.getValueAt(fila,7));
        if("ANULADA".equals(estado)){
            int respuesta=JOptionPane.showConfirmDialog(this,"La venta está anulada. ¿Registrar un reintegro YA realizado?",
                "Reintegro",JOptionPane.YES_NO_OPTION);
            if(respuesta==JOptionPane.YES_OPTION)confirmarReintegro(id);
            return;
        }
        if(!"FINALIZADA".equals(estado) && !"DEVUELTA_PARCIAL".equals(estado)){
            JOptionPane.showMessageDialog(this,"La venta no admite nuevas devoluciones: "+estado);return;
        }
        boolean anular=false;
        if("FINALIZADA".equals(estado)){
            Object[] opciones={"Devolución parcial o total","Anulación administrativa","Cancelar"};
            int r=JOptionPane.showOptionDialog(this,
                "Elegí la operación. Cada producto tendrá su propio destino de mercadería.",
                "Serena Soft | Anulación / Devolución",JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
            if(r==2||r==JOptionPane.CLOSED_OPTION)return;
            anular=(r==1);
        }
        if(!sesion.SesionUsuario.tienePermiso(anular?"VENTA_ANULAR":"VENTA_DEVOLVER")){
            JOptionPane.showMessageDialog(this,"No tenés permiso para esa operación.");return;
        }
        try{
            java.util.List<services.DevolucionVentaService.Linea> disponibles=services.DevolucionVentaService.consultar(id);
            if(disponibles.isEmpty())throw new IllegalStateException("No hay productos en esta venta.");
            Dialogos.DialogoEvaluacionDevolucion dialogo = new Dialogos.DialogoEvaluacionDevolucion(
                javax.swing.SwingUtilities.getWindowAncestor(this), disponibles, anular);
            dialogo.setVisible(true);
            if (!dialogo.fueConfirmado()) return;
            final String motivo = dialogo.getMotivo();
            final java.util.List<services.DevolucionVentaService.Linea> seleccionadas = dialogo.getSeleccionadas();
            final boolean esAnulacion=anular;
            btnAnular.setEnabled(false);
            new SwingWorker<services.DevolucionVentaService.Resultado,Void>(){
                @Override protected services.DevolucionVentaService.Resultado doInBackground()throws Exception{
                    return esAnulacion?services.DevolucionVentaService.anular(id,motivo,seleccionadas):
                        services.DevolucionVentaService.devolver(id,motivo,seleccionadas);
                }
                @Override protected void done(){
                    btnAnular.setEnabled(true);
                    try{
                        services.DevolucionVentaService.Resultado resultado=get();
                        cargarVentas();
                        JOptionPane.showMessageDialog(PanelHistorialVentas.this,
                            "Operación registrada correctamente.\nReintegro PENDIENTE: "+dinero(resultado.monto)+
                            "\nLíneas aptas reintegradas al stock: "+resultado.reingresados+
                            (esAnulacion?"":"\nID de devolución: "+resultado.idDevolucion)+
                            "\nNo se devolvió dinero automáticamente.","Serena Soft",JOptionPane.INFORMATION_MESSAGE);
                    }catch(Exception ex){
                        Throwable causa=ex instanceof ExecutionException && ex.getCause()!=null?ex.getCause():ex;
                        JOptionPane.showMessageDialog(PanelHistorialVentas.this,
                            "No se registró la operación.\n"+causa.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"No se pudo preparar la operación: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void revisarMercaderia(){
        long id=seleccion();if(id<0)return;
        if(!sesion.SesionUsuario.tienePermiso("VENTA_DEVOLVER")){
            JOptionPane.showMessageDialog(this,"Sin permiso VENTA_DEVOLVER.");return;
        }
        Object[] acciones={"Resolver mercadería en revisión","Confirmar reintegro YA realizado","Cancelar"};
        int accion=JOptionPane.showOptionDialog(this,"¿Qué necesitás gestionar?", "Revisión y reintegros",
            JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,acciones,acciones[0]);
        if(accion==2 || accion==JOptionPane.CLOSED_OPTION)return;
        if(accion==1){
            try{
                java.util.List<services.DevolucionVentaService.Reintegro> pendientes=
                    services.DevolucionVentaService.reintegrosPendientes(id);
                if(pendientes.isEmpty()){
                    JOptionPane.showMessageDialog(this,"No hay reintegros de devoluciones pendientes.");return;
                }
                Object elegido=JOptionPane.showInputDialog(this,"Seleccioná el reintegro:",
                    "Confirmación de dinero devuelto",JOptionPane.QUESTION_MESSAGE,null,
                    pendientes.toArray(),pendientes.get(0));
                if(!(elegido instanceof services.DevolucionVentaService.Reintegro))return;
                String referencia=JOptionPane.showInputDialog(this,"Referencia comprobable del dinero YA DEVUELTO:");
                if(referencia==null)return;
                int ok=JOptionPane.showConfirmDialog(this,
                    "Confirmá SOLO si el dinero ya fue entregado al cliente.\n¿Registrar el reintegro?",
                    "Confirmación financiera",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if(ok!=JOptionPane.YES_OPTION)return;
                services.DevolucionVentaService.confirmarReintegroDevolucion(
                    ((services.DevolucionVentaService.Reintegro)elegido).idDevolucion,referencia);
                JOptionPane.showMessageDialog(this,"Reintegro marcado como CONFIRMADO.");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"No se pudo confirmar: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        try{
            java.util.List<services.DevolucionVentaService.Revision> revisiones=
                services.DevolucionVentaService.revisionesPendientes(id);
            if(revisiones.isEmpty()){
                JOptionPane.showMessageDialog(this,"Esta venta no tiene mercadería pendiente de revisión.");return;
            }
            Object elegido=JOptionPane.showInputDialog(this,"Seleccioná el producto a evaluar:",
                "Revisión de mercadería",JOptionPane.QUESTION_MESSAGE,null,
                revisiones.toArray(),revisiones.get(0));
            if(!(elegido instanceof services.DevolucionVentaService.Revision))return;
            services.DevolucionVentaService.Revision rev=(services.DevolucionVentaService.Revision)elegido;
            Object[] opciones={"APTO PARA REVENTA","MERMA / DESCARTE","Cancelar"};
            int destino=JOptionPane.showOptionDialog(this,
                "¿Cuál es el resultado de la inspección?\n"+rev,
                "Resolver revisión",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,
                null,opciones,opciones[1]);
            if(destino!=0 && destino!=1)return;
            String observacion=JOptionPane.showInputDialog(this,"Informe de evaluación (mínimo 10 caracteres):");
            if(observacion==null)return;
            if(observacion.trim().length()<10){
                JOptionPane.showMessageDialog(this,"El informe debe tener al menos 10 caracteres.");return;
            }
            services.DevolucionVentaService.Destino resultado=destino==0?
                services.DevolucionVentaService.Destino.APTO:services.DevolucionVentaService.Destino.MERMA;
            int confirmar=JOptionPane.showConfirmDialog(this,
                "¿Confirmás "+resultado+" para "+rev.producto+"?\n"+
                "Solo APTO aumenta el stock disponible. Esta resolución queda auditada.",
                "Confirmar revisión",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(confirmar!=JOptionPane.YES_OPTION)return;
            services.DevolucionVentaService.resolverRevision(rev.idRetorno,resultado,observacion);
            JOptionPane.showMessageDialog(this,"Revisión registrada correctamente.");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"No se pudo resolver: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarReintegro(long id) {
        String referencia=JOptionPane.showInputDialog(this,
            "Referencia del dinero YA DEVUELTO (recibo, transferencia, fecha y medio):",
            "Registrar reintegro realizado",JOptionPane.QUESTION_MESSAGE);
        if(referencia==null)return;
        try {
            services.AnulacionVentaService.confirmarReintegro(id,referencia);
            JOptionPane.showMessageDialog(this,"Reintegro registrado como CONFIRMADO.");
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"El reintegro sigue PENDIENTE: "+ex.getMessage(),
                "No se confirmó el reintegro",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void reimprimir() {
        long id = seleccion(); if (id < 0) return;
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Reimprimir el ticket de la venta seleccionada?\nSe enviará a la impresora predeterminada.",
            "Reimprimir ticket", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) return;
        btnReimprimir.setEnabled(false);
        new SwingWorker<Void,Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection c = conexion.getConexion();
                     PreparedStatement ps = c.prepareStatement("SELECT estado FROM venta WHERE id_venta=?")) {
                    ps.setLong(1,id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !"FINALIZADA".equals(rs.getString(1)))
                            throw new IllegalStateException("Solo se reimprimen tickets de ventas FINALIZADAS.");
                    }
                }
                TicketVentaJasper.imprimirRapido(id);
                return null;
            }
            @Override protected void done() {
                btnReimprimir.setEnabled(true);
                try { get(); JOptionPane.showMessageDialog(PanelHistorialVentas.this, "Ticket enviado a la impresora."); }
                catch (Exception ex) {
                    Throwable causa = ex instanceof ExecutionException && ex.getCause()!=null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(PanelHistorialVentas.this, "Error de impresión: " + causa.getMessage(), "Impresión", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    private void exportarExcel() {
        final LocalDate desde, hasta;
        try {
            desde = fecha(txtDesde.getText()); hasta = fecha(txtHasta.getText());
            if (desde == null || hasta == null) throw new IllegalArgumentException("Ingresá fecha Desde y Hasta para exportar.");
            if (desde.isAfter(hasta)) throw new IllegalArgumentException("Desde no puede superar Hasta.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revisar fechas", JOptionPane.WARNING_MESSAGE); return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar historial de ventas en Excel");
        fc.setSelectedFile(new File("SerenaSoft_Ventas_" + desde + "_a_" + hasta + ".xlsx"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File archivo = fc.getSelectedFile();
        if (!archivo.getName().toLowerCase(Locale.ROOT).endsWith(".xlsx")) archivo = new File(archivo.getParentFile(), archivo.getName() + ".xlsx");
        if (archivo.exists() && JOptionPane.showConfirmDialog(this, "El archivo existe. ¿Reemplazarlo?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        final File destino = archivo;
        final String numero = txtNumeroVenta.getText().trim();
        final String cliente = txtCliente.getText().trim();
        final String pago = (String)comboPago.getSelectedItem();
        final String estado = (String)comboEstado.getSelectedItem();
        btnExportar.setEnabled(false);
        new SwingWorker<Integer,Void>() {
            @Override protected Integer doInBackground() throws Exception {
                return services.ExportadorVentasExcel.exportar(destino, desde, hasta, numero, cliente, pago, estado);
            }
            @Override protected void done() {
                btnExportar.setEnabled(true);
                try {
                    int n = get();
                    JOptionPane.showMessageDialog(PanelHistorialVentas.this,
                        "Excel generado correctamente.\nVentas exportadas: " + n + "\nArchivo: " + destino.getAbsolutePath(),
                        "Exportación finalizada", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Throwable cause = ex instanceof ExecutionException && ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(PanelHistorialVentas.this, "No se pudo exportar: " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    //==========================================================
    // COMPONENTES AUXILIARES
    //==========================================================

    private JPanel crearPanelBlanco() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return panel;
    }

    private JLabel crearTituloSeccion(
            String texto) {

        JLabel label =
                new JLabel(texto);

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

    private JLabel crearLabel(
            String texto) {

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        return label;
    }

    private JTextField crearCampo(
            String texto) {

        JTextField campo =
                new JTextField(texto);

        campo.setPreferredSize(
                new Dimension(
                        145,
                        32
                )
        );

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        return campo;
    }

    private JComboBox<String> crearCombo(
            String[] datos) {

        JComboBox<String> combo =
                new JComboBox<>(datos);

        combo.setPreferredSize(
                new Dimension(
                        150,
                        32
                )
        );

        combo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        return combo;
    }

    private JButton crearBoton(
            String texto,
            Color fondo,
            int ancho) {

        JButton boton =
                new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        36
                )
        );

        boton.setBackground(fondo);

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

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
