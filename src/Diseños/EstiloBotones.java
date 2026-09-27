package Diseños;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;


public final class EstiloBotones {

    public static final Color AZUL =
            new Color(15, 50, 110);

    public static final Color VERDE =
            new Color(25, 135, 84);

    public static final Color ROJO =
            new Color(190, 50, 50);

    public static final Color GRIS =
            new Color(110, 120, 135);

    private EstiloBotones() {
    }

    // =========================================================
    // ESTILO GENERAL
    // =========================================================

    public static void aplicar(
            JButton boton,
            Color color) {

        boton.setBackground(color);
        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }
    
    public static void corregirBotones(
        java.awt.Container contenedor) {

    for (java.awt.Component componente
            : contenedor.getComponents()) {

        if (componente instanceof JButton boton) {

            Color colorActual =
                    boton.getBackground();

            aplicar(
                    boton,
                    colorActual
            );
        }

        if (componente instanceof java.awt.Container hijo) {

            corregirBotones(hijo);
        }
    }
}

    // =========================================================
    // TIPOS
    // =========================================================

    public static void azul(JButton boton) {
        aplicar(boton, AZUL);
    }

    public static void verde(JButton boton) {
        aplicar(boton, VERDE);
    }

    public static void rojo(JButton boton) {
        aplicar(boton, ROJO);
    }

    public static void gris(JButton boton) {
        aplicar(boton, GRIS);
    }
}