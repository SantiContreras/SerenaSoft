package Dialogos;

import Diseños.EstiloBotones;
import Diseños.PanelRedondeado;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import rojeru_san.rsbutton.RSButtonRound;

public class DialogoBase extends JDialog {

    protected PanelRedondeado panelPrincipal;
    protected JPanel panelTitulo;
    protected JPanel panelContenido;
    protected JPanel panelBotones;
    protected RSButtonRound btnCerrar;
    private Point clickInicial;

    protected JLabel lblTitulo;

    public DialogoBase(Frame owner, String titulo) {

        super(owner, true);

        setUndecorated(true);
        setTitle(titulo);

        setBackground(new Color(0, 0, 0, 0));

        inicializar();

        pack();
        
         EstiloBotones.corregirBotones(
                getContentPane()
    );

        setMinimumSize(getSize());

        setLocationRelativeTo(owner);

        panelTitulo.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                clickInicial = e.getPoint();
            }

        });

        panelTitulo.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                Point p = e.getLocationOnScreen();

                setLocation(
                        p.x - clickInicial.x,
                        p.y - clickInicial.y);

            }

        });

    }

    private void inicializar() {

        setLayout(new GridBagLayout());

        panelPrincipal = new PanelRedondeado();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(Color.white);
        panelPrincipal.setPreferredSize(new Dimension(720, 520));

        //-------------------------------------------------------
        // TITULO
        //-------------------------------------------------------
        panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        new Color(255, 255, 255, 80)
                )
        );
        panelTitulo.setPreferredSize(
                new Dimension(0, 55));

        panelTitulo.setBackground(new Color(80, 100, 160, 140));

        lblTitulo = new JLabel();
        lblTitulo.setIcon(
                new ImageIcon(getClass().getResource(
                        "/img/stock1.png")));
        lblTitulo.setText(getTitle());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(new EmptyBorder(0, 20, 0, 0));

        btnCerrar = new RSButtonRound();

        btnCerrar.setText("X");

        btnCerrar.setPreferredSize(new Dimension(40, 40));

        btnCerrar.setBackground(new Color(20, 55, 120));

        btnCerrar.setColorHover(new Color(190, 40, 40));

        btnCerrar.setColorText(Color.WHITE);

        btnCerrar.setColorTextHover(Color.WHITE);

        btnCerrar.addActionListener(e -> dispose());

        panelTitulo.add(lblTitulo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                8,
                8));

        derecha.setOpaque(false);

        derecha.add(btnCerrar);

        panelTitulo.add(derecha, BorderLayout.EAST);

        KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");

        getRootPane().registerKeyboardAction(
                e -> dispose(),
                esc,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        //-------------------------------------------------------
        // CONTENIDO
        //-------------------------------------------------------
        panelContenido = new JPanel();
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        //-------------------------------------------------------
        // BOTONES
        //-------------------------------------------------------
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setPreferredSize(new Dimension(0, 65));
        panelBotones.setBackground(Color.WHITE);

        //-------------------------------------------------------
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

    }

    public JPanel getContenido() {

        return panelContenido;

    }

    public JPanel getBotones() {

        return panelBotones;

    }

    public void setTitulo(String texto) {

        lblTitulo.setText(texto);

    }

}
