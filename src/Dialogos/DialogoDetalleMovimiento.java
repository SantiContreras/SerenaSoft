package Dialogos;

import Dao.AjusteStockDao;
import Dao.StockProductoDao;
import services.CompraService;
import services.SalidaStockService;
import model.Compra;
import model.CompraDetalle;
import model.SalidaStock;
import model.SalidaStockDetalle;
import model.AjusteStock;
import model.AjusteStockDetalle;
import model.StockProducto;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/** Ficha de producto y movimientos confirmados de ajustes de stock. */
public class DialogoDetalleMovimiento extends JDialog {
    private static final Color NAVY = new Color(15,50,110);
    private static final Color BLUE = new Color(24,72,145);
    private static final Color BG = new Color(238,242,247);
    private static final Color GREEN = new Color(25,135,84);
    private static final Color RED = new Color(190,65,65);
    private static final Color ORANGE = new Color(205,130,15);
    private final int idProducto;
    private final String depositoInicial;
    private final JLabel lblEstado = new JLabel();
    private final JComboBox<String> comboDeposito = new JComboBox<>();
    private final DefaultTableModel modeloDepositos = new DefaultTableModel(new String[]{"Depósito", "Cantidad"},0) {
        @Override public boolean isCellEditable(int r,int c) { return false; }
    };
    private final DefaultTableModel modeloMovimientos = new DefaultTableModel(
            new String[]{"Fecha y hora", "Movimiento", "Diferencia", "Depósito", "Usuario", "Motivo"},0) {
        @Override public boolean isCellEditable(int r,int c) { return false; }
    };
    private final JLabel lblConteo = new JLabel("0 movimientos");
    private final JLabel lblAviso = new JLabel("Operaciones confirmadas registradas en el sistema. Las entradas manuales requieren integrar su registro de origen.");
    private final JTable tablaDepositos = new JTable(modeloDepositos);
    private final JTable tablaMovimientos = new JTable(modeloMovimientos);
    private final DateTimeFormatter fechaFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DialogoDetalleMovimiento(Window owner, int idProducto, String codigo, String producto,
            String marca, String categoria, String deposito, String stock, String minimo,
            String compra, String venta, String estado) {
        super(owner, "Serena Soft · Detalle de movimientos", ModalityType.APPLICATION_MODAL);
        this.idProducto = idProducto;
        this.depositoInicial = deposito;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(850,570));
        setSize(1060,690);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        JPanel cabecera = new JPanel(new BorderLayout(12,5));
        cabecera.setBackground(NAVY);
        cabecera.setBorder(new EmptyBorder(17,23,17,23));
        JLabel titulo = new JLabel("  DETALLE Y MOVIMIENTOS DEL PRODUCTO");
        titulo.setIcon(icon("box",Color.WHITE));
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI",Font.BOLD,20));
        JLabel sub = new JLabel(codigo+"  ·  "+producto);
        sub.setForeground(new Color(212,226,247));
        sub.setFont(new Font("Segoe UI",Font.PLAIN,14));
        cabecera.add(titulo,BorderLayout.NORTH);
        cabecera.add(sub,BorderLayout.CENTER);
        add(cabecera,BorderLayout.NORTH);

        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo,BoxLayout.Y_AXIS));
        cuerpo.setBackground(BG);
        cuerpo.setBorder(new EmptyBorder(14,17,14,17));

        JPanel ficha = tarjeta("INFORMACIÓN DEL PRODUCTO", "box");
        JPanel datos = new JPanel(new GridLayout(3,4,12,12));
        datos.setOpaque(false);
        datos.setBorder(new EmptyBorder(8,4,8,4));
        datos.add(dato("CÓDIGO",codigo));
        datos.add(dato("PRODUCTO",producto));
        datos.add(dato("MARCA",marca));
        datos.add(dato("CATEGORÍA",categoria));
        datos.add(dato("DEPÓSITO SELECCIONADO",deposito));
        datos.add(dato("STOCK EN DEPÓSITO",stock));
        datos.add(dato("STOCK MÍNIMO",minimo));
        JPanel estadoPanel = dato("ESTADO",estado);
        lblEstado.setText(estado);
        lblEstado.setForeground("Disponible".equalsIgnoreCase(estado)?GREEN:("Bajo Stock".equalsIgnoreCase(estado)?ORANGE:RED));
        datos.add(estadoPanel);
        datos.add(dato("PRECIO DE COMPRA",compra));
        datos.add(dato("PRECIO DE VENTA",venta));
        ficha.add(datos,BorderLayout.CENTER);
        ficha.setMaximumSize(new Dimension(Integer.MAX_VALUE,215));
        cuerpo.add(ficha);
        cuerpo.add(Box.createVerticalStrut(12));

        JPanel historial = tarjeta("HISTORIAL DE MOVIMIENTOS DEL PRODUCTO", "history");
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT,12,8));
        filtros.setOpaque(false);
        JLabel lblFiltro = new JLabel("Depósito:");
        lblFiltro.setFont(new Font("Segoe UI",Font.BOLD,13));
        lblFiltro.setForeground(NAVY);
        filtros.add(lblFiltro);
        comboDeposito.setFont(new Font("Segoe UI",Font.PLAIN,13));
        comboDeposito.setPreferredSize(new Dimension(240,32));
        filtros.add(comboDeposito);
        lblConteo.setForeground(BLUE);
        lblConteo.setFont(new Font("Segoe UI",Font.BOLD,12));
        filtros.add(lblConteo);
        JPanel encabezadoHistorial = new JPanel(new BorderLayout());
        encabezadoHistorial.setOpaque(false);
        JLabel tituloHistorial = (JLabel) historial.getComponent(0);
        historial.remove(tituloHistorial);
        encabezadoHistorial.add(tituloHistorial, BorderLayout.NORTH);
        encabezadoHistorial.add(filtros, BorderLayout.SOUTH);
        historial.add(encabezadoHistorial,BorderLayout.NORTH);
        prepararTabla(tablaMovimientos);
        tablaMovimientos.getColumnModel().getColumn(1).setCellRenderer(new TipoRenderer());
        JScrollPane scrollM = new JScrollPane(tablaMovimientos);
        scrollM.setBorder(new EmptyBorder(0,10,0,10));
        historial.add(scrollM,BorderLayout.CENTER);
        lblAviso.setForeground(new Color(115,92,40));
        lblAviso.setFont(new Font("Segoe UI",Font.PLAIN,11));
        lblAviso.setBorder(new EmptyBorder(7,13,9,10));
        historial.add(lblAviso,BorderLayout.SOUTH);
        historial.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        historial.setPreferredSize(new Dimension(950,340));
        cuerpo.add(historial);
        JScrollPane scrollPrincipal = new JScrollPane(cuerpo);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(18);
        add(scrollPrincipal,BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,10));
        pie.setBackground(Color.WHITE);
        JButton actualizar = boton("ACTUALIZAR",BLUE,"refresh");
        JButton cerrar = boton("CERRAR",new Color(87,98,112),"close");
        actualizar.addActionListener(e -> cargarDatos());
        cerrar.addActionListener(e -> dispose());
        pie.add(actualizar);
        pie.add(cerrar);
        add(pie,BorderLayout.SOUTH);
        comboDeposito.addActionListener(e -> cargarMovimientos());
        cargarDatos();
        setLocationRelativeTo(owner);
    }

    private JPanel tarjeta(String titulo,String icono) {
        JPanel panel = new JPanel(new BorderLayout(0,6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(210,218,228)));
        JLabel label = new JLabel(titulo);
        label.setIcon(icon(icono,BLUE));
        label.setIconTextGap(9);
        label.setForeground(NAVY);
        label.setFont(new Font("Segoe UI",Font.BOLD,14));
        label.setBorder(new EmptyBorder(10,14,4,14));
        panel.add(label,BorderLayout.PAGE_START);
        return panel;
    }
    private JPanel dato(String titulo,String valor) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        JLabel a = new JLabel(titulo);
        a.setForeground(NAVY);
        a.setFont(new Font("Segoe UI",Font.BOLD,11));
        JLabel b = new JLabel(valor == null || valor.isEmpty()?"-":valor);
        b.setForeground(new Color(50,59,70));
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setToolTipText(valor);
        p.add(a,BorderLayout.NORTH);
        p.add(b,BorderLayout.CENTER);
        return p;
    }
    private void prepararTabla(JTable t) {
        t.setFont(new Font("Segoe UI",Font.PLAIN,12));
        t.setRowHeight(32);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setGridColor(new Color(226,232,239));
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(215,228,245));
        t.setSelectionForeground(Color.BLACK);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setPreferredSize(new Dimension(0,39));
        t.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(NAVY);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI",Font.BOLD,12));
        for(int i=0;i<t.getColumnCount();i++) t.getColumnModel().getColumn(i).setHeaderRenderer(header);
    }
    private JButton boton(String texto,Color fondo,String icono) {
        JButton b = new JButton(texto,icon(icono,Color.WHITE));
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,12));
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11,18,11,18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private void cargarDatos() {
        String seleccionado = (String)comboDeposito.getSelectedItem();
        modeloDepositos.setRowCount(0);
        comboDeposito.removeAllItems();
        comboDeposito.addItem("Todos los depósitos");
        try {
            List<StockProducto> stocks = new StockProductoDao().listarPorProducto(idProducto);
            Set<String> nombres = new LinkedHashSet<>();
            for(StockProducto s: stocks) {
                String nombre = s.getDeposito()==null?"Sin depósito":s.getDeposito().getNombre();
                modeloDepositos.addRow(new Object[]{nombre,formatear(s.getCantidad())});
                nombres.add(nombre);
            }
            for(String nombre:nombres) comboDeposito.addItem(nombre);
            String preferido = seleccionado == null ? depositoInicial : seleccionado;
            if(nombres.contains(preferido)) comboDeposito.setSelectedItem(preferido);
            else comboDeposito.setSelectedIndex(0);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"No se pudieron cargar las existencias: "+ex.getMessage(),"Serena Soft",JOptionPane.WARNING_MESSAGE);
        }
        cargarMovimientos();
    }
    private void cargarMovimientos() {
        modeloMovimientos.setRowCount(0);
        String deposito = (String) comboDeposito.getSelectedItem();
        if (deposito == null) return;
        List<Object[]> filas = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        try {
            for (AjusteStock a : new AjusteStockDao().listarPorEstado("CONFIRMADO")) {
                String dep = nombreDeposito(a.getDeposito());
                if (!incluyeDeposito(deposito, dep) || a.getDetalles() == null) continue;
                for (AjusteStockDetalle d : a.getDetalles()) {
                    if (d.getProducto() == null || d.getProducto().getIdProducto() != idProducto) continue;
                    BigDecimal dif = d.getDiferencia() == null ? BigDecimal.ZERO : d.getDiferencia();
                    filas.add(new Object[]{fecha(a.getFecha()), "AJUSTE", cantidadConSigno(dif), dep,
                        a.getUsuario() == null ? "-" : a.getUsuario().getNombreCompleto(),
                        a.getMotivo() == null ? "-" : a.getMotivo()});
                }
            }
        } catch (Exception ex) { errores.add("ajustes: " + ex.getMessage()); }
        try {
            for (Compra c : new CompraService().listarTodos()) {
                if (!"CONFIRMADA".equalsIgnoreCase(c.getEstado()) || c.getDetalles() == null) continue;
                String dep = nombreDeposito(c.getDeposito());
                if (!incluyeDeposito(deposito, dep)) continue;
                for (CompraDetalle d : c.getDetalles()) {
                    if (d.getProducto() == null || d.getProducto().getIdProducto() != idProducto) continue;
                    BigDecimal n = d.getCantidadStock();
                    filas.add(new Object[]{fecha(c.getFecha()), "COMPRA / ENTRADA", cantidadConSigno(n), dep,
                        c.getUsuario() == null ? "-" : c.getUsuario().getNombreCompleto(),
                        "Compra #" + c.getIdCompra()});
                }
            }
        } catch (Exception ex) { errores.add("compras: " + ex.getMessage()); }
        try {
            for (SalidaStock s : new SalidaStockService().listarTodas()) {
                if (!"CONFIRMADA".equalsIgnoreCase(s.getEstado()) || s.getDetalles() == null) continue;
                String dep = nombreDeposito(s.getDeposito());
                if (!incluyeDeposito(deposito, dep)) continue;
                for (SalidaStockDetalle d : s.getDetalles()) {
                    if (d.getProducto() == null || d.getProducto().getIdProducto() != idProducto) continue;
                    BigDecimal n = d.getCantidad();
                    filas.add(new Object[]{fechaPorMetodo(s), "SALIDA", n == null ? "0" : "-" + formatear(n.abs()), dep,
                        s.getUsuario() == null ? "-" : s.getUsuario().getNombreCompleto(),
                        "Salida #" + s.getIdSalida()});
                }
            }
        } catch (Exception ex) { errores.add("salidas: " + ex.getMessage()); }
        filas.sort((x,y) -> {
            try { return java.time.LocalDateTime.parse(String.valueOf(y[0]), fechaFormato)
                    .compareTo(java.time.LocalDateTime.parse(String.valueOf(x[0]), fechaFormato)); }
            catch (Exception ex) { return 0; }
        });
        for (Object[] f : filas) modeloMovimientos.addRow(f);
        lblConteo.setText(filas.size() + " movimientos");
        lblAviso.setText(errores.isEmpty()
                ? "Compras, salidas y ajustes confirmados. Entradas manuales pendientes de integración."
                : "No se pudo consultar: " + String.join("; ", errores));
    }

    private boolean incluyeDeposito(String filtro, String nombre) {
        return "Todos los depósitos".equals(filtro) || filtro.equals(nombre);
    }
    private String nombreDeposito(model.Deposito d) {
        return d == null ? "Sin depósito" : d.getNombre();
    }
    private String cantidadConSigno(BigDecimal n) {
        if (n == null) return "0";
        return (n.signum() > 0 ? "+" : "") + formatear(n);
    }
    private String fecha(Object valor) {
        if (valor == null) return "-";
        if (valor instanceof java.time.LocalDateTime) return ((java.time.LocalDateTime) valor).format(fechaFormato);
        if (valor instanceof java.sql.Timestamp) return ((java.sql.Timestamp) valor).toLocalDateTime().format(fechaFormato);
        if (valor instanceof java.time.LocalDate) return ((java.time.LocalDate) valor).atStartOfDay().format(fechaFormato);
        if (valor instanceof java.util.Date) return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(valor);
        return valor.toString();
    }
    private String fechaPorMetodo(Object objeto) {
        for (String metodo : new String[]{"getFecha", "getFechaSalida", "getFechaCreacion"}) {
            try { return fecha(objeto.getClass().getMethod(metodo).invoke(objeto)); }
            catch (ReflectiveOperationException ignored) { }
        }
        return "-";
    }
    private String formatear(BigDecimal n) {
        if(n==null) return "0";
        return NumberFormat.getNumberInstance(new Locale("es","AR")).format(n.stripTrailingZeros());
    }
    private class TipoRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col) {
            JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,selected,focus,row,col);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI",Font.BOLD,11));
            if(!selected) {
                boolean mas=String.valueOf(v).contains("COMPRA");
                boolean menos=String.valueOf(v).contains("SALIDA");
                l.setBackground(mas?new Color(225,243,232):menos?new Color(252,231,231):new Color(229,239,251));
                l.setForeground(mas?GREEN:menos?RED:BLUE);
            }
            return l;
        }
    }
    private Icon icon(String tipo,Color color) {
        return new Icon() {
            @Override public int getIconWidth(){return 19;}
            @Override public int getIconHeight(){return 19;}
            @Override public void paintIcon(Component c,Graphics g,int x,int y) {
                Graphics2D d=(Graphics2D)g.create();
                d.translate(x,y);
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                d.setColor(color);
                d.setStroke(new BasicStroke(2.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                switch(tipo) {
                    case "refresh": d.drawArc(3,3,13,13,35,285);d.drawLine(15,3,16,9);d.drawLine(16,9,11,8);break;
                    case "close": d.drawLine(4,4,15,15);d.drawLine(15,4,4,15);break;
                    case "history": d.drawArc(3,3,13,13,0,300);d.drawLine(9,5,9,10);d.drawLine(9,10,13,12);break;
                    case "warehouse": d.drawLine(2,7,9,2);d.drawLine(9,2,17,7);d.drawRect(3,7,13,10);d.drawRect(7,11,5,6);break;
                    default: d.drawRect(4,5,11,11);d.drawLine(4,9,15,9);break;
                }
                d.dispose();
            }
        };
    }
}
