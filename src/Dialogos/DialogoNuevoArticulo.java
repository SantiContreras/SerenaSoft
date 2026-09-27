package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import rojerusan.RSComboBox;

public class DialogoNuevoArticulo extends JDialog {

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

    private static final Color BLANCO =
            Color.WHITE;

    private static final Color BORDE =
            new Color(205, 215, 228);

    private static final Color TEXTO =
            new Color(45, 52, 62);

    private static final Color TEXTO_SECUNDARIO =
            new Color(100, 108, 120);

    private static final Color GRIS_METAL =
            new Color(70, 78, 88);

    private static final Color GRIS_METAL_HOVER =
            new Color(88, 98, 110);

    private static final Color GRIS_CLARO =
            new Color(222, 227, 233);

    private static final Color GRIS_CLARO_HOVER =
            new Color(205, 212, 220);

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JTextField txtCodigo;
    private JTextField txtCodigoBarra;
    private JTextField txtCodigoInterno;
    private JTextField txtDescripcion;
    private JTextField txtUbicacion;

    private RSComboBox comboMarca;
    private RSComboBox comboCategoria;
    private RSComboBox comboRubro;

    private RSComboBox comboUnidadCompra;
    private RSComboBox comboUnidadVenta;

    //==========================================================
    // PROVEEDOR
    //==========================================================
    private JTextField txtProveedor;
    private JButton btnBuscarProveedor;

    private int idProveedorSeleccionado = -1;

    //==========================================================
    // STOCK
    //==========================================================
    private JTextField txtStockActual;
    private JTextField txtStockMinimo;

    private RSComboBox comboUnidadStock;

    //==========================================================
    // PRECIOS
    //==========================================================
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JTextField txtMargen;

    private RSComboBox comboIVA;

    //==========================================================
    // OPCIONES
    //==========================================================
    private JCheckBox chkActivo;
    private JCheckBox chkPesable;
    private JCheckBox chkControlStock;
    private JCheckBox chkPermiteDescuento;

    //==========================================================
    // OBSERVACIONES
    //==========================================================
    private JTextArea txtObservaciones;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnLimpiar;
    private JButton btnCancelar;
    private JButton btnGuardar;

    //==========================================================
    // RESULTADO
    //==========================================================
    private boolean guardado = false;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoNuevoArticulo(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        cargarDatosPrueba();

        configurarEventos();

        setTitle(
                "Nuevo Artículo"
        );

        setModal(true);

        setSize(
                new Dimension(
                        880,
                        690
                )
        );

        setMinimumSize(
                new Dimension(
                        760,
                        580
                )
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        setResizable(true);
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setLocationRelativeTo(
                parent
        );
    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // CAMPOS GENERALES
        //======================================================
        txtCodigo =
                crearCampoTexto();

        txtCodigoBarra =
                crearCampoTexto();

        txtCodigoInterno =
                crearCampoTexto();

        txtDescripcion =
                crearCampoTexto();

        txtUbicacion =
                crearCampoTexto();

        //======================================================
        // COMBOS
        //======================================================
        comboMarca =
                crearCombo();

        comboCategoria =
                crearCombo();

        comboRubro =
                crearCombo();

        comboUnidadCompra =
                crearCombo();

        comboUnidadVenta =
                crearCombo();

        //======================================================
        // PROVEEDOR
        //======================================================
        txtProveedor =
                crearCampoTexto();

        txtProveedor.setEditable(
                false
        );

        txtProveedor.setBackground(
                new Color(
                        248,
                        249,
                        251
                )
        );

        btnBuscarProveedor =
                crearBotonPrimario(
                        "BUSCAR",
                        100
                );

        //======================================================
        // STOCK
        //======================================================
        txtStockActual =
                crearCampoTexto();

        txtStockMinimo =
                crearCampoTexto();

        comboUnidadStock =
                crearCombo();

        //======================================================
        // PRECIOS
        //======================================================
        txtPrecioCompra =
                crearCampoTexto();

        txtPrecioVenta =
                crearCampoTexto();

        txtMargen =
                crearCampoTexto();

        comboIVA =
                crearCombo();

        //======================================================
        // CHECKBOX
        //======================================================
        chkActivo =
                new JCheckBox(
                        "Producto activo"
                );

        chkPesable =
                new JCheckBox(
                        "Producto pesable"
                );

        chkControlStock =
                new JCheckBox(
                        "Controlar stock"
                );

        chkPermiteDescuento =
                new JCheckBox(
                        "Permitir descuento"
                );

        configurarCheck(
                chkActivo
        );

        configurarCheck(
                chkPesable
        );

        configurarCheck(
                chkControlStock
        );

        configurarCheck(
                chkPermiteDescuento
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
        // BOTONES FOOTER
        //======================================================
        btnLimpiar =
                crearBotonClaro(
                        "LIMPIAR",
                        115
                );

        btnCancelar =
                crearBotonMetal(
                        "CANCELAR",
                        125
                );

        btnGuardar =
                crearBotonPrimario(
                        "GUARDAR ARTÍCULO",
                        175
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
        // HEADER FIJO
        //======================================================
        JPanel header =
                crearHeader();

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO CON SCROLL
        //======================================================
        JPanel contenido =
                crearContenido();

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
                        16
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
        JPanel footer =
                crearFooter();

        add(
                footer,
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
                        "NUEVO ARTÍCULO"
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
                        "Complete la información necesaria para registrar el producto"
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

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        subtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
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
    // CONTENIDO
    //==========================================================
    private JPanel crearContenido() {

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

        JPanel datosGenerales =
                crearPanelDatosGenerales();

        JPanel stock =
                crearPanelStock();

        JPanel precios =
                crearPanelPrecios();

        JPanel opciones =
                crearPanelOpciones();

        JPanel observaciones =
                crearPanelObservaciones();

        datosGenerales.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        stock.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        precios.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        opciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        observaciones.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contenido.add(
                datosGenerales
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                stock
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                precios
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                opciones
        );

        contenido.add(
                Box.createVerticalStrut(
                        12
                )
        );

        contenido.add(
                observaciones
        );

        contenido.add(
                Box.createVerticalStrut(
                        10
                )
        );

        return contenido;
    }

    //==========================================================
    // DATOS GENERALES
    //==========================================================
    private JPanel crearPanelDatosGenerales() {

        JPanel panel =
                crearTarjetaSeccion(
                        "DATOS GENERALES"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // FILA 0
        // Código | Código barras
        //======================================================
        agregarCampoDosColumnas(
                panel,
                c,
                0,
                0,
                "Código",
                txtCodigo
        );

        agregarCampoDosColumnas(
                panel,
                c,
                0,
                2,
                "Código de Barras",
                txtCodigoBarra
        );

        //======================================================
        // FILA 1
        // Código interno | Ubicación
        //======================================================
        agregarCampoDosColumnas(
                panel,
                c,
                1,
                0,
                "Código Interno",
                txtCodigoInterno
        );

        agregarCampoDosColumnas(
                panel,
                c,
                1,
                2,
                "Ubicación",
                txtUbicacion
        );

        //======================================================
        // FILA 2
        // DESCRIPCIÓN COMPLETA
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Descripción"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                txtDescripcion,
                c
        );

        c.gridwidth = 1;

        //======================================================
        // FILA 3
        // MARCA | CATEGORIA
        //======================================================
        agregarComboDosColumnas(
                panel,
                c,
                3,
                0,
                "Marca",
                comboMarca
        );

        agregarComboDosColumnas(
                panel,
                c,
                3,
                2,
                "Categoría",
                comboCategoria
        );

        //======================================================
        // FILA 4
        // RUBRO | PROVEEDOR
        //======================================================
        agregarComboDosColumnas(
                panel,
                c,
                4,
                0,
                "Rubro",
                comboRubro
        );

        //======================================================
        // PROVEEDOR - LABEL
        //======================================================
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 0;
        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        "Proveedor"
                ),
                c
        );

        //======================================================
        // PANEL PROVEEDOR
        //======================================================
        JPanel panelProveedor =
                new JPanel(
                        new BorderLayout(
                                7,
                                0
                        )
                );

        panelProveedor.setOpaque(
                false
        );

        panelProveedor.add(
                txtProveedor,
                BorderLayout.CENTER
        );

        panelProveedor.add(
                btnBuscarProveedor,
                BorderLayout.EAST
        );

        c.gridx = 3;
        c.gridy = 4;
        c.weightx = 1;
        c.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                panelProveedor,
                c
        );

        //======================================================
        // FILA 5
        // UNIDAD COMPRA | UNIDAD VENTA
        //======================================================
        agregarComboDosColumnas(
                panel,
                c,
                5,
                0,
                "Unidad Compra",
                comboUnidadCompra
        );

        agregarComboDosColumnas(
                panel,
                c,
                5,
                2,
                "Unidad Venta",
                comboUnidadVenta
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        320
                )
        );

        return panel;
    }

    //==========================================================
    // STOCK
    //==========================================================
    private JPanel crearPanelStock() {

        JPanel panel =
                crearTarjetaSeccion(
                        "STOCK"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // STOCK ACTUAL
        //======================================================
        agregarCampoDosColumnas(
                panel,
                c,
                0,
                0,
                "Stock Actual",
                txtStockActual
        );

        agregarCampoDosColumnas(
                panel,
                c,
                0,
                2,
                "Stock Mínimo",
                txtStockMinimo
        );

        //======================================================
        // UNIDAD
        //======================================================
        agregarComboDosColumnas(
                panel,
                c,
                1,
                0,
                "Unidad",
                comboUnidadStock
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        return panel;
    }

    //==========================================================
    // PRECIOS
    //==========================================================
    private JPanel crearPanelPrecios() {

        JPanel panel =
                crearTarjetaSeccion(
                        "PRECIOS"
                );

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // COMPRA | VENTA
        //======================================================
        agregarCampoDosColumnas(
                panel,
                c,
                0,
                0,
                "Precio Compra",
                txtPrecioCompra
        );

        agregarCampoDosColumnas(
                panel,
                c,
                0,
                2,
                "Precio Venta",
                txtPrecioVenta
        );

        //======================================================
        // MARGEN | IVA
        //======================================================
        agregarCampoDosColumnas(
                panel,
                c,
                1,
                0,
                "Margen %",
                txtMargen
        );

        agregarComboDosColumnas(
                panel,
                c,
                1,
                2,
                "IVA",
                comboIVA
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        return panel;
    }

    //==========================================================
    // OPCIONES
    //==========================================================
    private JPanel crearPanelOpciones() {

        JPanel panel =
                crearTarjetaSeccion(
                        "OPCIONES"
                );

        panel.setLayout(
                new GridLayout(
                        2,
                        2,
                        15,
                        8
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        panel.getBorder(),

                        new EmptyBorder(
                                10,
                                18,
                                12,
                                18
                        )
                )
        );

        panel.add(
                chkActivo
        );

        panel.add(
                chkPesable
        );

        panel.add(
                chkControlStock
        );

        panel.add(
                chkPermiteDescuento
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        return panel;
    }

    //==========================================================
    // OBSERVACIONES
    //==========================================================
    private JPanel crearPanelObservaciones() {

        JPanel panel =
                crearTarjetaSeccion(
                        "OBSERVACIONES"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JScrollPane scroll =
                new JScrollPane(
                        txtObservaciones
                );

        scroll.setPreferredSize(
                new Dimension(
                        600,
                        90
                )
        );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                195,
                                202,
                                210
                        )
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        return panel;
    }

    //==========================================================
    // FOOTER FIJO
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
                btnGuardar
        );

        return footer;
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        //======================================================
        // MARCAS
        //======================================================
        comboMarca.removeAllItems();

        comboMarca.addItem(
                "Seleccione..."
        );

        comboMarca.addItem(
                "Coca Cola"
        );

        comboMarca.addItem(
                "Natura"
        );

        comboMarca.addItem(
                "Playadito"
        );

        comboMarca.addItem(
                "Ledesma"
        );

        comboMarca.addItem(
                "La Serenísima"
        );

        //======================================================
        // CATEGORIAS
        //======================================================
        comboCategoria.removeAllItems();

        comboCategoria.addItem(
                "Seleccione..."
        );

        comboCategoria.addItem(
                "Bebidas"
        );

        comboCategoria.addItem(
                "Almacén"
        );

        comboCategoria.addItem(
                "Lácteos"
        );

        comboCategoria.addItem(
                "Carnes"
        );

        comboCategoria.addItem(
                "Limpieza"
        );

        comboCategoria.addItem(
                "Perfumería"
        );

        //======================================================
        // RUBROS
        //======================================================
        comboRubro.removeAllItems();

        comboRubro.addItem(
                "Seleccione..."
        );

        comboRubro.addItem(
                "Alimentos"
        );

        comboRubro.addItem(
                "Bebidas"
        );

        comboRubro.addItem(
                "Limpieza"
        );

        comboRubro.addItem(
                "Higiene"
        );

        //======================================================
        // UNIDAD COMPRA
        //======================================================
        comboUnidadCompra.removeAllItems();

        comboUnidadCompra.addItem(
                "Unidad"
        );

        comboUnidadCompra.addItem(
                "Caja"
        );

        comboUnidadCompra.addItem(
                "Pack"
        );

        comboUnidadCompra.addItem(
                "Kg"
        );

        comboUnidadCompra.addItem(
                "Litro"
        );

        //======================================================
        // UNIDAD VENTA
        //======================================================
        comboUnidadVenta.removeAllItems();

        comboUnidadVenta.addItem(
                "Unidad"
        );

        comboUnidadVenta.addItem(
                "Kg"
        );

        comboUnidadVenta.addItem(
                "Gramo"
        );

        comboUnidadVenta.addItem(
                "Litro"
        );

        //======================================================
        // UNIDAD STOCK
        //======================================================
        comboUnidadStock.removeAllItems();

        comboUnidadStock.addItem(
                "Unidad"
        );

        comboUnidadStock.addItem(
                "Kg"
        );

        comboUnidadStock.addItem(
                "Litro"
        );

        comboUnidadStock.addItem(
                "Caja"
        );

        //======================================================
        // IVA
        //======================================================
        comboIVA.removeAllItems();

        comboIVA.addItem(
                "21%"
        );

        comboIVA.addItem(
                "10.5%"
        );

        comboIVA.addItem(
                "27%"
        );

        comboIVA.addItem(
                "0%"
        );

        //======================================================
        // DEFAULTS
        //======================================================
        chkActivo.setSelected(
                true
        );

        chkControlStock.setSelected(
                true
        );
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // BUSCAR PROVEEDOR
        //======================================================
        btnBuscarProveedor.addActionListener(e -> {

            DialogoBuscarProveedor dialogo =
                    new DialogoBuscarProveedor(
                            this
                    );

            dialogo.setVisible(
                    true
            );

            if (dialogo.isSeleccionado()) {

                idProveedorSeleccionado =
                        dialogo.getIdSeleccionado();

                txtProveedor.setText(
                        dialogo.getRazonSocialSeleccionada()
                );
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

            guardado =
                    false;

            dispose();
        });

        //======================================================
        // GUARDAR
        //======================================================
        btnGuardar.addActionListener(e -> {

            if (!validarFormulario()) {

                return;
            }

            guardado =
                    true;

            JOptionPane.showMessageDialog(
                    this,
                    "Artículo preparado correctamente.\n"
                    + "La persistencia se conectará más adelante.",
                    "Nuevo Artículo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
        });
    }

    //==========================================================
    // VALIDAR
    //==========================================================
    private boolean validarFormulario() {

        //======================================================
        // CODIGO
        //======================================================
        if (txtCodigo
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el código del artículo.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCodigo.requestFocus();

            return false;
        }

        //======================================================
        // DESCRIPCION
        //======================================================
        if (txtDescripcion
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la descripción del artículo.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtDescripcion.requestFocus();

            return false;
        }

        //======================================================
        // PROVEEDOR OPCIONAL POR AHORA
        //======================================================
        /*
         * Si más adelante querés que proveedor sea obligatorio:
         *
         * if (idProveedorSeleccionado == -1) {
         *
         *     JOptionPane.showMessageDialog(...);
         *
         *     return false;
         * }
         */

        return true;
    }

    //==========================================================
    // LIMPIAR FORMULARIO
    //==========================================================
    private void limpiarFormulario() {

        //======================================================
        // DATOS GENERALES
        //======================================================
        txtCodigo.setText("");

        txtCodigoBarra.setText("");

        txtCodigoInterno.setText("");

        txtDescripcion.setText("");

        txtUbicacion.setText("");

        //======================================================
        // PROVEEDOR
        //======================================================
        idProveedorSeleccionado =
                -1;

        txtProveedor.setText("");

        //======================================================
        // STOCK
        //======================================================
        txtStockActual.setText("");

        txtStockMinimo.setText("");

        //======================================================
        // PRECIOS
        //======================================================
        txtPrecioCompra.setText("");

        txtPrecioVenta.setText("");

        txtMargen.setText("");

        //======================================================
        // OBSERVACIONES
        //======================================================
        txtObservaciones.setText("");

        //======================================================
        // COMBOS
        //======================================================
        if (comboMarca.getItemCount() > 0) {

            comboMarca.setSelectedIndex(
                    0
            );
        }

        if (comboCategoria.getItemCount() > 0) {

            comboCategoria.setSelectedIndex(
                    0
            );
        }

        if (comboRubro.getItemCount() > 0) {

            comboRubro.setSelectedIndex(
                    0
            );
        }

        if (comboUnidadCompra.getItemCount() > 0) {

            comboUnidadCompra.setSelectedIndex(
                    0
            );
        }

        if (comboUnidadVenta.getItemCount() > 0) {

            comboUnidadVenta.setSelectedIndex(
                    0
            );
        }

        if (comboUnidadStock.getItemCount() > 0) {

            comboUnidadStock.setSelectedIndex(
                    0
            );
        }

        if (comboIVA.getItemCount() > 0) {

            comboIVA.setSelectedIndex(
                    0
            );
        }

        //======================================================
        // CHECKS
        //======================================================
        chkActivo.setSelected(
                true
        );

        chkPesable.setSelected(
                false
        );

        chkControlStock.setSelected(
                true
        );

        chkPermiteDescuento.setSelected(
                false
        );

        txtCodigo.requestFocus();
    }

    //==========================================================
    // AGREGAR CAMPO DOS COLUMNAS
    //==========================================================
    private void agregarCampoDosColumnas(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            int columna,
            String titulo,
            JTextField campo) {

        //======================================================
        // LABEL
        //======================================================
        c.gridx =
                columna;

        c.gridy =
                fila;

        c.weightx =
                0;

        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        titulo
                ),
                c
        );

        //======================================================
        // CAMPO
        //======================================================
        c.gridx =
                columna + 1;

        c.weightx =
                1;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        campo.setPreferredSize(
                new Dimension(
                        240,
                        34
                )
        );

        panel.add(
                campo,
                c
        );
    }

    //==========================================================
    // AGREGAR COMBO DOS COLUMNAS
    //==========================================================
    private void agregarComboDosColumnas(
            JPanel panel,
            GridBagConstraints c,
            int fila,
            int columna,
            String titulo,
            JComboBox combo) {

        //======================================================
        // LABEL
        //======================================================
        c.gridx =
                columna;

        c.gridy =
                fila;

        c.weightx =
                0;

        c.fill =
                GridBagConstraints.NONE;

        panel.add(
                crearLabel(
                        titulo
                ),
                c
        );

        //======================================================
        // COMBO
        //======================================================
        c.gridx =
                columna + 1;

        c.weightx =
                1;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        combo.setPreferredSize(
                new Dimension(
                        240,
                        34
                )
        );

        panel.add(
                combo,
                c
        );
    }

    //==========================================================
    // TARJETA SECCION
    //==========================================================
    private JPanel crearTarjetaSeccion(
            String titulo) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                BLANCO
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
    // GRID BAG
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        7,
                        9,
                        7,
                        9
                );

        c.anchor =
                GridBagConstraints.WEST;

        return c;
    }

    //==========================================================
    // CAMPO TEXTO
    //==========================================================
    private JTextField crearCampoTexto() {

        JTextField campo =
                new JTextField();

        campo.setPreferredSize(
                new Dimension(
                        220,
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
    // COMBO
    //==========================================================
    private RSComboBox crearCombo() {

        RSComboBox combo =
                new RSComboBox();

        combo.setPreferredSize(
                new Dimension(
                        220,
                        34
                )
        );

        combo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        return combo;
    }

    //==========================================================
    // CHECKBOX
    //==========================================================
    private void configurarCheck(
            JCheckBox check) {

        check.setOpaque(
                false
        );

        check.setForeground(
                TEXTO
        );

        check.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );
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
    // BOTON PRIMARIO
    //==========================================================
    private JButton crearBotonPrimario(
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

        boton.setBackground(
                AZUL_SERENA
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

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
                new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        38
                )
        );

        boton.setBackground(
                GRIS_METAL
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

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
                new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        38
                )
        );

        boton.setBackground(
                GRIS_CLARO
        );

        boton.setForeground(
                TEXTO
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

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
    // GETTERS PARA LOGICA FUTURA
    //==========================================================
    public boolean isGuardado() {

        return guardado;
    }

    public String getCodigo() {

        return txtCodigo
                .getText()
                .trim();
    }

    public String getCodigoBarra() {

        return txtCodigoBarra
                .getText()
                .trim();
    }

    public String getCodigoInterno() {

        return txtCodigoInterno
                .getText()
                .trim();
    }

    public String getDescripcion() {

        return txtDescripcion
                .getText()
                .trim();
    }

    public String getUbicacion() {

        return txtUbicacion
                .getText()
                .trim();
    }

    public int getIdProveedorSeleccionado() {

        return idProveedorSeleccionado;
    }

    public String getProveedorSeleccionado() {

        return txtProveedor
                .getText()
                .trim();
    }

    public boolean isProductoActivo() {

        return chkActivo.isSelected();
    }

    public boolean isProductoPesable() {

        return chkPesable.isSelected();
    }

    public boolean isControlStock() {

        return chkControlStock.isSelected();
    }

    public boolean isPermiteDescuento() {

        return chkPermiteDescuento.isSelected();
    }

    public String getObservaciones() {

        return txtObservaciones
                .getText()
                .trim();
    }
}