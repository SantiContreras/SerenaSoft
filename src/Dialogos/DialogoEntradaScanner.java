package Dialogos;

import Dao.ProductoDao;
import model.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Recepción asistida por escáner HID.
 * Hereda íntegramente el circuito seguro de entrada manual: proveedor, depósito,
 * comprobante, impuestos, presentaciones, cantidades, costos, precios pendientes,
 * conciliación, CompraService y persistencia. Sólo automatiza la identificación.
 * No modifica stock por leer un código: el operador agrega y confirma cada línea.
 */
public final class DialogoEntradaScanner extends DialogoEntradaManual {
    private final ProductoDao productoDao = new ProductoDao();
    private final JTextField campoCodigo = new JTextField(26);
    private final JLabel estado = new JLabel("El lector identifica el artículo. Vos verificás presentación, cantidad, costo y precio antes de agregar.");
    private boolean procesando = false;

    public DialogoEntradaScanner(Window parent) {
        super(parent);
        setTitle("Serena Soft | Entrada de mercadería por escáner");
        configurarBarraScanner();
        // El modo escáner comienza en recepción, ahora el primer paso.
        seleccionarPasoProductos();
        SwingUtilities.invokeLater(() -> campoCodigo.requestFocusInWindow());
    }

    private void seleccionarPasoProductos() {
        for (java.awt.Component c : getContentPane().getComponents()) {
            if (c instanceof javax.swing.JTabbedPane) {
                ((javax.swing.JTabbedPane) c).setSelectedIndex(0);
            }
        }
    }

    private void configurarBarraScanner() {
        JPanel raiz = (JPanel) getContentPane();
        java.awt.Component cabeceraExistente = ((BorderLayout) raiz.getLayout())
                .getLayoutComponent(BorderLayout.NORTH);
        if (cabeceraExistente != null) raiz.remove(cabeceraExistente);

        JPanel encabezado = new JPanel(new BorderLayout());
        // El encabezado común es el mismo, pero identifica correctamente el modo escáner.
        cambiarTituloCabecera(cabeceraExistente);
        if (cabeceraExistente != null) encabezado.add(cabeceraExistente, BorderLayout.NORTH);

        JPanel barra = new JPanel(new BorderLayout(12, 7));
        barra.setBackground(new Color(232, 245, 241));
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(99, 165, 141)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        JLabel titulo = new JLabel("LECTURA DEL PRODUCTO   |   Escaneá el código y revisá la compra");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(new Color(25, 80, 68));
        barra.add(titulo, BorderLayout.NORTH);

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        campoCodigo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        campoCodigo.setPreferredSize(new Dimension(330, 43));
        campoCodigo.setBackground(Color.WHITE);
        campoCodigo.setForeground(new Color(25, 80, 68));
        campoCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(24, 135, 102), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoCodigo.setToolTipText("Lector USB tipo teclado: configurar sufijo Enter");
        fila.add(campoCodigo, BorderLayout.CENTER);
        JButton buscar = new JButton("BUSCAR ARTÍCULO") {
            @Override protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? new Color(12, 135, 104) : new Color(153, 166, 183));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                java.awt.FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        buscar.setContentAreaFilled(false);
        buscar.setBorderPainted(false);
        buscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buscar.setForeground(Color.WHITE);
        buscar.setBackground(new Color(12, 135, 104));
        buscar.setOpaque(true);
        buscar.setFocusPainted(false);
        buscar.setPreferredSize(new Dimension(175, 43));
        fila.add(buscar, BorderLayout.EAST);
        barra.add(fila, BorderLayout.CENTER);

        estado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        estado.setForeground(new Color(58, 74, 96));
        barra.add(estado, BorderLayout.SOUTH);
        encabezado.add(barra, BorderLayout.CENTER);
        raiz.add(encabezado, BorderLayout.NORTH);

        campoCodigo.addActionListener(e -> identificar());
        buscar.addActionListener(e -> identificar());
        raiz.revalidate();
        raiz.repaint();
    }

    private void cambiarTituloCabecera(java.awt.Component componente) {
        if (componente instanceof javax.swing.JLabel) {
            javax.swing.JLabel l = (javax.swing.JLabel) componente;
            if (l.getText() != null && l.getText().contains("ENTRADA MANUAL DE MERCADERÍA"))
                l.setText("SERENA SOFT   |   ENTRADA DE MERCADERÍA POR ESCÁNER");
        }
        if (componente instanceof java.awt.Container)
            for (java.awt.Component hijo : ((java.awt.Container) componente).getComponents())
                cambiarTituloCabecera(hijo);
    }

    private void identificar() {
        if (procesando) return;
        String codigo = campoCodigo.getText().trim();
        if (codigo.isEmpty()) {
            estado.setText("Ingresá un código para continuar.");
            campoCodigo.requestFocusInWindow();
            return;
        }
        procesando = true;
        try {
            Producto producto = productoDao.buscarParaEscaner(codigo);
            if (producto == null) {
                estado.setText("Código no encontrado: " + codigo + ". Registrá el artículo antes de recibirlo.");
                JOptionPane.showMessageDialog(this,
                        "El código '" + codigo + "' no figura en el catálogo.\n"
                        + "Usá + NUEVO ARTÍCULO en el formulario de recepción.\n"
                        + "Después de guardarlo, volvé a escanearlo.",
                        "Artículo no registrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!producto.isActivo()) {
                estado.setText("Artículo inactivo: " + producto.getNombre());
                JOptionPane.showMessageDialog(this,
                        "El artículo está inactivo. Un administrador debe revisarlo antes de recibirlo.",
                        "Artículo inactivo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            seleccionarProductoDesdeScanner(producto);
            estado.setText("Identificado: " + producto.getCodigo() + " | " + producto.getNombre()
                    + "  —  Elegí presentación, cantidad, costo y precio; luego AGREGAR PRODUCTO.");
            campoCodigo.setText("");
        } catch (Exception ex) {
            estado.setText("No se pudo identificar el código. Revisá la conexión y el catálogo.");
            JOptionPane.showMessageDialog(this,
                    "No se pudo buscar el artículo: " + ex.getMessage(),
                    "Lectura de código", JOptionPane.ERROR_MESSAGE);
        } finally {
            procesando = false;
        }
    }
}
