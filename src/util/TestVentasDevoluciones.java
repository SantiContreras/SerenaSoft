package util;

import Configuracion.conexion;
import Dao.UsuarioDao;
import model.Usuario;
import sesion.SesionUsuario;
import services.VentaService;
import services.DevolucionVentaService;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Prueba de integración DESTRUCTIVA: seis ventas reales y seis operaciones
 * reales. Ejecutar únicamente en una base con datos ficticios y respaldo
 * reciente. Sin argumentos: solo prevalidación; con --ejecutar
 * CONFIRMO_DATOS_FICTICIOS: escribe.
 */
public final class TestVentasDevoluciones {

    private static final String MARCA = "TEST_SERENA_6CASOS_";
    private static final BigDecimal CERO = BigDecimal.ZERO;

    private static BigDecimal n(String s) {
        return new BigDecimal(s);
    }

    private static void ok(boolean b, String msg) {
        if (!b) {
            throw new IllegalStateException(msg);
        }
    }

    private static final class Articulo {

        final int id;
        final String cantidad;
        final String devuelta;
        final DevolucionVentaService.Destino destino;

        Articulo(int id, String cant, String dev, DevolucionVentaService.Destino destino) {
            this.id = id;
            cantidad = cant;
            devuelta = dev;
            this.destino = destino;
        }
    }

    private static final class Caso {

        final String titulo;
        final boolean anulacion;
        final Articulo[] articulos;

        Caso(String titulo, boolean anulacion, Articulo... articulos) {
            this.titulo = titulo;
            this.anulacion = anulacion;
            this.articulos = articulos;
        }
    }
    private static final Caso[] CASOS = {
        new Caso("01 Error cajero: anulación total con stock apto", true, new Articulo(9, "1", "1", DevolucionVentaService.Destino.APTO), new Articulo(11, "1", "1", DevolucionVentaService.Destino.APTO)),
        new Caso("02 Jamón cocido en mal estado: merma", false, new Articulo(22, "0.500", "0.500", DevolucionVentaService.Destino.MERMA)),
        new Caso("03 Leche: conservación dudosa, revisión", false, new Articulo(17, "1", "1", DevolucionVentaService.Destino.REVISION)),
        new Caso("04 Gaseosa: devolución parcial apta", false, new Articulo(1, "2", "1", DevolucionVentaService.Destino.APTO)),
        new Caso("05 Queso cremoso deteriorado: descarte", false, new Articulo(20, "0.250", "0.250", DevolucionVentaService.Destino.MERMA)),
        new Caso("06 Arroz apto + salame merma", false, new Articulo(9, "1", "1", DevolucionVentaService.Destino.APTO), new Articulo(23, "0.200", "0.200", DevolucionVentaService.Destino.MERMA))
    };

    private static BigDecimal stock(Connection c, int producto) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT cantidad FROM stock_producto WHERE id_producto=? AND id_deposito=1")) {
            p.setInt(1, producto);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? r.getBigDecimal(1) : CERO;
            }
        }
    }

    private static String estado(Connection c, long id) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT estado FROM venta WHERE id_venta=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                ok(r.next(), "No existe venta #" + id);
                return r.getString(1);
            }
        }
    }

    private static BigDecimal precio(Connection c, int id) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT precio_venta FROM producto WHERE id_producto=? AND activo=1 AND controla_stock=1")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                ok(r.next(), "Producto no apto para el test: " + id);
                return r.getBigDecimal(1);
            }
        }
    }

    private static long cuenta(Connection c, String tabla, String columna, long id) throws SQLException {
        String q = "SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + "=?";
        try (PreparedStatement p = c.prepareStatement(q)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                r.next();
                return r.getLong(1);
            }
        }
    }

    private static void preflight(Connection c) throws SQLException {
        ok("serena_soft".equalsIgnoreCase(c.getCatalog()), "Conectado a base inesperada: " + c.getCatalog());
        for (String t : new String[]{"venta_retorno_destino", "devolucion_venta", "devolucion_reintegro", "venta_anulacion_reintegro", "stock_producto"}) {
            try (PreparedStatement p = c.prepareStatement("SELECT 1 FROM " + t + " LIMIT 1")) {
                p.executeQuery();
            }
        }
        for (Caso caso : CASOS) {
            for (Articulo a : caso.articulos) {
                ok(precio(c, a.id).signum() > 0, "Precio inválido: " + a.id);
                ok(stock(c, a.id).compareTo(n(a.cantidad)) >= 0, "Stock insuficiente producto " + a.id + ": " + stock(c, a.id));
            }
        }
        try (PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM venta WHERE observaciones LIKE ?")) {
            p.setString(1, MARCA + "%");
            try (ResultSet r = p.executeQuery()) {
                r.next();
                ok(r.getLong(1) == 0, "Ya hay ventas con la marca del test. No repetir para evitar duplicados.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        boolean ejecutar = args.length == 2 && "--ejecutar".equals(args[0]) && "CONFIRMO_DATOS_FICTICIOS".equals(args[1]);
        try (Connection c = conexion.getConexion()) {
            preflight(c);
            Usuario admin = new UsuarioDao().buscarPorUsername("admin");
            ok(admin != null && "ACTIVO".equals(admin.getEstado()), "Administrador no disponible");
            ok(admin.getRol() != null && admin.getRol().tienePermiso("VENTA_ANULAR") && admin.getRol().tienePermiso("VENTA_DEVOLVER"), "Faltan permisos del administrador");
            System.out.println("PREVALIDACIÓN OK | BD: " + c.getCatalog() + " | usuario admin: " + admin.getIdUsuario());
            if (!ejecutar) {
                System.out.println("MODO SEGURO: no se escribió nada. Para ejecutar: --ejecutar CONFIRMO_DATOS_FICTICIOS");
                return;
            }
            SesionUsuario.iniciarSesion(admin);
            int aprobados = 0;
            for (int i = 0; i < CASOS.length; i++) {
                Caso caso = CASOS[i];
                System.out.println("\n=== CASO " + (i + 1) + ": " + caso.titulo + " ===");
                try {
                    Map<Integer, BigDecimal> antes = new LinkedHashMap<>();
                    for (Articulo a : caso.articulos) {
                        antes.put(a.id, stock(c, a.id));
                    }
                    VentaService.Solicitud s = new VentaService.Solicitud();
                    s.usuarioId = admin.getIdUsuario();
                    s.depositoId = 1;
                    s.observaciones = MARCA + (i + 1);
                    BigDecimal total = CERO;
                    for (Articulo a : caso.articulos) {
                        s.items.add(new VentaService.Item(a.id, n(a.cantidad), CERO));
                        total = total.add(precio(c, a.id).multiply(n(a.cantidad)));
                    }
                    s.pagos.add(new VentaService.Pago(1, total, total, "TEST AUTOMATICO"));
                    VentaService.Resultado venta = new VentaService().confirmar(s);
                    System.out.println("Venta generada: numero=" + venta.numero + " id=" + venta.ventaId + " total=" + venta.total);
                    List<DevolucionVentaService.Linea> disponibles = DevolucionVentaService.consultar(venta.ventaId);
                    ok(disponibles.size() == caso.articulos.length, "Cantidad de líneas inesperada");
                    List<DevolucionVentaService.Linea> seleccion = new ArrayList<>();
                    for (int j = 0; j < caso.articulos.length; j++) {
                        Articulo a = caso.articulos[j];
                        DevolucionVentaService.Linea l = disponibles.get(j);
                        l.cantidad = n(a.devuelta);
                        l.destino = a.destino;
                        l.observacion = "Evaluación TEST: " + caso.titulo;
                        seleccion.add(l);
                    }
                    DevolucionVentaService.Resultado resultado = caso.anulacion
                            ? DevolucionVentaService.anular(venta.ventaId, "Prueba automatizada: " + caso.titulo, seleccion)
                            : DevolucionVentaService.devolver(venta.ventaId, "Prueba automatizada: " + caso.titulo, seleccion);
                    String esperado = caso.anulacion ? "ANULADA" : (i == 3 ? "DEVUELTA_PARCIAL" : "DEVUELTA_TOTAL");
                    ok(esperado.equals(estado(c, venta.ventaId)), "Estado incorrecto: " + estado(c, venta.ventaId));
                    for (Articulo a : caso.articulos) {
                        BigDecimal esperadoStock = antes.get(a.id).subtract(n(a.cantidad));
                        if (a.destino == DevolucionVentaService.Destino.APTO) {
                            esperadoStock = esperadoStock.add(n(a.devuelta));
                        }
                        BigDecimal real = stock(c, a.id);
                        ok(real.compareTo(esperadoStock) == 0, "Stock producto " + a.id + ": esperado=" + esperadoStock + " real=" + real);
                        System.out.println("Producto " + a.id + " destino=" + a.destino + " stock inicial=" + antes.get(a.id) + " final=" + real);
                    }
                    ok(cuenta(c, "venta_retorno_destino", "id_venta", venta.ventaId) == caso.articulos.length, "Destinos no registrados");
                    if (caso.anulacion) {
                        ok(cuenta(c, "venta_anulacion_reintegro", "id_venta", venta.ventaId) == 1, "Reintegro anulación ausente");
                    } else {
                        ok(cuenta(c, "devolucion_venta", "id_venta", venta.ventaId) == 1, "Devolución ausente");
                        ok(!DevolucionVentaService.reintegrosPendientes(venta.ventaId).isEmpty(), "Reintegro pendiente ausente");
                    }
                    System.out.println("PASS | " + esperado + " | reintegro pendiente=" + resultado.monto);
                    aprobados++;
                } catch (Exception ex) {
                    System.err.println("FAIL caso " + (i + 1) + ": " + ex.getMessage());
                    ex.printStackTrace();
                    System.err.println("ATENCIÓN: pasos anteriores de este caso pueden haber hecho COMMIT. Revisar base antes de reejecutar.");
                    break;
                }
            }
            System.out.println("\nRESULTADO: " + aprobados + " / 6 casos aprobados. Las ventas de prueba permanecen registradas.");
        } finally {
            SesionUsuario.cerrarSesion();
        }
    }
}
