/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Dialogos.DialogoEditarProveedor;
import Dialogos.DialogoFichaProveedor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Dialogos.DialogoNuevoProveedor;
import Diseños.EstiloBotones;
import java.awt.Window;
import javax.swing.SwingUtilities;

public class PanelProveedores extends javax.swing.JPanel {

    //==========================================================
    // BUSQUEDA
    //==========================================================
    private JTextField txtBuscar;
    private JButton btnBuscar;

    //==========================================================
    // RESUMEN
    //==========================================================
    private JLabel lblTotalProveedores;
    private JLabel lblProveedoresActivos;
    private JLabel lblProveedoresDeuda;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnVerFicha;
    private JButton btnActivarBaja;
    private JButton btnActualizar;

    //==========================================================
    // DETALLE RAPIDO
    //==========================================================
    private JLabel lblDetalleProveedor;
    private JLabel lblDetalleCuit;
    private JLabel lblDetalleSaldo;
    private JLabel lblDetalleUltimaCompra;
    private JLabel lblDetalleTotalComprado;

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
    public PanelProveedores() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();

        actualizarResumen();
        
         EstiloBotones.corregirBotones(this);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        txtBuscar = new JTextField();

        btnBuscar
                = crearBoton(
                        "Buscar",
                        AZUL,
                        100
                );

        btnNuevo
                = crearBoton(
                        "Nuevo Proveedor",
                        VERDE,
                        150
                );

        btnEditar
                = crearBoton(
                        "Editar",
                        AZUL,
                        110
                );

        btnVerFicha
                = crearBoton(
                        "Ver Ficha",
                        NARANJA,
                        120
                );

        btnActivarBaja
                = crearBoton(
                        "Activar / Dar de Baja",
                        GRIS,
                        170
                );

        btnActualizar
                = crearBoton(
                        "Actualizar",
                        AZUL_OSCURO,
                        120
                );

        lblTotalProveedores
                = crearValorResumen(
                        "0",
                        AZUL
                );

        lblProveedoresActivos
                = crearValorResumen(
                        "0",
                        VERDE
                );

        lblProveedoresDeuda
                = crearValorResumen(
                        "0",
                        ROJO
                );

        lblDetalleProveedor
                = new JLabel("-");

        lblDetalleCuit
                = new JLabel("-");

        lblDetalleSaldo
                = new JLabel("$ 0,00");

        lblDetalleUltimaCompra
                = new JLabel("-");

        lblDetalleTotalComprado
                = new JLabel("$ 0,00");

        inicializarTabla();
    }

    //==========================================================
    // TABLA
    //==========================================================
    private void inicializarTabla() {

        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "CUIT",
                            "Razón Social",
                            "Contacto",
                            "Teléfono",
                            "Estado",
                            "Última Compra"
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

        tablaProveedores
                = new JTable(modeloTabla);

        tablaProveedores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaProveedores.setRowHeight(30);

        tablaProveedores.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaProveedores.setBackground(
                Color.WHITE
        );

        tablaProveedores.setForeground(
                new Color(30, 30, 30)
        );

        tablaProveedores.setGridColor(
                new Color(
                        220,
                        226,
                        235
                )
        );

        tablaProveedores.setShowHorizontalLines(true);
        tablaProveedores.setShowVerticalLines(true);

        tablaProveedores.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaProveedores.setSelectionForeground(
                Color.BLACK
        );

        tablaProveedores.setFillsViewportHeight(true);

        tablaProveedores
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaProveedores
                .getTableHeader()
                .setReorderingAllowed(false);

        aplicarHeaderAzul();

        //======================================================
        // ANCHO COLUMNAS
        //======================================================
        tablaProveedores
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);

        tablaProveedores
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(130);

        tablaProveedores
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(220);

        tablaProveedores
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(160);

        tablaProveedores
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(130);

        tablaProveedores
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(100);

        tablaProveedores
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(130);
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
                i < tablaProveedores.getColumnCount();
                i++) {

            tablaProveedores
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

        setBackground(FONDO);

        JPanel contenedor
                = new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header
                = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new javax.swing.BoxLayout(
                        header,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        "PROVEEDORES"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        JLabel lblSubtitulo
                = new JLabel(
                        "Administración de proveedores, compras y cuentas comerciales"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        header.add(
                lblTitulo
        );

        header.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        header.add(
                lblSubtitulo
        );

        contenedor.add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CENTRO
        //======================================================
        JPanel centro
                = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new javax.swing.BoxLayout(
                        centro,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        10,
                        0,
                        10,
                        0
                )
        );

        JPanel panelBusqueda
                = crearPanelBusqueda();

        JPanel panelResumen
                = crearPanelResumen();

        JPanel panelTabla
                = crearPanelTabla();

        JPanel panelDetalle
                = crearPanelDetalleRapido();

        panelBusqueda.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelResumen.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelTabla.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelDetalle.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelBusqueda.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        80
                )
        );

        panelResumen.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        85
                )
        );

        panelTabla.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        320
                )
        );

        panelDetalle.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        centro.add(panelBusqueda);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelResumen);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelTabla);

        centro.add(
                javax.swing.Box
                        .createVerticalStrut(15)
        );

        centro.add(panelDetalle);

        //======================================================
        // SCROLL CENTRAL
        //======================================================
        JScrollPane scrollContenido
                = new JScrollPane(
                        centro
                );

        scrollContenido.setBorder(null);

        scrollContenido
                .setHorizontalScrollBarPolicy(
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        scrollContenido
                .setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                );

        scrollContenido
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollContenido
                .getViewport()
                .setBackground(
                        Color.WHITE
                );

        contenedor.add(
                scrollContenido,
                BorderLayout.CENTER
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // BUSQUEDA
    //==========================================================
    private JPanel crearPanelBusqueda() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Buscar Proveedor"
                )
        );

        GridBagConstraints c
                = crearConstraints();

        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Nombre / Razón Social / CUIT"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill
                = GridBagConstraints.HORIZONTAL;

        txtBuscar.setPreferredSize(
                new Dimension(
                        400,
                        32
                )
        );

        panel.add(
                txtBuscar,
                c
        );

        c.gridx = 2;
        c.weightx = 0;
        c.fill
                = GridBagConstraints.NONE;

        panel.add(
                btnBuscar,
                c
        );

        return panel;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private JPanel crearPanelResumen() {

        JPanel panel
                = new JPanel(
                        new java.awt.GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearTarjetaResumen(
                        "Proveedores",
                        lblTotalProveedores
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Activos",
                        lblProveedoresActivos
                )
        );

        panel.add(
                crearTarjetaResumen(
                        "Con deuda",
                        lblProveedoresDeuda
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

        panel.setBackground(
                new Color(
                        250,
                        251,
                        253
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
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
                        13
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
    // TABLA + BOTONES
    //==========================================================
    private JPanel crearPanelTabla() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Listado de Proveedores"
                )
        );

        JScrollPane scroll
                = new JScrollPane(
                        tablaProveedores
                );

        scroll.setPreferredSize(
                new Dimension(
                        900,
                        210
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel botones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                8
                        )
                );

        botones.setOpaque(false);

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnVerFicha);
        botones.add(btnActivarBaja);
        botones.add(btnActualizar);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    //==========================================================
    // DETALLE RAPIDO
    //==========================================================
    private JPanel crearPanelDetalleRapido() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Detalle Rápido"
                )
        );

        GridBagConstraints c
                = crearConstraints();

        //======================================================
        // PROVEEDOR
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel("Proveedor"),
                c
        );

        c.gridx = 1;

        panel.add(
                lblDetalleProveedor,
                c
        );

        //======================================================
        // CUIT
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel("CUIT"),
                c
        );

        c.gridx = 3;

        panel.add(
                lblDetalleCuit,
                c
        );

        //======================================================
        // SALDO
        //======================================================
        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Saldo Pendiente"
                ),
                c
        );

        c.gridx = 5;

        lblDetalleSaldo.setForeground(
                ROJO
        );

        lblDetalleSaldo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        panel.add(
                lblDetalleSaldo,
                c
        );

        //======================================================
        // ULTIMA COMPRA
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                crearLabel(
                        "Última Compra"
                ),
                c
        );

        c.gridx = 1;

        panel.add(
                lblDetalleUltimaCompra,
                c
        );

        //======================================================
        // TOTAL COMPRADO
        //======================================================
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Total Comprado"
                ),
                c
        );

        c.gridx = 3;

        lblDetalleTotalComprado.setForeground(
                VERDE
        );

        lblDetalleTotalComprado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        panel.add(
                lblDetalleTotalComprado,
                c
        );

        return panel;
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloTabla.setRowCount(0);

        modeloTabla.addRow(
                new Object[]{
                    1,
                    "30-12345678-9",
                    "Distribuidora Norte",
                    "Juan Pérez",
                    "3624-123456",
                    "Activo",
                    "21/08/2026"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    2,
                    "30-87654321-0",
                    "Coca Cola FEMSA",
                    "María Gómez",
                    "3624-555555",
                    "Activo",
                    "20/08/2026"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    3,
                    "30-45678912-3",
                    "Mayorista Central",
                    "Carlos López",
                    "3624-777777",
                    "Activo",
                    "18/08/2026"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    4,
                    "30-11223344-5",
                    "Distribuciones NEA",
                    "Ana Romero",
                    "3624-999999",
                    "Inactivo",
                    "10/07/2026"
                }
        );

        actualizarResumen();
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // SELECCION TABLA
        //======================================================
        tablaProveedores
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        actualizarDetalleSeleccionado();
                    }

                });

        //======================================================
        // BUSCAR
        //======================================================
        btnBuscar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "La búsqueda se conectará después con la base de datos.",
                            "Proveedores",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

        });

        //======================================================
        // NUEVO
        //======================================================
        btnNuevo.addActionListener(e -> {

            Window ventana
                    = SwingUtilities.getWindowAncestor(this);

            DialogoNuevoProveedor dialogo
                    = new DialogoNuevoProveedor(
                            ventana
                    );

            dialogo.setVisible(true);

        });

        //======================================================
        // EDITAR
        //======================================================
        btnEditar.addActionListener(e -> {

            int fila
                    = tablaProveedores.getSelectedRow();

            if (fila == -1) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un proveedor para editar.",
                        "Editar Proveedor",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            //==========================================================
            // OBTENEMOS DATOS DE LA TABLA
            //==========================================================
            int id
                    = Integer.parseInt(
                            modeloTabla
                                    .getValueAt(
                                            fila,
                                            0
                                    )
                                    .toString()
                    );

            String cuit
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    1
                            )
                            .toString();

            String razonSocial
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    2
                            )
                            .toString();

            String telefono
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    4
                            )
                            .toString();

            String estado
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    5
                            )
                            .toString();

            //==========================================================
            // VENTANA PADRE
            //==========================================================
            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            //==========================================================
            // ABRIMOS EDITAR
            //==========================================================
            DialogoEditarProveedor dialogo
                    = new DialogoEditarProveedor(
                            ventana,
                            id,
                            razonSocial,
                            cuit,
                            telefono,
                            estado
                    );

            dialogo.setVisible(true);

        });

        //======================================================
        // VER FICHA
        //======================================================
        btnVerFicha.addActionListener(e -> {

            int fila
                    = tablaProveedores.getSelectedRow();

            if (fila == -1) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un proveedor para ver su ficha.",
                        "Ficha del Proveedor",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int id
                    = Integer.parseInt(
                            modeloTabla
                                    .getValueAt(
                                            fila,
                                            0
                                    )
                                    .toString()
                    );

            String cuit
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    1
                            )
                            .toString();

            String razonSocial
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    2
                            )
                            .toString();

            String telefono
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    4
                            )
                            .toString();

            String estado
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    5
                            )
                            .toString();

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            DialogoFichaProveedor dialogo
                    = new DialogoFichaProveedor(
                            ventana,
                            id,
                            razonSocial,
                            cuit,
                            telefono,
                            estado
                    );

            dialogo.setVisible(true);

        });;

        //======================================================
        // BAJA / ACTIVAR
        //======================================================
        btnActivarBaja.addActionListener(e -> {

            if (!hayProveedorSeleccionado()) {
                return;
            }

            int fila
                    = tablaProveedores.getSelectedRow();

            String estado
                    = modeloTabla
                            .getValueAt(
                                    fila,
                                    5
                            )
                            .toString();

            if ("Activo".equalsIgnoreCase(
                    estado
            )) {

                modeloTabla.setValueAt(
                        "Inactivo",
                        fila,
                        5
                );

            } else {

                modeloTabla.setValueAt(
                        "Activo",
                        fila,
                        5
                );
            }

            actualizarResumen();
            actualizarDetalleSeleccionado();

        });

        //======================================================
        // ACTUALIZAR
        //======================================================
        btnActualizar.addActionListener(e -> {

            cargarDatosPrueba();

            limpiarDetalle();

        });
    }

    //==========================================================
    // DETALLE SELECCIONADO
    //==========================================================
    private void actualizarDetalleSeleccionado() {

        int fila
                = tablaProveedores.getSelectedRow();

        if (fila == -1) {

            limpiarDetalle();
            return;
        }

        lblDetalleProveedor.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                2
                        )
                        .toString()
        );

        lblDetalleCuit.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString()
        );

        lblDetalleUltimaCompra.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                6
                        )
                        .toString()
        );

        // Datos simulados por ahora
        if (fila == 0) {

            lblDetalleSaldo.setText(
                    "$ 185.400"
            );

            lblDetalleTotalComprado.setText(
                    "$ 2.850.000"
            );

        } else if (fila == 1) {

            lblDetalleSaldo.setText(
                    "$ 0"
            );

            lblDetalleTotalComprado.setText(
                    "$ 4.200.000"
            );

        } else {

            lblDetalleSaldo.setText(
                    "$ 75.000"
            );

            lblDetalleTotalComprado.setText(
                    "$ 980.000"
            );
        }
    }

    //==========================================================
    // LIMPIAR DETALLE
    //==========================================================
    private void limpiarDetalle() {

        lblDetalleProveedor.setText("-");
        lblDetalleCuit.setText("-");
        lblDetalleSaldo.setText("$ 0,00");
        lblDetalleUltimaCompra.setText("-");
        lblDetalleTotalComprado.setText("$ 0,00");
    }

    //==========================================================
    // SELECCION
    //==========================================================
    private boolean hayProveedorSeleccionado() {

        if (tablaProveedores.getSelectedRow() == -1) {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Seleccione un proveedor.",
                            "Proveedores",
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );

            return false;
        }

        return true;
    }

    //==========================================================
    // RESUMEN
    //==========================================================
    private void actualizarResumen() {

        int total
                = modeloTabla.getRowCount();

        int activos = 0;

        for (int i = 0;
                i < modeloTabla.getRowCount();
                i++) {

            String estado
                    = modeloTabla
                            .getValueAt(
                                    i,
                                    5
                            )
                            .toString();

            if ("Activo".equalsIgnoreCase(
                    estado
            )) {

                activos++;
            }
        }

        /*
         * Por ahora simulamos proveedores con deuda.
         * Después sale de cuenta corriente.
         */
        int conDeuda
                = Math.min(
                        2,
                        total
                );

        lblTotalProveedores.setText(
                String.valueOf(total)
        );

        lblProveedoresActivos.setText(
                String.valueOf(activos)
        );

        lblProveedoresDeuda.setText(
                String.valueOf(conDeuda)
        );
    }

    //==========================================================
    // VALOR RESUMEN
    //==========================================================
    private JLabel crearValorResumen(
            String texto,
            Color color) {

        JLabel label
                = new JLabel(texto);

        label.setForeground(color);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
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

        JButton boton
                = new JButton(texto);

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
