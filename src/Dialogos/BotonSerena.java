package Dialogos;
import java.awt.*;
import javax.swing.*;
/** Boton vectorial: conserva contraste incluso con LookAndFeel personalizados. */
public class BotonSerena extends JButton {
    private final Color fondo;
    private final String simbolo;
    public BotonSerena(String texto, Color fondo, String simbolo) {
        super(texto); this.fondo=fondo; this.simbolo=simbolo;
        setFont(new Font("Segoe UI",Font.BOLD,12)); setForeground(Color.WHITE);
        setOpaque(false);setContentAreaFilled(false);setBorderPainted(false);
        setFocusPainted(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(Math.max(130, texto.length()*9+48),42));
    }
    @Override protected void paintComponent(Graphics g){
        Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Color c=isEnabled()?fondo:new Color(145,157,174);
        if(getModel().isRollover()&&isEnabled())c=c.brighter();
        x.setColor(c);x.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
        x.setColor(Color.WHITE);x.setFont(getFont());FontMetrics fm=x.getFontMetrics();
        String texto=getText();int w=fm.stringWidth(texto);int y=(getHeight()-fm.getHeight())/2+fm.getAscent();
        if(simbolo!=null&&!simbolo.isEmpty()){
            int sx=Math.max(12,(getWidth()-w-30)/2);int sy=getHeight()/2;
            x.setStroke(new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            if("check".equals(simbolo)){x.drawLine(sx,sy,sx+5,sy+5);x.drawLine(sx+5,sy+5,sx+13,sy-6);}
            else if("plus".equals(simbolo)){x.drawLine(sx+6,sy-6,sx+6,sy+6);x.drawLine(sx,sy,sx+12,sy);}
            else if("close".equals(simbolo)){x.drawLine(sx,sy-5,sx+10,sy+5);x.drawLine(sx+10,sy-5,sx,sy+5);}
            else if("search".equals(simbolo)){x.drawOval(sx,sy-7,10,10);x.drawLine(sx+9,sy+2,sx+15,sy+8);}
            else if("edit".equals(simbolo)){x.drawLine(sx+1,sy+5,sx+12,sy-6);x.drawLine(sx+3,sy+7,sx+14,sy-4);}
            else if("eye".equals(simbolo)){x.drawOval(sx,sy-5,15,10);x.fillOval(sx+6,sy-2,4,4);}
            else if("refresh".equals(simbolo)){x.drawArc(sx,sy-7,14,14,35,280);x.drawLine(sx+12,sy-7,sx+16,sy-4);}
            else if("wallet".equals(simbolo)){x.drawRoundRect(sx,sy-6,15,12,3,3);x.drawLine(sx+9,sy-2,sx+15,sy-2);}
            x.drawString(texto,sx+23,y);
        }else{x.drawString(texto,(getWidth()-w)/2,y);}
        x.dispose();
    }
}
