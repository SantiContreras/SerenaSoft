package Paneles;

import Dao.MedioPagoDao;
import Dao.MedioPagoDao.Medio;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/** Presentación de medios de pago; conserva las operaciones del DAO existente. */
public final class PanelConfigMediosPago extends JPanel {
    private final MedioPagoDao dao = new MedioPagoDao();
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "MEDIO DE PAGO", "TIPO", "REFERENCIA", "CAJA FÍSICA", "ESTADO"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private java.util.List<Medio> medios = new ArrayList<>();
    private static final Color AZUL = new Color(28, 78, 154), AZUL_OSCURO = new Color(17, 47, 100);
    private static final Color VERDE = new Color(22, 136, 89), GRIS = new Color(94, 108, 127);
    private static final Color FONDO = new Color(245, 248, 253), BORDE = new Color(216, 225, 238);
    private static final Font NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font NEGRITA = new Font("Segoe UI", Font.BOLD, 14);

    public PanelConfigMediosPago() {
        setLayout(new BorderLayout(0, 18)); setBackground(FONDO);
        setBorder(new EmptyBorder(20, 22, 20, 22));
        JPanel encabezado = new JPanel(new BorderLayout(0, 7)); encabezado.setOpaque(false);
        JLabel titulo = new JLabel("MEDIOS DE PAGO"); titulo.setFont(new Font("Segoe UI", Font.BOLD, 24)); titulo.setForeground(AZUL_OSCURO);
        JLabel subtitulo = new JLabel("Administrá los medios habilitados para Ventas. Los pagos electrónicos se verifican fuera del sistema.");
        subtitulo.setFont(NORMAL); subtitulo.setForeground(GRIS);
        encabezado.add(titulo, BorderLayout.NORTH); encabezado.add(subtitulo, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);

        tabla.setFont(NORMAL); tabla.setRowHeight(43); tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true); tabla.setShowVerticalLines(false); tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(233, 239, 247)); tabla.setSelectionBackground(new Color(219, 233, 252));
        tabla.setSelectionForeground(AZUL_OSCURO); tabla.setIntercellSpacing(new Dimension(0, 1));
        JTableHeader cabecera = tabla.getTableHeader(); cabecera.setPreferredSize(new Dimension(0, 44));
        cabecera.setReorderingAllowed(false); cabecera.setResizingAllowed(true);
        cabecera.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                JLabel l = (JLabel)super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                l.setOpaque(true); l.setBackground(AZUL_OSCURO); l.setForeground(Color.WHITE);
                l.setFont(new Font("Segoe UI", Font.BOLD, 12)); l.setBorder(new EmptyBorder(0, 11, 0, 6));
                return l;
            }
        });
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                JLabel l = (JLabel)super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                l.setFont(NORMAL); l.setBorder(new EmptyBorder(0, 11, 0, 6));
                l.setOpaque(true); l.setBackground(sel ? new Color(219, 233, 252) : (row % 2 == 0 ? Color.WHITE : new Color(249, 251, 255)));
                l.setForeground(AZUL_OSCURO);
                if (col == 5) {
                    boolean activo = "Activo".equalsIgnoreCase(String.valueOf(v));
                    l.setText(activo ? "●  Activo" : "●  Inactivo");
                    l.setForeground(activo ? new Color(13, 132, 79) : new Color(170, 73, 59));
                    l.setFont(NEGRITA);
                } else if (col == 4 || col == 3) {
                    l.setForeground("Sí".equals(String.valueOf(v)) ? VERDE : GRIS);
                }
                return l;
            }
        });
        JScrollPane scroll = new JScrollPane(tabla); scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(Color.WHITE); add(scroll, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); acciones.setOpaque(false);
        acciones.add(boton("+  Nuevo", VERDE, () -> editar(null)));
        acciones.add(boton("Editar", AZUL, () -> { Medio m = seleccion(); if (m != null) editar(m); }));
        acciones.add(boton("Activar / Desactivar", GRIS, this::cambiarEstado));
        acciones.add(boton("Actualizar", AZUL, this::cargar));
        JLabel nota = new JLabel("Los medios usados en ventas se desactivan; no se eliminan para preservar el historial.");
        nota.setFont(new Font("Segoe UI", Font.PLAIN, 13)); nota.setForeground(GRIS);
        JPanel pie = new JPanel(new BorderLayout(0, 8)); pie.setOpaque(false);
        pie.add(acciones, BorderLayout.NORTH); pie.add(nota, BorderLayout.SOUTH); add(pie, BorderLayout.SOUTH);
        cargar();
    }

    private JButton boton(String texto, Color color, Runnable accion) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D x = (Graphics2D)g.create();
                x.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                x.setColor(!isEnabled() ? new Color(167, 176, 188) : getModel().isPressed() ? color.darker() : getModel().isRollover() ? color.brighter() : color);
                x.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 13, 13);
                x.setFont(getFont()); x.setColor(Color.WHITE);
                FontMetrics fm = x.getFontMetrics();
                x.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                x.dispose();
            }
        };
        b.setFont(NEGRITA); b.setPreferredSize(new Dimension(Math.max(124, texto.length()*9+36), 44));
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false); b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.addActionListener(e -> accion.run()); return b;
    }
    private void cargar() {
        try {
            medios = dao.listar(); modelo.setRowCount(0);
            for (Medio m : medios) modelo.addRow(new Object[]{m.id, m.nombre, m.tipo, m.requiereReferencia ? "Sí" : "No", m.afectaEfectivo ? "Sí" : "No", m.activo ? "Activo" : "Inactivo"});
        } catch (Exception ex) { error(ex); }
    }
    private Medio seleccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un medio de pago."); return null; }
        return medios.get(tabla.convertRowIndexToModel(row));
    }
    private void cambiarEstado() {
        Medio m = seleccion(); if (m == null) return;
        int op = JOptionPane.showConfirmDialog(this, (m.activo ? "¿Desactivar " : "¿Activar ") + m.nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        try { dao.cambiarEstado(m.id, !m.activo); cargar(); } catch (Exception ex) { error(ex); }
    }
    private void editar(Medio actual) {
        JTextField nombre = new JTextField(actual == null ? "" : actual.nombre, 24);
        JComboBox<String> tipo = new JComboBox<>(new String[]{"EFECTIVO", "TRANSFERENCIA", "TARJETA", "QR", "OTRO"});
        if (actual != null) tipo.setSelectedItem(actual.tipo);
        JCheckBox referencia = new JCheckBox("Exigir referencia de operación", actual != null && actual.requiereReferencia);
        JCheckBox activo = new JCheckBox("Disponible para nuevas ventas", actual == null || actual.activo);
        JSpinner orden = new JSpinner(new SpinnerNumberModel(actual == null ? 10 : actual.orden, 0, 9999, 1));
        for (JComponent c : new JComponent[]{nombre, tipo, orden}) { c.setFont(NORMAL); c.setPreferredSize(new Dimension(340, 38)); }
        referencia.setFont(NORMAL); activo.setFont(NORMAL);
        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(16, 20, 12, 20));
        GridBagConstraints gc = new GridBagConstraints(); gc.gridx = 0; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 5, 0);
        addForm(form, gc, etiqueta("Nombre del medio de pago")); addForm(form, gc, nombre);
        addForm(form, gc, etiqueta("Tipo de operación")); addForm(form, gc, tipo);
        addForm(form, gc, referencia); addForm(form, gc, activo);
        addForm(form, gc, etiqueta("Orden de visualización")); addForm(form, gc, orden);
        JPanel cont = new JPanel(new BorderLayout()); cont.setBackground(Color.WHITE);
        JLabel cab = new JLabel(actual == null ? "  NUEVO MEDIO DE PAGO" : "  EDITAR MEDIO DE PAGO");
        cab.setOpaque(true); cab.setBackground(AZUL_OSCURO); cab.setForeground(Color.WHITE);
        cab.setFont(new Font("Segoe UI", Font.BOLD, 17)); cab.setPreferredSize(new Dimension(420, 52));
        cont.add(cab, BorderLayout.NORTH); cont.add(form, BorderLayout.CENTER);
        JButton guardar = boton("Guardar", VERDE, () -> {}); JButton cancelar = boton("Cancelar", GRIS, () -> {});
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12)); acciones.setBackground(Color.WHITE);
        acciones.add(cancelar); acciones.add(guardar); cont.add(acciones, BorderLayout.SOUTH);
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), actual == null ? "Nuevo medio" : "Editar medio", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setContentPane(cont); dialogo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final boolean[] confirmado = {false};
        cancelar.addActionListener(e -> dialogo.dispose());
        guardar.addActionListener(e -> { confirmado[0] = true; dialogo.dispose(); });
        dialogo.pack(); dialogo.setMinimumSize(new Dimension(440, 490)); dialogo.setLocationRelativeTo(this); dialogo.setVisible(true);
        if (!confirmado[0]) return;
        String t = (String)tipo.getSelectedItem();
        Medio m = new Medio(actual == null ? 0 : actual.id, nombre.getText(), t, referencia.isSelected(), "EFECTIVO".equals(t), activo.isSelected(), (Integer)orden.getValue());
        try { dao.guardar(m); cargar(); } catch (Exception ex) { error(ex); }
    }
    private JLabel etiqueta(String txt) { JLabel l = new JLabel(txt); l.setFont(NEGRITA); l.setForeground(AZUL_OSCURO); return l; }
    private void addForm(JPanel p, GridBagConstraints gc, JComponent c) { gc.gridy++; p.add(c, gc); }
    private void error(Exception ex) { JOptionPane.showMessageDialog(this, "No se pudo completar la operación:\n" + ex.getMessage(), "Medios de pago", JOptionPane.ERROR_MESSAGE); }
}
