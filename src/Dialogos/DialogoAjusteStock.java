package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.awt.geom.*;
import Dao.DepositoDao;
import Dao.StockProductoDao;
import model.Deposito;
import model.Producto;
import services.AjusteStockService;
import services.ResultadoOperacion;

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
    private BigDecimal stockSistemaExacto = BigDecimal.ZERO;
    private Producto objetoSeleccionado;
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoDao stockDao = new StockProductoDao();
    private final AjusteStockService ajusteService = new AjusteStockService();
    private Deposito depositoPrincipal;

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
        depositoPrincipal = depositoDao.buscarPrincipal();

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

        panel.setBackground(AZUL_OSCURO);
        panel.setBorder(new EmptyBorder(20, 25, 19, 25));

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

        titulo.setForeground(Color.WHITE);
        titulo.setIcon(icono("ajuste", Color.WHITE, 26));
        titulo.setIconTextGap(12);

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

        subtitulo.setForeground(new Color(218, 231, 250));

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
                        "⌕  BUSCAR ARTÍCULO"
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
                        "▤  CONTEO DE INVENTARIO"
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
                        "✎  INFORMACIÓN DEL AJUSTE"
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

            objetoSeleccionado = dialogo.getObjetoProductoSeleccionado();
            if (objetoSeleccionado == null || depositoPrincipal == null) {
                JOptionPane.showMessageDialog(this, "Producto o depósito principal no disponible.");
                return;
            }
            codigoSeleccionado = objetoSeleccionado.getCodigo();
            productoSeleccionado = objetoSeleccionado.getNombre();
            marcaSeleccionada = objetoSeleccionado.getMarca() == null
                    ? "-" : objetoSeleccionado.getMarca().getNombre();
            categoriaSeleccionada = objetoSeleccionado.getCategoria() == null
                    ? "-" : objetoSeleccionado.getCategoria().getNombre();
            stockSistemaExacto = stockDao.obtenerCantidad(
                    objetoSeleccionado.getIdProducto(), depositoPrincipal.getIdDeposito());
            stockSistema = stockSistemaExacto.doubleValue();

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
        BigDecimal fisico = obtenerStockFisicoExacto();
        if (objetoSeleccionado == null || fisico == null || fisico.signum() < 0) {
            lblDiferencia.setText("0");
            lblDiferencia.setForeground(AZUL_SERENA);
            lblInterpretacion.setText("Seleccione un artículo e ingrese un conteo válido");
            return;
        }
        BigDecimal diferencia = fisico.subtract(stockSistemaExacto);
        lblDiferencia.setText((diferencia.signum() > 0 ? "+" : "") + formatearCantidad(diferencia));
        lblDiferencia.setForeground(diferencia.signum() > 0 ? VERDE
                : diferencia.signum() < 0 ? ROJO : AZUL_SERENA);
        lblInterpretacion.setText(diferencia.signum() > 0
                ? "Se agregarán " + formatearCantidad(diferencia) + " unidades al stock."
                : diferencia.signum() < 0
                ? "Se descontarán " + formatearCantidad(diferencia.abs()) + " unidades del stock."
                : "El conteo coincide con el stock del sistema.");
    }

    //==========================================================
    // APLICAR AJUSTE
    //==========================================================
    private void aplicarAjuste() {
        if (objetoSeleccionado == null || depositoPrincipal == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo y un depósito.");
            return;
        }
        BigDecimal fisico = obtenerStockFisicoExacto();
        if (fisico == null || fisico.signum() < 0 || fisico.stripTrailingZeros().scale() > 3) {
            JOptionPane.showMessageDialog(this, "Ingrese un stock físico válido (máximo 3 decimales).");
            return;
        }
        if (comboMotivo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el motivo del ajuste.");
            return;
        }
        if (objetoSeleccionado.getUnidadVenta() != null
                && !objetoSeleccionado.getUnidadVenta().isPermiteDecimales()
                && fisico.stripTrailingZeros().scale() > 0) {
            JOptionPane.showMessageDialog(this, "Este producto solo admite cantidades enteras.");
            return;
        }
        BigDecimal diferencia = fisico.subtract(stockSistemaExacto);
        if (diferencia.signum() == 0) {
            JOptionPane.showMessageDialog(this, "No hay diferencia de stock.");
            return;
        }
        String motivo = comboMotivo.getSelectedItem().toString();
        if (!mostrarConfirmacionAjuste(fisico, diferencia, motivo)) return;

        btnAplicar.setEnabled(false);
        try {
            ResultadoOperacion resultado = ajusteService.aplicarAjuste(
                    depositoPrincipal.getIdDeposito(), objetoSeleccionado.getIdProducto(),
                    stockSistemaExacto, fisico, motivo, txtObservaciones.getText().trim());
            if (!resultado.isExitoso()) {
                JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                        "No se pudo aplicar el ajuste", JOptionPane.WARNING_MESSAGE);
                return;
            }
            confirmado = true;
            mostrarExitoAjuste(resultado.getMensaje(), fisico, diferencia, motivo);
            dispose();
        } finally {
            btnAplicar.setEnabled(true);
        }
    }

    //==========================================================
    // ICONOS VECTORIALES - SIN LIBRERIAS NI IMAGENES EXTERNAS
    //==========================================================
    private Icon icono(String tipo, Color color, int tam) {
        return new Icon() {
            @Override public int getIconWidth() { return tam; }
            @Override public int getIconHeight() { return tam; }
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.translate(x, y);
                    g2.scale(tam / 24.0, tam / 24.0);
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (tipo) {
                        case "buscar" -> {
                            g2.drawOval(3, 3, 13, 13);
                            g2.drawLine(15, 15, 22, 22);
                        }
                        case "check" -> {
                            g2.drawOval(2, 2, 20, 20);
                            g2.drawLine(6, 12, 10, 16);
                            g2.drawLine(10, 16, 18, 8);
                        }
                        case "cerrar" -> {
                            g2.drawLine(5, 5, 19, 19);
                            g2.drawLine(19, 5, 5, 19);
                        }
                        case "limpiar" -> {
                            g2.drawRoundRect(5, 3, 14, 18, 2, 2);
                            g2.drawLine(8, 8, 16, 8);
                            g2.drawLine(8, 12, 16, 12);
                        }
                        case "caja" -> {
                            g2.drawRect(3, 7, 18, 14);
                            g2.drawLine(3, 7, 7, 3);
                            g2.drawLine(21, 7, 17, 3);
                            g2.drawLine(7, 3, 17, 3);
                            g2.drawLine(12, 7, 12, 21);
                        }
                        case "codigo" -> {
                            g2.drawLine(3, 5, 3, 19);
                            g2.drawLine(7, 5, 7, 19);
                            g2.drawLine(12, 5, 12, 19);
                            g2.drawLine(16, 5, 16, 19);
                            g2.drawLine(21, 5, 21, 19);
                        }
                        case "etiqueta" -> {
                            Path2D.Double ruta = new Path2D.Double();
                            ruta.moveTo(3, 4); ruta.lineTo(15, 4); ruta.lineTo(22, 12);
                            ruta.lineTo(15, 20); ruta.lineTo(3, 20); ruta.closePath();
                            g2.draw(ruta);
                            g2.fillOval(14, 10, 4, 4);
                        }
                        case "conteo" -> {
                            g2.drawRoundRect(4, 3, 16, 19, 3, 3);
                            g2.drawLine(8, 8, 16, 8);
                            g2.drawLine(8, 12, 16, 12);
                            g2.drawLine(8, 16, 13, 16);
                        }
                        case "nota" -> {
                            g2.drawRoundRect(4, 2, 16, 20, 2, 2);
                            g2.drawLine(8, 8, 16, 8);
                            g2.drawLine(8, 12, 16, 12);
                            g2.drawLine(8, 16, 13, 16);
                        }
                        default -> {
                            g2.drawOval(2, 2, 20, 20);
                            g2.drawLine(12, 5, 12, 19);
                            g2.drawLine(5, 12, 19, 12);
                        }
                    }
                } finally { g2.dispose(); }
            }
        };
    }

    //==========================================================
    // DIALOGOS SERENA SOFT - CONFIRMACION Y EXITO
    //==========================================================
    private JPanel crearCabeceraMensaje(String titulo, String subtitulo, boolean exito) {
        JPanel cabecera = new JPanel(new BorderLayout(13, 0));
        cabecera.setOpaque(false);
        JLabel simbolo = new JLabel(icono(exito ? "check" : "ajuste",
                exito ? VERDE : AZUL_SERENA, 42));
        cabecera.add(simbolo, BorderLayout.WEST);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLabel.setForeground(AZUL_OSCURO);
        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtituloLabel.setForeground(TEXTO_SECUNDARIO);
        textos.add(tituloLabel);
        textos.add(Box.createVerticalStrut(5));
        textos.add(subtituloLabel);
        cabecera.add(textos, BorderLayout.CENTER);
        return cabecera;
    }

    private JPanel crearPanelMensaje(String titulo, String subtitulo,
                                    String[][] datos, boolean exito) {
        JPanel cuerpo = new JPanel(new BorderLayout(0, 16));
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(new EmptyBorder(18, 20, 15, 20));
        cuerpo.add(crearCabeceraMensaje(titulo, subtitulo, exito), BorderLayout.NORTH);

        JPanel detalle = new JPanel(new GridBagLayout());
        detalle.setBackground(new Color(244, 247, 252));
        detalle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), new EmptyBorder(10, 12, 10, 12)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 7, 5, 7);
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < datos.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0;
            JLabel clave = new JLabel(datos[i][0]);
            clave.setFont(new Font("Segoe UI", Font.BOLD, 12));
            clave.setForeground(TEXTO_SECUNDARIO);
            detalle.add(clave, c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            JLabel valor = new JLabel(datos[i][1]);
            valor.setFont(new Font("Segoe UI", Font.BOLD, 12));
            valor.setForeground(datos[i][0].equals("Diferencia:")
                    ? (datos[i][1].startsWith("+") ? VERDE : ROJO) : AZUL_OSCURO);
            detalle.add(valor, c);
            c.fill = GridBagConstraints.NONE;
        }
        cuerpo.add(detalle, BorderLayout.CENTER);
        return cuerpo;
    }

    private String[][] datosAjuste(BigDecimal fisico, BigDecimal diferencia, String motivo) {
        return new String[][] {
            {"Producto:", productoSeleccionado},
            {"Depósito:", depositoPrincipal.getNombre()},
            {"Stock anterior:", formatearCantidad(stockSistemaExacto)},
            {"Conteo físico:", formatearCantidad(fisico)},
            {"Diferencia:", (diferencia.signum() > 0 ? "+" : "") + formatearCantidad(diferencia)},
            {"Motivo:", motivo}
        };
    }

    private boolean mostrarConfirmacionAjuste(BigDecimal fisico,
                                              BigDecimal diferencia, String motivo) {
        JPanel contenido = crearPanelMensaje("Confirmar ajuste de inventario",
                "Verificá los datos antes de actualizar el stock.",
                datosAjuste(fisico, diferencia, motivo), false);
        Object[] opciones = {"CONFIRMAR AJUSTE", "CANCELAR"};
        int respuesta = JOptionPane.showOptionDialog(this, contenido, "Confirmar ajuste",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[1]);
        return respuesta == 0;
    }

    private void mostrarExitoAjuste(String mensaje, BigDecimal fisico,
                                    BigDecimal diferencia, String motivo) {
        String[][] datos = datosAjuste(fisico, diferencia, motivo);
        String[][] datosFinales = new String[datos.length + 1][2];
        datosFinales[0] = new String[]{"Resultado:", mensaje};
        System.arraycopy(datos, 0, datosFinales, 1, datos.length);
        JPanel contenido = crearPanelMensaje("¡Ajuste registrado correctamente!",
                "El inventario se actualizó y el movimiento quedó registrado.",
                datosFinales, true);
        JOptionPane.showOptionDialog(this, contenido, "Ajuste confirmado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"ACEPTAR"}, "ACEPTAR");
    }

    private BigDecimal obtenerStockFisicoExacto() {
        try {
            String texto = txtStockFisico.getText().trim();
            if (texto.isEmpty()) return null;
            // Admite 1234,5 / 1234.5 / 1.234,5; sin separadores ambiguos.
            if (texto.contains(",")) texto = texto.replace(".", "").replace(',', '.');
            return new BigDecimal(texto);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String formatearCantidad(BigDecimal cantidad) {
        return new java.text.DecimalFormat("#,##0.###").format(cantidad);
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
        stockSistemaExacto = BigDecimal.ZERO;
        objetoSeleccionado = null;

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

        label.setForeground(TEXTO);
        String tipo = switch (texto) {
            case "Producto" -> "buscar";
            case "Código" -> "codigo";
            case "Marca" -> "etiqueta";
            case "Categoría" -> "etiqueta";
            case "Stock del Sistema" -> "caja";
            case "Stock Físico Contado" -> "conteo";
            case "Diferencia" -> "ajuste";
            case "Motivo" -> "nota";
            case "Observaciones" -> "nota";
            default -> "etiqueta";
        };
        label.setIcon(icono(tipo, AZUL_SERENA, 15));
        label.setIconTextGap(6);
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

        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String tipo = texto.contains("BUSCAR") ? "buscar"
                : texto.contains("LIMPIAR") ? "limpiar"
                : texto.contains("CANCELAR") ? "cerrar" : "check";
        boton.setIcon(icono(tipo, texto.contains("LIMPIAR") ? TEXTO : Color.WHITE, 16));
        boton.setIconTextGap(8);
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
    // ESTILO VISUAL UNIFICADO - SOLO PRESENTACIÓN
    //==========================================================
    private void aplicarEstiloVisualSerena() {
        personalizarBoton(btnBuscarProducto, "buscar", Color.WHITE);
        personalizarBoton(btnLimpiar, "limpiar", TEXTO);
        personalizarBoton(btnCancelar, "cerrar", Color.WHITE);
        personalizarBoton(btnAplicar, "check", Color.WHITE);
        btnBuscarProducto.setPreferredSize(new Dimension(118, 40));
        btnAplicar.setPreferredSize(new Dimension(180, 40));
        txtStockFisico.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                new EmptyBorder(4, 10, 4, 10)));
    }

    private void personalizarBoton(JButton boton, String tipo, Color colorIcono) {
        boton.setIcon(icono(tipo, colorIcono, 16));
        boton.setIconTextGap(8);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(7, 10, 7, 10));
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