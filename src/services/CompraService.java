package services;

import Dao.CompraDao;
import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.ProveedorDao;

import model.Compra;
import model.CompraDetalle;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.Usuario;

import sesion.SesionUsuario;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

import Configuracion.conexion;
import Dao.StockProductoDao;

public class CompraService {

    // =========================================================
    // DAOS
    // =========================================================
    private final CompraDao compraDao;
    private final ProveedorDao proveedorDao;
    private final ProductoDao productoDao;
    private final DepositoDao depositoDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public CompraService() {

        this.compraDao
                = new CompraDao();

        this.proveedorDao
                = new ProveedorDao();

        this.productoDao
                = new ProductoDao();

        this.depositoDao
                = new DepositoDao();
    }

    // =========================================================
// CONFIRMAR COMPRA
//
// Al confirmar:
//
// 1. Bloquea la compra.
// 2. Verifica que siga en BORRADOR.
// 3. Valida sus detalles.
// 4. Convierte cantidad comprada a cantidad de stock.
// 5. Suma o crea stock_producto.
// 6. Cambia BORRADOR -> CONFIRMADA.
// 7. Confirma todo con COMMIT.
//
// Si algo falla:
// ROLLBACK completo.
// =========================================================
    public ResultadoOperacion confirmarCompra(
            long idCompra) {

        // =====================================================
        // VALIDACIONES INICIALES
        // =====================================================
        if (idCompra <= 0) {

            return ResultadoOperacion.error(
                    "La compra indicada no es válida."
            );
        }

        if (!SesionUsuario.haySesion()) {

            return ResultadoOperacion.error(
                    "No hay una sesión de usuario activa."
            );
        }

        Connection cn = null;

        try {

            cn = conexion.getConexion();

            cn.setAutoCommit(false);

            // =================================================
            // 1. BLOQUEAR COMPRA
            // =================================================
            Compra compra
                    = compraDao.buscarPorIdParaActualizar(
                            idCompra,
                            cn
                    );

            if (compra == null) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "La compra no existe."
                );
            }

            // =================================================
            // 2. VERIFICAR ESTADO
            // =================================================
            if (!compra.estaEnBorrador()) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "La compra ya no se encuentra "
                        + "en estado BORRADOR."
                );
            }

            // =================================================
            // 3. VERIFICAR DEPÓSITO
            // =================================================
            if (compra.getDeposito() == null
                    || compra.getDeposito()
                            .getIdDeposito() <= 0) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "La compra no tiene un depósito válido."
                );
            }

            int idDeposito
                    = compra.getDeposito()
                            .getIdDeposito();

            // =================================================
            // 4. VERIFICAR DETALLES
            // =================================================
            List<CompraDetalle> detalles
                    = compra.getDetalles();

            if (detalles == null
                    || detalles.isEmpty()) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "No se puede confirmar una compra "
                        + "sin productos."
                );
            }

            // =================================================
            // 5. RECALCULAR Y VALIDAR TOTAL
            //
            // No confiamos ciegamente en los totales que
            // quedaron guardados anteriormente.
            // =================================================
            BigDecimal subtotalCalculado
                    = BigDecimal.ZERO;

            for (CompraDetalle detalle
                    : detalles) {

                if (detalle.getProducto() == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Existe un detalle sin producto."
                    );
                }

                if (detalle.getCantidad() == null
                        || detalle.getCantidad()
                                .compareTo(
                                        BigDecimal.ZERO
                                ) <= 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Existe un producto con "
                            + "cantidad inválida."
                    );
                }

                if (detalle.getCostoUnitario() == null
                        || detalle.getCostoUnitario()
                                .compareTo(
                                        BigDecimal.ZERO
                                ) < 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Existe un producto con "
                            + "costo inválido."
                    );
                }

                BigDecimal subtotalDetalle
                        = detalle.getCantidad()
                                .multiply(
                                        detalle.getCostoUnitario()
                                )
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );

                subtotalCalculado
                        = subtotalCalculado.add(
                                subtotalDetalle
                        );
            }

            subtotalCalculado
                    = subtotalCalculado.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal descuento
                    = compra.getDescuento();

            if (descuento == null) {

                descuento
                        = BigDecimal.ZERO;
            }

            descuento
                    = descuento.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            if (descuento.compareTo(
                    BigDecimal.ZERO
            ) < 0) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "El descuento de la compra "
                        + "no puede ser negativo."
                );
            }

            if (descuento.compareTo(
                    subtotalCalculado
            ) > 0) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "El descuento no puede superar "
                        + "el subtotal."
                );
            }

            BigDecimal totalCalculado
                    = subtotalCalculado
                            .subtract(
                                    descuento
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            // =================================================
            // 6. ACTUALIZAR TOTALES DEFINITIVOS
            // =================================================
            boolean totalesActualizados
                    = compraDao.actualizarTotales(
                            idCompra,
                            subtotalCalculado,
                            descuento,
                            totalCalculado,
                            cn
                    );

            if (!totalesActualizados) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "No se pudieron actualizar "
                        + "los totales de la compra."
                );
            }

            // =================================================
            // 7. PROCESAR CADA PRODUCTO
            // =================================================
            StockProductoDao stockProductoDao
                    = new StockProductoDao();

            for (CompraDetalle detalle
                    : detalles) {

                int idProducto
                        = detalle.getProducto()
                                .getIdProducto();

                // ---------------------------------------------
                // RECUPERAR PRODUCTO ACTUAL
                // ---------------------------------------------
                Producto producto
                        = productoDao.buscarPorId(
                                idProducto
                        );

                if (producto == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Uno de los productos "
                            + "ya no existe."
                    );
                }

                if (!producto.isActivo()) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El producto "
                            + producto.getNombre()
                            + " está inactivo."
                    );
                }

                if (!producto.isControlaStock()) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El producto "
                            + producto.getNombre()
                            + " no controla stock."
                    );
                }

                // =================================================
                // 8. CANTIDAD DE STOCK HISTÓRICA DEL DETALLE
                //
                // La presentación y el factor utilizados quedaron
                // guardados en compra_detalle al agregar el producto.
                // NO usamos producto.factorConversion porque puede
                // cambiar en compras futuras.
                // =================================================
                BigDecimal factorConversion
                        = detalle.getFactorConversion();

                if (factorConversion == null
                        || factorConversion.compareTo(
                                BigDecimal.ZERO
                        ) <= 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El producto "
                            + producto.getNombre()
                            + " tiene un factor de conversión "
                            + "inválido en el detalle de compra."
                    );
                }

                BigDecimal cantidadStock
                        = detalle.getCantidadStock();

                if (cantidadStock == null
                        || cantidadStock.compareTo(
                                BigDecimal.ZERO
                        ) <= 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La cantidad de stock guardada para "
                            + producto.getNombre()
                            + " no es válida."
                    );
                }

                BigDecimal cantidadStockEsperada
                        = detalle.getCantidad()
                                .multiply(
                                        factorConversion
                                );

                try {

                    cantidadStock
                            = cantidadStock.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                    cantidadStockEsperada
                            = cantidadStockEsperada.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                } catch (ArithmeticException ex) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La conversión de stock del producto "
                            + producto.getNombre()
                            + " genera más de 3 decimales."
                    );
                }

                if (cantidadStock.compareTo(
                        cantidadStockEsperada
                ) != 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La cantidad de stock del producto "
                            + producto.getNombre()
                            + " no coincide con la cantidad comprada "
                            + "y el factor de conversión."
                    );
                }

                // =================================================
                // 9. SUMAR O CREAR STOCK
                //
                // Si existe:
                //      cantidad = cantidad + ingreso
                //
                // Si no existe:
                //      INSERT stock_producto
                // =================================================
                boolean stockActualizado
                        = stockProductoDao.sumarOCrearStock(
                                cn,
                                idProducto,
                                idDeposito,
                                cantidadStock
                        );

                if (!stockActualizado) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No se pudo actualizar el stock "
                            + "del producto "
                            + producto.getNombre()
                            + "."
                    );
                }
            }

            // =================================================
            // 10. CAMBIAR ESTADO
            //
            // Esta condición es una segunda protección contra
            // confirmaciones duplicadas.
            // =================================================
            boolean estadoActualizado
                    = compraDao.cambiarEstadoSiCoincide(
                            idCompra,
                            "BORRADOR",
                            "CONFIRMADA",
                            cn
                    );

            if (!estadoActualizado) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "La compra no pudo ser confirmada "
                        + "porque su estado cambió."
                );
            }

            // =================================================
            // 11. COMMIT
            // =================================================
            cn.commit();

            return ResultadoOperacion.ok(
                    "Compra confirmada correctamente."
            );

        } catch (Exception ex) {

            if (cn != null) {

                try {

                    cn.rollback();

                } catch (SQLException rollbackEx) {
                    // No reemplazamos el error original.
                }
            }

            return ResultadoOperacion.error(
                    "Error al confirmar la compra: "
                    + ex.getMessage()
            );

        } finally {

            if (cn != null) {

                try {

                    cn.setAutoCommit(
                            true
                    );

                } catch (SQLException ex) {
                    // No hacemos nada.
                }

                try {

                    cn.close();

                } catch (SQLException ex) {
                    // No hacemos nada.
                }
            }
        }
    }

    // =========================================================
    // CREAR COMPRA BORRADOR
    // =========================================================
    public ResultadoOperacion crearCompra(
            Compra compra) {

        if (compra == null) {

            return ResultadoOperacion.error(
                    "La compra no puede ser nula."
            );
        }

        // -----------------------------------------------------
        // SESIÓN
        // -----------------------------------------------------
        if (!SesionUsuario.haySesion()) {

            return ResultadoOperacion.error(
                    "No hay una sesión de usuario activa."
            );
        }

        // -----------------------------------------------------
        // PROVEEDOR
        // -----------------------------------------------------
        if (compra.getProveedor() == null
                || compra.getProveedor()
                        .getIdProveedor() <= 0) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un proveedor."
            );
        }

        // -----------------------------------------------------
        // DEPÓSITO
        // -----------------------------------------------------
        if (compra.getDeposito() == null
                || compra.getDeposito()
                        .getIdDeposito() <= 0) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un depósito."
            );
        }

        // -----------------------------------------------------
        // LIMPIAR TEXTOS
        // -----------------------------------------------------
        compra.setNumeroComprobante(
                limpiar(
                        compra.getNumeroComprobante()
                )
        );

        compra.setObservaciones(
                limpiar(
                        compra.getObservaciones()
                )
        );

        // -----------------------------------------------------
        // LONGITUDES
        // -----------------------------------------------------
        if (compra.getNumeroComprobante() != null
                && compra.getNumeroComprobante()
                        .length() > 50) {

            return ResultadoOperacion.error(
                    "El número de comprobante no puede "
                    + "superar los 50 caracteres."
            );
        }

        if (compra.getObservaciones() != null
                && compra.getObservaciones()
                        .length() > 500) {

            return ResultadoOperacion.error(
                    "Las observaciones no pueden superar "
                    + "los 500 caracteres."
            );
        }

        // -----------------------------------------------------
        // ORIGEN DE CARGA
        // -----------------------------------------------------
        String origen
                = compra.getOrigenCarga();

        if (!origenValido(origen)) {

            return ResultadoOperacion.error(
                    "El origen de carga no es válido."
            );
        }

        try {

            // -------------------------------------------------
            // RECUPERAR PROVEEDOR REAL
            // -------------------------------------------------
            Proveedor proveedor
                    = proveedorDao.buscarPorId(
                            compra.getProveedor()
                                    .getIdProveedor()
                    );

            if (proveedor == null) {

                return ResultadoOperacion.error(
                        "El proveedor seleccionado no existe."
                );
            }

            if (!proveedor.isActivo()) {

                return ResultadoOperacion.error(
                        "El proveedor seleccionado está inactivo."
                );
            }

            // -------------------------------------------------
            // RECUPERAR DEPÓSITO REAL
            // -------------------------------------------------
            Deposito deposito
                    = depositoDao.buscarPorId(
                            compra.getDeposito()
                                    .getIdDeposito()
                    );

            if (deposito == null) {

                return ResultadoOperacion.error(
                        "El depósito seleccionado no existe."
                );
            }

            if (!deposito.isActivo()) {

                return ResultadoOperacion.error(
                        "El depósito seleccionado está inactivo."
                );
            }

            // -------------------------------------------------
            // USUARIO DE SESIÓN
            // -------------------------------------------------
            Usuario usuario
                    = SesionUsuario.getUsuarioActual();

            if (usuario == null) {

                return ResultadoOperacion.error(
                        "No se pudo obtener el usuario "
                        + "de la sesión."
                );
            }

            // -------------------------------------------------
            // PREPARAR BORRADOR
            // -------------------------------------------------
            compra.setProveedor(
                    proveedor
            );

            compra.setDeposito(
                    deposito
            );

            compra.setUsuario(
                    usuario
            );

            if (compra.getFecha() == null) {

                compra.setFecha(
                        LocalDateTime.now()
                );
            }

            compra.setSubtotal(
                    BigDecimal.ZERO
            );

            compra.setDescuento(
                    BigDecimal.ZERO
            );

            compra.setTotal(
                    BigDecimal.ZERO
            );

            compra.setEstado(
                    "BORRADOR"
            );

            // -------------------------------------------------
            // GUARDAR
            // -------------------------------------------------
            long idCompra
                    = compraDao.guardar(
                            compra
                    );

            if (idCompra <= 0) {

                return ResultadoOperacion.error(
                        "No se pudo crear la compra."
                );
            }

            return ResultadoOperacion.ok(
                    "Compra borrador creada correctamente. "
                    + "N.º "
                    + idCompra
            );

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al crear la compra: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // AGREGAR PRODUCTO
    // =========================================================
  public ResultadoOperacion agregarProducto(
        long idCompra,
        int idProducto,
        String unidadCompra,
        BigDecimal factorConversion,
        BigDecimal cantidad,
        BigDecimal costoUnitario) {

        if (idCompra <= 0) {
            return ResultadoOperacion.error(
                    "La compra indicada no es válida."
            );
        }

        if (idProducto <= 0) {
            return ResultadoOperacion.error(
                    "El producto indicado no es válido."
            );
        }

        unidadCompra = limpiar(unidadCompra);

        if (unidadCompra == null) {
            return ResultadoOperacion.error(
                    "Debe indicar la unidad o presentación de compra."
            );
        }

        if (unidadCompra.length() > 20) {
            return ResultadoOperacion.error(
                    "La unidad de compra no puede superar los 20 caracteres."
            );
        }

        if (factorConversion == null
                || factorConversion.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoOperacion.error(
                    "El factor de conversión debe ser mayor que cero."
            );
        }

        if (cantidad == null
                || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoOperacion.error(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (costoUnitario == null
                || costoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            return ResultadoOperacion.error(
                    "El costo unitario no puede ser negativo."
            );
        }

        try (Connection cn = conexion.getConexion()) {

            cn.setAutoCommit(false);

            try {

                Compra compra =
                        compraDao.buscarPorIdParaActualizar(
                                idCompra,
                                cn
                        );

                if (compra == null) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "La compra no existe."
                    );
                }

                if (!compra.estaEnBorrador()) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "Solo se pueden agregar productos "
                            + "a una compra en borrador."
                    );
                }

                Producto producto =
                        productoDao.buscarPorId(
                                idProducto
                        );

                if (producto == null) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "El producto no existe."
                    );
                }

                if (!producto.isActivo()) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "El producto está inactivo."
                    );
                }

                if (!producto.isControlaStock()) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "El producto no controla stock."
                    );
                }

                // Si se usa la unidad de compra o de venta/stock
                // configurada en el producto, respetamos su regla
                // de decimales. Las presentaciones desconocidas
                // (por ejemplo CAJA/BULTO ingresadas manualmente)
                // se consideran enteras.
                boolean permiteDecimalesCompra = false;

                if (producto.getUnidadCompra() != null
                        && producto.getUnidadCompra().getCodigo() != null
                        && producto.getUnidadCompra().getCodigo()
                                .equalsIgnoreCase(unidadCompra)) {

                    permiteDecimalesCompra =
                            producto.getUnidadCompra()
                                    .isPermiteDecimales();

                } else if (producto.getUnidadVenta() != null
                        && producto.getUnidadVenta().getCodigo() != null
                        && producto.getUnidadVenta().getCodigo()
                                .equalsIgnoreCase(unidadCompra)) {

                    permiteDecimalesCompra =
                            producto.getUnidadVenta()
                                    .isPermiteDecimales();
                }

                if (!permiteDecimalesCompra
                        && tieneDecimales(cantidad)) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La presentación "
                            + unidadCompra
                            + " no permite cantidades decimales."
                    );
                }

                for (CompraDetalle detalle : compra.getDetalles()) {

                    if (detalle.getProducto() != null
                            && detalle.getProducto()
                                    .getIdProducto() == idProducto) {

                        cn.rollback();

                        return ResultadoOperacion.error(
                                "El producto ya se encuentra "
                                + "en la compra."
                        );
                    }
                }

                try {

                    factorConversion =
                            factorConversion.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                    cantidad =
                            cantidad.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                } catch (ArithmeticException ex) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La cantidad y el factor de conversión "
                            + "admiten como máximo 3 decimales."
                    );
                }

                costoUnitario =
                        costoUnitario.setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

                BigDecimal cantidadStock;

                try {

                    cantidadStock =
                            cantidad.multiply(
                                    factorConversion
                            ).setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                } catch (ArithmeticException ex) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La conversión genera una cantidad de stock "
                            + "con más de 3 decimales."
                    );
                }

                BigDecimal subtotalDetalle =
                        cantidad.multiply(
                                costoUnitario
                        ).setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

                CompraDetalle detalle =
                        new CompraDetalle();

                detalle.setIdCompra(idCompra);
                detalle.setProducto(producto);
                detalle.setUnidadCompra(
                        unidadCompra.toUpperCase()
                );
                detalle.setFactorConversion(
                        factorConversion
                );
                detalle.setCantidad(cantidad);
                detalle.setCantidadStock(
                        cantidadStock
                );
                detalle.setCostoUnitario(
                        costoUnitario
                );
                detalle.setSubtotal(
                        subtotalDetalle
                );

                long idDetalle =
                        compraDao.guardarDetalle(
                                detalle,
                                cn
                        );

                if (idDetalle <= 0) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "No se pudo agregar el producto."
                    );
                }

                recalcularTotales(
                        idCompra,
                        compra.getDescuento(),
                        cn
                );

                cn.commit();

                return ResultadoOperacion.ok(
                        "Producto agregado correctamente."
                );

            } catch (Exception ex) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Error al agregar el producto: "
                        + ex.getMessage()
                );

            } finally {

                try {
                    cn.setAutoCommit(true);
                } catch (SQLException ex) {
                    // No hacemos nada.
                }
            }

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error de conexión: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ACTUALIZAR DETALLE
    // =========================================================
    public ResultadoOperacion actualizarDetalle(
            long idCompra,
            long idDetalle,
            String unidadCompra,
            BigDecimal factorConversion,
            BigDecimal cantidad,
            BigDecimal costoUnitario) {

        if (idCompra <= 0
                || idDetalle <= 0) {
            return ResultadoOperacion.error(
                    "La compra o el detalle no son válidos."
            );
        }

        unidadCompra = limpiar(unidadCompra);

        if (unidadCompra == null) {
            return ResultadoOperacion.error(
                    "Debe indicar la unidad o presentación de compra."
            );
        }

        if (unidadCompra.length() > 20) {
            return ResultadoOperacion.error(
                    "La unidad de compra no puede superar los 20 caracteres."
            );
        }

        if (factorConversion == null
                || factorConversion.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoOperacion.error(
                    "El factor de conversión debe ser mayor que cero."
            );
        }

        if (cantidad == null
                || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoOperacion.error(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (costoUnitario == null
                || costoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            return ResultadoOperacion.error(
                    "El costo unitario no puede ser negativo."
            );
        }

        try (Connection cn = conexion.getConexion()) {

            cn.setAutoCommit(false);

            try {

                Compra compra =
                        compraDao.buscarPorIdParaActualizar(
                                idCompra,
                                cn
                        );

                if (compra == null) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "La compra no existe."
                    );
                }

                if (!compra.estaEnBorrador()) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "Solo se puede modificar "
                            + "una compra en borrador."
                    );
                }

                CompraDetalle detalleEncontrado = null;

                for (CompraDetalle detalle : compra.getDetalles()) {
                    if (detalle.getIdDetalle() == idDetalle) {
                        detalleEncontrado = detalle;
                        break;
                    }
                }

                if (detalleEncontrado == null) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "El detalle no pertenece a la compra."
                    );
                }

                Producto producto =
                        detalleEncontrado.getProducto();

                if (producto == null) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "No se pudo obtener el producto."
                    );
                }

                boolean permiteDecimalesCompra = false;

                if (producto.getUnidadCompra() != null
                        && producto.getUnidadCompra().getCodigo() != null
                        && producto.getUnidadCompra().getCodigo()
                                .equalsIgnoreCase(unidadCompra)) {

                    permiteDecimalesCompra =
                            producto.getUnidadCompra()
                                    .isPermiteDecimales();

                } else if (producto.getUnidadVenta() != null
                        && producto.getUnidadVenta().getCodigo() != null
                        && producto.getUnidadVenta().getCodigo()
                                .equalsIgnoreCase(unidadCompra)) {

                    permiteDecimalesCompra =
                            producto.getUnidadVenta()
                                    .isPermiteDecimales();
                }

                if (!permiteDecimalesCompra
                        && tieneDecimales(cantidad)) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La presentación "
                            + unidadCompra
                            + " no permite cantidades decimales."
                    );
                }

                try {

                    factorConversion =
                            factorConversion.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                    cantidad =
                            cantidad.setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                } catch (ArithmeticException ex) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La cantidad y el factor de conversión "
                            + "admiten como máximo 3 decimales."
                    );
                }

                costoUnitario =
                        costoUnitario.setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

                BigDecimal cantidadStock;

                try {

                    cantidadStock =
                            cantidad.multiply(
                                    factorConversion
                            ).setScale(
                                    3,
                                    RoundingMode.UNNECESSARY
                            );

                } catch (ArithmeticException ex) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La conversión genera una cantidad de stock "
                            + "con más de 3 decimales."
                    );
                }

                BigDecimal subtotal =
                        cantidad.multiply(
                                costoUnitario
                        ).setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

                detalleEncontrado.setUnidadCompra(
                        unidadCompra.toUpperCase()
                );
                detalleEncontrado.setFactorConversion(
                        factorConversion
                );
                detalleEncontrado.setCantidad(
                        cantidad
                );
                detalleEncontrado.setCantidadStock(
                        cantidadStock
                );
                detalleEncontrado.setCostoUnitario(
                        costoUnitario
                );
                detalleEncontrado.setSubtotal(
                        subtotal
                );

                boolean actualizado =
                        compraDao.actualizarDetalle(
                                detalleEncontrado,
                                cn
                        );

                if (!actualizado) {
                    cn.rollback();
                    return ResultadoOperacion.error(
                            "No se pudo actualizar el detalle."
                    );
                }

                recalcularTotales(
                        idCompra,
                        compra.getDescuento(),
                        cn
                );

                cn.commit();

                return ResultadoOperacion.ok(
                        "Detalle actualizado correctamente."
                );

            } catch (Exception ex) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Error al actualizar el detalle: "
                        + ex.getMessage()
                );

            } finally {

                try {
                    cn.setAutoCommit(true);
                } catch (SQLException ex) {
                    // No hacemos nada.
                }
            }

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error de conexión: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ELIMINAR PRODUCTO DE LA COMPRA
    // =========================================================
    public ResultadoOperacion eliminarDetalle(
            long idCompra,
            long idDetalle) {

        if (idCompra <= 0
                || idDetalle <= 0) {

            return ResultadoOperacion.error(
                    "La compra o el detalle no son válidos."
            );
        }

        try (Connection cn
                = conexion.getConexion()) {

            cn.setAutoCommit(
                    false
            );

            try {

                Compra compra
                        = compraDao.buscarPorIdParaActualizar(
                                idCompra,
                                cn
                        );

                if (compra == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La compra no existe."
                    );
                }

                if (!compra.estaEnBorrador()) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Solo se pueden eliminar productos "
                            + "de una compra en borrador."
                    );
                }

                boolean pertenece
                        = false;

                for (CompraDetalle detalle
                        : compra.getDetalles()) {

                    if (detalle.getIdDetalle()
                            == idDetalle) {

                        pertenece
                                = true;

                        break;
                    }
                }

                if (!pertenece) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El detalle no pertenece "
                            + "a la compra."
                    );
                }

                boolean eliminado
                        = compraDao.eliminarDetalle(
                                idDetalle,
                                cn
                        );

                if (!eliminado) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No se pudo eliminar el producto."
                    );
                }

                recalcularTotales(
                        idCompra,
                        compra.getDescuento(),
                        cn
                );

                cn.commit();

                return ResultadoOperacion.ok(
                        "Producto eliminado correctamente."
                );

            } catch (Exception ex) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Error al eliminar el producto: "
                        + ex.getMessage()
                );

            } finally {

                try {

                    cn.setAutoCommit(
                            true
                    );

                } catch (SQLException ex) {
                    // No hacemos nada.
                }
            }

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error de conexión: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // APLICAR DESCUENTO
    // =========================================================
    public ResultadoOperacion aplicarDescuento(
            long idCompra,
            BigDecimal descuento) {

        if (idCompra <= 0) {

            return ResultadoOperacion.error(
                    "La compra indicada no es válida."
            );
        }

        if (descuento == null) {

            descuento
                    = BigDecimal.ZERO;
        }

        if (descuento.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            return ResultadoOperacion.error(
                    "El descuento no puede ser negativo."
            );
        }

        descuento
                = descuento.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        try (Connection cn
                = conexion.getConexion()) {

            cn.setAutoCommit(
                    false
            );

            try {

                Compra compra
                        = compraDao.buscarPorIdParaActualizar(
                                idCompra,
                                cn
                        );

                if (compra == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La compra no existe."
                    );
                }

                if (!compra.estaEnBorrador()) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Solo se puede aplicar descuento "
                            + "a una compra en borrador."
                    );
                }

                BigDecimal subtotal
                        = calcularSubtotal(
                                compra.getDetalles()
                        );

                if (descuento.compareTo(
                        subtotal
                ) > 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El descuento no puede ser mayor "
                            + "que el subtotal de la compra."
                    );
                }

                BigDecimal total
                        = subtotal
                                .subtract(
                                        descuento
                                )
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );

                boolean actualizado
                        = compraDao.actualizarTotales(
                                idCompra,
                                subtotal,
                                descuento,
                                total,
                                cn
                        );

                if (!actualizado) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No se pudo aplicar el descuento."
                    );
                }

                cn.commit();

                return ResultadoOperacion.ok(
                        "Descuento aplicado correctamente."
                );

            } catch (Exception ex) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "Error al aplicar el descuento: "
                        + ex.getMessage()
                );

            } finally {

                try {

                    cn.setAutoCommit(
                            true
                    );

                } catch (SQLException ex) {
                    // No hacemos nada.
                }
            }

        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error de conexión: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================
    public Compra buscarPorId(
            long idCompra) {

        if (idCompra <= 0) {
            return null;
        }

        try {

            return compraDao.buscarPorId(
                    idCompra
            );

        } catch (SQLException ex) {

            return null;
        }
    }

    // =========================================================
    // LISTAR TODAS
    // =========================================================
    public List<Compra> listarTodos() {

        try {

            return compraDao.listarTodos();

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }

    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================
    public List<Compra> listarPorEstado(
            String estado) {

        if (estado == null) {

            return Collections.emptyList();
        }

        try {

            return compraDao.listarPorEstado(
                    estado
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }

    // =========================================================
    // LISTAR POR PROVEEDOR
    // =========================================================
    public List<Compra> listarPorProveedor(
            int idProveedor) {

        if (idProveedor <= 0) {

            return Collections.emptyList();
        }

        try {

            return compraDao.listarPorProveedor(
                    idProveedor
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }

    // =========================================================
    // RECALCULAR TOTALES
    // =========================================================
    private void recalcularTotales(
            long idCompra,
            BigDecimal descuento,
            Connection cn)
            throws SQLException {

        List<CompraDetalle> detalles
                = compraDao.listarDetalles(
                        idCompra,
                        cn
                );

        BigDecimal subtotal
                = calcularSubtotal(
                        detalles
                );

        if (descuento == null) {

            descuento
                    = BigDecimal.ZERO;
        }

        descuento
                = descuento.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        // Si al eliminar productos el descuento anterior
        // queda por encima del subtotal, lo dejamos en el
        // máximo permitido para mantener integridad.
        if (descuento.compareTo(
                subtotal
        ) > 0) {

            descuento
                    = subtotal;
        }

        BigDecimal total
                = subtotal
                        .subtract(
                                descuento
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        compraDao.actualizarTotales(
                idCompra,
                subtotal,
                descuento,
                total,
                cn
        );
    }

    // =========================================================
    // CALCULAR SUBTOTAL
    // =========================================================
    private BigDecimal calcularSubtotal(
            List<CompraDetalle> detalles) {

        BigDecimal subtotal
                = BigDecimal.ZERO;

        if (detalles == null) {

            return subtotal.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        for (CompraDetalle detalle
                : detalles) {

            if (detalle.getSubtotal()
                    != null) {

                subtotal
                        = subtotal.add(
                                detalle.getSubtotal()
                        );
            }
        }

        return subtotal.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    // =========================================================
    // VALIDAR DECIMALES
    // =========================================================
    private boolean tieneDecimales(
            BigDecimal cantidad) {

        if (cantidad == null) {

            return false;
        }

        return cantidad
                .stripTrailingZeros()
                .scale() > 0;
    }

    // =========================================================
    // ORIGEN VÁLIDO
    // =========================================================
    private boolean origenValido(
            String origen) {

        return "MANUAL".equals(origen)
                || "CODIGO_BARRAS".equals(origen)
                || "PDF".equals(origen);
    }

    // =========================================================
    // LIMPIAR STRING
    // =========================================================
    private String limpiar(
            String texto) {

        if (texto == null) {

            return null;
        }

        String resultado
                = texto.trim();

        if (resultado.isEmpty()) {

            return null;
        }

        return resultado;
    }
}
