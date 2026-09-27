package Dialogos;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
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

        inicializarComponentes();

        construirDialogo();

        cargarDatosProveedor(
                razonSocial,
                cuit,
                telefono,
                estado
        );

        cargarDatosPrueba();

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
         EstiloBotones.corregirBotones(
                getContentPane()
    );
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

        return tabla;
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
                Color.WHITE
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
                        "FICHA DEL PROVEEDOR"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        JLabel lblSubtitulo
                = new JLabel(
                        "Información comercial, compras, facturas y cuenta corriente"
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

        //======================================================
        // PESTAÑAS
        //======================================================
        JTabbedPane tabs
                = new JTabbedPane();

        tabs.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabs.addTab(
                "Datos Generales",
                crearPanelDatosGenerales()
        );

        tabs.addTab(
                "Compras",
                crearPanelTabla(
                        tablaCompras
                )
        );

        tabs.addTab(
                "Facturas",
                crearPanelTabla(
                        tablaFacturas
                )
        );

        tabs.addTab(
                "Cuenta Corriente",
                crearPanelTabla(
                        tablaCuentaCorriente
                )
        );

        tabs.addTab(
                "Pagos",
                crearPanelTabla(
                        tablaPagos
                )
        );

        centro.add(
                tabs,
                BorderLayout.CENTER
        );

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

        panel.setBackground(
                Color.WHITE
        );

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

        // Datos simulados
        lblCondicionIva.setText(
                "Responsable Inscripto"
        );

        lblDireccion.setText(
                "Av. Principal 123"
        );

        lblLocalidad.setText(
                "Resistencia"
        );

        lblProvincia.setText(
                "Chaco"
        );

        lblEmail.setText(
                "contacto@proveedor.com"
        );

        lblContacto.setText(
                "Juan Pérez"
        );
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        //======================================================
        // RESUMEN
        //==========================================================
        lblTotalComprado.setText(
                "$ 2.850.000"
        );

        lblTotalPagado.setText(
                "$ 2.664.600"
        );

        lblSaldoPendiente.setText(
                "$ 185.400"
        );

        lblUltimaCompra.setText(
                "21/08/2026"
        );

        //======================================================
        // COMPRAS
        //==========================================================
        modeloCompras.setRowCount(0);

        modeloCompras.addRow(
                new Object[]{
                    1,
                    "21/08/2026",
                    "0004-00012345",
                    30,
                    "$ 185.400",
                    "Pendiente"
                }
        );

        modeloCompras.addRow(
                new Object[]{
                    2,
                    "10/08/2026",
                    "0004-00012001",
                    18,
                    "$ 120.000",
                    "Pagada"
                }
        );

        //======================================================
        // FACTURAS
        //==========================================================
        modeloFacturas.setRowCount(0);

        modeloFacturas.addRow(
                new Object[]{
                    "0004-00012345",
                    "21/08/2026",
                    "20/09/2026",
                    "$ 185.400",
                    "$ 0",
                    "$ 185.400",
                    "Pendiente"
                }
        );

        modeloFacturas.addRow(
                new Object[]{
                    "0004-00012001",
                    "10/08/2026",
                    "10/09/2026",
                    "$ 120.000",
                    "$ 120.000",
                    "$ 0",
                    "Pagada"
                }
        );

        //======================================================
        // CUENTA
        //==========================================================
        modeloCuenta.setRowCount(0);

        modeloCuenta.addRow(
                new Object[]{
                    "21/08/2026",
                    "Compra",
                    "0004-00012345",
                    "$ 185.400",
                    "$ 0",
                    "$ 185.400"
                }
        );

        modeloCuenta.addRow(
                new Object[]{
                    "15/08/2026",
                    "Pago",
                    "Transferencia",
                    "$ 0",
                    "$ 100.000",
                    "$ 0"
                }
        );

        //======================================================
        // PAGOS
        //==========================================================
        modeloPagos.setRowCount(0);

        modeloPagos.addRow(
                new Object[]{
                    "15/08/2026",
                    "$ 100.000",
                    "Transferencia",
                    "TRX-55421",
                    "Pago parcial"
                }
        );

        modeloPagos.addRow(
                new Object[]{
                    "05/08/2026",
                    "$ 250.000",
                    "Efectivo",
                    "-",
                    "Pago de facturas"
                }
        );
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

        btnRegistrarPago.addActionListener(e -> {

            DialogoRegistrarPagoProveedor dialogo
                    = new DialogoRegistrarPagoProveedor(
                            this,
                            idProveedor,
                            lblRazonSocial.getText(),
                            185400.00
                    );

            dialogo.setVisible(true);

        });

        btnCerrar.addActionListener(e -> {

            dispose();

        });
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
}
