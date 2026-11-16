package Paneles;

import services.BackupService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.*;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

/** Panel seguro: respaldos manuales y verificación SHA-256. Sin restauración destructiva. */
public class PanelConfigBackup extends JPanel {
    private final Preferences prefs=Preferences.userNodeForPackage(PanelConfigBackup.class);
    private final JTextField ruta=new JTextField();
    private final JTextField ejecutable=new JTextField();
    private final JLabel estado=new JLabel("Todavía no se verificó ningún respaldo");
    private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"Archivo SQL", "Tamaño (KB)", "Integridad"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(modelo);
    private final JButton crear=boton("CREAR RESPALDO",new Color(27,133,83));
    private final JButton verificar=boton("VERIFICAR",new Color(30,77,147));
    private final JButton abrir=boton("ABRIR CARPETA",new Color(91,102,120));
    private final JButton guardar=boton("GUARDAR CONFIGURACIÓN",new Color(30,77,147));
    public PanelConfigBackup(){
        setLayout(new BorderLayout(12,12));setBackground(Color.WHITE);setBorder(BorderFactory.createEmptyBorder(22,25,20,25));
        JLabel titulo=new JLabel("COPIAS DE SEGURIDAD");titulo.setFont(new Font("SansSerif",Font.BOLD,23));titulo.setForeground(new Color(16,52,110));
        JLabel nota=new JLabel("Respaldo manual de la base. No se modifica Ventas, Caja ni Stock.");nota.setForeground(new Color(95,104,115));
        JPanel cab=new JPanel(new GridLayout(2,1,0,4));cab.setOpaque(false);cab.add(titulo);cab.add(nota);add(cab,BorderLayout.NORTH);
        JPanel contenido=new JPanel();contenido.setOpaque(false);contenido.setLayout(new BoxLayout(contenido,BoxLayout.Y_AXIS));
        ruta.setFont(new Font("Segoe UI",Font.PLAIN,14));ruta.setPreferredSize(new Dimension(320,42));ejecutable.setFont(new Font("Segoe UI",Font.PLAIN,14));ejecutable.setPreferredSize(new Dimension(320,42));
        ruta.setText(prefs.get("backup.ruta",Paths.get(System.getProperty("user.home"),"SerenaSoft","Backups").toString()));
        ejecutable.setText(prefs.get("backup.mysqldump","mysqldump"));
        JPanel ubic=new JPanel(new BorderLayout(12,10));ubic.setOpaque(false);ubic.setBorder(BorderFactory.createTitledBorder("Ubicación de los respaldos"));
        JButton seleccionar=boton("SELECCIONAR",new Color(30,77,147));ubic.add(ruta,BorderLayout.CENTER);ubic.add(seleccionar,BorderLayout.EAST);
        contenido.add(ubic);contenido.add(Box.createVerticalStrut(12));
        JPanel bin=new JPanel(new BorderLayout(12,10));bin.setOpaque(false);bin.setBorder(BorderFactory.createTitledBorder("Herramienta mysqldump (MySQL/MariaDB)"));bin.add(ejecutable,BorderLayout.CENTER);contenido.add(bin);contenido.add(Box.createVerticalStrut(12));
        JPanel acciones=new JPanel(new FlowLayout(FlowLayout.LEFT,12,10));acciones.setOpaque(false);acciones.add(crear);acciones.add(verificar);acciones.add(abrir);acciones.add(guardar);contenido.add(acciones);
        JLabel aviso=new JLabel("Restauración automática deshabilitada: se realiza mediante procedimiento controlado.");aviso.setForeground(new Color(145,77,17));contenido.add(aviso);contenido.add(Box.createVerticalStrut(8));
        tabla.setFont(new Font("Segoe UI",Font.PLAIN,13));tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,13));tabla.setRowHeight(34);tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);JScrollPane sc=new JScrollPane(tabla);sc.setPreferredSize(new Dimension(600,220));
        JPanel hist=new JPanel(new BorderLayout());hist.setOpaque(false);hist.setBorder(BorderFactory.createTitledBorder("Historial de archivos locales"));hist.add(sc);contenido.add(hist);contenido.add(Box.createVerticalGlue());
        JScrollPane scroll=new JScrollPane(contenido);scroll.setBorder(null);scroll.getViewport().setBackground(Color.WHITE);scroll.getVerticalScrollBar().setUnitIncrement(16);add(scroll,BorderLayout.CENTER);
        estado.setFont(new Font("Segoe UI",Font.PLAIN,13));estado.setForeground(new Color(24,87,70));add(estado,BorderLayout.SOUTH);
        seleccionar.addActionListener(e->{JFileChooser fc=new JFileChooser(ruta.getText());fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)ruta.setText(fc.getSelectedFile().getAbsolutePath());});
        guardar.addActionListener(e->{prefs.put("backup.ruta",ruta.getText().trim());prefs.put("backup.mysqldump",ejecutable.getText().trim());estado.setText("Preferencias locales guardadas");});
        abrir.addActionListener(e->{try{Path p=Paths.get(ruta.getText());Files.createDirectories(p);Desktop.getDesktop().open(p.toFile());}catch(Exception ex){error(ex);}});
        crear.addActionListener(e->{guardar.doClick();crear.setEnabled(false);estado.setText("Generando respaldo... no cierres la aplicación");SwingWorker<BackupService.Resultado,Void> w=new SwingWorker<>(){protected BackupService.Resultado doInBackground()throws Exception{return BackupService.crear(Paths.get(ruta.getText().trim()),ejecutable.getText().trim());}protected void done(){crear.setEnabled(true);try{BackupService.Resultado r=get();estado.setText("Respaldo creado y SHA-256 calculado: "+r.archivo.getFileName());recargar();JOptionPane.showMessageDialog(PanelConfigBackup.this,"Respaldo generado:\n"+r.archivo+"\nVerificá periódicamente restaurándolo en una base de prueba.");}catch(Exception ex){error(ex);estado.setText("No se pudo completar el respaldo");}}};w.execute();});
        verificar.addActionListener(e->{int row=tabla.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Seleccioná un respaldo del historial");return;}try{Path p=Paths.get(ruta.getText()).resolve(modelo.getValueAt(row,0).toString());boolean ok=BackupService.verificar(p);modelo.setValueAt(ok?"SHA-256 correcto":"FALLÓ",row,2);estado.setText(ok?"Integridad del archivo verificada":"Archivo alterado o falta .sha256");}catch(Exception ex){error(ex);}});
        recargar();
    }
    private void recargar(){modelo.setRowCount(0);try{Path p=Paths.get(ruta.getText().trim());if(!Files.isDirectory(p))return;try(Stream<Path> files=Files.list(p)){files.filter(f->f.getFileName().toString().endsWith(".sql")).sorted((a,b)->b.getFileName().toString().compareTo(a.getFileName().toString())).limit(100).forEach(f->{try{modelo.addRow(new Object[]{f.getFileName().toString(),Files.size(f)/1024,"Sin verificar"});}catch(Exception ignored){}});}}catch(Exception ex){estado.setText("No se pudo leer el historial: "+ex.getMessage());}}
    private void error(Exception ex){Throwable t=ex;while(t.getCause()!=null)t=t.getCause();JOptionPane.showMessageDialog(this,t.getMessage(),"Error de respaldo",JOptionPane.ERROR_MESSAGE);}
    private static JButton boton(String texto, Color color) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D d=(Graphics2D)g.create();
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                d.setColor(isEnabled()?color:new Color(190,198,210));
                d.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                d.setColor(isEnabled()?Color.WHITE:new Color(65,75,90));
                d.setFont(getFont());FontMetrics fm=d.getFontMetrics();
                d.drawString(getText(),Math.max(8,(getWidth()-fm.stringWidth(getText()))/2),(getHeight()-fm.getHeight())/2+fm.getAscent());
                d.dispose();
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);b.setOpaque(false);b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12,18,12,18));
        b.setPreferredSize(new Dimension(Math.max(140,b.getFontMetrics(b.getFont()).stringWidth(texto)+42),43));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
