package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DialogoBuscarCliente extends JDialog {

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnCancelar;
    private JButton btnSeleccionar;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private boolean seleccionado = false;

    private int idSeleccionado = -1;
    private String nombreSeleccionado;
    private String documentoSeleccionado;
    private String condicionIVASeleccionada;
    private String tipoClienteSeleccionado;

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

    public DialogoBuscarCliente(Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        cargarDatosPrueba();
        configurarEventos();

        setTitle(
                "Buscar Cliente"
        );

        setModal(true);

        setSize(
                new Dimension(
                        900,
                        520
                )
        );

        setMinimumSize(
                new Dimension(
                        800,
                        470
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

        btnSeleccionar =
                crearBoton(
                        "Seleccionar",
                        VERDE,
                        140
                );

        inicializarTabla();
    }

    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "CUIT / DNI",
                            "Cliente",
                            "Condición IVA",
                            "Tipo",
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

        tablaClientes =
                new JTable(
                        modeloTabla
                );

        tablaClientes.setRowHeight(
                30
        );

        tablaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaClientes.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaClientes.setBackground(
                Color.WHITE
        );

        tablaClientes.setGridColor(
                new Color(
                        220,
                        226,
                        235
                )
        );

        tablaClientes.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tablaClientes.setSelectionForeground(
                Color.BLACK
        );

        tablaClientes.setFillsViewportHeight(
                true
        );

        tablaClientes
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        tablaClientes
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
                                super
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
             i < tablaClientes.getColumnCount();
             i++) {

            tablaClientes
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

        //=====================================================
        // HEADER
        //=====================================================
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
                        "BUSCAR CLIENTE"
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
                        "Busque por nombre, apellido, razón social, CUIT o DNI"
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

        //=====================================================
        // CONTENIDO
        //=====================================================
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
                        tablaClientes
                );

        centro.add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                centro,
                BorderLayout.CENTER
        );

        //=====================================================
        // FOOTER
        //=====================================================
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
                btnSeleccionar
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
                    1,
                    "20301234567",
                    "Juan Pérez",
                    "Consumidor Final",
                    "Minorista",
                    "Activo"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    2,
                    "30712345678",
                    "Supermercado Norte SRL",
                    "Responsable Inscripto",
                    "Mayorista",
                    "Activo"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    3,
                    "27123456789",
                    "María López",
                    "Monotributista",
                    "Minorista",
                    "Activo"
                }
        );

        modeloTabla.addRow(
                new Object[]{
                    4,
                    "30999888777",
                    "Comercial NEA SA",
                    "Responsable Inscripto",
                    "Cuenta Corriente",
                    "Activo"
                }
        );
    }

    private void configurarEventos() {

        btnCancelar.addActionListener(e -> {

            seleccionado = false;

            dispose();

        });

        btnSeleccionar.addActionListener(e -> {

            seleccionarCliente();

        });

        btnBuscar.addActionListener(e -> {

            filtrar();

        });

        txtBuscar.addActionListener(e -> {

            filtrar();

        });

        tablaClientes.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {

                    seleccionarCliente();
                }
            }
        });
    }

    private void seleccionarCliente() {

        int fila =
                tablaClientes.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente.",
                    "Cliente",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String estado =
                modeloTabla
                        .getValueAt(
                                fila,
                                5
                        )
                        .toString();

        if (!"Activo".equalsIgnoreCase(
                estado
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "El cliente seleccionado se encuentra inactivo.",
                    "Cliente inactivo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        idSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        documentoSeleccionado =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        nombreSeleccionado =
                modeloTabla
                        .getValueAt(
                                fila,
                                2
                        )
                        .toString();

        condicionIVASeleccionada =
                modeloTabla
                        .getValueAt(
                                fila,
                                3
                        )
                        .toString();

        tipoClienteSeleccionado =
                modeloTabla
                        .getValueAt(
                                fila,
                                4
                        )
                        .toString();

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

            String documento =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    1
                            )
                            .toString()
                            .toLowerCase();

            String cliente =
                    modeloTabla
                            .getValueAt(
                                    i,
                                    2
                            )
                            .toString()
                            .toLowerCase();

            if (!documento.contains(texto)
                    && !cliente.contains(texto)) {

                modeloTabla.removeRow(i);
            }
        }
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public int getIdSeleccionado() {
        return idSeleccionado;
    }

    public String getNombreSeleccionado() {
        return nombreSeleccionado;
    }

    public String getDocumentoSeleccionado() {
        return documentoSeleccionado;
    }

    public String getCondicionIVASeleccionada() {
        return condicionIVASeleccionada;
    }

    public String getTipoClienteSeleccionado() {
        return tipoClienteSeleccionado;
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

        boton.setFocusPainted(
                false
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
}