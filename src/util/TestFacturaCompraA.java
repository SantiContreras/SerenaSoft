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
 * Test de integración Serena Soft. Por defecto SOLO AUDITA (no escribe). Para
 * crear: --crear <idProveedor> <idDeposito> <usuario> <clave>
 * Requiere 4 productos con IVA configurado y presentaciones activas. Genera una
 * factura de demostración por $4.000.000, con 4 renglones.
 */
public final class TestFacturaCompraA {

    private static final BigDecimal TOTAL = new BigDecimal("4000000.00");
    private static final BigDecimal TOL = new BigDecimal("0.02");
    private static final List<String> errores = new ArrayList<>();
    private static int checks = 0;

    private static class Item {

        int id;
        String codigo, nombre, unidad;
        BigDecimal tasa, factor, cantidad, precio, subtotal;
    }

    private static void check(boolean ok, String mensaje) {
        checks++;
        System.out.println((ok ? "[OK] " : "[ERROR] ") + mensaje);
        if (!ok) {
            errores.add(mensaje);
        }
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    private static BigDecimal dos(BigDecimal n) {
        return n.setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        try {
            System.out.println("===== TEST DE INTEGRACION DE COMPRAS SERENA SOFT =====");
            if (args.length == 0) {
                ejecutarAsistente();
            } else if (args.length == 2 && "--auditar".equals(args[0])) {
                auditar(Long.parseLong(args[1]));
            } else if (args.length == 2 && "--detalle".equals(args[0])) {
                detalle(Long.parseLong(args[1]));
            } else if (args.length == 1 && "--listar".equals(args[0])) {
                listar();
            } else if (args.length == 5 && "--crear".equals(args[0])) {
                System.out.println("ATENCION: esta opcion CREA una compra y MODIFICA stock real.");
                crear(Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3], args[4]);
            } else {
                System.out.println("Uso: sin argumentos (asistente) | --auditar ID | --detalle ID | --listar | --crear ID_PROVEEDOR ID_DEPOSITO USUARIO CLAVE");
                check(false, "Argumentos no validos");
            }
        } catch (Exception ex) {
            check(false, "Excepcion: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            ex.printStackTrace(System.out);
        } finally {
            System.out.printf("%nRESUMEN: %d controles; %d errores%n", checks, errores.size());
            for (String e : errores) {
                System.out.println(" - " + e);
            }
            // No cerrar bruscamente NetBeans: el resumen ya indica el resultado.
        }
    }

    private static void ejecutarAsistente() throws Exception {
        String[] opciones = {"CREAR FACTURA A $4.000.000", "AUDITAR COMPRA #41", "CANCELAR"};
        int op = javax.swing.JOptionPane.showOptionDialog(null,
                "Serena Soft - Test de compras\n\nCREAR registra una compra real y aumenta stock en la base actual.\nSe seleccionaran cuatro productos con presentaciones activas.",
                "Prueba de integracion", javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE, null, opciones, opciones[1]);
        if (op == 1) {
            auditar(41L);
            return;
        }
        if (op != 0) {
            System.out.println("Prueba cancelada. Sin cambios.");
            return;
        }
        int proveedor = primerId("SELECT id_proveedor FROM proveedor ORDER BY id_proveedor LIMIT 1");
        int deposito = primerId("SELECT id_deposito FROM deposito ORDER BY id_deposito LIMIT 1");
        if (proveedor <= 0 || deposito <= 0) {
            throw new IllegalStateException("Falta proveedor o deposito en la BD");
        }
        javax.swing.JTextField p = new javax.swing.JTextField(String.valueOf(proveedor), 12);
        javax.swing.JTextField d = new javax.swing.JTextField(String.valueOf(deposito), 12);
        javax.swing.JTextField u = new javax.swing.JTextField(15);
        javax.swing.JPasswordField c = new javax.swing.JPasswordField(15);
        Object[] campos = {"ID proveedor:", p, "ID deposito:", d, "Usuario:", u, "Clave:", c};
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(null, campos,
                "Confirmar compra de prueba por $4.000.000", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (confirmar != javax.swing.JOptionPane.OK_OPTION) {
            System.out.println("Cancelado. Sin cambios.");
            return;
        }
        crear(Integer.parseInt(p.getText().trim()), Integer.parseInt(d.getText().trim()),
                u.getText().trim(), new String(c.getPassword()));
    }

    private static int primerId(String sql) throws SQLException {
        try (Connection c = conexion.getConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    private static void detalle(long id) throws SQLException {
        System.out.println("\n================ DETALLE DE COMPRA #" + id + " ================");
        try (Connection c = conexion.getConexion()) {
            String cab = "SELECT numero_comprobante, estado, total, fecha FROM compra WHERE id_compra=?";
            try (PreparedStatement ps = c.prepareStatement(cab)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    if (!r.next()) {
                        check(false, "Compra inexistente #" + id);
                        return;
                    }
                    System.out.printf("Comprobante: %s | Estado: %s | Fecha: %s | Total: $%s%n",
                            r.getString(1), r.getString(2), r.getTimestamp(4), r.getBigDecimal(3));
                }
            }
            String sql = "SELECT p.codigo,p.nombre,cd.unidad_compra,cd.factor_conversion,cd.cantidad,"
                    + "cd.cantidad_stock,cd.costo_unitario,cd.subtotal "
                    + "FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto "
                    + "WHERE cd.id_compra=? ORDER BY cd.id_detalle";
            System.out.println("CODIGO | PRODUCTO | PRESENTACION | FACTOR | CANTIDAD | INGRESA STOCK | COSTO | SUBTOTAL");
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        System.out.printf("%s | %s | %s | x%s | %s | %s | $%s | $%s%n",
                                r.getString(1), r.getString(2), r.getString(3), r.getBigDecimal(4),
                                r.getBigDecimal(5), r.getBigDecimal(6), r.getBigDecimal(7), r.getBigDecimal(8));
                    }
                }
            }
            String iva = "SELECT alicuota,base_imponible,importe_iva,credito_computable FROM compra_fiscal_iva WHERE id_compra=? ORDER BY alicuota";
            try (PreparedStatement ps = c.prepareStatement(iva)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        System.out.printf("IVA %s%% | Neto $%s | IVA $%s | Credito computable $%s%n",
                                r.getBigDecimal(1), r.getBigDecimal(2), r.getBigDecimal(3), r.getBigDecimal(4));
                    }
                }
            }
        }
    }

    private static void crear(int idProveedor, int idDeposito, String usuario, String clave) throws Exception {
        ResultadoLogin login = new LoginService().login(usuario, clave);
        if (!login.isCorrecto()) {
            throw new IllegalStateException("Login: " + login.getMensaje());
        }
        List<Item> items = seleccionarItems();
        check(items.size() == 4, "Cuatro productos diferentes con IVA y presentaciones activas");
        if (items.size() != 4) {
            return;
        }
        // 4 importes que suman 4 millones. Precios FINALES con IVA incluido.
        String[] montos = {"1000000.00", "900000.00", "1100000.00", "1000000.00"};
        BigDecimal[] cantidades = {bd("10"), bd("20"), bd("25"), bd("40")};
        String folio = "TEST-A-" + System.currentTimeMillis();
        Compra compra = new Compra();
        Proveedor p = new Proveedor();
        p.setIdProveedor(idProveedor);
        compra.setProveedor(p);
        Deposito d = new Deposito();
        d.setIdDeposito(idDeposito);
        compra.setDeposito(d);
        compra.setNumeroComprobante(folio);
        compra.setObservaciones("PRUEBA AUTOMATICA - TestFacturaCompraA - $4.000.000");
        CompraService servicio = new CompraService();
        exigir(servicio.crearCompra(compra), "Crear borrador");
        long id = compra.getIdCompra();
        if (id <= 0) {
            id = buscarIdPorComprobante(folio);
        }
        System.out.println("ID COMPRA DE PRUEBA: " + id + " | Comprobante: " + folio);
        check(id > 0, "Borrador persistido: compra " + id);
        if (id <= 0) {
            return;
        }
        for (int n = 0; n < items.size(); n++) {
            Item i = items.get(n);
            i.cantidad = cantidades[n];
            i.subtotal = bd(montos[n]);
            i.precio = i.subtotal.divide(i.cantidad, 2, RoundingMode.UNNECESSARY);
            System.out.printf("LINEA %d: %s | %s x%s | cantidad=%s | unidades stock=%s | costo/presentacion=%s | subtotal=%s | IVA=%s%%%n",
                    n + 1, i.codigo, i.unidad, i.factor, i.cantidad,
                    i.cantidad.multiply(i.factor), i.precio, i.subtotal, i.tasa);
            exigir(servicio.agregarProducto(id, i.id, i.unidad, i.factor, i.cantidad, i.precio),
                    "Agregar " + i.codigo + " (factor " + i.factor + ")");
        }
        CompraFiscal fiscal = new CompraFiscal();
        fiscal.idCompra = id;
        fiscal.tipoComprobante = "FACTURA";
        fiscal.letra = "A";
        fiscal.numero = folio;
        fiscal.fechaEmision = LocalDate.now();
        fiscal.totalComprobante = TOTAL;
        fiscal.observacionesFiscales = "PRUEBA AUTOMATICA; IVA estimado, credito fiscal 0";
        Map<BigDecimal, BigDecimal> porTasa = new TreeMap<>();
        for (Item i : items) {
            porTasa.merge(i.tasa, i.subtotal, BigDecimal::add);
        }
        for (Map.Entry<BigDecimal, BigDecimal> e : porTasa.entrySet()) {
            BigDecimal base = e.getValue().divide(BigDecimal.ONE.add(e.getKey().movePointLeft(2)), 2, RoundingMode.HALF_UP);
            fiscal.alicuotas.add(new CompraFiscal.DetalleIva(e.getKey(), base, e.getValue().subtract(base), BigDecimal.ZERO));
        }
        // Validación previa: nunca confirmar si los importes no coinciden.
        BigDecimal suma = BigDecimal.ZERO;
        for (Item i : items) {
            suma = suma.add(i.subtotal);
        }
        check(suma.compareTo(TOTAL) == 0, "Total preparado: " + suma);
        if (!errores.isEmpty()) {
            return;
        }
        exigir(servicio.confirmarCompra(id, Collections.emptyMap(), fiscal), "Confirmar compra + fiscal + stock");
        auditar(id);
        detalle(id);
        System.out.println("COMPRA GENERADA: #" + id + " | Total esperado: $4.000.000,00");
    }

    private static void exigir(ResultadoOperacion r, String etapa) {
        check(r.isExitoso(), etapa + ": " + r.getMensaje());
        if (!r.isExitoso()) {
            throw new IllegalStateException("Prueba detenida en: " + etapa);
        }
    }

    private static List<Item> seleccionarItems() throws SQLException {
        List<Item> out = new ArrayList<>();
        String sql = "SELECT p.id_producto,p.codigo,p.nombre,t.porcentaje,pp.factor_conversion,u.codigo AS unidad "
                + "FROM producto p JOIN tipo_iva t ON t.id_iva=p.id_iva AND t.activo=1 "
                + "JOIN producto_presentacion pp ON pp.id_producto=p.id_producto AND pp.activo=1 "
                + "JOIN unidad_medida u ON u.id_unidad=pp.id_unidad_compra "
                + "WHERE pp.factor_conversion>0 AND p.id_iva IS NOT NULL "
                + "ORDER BY pp.factor_conversion DESC,p.id_producto LIMIT 200";
        try (Connection c = conexion.getConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            Set<Integer> ids = new HashSet<>();
            Set<String> unidades = new HashSet<>();
            List<Item> candidatos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                if (!ids.add(id)) {
                    continue;
                }
                Item i = new Item();
                i.id = id;
                i.codigo = rs.getString(2);
                i.nombre = rs.getString(3);
                i.tasa = rs.getBigDecimal(4);
                i.factor = rs.getBigDecimal(5);
                i.unidad = rs.getString(6);
                if (i.tasa == null || i.tasa.signum() <= 0 || i.factor == null || i.factor.signum() <= 0) {
                    continue;
                }
                candidatos.add(i);
            }
            // Priorizar presentaciones distintas, y completar hasta cuatro productos.
            for (Item i : candidatos) {
                if (unidades.add(i.unidad)) {
                    out.add(i);
                }
                if (out.size() == 4) {
                    break;
                }
            }
            for (Item i : candidatos) {
                if (out.size() == 4) {
                    break;
                }
                if (!out.contains(i)) {
                    out.add(i);
                }
            }
        }
        for (Item i : out) {
            System.out.printf("Seleccionado %s %s, %s x %s, IVA %s%%%n", i.codigo, i.nombre, i.unidad, i.factor, i.tasa);
        }
        return out;
    }

    private static long buscarIdPorComprobante(String folio) throws SQLException {
        try (Connection c = conexion.getConexion(); PreparedStatement p = c.prepareStatement("SELECT id_compra FROM compra WHERE numero_comprobante=? ORDER BY id_compra DESC LIMIT 1")) {
            p.setString(1, folio);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? r.getLong(1) : -1;
            }
        }
    }

    private static void auditar(long id) throws SQLException {
        System.out.println("\n====== AUDITORIA COMPRA #" + id + " ======");
        try (Connection c = conexion.getConexion()) {
            BigDecimal total = null;
            String estado = null;
            try (PreparedStatement p = c.prepareStatement("SELECT total,estado FROM compra WHERE id_compra=?")) {
                p.setLong(1, id);
                try (ResultSet r = p.executeQuery()) {
                    if (r.next()) {
                        total = r.getBigDecimal(1);
                        estado = r.getString(2);
                    }
                }
            }
            check(total != null, "Existe compra #" + id);
            if (total == null) {
                return;
            }
            System.out.println("Estado=" + estado + " Total=" + total);
            check("CONFIRMADA".equalsIgnoreCase(estado), "Compra confirmada");
            BigDecimal suma = BigDecimal.ZERO;
            int filas = 0;
            String q = "SELECT p.codigo,cd.unidad_compra,cd.factor_conversion,cd.cantidad,cd.cantidad_stock,cd.costo_unitario,cd.subtotal "
                    + "FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto WHERE cd.id_compra=?";
            try (PreparedStatement ps = c.prepareStatement(q)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        filas++;
                        BigDecimal factor = r.getBigDecimal(3), cantidad = r.getBigDecimal(4), stock = r.getBigDecimal(5), costo = r.getBigDecimal(6), sub = r.getBigDecimal(7);
                        String cod = r.getString(1);
                        System.out.printf("%s %s factor=%s cant=%s stock=%s costo=%s subtotal=%s%n", cod, r.getString(2), factor, cantidad, stock, costo, sub);
                        check(cantidad.multiply(factor).compareTo(stock) == 0, "Conversión " + cod);
                        check(dos(cantidad.multiply(costo)).subtract(sub).abs().compareTo(TOL) <= 0, "Subtotal " + cod);
                        suma = suma.add(sub);
                    }
                }
            }
            // Comprobar cada movimiento de compra contra su detalle de origen.
            String mov = "SELECT p.codigo,cd.cantidad_stock,m.cantidad,m.stock_anterior,m.stock_posterior "
                    + "FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto "
                    + "LEFT JOIN movimiento_stock m ON m.id_detalle_origen=cd.id_detalle "
                    + "AND m.tipo_movimiento='COMPRA' WHERE cd.id_compra=?";
            try (PreparedStatement ps = c.prepareStatement(mov)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        String cod = r.getString(1);
                        BigDecimal registrado = r.getBigDecimal(3);
                        check(registrado != null, "Movimiento de stock para " + cod);
                        if (registrado != null) {
                            check(registrado.compareTo(r.getBigDecimal(2)) == 0, "Movimiento coincide con detalle " + cod);
                            check(r.getBigDecimal(4).add(registrado).compareTo(r.getBigDecimal(5)) == 0,
                                    "Stock anterior + movimiento = posterior " + cod);
                        }
                    }
                }
            }
            String duplicados = "SELECT cd.id_detalle,COUNT(m.id_movimiento) AS cantidad "
                    + "FROM compra_detalle cd LEFT JOIN movimiento_stock m ON m.id_detalle_origen=cd.id_detalle "
                    + "AND m.tipo_movimiento='COMPRA' WHERE cd.id_compra=? GROUP BY cd.id_detalle";
            try (PreparedStatement ps = c.prepareStatement(duplicados)) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        check(r.getLong(2) == 1, "Exactamente un movimiento por detalle #" + r.getLong(1));
                    }
                }
            }
            check(filas > 0, "Hay detalles de compra");
            check(total.signum() >= 0, "Total de compra no negativo");
            check(suma.subtract(total).abs().compareTo(TOL) <= 0, "Detalle suma " + suma + " = total " + total + " (sin descuentos)");
            BigDecimal fiscalTotal = null, neto = BigDecimal.ZERO, iva = BigDecimal.ZERO;
            BigDecimal noGravado = BigDecimal.ZERO, exento = BigDecimal.ZERO, percIva = BigDecimal.ZERO,
                    percIibb = BigDecimal.ZERO, otros = BigDecimal.ZERO;
            String tipoFiscal = null, letraFiscal = null;
            try (PreparedStatement ps = c.prepareStatement("SELECT total_comprobante,tipo_comprobante,letra,neto_no_gravado,importe_exento,percepciones_iva,percepciones_iibb,otros_tributos FROM compra_fiscal WHERE id_compra=?")) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    if (r.next()) {
                        fiscalTotal = r.getBigDecimal(1);
                        tipoFiscal = r.getString(2);
                        letraFiscal = r.getString(3);
                        noGravado = nvl(r.getBigDecimal(4));
                        exento = nvl(r.getBigDecimal(5));
                        percIva = nvl(r.getBigDecimal(6));
                        percIibb = nvl(r.getBigDecimal(7));
                        otros = nvl(r.getBigDecimal(8));
                    }
                }
            }
            check(fiscalTotal != null, "Existe cabecera fiscal");
            if (fiscalTotal != null) {
                check("FACTURA".equalsIgnoreCase(tipoFiscal), "Tipo fiscal FACTURA");
                check("A".equalsIgnoreCase(letraFiscal), "Letra fiscal A");
            }
            if (fiscalTotal != null) {
                check(fiscalTotal.subtract(total).abs().compareTo(TOL) <= 0, "Total fiscal concilia");
            }
            int tasas = 0;
            try (PreparedStatement ps = c.prepareStatement("SELECT alicuota,base_imponible,importe_iva,credito_computable FROM compra_fiscal_iva WHERE id_compra=?")) {
                ps.setLong(1, id);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        tasas++;
                        BigDecimal tasa = r.getBigDecimal(1), base = r.getBigDecimal(2), imp = r.getBigDecimal(3), credito = r.getBigDecimal(4);
                        System.out.printf("IVA %s%%: base=%s IVA=%s crédito=%s%n", tasa, base, imp, credito);
                        check(dos(base.multiply(tasa).movePointLeft(2)).subtract(imp).abs().compareTo(new BigDecimal("0.03")) <= 0, "Cálculo IVA tasa " + tasa);
                        check(base.signum() >= 0 && imp.signum() >= 0, "Base e IVA no negativos");
                        check(credito.signum() >= 0 && credito.compareTo(imp) <= 0, "Credito no excede IVA");
                        neto = neto.add(base);
                        iva = iva.add(imp);
                    }
                }
            }
            check(tasas > 0, "Existen alícuotas fiscales");
            if (fiscalTotal != null) {
                BigDecimal conciliado = neto.add(iva).add(noGravado).add(exento).add(percIva).add(percIibb).add(otros);
                check(conciliado.subtract(fiscalTotal).abs().compareTo(TOL) <= 0,
                        "Neto + IVA + exentos + no gravados + percepciones + tributos = factura");
                System.out.printf("CONCILIACION: neto=%s iva=%s noGravado=%s exento=%s percIVA=%s percIIBB=%s otros=%s%n",
                        neto, iva, noGravado, exento, percIva, percIibb, otros);
            }
            System.out.printf("RESUMEN compra=%s detalle=%s neto=%s iva=%s fiscal=%s%n", total, suma, neto, iva, fiscalTotal);
            System.out.println("NOTA: el saldo actual puede incluir movimientos posteriores a esta compra.");
        }
        listar();
    }

    private static void listar() throws SQLException {
        System.out.println("\n===== ULTIMAS 20 COMPRAS =====");
        try (Connection c = conexion.getConexion(); PreparedStatement p = c.prepareStatement(
                "SELECT id_compra,numero_comprobante,estado,total,fecha FROM compra ORDER BY id_compra DESC LIMIT 20"); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                System.out.printf("#%-5d %-24s %-12s $%-14s %s%n", r.getLong(1), r.getString(2), r.getString(3), r.getBigDecimal(4), r.getTimestamp(5));
            }
        }
    }
}
