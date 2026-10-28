package services;

import Configuracion.conexion;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/** Vista previa e impresión de ticket no fiscal desde una venta ya confirmada. */
public final class TicketVentaJasper {
    private TicketVentaJasper() {}
    public static void mostrar(long idVenta) throws Exception {
        try(InputStream in=TicketVentaJasper.class.getResourceAsStream("/reportes/ticket_venta.jrxml")) {
            if(in==null) throw new IllegalStateException("Falta reportes/ticket_venta.jrxml en el classpath");
            JasperReport reporte=JasperCompileManager.compileReport(in);
            Map<String,Object> parametros=new HashMap<>();
            parametros.put("VENTA_ID",Long.valueOf(idVenta));
            try(Connection c=conexion.getConexion()) {
                JasperPrint print=JasperFillManager.fillReport(reporte,parametros,c);
                if(print.getPages().isEmpty()) throw new IllegalStateException("La venta no tiene detalles para imprimir");
                JasperViewer.viewReport(print,false);
            }
        }
    }
}
