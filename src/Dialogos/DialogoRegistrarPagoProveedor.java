package Dialogos;

import Configuracion.conexion;
import services.PagoCompraService;
import sesion.SesionUsuario;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

/** Pago real asociado a una compra confirmada. Nunca usa importes simulados. */
public class DialogoRegistrarPagoProveedor extends JDialog {
    private static final Color AZUL=new Color(20,53,110), VERDE=new Color(20,137,86);
    private final int proveedor;
    private final long compraPreseleccionada;
    private final JToggleButton pagarTodo=new JToggleButton("PAGAR TODO",true), pagarParte=new JToggleButton("PAGO PARCIAL");
    private final JComboBox<Opcion> compras=new JComboBox<>(), medios=new JComboBox<>();
    private final JTextField importe=new JTextField(15),referencia=new JTextField(20);
    private final JTextArea observaciones=new JTextArea(2,30);
    private final JLabel deuda=new JLabel("Saldo: —"), aviso=new JLabel(" ");
    private final PagoCompraService servicio=new PagoCompraService();
    private static final class Opcion {
        final int id;final String texto;final BigDecimal saldo;
        Opcion(int i,String t,BigDecimal s){id=i;texto=t;saldo=s;}
        public String toString(){return texto;}
    }
    public DialogoRegistrarPagoProveedor(Window parent,int idProveedor,String razonSocial,double saldoIgnorado) {
        this(parent,idProveedor,razonSocial,saldoIgnorado,-1);
    }
    public DialogoRegistrarPagoProveedor(Window parent,int idProveedor,String razonSocial,double saldoIgnorado,long compraPreferida) {
        super(parent,"Registrar pago a proveedor",ModalityType.APPLICATION_MODAL);
        this.proveedor=idProveedor;this.compraPreseleccionada=compraPreferida;
        setSize(780,615);setMinimumSize(new Dimension(720,590));setLocationRelativeTo(parent);
        JPanel main=new JPanel(new BorderLayout());main.setBackground(new Color(245,248,253));
        JPanel cabecera=new JPanel(new BorderLayout(12,6));cabecera.setBackground(new Color(17,49,105));
        cabecera.setBorder(BorderFactory.createEmptyBorder(21,27,21,27));
        JLabel h=new JLabel("PAGAR COMPRA PENDIENTE");h.setFont(new Font("Segoe UI",Font.BOLD,22));h.setForeground(Color.WHITE);
        JLabel sub=new JLabel(razonSocial);sub.setFont(new Font("Segoe UI",Font.PLAIN,15));sub.setForeground(new Color(218,231,251));
        JPanel ht=new JPanel(new GridLayout(2,1,0,7));ht.setOpaque(false);ht.add(h);ht.add(sub);cabecera.add(ht,BorderLayout.CENTER);
        main.add(cabecera,BorderLayout.NORTH);
        JPanel cuerpo=new JPanel(new BorderLayout(0,12));cuerpo.setOpaque(false);cuerpo.setBorder(BorderFactory.createEmptyBorder(20,25,12,25));
        JPanel fields=new JPanel(new GridBagLayout());fields.setBackground(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(215,225,240)),BorderFactory.createEmptyBorder(14,18,14,18)));
        GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(7,6,7,6);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;
        estilizarCombo(compras);estilizarCombo(medios);estilizarCampo(importe);estilizarCampo(referencia);
        observaciones.setFont(new Font("Segoe UI",Font.PLAIN,14));observaciones.setLineWrap(true);observaciones.setWrapStyleWord(true);
        observaciones.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        JScrollPane nota=new JScrollPane(observaciones);nota.setPreferredSize(new Dimension(100,65));nota.setBorder(BorderFactory.createLineBorder(new Color(201,216,234)));
        deuda.setFont(new Font("Segoe UI",Font.BOLD,19));deuda.setForeground(new Color(191,62,53));
        addRow(fields,g,0,"Compra pendiente",compras);
        addRow(fields,g,1,"SALDO A PAGAR",deuda);
        JPanel opciones=new JPanel(new GridLayout(1,2,12,0));opciones.setOpaque(false);
        opciones.setPreferredSize(new Dimension(340,42));
        ButtonGroup grupo=new ButtonGroup();grupo.add(pagarTodo);grupo.add(pagarParte);
        estilizarOpcion(pagarTodo);estilizarOpcion(pagarParte);
        pagarTodo.setFont(new Font("Segoe UI",Font.BOLD,13));pagarParte.setFont(new Font("Segoe UI",Font.BOLD,13));
        opciones.add(pagarTodo);opciones.add(pagarParte);
        addRow(fields,g,2,"Tipo de pago",opciones);
        addRow(fields,g,3,"Importe ($)",importe);
        addRow(fields,g,4,"Medio de pago",medios);
        addRow(fields,g,5,"Referencia",referencia);
        addRow(fields,g,6,"Observaciones",nota);
        cuerpo.add(fields,BorderLayout.CENTER);
        JLabel notaCaja=new JLabel("EFECTIVO: requiere caja propia abierta y registra salida  |  TRANSFERENCIA: no descuenta efectivo");
        notaCaja.setFont(new Font("Segoe UI",Font.PLAIN,12));notaCaja.setForeground(new Color(72,93,125));
        cuerpo.add(notaCaja,BorderLayout.SOUTH);main.add(cuerpo,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new BorderLayout());bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(215,225,240)),BorderFactory.createEmptyBorder(11,22,11,22)));
        aviso.setForeground(new Color(180,62,43));bottom.add(aviso,BorderLayout.CENTER);
        JPanel botones=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));botones.setOpaque(false);
        JButton cancelar=new BotonSerena("CANCELAR",new Color(100,115,135),"close");
        JButton guardar=new BotonSerena("CONFIRMAR PAGO",VERDE,"check");
        cancelar.addActionListener(e->dispose());guardar.addActionListener(e->registrar());
        botones.add(cancelar);botones.add(guardar);bottom.add(botones,BorderLayout.EAST);main.add(bottom,BorderLayout.SOUTH);
        setContentPane(main);
        compras.addActionListener(e->{Opcion o=(Opcion)compras.getSelectedItem();if(o!=null){deuda.setText(formatear(o.saldo));actualizarImporte();}});
        pagarTodo.addActionListener(e->{actualizarImporte();refrescarOpciones();});
        pagarParte.addActionListener(e->{importe.setEditable(true);refrescarOpciones();importe.requestFocusInWindow();importe.selectAll();});
        refrescarOpciones();
        cargar();
    }
    private static String formatear(BigDecimal monto){return java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es","AR")).format(monto);}
    private void estilizarCampo(JTextField t){
        t.setFont(new Font("Segoe UI",Font.PLAIN,14));
        t.setPreferredSize(new Dimension(180,40));
        t.setBackground(Color.WHITE);t.setForeground(new Color(22,48,86));
        t.setCaretColor(AZUL);t.setSelectionColor(new Color(192,219,252));
        t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(185,204,230),1,true),BorderFactory.createEmptyBorder(7,12,7,12)));
    }
    private void estilizarCombo(JComboBox<?> c){
        c.setFont(new Font("Segoe UI",Font.PLAIN,14));
        c.setBackground(Color.WHITE);c.setForeground(new Color(22,48,86));
        c.setFocusable(false);c.setPreferredSize(new Dimension(180,42));
        c.setBorder(BorderFactory.createLineBorder(new Color(185,204,230),1,true));
        c.setRenderer(new javax.swing.DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean selected,boolean focus){
                JLabel label=(JLabel)super.getListCellRendererComponent(list,value,index,selected,focus);
                label.setFont(new Font("Segoe UI",Font.PLAIN,14));
                label.setBorder(BorderFactory.createEmptyBorder(9,12,9,12));
                label.setOpaque(true);
                label.setBackground(selected?new Color(223,237,255):Color.WHITE);
                label.setForeground(new Color(22,48,86));
                return label;
            }
        });
    }
    private void estilizarOpcion(JToggleButton b){
        b.setOpaque(true);b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(true);b.setBorderPainted(true);b.setMargin(new Insets(4,10,4,10));
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setPreferredSize(new Dimension(160,42));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setBorder(BorderFactory.createLineBorder(new Color(188,207,233),1,true));
    }
    private void refrescarOpciones(){
        for(JToggleButton b:new JToggleButton[]{pagarTodo,pagarParte}){
            boolean active=b.isSelected();
            b.setBackground(active?new Color(224,237,255):Color.WHITE);
            b.setForeground(active?AZUL:new Color(78,98,125));
            b.setBorder(BorderFactory.createLineBorder(active?new Color(48,110,204):new Color(188,207,233),1,true));
        }
    }
    private void actualizarImporte(){
        Opcion o=(Opcion)compras.getSelectedItem();
        if(o!=null && pagarTodo.isSelected())importe.setText(o.saldo.toPlainString());
        importe.setEditable(pagarParte.isSelected());
        importe.setBackground(pagarParte.isSelected()?Color.WHITE:new Color(238,244,251));
    }

    private void addRow(JPanel p,GridBagConstraints g,int y,String nombre,JComponent input){
        g.gridy=y;g.gridx=0;g.weightx=0;JLabel l=new JLabel(nombre);l.setForeground(AZUL);l.setFont(new Font("Segoe UI",Font.BOLD,12));p.add(l,g);
        g.gridx=1;g.weightx=1;p.add(input,g);
    }
    private void cargar(){
        try(Connection c=conexion.getConexion()){
            try(PreparedStatement p=c.prepareStatement("SELECT c.id_compra,c.numero_comprobante,c.total-COALESCE((SELECT SUM(cp.monto) FROM compra_pago cp WHERE cp.id_compra=c.id_compra),0) saldo FROM compra c WHERE c.id_proveedor=? AND c.estado='CONFIRMADA' ORDER BY c.id_compra DESC")){
                p.setInt(1,proveedor);try(ResultSet r=p.executeQuery()){while(r.next()){
                    BigDecimal saldo=r.getBigDecimal(3);if(saldo.signum()>0)compras.addItem(new Opcion(r.getInt(1),"Compra #"+r.getInt(1)+" · "+(r.getString(2)==null?"Sin comprobante":r.getString(2)),saldo));
                }}
            }
            try(PreparedStatement p=c.prepareStatement("SELECT id_medio_pago,nombre FROM medio_pago WHERE activo=1 ORDER BY orden_visual,id_medio_pago");ResultSet r=p.executeQuery()){
                while(r.next())medios.addItem(new Opcion(r.getInt(1),r.getString(2),BigDecimal.ZERO));
            }
            if(compraPreseleccionada>0){
                boolean encontrada=false;
                for(int i=0;i<compras.getItemCount();i++)if(compras.getItemAt(i).id==compraPreseleccionada){compras.setSelectedIndex(i);encontrada=true;break;}
                if(!encontrada)aviso.setText("La compra seleccionada ya no tiene saldo pendiente.");
            }
            if(compras.getItemCount()==0)aviso.setText("No hay compras confirmadas con saldo pendiente.");
            actualizarImporte();
        }catch(SQLException ex){aviso.setText("Error al cargar: "+ex.getMessage());}
    }
    private static BigDecimal parseMonto(String valor){
        String t=valor.trim().replace("$","").replace(" ","");
        if(t.contains(","))t=t.replace(".","").replace(",",".");
        return new BigDecimal(t);
    }
    private void registrar(){
        Opcion compra=(Opcion)compras.getSelectedItem(),medio=(Opcion)medios.getSelectedItem();
        if(compra==null||medio==null){JOptionPane.showMessageDialog(this,"Seleccioná una compra pendiente y un medio de pago.");return;}
        try{
            BigDecimal monto=parseMonto(importe.getText());
            if(monto.signum()<=0||monto.scale()>2||monto.compareTo(compra.saldo)>0)throw new IllegalArgumentException("Importe inválido o mayor al saldo.");
            int usuario=SesionUsuario.getIdUsuario();if(usuario<=0)throw new IllegalArgumentException("No hay usuario autenticado.");
            int ok=JOptionPane.showOptionDialog(this,"Compra #"+compra.id+"\nPago: $"+monto+"\nMedio: "+medio.texto+"\n¿Confirmar pago?","Confirmación",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Confirmar pago","Volver"},"Volver");
            if(ok!=0)return;
            long id=servicio.registrar(compra.id,usuario,medio.id,monto,referencia.getText().trim(),observaciones.getText().trim());
            JOptionPane.showMessageDialog(this,"Pago #"+id+" registrado correctamente.\nLa cuenta corriente y Caja se actualizarán al refrescar.");
            dispose();
        }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"No se pudo registrar el pago",JOptionPane.ERROR_MESSAGE);}
    }
}
