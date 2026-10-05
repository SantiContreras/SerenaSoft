package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Proveedor;
import services.ProveedorService;

import java.util.List;

public class DialogoBuscarProveedor extends JDialog {

    //==========================================================
    // BUSQUEDA
    //==========================================================
    private JTextField txtBuscar;
    private JButton btnBuscar;

    //==========================================================
    // TABLA
    //==========================================================
    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnCancelar;
    private JButton btnSeleccionar;

    //==========================================================
    // RESULTADO
    //==========================================================
    private int idSeleccionado = -1;
    private String razonSocialSeleccionada;
    private String cuitSeleccionado;

    private boolean seleccionado = false;

    //==========================================================
// SERVICIOS
//==========================================================
    private final ProveedorService proveedorService
            = new ProveedorService();

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color AZUL
            = new Color(25, 70, 145);

    private final Color VERDE
            = new Color(25, 135, 84);

    private final Color GRIS
            = new Color(110, 120, 135);

    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoBuscarProveedor(Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        cargarProveedores();
        configurarEventos();

        setModal(true);

        setTitle("Buscar Proveedor");

        setSize(
                new Dimension(
                        850,
                        520
                )
        );

        setMinimumSize(
                new Dimension(
                        760,
                        470
                )
        );

        EstiloBotones.corregirBotones(
                getContentPane()
        );
        setLocationRelativeTo(parent);

        setResizable(true);
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

        btnCancelar
                = crearBoton(
                        "Cancelar",
                        GRIS,
                        120
                );

        btnSeleccionar
                = crearBoton(
                        "Seleccionar",
                        VERDE,
                        140
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
                            "ID",
                            "CUIT",
                            "Razón Social",
                            "Contacto",
                            "Teléfono",
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

        tablaProveedores.setGridColor(
                new Color(
                        220,
                        226,
                        235
                )
        );

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
    // CONSTRUIR
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
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
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
                        "BUSCAR PROVEEDOR"
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
                        "Busque por razón social, nombre comercial o CUIT"
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
                Box.createVerticalStrut(5)
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
        // BUSCADOR
        //======================================================
        JPanel panelBuscar
                = new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        panelBuscar.setBackground(
                Color.WHITE
        );

        panelBuscar.setBorder(
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

        JLabel lblBuscar
                = new JLabel(
                        "Buscar:"
                );

        lblBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panelBuscar.add(
                lblBuscar,
                BorderLayout.WEST
        );

        txtBuscar.setPreferredSize(
                new Dimension(
                        450,
                        34
                )
        );

        panelBuscar.add(
                txtBuscar,
                BorderLayout.CENTER
        );

        panelBuscar.add(
                btnBuscar,
                BorderLayout.EAST
        );

        centro.add(
                panelBuscar,
                BorderLayout.NORTH
        );

        //======================================================
        // TABLA
        //======================================================
        JScrollPane scroll
                = new JScrollPane(
                        tablaProveedores
                );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        centro.add(
                scroll,
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
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                12
                        )
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDE
                )
        );

        footer.add(
                btnCancelar
        );

        footer.add(
                btnSeleccionar
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    //==========================================================
// CARGAR PROVEEDORES ACTIVOS
//==========================================================
    private void cargarProveedores() {

        modeloTabla.setRowCount(0);

        List<Proveedor> proveedores
                = proveedorService.listarActivos();

        for (Proveedor proveedor : proveedores) {

            agregarProveedorTabla(proveedor);
        }
    }

//==========================================================
// AGREGAR PROVEEDOR A LA TABLA
//==========================================================
    private void agregarProveedorTabla(
            Proveedor proveedor) {

        String contacto
                = proveedor.getPersonaContacto();

        String telefono
                = proveedor.getTelefono();

        if (contacto == null) {
            contacto = "";
        }

        if (telefono == null) {
            telefono = "";
        }

        String razonSocial
                = proveedor.getRazonSocial();

        // Si tiene nombre comercial, también lo mostramos.
        if (proveedor.getNombreComercial() != null
                && !proveedor.getNombreComercial()
                        .trim()
                        .isEmpty()) {

            razonSocial
                    = proveedor.getNombreComercial()
                    + " - "
                    + proveedor.getRazonSocial();
        }

        modeloTabla.addRow(
                new Object[]{
                    proveedor.getIdProveedor(),
                    proveedor.getCuit() == null
                    ? ""
                    : proveedor.getCuit(),
                    razonSocial,
                    contacto,
                    telefono,
                    "Activo"
                }
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            seleccionado = false;

            dispose();

        });

        //======================================================
// FILTRO EN TIEMPO REAL
//======================================================
        txtBuscar.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarProveedor();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarProveedor();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {

                filtrarProveedor();
            }
        });

        btnSeleccionar.addActionListener(e -> {

            seleccionarProveedor();

        });

        btnBuscar.addActionListener(e -> {

            filtrarProveedor();

        });

        txtBuscar.addActionListener(e -> {

            filtrarProveedor();

        });

        tablaProveedores.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {

                    seleccionarProveedor();
                }
            }
        });
    }

    //==========================================================
    // SELECCIONAR
    //==========================================================
   //==========================================================
// SELECCIONAR PROVEEDOR
//==========================================================

private void seleccionarProveedor() {

    int fila
            = tablaProveedores.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un proveedor.",
                "Proveedor",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }


    //======================================================
    // OBTENER ID
    //======================================================

    int idProveedor
            = Integer.parseInt(
                    modeloTabla
                            .getValueAt(
                                    fila,
                                    0
                            )
                            .toString()
            );


    //======================================================
    // RECUPERAR OBJETO REAL
    //======================================================

    Proveedor proveedor
            = proveedorService.buscarPorId(
                    idProveedor
            );


    if (proveedor == null) {

        JOptionPane.showMessageDialog(
                this,
                "No se pudo recuperar el proveedor seleccionado.",
                "Proveedor",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }


    //======================================================
    // GUARDAR RESULTADO
    //======================================================

    idSeleccionado
            = proveedor.getIdProveedor();

    razonSocialSeleccionada
            = proveedor.getRazonSocial();

    cuitSeleccionado
            = proveedor.getCuit();

    seleccionado = true;


    //======================================================
    // CERRAR
    //======================================================

    dispose();
}
    //==========================================================
    // FILTRO VISUAL TEMPORAL
    //==========================================================
    //==========================================================
// FILTRAR PROVEEDORES
//==========================================================
    private void filtrarProveedor() {

        String texto
                = txtBuscar
                        .getText()
                        .trim();

        modeloTabla.setRowCount(0);

        //======================================================
        // SIN BÚSQUEDA -> MOSTRAR TODOS LOS ACTIVOS
        //======================================================
        List<Proveedor> proveedores;

        if (texto.isEmpty()) {

            proveedores
                    = proveedorService.listarActivos();

        } else {

            proveedores
                    = proveedorService.buscarActivos(
                            texto
                    );
        }

        //======================================================
        // MOSTRAR RESULTADOS
        //======================================================
        for (Proveedor proveedor : proveedores) {

            agregarProveedorTabla(
                    proveedor
            );
        }
    }

    //==========================================================
    // GETTERS
    //==========================================================
    public boolean isSeleccionado() {

        return seleccionado;
    }

    public int getIdSeleccionado() {

        return idSeleccionado;
    }

    public String getRazonSocialSeleccionada() {

        return razonSocialSeleccionada;
    }

    public String getCuitSeleccionado() {

        return cuitSeleccionado;
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
}
