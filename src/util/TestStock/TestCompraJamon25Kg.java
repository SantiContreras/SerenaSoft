/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

public class TestCompraJamon25Kg {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST COMPRA JAMÓN CRUDO 25 KG - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );


        // =====================================================
        // DATOS DE LA PRUEBA
        // =====================================================

        final String CODIGO_PRODUCTO =
                "JAM-CRUDO-001";

        final int ID_PROVEEDOR = 1;

        final int ID_DEPOSITO = 1;

        final BigDecimal CANTIDAD_COMPRA =
                new BigDecimal("25.000");

        final BigDecimal COSTO_KG =
                new BigDecimal("12000.00");


        // =====================================================
        // SERVICIOS / DAO
        // =====================================================

        CompraService compraService =
                new CompraService();

        ProductoService productoService =
                new ProductoService();

        ProveedorDao proveedorDao =
                new ProveedorDao();

        DepositoDao depositoDao =
                new DepositoDao();

        StockProductoDao stockDao =
                new StockProductoDao();


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
            // 2. BUSCAR JAMÓN CRUDO
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
                        "ERROR: No existe el producto "
                        + CODIGO_PRODUCTO
                );

                System.out.println(
                        "Ejecutá primero "
                        + "TestCrearJamonCrudo."
                );

                return;
            }


            System.out.println(
                    "Producto: "
                    + jamon.getNombre()
            );

            System.out.println(
                    "ID producto: "
                    + jamon.getIdProducto()
            );

            System.out.println(
                    "Unidad compra: "
                    + jamon.getUnidadCompra()
            );

            System.out.println(
                    "Factor conversión: "
                    + jamon.getFactorConversion()
            );


            // =================================================
            // 3. VERIFICAR CONFIGURACIÓN DEL PRODUCTO
            // =================================================

            if (!jamon.isActivo()) {

                System.out.println(
                        "ERROR: El producto está inactivo."
                );

                return;
            }


            if (!jamon.isControlaStock()) {

                System.out.println(
                        "ERROR: Jamón Crudo debe "
                        + "controlar stock."
                );

                return;
            }


            if (jamon.getUnidadCompra() == null
                    || !jamon.getUnidadCompra()
                            .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: La unidad de compra "
                        + "debe admitir decimales."
                );

                return;
            }


            if (jamon.getFactorConversion()
                    .compareTo(BigDecimal.ONE) != 0) {

                System.out.println(
                        "ERROR: Para esta prueba el "
                        + "factor de conversión debe ser 1."
                );

                return;
            }


            // =================================================
            // 4. VERIFICAR QUE NO EXISTA STOCK_PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "3) VERIFICANDO STOCK_PRODUCTO INICIAL..."
            );


            StockProducto stockExistente =
                    stockDao.buscarPorProductoDeposito(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            if (stockExistente != null) {

                System.out.println(
                        "ERROR: Ya existe un registro "
                        + "stock_producto para Jamón Crudo."
                );

                System.out.println(
                        "Stock encontrado: "
                        + stockExistente.getCantidad()
                        + " KG"
                );

                System.out.println(
                        "Esta prueba necesita comenzar "
                        + "sin registro de stock."
                );

                return;
            }


            System.out.println(
                    "OK: No existe stock_producto."
            );

            System.out.println(
                    "La primera compra deberá crearlo."
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
                        "ERROR: No existe el proveedor."
                );

                return;
            }


            if (deposito == null) {

                System.out.println(
                        "ERROR: No existe el depósito."
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
                    "TEST-JAMON-25KG"
            );

            compra.setOrigenCarga(
                    "MANUAL"
            );

            compra.setObservaciones(
                    "Prueba compra 25 KG de Jamón Crudo"
            );


            ResultadoOperacion crearCompra =
                    compraService.crearCompra(
                            compra
                    );


            System.out.println(
                    crearCompra.getMensaje()
            );


            if (!crearCompra.isExitoso()) {
                return;
            }


            System.out.println(
                    "Compra creada: #"
                    + compra.getIdCompra()
            );


            // =================================================
            // 7. AGREGAR 25 KG
            // =================================================

            System.out.println();
            System.out.println(
                    "5) AGREGANDO 25.000 KG..."
            );


            ResultadoOperacion agregar =
                    compraService.agregarProducto(
                            compra.getIdCompra(),
                            jamon.getIdProducto(),
                            CANTIDAD_COMPRA,
                            COSTO_KG
                    );


            System.out.println(
                    agregar.getMensaje()
            );


            if (!agregar.isExitoso()) {
                return;
            }


            // =================================================
            // 8. COMPROBAR QUE BORRADOR NO CREÓ STOCK
            // =================================================

            System.out.println();
            System.out.println(
                    "6) VERIFICANDO STOCK EN BORRADOR..."
            );


            StockProducto stockEnBorrador =
                    stockDao.buscarPorProductoDeposito(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            if (stockEnBorrador != null) {

                System.out.println(
                        "ERROR: La compra BORRADOR "
                        + "creó stock_producto."
                );

                return;
            }


            System.out.println(
                    "OK: BORRADOR no creó stock_producto."
            );


            BigDecimal cantidadAntes =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Cantidad antes de confirmar: "
                    + cantidadAntes
                    + " KG"
            );


            if (cantidadAntes.compareTo(
                    BigDecimal.ZERO
            ) != 0) {

                System.out.println(
                        "ERROR: El stock debería ser 0."
                );

                return;
            }


            // =================================================
            // 9. CONSULTAR TOTALES DE LA COMPRA
            // =================================================

            Compra compraBorrador =
                    compraService.buscarPorId(
                            compra.getIdCompra()
                    );


            if (compraBorrador == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "la compra."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "Compra #: "
                    + compraBorrador.getIdCompra()
            );

            System.out.println(
                    "Estado: "
                    + compraBorrador.getEstado()
            );

            System.out.println(
                    "Cantidad detalles: "
                    + compraBorrador.cantidadDetalles()
            );

            System.out.println(
                    "Subtotal: $"
                    + compraBorrador.getSubtotal()
            );

            System.out.println(
                    "Total: $"
                    + compraBorrador.getTotal()
            );


            // 25 KG × $12.000 = $300.000
            BigDecimal totalEsperado =
                    new BigDecimal("300000.00");


            if (compraBorrador.getTotal()
                    .compareTo(totalEsperado) != 0) {

                System.out.println(
                        "ERROR: Total incorrecto."
                );

                System.out.println(
                        "Esperado: $"
                        + totalEsperado
                );

                return;
            }


            System.out.println(
                    "OK: 25 KG x $12.000 = $300.000"
            );


            // =================================================
            // 10. CONFIRMAR COMPRA
            // =================================================

            System.out.println();
            System.out.println(
                    "7) CONFIRMANDO COMPRA..."
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
                        "ERROR: No se pudo confirmar "
                        + "la compra."
                );

                return;
            }


            // =================================================
            // 11. VERIFICAR ESTADO
            // =================================================

            Compra compraConfirmada =
                    compraService.buscarPorId(
                            compra.getIdCompra()
                    );


            if (compraConfirmada == null
                    || !compraConfirmada
                            .estaConfirmada()) {

                System.out.println(
                        "ERROR: La compra no quedó "
                        + "CONFIRMADA."
                );

                return;
            }


            System.out.println(
                    "OK: Compra CONFIRMADA."
            );


            // =================================================
            // 12. COMPROBAR CREACIÓN DE STOCK_PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "8) VERIFICANDO STOCK_PRODUCTO..."
            );


            StockProducto stockCreado =
                    stockDao.buscarPorProductoDeposito(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            if (stockCreado == null) {

                System.out.println(
                        "ERROR CRÍTICO:"
                );

                System.out.println(
                        "La compra fue confirmada pero "
                        + "no se creó stock_producto."
                );

                return;
            }


            System.out.println(
                    "OK: stock_producto creado."
            );

            System.out.println(
                    "ID stock: "
                    + stockCreado.getIdStock()
            );

            System.out.println(
                    "Cantidad: "
                    + stockCreado.getCantidad()
                    + " KG"
            );


            // =================================================
            // 13. VALIDAR 25.000 KG EXACTOS
            // =================================================

            BigDecimal stockEsperado =
                    new BigDecimal("25.000");


            if (stockCreado.getCantidad()
                    .compareTo(stockEsperado) != 0) {

                System.out.println();
                System.out.println(
                        "ERROR DE STOCK."
                );

                System.out.println(
                        "Esperado: "
                        + stockEsperado
                        + " KG"
                );

                System.out.println(
                        "Obtenido: "
                        + stockCreado.getCantidad()
                        + " KG"
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "Stock esperado: 25.000 KG"
            );

            System.out.println(
                    "Stock obtenido: "
                    + stockCreado.getCantidad()
                    + " KG"
            );

            System.out.println(
                    "OK: STOCK EXACTO."
            );


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST COMPRA JAMÓN FINALIZADO CORRECTAMENTE"
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
