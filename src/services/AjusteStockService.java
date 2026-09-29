package services;

import Configuracion.conexion;

import Dao.AjusteStockDao;
import Dao.DepositoDao;
import Dao.ProductoDao;
import Dao.StockProductoDao;

import model.AjusteStock;
import model.AjusteStockDetalle;
import model.Deposito;
import model.Producto;
import model.StockProducto;
import model.Usuario;

import sesion.SesionUsuario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collections;
import java.util.List;

public class AjusteStockService {

    // =========================================================
    // DAO
    // =========================================================

    private final AjusteStockDao ajusteStockDao;
    private final DepositoDao depositoDao;
    private final ProductoDao productoDao;
    private final StockProductoDao stockProductoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AjusteStockService() {

        this.ajusteStockDao =
                new AjusteStockDao();

        this.depositoDao =
                new DepositoDao();

        this.productoDao =
                new ProductoDao();

        this.stockProductoDao =
                new StockProductoDao();
    }


    // =========================================================
    // CREAR AJUSTE
    // =========================================================

    public ResultadoOperacion crearAjuste(
            int idDeposito,
            String motivo,
            String observaciones) {

        // -----------------------------------------------------
        // VALIDAR SESIÓN
        // -----------------------------------------------------

        if (!SesionUsuario.haySesion()) {

            return ResultadoOperacion.error(
                    "No hay una sesión iniciada."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DEPÓSITO
        // -----------------------------------------------------

        if (idDeposito <= 0) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un depósito."
            );
        }


        // -----------------------------------------------------
        // LIMPIAR TEXTOS
        // -----------------------------------------------------

        motivo =
                limpiar(motivo);

        observaciones =
                limpiar(observaciones);


        // -----------------------------------------------------
        // VALIDAR MOTIVO
        // -----------------------------------------------------

        if (motivo == null) {

            return ResultadoOperacion.error(
                    "Debe ingresar el motivo del ajuste."
            );
        }


        if (motivo.length() > 120) {

            return ResultadoOperacion.error(
                    "El motivo no puede superar "
                    + "los 120 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR OBSERVACIONES
        // -----------------------------------------------------

        if (observaciones != null
                && observaciones.length() > 500) {

            return ResultadoOperacion.error(
                    "Las observaciones no pueden superar "
                    + "los 500 caracteres."
            );
        }


        try {

            // -------------------------------------------------
            // BUSCAR DEPÓSITO
            // -------------------------------------------------

            Deposito deposito =
                    depositoDao.buscarPorId(
                            idDeposito
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
            // OBTENER USUARIO DE SESIÓN
            // -------------------------------------------------

            Usuario usuario =
                    SesionUsuario.getUsuarioActual();


            if (usuario == null) {

                return ResultadoOperacion.error(
                        "No se pudo obtener el usuario "
                        + "de la sesión."
                );
            }


            // -------------------------------------------------
            // CREAR OBJETO AJUSTE
            // -------------------------------------------------

            AjusteStock ajuste =
                    new AjusteStock();

            ajuste.setDeposito(
                    deposito
            );

            ajuste.setUsuario(
                    usuario
            );

            ajuste.setMotivo(
                    motivo
            );

            ajuste.setObservaciones(
                    observaciones
            );

            ajuste.setEstado(
                    "BORRADOR"
            );


            // -------------------------------------------------
            // GUARDAR
            // -------------------------------------------------

            long idAjuste =
                    ajusteStockDao.guardar(
                            ajuste
                    );


            if (idAjuste <= 0) {

                return ResultadoOperacion.error(
                        "No se pudo crear "
                        + "el ajuste de stock."
                );
            }


            return ResultadoOperacion.ok(
                    "Ajuste de stock creado correctamente. "
                    + "ID: "
                    + idAjuste
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al crear el ajuste: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // AGREGAR PRODUCTO AL AJUSTE
    // =========================================================

    public ResultadoOperacion agregarProducto(
            long idAjuste,
            int idProducto,
            BigDecimal stockFisico) {

        // -----------------------------------------------------
        // VALIDACIONES BÁSICAS
        // -----------------------------------------------------

        if (idAjuste <= 0) {

            return ResultadoOperacion.error(
                    "El ajuste indicado no es válido."
            );
        }


        if (idProducto <= 0) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un producto."
            );
        }


        if (stockFisico == null) {

            return ResultadoOperacion.error(
                    "Debe ingresar el stock físico."
            );
        }


        if (stockFisico.compareTo(
                BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El stock físico no puede ser negativo."
            );
        }


        try {

            // -------------------------------------------------
            // BUSCAR AJUSTE
            // -------------------------------------------------

            AjusteStock ajuste =
                    ajusteStockDao.buscarPorId(
                            idAjuste
                    );


            if (ajuste == null) {

                return ResultadoOperacion.error(
                        "El ajuste de stock no existe."
                );
            }


            // -------------------------------------------------
            // SOLO SE EDITAN BORRADORES
            // -------------------------------------------------

            if (!ajuste.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "Solo se pueden agregar productos "
                        + "a un ajuste en BORRADOR."
                );
            }


            // -------------------------------------------------
            // BUSCAR PRODUCTO COMPLETO
            // -------------------------------------------------

            Producto producto =
                    productoDao.buscarPorId(
                            idProducto
                    );


            if (producto == null) {

                return ResultadoOperacion.error(
                        "El producto seleccionado no existe."
                );
            }


            if (!producto.isActivo()) {

                return ResultadoOperacion.error(
                        "El producto seleccionado está inactivo."
                );
            }


            if (!producto.isControlaStock()) {

                return ResultadoOperacion.error(
                        "El producto "
                        + producto.getNombre()
                        + " no controla stock."
                );
            }


            // -------------------------------------------------
            // VALIDAR CANTIDADES DECIMALES
            // -------------------------------------------------

            if (producto.getUnidadVenta() != null
                    && !producto
                            .getUnidadVenta()
                            .isPermiteDecimales()
                    && tieneDecimales(stockFisico)) {

                return ResultadoOperacion.error(
                        "El producto "
                        + producto.getNombre()
                        + " no permite cantidades decimales."
                );
            }


            // -------------------------------------------------
            // EVITAR PRODUCTOS REPETIDOS
            // -------------------------------------------------

            if (ajuste.getDetalles() != null) {

                for (AjusteStockDetalle detalle
                        : ajuste.getDetalles()) {

                    if (detalle.getProducto() != null
                            && detalle
                                    .getProducto()
                                    .getIdProducto()
                            == idProducto) {

                        return ResultadoOperacion.error(
                                "El producto "
                                + producto.getNombre()
                                + " ya fue agregado al ajuste."
                        );
                    }
                }
            }


            // -------------------------------------------------
            // OBTENER STOCK ACTUAL DEL SISTEMA
            // -------------------------------------------------

            StockProducto stock =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    idProducto,
                                    ajuste
                                            .getDeposito()
                                            .getIdDeposito()
                            );


            if (stock == null) {

                return ResultadoOperacion.error(
                        "No existe un registro de stock para "
                        + producto.getNombre()
                        + " en el depósito seleccionado."
                );
            }


            BigDecimal stockSistema =
                    stock.getCantidad();


            if (stockSistema == null) {

                stockSistema =
                        BigDecimal.ZERO;
            }


            // -------------------------------------------------
            // CALCULAR DIFERENCIA
            //
            // FÍSICO - SISTEMA
            // -------------------------------------------------

            BigDecimal diferencia =
                    stockFisico.subtract(
                            stockSistema
                    );


            // -------------------------------------------------
            // CREAR DETALLE
            // -------------------------------------------------

            AjusteStockDetalle detalle =
                    new AjusteStockDetalle();

            detalle.setIdAjuste(
                    idAjuste
            );

            detalle.setProducto(
                    producto
            );

            detalle.setStockSistema(
                    stockSistema
            );

            detalle.setStockFisico(
                    stockFisico
            );

            detalle.setDiferencia(
                    diferencia
            );


            // -------------------------------------------------
            // GUARDAR DETALLE
            // -------------------------------------------------

            long idDetalle =
                    ajusteStockDao.guardarDetalle(
                            detalle
                    );


            if (idDetalle <= 0) {

                return ResultadoOperacion.error(
                        "No se pudo agregar el producto "
                        + "al ajuste."
                );
            }


            return ResultadoOperacion.ok(
                    "Producto agregado correctamente "
                    + "al ajuste."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al agregar el producto: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ACTUALIZAR STOCK FÍSICO
    // =========================================================

    public ResultadoOperacion actualizarStockFisico(
            long idAjuste,
            long idDetalle,
            BigDecimal nuevoStockFisico) {

        // -----------------------------------------------------
        // VALIDACIONES
        // -----------------------------------------------------

        if (idAjuste <= 0) {

            return ResultadoOperacion.error(
                    "El ajuste indicado no es válido."
            );
        }


        if (idDetalle <= 0) {

            return ResultadoOperacion.error(
                    "El detalle indicado no es válido."
            );
        }


        if (nuevoStockFisico == null) {

            return ResultadoOperacion.error(
                    "Debe ingresar el stock físico."
            );
        }


        if (nuevoStockFisico.compareTo(
                BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El stock físico no puede ser negativo."
            );
        }


        try {

            // -------------------------------------------------
            // BUSCAR AJUSTE
            // -------------------------------------------------

            AjusteStock ajuste =
                    ajusteStockDao.buscarPorId(
                            idAjuste
                    );


            if (ajuste == null) {

                return ResultadoOperacion.error(
                        "El ajuste de stock no existe."
                );
            }


            if (!ajuste.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "Solo se puede modificar "
                        + "un ajuste en BORRADOR."
                );
            }


            // -------------------------------------------------
            // BUSCAR DETALLE
            // -------------------------------------------------

            AjusteStockDetalle encontrado =
                    null;


            for (AjusteStockDetalle detalle
                    : ajuste.getDetalles()) {

                if (detalle.getIdDetalle()
                        == idDetalle) {

                    encontrado =
                            detalle;

                    break;
                }
            }


            if (encontrado == null) {

                return ResultadoOperacion.error(
                        "El detalle indicado no pertenece "
                        + "al ajuste."
                );
            }


            // -------------------------------------------------
            // BUSCAR PRODUCTO COMPLETO
            //
            // El producto mapeado por AjusteStockDao contiene
            // datos básicos. ProductoDao nos devuelve también
            // sus relaciones, incluida UnidadVenta.
            // -------------------------------------------------

            Producto producto =
                    productoDao.buscarPorId(
                            encontrado
                                    .getProducto()
                                    .getIdProducto()
                    );


            if (producto == null) {

                return ResultadoOperacion.error(
                        "No se pudo obtener el producto."
                );
            }


            // -------------------------------------------------
            // VALIDAR DECIMALES
            // -------------------------------------------------

            if (producto.getUnidadVenta() != null
                    && !producto
                            .getUnidadVenta()
                            .isPermiteDecimales()
                    && tieneDecimales(
                            nuevoStockFisico
                    )) {

                return ResultadoOperacion.error(
                        "El producto "
                        + producto.getNombre()
                        + " no permite cantidades decimales."
                );
            }


            // -------------------------------------------------
            // RECALCULAR DIFERENCIA
            //
            // IMPORTANTE:
            // Conservamos stockSistema original.
            // -------------------------------------------------

            encontrado.setStockFisico(
                    nuevoStockFisico
            );


            encontrado.setDiferencia(
                    nuevoStockFisico.subtract(
                            encontrado.getStockSistema()
                    )
            );


            // -------------------------------------------------
            // ACTUALIZAR DETALLE
            // -------------------------------------------------

            boolean actualizado =
                    ajusteStockDao.actualizarDetalle(
                            encontrado
                    );


            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo actualizar "
                        + "el stock físico."
                );
            }


            return ResultadoOperacion.ok(
                    "Stock físico actualizado correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al actualizar el stock físico: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ELIMINAR DETALLE
    // =========================================================

    public ResultadoOperacion eliminarDetalle(
            long idAjuste,
            long idDetalle) {

        if (idAjuste <= 0
                || idDetalle <= 0) {

            return ResultadoOperacion.error(
                    "El ajuste o detalle indicado "
                    + "no es válido."
            );
        }


        try {

            // -------------------------------------------------
            // BUSCAR AJUSTE
            // -------------------------------------------------

            AjusteStock ajuste =
                    ajusteStockDao.buscarPorId(
                            idAjuste
                    );


            if (ajuste == null) {

                return ResultadoOperacion.error(
                        "El ajuste de stock no existe."
                );
            }


            // -------------------------------------------------
            // SOLO BORRADORES
            // -------------------------------------------------

            if (!ajuste.estaEnBorrador()) {

                return ResultadoOperacion.error(
                        "Solo se pueden eliminar productos "
                        + "de un ajuste en BORRADOR."
                );
            }


            // -------------------------------------------------
            // VERIFICAR QUE EL DETALLE PERTENEZCA AL AJUSTE
            // -------------------------------------------------

            boolean pertenece =
                    false;


            for (AjusteStockDetalle detalle
                    : ajuste.getDetalles()) {

                if (detalle.getIdDetalle()
                        == idDetalle) {

                    pertenece =
                            true;

                    break;
                }
            }


            if (!pertenece) {

                return ResultadoOperacion.error(
                        "El detalle indicado no pertenece "
                        + "al ajuste."
                );
            }


            // -------------------------------------------------
            // ELIMINAR
            // -------------------------------------------------

            boolean eliminado =
                    ajusteStockDao.eliminarDetalle(
                            idDetalle
                    );


            if (!eliminado) {

                return ResultadoOperacion.error(
                        "No se pudo eliminar el producto "
                        + "del ajuste."
                );
            }


            return ResultadoOperacion.ok(
                    "Producto eliminado del ajuste "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al eliminar el producto: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // CONFIRMAR AJUSTE
    //
    // ESTA ES LA OPERACIÓN MÁS IMPORTANTE DEL SERVICE.
    //
    // 1. Abre una transacción.
    // 2. Bloquea el ajuste.
    // 3. Verifica BORRADOR.
    // 4. Bloquea cada stock.
    // 5. Comprueba que no haya cambiado desde el conteo.
    // 6. Actualiza stock al valor físico.
    // 7. Cambia BORRADOR -> CONFIRMADO.
    // 8. COMMIT.
    //
    // Si cualquier paso falla:
    // ROLLBACK.
    // =========================================================

    public ResultadoOperacion confirmarAjuste(
            long idAjuste) {

        // -----------------------------------------------------
        // VALIDACIONES INICIALES
        // -----------------------------------------------------

        if (idAjuste <= 0) {

            return ResultadoOperacion.error(
                    "El ajuste indicado no es válido."
            );
        }


        if (!SesionUsuario.haySesion()) {

            return ResultadoOperacion.error(
                    "No hay una sesión iniciada."
            );
        }


        Connection cn =
                null;


        try {

            // =================================================
            // INICIAR TRANSACCIÓN
            // =================================================

            cn =
                    conexion.getConexion();

            cn.setAutoCommit(false);


            // =================================================
            // BLOQUEAR AJUSTE
            //
            // SELECT ... FOR UPDATE
            // =================================================

            AjusteStock ajuste =
                    ajusteStockDao
                            .buscarPorIdParaActualizar(
                                    idAjuste,
                                    cn
                            );


            if (ajuste == null) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "El ajuste de stock no existe."
                );
            }


            // =================================================
            // VALIDAR ESTADO
            // =================================================

            if (!ajuste.estaEnBorrador()) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "El ajuste no puede confirmarse "
                        + "porque su estado actual es "
                        + ajuste.getEstado()
                        + "."
                );
            }


            // =================================================
            // VALIDAR DETALLES
            // =================================================

            if (ajuste.getDetalles() == null
                    || ajuste
                            .getDetalles()
                            .isEmpty()) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "El ajuste no contiene productos."
                );
            }


            // =================================================
            // PRIMERA PASADA
            //
            // BLOQUEAMOS Y VALIDAMOS TODO ANTES DE MODIFICAR.
            // =================================================

            for (AjusteStockDetalle detalle
                    : ajuste.getDetalles()) {


                // ---------------------------------------------
                // VALIDAR PRODUCTO
                // ---------------------------------------------

                if (detalle.getProducto() == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Existe un detalle sin producto."
                    );
                }


                // ---------------------------------------------
                // VALIDAR CANTIDADES
                // ---------------------------------------------

                if (detalle.getStockSistema() == null
                        || detalle.getStockFisico() == null
                        || detalle.getDiferencia() == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "Existe un detalle con "
                            + "cantidades inválidas."
                    );
                }


                if (detalle
                        .getStockFisico()
                        .compareTo(
                                BigDecimal.ZERO
                        ) < 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El stock físico no puede "
                            + "ser negativo."
                    );
                }


                // ---------------------------------------------
                // VALIDAR QUE DIFERENCIA SEA CORRECTA
                //
                // Esto evita confiar ciegamente en lo
                // almacenado en la tabla.
                // ---------------------------------------------

                BigDecimal diferenciaEsperada =
                        detalle
                                .getStockFisico()
                                .subtract(
                                        detalle
                                                .getStockSistema()
                                );


                if (diferenciaEsperada.compareTo(
                        detalle.getDiferencia()
                ) != 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "La diferencia de stock del producto "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + " no es válida."
                    );
                }


                // ---------------------------------------------
                // BLOQUEAR STOCK
                //
                // SELECT ... FOR UPDATE
                // ---------------------------------------------

                StockProducto stockActual =
                        stockProductoDao
                                .buscarParaActualizar(
                                        cn,
                                        detalle
                                                .getProducto()
                                                .getIdProducto(),
                                        ajuste
                                                .getDeposito()
                                                .getIdDeposito()
                                );


                if (stockActual == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No existe stock para el producto "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + " en el depósito."
                    );
                }


                BigDecimal cantidadActual =
                        stockActual.getCantidad();


                if (cantidadActual == null) {

                    cantidadActual =
                            BigDecimal.ZERO;
                }


                // ---------------------------------------------
                // CONTROL DE CONCURRENCIA
                //
                // El stock actual debe coincidir con el stock
                // que había cuando hicimos el conteo.
                // ---------------------------------------------

                if (cantidadActual.compareTo(
                        detalle.getStockSistema()
                ) != 0) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "El stock del producto "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + " cambió desde que se realizó "
                            + "el conteo. "
                            + "Stock al contar: "
                            + detalle.getStockSistema()
                            + " | Stock actual: "
                            + cantidadActual
                            + ". Revise el ajuste antes "
                            + "de confirmarlo."
                    );
                }
            }


            // =================================================
            // SEGUNDA PASADA
            //
            // TODOS LOS PRODUCTOS YA FUERON VALIDADOS.
            //
            // AHORA ACTUALIZAMOS EL STOCK.
            // =================================================

            for (AjusteStockDetalle detalle
                    : ajuste.getDetalles()) {


                StockProducto stock =
                        stockProductoDao
                                .buscarParaActualizar(
                                        cn,
                                        detalle
                                                .getProducto()
                                                .getIdProducto(),
                                        ajuste
                                                .getDeposito()
                                                .getIdDeposito()
                                );


                if (stock == null) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No se pudo obtener el stock "
                            + "del producto "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + "."
                    );
                }


                // ---------------------------------------------
                // AJUSTE:
                //
                // NO hacemos:
                //
                // cantidad = cantidad + diferencia
                //
                // Dejamos directamente:
                //
                // cantidad = stockFisico
                // ---------------------------------------------

                boolean actualizado =
                        stockProductoDao
                                .actualizarCantidad(
                                        cn,
                                        stock.getIdStock(),
                                        detalle
                                                .getStockFisico()
                                );


                if (!actualizado) {

                    cn.rollback();

                    return ResultadoOperacion.error(
                            "No se pudo actualizar el stock "
                            + "del producto "
                            + detalle
                                    .getProducto()
                                    .getNombre()
                            + "."
                    );
                }
            }


            // =================================================
            // CAMBIAR ESTADO
            //
            // BORRADOR -> CONFIRMADO
            // =================================================

            boolean estadoActualizado =
                    ajusteStockDao
                            .cambiarEstadoSiCoincide(
                                    idAjuste,
                                    "BORRADOR",
                                    "CONFIRMADO",
                                    cn
                            );


            if (!estadoActualizado) {

                cn.rollback();

                return ResultadoOperacion.error(
                        "No se pudo confirmar el ajuste. "
                        + "El estado pudo haber cambiado."
                );
            }


            // =================================================
            // TODO SALIÓ BIEN
            //
            // COMMIT
            // =================================================

            cn.commit();


            return ResultadoOperacion.ok(
                    "Ajuste de stock confirmado "
                    + "correctamente."
            );


        } catch (SQLException ex) {

            // =================================================
            // ERROR
            //
            // ROLLBACK
            // =================================================

            if (cn != null) {

                try {

                    cn.rollback();

                } catch (SQLException ignored) {
                }
            }


            return ResultadoOperacion.error(
                    "Error al confirmar el ajuste: "
                    + ex.getMessage()
            );


        } finally {

            // =================================================
            // RESTAURAR AUTOCOMMIT Y CERRAR
            // =================================================

            if (cn != null) {

                try {

                    cn.setAutoCommit(true);

                } catch (SQLException ignored) {
                }


                try {

                    cn.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public AjusteStock buscarPorId(
            long idAjuste) {

        try {

            return ajusteStockDao.buscarPorId(
                    idAjuste
            );

        } catch (SQLException ex) {

            return null;
        }
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<AjusteStock> listarTodos() {

        try {

            return ajusteStockDao.listarTodos();

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================

    public List<AjusteStock> listarPorEstado(
            String estado) {

        try {

            return ajusteStockDao.listarPorEstado(
                    estado
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // LISTAR POR DEPÓSITO
    // =========================================================

    public List<AjusteStock> listarPorDeposito(
            int idDeposito) {

        try {

            return ajusteStockDao.listarPorDeposito(
                    idDeposito
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // MÉTODO AUXILIAR
    // ¿LA CANTIDAD TIENE DECIMALES REALES?
    //
    // 10.000 -> false
    // 10.500 -> true
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
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(
            String texto) {

        if (texto == null) {

            return null;
        }


        String resultado =
                texto.trim();


        if (resultado.isEmpty()) {

            return null;
        }


        return resultado;
    }
}