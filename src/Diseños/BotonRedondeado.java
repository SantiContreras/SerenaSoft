/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Diseños;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 *
 * @author AMBIENTE 2
 */
public class BotonRedondeado extends JButton {

    private int radio = 20;

    public BotonRedondeado(String texto) {
        super(texto);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
         setOpaque(false);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🔵 color base
        g2.setColor(new Color(0, 102, 204));

        // 🔥 efecto hover
        if (getModel().isRollover()) {
            g2.setColor(new Color(0, 120, 215));
        }

        // 🔥 efecto click
        if (getModel().isPressed()) {
            g2.setColor(new Color(0, 80, 180));
        }
       
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // sin borde
    }

}
