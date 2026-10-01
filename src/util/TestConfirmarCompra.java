package util;

import Dao.StockProductoDao;

import model.Compra;

import services.CompraService;
import services.LoginService;
import services.ResultadoLogin;
import services.ResultadoOperacion;

import java.math.BigDecimal;

public class TestConfirmarCompra {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "TEST CONFIRMAR COMPRA - SERENA SOFT"
        );

        System.out.println(
                "=============================================="
        );


        // =====================================================
        // SERVICIOS / DAO
        // =====================================================

        CompraService compraService =
                new CompraService();

        StockProductoDao stockProductoDao =
                new StockProductoDao();


        // =====================================================
        // DATOS DE PRUEBA
        // =====================================================

        long idCompra = 1;

        int idProducto = 1;

        int idDeposito = 1;


        try {

            // =================================================
            // 1. INICIAR SESIÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "1) INICIANDO SESIÓN..."
            );


            LoginService loginService =
                    new LoginService();


            ResultadoLogin resultadoLogin =
                    loginService.login(
                            "juan",
                            "Caja1234"
                    );


            if (!resultadoLogin.isCorrecto()) {

                System.out.println(
                        "ERROR DE LOGIN: "
                        + resultadoLogin.getMensaje()
                );

                return;
            }


            System.out.println(
                    "Sesión iniciada correctamente."
            );

            System.out.println(
                    "Usuario: "
                    + resultadoLogin
                            .getUsuario()
                            .getNombreCompleto()
            );


            // =================================================
            // 2. CONSULTAR COMPRA ANTES
            // =================================================

            System.out.println();
            System.out.println(
                    "2) CONSULTANDO COMPRA ANTES..."
            );


            Compra compraAntes =
                    compraService.buscarPorId(
                            idCompra
                    );


            if (compraAntes == null) {

                System.out.println(
                        "ERROR: No existe la Compra #"
                        + idCompra
                );

                return;
            }


            System.out.println(
                    "Compra #: "
                    + compraAntes.getIdCompra()
            );

            System.out.println(
                    "Estado: "
                    + compraAntes.getEstado()
            );

            System.out.println(
                    "Total: $"
                    + compraAntes.getTotal()
            );

            System.out.println(
                    "Detalles: "
                    + compraAntes.cantidadDetalles()
            );


            if (!compraAntes.estaEnBorrador()) {

                System.out.println();
                System.out.println(
                        "ERROR: La Compra #"
                        + idCompra
                        + " ya no está en BORRADOR."
                );

                System.out.println(
                        "Este test necesita una compra "
                        + "BORRADOR."
                );

                return;
            }


            // =================================================
            // 3. CONSULTAR STOCK ANTES
            // =================================================

            System.out.println();
            System.out.println(
                    "3) CONSULTANDO STOCK ANTES..."
            );


            BigDecimal stockAntes =
                    stockProductoDao.obtenerCantidad(
                            idProducto,
                            idDeposito
                    );


            System.out.println(
                    "Stock antes: "
                    + stockAntes
            );


            // =================================================
            // 4. CONFIRMAR COMPRA
            // =================================================

            System.out.println();
            System.out.println(
                    "4) CONFIRMANDO COMPRA..."
            );


            ResultadoOperacion resultadoConfirmar =
                    compraService.confirmarCompra(
                            idCompra
                    );


            System.out.println(
                    resultadoConfirmar.getMensaje()
            );


            if (!resultadoConfirmar.isExitoso()) {

                System.out.println();
                System.out.println(
                        "ERROR: No se pudo confirmar "
                        + "la compra."
                );

                return;
            }


            // =================================================
            // 5. CONSULTAR COMPRA DESPUÉS
            // =================================================

            System.out.println();
            System.out.println(
                    "5) CONSULTANDO COMPRA DESPUÉS..."
            );


            Compra compraDespues =
                    compraService.buscarPorId(
                            idCompra
                    );


            if (compraDespues == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "la compra."
                );

                return;
            }


            System.out.println(
                    "Estado después: "
                    + compraDespues.getEstado()
            );


            if (!compraDespues.estaConfirmada()) {

                System.out.println(
                        "ERROR: La compra debería estar "
                        + "CONFIRMADA."
                );

                return;
            }


            System.out.println(
                    "OK: Compra en estado CONFIRMADA."
            );


            // =================================================
            // 6. CONSULTAR STOCK DESPUÉS
            // =================================================

            System.out.println();
            System.out.println(
                    "6) CONSULTANDO STOCK DESPUÉS..."
            );


            BigDecimal stockDespues =
                    stockProductoDao.obtenerCantidad(
                            idProducto,
                            idDeposito
                    );


            System.out.println(
                    "Stock antes:   "
                    + stockAntes
            );

            System.out.println(
                    "Stock después: "
                    + stockDespues
            );


            // =================================================
            // 7. CALCULAR STOCK ESPERADO
            //
            // Compra #1:
            // 10 unidades
            // factor = 1
            //
            // 66 + 10 = 76
            // =================================================

            BigDecimal cantidadEsperada =
                    new BigDecimal(
                            "10.000"
                    );


            BigDecimal stockEsperado =
                    stockAntes.add(
                            cantidadEsperada
                    );


            if (stockDespues.compareTo(
                    stockEsperado
            ) != 0) {

                System.out.println();
                System.out.println(
                        "ERROR DE STOCK."
                );

                System.out.println(
                        "Esperado: "
                        + stockEsperado
                );

                System.out.println(
                        "Obtenido: "
                        + stockDespues
                );

                return;
            }


            System.out.println(
                    "OK: Stock actualizado correctamente."
            );


            // =================================================
            // 8. INTENTAR CONFIRMAR NUEVAMENTE
            // =================================================

            System.out.println();
            System.out.println(
                    "7) INTENTANDO SEGUNDA CONFIRMACIÓN..."
            );


            ResultadoOperacion segundaConfirmacion =
                    compraService.confirmarCompra(
                            idCompra
                    );


            System.out.println(
                    segundaConfirmacion.getMensaje()
            );


            if (segundaConfirmacion.isExitoso()) {

                System.out.println(
                        "ERROR: La segunda confirmación "
                        + "debería haber sido rechazada."
                );

                return;
            }


            System.out.println(
                    "OK: Segunda confirmación rechazada."
            );


            // =================================================
            // 9. VERIFICAR QUE EL STOCK NO VOLVIÓ A CAMBIAR
            // =================================================

            System.out.println();
            System.out.println(
                    "8) VERIFICANDO STOCK FINAL..."
            );


            BigDecimal stockFinal =
                    stockProductoDao.obtenerCantidad(
                            idProducto,
                            idDeposito
                    );


            System.out.println(
                    "Stock después de confirmar: "
                    + stockDespues
            );

            System.out.println(
                    "Stock después del segundo intento: "
                    + stockFinal
            );


            if (stockFinal.compareTo(
                    stockDespues
            ) != 0) {

                System.out.println();
                System.out.println(
                        "ERROR CRÍTICO: "
                        + "La segunda confirmación "
                        + "modificó nuevamente el stock."
                );

                return;
            }


            System.out.println(
                    "OK: El stock no se duplicó."
            );


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST CONFIRMAR COMPRA FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "=============================================="
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "ERROR DURANTE EL TEST:"
            );

            System.out.println(
                    ex.getMessage()
            );

            ex.printStackTrace();
        }
    }
}