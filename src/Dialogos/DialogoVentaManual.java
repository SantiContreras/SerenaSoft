package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoVentaManual extends JDialog {

    private JTextField txtDescripcion;
    private JSpinner spnCantidad;
    private JTextField txtPrecio;

    private JLabel lblSubtotal;

    private JButton btnCancelar;
    private JButton btnAgregar;

    private boolean confirmado = false;

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color FONDO =
            new Color(245, 247, 250);

    public DialogoVentaManual(
            Window parent) {

        super(parent);

        inicializarComponentes();
        construirDialogo();
        configurarEventos();

        setTitle(
                "Agregar Concepto Manual"
        );

        setModal(true);

        setSize(
                new Dimension(
                        560,
                        430
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

        txtDescripcion =
                new JTextField();

        spnCantidad =
                new JSpinner(
                        new SpinnerNumberModel(
                                1.0,
                                0.001,
                                99999.0,
                                1.0
                        )
                );

        txtPrecio =
                new JTextField();

        lblSubtotal =
                new JLabel(
                        "$ 0,00"
                );

        lblSubtotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        lblSubtotal.setForeground(
                VERDE
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
                        "AGREGAR CONCEPTO MANUAL"
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
                        "Utilice esta opción para conceptos no registrados en artículos"
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
                new JLabel("Descripción"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        centro.add(
                txtDescripcion,
                c
        );

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;

        centro.add(
                new JLabel("Cantidad"),
                c
        );

        c.gridx = 1;

        spnCantidad.setPreferredSize(
                new Dimension(
                        160,
                        32
                )
        );

        centro.add(
                spnCantidad,
                c
        );

        c.gridx = 0;
        c.gridy = 2;

        centro.add(
                new JLabel("Precio Unitario"),
                c
        );

        c.gridx = 1;

        txtPrecio.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        centro.add(
                txtPrecio,
                c
        );

        c.gridx = 0;
        c.gridy = 3;

        centro.add(
                new JLabel("SUBTOTAL"),
                c
        );

        c.gridx = 1;

        centro.add(
                lblSubtotal,
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

        txtPrecio.addKeyListener(
                new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(
                    java.awt.event.KeyEvent e) {

                actualizarSubtotal();
            }
        });

        spnCantidad.addChangeListener(e -> {

            actualizarSubtotal();
        });

        btnCancelar.addActionListener(e -> {

            confirmado = false;

            dispose();
        });

        btnAgregar.addActionListener(e -> {

            if (txtDescripcion
                    .getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese una descripción.",
                        "Venta Manual",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (obtenerPrecio() <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un precio válido.",
                        "Venta Manual",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            confirmado = true;

            dispose();
        });
    }

    private void actualizarSubtotal() {

        lblSubtotal.setText(
                formatear(
                        getSubtotal()
                )
        );
    }

    private double obtenerPrecio() {

        try {

            String texto =
                    txtPrecio
                            .getText()
                            .trim()
                            .replace(",", ".");

            if (texto.isEmpty()) {
                return 0;
            }

            return Double.parseDouble(
                    texto
            );

        } catch (Exception e) {

            return 0;
        }
    }

    public String getDescripcion() {

        return txtDescripcion
                .getText()
                .trim();
    }

    public double getCantidad() {

        return ((Number)
                spnCantidad.getValue())
                .doubleValue();
    }

    public double getPrecio() {

        return obtenerPrecio();
    }

    public double getSubtotal() {

        return getCantidad()
                * obtenerPrecio();
    }

    public boolean isConfirmado() {

        return confirmado;
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
