package util.TestStock;

import Dao.DepositoDao;
import Dao.ProveedorDao;
import Dao.StockProductoDao;

import model.Compra;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.StockProducto;

import services.CompraService;
import services.LoginService;
import services.ProductoService;
import services.ResultadoLogin;
import services.ResultadoOperacion;

import java.math.BigDecimal;

public class TestCompraCaja12 {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST COMPRA CAJA x12 - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );


        // =====================================================
        // DATOS DEL TEST
        // =====================================================

        final String CODIGO_PRODUCTO =
                "AGU-0001";

        final int ID_PROVEEDOR = 1;

        final int ID_DEPOSITO = 1;

        final BigDecimal CANTIDAD_INVALIDA =
                new BigDecimal("2.500");

        final BigDecimal CANTIDAD_CAJAS =
                new BigDecimal("5.000");

        final BigDecimal COSTO_CAJA =
                new BigDecimal("6000.00");

        final BigDecimal STOCK_ESPERADO =
                new BigDecimal("60.000");


        // =====================================================
        // SERVICIOS / DAO
        // =====================================================

        LoginService loginService =
                new LoginService();

        ProductoService productoService =
                new ProductoService();

        CompraService compraService =
                new CompraService();

        ProveedorDao proveedorDao =
                new ProveedorDao();

        DepositoDao depositoDao =
                new DepositoDao();

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
            // 2. BUSCAR PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "2) BUSCANDO AGUA MINERAL..."
            );


            Producto producto =
                    productoService.buscarPorCodigo(
                            CODIGO_PRODUCTO
                    );


            if (producto == null) {

                System.out.println(
                        "ERROR: No existe "
                        + CODIGO_PRODUCTO
                );

                System.out.println(
                        "Ejecutá primero "
                        + "TestProductoCaja12."
                );

                return;
            }


            System.out.println(
                    "Producto: "
                    + producto.getNombre()
            );

            System.out.println(
                    "ID: "
                    + producto.getIdProducto()
            );

            System.out.println(
                    "Unidad compra: "
                    + producto.getUnidadCompra()
            );

            System.out.println(
                    "Unidad venta: "
                    + producto.getUnidadVenta()
            );

            System.out.println(
                    "Factor conversión: "
                    + producto.getFactorConversion()
            );


            // =================================================
            // 3. VALIDAR CONFIGURACIÓN
            // =================================================

            if (producto.getUnidadCompra() == null
                    || !"CAJA".equalsIgnoreCase(
                            producto
                                    .getUnidadCompra()
                                    .getCodigo()
                    )) {

                System.out.println(
                        "ERROR: Unidad compra debe ser CAJA."
                );

                return;
            }


            if (producto.getUnidadVenta() == null
                    || !"UN".equalsIgnoreCase(
                            producto
                                    .getUnidadVenta()
                                    .getCodigo()
                    )) {

                System.out.println(
                        "ERROR: Unidad venta debe ser UN."
                );

                return;
            }


            if (producto.getFactorConversion()
                    .compareTo(
                            new BigDecimal("12")
                    ) != 0) {

                System.out.println(
                        "ERROR: Factor conversión debe ser 12."
                );

                return;
            }


            if (producto.getUnidadCompra()
                    .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: CAJA no debe "
                        + "permitir decimales."
                );

                return;
            }


            System.out.println(
                    "OK: Configuración CAJA x12 correcta."
            );


            // =================================================
            // 4. VERIFICAR STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "3) VERIFICANDO STOCK INICIAL..."
            );


            StockProducto registroInicial =
                    stockDao.buscarPorProductoDeposito(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            if (registroInicial != null
                    && registroInicial.getCantidad()
                            .compareTo(BigDecimal.ZERO) != 0) {

                System.out.println(
                        "ERROR: Esta prueba necesita "
                        + "stock inicial 0."
                );

                System.out.println(
                        "Stock encontrado: "
                        + registroInicial.getCantidad()
                        + " UN"
                );

                return;
            }


            BigDecimal stockInicial =
                    stockDao.obtenerCantidad(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock inicial: "
                    + stockInicial
                    + " UN"
            );


            // =================================================
            // 5. BUSCAR PROVEEDOR Y DEPÓSITO
            // =================================================

            Proveedor proveedor =
                    proveedorDao.buscarPorId(
                            ID_PROVEEDOR
                    );


            Deposito deposito =
                    depositoDao.buscarPorId(
                            ID_DEPOSITO
                    );


            if (proveedor == null) {

                System.out.println(
                        "ERROR: No existe proveedor."
                );

                return;
            }


            if (deposito == null) {

                System.out.println(
                        "ERROR: No existe depósito."
                );

                return;
            }


            // =================================================
            // 6. CREAR COMPRA BORRADOR
            // =================================================

            System.out.println();
            System.out.println(
                    "4) CREANDO COMPRA BORRADOR..."
            );


            Compra compra =
                    new Compra();


            compra.setProveedor(
                    proveedor
            );

            compra.setDeposito(
                    deposito
            );

            compra.setNumeroComprobante(
                    "TEST-CAJA-X12"
            );

            compra.setOrigenCarga(
                    "MANUAL"
            );

            compra.setObservaciones(
                    "Prueba conversión CAJA x12 a UN"
            );


            ResultadoOperacion crear =
                    compraService.crearCompra(
                            compra
                    );


            System.out.println(
                    crear.getMensaje()
            );


            if (!crear.isExitoso()) {
                return;
            }


            System.out.println(
                    "Compra #: "
                    + compra.getIdCompra()
            );


            // =================================================
            // 7. PRUEBA NEGATIVA: 2.5 CAJAS
            // =================================================

            System.out.println();
            System.out.println(
                    "5) INTENTANDO AGREGAR 2.500 CAJAS..."
            );


            ResultadoOperacion decimal =
                    compraService.agregarProducto(

                            compra.getIdCompra(),

                            producto.getIdProducto(),

                            "CAJA",

                            new BigDecimal("12"),

                            CANTIDAD_INVALIDA,

                            COSTO_CAJA
                    );


            System.out.println(
                    decimal.getMensaje()
            );


            if (decimal.isExitoso()) {

                System.out.println(
                        "ERROR CRÍTICO:"
                );

                System.out.println(
                        "El sistema permitió comprar "
                        + "2.500 CAJAS."
                );

                return;
            }


            System.out.println(
                    "OK: 2.500 CAJAS rechazadas."
            );


            // =================================================
            // 8. AGREGAR 5 CAJAS
            // =================================================

            System.out.println();
            System.out.println(
                    "6) AGREGANDO 5 CAJAS..."
            );


            ResultadoOperacion agregar =
                    compraService.agregarProducto(

                            compra.getIdCompra(),

                            producto.getIdProducto(),

                            "CAJA",

                            new BigDecimal("12"),

                            CANTIDAD_CAJAS,

                            COSTO_CAJA
                    );


            System.out.println(
                    agregar.getMensaje()
            );


            if (!agregar.isExitoso()) {
                return;
            }


            // =================================================
            // 9. VERIFICAR COMPRA
            // =================================================

            Compra borrador =
                    compraService.buscarPorId(
                            compra.getIdCompra()
                    );


            if (borrador == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar compra."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "Compra #: "
                    + borrador.getIdCompra()
            );

            System.out.println(
                    "Estado: "
                    + borrador.getEstado()
            );

            System.out.println(
                    "Detalles: "
                    + borrador.cantidadDetalles()
            );

            System.out.println(
                    "Subtotal: $"
                    + borrador.getSubtotal()
            );

            System.out.println(
                    "Total: $"
                    + borrador.getTotal()
            );


            // 5 cajas x $6.000 = $30.000
            BigDecimal totalEsperado =
                    new BigDecimal("30000.00");


            if (borrador.getTotal()
                    .compareTo(totalEsperado) != 0) {

                System.out.println(
                        "ERROR: Total incorrecto."
                );

                return;
            }


            System.out.println(
                    "OK: 5 CAJAS x $6.000 = $30.000"
            );


            // =================================================
            // 10. BORRADOR NO DEBE MODIFICAR STOCK
            // =================================================

            System.out.println();
            System.out.println(
                    "7) VERIFICANDO STOCK EN BORRADOR..."
            );


            BigDecimal stockBorrador =
                    stockDao.obtenerCantidad(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock: "
                    + stockBorrador
                    + " UN"
            );


            if (stockBorrador.compareTo(
                    BigDecimal.ZERO
            ) != 0) {

                System.out.println(
                        "ERROR: BORRADOR modificó stock."
                );

                return;
            }


            System.out.println(
                    "OK: Stock continúa en 0 UN."
            );


            // =================================================
            // 11. MOSTRAR CONVERSIÓN ESPERADA
            // =================================================

            BigDecimal conversionEsperada =
                    CANTIDAD_CAJAS.multiply(
                            producto.getFactorConversion()
                    );


            System.out.println();
            System.out.println(
                    "8) CONVERSIÓN ESPERADA"
            );

            System.out.println(
                    CANTIDAD_CAJAS
                    + " CAJAS"
            );

            System.out.println(
                    "x "
                    + producto.getFactorConversion()
                    + " UN por CAJA"
            );

            System.out.println(
                    "= "
                    + conversionEsperada
                    + " UN"
            );


            if (conversionEsperada.compareTo(
                    STOCK_ESPERADO
            ) != 0) {

                System.out.println(
                        "ERROR calculando conversión."
                );

                return;
            }


            // =================================================
            // 12. CONFIRMAR COMPRA
            // =================================================

            System.out.println();
            System.out.println(
                    "9) CONFIRMANDO COMPRA..."
            );


            ResultadoOperacion confirmar =
                    compraService.confirmarCompra(
                            compra.getIdCompra()
                    );


            System.out.println(
                    confirmar.getMensaje()
            );


            if (!confirmar.isExitoso()) {

                System.out.println(
                        "ERROR: No se pudo confirmar."
                );

                return;
            }


            // =================================================
            // 13. VERIFICAR ESTADO
            // =================================================

            Compra confirmada =
                    compraService.buscarPorId(
                            compra.getIdCompra()
                    );


            if (confirmada == null
                    || !confirmada.estaConfirmada()) {

                System.out.println(
                        "ERROR: Compra no quedó CONFIRMADA."
                );

                return;
            }


            System.out.println(
                    "OK: Compra CONFIRMADA."
            );


            // =================================================
            // 14. VERIFICAR STOCK_PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "10) VERIFICANDO STOCK..."
            );


            StockProducto stockCreado =
                    stockDao.buscarPorProductoDeposito(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            if (stockCreado == null) {

                System.out.println(
                        "ERROR: No se creó stock_producto."
                );

                return;
            }


            System.out.println(
                    "ID stock: "
                    + stockCreado.getIdStock()
            );

            System.out.println(
                    "Stock obtenido: "
                    + stockCreado.getCantidad()
                    + " UN"
            );


            // =================================================
            // 15. VALIDAR 60 UN
            // =================================================

            if (stockCreado.getCantidad()
                    .compareTo(
                            STOCK_ESPERADO
                    ) != 0) {

                System.out.println(
                        "ERROR DE CONVERSIÓN."
                );

                System.out.println(
                        "Esperado: "
                        + STOCK_ESPERADO
                        + " UN"
                );

                System.out.println(
                        "Obtenido: "
                        + stockCreado.getCantidad()
                        + " UN"
                );

                return;
            }


            System.out.println(
                    "OK: 5 CAJAS x 12 = 60 UN"
            );


            // =================================================
            // 16. INTENTAR CONFIRMAR DE NUEVO
            // =================================================

            System.out.println();
            System.out.println(
                    "11) INTENTANDO CONFIRMAR NUEVAMENTE..."
            );


            ResultadoOperacion segundaConfirmacion =
                    compraService.confirmarCompra(
                            compra.getIdCompra()
                    );


            System.out.println(
                    segundaConfirmacion.getMensaje()
            );


            if (segundaConfirmacion.isExitoso()) {

                System.out.println(
                        "ERROR CRÍTICO:"
                );

                System.out.println(
                        "La compra se confirmó dos veces."
                );

                return;
            }


            // =================================================
            // 17. STOCK DEBE SEGUIR EN 60
            // =================================================

            BigDecimal stockFinal =
                    stockDao.obtenerCantidad(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock después del segundo intento: "
                    + stockFinal
                    + " UN"
            );


            if (stockFinal.compareTo(
                    STOCK_ESPERADO
            ) != 0) {

                System.out.println(
                        "ERROR CRÍTICO:"
                );

                System.out.println(
                        "La segunda confirmación "
                        + "alteró el stock."
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
                    "TEST CAJA x12 FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "2.500 CAJAS -> RECHAZADAS"
            );

            System.out.println(
                    "5 CAJAS x 12 -> 60 UN"
            );

            System.out.println(
                    "Stock final -> 60.000 UN"
            );

            System.out.println(
                    "Doble confirmación -> RECHAZADA"
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