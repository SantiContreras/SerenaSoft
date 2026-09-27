package Diseños;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TablaEstilo {

    public static void aplicar(JTable tabla, JScrollPane scroll) {

        //------------------------------------
        // TABLA
        //------------------------------------
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(220, 235, 255));
        tabla.setSelectionForeground(Color.BLACK);

        //------------------------------------
        // HEADER
        //------------------------------------
        //------------------------------------
// HEADER
//------------------------------------
        JTableHeader header = tabla.getTableHeader();

        header.setPreferredSize(new java.awt.Dimension(0, 36));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {

            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);

                setBackground(new Color(18, 56, 120));   // Azul Serena
                setForeground(Color.WHITE);

                setFont(new Font("Segoe UI", Font.BOLD, 13));

                setBorder(BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        1,
                        new Color(12, 38, 90)));

                return this;
            }

        });

        //------------------------------------
        // CENTRAR TEXTO DEL HEADER
        //------------------------------------
        DefaultTableCellRenderer renderer
                = (DefaultTableCellRenderer) header.getDefaultRenderer();

        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        //------------------------------------
        // CELDAS
        //------------------------------------
        DefaultTableCellRenderer celdas = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component c = super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);

                if (!isSelected) {

                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(247, 249, 252));
                    }

                }

                return c;

            }

        };

        tabla.setDefaultRenderer(Object.class, celdas);

    }

}
