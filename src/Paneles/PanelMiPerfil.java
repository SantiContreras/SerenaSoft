package Paneles;

import Diseños.EstiloBotones;
import Principal.PanelPrincipal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class PanelMiPerfil extends JPanel {

    // ====================================================
    // REFERENCIA AL PANEL PRINCIPAL
    // ====================================================

    private PanelPrincipal panelPrincipal;

    // ====================================================
    // COMPONENTES
    // ====================================================

    private JLabel lblTitulo;

    private JLabel lblUsuarioTitulo;
    private JLabel lblUsuario;

    private JLabel lblNombreTitulo;
    private JLabel lblNombre;

    private JLabel lblRolTitulo;
    private JLabel lblRol;

    private JLabel lblEstadoTitulo;
    private JLabel lblEstado;

    private JLabel lblSeguridad;

    private JButton btnCambiarClave;
    private JButton btnVolver;

    // ====================================================
    // COLORES
    // ====================================================

    private final Color AZUL = new Color(25, 70, 145);

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(0, 145, 70);

    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color GRIS =
            new Color(90, 90, 90);

    // ====================================================
    // CONSTRUCTOR
    // ====================================================

    public PanelMiPerfil(PanelPrincipal panelPrincipal) {

        this.panelPrincipal = panelPrincipal;

        inicializarComponentes();

        construirPanel();

        configurarEventos();
        
         EstiloBotones.corregirBotones(this);

    }

    // ====================================================
    // INICIALIZAR COMPONENTES
    // ====================================================

    private void inicializarComponentes() {

        lblTitulo =
                new JLabel("MI PERFIL");

        lblUsuarioTitulo =
                new JLabel("Usuario");

        lblUsuario =
                new JLabel("Santiago");

        lblNombreTitulo =
                new JLabel("Nombre");

        lblNombre =
                new JLabel("Santiago Contreras");

        lblRolTitulo =
                new JLabel("Rol");

        lblRol =
                new JLabel("Administrador");

        lblEstadoTitulo =
                new JLabel("Estado");

        lblEstado =
                new JLabel("● Activo");

        lblSeguridad =
                new JLabel("SEGURIDAD");

        btnCambiarClave =
                new JButton("Cambiar Contraseña");

        btnVolver =
                new JButton("Volver");

    }

    // ====================================================
    // CONSTRUIR PANEL
    // ====================================================

    private void construirPanel() {

        setLayout(new GridBagLayout());

        setBackground(FONDO);

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        // =================================================
        // TÍTULO
        // =================================================

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        GridBagConstraints gbcTitulo =
                new GridBagConstraints();

        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;

        gbcTitulo.gridwidth = 2;

        gbcTitulo.anchor =
                GridBagConstraints.WEST;

        gbcTitulo.insets =
                new Insets(
                        0,
                        0,
                        25,
                        0
                );

        add(
                lblTitulo,
                gbcTitulo
        );

        // =================================================
        // CONFIGURAR ETIQUETAS
        // =================================================

        configurarEtiquetaTitulo(
                lblUsuarioTitulo
        );

        configurarEtiquetaTitulo(
                lblNombreTitulo
        );

        configurarEtiquetaTitulo(
                lblRolTitulo
        );

        configurarEtiquetaTitulo(
                lblEstadoTitulo
        );

        configurarValor(
                lblUsuario
        );

        configurarValor(
                lblNombre
        );

        configurarValor(
                lblRol
        );

        // =================================================
        // ESTADO
        // =================================================

        lblEstado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblEstado.setForeground(
                VERDE
        );

        // =================================================
        // FILAS
        // =================================================

        agregarFila(
                lblUsuarioTitulo,
                lblUsuario,
                1
        );

        agregarFila(
                lblNombreTitulo,
                lblNombre,
                2
        );

        agregarFila(
                lblRolTitulo,
                lblRol,
                3
        );

        agregarFila(
                lblEstadoTitulo,
                lblEstado,
                4
        );

        // =================================================
        // SEGURIDAD
        // =================================================

        lblSeguridad.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        lblSeguridad.setForeground(
                AZUL_OSCURO
        );

        GridBagConstraints gbcSeguridad =
                new GridBagConstraints();

        gbcSeguridad.gridx = 0;
        gbcSeguridad.gridy = 5;

        gbcSeguridad.gridwidth = 2;

        gbcSeguridad.anchor =
                GridBagConstraints.WEST;

        gbcSeguridad.insets =
                new Insets(
                        30,
                        0,
                        15,
                        0
                );

        add(
                lblSeguridad,
                gbcSeguridad
        );

        // =================================================
        // BOTÓN CAMBIAR CONTRASEÑA
        // =================================================

        btnCambiarClave.setPreferredSize(
                new Dimension(
                        220,
                        40
                )
        );

        btnCambiarClave.setFocusPainted(
                false
        );

        btnCambiarClave.setBackground(
                AZUL
        );

        btnCambiarClave.setForeground(
                Color.WHITE
        );

        btnCambiarClave.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        GridBagConstraints gbcClave =
                new GridBagConstraints();

        gbcClave.gridx = 0;
        gbcClave.gridy = 6;

        gbcClave.gridwidth = 2;

        gbcClave.anchor =
                GridBagConstraints.WEST;

        gbcClave.insets =
                new Insets(
                        5,
                        0,
                        20,
                        0
                );

        add(
                btnCambiarClave,
                gbcClave
        );

        // =================================================
        // BOTÓN VOLVER
        // =================================================

        btnVolver.setPreferredSize(
                new Dimension(
                        140,
                        40
                )
        );

        btnVolver.setFocusPainted(
                false
        );

        btnVolver.setBackground(
                new Color(
                        120,
                        120,
                        120
                )
        );

        btnVolver.setForeground(
                Color.WHITE
        );

        btnVolver.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        GridBagConstraints gbcVolver =
                new GridBagConstraints();

        gbcVolver.gridx = 0;
        gbcVolver.gridy = 7;

        gbcVolver.gridwidth = 2;

        gbcVolver.anchor =
                GridBagConstraints.WEST;

        gbcVolver.insets =
                new Insets(
                        10,
                        0,
                        0,
                        0
                );

        add(
                btnVolver,
                gbcVolver
        );

    }

    // ====================================================
    // AGREGAR FILA
    // ====================================================

    private void agregarFila(
            JLabel titulo,
            JLabel valor,
            int fila) {

        GridBagConstraints gbcTitulo =
                new GridBagConstraints();

        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = fila;

        gbcTitulo.anchor =
                GridBagConstraints.WEST;

        gbcTitulo.insets =
                new Insets(
                        7,
                        0,
                        7,
                        30
                );

        add(
                titulo,
                gbcTitulo
        );

        GridBagConstraints gbcValor =
                new GridBagConstraints();

        gbcValor.gridx = 1;
        gbcValor.gridy = fila;

        gbcValor.weightx = 1;

        gbcValor.anchor =
                GridBagConstraints.WEST;

        gbcValor.insets =
                new Insets(
                        7,
                        0,
                        7,
                        0
                );

        add(
                valor,
                gbcValor
        );

    }

    // ====================================================
    // CONFIGURAR ETIQUETAS
    // ====================================================

    private void configurarEtiquetaTitulo(
            JLabel etiqueta) {

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        etiqueta.setForeground(
                GRIS
        );

    }

    // ====================================================
    // CONFIGURAR VALORES
    // ====================================================

    private void configurarValor(
            JLabel etiqueta) {

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        etiqueta.setForeground(
                new Color(
                        30,
                        30,
                        30
                )
        );

    }

    // ====================================================
    // EVENTOS
    // ====================================================

    private void configurarEventos() {

        // =================================================
        // CAMBIAR CONTRASEÑA
        // =================================================

        btnCambiarClave.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Módulo para cambiar contraseña.",
                    "Seguridad",
                    JOptionPane.INFORMATION_MESSAGE
            );

        });

        // =================================================
        // VOLVER
        // =================================================

        btnVolver.addActionListener(e -> {

            if (panelPrincipal != null) {

                panelPrincipal.mostrarPanel(
                        new PanelVentas()
                );

            }

        });

    }

}