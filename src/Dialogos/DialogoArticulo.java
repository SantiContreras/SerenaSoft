/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dialogos;

import Diseños.EstiloBotones;
import Diseños.PanelRedondeado;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import rojeru_san.rsbutton.RSButtonRound;
import rojerusan.RSComboBox;

/**
 *
 * @author santi
 */
public class DialogoArticulo extends JDialog {
//====================================================
// PANELES
//====================================================

    private PanelRedondeado panelDatos;
    private PanelRedondeado panelPrecios;
    private PanelRedondeado panelOpciones;
    private PanelRedondeado panelObservaciones;
    private PanelRedondeado panelBotones;
    private PanelRedondeado panelStock;

    private TextFieldRedondeado txtCodigoInterno;
    private TextFieldRedondeado txtUbicacion;

    private RSComboBox cmbRubro;
    private RSComboBox cmbUnidadCompra;
    private RSComboBox cmbUnidadVenta;

//====================================================
// LABELS
//====================================================
//====================================================
// DATOS
//====================================================
    private TextFieldRedondeado txtCodigo;
    private TextFieldRedondeado txtCodigoBarra;
    private TextFieldRedondeado txtDescripcion;

    private RSComboBox cmbMarca;
    private RSComboBox cmbCategoria;
    private RSComboBox cmbProveedor;

//====================================================
// PRECIOS
//====================================================
    private TextFieldRedondeado txtPrecioCompra;
    private TextFieldRedondeado txtPrecioVenta;
    private TextFieldRedondeado txtGanancia;

    private RSComboBox cmbIVA;

//====================================================
// STOCK
//====================================================
    private TextFieldRedondeado txtStock;
    private TextFieldRedondeado txtStockMinimo;

    private RSComboBox cmbUnidad;

//====================================================
// OPCIONES
//====================================================
    private JCheckBox chkActivo;
    private JCheckBox chkPesable;
    private JCheckBox chkControlStock;
    private JCheckBox chkPermiteDescuento;

//====================================================
// OBSERVACIONES
//====================================================
    private JTextArea txtObservacion;
    private JScrollPane scrollObservacion;

//====================================================
// BOTONES
//====================================================
    private RSButtonRound btnGuardar;
    private RSButtonRound btnCancelar;
    private RSButtonRound btnLimpiar;

    public DialogoArticulo(java.awt.Frame parent, boolean modal) {

        super(parent, modal);

        initComponents();

        configurarVentana();

        inicializarComponentes();

        armarLayout();

        armarPanelDatos();

        armarPanelStock();

        armarPanelPrecios();

        armarPanelOpciones();

        armarPanelObservaciones();

        armarPanelBotones();
        
         EstiloBotones.corregirBotones(
                getContentPane()
    );

    }

    private void inicializarComponentes() {

        //------------------------
        // Paneles
        //------------------------
        panelDatos = new PanelRedondeado();
        panelPrecios = new PanelRedondeado();
        panelOpciones = new PanelRedondeado();
        panelObservaciones = new PanelRedondeado();
        panelBotones = new PanelRedondeado();
        panelStock = new PanelRedondeado();
        txtCodigoInterno = new TextFieldRedondeado();
        txtUbicacion = new TextFieldRedondeado();

        cmbRubro = new RSComboBox();
        cmbUnidadCompra = new RSComboBox();
        cmbUnidadVenta = new RSComboBox();

        //------------------------
        // TextField
        //------------------------
        txtCodigo = new TextFieldRedondeado();
        txtCodigoBarra = new TextFieldRedondeado();
        txtDescripcion = new TextFieldRedondeado();

        txtPrecioCompra = new TextFieldRedondeado();
        txtPrecioVenta = new TextFieldRedondeado();
        txtGanancia = new TextFieldRedondeado();

        txtStock = new TextFieldRedondeado();
        txtStockMinimo = new TextFieldRedondeado();

        //------------------------
        // Combos
        //------------------------
        cmbMarca = new RSComboBox();
        cmbCategoria = new RSComboBox();
        cmbProveedor = new RSComboBox();

        cmbIVA = new RSComboBox();
        cmbUnidad = new RSComboBox();

        //------------------------
        // Check
        //------------------------
        chkActivo = new JCheckBox("Producto Activo");
        chkPesable = new JCheckBox("Producto Pesable");
        chkControlStock = new JCheckBox("Controlar Stock");
        chkPermiteDescuento = new JCheckBox("Permite Descuento");

        //------------------------
        // Observaciones
        //------------------------
        txtObservacion = new JTextArea();
        scrollObservacion = new JScrollPane(txtObservacion);

        //------------------------
        // Botones
        //------------------------
        btnGuardar = new RSButtonRound();
        btnCancelar = new RSButtonRound();
        btnLimpiar = new RSButtonRound();

    }

    private void configurarVentana() {

        setTitle("Nuevo Artículo");

        setPreferredSize(new Dimension(980, 730));
        pack();
        setLocationRelativeTo(getParent());

        setResizable(false);
       
        getContentPane().setBackground(new Color(245, 247, 250));
    }

    private void initComponents() {

    }

    private void armarLayout() {

        getContentPane().removeAll();

        getContentPane().setLayout(new BorderLayout(10, 10));

        panelDatos.setPreferredSize(new Dimension(0, 260));

        panelOpciones.setPreferredSize(new Dimension(0, 90));

        panelObservaciones.setPreferredSize(new Dimension(0, 120));

        panelBotones.setPreferredSize(new Dimension(0, 65));

        //--------------------------
        // Panel central Stock + Precio
        //--------------------------
        JPanel filaCentral = new JPanel();

        filaCentral.setOpaque(false);

        filaCentral.setLayout(new BorderLayout(10, 0));

        panelStock.setPreferredSize(new Dimension(430, 95));

        filaCentral.add(panelStock, BorderLayout.WEST);

        filaCentral.add(panelPrecios, BorderLayout.CENTER);

        //--------------------------
        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        centro.add(panelDatos);

        centro.add(Box.createVerticalStrut(8));

        centro.add(filaCentral);

        centro.add(Box.createVerticalStrut(8));

        centro.add(panelOpciones);

        centro.add(Box.createVerticalStrut(8));

        centro.add(panelObservaciones);

        add(centro, BorderLayout.CENTER);

        add(panelBotones, BorderLayout.SOUTH);

    }

    private void armarPanelDatos() {

        panelDatos.removeAll();
        panelDatos.setLayout(null);
        panelDatos.setBackground(Color.WHITE);

        Font titulo = new Font("Segoe UI", Font.BOLD, 14);
        Font texto = new Font("Segoe UI", Font.PLAIN, 12);

        //-------------------------
        JLabel lblTitulo = new JLabel("DATOS GENERALES");
        lblTitulo.setFont(titulo);
        lblTitulo.setForeground(new Color(20, 55, 120));
        lblTitulo.setBounds(20, 10, 250, 22);

        panelDatos.add(lblTitulo);

        //----------------------------------------------------------
        // FILA 1
        //----------------------------------------------------------
        JLabel lblCodigo = new JLabel("Código");
        lblCodigo.setFont(texto);
        lblCodigo.setBounds(20, 40, 80, 18);

        txtCodigo.setBounds(20, 58, 150, 28);

        JLabel lblBarra = new JLabel("Código Barra");
        lblBarra.setFont(texto);
        lblBarra.setBounds(190, 40, 100, 18);

        txtCodigoBarra.setBounds(190, 58, 250, 28);

        JLabel lblInterno = new JLabel("Código Interno");
        lblInterno.setFont(texto);
        lblInterno.setBounds(460, 40, 110, 18);

        txtCodigoInterno.setBounds(460, 58, 150, 28);

        //----------------------------------------------------------
        // FILA 2
        //----------------------------------------------------------
        JLabel lblDescripcion = new JLabel("Descripción");
        lblDescripcion.setFont(texto);
        lblDescripcion.setBounds(20, 95, 100, 18);

        txtDescripcion.setBounds(20, 113, 840, 28);

        //----------------------------------------------------------
        // FILA 3
        //----------------------------------------------------------
        JLabel lblMarca = new JLabel("Marca");
        lblMarca.setFont(texto);
        lblMarca.setBounds(20, 150, 80, 18);

        cmbMarca.setBounds(20, 168, 190, 28);

        JLabel lblCategoria = new JLabel("Categoría");
        lblCategoria.setFont(texto);
        lblCategoria.setBounds(230, 150, 90, 18);

        cmbCategoria.setBounds(230, 168, 190, 28);

        JLabel lblProveedor = new JLabel("Proveedor");
        lblProveedor.setFont(texto);
        lblProveedor.setBounds(440, 150, 90, 18);

        cmbProveedor.setBounds(440, 168, 220, 28);

        //----------------------------------------------------------
        // FILA 4
        //----------------------------------------------------------
        JLabel lblRubro = new JLabel("Rubro");
        lblRubro.setFont(texto);
        lblRubro.setBounds(20, 205, 80, 18);

        cmbRubro.setBounds(20, 223, 150, 28);

        JLabel lblUnidadCompra = new JLabel("Unidad Compra");
        lblUnidadCompra.setFont(texto);
        lblUnidadCompra.setBounds(190, 205, 110, 18);

        cmbUnidadCompra.setBounds(190, 223, 170, 28);

        JLabel lblUnidadVenta = new JLabel("Unidad Venta");
        lblUnidadVenta.setFont(texto);
        lblUnidadVenta.setBounds(380, 205, 110, 18);

        cmbUnidadVenta.setBounds(380, 223, 170, 28);

        JLabel lblUbicacion = new JLabel("Ubicación");
        lblUbicacion.setFont(texto);
        lblUbicacion.setBounds(570, 205, 90, 18);

        txtUbicacion.setBounds(570, 223, 170, 28);

        //----------------------------------------------------------
        panelDatos.add(lblCodigo);
        panelDatos.add(txtCodigo);

        panelDatos.add(lblBarra);
        panelDatos.add(txtCodigoBarra);

        panelDatos.add(lblInterno);
        panelDatos.add(txtCodigoInterno);

        panelDatos.add(lblDescripcion);
        panelDatos.add(txtDescripcion);

        panelDatos.add(lblMarca);
        panelDatos.add(cmbMarca);

        panelDatos.add(lblCategoria);
        panelDatos.add(cmbCategoria);

        panelDatos.add(lblProveedor);
        panelDatos.add(cmbProveedor);

        panelDatos.add(lblRubro);
        panelDatos.add(cmbRubro);

        panelDatos.add(lblUnidadCompra);
        panelDatos.add(cmbUnidadCompra);

        panelDatos.add(lblUnidadVenta);
        panelDatos.add(cmbUnidadVenta);

        panelDatos.add(lblUbicacion);
        panelDatos.add(txtUbicacion);

    }

    private void armarPanelPrecios() {

        panelPrecios.removeAll();

        panelPrecios.setLayout(null);

        panelPrecios.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("PRECIOS");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        titulo.setForeground(new Color(20, 55, 120));

        titulo.setBounds(20, 10, 200, 25);

        panelPrecios.add(titulo);

        //----------------------
        JLabel lblCompra = new JLabel("Compra");

        lblCompra.setBounds(20, 42, 100, 20);

        txtPrecioCompra.setBounds(20, 62, 120, 34);

        //----------------------
        JLabel lblVenta = new JLabel("Venta");

        lblVenta.setBounds(160, 42, 100, 20);

        txtPrecioVenta.setBounds(160, 62, 120, 34);

        //----------------------
        JLabel lblGanancia = new JLabel("%");

        lblGanancia.setBounds(300, 42, 50, 20);

        txtGanancia.setBounds(300, 62, 80, 34);

        //----------------------
        JLabel lblIva = new JLabel("IVA");

        lblIva.setBounds(400, 42, 50, 20);

        cmbIVA.setBounds(400, 62, 120, 34);

        panelPrecios.add(lblCompra);

        panelPrecios.add(txtPrecioCompra);

        panelPrecios.add(lblVenta);

        panelPrecios.add(txtPrecioVenta);

        panelPrecios.add(lblGanancia);

        panelPrecios.add(txtGanancia);

        panelPrecios.add(lblIva);

        panelPrecios.add(cmbIVA);

    }

    private void armarPanelOpciones() {

        panelOpciones.removeAll();
        panelOpciones.setLayout(null);
        panelOpciones.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("OPCIONES");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(20, 55, 120));
        titulo.setBounds(20, 10, 200, 30);

        panelOpciones.add(titulo);

        titulo.setBounds(20, 10, 220, 22);

        chkActivo.setBounds(30, 45, 160, 25);

        chkPesable.setBounds(220, 45, 180, 25);

        chkControlStock.setBounds(430, 45, 180, 25);

        chkPermiteDescuento.setBounds(650, 45, 180, 25);

        panelOpciones.add(chkActivo);
        panelOpciones.add(chkPesable);
        panelOpciones.add(chkControlStock);
        panelOpciones.add(chkPermiteDescuento);

    }

    private void armarPanelObservaciones() {

        panelObservaciones.removeAll();

        panelObservaciones.setLayout(null);

        panelObservaciones.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("OBSERVACIONES");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        titulo.setForeground(new Color(20, 55, 120));

        titulo.setBounds(20, 10, 220, 25);

        txtObservacion.setLineWrap(true);

        txtObservacion.setWrapStyleWord(true);

        txtObservacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        scrollObservacion.setBorder(BorderFactory.createEmptyBorder());

        scrollObservacion.setBounds(20, 40, 840, 65);

        panelObservaciones.add(titulo);

        panelObservaciones.add(scrollObservacion);

    }

    private void armarPanelBotones() {

        panelBotones.removeAll();

        panelBotones.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.RIGHT,
                12,
                15));

        panelBotones.setBackground(Color.WHITE);

        btnGuardar.setText("Guardar");
        btnCancelar.setText("Cancelar");
        btnLimpiar.setText("Limpiar");

        btnGuardar.setPreferredSize(new Dimension(130, 40));
        btnCancelar.setPreferredSize(new Dimension(130, 40));
        btnLimpiar.setPreferredSize(new Dimension(130, 40));

        btnGuardar.setBackground(new Color(0, 140, 70));

        btnCancelar.setBackground(new Color(220, 55, 55));

        btnLimpiar.setBackground(new Color(40, 120, 220));

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

    }

    private void armarPanelStock() {

        panelStock.removeAll();

        panelStock.setLayout(null);

        panelStock.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("STOCK");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        titulo.setForeground(new Color(20, 55, 120));

        titulo.setBounds(20, 10, 200, 25);

        panelStock.add(titulo);

        //----------------------
        JLabel lblStock = new JLabel("Stock");

        lblStock.setBounds(20, 42, 100, 20);

        txtStock.setBounds(20, 62, 120, 34);

        //----------------------
        JLabel lblMinimo = new JLabel("Mínimo");

        lblMinimo.setBounds(160, 42, 100, 20);

        txtStockMinimo.setBounds(160, 62, 120, 34);

        //----------------------
        JLabel lblUnidad = new JLabel("Unidad");

        lblUnidad.setBounds(300, 42, 100, 20);

        cmbUnidad.setBounds(300, 62, 100, 34);

        panelStock.add(lblStock);

        panelStock.add(txtStock);

        panelStock.add(lblMinimo);

        panelStock.add(txtStockMinimo);

        panelStock.add(lblUnidad);

        panelStock.add(cmbUnidad);

    }

}
