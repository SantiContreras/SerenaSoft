package util;

import Configuracion.conexion;
import java.math.BigDecimal;
import java.sql.*;

/** Auditoría de solo lectura del circuito de caja. No modifica datos. */
public class TestCircuitoCaja {
    private static int ok = 0, fail = 0;
    private static void verificar(String titulo, boolean condicion, String detalle) {
        System.out.printf("[%s] %s%s%n", condicion ? "OK" : "REVISAR", titulo,
                detalle == null ? "" : " - " + detalle);
        if (condicion) ok++; else fail++;
    }
    public static void main(String[] args) {
        System.out.println("=== SERENA SOFT - AUDITORIA DE CAJAS (SOLO LECTURA) ===");
        try (Connection c = conexion.getConexion()) {
            if (c == null) throw new SQLException("Conexión nula");
            verificar("Conexión MariaDB", true, null);
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(
                    "SELECT id_usuario_apertura, COUNT(*) n FROM caja_sesion WHERE estado='ABIERTA' " +
                    "GROUP BY id_usuario_apertura HAVING COUNT(*)>1")) {
                int duplicados = 0;
                while (r.next()) { duplicados++; System.out.printf("  Usuario %d: %d cajas abiertas%n", r.getInt(1), r.getInt(2)); }
                verificar("Máximo una caja abierta por usuario", duplicados == 0, "Duplicados: " + duplicados);
            }
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(
                    "SELECT cs.id_sesion, cs.id_usuario_apertura, c.codigo " +
                    "FROM caja_sesion cs JOIN caja c ON c.id_caja=cs.id_caja WHERE cs.estado='ABIERTA'")) {
                int abiertas = 0, invalidas = 0;
                while (r.next()) {
                    abiertas++;
                    long id = r.getLong(1); int usuario = r.getInt(2); String codigo = r.getString(3);
                    System.out.printf("  Caja abierta: sesión=%d, usuario=%d, código=%s%n", id, usuario, codigo);
                    if (!codigo.equals("CAJERO-" + usuario)) invalidas++;
                }
                verificar("Cajas abiertas asignadas al cajero correcto", invalidas == 0,
                        "Abiertas: " + abiertas + ", asignaciones inconsistentes: " + invalidas);
            }
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(
                    "SELECT COUNT(*) FROM caja_movimiento m JOIN caja_sesion cs ON cs.id_sesion=m.id_sesion " +
                    "WHERE m.id_usuario<>cs.id_usuario_apertura")) {
                r.next(); long n = r.getLong(1);
                verificar("Movimientos manuales pertenecen al dueño de la caja", n == 0, "Diferencias: " + n);
            }
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(
                    "SELECT COUNT(*) FROM caja_sesion WHERE estado='CERRADA' " +
                    "AND (fecha_cierre IS NULL OR efectivo_esperado IS NULL OR efectivo_contado IS NULL OR diferencia IS NULL " +
                    "OR ABS(diferencia - (efectivo_contado-efectivo_esperado)) > 0.009)")) {
                r.next(); long n = r.getLong(1);
                verificar("Cierres con conteo y diferencia coherentes", n == 0, "Inconsistencias: " + n);
            }
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(
                    "SELECT COUNT(*) FROM arqueo_caja a JOIN caja_sesion cs ON cs.id_sesion=a.id_sesion " +
                    "WHERE a.id_usuario<>cs.id_usuario_apertura " +
                    "OR ABS(a.diferencia - (a.efectivo_contado-a.efectivo_esperado)) > 0.009")) {
                r.next(); long n = r.getLong(1);
                verificar("Arqueos con usuario y diferencia coherentes", n == 0, "Inconsistencias: " + n);
            }
            System.out.printf("%nResultado: %d OK, %d para revisar.%n", ok, fail);
            System.out.println("Nota: esto NO ejecuta aperturas, ingresos, egresos, arqueos ni cierres.");
        } catch (Exception e) {
            System.err.println("ERROR DE AUDITORÍA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
