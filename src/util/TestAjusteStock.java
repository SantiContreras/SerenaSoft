package util;

import Dao.StockProductoDao;
import Dao.UsuarioDao;
import java.math.BigDecimal;
import model.StockProducto;
import model.Usuario;
import services.AjusteStockService;
import services.ResultadoOperacion;
import sesion.SesionUsuario;

/** Prueba de integración para la API actual de AjusteStockService.
 *  ATENCIÓN: con --ejecutar modifica el stock real y registra movimientos.
 *  Ejecutar exclusivamente contra una base de datos de pruebas.
 */
public class TestAjusteStock {
    private static final int ID_PRODUCTO = 1;
    private static final int ID_DEPOSITO = 1;

    public static void main(String[] args) {
        if (args.length == 0 || !"--ejecutar".equals(args[0])) {
            System.out.println("Prueba de integración deshabilitada por seguridad.");
            System.out.println("Usar --ejecutar SOLO con una base de datos de pruebas.");
            return;
        }
        try {
            Usuario usuario = new UsuarioDao().buscarPorUsername("admin");
            if (usuario == null) throw new IllegalStateException("No existe el usuario admin.");
            SesionUsuario.iniciarSesion(usuario);

            StockProductoDao stockDao = new StockProductoDao();
            StockProducto inicial = stockDao.buscarPorProductoDeposito(ID_PRODUCTO, ID_DEPOSITO);
            if (inicial == null) throw new IllegalStateException("No hay stock para producto/deposito de prueba.");
            BigDecimal antes = inicial.getCantidad();
            BigDecimal esperado = antes.add(BigDecimal.ONE);
            AjusteStockService servicio = new AjusteStockService();

            ResultadoOperacion resultado = servicio.aplicarAjuste(
                    ID_DEPOSITO, ID_PRODUCTO, antes, esperado,
                    "PRUEBA AJUSTE STOCK", "Test de integracion Serena Soft");
            if (!resultado.isExitoso()) throw new AssertionError(resultado.getMensaje());

            StockProducto posterior = stockDao.buscarPorProductoDeposito(ID_PRODUCTO, ID_DEPOSITO);
            if (posterior == null || posterior.getCantidad().compareTo(esperado) != 0)
                throw new AssertionError("Stock final distinto del esperado.");

            ResultadoOperacion repetido = servicio.aplicarAjuste(
                    ID_DEPOSITO, ID_PRODUCTO, antes, esperado,
                    "PRUEBA AJUSTE STOCK", "Reintento con lectura obsoleta");
            if (repetido.isExitoso())
                throw new AssertionError("Se aceptó un ajuste con stockVisto obsoleto.");

            StockProducto finalStock = stockDao.buscarPorProductoDeposito(ID_PRODUCTO, ID_DEPOSITO);
            if (finalStock.getCantidad().compareTo(esperado) != 0)
                throw new AssertionError("El reintento modificó el stock.");
            System.out.println("OK: ajuste aplicado y reintento obsoleto rechazado.");
            System.out.println("Stock anterior: " + antes + " | Stock final: " + esperado);
            System.out.println("IMPORTANTE: el cambio permanece en la base de pruebas.");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            SesionUsuario.cerrarSesion();
        }
    }
}
