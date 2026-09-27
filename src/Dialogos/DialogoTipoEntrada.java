package Dialogos;

import Diseños.EstiloBotones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DialogoTipoEntrada extends JDialog {

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnManual;
    private JButton btnCodigoBarras;
    private JButton btnImportarPDF;
    private JButton btnCancelar;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color AZUL_OSCURO
            = new Color(15, 50, 110);

    private final Color AZUL
            = new Color(25, 70, 145);

    private final Color AZUL_CLARO
            = new Color(230, 238, 250);

    private final Color VERDE
            = new Color(25, 135, 84);

    private final Color NARANJA
            = new Color(235, 145, 20);

    private final Color FONDO
            = new Color(245, 247, 250);

    private final Color BORDE
            = new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO
            = new Color(95, 105, 120);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoTipoEntrada(Window parent) {

        super(parent);

        inicializarComponentes();

        construirDialogo();

        configurarEventos();

        setModal(true);

        setSize(
                new Dimension(
                        850,
                        470
                )
        );

        setLocationRelativeTo(parent);
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        setResizable(false);
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        btnManual
                = crearBotonAccion(
                        "Comenzar"
                );

        btnCodigoBarras
                = crearBotonAccion(
                        "Escanear"
                );

        btnImportarPDF
                = crearBotonAccion(
                        "Seleccionar PDF"
                );

        btnCancelar
                = new JButton("Cancelar");

        btnCancelar.setPreferredSize(
                new Dimension(
                        130,
                        38
                )
        );

        btnCancelar.setBackground(
                new Color(
                        120,
                        120,
                        120
                )
        );

        btnCancelar.setForeground(
                Color.WHITE
        );

        btnCancelar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btnCancelar.setFocusPainted(false);

        btnCancelar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    //==========================================================
    // CONSTRUIR DIALOGO
    //==========================================================
    private void construirDialogo() {

        setTitle(
                "Nueva Entrada de Mercadería"
        );

        setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                FONDO
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header
                = new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                Color.WHITE
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        JPanel titulos
                = new JPanel();

        titulos.setOpaque(false);

        titulos.setLayout(
                new javax.swing.BoxLayout(
                        titulos,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        "NUEVA ENTRADA DE MERCADERÍA"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        JLabel lblSubtitulo
                = new JLabel(
                        "Seleccione cómo desea registrar el ingreso de mercadería"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        titulos.add(lblTitulo);

        titulos.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        titulos.add(lblSubtitulo);

        header.add(
                titulos,
                BorderLayout.WEST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // TARJETAS
        //======================================================
        JPanel centro
                = new JPanel(
                        new GridLayout(
                                1,
                                3,
                                18,
                                0
                        )
                );

        centro.setBackground(
                FONDO
        );

        centro.setBorder(
                new EmptyBorder(
                        25,
                        25,
                        20,
                        25
                )
        );

        centro.add(
                crearTarjeta(
                        "CARGA MANUAL",
                        "Agregue los productos uno por uno indicando cantidad y costo.",
                        btnManual,
                        AZUL
                )
        );

        centro.add(
                crearTarjeta(
                        "CÓDIGO DE BARRAS",
                        "Escanee rápidamente los productos que ingresan al stock.",
                        btnCodigoBarras,
                        VERDE
                )
        );

        centro.add(
                crearTarjeta(
                        "IMPORTAR FACTURA PDF",
                        "Seleccione una factura del proveedor y revise los productos detectados.",
                        btnImportarPDF,
                        NARANJA
                )
        );

        add(
                centro,
                BorderLayout.CENTER
        );

        //======================================================
        // FOOTER
        //======================================================
        JPanel footer
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                10
                        )
                );

        footer.setBackground(
                Color.WHITE
        );

        footer.setBorder(
                new EmptyBorder(
                        5,
                        20,
                        10,
                        20
                )
        );

        footer.add(
                btnCancelar
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }

    //==========================================================
    // CREAR TARJETA
    //==========================================================
    private JPanel crearTarjeta(
            String titulo,
            String descripcion,
            JButton boton,
            Color color) {

        JPanel tarjeta
                = new JPanel(
                        new BorderLayout(
                                10,
                                15
                        )
                );

        tarjeta.setBackground(
                Color.WHITE
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDE
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        //======================================================
        // TITULO
        //======================================================
        JLabel lblTitulo
                = new JLabel(
                        titulo,
                        SwingConstants.CENTER
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lblTitulo.setForeground(
                color
        );

        tarjeta.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        //======================================================
        // DESCRIPCION
        //======================================================
        JLabel lblDescripcion
                = new JLabel(
                        "<html><div style='text-align:center;'>"
                        + descripcion
                        + "</div></html>",
                        SwingConstants.CENTER
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblDescripcion.setForeground(
                TEXTO_SECUNDARIO
        );

        tarjeta.add(
                lblDescripcion,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTON
        //======================================================
        boton.setBackground(
                color
        );

        JPanel panelBoton
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        panelBoton.setOpaque(false);

        panelBoton.add(
                boton
        );

        tarjeta.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return tarjeta;
    }

    //==========================================================
    // CREAR BOTON
    //==========================================================
    private JButton crearBotonAccion(
            String texto) {

        JButton boton
                = new JButton(
                        texto
                );

        boton.setPreferredSize(
                new Dimension(
                        150,
                        38
                )
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        boton.setFocusPainted(
                false
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // MANUAL
        //======================================================
        btnManual.addActionListener(e -> {

            Window ventanaPadre
                    = getOwner();

            dispose();

            DialogoEntradaManual dialogo
                    = new DialogoEntradaManual(
                            ventanaPadre
                    );

            dialogo.setVisible(true);

        });

        //======================================================
        // CODIGO DE BARRAS
        //======================================================
        btnCodigoBarras.addActionListener(e -> {

            Window ventanaPadre
                    = getOwner();

            dispose();

            DialogoEntradaScanner dialogo
                    = new DialogoEntradaScanner(
                            ventanaPadre
                    );

            dialogo.setVisible(true);

        });

        //======================================================
        // PDF
        //======================================================
        btnImportarPDF.addActionListener(e -> {

            Window ventanaPadre
                    = getOwner();

            dispose();

            DialogoImportarFacturaPDF dialogo
                    = new DialogoImportarFacturaPDF(
                            ventanaPadre
                    );

            dialogo.setVisible(true);

        });

        //======================================================
        // CANCELAR
        //======================================================
        btnCancelar.addActionListener(e -> {

            dispose();

        });
    }
}
