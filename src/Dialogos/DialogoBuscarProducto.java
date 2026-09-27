package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color AZUL =
            new Color(25, 70, 145);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BORDE =
            new Color(215, 222, 232);

    public DialogoBuscarProducto(Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        cargarDatosPrueba();
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

        txtBuscar =
                new JTextField();

        btnBuscar =
                crearBoton(
                        "Buscar",
                        AZUL,
                        100
                );

        btnCancelar =
                crearBoton(
                        "Cancelar",
                        GRIS,
                        120
                );

        btnAgregar =
                crearBoton(
                        "Agregar a Venta",
                        VERDE,
                        160
                );

        inicializarTabla();
    }

    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
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

        tablaProductos =
                new JTable(
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

        JPanel header =
                new JPanel();

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

        JLabel titulo =
                new JLabel(
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

        JLabel subtitulo =
                new JLabel(
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

        JPanel centro =
                new JPanel(
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

        JPanel buscador =
                new JPanel(
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

        JLabel lblBuscar =
                new JLabel(
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

        JScrollPane scroll =
                new JScrollPane(
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

        JPanel footer =
                new JPanel(
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

    private void cargarDatosPrueba() {

        modeloTabla.setRowCount(
                0
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0001",
                    "Coca Cola 2.25 L",
                    "Coca Cola",
                    120,
                    2600,
                    "Disponible"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0002",
                    "Aceite Natura 900 ml",
                    "Natura",
                    18,
                    3200,
                    "Disponible"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0003",
                    "Yerba Playadito 1 Kg",
                    "Playadito",
                    55,
                    4900,
                    "Disponible"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    "A0004",
                    "Azúcar Ledesma",
                    "Ledesma",
                    7,
                    1450,
                    "Bajo Stock"
                }
        );
    }

    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            seleccionado = false;

            dispose();
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

    private void seleccionarProducto() {

        int fila =
                tablaProductos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto.",
                    "Producto",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        codigoSeleccionado =
                modeloTabla
                        .getValueAt(
                                fila,
                                0
                        )
                        .toString();

        productoSeleccionado =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        stockSeleccionado =
                Double.parseDouble(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        3
                                )
                                .toString()
                );

        precioSeleccionado =
                Double.parseDouble(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        4
                                )
                                .toString()
                );

        seleccionado = true;

        dispose();
    }

    private void filtrar() {

        String texto =
                txtBuscar
                        .getText()
                        .trim()
                        .toLowerCase();

        cargarDatosPrueba();

        if (texto.isEmpty()) {
            return;
        }

        for (int i =
                modeloTabla.getRowCount() - 1;
             i >= 0;
             i--) {

            String codigo =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    0
                            )
                            .toString()
                            .toLowerCase();

            String producto =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    1
                            )
                            .toString()
                            .toLowerCase();

            String marca =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    2
                            )
                            .toString()
                            .toLowerCase();

            if (!codigo.contains(texto)
                    && !producto.contains(texto)
                    && !marca.contains(texto)) {

                modeloTabla.removeRow(i);
            }
        }
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public String getCodigoSeleccionado() {
        return codigoSeleccionado;
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

        JButton boton =
                new JButton(
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