package Dialogos;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public final class DialogoClienteFacturacionVentas extends JDialog {
 private final JTextField nombre=new JTextField(),documento=new JTextField();
 private final JComboBox<String> iva=new JComboBox<>(new String[]{"Consumidor Final","Responsable Inscripto","Monotributista","Exento"});
 private final JComboBox<String> tipo=new JComboBox<>(new String[]{"Minorista","Mayorista"});
 private final JComboBox<String> comprobante=new JComboBox<>(new String[]{"Ticket","Factura A","Factura B","Factura C"});
 private boolean confirmado;
 public DialogoClienteFacturacionVentas(Window parent,String n,String d,String i,String t,String c){
   super(parent,"Cliente y facturación",ModalityType.APPLICATION_MODAL);
   nombre.setText(n);documento.setText(d);iva.setSelectedItem(i);tipo.setSelectedItem(t);comprobante.setSelectedItem(c);
   JPanel principal=new JPanel(new BorderLayout(12,18));principal.setBorder(new EmptyBorder(24,28,24,28));
   principal.add(EstiloDialogosVentas.encabezado("▣","CLIENTE Y FACTURACIÓN","Datos del cliente y tipo de comprobante"),BorderLayout.NORTH);
   JPanel campos=new JPanel(new GridBagLayout());GridBagConstraints g=new GridBagConstraints();g.fill=GridBagConstraints.HORIZONTAL;g.weightx=1;g.insets=new Insets(8,4,8,4);
   fila(campos,g,0,"Cliente",nombre);fila(campos,g,1,"CUIT / DNI",documento);fila(campos,g,2,"Condición frente al IVA",iva);fila(campos,g,3,"Tipo de venta",tipo);fila(campos,g,4,"Comprobante",comprobante);
   principal.add(campos,BorderLayout.CENTER);
   JPanel pie=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
   JButton buscar=new JButton("⌕  Buscar cliente");buscar.addActionListener(e->{
      DialogoBuscarCliente b=new DialogoBuscarCliente(this);b.setVisible(true);
      if(b.isSeleccionado()){nombre.setText(b.getNombreSeleccionado());documento.setText(b.getDocumentoSeleccionado());iva.setSelectedItem(b.getCondicionIVASeleccionada());tipo.setSelectedItem(b.getTipoClienteSeleccionado());}
   });
   JButton cancelar=new JButton("✕  Cancelar");cancelar.addActionListener(e->dispose());
   JButton aceptar=new JButton("✓  Guardar cambios");aceptar.addActionListener(e->{confirmado=true;dispose();});
   pie.add(buscar);pie.add(cancelar);pie.add(aceptar);principal.add(pie,BorderLayout.SOUTH);
   setContentPane(principal);setSize(700,580);setMinimumSize(new Dimension(650,540));setLocationRelativeTo(parent);
   EstiloDialogosVentas.aplicar(this);getRootPane().setDefaultButton(aceptar);
   getRootPane().registerKeyboardAction(e->dispose(),KeyStroke.getKeyStroke("ESCAPE"),JComponent.WHEN_IN_FOCUSED_WINDOW);
 }
 private static void fila(JPanel p,GridBagConstraints g,int y,String label,JComponent control){
   g.gridy=y;g.gridx=0;g.weightx=0.36;p.add(new JLabel(label),g);
   g.gridx=1;g.weightx=0.64;control.setPreferredSize(new Dimension(310,48));p.add(control,g);
 }
 public boolean isConfirmado(){return confirmado;}
 public String getNombre(){String n=nombre.getText().trim();return n.isEmpty()?"Consumidor Final":n;}
 public String getDocumento(){return documento.getText().trim();}
 public String getCondicionIVA(){return String.valueOf(iva.getSelectedItem());}
 public String getTipoVenta(){return String.valueOf(tipo.getSelectedItem());}
 public String getComprobante(){return String.valueOf(comprobante.getSelectedItem());}
}
