package Dialogos;

import Configuracion.conexion;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.text.Normalizer;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/** Alta de productos Serena Soft: catálogo real, código correlativo y persistencia. */
public class DialogoNuevoArticulo extends JDialog {
    private static final Color NAVY=new Color(17,49,105), BLUE=new Color(16,102,204), BG=new Color(224,234,247), GREEN=new Color(0,151,92);
    private final JTextField nombre=field(), descripcion=field(), barra=field(), interno=field(), ubicacion=field(), codigo=field(), proveedor=field();
    private final JComboBox<String> modoBarra=new JComboBox<>(new String[]{"Código del fabricante (escáner)","Etiqueta interna Code 128", "Sin código por ahora"});
    private final JCheckBox imprimirEtiqueta=new JCheckBox("Imprimir etiqueta al guardar");
    private final JLabel ayudaBarra=new JLabel("Escaneá o escribí el código real del envase.");
    private final JTextField costo=field(), precio=field(), margen=field(), minimo=field(), factor=field();
    private final JTextField costoVenta=field();
    private final JLabel unidadCosto=new JLabel("Costo por unidad de compra"), unidadPrecio=new JLabel("Precio por unidad de venta");
    private final JLabel ayudaConversion=new JLabel("Elegí las unidades para ver cómo se calcula el costo.");
    private final JLabel ayudaMargen=new JLabel("Recargo sobre costo = (precio de venta - costo unitario) / costo unitario.");
    private final JCheckBox pesoVariable=new JCheckBox("Compra por pieza de peso variable (registrar peso real al recibir)");
    private final JComboBox<String> rubro=combo(), categoria=combo(), marca=combo(), compra=combo(), venta=combo(), iva=combo();
    private final JCheckBox activo=new JCheckBox("Producto activo",true), controla=new JCheckBox("Controlar stock",true), pesable=new JCheckBox("Pesable"), descuento=new JCheckBox("Permitir descuentos"), sinStock=new JCheckBox("Venta sin stock");
    private final JTextArea observaciones=new JTextArea(3,30);
    private final Map<JComboBox<String>,Map<String,Integer>> ids=new IdentityHashMap<>();
    private final JLabel aviso=new JLabel("Seleccioná una categoría para generar el código");
    private int idProveedor=-1;
    private boolean guardado=false, cargando=false;
    private boolean guardarOtro=false;
    private JTabbedPane asistente;
    private final JButton[] pasosVisuales=new JButton[3];
    private JButton anteriorPaso, siguientePaso, guardarFinal, guardarYOtro;
    private final JLabel indicadorPaso=new JLabel();

    public DialogoNuevoArticulo(Window parent){
        super(parent,"Nuevo artículo · Serena Soft",ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());getContentPane().setBackground(BG);
        Rectangle area=GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(1080,area.width-50),Math.min(740,area.height-50));
        setMinimumSize(new Dimension(820,520));
        JPanel head=new JPanel(new BorderLayout());head.setBackground(NAVY);head.setBorder(new EmptyBorder(18,22,18,22));
        JLabel titulo=new JLabel("  NUEVO ARTÍCULO  ·  SERENA SOFT", new SymbolIcon("box", Color.WHITE, 27), SwingConstants.LEFT);titulo.setIconTextGap(14);titulo.setForeground(Color.WHITE);titulo.setFont(new Font("Segoe UI",Font.BOLD,22));head.add(titulo);add(head,BorderLayout.NORTH);
        // Tres pestañas compactas: sin desplazamiento horizontal ni formulario interminable.
        JTabbedPane tabs=new JTabbedPane();
        asistente=tabs;
        tabs.setFont(new Font("Segoe UI",Font.BOLD,14));
        tabs.setForeground(NAVY);tabs.setBackground(Color.WHITE);

        JPanel datos=pagina();
        JPanel identificacion=grid(2);
        codigo.setEditable(false);codigo.setBackground(new Color(210,231,255));codigo.setFont(new Font("Segoe UI",Font.BOLD,18));
        identificacion.add(cell("Código automático",codigo));
        JPanel barraPanel=new JPanel(new BorderLayout(0,5));barraPanel.setOpaque(false);
        modoBarra.setFont(new Font("Segoe UI",Font.PLAIN,13));
        modoBarra.setPreferredSize(new Dimension(280,34));
        barraPanel.add(modoBarra,BorderLayout.NORTH);
        barraPanel.add(barra,BorderLayout.CENTER);
        ayudaBarra.setFont(new Font("Segoe UI",Font.PLAIN,11));ayudaBarra.setForeground(BLUE);
        JPanel pieCodigo=new JPanel(new GridLayout(2,1,0,3));pieCodigo.setOpaque(false);
        imprimirEtiqueta.setOpaque(false);imprimirEtiqueta.setForeground(NAVY);
        imprimirEtiqueta.setFont(new Font("Segoe UI",Font.PLAIN,12));
        pieCodigo.add(ayudaBarra);pieCodigo.add(imprimirEtiqueta);
        barraPanel.add(pieCodigo,BorderLayout.SOUTH);
        identificacion.add(cell("Identificación para escáner",barraPanel));
        // codigo_interno sigue existiendo en BD, pero no se expone como tercer código confuso.
        // En las altas nuevas se mantiene NULL; el identificador estable es producto.codigo.
        interno.setText("");
        modoBarra.addActionListener(e->actualizarModoBarra());
        barra.addActionListener(e->{ if(modoBarra.getSelectedIndex()==0)nombre.requestFocusInWindow(); });
        identificacion.add(cell("Nombre del producto *",nombre));
        identificacion.add(cell("Ubicación / estantería",ubicacion));
        identificacion.add(cell("Descripción adicional",descripcion));
        JPanel codigoInfo=new JPanel(new BorderLayout(0,6));codigoInfo.setOpaque(false);
        codigoInfo.add(identificacion,BorderLayout.CENTER);
        aviso.setForeground(BLUE);aviso.setFont(new Font("Segoe UI",Font.BOLD,12));
        codigoInfo.add(aviso,BorderLayout.SOUTH);
        section(datos,"01  IDENTIFICACIÓN DEL ARTÍCULO",codigoInfo);

        JPanel clasif=grid(2);
        JPanel marcaBox=new JPanel(new BorderLayout(5,0));marcaBox.setOpaque(false);
        marcaBox.add(marca,BorderLayout.CENTER);
        JButton addMarca=button("NUEVA",GREEN);addMarca.setIcon(new SymbolIcon("plus",Color.WHITE,15));
        JButton buscarMarca=button("BUSCAR",BLUE);buscarMarca.setIcon(new SymbolIcon("search",Color.WHITE,14));
        JPanel accionesMarca=new JPanel(new GridLayout(1,2,5,0));accionesMarca.setOpaque(false);
        accionesMarca.add(buscarMarca);accionesMarca.add(addMarca);
        marcaBox.add(accionesMarca,BorderLayout.EAST);
        buscarMarca.addActionListener(e->buscarMarca());addMarca.addActionListener(e->nuevaMarca());
        JPanel categoriaBox=new JPanel(new BorderLayout(5,0));categoriaBox.setOpaque(false);
        categoriaBox.add(categoria,BorderLayout.CENTER);
        JButton addCategoria=button("NUEVA",GREEN);addCategoria.setIcon(new SymbolIcon("plus",Color.WHITE,14));
        addCategoria.addActionListener(e->nuevaCategoria());categoriaBox.add(addCategoria,BorderLayout.EAST);
        clasif.add(cell("Rubro",rubro));clasif.add(cell("Categoría *",categoriaBox));clasif.add(cell("Marca · buscá entre todas las marcas",marcaBox));
        JPanel provBox=new JPanel(new BorderLayout(5,0));provBox.setOpaque(false);
        proveedor.setEditable(false);provBox.add(proveedor,BorderLayout.CENTER);
        JButton buscar=button("BUSCAR",BLUE);buscar.setIcon(new SymbolIcon("search",Color.WHITE,15));
        provBox.add(buscar,BorderLayout.EAST);buscar.addActionListener(e->buscarProveedor());
        clasif.add(cell("Proveedor (opcional)",provBox));
        clasif.add(cell("Unidad de compra *",compra));clasif.add(cell("Unidad de venta *",venta));
        section(datos,"02  CLASIFICACIÓN Y PROVEEDOR",clasif);
        // En el alta inicial, la categoría es imprescindible: se muestra primero.
        Component identificacionCard=datos.getComponent(0);
        Component separadorIdentificacion=datos.getComponent(1);
        datos.remove(1);datos.remove(0);
        datos.add(identificacionCard);datos.add(separadorIdentificacion);
        tabs.addTab("  DATOS DEL ARTÍCULO  ",new SymbolIcon("box",NAVY,17),envolver(datos));

        JPanel precios=pagina();
        JPanel numeros=grid(2);
        numeros.add(cell(unidadCosto,costo));numeros.add(cell(unidadPrecio,precio));
        costoVenta.setEditable(false);costoVenta.setBackground(new Color(213,232,255));
        numeros.add(cell("Costo equivalente por unidad de venta ($)",costoVenta));
        margen.setEditable(false);margen.setBackground(new Color(213,232,255));
        numeros.add(cell("Recargo sobre costo (%) · automático",margen));
        numeros.add(cell("IVA",iva));numeros.add(cell("Stock mínimo (unidad de venta)",minimo));
        factor.setText("1");
        JPanel conversion=grid(1);
        conversion.add(cell("CONTENIDO DE LA PRESENTACIÓN: ¿cuántas unidades de stock contiene? *",factor));
        factor.setFont(new Font("Segoe UI",Font.PLAIN,17));factor.setPreferredSize(new Dimension(220,44));
        factor.setToolTipText("Ejemplo: CAJA x8 = 8; BULTO x12 = 12; PALLET x480 = 480. No hay contenido estándar.");
        ayudaConversion.setForeground(NAVY);ayudaConversion.setFont(new Font("Segoe UI",Font.PLAIN,12));
        conversion.add(ayudaConversion);
        // La compra por pieza de peso variable queda reservada para una versión futura.
        // No se presenta en el Paso 2: por ahora se trabaja con peso total en KG.
        // Se conserva el campo y su lógica interna para no alterar otras operaciones.
        pesoVariable.setSelected(false);
        JLabel notaCosto=new JLabel("Costo opcional: si aún no compraste el artículo, dejalo vacío y se guardará en $0.");
        notaCosto.setFont(new Font("Segoe UI",Font.PLAIN,12));notaCosto.setForeground(BLUE);
        JPanel bloquePrecios=new JPanel(new BorderLayout(0,8));bloquePrecios.setOpaque(false);
        bloquePrecios.add(numeros,BorderLayout.CENTER);bloquePrecios.add(notaCosto,BorderLayout.SOUTH);
        section(precios,"03  PRECIOS DE COMPRA Y VENTA",bloquePrecios);
        section(precios,"CONVERSIÓN DE COMPRA A VENTA",conversion);
        ayudaMargen.setForeground(NAVY);ayudaMargen.setFont(new Font("Segoe UI",Font.PLAIN,12));
        section(precios,"CÓMO SE CALCULA LA GANANCIA",ayudaMargen);
        JLabel cero=new JLabel("  Stock inicial: 0  ·  Las cantidades se registran desde Nueva entrada de mercadería");
        cero.setFont(new Font("Segoe UI",Font.BOLD,13));cero.setForeground(NAVY);
        JPanel stockInfo=new JPanel(new BorderLayout());stockInfo.setBackground(new Color(221,237,255));
        stockInfo.setBorder(new EmptyBorder(16,10,16,10));stockInfo.add(cero);
        section(precios,"EXISTENCIAS",stockInfo);
        tabs.addTab("  PRECIOS Y UNIDADES  ",new SymbolIcon("box",NAVY,17),envolver(precios));

        JPanel config=pagina();
        // Paso 3: opciones explicadas en lenguaje cotidiano, sin modificar la lógica de guardado.
        JLabel introConfig=new JLabel("Para la mayoría de los artículos, dejá las opciones como están.");
        introConfig.setFont(new Font("Segoe UI",Font.PLAIN,14));introConfig.setForeground(NAVY);
        section(config,"03  ÚLTIMOS DETALLES",introConfig);

        JPanel tipoProducto=new JPanel(new BorderLayout(0,9));tipoProducto.setOpaque(false);
        JLabel tituloPeso=new JLabel("¿El producto se vende por peso?");
        tituloPeso.setFont(new Font("Segoe UI",Font.BOLD,16));tituloPeso.setForeground(NAVY);
        tipoProducto.add(tituloPeso,BorderLayout.NORTH);
        pesable.setText("SÍ, SE VENDE POR KILOGRAMOS (KG)");
        pesable.setFont(new Font("Segoe UI",Font.BOLD,14));
        pesable.setForeground(NAVY);pesable.setOpaque(false);
        pesable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel selectorPeso=new JPanel(new BorderLayout(0,8));selectorPeso.setOpaque(false);
        selectorPeso.add(pesable,BorderLayout.NORTH);
        JLabel explicacionPeso=new JLabel();
        explicacionPeso.setFont(new Font("Segoe UI",Font.PLAIN,13));
        explicacionPeso.setForeground(new Color(57,78,109));
        selectorPeso.add(explicacionPeso,BorderLayout.CENTER);
        JLabel ejemploPeso=new JLabel("Ejemplo: recibís 4,750 kg de queso y registrás 4,750 kg en Entrada de mercadería.");
        ejemploPeso.setFont(new Font("Segoe UI",Font.PLAIN,12));
        ejemploPeso.setForeground(BLUE);
        selectorPeso.add(ejemploPeso,BorderLayout.SOUTH);
        tipoProducto.add(selectorPeso,BorderLayout.CENTER);
        Runnable actualizarAyudaPeso=()->{
            explicacionPeso.setText(pesable.isSelected()
                    ? "Producto pesable activado. El peso total se carga al recibir la mercadería."
                    : "Para productos comunes (fideos, aceite, arroz, etc.), dejá esta casilla sin marcar.");
            ejemploPeso.setVisible(pesable.isSelected());
        };
        pesable.addItemListener(e->actualizarAyudaPeso.run());
        actualizarAyudaPeso.run();
        section(config,"TIPO DE PRODUCTO",tipoProducto);

        JPanel opciones=new JPanel(new GridLayout(0,2,18,13));opciones.setOpaque(false);
        for(JCheckBox c:new JCheckBox[]{activo,controla,descuento,sinStock}){
            c.setOpaque(false);c.setForeground(NAVY);c.setFont(new Font("Segoe UI",Font.BOLD,13));
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            opciones.add(c);
        }
        JLabel notaOpciones=new JLabel("Recomendado: artículo activo, control de stock habilitado y venta sin stock desactivada.");
        notaOpciones.setFont(new Font("Segoe UI",Font.PLAIN,12));notaOpciones.setForeground(new Color(57,78,109));
        JPanel panelOpciones=new JPanel(new BorderLayout(0,12));panelOpciones.setOpaque(false);
        panelOpciones.add(opciones,BorderLayout.CENTER);panelOpciones.add(notaOpciones,BorderLayout.SOUTH);
        section(config,"OTRAS OPCIONES (PODÉS DEJARLAS COMO ESTÁN)",panelOpciones);

        observaciones.setFont(new Font("Segoe UI",Font.PLAIN,13));
        observaciones.setLineWrap(true);observaciones.setWrapStyleWord(true);
        JScrollPane notas=new JScrollPane(observaciones);
        notas.setPreferredSize(new Dimension(300,76));
        section(config,"OBSERVACIONES (OPCIONAL)",notas);
        tabs.addTab("  CONFIGURACIÓN  ",new SymbolIcon("box",NAVY,17),envolver(config));
        // Barra de progreso independiente del Look and Feel de Swing.
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
            @Override protected int calculateTabAreaHeight(int placement,int runs,int maxTabHeight){return 0;}
            @Override protected void paintTabArea(Graphics g,int placement,int selectedIndex){}
        });
        JPanel centro=new JPanel(new BorderLayout());centro.setBackground(BG);
        JPanel pasos=new JPanel(new GridLayout(1,3,10,0));pasos.setBackground(new Color(246,249,253));
        pasos.setBorder(new EmptyBorder(12,18,12,18));
        String[] textos={"1  IDENTIFICÁ EL ARTÍCULO","2  PRECIOS Y UNIDADES","3  OPCIONES Y GUARDADO"};
        for(int i=0;i<3;i++){
            final int destino=i;
            JButton boton=button(textos[i],new Color(101,117,140));
            boton.setFont(new Font("Segoe UI",Font.BOLD,13));
            boton.setPreferredSize(new Dimension(170,50));
            boton.addActionListener(e->irAlPaso(destino));
            pasosVisuales[i]=boton;pasos.add(boton);
        }
        centro.add(pasos,BorderLayout.NORTH);centro.add(tabs,BorderLayout.CENTER);
        add(centro,BorderLayout.CENTER);
        tabs.addChangeListener(e->actualizarNavegacion());
        JPanel footer=new JPanel(new BorderLayout(12,0));footer.setBackground(Color.WHITE);
        footer.setBorder(new CompoundBorder(new MatteBorder(1,0,0,0,new Color(206,219,235)),new EmptyBorder(12,18,12,18)));
        JPanel izq=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));izq.setOpaque(false);
        JButton cancelar=button("CANCELAR",new Color(91,105,125));
        JButton limpiar=button("LIMPIAR CAMPOS",new Color(75,104,148));
        cancelar.addActionListener(e->dispose());
        limpiar.addActionListener(e->{if(JOptionPane.showConfirmDialog(this,"¿Limpiar los datos de este artículo?","Confirmar limpieza",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){limpiar();irAlPaso(0);}});
        izq.add(cancelar);izq.add(limpiar);footer.add(izq,BorderLayout.WEST);
        JPanel der=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));der.setOpaque(false);
        anteriorPaso=button("← ANTERIOR",new Color(75,104,148));
        siguientePaso=button("SIGUIENTE  →",BLUE);
        guardarFinal=button("GUARDAR Y CERRAR",GREEN);
        guardarYOtro=button("GUARDAR Y CARGAR OTRO",new Color(0,124,106));
        anteriorPaso.addActionListener(e->irAlPaso(asistente.getSelectedIndex()-1));
        siguientePaso.addActionListener(e->irAlPaso(asistente.getSelectedIndex()+1));
        guardarFinal.addActionListener(e->{guardarOtro=false;guardar();});
        guardarYOtro.addActionListener(e->{guardarOtro=true;guardar();});
        der.add(anteriorPaso);der.add(siguientePaso);der.add(guardarYOtro);der.add(guardarFinal);
        footer.add(der,BorderLayout.EAST);add(footer,BorderLayout.SOUTH);
        actualizarNavegacion();
        actualizarModoBarra();cargarCatalogos();rubro.addActionListener(e->{if(!cargando)cargarCategorias();});categoria.addActionListener(e->{if(!cargando)sincronizarRubroDeCategoria();});
        compra.addActionListener(e->actualizarCostos());venta.addActionListener(e->actualizarCostos());
        pesable.addActionListener(e->{if(!pesable.isSelected())pesoVariable.setSelected(false);actualizarCostos();});
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
    private void irAlPaso(int destino){
        if(destino<0||destino>=asistente.getTabCount())return;
        // Solo se valida el nombre y la categoría antes de pasar al segundo paso.
        if(asistente.getSelectedIndex()==0 && destino>0){
            if(nombre.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this,"Ingresá el nombre del artículo para continuar.","Falta un dato",JOptionPane.INFORMATION_MESSAGE);
                nombre.requestFocusInWindow();return;
            }
            if(id(categoria)==null){
                JOptionPane.showMessageDialog(this,"Seleccioná la categoría del artículo.","Falta un dato",JOptionPane.INFORMATION_MESSAGE);
                categoria.requestFocusInWindow();return;
            }
        }
        asistente.setSelectedIndex(destino);
        SwingUtilities.invokeLater(()->{
            if(destino==0)nombre.requestFocusInWindow();
            else if(destino==1)compra.requestFocusInWindow();
        });
    }
    private void actualizarNavegacion(){
        if(asistente==null||anteriorPaso==null)return;
        int actual=asistente.getSelectedIndex();
        anteriorPaso.setVisible(actual>0);
        siguientePaso.setVisible(actual<2);
        guardarFinal.setVisible(actual==2);
        guardarYOtro.setVisible(actual==2);
        for(int i=0;i<3;i++){
            pasosVisuales[i].putClientProperty("paso",i);
            pasosVisuales[i].setText((i<actual?"✓  ":(i+1)+"  ")+
                new String[]{"DATOS DEL ARTÍCULO","PRECIOS Y UNIDADES","CONFIGURACIÓN"}[i]);
            pasosVisuales[i].setForeground(Color.WHITE);
            pasosVisuales[i].setToolTipText(i==actual?"Paso actual":"Ir al paso "+(i+1));
            pasosVisuales[i].setBackground(i==actual?GREEN:(i<actual?BLUE:new Color(101,117,140)));
        }
        getRootPane().setDefaultButton(actual==2?guardarFinal:siguientePaso);
    }
    private static JTextField field(){JTextField f=new JTextField();f.setPreferredSize(new Dimension(150,38));f.setFont(new Font("Segoe UI",Font.PLAIN,13));f.setForeground(NAVY);f.setBackground(new Color(246,250,255));f.setBorder(new CompoundBorder(new LineBorder(new Color(156,181,214)),new EmptyBorder(4,10,4,10)));return f;}
    private static JComboBox<String> combo(){JComboBox<String> c=new JComboBox<>();c.setPreferredSize(new Dimension(200,44));c.setFont(new Font("Segoe UI",Font.PLAIN,15));
        c.setMaximumRowCount(14);c.setBackground(new Color(247,250,254));return c;}
    private static JButton button(String label,Color color){
        JButton b=new JButton(label){
            @Override protected void paintComponent(Graphics g){
                Graphics2D d=(Graphics2D)g.create();d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color c=isEnabled()?(getClientProperty("paso")!=null?getBackground():color):new Color(142,153,169);
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
    private static JPanel grid(int n){
        // GridBagLayout conserva la altura natural de los campos: GridLayout
        // estiraba nombre, ubicación y descripción hasta ~100 px.
        JPanel p=new JPanel(new GridBagLayout()){
            private int indice=0;
            @Override public Component add(Component c){
                GridBagConstraints g=new GridBagConstraints();
                g.gridx=indice%n;g.gridy=indice/n;
                g.weightx=1.0;g.fill=GridBagConstraints.HORIZONTAL;
                g.anchor=GridBagConstraints.NORTHWEST;
                g.insets=new Insets(indice>=n?12:0,0,0,(indice%n==n-1)?0:14);
                super.add(c,g);indice++;return c;
            }
        };
        p.setOpaque(false);return p;
    }
    private static JPanel cell(String label,JComponent component){return cell(new JLabel(label),component);}
    private static JPanel cell(JLabel l,JComponent component){JPanel p=new JPanel(new BorderLayout(0,5));p.setOpaque(false);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(NAVY);p.add(l,BorderLayout.NORTH);p.add(component,BorderLayout.CENTER);return p;}
    private static void section(JPanel root,String title,JComponent content){JPanel p=new JPanel(new BorderLayout(0,12));p.setBackground(new Color(247,250,255));p.setBorder(new CompoundBorder(new LineBorder(new Color(163,185,217)),new EmptyBorder(13,15,15,15)));JLabel h=new JLabel(title);h.setFont(new Font("Segoe UI",Font.BOLD,16));h.setForeground(NAVY);p.add(h,BorderLayout.NORTH);p.add(content);p.setAlignmentX(Component.LEFT_ALIGNMENT);p.setMaximumSize(new Dimension(Integer.MAX_VALUE,p.getPreferredSize().height));root.add(p);root.add(Box.createVerticalStrut(10));}
    private void cargarCombo(Connection cn,JComboBox<String> cb,String sql)throws SQLException{cb.removeAllItems();cb.addItem("Seleccione...");Map<String,Integer> m=new HashMap<>();try(PreparedStatement ps=cn.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){String n=rs.getString(2);cb.addItem(n);m.put(n,rs.getInt(1));}}ids.put(cb,m);}
    private Integer id(JComboBox<String> cb){Map<String,Integer> m=ids.get(cb);return m==null?null:m.get((String)cb.getSelectedItem());}
    private void cargarCatalogos(){cargando=true;try(Connection cn=conexion.getConexion()){cargarCombo(cn,marca,"SELECT id_marca,nombre FROM marca WHERE activo=1 ORDER BY nombre");cargarCombo(cn,rubro,"SELECT id_rubro,nombre FROM rubro WHERE activo=1 ORDER BY nombre");cargarCombo(cn,compra,"SELECT id_unidad,CONCAT(nombre,' (',codigo,')') FROM unidad_medida WHERE activo=1 ORDER BY nombre");cargarCombo(cn,venta,"SELECT id_unidad,CONCAT(nombre,' (',codigo,')') FROM unidad_medida WHERE activo=1 ORDER BY nombre");cargarCombo(cn,iva,"SELECT id_iva,CONCAT(nombre,' (',porcentaje,'%)') FROM tipo_iva WHERE activo=1 ORDER BY porcentaje");}catch(SQLException e){error("Error cargando catálogos",e);}finally{cargando=false;}cargarCategorias();}
    private void cargarCategorias(){
        Integer seleccionAnterior=id(categoria);
        cargando=true;
        try(Connection cn=conexion.getConexion()){
            Integer r=id(rubro);
            String sql="SELECT id_categoria,nombre FROM categoria WHERE activo=1"+(r==null?"":" AND id_rubro=?")+" ORDER BY nombre";
            categoria.removeAllItems();categoria.addItem("Seleccione...");
            Map<String,Integer> m=new HashMap<>();
            String nombreRestaurado=null;
            try(PreparedStatement ps=cn.prepareStatement(sql)){
                if(r!=null)ps.setInt(1,r);
                try(ResultSet rs=ps.executeQuery()){
                    while(rs.next()){
                        int idCategoria=rs.getInt(1);String n=rs.getString(2);
                        categoria.addItem(n);m.put(n,idCategoria);
                        if(seleccionAnterior!=null && seleccionAnterior==idCategoria)nombreRestaurado=n;
                    }
                }
            }
            ids.put(categoria,m);
            if(nombreRestaurado!=null)categoria.setSelectedItem(nombreRestaurado);
        }catch(SQLException e){error("Error cargando categorías",e);}
        finally{cargando=false;}
        previsualizar();
    }
    // Si el usuario elige primero una categoría, seleccionamos su rubro automáticamente.
    // Así no se borra la categoría ni el código al completar el rubro después.
    private void sincronizarRubroDeCategoria(){
        Integer cat=id(categoria);
        if(cat==null){previsualizar();return;}
        try(Connection cn=conexion.getConexion();
            PreparedStatement ps=cn.prepareStatement("SELECT id_rubro FROM categoria WHERE id_categoria=?")){
            ps.setInt(1,cat);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    int rubroCategoria=rs.getInt(1);
                    Integer rubroActual=id(rubro);
                    if(rubroActual==null || rubroActual!=rubroCategoria){
                        Map<String,Integer> rubros=ids.get(rubro);
                        if(rubros!=null){
                            for(Map.Entry<String,Integer> entry:rubros.entrySet()){
                                if(entry.getValue()!=null && entry.getValue()==rubroCategoria){
                                    cargando=true;
                                    try{rubro.setSelectedItem(entry.getKey());}
                                    finally{cargando=false;}
                                    cargarCategorias();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }catch(SQLException e){error("No se pudo identificar el rubro de la categoría",e);}
        previsualizar();
    }
    private void buscarMarca(){
        JDialog dlg=new JDialog(this,"Buscar marca · Serena Soft",true);
        dlg.setLayout(new BorderLayout(10,10));dlg.getContentPane().setBackground(Color.WHITE);
        JPanel superior=new JPanel(new BorderLayout(8,5));superior.setBackground(Color.WHITE);
        superior.setBorder(new EmptyBorder(14,16,8,16));
        JLabel titulo=new JLabel("SELECCIONAR MARCA");titulo.setForeground(NAVY);
        titulo.setFont(new Font("Segoe UI",Font.BOLD,17));superior.add(titulo,BorderLayout.NORTH);
        JTextField filtro=field();filtro.setToolTipText("Escribí el nombre o parte de la marca");
        superior.add(filtro,BorderLayout.CENTER);dlg.add(superior,BorderLayout.NORTH);
        DefaultListModel<String> modelo=new DefaultListModel<>();JList<String> lista=new JList<>(modelo);
        lista.setFont(new Font("Segoe UI",Font.PLAIN,14));lista.setFixedCellHeight(34);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll=new JScrollPane(lista);scroll.setBorder(new EmptyBorder(0,16,0,16));dlg.add(scroll,BorderLayout.CENTER);
        java.util.List<String> marcas=new ArrayList<>();
        for(int i=1;i<marca.getItemCount();i++)marcas.add(marca.getItemAt(i));
        Runnable actualizar=()->{modelo.clear();String q=Normalizer.normalize(filtro.getText().trim(),Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(Locale.ROOT);
            for(String n:marcas){String normal=Normalizer.normalize(n,Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(Locale.ROOT);
                if(normal.contains(q))modelo.addElement(n);}
            if(!modelo.isEmpty())lista.setSelectedIndex(0);
        };actualizar.run();
        filtro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){actualizar.run();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){actualizar.run();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){actualizar.run();}
        });
        Runnable elegir=()->{String n=lista.getSelectedValue();if(n!=null){marca.setSelectedItem(n);dlg.dispose();}};
        lista.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)elegir.run();}});
        JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));pie.setBackground(Color.WHITE);
        JButton cancelar=button("CANCELAR",new Color(75,82,95)),seleccionar=button("SELECCIONAR",BLUE);
        cancelar.addActionListener(e->dlg.dispose());seleccionar.addActionListener(e->elegir.run());
        pie.add(cancelar);pie.add(seleccionar);dlg.add(pie,BorderLayout.SOUTH);
        dlg.getRootPane().setDefaultButton(seleccionar);dlg.setSize(530,490);dlg.setLocationRelativeTo(this);
        SwingUtilities.invokeLater(()->filtro.requestFocusInWindow());dlg.setVisible(true);
    }
    private void nuevaCategoria(){
        Integer idRubro=id(rubro);
        if(idRubro==null){JOptionPane.showMessageDialog(this,"Seleccioná primero el rubro de la nueva categoría.");return;}
        JTextField nombreCat=field();JPanel panel=grid(1);panel.add(cell("Nombre de categoría *",nombreCat));
        if(JOptionPane.showConfirmDialog(this,panel,"Nueva categoría",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        String nombreNuevo=nombreCat.getText().trim();if(nombreNuevo.isEmpty()){JOptionPane.showMessageDialog(this,"Ingresá un nombre.");return;}
        try(Connection cn=conexion.getConexion()){
            try(PreparedStatement check=cn.prepareStatement("SELECT id_categoria FROM categoria WHERE id_rubro=? AND LOWER(nombre)=LOWER(?)")){
                check.setInt(1,idRubro);check.setString(2,nombreNuevo);
                try(ResultSet rs=check.executeQuery()){if(rs.next()){JOptionPane.showMessageDialog(this,"La categoría ya existe en este rubro.");return;}}
            }
            try(PreparedStatement ps=cn.prepareStatement("INSERT INTO categoria(id_rubro,nombre,activo) VALUES(?,?,1)")){
                ps.setInt(1,idRubro);ps.setString(2,nombreNuevo);ps.executeUpdate();
            }
            cargarCategorias();categoria.setSelectedItem(nombreNuevo);
        }catch(SQLException e){error("No se pudo crear la categoría",e);}
    }
    private void nuevaMarca(){JTextField n=field(),d=field();JPanel p=grid(1);p.add(cell("Nombre de la marca *",n));p.add(cell("Descripción opcional",d));if(JOptionPane.showConfirmDialog(this,p,"Nueva marca",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;if(n.getText().isBlank()){JOptionPane.showMessageDialog(this,"Ingresá una marca.");return;}try(Connection cn=conexion.getConexion()){try(PreparedStatement ps=cn.prepareStatement("INSERT INTO marca(nombre,descripcion,activo) VALUES(?,?,1)")){ps.setString(1,n.getText().trim());ps.setString(2,d.getText().trim());ps.executeUpdate();}cargarCombo(cn,marca,"SELECT id_marca,nombre FROM marca WHERE activo=1 ORDER BY nombre");marca.setSelectedItem(n.getText().trim());}catch(SQLException e){error("No se pudo crear la marca; comprobá que no exista",e);}}
    private void buscarProveedor(){DialogoBuscarProveedor d=new DialogoBuscarProveedor(this);d.setVisible(true);if(d.isSeleccionado()){idProveedor=d.getIdSeleccionado();proveedor.setText(d.getRazonSocialSeleccionada());}}
    private static String prefijo(String nombre){String s=Normalizer.normalize(nombre,Normalizer.Form.NFD).replaceAll("\\p{M}","").toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]","");return s.substring(0,Math.min(3,s.length()));}
    private static int siguiente(Connection cn,String pref)throws SQLException{int max=0;try(PreparedStatement ps=cn.prepareStatement("SELECT codigo FROM producto WHERE codigo LIKE ?")){ps.setString(1,pref+"-%");try(ResultSet rs=ps.executeQuery()){while(rs.next()){String c=rs.getString(1);if(c!=null&&c.matches(java.util.regex.Pattern.quote(pref)+"-[0-9]+")){try{max=Math.max(max,Integer.parseInt(c.substring(pref.length()+1)));}catch(NumberFormatException ignored){}}}}}return max+1;}
    private void actualizarModoBarra(){
        int modo=modoBarra.getSelectedIndex();
        imprimirEtiqueta.setVisible(modo==1);
        if(modo!=1)imprimirEtiqueta.setSelected(false);
        barra.setEditable(modo==0);barra.setFocusable(modo==0);
        if(modo==1){barra.setText(codigo.getText().startsWith("Seleccion")?"":codigo.getText());
            ayudaBarra.setText("Se guardará el código definitivo; imprimí una etiqueta Code 128.");}
        else if(modo==2){barra.setText("");ayudaBarra.setText("Podrás completar el código de barras más adelante.");}
        else{if(barra.getText().equals(codigo.getText()))barra.setText("");
            ayudaBarra.setText("Escaneá o escribí el código real del envase.");}
        barra.setBackground(modo==0?Color.WHITE:new Color(235,241,248));
    }
    private static void validarCodigoEscaneable(Connection cn,String valor,String codigoNuevo)throws SQLException{
        if(valor==null||valor.isBlank())return;
        if(valor.length()>100)throw new SQLException("El código de barras supera el máximo de 100 caracteres.");
        // Impide colisiones entre los identificadores que reconoce el lector de Ventas.
        String sql="SELECT id_producto,nombre FROM producto WHERE codigo_barra=? OR codigo=? LIMIT 1";
        try(PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setString(1,valor);ps.setString(2,valor);
            try(ResultSet rs=ps.executeQuery()){if(rs.next())throw new SQLException("El código escaneable ya pertenece a: "+rs.getString(2));}
        }
        if(!valor.equals(codigoNuevo)){
            try(PreparedStatement ps=cn.prepareStatement("SELECT id_producto,nombre FROM producto WHERE codigo=? OR codigo_barra=? LIMIT 1")){
                ps.setString(1,valor);ps.setString(2,valor);
                try(ResultSet rs=ps.executeQuery()){if(rs.next())throw new SQLException("Código duplicado: "+rs.getString(2));}
            }
        }
    }
    private void previsualizar(){Integer cat=id(categoria);if(cat==null){codigo.setText("Seleccioná categoría");aviso.setText("El código se calcula por categoría");return;}String pref=prefijo((String)categoria.getSelectedItem());try(Connection cn=conexion.getConexion()){codigo.setText(pref+"-"+String.format("%03d",siguiente(cn,pref)));aviso.setText("Vista previa · el código definitivo se valida al guardar");if(modoBarra.getSelectedIndex()==1)barra.setText(codigo.getText());}catch(SQLException e){codigo.setText(pref+"-???");aviso.setText("No se pudo consultar el correlativo");}}
    private void actualizarCostos(){
        String uCompra=compra.getSelectedItem()==null?"unidad de compra":compra.getSelectedItem().toString();
        String uVenta=venta.getSelectedItem()==null?"unidad de venta":venta.getSelectedItem().toString();
        unidadCosto.setText("Precio de compra por " + uCompra + " ($)");
        unidadPrecio.setText("Precio de venta por " + uVenta + " ($) *");
        if(pesoVariable.isSelected()){
            ayudaConversion.setText("Peso variable: al recibir cada pieza se registra su peso real en Nueva entrada.");
        }else{
            ayudaConversion.setText("Ej.: caja de 12 unidades → factor 12; KG a KG → factor 1.");
        }
        try{
            BigDecimal f=decimal(factor.getText()), c=decimal(costo.getText()), v=decimal(precio.getText());
            if(f.signum()<=0){costoVenta.setText("Factor inválido");margen.setText("");return;}
            BigDecimal cv=c.divide(f,2,RoundingMode.HALF_UP);
            costoVenta.setText(c.signum()==0?"Costo pendiente":cv.toPlainString());
            margen.setText(c.signum()>0 && cv.signum()>0?v.subtract(cv).multiply(new BigDecimal("100")).divide(cv,2,RoundingMode.HALF_UP).toPlainString():"Pendiente de costo");
        }catch(Exception e){costoVenta.setText("");margen.setText("");}
    }
    private static BigDecimal decimal(String value)throws Exception{String t=value.trim().replace("$","").replace(" ","");if(t.isEmpty())return BigDecimal.ZERO;if(t.contains(",")&&t.contains(".")){if(t.lastIndexOf(',')>t.lastIndexOf('.'))t=t.replace(".","").replace(',','.');else t=t.replace(",","");}else if(t.contains(","))t=t.replace(',','.');try{return new BigDecimal(t);}catch(NumberFormatException e){throw new Exception("Número inválido: "+value);}}
    private static void text(PreparedStatement ps,int n,String s)throws SQLException{if(s==null||s.trim().isEmpty())ps.setNull(n,Types.VARCHAR);else ps.setString(n,s.trim());}
    private static void integer(PreparedStatement ps,int n,Integer i)throws SQLException{if(i==null)ps.setNull(n,Types.INTEGER);else ps.setInt(n,i);}
    private void guardar(){if(nombre.getText().isBlank()){JOptionPane.showMessageDialog(this,"Ingresá el nombre del producto.");return;}Integer cat=id(categoria),uc=id(compra),uv=id(venta);if(cat==null||uc==null||uv==null){JOptionPane.showMessageDialog(this,"Seleccioná categoría, unidad de compra y unidad de venta.");return;}try{BigDecimal pc=decimal(costo.getText()),pv=decimal(precio.getText()),pm=decimal(minimo.getText()),fc=decimal(factor.getText());
        // El margen es informativo y se calcula con números, nunca se interpreta su texto.
        BigDecimal costoPorUnidad = pc.divide(fc.signum()>0?fc:BigDecimal.ONE, 2, RoundingMode.HALF_UP);
        BigDecimal mg = pc.signum()>0 && costoPorUnidad.signum()>0
                ? pv.subtract(costoPorUnidad).multiply(new BigDecimal("100"))
                    .divide(costoPorUnidad,2,RoundingMode.HALF_UP)
                : BigDecimal.ZERO;if(pc.signum()<0||pv.signum()<0||pm.signum()<0)throw new Exception("Los precios y el stock mínimo no pueden ser negativos.");if(fc.signum()<=0)throw new Exception("El factor de conversión debe ser mayor que cero.");// Alta de catálogo permitida sin precio; no significa que pueda venderse a $0.
        if(pv.signum()==0){
            int r=JOptionPane.showConfirmDialog(this,
                "Este artículo se guardará SIN PRECIO DE VENTA ($0,00).\n"
                + "Deberás asignarle un precio antes de venderlo.\n\n¿Continuar?",
                "Artículo pendiente de precio",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(r!=JOptionPane.YES_OPTION)return;
        }if(fc.stripTrailingZeros().scale()>4)throw new Exception("El factor admite como máximo 4 decimales.");if(pc.scale()>2||pv.scale()>2)throw new Exception("Los precios admiten como máximo 2 decimales.");if(pm.stripTrailingZeros().scale()>3)throw new Exception("El stock mínimo admite como máximo 3 decimales.");
        // Una presentación de embalaje no debe crearse con factor 1 por descuido.
        String unidadCompraTexto = String.valueOf(compra.getSelectedItem()).toUpperCase(java.util.Locale.ROOT);
        boolean embalaje = unidadCompraTexto.contains("CAJA") || unidadCompraTexto.contains("PACK")
                || unidadCompraTexto.contains("BULTO") || unidadCompraTexto.contains("DISPLAY")
                || unidadCompraTexto.contains("PALLET") || unidadCompraTexto.contains("BANDEJA") || unidadCompraTexto.contains("BOLSA");
        if (embalaje && fc.compareTo(BigDecimal.ONE)==0 && !pesoVariable.isSelected()) {
            throw new Exception("La presentación " + compra.getSelectedItem()
                    + " tiene contenido 1. Indicá cuántas unidades trae antes de guardar. "
                    + "Si comprás unidades sueltas, seleccioná UNIDAD.");
        }
        if(!uc.equals(uv) && fc.compareTo(BigDecimal.ONE)==0 && !pesoVariable.isSelected()){
            int respuesta=JOptionPane.showConfirmDialog(this,
                "Seleccionaste unidades de compra y venta diferentes, pero el factor es 1.\n"
                + "Eso significa que cada unidad de compra contiene exactamente 1 unidad de venta.\n\n"
                + "Por ejemplo, una CAJA de 12 unidades debe tener factor 12.\n"
                + "¿Confirmás que el factor 1 es correcto?",
                "Verificar conversión de unidades",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(respuesta!=JOptionPane.YES_OPTION)return;
        }
        if(!String.valueOf(venta.getSelectedItem()).toUpperCase(java.util.Locale.ROOT).contains("KG") && fc.stripTrailingZeros().scale()>0)throw new Exception("El stock por unidades no admite fracciones en el contenido de la presentación.");
        if(pesoVariable.isSelected() && !pesable.isSelected())throw new Exception("Para compras de peso variable, marcá Pesable en Configuración.");if(pc.signum()>0 && pv.signum()>0 && pv.compareTo(pc.divide(fc,2,RoundingMode.HALF_UP))<0 && JOptionPane.showConfirmDialog(this,"El precio de venta es inferior al costo por unidad de venta. ¿Deseás continuar?","Advertencia de precio",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)!=JOptionPane.YES_OPTION)return;try(Connection cn=conexion.getConexion()){cn.setAutoCommit(false);try{String catNombre;try(PreparedStatement ps=cn.prepareStatement("SELECT nombre FROM categoria WHERE id_categoria=? FOR UPDATE")){ps.setInt(1,cat);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("Categoría inexistente");catNombre=rs.getString(1);}}String code=prefijo(catNombre)+"-"+String.format("%03d",siguiente(cn,prefijo(catNombre)));
                    String valorBarra=modoBarra.getSelectedIndex()==1?code:(modoBarra.getSelectedIndex()==2?null:barra.getText().trim());
                    if(modoBarra.getSelectedIndex()==0 && valorBarra.isBlank())throw new SQLException("Escaneá el código del fabricante o elegí 'Sin código por ahora'.");
                    // El nuevo código interno tampoco puede coincidir con un barcode previo.
                    try(PreparedStatement chk=cn.prepareStatement("SELECT nombre FROM producto WHERE codigo_barra=? LIMIT 1")){
                        chk.setString(1,code);
                        try(ResultSet r=chk.executeQuery()){if(r.next())throw new SQLException("El código automático coincide con una etiqueta existente: "+r.getString(1));}
                    }
                    validarCodigoEscaneable(cn,valorBarra,code);
                    String sql="INSERT INTO producto(codigo,codigo_interno,codigo_barra,nombre,descripcion,ubicacion,id_rubro,id_categoria,id_marca,id_unidad_compra,id_unidad_venta,factor_conversion,precio_compra,precio_venta,margen_porcentaje,id_iva,stock_minimo,controla_stock,permite_venta_sin_stock,es_pesable,permite_descuento,activo,observaciones) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";int productoId;try(PreparedStatement ps=cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,code);text(ps,2,interno.getText());text(ps,3,valorBarra);ps.setString(4,nombre.getText().trim());text(ps,5,descripcion.getText());text(ps,6,ubicacion.getText());integer(ps,7,id(rubro));ps.setInt(8,cat);integer(ps,9,id(marca));ps.setInt(10,uc);ps.setInt(11,uv);ps.setBigDecimal(12,fc);ps.setBigDecimal(13,costoPorUnidad);ps.setBigDecimal(14,pv);ps.setBigDecimal(15,pc.signum()==0?BigDecimal.ZERO:mg);integer(ps,16,id(iva));ps.setBigDecimal(17,pm);ps.setBoolean(18,controla.isSelected());ps.setBoolean(19,sinStock.isSelected());ps.setBoolean(20,pesable.isSelected());ps.setBoolean(21,descuento.isSelected());ps.setBoolean(22,activo.isSelected());text(ps,23,observaciones.getText());ps.executeUpdate();try(ResultSet rs=ps.getGeneratedKeys()){if(!rs.next())throw new SQLException("No se obtuvo ID del producto");productoId=rs.getInt(1);}}if(idProveedor>0){try(PreparedStatement ps=cn.prepareStatement("INSERT INTO producto_proveedor(id_producto,id_proveedor,costo_ultimo,proveedor_principal) VALUES(?,?,?,1)")){ps.setInt(1,productoId);ps.setInt(2,idProveedor);ps.setBigDecimal(3,pc);ps.executeUpdate();}}
            // La presentación inicial debe guardarse en la MISMA transacción que el producto.
            String nombreUnidadCompra;
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT codigo FROM unidad_medida WHERE id_unidad=?")) {
                ps.setInt(1, uc);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("No existe la unidad de compra seleccionada.");
                    nombreUnidadCompra = rs.getString(1);
                }
            }
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO producto_presentacion " +
                    "(id_producto,nombre,id_unidad_compra,factor_conversion,es_predeterminada,activo) " +
                    "VALUES (?,?,?,?,1,1)")) {
                ps.setInt(1, productoId);
                ps.setString(2, "Original - " + nombreUnidadCompra);
                ps.setInt(3, uc);
                ps.setBigDecimal(4, fc);
                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo crear la presentación inicial del producto.");
                }
            }
            cn.commit();guardado=true;codigo.setText(code);
            boolean imprimirAhora=modoBarra.getSelectedIndex()==1 && imprimirEtiqueta.isSelected();
            mostrarExito(code);
            if(imprimirAhora)imprimirEtiquetaCode128(code,nombre.getText().trim());if(JOptionPane.showConfirmDialog(this,"¿Querés configurar ahora packs, cajas o bultos para este artículo?\nTambién podés agregarlos más adelante desde Entrada de mercadería.","Presentaciones",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){DialogoPresentacionesProducto dlg=new DialogoPresentacionesProducto(this,productoId,nombre.getText().trim(),uv,obtenerCodigoUnidad(uv));dlg.setVisible(true);}if(guardarOtro){
                limpiar();
            }else{dispose();}
            }catch(Exception e){cn.rollback();throw e;}finally{cn.setAutoCommit(true);}}}catch(Exception e){error("No se pudo guardar el producto",e);}}

    private String obtenerCodigoUnidad(int idUnidad) throws SQLException {
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("SELECT codigo FROM unidad_medida WHERE id_unidad=?")){
            ps.setInt(1,idUnidad);
            try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getString(1):"UN";}
        }
    }


    // Code 128-B: ASCII imprimible (32-126), checksum módulo 103 y patrón STOP.
    // Se genera después de guardar para imprimir el identificador definitivo.
    private static final String[] CODE128_PATRONES={
            "212222",
            "222122",
            "222221",
            "121223",
            "121322",
            "131222",
            "122213",
            "122312",
            "132212",
            "221213",
            "221312",
            "231212",
            "112232",
            "122132",
            "122231",
            "113222",
            "123122",
            "123221",
            "223211",
            "221132",
            "221231",
            "213212",
            "223112",
            "312131",
            "311222",
            "321122",
            "321221",
            "312212",
            "322112",
            "322211",
            "212123",
            "212321",
            "232121",
            "111323",
            "131123",
            "131321",
            "112313",
            "132113",
            "132311",
            "211313",
            "231113",
            "231311",
            "112133",
            "112331",
            "132131",
            "113123",
            "113321",
            "133121",
            "313121",
            "211331",
            "231131",
            "213113",
            "213311",
            "213131",
            "311123",
            "311321",
            "331121",
            "312113",
            "312311",
            "332111",
            "314111",
            "221411",
            "431111",
            "111224",
            "111422",
            "121124",
            "121421",
            "141122",
            "141221",
            "112214",
            "112412",
            "122114",
            "122411",
            "142112",
            "142211",
            "241211",
            "221114",
            "413111",
            "241112",
            "134111",
            "111242",
            "121142",
            "121241",
            "114212",
            "124112",
            "124211",
            "411212",
            "421112",
            "421211",
            "212141",
            "214121",
            "412121",
            "111143",
            "111341",
            "131141",
            "114113",
            "114311",
            "411113",
            "411311",
            "113141",
            "114131",
            "311141",
            "411131",
            "211412",
            "211214",
            "211232",
            "2331112"
    };

    private static String codificarCode128B(String valor){
        if(valor==null||valor.isEmpty())throw new IllegalArgumentException("Código vacío.");
        int suma=104;
        StringBuilder barras=new StringBuilder(CODE128_PATRONES[104]);
        for(int i=0;i<valor.length();i++){
            char c=valor.charAt(i);
            if(c<32||c>126)throw new IllegalArgumentException("Code 128-B admite caracteres ASCII imprimibles.");
            int indice=c-32;
            barras.append(CODE128_PATRONES[indice]);
            suma+=(i+1)*indice;
        }
        barras.append(CODE128_PATRONES[suma%103]);
        barras.append(CODE128_PATRONES[106]);
        return barras.toString();
    }

    private void imprimirEtiquetaCode128(String valor,String articulo){
        final String patron;
        try{patron=codificarCode128B(valor);}
        catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Etiqueta Code 128",JOptionPane.ERROR_MESSAGE);
            return;
        }
        PrinterJob trabajo=PrinterJob.getPrinterJob();
        trabajo.setJobName("Serena Soft - Etiqueta " + valor);
        trabajo.setPrintable((Graphics graphics,PageFormat pagina,int numero)->{
            if(numero>0)return Printable.NO_SUCH_PAGE;
            Graphics2D g=(Graphics2D)graphics.create();
            try{
                g.translate(pagina.getImageableX(),pagina.getImageableY());
                g.setColor(Color.WHITE);
                double ancho=Math.min(210,pagina.getImageableWidth()-8);
                if(ancho<80)return Printable.NO_SUCH_PAGE;
                g.fillRect(0,0,(int)ancho,115);
                g.setColor(Color.BLACK);
                g.setFont(new Font("SansSerif",Font.BOLD,10));
                String titulo=articulo.length()>32?articulo.substring(0,29)+"...":articulo;
                g.drawString(titulo,6,15);
                int modulos=20; // zona silenciosa: 10 módulos por lado
                for(int i=0;i<patron.length();i++)modulos+=patron.charAt(i)-'0';
                double unidad=(ancho-12)/modulos;
                double x=6+10*unidad;
                for(int i=0;i<patron.length();i++){
                    double w=(patron.charAt(i)-'0')*unidad;
                    if(i%2==0)g.fill(new java.awt.geom.Rectangle2D.Double(x,23,w,54));
                    x+=w;
                }
                g.setFont(new Font("Monospaced",Font.BOLD,12));
                FontMetrics fm=g.getFontMetrics();
                g.drawString(valor,(int)((ancho-fm.stringWidth(valor))/2),94);
                return Printable.PAGE_EXISTS;
            }finally{g.dispose();}
        });
        if(!trabajo.printDialog())return;
        try{trabajo.print();}
        catch(java.awt.print.PrinterException ex){
            JOptionPane.showMessageDialog(this,"No se pudo imprimir la etiqueta: "+ex.getMessage(),
                    "Serena Soft",JOptionPane.ERROR_MESSAGE);
        }
    }

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
        if(modoBarra.getSelectedIndex()==1){
            JButton imprimir=button("IMPRIMIR CODE 128",BLUE);
            imprimir.addActionListener(e->imprimirEtiquetaCode128(code,nombre.getText().trim()));
            foot.add(imprimir);
        }
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
    /** Deja el asistente listo para un alta nueva sin tocar los artículos guardados. */
    private void limpiar(){
        cargando=true;
        try {
            for(JTextField f:new JTextField[]{nombre,descripcion,barra,interno,ubicacion,proveedor,
                    costo,precio,margen,minimo,factor,costoVenta,codigo})f.setText("");
            observaciones.setText("");
            factor.setText("1");
            idProveedor=-1;
            activo.setSelected(true);
            controla.setSelected(true);
            pesable.setSelected(false);
            descuento.setSelected(false);
            sinStock.setSelected(false);
            pesoVariable.setSelected(false);
            imprimirEtiqueta.setSelected(false);
            modoBarra.setSelectedIndex(0);
            for(JComboBox<String> c:new JComboBox[]{marca,rubro,categoria,compra,venta,iva}){
                if(c.getItemCount()>0)c.setSelectedIndex(0);
            }
        } finally {
            cargando=false;
        }
        cargarCategorias();
        actualizarModoBarra();
        actualizarCostos();
        previsualizar();
        asistente.setSelectedIndex(0);
        // El desplazamiento anterior no debe quedar a mitad de la primera pantalla.
        SwingUtilities.invokeLater(()->{
            Component vista=asistente.getComponentAt(0);
            if(vista instanceof JScrollPane){
                ((JScrollPane)vista).getVerticalScrollBar().setValue(0);
            }
            nombre.requestFocusInWindow();
        });
    }
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
