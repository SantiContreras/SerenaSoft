package Paneles;

import model.Cliente;
import services.ClienteService;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

/** Serena Soft - Clientes. Compatible con Cliente/ClienteService existentes. */
public class PanelClientes extends JPanel {
    private static final Color NAVY=new Color(15,49,107), BLUE=new Color(17,94,184), BG=new Color(239,244,250), INK=new Color(19,43,80), GREEN=new Color(18,153,82), RED=new Color(210,58,61);
    private static final Font FONT=new Font("Segoe UI",Font.PLAIN,13);
    private static final Font BUTTON_FONT=new Font("Segoe UI Semibold",Font.BOLD,12);
    private final ClienteService service=new ClienteService();
    private final JTextField buscar=new ModernSearchField("Nombre, documento o teléfono...");
    private final JComboBox<String> estado=new JComboBox<>(new String[]{"Todos","Activos","Inactivos"});
    private final JComboBox<String> tipo=new JComboBox<>(new String[]{"Todos","MINORISTA","MAYORISTA"});
    private final JComboBox<String> iva=new JComboBox<>(new String[]{"Todas","CONSUMIDOR FINAL","RESPONSABLE INSCRIPTO","MONOTRIBUTISTA","EXENTO","NO RESPONSABLE"});
    private final JLabel total=new JLabel("0"),activos=new JLabel("0"),inactivos=new JLabel("0"),corriente=new JLabel("0"),resultados=new JLabel("0 clientes");
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Documento","Razón social / Nombre","Tipo","Cond. IVA","Teléfono","Localidad","Estado"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(model);
    private List<Cliente> visibles=new ArrayList<>();
    private int pagina=0, porPagina=20;
    private final JLabel paginaLabel=new JLabel("1 / 1");
    private final JComboBox<Integer> selectorPaginas=new JComboBox<>(new Integer[]{10,20,50,100});
    private List<Cliente> todos=new ArrayList<>();
    private final JButton modificar=button("MODIFICAR",new Color(0,105,225),"edit"), detalle=button("VER DETALLE",new Color(77,108,141),"eye"), cambiar=button("DESACTIVAR",new Color(239,59,65),"ban");

    public PanelClientes(){
        setLayout(new BorderLayout(0,10));setBackground(BG);setBorder(new EmptyBorder(10,12,10,12));
        add(header(),BorderLayout.NORTH);
        JPanel body=new JPanel(new BorderLayout(14,0));body.setOpaque(false);
        JPanel side=new JPanel(new BorderLayout(0,9));side.setOpaque(false);side.setPreferredSize(new Dimension(286,100));
        JPanel sideContent=new JPanel();sideContent.setOpaque(false);sideContent.setLayout(new BoxLayout(sideContent,BoxLayout.Y_AXIS));
        JPanel filterCard=filtros(),summaryCard=resumen();
        filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,365));
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,216));
        sideContent.add(filterCard);sideContent.add(Box.createVerticalStrut(9));sideContent.add(summaryCard);
        JScrollPane sideScroll=new JScrollPane(sideContent,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sideScroll.setBorder(null);sideScroll.getVerticalScrollBar().setUnitIncrement(14);sideScroll.getViewport().setBackground(BG);
        side.add(sideScroll,BorderLayout.CENTER);
        body.add(side,BorderLayout.WEST);body.add(tablaPanel(),BorderLayout.CENTER);add(body,BorderLayout.CENTER);
        tabla.getSelectionModel().addListSelectionListener(e->actualizarAcciones());
        tabla.addMouseListener(new MouseAdapter(){@Override public void mouseClicked(MouseEvent e){if(e.getClickCount()==2)abrir(false);}});
        buscar.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){filtrar();}public void removeUpdate(DocumentEvent e){filtrar();}public void changedUpdate(DocumentEvent e){filtrar();}});
        estado.addActionListener(e->filtrar());tipo.addActionListener(e->filtrar());iva.addActionListener(e->filtrar());
        actualizarAcciones();cargar();
    }
    private JPanel header(){
        JPanel p=card(new BorderLayout(8,0));
        p.setBorder(new CompoundBorder(new LineBorder(new Color(215,226,240)),new EmptyBorder(9,12,9,12)));
        JLabel title=new JLabel("CLIENTES");title.setFont(new Font("Segoe UI",Font.BOLD,23));title.setForeground(NAVY);
        JLabel sub=new JLabel("Gestión de clientes para ventas y cuenta corriente");sub.setFont(new Font("Segoe UI",Font.PLAIN,11));sub.setForeground(INK);
        JPanel txt=new JPanel(new GridLayout(2,1,0,1));txt.setOpaque(false);txt.add(title);txt.add(sub);
        JPanel brand=new JPanel(new BorderLayout(12,0));brand.setOpaque(false);
        JLabel icon=new JLabel(resourceIcon("/img/clientes2.png",42,42,new GlyphIcon("users",new Color(10,53,116),42)));brand.add(icon,BorderLayout.WEST);brand.add(txt,BorderLayout.CENTER);
        p.add(brand,BorderLayout.WEST);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT,7,3));actions.setOpaque(false);
        JButton nuevo=button("NUEVO",new Color(0,172,91),"plus"), actualizar=button("ACTUALIZAR",Color.WHITE,"refresh"), imprimir=button("IMPRIMIR",Color.WHITE,"print");
        nuevo.setIcon(resourceIcon("/img/nuevo_cliente.png",20,20,new GlyphIcon("plus",Color.WHITE,19)));
        detalle.setIcon(resourceIcon("/img/ver_detalle_cliente.png",20,20,new GlyphIcon("eye",Color.WHITE,19)));
        cambiar.setIcon(resourceIcon("/img/desactivar_cliente.png",19,19,new GlyphIcon("ban",Color.WHITE,18)));
        actualizar.setIcon(resourceIcon("/img/actualizar_cliente.png",20,20,new GlyphIcon("refresh",NAVY,19)));
        imprimir.setIcon(resourceIcon("/img/imprimir_clientes.png",21,21,new GlyphIcon("print",NAVY,19)));
        imprimir.setDisabledIcon(imprimir.getIcon());
        imprimir.setToolTipText("Imprimir la lista de clientes filtrados");
        actualizar.setToolTipText("Volver a consultar los clientes en la base de datos");
        actions.add(nuevo);actions.add(modificar);actions.add(detalle);actions.add(cambiar);actions.add(actualizar);actions.add(imprimir);
        nuevo.addActionListener(e->abrirNuevo());modificar.addActionListener(e->abrir(false));detalle.addActionListener(e->abrir(true));cambiar.addActionListener(e->cambiarEstado());actualizar.addActionListener(e->cargar());
        imprimir.addActionListener(e->imprimirJasper());
        p.add(actions,BorderLayout.EAST);return p;
    }
    private JPanel filtros(){
        JPanel p=card(new GridBagLayout());
        p.setBorder(new CompoundBorder(new LineBorder(new Color(215,226,240)),new EmptyBorder(13,14,16,14)));
        GridBagConstraints g=new GridBagConstraints();
        g.gridx=0;g.weightx=1;g.fill=GridBagConstraints.HORIZONTAL;
        g.insets=new Insets(0,0,0,0);int y=0;
        JLabel t=section("FILTROS");
        t.setIcon(resourceIcon("/img/filtro_clientes.png",24,24,new GlyphIcon("search",NAVY,22)));
        t.setIconTextGap(9);g.gridy=y++;g.insets=new Insets(0,0,14,0);p.add(t,g);

        JLabel searchLabel=new JLabel("BUSCAR CLIENTE");
        searchLabel.setForeground(NAVY);searchLabel.setFont(new Font("Segoe UI",Font.BOLD,11));
        g.gridy=y++;g.insets=new Insets(0,0,6,0);p.add(searchLabel,g);
        buscar.setFont(new Font("Segoe UI",Font.PLAIN,13));
        buscar.setToolTipText("Buscar por nombre, razón social, documento, teléfono o email");
        buscar.setPreferredSize(new Dimension(242,42));
        buscar.setBorder(new CompoundBorder(new LineBorder(new Color(193,210,232),1,true),new EmptyBorder(0,11,0,9)));
        buscar.setBackground(Color.WHITE);buscar.setForeground(INK);buscar.setCaretColor(BLUE);
        g.gridy=y++;g.insets=new Insets(0,0,13,0);p.add(buscar,g);

        for(Object[] entry:new Object[][]{{"ESTADO",estado},{"TIPO DE CLIENTE",tipo},{"CONDICIÓN IVA",iva}}){
            JLabel etiqueta=new JLabel((String)entry[0]);
            etiqueta.setForeground(NAVY);etiqueta.setFont(new Font("Segoe UI",Font.BOLD,11));
            g.gridy=y++;g.insets=new Insets(0,0,5,0);p.add(etiqueta,g);
            JComboBox<?> cb=(JComboBox<?>)entry[1];
            cb.setFont(new Font("Segoe UI",Font.PLAIN,12));cb.setForeground(INK);
            cb.setBackground(Color.WHITE);cb.setOpaque(true);
            cb.setPreferredSize(new Dimension(242,38));
            cb.setBorder(new LineBorder(new Color(193,210,232),1,true));
            cb.setRenderer(new ModernComboRenderer());
            g.gridy=y++;g.insets=new Insets(0,0,13,0);p.add(cb,g);
        }
        JPanel buttons=new JPanel(new GridLayout(1,2,10,0));buttons.setOpaque(false);
        JButton aplicar=button("BUSCAR",new Color(0,105,225),"search");
        JButton limpiar=button("LIMPIAR",new Color(230,239,249),"clear");
        aplicar.setPreferredSize(new Dimension(112,42));limpiar.setPreferredSize(new Dimension(112,42));
        buttons.add(aplicar);buttons.add(limpiar);
        g.gridy=y++;g.insets=new Insets(0,0,0,0);p.add(buttons,g);
        p.setMinimumSize(new Dimension(268,350));
        aplicar.addActionListener(e->filtrar());
        limpiar.addActionListener(e->{buscar.setText("");estado.setSelectedIndex(0);tipo.setSelectedIndex(0);iva.setSelectedIndex(0);filtrar();});
        return p;
    }

    private static final class ModernComboRenderer extends DefaultListCellRenderer {
        @Override public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean selected,boolean focus){
            JLabel l=(JLabel)super.getListCellRendererComponent(list,value,index,selected,focus);
            l.setFont(new Font("Segoe UI",Font.PLAIN,12));
            l.setBorder(new EmptyBorder(6,11,6,4));
            l.setForeground(selected&&index>=0?Color.WHITE:INK);
            l.setBackground(selected&&index>=0?BLUE:Color.WHITE);
            return l;
        }
    }

    private static final class ModernSearchField extends JTextField {
        private final String placeholder;
        ModernSearchField(String placeholder){this.placeholder=placeholder;}
        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if(getText().isEmpty()&&!isFocusOwner()){
                Graphics2D d=(Graphics2D)g.create();
                d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                d.setColor(new Color(133,148,169));d.setFont(getFont().deriveFont(11f));
                FontMetrics fm=d.getFontMetrics();
                d.drawString(placeholder,12,(getHeight()-fm.getHeight())/2+fm.getAscent());d.dispose();
            }
        }
    }

    private JPanel resumen(){
        JPanel p=card(new BorderLayout(0,5));p.setBorder(new CompoundBorder(new LineBorder(new Color(215,226,240)),new EmptyBorder(9,12,9,12)));
        JLabel tituloResumen=section("RESUMEN");tituloResumen.setIcon(resourceIcon("/img/estadistica_clientes.png",25,25,new GlyphIcon("users",NAVY,23)));tituloResumen.setIconTextGap(9);p.add(tituloResumen,BorderLayout.NORTH);
        JPanel stats=new JPanel(new GridLayout(4,1,0,1));stats.setOpaque(false);
        stats.add(stat("Total de clientes",total,NAVY));stats.add(stat("Activos",activos,GREEN));stats.add(stat("Inactivos",inactivos,RED));stats.add(stat("Con cuenta corriente",corriente,BLUE));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);top.add(stats,BorderLayout.NORTH);p.add(top,BorderLayout.CENTER);return p;
    }
    private JPanel stat(String label,JLabel n,Color color){JPanel p=new JPanel(new BorderLayout());p.setBackground(new Color(247,250,254));p.setBorder(new EmptyBorder(6,9,6,9));JLabel l=new JLabel(label);l.setFont(FONT);l.setForeground(INK);n.setFont(new Font("Segoe UI",Font.BOLD,20));n.setForeground(color);p.add(l,BorderLayout.WEST);p.add(n,BorderLayout.EAST);return p;}
    private JPanel tablaPanel(){
        JPanel p=card(new BorderLayout(0,6));p.setBorder(new CompoundBorder(new LineBorder(new Color(215,226,240)),new EmptyBorder(6,6,6,6)));
        tabla.setFont(new Font("Segoe UI",Font.PLAIN,12));tabla.setRowHeight(34);tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowVerticalLines(true);tabla.setShowHorizontalLines(true);tabla.setGridColor(new Color(231,238,248));tabla.setFillsViewportHeight(true);tabla.setSelectionBackground(new Color(0,129,242));tabla.setSelectionForeground(Color.WHITE);
        tabla.setAutoCreateRowSorter(false);tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        JTableHeader h=tabla.getTableHeader();h.setFont(new Font("Segoe UI",Font.BOLD,12));h.setPreferredSize(new Dimension(0,40));h.setReorderingAllowed(false);
        h.setDefaultRenderer(new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){
            JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,selected,focus,row,col);l.setBackground(NAVY);l.setForeground(Color.WHITE);
            l.setOpaque(true);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setBorder(new CompoundBorder(new MatteBorder(0,0,0,1,new Color(41,87,150)),new EmptyBorder(0,9,0,3)));return l;
        }});
        tabla.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){@Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int r,int c){
            super.getTableCellRendererComponent(t,v,sel,focus,r,c);setOpaque(true);setBorder(new EmptyBorder(0,9,0,5));
            setBackground(sel?new Color(17,127,241):(r%2==0?Color.WHITE:new Color(240,247,254)));
            setForeground(sel?Color.WHITE:INK);setFont(new Font("Segoe UI",Font.PLAIN,12));
            if(c==0)setHorizontalAlignment(SwingConstants.CENTER);else setHorizontalAlignment(SwingConstants.LEFT);
            if(c==7){setText("\u25cf  "+v);setFont(new Font("Segoe UI",Font.BOLD,12));if(!sel)setForeground("Activo".equals(v)?new Color(0,158,79):RED);}
            if(c==3&&!sel){setForeground(new Color(0,75,175));setFont(new Font("Segoe UI",Font.BOLD,11));setBackground(new Color(222,237,255));}
            if(c==7&&!sel)setText(("Activo".equals(v)?"●  ":"●  ")+v);
            return this;
        }});
        int[] widths={45,152,205,99,151,110,109,83};for(int i=0;i<widths.length;i++){tabla.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);tabla.getColumnModel().getColumn(i).setMinWidth(i==2?135:55);}
        JScrollPane scroll=new JScrollPane(tabla);scroll.setBorder(BorderFactory.createEmptyBorder());scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll,BorderLayout.CENTER);
        JPanel foot=new JPanel(new BorderLayout(8,0));foot.setOpaque(false);foot.setBorder(new EmptyBorder(5,7,2,7));
        resultados.setForeground(NAVY);resultados.setFont(new Font("Segoe UI",Font.PLAIN,12));foot.add(resultados,BorderLayout.WEST);
        JPanel nav=new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));nav.setOpaque(false);
        JButton primera=smallButton("<<"),previa=smallButton("<"),siguiente=smallButton(">"),ultima=smallButton(">>");
        primera.addActionListener(e->{pagina=0;llenarPagina();});previa.addActionListener(e->{pagina=Math.max(0,pagina-1);llenarPagina();});
        siguiente.addActionListener(e->{pagina=Math.min(totalPaginas()-1,pagina+1);llenarPagina();});ultima.addActionListener(e->{pagina=totalPaginas()-1;llenarPagina();});
        paginaLabel.setFont(new Font("Segoe UI",Font.BOLD,12));paginaLabel.setForeground(NAVY);
        selectorPaginas.setSelectedItem(20);selectorPaginas.setFont(FONT);selectorPaginas.setToolTipText("Clientes por página");
        selectorPaginas.addActionListener(e->{Integer n=(Integer)selectorPaginas.getSelectedItem();if(n!=null){porPagina=n;pagina=0;llenarPagina();}});
        nav.add(primera);nav.add(previa);nav.add(paginaLabel);nav.add(siguiente);nav.add(ultima);nav.add(selectorPaginas);
        foot.add(nav,BorderLayout.EAST);p.add(foot,BorderLayout.SOUTH);return p;
    }
    private int totalPaginas(){return Math.max(1,(visibles.size()+porPagina-1)/porPagina);}
    private void llenarPagina(){pagina=Math.max(0,Math.min(pagina,totalPaginas()-1));model.setRowCount(0);
        int inicio=pagina*porPagina,fin=Math.min(visibles.size(),inicio+porPagina);
        for(int j=inicio;j<fin;j++){Cliente c=visibles.get(j);String doc=(safe(c.getTipoDocumento())+" "+safe(c.getNroDocumento())).trim();
            model.addRow(new Object[]{c.getIdCliente(),doc,c.getNombreMostrar(),safe(c.getTipoCliente()),safe(c.getCondicionIva()),safe(c.getTelefono()),safe(c.getLocalidad()),c.isActivo()?"Activo":"Inactivo"});}
        paginaLabel.setText("  "+(pagina+1)+" / "+totalPaginas()+"  ");actualizarAcciones();
    }
    /** Iconos del proyecto (src/img). Si falta un recurso, conserva el icono Java2D. */
    private static Icon resourceIcon(String path,int width,int height,Icon fallback){
        java.net.URL url=PanelClientes.class.getResource(path);
        if(url==null)return fallback;
        ImageIcon original=new ImageIcon(url);
        if(original.getIconWidth()<=0)return fallback;
        Image scaled=original.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    private static JPanel card(LayoutManager lm){JPanel p=new JPanel(lm);p.setBackground(Color.WHITE);return p;}
    private static JLabel section(String s){JLabel l=new JLabel(s);l.setForeground(NAVY);l.setFont(new Font("Segoe UI",Font.BOLD,16));return l;}
    private static JButton smallButton(String s){JButton b=button(s,Color.WHITE,"none");b.setIcon(null);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setPreferredSize(new Dimension(34,30));return b;}
    private static JButton button(String text,Color bg){return button(text,bg,"none");}
    private static JButton button(String text,Color bg,String glyph){
        boolean light=bg.equals(Color.WHITE)||bg.getRed()>205&&bg.getGreen()>205;
        Color foreground=light?NAVY:Color.WHITE;
        JButton b=new StyledButton(text,bg,light);
        if(!"none".equals(glyph))b.setIcon(new GlyphIcon(glyph,foreground,17));
        b.setFont(BUTTON_FONT);b.setForeground(foreground);
        b.setDisabledIcon(b.getIcon());
        b.setToolTipText(text);
        b.setFocusPainted(false);b.setBorderPainted(false);b.setContentAreaFilled(false);
        b.setOpaque(false);b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.CENTER);b.setIconTextGap(7);
        b.setMargin(new Insets(0,9,0,9));
        int width=switch(text){case "NUEVO"->94;case "MODIFICAR"->110;case "VER DETALLE"->125;case "DESACTIVAR","ACTIVAR"->115;case "ACTUALIZAR"->122;case "IMPRIMIR"->106;default->99;};
        b.setPreferredSize(new Dimension(width+8,42));
        return b;
    }
    /** Pinta el fondo sin depender del Look & Feel instalado en NetBeans. */
    private static final class StyledButton extends JButton {
        private final boolean light;
        StyledButton(String text,Color background,boolean light){super(text);this.light=light;setBackground(background);}
        @Override protected void paintComponent(Graphics g){
            Graphics2D d=(Graphics2D)g.create();
            d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth()-1,h=getHeight()-1;
            Color base=getBackground();
            if(getModel().isPressed())base=base.darker();
            else if(getModel().isRollover())base=light?new Color(232,241,252):base.brighter();
            // No desteñir el botón por falta de selección: mantener su identidad visual.
            Color top=light?Color.WHITE:base.brighter();
            d.setPaint(new GradientPaint(0,0,top,0,h,base));
            d.fillRoundRect(0,0,w,h,10,10);
            d.setColor(light?new Color(189,207,231):base.darker());
            d.setStroke(new BasicStroke(1.0f));d.drawRoundRect(0,0,w,h,10,10);
            d.dispose();
            // La interfaz del sistema puede cambiar colores al deshabilitar.
            // Pintamos texto e icono de forma consistente aun sin selección.
            Graphics2D textG=(Graphics2D)g.create();
            textG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            String label=getText();textG.setFont(getFont());FontMetrics fm=textG.getFontMetrics();
            Icon icon=getIcon();int iw=icon==null?0:icon.getIconWidth();int gap=icon==null?0:getIconTextGap();
            int totalWidth=iw+gap+fm.stringWidth(label);int x=(getWidth()-totalWidth)/2;
            if(icon!=null){icon.paintIcon(this,textG,x,(getHeight()-icon.getIconHeight())/2);x+=iw+gap;}
            textG.setColor(light?NAVY:Color.WHITE);
            textG.drawString(label,x,(getHeight()-fm.getHeight())/2+fm.getAscent());textG.dispose();
        }
    }
    private static final class GlyphIcon implements Icon{
        private final String kind;private final Color color;private final int size;
        GlyphIcon(String k,Color c,int s){kind=k;color=c;size=s;}
        public int getIconWidth(){return size;}public int getIconHeight(){return size;}
        public void paintIcon(Component comp,Graphics g,int x,int y){Graphics2D d=(Graphics2D)g.create();d.translate(x,y);d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);d.setColor(color);
            d.setStroke(new BasicStroke(Math.max(1.8f,size/9f),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));int s=size;
            switch(kind){
                case "plus":d.drawLine(s/2,s/5,s/2,s*4/5);d.drawLine(s/5,s/2,s*4/5,s/2);break;
                case "edit":
                    d.rotate(-Math.PI/4,s/2.0,s/2.0);
                    d.fillRoundRect(s*2/5,s/7,s/5,s*3/5,2,2);
                    d.fillPolygon(new int[]{s*2/5,s*3/5,s/2},new int[]{s*3/4,s*3/4,s*9/10},3);
                    break;
                case "eye":
                    java.awt.geom.Path2D eye=new java.awt.geom.Path2D.Double();
                    eye.moveTo(s/9.0,s/2.0);eye.quadTo(s/2.0,-s/10.0,s*8/9.0,s/2.0);
                    eye.quadTo(s/2.0,s*11/10.0,s/9.0,s/2.0);d.draw(eye);
                    d.fillOval(s*2/5,s*2/5,s/5,s/5);break;
                case "ban":d.drawOval(s/6,s/6,s*2/3,s*2/3);d.drawLine(s/4,s*3/4,s*3/4,s/4);break;
                case "refresh":
                    d.drawArc(s/6,s/6,s*2/3,s*2/3,35,295);
                    d.fillPolygon(new int[]{s*5/6,s*5/6,s*3/5},new int[]{s/6,s*2/5,s/4},3);break;
                case "print":
                    d.drawRoundRect(s/7,s*7/20,s*5/7,s*2/5,2,2);
                    d.drawRect(s/4,s/10,s/2,s*7/20);
                    d.setColor(comp.isEnabled()?color:color);d.fillOval(s*7/10,s/2,Math.max(2,s/10),Math.max(2,s/10));
                    d.setColor(color);d.fillRect(s/4,s*13/20,s/2,s/4);
                    d.setColor(Color.WHITE);d.setStroke(new BasicStroke(1.0f));
                    d.drawLine(s/3,s*3/4,s*2/3,s*3/4);break;
                case "search":d.drawOval(s/7,s/7,s/2,s/2);d.drawLine(s*3/5,s*3/5,s*6/7,s*6/7);break;
                case "clear":d.drawLine(s/4,s/4,s*3/4,s*3/4);d.drawLine(s*3/4,s/4,s/4,s*3/4);break;
                case "users":d.fillOval(s/4,s/7,s/3,s/3);d.fillRoundRect(s/10,s/2,s*3/5,s*2/5,9,9);d.fillOval(s*3/5,s/5,s/4,s/4);d.fillRoundRect(s*3/5,s/2,s/3,s/3,9,9);break;
                default:d.fillOval(s/3,s/3,s/3,s/3);
            }d.dispose();}
    }
    /** Reporte A4 apaisado: imprime todos los clientes que cumplen los filtros, no solo la página visible. */
    // Compilar una sola vez por ejecución: las impresiones siguientes reutilizan el reporte.
    private static volatile net.sf.jasperreports.engine.JasperReport reporteClientesCache;

    private static net.sf.jasperreports.engine.JasperReport obtenerReporteClientes() throws Exception {
        net.sf.jasperreports.engine.JasperReport r = reporteClientesCache;
        if (r != null) return r;
        synchronized (PanelClientes.class) {
            if (reporteClientesCache != null) return reporteClientesCache;
            try (java.io.InputStream in = PanelClientes.class.getResourceAsStream("/reportes/ReporteClientes.jrxml")) {
                if (in != null) {
                    reporteClientesCache = net.sf.jasperreports.engine.JasperCompileManager.compileReport(in);
                } else {
                    java.io.File archivo = new java.io.File("src/reportes/ReporteClientes.jrxml");
                    if (!archivo.isFile()) throw new java.io.FileNotFoundException("No se encontró ReporteClientes.jrxml en src/reportes/");
                    reporteClientesCache = net.sf.jasperreports.engine.JasperCompileManager.compileReport(archivo.getPath());
                }
            }
            return reporteClientesCache;
        }
    }

    private void imprimirJasper(){
        if(visibles.isEmpty()){
            JOptionPane.showMessageDialog(this,"No hay clientes para imprimir con los filtros actuales.",
                    "Serena Soft · Reportes",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try{
            net.sf.jasperreports.engine.JasperReport reporte=obtenerReporteClientes();
            java.util.List<java.util.Map<String,?>> filas=new java.util.ArrayList<>();
            for(Cliente c:visibles){
                java.util.Map<String,Object> f=new java.util.HashMap<>();
                f.put("id",String.valueOf(c.getIdCliente()));
                f.put("documento",(safe(c.getTipoDocumento())+" "+safe(c.getNroDocumento())).trim());
                f.put("cliente",safe(c.getNombreMostrar()));
                f.put("tipo",safe(c.getTipoCliente()));
                f.put("iva",safe(c.getCondicionIva()));
                f.put("telefono",safe(c.getTelefono()));
                f.put("localidad",safe(c.getLocalidad()));
                f.put("estado",c.isActivo()?"Activo":"Inactivo");
                filas.add(f);
            }
            java.util.Map<String,Object> parametros=new java.util.HashMap<>();
            parametros.put("TITULO","SERENA SOFT");
            parametros.put("SUBTITULO","Sistema de Gestión de Ventas");
            parametros.put("USUARIO",System.getProperty("serenasoft.usuario","No especificado"));
            parametros.put("FECHA",new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
            parametros.put("TOTAL",String.valueOf(visibles.size()));
            parametros.put("FILTROS", "Estado: "+estado.getSelectedItem()+"   |   Tipo: "+tipo.getSelectedItem()+"   |   IVA: "+iva.getSelectedItem());
            net.sf.jasperreports.engine.JasperPrint print=net.sf.jasperreports.engine.JasperFillManager.fillReport(
                reporte,parametros,new net.sf.jasperreports.engine.data.JRMapCollectionDataSource(filas));
            net.sf.jasperreports.view.JasperViewer.viewReport(print,false);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"No se pudo generar el reporte Jasper:\n"+ex.getMessage(),
                "Serena Soft · Reportes",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargar(){try{todos=service.listar("",true);filtrar();}catch(Exception e){error(e);}}
    private void filtrar(){if(model==null)return;String q=buscar.getText().trim().toLowerCase(Locale.ROOT);visibles=new ArrayList<>();int a=0,i=0,cc=0;
        for(Cliente c:todos){if(c.isActivo())a++;else i++;if(c.isPermiteCuentaCorriente())cc++;
            if(estado.getSelectedIndex()==1&&!c.isActivo())continue;if(estado.getSelectedIndex()==2&&c.isActivo())continue;
            if(tipo.getSelectedIndex()>0&&!Objects.equals(tipo.getSelectedItem(),c.getTipoCliente()))continue;
            if(iva.getSelectedIndex()>0&&!Objects.equals(iva.getSelectedItem(),c.getCondicionIva()))continue;
            String search=(safe(c.getNombre())+" "+safe(c.getApellido())+" "+safe(c.getRazonSocial())+" "+safe(c.getNroDocumento())+" "+safe(c.getTelefono())+" "+safe(c.getEmail())).toLowerCase(Locale.ROOT);
            if(!search.contains(q))continue;visibles.add(c);
        }
        pagina=0;llenarPagina();
        total.setText(String.valueOf(todos.size()));activos.setText(String.valueOf(a));inactivos.setText(String.valueOf(i));corriente.setText(String.valueOf(cc));resultados.setText("  "+visibles.size()+" clientes encontrados");actualizarAcciones();
    }
    private static String safe(String s){return s==null?"":s;}
    private Cliente selected(){int row=tabla.getSelectedRow();if(row<0)return null;int m=tabla.convertRowIndexToModel(row);return pagina*porPagina+m>=0&&pagina*porPagina+m<visibles.size()?visibles.get(pagina*porPagina+m):null;}
    private void actualizarAcciones(){Cliente c=selected();modificar.setEnabled(c!=null);detalle.setEnabled(c!=null);cambiar.setEnabled(c!=null);if(c!=null){cambiar.setText(c.isActivo()?"DESACTIVAR":"ACTIVAR");cambiar.setBackground(c.isActivo()?RED:GREEN);cambiar.setIcon(c.isActivo()?resourceIcon("/img/desactivar_cliente.png",19,19,new GlyphIcon("ban",Color.WHITE,18)):new GlyphIcon("plus",Color.WHITE,18));}}
    private void abrirNuevo(){abrirDialogo(null,false);}
    private void abrir(boolean soloLectura){Cliente c=selected();if(c==null)return;abrirDialogo(c,soloLectura);}
    private void abrirDialogo(Cliente c,boolean readOnly){Window w=SwingUtilities.getWindowAncestor(this);DialogoCliente d=new DialogoCliente(w,c,service,readOnly);d.setVisible(true);if(d.guardado)cargar();}
    private void cambiarEstado(){Cliente c=selected();if(c==null)return;boolean next=!c.isActivo();String msg=(next?"¿Activar a ":"¿Desactivar a ")+c.getNombreMostrar()+"?";if(JOptionPane.showConfirmDialog(this,msg,"Confirmar cambio de estado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)!=JOptionPane.YES_OPTION)return;try{service.cambiarActivo(c.getIdCliente(),next);cargar();}catch(Exception e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Serena Soft · Clientes",JOptionPane.ERROR_MESSAGE);}

    private static final class DialogoCliente extends JDialog {
        private final ClienteService service;private final Cliente original;private final boolean readOnly;boolean guardado=false;
        private final JTextField nombre=new JTextField(),apellido=new JTextField(),razon=new JTextField(),documento=new JTextField(),telefono=new JTextField(),email=new JTextField(),direccion=new JTextField(),localidad=new JTextField(),limite=new JTextField("0");
        private final JComboBox<String> provincia=new JComboBox<>(new String[]{"","Chaco","Corrientes","Buenos Aires","CABA","Catamarca","Chubut","Córdoba","Entre Ríos","Formosa","Jujuy","La Pampa","La Rioja","Mendoza","Misiones","Neuquén","Río Negro","Salta","San Juan","San Luis","Santa Cruz","Santa Fe","Santiago del Estero","Tierra del Fuego","Tucumán"});
        private final JComboBox<String> tipoDoc=new JComboBox<>(new String[]{"","DNI","CUIT","CUIL","PASAPORTE","OTRO"});
        private final JComboBox<String> iva=new JComboBox<>(new String[]{"","CONSUMIDOR FINAL","RESPONSABLE INSCRIPTO","MONOTRIBUTISTA","EXENTO","NO RESPONSABLE"});
        private final JComboBox<String> tipo=new JComboBox<>(new String[]{"MINORISTA","MAYORISTA"});
        private final JCheckBox corriente=new JCheckBox("Permite cuenta corriente"),activo=new JCheckBox("Cliente activo",true);
        private final JTabbedPane tabs=new JTabbedPane(JTabbedPane.LEFT);
        DialogoCliente(Window owner,Cliente c,ClienteService s,boolean read){super(owner,c==null?"Serena Soft · Nuevo cliente":"Serena Soft · Cliente",ModalityType.APPLICATION_MODAL);original=c;service=s;readOnly=read;
            setLayout(new BorderLayout());setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(920,590);setMinimumSize(new Dimension(790,530));setLocationRelativeTo(owner);
            JPanel head=new JPanel(new BorderLayout());head.setBackground(NAVY);head.setBorder(new EmptyBorder(14,22,14,22));JLabel title=new JLabel("CLIENTE  ·  "+(c==null?"Nuevo cliente":read?"Detalle de cliente":"Modificar cliente"));title.setForeground(Color.WHITE);title.setFont(new Font("Segoe UI",Font.BOLD,21));head.add(title);add(head,BorderLayout.NORTH);
            tabs.setFont(new Font("Segoe UI",Font.BOLD,13));tabs.setBackground(BG);
            tabs.setForeground(NAVY);tabs.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));tabs.addTab("Datos generales",general());tabs.addTab("Datos comerciales",comercial());tabs.addTab("Cuenta corriente",cuenta());add(tabs,BorderLayout.CENTER);
            JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,10));footer.setBackground(Color.WHITE);JButton save=button("GUARDAR",GREEN,"plus"),cancel=button(read?"CERRAR":"CANCELAR",new Color(100,118,137),"clear");footer.add(save);footer.add(cancel);add(footer,BorderLayout.SOUTH);cancel.addActionListener(e->dispose());save.addActionListener(e->guardar());save.setVisible(!read);
            corriente.addActionListener(e->syncCredito());
            tipoDoc.addActionListener(e->actualizarTipoDocumento());
            if(c!=null)llenar(c);else {iva.setSelectedItem("CONSUMIDOR FINAL");provincia.setSelectedItem("Chaco");syncCredito();}
            actualizarTipoDocumento();
            if(read)bloquear(this);
            getRootPane().setDefaultButton(save);
            getRootPane().registerKeyboardAction(e->dispose(),KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW);
            if(c==null)SwingUtilities.invokeLater(()->nombre.requestFocusInWindow());
        }
        private JPanel general(){JPanel p=page();p.add(section("DATOS PERSONALES / FISCALES"));p.add(Box.createVerticalStrut(7));p.add(row(field("Tipo de documento",tipoDoc),field("Nro. documento",documento),field("Condición IVA",iva)));
            p.add(row(field("Razón social / Empresa",razon)));p.add(row(field("Nombre",nombre),field("Apellido",apellido),field("Tipo de cliente",tipo)));
            p.add(Box.createVerticalStrut(10));p.add(section("CONTACTO Y UBICACIÓN"));p.add(row(field("Teléfono",telefono),field("Email",email)));p.add(row(field("Dirección",direccion)));p.add(row(field("Localidad",localidad),field("Provincia",provincia)));return wrap(p);}
        private JPanel comercial(){JPanel p=page();p.add(section("DATOS COMERCIALES"));JLabel hint=new JLabel("Los datos fiscales, el tipo de cliente y la razón social se editan en Datos generales.");hint.setForeground(new Color(96,110,130));p.add(Box.createVerticalStrut(14));p.add(hint);p.add(Box.createVerticalStrut(18));p.add(section("Preparado para Ventas"));JLabel info=new JLabel("La vinculación con ventas, comprobantes e historial se incorporará al desarrollar ese módulo.");info.setForeground(new Color(96,110,130));p.add(info);return wrap(p);}
        private JPanel cuenta(){JPanel p=page();p.add(section("ESTADO Y CONFIGURACIÓN"));p.add(row(field("Estado del cliente",activo)));p.add(row(field("Cuenta corriente",corriente)));p.add(row(field("Límite de crédito ($)",limite)));JLabel hint=new JLabel("La gestión de saldos y cobros se habilitará con el módulo de Ventas y Caja.");hint.setForeground(new Color(96,110,130));p.add(hint);return wrap(p);}
        private JPanel page(){JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBackground(Color.WHITE);p.setBorder(new EmptyBorder(18,24,18,24));return p;}
        private JPanel wrap(JPanel p){JPanel outer=new JPanel(new BorderLayout());outer.setBackground(Color.WHITE);
            JScrollPane scroll=new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(BorderFactory.createEmptyBorder());scroll.getVerticalScrollBar().setUnitIncrement(16);
            outer.add(scroll,BorderLayout.CENTER);return outer;}
        private JPanel row(JPanel... fields){JPanel r=new JPanel(new GridLayout(1,fields.length,12,0));r.setOpaque(false);r.setMaximumSize(new Dimension(Integer.MAX_VALUE,76));r.setAlignmentX(Component.LEFT_ALIGNMENT);for(JPanel f:fields)r.add(f);return r;}
        private JPanel field(String label,JComponent input){JPanel p=new JPanel(new BorderLayout(0,5));p.setOpaque(false);p.setBorder(new EmptyBorder(7,0,5,0));JLabel l=new JLabel(label);l.setForeground(NAVY);l.setFont(new Font("Segoe UI",Font.BOLD,12));p.add(l,BorderLayout.NORTH);input.setFont(FONT);input.setPreferredSize(new Dimension(160,35));
            if(input instanceof JTextField){((JTextField)input).setMargin(new Insets(3,9,3,9));}
            if(input instanceof JComboBox){((JComboBox<?>)input).setBackground(Color.WHITE);}p.add(input,BorderLayout.CENTER);return p;}
        private void actualizarTipoDocumento(){
            String d=(String)tipoDoc.getSelectedItem();
            documento.setToolTipText("CUIT".equals(d)||"CUIL".equals(d)?"Ingresá el número con 11 dígitos (con o sin guiones)":"Número de documento del cliente");
        }
        private void validarFormulario(){
            boolean empresa=!val(razon).isBlank();
            if(!empresa && (val(nombre).isBlank()||val(apellido).isBlank())){
                tabs.setSelectedIndex(0);nombre.requestFocusInWindow();
                throw new IllegalArgumentException("Ingresá nombre y apellido, o completá la razón social de la empresa.");
            }
            String td=(String)tipoDoc.getSelectedItem(),nd=val(documento);
            if((td==null||td.isBlank())!=nd.isBlank()){
                tabs.setSelectedIndex(0);tipoDoc.requestFocusInWindow();
                throw new IllegalArgumentException("Completá tanto el tipo como el número de documento, o dejá ambos vacíos.");
            }
            if(!nd.isBlank() && !nd.matches("[A-Za-z0-9./ -]{4,30}")){
                tabs.setSelectedIndex(0);documento.requestFocusInWindow();
                throw new IllegalArgumentException("El número de documento tiene un formato inválido.");
            }
            if(("CUIT".equals(td)||"CUIL".equals(td))&&!nd.isBlank()&&nd.replaceAll("[^0-9]","").length()!=11){
                tabs.setSelectedIndex(0);documento.requestFocusInWindow();
                throw new IllegalArgumentException("El CUIT/CUIL debe contener 11 dígitos.");
            }
            if(!val(email).isBlank()&&!val(email).matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")){
                tabs.setSelectedIndex(0);email.requestFocusInWindow();
                throw new IllegalArgumentException("Revisá el formato del correo electrónico.");
            }
            if(corriente.isSelected()){
                BigDecimal valor;
                try{valor=credito();}catch(NumberFormatException ex){
                    tabs.setSelectedIndex(2);limite.requestFocusInWindow();
                    throw new IllegalArgumentException("El límite de crédito debe ser un importe válido.");
                }
                if(valor.signum()<0){
                    tabs.setSelectedIndex(2);limite.requestFocusInWindow();
                    throw new IllegalArgumentException("El límite de crédito no puede ser negativo.");
                }
            }
        }
        private void syncCredito(){limite.setEnabled(corriente.isSelected()&&!readOnly);if(!corriente.isSelected()&&!readOnly)limite.setText("0");}
        private void llenar(Cliente c){nombre.setText(safe(c.getNombre()));apellido.setText(safe(c.getApellido()));razon.setText(safe(c.getRazonSocial()));documento.setText(safe(c.getNroDocumento()));telefono.setText(safe(c.getTelefono()));email.setText(safe(c.getEmail()));direccion.setText(safe(c.getDireccion()));localidad.setText(safe(c.getLocalidad()));select(tipoDoc,c.getTipoDocumento());select(iva,c.getCondicionIva());select(tipo,c.getTipoCliente());select(provincia,c.getProvincia());corriente.setSelected(c.isPermiteCuentaCorriente());limite.setText(c.getLimiteCredito()==null?"0":c.getLimiteCredito().toPlainString());activo.setSelected(c.isActivo());syncCredito();}
        private void select(JComboBox<String> cb,String v){if(v==null||v.isBlank())return;boolean found=false;for(int i=0;i<cb.getItemCount();i++)if(v.equalsIgnoreCase(cb.getItemAt(i))){cb.setSelectedIndex(i);found=true;break;}if(!found){cb.addItem(v);cb.setSelectedItem(v);}}
        private void bloquear(Container c){for(Component component:c.getComponents()){if(component instanceof JTextField)((JTextField)component).setEditable(false);if(component instanceof JComboBox)((JComboBox<?>)component).setEnabled(false);if(component instanceof JCheckBox)((JCheckBox)component).setEnabled(false);if(component instanceof Container)bloquear((Container)component);}}
        private static String val(JTextField t){return t.getText().trim();}
        private BigDecimal credito(){String s=val(limite).replace("$","").replace(" ","");if(s.isEmpty())return BigDecimal.ZERO;if(s.contains(","))s=s.replace(".","").replace(",",".");return new BigDecimal(s);}
        private void guardar(){try{validarFormulario();Cliente c=new Cliente();if(original!=null)c.setIdCliente(original.getIdCliente());c.setNombre(val(nombre));c.setApellido(val(apellido));c.setRazonSocial(val(razon));c.setTipoDocumento((String)tipoDoc.getSelectedItem());c.setNroDocumento(val(documento));c.setCondicionIva((String)iva.getSelectedItem());c.setTipoCliente((String)tipo.getSelectedItem());c.setTelefono(val(telefono));c.setEmail(val(email));c.setDireccion(val(direccion));c.setLocalidad(val(localidad));c.setProvincia((String)provincia.getSelectedItem());c.setActivo(activo.isSelected());c.setPermiteCuentaCorriente(corriente.isSelected());c.setLimiteCredito(corriente.isSelected()?credito():BigDecimal.ZERO);
            if(original==null)service.crear(c);else service.modificar(c);
            guardado=true;
            JOptionPane.showMessageDialog(this,original==null?"Cliente registrado correctamente.":"Datos del cliente actualizados correctamente.","Serena Soft",JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Revisar datos",JOptionPane.WARNING_MESSAGE);}}
    }
}
