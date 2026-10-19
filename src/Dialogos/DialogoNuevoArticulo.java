package Dialogos;

import Configuracion.conexion;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.Normalizer;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/** Alta de productos Serena Soft: catálogo real, código correlativo y persistencia. */
public class DialogoNuevoArticulo extends JDialog {
    private static final Color NAVY=new Color(17,49,105), BLUE=new Color(16,102,204), BG=new Color(224,234,247), GREEN=new Color(0,151,92);
    private final JTextField nombre=field(), descripcion=field(), barra=field(), interno=field(), ubicacion=field(), codigo=field(), proveedor=field();
    private final JTextField costo=field(), precio=field(), margen=field(), minimo=field(), factor=field();
    private final JTextField costoVenta=field();
    private final JLabel unidadCosto=new JLabel("Costo por unidad de compra"), unidadPrecio=new JLabel("Precio por unidad de venta");
    private final JLabel ayudaConversion=new JLabel("Elegí las unidades para ver cómo se calcula el costo.");
    private final JLabel ayudaMargen=new JLabel("Margen = (precio de venta - costo por unidad de venta) / costo por unidad de venta");
    private final JCheckBox pesoVariable=new JCheckBox("Compra por pieza de peso variable (registrar peso real al recibir)");
    private final JComboBox<String> rubro=combo(), categoria=combo(), marca=combo(), compra=combo(), venta=combo(), iva=combo();
    private final JCheckBox activo=new JCheckBox("Producto activo",true), controla=new JCheckBox("Controlar stock",true), pesable=new JCheckBox("Pesable"), descuento=new JCheckBox("Permitir descuentos"), sinStock=new JCheckBox("Venta sin stock");
    private final JTextArea observaciones=new JTextArea(3,30);
    private final Map<JComboBox<String>,Map<String,Integer>> ids=new IdentityHashMap<>();
    private final JLabel aviso=new JLabel("Seleccioná una categoría para generar el código");
    private int idProveedor=-1;
    private boolean guardado=false, cargando=false;

    public DialogoNuevoArticulo(Window parent){
        super(parent,"Nuevo artículo · Serena Soft",ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());getContentPane().setBackground(BG);
        Rectangle area=GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(960,area.width-40),Math.min(660,area.height-50));
        setMinimumSize(new Dimension(740,470));
        JPanel head=new JPanel(new BorderLayout());head.setBackground(NAVY);head.setBorder(new EmptyBorder(18,22,18,22));
        JLabel titulo=new JLabel("  NUEVO ARTÍCULO  ·  SERENA SOFT", new SymbolIcon("box", Color.WHITE, 27), SwingConstants.LEFT);titulo.setIconTextGap(14);titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,22));head.add(titulo);add(head,BorderLayout.NORTH);
        // Tres pestañas compactas: sin desplazamiento horizontal ni formulario interminable.
        JTabbedPane tabs=new JTabbedPane();
        tabs.setFont(new Font("Segoe UI",Font.BOLD,14));
        tabs.setForeground(NAVY);tabs.setBackground(Color.WHITE);

        JPanel datos=pagina();
        JPanel identificacion=grid(3);
        codigo.setEditable(false);codigo.setBackground(new Color(210,231,255));codigo.setFont(new Font("Segoe UI",Font.BOLD,18));
        identificacion.add(cell("Código automático",codigo));
        identificacion.add(cell("Código de barras / escáner",barra));
        identificacion.add(cell("Código interno (opcional)",interno));
        identificacion.add(cell("Nombre del producto *",nombre));
        identificacion.add(cell("Ubicación / estantería",ubicacion));
        identificacion.add(cell("Descripción adicional",descripcion));
        JPanel codigoInfo=new JPanel(new BorderLayout(0,6));codigoInfo.setOpaque(false);
        codigoInfo.add(identificacion,BorderLayout.CENTER);
        aviso.setForeground(BLUE);aviso.setFont(new Font("Segoe UI",Font.BOLD,12));
        codigoInfo.add(aviso,BorderLayout.SOUTH);
        section(datos,"01  IDENTIFICACIÓN DEL ARTÍCULO",codigoInfo);

        JPanel clasif=grid(3);
        JPanel marcaBox=new JPanel(new BorderLayout(5,0));marcaBox.setOpaque(false);
        marcaBox.add(marca,BorderLayout.CENTER);
        JButton addMarca=button("NUEVA",GREEN);addMarca.setIcon(new SymbolIcon("plus",Color.WHITE,15));
        marcaBox.add(addMarca,BorderLayout.EAST);addMarca.addActionListener(e->nuevaMarca());
        clasif.add(cell("Rubro",rubro));clasif.add(cell("Categoría *",categoria));clasif.add(cell("Marca",marcaBox));
        JPanel provBox=new JPanel(new BorderLayout(5,0));provBox.setOpaque(false);
        proveedor.setEditable(false);provBox.add(proveedor,BorderLayout.CENTER);
        JButton buscar=button("BUSCAR",BLUE);buscar.setIcon(new SymbolIcon("search",Color.WHITE,15));
        provBox.add(buscar,BorderLayout.EAST);buscar.addActionListener(e->buscarProveedor());
        clasif.add(cell("Proveedor (opcional)",provBox));
        clasif.add(cell("Unidad de compra *",compra));clasif.add(cell("Unidad de venta *",venta));
        section(datos,"02  CLASIFICACIÓN Y PROVEEDOR",clasif);
        tabs.addTab("  DATOS DEL ARTÍCULO  ",new SymbolIcon("box",NAVY,17),envolver(datos));

        JPanel precios=pagina();
        JPanel numeros=grid(2);
        numeros.add(cell(unidadCosto,costo));numeros.add(cell(unidadPrecio,precio));
        costoVenta.setEditable(false);costoVenta.setBackground(new Color(213,232,255));
        numeros.add(cell("Costo calculado por unidad de venta",costoVenta));
        margen.setEditable(false);margen.setBackground(new Color(213,232,255));
        numeros.add(cell("Margen sobre costo (%) · automático",margen));
        numeros.add(cell("IVA",iva));numeros.add(cell("Stock mínimo (unidad de venta)",minimo));
        factor.setText("1");
        JPanel conversion=grid(1);
        conversion.add(cell("Unidades de venta por unidad de compra *",factor));
        ayudaConversion.setForeground(NAVY);ayudaConversion.setFont(new Font("Segoe UI",Font.PLAIN,12));
        conversion.add(ayudaConversion);
        pesoVariable.setOpaque(false);pesoVariable.setForeground(NAVY);
        conversion.add(pesoVariable);
        JLabel aclaracion=new JLabel("Si el proveedor vende una pieza de peso variable, cargá su peso exacto desde Nueva entrada.");
        aclaracion.setFont(new Font("Segoe UI",Font.PLAIN,12));aclaracion.setForeground(BLUE);
        conversion.add(aclaracion);
        section(precios,"03  COSTOS, PRECIOS Y UNIDADES",numeros);
        section(precios,"CONVERSIÓN DE COMPRA A VENTA",conversion);
        ayudaMargen.setForeground(NAVY);ayudaMargen.setFont(new Font("Segoe UI",Font.PLAIN,12));
        section(precios,"CÓMO SE CALCULA EL MARGEN",ayudaMargen);
        JLabel cero=new JLabel("  Stock inicial: 0  ·  Las cantidades se registran desde Nueva entrada de mercadería");
        cero.setFont(new Font("Segoe UI",Font.BOLD,13));cero.setForeground(NAVY);
        JPanel stockInfo=new JPanel(new BorderLayout());stockInfo.setBackground(new Color(221,237,255));
        stockInfo.setBorder(new EmptyBorder(16,10,16,10));stockInfo.add(cero);
        section(precios,"EXISTENCIAS",stockInfo);
        tabs.addTab("  PRECIOS Y UNIDADES  ",new SymbolIcon("box",NAVY,17),envolver(precios));

        JPanel config=pagina();
        JPanel opciones=new JPanel(new GridLayout(0,2,15,16));opciones.setOpaque(false);
        for(JCheckBox c:new JCheckBox[]{activo,controla,pesable,descuento,sinStock}){
            c.setOpaque(false);c.setForeground(NAVY);c.setFont(new Font("Segoe UI",Font.BOLD,13));opciones.add(c);
        }
        section(config,"04  CONFIGURACIÓN",opciones);
        observaciones.setFont(new Font("Segoe UI",Font.PLAIN,13));
        observaciones.setLineWrap(true);observaciones.setWrapStyleWord(true);
        JScrollPane notas=new JScrollPane(observaciones);
        notas.setPreferredSize(new Dimension(300,105));
        section(config,"05  OBSERVACIONES",notas);
        tabs.addTab("  CONFIGURACIÓN  ",new SymbolIcon("box",NAVY,17),envolver(config));
        add(tabs,BorderLayout.CENTER);
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));footer.setBackground(Color.WHITE);JButton limpiar=button("LIMPIAR",new Color(75,104,148)), cancelar=button("CANCELAR",new Color(75,82,95)), guardar=button("GUARDAR ARTÍCULO",GREEN);
        limpiar.setIcon(new SymbolIcon("clear",Color.WHITE,15));cancelar.setIcon(new SymbolIcon("close",Color.WHITE,15));guardar.setIcon(new SymbolIcon("check",Color.WHITE,17));
        limpiar.addActionListener(e->limpiar());cancelar.addActionListener(e->dispose());guardar.addActionListener(e->guardar());footer.add(limpiar);footer.add(cancelar);footer.add(guardar);add(footer,BorderLayout.SOUTH);getRootPane().setDefaultButton(guardar);
        cargarCatalogos();rubro.addActionListener(e->{if(!cargando)cargarCategorias();});categoria.addActionListener(e->{if(!cargando)previsualizar();});
        compra.addActionListener(e->actualizarCostos());venta.addActionListener(e->actualizarCostos());
        pesoVariable.addActionListener(e->actualizarCostos());
        javax.swing.event.DocumentListener calculadora=new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){actualizarCostos();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){actualizarCostos();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){actualizarCostos();}
        };
        costo.getDocument().addDocumentListener(calculadora);
        precio.getDocument().addDocumentListener(calculadora);
        factor.getDocument().addDocumentListener(calculadora);
        actualizarCostos();
        setLocationRelativeTo(parent);SwingUtilities.invokeLater(()->nombre.requestFocusInWindow());
    }
    private static JTextField field(){JTextField f=new JTextField();f.setPreferredSize(new Dimension(150,40));f.setFont(new Font("Segoe UI",Font.PLAIN,13));f.setForeground(NAVY);f.setBackground(new Color(246,250,255));f.setBorder(new CompoundBorder(new LineBorder(new Color(156,181,214)),new EmptyBorder(4,10,4,10)));return f;}
    private static JComboBox<String> combo(){JComboBox<String> c=new JComboBox<>();c.setPreferredSize(new Dimension(130,40));c.setFont(new Font("Segoe UI",Font.PLAIN,13));c.setBackground(new Color(230,240,254));return c;}
    private static JButton button(String label,Color color){
        JButton b=new JButton(label){
            @Override protected void paintComponent(Graphics g){
                Graphics2D d=(Graphics2D)g.create();d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color c=isEnabled()?color:new Color(142,153,169);
                if(getModel().isPressed())c=c.darker();else if(getModel().isRollover())c=c.brighter();
                d.setColor(c);d.fillRoundRect(0,0,getWidth()-1,getHeight()-1,9,9);d.dispose();super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);b.setOpaque(false);b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11,13,11,13));b.setIconTextGap(7);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;
    }
    private static JPanel pagina(){
        JPanel p=new JPanel();p.setBackground(BG);
        p.setBorder(new EmptyBorder(14,16,14,16));
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));return p;
    }
    private static JScrollPane envolver(JPanel panel){
        // Solo desplazamiento vertical cuando la pantalla sea muy pequeña.
        JScrollPane sc=new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.setBorder(null);sc.getVerticalScrollBar().setUnitIncrement(18);
        return sc;
    }
    private static JPanel grid(int n){JPanel p=new JPanel(new GridLayout(0,n,14,12));p.setOpaque(false);return p;}
    private static JPanel cell(String label,JComponent component){return cell(new JLabel(label),component);}
    private static JPanel cell(JLabel l,JComponent component){JPanel p=new JPanel(new BorderLayout(0,5));p.setOpaque(false);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(NAVY);p.add(l,BorderLayout.NORTH);p.add(component,BorderLayout.CENTER);return p;}
    private static void section(JPanel root,String title,JComponent content){JPanel p=new JPanel(new BorderLayout(0,12));p.setBackground(new Color(247,250,255));p.setBorder(new CompoundBorder(new LineBorder(new Color(163,185,217)),new EmptyBorder(13,15,15,15)));JLabel h=new JLabel(title);h.setFont(new Font("Segoe UI",Font.BOLD,16));h.setForeground(NAVY);p.add(h,BorderLayout.NORTH);p.add(content);p.setAlignmentX(Component.LEFT_ALIGNMENT);p.setMaximumSize(new Dimension(Integer.MAX_VALUE,p.getPreferredSize().height));root.add(p);root.add(Box.createVerticalStrut(10));}
    private void cargarCombo(Connection cn,JComboBox<String> cb,String sql)throws SQLException{cb.removeAllItems();cb.addItem("Seleccione...");Map<String,Integer> m=new HashMap<>();try(PreparedStatement ps=cn.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){String n=rs.getString(2);cb.addItem(n);m.put(n,rs.getInt(1));}}ids.put(cb,m);}
    private Integer id(JComboBox<String> cb){Map<String,Integer> m=ids.get(cb);return m==null?null:m.get((String)cb.getSelectedItem());}
    private void cargarCatalogos(){cargando=true;try(Connection cn=conexion.getConexion()){cargarCombo(cn,marca,"SELECT id_marca,nombre FROM marca WHERE activo=1 ORDER BY nombre");cargarCombo(cn,rubro,"SELECT id_rubro,nombre FROM rubro WHERE activo=1 ORDER BY nombre");cargarCombo(cn,compra,"SELECT id_unidad,CONCAT(nombre,' (',codigo,')') FROM unidad_medida WHERE activo=1 ORDER BY nombre");cargarCombo(cn,venta,"SELECT id_unidad,CONCAT(nombre,' (',codigo,')') FROM unidad_medida WHERE activo=1 ORDER BY nombre");cargarCombo(cn,iva,"SELECT id_iva,CONCAT(nombre,' (',porcentaje,'%)') FROM tipo_iva WHERE activo=1 ORDER BY porcentaje");}catch(SQLException e){error("Error cargando catálogos",e);}finally{cargando=false;}cargarCategorias();}
    private void cargarCategorias(){cargando=true;try(Connection cn=conexion.getConexion()){Integer r=id(rubro);String sql="SELECT id_categoria,nombre FROM categoria WHERE activo=1"+(r==null?"":" AND id_rubro=?")+" ORDER BY nombre";categoria.removeAllItems();categoria.addItem("Seleccione...");Map<String,Integer> m=new HashMap<>();try(PreparedStatement ps=cn.prepareStatement(sql)){if(r!=null)ps.setInt(1,r);try(ResultSet rs=ps.executeQuery()){while(rs.next()){String n=rs.getString(2);categoria.addItem(n);m.put(n,rs.getInt(1));}}}ids.put(categoria,m);}catch(SQLException e){error("Error cargando categorías",e);}finally{cargando=false;}previsualizar();}
    private void nuevaMarca(){JTextField n=field(),d=field();JPanel p=grid(1);p.add(cell("Nombre de la marca *",n));p.add(cell("Descripción opcional",d));if(JOptionPane.showConfirmDialog(this,p,"Nueva marca",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;if(n.getText().isBlank()){JOptionPane.showMessageDialog(this,"Ingresá una marca.");return;}try(Connection cn=conexion.getConexion()){try(PreparedStatement ps=cn.prepareStatement("INSERT INTO marca(nombre,descripcion,activo) VALUES(?,?,1)")){ps.setString(1,n.getText().trim());ps.setString(2,d.getText().trim());ps.executeUpdate();}cargarCombo(cn,marca,"SELECT id_marca,nombre FROM marca WHERE activo=1 ORDER BY nombre");marca.setSelectedItem(n.getText().trim());}catch(SQLException e){error("No se pudo crear la marca; comprobá que no exista",e);}}
    private void buscarProveedor(){DialogoBuscarProveedor d=new DialogoBuscarProveedor(this);d.setVisible(true);if(d.isSeleccionado()){idProveedor=d.getIdSeleccionado();proveedor.setText(d.getRazonSocialSeleccionada());}}
    private static String prefijo(String nombre){String s=Normalizer.normalize(nombre,Normalizer.Form.NFD).replaceAll("\\p{M}","").toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]","");return s.substring(0,Math.min(3,s.length()));}
    private static int siguiente(Connection cn,String pref)throws SQLException{int max=0;try(PreparedStatement ps=cn.prepareStatement("SELECT codigo FROM producto WHERE codigo LIKE ?")){ps.setString(1,pref+"-%");try(ResultSet rs=ps.executeQuery()){while(rs.next()){String c=rs.getString(1);if(c!=null&&c.matches(java.util.regex.Pattern.quote(pref)+"-[0-9]+")){try{max=Math.max(max,Integer.parseInt(c.substring(pref.length()+1)));}catch(NumberFormatException ignored){}}}}}return max+1;}
    private void previsualizar(){Integer cat=id(categoria);if(cat==null){codigo.setText("Seleccioná categoría");aviso.setText("El código se calcula por categoría");return;}String pref=prefijo((String)categoria.getSelectedItem());try(Connection cn=conexion.getConexion()){codigo.setText(pref+"-"+String.format("%03d",siguiente(cn,pref)));aviso.setText("Vista previa · el código definitivo se valida al guardar");}catch(SQLException e){codigo.setText(pref+"-???");aviso.setText("No se pudo consultar el correlativo");}}
    private void actualizarCostos(){
        String uCompra=compra.getSelectedItem()==null?"unidad de compra":compra.getSelectedItem().toString();
        String uVenta=venta.getSelectedItem()==null?"unidad de venta":venta.getSelectedItem().toString();
        unidadCosto.setText("Costo por " + uCompra + " (no costo total de factura)");
        unidadPrecio.setText("Precio por " + uVenta);
        if(pesoVariable.isSelected()){
            ayudaConversion.setText("Peso variable: el factor es orientativo. En cada entrada se usa el peso real recibido.");
        }else{
            ayudaConversion.setText("Ej.: caja de 12 unidades → factor 12; KG a KG → factor 1.");
        }
        try{
            BigDecimal f=decimal(factor.getText()), c=decimal(costo.getText()), v=decimal(precio.getText());
            if(f.signum()<=0){costoVenta.setText("Factor inválido");margen.setText("");return;}
            BigDecimal cv=c.divide(f,2,RoundingMode.HALF_UP);
            costoVenta.setText(cv.toPlainString());
            margen.setText(cv.signum()>0?v.subtract(cv).multiply(new BigDecimal("100")).divide(cv,2,RoundingMode.HALF_UP).toPlainString():"");
        }catch(Exception e){costoVenta.setText("");margen.setText("");}
    }
    private static BigDecimal decimal(String value)throws Exception{String t=value.trim().replace("$","").replace(" ","");if(t.isEmpty())return BigDecimal.ZERO;if(t.contains(",")&&t.contains(".")){if(t.lastIndexOf(',')>t.lastIndexOf('.'))t=t.replace(".","").replace(',','.');else t=t.replace(",","");}else if(t.contains(","))t=t.replace(',','.');try{return new BigDecimal(t);}catch(NumberFormatException e){throw new Exception("Número inválido: "+value);}}
    private static void text(PreparedStatement ps,int n,String s)throws SQLException{if(s==null||s.trim().isEmpty())ps.setNull(n,Types.VARCHAR);else ps.setString(n,s.trim());}
    private static void integer(PreparedStatement ps,int n,Integer i)throws SQLException{if(i==null)ps.setNull(n,Types.INTEGER);else ps.setInt(n,i);}
    private void guardar(){if(nombre.getText().isBlank()){JOptionPane.showMessageDialog(this,"Ingresá el nombre del producto.");return;}Integer cat=id(categoria),uc=id(compra),uv=id(venta);if(cat==null||uc==null||uv==null){JOptionPane.showMessageDialog(this,"Seleccioná categoría, unidad de compra y unidad de venta.");return;}try{BigDecimal pc=decimal(costo.getText()),pv=decimal(precio.getText()),pm=decimal(minimo.getText()),mg=decimal(margen.getText()),fc=decimal(factor.getText());if(pc.signum()<0||pv.signum()<0||pm.signum()<0)throw new Exception("Los precios y el stock mínimo no pueden ser negativos.");if(fc.signum()<=0)throw new Exception("El factor de conversión debe ser mayor que cero.");
        if(pesoVariable.isSelected() && !pesable.isSelected())throw new Exception("Para compras de peso variable, marcá Pesable en Configuración.");if(pc.signum()>0 && pv.compareTo(pc.divide(fc,2,RoundingMode.HALF_UP))<0 && JOptionPane.showConfirmDialog(this,"El precio de venta es inferior al costo por unidad de venta. ¿Deseás continuar?","Advertencia de precio",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)!=JOptionPane.YES_OPTION)return;try(Connection cn=conexion.getConexion()){cn.setAutoCommit(false);try{String catNombre;try(PreparedStatement ps=cn.prepareStatement("SELECT nombre FROM categoria WHERE id_categoria=? FOR UPDATE")){ps.setInt(1,cat);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("Categoría inexistente");catNombre=rs.getString(1);}}String code=prefijo(catNombre)+"-"+String.format("%03d",siguiente(cn,prefijo(catNombre)));String sql="INSERT INTO producto(codigo,codigo_interno,codigo_barra,nombre,descripcion,ubicacion,id_rubro,id_categoria,id_marca,id_unidad_compra,id_unidad_venta,factor_conversion,precio_compra,precio_venta,margen_porcentaje,id_iva,stock_minimo,controla_stock,permite_venta_sin_stock,es_pesable,permite_descuento,activo,observaciones) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";int productoId;try(PreparedStatement ps=cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,code);text(ps,2,interno.getText());text(ps,3,barra.getText());ps.setString(4,nombre.getText().trim());text(ps,5,descripcion.getText());text(ps,6,ubicacion.getText());integer(ps,7,id(rubro));ps.setInt(8,cat);integer(ps,9,id(marca));ps.setInt(10,uc);ps.setInt(11,uv);ps.setBigDecimal(12,fc);ps.setBigDecimal(13,pc);ps.setBigDecimal(14,pv);ps.setBigDecimal(15,mg);integer(ps,16,id(iva));ps.setBigDecimal(17,pm);ps.setBoolean(18,controla.isSelected());ps.setBoolean(19,sinStock.isSelected());ps.setBoolean(20,pesable.isSelected());ps.setBoolean(21,descuento.isSelected());ps.setBoolean(22,activo.isSelected());text(ps,23,observaciones.getText());ps.executeUpdate();try(ResultSet rs=ps.getGeneratedKeys()){if(!rs.next())throw new SQLException("No se obtuvo ID del producto");productoId=rs.getInt(1);}}if(idProveedor>0){try(PreparedStatement ps=cn.prepareStatement("INSERT INTO producto_proveedor(id_producto,id_proveedor,costo_ultimo,proveedor_principal) VALUES(?,?,?,1)")){ps.setInt(1,productoId);ps.setInt(2,idProveedor);ps.setBigDecimal(3,pc);ps.executeUpdate();}}cn.commit();guardado=true;codigo.setText(code);mostrarExito(code);dispose();}catch(Exception e){cn.rollback();throw e;}finally{cn.setAutoCommit(true);}}}catch(Exception e){error("No se pudo guardar el producto",e);}}

    private void mostrarExito(String code){
        JDialog dialog=new JDialog(this,"Artículo registrado · Serena Soft",Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel root=new JPanel(new BorderLayout());root.setBackground(Color.WHITE);
        JPanel top=new JPanel(new BorderLayout(14,0));top.setBackground(NAVY);top.setBorder(new EmptyBorder(22,24,22,24));
        JLabel logo=new JLabel(new SymbolIcon("check",new Color(101,240,173),42));top.add(logo,BorderLayout.WEST);
        JPanel textos=new JPanel(new GridLayout(2,1,0,5));textos.setOpaque(false);
        JLabel title=new JLabel("¡ARTÍCULO GUARDADO!");title.setForeground(Color.WHITE);title.setFont(new Font("Segoe UI",Font.BOLD,21));
        JLabel sub=new JLabel("El producto se registró correctamente en MySQL");sub.setForeground(new Color(216,231,255));sub.setFont(new Font("Segoe UI",Font.PLAIN,12));
        textos.add(title);textos.add(sub);top.add(textos,BorderLayout.CENTER);root.add(top,BorderLayout.NORTH);
        JPanel detail=new JPanel(new GridLayout(3,1,0,16));detail.setBackground(new Color(247,250,255));detail.setBorder(new EmptyBorder(22,28,22,28));
        detail.add(datoExito("PRODUCTO",nombre.getText().trim()));
        detail.add(datoExito("CÓDIGO SERENA SOFT",code));
        detail.add(datoExito("EXISTENCIA INICIAL","0 · Las cantidades se registran desde Nueva entrada de mercadería"));
        root.add(detail,BorderLayout.CENTER);
        JPanel foot=new JPanel(new FlowLayout(FlowLayout.RIGHT,14,13));foot.setBackground(Color.WHITE);
        JButton aceptar=button("ACEPTAR",GREEN);aceptar.setIcon(new SymbolIcon("check",Color.WHITE,16));
        aceptar.addActionListener(e->dialog.dispose());foot.add(aceptar);root.add(foot,BorderLayout.SOUTH);
        dialog.setContentPane(root);dialog.setSize(590,430);dialog.setResizable(false);dialog.setLocationRelativeTo(this);
        dialog.getRootPane().setDefaultButton(aceptar);dialog.setVisible(true);
    }
    private JPanel datoExito(String label,String value){
        JPanel p=new JPanel(new BorderLayout(0,4));p.setOpaque(false);
        JLabel a=new JLabel(label);a.setForeground(new Color(85,105,139));a.setFont(new Font("Segoe UI",Font.BOLD,11));
        JLabel b=new JLabel(value);b.setForeground(NAVY);b.setFont(new Font("Segoe UI",Font.BOLD,14));
        p.add(a,BorderLayout.NORTH);p.add(b,BorderLayout.CENTER);return p;
    }
    private static class SymbolIcon implements Icon {
        private final String type;private final Color color;private final int size;
        SymbolIcon(String type,Color color,int size){this.type=type;this.color=color;this.size=size;}
        public int getIconWidth(){return size;}public int getIconHeight(){return size;}
        public void paintIcon(Component c,Graphics g,int x,int y){
            Graphics2D d=(Graphics2D)g.create();d.translate(x,y);d.scale(size/24.0,size/24.0);
            d.setColor(color);d.setStroke(new BasicStroke(2.6f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            switch(type){
                case "plus":d.drawLine(12,4,12,20);d.drawLine(4,12,20,12);break;
                case "search":d.drawOval(3,3,13,13);d.drawLine(15,15,22,22);break;
                case "check":d.drawOval(2,2,20,20);d.drawLine(6,12,10,16);d.drawLine(10,16,18,8);break;
                case "close":d.drawLine(5,5,19,19);d.drawLine(19,5,5,19);break;
                case "clear":d.drawLine(5,7,19,7);d.drawLine(9,4,15,4);d.drawRect(7,8,10,12);d.drawLine(11,11,11,17);d.drawLine(14,11,14,17);break;
                default:d.drawRoundRect(3,5,18,16,2,2);d.drawLine(3,10,21,10);d.drawLine(9,3,15,3);
            }
            d.dispose();
        }
    }
    private void limpiar(){for(JTextField f:new JTextField[]{nombre,descripcion,barra,interno,ubicacion,proveedor,costo,precio,margen,minimo,factor})f.setText("");observaciones.setText("");factor.setText("1");pesoVariable.setSelected(false);idProveedor=-1;for(JComboBox<String> c:new JComboBox[]{marca,rubro,compra,venta,iva})if(c.getItemCount()>0)c.setSelectedIndex(0);cargarCategorias();}
    private void error(String titulo,Exception e){JOptionPane.showMessageDialog(this,titulo+":\n"+e.getMessage(),"Serena Soft",JOptionPane.ERROR_MESSAGE);}
    public boolean isGuardado(){return guardado;}
    public String getCodigo(){return codigo.getText().trim();}
    public String getCodigoBarra(){return barra.getText().trim();}
    public String getCodigoInterno(){return interno.getText().trim();}
    public String getDescripcion(){return nombre.getText().trim();}
    public String getUbicacion(){return ubicacion.getText().trim();}
    public int getIdProveedorSeleccionado(){return idProveedor;}
    public String getProveedorSeleccionado(){return proveedor.getText().trim();}
    public boolean isProductoActivo(){return activo.isSelected();}
    public boolean isProductoPesable(){return pesable.isSelected();}
    public boolean isControlStock(){return controla.isSelected();}
    public boolean isPermiteDescuento(){return descuento.isSelected();}
    public String getObservaciones(){return observaciones.getText().trim();}
}
