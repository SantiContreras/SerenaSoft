package Diseños;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CardResumen extends PanelRedondeado {

    private JLabel lblIcono;
    private JLabel lblValor;
    private JLabel lblTitulo;

    public CardResumen() {

        inicializar();

    }

    private void inicializar() {

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(220,110));

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //------------------------------------------------
        // Icono
        //------------------------------------------------

        lblIcono = new JLabel("📦");

        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN,28));

        lblIcono.setForeground(new Color(40,120,220));

        //------------------------------------------------
        // Valor
        //------------------------------------------------

        lblValor = new JLabel("0");

        lblValor.setHorizontalAlignment(SwingConstants.CENTER);

        lblValor.setFont(new Font("Segoe UI", Font.BOLD,24));

        lblValor.setForeground(new Color(20,55,120));

        //------------------------------------------------
        // Titulo
        //------------------------------------------------

        lblTitulo = new JLabel("Título");

        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN,14));

        lblTitulo.setForeground(new Color(120,120,120));

        //------------------------------------------------

        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(new BorderLayout());

        centro.add(lblIcono, BorderLayout.NORTH);

        centro.add(lblValor, BorderLayout.CENTER);

        centro.add(lblTitulo, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);

    }

    //====================================================
    // SETTERS
    //====================================================

    public void setTitulo(String titulo){

        lblTitulo.setText(titulo);

    }

    public void setValor(String valor){

        lblValor.setText(valor);

    }

    public void setIcono(String icono){

        lblIcono.setText(icono);

    }

    public void setColorTitulo(Color color){

        lblTitulo.setForeground(color);

    }

    public void setColorValor(Color color){

        lblValor.setForeground(color);

    }

    public void setColorIcono(Color color){

        lblIcono.setForeground(color);

    }

}