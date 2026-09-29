package util;

import Dao.StockProductoDao;
import Dao.UsuarioDao;

import model.AjusteStock;
import model.AjusteStockDetalle;
import model.StockProducto;
import model.Usuario;

import services.AjusteStockService;
import services.ResultadoOperacion;

import sesion.SesionUsuario;

import java.math.BigDecimal;

public class TestAjusteStockConcurrencia {

    // =========================================================
    // DATOS DE PRUEBA
    // =========================================================

    private static final int ID_PRODUCTO = 1;
    private static final int ID_DEPOSITO = 1;

    /*
     * Stock físico que vamos a declarar en el ajuste.
     *
     * Si actualmente tenemos 67 UN:
     *
     * Sistema    = 67
     * Físico     = 65
     * Diferencia = -2
     */
    private static final BigDecimal STOCK_FISICO_CONTADO =
            new BigDecimal("65");


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "TEST CONCURRENCIA AJUSTE STOCK - SERENA SOFT"
        );

        System.out.println(
                "=================================================="
        );


        try {

            // =================================================
            // DAO Y SERVICE
            // =================================================

            UsuarioDao usuarioDao =
                    new UsuarioDao();

            StockProductoDao stockProductoDao =
                    new StockProductoDao();

            AjusteStockService ajusteStockService =
                    new AjusteStockService();


            // =================================================
            // 1. INICIAR SESIÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "1) INICIANDO SESIÓN..."
            );


            Usuario usuario =
                    usuarioDao.buscarPorUsername(
                            "admin"
                    );


            if (usuario == null) {

                System.out.println(
                        "ERROR: No se encontró el usuario admin."
                );

                return;
            }


            SesionUsuario.iniciarSesion(
                    usuario
            );


            System.out.println(
                    "Usuario: "
                    + usuario.getUsername()
            );


            // =================================================
            // 2. CONSULTAR STOCK ACTUAL
            // =================================================

            System.out.println();
            System.out.println(
                    "2) CONSULTANDO STOCK ACTUAL..."
            );


            StockProducto stockInicial =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            if (stockInicial == null) {

                System.out.println(
                        "ERROR: No existe registro de stock."
                );

                return;
            }


            BigDecimal cantidadInicial =
                    stockInicial.getCantidad();


            System.out.println(
                    "Stock actual: "
                    + cantidadInicial
                    + " UN"
            );


            // =================================================
            // 3. CREAR AJUSTE EN BORRADOR
            // =================================================

            System.out.println();
            System.out.println(
                    "3) CREANDO AJUSTE EN BORRADOR..."
            );


            ResultadoOperacion resultadoCrear =
                    ajusteStockService.crearAjuste(
                            ID_DEPOSITO,
                            "TEST CONCURRENCIA AJUSTE",
                            "Simulación de cambio de stock "
                            + "posterior al conteo"
                    );


            System.out.println(
                    resultadoCrear.getMensaje()
            );


            if (!resultadoCrear.isExitoso()) {

                return;
            }


            // =================================================
            // 4. RECUPERAR EL AJUSTE CREADO
            // =================================================

            AjusteStock ajuste =
                    null;


            for (AjusteStock candidato
                    : ajusteStockService
                            .listarPorEstado(
                                    "BORRADOR"
                            )) {

                if ("TEST CONCURRENCIA AJUSTE".equals(
                        candidato.getMotivo()
                )) {

                    ajuste =
                            candidato;

                    break;
                }
            }


            if (ajuste == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "el ajuste creado."
                );

                return;
            }


            long idAjuste =
                    ajuste.getIdAjuste();


            System.out.println(
                    "Ajuste creado. ID: "
                    + idAjuste
            );


            System.out.println(
                    "Estado: "
                    + ajuste.getEstado()
            );


            // =================================================
            // 5. AGREGAR PRODUCTO
            //
            // El Service capturará automáticamente el stock
            // actual del sistema.
            // =================================================

            System.out.println();
            System.out.println(
                    "4) AGREGANDO PRODUCTO AL AJUSTE..."
            );


            ResultadoOperacion resultadoAgregar =
                    ajusteStockService.agregarProducto(
                            idAjuste,
                            ID_PRODUCTO,
                            STOCK_FISICO_CONTADO
                    );


            System.out.println(
                    resultadoAgregar.getMensaje()
            );


            if (!resultadoAgregar.isExitoso()) {

                return;
            }


            // =================================================
            // 6. CONSULTAR DETALLE CREADO
            // =================================================

            ajuste =
                    ajusteStockService.buscarPorId(
                            idAjuste
                    );


            if (ajuste == null
                    || ajuste.getDetalles() == null
                    || ajuste.getDetalles().isEmpty()) {

                System.out.println(
                        "ERROR: No se encontró el detalle."
                );

                return;
            }


            AjusteStockDetalle detalle =
                    ajuste
                            .getDetalles()
                            .get(0);


            System.out.println();
            System.out.println(
                    "5) DATOS GUARDADOS EN EL AJUSTE:"
            );


            System.out.println(
                    "Producto: "
                    + detalle
                            .getProducto()
                            .getNombre()
            );


            System.out.println(
                    "Stock sistema al contar: "
                    + detalle.getStockSistema()
                    + " UN"
            );


            System.out.println(
                    "Stock físico contado: "
                    + detalle.getStockFisico()
                    + " UN"
            );


            System.out.println(
                    "Diferencia calculada: "
                    + detalle.getDiferencia()
                    + " UN"
            );


            // =================================================
            // 7. VERIFICAR QUE EL STOCK SIGUE IGUAL
            // =================================================

            StockProducto stockAntesMovimiento =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            System.out.println();
            System.out.println(
                    "6) STOCK ANTES DEL MOVIMIENTO SIMULADO:"
            );


            System.out.println(
                    stockAntesMovimiento.getCantidad()
                    + " UN"
            );


            // =================================================
            // 8. SIMULAR OTRO MOVIMIENTO
            //
            // Ejemplo:
            //
            // El ajuste se creó cuando había 67.
            //
            // Pero mientras el usuario estaba contando,
            // otra caja realizó una operación.
            //
            // Simulamos:
            //
            // 67 -> 66
            //
            // IMPORTANTE:
            // Esto representa otro movimiento del sistema.
            // =================================================

            System.out.println();
            System.out.println(
                    "7) SIMULANDO OTRO MOVIMIENTO DE STOCK..."
            );


            BigDecimal stockDespuesMovimiento =
                    cantidadInicial.subtract(
                            BigDecimal.ONE
                    );


            boolean movimientoSimulado =
                    stockProductoDao.actualizarCantidad(
                            stockInicial.getIdStock(),
                            stockDespuesMovimiento
                    );


            if (!movimientoSimulado) {

                System.out.println(
                        "ERROR: No se pudo simular "
                        + "el movimiento externo."
                );

                return;
            }


            System.out.println(
                    "Movimiento simulado correctamente."
            );


            System.out.println(
                    "Stock anterior: "
                    + cantidadInicial
                    + " UN"
            );


            System.out.println(
                    "Stock nuevo: "
                    + stockDespuesMovimiento
                    + " UN"
            );


            // =================================================
            // 9. COMPROBAR CAMBIO REAL
            // =================================================

            StockProducto stockModificado =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            System.out.println();
            System.out.println(
                    "8) STOCK REAL EN BASE DE DATOS:"
            );


            System.out.println(
                    stockModificado.getCantidad()
                    + " UN"
            );


            // =================================================
            // 10. INTENTAR CONFIRMAR EL AJUSTE VIEJO
            //
            // El ajuste recuerda:
            //
            // stock_sistema = cantidadInicial
            //
            // Pero actualmente:
            //
            // stock = cantidadInicial - 1
            //
            // DEBE RECHAZARSE.
            // =================================================

            System.out.println();
            System.out.println(
                    "9) INTENTANDO CONFIRMAR AJUSTE..."
            );


            ResultadoOperacion resultadoConfirmar =
                    ajusteStockService.confirmarAjuste(
                            idAjuste
                    );


            System.out.println(
                    resultadoConfirmar.getMensaje()
            );


            // =================================================
            // NO DEBE HABER SIDO EXITOSO
            // =================================================

            if (resultadoConfirmar.isExitoso()) {

                System.out.println();
                System.out.println(
                        "ERROR GRAVE:"
                );

                System.out.println(
                        "El sistema permitió confirmar "
                        + "un ajuste con stock desactualizado."
                );

                return;
            }


            System.out.println(
                    "OK: Serena Soft detectó "
                    + "que el stock había cambiado."
            );


            // =================================================
            // 11. VERIFICAR QUE EL AJUSTE SIGUE BORRADOR
            // =================================================

            ajuste =
                    ajusteStockService.buscarPorId(
                            idAjuste
                    );


            System.out.println();
            System.out.println(
                    "10) VERIFICANDO ESTADO DEL AJUSTE..."
            );


            System.out.println(
                    "Estado actual: "
                    + ajuste.getEstado()
            );


            if (!"BORRADOR".equals(
                    ajuste.getEstado()
            )) {

                System.out.println(
                        "ERROR: El ajuste cambió de estado."
                );

                return;
            }


            System.out.println(
                    "OK: el ajuste permanece en BORRADOR."
            );


            // =================================================
            // 12. VERIFICAR QUE NO PISÓ EL STOCK
            //
            // Debe seguir en:
            //
            // cantidadInicial - 1
            //
            // NO debe pasar al stock físico contado.
            // =================================================

            StockProducto stockFinal =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            System.out.println();
            System.out.println(
                    "11) VERIFICANDO STOCK FINAL..."
            );


            System.out.println(
                    "Stock que debe conservarse: "
                    + stockDespuesMovimiento
                    + " UN"
            );


            System.out.println(
                    "Stock obtenido: "
                    + stockFinal.getCantidad()
                    + " UN"
            );


            if (stockFinal
                    .getCantidad()
                    .compareTo(
                            stockDespuesMovimiento
                    ) != 0) {

                System.out.println(
                        "ERROR: El ajuste modificó "
                        + "el stock incorrectamente."
                );

                return;
            }


            System.out.println(
                    "OK: el movimiento posterior "
                    + "fue respetado."
            );


            // =================================================
            // 13. RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=================================================="
            );

            System.out.println(
                    "TEST DE CONCURRENCIA FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "=================================================="
            );


            System.out.println();
            System.out.println(
                    "RESULTADO:"
            );


            System.out.println(
                    "El ajuste NO pisó un movimiento "
                    + "de stock posterior al conteo."
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "ERROR GENERAL:"
            );

            ex.printStackTrace();

        } finally {

            // =================================================
            // CERRAR SESIÓN
            // =================================================

            SesionUsuario.cerrarSesion();
        }
    }
}