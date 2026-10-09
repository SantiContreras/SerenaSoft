package Dialogos;

import Dao.DepositoDao;
import Dao.ProductoDao;
import Diseños.EstiloBotones;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Deposito;
import model.Producto;
import services.StockProductoService;

/**
 * Serena Soft - Modificar parámetros de producto y stock.
 * No modifica la cantidad física: para eso se utiliza Ajuste de Stock.
 * Reutiliza DialogoBuscarProducto y conserva el constructor anterior.
 */
public class DialogoModificarStock extends JDialog {

    private static final Color AZUL = new Color(19, 51, 111);
    private static final Color AZUL_BOTON = new Color(29, 73, 150);
    private static final Color FONDO = new Color(237, 242, 249);
    private static final Color TEXTO = new Color(28, 47, 77);
    private static final Color VERDE = new Color(19, 137, 83);
    private static final Color GRIS_BOTON = new Color(76, 88, 107);
    private static final Color VERDE_BOTON = new Color(18, 126, 81);
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FUENTE_BOLD = new Font("Segoe UI", Font.BOLD, 15);

    private final ProductoDao productoDao = new ProductoDao();
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoService stockService = new StockProductoService();

    private Producto productoActual;
    private Deposito depositoActual;
    private boolean confirmado;

    private JTextField txtProducto, txtNombre, txtCompra, txtVenta;
    private JTextField txtMinimo, txtMaximo, txtStock;
    private JTextArea txtObservaciones;
    private JLabel lblCodigo, lblMarca, lblCategoria, lblUnidad, lblDeposito;
    private JLabel lblEstado, lblMargen;
    private JButton btnBuscar, btnGuardar, btnCancelar, btnLimpiar;

    public DialogoModificarStock(Frame owner) {
        super(owner, "Modificar Stock - Serena Soft", true);
        construirPantalla();
        configurarEventos();
        limpiarFormulario();
        setSize(940, 730);
        setMinimumSize(new Dimension(790, 620));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        EstiloBotones.corregirBotones(getContentPane());
    }

    // =============================================================
    // 1. DISEÑO PRINCIPAL
    // =============================================================
    private void construirPantalla() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        setContentPane(raiz);

        JPanel cabecera = new JPanel(new BorderLayout(0, 5));
        cabecera.setBackground(AZUL);
        cabecera.setBorder(new EmptyBorder(20, 26, 20, 26));
        JLabel titulo = new JLabel("MODIFICAR STOCK");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setIcon(icono("edit", Color.WHITE, 24));
        titulo.setIconTextGap(14);
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Actualizá los parámetros del artículo sin alterar el inventario físico");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(223, 233, 252));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(subtitulo, BorderLayout.SOUTH);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(16, 20, 16, 20));
        agregarSeccion(contenido, panelBusqueda());
        agregarSeccion(contenido, panelDatos());
        agregarSeccion(contenido, panelPrecios());
        agregarSeccion(contenido, panelStock());
        agregarSeccion(contenido, panelObservaciones());
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        raiz.add(scroll, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pie.setBackground(Color.WHITE);
        pie.setBorder(new MatteBorder(1, 0, 0, 0, new Color(209, 220, 237)));
        btnLimpiar = boton("LIMPIAR", GRIS_BOTON);
        btnLimpiar.setIcon(icono("refresh", Color.WHITE, 18));
        btnCancelar = boton("CANCELAR", new Color(52, 63, 82));
        btnCancelar.setIcon(icono("close", Color.WHITE, 18));
        btnGuardar = boton("GUARDAR CAMBIOS", VERDE_BOTON);
        btnGuardar.setIcon(icono("save", Color.WHITE, 18));
        pie.add(btnLimpiar);
        pie.add(btnCancelar);
        pie.add(btnGuardar);
        raiz.add(pie, BorderLayout.SOUTH);
    }

    private void agregarSeccion(JPanel padre, JPanel seccion) {
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, seccion.getPreferredSize().height));
        padre.add(seccion);
        padre.add(Box.createVerticalStrut(13));
    }

    private JPanel seccion(String titulo) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(204, 216, 233)),
                BorderFactory.createTitledBorder(new EmptyBorder(7, 10, 8, 10),
                        titulo, TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 16), AZUL)));
        return p;
    }

    private GridBagConstraints gbc(int x, int y, double peso) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.weightx = peso;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(9, 12, 9, 12);
        return c;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_BOLD);
        l.setForeground(TEXTO);
        return l;
    }

    private JLabel dato() {
        JLabel l = new JLabel("—");
        l.setFont(FUENTE);
        l.setForeground(AZUL);
        return l;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(FUENTE);
        t.setPreferredSize(new Dimension(190, 38));
        t.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 192, 210)), new EmptyBorder(6, 10, 6, 10)));
        return t;
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(texto.contains("GUARDAR") ? 218 : 155, 46));
        b.setBorder(new EmptyBorder(8, 12, 8, 12));
        b.setOpaque(true);
        b.setIconTextGap(11);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        return b;
    }

    private JPanel panelBusqueda() {
        JPanel p = seccion("BUSCAR ARTÍCULO");
        txtProducto = campo();
        txtProducto.setEditable(false);
        txtProducto.setBackground(new Color(247, 249, 252));
        btnBuscar = boton("BUSCAR", AZUL_BOTON);
        btnBuscar.setIcon(icono("search", Color.WHITE, 18));
        p.add(etiqueta("Producto"), gbc(0, 0, 0));
        p.add(txtProducto, gbc(1, 0, 1));
        p.add(btnBuscar, gbc(2, 0, 0));
        lblCodigo = dato();
        lblDeposito = dato();
        p.add(etiqueta("Código"), gbc(0, 1, 0));
        p.add(lblCodigo, gbc(1, 1, 1));
        p.add(lblDeposito, gbc(2, 1, 0));
        return p;
    }

    private JPanel panelDatos() {
        JPanel p = seccion("DATOS GENERALES");
        txtNombre = campo();
        lblMarca = dato();
        lblCategoria = dato();
        lblUnidad = dato();
        lblEstado = dato();
        p.add(etiqueta("Nombre"), gbc(0, 0, 0));
        p.add(txtNombre, gbc(1, 0, 1));
        p.add(etiqueta("Estado"), gbc(2, 0, 0));
        p.add(lblEstado, gbc(3, 0, 1));
        p.add(etiqueta("Marca"), gbc(0, 1, 0));
        p.add(lblMarca, gbc(1, 1, 1));
        p.add(etiqueta("Categoría"), gbc(2, 1, 0));
        p.add(lblCategoria, gbc(3, 1, 1));
        p.add(etiqueta("Unidad venta"), gbc(0, 2, 0));
        p.add(lblUnidad, gbc(1, 2, 1));
        return p;
    }

    private JPanel panelPrecios() {
        JPanel p = seccion("PRECIOS Y RENTABILIDAD");
        txtCompra = campo();
        txtVenta = campo();
        lblMargen = dato();
        lblMargen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(etiqueta("Precio compra"), gbc(0, 0, 0));
        p.add(txtCompra, gbc(1, 0, 1));
        p.add(etiqueta("Precio venta"), gbc(2, 0, 0));
        p.add(txtVenta, gbc(3, 0, 1));
        p.add(etiqueta("Margen sobre costo"), gbc(0, 1, 0));
        p.add(lblMargen, gbc(1, 1, 1));
        return p;
    }

    private JPanel panelStock() {
        JPanel p = seccion("CONTROL DE INVENTARIO");
        txtStock = campo();
        txtStock.setEditable(false);
        txtStock.setBackground(new Color(239, 244, 252));
        txtMinimo = campo();
        txtMaximo = campo();
        p.add(etiqueta("Stock actual"), gbc(0, 0, 0));
        p.add(txtStock, gbc(1, 0, 1));
        p.add(etiqueta("Stock mínimo"), gbc(2, 0, 0));
        p.add(txtMinimo, gbc(3, 0, 1));
        p.add(etiqueta("Stock máximo"), gbc(0, 1, 0));
        p.add(txtMaximo, gbc(1, 1, 1));
        JLabel nota = new JLabel("ⓘ  Para modificar existencias utilizá Entrada, Salida o Ajuste de Stock.");
        nota.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nota.setForeground(new Color(84, 101, 128));
        GridBagConstraints c = gbc(0, 2, 1);
        c.gridwidth = 4;
        p.add(nota, c);
        return p;
    }

    private JPanel panelObservaciones() {
        JPanel p = seccion("☷  OBSERVACIONES DEL PRODUCTO");
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setFont(FUENTE);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setBorder(new EmptyBorder(9, 10, 9, 10));
        JScrollPane scroll = new JScrollPane(txtObservaciones);
        scroll.setPreferredSize(new Dimension(500, 92));
        GridBagConstraints c = gbc(0, 0, 1);
        c.gridwidth = 4;
        p.add(scroll, c);
        return p;
    }

    // =============================================================
    // 2. BUSCADOR COMPARTIDO Y CARGA DESDE MYSQL
    // =============================================================
    private void configurarEventos() {
        btnBuscar.addActionListener(e -> buscarProducto());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnCancelar.addActionListener(e -> dispose());
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarMargen(); }
            public void removeUpdate(DocumentEvent e) { actualizarMargen(); }
            public void changedUpdate(DocumentEvent e) { actualizarMargen(); }
        };
        txtCompra.getDocument().addDocumentListener(listener);
        txtVenta.getDocument().addDocumentListener(listener);
    }

    private void buscarProducto() {
        DialogoBuscarProducto buscador = new DialogoBuscarProducto(this);
        buscador.setVisible(true);
        if (!buscador.isSeleccionado()) return;
        Producto seleccionado = buscador.getObjetoProductoSeleccionado();
        if (seleccionado == null) {
            mensaje("No se pudo recuperar el producto seleccionado.", false);
            return;
        }
        Producto actualizado = productoDao.buscarPorId(seleccionado.getIdProducto());
        if (actualizado == null) {
            mensaje("El producto ya no existe en la base de datos.", false);
            return;
        }
        depositoActual = depositoDao.buscarPrincipal();
        if (depositoActual == null) {
            mensaje("No se encontró el depósito principal.", false);
            return;
        }
        productoActual = actualizado;
        txtProducto.setText(actualizado.getNombre());
        txtNombre.setText(actualizado.getNombre());
        lblCodigo.setText(actualizado.getCodigo());
        lblDeposito.setText("Depósito: " + depositoActual.getNombre());
        lblMarca.setText(actualizado.getMarca() == null ? "Sin marca" : actualizado.getMarca().getNombre());
        lblCategoria.setText(actualizado.getCategoria() == null ? "Sin categoría" : actualizado.getCategoria().getNombre());
        lblUnidad.setText(actualizado.getUnidadVenta() == null ? "Sin unidad" : actualizado.getUnidadVenta().getCodigo());
        lblEstado.setText(actualizado.isActivo() ? "ACTIVO" : "INACTIVO");
        lblEstado.setForeground(actualizado.isActivo() ? VERDE : Color.RED.darker());
        txtCompra.setText(formatear(actualizado.getPrecioCompra()));
        txtVenta.setText(formatear(actualizado.getPrecioVenta()));
        txtMinimo.setText(formatear(actualizado.getStockMinimo()));
        txtMaximo.setText(actualizado.getStockMaximo() == null ? "" : formatear(actualizado.getStockMaximo()));
        txtObservaciones.setText(actualizado.getObservaciones() == null ? "" : actualizado.getObservaciones());
        refrescarStock();
        actualizarMargen();
        btnGuardar.setEnabled(true);
    }

    private void refrescarStock() {
        if (productoActual == null || depositoActual == null) return;
        BigDecimal cantidad = stockService.obtenerCantidad(productoActual.getIdProducto(), depositoActual.getIdDeposito());
        txtStock.setText(formatear(cantidad) + " " + (productoActual.getUnidadVenta() == null ? "" : productoActual.getUnidadVenta().getCodigo()));
    }

    // =============================================================
    // 3. VALIDACIONES Y GUARDADO REAL
    // =============================================================
    private void guardarCambios() {
        if (productoActual == null) {
            mensaje("Primero seleccioná un producto.", false);
            return;
        }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mensaje("El nombre del producto es obligatorio.", false);
            txtNombre.requestFocusInWindow();
            return;
        }
        if (nombre.length() > 150) {
            mensaje("El nombre no puede superar 150 caracteres.", false);
            return;
        }
        BigDecimal compra, venta, minimo, maximo;
        try {
            compra = leerNumero(txtCompra.getText(), false);
            venta = leerNumero(txtVenta.getText(), false);
            minimo = leerNumero(txtMinimo.getText(), false);
            maximo = txtMaximo.getText().trim().isEmpty() ? null : leerNumero(txtMaximo.getText(), false);
        } catch (NumberFormatException ex) {
            mensaje("Revisá los precios y los límites de stock. Ingresá números válidos y no negativos.", false);
            return;
        }
        if (maximo != null && maximo.compareTo(minimo) < 0) {
            mensaje("El stock máximo no puede ser menor que el mínimo.", false);
            return;
        }
        if (minimo.stripTrailingZeros().scale() > 3 ||
                (maximo != null && maximo.stripTrailingZeros().scale() > 3)) {
            mensaje("El stock admite como máximo tres decimales.", false);
            return;
        }
        if (productoActual.getUnidadVenta() != null && !productoActual.getUnidadVenta().isPermiteDecimales() &&
                (minimo.stripTrailingZeros().scale() > 0 ||
                (maximo != null && maximo.stripTrailingZeros().scale() > 0))) {
            mensaje("La unidad de venta no permite cantidades decimales.", false);
            return;
        }
        if (txtObservaciones.getText().length() > 500) {
            mensaje("Las observaciones no pueden superar 500 caracteres.", false);
            return;
        }
        BigDecimal margen = compra.signum() == 0 ? BigDecimal.ZERO :
                venta.subtract(compra).multiply(new BigDecimal("100"))
                        .divide(compra, 3, RoundingMode.HALF_UP);
        if (confirmar(nombre, compra, venta, minimo, maximo) != JOptionPane.YES_OPTION) return;

        // Recargar para no guardar un objeto obsoleto obtenido del buscador.
        Producto paraGuardar = productoDao.buscarPorId(productoActual.getIdProducto());
        if (paraGuardar == null) {
            mensaje("El producto dejó de estar disponible.", false);
            return;
        }
        paraGuardar.setNombre(nombre);
        paraGuardar.setPrecioCompra(compra);
        paraGuardar.setPrecioVenta(venta);
        paraGuardar.setMargenPorcentaje(margen);
        paraGuardar.setStockMinimo(minimo);
        paraGuardar.setStockMaximo(maximo);
        paraGuardar.setObservaciones(txtObservaciones.getText().trim());
        if (!productoDao.actualizar(paraGuardar)) {
            mensaje("No se pudieron guardar los cambios en MySQL.", false);
            return;
        }
        productoActual = paraGuardar;
        confirmado = true;
        mensajeExito(nombre, compra, venta, minimo, maximo);
        dispose();
    }

    // Admite 1500, 1.500 y 1.500,50 (formato argentino).
    private BigDecimal leerNumero(String texto, boolean permiteVacio) {
        String s = texto.trim().replace("$", "").replace(" ", "");
        if (s.isEmpty()) {
            if (permiteVacio) return null;
            throw new NumberFormatException("Vacío");
        }
        if (s.contains(",")) s = s.replace(".", "").replace(",", ".");
        else if (s.matches("\\d{1,3}(\\.\\d{3})+")) s = s.replace(".", "");
        BigDecimal n = new BigDecimal(s);
        if (n.signum() < 0) throw new NumberFormatException("Negativo");
        return n;
    }

    private String formatear(BigDecimal n) {
        if (n == null) return "0";
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("es", "AR"));
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.###", simbolos);
        return df.format(n);
    }

    private void actualizarMargen() {
        try {
            BigDecimal compra = leerNumero(txtCompra.getText(), false);
            BigDecimal venta = leerNumero(txtVenta.getText(), false);
            if (compra.signum() == 0) {
                lblMargen.setText("—");
                return;
            }
            BigDecimal margen = venta.subtract(compra).multiply(new BigDecimal("100"))
                    .divide(compra, 2, RoundingMode.HALF_UP);
            lblMargen.setText(formatear(margen) + " %");
            lblMargen.setForeground(margen.signum() < 0 ? Color.RED.darker() : VERDE);
        } catch (NumberFormatException ex) {
            lblMargen.setText("—");
        }
    }

    // =============================================================
    // 4. CONFIRMACIÓN Y RESULTADOS CON ESTILO SERENA SOFT
    // =============================================================
    private int confirmar(String nombre, BigDecimal compra, BigDecimal venta,
                         BigDecimal minimo, BigDecimal maximo) {
        JPanel panel = panelMensaje("¿Confirmar cambios del producto?", new Color(230, 239, 255),
                "Producto: " + nombre + "\n" +
                "Código: " + productoActual.getCodigo() + "\n" +
                "Precio compra: $ " + formatear(compra) + "\n" +
                "Precio venta: $ " + formatear(venta) + "\n" +
                "Stock mínimo: " + formatear(minimo) + "\n" +
                "Stock máximo: " + (maximo == null ? "Sin límite" : formatear(maximo)) + "\n\n" +
                "La cantidad física del inventario no se modificará.");
        return JOptionPane.showConfirmDialog(this, panel, "Confirmar modificación",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    private void mensajeExito(String nombre, BigDecimal compra, BigDecimal venta,
                             BigDecimal minimo, BigDecimal maximo) {
        JPanel panel = panelMensaje("✓  ¡Producto actualizado correctamente!", new Color(222, 245, 233),
                "Producto: " + nombre + "\n" +
                "Código: " + productoActual.getCodigo() + "\n" +
                "Compra: $ " + formatear(compra) + "\n" +
                "Venta: $ " + formatear(venta) + "\n" +
                "Stock mínimo: " + formatear(minimo) + "\n" +
                "Stock máximo: " + (maximo == null ? "Sin límite" : formatear(maximo)) + "\n" +
                "Estado: GUARDADO EN MYSQL");
        JOptionPane.showMessageDialog(this, panel, "Modificación confirmada", JOptionPane.PLAIN_MESSAGE);
    }

    private void mensaje(String texto, boolean correcto) {
        JOptionPane.showMessageDialog(this,
                panelMensaje(correcto ? "Operación correcta" : "Atención", correcto ?
                        new Color(222, 245, 233) : new Color(255, 242, 222), texto),
                correcto ? "Serena Soft" : "Validación", JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel panelMensaje(String titulo, Color fondoTitulo, String detalle) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel encabezado = new JLabel(titulo);
        encabezado.setOpaque(true);
        encabezado.setBackground(AZUL);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        encabezado.setBorder(new EmptyBorder(15, 18, 15, 18));
        panel.add(encabezado, BorderLayout.NORTH);
        JTextArea info = new JTextArea(detalle);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        info.setForeground(TEXTO);
        info.setBackground(fondoTitulo);
        info.setEditable(false);
        info.setFocusable(false);
        info.setBorder(new EmptyBorder(15, 18, 15, 18));
        panel.add(info, BorderLayout.CENTER);
        return panel;
    }

    private void limpiarFormulario() {
        productoActual = null;
        depositoActual = null;
        txtProducto.setText("");
        txtNombre.setText("");
        txtCompra.setText("");
        txtVenta.setText("");
        txtMinimo.setText("");
        txtMaximo.setText("");
        txtStock.setText("");
        txtObservaciones.setText("");
        lblCodigo.setText("—");
        lblDeposito.setText("Depósito: —");
        lblMarca.setText("—");
        lblCategoria.setText("—");
        lblUnidad.setText("—");
        lblEstado.setText("—");
        lblMargen.setText("—");
        btnGuardar.setEnabled(false);
    }

    public boolean isConfirmado() { return confirmado; }

    // Iconos vectoriales: se dibujan con Java2D y no dependen de fuentes ni PNG.
    private static Icon icono(String tipo, Color color, int tam) {
        return new Icon() {
            @Override public int getIconWidth() { return tam; }
            @Override public int getIconHeight() { return tam; }
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D d = (Graphics2D) g.create();
                d.translate(x, y);
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                d.setColor(color);
                d.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                double z = tam / 24.0;
                d.scale(z, z);
                switch (tipo) {
                    case "search":
                        d.drawOval(3, 3, 13, 13);
                        d.drawLine(15, 15, 22, 22);
                        break;
                    case "save":
                        d.drawRoundRect(3, 3, 18, 18, 2, 2);
                        d.drawRect(7, 3, 10, 7);
                        d.drawRect(7, 14, 10, 7);
                        break;
                    case "refresh":
                        d.drawArc(4, 4, 16, 16, 35, 285);
                        d.drawLine(20, 4, 20, 10);
                        d.drawLine(20, 10, 14, 10);
                        break;
                    case "close":
                        d.drawLine(5, 5, 19, 19);
                        d.drawLine(19, 5, 5, 19);
                        break;
                    case "edit":
                        d.drawLine(5, 17, 16, 6);
                        d.drawLine(8, 20, 19, 9);
                        d.drawLine(16, 6, 19, 9);
                        d.drawLine(5, 17, 4, 21);
                        d.drawLine(4, 21, 8, 20);
                        break;
                    default: d.drawOval(4, 4, 16, 16);
                }
                d.dispose();
            }
        };
    }
}
