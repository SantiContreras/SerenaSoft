package util.TestStock;

import Dao.DepositoDao;
import Dao.StockProductoDao;
import Dao.UsuarioDao;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import model.Compra;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.SalidaStock;
import model.Usuario;

import services.CompraService;
import services.ProductoService;
import services.ProveedorService;
import services.ResultadoOperacion;
import services.SalidaStockService;

import sesion.SesionUsuario;

public class TestFinalCompraSalida {

    // =========================================================
    // SERVICES / DAO
    // =========================================================

    private static final ProductoService productoService =
            new ProductoService();

    private static final ProveedorService proveedorService =
            new ProveedorService();

    private static final CompraService compraService =
            new CompraService();

    private static final SalidaStockService salidaService =
            new SalidaStockService();

    private static final StockProductoDao stockDao =
            new StockProductoDao();

    private static final DepositoDao depositoDao =
            new DepositoDao();

    private static final UsuarioDao usuarioDao =
            new UsuarioDao();


    // =========================================================
    // DATOS DE LA PRUEBA
    // =========================================================

    /*
     * Todos estos códigos fueron cargados por CargaDatosDemo.
     *
     * Probamos distintos tipos de conversión:
     *
     * BEB-0002 -> PACK -> UN   factor 6
     * ARR-0001 -> BULTO -> UN  factor 10
     * FID-0001 -> CAJA -> UN   factor 20
     * LEC-0001 -> CAJA -> UN   factor 12
     * QUE-0001 -> KG -> KG     factor 1
     * DET-0001 -> CAJA -> UN   factor 12
     */

    private static Producto coca;
    private static Producto arroz;
    private static Producto fideos;
    private static Producto leche;
    private static Producto queso;
    private static Producto detergente;

    private static Deposito deposito;
    private static Proveedor proveedor;


    // =========================================================
    // STOCK INICIAL
    // =========================================================

    private static BigDecimal stockInicialCoca;
    private static BigDecimal stockInicialArroz;
    private static BigDecimal stockInicialFideos;
    private static BigDecimal stockInicialLeche;
    private static BigDecimal stockInicialQueso;
    private static BigDecimal stockInicialDetergente;


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println(
                "===================================================="
        );
        System.out.println(
                "TEST FINAL - COMPRA + STOCK + SALIDA"
        );
        System.out.println(
                "===================================================="
        );

        try {

            // =================================================
            // 1. INICIAR SESIÓN
            // =================================================

            iniciarSesion();


            // =================================================
            // 2. BUSCAR DATOS BASE
            // =================================================

            cargarDatosBase();


            // =================================================
            // 3. GUARDAR STOCK INICIAL
            // =================================================

            guardarStockInicial();


            // =================================================
            // 4. MOSTRAR STOCK INICIAL
            // =================================================

            mostrarStockInicial();


            // =================================================
            // 5. CREAR COMPRA
            // =================================================

            long idCompra =
                    probarCompra();


            // =================================================
            // 6. VERIFICAR STOCK DESPUÉS DE COMPRA
            // =================================================

            verificarStockDespuesCompra();


            // =================================================
            // 7. CREAR SALIDA
            // =================================================

            long idSalida =
                    probarSalida();


            // =================================================
            // 8. VERIFICAR SALDOS FINALES
            // =================================================

            verificarSaldosFinales();


            // =================================================
            // RESULTADO
            // =================================================

            System.out.println();
            System.out.println(
                    "===================================================="
            );
            System.out.println(
                    "TEST FINAL COMPLETADO CORRECTAMENTE"
            );
            System.out.println(
                    "===================================================="
            );

            System.out.println(
                    "Compra utilizada: ID "
                    + idCompra
            );

            System.out.println(
                    "Salida utilizada: ID "
                    + idSalida
            );

            System.out.println();

            System.out.println(
                    "COMPRA -> STOCK -> SALIDA: OK"
            );

            System.out.println(
                    "Conversión de unidades: OK"
            );

            System.out.println(
                    "Compra BORRADOR no modifica stock: OK"
            );

            System.out.println(
                    "Salida BORRADOR no modifica stock: OK"
            );

            System.out.println(
                    "Saldos finales: OK"
            );

            System.out.println(
                    "===================================================="
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "===================================================="
            );
            System.out.println(
                    "TEST FINAL FALLÓ"
            );
            System.out.println(
                    "===================================================="
            );

            System.out.println(
                    ex.getMessage()
            );

            ex.printStackTrace();

        } finally {

            if (SesionUsuario.haySesion()) {

                SesionUsuario.cerrarSesion();
            }
        }
    }


    // =========================================================
    // INICIAR SESIÓN
    // =========================================================

    private static void iniciarSesion()
            throws Exception {

        System.out.println();
        System.out.println(
                "1) INICIANDO SESIÓN"
        );
        System.out.println(
                "----------------------------------------------------"
        );

        /*
         * Usamos el usuario "juan" que ya tenemos
         * creado para las pruebas.
         *
         * No necesitamos contraseña porque este test
         * no está probando LoginService.
         *
         * Necesitamos una sesión válida porque CompraService
         * y SalidaStockService toman el usuario desde
         * SesionUsuario.
         */

        Usuario usuario =
                usuarioDao.buscarPorUsername(
                        "juan"
                );

        if (usuario == null) {

            throw new Exception(
                    "No existe el usuario 'juan'."
            );
        }

        SesionUsuario.iniciarSesion(
                usuario
        );

        if (!SesionUsuario.haySesion()) {

            throw new Exception(
                    "No se pudo iniciar la sesión."
            );
        }

        System.out.println(
                "[OK] Sesión iniciada: "
                + SesionUsuario.getNombreUsuario()
        );
    }


    // =========================================================
    // CARGAR DATOS BASE
    // =========================================================

    private static void cargarDatosBase()
            throws Exception {

        System.out.println();
        System.out.println(
                "2) BUSCANDO PRODUCTOS POR CÓDIGO"
        );
        System.out.println(
                "----------------------------------------------------"
        );


        // -----------------------------------------------------
        // PRODUCTOS
        // -----------------------------------------------------

        coca =
                buscarProducto(
                        "BEB-0002"
                );

        arroz =
                buscarProducto(
                        "ARR-0001"
                );

        fideos =
                buscarProducto(
                        "FID-0001"
                );

        leche =
                buscarProducto(
                        "LEC-0001"
                );

        queso =
                buscarProducto(
                        "QUE-0001"
                );

        detergente =
                buscarProducto(
                        "DET-0001"
                );


        // -----------------------------------------------------
        // DEPÓSITO
        // -----------------------------------------------------

        deposito =
                depositoDao.buscarPorCodigo(
                        "DEP-01"
                );

        if (deposito == null) {

            throw new Exception(
                    "No se encontró el depósito DEP-01."
            );
        }

        System.out.println(
                "[OK] Depósito: "
                + deposito.getNombre()
        );


        // -----------------------------------------------------
        // PROVEEDOR
        // -----------------------------------------------------

        proveedor =
                proveedorService.buscarPorCuit(
                        "30-90000004-4"
                );

        if (proveedor == null) {

            throw new Exception(
                    "No se encontró el proveedor demo "
                    + "Mayorista Norte."
            );
        }

        System.out.println(
                "[OK] Proveedor: "
                + proveedor
        );
    }


    // =========================================================
    // BUSCAR PRODUCTO
    // =========================================================

    private static Producto buscarProducto(
            String codigo)
            throws Exception {

        Producto producto =
                productoService.buscarPorCodigo(
                        codigo
                );

        if (producto == null) {

            throw new Exception(
                    "No existe el producto "
                    + codigo
                    + ". Ejecutá primero CargaDatosDemo."
            );
        }

        System.out.println(
                "[OK] "
                + codigo
                + " - "
                + producto.getNombre()
        );

        return producto;
    }


    // =========================================================
    // GUARDAR STOCK INICIAL
    // =========================================================

    private static void guardarStockInicial() {

        stockInicialCoca =
                obtenerStock(coca);

        stockInicialArroz =
                obtenerStock(arroz);

        stockInicialFideos =
                obtenerStock(fideos);

        stockInicialLeche =
                obtenerStock(leche);

        stockInicialQueso =
                obtenerStock(queso);

        stockInicialDetergente =
                obtenerStock(detergente);
    }


    // =========================================================
    // MOSTRAR STOCK INICIAL
    // =========================================================

    private static void mostrarStockInicial() {

        System.out.println();
        System.out.println(
                "3) STOCK INICIAL"
        );
        System.out.println(
                "----------------------------------------------------"
        );

        imprimirStock(
                coca,
                stockInicialCoca
        );

        imprimirStock(
                arroz,
                stockInicialArroz
        );

        imprimirStock(
                fideos,
                stockInicialFideos
        );

        imprimirStock(
                leche,
                stockInicialLeche
        );

        imprimirStock(
                queso,
                stockInicialQueso
        );

        imprimirStock(
                detergente,
                stockInicialDetergente
        );
    }


    // =========================================================
    // PRUEBA DE COMPRA
    // =========================================================

    private static long probarCompra()
            throws Exception {

        System.out.println();
        System.out.println(
                "4) CREANDO COMPRA BORRADOR"
        );
        System.out.println(
                "----------------------------------------------------"
        );


        // -----------------------------------------------------
        // CREAR CABECERA
        // -----------------------------------------------------

        Compra compra =
                new Compra();

        compra.setProveedor(
                proveedor
        );

        compra.setDeposito(
                deposito
        );

        compra.setNumeroComprobante(
                "DEMO-FINAL-COMPRA"
        );

        compra.setOrigenCarga(
                "MANUAL"
        );

        compra.setObservaciones(
                "Test final Compra -> Stock -> Salida"
        );


        ResultadoOperacion resultado =
                compraService.crearCompra(
                        compra
                );

        exigirOk(
                "Crear compra",
                resultado
        );


        // -----------------------------------------------------
        // RECUPERAR ID
        // -----------------------------------------------------

        long idCompra =
                obtenerUltimaCompra();

        System.out.println(
                "[OK] Compra BORRADOR ID: "
                + idCompra
        );


        // -----------------------------------------------------
        // AGREGAR PRODUCTOS
        // -----------------------------------------------------

        System.out.println();
        System.out.println(
                "Agregando productos a la compra..."
        );


        /*
         * 10 PACK Coca × 6
         * = 60 UN de stock
         */

        agregarCompra(
                idCompra,
                coca,
                "10",
                "9000"
        );


        /*
         * 5 BULTOS Arroz × 10
         * = 50 UN de stock
         */

        agregarCompra(
                idCompra,
                arroz,
                "5",
                "11000"
        );


        /*
         * 3 CAJAS Fideos × 20
         * = 60 UN de stock
         */

        agregarCompra(
                idCompra,
                fideos,
                "3",
                "16000"
        );


        /*
         * 4 CAJAS Leche × 12
         * = 48 UN de stock
         */

        agregarCompra(
                idCompra,
                leche,
                "4",
                "15000"
        );


        /*
         * 8,500 KG × 1
         * = 8,500 KG de stock
         */

        agregarCompra(
                idCompra,
                queso,
                "8.500",
                "8500"
        );


        /*
         * 2 CAJAS Detergente × 12
         * = 24 UN de stock
         */

        agregarCompra(
                idCompra,
                detergente,
                "2",
                "14500"
        );


        // -----------------------------------------------------
        // BORRADOR NO DEBE MODIFICAR STOCK
        // -----------------------------------------------------

        System.out.println();
        System.out.println(
                "Verificando que BORRADOR no modifique stock..."
        );

        exigirStock(
                coca,
                stockInicialCoca,
                "Coca-Cola cambió antes de confirmar compra"
        );

        exigirStock(
                arroz,
                stockInicialArroz,
                "Arroz cambió antes de confirmar compra"
        );

        exigirStock(
                fideos,
                stockInicialFideos,
                "Fideos cambió antes de confirmar compra"
        );

        exigirStock(
                leche,
                stockInicialLeche,
                "Leche cambió antes de confirmar compra"
        );

        exigirStock(
                queso,
                stockInicialQueso,
                "Queso cambió antes de confirmar compra"
        );

        exigirStock(
                detergente,
                stockInicialDetergente,
                "Detergente cambió antes de confirmar compra"
        );

        System.out.println(
                "[OK] Compra BORRADOR no modificó stock."
        );


        // -----------------------------------------------------
        // CONFIRMAR
        // -----------------------------------------------------

        System.out.println();
        System.out.println(
                "Confirmando compra..."
        );

        resultado =
                compraService.confirmarCompra(
                        idCompra
                );

        exigirOk(
                "Confirmar compra",
                resultado
        );

        System.out.println(
                "[OK] Compra confirmada."
        );


        // -----------------------------------------------------
        // DOBLE CONFIRMACIÓN
        // -----------------------------------------------------

        resultado =
                compraService.confirmarCompra(
                        idCompra
                );

        if (resultado.isExitoso()) {

            throw new Exception(
                    "ERROR: la compra pudo confirmarse dos veces."
            );
        }

        System.out.println(
                "[OK] Doble confirmación de compra rechazada."
        );

        return idCompra;
    }


    // =========================================================
    // AGREGAR PRODUCTO A COMPRA
    // =========================================================

    private static void agregarCompra(
            long idCompra,
            Producto producto,
            String cantidad,
            String costo)
            throws Exception {

        ResultadoOperacion resultado =
                compraService.agregarProducto(
                        idCompra,
                        producto.getIdProducto(),
                        new BigDecimal(cantidad),
                        new BigDecimal(costo)
                );

        exigirOk(
                "Agregar "
                + producto.getNombre(),
                resultado
        );

        System.out.println(
                "[OK] "
                + producto.getCodigo()
                + " | compra: "
                + cantidad
                + " "
                + producto.getUnidadCompra().getCodigo()
        );
    }


    // =========================================================
    // VERIFICAR STOCK DESPUÉS DE COMPRA
    // =========================================================

    private static void verificarStockDespuesCompra()
            throws Exception {

        System.out.println();
        System.out.println(
                "5) STOCK DESPUÉS DE CONFIRMAR COMPRA"
        );
        System.out.println(
                "----------------------------------------------------"
        );


        BigDecimal esperadoCoca =
                stockInicialCoca.add(
                        bd("60")
                );

        BigDecimal esperadoArroz =
                stockInicialArroz.add(
                        bd("50")
                );

        BigDecimal esperadoFideos =
                stockInicialFideos.add(
                        bd("60")
                );

        BigDecimal esperadoLeche =
                stockInicialLeche.add(
                        bd("48")
                );

        BigDecimal esperadoQueso =
                stockInicialQueso.add(
                        bd("8.500")
                );

        BigDecimal esperadoDetergente =
                stockInicialDetergente.add(
                        bd("24")
                );


        exigirStock(
                coca,
                esperadoCoca,
                "Conversión PACK -> UN incorrecta"
        );

        exigirStock(
                arroz,
                esperadoArroz,
                "Conversión BULTO -> UN incorrecta"
        );

        exigirStock(
                fideos,
                esperadoFideos,
                "Conversión CAJA -> UN incorrecta"
        );

        exigirStock(
                leche,
                esperadoLeche,
                "Conversión CAJA -> UN incorrecta"
        );

        exigirStock(
                queso,
                esperadoQueso,
                "Conversión KG -> KG incorrecta"
        );

        exigirStock(
                detergente,
                esperadoDetergente,
                "Conversión CAJA -> UN incorrecta"
        );


        imprimirStock(
                coca,
                esperadoCoca
        );

        imprimirStock(
                arroz,
                esperadoArroz
        );

        imprimirStock(
                fideos,
                esperadoFideos
        );

        imprimirStock(
                leche,
                esperadoLeche
        );

        imprimirStock(
                queso,
                esperadoQueso
        );

        imprimirStock(
                detergente,
                esperadoDetergente
        );


        System.out.println();
        System.out.println(
                "[OK] Todas las conversiones de compra son correctas."
        );
    }


    // =========================================================
    // PRUEBA DE SALIDA
    // =========================================================

    private static long probarSalida()
            throws Exception {

        System.out.println();
        System.out.println(
                "6) CREANDO SALIDA BORRADOR"
        );
        System.out.println(
                "----------------------------------------------------"
        );


        // -----------------------------------------------------
        // STOCK ANTES DE SALIDA
        // -----------------------------------------------------

        BigDecimal cocaAntes =
                obtenerStock(coca);

        BigDecimal arrozAntes =
                obtenerStock(arroz);

        BigDecimal fideosAntes =
                obtenerStock(fideos);

        BigDecimal quesoAntes =
                obtenerStock(queso);

        BigDecimal detergenteAntes =
                obtenerStock(detergente);


        // -----------------------------------------------------
        // CREAR SALIDA
        // -----------------------------------------------------

        ResultadoOperacion resultado =
                salidaService.crearSalida(
                        deposito.getIdDeposito(),
                        "Salida demo final",
                        "Sector de ventas",
                        "Test final Compra -> Stock -> Salida"
                );

        exigirOk(
                "Crear salida",
                resultado
        );


        // -----------------------------------------------------
        // RECUPERAR ID
        // -----------------------------------------------------

        long idSalida =
                obtenerUltimaSalida();

        System.out.println(
                "[OK] Salida BORRADOR ID: "
                + idSalida
        );


        // -----------------------------------------------------
        // AGREGAR PRODUCTOS
        // -----------------------------------------------------

        agregarSalida(
                idSalida,
                coca,
                "5"
        );

        agregarSalida(
                idSalida,
                arroz,
                "3"
        );

        agregarSalida(
                idSalida,
                fideos,
                "4"
        );

        agregarSalida(
                idSalida,
                queso,
                "1.250"
        );

        agregarSalida(
                idSalida,
                detergente,
                "2"
        );


        // -----------------------------------------------------
        // BORRADOR NO DEBE DESCONTAR
        // -----------------------------------------------------

        System.out.println();
        System.out.println(
                "Verificando salida BORRADOR..."
        );

        exigirStock(
                coca,
                cocaAntes,
                "Coca-Cola se descontó en BORRADOR"
        );

        exigirStock(
                arroz,
                arrozAntes,
                "Arroz se descontó en BORRADOR"
        );

        exigirStock(
                fideos,
                fideosAntes,
                "Fideos se descontaron en BORRADOR"
        );

        exigirStock(
                queso,
                quesoAntes,
                "Queso se descontó en BORRADOR"
        );

        exigirStock(
                detergente,
                detergenteAntes,
                "Detergente se descontó en BORRADOR"
        );

        System.out.println(
                "[OK] Salida BORRADOR no modificó stock."
        );


        // -----------------------------------------------------
        // CONFIRMAR SALIDA
        // -----------------------------------------------------

        System.out.println();
        System.out.println(
                "Confirmando salida..."
        );

        resultado =
                salidaService.confirmarSalida(
                        idSalida
                );

        exigirOk(
                "Confirmar salida",
                resultado
        );

        System.out.println(
                "[OK] Salida confirmada."
        );


        // -----------------------------------------------------
        // DOBLE CONFIRMACIÓN
        // -----------------------------------------------------

        resultado =
                salidaService.confirmarSalida(
                        idSalida
                );

        if (resultado.isExitoso()) {

            throw new Exception(
                    "ERROR: la salida pudo confirmarse dos veces."
            );
        }

        System.out.println(
                "[OK] Doble confirmación de salida rechazada."
        );

        return idSalida;
    }


    // =========================================================
    // AGREGAR PRODUCTO A SALIDA
    // =========================================================

    private static void agregarSalida(
            long idSalida,
            Producto producto,
            String cantidad)
            throws Exception {

        ResultadoOperacion resultado =
                salidaService.agregarProducto(
                        idSalida,
                        producto.getIdProducto(),
                        new BigDecimal(cantidad)
                );

        exigirOk(
                "Agregar a salida "
                + producto.getNombre(),
                resultado
        );

        System.out.println(
                "[OK] "
                + producto.getCodigo()
                + " | salida: "
                + cantidad
                + " "
                + producto.getUnidadVenta().getCodigo()
        );
    }


    // =========================================================
    // VERIFICAR SALDOS FINALES
    // =========================================================

    private static void verificarSaldosFinales()
            throws Exception {

        System.out.println();
        System.out.println(
                "7) SALDOS FINALES"
        );
        System.out.println(
                "----------------------------------------------------"
        );


        /*
         * Fórmula:
         *
         * stock final =
         * stock inicial
         * + ingreso por compra
         * - salida
         */


        BigDecimal finalCoca =
                stockInicialCoca
                        .add(bd("60"))
                        .subtract(bd("5"));

        BigDecimal finalArroz =
                stockInicialArroz
                        .add(bd("50"))
                        .subtract(bd("3"));

        BigDecimal finalFideos =
                stockInicialFideos
                        .add(bd("60"))
                        .subtract(bd("4"));

        BigDecimal finalLeche =
                stockInicialLeche
                        .add(bd("48"));

        BigDecimal finalQueso =
                stockInicialQueso
                        .add(bd("8.500"))
                        .subtract(bd("1.250"));

        BigDecimal finalDetergente =
                stockInicialDetergente
                        .add(bd("24"))
                        .subtract(bd("2"));


        // -----------------------------------------------------
        // VALIDAR
        // -----------------------------------------------------

        exigirStock(
                coca,
                finalCoca,
                "Saldo final Coca-Cola incorrecto"
        );

        exigirStock(
                arroz,
                finalArroz,
                "Saldo final Arroz incorrecto"
        );

        exigirStock(
                fideos,
                finalFideos,
                "Saldo final Fideos incorrecto"
        );

        exigirStock(
                leche,
                finalLeche,
                "Saldo final Leche incorrecto"
        );

        exigirStock(
                queso,
                finalQueso,
                "Saldo final Queso incorrecto"
        );

        exigirStock(
                detergente,
                finalDetergente,
                "Saldo final Detergente incorrecto"
        );


        // -----------------------------------------------------
        // MOSTRAR
        // -----------------------------------------------------

        imprimirStock(
                coca,
                finalCoca
        );

        imprimirStock(
                arroz,
                finalArroz
        );

        imprimirStock(
                fideos,
                finalFideos
        );

        imprimirStock(
                leche,
                finalLeche
        );

        imprimirStock(
                queso,
                finalQueso
        );

        imprimirStock(
                detergente,
                finalDetergente
        );


        System.out.println();
        System.out.println(
                "[OK] Todos los saldos finales coinciden."
        );
    }


    // =========================================================
    // OBTENER STOCK
    // =========================================================

    private static BigDecimal obtenerStock(
            Producto producto) {

        return stockDao.obtenerCantidad(
                producto.getIdProducto(),
                deposito.getIdDeposito()
        );
    }


    // =========================================================
    // EXIGIR STOCK
    // =========================================================

    private static void exigirStock(
            Producto producto,
            BigDecimal esperado,
            String mensaje)
            throws Exception {

        BigDecimal real =
                obtenerStock(
                        producto
                );

        if (real.compareTo(
                esperado
        ) != 0) {

            throw new Exception(
                    mensaje
                    + " | Producto: "
                    + producto.getCodigo()
                    + " | esperado="
                    + esperado.toPlainString()
                    + " | real="
                    + real.toPlainString()
            );
        }
    }


    // =========================================================
    // IMPRIMIR STOCK
    // =========================================================

    private static void imprimirStock(
            Producto producto,
            BigDecimal cantidad) {

        String unidad =
                producto.getUnidadVenta() != null
                ? producto.getUnidadVenta().getCodigo()
                : "";

        System.out.println(
                producto.getCodigo()
                + " | "
                + producto.getNombre()
                + " = "
                + cantidad.toPlainString()
                + " "
                + unidad
        );
    }


    // =========================================================
    // OBTENER ÚLTIMA COMPRA
    // =========================================================

    private static long obtenerUltimaCompra()
            throws Exception {

        List<Compra> compras =
                compraService.listarTodos();

        if (compras == null
                || compras.isEmpty()) {

            throw new Exception(
                    "No se pudo recuperar la compra creada."
            );
        }

        return compras.stream()
                .map(Compra::getIdCompra)
                .max(Comparator.naturalOrder())
                .orElseThrow(
                        () -> new Exception(
                                "No se pudo determinar ID de compra."
                        )
                );
    }


    // =========================================================
    // OBTENER ÚLTIMA SALIDA
    // =========================================================

    private static long obtenerUltimaSalida()
            throws Exception {

        List<SalidaStock> salidas =
                salidaService.listarTodas();

        if (salidas == null
                || salidas.isEmpty()) {

            throw new Exception(
                    "No se pudo recuperar la salida creada."
            );
        }

        return salidas.stream()
                .map(SalidaStock::getIdSalida)
                .max(Comparator.naturalOrder())
                .orElseThrow(
                        () -> new Exception(
                                "No se pudo determinar ID de salida."
                        )
                );
    }


    // =========================================================
    // EXIGIR RESULTADO EXITOSO
    // =========================================================

    private static void exigirOk(
            String operacion,
            ResultadoOperacion resultado)
            throws Exception {

        if (resultado == null) {

            throw new Exception(
                    operacion
                    + " devolvió null."
            );
        }

        if (!resultado.isExitoso()) {

            throw new Exception(
                    operacion
                    + " falló: "
                    + resultado.getMensaje()
            );
        }
    }


    // =========================================================
    // BIG DECIMAL
    // =========================================================

    private static BigDecimal bd(
            String valor) {

        return new BigDecimal(
                valor
        );
    }
}