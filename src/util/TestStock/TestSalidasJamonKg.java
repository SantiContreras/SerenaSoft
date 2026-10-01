/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.TestStock;


import Dao.StockProductoDao;

import model.Producto;
import model.SalidaStock;

import services.LoginService;
import services.ProductoService;
import services.ResultadoLogin;
import services.ResultadoOperacion;
import services.SalidaStockService;

import java.math.BigDecimal;

public class TestSalidasJamonKg {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST 10 SALIDAS JAMÓN CRUDO - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );


        // =====================================================
        // DATOS
        // =====================================================

        final String CODIGO_PRODUCTO =
                "JAM-CRUDO-001";

        final int ID_DEPOSITO = 1;

        final BigDecimal STOCK_INICIAL_ESPERADO =
                new BigDecimal("25.000");

        final BigDecimal STOCK_FINAL_ESPERADO =
                new BigDecimal("14.100");


        /*
         * Simulamos 10 ventas por peso.
         *
         * TOTAL:
         * 10.900 KG
         */
        BigDecimal[] ventas = {

            new BigDecimal("0.250"),
            new BigDecimal("0.475"),
            new BigDecimal("1.200"),
            new BigDecimal("0.835"),
            new BigDecimal("2.150"),
            new BigDecimal("0.325"),
            new BigDecimal("1.750"),
            new BigDecimal("0.600"),
            new BigDecimal("2.400"),
            new BigDecimal("0.915")
        };


        // =====================================================
        // SERVICIOS / DAO
        // =====================================================

        LoginService loginService =
                new LoginService();

        ProductoService productoService =
                new ProductoService();

        SalidaStockService salidaService =
                new SalidaStockService();

        StockProductoDao stockDao =
                new StockProductoDao();


        try {

            // =================================================
            // 1. LOGIN
            // =================================================

            System.out.println();
            System.out.println(
                    "1) INICIANDO SESIÓN..."
            );


            ResultadoLogin login =
                    loginService.login(
                            "juan",
                            "Caja1234"
                    );


            if (!login.isCorrecto()) {

                System.out.println(
                        "ERROR LOGIN: "
                        + login.getMensaje()
                );

                return;
            }


            System.out.println(
                    "Usuario: "
                    + login.getUsuario()
                            .getNombreCompleto()
            );


            // =================================================
            // 2. BUSCAR JAMÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "2) BUSCANDO JAMÓN CRUDO..."
            );


            Producto jamon =
                    productoService.buscarPorCodigo(
                            CODIGO_PRODUCTO
                    );


            if (jamon == null) {

                System.out.println(
                        "ERROR: No existe Jamón Crudo."
                );

                return;
            }


            System.out.println(
                    "Producto: "
                    + jamon.getNombre()
            );

            System.out.println(
                    "ID: "
                    + jamon.getIdProducto()
            );

            System.out.println(
                    "Unidad venta: "
                    + jamon.getUnidadVenta()
            );


            // =================================================
            // 3. VERIFICAR QUE KG ADMITE DECIMALES
            // =================================================

            if (jamon.getUnidadVenta() == null
                    || !jamon.getUnidadVenta()
                            .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: Jamón Crudo debe venderse "
                        + "en una unidad decimal."
                );

                return;
            }


            System.out.println(
                    "OK: La unidad permite decimales."
            );


            // =================================================
            // 4. STOCK INICIAL
            // =================================================

            BigDecimal stockInicial =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println();
            System.out.println(
                    "3) STOCK INICIAL: "
                    + stockInicial
                    + " KG"
            );


            if (stockInicial.compareTo(
                    STOCK_INICIAL_ESPERADO
            ) != 0) {

                System.out.println(
                        "ERROR: Para ejecutar esta prueba "
                        + "esperábamos 25.000 KG."
                );

                System.out.println(
                        "Stock encontrado: "
                        + stockInicial
                        + " KG"
                );

                return;
            }


            // =================================================
            // 5. CALCULAR TOTAL A RETIRAR
            // =================================================

            BigDecimal totalSalidas =
                    BigDecimal.ZERO;


            for (BigDecimal cantidad : ventas) {

                totalSalidas =
                        totalSalidas.add(
                                cantidad
                        );
            }


            System.out.println(
                    "Total de las 10 operaciones: "
                    + totalSalidas
                    + " KG"
            );


            if (totalSalidas.compareTo(
                    new BigDecimal("10.900")
            ) != 0) {

                System.out.println(
                        "ERROR: Las cantidades del test "
                        + "no suman 10.900 KG."
                );

                return;
            }


            // =================================================
            // 6. EJECUTAR LAS 10 SALIDAS
            // =================================================

            System.out.println();
            System.out.println(
                    "4) SIMULANDO 10 OPERACIONES..."
            );


            BigDecimal stockEsperado =
                    stockInicial;


            for (int i = 0;
                    i < ventas.length;
                    i++) {


                BigDecimal cantidad =
                        ventas[i];


                int numeroOperacion =
                        i + 1;


                System.out.println();
                System.out.println(
                        "----------------------------------------------"
                );

                System.out.println(
                        "OPERACIÓN "
                        + numeroOperacion
                );

                System.out.println(
                        "Cantidad: "
                        + cantidad
                        + " KG"
                );


                // =============================================
                // 6.1 CREAR SALIDA
                // =============================================

                ResultadoOperacion crear =
                        salidaService.crearSalida(

                                ID_DEPOSITO,

                                "SIMULACIÓN VENTA JAMÓN",

                                "Mostrador",

                                "Operación de prueba N° "
                                + numeroOperacion
                        );


                System.out.println(
                        "Crear: "
                        + crear.getMensaje()
                );


                if (!crear.isExitoso()) {

                    System.out.println(
                            "ERROR creando salida."
                    );

                    return;
                }


                // =============================================
                // 6.2 OBTENER ÚLTIMA SALIDA CREADA
                //
                // crearSalida() devuelve el ID dentro
                // del mensaje, pero no el objeto.
                //
                // Por eso obtenemos la última salida
                // de la lista.
                // =============================================

                SalidaStock salidaCreada =
                        null;


                for (SalidaStock salida
                        : salidaService.listarTodas()) {

                    if (salidaCreada == null
                            || salida.getIdSalida()
                            > salidaCreada.getIdSalida()) {

                        salidaCreada =
                                salida;
                    }
                }


                if (salidaCreada == null) {

                    System.out.println(
                            "ERROR: No se pudo recuperar "
                            + "la salida creada."
                    );

                    return;
                }


                long idSalida =
                        salidaCreada.getIdSalida();


                System.out.println(
                        "Salida #: "
                        + idSalida
                );


                // =============================================
                // 6.3 AGREGAR JAMÓN
                // =============================================

                ResultadoOperacion agregar =
                        salidaService.agregarProducto(

                                idSalida,

                                jamon.getIdProducto(),

                                cantidad
                        );


                System.out.println(
                        "Agregar: "
                        + agregar.getMensaje()
                );


                if (!agregar.isExitoso()) {

                    System.out.println(
                            "ERROR agregando producto."
                    );

                    return;
                }


                // =============================================
                // 6.4 BORRADOR NO DEBE MODIFICAR STOCK
                // =============================================

                BigDecimal stockAntesConfirmar =
                        stockDao.obtenerCantidad(

                                jamon.getIdProducto(),

                                ID_DEPOSITO
                        );


                if (stockAntesConfirmar.compareTo(
                        stockEsperado
                ) != 0) {

                    System.out.println(
                            "ERROR: BORRADOR modificó stock."
                    );

                    return;
                }


                // =============================================
                // 6.5 CONFIRMAR
                // =============================================

                ResultadoOperacion confirmar =
                        salidaService.confirmarSalida(
                                idSalida
                        );


                System.out.println(
                        "Confirmar: "
                        + confirmar.getMensaje()
                );


                if (!confirmar.isExitoso()) {

                    System.out.println(
                            "ERROR confirmando salida."
                    );

                    return;
                }


                // =============================================
                // 6.6 CALCULAR STOCK ESPERADO
                // =============================================

                stockEsperado =
                        stockEsperado.subtract(
                                cantidad
                        );


                // =============================================
                // 6.7 LEER STOCK REAL
                // =============================================

                BigDecimal stockReal =
                        stockDao.obtenerCantidad(

                                jamon.getIdProducto(),

                                ID_DEPOSITO
                        );


                System.out.println(
                        "Stock esperado: "
                        + stockEsperado
                        + " KG"
                );

                System.out.println(
                        "Stock real:     "
                        + stockReal
                        + " KG"
                );


                if (stockReal.compareTo(
                        stockEsperado
                ) != 0) {

                    System.out.println(
                            "ERROR DE PRECISIÓN "
                            + "EN OPERACIÓN "
                            + numeroOperacion
                    );

                    return;
                }


                System.out.println(
                        "OK operación "
                        + numeroOperacion
                );
            }


            // =================================================
            // 7. VERIFICACIÓN FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "5) RESULTADO DE LAS 10 OPERACIONES"
            );

            System.out.println(
                    "=============================================="
            );


            BigDecimal stockFinal =
                    stockDao.obtenerCantidad(

                            jamon.getIdProducto(),

                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock inicial:     "
                    + stockInicial
                    + " KG"
            );

            System.out.println(
                    "Total retirado:   -"
                    + totalSalidas
                    + " KG"
            );

            System.out.println(
                    "Stock esperado:    "
                    + STOCK_FINAL_ESPERADO
                    + " KG"
            );

            System.out.println(
                    "Stock obtenido:    "
                    + stockFinal
                    + " KG"
            );


            if (stockFinal.compareTo(
                    STOCK_FINAL_ESPERADO
            ) != 0) {

                System.out.println();
                System.out.println(
                        "ERROR: STOCK FINAL INCORRECTO."
                );

                return;
            }


            // =================================================
            // 8. PROBAR STOCK INSUFICIENTE
            // =================================================

            System.out.println();
            System.out.println(
                    "6) PROBANDO RETIRO DE 15.000 KG..."
            );


            ResultadoOperacion crearExceso =
                    salidaService.crearSalida(

                            ID_DEPOSITO,

                            "PRUEBA STOCK INSUFICIENTE",

                            "Mostrador",

                            "Debe ser rechazada"
                    );


            if (!crearExceso.isExitoso()) {

                System.out.println(
                        crearExceso.getMensaje()
                );

                return;
            }


            // =================================================
            // RECUPERAR ÚLTIMA SALIDA
            // =================================================

            SalidaStock salidaExceso =
                    null;


            for (SalidaStock salida
                    : salidaService.listarTodas()) {

                if (salidaExceso == null
                        || salida.getIdSalida()
                        > salidaExceso.getIdSalida()) {

                    salidaExceso =
                            salida;
                }
            }


            if (salidaExceso == null) {

                System.out.println(
                        "ERROR recuperando salida."
                );

                return;
            }


            long idSalidaExceso =
                    salidaExceso.getIdSalida();


            ResultadoOperacion agregarExceso =
                    salidaService.agregarProducto(

                            idSalidaExceso,

                            jamon.getIdProducto(),

                            new BigDecimal("15.000")
                    );


            if (!agregarExceso.isExitoso()) {

                System.out.println(
                        agregarExceso.getMensaje()
                );

                return;
            }


            // =================================================
            // INTENTAR CONFIRMAR
            // =================================================

            ResultadoOperacion confirmarExceso =
                    salidaService.confirmarSalida(
                            idSalidaExceso
                    );


            System.out.println(
                    confirmarExceso.getMensaje()
            );


            if (confirmarExceso.isExitoso()) {

                System.out.println(
                        "ERROR CRÍTICO:"
                );

                System.out.println(
                        "El sistema permitió retirar "
                        + "15 KG teniendo solamente "
                        + stockFinal
                        + " KG."
                );

                return;
            }


            System.out.println(
                    "OK: Stock insuficiente rechazado."
            );


            // =================================================
            // 9. COMPROBAR QUE EL RECHAZO NO ALTERÓ STOCK
            // =================================================

            BigDecimal stockDespuesRechazo =
                    stockDao.obtenerCantidad(

                            jamon.getIdProducto(),

                            ID_DEPOSITO
                    );


            System.out.println();
            System.out.println(
                    "Stock antes del intento: "
                    + stockFinal
                    + " KG"
            );

            System.out.println(
                    "Stock después del intento: "
                    + stockDespuesRechazo
                    + " KG"
            );


            if (stockDespuesRechazo.compareTo(
                    STOCK_FINAL_ESPERADO
            ) != 0) {

                System.out.println(
                        "ERROR CRÍTICO: El rechazo "
                        + "modificó el stock."
                );

                return;
            }


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "25.000 - 10.900 = 14.100 KG"
            );

            System.out.println(
                    "Intento de 15.000 KG rechazado"
            );

            System.out.println(
                    "Stock final = 14.100 KG"
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