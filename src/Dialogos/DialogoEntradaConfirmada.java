package Dialogos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Solo presenta una compra ya confirmada; no modifica datos ni stock. */
public class DialogoEntradaConfirmada extends JDialog {

    private static final Color AZUL = new Color(20, 51, 105);
    private static final Color VERDE = new Color(21, 133, 83);
    private static final Color FONDO = new Color(247, 249, 252);
    private static final Color TEXTO = new Color(45, 55, 72);
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 13);

    public DialogoEntradaConfirmada(Frame propietario, long numeroCompra,
            String proveedor, int cantidadProductos, String deposito,
            String totalFormateado) {
        super(propietario, "Entrada confirmada", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        construir(numeroCompra, proveedor, cantidadProductos, deposito, totalFormateado);
        pack();
        setSize(Math.max(getWidth(), 570), Math.max(getHeight(), 470));
        setLocationRelativeTo(propietario);
    }

    private void construir(long numeroCompra, String proveedor,
            int cantidadProductos, String deposito, String totalFormateado) {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);

        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(AZUL);
        cabecera.setBorder(new EmptyBorder(20, 24, 21, 24));

        JLabel titulo = new JLabel("ENTRADA CONFIRMADA", iconoConfirmacion(30), SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setIconTextGap(12);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(10));

        JLabel subtitulo = new JLabel("La mercadería se registró correctamente");
        subtitulo.setFont(FUENTE);
        subtitulo.setForeground(new Color(225, 235, 248));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecera.add(subtitulo);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(22, 28, 10, 28));

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                new EmptyBorder(15, 17, 15, 17)));

        agregarFila(tarjeta, 0, "Compra N.º", String.valueOf(numeroCompra), null, false);
        agregarFila(tarjeta, 1, "Proveedor", valor(proveedor), "clientes2.png", false);
        agregarFila(tarjeta, 2, "Productos", String.valueOf(cantidadProductos), "caja1.png", false);
        agregarFila(tarjeta, 3, "Depósito", valor(deposito), null, false);
        agregarFila(tarjeta, 4, "TOTAL", valor(totalFormateado), null, true);
        centro.add(tarjeta, BorderLayout.CENTER);

        JLabel estado = new JLabel("Stock actualizado correctamente", iconoConfirmacion(21), SwingConstants.CENTER);
        estado.setForeground(VERDE);
        estado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        estado.setIconTextGap(10);
        centro.add(estado, BorderLayout.SOUTH);
        raiz.add(centro, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 13));
        pie.setOpaque(false);
        pie.setBorder(new EmptyBorder(0, 28, 14, 28));

        // Pintado propio para evitar que el Look and Feel ignore los colores.
        JButton aceptar = new JButton("ACEPTAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new Color(12, 94, 57)
                        : getModel().isRollover() ? new Color(17, 113, 70) : VERDE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        aceptar.setPreferredSize(new Dimension(195, 45));
        aceptar.setContentAreaFilled(false);
        aceptar.setOpaque(false);
        aceptar.setBorderPainted(false);
        aceptar.setForeground(Color.WHITE);
        aceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aceptar.setFocusPainted(false);
        aceptar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        aceptar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(aceptar);
        pie.add(aceptar);
        raiz.add(pie, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void agregarFila(JPanel panel, int fila, String etiqueta,
            String valor, String recurso, boolean destacado) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(destacado ? 12 : 8, 3, 8, 15);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.weightx = 0;

        JLabel izquierda = new JLabel(etiqueta);
        izquierda.setFont(new Font("Segoe UI", Font.BOLD, destacado ? 15 : 13));
        izquierda.setForeground(TEXTO);
        ImageIcon icono = cargarIcono(recurso, 19);
        if (icono != null) {
            izquierda.setIcon(icono);
            izquierda.setIconTextGap(9);
        }
        panel.add(izquierda, c);

        JLabel derecha = new JLabel(valor);
        derecha.setFont(new Font("Segoe UI", Font.BOLD, destacado ? 20 : 13));
        derecha.setForeground(destacado ? VERDE : AZUL);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(derecha, c);
    }

    private ImageIcon cargarIcono(String archivo, int lado) {
        if (archivo == null) return null;
        URL url = getClass().getResource("/img/" + archivo);
        if (url == null) return null;
        Image imagen = new ImageIcon(url).getImage().getScaledInstance(
                lado, lado, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    // Check dibujado: evita caracteres cuadrados por fuentes sin soporte Unicode.
    private ImageIcon iconoConfirmacion(int lado) {
        BufferedImage imagen = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(VERDE);
        g.fillOval(1, 1, lado - 2, lado - 2);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(Math.max(2f, lado / 10f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(lado * 25 / 100, lado * 51 / 100,
                lado * 43 / 100, lado * 68 / 100);
        g.drawLine(lado * 43 / 100, lado * 68 / 100,
                lado * 76 / 100, lado * 33 / 100);
        g.dispose();
        return new ImageIcon(imagen);
    }

    private String valor(String texto) {
        return texto == null || texto.isBlank() ? "No especificado" : texto;
    }
}
