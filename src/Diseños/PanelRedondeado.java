/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Diseños;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author AMBIENTE 2
 */
public class PanelRedondeado extends JPanel {
    
     
    private int radio = 20;

    private Color colorFondo = Color.WHITE;
    private Color colorBorde = new Color(60,125,205); // azul moderno
    private Color colorBordeHover = new Color(0, 150, 255);

    private boolean hover = false;

    public PanelRedondeado() {
        setOpaque(false); // 🔥 obligatorio para dibujar bien

        // 🔥 efecto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 🔥 suavizado PRO
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // =========================
        // 🔹 SOMBRA (muy importante para look pro)
        // =========================
        g2.setColor(new Color(0, 0, 0, 30)); // sombra suave
        g2.fillRoundRect(3, 3, width - 6, height - 6, radio, radio);

        // =========================
        // 🔹 FONDO
        // =========================
        g2.setColor(colorFondo);
        g2.fillRoundRect(0, 0, width - 4, height - 4, radio, radio);

        // =========================
        // 🔹 BORDE
        // =========================
        if (hover) {
            g2.setColor(colorBordeHover);
        } else {
            g2.setColor(colorBorde);
        }

        g2.setStroke(new BasicStroke(2)); // grosor borde
        g2.drawRoundRect(0, 0, width - 4, height - 4, radio, radio);

        g2.dispose();

        super.paintComponent(g);
    }

    // =========================
    // 🔹 SETTERS (por si querés customizar)
    // =========================
    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        repaint();
    }

    public void setColorBorde(Color colorBorde) {
        this.colorBorde = colorBorde;
        repaint();
    }

    public void setColorBordeHover(Color colorBordeHover) {
        this.colorBordeHover = colorBordeHover;
        repaint();
    }
}
