package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogoAjusteStock extends JDialog {

    //==========================================================
    // COLORES SERENA SOFT
    //==========================================================
    private static final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private static final Color AZUL_SERENA =
            new Color(25, 70, 145);

    private static final Color AZUL_HOVER =
            new Color(45, 95, 180);

    private static final Color FONDO =
            new Color(225, 232, 241);

    private static final Color BORDE =
            new Color(205, 215, 228);

    private static final Color TEXTO =
            new Color(45, 52, 62);

    private static final Color TEXTO_SECUNDARIO =
            new Color(100, 108, 120);

    private static final Color VERDE =
            new Color(25, 135, 84);

    private static final Color ROJO =
            new Color(190, 65, 65);

    private static final Color GRIS_METAL =
            new Color(70, 78, 88);

    private static final Color GRIS_METAL_HOVER =
            new Color(88, 98, 110);

    private static final Color GRIS_CLARO =
            new Color(222, 227, 233);

    private static final Color GRIS_CLARO_HOVER =
            new Color(205, 212, 220);

    //==========================================================
    // PRODUCTO
    //==========================================================
    private JTextField txtProducto;
    private JButton btnBuscarProducto;

    private JLabel lblCodigo;
    private JLabel lblMarca;
    private JLabel lblCategoria;

    //==========================================================
    // CONTEO
    //==========================================================
    private JLabel lblStockSistema;

    private JTextField txtStockFisico;

    private JLabel lblDiferencia;
    private JLabel lblInterpretacion;

    //==========================================================
    // AJUSTE
    //==========================================================
    private JComboBox<String> comboMotivo;

    private JTextArea txtObservaciones;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnLimpiar;
    private JButton btnCancelar;
    private JButton btnAplicar;

    //==========================================================
    // PRODUCTO SELECCIONADO
    //==========================================================
    private String codigoSeleccionado;
    private String productoSeleccionado;

    private String marcaSeleccionada;
    private String categoriaSeleccionada;

    private double stockSistema;

    //==========================================================
    // ESTADO
    //==========================================================
    private boolean confirmado = false;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoAjusteStock(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        cargarDatosPrueba();

        configurarEventos();

        setTitle(
                "Ajuste de Stock"
        );

        setModal(
                true
        );

        setSize(
                new Dimension(
                        820,
                        650
                )
        );

        setMinimumSize(
                new Dimension(
                        720,
                        560
                )
        );

        setResizable(
                true
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );
        
         EstiloBotones.corregirBotones(
                getContentPane()
    );

        setLocationRelativeTo(
                parent
        );
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // PRODUCTO
        //======================================================
        txtProducto =
                crearCampoTexto();

        txtProducto.setEditable(
                false
        );

        txtProducto.setBackground(
                new Color(
                        248,
                        249,
                        251
                )
        );

        btnBuscarProducto =
                crearBotonPrimario(
                        "BUSCAR",
                        105
                );

        lblCodigo =
                crearValorDetalle("-");

        lblMarca =
                crearValorDetalle("-");

        lblCategoria =
                crearValorDetalle("-");

        //======================================================
        // CONTEO
        //======================================================
        lblStockSistema =
                new JLabel(
                        "-"
                );

        lblStockSistema.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        lblStockSistema.setForeground(
                AZUL_OSCURO
        );

        txtStockFisico =
                crearCampoTexto();

        txtStockFisico.setHorizontalAlignment(
                JTextField.CENTER
        );

        txtStockFisico.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblDiferencia =
                new JLabel(
                        "0"
                );

        lblDiferencia.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        lblDiferencia.setForeground(
                AZUL_SERENA
        );

        lblInterpretacion =
                new JLabel(
                        "Seleccione un artículo e ingrese el stock físico"
                );

        lblInterpretacion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblInterpretacion.setForeground(
                TEXTO_SECUNDARIO
        );

        //======================================================
        // MOTIVO
        //======================================================
        comboMotivo =
                new JComboBox<>();

        comboMotivo.setPreferredSize(
                new Dimension(
                        300,
                        34
                )
        );

        comboMotivo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        //======================================================
        // OBSERVACIONES
        //======================================================
        txtObservaciones =
                new JTextArea();

        txtObservaciones.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        txtObservaciones.setLineWrap(
                true
        );

        txtObservaciones.setWrapStyleWord(
                true
        );

        //======================================================
        // BOTONES
        //======================================================
        btnLimpiar =
                crearBotonClaro(
                        "LIMPIAR",
                        110
                );

        btnCancelar =
                crearBotonMetal(
                        "CANCELAR",
                        120
                );

        btnAplicar =
                crearBotonPrimario(
                        "APLICAR AJUSTE",
                        165
                );
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
        add(
                crearHeader(),
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO
        //======================================================
        JPanel contenido =
                new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setBackground(
                FONDO
        );

        contenido.setBorder(
                new EmptyBorder(
                        15,
                        18,
                        15,
                        18
                )
        );

        JPanel producto =
                crearPanelProducto();

        JPanel conteo =
                crearPanelConteo();

        JPanel ajuste =
                crearPanelInformacionAjuste();

        producto.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        conteo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        ajuste.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(
                producto
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                conteo
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                ajuste
        );

        contenido.add(
                Box.createVerticalStrut(
                        10
                )
        );

        //======================================================
        // SCROLL GENERAL
        //======================================================
        JScrollPane scrollGeneral =
                new JScrollPane(
                        contenido
                );

        scrollGeneral.setBorder(
                null
        );

        scrollGeneral.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollGeneral.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollGeneral
                .getVerticalScrollBar()
                .setUnitIncrement(
                        18
                );

        scrollGeneral
                .getViewport()
                .setBackground(
                        FONDO
                );

        add(
                scrollGeneral,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER FIJO
        //======================================================
        add(
                crearFooter(),
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // HEADER
    //==========================================================
    private JPanel crearHeader() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                new EmptyBorder(
                        17,
                        25,
                        15,
                        25
                )
        );

        JLabel titulo =
                new JLabel(
                        "AJUSTE DE STOCK"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        titulo.setForeground(
                AZUL_OSCURO
        );

        JLabel subtitulo =
                new JLabel(
                        "Corrija diferencias entre el stock registrado y el conteo físico"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        panel.add(
                titulo
        );

        panel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        panel.add(
                subtitulo
        );

        return panel;
    }

    //==========================================================
    // BUSCAR ARTICULO
    //==========================================================
    private JPanel crearPanelProducto() {

        JPanel panel =
                crearTarjetaSeccion(
                        "BUSCAR ARTÍCULO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // PRODUCTO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Producto"
                ),
                c
        );

        JPanel buscador =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        buscador.setOpaque(
                false
        );

        buscador.add(
                txtProducto,
                BorderLayout.CENTER
        );

        buscador.add(
                btnBuscarProducto,
                BorderLayout.EAST
        );

        c.gridx = 1;
        c.gridwidth = 5;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                buscador,
                c
        );

        //======================================================
        // DATOS
        //======================================================
        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Código"
                ),
                c
        );

        c.gridx = 1;

        panel.add(
                lblCodigo,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Marca"
                ),
                c
        );

        c.gridx = 3;

        panel.add(
                lblMarca,
                c
        );

        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Categoría"
                ),
                c
        );

        c.gridx = 5;
        c.weightx = 1;

        panel.add(
                lblCategoria,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        135
                )
        );

        return panel;
    }

    //==========================================================
    // CONTEO
    //==========================================================
    private JPanel crearPanelConteo() {

        JPanel panel =
                crearTarjetaSeccion(
                        "CONTEO DE INVENTARIO"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // TITULOS
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Stock del Sistema"
                ),
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Stock Físico Contado"
                ),
                c
        );

        c.gridx = 4;

        panel.add(
                crearLabel(
                        "Diferencia"
                ),
                c
        );

        //======================================================
        // VALORES
        //======================================================
        c.gridx = 0;
        c.gridy = 1;

        panel.add(
                lblStockSistema,
                c
        );

        c.gridx = 2;

        txtStockFisico.setPreferredSize(
                new Dimension(
                        180,
                        42
                )
        );

        panel.add(
                txtStockFisico,
                c
        );

        c.gridx = 4;

        panel.add(
                lblDiferencia,
                c
        );

        //======================================================
        // INTERPRETACION
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 5;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                lblInterpretacion,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        165
                )
        );

        return panel;
    }

    //==========================================================
    // INFORMACION DEL AJUSTE
    //==========================================================
    private JPanel crearPanelInformacionAjuste() {

        JPanel panel =
                crearTarjetaSeccion(
                        "INFORMACIÓN DEL AJUSTE"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // MOTIVO
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Motivo"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                comboMotivo,
                c
        );

        //======================================================
        // OBSERVACIONES
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Observaciones"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.BOTH;

        JScrollPane scroll =
                new JScrollPane(
                        txtObservaciones
                );

        scroll.setPreferredSize(
                new Dimension(
                        600,
                        95
                )
        );

        panel.add(
                scroll,
                c
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        195
                )
        );

        return panel;
    }

    //==========================================================
    // FOOTER
    //==========================================================
    private JPanel crearFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                11
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
                btnLimpiar
        );

        footer.add(
                btnCancelar
        );

        footer.add(
                btnAplicar
        );

        return footer;
    }

    //==========================================================
    // DATOS PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        comboMotivo.removeAllItems();

        comboMotivo.addItem(
                "Seleccione..."
        );

        comboMotivo.addItem(
                "Conteo físico / Inventario"
        );

        comboMotivo.addItem(
                "Error de carga"
        );

        comboMotivo.addItem(
                "Mercadería dañada"
        );

        comboMotivo.addItem(
                "Vencimiento"
        );

        comboMotivo.addItem(
                "Robo o pérdida"
        );

        comboMotivo.addItem(
                "Corrección administrativa"
        );

        comboMotivo.addItem(
                "Otro"
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // BUSCAR PRODUCTO
        //======================================================
        btnBuscarProducto.addActionListener(e -> {

            DialogoBuscarProducto dialogo =
                    new DialogoBuscarProducto(
                            this
                    );

            dialogo.setVisible(
                    true
            );

            if (!dialogo.isSeleccionado()) {

                return;
            }

            codigoSeleccionado =
                    dialogo.getCodigoSeleccionado();

            productoSeleccionado =
                    dialogo.getProductoSeleccionado();

            stockSistema =
                    dialogo.getStockSeleccionado();

            /*
             * DialogoBuscarProducto actual no devuelve todavía
             * marca y categoría.
             *
             * Por ahora dejamos datos de prueba.
             * Cuando conectemos BD se cargarán realmente.
             */
            marcaSeleccionada =
                    "Marca del producto";

            categoriaSeleccionada =
                    "Categoría";

            txtProducto.setText(
                    productoSeleccionado
            );

            lblCodigo.setText(
                    codigoSeleccionado
            );

            lblMarca.setText(
                    marcaSeleccionada
            );

            lblCategoria.setText(
                    categoriaSeleccionada
            );

            lblStockSistema.setText(
                    formatearCantidad(
                            stockSistema
                    )
                    + " unidades"
            );

            txtStockFisico.setText(
                    ""
            );

            txtStockFisico.requestFocus();

            actualizarDiferencia();
        });

        //======================================================
        // STOCK FISICO EN TIEMPO REAL
        //======================================================
        txtStockFisico
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

            @Override
            public void insertUpdate(
                    DocumentEvent e) {

                actualizarDiferencia();
            }

            @Override
            public void removeUpdate(
                    DocumentEvent e) {

                actualizarDiferencia();
            }

            @Override
            public void changedUpdate(
                    DocumentEvent e) {

                actualizarDiferencia();
            }
        });

        //======================================================
        // LIMPIAR
        //======================================================
        btnLimpiar.addActionListener(e -> {

            limpiarFormulario();
        });

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar.addActionListener(e -> {

            confirmado =
                    false;

            dispose();
        });

        //======================================================
        // APLICAR
        //======================================================
        btnAplicar.addActionListener(e -> {

            aplicarAjuste();
        });
    }

    //==========================================================
    // ACTUALIZAR DIFERENCIA
    //==========================================================
    private void actualizarDiferencia() {

        if (codigoSeleccionado == null) {

            lblDiferencia.setText(
                    "0"
            );

            lblDiferencia.setForeground(
                    AZUL_SERENA
            );

            lblInterpretacion.setText(
                    "Seleccione un artículo e ingrese el stock físico"
            );

            return;
        }

        Double stockFisico =
                obtenerStockFisico();

        if (stockFisico == null) {

            lblDiferencia.setText(
                    "0"
            );

            lblDiferencia.setForeground(
                    AZUL_SERENA
            );

            lblInterpretacion.setText(
                    "Ingrese la cantidad física contada"
            );

            return;
        }

        double diferencia =
                stockFisico
                - stockSistema;

        if (diferencia > 0) {

            lblDiferencia.setText(
                    "+"
                    + formatearCantidad(
                            diferencia
                    )
            );

            lblDiferencia.setForeground(
                    VERDE
            );

            lblInterpretacion.setText(
                    "Se agregarán "
                    + formatearCantidad(
                            diferencia
                    )
                    + " unidades al stock."
            );

        } else if (diferencia < 0) {

            lblDiferencia.setText(
                    formatearCantidad(
                            diferencia
                    )
            );

            lblDiferencia.setForeground(
                    ROJO
            );

            lblInterpretacion.setText(
                    "Se descontarán "
                    + formatearCantidad(
                            Math.abs(
                                    diferencia
                            )
                    )
                    + " unidades del stock."
            );

        } else {

            lblDiferencia.setText(
                    "0"
            );

            lblDiferencia.setForeground(
                    AZUL_SERENA
            );

            lblInterpretacion.setText(
                    "El conteo físico coincide con el stock del sistema."
            );
        }
    }

    //==========================================================
    // APLICAR AJUSTE
    //==========================================================
    private void aplicarAjuste() {

        //======================================================
        // PRODUCTO
        //======================================================
        if (codigoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un artículo.",
                    "Ajuste de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        //======================================================
        // STOCK FISICO
        //======================================================
        Double stockFisico =
                obtenerStockFisico();

        if (stockFisico == null
                || stockFisico < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un stock físico válido.",
                    "Ajuste de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            txtStockFisico.requestFocus();

            return;
        }

        //======================================================
        // MOTIVO
        //======================================================
        if (comboMotivo.getSelectedIndex()
                == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el motivo del ajuste.",
                    "Ajuste de Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            comboMotivo.requestFocus();

            return;
        }

        //======================================================
        // DIFERENCIA
        //======================================================
        double diferencia =
                stockFisico
                - stockSistema;

        if (diferencia == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existe diferencia entre el stock físico y el stock del sistema.",
                    "Sin diferencias",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        //======================================================
        // CONFIRMACION
        //======================================================
        String signo =
                diferencia > 0
                        ? "+"
                        : "";

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea aplicar este ajuste?\n\n"
                        + "Producto: "
                        + productoSeleccionado
                        + "\n"
                        + "Stock anterior: "
                        + formatearCantidad(
                                stockSistema
                        )
                        + "\n"
                        + "Stock físico: "
                        + formatearCantidad(
                                stockFisico
                        )
                        + "\n"
                        + "Diferencia: "
                        + signo
                        + formatearCantidad(
                                diferencia
                        )
                        + "\n"
                        + "Motivo: "
                        + comboMotivo
                                .getSelectedItem(),
                        "Confirmar Ajuste",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (opcion
                != JOptionPane.YES_OPTION) {

            return;
        }

        confirmado =
                true;

        //======================================================
        // POR AHORA SOLO VISUAL
        //======================================================
        JOptionPane.showMessageDialog(
                this,
                "Ajuste preparado correctamente.\n\n"
                + "Stock anterior: "
                + formatearCantidad(
                        stockSistema
                )
                + "\n"
                + "Stock nuevo: "
                + formatearCantidad(
                        stockFisico
                )
                + "\n"
                + "Diferencia: "
                + signo
                + formatearCantidad(
                        diferencia
                )
                + "\n\n"
                + "Más adelante este cambio se guardará en MySQL\n"
                + "y se registrará en el historial de movimientos.",
                "Ajuste Registrado",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    //==========================================================
    // OBTENER STOCK FISICO
    //==========================================================
    private Double obtenerStockFisico() {

        try {

            String texto =
                    txtStockFisico
                            .getText()
                            .trim()
                            .replace(
                                    ",",
                                    "."
                            );

            if (texto.isEmpty()) {

                return null;
            }

            return Double.parseDouble(
                    texto
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    //==========================================================
    // LIMPIAR
    //==========================================================
    private void limpiarFormulario() {

        codigoSeleccionado =
                null;

        productoSeleccionado =
                null;

        marcaSeleccionada =
                null;

        categoriaSeleccionada =
                null;

        stockSistema =
                0;

        txtProducto.setText(
                ""
        );

        lblCodigo.setText(
                "-"
        );

        lblMarca.setText(
                "-"
        );

        lblCategoria.setText(
                "-"
        );

        lblStockSistema.setText(
                "-"
        );

        txtStockFisico.setText(
                ""
        );

        lblDiferencia.setText(
                "0"
        );

        lblDiferencia.setForeground(
                AZUL_SERENA
        );

        lblInterpretacion.setText(
                "Seleccione un artículo e ingrese el stock físico"
        );

        comboMotivo.setSelectedIndex(
                0
        );

        txtObservaciones.setText(
                ""
        );
    }

    //==========================================================
    // TARJETA
    //==========================================================
    private JPanel crearTarjetaSeccion(
            String titulo) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
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
                )
        );

        return panel;
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        8,
                        10,
                        8,
                        10
                );

        c.anchor =
                GridBagConstraints.WEST;

        return c;
    }

    //==========================================================
    // CAMPO
    //==========================================================
    private JTextField crearCampoTexto() {

        JTextField campo =
                new JTextField();

        campo.setPreferredSize(
                new Dimension(
                        260,
                        34
                )
        );

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        return campo;
    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // VALOR
    //==========================================================
    private JLabel crearValorDetalle(
            String texto) {

        JLabel label =
                new JLabel(
                        texto
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                TEXTO
        );

        return label;
    }

    //==========================================================
    // BOTON BASE
    //==========================================================
    private JButton crearBotonBase(
            String texto,
            int ancho) {

        JButton boton =
                new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        38
                )
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        boton.setFocusPainted(
                false
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    //==========================================================
    // BOTON PRIMARIO
    //==========================================================
    private JButton crearBotonPrimario(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                AZUL_SERENA
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        AZUL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        AZUL_SERENA
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON METAL
    //==========================================================
    private JButton crearBotonMetal(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_METAL
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_METAL
                );
            }
        });

        return boton;
    }

    //==========================================================
    // BOTON CLARO
    //==========================================================
    private JButton crearBotonClaro(
            String texto,
            int ancho) {

        JButton boton =
                crearBotonBase(
                        texto,
                        ancho
                );

        boton.setBackground(
                GRIS_CLARO
        );

        boton.setForeground(
                TEXTO
        );

        boton.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_CLARO_HOVER
                );
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent e) {

                boton.setBackground(
                        GRIS_CLARO
                );
            }
        });

        return boton;
    }

    //==========================================================
    // FORMATEAR CANTIDAD
    //==========================================================
    private String formatearCantidad(
            double valor) {

        DecimalFormat formato =
                new DecimalFormat(
                        "#,##0.###"
                );

        return formato.format(
                valor
        );
    }

    //==========================================================
    // GETTERS PARA LOGICA FUTURA
    //==========================================================
    public boolean isConfirmado() {

        return confirmado;
    }

    public String getCodigoSeleccionado() {

        return codigoSeleccionado;
    }

    public String getProductoSeleccionado() {

        return productoSeleccionado;
    }

    public double getStockSistema() {

        return stockSistema;
    }

    public double getStockFisico() {

        Double valor =
                obtenerStockFisico();

        return valor == null
                ? 0
                : valor;
    }

    public double getDiferencia() {

        return getStockFisico()
                - stockSistema;
    }

    public String getMotivo() {

        Object valor =
                comboMotivo
                        .getSelectedItem();

        return valor == null
                ? ""
                : valor.toString();
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }
}