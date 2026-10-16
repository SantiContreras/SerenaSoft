package Dialogos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
import Configuracion.conexion;
import java.text.Normalizer;

/** Serena Soft - alta de artículos. Rediseño visual, sin persistencia. */
public class DialogoNuevoArticulo extends JDialog {
    private static final Color NAVY = new Color(15, 43, 91), BLUE = new Color(30, 87, 169);
    private static final Color BG = new Color(218, 228, 241), WHITE = Color.WHITE;
    private static final Color TEXT = new Color(32, 49, 75), MUTED = new Color(105, 119, 139);
    private static final Color LINE = new Color(148, 169, 198), GREEN = new Color(18, 142, 99);
    private final JTextField txtCodigo = field(), txtCodigoBarra = field(), txtCodigoInterno = field();
    private final JTextField txtDescripcion = field(), txtUbicacion = field(), txtProveedor = field();
    private final JTextField txtStockActual = field(), txtStockMinimo = field();
    private final JTextField txtPrecioCompra = field(), txtPrecioVenta = field(), txtMargen = field();
    private final JComboBox<String> comboMarca = combo(), comboCategoria = combo(), comboRubro = combo();
    private final JComboBox<String> comboUnidadCompra = combo(), comboUnidadVenta = combo();
    private final JComboBox<String> comboUnidadStock = combo(), comboIVA = combo();
    private final JCheckBox chkActivo = check("Producto activo", true);
    private final JCheckBox chkPesable = check("Producto pesable", false);
    private final JCheckBox chkControlStock = check("Controlar stock", true);
    private final JCheckBox chkPermiteDescuento = check("Permitir descuentos", false);
    private final JTextArea txtObservaciones = new JTextArea(3, 30);
    private int idProveedorSeleccionado = -1;
    private final JLabel lblEstadoCodigo = new JLabel("Seleccioná una categoría para previsualizar el código");
    private boolean guardado = false;

    public DialogoNuevoArticulo(Window parent) {
        super(parent, "Nuevo artículo · Serena Soft", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(850, 640));
        setSize(1060, 790);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);
        add(header(), BorderLayout.NORTH);
        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(18, 24, 22, 24));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        addSection(content, "01", "Identificación del artículo", "Información principal y lectura con escáner", identification());
        addSection(content, "02", "Clasificación y proveedor", "Organización del catálogo comercial", classification());
        addSection(content, "03", "Unidades y existencias", "Las existencias reales se registrarán desde Stock", inventory());
        addSection(content, "04", "Precios e impuestos", "Valores comerciales del artículo", prices());
        addSection(content, "05", "Configuración", "Comportamiento del artículo en el sistema", options());
        addSection(content, "06", "Observaciones", "Notas adicionales para el equipo", notes());
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        loadDemoOptions();
        comboCategoria.addActionListener(e -> actualizarVistaPreviaCodigo());
        actualizarVistaPreviaCodigo();
        txtCodigo.setEditable(false);
        txtCodigo.setText("Seleccioná una categoría");
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        txtCodigo.setForeground(new Color(15, 70, 145));
        txtCodigo.setBackground(new Color(220, 237, 255));
        txtStockActual.setEditable(false);
        txtStockActual.setText("0");
        txtStockActual.setBackground(new Color(237, 243, 250));
        txtProveedor.setEditable(false);
        txtProveedor.setBackground(new Color(237, 243, 250));
        setLocationRelativeTo(parent);
        SwingUtilities.invokeLater(() -> txtDescripcion.requestFocusInWindow());
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setBackground(NAVY);
        p.setBorder(new EmptyBorder(21, 27, 21, 27));
        JLabel emblem = new JLabel("▣", SwingConstants.CENTER);
        emblem.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
        emblem.setForeground(WHITE);
        emblem.setPreferredSize(new Dimension(54, 56));
        emblem.setOpaque(true);
        emblem.setBackground(BLUE);
        p.add(emblem, BorderLayout.WEST);
        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("NUEVO ARTÍCULO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(WHITE);
        JLabel subtitle = new JLabel("Registrá un producto en el catálogo de Serena Soft");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(202, 218, 241));
        words.add(title); words.add(Box.createVerticalStrut(5)); words.add(subtitle);
        p.add(words, BorderLayout.CENTER);
        JLabel badge = new JLabel("CATÁLOGO  /  ALTA", SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(220, 233, 251));
        badge.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(78, 112, 164)), new EmptyBorder(9, 13, 9, 13)));
        p.add(badge, BorderLayout.EAST);
        return p;
    }
    private void addSection(JPanel root, String index, String title, String subtitle, JPanel body) {
        JPanel card = new JPanel(new BorderLayout(0, 17));
        card.setBackground(new Color(248, 251, 255));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(19, 22, 21, 22)));
        JPanel top = new JPanel(new BorderLayout(12, 0)); top.setOpaque(false);
        JLabel number = new JLabel(index, SwingConstants.CENTER);
        number.setOpaque(true); number.setBackground(NAVY); number.setForeground(WHITE);
        number.setFont(new Font("Segoe UI", Font.BOLD, 13)); number.setPreferredSize(new Dimension(39, 39));
        top.add(number, BorderLayout.WEST);
        JPanel names = new JPanel(); names.setOpaque(false); names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        JLabel heading = new JLabel(title); heading.setForeground(NAVY); heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        JLabel hint = new JLabel(subtitle); hint.setForeground(MUTED); hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        names.add(heading); names.add(Box.createVerticalStrut(2)); names.add(hint);
        top.add(names, BorderLayout.CENTER);
        card.add(top, BorderLayout.NORTH); card.add(body, BorderLayout.CENTER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        root.add(card); root.add(Box.createVerticalStrut(14));
    }
    private JPanel grid(int cols) {
        JPanel p = new JPanel(new GridLayout(0, cols, 17, 14)); p.setOpaque(false); return p;
    }
    private JPanel cell(String label, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(0, 7)); p.setOpaque(false);
        JLabel l = new JLabel(label); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); l.setForeground(TEXT);
        p.add(l, BorderLayout.NORTH); p.add(input, BorderLayout.CENTER); return p;
    }
    private JPanel identification() {
        JPanel p = new JPanel(new BorderLayout(0, 15)); p.setOpaque(false);
        JPanel first = grid(3);
        first.add(cell("Código Serena Soft · automático", txtCodigo));
        first.add(cell("Código de barras · escáner", txtCodigoBarra));
        first.add(cell("Código interno · opcional", txtCodigoInterno));
        JPanel top = new JPanel(new BorderLayout(0, 6)); top.setOpaque(false);
        top.add(first, BorderLayout.CENTER);
        lblEstadoCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoCodigo.setForeground(new Color(20, 89, 149));
        top.add(lblEstadoCodigo, BorderLayout.SOUTH);
        p.add(top, BorderLayout.NORTH);
        JPanel bottom = grid(2);
        bottom.add(cell("Descripción del producto *", txtDescripcion));
        bottom.add(cell("Ubicación / estantería", txtUbicacion));
        p.add(bottom, BorderLayout.CENTER);
        return p;
    }
    private JPanel classification() {
        JPanel p = grid(3);
        p.add(cell("Marca", comboMarca)); p.add(cell("Categoría", comboCategoria)); p.add(cell("Rubro", comboRubro));
        JPanel provider = new JPanel(new BorderLayout(8, 0)); provider.setOpaque(false);
        provider.add(txtProveedor, BorderLayout.CENTER);
        JButton search = button("⌕  BUSCAR", BLUE, WHITE);
        search.setPreferredSize(new Dimension(115, 43));
        search.addActionListener(e -> selectProvider());
        provider.add(search, BorderLayout.EAST);
        p.add(cell("Proveedor · opcional", provider));
        p.add(cell("Unidad de compra", comboUnidadCompra));
        p.add(cell("Unidad de venta", comboUnidadVenta));
        return p;
    }
    private JPanel inventory() {
        JPanel p = new JPanel(new BorderLayout(0, 12)); p.setOpaque(false);
        JPanel fields = grid(3);
        fields.add(cell("Stock actual · solo lectura", txtStockActual));
        fields.add(cell("Stock mínimo", txtStockMinimo));
        fields.add(cell("Unidad de stock", comboUnidadStock));
        p.add(fields, BorderLayout.NORTH);
        JLabel note = new JLabel("ⓘ  Para ingresar mercadería utilizá Compras / Entradas o Ajustes de Stock.");
        note.setForeground(new Color(60, 104, 154)); note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(note, BorderLayout.SOUTH); return p;
    }
    private JPanel prices() {
        JPanel p = grid(4);
        p.add(cell("Precio de compra", txtPrecioCompra));
        p.add(cell("Precio de venta", txtPrecioVenta));
        p.add(cell("Margen (%)", txtMargen));
        p.add(cell("IVA", comboIVA)); return p;
    }
    private JPanel options() {
        JPanel p = grid(4);
        p.add(chkActivo); p.add(chkControlStock); p.add(chkPesable); p.add(chkPermiteDescuento);
        return p;
    }
    private JPanel notes() {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtObservaciones.setForeground(TEXT); txtObservaciones.setBackground(new Color(242, 247, 255)); txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true); txtObservaciones.setMargin(new Insets(10, 12, 10, 12));
        JScrollPane s = new JScrollPane(txtObservaciones);
        s.setBorder(new LineBorder(LINE, 1, true)); s.setPreferredSize(new Dimension(600, 86));
        p.add(s); return p;
    }
    private JPanel footer() {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(13, 24, 13, 24)));
        JLabel hint = new JLabel("* Campo obligatorio     ·     Los datos aún no se guardan en MySQL");
        hint.setForeground(MUTED); hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        p.add(hint, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0)); actions.setOpaque(false);
        JButton clear = button("↺  LIMPIAR", new Color(234, 240, 247), TEXT);
        JButton cancel = button("CANCELAR", new Color(79, 91, 109), WHITE);
        JButton save = button("✓  VALIDAR ARTÍCULO", GREEN, WHITE);
        clear.addActionListener(e -> clear()); cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> validateDraft());
        actions.add(clear); actions.add(cancel); actions.add(save);
        p.add(actions, BorderLayout.EAST); getRootPane().setDefaultButton(save); return p;
    }
    private void selectProvider() {
        DialogoBuscarProveedor dialog = new DialogoBuscarProveedor(this);
        dialog.setVisible(true);
        if (dialog.isSeleccionado()) {
            idProveedorSeleccionado = dialog.getIdSeleccionado();
            txtProveedor.setText(dialog.getRazonSocialSeleccionada());
        }
    }
    private void validateDraft() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresá la descripción del producto.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtDescripcion.requestFocusInWindow(); return;
        }
        JOptionPane.showMessageDialog(this,
            "El formulario pasó la validación visual.\nTodavía NO se guardó en MySQL.\nLa generación del código se integrará con ProductoService.",
            "Vista previa · Serena Soft", JOptionPane.INFORMATION_MESSAGE);
    }
    private void clear() {
        for (JTextField f : new JTextField[]{txtCodigoBarra, txtCodigoInterno, txtDescripcion, txtUbicacion, txtStockMinimo, txtPrecioCompra, txtPrecioVenta, txtMargen, txtProveedor}) f.setText("");
        txtObservaciones.setText(""); idProveedorSeleccionado = -1;
        for (JComboBox<?> c : new JComboBox<?>[]{comboMarca, comboCategoria, comboRubro, comboUnidadCompra, comboUnidadVenta, comboUnidadStock, comboIVA}) if (c.getItemCount() > 0) c.setSelectedIndex(0);
        chkActivo.setSelected(true); chkControlStock.setSelected(true); chkPesable.setSelected(false); chkPermiteDescuento.setSelected(false);
        actualizarVistaPreviaCodigo();
        txtDescripcion.requestFocusInWindow();
    }
    private void loadDemoOptions() {
        fill(comboMarca, "Seleccione...", "Coca Cola", "Natura", "Playadito", "Ledesma", "La Serenísima", "Serrana");
        fill(comboCategoria, "Seleccione...");
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM categoria WHERE activo = 1 ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) comboCategoria.addItem(rs.getString(1));
        } catch (SQLException ex) {
            lblEstadoCodigo.setText("No se pudieron cargar las categorías desde MySQL");
        }
        fill(comboRubro, "Seleccione...", "Alimentos", "Bebidas", "Limpieza", "Higiene");
        fill(comboUnidadCompra, "Unidad", "Caja", "Pack", "Kg", "Litro");
        fill(comboUnidadVenta, "Unidad", "Kg", "Gramo", "Litro");
        fill(comboUnidadStock, "Unidad", "Kg", "Litro", "Caja");
        fill(comboIVA, "21%", "10.5%", "27%", "0%");
    }
    private static void fill(JComboBox<String> c, String... values) { for (String v : values) c.addItem(v); }
    private static JTextField field() {
        JTextField f = new JTextField(); f.setFont(new Font("Segoe UI", Font.PLAIN, 14)); f.setForeground(TEXT);
        f.setCaretColor(BLUE); f.setBackground(new Color(244, 248, 255)); f.setPreferredSize(new Dimension(150, 43));
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(0, 12, 0, 12)));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BLUE, 2, true), new EmptyBorder(0, 11, 0, 11))); }
            public void focusLost(FocusEvent e) { f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(0, 12, 0, 12))); }
        }); return f;
    }
    private static JComboBox<String> combo() {
        JComboBox<String> c = new JComboBox<>(); c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setForeground(NAVY); c.setBackground(new Color(229, 239, 253)); c.setFocusable(false);
        c.setPreferredSize(new Dimension(150, 43)); c.setBorder(new LineBorder(LINE, 1, true));
        c.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected, focus);
                l.setBorder(new EmptyBorder(8, 10, 8, 10)); l.setFont(new Font("Segoe UI", Font.BOLD, 13)); return l;
            }
        }); return c;
    }
    private static JCheckBox check(String title, boolean selected) {
        JCheckBox c = new JCheckBox(title, selected); c.setOpaque(false); c.setForeground(TEXT);
        c.setFont(new Font("Segoe UI", Font.BOLD, 12)); c.setFocusPainted(false); return c;
    }
    private static JButton button(String title, Color bg, Color fg) {
        JButton b = new JButton(title); b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(fg); b.setBackground(bg); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 18, 12, 18)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true); return b;
    }

    /** Vista previa informativa. La asignación definitiva debe hacerse al guardar,
     * dentro de una transacción, para evitar colisiones entre terminales. */
    private void actualizarVistaPreviaCodigo() {
        Object seleccionado = comboCategoria.getSelectedItem();
        String categoria = seleccionado == null ? "" : seleccionado.toString().trim();
        if (categoria.isEmpty() || categoria.equalsIgnoreCase("Seleccione...")) {
            txtCodigo.setText("Seleccioná una categoría");
            lblEstadoCodigo.setText("Código provisional · pendiente de categoría");
            return;
        }
        String limpio = Normalizer.normalize(categoria, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase(java.util.Locale.ROOT)
                .replaceAll("[^A-Z0-9]", "");
        String prefijo = limpio.substring(0, Math.min(3, limpio.length()));
        if (prefijo.isEmpty()) return;
        int siguiente = 1;
        String sql = "SELECT codigo FROM producto WHERE codigo LIKE ?";
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, prefijo + "-%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String codigo = rs.getString(1);
                    if (codigo != null && codigo.matches(java.util.regex.Pattern.quote(prefijo) + "-[0-9]+")) {
                        String numero = codigo.substring(prefijo.length() + 1);
                        try { siguiente = Math.max(siguiente, Integer.parseInt(numero) + 1); }
                        catch (NumberFormatException ignored) { }
                    }
                }
            }
            txtCodigo.setText(prefijo + "-" + String.format("%03d", siguiente));
            lblEstadoCodigo.setText("● VISTA PREVIA · Código disponible al consultar · se confirma al guardar");
        } catch (SQLException ex) {
            txtCodigo.setText(prefijo + "-???");
            lblEstadoCodigo.setText("No se pudo consultar MySQL · " + ex.getMessage());
        }
    }

    public boolean isGuardado() { return guardado; }
    public String getCodigo() { return txtCodigo.getText().trim(); }
    public String getCodigoBarra() { return txtCodigoBarra.getText().trim(); }
    public String getCodigoInterno() { return txtCodigoInterno.getText().trim(); }
    public String getDescripcion() { return txtDescripcion.getText().trim(); }
    public String getUbicacion() { return txtUbicacion.getText().trim(); }
    public int getIdProveedorSeleccionado() { return idProveedorSeleccionado; }
    public String getProveedorSeleccionado() { return txtProveedor.getText().trim(); }
    public boolean isProductoActivo() { return chkActivo.isSelected(); }
    public boolean isProductoPesable() { return chkPesable.isSelected(); }
    public boolean isControlStock() { return chkControlStock.isSelected(); }
    public boolean isPermiteDescuento() { return chkPermiteDescuento.isSelected(); }
    public String getObservaciones() { return txtObservaciones.getText().trim(); }
}
