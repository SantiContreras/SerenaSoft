package Dialogos;

import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.StockProductoDao;
import Diseños.EstiloBotones;
import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Deposito;
import model.Producto;

/**
 * Baja lógica de artículos: modifica únicamente producto.activo.
 * No borra productos, stock ni movimientos históricos.
 */
public class DialogoDarDeBajaProducto extends JDialog {
    private static final Color AZUL = new Color(22, 53, 110);
    private static final Color AZUL_BOTON = new Color(32, 76, 151);
    private static final Color FONDO = new Color(237, 242, 249);
    private static final Color BORDE = new Color(203, 216, 235);
    private static final Color TEXTO = new Color(13, 48, 105);
    private static final Color VERDE = new Color(18, 143, 87);
    private static final Color ROJO = new Color(175, 48, 56);
    private static final Color GRIS = new Color(57, 68, 88);
    private final ProductoDao productoDao = new ProductoDao();
    private final DepositoDao depositoDao = new DepositoDao();
    private final StockProductoDao stockDao = new StockProductoDao();
    private Producto productoActual;
    private Deposito depositoActual;
    private boolean confirmado;

    private final JTextField txtProducto = campo();
    private final JLabel lblCodigo = valor();
    private final JLabel lblNombre = valor();
    private final JLabel lblMarca = valor();
    private final JLabel lblCategoria = valor();
    private final JLabel lblUnidad = valor();
    private final JLabel lblStock = valor();
    private final JLabel lblPrecio = valor();
    private final JLabel lblEstado = valor();
    private final JLabel lblDeposito = valor();
    private final JComboBox<String> cboMotivo = new JComboBox<>(new String[]{
        "Seleccione un motivo...", "Producto discontinuado", "Producto vencido",
        "Producto defectuoso", "Error de carga", "Cambio de proveedor", "Otro"
    });
    private final JTextArea txtObservaciones = new JTextArea(3, 30);
    private final JCheckBox chkConfirmar = new JCheckBox("Confirmo que deseo desactivar este artículo.");
    private final JButton btnBuscar = boton("BUSCAR", AZUL_BOTON, "buscar", 125);
    private final JButton btnLimpiar = boton("LIMPIAR", new Color(84, 98, 120), "limpiar", 135);
    private final JButton btnCancelar = boton("CANCELAR", GRIS, "cerrar", 135);
    private final JButton btnDarBaja = boton("DAR DE BAJA", ROJO, "baja", 170);

    public DialogoDarDeBajaProducto(Frame owner) {
        super(owner, "Dar de Baja Producto - Serena Soft", true);
        construir();
        eventos();
        limpiar();
        setSize(900, 690);
        setMinimumSize(new Dimension(720, 560));
        Dimension pantalla = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds().getSize();
        setSize(Math.min(getWidth(), pantalla.width - 35),
                Math.min(getHeight(), pantalla.height - 35));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        EstiloBotones.corregirBotones(getContentPane());
        // Reafirmar el color de texto de los botones tras aplicar el estilo global.
        for (JButton b : new JButton[]{btnBuscar, btnLimpiar, btnCancelar, btnDarBaja}) {
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }
    }

    private void construir() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        setContentPane(raiz);
        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setBackground(AZUL);
        header.setBorder(new EmptyBorder(15, 22, 15, 22));
        JLabel titulo = new JLabel("DAR DE BAJA PRODUCTO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        titulo.setForeground(Color.WHITE);
        titulo.setIcon(icono("baja", Color.WHITE, 25));
        titulo.setIconTextGap(14);
        JLabel subtitulo = new JLabel("Desactivá un artículo sin eliminar su información ni modificar el stock físico");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(224, 233, 251));
        header.add(titulo, BorderLayout.NORTH);
        header.add(subtitulo, BorderLayout.SOUTH);
        raiz.add(header, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(10, 14, 12, 14));
        JPanel busqueda = seccion("BUSCAR ARTÍCULO");
        busqueda.setLayout(new GridBagLayout());
        GridBagConstraints c = constraints();
        c.gridx = 0; c.gridy = 0; busqueda.add(etiqueta("Producto"), c);
        txtProducto.setEditable(false);
        txtProducto.setMinimumSize(new Dimension(100, 36));
        txtProducto.setBackground(new Color(247, 249, 253));
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        busqueda.add(txtProducto, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        busqueda.add(btnBuscar, c);
        c.gridx = 0; c.gridy = 1; busqueda.add(etiqueta("Código"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        busqueda.add(lblCodigo, c);
        c.gridx = 2; busqueda.add(lblDeposito, c);
        agregar(contenido, busqueda);

        JPanel datos = seccion("DATOS GENERALES DEL PRODUCTO");
        datos.setLayout(new GridBagLayout());
        fila(datos, 0, "Nombre", lblNombre, "Estado", lblEstado);
        fila(datos, 1, "Marca", lblMarca, "Categoría", lblCategoria);
        fila(datos, 2, "Unidad venta", lblUnidad, "Precio de venta", lblPrecio);
        fila(datos, 3, "Stock actual", lblStock, "", new JLabel(""));
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 16));
        agregar(contenido, datos);

        JPanel baja = seccion("MOTIVO Y CONFIRMACIÓN DE BAJA");
        baja.setLayout(new GridBagLayout());
        JPanel advertencia = new JPanel(new BorderLayout(12, 0));
        advertencia.setBackground(new Color(255, 246, 228));
        advertencia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(238, 206, 150)), new EmptyBorder(8, 10, 8, 10)));
        JLabel aviso = new JLabel("<html><div style='width:450px'><b>Atención: baja lógica del producto</b><p style='margin-top:4px'>"
                + "El artículo quedará INACTIVO. No se eliminarán su historial ni sus existencias. "
                + "Las operaciones futuras deberán respetar el estado activo del producto.</p></div></html>");
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aviso.setForeground(TEXTO);
        advertencia.add(new JLabel(icono("aviso", new Color(169, 103, 21), 27)), BorderLayout.WEST);
        advertencia.add(aviso, BorderLayout.CENTER);
        advertencia.setPreferredSize(new Dimension(540, 70));
        c = constraints(); c.gridx = 0; c.gridy = 0; c.gridwidth = 4;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        baja.add(advertencia, c);
        c = constraints(); c.gridx = 0; c.gridy = 1; baja.add(etiqueta("Motivo"), c);
        cboMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboMotivo.setPreferredSize(new Dimension(200, 36));
        c.gridx = 1; c.gridwidth = 3; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        baja.add(cboMotivo, c);
        c = constraints(); c.gridx = 0; c.gridy = 2; baja.add(etiqueta("Observaciones"), c);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JScrollPane observaciones = new JScrollPane(txtObservaciones);
        observaciones.setPreferredSize(new Dimension(240, 66));
        c.gridx = 1; c.gridwidth = 3; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        baja.add(observaciones, c);
        chkConfirmar.setOpaque(false);
        chkConfirmar.setForeground(TEXTO);
        chkConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        c = constraints(); c.gridx = 0; c.gridy = 3; c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        baja.add(chkConfirmar, c);
        agregar(contenido, baja);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        raiz.add(scroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 7));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE));
        footer.add(btnLimpiar); footer.add(btnCancelar); footer.add(btnDarBaja);
        raiz.add(footer, BorderLayout.SOUTH);
    }

    private void eventos() {
        btnBuscar.addActionListener(e -> buscarProducto());
        btnLimpiar.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dispose());
        chkConfirmar.addActionListener(e -> actualizarBoton());
        cboMotivo.addActionListener(e -> actualizarBoton());
        btnDarBaja.addActionListener(e -> darDeBaja());
    }

    private void buscarProducto() {
        DialogoBuscarProducto buscador = new DialogoBuscarProducto(this);
        buscador.setVisible(true);
        if (!buscador.isSeleccionado()) return;
        Producto elegido = buscador.getObjetoProductoSeleccionado();
        if (elegido == null) { mensaje("No se pudo recuperar el producto seleccionado."); return; }
        Producto actualizado = productoDao.buscarPorId(elegido.getIdProducto());
        if (actualizado == null) { mensaje("El producto ya no está disponible."); return; }
        depositoActual = depositoDao.buscarPrincipal();
        if (depositoActual == null) { mensaje("No se encontró el depósito principal."); return; }
        productoActual = actualizado;
        txtProducto.setText(actualizado.getNombre());
        lblCodigo.setText(actualizado.getCodigo());
        lblNombre.setText(actualizado.getNombre());
        lblMarca.setText(actualizado.getMarca() == null ? "Sin marca" : actualizado.getMarca().getNombre());
        lblCategoria.setText(actualizado.getCategoria() == null ? "Sin categoría" : actualizado.getCategoria().getNombre());
        lblUnidad.setText(actualizado.getUnidadVenta() == null ? "-" : actualizado.getUnidadVenta().getCodigo());
        lblPrecio.setText(dinero(actualizado.getPrecioVenta()));
        lblEstado.setText(actualizado.isActivo() ? "ACTIVO" : "INACTIVO");
        lblEstado.setForeground(actualizado.isActivo() ? VERDE : ROJO);
        lblDeposito.setText("Depósito: " + depositoActual.getNombre());
        BigDecimal cantidad = stockDao.obtenerCantidad(actualizado.getIdProducto(), depositoActual.getIdDeposito());
        lblStock.setText(cantidad.stripTrailingZeros().toPlainString() + " "
                + (actualizado.getUnidadVenta() == null ? "UN" : actualizado.getUnidadVenta().getCodigo()));
        cboMotivo.setSelectedIndex(0);
        txtObservaciones.setText("");
        chkConfirmar.setSelected(false);
        actualizarBoton();
    }

    private void darDeBaja() {
        if (productoActual == null) { mensaje("Primero seleccioná un producto."); return; }
        if (cboMotivo.getSelectedIndex() <= 0) { mensaje("Seleccioná el motivo de la baja."); return; }
        if (!chkConfirmar.isSelected()) { mensaje("Marcá la casilla de confirmación."); return; }
        Producto vigente = productoDao.buscarPorId(productoActual.getIdProducto());
        if (vigente == null) { mensaje("El producto ya no existe."); return; }
        if (!vigente.isActivo()) { mensaje("El producto ya se encuentra inactivo."); return; }
        String motivo = String.valueOf(cboMotivo.getSelectedItem());
        String detalle = "Producto: " + vigente.getNombre() + "\nCódigo: " + vigente.getCodigo()
                + "\nMotivo: " + motivo + "\nObservaciones: " + txtObservaciones.getText().trim()
                + "\n\nSe cambiará su estado a INACTIVO sin eliminar datos ni modificar stock.";
        int opcion = confirmar("CONFIRMAR BAJA DEL PRODUCTO", detalle);
        if (opcion != JOptionPane.YES_OPTION) return;
        btnDarBaja.setEnabled(false);
        try {
            // Única escritura en la base de datos: producto.activo = false.
            boolean guardado = productoDao.cambiarEstado(vigente.getIdProducto(), false);
            if (!guardado) { mensaje("No se pudo cambiar el estado del producto en MySQL."); return; }
            confirmado = true;
            lblEstado.setText("INACTIVO");
            lblEstado.setForeground(ROJO);
            JPanel panel = panelMensaje("PRODUCTO DESACTIVADO CORRECTAMENTE",
                    "Producto: " + vigente.getNombre() + "\nCódigo: " + vigente.getCodigo()
                    + "\nEstado: INACTIVO\nMotivo: " + motivo
                    + "\n\nLas existencias y el historial no fueron eliminados.", true);
            JOptionPane.showMessageDialog(this, panel, "Baja confirmada", JOptionPane.PLAIN_MESSAGE);
            dispose();
        } finally { actualizarBoton(); }
    }

    private void actualizarBoton() {
        btnDarBaja.setEnabled(productoActual != null && productoActual.isActivo()
                && cboMotivo.getSelectedIndex() > 0 && chkConfirmar.isSelected() && !confirmado);
    }

    private void limpiar() {
        productoActual = null; depositoActual = null; confirmado = false;
        txtProducto.setText("");
        for (JLabel l : new JLabel[]{lblCodigo, lblNombre, lblMarca, lblCategoria,
                lblUnidad, lblStock, lblPrecio, lblEstado, lblDeposito}) l.setText("-");
        lblEstado.setForeground(VERDE);
        cboMotivo.setSelectedIndex(0);
        txtObservaciones.setText("");
        chkConfirmar.setSelected(false);
        actualizarBoton();
    }

    private int confirmar(String titulo, String detalle) {
        return JOptionPane.showConfirmDialog(this, panelMensaje(titulo, detalle, false),
                "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, panelMensaje("ATENCIÓN", texto, false),
                "Validación", JOptionPane.PLAIN_MESSAGE);
    }
    private JPanel panelMensaje(String titulo, String detalle, boolean exito) {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(14, 17, 14, 17));
        JLabel encabezado = new JLabel(titulo);
        encabezado.setOpaque(true);
        encabezado.setBackground(AZUL);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        encabezado.setIcon(icono(exito ? "check" : "aviso", Color.WHITE, 22));
        encabezado.setIconTextGap(10);
        encabezado.setBorder(new EmptyBorder(13, 14, 13, 14));
        p.add(encabezado, BorderLayout.NORTH);
        JTextArea cuerpo = new JTextArea(detalle);
        cuerpo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cuerpo.setForeground(TEXTO);
        cuerpo.setEditable(false);
        cuerpo.setLineWrap(true);
        cuerpo.setWrapStyleWord(true);
        cuerpo.setBackground(exito ? new Color(224, 247, 235) : new Color(232, 240, 252));
        cuerpo.setBorder(new EmptyBorder(14, 16, 14, 16));
        cuerpo.setColumns(42);
        p.add(cuerpo, BorderLayout.CENTER);
        return p;
    }

    private static JPanel seccion(String nombre) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDE),
                nombre, 0, 0, new Font("Segoe UI", Font.BOLD, 15), TEXTO));
        return p;
    }
    private static void agregar(JPanel destino, JPanel tarjeta) {
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        destino.add(tarjeta);
        destino.add(Box.createVerticalStrut(8));
    }
    private static void fila(JPanel panel, int fila, String t1, JLabel v1, String t2, JLabel v2) {
        GridBagConstraints c = constraints(); c.gridy = fila;
        c.gridx = 0; panel.add(etiqueta(t1), c);
        c.gridx = 1; c.weightx = 0.5; c.fill = GridBagConstraints.HORIZONTAL; panel.add(v1, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE; panel.add(etiqueta(t2), c);
        c.gridx = 3; c.weightx = 0.5; c.fill = GridBagConstraints.HORIZONTAL; panel.add(v2, c);
    }
    private static GridBagConstraints constraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 9, 6, 9);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }
    private static JLabel etiqueta(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(TEXTO);
        return l;
    }
    private static JLabel valor() {
        JLabel l = new JLabel("-");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(TEXTO);
        return l;
    }
    private static JTextField campo() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(200, 36));
        return f;
    }
    private static JButton boton(String nombre, Color color, String icono, int ancho) {
        JButton b = new JButton(nombre);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setIcon(icono(icono, Color.WHITE, 19));
        b.setIconTextGap(10);
        b.setPreferredSize(new Dimension(ancho, 43));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private static String dinero(BigDecimal n) {
        if (n == null) return "-";
        NumberFormat f = NumberFormat.getNumberInstance(new Locale("es", "AR"));
        f.setMinimumFractionDigits(0);
        f.setMaximumFractionDigits(2);
        return "$ " + f.format(n);
    }
    private static Icon icono(String tipo, Color color, int tam) {
        return new Icon() {
            public int getIconWidth() { return tam; }
            public int getIconHeight() { return tam; }
            public void paintIcon(Component comp, Graphics g, int x, int y) {
                Graphics2D d = (Graphics2D) g.create();
                try {
                    d.translate(x, y);
                    d.scale(tam / 24.0, tam / 24.0);
                    d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    d.setColor(color);
                    d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (tipo) {
                        case "buscar" -> { d.drawOval(3, 3, 13, 13); d.drawLine(15, 15, 22, 22); }
                        case "cerrar" -> { d.drawLine(5, 5, 19, 19); d.drawLine(19, 5, 5, 19); }
                        case "limpiar" -> { d.drawArc(4, 4, 16, 16, 45, 285); d.drawLine(18, 4, 21, 10); d.drawLine(18, 4, 13, 5); }
                        case "check" -> { d.drawOval(2, 2, 20, 20); d.drawLine(6, 12, 10, 16); d.drawLine(10, 16, 18, 8); }
                        case "aviso" -> { Path2D p = new Path2D.Double(); p.moveTo(12, 2); p.lineTo(23, 22); p.lineTo(1, 22); p.closePath(); d.draw(p); d.drawLine(12, 9, 12, 15); d.fillOval(11, 18, 2, 2); }
                        default -> { d.drawRoundRect(4, 3, 16, 18, 3, 3); d.drawLine(8, 9, 16, 9); d.drawLine(8, 14, 16, 14); d.drawLine(10, 19, 14, 19); }
                    }
                } finally { d.dispose(); }
            }
        };
    }
    public boolean isConfirmado() { return confirmado; }
}
