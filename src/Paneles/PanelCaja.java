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
import Diseños.EstiloBotones;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import rojeru_san.rsbutton.RSButtonRound;
import rojerusan.RSComboBox;

/**
 *
 * @author santi
 */
public class PanelCaja extends javax.swing.JPanel {

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

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // BOTONES
    //==========================================================
    private RSButtonRound btnAbrirCaja;
    private RSButtonRound btnIngreso;
    private RSButtonRound btnEgreso;
    private RSButtonRound btnArqueo;
    private RSButtonRound btnCerrarCaja;

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

        cargarDatosPrueba();
         EstiloBotones.corregirBotones(this);

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

        setLayout(
                new BorderLayout()
        );

        setBackground(
                FONDO
        );

        JPanel contenedor
                = new JPanel();

        contenedor.setLayout(
                new BoxLayout(
                        contenedor,
                        BoxLayout.Y_AXIS
                )
        );

        contenedor.setBackground(
                FONDO
        );

        contenedor.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
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

        contenedor.add(
                header
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // ESTADO
        //======================================================
        JPanel estado
                = crearPanelEstadoCaja();

        estado.setAlignmentX(
                LEFT_ALIGNMENT
        );

        estado.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        90
                )
        );

        contenedor.add(
                estado
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // RESUMEN
        //======================================================
        JPanel resumen
                = crearPanelResumen();

        resumen.setAlignmentX(
                LEFT_ALIGNMENT
        );

        resumen.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        contenedor.add(
                resumen
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // FILTROS
        //======================================================
        JPanel filtros
                = crearPanelFiltros();

        filtros.setAlignmentX(
                LEFT_ALIGNMENT
        );

        filtros.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        70
                )
        );

        contenedor.add(
                filtros
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // TABLA
        //======================================================
        JPanel tabla
                = crearPanelMovimientos();

        tabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        tabla.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        330
                )
        );

        contenedor.add(
                tabla
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // MEDIOS DE PAGO
        //======================================================
        JPanel medios
                = crearPanelMediosPago();

        medios.setAlignmentX(
                LEFT_ALIGNMENT
        );

        medios.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        contenedor.add(
                medios
        );

        contenedor.add(
                Box.createVerticalStrut(12)
        );

        //======================================================
        // BOTONES
        //======================================================
        JPanel acciones
                = crearPanelAcciones();

        acciones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        acciones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        70
                )
        );

        contenedor.add(
                acciones
        );

        JScrollPane scroll
                = new JScrollPane(
                        contenedor
                );

        scroll.setBorder(
                null
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

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

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(
                false
        );

        JLabel titulo
                = new JLabel(
                        "CAJA"
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

        JLabel subtitulo
                = new JLabel(
                        "Control de movimientos, ingresos, egresos y cierre de caja"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    //==========================================================
    // ESTADO CAJA
    //==========================================================
    private JPanel crearPanelEstadoCaja() {

        JPanel panel
                = new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
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
                                5,
                                12,
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

        panel.add(
                crearTarjetaDato(
                        "Saldo Actual",
                        lblSaldoActual
                )
        );

        return panel;
    }

    //==========================================================
    // FILTROS
    //==========================================================
    private JPanel crearPanelFiltros() {

        JPanel panel
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Filtros"
                )
        );

        panel.add(
                crearLabel(
                        "Tipo"
                )
        );

        comboTipoMovimiento.setPreferredSize(
                new Dimension(
                        170,
                        32
                )
        );

        panel.add(
                comboTipoMovimiento
        );

        panel.add(
                crearLabel(
                        "Período"
                )
        );

        comboFecha.setPreferredSize(
                new Dimension(
                        160,
                        32
                )
        );

        panel.add(
                comboFecha
        );

        panel.add(
                btnBuscar
        );

        return panel;
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

        panel.setBorder(
                crearBordeTitulo(
                        "Movimientos de Caja"
                )
        );

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

        panel.setBorder(
                crearBordeTitulo(
                        "Resumen por Medio de Pago"
                )
        );

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

        return panel;
    }

    //==========================================================
    // TARJETA
    //==========================================================
    private JPanel crearTarjetaDato(
            String titulo,
            JLabel valor) {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
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
                                10,
                                14,
                                10,
                                14
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
                        65,
                        70,
                        80
                )
        );

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                valor,
                BorderLayout.CENTER
        );

        return panel;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloTabla.setRowCount(
                0
        );

        modeloTabla.addRow(
                new Object[]{
                    "26/08/2026",
                    "08:45",
                    "Venta",
                    "Venta N° 00000120",
                    "Efectivo",
                    "$ 18.500",
                    "",
                    "Santiago"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "26/08/2026",
                    "09:10",
                    "Venta",
                    "Venta N° 00000121",
                    "Tarjeta",
                    "$ 32.000",
                    "",
                    "Santiago"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "26/08/2026",
                    "09:35",
                    "Ingreso",
                    "Fondo adicional",
                    "Efectivo",
                    "$ 20.000",
                    "",
                    "Santiago"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "26/08/2026",
                    "10:05",
                    "Egreso",
                    "Compra de insumos",
                    "Efectivo",
                    "",
                    "$ 5.500",
                    "Santiago"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "26/08/2026",
                    "10:45",
                    "Pago Proveedor",
                    "Distribuidora Norte",
                    "Transferencia",
                    "",
                    "$ 10.000",
                    "Santiago"
                }
        );
    }

    //==========================================================
    // EVENTOS TEMPORALES
    //==========================================================
    private void configurarEventos() {

        btnAbrirCaja.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(
                            this
                    );

            DialogoAperturaCaja dialogo
                    = new DialogoAperturaCaja(
                            ventana,
                            "Santiago"
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                double saldoInicial
                        = dialogo.getSaldoInicial();

                System.out.println(
                        "Caja abierta con: "
                        + saldoInicial
                );
            }
        });

        btnIngreso.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(this);

            DialogoIngresoCaja dialogo
                    = new DialogoIngresoCaja(
                            ventana,
                            "Santiago"
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println(
                        "Ingreso: $ "
                        + dialogo.getMonto()
                );

                System.out.println(
                        "Medio: "
                        + dialogo.getMedioPago()
                );

                System.out.println(
                        "Motivo: "
                        + dialogo.getMotivo()
                );
            }
        });

        btnEgreso.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(this);

            DialogoEgresoCaja dialogo
                    = new DialogoEgresoCaja(
                            ventana,
                            "Santiago"
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println(
                        "Egreso: $ "
                        + dialogo.getMonto()
                );

                System.out.println(
                        "Medio de pago: "
                        + dialogo.getMedioPago()
                );

                System.out.println(
                        "Motivo: "
                        + dialogo.getMotivo()
                );
            }
        });
        btnArqueo.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(this);

            DialogoArqueoCaja dialogo
                    = new DialogoArqueoCaja(
                            ventana,
                            "Santiago",
                            152500,
                            64500,
                            22900,
                            0
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println(
                        "Esperado: "
                        + dialogo.getEfectivoEsperado()
                );

                System.out.println(
                        "Contado: "
                        + dialogo.getEfectivoContado()
                );

                System.out.println(
                        "Diferencia: "
                        + dialogo.getDiferencia()
                );

                System.out.println(
                        "Estado: "
                        + dialogo.getEstadoArqueo()
                );
            }
        });

        btnCerrarCaja.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(this);

            DialogoCerrarCaja dialogo
                    = new DialogoCerrarCaja(
                            ventana,
                            "Santiago",
                            50000, // saldo inicial
                            98000, // ventas efectivo
                            20000, // ingresos efectivo
                            15500, // egresos efectivo

                            64500, // tarjeta
                            22900, // transferencia
                            0 // Mercado Pago
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println("CAJA CERRADA");

                System.out.println(
                        "Esperado: "
                        + dialogo.getEfectivoEsperado()
                );

                System.out.println(
                        "Contado: "
                        + dialogo.getEfectivoContado()
                );

                System.out.println(
                        "Diferencia: "
                        + dialogo.getDiferencia()
                );

                System.out.println(
                        "Estado: "
                        + dialogo.getEstadoCierre()
                );
            }
        });
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

    private RSButtonRound crearBotonAccion(
            String texto,
            Color color) {

        RSButtonRound boton
                = new RSButtonRound();

        boton.setText(
                texto
        );

        boton.setPreferredSize(
                new Dimension(
                        145,
                        40
                )
        );

        boton.setBackground(
                color
        );

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

    //==========================================================
    // BORDE
    //==========================================================
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
