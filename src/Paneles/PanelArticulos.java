/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Diseños.PanelRedondeado;
import Diseños.RendererTabla;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import rojeru_san.rsbutton.RSButtonRound;
import rojerusan.RSComboBox;
import Dialogos.DialogoArticulo;
import Dialogos.DialogoNuevoArticulo;
import java.awt.Toolkit;
import Diseños.TablaEstilo;

/**
 *
 * @author santi
 */
public class PanelArticulos extends javax.swing.JPanel {

    ///variable globales
    private JTable tabla;

    private JScrollPane scroll;

    private PanelRedondeado panelFiltros;

    private PanelRedondeado panelTabla;

    private PanelRedondeado panelBotones;

    //==========================================
// LABELS
//==========================================
    private JLabel lblTitulo;
    private JLabel lblBuscar;
    private JLabel lblCategoria;
    private JLabel lblEstado;
    private JLabel lblSubtitulo;

//==========================================
// CAMPOS
//==========================================
    private TextFieldRedondeado txtBuscar;

    private RSComboBox comboCategoria;
    private RSComboBox comboEstado;
    private RSButtonRound btnFiltrar;

//==========================================
// BOTONES
//==========================================
    private RSButtonRound btnNuevo;
    private RSButtonRound btnEditar;
    private RSButtonRound btnEliminar;
    private RSButtonRound btnActualizar;

    //======================================
    //======== COLORES =====================
    //======================================
    Color AZUL_PRESION = new Color(15, 55, 110);

    Color VERDE_OK = new Color(25, 135, 84);
    Color ROJO_ERROR = new Color(220, 53, 69);

    Color BORDE_PANEL = new Color(30, 80, 160);
    Color TITULO_PANEL = new Color(25, 55, 110);

    // Azul Serena Soft - acciones principales
    private static final Color AZUL_PRINCIPAL = new Color(24, 72, 145);
    private static final Color AZUL_HOVER = new Color(35, 92, 180);

// Gris metálico oscuro
    private static final Color GRIS_METAL = new Color(70, 78, 88);
    private static final Color GRIS_METAL_HOVER = new Color(88, 98, 110);

// Gris acero claro
    private static final Color GRIS_CLARO = new Color(225, 229, 234);
    private static final Color GRIS_CLARO_HOVER = new Color(208, 214, 221);
    private static final Color TEXTO_OSCURO = new Color(45, 52, 60);

// Rojo solamente para acciones destructivas
    private static final Color ROJO_ACCION = new Color(190, 65, 65);
    private static final Color ROJO_HOVER = new Color(215, 75, 75);

    public PanelArticulos() {
        initComponents();
        armarLayOut();
        armarPanelFiltros();
        armarTabla();
        armarBotones();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void armarLayOut() {

        removeAll();
        setBackground(Color.white);
        setLayout(new BorderLayout(10, 10));

        //--------------------------------
        //------ Paneles -----------------
        //--------------------------------
        panelTabla = new PanelRedondeado();
        panelFiltros = new PanelRedondeado();
        panelBotones = new PanelRedondeado();

        //------------------------------
        //---------- tamaños -----------
        //------------------------------
        panelFiltros.setPreferredSize(new Dimension(0, 130));
        panelBotones.setPreferredSize(new Dimension(0, 80));

        //------------------------------
        //-------- agregar -------------
        //------------------------------
        add(panelFiltros, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void armarPanelFiltros() {

        panelFiltros.removeAll();
        panelFiltros.setLayout(null);
        panelFiltros.setBackground(Color.WHITE);

        //-----------------------------
        // TITULO
        //-----------------------------
        lblTitulo = new JLabel("GESTIÓN DE ARTÍCULOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 55, 110));
        lblTitulo.setBounds(25, 15, 320, 30);
        panelFiltros.add(lblTitulo);

        //-----------------------------
        // SUBTITULO
        //-----------------------------
        lblSubtitulo = new JLabel("Administración de productos del sistema");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(25, 42, 350, 20);
        panelFiltros.add(lblSubtitulo);

        //------------------------------------
        // BUSCAR
        //------------------------------------
        lblBuscar = new JLabel("Buscar");

        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setBounds(25, 82, 60, 25);

        panelFiltros.add(lblBuscar);

        txtBuscar = new TextFieldRedondeado();
        txtBuscar.setBounds(85, 78, 260, 36);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelFiltros.add(txtBuscar);

        //------------------------------------
        // CATEGORIA
        //------------------------------------
        lblCategoria = new JLabel("Categoría");

        lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCategoria.setBounds(370, 82, 70, 25);

        panelFiltros.add(lblCategoria);

        comboCategoria = new RSComboBox();
        comboCategoria.setBounds(445, 78, 170, 36);

        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{
                    "Todas",
                    "Bebidas",
                    "Almacén",
                    "Lácteos",
                    "Carnes",
                    "Limpieza"
                }
        ));

        panelFiltros.add(comboCategoria);

        //------------------------------------
        // ESTADO
        //------------------------------------
        lblEstado = new JLabel("Estado");

        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setBounds(640, 82, 55, 25);

        panelFiltros.add(lblEstado);

        comboEstado = new RSComboBox();
        comboEstado.setBounds(695, 78, 140, 36);

        comboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{
                    "Todos",
                    "Activos",
                    "Inactivos"
                }
        ));

        panelFiltros.add(comboEstado);

        //------------------------------------
        // BOTON
        //------------------------------------
        btnFiltrar = new RSButtonRound();

        btnFiltrar.setText("FILTRAR");

        btnFiltrar.setBounds(870, 76, 120, 40);

        btnFiltrar.setBackground(new Color(35, 90, 185));

        btnFiltrar.setColorHover(new Color(60, 120, 220));

        panelFiltros.add(btnFiltrar);

        panelFiltros.revalidate();
        panelFiltros.repaint();
    }

    private void armarTabla() {

        panelTabla.removeAll();
        panelTabla.setLayout(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);

        String columnas[] = {
            "Código",
            "Producto",
            "Categoría",
            "Costo",
            "Precio",
            "Stock",
            "Estado"
        };

        Object datos[][] = {
            {"A001", "Coca Cola 2.25L", "Bebidas", "$1200", "$2100", "55", "Activo"},
            {"A002", "Sprite 2.25L", "Bebidas", "$1150", "$2050", "33", "Activo"},
            {"A003", "Fanta 2.25L", "Bebidas", "$1180", "$2080", "18", "Activo"},
            {"A004", "Azúcar Ledesma", "Almacén", "$890", "$1500", "90", "Activo"},
            {"A005", "Yerba Playadito", "Almacén", "$3100", "$4700", "40", "Activo"},
            {"A006", "Arroz Gallo", "Almacén", "$980", "$1650", "15", "Activo"},
            {"A007", "Jamón Cocido", "Fiambrería", "$7200", "$9800", "12", "Activo"},
            {"A008", "Queso Cremoso", "Lácteos", "$6800", "$9100", "9", "Activo"},
            {"A009", "Leche Entera", "Lácteos", "$990", "$1450", "70", "Activo"},
            {"A010", "Aceite Natura", "Almacén", "$2200", "$3200", "28", "Activo"}

        };

        tabla = new JTable(datos, columnas);
        RendererTabla renderer = new RendererTabla();

        for (int i = 0; i < tabla.getColumnCount(); i++) {

            tabla.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderer);

        }

        tabla.setRowHeight(34);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabla.setSelectionBackground(new Color(35, 95, 185));

        tabla.setSelectionForeground(Color.WHITE);

        tabla.setGridColor(new Color(235, 235, 235));

        tabla.setShowVerticalLines(false);

        tabla.setShowHorizontalLines(true);

        tabla.setIntercellSpacing(new Dimension(0, 1));

        //-------------------------
        // Header
        //-------------------------
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabla.getTableHeader().setBackground(AZUL_PRESION);

        tabla.getTableHeader().setForeground(AZUL_PRESION);

        tabla.getTableHeader().setPreferredSize(new Dimension(0, 42));

        //-------------------------
        // Anchos
        //-------------------------
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);

        tabla.getColumnModel().getColumn(1).setPreferredWidth(280);

        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);

        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);

        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);

        tabla.getColumnModel().getColumn(5).setPreferredWidth(70);

        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);

        scroll = new JScrollPane(tabla);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(Color.WHITE);

        panelTabla.add(scroll, BorderLayout.CENTER);

        TablaEstilo.aplicar(tabla, scroll);

    }

   private void armarBotones() {

    panelBotones.removeAll();

    panelBotones.setLayout(
            new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    18
            )
    );

    panelBotones.setBackground(
            Color.WHITE
    );

    //==========================================================
    // NUEVO - ACCIÓN PRINCIPAL
    //==========================================================
    btnNuevo =
            crearBotonPrimarioPanel(
                    "NUEVO",
                    140
            );

    btnNuevo.addActionListener(e -> {

        abrirDialogArticulo();

    });

    //==========================================================
    // EDITAR
    //==========================================================
    btnEditar =
            crearBotonMetalPanel(
                    "EDITAR",
                    140
            );

    btnEditar.addActionListener(e -> {

        System.out.println(
                "Editar artículo"
        );

    });

    //==========================================================
    // ELIMINAR
    //==========================================================
    btnEliminar =
            crearBotonEliminarPanel(
                    "ELIMINAR",
                    140
            );

    btnEliminar.addActionListener(e -> {

        System.out.println(
                "Eliminar artículo"
        );

    });

    //==========================================================
    // ACTUALIZAR
    //==========================================================
    btnActualizar =
            crearBotonClaroPanel(
                    "ACTUALIZAR",
                    150
            );

    btnActualizar.addActionListener(e -> {

        System.out.println(
                "Actualizar tabla"
        );

    });

    //==========================================================
    // AGREGAR
    //==========================================================
    panelBotones.add(
            btnNuevo
    );

    panelBotones.add(
            btnEditar
    );

    panelBotones.add(
            btnEliminar
    );

    panelBotones.add(
            btnActualizar
    );

    panelBotones.revalidate();

    panelBotones.repaint();
}
   private RSButtonRound crearBotonMetalPanel(
        String texto,
        int ancho) {

    RSButtonRound boton =
            new RSButtonRound();

    boton.setText(
            texto
    );

    boton.setPreferredSize(
            new Dimension(
                    ancho,
                    40
            )
    );

    boton.setBackground(
            GRIS_METAL
    );

    boton.setColorHover(
            GRIS_METAL_HOVER
    );

    boton.setColorText(
            Color.WHITE
    );

    boton.setForeground(
            Color.WHITE
    );

    boton.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
            )
    );

    boton.setFocusable(
            false
    );

    return boton;
}
   private RSButtonRound crearBotonEliminarPanel(
        String texto,
        int ancho) {

    RSButtonRound boton =
            new RSButtonRound();

    boton.setText(
            texto
    );

    boton.setPreferredSize(
            new Dimension(
                    ancho,
                    40
            )
    );

    // Normalmente gris, no rojo permanentemente
    boton.setBackground(
            GRIS_METAL
    );

    // Rojo solamente cuando el usuario pasa el mouse
    boton.setColorHover(
            ROJO_ACCION
    );

    boton.setColorText(
            Color.WHITE
    );

    boton.setForeground(
            Color.WHITE
    );

    boton.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
            )
    );

    boton.setFocusable(
            false
    );

    return boton;
}
   
   private RSButtonRound crearBotonClaroPanel(
        String texto,
        int ancho) {

    RSButtonRound boton =
            new RSButtonRound();

    boton.setText(
            texto
    );

    boton.setPreferredSize(
            new Dimension(
                    ancho,
                    40
            )
    );

    boton.setBackground(
            GRIS_CLARO
    );

    boton.setColorHover(
            GRIS_CLARO_HOVER
    );

    boton.setColorText(
            TEXTO_OSCURO
    );

    boton.setForeground(
            TEXTO_OSCURO
    );

    boton.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
            )
    );

    boton.setFocusable(
            false
    );

    return boton;
}
   
   private RSButtonRound crearBotonPrimarioPanel(
        String texto,
        int ancho) {

    RSButtonRound boton =
            new RSButtonRound();

    boton.setText(
            texto
    );

    boton.setPreferredSize(
            new Dimension(
                    ancho,
                    40
            )
    );

    boton.setBackground(
            AZUL_PRINCIPAL
    );

    boton.setColorHover(
            AZUL_HOVER
    );

    boton.setColorText(
            Color.WHITE
    );

    boton.setForeground(
            Color.WHITE
    );

    boton.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
            )
    );

    boton.setFocusable(
            false
    );

    return boton;
}

   private void abrirDialogArticulo() {

    // Obtenemos la ventana principal
    java.awt.Window ventana =
            javax.swing.SwingUtilities
                    .getWindowAncestor(this);

    // Creamos el nuevo diálogo
    DialogoNuevoArticulo dialogo =
            new DialogoNuevoArticulo(
                    ventana
            );

    // Lo centramos respecto al sistema
    dialogo.setLocationRelativeTo(
            ventana
    );

    // Mostramos
    dialogo.setVisible(true);

    // Si el usuario presionó GUARDAR
    if (dialogo.isGuardado()) {

        System.out.println(
                "Nuevo artículo confirmado"
        );

        /*
         * MÁS ADELANTE:
         *
         * recargarTablaArticulos();
         *
         * Acá conectaremos MySQL.
         */
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
