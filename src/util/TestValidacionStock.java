package util;

import Dao.DepositoDao;
import Dao.ProductoDao;

import java.math.BigDecimal;

import model.Deposito;
import model.Producto;

import services.ResultadoOperacion;
import services.StockProductoService;

public class TestValidacionStock {

    public static void main(String[] args) {

        StockProductoService stockService =
                new StockProductoService();

        ProductoDao productoDao =
                new ProductoDao();

        DepositoDao depositoDao =
                new DepositoDao();


        System.out.println(
                "========================================"
        );

        System.out.println(
                "      PRUEBA VALIDACION DE STOCK"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // BUSCAR PRODUCTO Y DEPÓSITO
        // =====================================================

        Producto producto =
                productoDao.buscarPorCodigo(
                        "BEB-0001"
                );

        Deposito deposito =
                depositoDao.buscarPorCodigo(
                        "DEP-01"
                );


        if (producto == null
                || deposito == null) {

            System.out.println(
                    "ERROR: Faltan datos para realizar la prueba."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Producto: "
                + producto.getNombre()
        );

        System.out.println(
                "Unidad: "
                + producto.getUnidadVenta().getCodigo()
        );

        System.out.println(
                "Permite decimales: "
                + producto.getUnidadVenta()
                        .isPermiteDecimales()
        );


        // =====================================================
        // PRUEBA 1
        // 75 UN -> DEBE FUNCIONAR
        // =====================================================

        System.out.println();
        System.out.println(
                "1. PROBANDO 75 UN"
        );

        System.out.println(
                "----------------------------------------"
        );


        ResultadoOperacion resultado1 =
                stockService.ajustarCantidad(

                        producto.getIdProducto(),

                        deposito.getIdDeposito(),

                        new BigDecimal("75")
                );


        System.out.println(
                resultado1.getMensaje()
        );


        // =====================================================
        // PRUEBA 2
        // 75.000 UN -> DEBE FUNCIONAR
        // =====================================================

        System.out.println();
        System.out.println(
                "2. PROBANDO 75.000 UN"
        );

        System.out.println(
                "----------------------------------------"
        );


        ResultadoOperacion resultado2 =
                stockService.ajustarCantidad(

                        producto.getIdProducto(),

                        deposito.getIdDeposito(),

                        new BigDecimal("75.000")
                );


        System.out.println(
                resultado2.getMensaje()
        );


        // =====================================================
        // PRUEBA 3
        // 75.500 UN -> DEBE RECHAZARSE
        // =====================================================

        System.out.println();
        System.out.println(
                "3. PROBANDO 75.500 UN"
        );

        System.out.println(
                "----------------------------------------"
        );


        ResultadoOperacion resultado3 =
                stockService.ajustarCantidad(

                        producto.getIdProducto(),

                        deposito.getIdDeposito(),

                        new BigDecimal("75.500")
                );


        System.out.println(
                resultado3.getMensaje()
        );


        // =====================================================
        // PRUEBA 4
        // CANTIDAD NEGATIVA -> DEBE RECHAZARSE
        // =====================================================

        System.out.println();
        System.out.println(
                "4. PROBANDO -10 UN"
        );

        System.out.println(
                "----------------------------------------"
        );


        ResultadoOperacion resultado4 =
                stockService.ajustarCantidad(

                        producto.getIdProducto(),

                        deposito.getIdDeposito(),

                        new BigDecimal("-10")
                );


        System.out.println(
                resultado4.getMensaje()
        );


        // =====================================================
        // CONSULTAR RESULTADO FINAL
        // =====================================================

        System.out.println();
        System.out.println(
                "5. STOCK FINAL"
        );

        System.out.println(
                "----------------------------------------"
        );


        BigDecimal cantidadFinal =
                stockService.obtenerCantidad(

                        producto.getIdProducto(),

                        deposito.getIdDeposito()
                );


        System.out.println(
                "Stock almacenado: "
                + FormateadorCantidad.formatearConUnidad(

                        cantidadFinal,

                        producto.getUnidadVenta()
                )
        );


        // =====================================================
        // FIN
        // =====================================================

        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "       FIN PRUEBA VALIDACION"
        );

        System.out.println(
                "========================================"
        );
    }
}