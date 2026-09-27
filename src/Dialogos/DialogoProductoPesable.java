package Dialogos;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Diseños.EstiloBotones;

public class DialogoProductoPesable extends JDialog {

    private JTextField txtProducto;
    private JTextField txtPeso;

    private JLabel lblPrecioKg;
    private JLabel lblTotal;

    private JButton btnBuscarProducto;
    private JButton btnCancelar;
    private JButton btnAgregar;

    private String codigoProducto;
    private String nombreProducto;
    private double precioKg;

    private boolean confirmado = false;

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color FONDO =
            new Color(245, 247, 250);

    public DialogoProductoPesable(
            Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        configurarEventos();

        setTitle(
                "Producto Pesable"
        );

        setModal(true);

        setSize(
                new Dimension(
                        600,
                        470
                )
        );

        setLocationRelativeTo(
                parent
        );
        EstiloBotones.corregirBotones(
                getContentPane()
    );

        setResizable(false);
    }

    private void inicializarComponentes() {

        txtProducto =
                new JTextField();

        txtProducto.setEditable(
                false
        );

        txtPeso =
                new JTextField();

        lblPrecioKg =
                new JLabel(
                        "$ 0,00"
                );

        lblTotal =
                new JLabel(
                        "$ 0,00"
                );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        lblTotal.setForeground(
                VERDE
        );

        btnBuscarProducto =
                crearBoton(
                        "Buscar",
                        AZUL_OSCURO,
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
                        "Agregar",
                        VERDE,
                        140
                );
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
                        15,
                        25
                )
        );

        JLabel titulo =
                new JLabel(
                        "PRODUCTO PESABLE"
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
                        "Ingrese el peso del producto"
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
                        new GridBagLayout()
                );

        centro.setBackground(
                Color.WHITE
        );

        centro.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        c.anchor =
                GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;

        centro.add(
                new JLabel("Producto"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        centro.add(
                txtProducto,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        centro.add(
                btnBuscarProducto,
                c
        );

        c.gridx = 0;
        c.gridy = 1;

        centro.add(
                new JLabel("Precio por Kg"),
                c
        );

        c.gridx = 1;

        lblPrecioKg.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        centro.add(
                lblPrecioKg,
                c
        );

        c.gridx = 0;
        c.gridy = 2;

        centro.add(
                new JLabel("Peso"),
                c
        );

        c.gridx = 1;

        txtPeso.setPreferredSize(
                new Dimension(
                        180,
                        34
                )
        );

        centro.add(
                txtPeso,
                c
        );

        c.gridx = 2;

        centro.add(
                new JLabel("Kg"),
                c
        );

        c.gridx = 0;
        c.gridy = 3;

        centro.add(
                new JLabel("TOTAL"),
                c
        );

        c.gridx = 1;
        c.gridwidth = 2;

        centro.add(
                lblTotal,
                c
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

    private void configurarEventos() {

        btnBuscarProducto.addActionListener(e -> {

            DialogoBuscarProducto dialogo =
                    new DialogoBuscarProducto(
                            this
                    );

            dialogo.setVisible(true);

            if (dialogo.isSeleccionado()) {

                codigoProducto =
                        dialogo.getCodigoSeleccionado();

                nombreProducto =
                        dialogo.getProductoSeleccionado();

                precioKg =
                        dialogo.getPrecioSeleccionado();

                txtProducto.setText(
                        nombreProducto
                );

                lblPrecioKg.setText(
                        formatear(
                                precioKg
                        )
                );

                actualizarTotal();
            }
        });

        txtPeso.addActionListener(e -> {

            actualizarTotal();
        });

        txtPeso.addKeyListener(
                new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(
                    java.awt.event.KeyEvent e) {

                actualizarTotal();
            }
        });

        btnCancelar.addActionListener(e -> {

            confirmado = false;

            dispose();
        });

        btnAgregar.addActionListener(e -> {

            if (nombreProducto == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un producto.",
                        "Producto",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (obtenerPeso() <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un peso válido.",
                        "Peso",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            confirmado = true;

            dispose();
        });
    }

    private void actualizarTotal() {

        double peso =
                obtenerPeso();

        double total =
                peso * precioKg;

        lblTotal.setText(
                formatear(
                        total
                )
        );
    }

    private double obtenerPeso() {

        try {

            String texto =
                    txtPeso
                            .getText()
                            .trim()
                            .replace(",", ".");

            if (texto.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(
                    texto
            );

        } catch (Exception ex) {

            return 0;
        }
    }

    private String formatear(
            double valor) {

        DecimalFormat formato =
                new DecimalFormat(
                        "#,##0.00"
                );

        return "$ "
                + formato.format(
                        valor
                );
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPeso() {
        return obtenerPeso();
    }

    public double getPrecioKg() {
        return precioKg;
    }

    public double getTotal() {
        return obtenerPeso() * precioKg;
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

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        boton.setFocusPainted(
                false
        );

        return boton;
    }
}