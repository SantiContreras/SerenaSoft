package services;
import Configuracion.conexion;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/** Ticket no fiscal configurable. Conserva impresión rápida y vista previa. */
public final class TicketVentaJasper {
    private TicketVentaJasper() {}
    public static void mostrar(long idVenta) throws Exception {
        JasperViewer.viewReport(preparar(idVenta), false);
    }
    public static void imprimirRapido(long idVenta) throws Exception {
        if (PrintServiceLookup.lookupDefaultPrintService() == null)
            throw new IllegalStateException("No hay una impresora predeterminada configurada en Windows.");
        JasperPrintManager.printReport(preparar(idVenta), false);
    }
    private static JasperPrint preparar(long idVenta) throws Exception {
        if (idVenta <= 0) throw new IllegalArgumentException("ID de venta inválido");
        try (InputStream in = TicketVentaJasper.class.getResourceAsStream("/Reportes/ticket_venta.jrxml")) {
            if (in == null) throw new IllegalStateException("Falta /reportes/ticket_venta.jrxml");
            JasperReport reporte = JasperCompileManager.compileReport(in);
            Properties cfg = new Properties();
            try (InputStream archivo = TicketVentaJasper.class.getResourceAsStream("/Reportes/comercio_ticket.properties")) {
                if (archivo != null) cfg.load(new java.io.InputStreamReader(archivo, java.nio.charset.StandardCharsets.UTF_8));
            }
            Map<String,Object> p = new HashMap<>();
            p.put("VENTA_ID", idVenta);
            p.put("COMERCIO_NOMBRE", cfg.getProperty("nombre", "NOMBRE DEL COMERCIO"));
            p.put("COMERCIO_RAZON", cfg.getProperty("razon_social", "Configurar razón social"));
            p.put("COMERCIO_CUIT", cfg.getProperty("cuit", "Sin configurar"));
            p.put("COMERCIO_DIRECCION", cfg.getProperty("direccion", "Configurar domicilio"));
            p.put("COMERCIO_TELEFONO", cfg.getProperty("telefono", "Sin configurar"));
            p.put("MENSAJE_FINAL", cfg.getProperty("mensaje", "¡Gracias por su compra!"));
            try (Connection c = conexion.getConexion()) {
                // Empresa configurada desde el módulo Empresa; evita datos de prueba del .properties.
                try (java.sql.PreparedStatement st=c.prepareStatement("SELECT nombre_comercial,razon_social,cuit,direccion,telefono FROM empresa WHERE activo=1 ORDER BY id_empresa LIMIT 1");java.sql.ResultSet r=st.executeQuery()) {
                    if(r.next()) {
                        p.put("COMERCIO_NOMBRE",r.getString(1));p.put("COMERCIO_RAZON",r.getString(2));p.put("COMERCIO_CUIT",r.getString(3));p.put("COMERCIO_DIRECCION",r.getString(4));p.put("COMERCIO_TELEFONO",r.getString(5));
                    }
                }
                Dao.ConfiguracionComprobanteDao.Datos opciones=Dao.ConfiguracionComprobanteDao.cargar();
                p.put("MENSAJE_FINAL",opciones.pie);
                if(!opciones.nombre)p.put("COMERCIO_NOMBRE", "");
                if(!opciones.cuit)p.put("COMERCIO_CUIT", "");
                if(!opciones.direccion)p.put("COMERCIO_DIRECCION", "");
                if(!opciones.telefono)p.put("COMERCIO_TELEFONO", "");
                JasperPrint print = JasperFillManager.fillReport(reporte, p, c);
                if (print.getPages().isEmpty()) throw new IllegalStateException("La venta no tiene detalles para imprimir");
                return print;
            }
        }
    }
}
