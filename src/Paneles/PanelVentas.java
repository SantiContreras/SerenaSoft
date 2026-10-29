/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles;

import Diseños.BotonRedondeado;
import Diseños.PanelRedondeado;
import Diseños.TextFieldRedondeado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import rojeru_san.rsbutton.RSButtonRound;
import rojerusan.RSComboBox;
import rojerusan.RSPopuMenu;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import Diseños.TablaEstilo;
import java.awt.Cursor;
import java.awt.FlowLayout;

/**
 *
 * @author santi
 */
public class PanelVentas extends javax.swing.JPanel {

    private final java.util.LinkedHashMap<Integer, LineaCarrito> carrito = new java.util.LinkedHashMap<>();
    private javax.swing.JTextField txtImporteRecibido;
    private javax.swing.JLabel lblCambioCalculado;
    private javax.swing.JLabel lblResumenCliente;
    private JLabel lblTotalVentaReal;
    private JLabel lblSubtotalVentaReal;
    private JLabel lblDescuentoVentaReal;
    private JLabel lblNumeroVentaActual, lblItemsActual, lblUnidadesActual;
    private java.math.BigDecimal descuentoGeneralVenta = java.math.BigDecimal.ZERO;
    private static final class LineaCarrito {
        model.Producto producto;
        java.math.BigDecimal cantidad;
        LineaCarrito(model.Producto p, java.math.BigDecimal c) { producto=p; cantidad=c; }
    }
    private JTable tabla;
    private TextFieldRedondeado txtCodigo;

    private RSComboBox comboPago;

    private RSButtonRound btnAgregar;

    private RSButtonRound btnEliminar;

    private RSButtonRound btnCobrar;

    private JLabel lblProducto;

    private RSButtonRound btnManual;

    private RSButtonRound btnPesable;

    private RSButtonRound btnScan;

    private TextFieldRedondeado txtCuit;

    private PanelRedondeado panelTotal;
    private PanelRedondeado panelPago;
    private PanelRedondeado panelAcciones;

    //=====================================================
// PANEL CLIENTE / FACTURACIÓN
//=====================================================
    private JPanel panelCliente;

    private JLabel lblCliente;
    private JLabel lblBuscar;
    private JLabel lblDocumento;
    private JLabel lblTipoCliente;
    private JLabel lblClienteTitulo;

    private TextFieldRedondeado txtCliente;
    private TextFieldRedondeado txtDocumento;

    private JButton btnBuscarCliente;

    private JRadioButton rbConsumidorFinal;
    private JRadioButton rbResponsableInscripto;
    private JRadioButton rbMonotributista;
    private JRadioButton rbMayorista;

    //condicion del cliente 
    private RSComboBox comboCondicionIVA;
    private RSComboBox comboTipoCliente;
    private RSComboBox comboComprobante;

    private ButtonGroup grupoTipoCliente;

    Color AZUL_PRINCIPAL = new Color(20, 70, 140);
    Color AZUL_HOVER = new Color(30, 90, 170);
    Color AZUL_PRESION = new Color(15, 55, 110);

    Color VERDE_OK = new Color(25, 135, 84);
    Color ROJO_ERROR = new Color(220, 53, 69);

    Color BORDE_PANEL = new Color(30, 80, 160);
    Color TITULO_PANEL = new Color(25, 55, 110);

    //titulos de la tabla
    Color HEADER = new Color(25, 70, 150);
    Color HEADER_BORDE = new Color(15, 55, 120);

    public PanelVentas() {
        initComponents();

        armarLayout();

        armarSubPanelProducto();

        armarSubPanelCliente();

        armarTabla();

        configurarEventosProductos();
        txtCodigo.addActionListener(e -> buscarCodigoEscaneado());
        btnScan.addActionListener(e -> buscarCodigoEscaneado());
        actualizarCarrito();
        configurarAtajosCaja();
        javax.swing.SwingUtilities.invokeLater(() -> txtCodigo.requestFocusInWindow());

    }

    private void alinearIzquierda(JComponent comp) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
    }

    private void configurarEventosProductos() {

        //==========================================================
        // AGREGAR PRODUCTO EXISTENTE
        //==========================================================
        btnAgregar.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoBuscarProducto dialogo
                    = new Dialogos.DialogoBuscarProducto(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isSeleccionado()) {

                model.Producto producto = dialogo.getObjetoProductoSeleccionado();
                if (producto != null) agregarAlCarrito(producto, java.math.BigDecimal.ONE);
            }
        });

        //==========================================================
        // PRODUCTO PESABLE
        //==========================================================
        btnPesable.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoProductoPesable dialogo
                    = new Dialogos.DialogoProductoPesable(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                model.Producto producto = new Dao.ProductoDao().buscarPorCodigoBarra(dialogo.getCodigoProducto());
                if (producto == null) producto = new Dao.ProductoDao().buscarPorCodigo(dialogo.getCodigoProducto());
                if (producto == null) {
                    javax.swing.JOptionPane.showMessageDialog(this,"El producto pesable no existe en la base de datos.");
                } else {
                    agregarAlCarrito(producto, java.math.BigDecimal.valueOf(dialogo.getPeso()));
                }
            }
        });

        //==========================================================
        // VENTA / CONCEPTO MANUAL
        //==========================================================
        btnManual.addActionListener(e -> {

            java.awt.Window ventana
                    = javax.swing.SwingUtilities
                            .getWindowAncestor(
                                    this
                            );

            Dialogos.DialogoVentaManual dialogo
                    = new Dialogos.DialogoVentaManual(
                            ventana
                    );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {

                javax.swing.JOptionPane.showMessageDialog(this,
                    "Las ventas manuales requieren un concepto asociado a un producto registrado. "
                    + "Esta opción no se agregará al carrito para evitar diferencias de stock y contabilidad.",
                    "Venta manual",javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //==========================================================
        // ELIMINAR
        //==========================================================
        btnEliminar.addActionListener(e -> {

            int fila
                    = tabla.getSelectedRow();

            if (fila == -1) {

                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un producto de la venta.",
                        "Eliminar Producto",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String producto
                    = tabla.getValueAt(
                            fila,
                            1
                    ).toString();

            int opcion
                    = javax.swing.JOptionPane.showConfirmDialog(
                            this,
                            "¿Desea quitar de la venta:\n\n"
                            + producto
                            + "?",
                            "Eliminar Producto",
                            javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE
                    );

            if (opcion
                    == javax.swing.JOptionPane.YES_OPTION) {

                int modeloFila = tabla.convertRowIndexToModel(fila);
                int id = ((Number) tabla.getModel().getValueAt(modeloFila,0)).intValue();
                carrito.remove(id);
                actualizarCarrito();
            }
        });
    }

    // Nueva venta: borra únicamente el carrito en memoria, sin tocar ventas ya guardadas.
    private void limpiarVentaActual() {
        if (!carrito.isEmpty()) {
            int opcion = javax.swing.JOptionPane.showConfirmDialog(this,
                    "¿Limpiar todos los productos y reiniciar el total de esta venta?",
                    "Nueva venta", javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (opcion != javax.swing.JOptionPane.YES_OPTION) return;
        }
        carrito.clear();
        descuentoGeneralVenta = java.math.BigDecimal.ZERO;
        if (tabla != null) tabla.clearSelection();
        if (txtCodigo != null) {
            txtCodigo.setText("");
            txtCodigo.requestFocusInWindow();
        }
        if(txtImporteRecibido!=null)txtImporteRecibido.setText("");
        actualizarCarrito();
    }

    private void configurarAtajosCaja() {
        // WHEN_IN_FOCUSED_WINDOW permite usarlos incluso con el foco en la tabla.
        javax.swing.InputMap teclas = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap acciones = getActionMap();
        registrarAtajo(teclas, acciones, "F2", "buscarProducto", () -> btnAgregar.doClick());
        registrarAtajo(teclas, acciones, "F3", "pesable", () -> btnPesable.doClick());
        registrarAtajo(teclas, acciones, "F4", "manual", () -> btnManual.doClick());
        registrarAtajo(teclas, acciones, "F6", "quitar", () -> btnEliminar.doClick());
        registrarAtajo(teclas, acciones, "F7", "clienteFacturacion", () -> abrirClienteFacturacion());
        registrarAtajo(teclas, acciones, "F8", "nuevaVenta", () -> limpiarVentaActual());
        registrarAtajo(teclas, acciones, "F9", "cobrar", () -> btnCobrar.doClick());
        registrarAtajo(teclas, acciones, "F10", "codigo", () -> {
            txtCodigo.requestFocusInWindow();
            txtCodigo.selectAll();
        });
        btnAgregar.setToolTipText("F2 - Buscar producto");
        btnPesable.setToolTipText("F3 - Producto pesable");
        btnManual.setToolTipText("F4 - Venta manual (pendiente)");
        btnEliminar.setToolTipText("F6 - Quitar producto seleccionado");
        btnCobrar.setToolTipText("F9 - Cobrar venta");
        btnScan.setToolTipText("Ingresá un código y presioná Enter (o hacé clic aquí)");
    }

    private void registrarAtajo(javax.swing.InputMap teclas, javax.swing.ActionMap acciones,
            String tecla, String nombre, Runnable accion) {
        teclas.put(javax.swing.KeyStroke.getKeyStroke(tecla), nombre);
        acciones.put(nombre, new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                accion.run();
            }
        });
    }

    private static java.math.BigDecimal dinero(java.math.BigDecimal x) {
        return x.setScale(2, java.math.RoundingMode.HALF_UP);
    }
    private java.math.BigDecimal subtotalCarrito() {
        java.math.BigDecimal t=java.math.BigDecimal.ZERO;
        for (LineaCarrito l:carrito.values()) t=t.add(l.producto.getPrecioVenta().multiply(l.cantidad));
        return dinero(t);
    }
    private void buscarCodigoEscaneado() {
        String codigo=txtCodigo.getText().trim();
        if(codigo.isEmpty())return;
        Dao.ProductoDao dao=new Dao.ProductoDao();
        model.Producto p=dao.buscarPorCodigoBarra(codigo);
        if(p==null)p=dao.buscarPorCodigo(codigo);
        if(p==null){javax.swing.JOptionPane.showMessageDialog(this,"No se encontró el código: "+codigo);txtCodigo.selectAll();txtCodigo.requestFocusInWindow();return;}
        if (p.isPesable()) {
            abrirDialogoPesable(p);
        } else {
            agregarAlCarrito(p,java.math.BigDecimal.ONE);
        }
        txtCodigo.setText("");txtCodigo.requestFocusInWindow();
    }
    private void abrirDialogoPesable(model.Producto producto) {
        java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(this);
        Dialogos.DialogoProductoPesable d = new Dialogos.DialogoProductoPesable(ventana);
        d.seleccionarProducto(producto);
        d.setVisible(true);
        if (d.isConfirmado()) {
            agregarAlCarrito(producto, java.math.BigDecimal.valueOf(d.getPeso()));
        }
        txtCodigo.requestFocusInWindow();
    }

    private java.math.BigDecimal parsearImporte(String valor) {
        String t=valor.trim().replace("$", "").replace(" ", "");
        if (t.contains(",")) t=t.replace(".", "").replace(",", ".");
        return new java.math.BigDecimal(t);
    }
    private java.math.BigDecimal totalVentaActual() {
        return subtotalCarrito().subtract(descuentoGeneralVenta).max(java.math.BigDecimal.ZERO);
    }
    private void calcularCambio(boolean avisar) {
        if (txtImporteRecibido==null || lblCambioCalculado==null) return;
        try {
            String t=txtImporteRecibido.getText().trim();
            if (t.isEmpty()) {lblCambioCalculado.setText("$ 0,00");return;}
            java.math.BigDecimal recibido=parsearImporte(t);
            if(recibido.signum()<0)throw new NumberFormatException();
            java.math.BigDecimal diferencia=recibido.subtract(totalVentaActual());
            lblCambioCalculado.setText(formatoPeso(diferencia.max(java.math.BigDecimal.ZERO)));
            lblCambioCalculado.setForeground(diferencia.signum()<0?ROJO_ERROR:VERDE_OK);
            if(avisar && diferencia.signum()<0)javax.swing.JOptionPane.showMessageDialog(this,
                "Faltan "+formatoPeso(diferencia.negate())+" para cubrir el total.");
        } catch(Exception ex) {
            lblCambioCalculado.setText("Importe inválido");
            lblCambioCalculado.setForeground(ROJO_ERROR);
            if(avisar)javax.swing.JOptionPane.showMessageDialog(this,"Ingresá un importe válido.");
        }
    }

    private void agregarAlCarrito(model.Producto p,java.math.BigDecimal cantidad) {
        if(p==null || !p.isActivo() || p.getPrecioVenta()==null || p.getPrecioVenta().signum()<0
           || cantidad==null || cantidad.signum()<=0) {
            javax.swing.JOptionPane.showMessageDialog(this,"Producto, precio o cantidad inválidos.");return;
        }
        LineaCarrito actual=carrito.get(p.getIdProducto());
        if(actual==null)carrito.put(p.getIdProducto(),new LineaCarrito(p,cantidad));
        else actual.cantidad=actual.cantidad.add(cantidad);
        actualizarCarrito();
    }
    private void actualizarCarrito() {
        if(tabla==null)return;
        DefaultTableModel m=(DefaultTableModel)tabla.getModel();m.setRowCount(0);
        for(LineaCarrito l:carrito.values()){
            m.addRow(new Object[]{l.producto.getIdProducto(),l.producto.getNombre(),l.cantidad,
                dinero(l.producto.getPrecioVenta()),dinero(l.producto.getPrecioVenta().multiply(l.cantidad))});
        }
        if(lblTotalVentaReal!=null)lblTotalVentaReal.setText(formatoPeso(subtotalCarrito().subtract(descuentoGeneralVenta)));
        if(lblSubtotalVentaReal!=null)lblSubtotalVentaReal.setText(formatoPeso(subtotalCarrito()));
        if(lblDescuentoVentaReal!=null)lblDescuentoVentaReal.setText(formatoPeso(descuentoGeneralVenta));
        if(lblItemsActual!=null) lblItemsActual.setText("Items: " + carrito.size());
        if(lblUnidadesActual!=null) {
            java.math.BigDecimal unidades=java.math.BigDecimal.ZERO;
            for(LineaCarrito linea:carrito.values()) unidades=unidades.add(linea.cantidad);
            lblUnidadesActual.setText("Unidades: " + unidades.stripTrailingZeros().toPlainString().replace('.',','));
        }
        if(btnCobrar!=null)btnCobrar.setText("COBRAR   "+formatoPeso(totalVentaActual()));
        calcularCambio(false);
    }
    private String formatoPeso(java.math.BigDecimal importe) {
        java.text.NumberFormat f=java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es","AR"));
        return f.format(importe);
    }
    private int seleccionarId(String sql,String titulo)throws java.sql.SQLException {
        java.util.LinkedHashMap<String,Integer> opciones=new java.util.LinkedHashMap<>();
        try(java.sql.Connection c=Configuracion.conexion.getConexion();
            java.sql.PreparedStatement p=c.prepareStatement(sql);
            java.sql.ResultSet r=p.executeQuery()){
            while(r.next())opciones.put(r.getString(2)+" (#"+r.getInt(1)+")",r.getInt(1));
        }
        if(opciones.isEmpty())throw new java.sql.SQLException("No hay registros disponibles para "+titulo);
        Object eleccion=javax.swing.JOptionPane.showInputDialog(this,"Seleccioná "+titulo,titulo,
           javax.swing.JOptionPane.QUESTION_MESSAGE,null,opciones.keySet().toArray(),opciones.keySet().iterator().next());
        return eleccion==null?-1:opciones.get(eleccion.toString());
    }
    private int medioPagoSeleccionado(String nombre) throws java.sql.SQLException {
        try (java.sql.Connection c=Configuracion.conexion.getConexion();
             java.sql.PreparedStatement p=c.prepareStatement("SELECT id_medio_pago FROM medio_pago WHERE activo=1 AND LOWER(nombre)=LOWER(?)")) {
            p.setString(1,nombre);
            try(java.sql.ResultSet r=p.executeQuery()) {
                if(r.next()) return r.getInt(1);
            }
        }
        throw new java.sql.SQLException("El medio de pago '"+nombre+"' no está configurado en medio_pago. Revisá el catálogo.");
    }
    private Integer clienteSeleccionado() throws java.sql.SQLException {
        String doc=txtDocumento.getText().trim();
        if(doc.isEmpty()) return null;
        try(java.sql.Connection c=Configuracion.conexion.getConexion();
            java.sql.PreparedStatement p=c.prepareStatement("SELECT id_cliente FROM cliente WHERE activo=1 AND nro_documento=?")) {
            p.setString(1,doc);
            try(java.sql.ResultSet r=p.executeQuery()) {
                if(r.next()) { int id=r.getInt(1); if(r.next()) throw new java.sql.SQLException("Documento ambiguo: seleccioná un cliente único"); return id; }
            }
        }
        throw new java.sql.SQLException("No existe un cliente activo con documento "+doc);
    }
    /** El cajero confirma todos los datos en una sola ventana, sin prompts intermedios. */
    private void confirmarVentaReal() {
        if(carrito.isEmpty()) {javax.swing.JOptionPane.showMessageDialog(this,"Agregá productos al carrito.");return;}
        try {
            java.math.BigDecimal total=totalVentaActual();
            if(total.signum()<=0)throw new IllegalArgumentException("El total de la venta debe ser positivo.");
            java.awt.Window ventana=javax.swing.SwingUtilities.getWindowAncestor(this);
            java.math.BigDecimal inicial=java.math.BigDecimal.ZERO;
            try {if(!txtImporteRecibido.getText().trim().isEmpty())inicial=parsearImporte(txtImporteRecibido.getText());}catch(Exception ignored){}
            Dialogos.DialogoConfirmarVenta d=new Dialogos.DialogoConfirmarVenta(ventana,
                txtCliente.getText(),txtDocumento.getText(),String.valueOf(comboCondicionIVA.getSelectedItem()),
                String.valueOf(comboComprobante.getSelectedItem()),String.valueOf(comboPago.getSelectedItem()),
                subtotalCarrito().doubleValue(),descuentoGeneralVenta.doubleValue(),total.doubleValue(),
                inicial.doubleValue(),java.math.BigDecimal.ZERO.doubleValue());
            d.setVisible(true);
            if(!d.isVentaConfirmada())return;
            // Los IDs se resuelven después de confirmar la ventana, antes de registrar la transacción.
            int medio=medioPagoSeleccionado(d.getFormaPago());
            int deposito=depositoPrincipalActivo();
            java.math.BigDecimal recibido=d.getImporteRecibido();
            services.VentaService.Solicitud solicitud=new services.VentaService.Solicitud();
            solicitud.usuarioId=sesion.SesionUsuario.getIdUsuario();
            solicitud.depositoId=deposito;
            solicitud.clienteId=clienteSeleccionado();
            solicitud.observaciones=d.getObservaciones();
            solicitud.descuentoGeneral=descuentoGeneralVenta;
            for(LineaCarrito linea:carrito.values())solicitud.items.add(new services.VentaService.Item(
                linea.producto.getIdProducto(),linea.cantidad,java.math.BigDecimal.ZERO));
            solicitud.pagos.add(new services.VentaService.Pago(medio,total,recibido,""));
            services.VentaService.Resultado resultado=new services.VentaService().confirmar(solicitud);
            carrito.clear();descuentoGeneralVenta=java.math.BigDecimal.ZERO;
            txtImporteRecibido.setText("");
            txtCliente.setText("Consumidor Final");
            txtDocumento.setText("");
            comboCondicionIVA.setSelectedItem("Consumidor Final");
            comboTipoCliente.setSelectedItem("Minorista");
            comboComprobante.setSelectedItem("Ticket");
            lblResumenCliente.setText("Consumidor Final  |  Minorista  |  Ticket");
            actualizarCarrito();
            Dialogos.DialogoVentaFinalizada fin=new Dialogos.DialogoVentaFinalizada(ventana,
                String.format("%08d",resultado.numero),String.valueOf(comboComprobante.getSelectedItem()),d.getFormaPago(),
                resultado.total.doubleValue(),recibido.doubleValue(),resultado.cambio.doubleValue(),
                sesion.SesionUsuario.getNombreUsuario());
            fin.setVentaId(resultado.ventaId);
            fin.setVisible(true);
        }catch(Exception ex){javax.swing.JOptionPane.showMessageDialog(this,
            "No se registró la venta: "+ex.getMessage(),"Venta no confirmada",javax.swing.JOptionPane.ERROR_MESSAGE);}
    }
    private int depositoPrincipalActivo() throws java.sql.SQLException {
        try(java.sql.Connection c=Configuracion.conexion.getConexion();
            java.sql.PreparedStatement p=c.prepareStatement(
                "SELECT id_deposito FROM deposito WHERE activo=1 ORDER BY es_principal DESC,id_deposito LIMIT 1");
            java.sql.ResultSet r=p.executeQuery()) {
            if(r.next())return r.getInt(1);
        }
        throw new java.sql.SQLException("No hay un depósito activo configurado.");
    }

    private void armarSubPanelProducto() {

        subPanelProducto.removeAll();

        subPanelProducto.setLayout(
                new BoxLayout(
                        subPanelProducto,
                        BoxLayout.Y_AXIS
                )
        );

        subPanelProducto.setPreferredSize(
                new Dimension(0, 190)
        );

        subPanelProducto.setOpaque(false);

        subPanelProducto.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel("PRODUCTO");

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        titulo.setForeground(
                TITULO_PANEL
        );

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        subPanelProducto.add(titulo);

        subPanelProducto.add(
                Box.createVerticalStrut(5)
        );

        //====================================================
        // SUBTITULO
        //====================================================
        JLabel subtitulo
                = new JLabel(
                        "Ingrese, busque o escanee el código del producto"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                Color.GRAY
        );

        subtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        subPanelProducto.add(subtitulo);

        subPanelProducto.add(
                Box.createVerticalStrut(8)
        );

        //====================================================
        // FILA CODIGO
        //====================================================
        JPanel filaCodigo
                = new JPanel();

        filaCodigo.setOpaque(false);

        filaCodigo.setLayout(
                new BoxLayout(
                        filaCodigo,
                        BoxLayout.X_AXIS
                )
        );

        filaCodigo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        txtCodigo
                = new TextFieldRedondeado();

        txtCodigo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        txtCodigo.setText(
                "LAC009"
        );

        txtCodigo.setPreferredSize(
                new Dimension(
                        320,
                        40
                )
        );

        txtCodigo.setMaximumSize(
                new Dimension(
                        420,
                        40
                )
        );

        btnScan
                = new RSButtonRound();

        btnScan.setPreferredSize(
                new Dimension(
                        58,
                        40
                )
        );

        btnScan.setMaximumSize(
                new Dimension(
                        58,
                        40
                )
        );

        btnScan.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/barcode.png"
                        )
                )
        );

        filaCodigo.add(txtCodigo);

        filaCodigo.add(
                Box.createHorizontalStrut(10)
        );

        filaCodigo.add(btnScan);

        filaCodigo.add(
                Box.createHorizontalGlue()
        );

        subPanelProducto.add(filaCodigo);

        subPanelProducto.add(
                Box.createVerticalStrut(6)
        );

        //====================================================
        // PRODUCTO ENCONTRADO
        //====================================================
        lblProducto
                = new JLabel(
                        "Jamón La Piamontesa x kg"
                );

        lblProducto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        lblProducto.setForeground(
                new Color(
                        0,
                        140,
                        0
                )
        );

        subPanelProducto.add(lblProducto);

        subPanelProducto.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // BOTONES DE PRODUCTO
        //====================================================
        JPanel filaBotones
                = new JPanel();

        filaBotones.setOpaque(false);

        filaBotones.setLayout(
                new BoxLayout(
                        filaBotones,
                        BoxLayout.X_AXIS
                )
        );

        filaBotones.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        Dimension sizeBtn
                = new Dimension(
                        140,
                        40
                );

        btnPesable
                = new RSButtonRound();

        btnAgregar
                = new RSButtonRound();

        btnManual
                = new RSButtonRound();

        btnEliminar
                = new RSButtonRound();

        btnPesable.setText("Pesable");
        btnAgregar.setText("Agregar");
        btnManual.setText("Manual");
        btnEliminar.setText("Eliminar");

        btnPesable.setBackground(AZUL_PRESION);
        btnAgregar.setBackground(AZUL_PRESION);
        btnManual.setBackground(AZUL_PRESION);
        btnEliminar.setBackground(AZUL_PRESION);

        btnPesable.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/balanza1.png"
                        )
                )
        );

        btnAgregar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/agregar5.png"
                        )
                )
        );

        btnManual.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/manual3.png"
                        )
                )
        );

        btnEliminar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/eliminar4.png"
                        )
                )
        );

        RSButtonRound[] botones = {
            btnPesable,
            btnAgregar,
            btnManual,
            btnEliminar
        };

        for (RSButtonRound boton : botones) {

            boton.setPreferredSize(
                    sizeBtn
            );

            boton.setMaximumSize(
                    sizeBtn
            );

            boton.setHorizontalTextPosition(
                    SwingConstants.RIGHT
            );

            boton.setIconTextGap(8);

            filaBotones.add(boton);

            filaBotones.add(
                    Box.createHorizontalStrut(10)
            );
        }

        filaBotones.add(
                Box.createHorizontalGlue()
        );

        alinearIzquierda(titulo);
        alinearIzquierda(subtitulo);
        alinearIzquierda(filaCodigo);
        alinearIzquierda(lblProducto);
        alinearIzquierda(filaBotones);

        subPanelProducto.add(
                filaBotones
        );

        subPanelProducto.revalidate();
        subPanelProducto.repaint();
    }

    private void armarSubPanelCliente() {
        subPanelClientes.removeAll();
        subPanelClientes.setLayout(new java.awt.BorderLayout(12,0));
        subPanelClientes.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,16,10,16));
        subPanelClientes.setOpaque(false);
        txtCliente = new TextFieldRedondeado(); txtCliente.setText("Consumidor Final");
        txtDocumento = new TextFieldRedondeado();
        comboCondicionIVA = new RSComboBox();
        comboCondicionIVA.setModel(new DefaultComboBoxModel<>(new String[]{
            "Consumidor Final","Responsable Inscripto","Monotributista","Exento"}));
        comboTipoCliente = new RSComboBox();
        comboTipoCliente.setModel(new DefaultComboBoxModel<>(new String[]{"Minorista","Mayorista"}));
        comboComprobante = new RSComboBox();
        comboComprobante.setModel(new DefaultComboBoxModel<>(new String[]{"Ticket","Factura A","Factura B","Factura C"}));
        lblResumenCliente = new JLabel("Consumidor Final  |  Minorista  |  Ticket");
        lblResumenCliente.setFont(new Font("Segoe UI",Font.BOLD,14));
        lblResumenCliente.setForeground(AZUL_PRESION);
        JButton boton = new JButton("CLIENTE / FACTURACIÓN  F7");
        boton.setFont(new Font("Segoe UI",Font.BOLD,14));
        boton.setPreferredSize(new Dimension(280,48));
        boton.setEnabled(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        boton.setForeground(Color.WHITE);
        boton.setIcon(new javax.swing.Icon(){
            public int getIconWidth(){return 23;}public int getIconHeight(){return 23;}
            public void paintIcon(java.awt.Component c,java.awt.Graphics g,int x,int y){
                java.awt.Graphics2D a=(java.awt.Graphics2D)g.create();
                a.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                a.translate(x,y);a.setColor(new Color(255,206,77));a.fillRoundRect(1,1,21,21,6,6);
                a.setColor(new Color(17,57,117));a.setStroke(new java.awt.BasicStroke(1.7f));
                a.drawOval(7,4,9,9);a.drawArc(4,13,15,9,0,180);a.dispose();
            }
        });
        boton.setIconTextGap(8);
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI(){
            @Override public void paint(java.awt.Graphics g,javax.swing.JComponent c){
                javax.swing.AbstractButton b=(javax.swing.AbstractButton)c;
                java.awt.Graphics2D a=(java.awt.Graphics2D)g.create();
                a.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                a.setColor(b.getModel().isPressed()?new Color(12,55,117):b.getModel().isRollover()?new Color(23,135,219):new Color(17,82,163));
                a.fillRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,14,14);a.dispose();
                super.paint(g,c);
            }
        });
        boton.setRolloverEnabled(true);
        boton.addActionListener(e -> abrirClienteFacturacion());
        subPanelClientes.add(lblResumenCliente,java.awt.BorderLayout.CENTER);
        subPanelClientes.add(boton,java.awt.BorderLayout.EAST);
    }
    private void abrirClienteFacturacion() {
        Dialogos.DialogoClienteFacturacionVentas dialogo = new Dialogos.DialogoClienteFacturacionVentas(
                javax.swing.SwingUtilities.getWindowAncestor(this),
                txtCliente.getText(),txtDocumento.getText(),
                String.valueOf(comboCondicionIVA.getSelectedItem()),
                String.valueOf(comboTipoCliente.getSelectedItem()),
                String.valueOf(comboComprobante.getSelectedItem()));
        dialogo.setVisible(true);
        if(!dialogo.isConfirmado())return;
        txtCliente.setText(dialogo.getNombre());
        txtDocumento.setText(dialogo.getDocumento());
        comboCondicionIVA.setSelectedItem(dialogo.getCondicionIVA());
        comboTipoCliente.setSelectedItem(dialogo.getTipoVenta());
        comboComprobante.setSelectedItem(dialogo.getComprobante());
        lblResumenCliente.setText(dialogo.getNombre()+"  |  "+dialogo.getTipoVenta()+"  |  "+dialogo.getComprobante());
    }

    private void armarTabla() {

        //==========================================================
        // MODELO
        //==========================================================
        String columnas[] = {
            "ID",
            "PRODUCTO",
            "CANT.",
            "PRECIO",
            "SUBTOTAL"
        };

        DefaultTableModel modelo
                = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tabla = new JTable(modelo);

        //==========================================================
        // CONFIGURACION GENERAL TABLA
        //==========================================================
        tabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tabla.setRowHeight(32);

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        tabla.setGridColor(
                new Color(
                        230,
                        234,
                        240
                )
        );

        tabla.setSelectionBackground(
                new Color(
                        205,
                        220,
                        242
                )
        );

        tabla.setSelectionForeground(
                Color.BLACK
        );

        tabla.setBackground(
                Color.WHITE
        );

        tabla.setForeground(
                new Color(
                        40,
                        40,
                        40
                )
        );

        tabla.setFillsViewportHeight(true);

        //==========================================================
        // HEADER
        //==========================================================
        JTableHeader header
                = tabla.getTableHeader();

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        38
                )
        );

        header.setReorderingAllowed(false);

        //==========================================================
        // TAMAÑO COLUMNAS
        //==========================================================
        tabla.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);

        tabla.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(430);

        tabla.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        tabla.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        tabla.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        //==========================================================
        // ALINEACION COLUMNAS
        //==========================================================
        DefaultTableCellRenderer centro
                = new DefaultTableCellRenderer();

        centro.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        DefaultTableCellRenderer derecha
                = new DefaultTableCellRenderer();

        derecha.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        tabla.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centro);

        tabla.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centro);

        tabla.getColumnModel()
                .getColumn(3)
                .setCellRenderer(derecha);

        tabla.getColumnModel()
                .getColumn(4)
                .setCellRenderer(derecha);

        //==========================================================
        // BARRA INFORMACION DE VENTA
        //==========================================================
        JPanel panelInfoVenta
                = new JPanel(
                        new BorderLayout()
                );

        panelInfoVenta.setBackground(
                Color.WHITE
        );

        panelInfoVenta.setPreferredSize(
                new Dimension(
                        0,
                        44
                )
        );

        panelInfoVenta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(
                                        220,
                                        225,
                                        232
                                )
                        ),
                        new EmptyBorder(
                                5,
                                15,
                                5,
                                15
                        )
                )
        );

        //==========================================================
        // NUMERO DE VENTA
        //==========================================================
        JPanel panelNumero
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                3
                        )
                );

        panelNumero.setOpaque(false);

        JLabel lblVenta
                = new JLabel(
                        "Venta N°"
                );

        lblVenta.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblVenta.setForeground(
                new Color(
                        100,
                        105,
                        115
                )
        );

        JLabel lblNumeroVenta
                = new JLabel(
                        "Al confirmar"
                );

        lblNumeroVenta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblNumeroVenta.setForeground(
                TITULO_PANEL
        );

        lblNumeroVentaActual = lblNumeroVenta;
        panelNumero.add(lblVenta);
        panelNumero.add(lblNumeroVenta);

        //==========================================================
        // ITEMS Y UNIDADES
        //==========================================================
        JPanel panelEstadisticas
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                18,
                                3
                        )
                );

        panelEstadisticas.setOpaque(false);

        JLabel lblItems
                = new JLabel(
                        "Items: 0"
                );

        lblItems.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblItems.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        JLabel lblUnidades
                = new JLabel(
                        "Unidades: 0"
                );

        lblUnidades.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblUnidades.setForeground(
                new Color(
                        70,
                        75,
                        85
                )
        );

        lblItemsActual = lblItems;
        lblUnidadesActual = lblUnidades;
        panelEstadisticas.add(lblItems);
        panelEstadisticas.add(lblUnidades);

        panelInfoVenta.add(
                panelNumero,
                BorderLayout.WEST
        );

        panelInfoVenta.add(
                panelEstadisticas,
                BorderLayout.EAST
        );

        //==========================================================
        // SCROLL TABLA
        //==========================================================
        JScrollPane scroll
                = new JScrollPane(
                        tabla
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.getViewport()
                .setBackground(
                        Color.WHITE
                );

        //==========================================================
        // CONTENEDOR COMPLETO
        //==========================================================
        JPanel contenedorTabla
                = new JPanel(
                        new BorderLayout()
                );

        contenedorTabla.setBackground(
                Color.WHITE
        );

        contenedorTabla.add(
                panelInfoVenta,
                BorderLayout.NORTH
        );

        contenedorTabla.add(
                scroll,
                BorderLayout.CENTER
        );

        subPanelDetalleVenta.removeAll();

        subPanelDetalleVenta.setLayout(
                new BorderLayout()
        );

        subPanelDetalleVenta.add(
                contenedorTabla,
                BorderLayout.CENTER
        );

        //==========================================================
        // ESTILO SERENA SOFT
        //==========================================================
        TablaEstilo.aplicar(
                tabla,
                scroll
        );

        subPanelDetalleVenta.revalidate();
        subPanelDetalleVenta.repaint();
    }

    private void armarPanelDerecho(JPanel panelDerecho) {

        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        javax.swing.JLabel lblTotal = new javax.swing.JLabel("$ 0,00");
        lblTotal.setFont(new java.awt.Font("Arial", 1, 28));

        javax.swing.JComboBox<String> comboPago
                = new javax.swing.JComboBox<>(new String[]{"Efectivo", "Tarjeta"});

        javax.swing.JTextField txtPago = new javax.swing.JTextField();

        javax.swing.JButton btnCobrar = new javax.swing.JButton("COBRAR");

        panelDerecho.add(new javax.swing.JLabel("TOTAL"));
        panelDerecho.add(lblTotal);
        panelDerecho.add(new javax.swing.JLabel("Forma de pago"));
        panelDerecho.add(comboPago);
        panelDerecho.add(new javax.swing.JLabel("Importe"));
        panelDerecho.add(txtPago);
        panelDerecho.add(btnCobrar);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelCentralVentas = new javax.swing.JPanel();
        subPanelProducto = new Diseños.PanelRedondeado();
        subPanelClientes = new Diseños.PanelRedondeado();
        subPanelDetalleVenta = new Diseños.PanelRedondeado();

        setBackground(new java.awt.Color(51, 51, 51));
        setLayout(new java.awt.BorderLayout());

        PanelCentralVentas.setLayout(new javax.swing.BoxLayout(PanelCentralVentas, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout subPanelProductoLayout = new javax.swing.GroupLayout(subPanelProducto);
        subPanelProducto.setLayout(subPanelProductoLayout);
        subPanelProductoLayout.setHorizontalGroup(
            subPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelProductoLayout.setVerticalGroup(
            subPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelProducto);

        javax.swing.GroupLayout subPanelClientesLayout = new javax.swing.GroupLayout(subPanelClientes);
        subPanelClientes.setLayout(subPanelClientesLayout);
        subPanelClientesLayout.setHorizontalGroup(
            subPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelClientesLayout.setVerticalGroup(
            subPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelClientes);

        javax.swing.GroupLayout subPanelDetalleVentaLayout = new javax.swing.GroupLayout(subPanelDetalleVenta);
        subPanelDetalleVenta.setLayout(subPanelDetalleVentaLayout);
        subPanelDetalleVentaLayout.setHorizontalGroup(
            subPanelDetalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        subPanelDetalleVentaLayout.setVerticalGroup(
            subPanelDetalleVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PanelCentralVentas.add(subPanelDetalleVenta);

        add(PanelCentralVentas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentralVentas;
    private Diseños.PanelRedondeado subPanelClientes;
    private Diseños.PanelRedondeado subPanelDetalleVenta;
    private Diseños.PanelRedondeado subPanelProducto;
    // End of variables declaration//GEN-END:variables

    private void armarLayout() {

        removeAll();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        //=========================================
        // PANEL IZQUIERDO
        //=========================================
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 12));
        panelIzquierdo.setOpaque(false);

        // Alturas
        subPanelProducto.setPreferredSize(new Dimension(0, 190));
        subPanelClientes.setPreferredSize(new Dimension(0, 65));

        panelIzquierdo.add(subPanelProducto, BorderLayout.NORTH);
        panelIzquierdo.add(subPanelDetalleVenta, BorderLayout.CENTER);
        panelIzquierdo.add(subPanelClientes, BorderLayout.SOUTH);

        //=========================================
        // PANEL DERECHO
        //=========================================
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 12));
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(330, 0));

        panelTotal = new PanelRedondeado();
        panelPago = new PanelRedondeado();
        panelAcciones = new PanelRedondeado();
        panelTotal.setPreferredSize(
                new Dimension(
                        330,
                        175
                )
        );

        panelPago.setPreferredSize(
                new Dimension(
                        330,
                        285
                )
        );

        panelAcciones.setPreferredSize(
                new Dimension(
                        330,
                        120
                )
        );

        armarPanelTotal(panelTotal);
        armarPanelPago(panelPago);
        armarPanelAcciones(panelAcciones);

        panelDerecho.add(panelTotal, BorderLayout.NORTH);
        panelDerecho.add(panelPago, BorderLayout.CENTER);
        panelDerecho.add(panelAcciones, BorderLayout.SOUTH);

        //=========================================
        // AGREGAR AL PANEL PRINCIPAL
        //=========================================
        add(panelIzquierdo, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void armarPanelPago(JPanel panel) {

        panel.removeAll();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );

        panel.setOpaque(false);

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel(
                        "PAGO"
                );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        titulo.setForeground(
                TITULO_PANEL
        );

        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // FORMA DE PAGO
        //====================================================
        JPanel filaForma
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaForma.setOpaque(false);

        filaForma.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );

        JLabel lblForma
                = new JLabel(
                        "Forma:"
                );

        lblForma.setForeground(
                AZUL_PRESION
        );

        // IMPORTANTE:
        // usamos el atributo de la clase
        comboPago
                = new RSComboBox();

        comboPago.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{
                            "Efectivo",
                            "Tarjeta",
                            "Transferencia",
                            "Mercado Pago"
                        }
                )
        );

        comboPago.setPreferredSize(
                new Dimension(
                        190,
                        32
                )
        );

        filaForma.add(
                lblForma,
                BorderLayout.WEST
        );

        filaForma.add(
                comboPago,
                BorderLayout.CENTER
        );

        panel.add(filaForma);

        panel.add(
                Box.createVerticalStrut(9)
        );

        //====================================================
        // RECIBE
        //====================================================
        JPanel filaImporte
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaImporte.setOpaque(false);

        filaImporte.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        JLabel lblImporte
                = new JLabel(
                        "Recibe:"
                );

        lblImporte.setForeground(
                AZUL_PRESION
        );

        JTextField txtImporte
                = new JTextField();
        txtImporteRecibido = txtImporte;

        txtImporte.setHorizontalAlignment(
                JTextField.RIGHT
        );

        txtImporte.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        filaImporte.add(
                lblImporte,
                BorderLayout.WEST
        );

        filaImporte.add(
                txtImporte,
                BorderLayout.CENTER
        );

        panel.add(filaImporte);

        panel.add(
                Box.createVerticalStrut(9)
        );

        //====================================================
        // CAMBIO
        //====================================================
        JPanel filaCambio
                = new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        filaCambio.setOpaque(false);

        filaCambio.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        JLabel lblCambio
                = new JLabel(
                        "Cambio:"
                );

        lblCambio.setForeground(
                AZUL_PRESION
        );

        JLabel cambio
                = new JLabel(
                        "$ 0,00"
                );

        lblCambioCalculado = cambio;
        cambio.setOpaque(true);

        cambio.setBackground(
                new Color(
                        228,
                        245,
                        228
                )
        );

        cambio.setForeground(
                new Color(
                        0,
                        130,
                        60
                )
        );

        cambio.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        cambio.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        filaCambio.add(
                lblCambio,
                BorderLayout.WEST
        );

        filaCambio.add(
                cambio,
                BorderLayout.CENTER
        );

        panel.add(filaCambio);

        panel.add(
                Box.createVerticalStrut(10)
        );

        //====================================================
        // CALCULAR CAMBIO
        //====================================================
        RSButtonRound btnCalcular
                = new RSButtonRound();

        btnCalcular.setText(
                "CALCULAR CAMBIO"
        );

        btnCalcular.setBackground(
                new Color(
                        15,
                        125,
                        195
                )
        );

        btnCalcular.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnCalcular.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        panel.add(btnCalcular);
        btnCalcular.addActionListener(e -> calcularCambio(true));
        txtImporte.addActionListener(e -> calcularCambio(true));
        txtImporte.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e){calcularCambio(false);}
            public void removeUpdate(javax.swing.event.DocumentEvent e){calcularCambio(false);}
            public void changedUpdate(javax.swing.event.DocumentEvent e){calcularCambio(false);}
        });

        panel.add(
                Box.createVerticalStrut(8)
        );

        //====================================================
        // COBRAR
        //====================================================
        btnCobrar
                = new RSButtonRound();

        btnCobrar.setText(
                "COBRAR   $ 0,00"
        );

        btnCobrar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        btnCobrar.setBackground(
                VERDE_OK
        );

        btnCobrar.setForeground(
                Color.WHITE
        );

        btnCobrar.setIcon(
                new ImageIcon(
                        getClass().getResource(
                                "/img/cobrar1.png"
                        )
                )
        );

        btnCobrar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnCobrar.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        panel.add(btnCobrar);

        btnCobrar.addActionListener(e -> confirmarVentaReal());

        panel.add(
                Box.createVerticalGlue()
        );

        panel.revalidate();
        panel.repaint();
    }

    private void armarPanelAcciones(JPanel panel) {

        panel.removeAll();

        panel.setLayout(
                new GridLayout(
                        2,
                        2,
                        10,
                        10
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        panel.add(
                crearBotonAccion(
                        "DESCUENTO"
                )
        );

        panel.add(
                crearBotonAccion(
                        "PENDIENTE"
                )
        );

        panel.add(
                crearBotonAccion(
                        "OBSERVACIÓN"
                )
        );

        panel.add(
                crearBotonAccion(
                        "LIMPIAR"
                )
        );

        panel.revalidate();
        panel.repaint();
    }

    private RSButtonRound crearBotonAccion(String texto) {

        RSButtonRound b = new RSButtonRound();

        b.setPreferredSize(new Dimension(135, 42));

        b.setText(texto);

        b.setBackground(new Color(17, 72, 148));

        b.setForeground(Color.WHITE);

        return b;

    }

    private void armarPanelTotal(JPanel panel) {

        panel.removeAll();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        panel.setOpaque(false);

        //====================================================
        // TITULO
        //====================================================
        JLabel titulo
                = new JLabel(
                        "TOTAL DE LA VENTA"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        titulo.setForeground(
                new Color(
                        18,
                        120,
                        45
                )
        );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //====================================================
        // TOTAL PRINCIPAL
        //====================================================
        JLabel lblTotal
                = new JLabel("$ 0,00");
        lblTotalVentaReal = lblTotal;

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        40
                )
        );

        lblTotal.setForeground(
                new Color(
                        0,
                        140,
                        70
                )
        );

        lblTotal.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        //====================================================
        // SUBTOTAL
        //====================================================
        JPanel filaSubtotal
                = crearFilaImporte(
                        "Subtotal:",
                        "$ 0,00",
                        Color.BLACK
                );

        //====================================================
        // DESCUENTO
        //====================================================
        JPanel filaDescuento
                = crearFilaImporte(
                        "Descuento:",
                        "$ 0,00",
                        ROJO_ERROR
                );

        lblSubtotalVentaReal = (JLabel)((BorderLayout)filaSubtotal.getLayout()).getLayoutComponent(BorderLayout.EAST);
        lblDescuentoVentaReal = (JLabel)((BorderLayout)filaDescuento.getLayout()).getLayoutComponent(BorderLayout.EAST);
        panel.add(titulo);

        panel.add(
                Box.createVerticalStrut(8)
        );

        panel.add(lblTotal);

        panel.add(
                Box.createVerticalStrut(10)
        );

        panel.add(filaSubtotal);

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(filaDescuento);

        panel.revalidate();
        panel.repaint();
    }

    private JPanel crearFilaImporte(
            String texto,
            String valor,
            Color colorValor) {

        JPanel fila
                = new JPanel(
                        new BorderLayout()
                );

        fila.setOpaque(false);

        fila.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        24
                )
        );

        JLabel titulo
                = new JLabel(texto);

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel importe
                = new JLabel(valor);

        importe.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        importe.setForeground(
                colorValor
        );

        fila.add(
                titulo,
                BorderLayout.WEST
        );

        fila.add(
                importe,
                BorderLayout.EAST
        );

        return fila;
    }

}
