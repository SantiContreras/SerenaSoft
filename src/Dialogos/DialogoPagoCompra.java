package Dialogos;

import Configuracion.conexion;
import services.PagoCompraService;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Pago total o parcial de una compra confirmada. */
public class DialogoPagoCompra extends JDialog {
    private final long compra;
    private final PagoCompraService servicio=new PagoCompraService();
    private final JLabel saldo=new JLabel();
    private final JTextField importe=new JTextField();
    private final JTextField referencia=new JTextField();
    private final JTextField observacion=new JTextField();
    private final JComboBox<Medio> medio=new JComboBox<>();
    private final int usuario;
    public DialogoPagoCompra(Window owner,long compra,int usuario) {
        super(owner,"Pago a proveedor - compra #"+compra,ModalityType.APPLICATION_MODAL);
        this.compra=compra;this.usuario=usuario;
        setSize(570,410);setMinimumSize(new Dimension(520,390));setLocationRelativeTo(owner);
        JPanel panel=new JPanel(new BorderLayout(0,16));panel.setBackground(new Color(245,248,253));panel.setBorder(new EmptyBorder(20,22,20,22));setContentPane(panel);
        JLabel titulo=new JLabel("REGISTRAR PAGO A PROVEEDOR");titulo.setForeground(new Color(15,47,105));titulo.setFont(new Font("Segoe UI",Font.BOLD,20));panel.add(titulo,BorderLayout.NORTH);
        JPanel campos=new JPanel(new GridLayout(5,2,12,10));campos.setOpaque(false);
        campos.add(new JLabel("Compra:"));campos.add(new JLabel("#"+compra));
        campos.add(new JLabel("Saldo pendiente:"));campos.add(saldo);
        campos.add(new JLabel("Importe a pagar:"));campos.add(importe);
        campos.add(new JLabel("Medio de pago:"));campos.add(medio);
        campos.add(new JLabel("Referencia:"));campos.add(referencia);
        panel.add(campos,BorderLayout.CENTER);
        JPanel pie=new JPanel(new BorderLayout(0,10));pie.setOpaque(false);
        pie.add(observacion,BorderLayout.NORTH);observacion.setToolTipText("Observación opcional");
        JPanel botones=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));botones.setOpaque(false);
        JButton cancelar=new JButton("CANCELAR");JButton pagar=new JButton("REGISTRAR PAGO");
        pagar.setBackground(new Color(23,135,90));pagar.setForeground(Color.WHITE);pagar.setOpaque(true);
        cancelar.addActionListener(e->dispose());pagar.addActionListener(e->guardar());
        botones.add(cancelar);botones.add(pagar);pie.add(botones,BorderLayout.SOUTH);panel.add(pie,BorderLayout.SOUTH);
        try {
            PagoCompraService.Estado e=servicio.consultar(compra);
            saldo.setText("$ "+e.pendiente+"  ("+e.estado+")");importe.setText(e.pendiente.toPlainString());
            try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement("SELECT id_medio_pago,nombre FROM medio_pago WHERE activo=1 ORDER BY orden_visual,id_medio_pago");ResultSet r=p.executeQuery()){
                while(r.next())medio.addItem(new Medio(r.getInt(1),r.getString(2)));
            }
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);pagar.setEnabled(false);}
    }
    private void guardar(){
        try {
            Medio m=(Medio)medio.getSelectedItem();if(m==null)throw new SQLException("Elegí un medio de pago.");
            BigDecimal valor=new BigDecimal(importe.getText().trim().replace(",","."));
            if(JOptionPane.showConfirmDialog(this,"¿Registrar pago de $"+valor+" para compra #"+compra+"?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            long id=servicio.registrar(compra,usuario,m.id,valor,referencia.getText().trim(),observacion.getText().trim());
            JOptionPane.showMessageDialog(this,"Pago #"+id+" registrado correctamente.");dispose();
        }catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"No se pudo registrar",JOptionPane.ERROR_MESSAGE);}
    }
    private static class Medio {final int id;final String nombre;Medio(int id,String nombre){this.id=id;this.nombre=nombre;}public String toString(){return nombre;}}
}
