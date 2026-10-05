package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Producto;
import services.ProductoService;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class DialogoBuscarProducto extends JDialog {

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnCancelar;
    private JButton btnAgregar;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private boolean seleccionado = false;

    private String codigoSeleccionado;
    private String productoSeleccionado;
    private double precioSeleccionado;
    private double stockSeleccionado;

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

    //==========================================================
// SERVICIOS Y DATOS
//==========================================================
    private final ProductoService productoService
            = new ProductoService();

    private List<Producto> productosActivos
            = new ArrayList<>();

    private Producto objetoProductoSeleccionado;

    public DialogoBuscarProducto(Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        cargarProductos();
        configurarEventos();

        setTitle("Buscar Producto");
        setModal(true);

        setSize(
                new Dimension(
                        900,
                        540
                )
        );

        setMinimumSize(
                new Dimension(
                        820,
                        500
                )
        );

        EstiloBotones.corregirBotones(
                getContentPane()
        );
        setLocationRelativeTo(parent);
        setResizable(true);
    }

    private void inicializarComponentes() {

        txtBuscar
                = new JTextField();

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

        btnAgregar
                = crearBoton(
                        "Seleccionar",
                        VERDE,
                        140
                );

        inicializarTabla();
    }

    //==========================================================
// CARGAR PRODUCTOS ACTIVOS
//==========================================================
    private void cargarProductos() {

        productosActivos
                = productoService.listarActivos();

        mostrarProductos(
                productosActivos
        );
    }

//==========================================================
// MOSTRAR PRODUCTOS
//==========================================================
    private void mostrarProductos(
            List<Producto> productos) {

        modeloTabla.setRowCount(0);

        for (Producto producto : productos) {

            agregarProductoTabla(
                    producto
            );
        }
    }

//==========================================================
// AGREGAR PRODUCTO A TABLA
//==========================================================
    private void agregarProductoTabla(
            Producto producto) {

        String marca = "";

        if (producto.getMarca() != null
                && producto.getMarca().getNombre() != null) {

            marca = producto
                    .getMarca()
                    .getNombre();
        }

        BigDecimal precio
                = producto.getPrecioVenta();

        if (precio == null) {
            precio = BigDecimal.ZERO;
        }


        /*
     * Por ahora dejamos el stock en cero.
     *
     * El stock real depende del depósito.
     * Cuando conectemos este buscador específicamente
     * con Entrada Manual, vamos a obtener el stock
     * del depósito de destino.
         */
        BigDecimal stock
                = BigDecimal.ZERO;

        modeloTabla.addRow(
                new Object[]{
                    producto.getCodigo(),
                    producto.getNombre(),
                    marca,
                    stock,
                    precio,
                    "Disponible"
                }
        );
    }

    private void inicializarTabla() {

        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "Código",
                            "Producto",
                            "Marca",
                            "Stock",
                            "Precio",
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

        tablaProductos
                = new JTable(
                        modeloTabla
                );

        tablaProductos.setRowHeight(30);

        tablaProductos.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaProductos.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaProductos.setSelectionForeground(
                Color.BLACK
        );

        tablaProductos.setFillsViewportHeight(
                true
        );

        tablaProductos
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaProductos
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        aplicarHeaderAzul();
    }

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

                label.setOpaque(true);

                return label;
            }
        };

        for (int i = 0;
                i < tablaProductos.getColumnCount();
                i++) {

            tablaProductos
                    .getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(
                            renderer
                    );
        }
    }

    private void construirDialogo() {

        setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                FONDO
        );

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

        JLabel titulo
                = new JLabel(
                        "BUSCAR PRODUCTO"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        JLabel subtitulo
                = new JLabel(
                        "Busque por código, descripción, marca o categoría"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                Color.GRAY
        );

        header.add(titulo);
        header.add(
                Box.createVerticalStrut(5)
        );
        header.add(subtitulo);

        add(
                header,
                BorderLayout.NORTH
        );

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

        JPanel buscador
                = new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        buscador.setBackground(
                Color.WHITE
        );

        buscador.setBorder(
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

        buscador.add(
                lblBuscar,
                BorderLayout.WEST
        );

        buscador.add(
                txtBuscar,
                BorderLayout.CENTER
        );

        buscador.add(
                btnBuscar,
                BorderLayout.EAST
        );

        centro.add(
                buscador,
                BorderLayout.NORTH
        );

        JScrollPane scroll
                = new JScrollPane(
                        tablaProductos
                );

        centro.add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                centro,
                BorderLayout.CENTER
        );

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

        footer.add(
                btnCancelar
        );

        footer.add(
                btnAgregar
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            seleccionado = false;

            dispose();
        });

        //======================================================
// FILTRO EN TIEMPO REAL
//======================================================
        txtBuscar.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {

                        filtrar();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {

                        filtrar();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {

                        filtrar();
                    }
                });

        btnAgregar.addActionListener(e -> {

            seleccionarProducto();
        });

        btnBuscar.addActionListener(e -> {

            filtrar();
        });

        txtBuscar.addActionListener(e -> {

            filtrar();
        });
        

        tablaProductos.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {

                    seleccionarProducto();
                }
            }
        });
        
        
    }

    //==========================================================
// SELECCIONAR PRODUCTO
//==========================================================
    private void seleccionarProducto() {

        int fila
                = tablaProductos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto.",
                    "Producto",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        //======================================================
        // OBTENER CÓDIGO
        //======================================================
        String codigo
                = modeloTabla
                        .getValueAt(
                                fila,
                                0
                        )
                        .toString();

        //======================================================
        // RECUPERAR PRODUCTO REAL
        //======================================================
        Producto producto
                = productoService.buscarPorCodigo(
                        codigo
                );

        if (producto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo recuperar el producto seleccionado.",
                    "Producto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        //======================================================
        // GUARDAR PRODUCTO SELECCIONADO
        //======================================================
        objetoProductoSeleccionado
                = producto;

        codigoSeleccionado
                = producto.getCodigo();

        productoSeleccionado
                = producto.getNombre();

        //======================================================
        // PRECIO
        //======================================================
        if (producto.getPrecioVenta() != null) {

            precioSeleccionado
                    = producto
                            .getPrecioVenta()
                            .doubleValue();

        } else {

            precioSeleccionado = 0;
        }

        // El stock real lo resolveremos según depósito.
        stockSeleccionado = 0;

        seleccionado = true;

        dispose();
    }

    //==========================================================
// FILTRAR PRODUCTOS
//==========================================================
    private void filtrar() {

        String texto
                = normalizar(
                        txtBuscar
                                .getText()
                                .trim()
                );

        //======================================================
        // SIN TEXTO -> MOSTRAR TODOS
        //======================================================
        if (texto.isEmpty()) {

            mostrarProductos(
                    productosActivos
            );

            return;
        }

        //======================================================
        // FILTRAR
        //======================================================
        List<Producto> filtrados
                = new ArrayList<>();

        for (Producto producto : productosActivos) {

            String codigo
                    = normalizar(
                            producto.getCodigo()
                    );

            String codigoInterno
                    = normalizar(
                            producto.getCodigoInterno()
                    );

            String codigoBarra
                    = normalizar(
                            producto.getCodigoBarra()
                    );

            String nombre
                    = normalizar(
                            producto.getNombre()
                    );

            //==================================================
            // MARCA
            //==================================================
            String marca = "";

            if (producto.getMarca() != null) {

                marca = normalizar(
                        producto
                                .getMarca()
                                .getNombre()
                );
            }

            //==================================================
            // CATEGORÍA
            //==================================================
            String categoria = "";

            if (producto.getCategoria() != null) {

                categoria = normalizar(
                        producto
                                .getCategoria()
                                .getNombre()
                );
            }

            //==================================================
            // COINCIDENCIA
            //==================================================
            if (codigo.contains(texto)
                    || codigoInterno.contains(texto)
                    || codigoBarra.contains(texto)
                    || nombre.contains(texto)
                    || marca.contains(texto)
                    || categoria.contains(texto)) {

                filtrados.add(
                        producto
                );
            }
        }

        mostrarProductos(
                filtrados
        );
    }

//==========================================================
// NORMALIZAR TEXTO PARA BÚSQUEDA
//==========================================================
    private String normalizar(
            String texto) {

        if (texto == null) {
            return "";
        }

        String normalizado
                = Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );

        normalizado
                = normalizado.replaceAll(
                        "\\p{M}",
                        ""
                );

        return normalizado
                .toLowerCase()
                .trim();
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public String getCodigoSeleccionado() {
        return codigoSeleccionado;
    }

    public Producto getObjetoProductoSeleccionado() {

        return objetoProductoSeleccionado;
    }

    public String getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public double getPrecioSeleccionado() {
        return precioSeleccionado;
    }

    public double getStockSeleccionado() {
        return stockSeleccionado;
    }

    private JButton crearBoton(
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
                        36
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
}
