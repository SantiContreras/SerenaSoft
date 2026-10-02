package util.TestStock;

import Dao.StockProductoDao;

import java.math.BigDecimal;

import model.Producto;
import model.SalidaStock;

import services.LoginService;
import services.ProductoService;
import services.ResultadoLogin;
import services.ResultadoOperacion;
import services.SalidaStockService;

public class TestSalidaStockFinal {

    // =========================================================
    // CONFIGURACIÓN DEL TEST
    // =========================================================

    private static final int ID_DEPOSITO = 1;

    private static final String CODIGO_AGUA =
            "AGU-0001";

    private static final String CODIGO_JAMON =
            "JAM-CRUDO-001";


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println(
                "======================================================"
        );
        System.out.println(
                "TEST FINAL SALIDA STOCK - SERENA SOFT"
        );
        System.out.println(
                "======================================================"
        );


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

                error(
                        "No se pudo iniciar sesión: "
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
            // 2. BUSCAR PRODUCTOS
            // =================================================

            System.out.println();
            System.out.println(
                    "2) BUSCANDO PRODUCTOS..."
            );


            Producto agua =
                    productoService.buscarPorCodigo(
                            CODIGO_AGUA
                    );


            Producto jamon =
                    productoService.buscarPorCodigo(
                            CODIGO_JAMON
                    );


            if (agua == null) {

                error(
                        "No existe el producto "
                        + CODIGO_AGUA
                );

                return;
            }


            if (jamon == null) {

                error(
                        "No existe el producto "
                        + CODIGO_JAMON
                );

                return;
            }


            System.out.println(
                    "Agua: "
                    + agua.getNombre()
                    + " | Unidad venta: "
                    + agua.getUnidadVenta().getCodigo()
            );


            System.out.println(
                    "Jamón: "
                    + jamon.getNombre()
                    + " | Unidad venta: "
                    + jamon.getUnidadVenta().getCodigo()
            );


            // =================================================
            // 3. LEER STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "3) STOCK INICIAL..."
            );


            BigDecimal stockAguaInicial =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal stockJamonInicial =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Agua: "
                    + stockAguaInicial
                    + " UN"
            );


            System.out.println(
                    "Jamón: "
                    + stockJamonInicial
                    + " KG"
            );


            // Necesitamos al menos 10 UN de agua
            // para realizar todas las pruebas.

            if (stockAguaInicial.compareTo(
                    new BigDecimal("10.000")
            ) < 0) {

                error(
                        "El test necesita al menos "
                        + "10 UN de Agua."
                );

                return;
            }


            if (stockJamonInicial.compareTo(
                    new BigDecimal("1.000")
            ) < 0) {

                error(
                        "El test necesita al menos "
                        + "1 KG de Jamón."
                );

                return;
            }


            // =================================================
            // PRUEBA A
            // UN NO ADMITE DECIMALES
            // =================================================

            System.out.println();
            System.out.println(
                    "======================================================"
            );
            System.out.println(
                    "PRUEBA A - UN NO ADMITE DECIMALES"
            );
            System.out.println(
                    "======================================================"
            );


            long idSalidaA =
                    crearSalidaYObtenerId(
                            salidaService,
                            "PRUEBA",
                            "TEST FINAL",
                            "Prueba decimal sobre unidad UN"
                    );


            if (idSalidaA <= 0) {
                return;
            }


            System.out.println(
                    "Salida creada: #"
                    + idSalidaA
            );


            ResultadoOperacion decimal =
                    salidaService.agregarProducto(
                            idSalidaA,
                            agua.getIdProducto(),
                            new BigDecimal("2.500")
                    );


            System.out.println(
                    "Resultado: "
                    + decimal.getMensaje()
            );


            if (decimal.isExitoso()) {

                error(
                        "El sistema permitió retirar "
                        + "2.500 UN."
                );

                return;
            }


            System.out.println(
                    "OK: 2.500 UN fueron rechazadas."
            );


            // Verificamos que el intento inválido
            // no haya tocado stock.

            BigDecimal stockAguaDespuesDecimal =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Stock Agua después del intento decimal",
                    stockAguaInicial,
                    stockAguaDespuesDecimal
            );


            // =================================================
            // PRUEBA B
            // SALIDA VÁLIDA + DOBLE CONFIRMACIÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "======================================================"
            );
            System.out.println(
                    "PRUEBA B - SALIDA VÁLIDA Y DOBLE CONFIRMACIÓN"
            );
            System.out.println(
                    "======================================================"
            );


            long idSalidaB =
                    crearSalidaYObtenerId(
                            salidaService,
                            "CONSUMO",
                            "TEST FINAL",
                            "Salida válida de 5 UN de Agua"
                    );


            if (idSalidaB <= 0) {
                return;
            }


            System.out.println(
                    "Salida creada: #"
                    + idSalidaB
            );


            ResultadoOperacion agregarAgua =
                    salidaService.agregarProducto(
                            idSalidaB,
                            agua.getIdProducto(),
                            new BigDecimal("5.000")
                    );


            System.out.println(
                    agregarAgua.getMensaje()
            );


            if (!agregarAgua.isExitoso()) {

                error(
                        "No se pudieron agregar "
                        + "5 UN de Agua."
                );

                return;
            }


            // -------------------------------------------------
            // BORRADOR NO DEBE MODIFICAR STOCK
            // -------------------------------------------------

            BigDecimal stockAguaBorrador =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Stock Agua mientras está BORRADOR",
                    stockAguaInicial,
                    stockAguaBorrador
            );


            SalidaStock salidaBorrador =
                    salidaService.buscarPorId(
                            idSalidaB
                    );


            if (salidaBorrador == null) {

                error(
                        "No se pudo recuperar la salida."
                );

                return;
            }


            System.out.println(
                    "Estado antes de confirmar: "
                    + salidaBorrador.getEstado()
            );


            if (!salidaBorrador.estaEnBorrador()) {

                error(
                        "La salida debería estar BORRADOR."
                );

                return;
            }


            // -------------------------------------------------
            // CONFIRMAR
            // -------------------------------------------------

            System.out.println();
            System.out.println(
                    "Confirmando salida..."
            );


            ResultadoOperacion confirmarB =
                    salidaService.confirmarSalida(
                            idSalidaB
                    );


            System.out.println(
                    confirmarB.getMensaje()
            );


            if (!confirmarB.isExitoso()) {

                error(
                        "No se pudo confirmar "
                        + "la salida válida."
                );

                return;
            }


            BigDecimal esperadoAguaDespuesB =
                    stockAguaInicial.subtract(
                            new BigDecimal("5.000")
                    );


            BigDecimal stockAguaDespuesB =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Stock Agua después de retirar 5 UN",
                    esperadoAguaDespuesB,
                    stockAguaDespuesB
            );


            SalidaStock salidaConfirmada =
                    salidaService.buscarPorId(
                            idSalidaB
                    );


            if (salidaConfirmada == null
                    || !salidaConfirmada.estaConfirmada()) {

                error(
                        "La salida no quedó CONFIRMADA."
                );

                return;
            }


            System.out.println(
                    "OK: Salida quedó CONFIRMADA."
            );


            // -------------------------------------------------
            // SEGUNDA CONFIRMACIÓN
            // -------------------------------------------------

            System.out.println();
            System.out.println(
                    "Intentando confirmar nuevamente..."
            );


            ResultadoOperacion segundaConfirmacion =
                    salidaService.confirmarSalida(
                            idSalidaB
                    );


            System.out.println(
                    segundaConfirmacion.getMensaje()
            );


            if (segundaConfirmacion.isExitoso()) {

                error(
                        "La misma salida pudo "
                        + "confirmarse dos veces."
                );

                return;
            }


            BigDecimal stockAguaSegundaConfirmacion =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Stock después del segundo intento",
                    esperadoAguaDespuesB,
                    stockAguaSegundaConfirmacion
            );


            System.out.println(
                    "OK: Doble confirmación rechazada."
            );


            // =================================================
            // PRUEBA C
            // ROLLBACK MULTIPRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "======================================================"
            );
            System.out.println(
                    "PRUEBA C - ROLLBACK MULTIPRODUCTO"
            );
            System.out.println(
                    "======================================================"
            );


            BigDecimal aguaAntesRollback =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal jamonAntesRollback =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            // Pedimos deliberadamente 1 KG más
            // de Jamón que el disponible.

            BigDecimal jamonImposible =
                    jamonAntesRollback.add(
                            new BigDecimal("1.000")
                    );


            System.out.println(
                    "Stock antes del intento:"
            );


            System.out.println(
                    "Agua: "
                    + aguaAntesRollback
                    + " UN"
            );


            System.out.println(
                    "Jamón: "
                    + jamonAntesRollback
                    + " KG"
            );


            System.out.println(
                    "Intentaremos retirar:"
            );


            System.out.println(
                    "Agua: 2 UN"
            );


            System.out.println(
                    "Jamón: "
                    + jamonImposible
                    + " KG"
            );


            long idSalidaC =
                    crearSalidaYObtenerId(
                            salidaService,
                            "CONSUMO",
                            "TEST ROLLBACK",
                            "Debe fallar por stock insuficiente de Jamón"
                    );


            if (idSalidaC <= 0) {
                return;
            }


            ResultadoOperacion agregarAguaC =
                    salidaService.agregarProducto(
                            idSalidaC,
                            agua.getIdProducto(),
                            new BigDecimal("2.000")
                    );


            if (!agregarAguaC.isExitoso()) {

                error(
                        "No se pudo agregar Agua "
                        + "a la salida multiproducto."
                );

                return;
            }


            ResultadoOperacion agregarJamonC =
                    salidaService.agregarProducto(
                            idSalidaC,
                            jamon.getIdProducto(),
                            jamonImposible
                    );


            if (!agregarJamonC.isExitoso()) {

                error(
                        "No se pudo agregar Jamón "
                        + "al BORRADOR."
                );

                return;
            }


            System.out.println(
                    "Productos agregados al BORRADOR."
            );


            System.out.println();
            System.out.println(
                    "Confirmando salida que debe fallar..."
            );


            ResultadoOperacion confirmarC =
                    salidaService.confirmarSalida(
                            idSalidaC
                    );


            System.out.println(
                    confirmarC.getMensaje()
            );


            if (confirmarC.isExitoso()) {

                error(
                        "La salida fue confirmada "
                        + "sin stock suficiente."
                );

                return;
            }


            // -------------------------------------------------
            // VERIFICAR ROLLBACK TOTAL
            // -------------------------------------------------

            BigDecimal aguaDespuesRollback =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal jamonDespuesRollback =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Agua después del rollback",
                    aguaAntesRollback,
                    aguaDespuesRollback
            );


            validarIgual(
                    "Jamón después del rollback",
                    jamonAntesRollback,
                    jamonDespuesRollback
            );


            SalidaStock salidaFallida =
                    salidaService.buscarPorId(
                            idSalidaC
                    );


            if (salidaFallida == null) {

                error(
                        "No se pudo recuperar "
                        + "la salida fallida."
                );

                return;
            }


            if (!salidaFallida.estaEnBorrador()) {

                error(
                        "La salida fallida debería "
                        + "seguir en BORRADOR."
                );

                return;
            }


            System.out.println(
                    "Estado después del fallo: "
                    + salidaFallida.getEstado()
            );


            System.out.println(
                    "OK: Rollback completo."
            );


            // =================================================
            // PRUEBA D
            // SALIDA MULTIPRODUCTO VÁLIDA
            // =================================================

            System.out.println();
            System.out.println(
                    "======================================================"
            );
            System.out.println(
                    "PRUEBA D - SALIDA MULTIPRODUCTO VÁLIDA"
            );
            System.out.println(
                    "======================================================"
            );


            BigDecimal aguaAntesD =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal jamonAntesD =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal salidaAguaD =
                    new BigDecimal("3.000");


            BigDecimal salidaJamonD =
                    new BigDecimal("0.500");


            long idSalidaD =
                    crearSalidaYObtenerId(
                            salidaService,
                            "CONSUMO",
                            "TEST MULTIPRODUCTO",
                            "Salida válida Agua + Jamón"
                    );


            if (idSalidaD <= 0) {
                return;
            }


            ResultadoOperacion agregarAguaD =
                    salidaService.agregarProducto(
                            idSalidaD,
                            agua.getIdProducto(),
                            salidaAguaD
                    );


            if (!agregarAguaD.isExitoso()) {

                error(
                        "No se pudo agregar Agua."
                );

                return;
            }


            ResultadoOperacion agregarJamonD =
                    salidaService.agregarProducto(
                            idSalidaD,
                            jamon.getIdProducto(),
                            salidaJamonD
                    );


            if (!agregarJamonD.isExitoso()) {

                error(
                        "No se pudo agregar Jamón."
                );

                return;
            }


            // Stock todavía intacto en BORRADOR.

            validarIgual(
                    "Agua antes de confirmar multiproducto",
                    aguaAntesD,
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    )
            );


            validarIgual(
                    "Jamón antes de confirmar multiproducto",
                    jamonAntesD,
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    )
            );


            System.out.println();
            System.out.println(
                    "Confirmando salida multiproducto..."
            );


            ResultadoOperacion confirmarD =
                    salidaService.confirmarSalida(
                            idSalidaD
                    );


            System.out.println(
                    confirmarD.getMensaje()
            );


            if (!confirmarD.isExitoso()) {

                error(
                        "No se pudo confirmar "
                        + "la salida multiproducto válida."
                );

                return;
            }


            BigDecimal esperadoAguaD =
                    aguaAntesD.subtract(
                            salidaAguaD
                    );


            BigDecimal esperadoJamonD =
                    jamonAntesD.subtract(
                            salidaJamonD
                    );


            BigDecimal aguaFinal =
                    stockDao.obtenerCantidad(
                            agua.getIdProducto(),
                            ID_DEPOSITO
                    );


            BigDecimal jamonFinal =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            validarIgual(
                    "Stock final Agua",
                    esperadoAguaD,
                    aguaFinal
            );


            validarIgual(
                    "Stock final Jamón",
                    esperadoJamonD,
                    jamonFinal
            );


            SalidaStock salidaDFinal =
                    salidaService.buscarPorId(
                            idSalidaD
                    );


            if (salidaDFinal == null
                    || !salidaDFinal.estaConfirmada()) {

                error(
                        "La salida multiproducto "
                        + "no quedó CONFIRMADA."
                );

                return;
            }


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "======================================================"
            );
            System.out.println(
                    "TEST FINAL SALIDA STOCK: APROBADO"
            );
            System.out.println(
                    "======================================================"
            );

            System.out.println(
                    "OK - UN rechaza cantidades decimales"
            );

            System.out.println(
                    "OK - BORRADOR no modifica stock"
            );

            System.out.println(
                    "OK - Confirmación descuenta stock"
            );

            System.out.println(
                    "OK - Doble confirmación rechazada"
            );

            System.out.println(
                    "OK - Stock insuficiente rechazado"
            );

            System.out.println(
                    "OK - Rollback multiproducto completo"
            );

            System.out.println(
                    "OK - Salida multiproducto válida"
            );

            System.out.println(
                    "OK - KG admite cantidades decimales"
            );

            System.out.println();
            System.out.println(
                    "Stock inicial Agua: "
                    + stockAguaInicial
                    + " UN"
            );

            System.out.println(
                    "Stock final Agua:   "
                    + aguaFinal
                    + " UN"
            );

            System.out.println(
                    "Stock inicial Jamón: "
                    + stockJamonInicial
                    + " KG"
            );

            System.out.println(
                    "Stock final Jamón:   "
                    + jamonFinal
                    + " KG"
            );

            System.out.println();
            System.out.println(
                    "MÓDULO SALIDA STOCK VALIDADO."
            );

            System.out.println(
                    "======================================================"
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "======================================================"
            );

            System.out.println(
                    "ERROR INESPERADO DURANTE EL TEST"
            );

            System.out.println(
                    "======================================================"
            );

            System.out.println(
                    ex.getMessage()
            );

            ex.printStackTrace();
        }
    }


    // =========================================================
    // CREAR SALIDA Y OBTENER ID
    // =========================================================

    /*
     * Actualmente crearSalida() devuelve ResultadoOperacion
     * pero no devuelve directamente el ID generado.
     *
     * Por eso, para estos tests recuperamos la salida
     * más nueva después de crearla.
     *
     * Esto está bien para un test local.
     *
     * Para producción multi-PC posteriormente conviene
     * que crearSalida() devuelva directamente el ID
     * generado por la base de datos.
     */
    private static long crearSalidaYObtenerId(
            SalidaStockService salidaService,
            String motivo,
            String destino,
            String observaciones) {


        ResultadoOperacion resultado =
                salidaService.crearSalida(
                        ID_DEPOSITO,
                        motivo,
                        destino,
                        observaciones
                );


        System.out.println(
                resultado.getMensaje()
        );


        if (!resultado.isExitoso()) {

            error(
                    "No se pudo crear la salida."
            );

            return -1;
        }


        long mayorId = -1;


        for (SalidaStock salida
                : salidaService.listarTodas()) {

            if (salida != null
                    && salida.getIdSalida() > mayorId) {

                mayorId =
                        salida.getIdSalida();
            }
        }


        if (mayorId <= 0) {

            error(
                    "No se pudo determinar "
                    + "el ID de la salida creada."
            );

            return -1;
        }


        return mayorId;
    }


    // =========================================================
    // VALIDAR BIGDECIMAL
    // =========================================================

    private static void validarIgual(
            String descripcion,
            BigDecimal esperado,
            BigDecimal obtenido) {


        if (esperado == null
                || obtenido == null) {

            throw new IllegalStateException(
                    descripcion
                    + " -> valor nulo."
            );
        }


        if (esperado.compareTo(
                obtenido
        ) != 0) {

            throw new IllegalStateException(
                    descripcion
                    + " INCORRECTO."
                    + " Esperado: "
                    + esperado
                    + " | Obtenido: "
                    + obtenido
            );
        }


        System.out.println(
                "OK: "
                + descripcion
                + " = "
                + obtenido
        );
    }


    // =========================================================
    // ERROR
    // =========================================================

    private static void error(
            String mensaje) {

        System.out.println();
        System.out.println(
                "*************** ERROR ***************"
        );

        System.out.println(
                mensaje
        );

        System.out.println(
                "*************************************"
        );
    }
}