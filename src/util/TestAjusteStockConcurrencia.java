package util;

import Dao.StockProductoDao;
import Dao.UsuarioDao;
import java.math.BigDecimal;
import model.StockProducto;
import model.Usuario;
import services.AjusteStockService;
import services.ResultadoOperacion;
import sesion.SesionUsuario;

/** Prueba de lectura obsoleta (control optimista), sin modificar stock.
 *  No sustituye una prueba multihilo de concurrencia real.
 */
public class TestAjusteStockConcurrencia {
    private static final int ID_PRODUCTO = 1;
    private static final int ID_DEPOSITO = 1;

    public static void main(String[] args) {
        try {
            Usuario usuario = new UsuarioDao().buscarPorUsername("admin");
            if (usuario == null) throw new IllegalStateException("No existe el usuario admin.");
            SesionUsuario.iniciarSesion(usuario);

            StockProductoDao stockDao = new StockProductoDao();
            StockProducto inicial = stockDao.buscarPorProductoDeposito(ID_PRODUCTO, ID_DEPOSITO);
            if (inicial == null) throw new IllegalStateException("No existe stock para la prueba.");
            BigDecimal actual = inicial.getCantidad();
            // Simula que el operador vio un valor diferente al vigente.
            BigDecimal vistoObsoleto = actual.add(BigDecimal.ONE);
            BigDecimal fisico = actual.add(BigDecimal.valueOf(2));

            ResultadoOperacion resultado = new AjusteStockService().aplicarAjuste(
                    ID_DEPOSITO, ID_PRODUCTO, vistoObsoleto, fisico,
                    "TEST LECTURA OBSOLETA", "No debe modificar existencias");
            if (resultado.isExitoso())
                throw new AssertionError("Se aceptó stockVisto distinto del stock actual.");

            StockProducto finalStock = stockDao.buscarPorProductoDeposito(ID_PRODUCTO, ID_DEPOSITO);
            if (finalStock == null || finalStock.getCantidad().compareTo(actual) != 0)
                throw new AssertionError("El intento obsoleto alteró las existencias.");

            System.out.println("OK: ajuste obsoleto rechazado sin modificar stock.");
            System.out.println("Motivo: " + resultado.getMensaje());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            SesionUsuario.cerrarSesion();
        }
    }
}
