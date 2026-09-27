/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Diseños.EstiloBotones;
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

    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // ACCIONES
    //==========================================================

    private JButton btnVerDetalle;
    private JButton btnReimprimir;
    private JButton btnAnular;
    private JButton btnExportar;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================

    public PanelHistorialVentas() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        construirInterfaz();

        cargarDatosPrueba();
        
        
        
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

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                10
                        )
                );

        botones.setOpaque(false);

        btnVerDetalle =
                crearBoton(
                        "VER DETALLE",
                        AZUL_OSCURO,
                        130
                );

        btnReimprimir =
                crearBoton(
                        "REIMPRIMIR",
                        AZUL,
                        125
                );

        btnAnular =
                crearBoton(
                        "ANULAR / DEVOLVER",
                        ROJO,
                        160
                );

        btnExportar =
                crearBoton(
                        "EXPORTAR",
                        VERDE,
                        120
                );

        botones.add(btnVerDetalle);
        botones.add(btnReimprimir);
        botones.add(btnAnular);
        botones.add(btnExportar);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        return panel;
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

    private void cargarDatosPrueba() {

        modeloTabla.addRow(
                new Object[]{
                    "00000125",
                    "28/08/2026",
                    "10:42",
                    "Consumidor Final",
                    "Efectivo",
                    "Ticket",
                    "$ 38.800",
                    "Realizada"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "00000124",
                    "28/08/2026",
                    "10:35",
                    "Juan Pérez",
                    "Tarjeta",
                    "Factura A",
                    "$ 25.600",
                    "Realizada"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "00000123",
                    "28/08/2026",
                    "10:17",
                    "María López",
                    "Transferencia",
                    "Factura B",
                    "$ 18.900",
                    "Realizada"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "00000122",
                    "28/08/2026",
                    "09:58",
                    "Consumidor Final",
                    "Efectivo",
                    "Ticket",
                    "$ 12.500",
                    "Anulada"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "00000121",
                    "28/08/2026",
                    "09:40",
                    "Comercial NEA",
                    "Cuenta Corriente",
                    "Factura A",
                    "$ 42.700",
                    "Realizada"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "00000120",
                    "27/08/2026",
                    "19:25",
                    "Consumidor Final",
                    "Efectivo",
                    "Ticket",
                    "$ 8.950",
                    "Realizada"
                }
        );
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
