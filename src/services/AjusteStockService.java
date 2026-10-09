package services;

import Configuracion.conexion;
import Dao.AjusteStockDao;
import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.StockProductoDao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import model.AjusteStock;
import model.AjusteStockDetalle;
import model.Deposito;
import model.Producto;
import model.StockProducto;
import sesion.SesionUsuario;

/** Registra un conteo físico y reemplaza el stock en una sola transacción. */
public class AjusteStockService {
    private final AjusteStockDao ajusteDao = new AjusteStockDao();
    private final DepositoDao depositoDao = new DepositoDao();
    private final ProductoDao productoDao = new ProductoDao();
    private final StockProductoDao stockDao = new StockProductoDao();

    public ResultadoOperacion aplicarAjuste(int idDeposito, int idProducto,
            BigDecimal stockVisto, BigDecimal stockFisico,
            String motivo, String observaciones) {
        if (!SesionUsuario.haySesion() || SesionUsuario.getUsuarioActual() == null)
            return ResultadoOperacion.error("Debe iniciar sesión.");
        if (idDeposito <= 0 || idProducto <= 0 || stockVisto == null || stockFisico == null)
            return ResultadoOperacion.error("Seleccione un producto y un depósito válidos.");
        if (stockFisico.signum() < 0 || stockFisico.stripTrailingZeros().scale() > 3)
            return ResultadoOperacion.error("El stock físico debe ser positivo o cero, con hasta 3 decimales.");
        if (motivo == null || motivo.isBlank() || motivo.length() > 120)
            return ResultadoOperacion.error("Seleccione un motivo válido.");
        if (observaciones != null && observaciones.length() > 500)
            return ResultadoOperacion.error("Las observaciones no pueden superar 500 caracteres.");

        Deposito deposito = depositoDao.buscarPorId(idDeposito);
        Producto producto = productoDao.buscarPorId(idProducto);
        if (deposito == null || !deposito.isActivo())
            return ResultadoOperacion.error("El depósito no está disponible.");
        if (producto == null || !producto.isActivo() || !producto.isControlaStock())
            return ResultadoOperacion.error("El producto no está activo o no controla stock.");
        if (producto.getUnidadVenta() != null
                && !producto.getUnidadVenta().isPermiteDecimales()
                && stockFisico.stripTrailingZeros().scale() > 0)
            return ResultadoOperacion.error("Este producto requiere cantidades enteras.");

        try (Connection cn = conexion.getConexion()) {
            cn.setAutoCommit(false);
            try {
                StockProducto stock = stockDao.buscarParaActualizar(cn, idProducto, idDeposito);
                BigDecimal actual = stock == null ? BigDecimal.ZERO : stock.getCantidad();
                if (actual == null) actual = BigDecimal.ZERO;
                // Evita reemplazar cambios realizados por otro operador.
                if (actual.compareTo(stockVisto) != 0)
                    throw new SQLException("El stock cambió desde que abrió el ajuste (ahora: "
                            + actual.stripTrailingZeros().toPlainString() + "). Vuelva a buscar el producto.");
                BigDecimal diferencia = stockFisico.subtract(actual);
                if (diferencia.signum() == 0)
                    throw new SQLException("No hay diferencias para ajustar.");

                AjusteStock ajuste = new AjusteStock();
                ajuste.setDeposito(deposito);
                ajuste.setUsuario(SesionUsuario.getUsuarioActual());
                ajuste.setMotivo(motivo.trim());
                ajuste.setObservaciones(observaciones == null ? "" : observaciones.trim());
                ajuste.setEstado("BORRADOR");
                long idAjuste = ajusteDao.guardar(ajuste, cn);
                if (idAjuste <= 0) throw new SQLException("No se pudo guardar la cabecera.");

                AjusteStockDetalle detalle = new AjusteStockDetalle();
                detalle.setIdAjuste(idAjuste);
                detalle.setProducto(producto);
                detalle.setStockSistema(actual);
                detalle.setStockFisico(stockFisico);
                detalle.setDiferencia(diferencia);
                if (ajusteDao.guardarDetalle(detalle, cn) <= 0)
                    throw new SQLException("No se pudo guardar el detalle.");

                if (stock == null) {
                    // Requiere UNIQUE(id_producto,id_deposito) en stock_producto.
                    if (!stockDao.sumarOCrearStock(cn, idProducto, idDeposito, stockFisico))
                        throw new SQLException("No se pudo crear el stock.");
                } else if (!stockDao.actualizarCantidad(cn, stock.getIdStock(), stockFisico)) {
                    throw new SQLException("No se pudo actualizar el stock.");
                }
                if (!ajusteDao.cambiarEstadoSiCoincide(idAjuste, "BORRADOR", "CONFIRMADO", cn))
                    throw new SQLException("No se pudo confirmar el ajuste.");
                cn.commit();
                return ResultadoOperacion.ok("Ajuste N.º " + idAjuste + " confirmado. Stock actualizado.");
            } catch (Exception e) {
                cn.rollback();
                return ResultadoOperacion.error(e.getMessage());
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return ResultadoOperacion.error("Error de base de datos: " + e.getMessage());
        }
    }
}
