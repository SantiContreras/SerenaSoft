/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Diseños.BotonRedondeado;
import Diseños.PanelRedondeado;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import rojeru_san.rsbutton.RSButtonRound;
import rojerusan.RSComboBox;
import rojerusan.RSPopuMenu;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import Diseños.TablaEstilo;
import java.awt.FlowLayout;

/**
 *
 * @author santi
 */
public class PanelVentas extends javax.swing.JPanel {

    private JTable tabla;
    private TextFieldRedondeado txtCodigo;

    private RSComboBox comboPago;

    private RSButtonRound btnAgregar;

    private RSButtonRound btnEliminar;

    private RSButtonRound btnCobrar;

    private JLabel lblProducto;

    private RSButtonRound btnManual;

    private RSButtonRound btnPesable;

    private RSButtonRound btnScan;

    private TextFieldRedondeado txtCuit;

    private PanelRedondeado panelTotal;
    private PanelRedondeado panelPago;
    private PanelRedondeado panelAcciones;

    //=====================================================
// PANEL CLIENTE / FACTURACIÓN
//=====================================================
    private JPanel panelCliente;

    private JLabel lblCliente;
    private JLabel lblBuscar;
    private JLabel lblDocumento;
    private JLabel lblTipoCliente;
    private JLabel lblClienteTitulo;

    private TextFieldRedondeado txtCliente;
    private TextFieldRedondeado txtDocumento;

    private JButton btnBuscarCliente;

    private JRadioButton rbConsumidorFinal;
    private JRadioButton rbResponsableInscripto;
    private JRadioButton rbMonotributista;
    private JRadioButton rbMayorista;

    //condicion del cliente 
    private RSComboBox comboCondicionIVA;
    private RSComboBox comboTipoCliente;
    private RSComboBox comboComprobante;

    private ButtonGroup grupoTipoCliente;

    Color AZUL_PRINCIPAL = new Color(20, 70, 140);
    Color AZUL_HOVER = new Color(30, 90, 170);
    Color AZUL_PRESION = new Color(15, 55, 110);

    Color VERDE_OK = new Color(25, 135, 84);
    Color ROJO_ERROR = new Color(220, 53, 69);

    Color BORDE_PANEL = new Color(30, 80, 160);
    Color TITULO_PANEL = new Color(25, 55, 110);

    //titulos de la tabla
    Color HEADER = new Color(25, 70, 150);
    Color HEADER_BORDE = new Color(15, 55, 120);

    public PanelVentas() {
        initComponents();

        armarLayout();

        armarSubPanelProducto();

        armarSubPanelCliente();

        armarTabla();

        configurarEventosProductos();

    }

    private void alinearIzquierda(JComponent comp) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
    }

    private void configurarEventosProductos() {

        //==========================================================
        // AGREGAR PRODUCTO EXISTENTE
        //==========================================================
        btnAgregar.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoBuscarProducto dialogo
                    = new Dialogos.DialogoBuscarProducto(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isSeleccionado()) {

                System.out.println(
                        "Producto seleccionado:"
                );

                System.out.println(
                        "Código: "
                        + dialogo.getCodigoSeleccionado()
                );

                System.out.println(
                        "Producto: "
                        + dialogo.getProductoSeleccionado()
                );

                System.out.println(
                        "Precio: "
                        + dialogo.getPrecioSeleccionado()
                );

                /*
             * MÁS ADELANTE:
             *
             * agregarProductoAVenta(...)
             *
             * Por ahora solamente comprobamos
             * que el diálogo devuelve bien los datos.
                 */
            }
        });

        //==========================================================
        // PRODUCTO PESABLE
        //==========================================================
        btnPesable.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoProductoPesable dialogo
                    = new Dialogos.DialogoProductoPesable(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println(
                        "Producto pesable:"
                );

                System.out.println(
                        "Código: "
                        + dialogo.getCodigoProducto()
                );

                System.out.println(
                        "Producto: "
                        + dialogo.getNombreProducto()
                );

                System.out.println(
                        "Peso: "
                        + dialogo.getPeso()
                );

                System.out.println(
                        "Precio Kg: "
                        + dialogo.getPrecioKg()
                );

                System.out.println(
                        "Total: "
                        + dialogo.getTotal()
                );

                /*
             * MÁS ADELANTE:
             *
             * agregarProductoPesableAVenta(...)
                 */
            }
        });

        //==========================================================
        // VENTA / CONCEPTO MANUAL
        //==========================================================
        btnManual.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoVentaManual dialogo
                    = new Dialogos.DialogoVentaManual(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                System.out.println(
                        "Concepto manual:"
                );

                System.out.println(
                        "Descripción: "
                        + dialogo.getDescripcion()
                );

                System.out.println(
                        "Cantidad: "
                        + dialogo.getCantidad()
                );

                System.out.println(
                        "Precio: "
                        + dialogo.getPrecio()
                );

                System.out.println(
                        "Subtotal: "
                        + dialogo.getSubtotal()
                );

                /*
             * MÁS ADELANTE:
             *
             * agregarConceptoManualAVenta(...)
                 */
            }
        });

        //==========================================================
        // ELIMINAR
        //==========================================================
        btnEliminar.addActionListener(e -> {

            int fila
                    = tabla.getSelectedRow();

            if (fila == -1) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un producto de la venta.",
                        "Eliminar Producto",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String producto
                    = tabla.getValueAt(
                            fila,
                            1
                    ).toString();

            int opcion
                    = javax.swing.JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea quitar de la venta:\n\n"
                            + producto
                            + "?",
                            "Eliminar Producto",
                            javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE
                    );

            if (opcion
                    == javax.swing.JOptionPane.YES_OPTION) {

                /*
             * MÁS ADELANTE:
             *
             * DefaultTableModel modelo =
             *      (DefaultTableModel) tabla.getModel();
             *
             * modelo.removeRow(fila);
             *
             * recalcularVenta();
                 */
                System.out.println(
                        "Eliminar: "
                        + producto
                );
            }
        });
    }

    private void armarSubPanelProducto() {

        subPanelProducto.removeAll();

        subPanelProducto.setLayout(
                new BoxLayout(
                        subPanelProducto,
                        BoxLayout.Y_AXIS
                )
        );

        subPanelProducto.setPreferredSize(
                new Dimension(0, 190)
        );

        subPanelProducto.setOpaque(false);

        subPanelProducto.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel("PRODUCTO");

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        titulo.setForeground(
                TITULO_PANEL
        );

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        subPanelProducto.add(titulo);

        subPanelProducto.add(
                Box.createVerticalStrut(5)
        );

        //====================================================
        // SUBTITULO
        //====================================================
        JLabel subtitulo
                = new JLabel(
                        "Ingrese, busque o escanee el código del producto"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                Color.GRAY
        );

        subtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        subPanelProducto.add(subtitulo);

        subPanelProducto.add(
                Box.createVerticalStrut(8)
        );

        //====================================================
        // FILA CODIGO
        //====================================================
        JPanel filaCodigo
                = new JPanel();

        filaCodigo.setOpaque(false);

        filaCodigo.setLayout(
                new BoxLayout(
                        filaCodigo,
                        BoxLayout.X_AXIS
                )
        );

        filaCodigo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        txtCodigo
                = new TextFieldRedondeado();

        txtCodigo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        txtCodigo.setText(
                "LAC009"
        );

        txtCodigo.setPreferredSize(
                new Dimension(
                        320,
                        40
                )
        );

        txtCodigo.setMaximumSize(
                new Dimension(
                        420,
                        40
                )
        );

        btnScan
                = new RSButtonRound();

        btnScan.setPreferredSize(
                new Dimension(
                        58,
                        40
                )
        );

        btnScan.setMaximumSize(
                new Dimension(
                        58,
                        40
                )
        );

        btnScan.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/barcode.png"
                        )
                )
        );

        filaCodigo.add(txtCodigo);

        filaCodigo.add(
                Box.createHorizontalStrut(10)
        );

        filaCodigo.add(btnScan);

        filaCodigo.add(
                Box.createHorizontalGlue()
        );

        subPanelProducto.add(filaCodigo);

        subPanelProducto.add(
                Box.createVerticalStrut(6)
        );

        //====================================================
        // PRODUCTO ENCONTRADO
        //====================================================
        lblProducto
                = new JLabel(
                        "Jamón La Piamontesa x kg"
                );

        lblProducto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblProducto.setForeground(
                new Color(
                        0,
                        140,
                        0
                )
        );

        subPanelProducto.add(lblProducto);

        subPanelProducto.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // BOTONES DE PRODUCTO
        //====================================================
        JPanel filaBotones
                = new JPanel();

        filaBotones.setOpaque(false);

        filaBotones.setLayout(
                new BoxLayout(
                        filaBotones,
                        BoxLayout.X_AXIS
                )
        );

        filaBotones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        Dimension sizeBtn
                = new Dimension(
                        140,
                        40
                );

        btnPesable
                = new RSButtonRound();

        btnAgregar
                = new RSButtonRound();

        btnManual
                = new RSButtonRound();

        btnEliminar
                = new RSButtonRound();

        btnPesable.setText("Pesable");
        btnAgregar.setText("Agregar");
        btnManual.setText("Manual");
        btnEliminar.setText("Eliminar");

        btnPesable.setBackground(AZUL_PRESION);
        btnAgregar.setBackground(AZUL_PRESION);
        btnManual.setBackground(AZUL_PRESION);
        btnEliminar.setBackground(AZUL_PRESION);

        btnPesable.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/balanza1.png"
                        )
                )
        );

        btnAgregar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/agregar5.png"
                        )
                )
        );

        btnManual.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/manual3.png"
                        )
                )
        );

        btnEliminar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/eliminar4.png"
                        )
                )
        );

        RSButtonRound[] botones = {
            btnPesable,
            btnAgregar,
            btnManual,
            btnEliminar
        };

        for (RSButtonRound boton : botones) {

            boton.setPreferredSize(
                    sizeBtn
            );

            boton.setMaximumSize(
                    sizeBtn
            );

            boton.setHorizontalTextPosition(
                    SwingConstants.RIGHT
            );

            boton.setIconTextGap(8);

            filaBotones.add(boton);

            filaBotones.add(
                    Box.createHorizontalStrut(10)
            );
        }

        filaBotones.add(
                Box.createHorizontalGlue()
        );

        alinearIzquierda(titulo);
        alinearIzquierda(subtitulo);
        alinearIzquierda(filaCodigo);
        alinearIzquierda(lblProducto);
        alinearIzquierda(filaBotones);

        subPanelProducto.add(
                filaBotones
        );

        subPanelProducto.revalidate();
        subPanelProducto.repaint();
    }

    private void armarSubPanelCliente() {

        subPanelClientes.removeAll();

        subPanelClientes.setLayout(null);
        subPanelClientes.setOpaque(false);

        //=====================================================
        // TITULO
        //=====================================================
        lblClienteTitulo
                = new JLabel(
                        "CLIENTE / FACTURACIÓN"
                );

        lblClienteTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lblClienteTitulo.setForeground(
                new Color(
                        20,
                        55,
                        120
                )
        );

        lblClienteTitulo.setBounds(
                20,
                10,
                320,
                25
        );

        //=====================================================
        // CLIENTE
        //=====================================================
        lblCliente
                = new JLabel(
                        "Cliente"
                );

        lblCliente.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblCliente.setBounds(
                20,
                42,
                90,
                20
        );

        txtCliente
                = new TextFieldRedondeado();

        txtCliente.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtCliente.setBounds(
                20,
                65,
                300,
                34
        );

        txtCliente.setEditable(false);

        txtCliente.setText(
                "Consumidor Final"
        );

        btnBuscarCliente
                = new RSButtonRound();

        btnBuscarCliente.setText(
                "..."
        );

        btnBuscarCliente.setBounds(
                330,
                65,
                45,
                34
        );

        //=====================================================
        // DOCUMENTO
        //=====================================================
        lblDocumento
                = new JLabel(
                        "CUIT / DNI"
                );

        lblDocumento.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblDocumento.setBounds(
                20,
                110,
                100,
                20
        );

        txtDocumento
                = new TextFieldRedondeado();

        txtDocumento.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtDocumento.setBounds(
                20,
                133,
                220,
                32
        );

        //=====================================================
        // CONDICION IVA
        //=====================================================
        JLabel lblCondicionIVA
                = new JLabel(
                        "Condición IVA"
                );

        lblCondicionIVA.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblCondicionIVA.setBounds(
                430,
                42,
                130,
                20
        );

        comboCondicionIVA
                = new RSComboBox();

        comboCondicionIVA.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Consumidor Final",
                            "Responsable Inscripto",
                            "Monotributista",
                            "Exento"
                        }
                )
        );

        comboCondicionIVA.setBounds(
                430,
                65,
                200,
                32
        );

        //=====================================================
        // TIPO DE CLIENTE
        //=====================================================
        JLabel lblTipoCliente
                = new JLabel(
                        "Tipo de Cliente"
                );

        lblTipoCliente.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblTipoCliente.setBounds(
                650,
                42,
                130,
                20
        );

        comboTipoCliente
                = new RSComboBox();

        comboTipoCliente.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Minorista",
                            "Mayorista",
                            "Cuenta Corriente"
                        }
                )
        );

        comboTipoCliente.setBounds(
                650,
                65,
                180,
                32
        );

        //=====================================================
        // COMPROBANTE
        //=====================================================
        JLabel lblComprobante
                = new JLabel(
                        "Comprobante"
                );

        lblComprobante.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblComprobante.setBounds(
                430,
                110,
                130,
                20
        );

        comboComprobante
                = new RSComboBox();

        comboComprobante.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Ticket",
                            "Factura A",
                            "Factura B",
                            "Factura C"
                        }
                )
        );

        comboComprobante.setBounds(
                430,
                133,
                200,
                32
        );

        //=====================================================
        // ESTADO FACTURACION
        //=====================================================
        JLabel lblEstadoFacturacion
                = new JLabel(
                        "Sin facturación electrónica"
                );

        lblEstadoFacturacion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblEstadoFacturacion.setForeground(
                new Color(
                        120,
                        120,
                        120
                )
        );

        lblEstadoFacturacion.setBounds(
                650,
                135,
                220,
                25
        );

        //=====================================================
        // AGREGAR
        //=====================================================
        subPanelClientes.add(
                lblClienteTitulo
        );

        subPanelClientes.add(
                lblCliente
        );

        subPanelClientes.add(
                txtCliente
        );

        subPanelClientes.add(
                btnBuscarCliente
        );

        subPanelClientes.add(
                lblDocumento
        );

        subPanelClientes.add(
                txtDocumento
        );

        subPanelClientes.add(
                lblCondicionIVA
        );

        subPanelClientes.add(
                comboCondicionIVA
        );

        subPanelClientes.add(
                lblTipoCliente
        );

        subPanelClientes.add(
                comboTipoCliente
        );

        subPanelClientes.add(
                lblComprobante
        );

        subPanelClientes.add(
                comboComprobante
        );

        subPanelClientes.add(
                lblEstadoFacturacion
        );

        //=====================================================
        // EVENTO BUSCAR CLIENTE
        //=====================================================
        btnBuscarCliente.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoBuscarCliente dialogo
                    = new Dialogos.DialogoBuscarCliente(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isSeleccionado()) {

                txtCliente.setText(
                        dialogo.getNombreSeleccionado()
                );

                txtDocumento.setText(
                        dialogo.getDocumentoSeleccionado()
                );

                comboCondicionIVA.setSelectedItem(
                        dialogo.getCondicionIVASeleccionada()
                );

                comboTipoCliente.setSelectedItem(
                        dialogo.getTipoClienteSeleccionado()
                );
            }
        });

        subPanelClientes.repaint();
        subPanelClientes.revalidate();
    }

    private void armarTabla() {

        //==========================================================
        // MODELO
        //==========================================================
        String columnas[] = {
            "ID",
            "PRODUCTO",
            "CANT.",
            "PRECIO",
            "SUBTOTAL"
        };

        DefaultTableModel modelo
                = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tabla = new JTable(modelo);

        //==========================================================
        // CONFIGURACION GENERAL TABLA
        //==========================================================
        tabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tabla.setRowHeight(32);

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        tabla.setGridColor(
                new Color(
                        230,
                        234,
                        240
                )
        );

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

        tabla.setBackground(
                Color.WHITE
        );

        tabla.setForeground(
                new Color(
                        40,
                        40,
                        40
                )
        );

        tabla.setFillsViewportHeight(true);

        //==========================================================
        // HEADER
        //==========================================================
        JTableHeader header
                = tabla.getTableHeader();

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        38
                )
        );

        header.setReorderingAllowed(false);

        //==========================================================
        // TAMAÑO COLUMNAS
        //==========================================================
        tabla.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(430);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        //==========================================================
        // ALINEACION COLUMNAS
        //==========================================================
        DefaultTableCellRenderer centro
                = new DefaultTableCellRenderer();

        centro.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        DefaultTableCellRenderer derecha
                = new DefaultTableCellRenderer();

        derecha.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        tabla.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centro);

        tabla.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centro);

        tabla.getColumnModel()
                .getColumn(3)
                .setCellRenderer(derecha);

        tabla.getColumnModel()
                .getColumn(4)
                .setCellRenderer(derecha);

        //==========================================================
        // BARRA INFORMACION DE VENTA
        //==========================================================
        JPanel panelInfoVenta
                = new JPanel(
                        new BorderLayout()
                );

        panelInfoVenta.setBackground(
                Color.WHITE
        );

        panelInfoVenta.setPreferredSize(
                new Dimension(
                        0,
                        44
                )
        );

        panelInfoVenta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(
                                        220,
                                        225,
                                        232
                                )
                        ),
                        new EmptyBorder(
                                5,
                                15,
                                5,
                                15
                        )
                )
        );

        //==========================================================
        // NUMERO DE VENTA
        //==========================================================
        JPanel panelNumero
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                3
                        )
                );

        panelNumero.setOpaque(false);

        JLabel lblVenta
                = new JLabel(
                        "Venta N°"
                );

        lblVenta.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblVenta.setForeground(
                new Color(
                        100,
                        105,
                        115
                )
        );

        JLabel lblNumeroVenta
                = new JLabel(
                        "00000125"
                );

        lblNumeroVenta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblNumeroVenta.setForeground(
                TITULO_PANEL
        );

        panelNumero.add(lblVenta);
        panelNumero.add(lblNumeroVenta);

        //==========================================================
        // ITEMS Y UNIDADES
        //==========================================================
        JPanel panelEstadisticas
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                18,
                                3
                        )
                );

        panelEstadisticas.setOpaque(false);

        JLabel lblItems
                = new JLabel(
                        "Items: 5"
                );

        lblItems.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblItems.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        JLabel lblUnidades
                = new JLabel(
                        "Unidades: 7,75"
                );

        lblUnidades.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblUnidades.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        panelEstadisticas.add(lblItems);
        panelEstadisticas.add(lblUnidades);

        panelInfoVenta.add(
                panelNumero,
                BorderLayout.WEST
        );

        panelInfoVenta.add(
                panelEstadisticas,
                BorderLayout.EAST
        );

        //==========================================================
        // SCROLL TABLA
        //==========================================================
        JScrollPane scroll
                = new JScrollPane(
                        tabla
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.getViewport()
                .setBackground(
                        Color.WHITE
                );

        //==========================================================
        // CONTENEDOR COMPLETO
        //==========================================================
        JPanel contenedorTabla
                = new JPanel(
                        new BorderLayout()
                );

        contenedorTabla.setBackground(
                Color.WHITE
        );

        contenedorTabla.add(
                panelInfoVenta,
                BorderLayout.NORTH
        );

        contenedorTabla.add(
                scroll,
                BorderLayout.CENTER
        );

        subPanelDetalleVenta.removeAll();

        subPanelDetalleVenta.setLayout(
                new BorderLayout()
        );

        subPanelDetalleVenta.add(
                contenedorTabla,
                BorderLayout.CENTER
        );

        //==========================================================
        // DATOS DE PRUEBA
        //==========================================================
        modelo.addRow(
                new Object[]{
                    1,
                    "Jamón La Piamontesa x kg",
                    "1.00",
                    "$ 12.000",
                    "$ 12.000"
                }
        );

        modelo.addRow(
                new Object[]{
                    2,
                    "Papel Higiénico Elite x4",
                    "2.00",
                    "$ 3.500",
                    "$ 7.000"
                }
        );

        modelo.addRow(
                new Object[]{
                    3,
                    "Aceite Natura 900 ml",
                    "1.00",
                    "$ 8.500",
                    "$ 8.500"
                }
        );

        modelo.addRow(
                new Object[]{
                    4,
                    "Coca Cola 2.25 L",
                    "3.00",
                    "$ 2.600",
                    "$ 7.800"
                }
        );

        modelo.addRow(
                new Object[]{
                    5,
                    "Queso Cremoso Punta del Agua",
                    "0.750",
                    "$ 14.000",
                    "$ 10.500"
                }
        );

        //==========================================================
        // ESTILO SERENA SOFT
        //==========================================================
        TablaEstilo.aplicar(
                tabla,
                scroll
        );

        subPanelDetalleVenta.revalidate();
        subPanelDetalleVenta.repaint();
    }

    private void armarPanelDerecho(JPanel panelDerecho) {

        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        javax.swing.JLabel lblTotal = new javax.swing.JLabel("$ 2.000.000");
        lblTotal.setFont(new java.awt.Font("Arial", 1, 28));

        javax.swing.JComboBox<String> comboPago
                = new javax.swing.JComboBox<>(new String[]{"Efectivo", "Tarjeta"});

        javax.swing.JTextField txtPago = new javax.swing.JTextField();

        javax.swing.JButton btnCobrar = new javax.swing.JButton("COBRAR");

        panelDerecho.add(new javax.swing.JLabel("TOTAL"));
        panelDerecho.add(lblTotal);
        panelDerecho.add(new javax.swing.JLabel("Forma de pago"));
        panelDerecho.add(comboPago);
        panelDerecho.add(new javax.swing.JLabel("Importe"));
        panelDerecho.add(txtPago);
        panelDerecho.add(btnCobrar);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelCentralVentas = new javax.swing.JPanel();
        subPanelProducto = new Diseños.PanelRedondeado();
        subPanelClientes = new Diseños.PanelRedondeado();
        subPanelDetalleVenta = new Diseños.PanelRedondeado();

        setBackground(new java.awt.Color(51, 51, 51));
        setLayout(new java.awt.BorderLayout());

        PanelCentralVentas.setLayout(new javax.swing.BoxLayout(PanelCentralVentas, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout subPanelProductoLayout = new javax.swing.GroupLayout(subPanelProducto);
        subPanelProducto.setLayout(subPanelProductoLayout);
        subPanelProductoLayout.setHorizontalGroup(
            subPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelProductoLayout.setVerticalGroup(
            subPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelProducto);

        javax.swing.GroupLayout subPanelClientesLayout = new javax.swing.GroupLayout(subPanelClientes);
        subPanelClientes.setLayout(subPanelClientesLayout);
        subPanelClientesLayout.setHorizontalGroup(
            subPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelClientesLayout.setVerticalGroup(
            subPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelClientes);

        javax.swing.GroupLayout subPanelDetalleVentaLayout = new javax.swing.GroupLayout(subPanelDetalleVenta);
        subPanelDetalleVenta.setLayout(subPanelDetalleVentaLayout);
        subPanelDetalleVentaLayout.setHorizontalGroup(
            subPanelDetalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelDetalleVentaLayout.setVerticalGroup(
            subPanelDetalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelDetalleVenta);

        add(PanelCentralVentas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentralVentas;
    private Diseños.PanelRedondeado subPanelClientes;
    private Diseños.PanelRedondeado subPanelDetalleVenta;
    private Diseños.PanelRedondeado subPanelProducto;
    // End of variables declaration//GEN-END:variables

    private void armarLayout() {

        removeAll();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        //=========================================
        // PANEL IZQUIERDO
        //=========================================
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 12));
        panelIzquierdo.setOpaque(false);

        // Alturas
        subPanelProducto.setPreferredSize(new Dimension(0, 190));
        subPanelClientes.setPreferredSize(new Dimension(0, 170));

        panelIzquierdo.add(subPanelProducto, BorderLayout.NORTH);
        panelIzquierdo.add(subPanelDetalleVenta, BorderLayout.CENTER);
        panelIzquierdo.add(subPanelClientes, BorderLayout.SOUTH);

        //=========================================
        // PANEL DERECHO
        //=========================================
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 12));
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(330, 0));

        panelTotal = new PanelRedondeado();
        panelPago = new PanelRedondeado();
        panelAcciones = new PanelRedondeado();
        panelTotal.setPreferredSize(
                new Dimension(
                        330,
                        175
                )
        );

        panelPago.setPreferredSize(
                new Dimension(
                        330,
                        285
                )
        );

        panelAcciones.setPreferredSize(
                new Dimension(
                        330,
                        120
                )
        );

        armarPanelTotal(panelTotal);
        armarPanelPago(panelPago);
        armarPanelAcciones(panelAcciones);

        panelDerecho.add(panelTotal, BorderLayout.NORTH);
        panelDerecho.add(panelPago, BorderLayout.CENTER);
        panelDerecho.add(panelAcciones, BorderLayout.SOUTH);

        //=========================================
        // AGREGAR AL PANEL PRINCIPAL
        //=========================================
        add(panelIzquierdo, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void armarPanelPago(JPanel panel) {

        panel.removeAll();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );

        panel.setOpaque(false);

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel(
                        "PAGO"
                );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        titulo.setForeground(
                TITULO_PANEL
        );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // FORMA DE PAGO
        //====================================================
        JPanel filaForma
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaForma.setOpaque(false);

        filaForma.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );

        JLabel lblForma
                = new JLabel(
                        "Forma:"
                );

        lblForma.setForeground(
                AZUL_PRESION
        );

        // IMPORTANTE:
        // usamos el atributo de la clase
        comboPago
                = new RSComboBox();

        comboPago.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Efectivo",
                            "Tarjeta",
                            "Transferencia",
                            "Mercado Pago"
                        }
                )
        );

        comboPago.setPreferredSize(
                new Dimension(
                        190,
                        32
                )
        );

        filaForma.add(
                lblForma,
                BorderLayout.WEST
        );

        filaForma.add(
                comboPago,
                BorderLayout.CENTER
        );

        panel.add(filaForma);

        panel.add(
                Box.createVerticalStrut(9)
        );

        //====================================================
        // RECIBE
        //====================================================
        JPanel filaImporte
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaImporte.setOpaque(false);

        filaImporte.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        JLabel lblImporte
                = new JLabel(
                        "Recibe:"
                );

        lblImporte.setForeground(
                AZUL_PRESION
        );

        JTextField txtImporte
                = new JTextField();

        txtImporte.setHorizontalAlignment(
                JTextField.RIGHT
        );

        txtImporte.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        filaImporte.add(
                lblImporte,
                BorderLayout.WEST
        );

        filaImporte.add(
                txtImporte,
                BorderLayout.CENTER
        );

        panel.add(filaImporte);

        panel.add(
                Box.createVerticalStrut(9)
        );

        //====================================================
        // CAMBIO
        //====================================================
        JPanel filaCambio
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaCambio.setOpaque(false);

        filaCambio.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        JLabel lblCambio
                = new JLabel(
                        "Cambio:"
                );

        lblCambio.setForeground(
                AZUL_PRESION
        );

        JLabel cambio
                = new JLabel(
                        "$ 0,00"
                );

        cambio.setOpaque(true);

        cambio.setBackground(
                new Color(
                        228,
                        245,
                        228
                )
        );

        cambio.setForeground(
                new Color(
                        0,
                        130,
                        60
                )
        );

        cambio.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        cambio.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        filaCambio.add(
                lblCambio,
                BorderLayout.WEST
        );

        filaCambio.add(
                cambio,
                BorderLayout.CENTER
        );

        panel.add(filaCambio);

        panel.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // CALCULAR CAMBIO
        //====================================================
        RSButtonRound btnCalcular
                = new RSButtonRound();

        btnCalcular.setText(
                "CALCULAR CAMBIO"
        );

        btnCalcular.setBackground(
                new Color(
                        15,
                        125,
                        195
                )
        );

        btnCalcular.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnCalcular.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        panel.add(btnCalcular);

        panel.add(
                Box.createVerticalStrut(8)
        );

        //====================================================
        // COBRAR
        //====================================================
        btnCobrar
                = new RSButtonRound();

        btnCobrar.setText(
                "COBRAR   $ 0,00"
        );

        btnCobrar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        btnCobrar.setBackground(
                VERDE_OK
        );

        btnCobrar.setForeground(
                Color.WHITE
        );

        btnCobrar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/cobrar1.png"
                        )
                )
        );

        btnCobrar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnCobrar.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        panel.add(btnCobrar);

        btnCobrar.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoConfirmarVenta dialogo
                    = new Dialogos.DialogoConfirmarVenta(
                            ventana,
                            txtCliente.getText(),
                            txtDocumento.getText(),
                            comboCondicionIVA
                                    .getSelectedItem()
                                    .toString(),
                            comboComprobante
                                    .getSelectedItem()
                                    .toString(),
                            comboPago
                                    .getSelectedItem()
                                    .toString(),
                            38800.00, // subtotal temporal

                            0.00, // descuento temporal

                            38800.00, // total temporal

                            40000.00, // recibe temporal

                            1200.00 // cambio temporal
                    );

            dialogo.setVisible(true);

            if (dialogo.isVentaConfirmada()) {

                Dialogos.DialogoVentaFinalizada finalizada
                        = new Dialogos.DialogoVentaFinalizada(
                                ventana,
                                "00000125",
                                comboComprobante
                                        .getSelectedItem()
                                        .toString(),
                                comboPago
                                        .getSelectedItem()
                                        .toString(),
                                38800.00,
                                40000.00,
                                1200.00,
                                "Santiago"
                        );

                finalizada.setVisible(true);

                if (finalizada.isNuevaVenta()) {

                    System.out.println(
                            "Preparar nueva venta..."
                    );
                }
            }

        });

        panel.add(
                Box.createVerticalGlue()
        );

        panel.revalidate();
        panel.repaint();
    }

    private void armarPanelAcciones(JPanel panel) {

        panel.removeAll();

        panel.setLayout(
                new GridLayout(
                        2,
                        2,
                        10,
                        10
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        panel.add(
                crearBotonAccion(
                        "DESCUENTO"
                )
        );

        panel.add(
                crearBotonAccion(
                        "PENDIENTE"
                )
        );

        panel.add(
                crearBotonAccion(
                        "OBSERVACIÓN"
                )
        );

        panel.add(
                crearBotonAccion(
                        "LIMPIAR"
                )
        );

        panel.revalidate();
        panel.repaint();
    }

    private RSButtonRound crearBotonAccion(String texto) {

        RSButtonRound b = new RSButtonRound();

        b.setPreferredSize(new Dimension(135, 42));

        b.setText(texto);

        b.setBackground(new Color(17, 72, 148));

        b.setForeground(Color.WHITE);

        return b;

    }

    private void armarPanelTotal(JPanel panel) {

        panel.removeAll();

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
                        15,
                        20
                )
        );

        panel.setOpaque(false);

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel(
                        "TOTAL DE LA VENTA"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        titulo.setForeground(
                new Color(
                        18,
                        120,
                        45
                )
        );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //====================================================
        // TOTAL PRINCIPAL
        //====================================================
        JLabel lblTotal
                = new JLabel(
                        "$ 2.2000.000"
                );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        40
                )
        );

        lblTotal.setForeground(
                new Color(
                        0,
                        140,
                        70
                )
        );

        lblTotal.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //====================================================
        // SUBTOTAL
        //====================================================
        JPanel filaSubtotal
                = crearFilaImporte(
                        "Subtotal:",
                        "$ 0,00",
                        Color.BLACK
                );

        //====================================================
        // DESCUENTO
        //====================================================
        JPanel filaDescuento
                = crearFilaImporte(
                        "Descuento:",
                        "$ 0,00",
                        ROJO_ERROR
                );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(lblTotal);

        panel.add(
                Box.createVerticalStrut(10)
        );

        panel.add(filaSubtotal);

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(filaDescuento);

        panel.revalidate();
        panel.repaint();
    }

    private JPanel crearFilaImporte(
            String texto,
            String valor,
            Color colorValor) {

        JPanel fila
                = new JPanel(
                        new BorderLayout()
                );

        fila.setOpaque(false);

        fila.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        24
                )
        );

        JLabel titulo
                = new JLabel(texto);

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel importe
                = new JLabel(valor);

        importe.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        importe.setForeground(
                colorValor
        );

        fila.add(
                titulo,
                BorderLayout.WEST
        );

        fila.add(
                importe,
                BorderLayout.EAST
        );

        return fila;
    }

}
