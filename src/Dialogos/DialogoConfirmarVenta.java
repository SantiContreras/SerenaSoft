package Dialogos;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Confirmación de cobro. No registra la venta: la transacción la realiza VentaService. */
public class DialogoConfirmarVenta extends JDialog {
    private static final Color AZUL = new Color(14,53,117), CELESTE = new Color(0,132,237), VERDE = new Color(19,145,79);
    private final BigDecimal total;
    private boolean confirmado;
    private final JComboBox<String> medios = new JComboBox<>(new String[]{"Efectivo","Transferencia","Débito","Crédito","QR"});
    private final JTextField recibido = new JTextField();
    private final JLabel cambio = new JLabel();
    private final JTextArea observaciones = new JTextArea(2,20);
    private final JCheckBox imprimir = new JCheckBox("Ofrecer impresión al finalizar",true);
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es","AR"));
    private final String pagoInicial;
    public DialogoConfirmarVenta(Window parent,String cliente,String documento,String condicionIVA,String comprobante,String formaPago,
                                 double subtotal,double descuento,double total,double recibe,double cambioInicial) {
        super(parent,"Confirmar venta",ModalityType.APPLICATION_MODAL);
        this.total=BigDecimal.valueOf(total).setScale(2,java.math.RoundingMode.HALF_UP);
        this.pagoInicial=formaPago;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenido(cliente,comprobante,subtotal,descuento));
        medios.setSelectedItem(formaPago);
        recibido.setText(recibe>0 ? BigDecimal.valueOf(recibe).stripTrailingZeros().toPlainString() : "");
        actualizarCambio();
        recibido.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){actualizarCambio();}public void removeUpdate(DocumentEvent e){actualizarCambio();}public void changedUpdate(DocumentEvent e){actualizarCambio();}});
        medios.addActionListener(e -> { if(!"Efectivo".equals(getFormaPago())){recibido.setText(DialogoConfirmarVenta.this.total.toPlainString());recibido.setEnabled(false);}else{recibido.setEnabled(true);recibido.selectAll();} actualizarCambio();});
        getRootPane().registerKeyboardAction(e -> dispose(),KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW);
        setSize(760,740);setMinimumSize(new Dimension(700,690));setLocationRelativeTo(parent);
        SwingUtilities.invokeLater(() -> recibido.requestFocusInWindow());
    }
    private JPanel crearContenido(String cliente,String comprobante,double subtotal,double descuento){
        JPanel root=new JPanel(new BorderLayout());root.setBackground(new Color(240,246,255));
        JPanel cab=new JPanel(new BorderLayout());cab.setBackground(AZUL);cab.setBorder(new EmptyBorder(22,27,22,27));
        JLabel titulo=new JLabel("  ✓  CONFIRMAR VENTA");titulo.setFont(new Font("SansSerif",Font.BOLD,25));titulo.setForeground(Color.WHITE);cab.add(titulo,BorderLayout.NORTH);
        JLabel subt=new JLabel("       Revisá el cobro antes de registrar la operación");subt.setForeground(new Color(195,220,255));cab.add(subt,BorderLayout.SOUTH);root.add(cab,BorderLayout.NORTH);
        JPanel cuerpo=new JPanel();cuerpo.setOpaque(false);cuerpo.setBorder(new EmptyBorder(18,25,12,25));cuerpo.setLayout(new BoxLayout(cuerpo,BoxLayout.Y_AXIS));
        JPanel resumen=tarjeta();resumen.setLayout(new BorderLayout(12,7));
        JLabel totalTitulo=new JLabel("TOTAL A COBRAR");totalTitulo.setFont(new Font("SansSerif",Font.BOLD,15));totalTitulo.setForeground(AZUL);
        JLabel totalLabel=new JLabel(moneda.format(total));totalLabel.setFont(new Font("SansSerif",Font.BOLD,35));totalLabel.setForeground(VERDE);
        JPanel cifras=new JPanel(new GridLayout(2,1));cifras.setOpaque(false);cifras.add(totalTitulo);cifras.add(totalLabel);resumen.add(cifras,BorderLayout.CENTER);
        JLabel info=new JLabel("<html><div style='text-align:right'>"+esc(cliente)+"<br>"+esc(comprobante)+"<br>Subtotal: "+moneda.format(subtotal)+"<br>Descuento: "+moneda.format(descuento)+"</div></html>");info.setForeground(AZUL);resumen.add(info,BorderLayout.EAST);cuerpo.add(resumen);cuerpo.add(Box.createVerticalStrut(13));
        JPanel pago=tarjeta();pago.setLayout(new GridBagLayout());GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(9,5,9,5);g.fill=GridBagConstraints.HORIZONTAL;
        fila(pago,g,0,"Medio de pago",medios);fila(pago,g,1,"Importe recibido",recibido);
        cambio.setFont(new Font("SansSerif",Font.BOLD,23));cambio.setForeground(VERDE);fila(pago,g,2,"Cambio",cambio);
        medios.setFont(new Font("SansSerif",Font.BOLD,16));medios.setBackground(Color.WHITE);recibido.setFont(new Font("SansSerif",Font.BOLD,21));recibido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(91,154,231),2,true),new EmptyBorder(9,12,9,12)));
        cuerpo.add(pago);cuerpo.add(Box.createVerticalStrut(12));
        JPanel obs=tarjeta();obs.setLayout(new BorderLayout(5,7));JLabel lblObs=new JLabel("Observaciones (opcional)");lblObs.setForeground(AZUL);lblObs.setFont(new Font("SansSerif",Font.BOLD,13));obs.add(lblObs,BorderLayout.NORTH);observaciones.setLineWrap(true);observaciones.setWrapStyleWord(true);observaciones.setBorder(new EmptyBorder(6,8,6,8));obs.add(new JScrollPane(observaciones),BorderLayout.CENTER);cuerpo.add(obs);
        imprimir.setOpaque(false);imprimir.setForeground(AZUL);imprimir.setAlignmentX(Component.LEFT_ALIGNMENT);cuerpo.add(Box.createVerticalStrut(9));cuerpo.add(imprimir);
        JScrollPane desplazamiento = new JScrollPane(cuerpo); desplazamiento.setBorder(null); desplazamiento.getVerticalScrollBar().setUnitIncrement(18); root.add(desplazamiento,BorderLayout.CENTER);
        JPanel pie=new JPanel(new GridLayout(1,2,14,0));pie.setOpaque(false);pie.setBorder(new EmptyBorder(9,25,22,25));
        JButton cancelar=boton("CANCELAR",AZUL);cancelar.addActionListener(e -> dispose());
        JButton aceptar=boton("CONFIRMAR COBRO",VERDE);aceptar.addActionListener(e -> confirmar());
        pie.add(cancelar);pie.add(aceptar);root.add(pie,BorderLayout.SOUTH);getRootPane().setDefaultButton(aceptar);
        return root;
    }
    private static String esc(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
    private JPanel tarjeta(){JPanel p=new JPanel();p.setBackground(Color.WHITE);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(179,207,241),1,true),new EmptyBorder(14,17,14,17)));p.setAlignmentX(Component.LEFT_ALIGNMENT);return p;}
    private void fila(JPanel panel,GridBagConstraints g,int y,String etiqueta,JComponent control){g.gridy=y;g.gridx=0;g.weightx=.36;JLabel l=new JLabel(etiqueta);l.setFont(new Font("SansSerif",Font.BOLD,14));l.setForeground(AZUL);panel.add(l,g);g.gridx=1;g.weightx=.64;panel.add(control,g);}
    private JButton boton(String t,Color base){JButton b=new JButton(t);b.setFont(new Font("SansSerif",Font.BOLD,16));b.setForeground(Color.WHITE);b.setBackground(base);b.setOpaque(true);b.setContentAreaFilled(true);b.setBorderPainted(false);b.setBorder(new EmptyBorder(17,9,17,9));b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.addMouseListener(new MouseAdapter(){public void mouseEntered(MouseEvent e){b.setBackground(base.brighter());}public void mouseExited(MouseEvent e){b.setBackground(base);}});return b;}
    private BigDecimal parsear(){String s=recibido.getText().trim().replace("$","").replace(" ","");if(s.contains(","))s=s.replace(".","").replace(',','.');return new BigDecimal(s);}
    private void actualizarCambio(){try{BigDecimal valor=parsear();BigDecimal dif=valor.subtract(total);cambio.setText(moneda.format(dif.max(BigDecimal.ZERO)));cambio.setForeground(dif.signum()<0?new Color(193,53,58):VERDE);}catch(Exception e){cambio.setText("—");}}
    private void confirmar(){try{if(parsear().compareTo(total)<0){JOptionPane.showMessageDialog(this,"El importe recibido es inferior al total.","Cobro insuficiente",JOptionPane.WARNING_MESSAGE);return;}confirmado=true;dispose();}catch(Exception e){JOptionPane.showMessageDialog(this,"Ingresá un importe válido.","Importe incorrecto",JOptionPane.WARNING_MESSAGE);}}
    public boolean isVentaConfirmada(){return confirmado;}
    public boolean isImprimirComprobante(){return imprimir.isSelected();}
    public String getObservaciones(){return observaciones.getText().trim();}
    public String getFormaPago(){return String.valueOf(medios.getSelectedItem());}
    public BigDecimal getImporteRecibido(){return parsear();}
}
