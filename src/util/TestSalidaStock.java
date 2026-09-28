package util;

import Dao.SalidaStockDao;
import Dao.StockProductoDao;
import Dao.UsuarioDao;

import model.SalidaStock;
import model.StockProducto;
import model.Usuario;

import services.ResultadoOperacion;
import services.SalidaStockService;

import sesion.SesionUsuario;

import java.math.BigDecimal;

public class TestSalidaStock {

    // =========================================================
    // DATOS DE PRUEBA
    // =========================================================

    private static final int ID_PRODUCTO = 1;
    private static final int ID_DEPOSITO = 1;

    private static final BigDecimal CANTIDAD_SALIDA =
            new BigDecimal("5");

    private static final BigDecimal CANTIDAD_INSUFICIENTE =
            new BigDecimal("100");


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "TEST SALIDA DE STOCK - SERENA SOFT"
        );

        System.out.println(
                "========================================"
        );


        try {

            // =================================================
            // DAO Y SERVICE
            // =================================================

            UsuarioDao usuarioDao =
                    new UsuarioDao();

            StockProductoDao stockDao =
                    new StockProductoDao();

            SalidaStockDao salidaDao =
                    new SalidaStockDao();

            SalidaStockService salidaService =
                    new SalidaStockService();


            // =================================================
            // 1. INICIAR SESIÓN DE PRUEBA
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
                        "ERROR: no se encontró "
                        + "el usuario admin."
                );

                return;
            }


            SesionUsuario.iniciarSesion(
                    usuario
            );


            System.out.println(
                    "Usuario: "
                    + SesionUsuario.getNombreUsuario()
            );

            System.out.println(
                    "Rol: "
                    + SesionUsuario.getNombreRol()
            );


            // =================================================
            // 2. STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "2) STOCK INICIAL"
            );

            StockProducto stockInicial =
                    stockDao.buscarPorProductoDeposito(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            if (stockInicial == null) {

                System.out.println(
                        "ERROR: no existe stock "
                        + "para el producto."
                );

                return;
            }


            BigDecimal cantidadInicial =
                    stockInicial.getCantidad();


            System.out.println(
                    "Stock inicial: "
                    + mostrarCantidad(
                            cantidadInicial
                    )
                    + " UN"
            );


            // =================================================
            // 3. CREAR SALIDA BORRADOR
            // =================================================

            System.out.println();
            System.out.println(
                    "3) CREANDO SALIDA BORRADOR..."
            );


            ResultadoOperacion resultadoCrear =
                    salidaService.crearSalida(
                            ID_DEPOSITO,
                            "PRUEBA SALIDA STOCK",
                            "TEST SERENA SOFT",
                            "Prueba automática del módulo "
                            + "de salida de stock"
                    );


            System.out.println(
                    resultadoCrear.getMensaje()
            );


            if (!resultadoCrear.isExitoso()) {

                System.out.println(
                        "TEST DETENIDO."
                );

                return;
            }


            // =================================================
            // 4. OBTENER SALIDA CREADA
            // =================================================

            SalidaStock salida =
                    obtenerUltimaSalidaBorrador(
                            salidaDao,
                            "PRUEBA SALIDA STOCK"
                    );


            if (salida == null) {

                System.out.println(
                        "ERROR: no se pudo recuperar "
                        + "la salida creada."
                );

                return;
            }


            long idSalida =
                    salida.getIdSalida();


            System.out.println(
                    "ID salida creada: "
                    + idSalida
            );


            // =================================================
            // 5. AGREGAR 5 UN
            // =================================================

            System.out.println();
            System.out.println(
                    "4) AGREGANDO PRODUCTO..."
            );


            ResultadoOperacion resultadoAgregar =
                    salidaService.agregarProducto(
                            idSalida,
                            ID_PRODUCTO,
                            CANTIDAD_SALIDA
                    );


            System.out.println(
                    resultadoAgregar.getMensaje()
            );


            if (!resultadoAgregar.isExitoso()) {

                System.out.println(
                        "TEST DETENIDO."
                );

                return;
            }


            // =================================================
            // 6. VERIFICAR BORRADOR
            // =================================================

            System.out.println();
            System.out.println(
                    "5) VERIFICANDO STOCK EN BORRADOR..."
            );


            BigDecimal stockEnBorrador =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock mientras está BORRADOR: "
                    + mostrarCantidad(
                            stockEnBorrador
                    )
                    + " UN"
            );


            if (stockEnBorrador.compareTo(
                    cantidadInicial) == 0) {

                System.out.println(
                        "OK: el BORRADOR no modificó stock."
                );

            } else {

                System.out.println(
                        "ERROR: el BORRADOR modificó stock."
                );

                return;
            }


            // =================================================
            // 7. CONFIRMAR SALIDA
            // =================================================

            System.out.println();
            System.out.println(
                    "6) CONFIRMANDO SALIDA..."
            );


            ResultadoOperacion resultadoConfirmar =
                    salidaService.confirmarSalida(
                            idSalida
                    );


            System.out.println(
                    resultadoConfirmar.getMensaje()
            );


            if (!resultadoConfirmar.isExitoso()) {

                System.out.println(
                        "ERROR: no se pudo confirmar "
                        + "la salida."
                );

                return;
            }


            // =================================================
            // 8. VERIFICAR STOCK FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "7) VERIFICANDO STOCK FINAL..."
            );


            BigDecimal stockFinal =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            BigDecimal stockEsperado =
                    cantidadInicial.subtract(
                            CANTIDAD_SALIDA
                    );


            System.out.println(
                    "Stock esperado: "
                    + mostrarCantidad(
                            stockEsperado
                    )
                    + " UN"
            );


            System.out.println(
                    "Stock obtenido: "
                    + mostrarCantidad(
                            stockFinal
                    )
                    + " UN"
            );


            if (stockFinal.compareTo(
                    stockEsperado) == 0) {

                System.out.println(
                        "OK: stock descontado correctamente."
                );

            } else {

                System.out.println(
                        "ERROR: stock incorrecto."
                );

                return;
            }


            // =================================================
            // 9. VERIFICAR ESTADO CONFIRMADA
            // =================================================

            System.out.println();
            System.out.println(
                    "8) VERIFICANDO ESTADO..."
            );


            SalidaStock salidaConfirmada =
                    salidaDao.buscarPorId(
                            idSalida
                    );


            if (salidaConfirmada == null) {

                System.out.println(
                        "ERROR: no se pudo recuperar "
                        + "la salida confirmada."
                );

                return;
            }


            System.out.println(
                    "Estado: "
                    + salidaConfirmada.getEstado()
            );


            if ("CONFIRMADA".equalsIgnoreCase(
                    salidaConfirmada.getEstado())) {

                System.out.println(
                        "OK: salida CONFIRMADA."
                );

            } else {

                System.out.println(
                        "ERROR: estado incorrecto."
                );

                return;
            }


            // =================================================
            // 10. INTENTAR CONFIRMAR NUEVAMENTE
            // =================================================

            System.out.println();
            System.out.println(
                    "9) PROBANDO DOBLE CONFIRMACIÓN..."
            );


            ResultadoOperacion segundaConfirmacion =
                    salidaService.confirmarSalida(
                            idSalida
                    );


            System.out.println(
                    segundaConfirmacion.getMensaje()
            );


            if (!segundaConfirmacion.isExitoso()) {

                System.out.println(
                        "OK: segunda confirmación rechazada."
                );

            } else {

                System.out.println(
                        "ERROR: permitió confirmar dos veces."
                );

                return;
            }


            // =================================================
            // 11. VERIFICAR QUE NO DESCONTÓ DOS VECES
            // =================================================

            BigDecimal stockDespuesSegundoIntento =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock después del segundo intento: "
                    + mostrarCantidad(
                            stockDespuesSegundoIntento
                    )
                    + " UN"
            );


            if (stockDespuesSegundoIntento.compareTo(
                    stockFinal) == 0) {

                System.out.println(
                        "OK: no se descontó dos veces."
                );

            } else {

                System.out.println(
                        "ERROR: el stock volvió a modificarse."
                );

                return;
            }


            // =================================================
            // 12. CREAR SALIDA CON STOCK INSUFICIENTE
            // =================================================

            System.out.println();
            System.out.println(
                    "10) PROBANDO STOCK INSUFICIENTE..."
            );


            ResultadoOperacion crearInsuficiente =
                    salidaService.crearSalida(
                            ID_DEPOSITO,
                            "PRUEBA STOCK INSUFICIENTE",
                            "TEST SERENA SOFT",
                            "Esta salida debe permanecer "
                            + "en BORRADOR"
                    );


            System.out.println(
                    crearInsuficiente.getMensaje()
            );


            if (!crearInsuficiente.isExitoso()) {

                System.out.println(
                        "ERROR: no se pudo crear "
                        + "la salida de prueba."
                );

                return;
            }


            // =================================================
            // 13. RECUPERAR SEGUNDA SALIDA
            // =================================================

            SalidaStock salidaInsuficiente =
                    obtenerUltimaSalidaBorrador(
                            salidaDao,
                            "PRUEBA STOCK INSUFICIENTE"
                    );


            if (salidaInsuficiente == null) {

                System.out.println(
                        "ERROR: no se encontró "
                        + "la segunda salida."
                );

                return;
            }


            long idSalidaInsuficiente =
                    salidaInsuficiente.getIdSalida();


            System.out.println(
                    "Nueva salida ID: "
                    + idSalidaInsuficiente
            );


            // =================================================
            // 14. AGREGAR 100 UN
            // =================================================

            ResultadoOperacion agregarInsuficiente =
                    salidaService.agregarProducto(
                            idSalidaInsuficiente,
                            ID_PRODUCTO,
                            CANTIDAD_INSUFICIENTE
                    );


            System.out.println(
                    agregarInsuficiente.getMensaje()
            );


            if (!agregarInsuficiente.isExitoso()) {

                System.out.println(
                        "ERROR: el detalle debe poder "
                        + "guardarse en BORRADOR."
                );

                return;
            }


            // =================================================
            // 15. INTENTAR CONFIRMAR
            // =================================================

            System.out.println();
            System.out.println(
                    "Intentando confirmar salida de "
                    + mostrarCantidad(
                            CANTIDAD_INSUFICIENTE
                    )
                    + " UN..."
            );


            ResultadoOperacion confirmarInsuficiente =
                    salidaService.confirmarSalida(
                            idSalidaInsuficiente
                    );


            System.out.println(
                    confirmarInsuficiente.getMensaje()
            );


            if (!confirmarInsuficiente.isExitoso()) {

                System.out.println(
                        "OK: salida rechazada "
                        + "por stock insuficiente."
                );

            } else {

                System.out.println(
                        "ERROR: permitió confirmar "
                        + "con stock insuficiente."
                );

                return;
            }


            // =================================================
            // 16. VERIFICAR STOCK SIN CAMBIOS
            // =================================================

            BigDecimal stockDespuesError =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock después del rechazo: "
                    + mostrarCantidad(
                            stockDespuesError
                    )
                    + " UN"
            );


            if (stockDespuesError.compareTo(
                    stockFinal) == 0) {

                System.out.println(
                        "OK: el stock permaneció intacto."
                );

            } else {

                System.out.println(
                        "ERROR: el stock cambió "
                        + "después del rechazo."
                );

                return;
            }


            // =================================================
            // 17. VERIFICAR QUE SIGA EN BORRADOR
            // =================================================

            SalidaStock salidaRechazada =
                    salidaDao.buscarPorId(
                            idSalidaInsuficiente
                    );


            if (salidaRechazada == null) {

                System.out.println(
                        "ERROR: no se pudo recuperar "
                        + "la salida rechazada."
                );

                return;
            }


            System.out.println(
                    "Estado salida rechazada: "
                    + salidaRechazada.getEstado()
            );


            if ("BORRADOR".equalsIgnoreCase(
                    salidaRechazada.getEstado())) {

                System.out.println(
                        "OK: permanece en BORRADOR."
                );

            } else {

                System.out.println(
                        "ERROR: el estado fue modificado."
                );

                return;
            }


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "TEST SALIDA STOCK FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "========================================"
            );


        } catch (Exception e) {

            System.err.println();
            System.err.println(
                    "ERROR GENERAL DEL TEST:"
            );

            e.printStackTrace();

        } finally {

            // =================================================
            // CERRAR SESIÓN DE PRUEBA
            // =================================================

            SesionUsuario.cerrarSesion();
        }
    }


    // =========================================================
    // OBTENER ÚLTIMA SALIDA BORRADOR POR MOTIVO
    // =========================================================

    private static SalidaStock obtenerUltimaSalidaBorrador(
            SalidaStockDao salidaDao,
            String motivo)
            throws Exception {

        for (SalidaStock salida
                : salidaDao.listarPorEstado(
                        "BORRADOR"
                )) {

            if (motivo.equals(
                    salida.getMotivo())) {

                return salida;
            }
        }

        return null;
    }


    // =========================================================
    // MOSTRAR CANTIDAD
    // =========================================================

    private static String mostrarCantidad(
            BigDecimal cantidad) {

        if (cantidad == null) {

            return "0";
        }

        return cantidad
                .stripTrailingZeros()
                .toPlainString();
    }
}