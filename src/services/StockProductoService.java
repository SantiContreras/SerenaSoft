package services;

import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.StockProductoDao;

import java.math.BigDecimal;
import java.util.List;

import model.Deposito;
import model.Producto;
import model.StockProducto;
import model.UnidadMedida;

public class StockProductoService {

    // =========================================================
    // DAO
    // =========================================================

    private final StockProductoDao stockDao;
    private final ProductoDao productoDao;
    private final DepositoDao depositoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public StockProductoService() {

        this.stockDao =
                new StockProductoDao();

        this.productoDao =
                new ProductoDao();

        this.depositoDao =
                new DepositoDao();
    }


    // =========================================================
    // INICIALIZAR STOCK
    // =========================================================

    public ResultadoOperacion inicializarStock(
            int idProducto,
            int idDeposito,
            BigDecimal cantidadInicial) {

        // -----------------------------------------------------
        // VALIDAR IDs
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // BUSCAR PRODUCTO
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // BUSCAR DEPÓSITO
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // VALIDAR CANTIDAD
        // -----------------------------------------------------

        ResultadoOperacion validacionCantidad =
                validarCantidad(
                        producto,
                        cantidadInicial
                );

        if (!validacionCantidad.isExitoso()) {

            return validacionCantidad;
        }


        // -----------------------------------------------------
        // VERIFICAR SI YA EXISTE STOCK
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // CREAR STOCK
        // -----------------------------------------------------

        StockProducto stock =
                new StockProducto();

        stock.setProducto(producto);
        stock.setDeposito(deposito);
        stock.setCantidad(cantidadInicial);


        // -----------------------------------------------------
        // GUARDAR
        // -----------------------------------------------------

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
    // IMPORTANTE:
    // Este método es técnico.
    // Más adelante los cambios normales de stock pasarán por
    // ingresos, salidas, ventas, compras y ajustes registrados.
    // =========================================================

    public ResultadoOperacion ajustarCantidad(
            int idProducto,
            int idDeposito,
            BigDecimal nuevaCantidad) {

        // -----------------------------------------------------
        // VALIDAR IDs
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // BUSCAR PRODUCTO
        // -----------------------------------------------------

        Producto producto =
                productoDao.buscarPorId(
                        idProducto
                );

        if (producto == null) {

            return ResultadoOperacion.error(
                    "El producto no existe."
            );
        }


        // -----------------------------------------------------
        // VALIDAR CANTIDAD SEGÚN UNIDAD
        // -----------------------------------------------------

        ResultadoOperacion validacionCantidad =
                validarCantidad(
                        producto,
                        nuevaCantidad
                );

        if (!validacionCantidad.isExitoso()) {

            return validacionCantidad;
        }


        // -----------------------------------------------------
        // BUSCAR STOCK
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // ACTUALIZAR
        // -----------------------------------------------------

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
    // BUSCAR POR PRODUCTO Y DEPÓSITO
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

        if (idProducto <= 0) {

            return List.of();
        }

        return stockDao.listarPorProducto(
                idProducto
        );
    }


    // =========================================================
    // LISTAR POR DEPÓSITO
    // =========================================================

    public List<StockProducto> listarPorDeposito(
            int idDeposito) {

        if (idDeposito <= 0) {

            return List.of();
        }

        return stockDao.listarPorDeposito(
                idDeposito
        );
    }


    // =========================================================
    // VALIDAR CANTIDAD
    // =========================================================

    private ResultadoOperacion validarCantidad(
            Producto producto,
            BigDecimal cantidad) {

        // -----------------------------------------------------
        // CANTIDAD OBLIGATORIA
        // -----------------------------------------------------

        if (cantidad == null) {

            return ResultadoOperacion.error(
                    "La cantidad es obligatoria."
            );
        }


        // -----------------------------------------------------
        // NO PERMITIR NEGATIVOS
        // -----------------------------------------------------

        if (cantidad.compareTo(
                BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "La cantidad no puede ser negativa."
            );
        }


        // -----------------------------------------------------
        // OBTENER UNIDAD DE VENTA
        // -----------------------------------------------------

        UnidadMedida unidad =
                producto.getUnidadVenta();


        // Si el producto no tiene unidad configurada,
        // no podemos aplicar la validación de decimales.
        // Permitimos continuar.
        //
        // Más adelante podemos hacer obligatoria la unidad
        // para productos que controlan stock.

        if (unidad == null) {

            return ResultadoOperacion.ok(
                    "Cantidad válida."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DECIMALES
        // -----------------------------------------------------

        if (!unidad.isPermiteDecimales()) {

            BigDecimal cantidadSinCeros =
                    cantidad.stripTrailingZeros();


            /*
             * Ejemplos:
             *
             * 75.000 -> stripTrailingZeros -> 75
             * scale = 0
             * VÁLIDO
             *
             * 75.500 -> stripTrailingZeros -> 75.5
             * scale = 1
             * INVÁLIDO
             */

            if (cantidadSinCeros.scale() > 0) {

                return ResultadoOperacion.error(
                        "La unidad "
                        + unidad.getCodigo()
                        + " no permite cantidades decimales."
                );
            }
        }


        // -----------------------------------------------------
        // MÁXIMO 3 DECIMALES
        // -----------------------------------------------------

        BigDecimal cantidadSinCeros =
                cantidad.stripTrailingZeros();

        if (cantidadSinCeros.scale() > 3) {

            return ResultadoOperacion.error(
                    "La cantidad no puede tener más "
                    + "de 3 decimales."
            );
        }


        return ResultadoOperacion.ok(
                "Cantidad válida."
        );
    }
}