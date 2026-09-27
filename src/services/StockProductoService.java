package services;

import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.StockProductoDao;

import model.Deposito;
import model.Producto;
import model.StockProducto;

import java.math.BigDecimal;

import java.util.List;

public class StockProductoService {

    private final StockProductoDao stockDao;
    private final ProductoDao productoDao;
    private final DepositoDao depositoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public StockProductoService() {

        stockDao =
                new StockProductoDao();

        productoDao =
                new ProductoDao();

        depositoDao =
                new DepositoDao();
    }


    // =========================================================
    // INICIALIZAR STOCK
    // =========================================================

    public ResultadoOperacion inicializarStock(
            int idProducto,
            int idDeposito,
            BigDecimal cantidadInicial) {


        // =====================================================
        // VALIDAR IDs
        // =====================================================

        if (idProducto <= 0) {

            return ResultadoOperacion.error(
                    "El producto no es válido."
            );
        }


        if (idDeposito <= 0) {

            return ResultadoOperacion.error(
                    "El depósito no es válido."
            );
        }


        // =====================================================
        // VALIDAR CANTIDAD
        // =====================================================

        if (cantidadInicial == null) {

            return ResultadoOperacion.error(
                    "La cantidad inicial es obligatoria."
            );
        }


        if (cantidadInicial.compareTo(
                BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "La cantidad inicial no puede ser negativa."
            );
        }


        // =====================================================
        // BUSCAR PRODUCTO
        // =====================================================

        Producto producto =
                productoDao.buscarPorId(
                        idProducto
                );


        if (producto == null) {

            return ResultadoOperacion.error(
                    "El producto no existe."
            );
        }


        if (!producto.isActivo()) {

            return ResultadoOperacion.error(
                    "El producto está inactivo."
            );
        }


        // =====================================================
        // BUSCAR DEPÓSITO
        // =====================================================

        Deposito deposito =
                depositoDao.buscarPorId(
                        idDeposito
                );


        if (deposito == null) {

            return ResultadoOperacion.error(
                    "El depósito no existe."
            );
        }


        if (!deposito.isActivo()) {

            return ResultadoOperacion.error(
                    "El depósito está inactivo."
            );
        }


        // =====================================================
        // VERIFICAR SI YA EXISTE
        // =====================================================

        StockProducto existente =
                stockDao.buscarPorProductoDeposito(
                        idProducto,
                        idDeposito
                );


        if (existente != null) {

            return ResultadoOperacion.error(
                    "El producto ya posee un registro de stock "
                    + "en este depósito."
            );
        }


        // =====================================================
        // CREAR STOCK
        // =====================================================

        StockProducto stock =
                new StockProducto();

        stock.setProducto(producto);
        stock.setDeposito(deposito);

        stock.setCantidad(
                cantidadInicial
        );


        // =====================================================
        // GUARDAR
        // =====================================================

        if (stockDao.guardar(stock)) {

            return ResultadoOperacion.ok(
                    "Stock inicializado correctamente."
            );
        }


        return ResultadoOperacion.error(
                "No se pudo inicializar el stock."
        );
    }


    // =========================================================
    // AJUSTAR CANTIDAD
    //
    // Método técnico temporal.
    // Más adelante los cambios reales de stock se harán
    // mediante movimientos de stock.
    // =========================================================

    public ResultadoOperacion ajustarCantidad(
            int idProducto,
            int idDeposito,
            BigDecimal nuevaCantidad) {


        if (nuevaCantidad == null) {

            return ResultadoOperacion.error(
                    "La cantidad es obligatoria."
            );
        }


        if (nuevaCantidad.compareTo(
                BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El stock no puede ser negativo."
            );
        }


        StockProducto stock =
                stockDao.buscarPorProductoDeposito(
                        idProducto,
                        idDeposito
                );


        if (stock == null) {

            return ResultadoOperacion.error(
                    "No existe stock para ese producto "
                    + "en el depósito seleccionado."
            );
        }


        if (stockDao.actualizarCantidad(
                stock.getIdStock(),
                nuevaCantidad)) {

            return ResultadoOperacion.ok(
                    "Stock actualizado correctamente."
            );
        }


        return ResultadoOperacion.error(
                "No se pudo actualizar el stock."
        );
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public StockProducto buscarPorId(
            long idStock) {

        if (idStock <= 0) {
            return null;
        }

        return stockDao.buscarPorId(
                idStock
        );
    }


    // =========================================================
    // BUSCAR PRODUCTO / DEPÓSITO
    // =========================================================

    public StockProducto buscar(
            int idProducto,
            int idDeposito) {

        if (idProducto <= 0
                || idDeposito <= 0) {

            return null;
        }

        return stockDao.buscarPorProductoDeposito(
                idProducto,
                idDeposito
        );
    }


    // =========================================================
    // OBTENER CANTIDAD
    // =========================================================

    public BigDecimal obtenerCantidad(
            int idProducto,
            int idDeposito) {

        if (idProducto <= 0
                || idDeposito <= 0) {

            return BigDecimal.ZERO;
        }

        return stockDao.obtenerCantidad(
                idProducto,
                idDeposito
        );
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<StockProducto> listarTodos() {

        return stockDao.listarTodos();
    }


    // =========================================================
    // LISTAR POR PRODUCTO
    // =========================================================

    public List<StockProducto> listarPorProducto(
            int idProducto) {

        return stockDao.listarPorProducto(
                idProducto
        );
    }


    // =========================================================
    // LISTAR POR DEPÓSITO
    // =========================================================

    public List<StockProducto> listarPorDeposito(
            int idDeposito) {

        return stockDao.listarPorDeposito(
                idDeposito
        );
    }
}