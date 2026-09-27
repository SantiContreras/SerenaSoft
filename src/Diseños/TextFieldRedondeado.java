/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Diseños;

import app.bolivia.swing.JCTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author AMBIENTE 2
 */
public class TextFieldRedondeado extends JTextField {
    
    private int radio = 15;
    
    public TextFieldRedondeado() {
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12)); // padding interno
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // fondo
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(200, 200, 200)); // borde gris suave
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
    }
}
