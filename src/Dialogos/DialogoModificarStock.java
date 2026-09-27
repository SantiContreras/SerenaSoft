package Dialogos;

import Diseños.EstiloBotones;
import static com.sun.java.accessibility.util.SwingEventMonitor.addDocumentListener;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rojeru_san.rsbutton.RSButtonRound;

public class DialogoModificarStock extends DialogoBase {

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtBuscarProducto;

    private JList<String> listaProductos;

    private DefaultListModel<String> modeloProductos;

    private JScrollPane scrollProductos;

    private JComboBox<String> cboCategoria;
    private JComboBox<String> cboUnidad;

    private JTextField txtNombre;
    private JTextField txtMarca;
    private JTextField txtStockMinimo;
    private JTextField txtStockActual;
    private JTextField txtCompra;
    private JTextField txtVenta;

    private JTextArea txtObservaciones;

    private JLabel lblCodigo;
    private JLabel lblEstado;
    private JLabel lblGanancia;

    private RSButtonRound btnGuardar;
    private RSButtonRound btnCancelar;

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public DialogoModificarStock(Frame owner) {

        super(owner, "MODIFICAR PRODUCTO");

        construirFormulario();

        cargarProductos();

        actualizarDatosProducto();
         EstiloBotones.corregirBotones(
                getContentPane()
    );
        configurarEventos();

    }

    private void cargarProductos() {

        modeloProductos.clear();

        modeloProductos.addElement("A0001 - Coca Cola 2.25L");
        modeloProductos.addElement("A0002 - Fanta 2.25L");
        modeloProductos.addElement("A0003 - Sprite 2.25L");
        modeloProductos.addElement("A0004 - Yerba Playadito");
        modeloProductos.addElement("A0005 - Azúcar Ledesma");
        modeloProductos.addElement("A0006 - Harina Favorita");
        modeloProductos.addElement("A0007 - Aceite Natura");

    }

    private void actualizarDatosProducto() {

        lblCodigo.setText("Código : A0001");

        txtMarca.setText("Coca Cola");

        cboCategoria.setSelectedItem("Bebidas");
        txtObservaciones.setText(
                "Gaseosa Coca Cola retornable de 2.25 litros."
        );

        txtCompra.setText("980");

        txtVenta.setText("1450");

        txtStockMinimo.setText("15");

    }

   private void configurarEventos() {

    listaProductos.addListSelectionListener(e -> {

        if(!e.getValueIsAdjusting()){

            actualizarDatosProducto();

        }

    });

    txtBuscarProducto.getDocument().addDocumentListener(
            new DocumentListener(){

        @Override
        public void insertUpdate(DocumentEvent e){

            actualizarLista(txtBuscarProducto.getText());

        }

        @Override
        public void removeUpdate(DocumentEvent e){

            actualizarLista(txtBuscarProducto.getText());

        }

        @Override
        public void changedUpdate(DocumentEvent e){}

    });

    btnCancelar.addActionListener(e -> dispose());

    btnGuardar.addActionListener(e -> {

        JOptionPane.showMessageDialog(
                DialogoModificarStock.this,
                "Producto actualizado correctamente.");

        dispose();

    });

}

    private void aplicarEstiloBoton(RSButtonRound boton,
            Color color) {

        boton.setBackground(color);

        boton.setColorHover(color.darker());

        boton.setColorText(Color.WHITE);

        boton.setColorTextHover(Color.WHITE);

        boton.setPreferredSize(new Dimension(180, 42));

        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        //------------------------------------------------------
        // Combos
        //------------------------------------------------------
        cboCategoria = new JComboBox<>();
        cboUnidad = new JComboBox<>();

        //------------------------------------------------------
        // TextFields
        //------------------------------------------------------
        txtNombre = new JTextField(25);
        txtMarca = new JTextField(15);
        txtStockMinimo = new JTextField(8);

        txtStockActual = new JTextField(8);
        txtStockActual.setEditable(false);
        txtStockActual.setBackground(new Color(245, 245, 245));

        txtCompra = new JTextField(10);
        txtVenta = new JTextField(10);

        //------------------------------------------------------
        // Observaciones
        //------------------------------------------------------
        txtObservaciones = new JTextArea(4, 30);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        //------------------------------------------------------
        // Labels
        //------------------------------------------------------
        lblCodigo = new JLabel("A0001");

        lblEstado = new JLabel("Disponible");
        lblEstado.setForeground(new Color(0, 120, 0));

        lblGanancia = new JLabel("0 %");
        lblGanancia.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGanancia.setForeground(new Color(25, 118, 210));

        //------------------------------------------------------
        // Botones
        //------------------------------------------------------
        btnGuardar = new RSButtonRound();
        btnGuardar.setText("Guardar Cambios");
        btnGuardar.setBackground(new Color(40, 167, 69));

        btnCancelar = new RSButtonRound();
        btnCancelar.setText("Cancelar");
        btnCancelar.setBackground(new Color(120, 120, 120));

        //------------------------------------------------------
        // Datos de prueba
        //------------------------------------------------------
        cboCategoria.addItem("Bebidas");
        cboCategoria.addItem("Alimentos");
        cboCategoria.addItem("Limpieza");

        cboUnidad.addItem("Unidad");
        cboUnidad.addItem("Kg");
        cboUnidad.addItem("Litro");

        txtBuscarProducto = new JTextField(30);

        modeloProductos = new DefaultListModel<>();

        listaProductos = new JList<>(modeloProductos);

        listaProductos.setVisibleRowCount(6);

        listaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        scrollProductos = new JScrollPane(listaProductos);

        scrollProductos.setPreferredSize(new Dimension(500, 120));

    }

   private void actualizarLista(String texto){

    modeloProductos.clear();

    String[] productos = {

        "A0001 - Coca Cola 2.25L",
        "A0002 - Fanta 2.25L",
        "A0003 - Sprite 2.25L",
        "A0004 - Yerba Playadito",
        "A0005 - Azúcar Ledesma",
        "A0006 - Harina Favorita",
        "A0007 - Aceite Natura"

    };

    for(String p : productos){

        if(p.toLowerCase().contains(texto.toLowerCase())){

            modeloProductos.addElement(p);

        }

    }

}

    private void construirFormulario() {

        inicializarComponentes();

        JPanel contenido = getContenido();
        contenido.setLayout(new BorderLayout());

        //-------------------------------------------------------
        // PANEL CENTRAL
        //-------------------------------------------------------
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //-------------------------------------------------------
        // PRODUCTO
        //-------------------------------------------------------
        centro.add(crearPanelProducto());
        centro.add(Box.createVerticalStrut(15));

        //-------------------------------------------------------
        // DATOS
        //-------------------------------------------------------
        centro.add(crearPanelDatos());
        centro.add(Box.createVerticalStrut(15));

        //-------------------------------------------------------
        // PRECIOS
        //-------------------------------------------------------
        centro.add(crearPanelPrecios());
        centro.add(Box.createVerticalStrut(15));

        //-------------------------------------------------------
        // STOCK
        //-------------------------------------------------------
        centro.add(crearPanelStock());
        centro.add(Box.createVerticalStrut(15));

        //-------------------------------------------------------
        // OBSERVACIONES
        //-------------------------------------------------------
        centro.add(crearPanelObservaciones());
        centro.add(Box.createVerticalGlue());

        //-------------------------------------------------------
        // SCROLL
        //-------------------------------------------------------
        JScrollPane scroll = new JScrollPane(centro);

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(8, 0));

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContenido().setLayout(new BorderLayout());

        getContenido().add(scroll, BorderLayout.CENTER);

        //-------------------------------------------------------
        // BOTONES
        //-------------------------------------------------------
        JPanel botones = getBotones();

        aplicarEstiloBoton(btnCancelar, new Color(120, 120, 120));
        aplicarEstiloBoton(btnGuardar, new Color(25, 135, 84));

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        actualizarLista("");

    }

    private JPanel crearPanelDatos() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Datos Generales",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(10, 12, 10, 12);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension campoGrande = new Dimension(240, 34);
        Dimension campoMedio = new Dimension(170, 34);

        //=========================================================
        // FILA 1
        //=========================================================
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        panel.add(new JLabel("Código"), c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(lblCodigo, c);

        c.gridx = 2;
        c.weightx = 0;
        panel.add(new JLabel("Nombre"), c);

        c.gridx = 3;
        c.weightx = 1;
        txtNombre.setPreferredSize(campoGrande);
        panel.add(txtNombre, c);

        //=========================================================
        // FILA 2
        //=========================================================
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        panel.add(new JLabel("Marca"), c);

        c.gridx = 1;
        c.weightx = 1;
        txtMarca.setPreferredSize(campoMedio);
        panel.add(txtMarca, c);

        c.gridx = 2;
        c.weightx = 0;
        panel.add(new JLabel("Categoría"), c);

        c.gridx = 3;
        c.weightx = 1;
        cboCategoria.setPreferredSize(campoMedio);
        panel.add(cboCategoria, c);

        //=========================================================
        // FILA 3
        //=========================================================
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        panel.add(new JLabel("Unidad"), c);

        c.gridx = 1;
        c.weightx = 1;
        cboUnidad.setPreferredSize(new Dimension(130, 34));
        panel.add(cboUnidad, c);

        c.gridx = 2;
        c.weightx = 0;
        panel.add(new JLabel("Estado"), c);

        c.gridx = 3;
        c.weightx = 1;
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblEstado, c);

        return panel;
    }

    private JPanel crearPanelPrecios() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Precios",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(10, 12, 10, 12);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension campo = new Dimension(170, 34);

        //--------------------------------------------------
        // Precio Compra
        //--------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        panel.add(new JLabel("Precio Compra"), c);

        c.gridx = 1;
        c.weightx = 1;
        txtCompra.setPreferredSize(campo);
        panel.add(txtCompra, c);

        //--------------------------------------------------
        // Precio Venta
        //--------------------------------------------------
        c.gridx = 2;
        c.weightx = 0;
        panel.add(new JLabel("Precio Venta"), c);

        c.gridx = 3;
        c.weightx = 1;
        txtVenta.setPreferredSize(campo);
        panel.add(txtVenta, c);

        //--------------------------------------------------
        // Ganancia
        //--------------------------------------------------
        c.gridx = 4;
        c.weightx = 0;
        panel.add(new JLabel("Ganancia"), c);

        c.gridx = 5;
        c.weightx = 1;

        lblGanancia.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGanancia.setForeground(new Color(25, 118, 210));

        panel.add(lblGanancia, c);

        return panel;

    }

    private JPanel crearPanelStock() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Stock",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(8, 8, 8, 8);

        //----------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Stock Actual"), c);

        c.gridx = 1;
        txtStockActual.setPreferredSize(new Dimension(100, 30));
        panel.add(txtStockActual, c);

        //----------------------------------------------------
        c.gridx = 2;
        panel.add(new JLabel("Stock Mínimo"), c);

        c.gridx = 3;
        txtStockMinimo.setPreferredSize(new Dimension(100, 30));
        panel.add(txtStockMinimo, c);

        return panel;

    }

    private JPanel crearPanelObservaciones() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Observaciones",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        JScrollPane scroll = new JScrollPane(txtObservaciones);

        scroll.setPreferredSize(new Dimension(620, 90));

        panel.add(scroll, BorderLayout.CENTER);

        return panel;

    }

    private JPanel crearPanelProducto() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Buscar Producto",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        //---------------------------------------------------
        // Panel superior
        //---------------------------------------------------
        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lblBuscar = new JLabel("Producto o Código");

        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        txtBuscarProducto.setPreferredSize(new Dimension(320, 34));

        superior.add(lblBuscar);
        superior.add(txtBuscarProducto);

        panel.add(superior, BorderLayout.NORTH);

        //---------------------------------------------------
        // Lista
        //---------------------------------------------------
        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaProductos.setVisibleRowCount(6);

        listaProductos.setFixedCellHeight(30);

        listaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        scrollProductos.setPreferredSize(new Dimension(620, 150));

        panel.add(scrollProductos, BorderLayout.CENTER);

        return panel;

    }

}
