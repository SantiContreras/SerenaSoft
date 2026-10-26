package util;

import Configuracion.conexion;
import services.CompraService;
import services.LoginService;
import services.ResultadoLogin;
import services.ResultadoOperacion;
import model.Compra;
import model.CompraFiscal;
import model.Proveedor;
import model.Deposito;
import java.sql.*;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Test de integración Serena Soft. Por defecto SOLO AUDITA (no escribe).
 * Para crear: --crear <idProveedor> <idDeposito> <usuario> <clave>
 * Requiere 4 productos con IVA configurado y presentaciones activas.
 * Genera una factura de demostración por $4.000.000, con 4 renglones.
 */
public final class TestFacturaCompraA {
    private static final BigDecimal TOTAL = new BigDecimal("4000000.00");
    private static final BigDecimal TOL = new BigDecimal("0.02");
    private static final List<String> errores = new ArrayList<>();
    private static int checks = 0;
    private static class Item {
        int id; String codigo, nombre, unidad; BigDecimal tasa, factor, cantidad, precio, subtotal;
    }
    private static void check(boolean ok, String mensaje) {
        checks++;
        System.out.println((ok ? "[OK] " : "[ERROR] ") + mensaje);
        if (!ok) errores.add(mensaje);
    }
    private static BigDecimal bd(String s) { return new BigDecimal(s); }
    private static BigDecimal dos(BigDecimal n) { return n.setScale(2, RoundingMode.HALF_UP); }
public static void main(String[] args) {
    try {
        System.out.println("===== TEST FACTURA COMPRA A =====");

        // Auditoría de la compra existente N.º 41
        auditar(41L);

    } catch (Exception ex) {
        check(false, "Excepción: "
                + ex.getClass().getSimpleName()
                + " - " + ex.getMessage());

        ex.printStackTrace(System.out);

    } finally {
        System.out.printf(
                "%nRESUMEN: %d controles; %d errores%n",
                checks,
                errores.size()
        );

        for (String e : errores) {
            System.out.println(" - " + e);
        }
    }
}
    private static void crear(int idProveedor, int idDeposito, String usuario, String clave) throws Exception {
        ResultadoLogin login = new LoginService().login(usuario, clave);
        if (!login.isCorrecto()) throw new IllegalStateException("Login: " + login.getMensaje());
        List<Item> items = seleccionarItems();
        check(items.size() == 4, "Cuatro productos diferentes con IVA y presentaciones activas");
        if (items.size() != 4) return;
        // 4 importes que suman 4 millones. Precios FINALES con IVA incluido.
        String[] montos = {"1000000.00", "900000.00", "1100000.00", "1000000.00"};
        String folio = "TEST-A-" + System.currentTimeMillis();
        Compra compra = new Compra();
        Proveedor p = new Proveedor(); p.setIdProveedor(idProveedor); compra.setProveedor(p);
        Deposito d = new Deposito(); d.setIdDeposito(idDeposito); compra.setDeposito(d);
        compra.setNumeroComprobante(folio);
        compra.setObservaciones("PRUEBA AUTOMATICA - TestFacturaCompraA - $4.000.000");
        CompraService servicio = new CompraService();
        exigir(servicio.crearCompra(compra), "Crear borrador");
        long id = buscarIdPorComprobante(folio);
        check(id > 0, "Borrador persistido: compra " + id);
        if (id <= 0) return;
        for (int n = 0; n < items.size(); n++) {
            Item i = items.get(n);
            i.cantidad = BigDecimal.ONE;
            i.subtotal = bd(montos[n]);
            i.precio = i.subtotal; // 1 presentación por renglón
            exigir(servicio.agregarProducto(id, i.id, i.unidad, i.factor, i.cantidad, i.precio),
                    "Agregar " + i.codigo + " (factor " + i.factor + ")");
        }
        CompraFiscal fiscal = new CompraFiscal();
        fiscal.idCompra = id; fiscal.tipoComprobante = "FACTURA"; fiscal.letra = "A";
        fiscal.numero = folio; fiscal.fechaEmision = LocalDate.now();
        fiscal.totalComprobante = TOTAL;
        fiscal.observacionesFiscales = "PRUEBA AUTOMATICA; IVA estimado, credito fiscal 0";
        Map<BigDecimal, BigDecimal> porTasa = new TreeMap<>();
        for (Item i : items) porTasa.merge(i.tasa, i.subtotal, BigDecimal::add);
        for (Map.Entry<BigDecimal, BigDecimal> e : porTasa.entrySet()) {
            BigDecimal base = e.getValue().divide(BigDecimal.ONE.add(e.getKey().movePointLeft(2)), 2, RoundingMode.HALF_UP);
            fiscal.alicuotas.add(new CompraFiscal.DetalleIva(e.getKey(), base, e.getValue().subtract(base), BigDecimal.ZERO));
        }
        // Validación previa: nunca confirmar si los importes no coinciden.
        BigDecimal suma = BigDecimal.ZERO;
        for (Item i : items) suma = suma.add(i.subtotal);
        check(suma.compareTo(TOTAL) == 0, "Total preparado: " + suma);
        if (!errores.isEmpty()) return;
        exigir(servicio.confirmarCompra(id, Collections.emptyMap(), fiscal), "Confirmar compra + fiscal + stock");
        auditar(id);
    }
    private static void exigir(ResultadoOperacion r, String etapa) {
        check(r.isExitoso(), etapa + ": " + r.getMensaje());
        if (!r.isExitoso()) throw new IllegalStateException("Prueba detenida en: " + etapa);
    }
    private static List<Item> seleccionarItems() throws SQLException {
        List<Item> out = new ArrayList<>();
        String sql = "SELECT p.id_producto,p.codigo,p.nombre,t.porcentaje,pp.factor_conversion,u.codigo AS unidad "
            + "FROM producto p JOIN tipo_iva t ON t.id_iva=p.id_iva AND t.activo=1 "
            + "JOIN producto_presentacion pp ON pp.id_producto=p.id_producto AND pp.activo=1 "
            + "JOIN unidad_medida u ON u.id_unidad=pp.id_unidad_compra "
            + "WHERE pp.factor_conversion>0 AND p.id_iva IS NOT NULL "
            + "ORDER BY pp.factor_conversion DESC,p.id_producto LIMIT 50";
        try (Connection c = conexion.getConexion(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            Set<Integer> ids = new HashSet<>();
            while (rs.next() && out.size()<4) {
                int id=rs.getInt(1); if (!ids.add(id)) continue;
                Item i=new Item(); i.id=id; i.codigo=rs.getString(2); i.nombre=rs.getString(3);
                i.tasa=rs.getBigDecimal(4); i.factor=rs.getBigDecimal(5); i.unidad=rs.getString(6);
                out.add(i);
                System.out.printf("Seleccionado %s %s, %s x %s, IVA %s%%%n",i.codigo,i.nombre,i.unidad,i.factor,i.tasa);
            }
        }
        return out;
    }
    private static long buscarIdPorComprobante(String folio) throws SQLException {
        try (Connection c=conexion.getConexion(); PreparedStatement p=c.prepareStatement("SELECT id_compra FROM compra WHERE numero_comprobante=? ORDER BY id_compra DESC LIMIT 1")) {
            p.setString(1,folio); try(ResultSet r=p.executeQuery()) {return r.next()?r.getLong(1):-1;}
        }
    }
    private static void auditar(long id) throws SQLException {
        System.out.println("\n====== AUDITORIA COMPRA #" + id + " ======");
        try(Connection c=conexion.getConexion()) {
            BigDecimal total=null; String estado=null;
            try(PreparedStatement p=c.prepareStatement("SELECT total,estado FROM compra WHERE id_compra=?")) {
                p.setLong(1,id);try(ResultSet r=p.executeQuery()) {if(r.next()){total=r.getBigDecimal(1);estado=r.getString(2);}}
            }
            check(total!=null,"Existe compra #"+id); if(total==null)return;
            System.out.println("Estado="+estado+" Total="+total);
            check("CONFIRMADA".equalsIgnoreCase(estado),"Compra confirmada");
            BigDecimal suma=BigDecimal.ZERO; int filas=0;
            String q="SELECT p.codigo,cd.unidad_compra,cd.factor_conversion,cd.cantidad,cd.cantidad_stock,cd.costo_unitario,cd.subtotal "
                +"FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto WHERE cd.id_compra=?";
            try(PreparedStatement ps=c.prepareStatement(q)) {
                ps.setLong(1,id);try(ResultSet r=ps.executeQuery()) {while(r.next()) {
                    filas++; BigDecimal factor=r.getBigDecimal(3),cantidad=r.getBigDecimal(4),stock=r.getBigDecimal(5),costo=r.getBigDecimal(6),sub=r.getBigDecimal(7);
                    String cod=r.getString(1); System.out.printf("%s %s factor=%s cant=%s stock=%s costo=%s subtotal=%s%n",cod,r.getString(2),factor,cantidad,stock,costo,sub);
                    check(cantidad.multiply(factor).compareTo(stock)==0,"Conversión " +cod);
                    check(dos(cantidad.multiply(costo)).subtract(sub).abs().compareTo(TOL)<=0,"Subtotal " +cod);
                    suma=suma.add(sub);
                }}
            }
            // Comprobar cada movimiento de compra contra su detalle de origen.
            String mov = "SELECT p.codigo,cd.cantidad_stock,m.cantidad,m.stock_anterior,m.stock_posterior "
                + "FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto "
                + "LEFT JOIN movimiento_stock m ON m.id_detalle_origen=cd.id_detalle "
                + "AND m.tipo_movimiento='COMPRA' WHERE cd.id_compra=?";
            try(PreparedStatement ps=c.prepareStatement(mov)) {
                ps.setLong(1,id);try(ResultSet r=ps.executeQuery()) {while(r.next()) {
                    String cod=r.getString(1);BigDecimal registrado=r.getBigDecimal(3);
                    check(registrado!=null,"Movimiento de stock para "+cod);
                    if(registrado!=null) {
                        check(registrado.compareTo(r.getBigDecimal(2))==0,"Movimiento coincide con detalle "+cod);
                        check(r.getBigDecimal(4).add(registrado).compareTo(r.getBigDecimal(5))==0,
                            "Stock anterior + movimiento = posterior "+cod);
                    }
                }}
            }
            check(filas>0,"Hay detalles de compra");
            check(suma.subtract(total).abs().compareTo(TOL)<=0,"Detalle suma " +suma+" = total "+total+" (sin descuentos)");
            BigDecimal fiscalTotal=null, neto=BigDecimal.ZERO,iva=BigDecimal.ZERO;
            try(PreparedStatement ps=c.prepareStatement("SELECT total_comprobante FROM compra_fiscal WHERE id_compra=?")) {
                ps.setLong(1,id);try(ResultSet r=ps.executeQuery()){if(r.next())fiscalTotal=r.getBigDecimal(1);}
            }
            check(fiscalTotal!=null,"Existe cabecera fiscal");
            if(fiscalTotal!=null)check(fiscalTotal.subtract(total).abs().compareTo(TOL)<=0,"Total fiscal concilia");
            int tasas=0;
            try(PreparedStatement ps=c.prepareStatement("SELECT alicuota,base_imponible,importe_iva,credito_computable FROM compra_fiscal_iva WHERE id_compra=?")) {
                ps.setLong(1,id);try(ResultSet r=ps.executeQuery()){while(r.next()){
                    tasas++;BigDecimal tasa=r.getBigDecimal(1),base=r.getBigDecimal(2),imp=r.getBigDecimal(3),credito=r.getBigDecimal(4);
                    System.out.printf("IVA %s%%: base=%s IVA=%s crédito=%s%n",tasa,base,imp,credito);
                    check(dos(base.multiply(tasa).movePointLeft(2)).subtract(imp).abs().compareTo(new BigDecimal("0.03"))<=0,"Cálculo IVA tasa "+tasa);
                    check(credito.signum()>=0 && credito.compareTo(imp)<=0,"Crédito no excede IVA");
                    neto=neto.add(base);iva=iva.add(imp);
                }}
            }
            check(tasas>0,"Existen alícuotas fiscales");
            if(fiscalTotal!=null)check(neto.add(iva).subtract(fiscalTotal).abs().compareTo(TOL)<=0,"Neto + IVA = factura (sin otros tributos)");
            System.out.printf("RESUMEN compra=%s detalle=%s neto=%s iva=%s fiscal=%s%n",total,suma,neto,iva,fiscalTotal);
            System.out.println("NOTA: el saldo actual puede incluir movimientos posteriores a esta compra.");
        }
        listar();
    }
    private static void listar() throws SQLException {
        System.out.println("\n===== ULTIMAS 20 COMPRAS =====");
        try(Connection c=conexion.getConexion();PreparedStatement p=c.prepareStatement(
                "SELECT id_compra,numero_comprobante,estado,total,fecha FROM compra ORDER BY id_compra DESC LIMIT 20");ResultSet r=p.executeQuery()) {
            while(r.next())System.out.printf("#%-5d %-24s %-12s $%-14s %s%n",r.getLong(1),r.getString(2),r.getString(3),r.getBigDecimal(4),r.getTimestamp(5));
        }
    }
}
