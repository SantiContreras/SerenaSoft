package Dialogos;

import Diseños.EstiloBotones;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rojeru_san.rsbutton.RSButtonRound;

public class DialogoDarDeBajaProducto extends DialogoBase {

    //=========================================================
    // BUSQUEDA
    //=========================================================
    private JTextField txtBuscarProducto;

    //=========================================================
    // DATOS DEL PRODUCTO
    //=========================================================
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblMarca;
    private JLabel lblCategoria;
    private JLabel lblStock;
    private JLabel lblEstado, lblPrecio;

    //=========================================================
    // MOTIVO
    //=========================================================
    private JComboBox<String> cboMotivo;

    private JList<String> listaProductos;

    private DefaultListModel<String> modeloListaProductos;
    private JList<String> listaResultados;
    private JScrollPane scrollResultados;

    private JScrollPane scrollProductos;

// productos del sistema
    private java.util.List<String> productos;
    //=========================================================
    // OBSERVACIONES
    //=========================================================
    private JTextArea txtObservaciones;

    //=========================================================
    // CONFIRMACION
    //=========================================================
    private JCheckBox chkConfirmar;

    //=========================================================
    // BOTONES
    //=========================================================
    private RSButtonRound btnCancelar;
    private RSButtonRound btnDarBaja;

    //=========================================================
    // LISTA PRODUCTOS
    //=========================================================
    //=====================================================
// MOTIVO
//=====================================================
//=====================================================
// LISTA DE PRODUCTOS (para el buscador inteligente)
//=====================================================
    //=========================================================
    // CONSTRUCTOR
    //=========================================================
    public DialogoDarDeBajaProducto(Frame owner) {

        super(owner, "DAR DE BAJA PRODUCTO");

        construirFormulario();

        cargarProductos();

        configurarEventos();
        actualizarProductos(""); // <-- carga todos los productos al iniciar
        EstiloBotones.corregirBotones(
                getContentPane()
        );

    }

    //=========================================================
    // COMPONENTES
    //=========================================================
    //=========================================================
// COMPONENTES
//=========================================================
    private void inicializarComponentes() {

        //-----------------------------------------
        // BUSCADOR
        //-----------------------------------------
        txtBuscarProducto = new JTextField(30);

        txtBuscarProducto = new JTextField(35);

        modeloListaProductos = new DefaultListModel<>();

        listaProductos = new JList<>(modeloListaProductos);

        listaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        listaProductos.setVisibleRowCount(6);

        listaProductos.setFont(
                new Font("Segoe UI", Font.PLAIN, 14));

        scrollProductos = new JScrollPane(listaProductos);

        scrollProductos.setPreferredSize(
                new Dimension(500, 140));

        productos = new ArrayList<>();

        listaResultados = new JList<>(modeloListaProductos);

        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        listaResultados.setFixedCellHeight(28);

        scrollResultados = new JScrollPane(listaResultados);

        scrollResultados.setPreferredSize(new Dimension(520, 150));

        //-----------------------------------------
        // INFORMACIÓN DEL PRODUCTO
        //-----------------------------------------
        lblCodigo = new JLabel("A0001");
        lblNombre = new JLabel("Coca Cola 2.25L");
        lblMarca = new JLabel("Coca Cola");
        lblCategoria = new JLabel("Bebidas");
        lblPrecio = new JLabel("$ 1450");

        lblStock = new JLabel("120 unidades");
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblStock.setForeground(new Color(25, 135, 84));

        lblEstado = new JLabel("ACTIVO");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstado.setForeground(new Color(25, 135, 84));

        //-----------------------------------------
        // MOTIVO
        //-----------------------------------------
        cboMotivo = new JComboBox<>();

        cboMotivo.addItem("Producto discontinuado");
        cboMotivo.addItem("Producto vencido");
        cboMotivo.addItem("Producto defectuoso");
        cboMotivo.addItem("Error de carga");
        cboMotivo.addItem("Cambio de proveedor");
        cboMotivo.addItem("Otro");

        cboMotivo.setPreferredSize(new Dimension(300, 32));

        //-----------------------------------------
        // OBSERVACIONES
        //-----------------------------------------
        txtObservaciones = new JTextArea(5, 40);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        //-----------------------------------------
        // CONFIRMACIÓN
        //-----------------------------------------
        chkConfirmar = new JCheckBox(
                "Confirmo que deseo dar de baja este producto.");

        chkConfirmar.setFont(
                new Font("Segoe UI", Font.BOLD, 14));

        //-----------------------------------------
        // BOTONES
        //-----------------------------------------
        btnCancelar = new RSButtonRound();
        btnCancelar.setText("Cancelar");

        btnDarBaja = new RSButtonRound();
        btnDarBaja.setText("Dar de Baja Producto");

        btnDarBaja.setEnabled(false);

    }

    private void construirFormulario() {

        inicializarComponentes();

        JPanel contenido = getContenido();

        contenido.setLayout(new BorderLayout(15, 15));

        //------------------------------------------------------
        // PANEL CENTRAL
        //------------------------------------------------------
        JPanel centro = new JPanel();

        centro.setLayout(new BoxLayout(
                centro,
                BoxLayout.Y_AXIS));

        centro.setOpaque(false);

        //------------------------------------------------------
        // Scroll
        //------------------------------------------------------
        JScrollPane scroll = new JScrollPane(centro);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        contenido.add(scroll, BorderLayout.CENTER);

        //------------------------------------------------------
        centro.add(crearPanelBusqueda());

        centro.add(Box.createVerticalStrut(15));

        centro.add(Box.createVerticalStrut(10));

        centro.add(chkConfirmar);

        centro.add(crearPanelInformacion());

        centro.add(crearPanelMotivo());

        centro.add(chkConfirmar);

        //------------------------------------------------------
        JPanel botones = getBotones();

        aplicarEstiloBoton(
                btnCancelar,
                new Color(120, 120, 120));

        aplicarEstiloBoton(
                btnDarBaja,
                new Color(183, 28, 28));

        botones.add(btnCancelar);

        botones.add(btnDarBaja);

    }

    private void cargarProductos() {

        productos.clear();

        productos.add("Coca Cola 2.25L");
        productos.add("Fanta 2.25L");
        productos.add("Sprite 2.25L");
        productos.add("Pepsi 2.25L");
        productos.add("Manaos Cola");
        productos.add("Agua Villa del Sur");
        productos.add("Aceite Natura");
        productos.add("Yerba Playadito");
        productos.add("Harina Favorita");
        productos.add("Azúcar Ledesma");

    }

    private void actualizarProductos(String filtro) {

        modeloListaProductos.clear();

        for (String producto : productos) {

            if (filtro == null
                    || filtro.trim().isEmpty()
                    || producto.toLowerCase().contains(filtro.toLowerCase())) {

                modeloListaProductos.addElement(producto);

            }

        }

    }

    private void actualizarDatosProducto(String producto) {

        lblNombre.setText(producto);

        switch (producto) {

            case "Coca Cola 2.25L":
                lblCodigo.setText("A0001");
                lblMarca.setText("Coca Cola");
                lblCategoria.setText("Bebidas");
                lblStock.setText("120 unidades");
                lblPrecio.setText("$1450");
                break;

            case "Pepsi 2.25L":
                lblCodigo.setText("A0002");
                lblMarca.setText("Pepsi");
                lblCategoria.setText("Bebidas");
                lblStock.setText("85 unidades");
                lblPrecio.setText("$1380");
                break;

            case "Manaos Cola":
                lblCodigo.setText("A0003");
                lblMarca.setText("Manaos");
                lblCategoria.setText("Bebidas");
                lblStock.setText("60 unidades");
                lblPrecio.setText("$980");
                break;

            case "Agua Villa del Sur":
                lblCodigo.setText("A0004");
                lblMarca.setText("Villa del Sur");
                lblCategoria.setText("Aguas");
                lblStock.setText("210 unidades");
                lblPrecio.setText("$950");
                break;

            case "Aceite Natura":
                lblCodigo.setText("A0005");
                lblMarca.setText("Natura");
                lblCategoria.setText("Almacén");
                lblStock.setText("18 unidades");
                lblPrecio.setText("$3200");
                break;

            case "Yerba Playadito":
                lblCodigo.setText("A0006");
                lblMarca.setText("Playadito");
                lblCategoria.setText("Almacén");
                lblStock.setText("32 unidades");
                lblPrecio.setText("$4800");
                break;

            case "Harina Favorita":
                lblCodigo.setText("A0007");
                lblMarca.setText("Favorita");
                lblCategoria.setText("Almacén");
                lblStock.setText("90 unidades");
                lblPrecio.setText("$1300");
                break;

            case "Azúcar Ledesma":
                lblCodigo.setText("A0008");
                lblMarca.setText("Ledesma");
                lblCategoria.setText("Almacén");
                lblStock.setText("45 unidades");
                lblPrecio.setText("$1450");
                break;
        }

    }

    private void configurarEventos() {

        txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarProductos(txtBuscarProducto.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarProductos(txtBuscarProducto.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

        }
        );

        listaProductos.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                String seleccionado = listaProductos.getSelectedValue();

                if (seleccionado != null) {

                    txtBuscarProducto.setText(seleccionado);

                    actualizarDatosProducto(seleccionado);

                }

            }

        });

    }

    private JPanel crearPanelBusqueda() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Buscar Producto",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(8, 8, 8, 8);

        c.fill = GridBagConstraints.HORIZONTAL;

        //--------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;

        panel.add(new JLabel("Producto o Código"), c);

        //--------------------------------------------------
        c.gridx = 0;
        c.gridy = 1;

        txtBuscarProducto.setPreferredSize(
                new Dimension(420, 32));

        panel.add(txtBuscarProducto, c);

        //--------------------------------------------------
        c.gridy = 2;

        panel.add(scrollProductos, c);

        return panel;

    }

    private void aplicarEstiloBoton(RSButtonRound boton,
            Color color) {

        boton.setBackground(color);

        boton.setColorHover(color.darker());

        boton.setColorText(Color.WHITE);

        boton.setColorTextHover(Color.WHITE);

        boton.setPreferredSize(new Dimension(180, 42));

        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));

    }

    private JPanel crearPanelInformacion() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Información del Producto",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(10, 12, 10, 12);
        c.anchor = GridBagConstraints.WEST;

        //------------------------------------------
        // FILA 1
        //------------------------------------------
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Código"), c);

        c.gridx = 1;
        lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(lblCodigo, c);

        c.gridx = 2;
        panel.add(new JLabel("Estado"), c);

        c.gridx = 3;
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(lblEstado, c);

        //------------------------------------------
        // FILA 2
        //------------------------------------------
        c.gridx = 0;
        c.gridy++;

        panel.add(new JLabel("Producto"), c);

        c.gridx = 1;
        c.gridwidth = 3;

        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));

        panel.add(lblNombre, c);

        c.gridwidth = 1;

        //------------------------------------------
        // FILA 3
        //------------------------------------------
        c.gridx = 0;
        c.gridy++;

        panel.add(new JLabel("Marca"), c);

        c.gridx = 1;
        panel.add(lblMarca, c);

        c.gridx = 2;
        panel.add(new JLabel("Categoría"), c);

        c.gridx = 3;
        panel.add(lblCategoria, c);

        //------------------------------------------
        // FILA 4
        //------------------------------------------
        c.gridx = 0;
        c.gridy++;

        panel.add(new JLabel("Stock"), c);

        c.gridx = 1;
        panel.add(lblStock, c);

        c.gridx = 2;
        panel.add(new JLabel("Precio Venta"), c);

        c.gridx = 3;
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblPrecio, c);

        return panel;

    }

    private JPanel crearPanelMotivo() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                "Dar de Baja",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 15),
                new Color(20, 55, 120)));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        //------------------------------------------------------
        // ADVERTENCIA
        //------------------------------------------------------
        JPanel panelAdvertencia = new JPanel(new BorderLayout());

        panelAdvertencia.setBackground(new Color(255, 248, 225));

        panelAdvertencia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel lblAdvertencia = new JLabel(
                "<html>"
                + "<b>⚠ Atención</b><br><br>"
                + "Dar de baja un producto NO lo elimina del sistema.<br>"
                + "• No podrá utilizarse en nuevas ventas.<br>"
                + "• No podrá recibir movimientos de stock.<br>"
                + "• Permanecerá disponible en reportes e historial."
                + "</html>");

        lblAdvertencia.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelAdvertencia.add(lblAdvertencia);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        panel.add(panelAdvertencia, c);

        //------------------------------------------------------
        // MOTIVO
        //------------------------------------------------------
        c.gridy++;
        c.gridwidth = 1;

        panel.add(new JLabel("Motivo"), c);

        c.gridx = 1;

        cboMotivo.setPreferredSize(new Dimension(260, 32));

        panel.add(cboMotivo, c);

        //------------------------------------------------------
        // OBSERVACIONES
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy++;

        panel.add(new JLabel("Observaciones"), c);

        c.gridx = 1;

        JScrollPane scroll = new JScrollPane(txtObservaciones);

        scroll.setPreferredSize(new Dimension(350, 110));

        panel.add(scroll, c);

        return panel;

    }

}
