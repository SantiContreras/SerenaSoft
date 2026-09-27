package Dialogos;

import Diseños.EstiloBotones;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rojeru_san.rsbutton.RSButtonRound;

public class DialogoEntradaStock extends DialogoBase {

    private TextFieldRedondeado txtCodigo;
    private TextFieldRedondeado txtProducto;
    private TextFieldRedondeado txtCantidad;
    private TextFieldRedondeado txtPrecioCompra;
    private TextFieldRedondeado txtProveedor;

    private RSButtonRound btnGuardar;
    private RSButtonRound btnCancelar;

    public DialogoEntradaStock(Frame owner) {

        super(owner, "Entrada de Stock");

        construirFormulario();
         EstiloBotones.corregirBotones(
                getContentPane()
    );

    }

    private void construirFormulario() {

        JPanel formulario = new JPanel();

        formulario.setOpaque(false);

        formulario.setLayout(new GridLayout(5,2,15,15));

        txtCodigo = new TextFieldRedondeado();
        txtProducto = new TextFieldRedondeado();
        txtCantidad = new TextFieldRedondeado();
        txtPrecioCompra = new TextFieldRedondeado();
        txtProveedor = new TextFieldRedondeado();

        formulario.add(crearLabel("Código"));
        formulario.add(txtCodigo);

        formulario.add(crearLabel("Producto"));
        formulario.add(txtProducto);

        formulario.add(crearLabel("Cantidad"));
        formulario.add(txtCantidad);

        formulario.add(crearLabel("Precio Compra"));
        formulario.add(txtPrecioCompra);

        formulario.add(crearLabel("Proveedor"));
        formulario.add(txtProveedor);

        getContenido().setLayout(new BorderLayout());

        getContenido().add(formulario,BorderLayout.NORTH);

        //------------------------------------------------
        // BOTONES
        //------------------------------------------------

        btnCancelar = new RSButtonRound();
        btnGuardar = new RSButtonRound();

        btnCancelar.setText("Cancelar");
        btnGuardar.setText("Guardar");

        btnCancelar.setBackground(new Color(180,40,40));
        btnGuardar.setBackground(new Color(30,140,70));
        btnCancelar.addActionListener(e -> dispose());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getBotones().add(btnCancelar);
        getBotones().add(btnGuardar);

    }

    private JLabel crearLabel(String texto){

        JLabel lbl = new JLabel(texto);

        lbl.setFont(new Font("Segoe UI",Font.BOLD,14));

        return lbl;

    }

}
