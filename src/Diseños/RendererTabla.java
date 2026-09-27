package Diseños;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererTabla extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean selected,
            boolean focused,
            int row,
            int column){

        super.getTableCellRendererComponent(
                table,value,selected,focused,row,column);

        if(!selected){

            if(row%2==0)
                setBackground(Color.WHITE);
            else
                setBackground(new Color(247,248,250));

            setForeground(new Color(50,50,50));

        }

        return this;
    }

}