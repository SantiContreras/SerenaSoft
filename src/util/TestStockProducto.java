package util;

import Dao.DepositoDao;
import Dao.ProductoDao;

import model.Deposito;
import model.Producto;
import model.StockProducto;

import services.ResultadoOperacion;
import services.StockProductoService;

import java.math.BigDecimal;

import java.util.List;

public class TestStockProducto {

    public static void main(String[] args) {

        StockProductoService stockService
                = new StockProductoService();

        ProductoDao productoDao
                = new ProductoDao();

        DepositoDao depositoDao
                = new DepositoDao();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "       PRUEBA MODULO STOCK PRODUCTO"
        );

        System.out.println(
                "========================================"
        );

        // =====================================================
        // 1. BUSCAR PRODUCTO
        // =====================================================
        System.out.println();
        System.out.println(
                "1. BUSCANDO PRODUCTO..."
        );

        Producto producto
                = productoDao.buscarPorCodigo(
                        "BEB-0001"
                );

        if (producto == null) {

            System.out.println(
                    "ERROR: No existe el producto BEB-0001."
            );

            return;
        }

        System.out.println(
                "Producto: "
                + producto.getNombre()
        );

        System.out.println(
                "ID Producto: "
                + producto.getIdProducto()
        );

        // =====================================================
        // 2. BUSCAR DEPÓSITO
        // =====================================================
        System.out.println();
        System.out.println(
                "2. BUSCANDO DEPOSITO..."
        );

        Deposito deposito
                = depositoDao.buscarPorCodigo(
                        "DEP-01"
                );

        if (deposito == null) {

            System.out.println(
                    "ERROR: No existe el depósito DEP-01."
            );

            return;
        }

        System.out.println(
                "Deposito: "
                + deposito.getNombre()
        );

        System.out.println(
                "ID Deposito: "
                + deposito.getIdDeposito()
        );

        // =====================================================
        // 3. INICIALIZAR STOCK
        // =====================================================
        System.out.println();
        System.out.println(
                "3. INICIALIZANDO STOCK..."
        );

        ResultadoOperacion resultado
                = stockService.inicializarStock(
                        producto.getIdProducto(),
                        deposito.getIdDeposito(),
                        new BigDecimal("50.000")
                );

        System.out.println(
                resultado.getMensaje()
        );

        // =====================================================
        // 4. CONSULTAR STOCK
        // =====================================================
        System.out.println();
        System.out.println(
                "4. CONSULTANDO STOCK..."
        );

        StockProducto stock
                = stockService.buscar(
                        producto.getIdProducto(),
                        deposito.getIdDeposito()
                );

        if (stock == null) {

            System.out.println(
                    "ERROR: No se encontró el stock."
            );

            return;
        }

        System.out.println(
                "ID Stock: "
                + stock.getIdStock()
        );

        System.out.println(
                "Producto: "
                + stock.getProducto().getNombre()
        );

        System.out.println(
                "Deposito: "
                + stock.getDeposito().getNombre()
        );

        System.out.println(
                "Cantidad: "
                + FormateadorCantidad.formatearConUnidad(
                        stock.getCantidad(),
                        stock.getProducto().getUnidadVenta()
                )
        );

        System.out.println(
                "Fecha actualización: "
                + stock.getFechaActualizacion()
        );

        // =====================================================
        // 5. AJUSTE TÉCNICO DE PRUEBA
        // =====================================================
        System.out.println();
        System.out.println(
                "5. AJUSTANDO STOCK DE PRUEBA..."
        );

        ResultadoOperacion ajuste
                = stockService.ajustarCantidad(
                        producto.getIdProducto(),
                        deposito.getIdDeposito(),
                        new BigDecimal("75.000")
                );

        System.out.println(
                ajuste.getMensaje()
        );

        // =====================================================
        // 6. VOLVER A CONSULTAR
        // =====================================================
        System.out.println();
        System.out.println(
                "6. VERIFICANDO NUEVO STOCK..."
        );

        StockProducto actualizado
                = stockService.buscar(
                        producto.getIdProducto(),
                        deposito.getIdDeposito()
                );

        if (actualizado != null) {

            System.out.println(
                    "Producto: "
                    + actualizado.getProducto().getNombre()
            );

            System.out.println(
                    "Deposito: "
                    + actualizado.getDeposito().getNombre()
            );

 

            System.out.println(
                    "Fecha actualización: "
                    + actualizado.getFechaActualizacion()
            );
        }

        // =====================================================
        // 7. LISTAR STOCK DEL DEPÓSITO
        // =====================================================
        System.out.println();
        System.out.println(
                "7. STOCK DEL DEPOSITO"
        );

        System.out.println(
                "----------------------------------------"
        );

        List<StockProducto> lista
                = stockService.listarPorDeposito(
                        deposito.getIdDeposito()
                );

     for (StockProducto item : lista) {

    System.out.println(
            item.getProducto().getCodigo()
            + " | "
            + item.getProducto().getNombre()
            + " | Stock: "
            + FormateadorCantidad.formatearConUnidad(
                    item.getCantidad(),
                    item.getProducto().getUnidadVenta()
            )
    );
}

        // =====================================================
        // FIN
        // =====================================================
        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "      FIN PRUEBA STOCK PRODUCTO"
        );

        System.out.println(
                "========================================"
        );
    }
}
