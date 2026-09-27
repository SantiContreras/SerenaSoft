package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class PanelConfigProductos extends JPanel {

    //==========================================================
    // MODELOS Y LISTAS
    //==========================================================
    private DefaultListModel<String> modeloCategorias;
    private DefaultListModel<String> modeloMarcas;
    private DefaultListModel<String> modeloUnidades;

    private JList<String> listaCategorias;
    private JList<String> listaMarcas;
    private JList<String> listaUnidades;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnNuevaCategoria;
    private JButton btnEditarCategoria;
    private JButton btnEliminarCategoria;

    private JButton btnNuevaMarca;
    private JButton btnEditarMarca;
    private JButton btnEliminarMarca;

    private JButton btnNuevaUnidad;
    private JButton btnEditarUnidad;
    private JButton btnEliminarUnidad;

    private JButton btnGuardar;

    //==========================================================
    // CONFIGURACION GENERAL
    //==========================================================
    private JComboBox<String> cboIvaPredeterminado;
    private JTextField txtStockMinimo;

    private JCheckBox chkAvisarStockMinimo;
    private JCheckBox chkPermitirVentaSinStock;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO = new Color(245, 247, 250);
    private final Color BLANCO = Color.WHITE;

    private final Color AZUL = new Color(25, 70, 145);
    private final Color AZUL_OSCURO = new Color(15, 50, 110);

    private final Color VERDE = new Color(25, 135, 84);
    private final Color ROJO = new Color(200, 55, 55);
    private final Color GRIS = new Color(110, 120, 135);

    private final Color BORDE = new Color(215, 222, 232);
    private final Color TEXTO_SECUNDARIO = new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigProductos() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();

    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        modeloCategorias = new DefaultListModel<>();
        modeloMarcas = new DefaultListModel<>();
        modeloUnidades = new DefaultListModel<>();

        listaCategorias = new JList<>(modeloCategorias);
        listaMarcas = new JList<>(modeloMarcas);
        listaUnidades = new JList<>(modeloUnidades);

        configurarLista(listaCategorias);
        configurarLista(listaMarcas);
        configurarLista(listaUnidades);

        btnNuevaCategoria = crearBoton("Nueva", AZUL);
        btnEditarCategoria = crearBoton("Editar", GRIS);
        btnEliminarCategoria = crearBoton("Eliminar", ROJO);

        btnNuevaMarca = crearBoton("Nueva", AZUL);
        btnEditarMarca = crearBoton("Editar", GRIS);
        btnEliminarMarca = crearBoton("Eliminar", ROJO);

        btnNuevaUnidad = crearBoton("Nueva", AZUL);
        btnEditarUnidad = crearBoton("Editar", GRIS);
        btnEliminarUnidad = crearBoton("Eliminar", ROJO);

        btnGuardar = crearBoton("Guardar Cambios", VERDE);
        btnGuardar.setPreferredSize(
                new Dimension(150, 36)
        );

        btnGuardar.setMinimumSize(
                new Dimension(150, 36)
        );

        cboIvaPredeterminado = new JComboBox<>();

        cboIvaPredeterminado.addItem("21%");
        cboIvaPredeterminado.addItem("10.5%");
        cboIvaPredeterminado.addItem("Exento");
        cboIvaPredeterminado.addItem("No Gravado");

        txtStockMinimo = new JTextField();

        chkAvisarStockMinimo
                = new JCheckBox("Avisar cuando un producto llegue al stock mínimo");

        chkPermitirVentaSinStock
                = new JCheckBox("Permitir venta de productos sin stock");

        chkAvisarStockMinimo.setOpaque(false);
        chkPermitirVentaSinStock.setOpaque(false);

    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        JPanel contenedor = new JPanel(new BorderLayout());

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(25, 30, 25, 30)
                )
        );

        //------------------------------------------------------
        // HEADER
        //------------------------------------------------------
        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo
                = new JLabel("PRODUCTOS");

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitulo.setForeground(AZUL_OSCURO);

        JLabel lblSubtitulo
                = new JLabel(
                        "Configuración utilizada por el catálogo de artículos"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(TEXTO_SECUNDARIO);

        header.add(lblTitulo);
        header.add(Box.createVerticalStrut(5));
        header.add(lblSubtitulo);

        contenedor.add(
                header,
                BorderLayout.NORTH
        );

        //------------------------------------------------------
        // CENTRO
        //------------------------------------------------------
        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new BoxLayout(
                        centro,
                        BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        20,
                        0
                )
        );

        JPanel filaCatalogos
                = new JPanel(
                        new java.awt.GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        filaCatalogos.setOpaque(false);

        filaCatalogos.add(
                crearPanelCatalogo(
                        "Categorías",
                        listaCategorias,
                        btnNuevaCategoria,
                        btnEditarCategoria,
                        btnEliminarCategoria
                )
        );

        filaCatalogos.add(
                crearPanelCatalogo(
                        "Marcas",
                        listaMarcas,
                        btnNuevaMarca,
                        btnEditarMarca,
                        btnEliminarMarca
                )
        );

        filaCatalogos.add(
                crearPanelCatalogo(
                        "Unidades",
                        listaUnidades,
                        btnNuevaUnidad,
                        btnEditarUnidad,
                        btnEliminarUnidad
                )
        );

        filaCatalogos.setPreferredSize(
                new Dimension(0, 230)
        );

        filaCatalogos.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        230
                )
        );

        centro.add(filaCatalogos);

        centro.add(
                Box.createVerticalStrut(20)
        );

        centro.add(
                crearPanelConfiguracionGeneral()
        );

        JScrollPane scrollCentro = new JScrollPane(centro);

        scrollCentro.setBorder(null);

        scrollCentro.setOpaque(false);
        scrollCentro.getViewport().setOpaque(false);

        scrollCentro.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollCentro.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);

        contenedor.add(
                scrollCentro,
                BorderLayout.CENTER
        );

        //------------------------------------------------------
        // BOTONES
        //------------------------------------------------------
        JPanel panelBotones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        panelBotones.add(btnGuardar);

        contenedor.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );

    }

    //==========================================================
    // PANEL CATALOGO
    //==========================================================
    private JPanel crearPanelCatalogo(
            String titulo,
            JList<String> lista,
            JButton btnNuevo,
            JButton btnEditar,
            JButton btnEliminar) {

        lista.setVisibleRowCount(5);

        lista.setPreferredSize(
                new Dimension(200, 140)
        );

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panel.setBackground(
                new Color(
                        250,
                        251,
                        253
                )
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDE),
                        titulo,
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

        panel.setPreferredSize(
                new Dimension(250, 210)
        );

        panel.setMinimumSize(
                new Dimension(220, 210)
        );

        JScrollPane scroll = new JScrollPane(lista);

        scroll.setPreferredSize(
                new Dimension(220, 150)
        );

        scroll.setMinimumSize(
                new Dimension(180, 150)
        );

        scroll.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel botones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                5,
                                5
                        )
                );

        botones.setOpaque(false);

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);

        panel.add(
                botones,
                BorderLayout.SOUTH
        );

        return panel;

    }

    //==========================================================
    // CONFIGURACION GENERAL
    //==========================================================
    private JPanel crearPanelConfiguracionGeneral() {

        JPanel panel
                = new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDE),
                        "Configuración General",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                15
                        ),
                        AZUL_OSCURO
                )
        );

        GridBagConstraints c
                = new GridBagConstraints();

        c.insets
                = new Insets(
                        10,
                        10,
                        10,
                        10
                );

        c.anchor
                = GridBagConstraints.WEST;

        //------------------------------------------------------
        // IVA
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "IVA Predeterminado"
                ),
                c
        );

        c.gridx = 1;

        cboIvaPredeterminado.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        panel.add(
                cboIvaPredeterminado,
                c
        );

        //------------------------------------------------------
        // STOCK MINIMO
        //------------------------------------------------------
        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Stock mínimo predeterminado"
                ),
                c
        );

        c.gridx = 3;

        txtStockMinimo.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        panel.add(
                txtStockMinimo,
                c
        );

        //------------------------------------------------------
        // CHECK 1
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;

        panel.add(
                chkAvisarStockMinimo,
                c
        );

        //------------------------------------------------------
        // CHECK 2
        //------------------------------------------------------
        c.gridy = 2;

        panel.add(
                chkPermitirVentaSinStock,
                c
        );

        return panel;

    }

    //==========================================================
    // CONFIGURAR LISTA
    //==========================================================
    private void configurarLista(JList<String> lista) {

        lista.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        lista.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lista.setFixedCellHeight(28);

        lista.setVisibleRowCount(5);

        lista.setBackground(Color.WHITE);

        lista.setBorder(
                new EmptyBorder(3, 5, 3, 5)
        );
    }

    //==========================================================
    // CREAR LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label
                = new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(
                        60,
                        70,
                        85
                )
        );

        return label;

    }

    //==========================================================
    // CREAR BOTON
    //==========================================================
    private JButton crearBoton(
            String texto,
            Color color) {

        JButton boton
                = new JButton(texto);
        boton.setPreferredSize(
                new Dimension(72, 32)
        );

        boton.setFocusPainted(false);

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );
        return boton;

    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        modeloCategorias.clear();

        modeloCategorias.addElement("Bebidas");
        modeloCategorias.addElement("Almacén");
        modeloCategorias.addElement("Limpieza");
        modeloCategorias.addElement("Perfumería");

        modeloMarcas.clear();

        modeloMarcas.addElement("Coca Cola");
        modeloMarcas.addElement("Natura");
        modeloMarcas.addElement("Playadito");
        modeloMarcas.addElement("Ledesma");

        modeloUnidades.clear();

        modeloUnidades.addElement("Unidad");
        modeloUnidades.addElement("Kg");
        modeloUnidades.addElement("Litro");
        modeloUnidades.addElement("Caja");
        modeloUnidades.addElement("Pack");

        cboIvaPredeterminado.setSelectedItem("21%");

        txtStockMinimo.setText("10");

        chkAvisarStockMinimo.setSelected(true);

        chkPermitirVentaSinStock.setSelected(false);

    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        btnNuevaCategoria.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Nueva categoría.",
                    "Categorías",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEditarCategoria.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Editar categoría.",
                    "Categorías",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEliminarCategoria.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Eliminar categoría.",
                    "Categorías",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        //------------------------------------------------------
        // MARCAS
        //------------------------------------------------------
        btnNuevaMarca.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Nueva marca.",
                    "Marcas",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEditarMarca.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Editar marca.",
                    "Marcas",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEliminarMarca.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Eliminar marca.",
                    "Marcas",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        //------------------------------------------------------
        // UNIDADES
        //------------------------------------------------------
        btnNuevaUnidad.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Nueva unidad de medida.",
                    "Unidades",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEditarUnidad.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Editar unidad.",
                    "Unidades",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        btnEliminarUnidad.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Eliminar unidad.",
                    "Unidades",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

        //------------------------------------------------------
        // GUARDAR
        //------------------------------------------------------
        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Configuración de productos guardada.",
                    "Productos",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

        });

    }

}
