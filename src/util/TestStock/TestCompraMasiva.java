package util.TestStock;

import Dao.DepositoDao;
import Dao.ProveedorDao;
import Dao.StockProductoDao;

import model.Compra;
import model.Deposito;
import model.Proveedor;

import services.CompraService;
import services.LoginService;
import services.ResultadoLogin;
import services.ResultadoOperacion;

import java.math.BigDecimal;

public class TestCompraMasiva {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST COMPRA MASIVA - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );

        final int ID_PRODUCTO = 1;   // Coca-Cola
        final int ID_PROVEEDOR = 1;
        final int ID_DEPOSITO = 1;

        final BigDecimal CANTIDAD_COMPRA =
                new BigDecimal("5000");

        final BigDecimal COSTO_UNITARIO =
                new BigDecimal("850.00");


        CompraService compraService =
                new CompraService();

        StockProductoDao stockDao =
                new StockProductoDao();

        ProveedorDao proveedorDao =
                new ProveedorDao();

        DepositoDao depositoDao =
                new DepositoDao();


        try {

            // =================================================
            // 1. LOGIN
            // =================================================

            System.out.println();
            System.out.println("1) INICIANDO SESIÓN...");

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
            // 2. STOCK INICIAL
            // =================================================

            BigDecimal stockInicial =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );

            System.out.println();
            System.out.println(
                    "2) STOCK INICIAL: "
                    + stockInicial
            );


            // =================================================
            // 3. CREAR COMPRA
            // =================================================

            Proveedor proveedor =
                    proveedorDao.buscarPorId(
                            ID_PROVEEDOR
                    );

            Deposito deposito =
                    depositoDao.buscarPorId(
                            ID_DEPOSITO
                    );


            Compra compra =
                    new Compra();

            compra.setProveedor(proveedor);
            compra.setDeposito(deposito);

            compra.setNumeroComprobante(
                    "TEST-MASIVA-5000"
            );

            compra.setOrigenCarga(
                    "MANUAL"
            );

            compra.setObservaciones(
                    "Prueba de compra masiva de 5000 unidades"
            );


            ResultadoOperacion crear =
                    compraService.crearCompra(
                            compra
                    );

            System.out.println();
            System.out.println(
                    "3) " + crear.getMensaje()
            );

            if (!crear.isExitoso()) {
                return;
            }


            // =================================================
            // 4. AGREGAR 5000 UNIDADES
            // =================================================

            ResultadoOperacion agregar =
                    compraService.agregarProducto(
                            compra.getIdCompra(),
                            ID_PRODUCTO,
                            CANTIDAD_COMPRA,
                            COSTO_UNITARIO
                    );


            System.out.println();
            System.out.println(
                    "4) " + agregar.getMensaje()
            );

            if (!agregar.isExitoso()) {
                return;
            }


            // =================================================
            // 5. VERIFICAR QUE BORRADOR NO CAMBIÓ STOCK
            // =================================================

            BigDecimal stockBorrador =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            System.out.println();
            System.out.println(
                    "5) STOCK EN BORRADOR: "
                    + stockBorrador
            );


            if (stockBorrador.compareTo(
                    stockInicial
            ) != 0) {

                System.out.println(
                        "ERROR: BORRADOR modificó stock."
                );

                return;
            }


            // =================================================
            // 6. CONFIRMAR
            // =================================================

            ResultadoOperacion confirmar =
                    compraService.confirmarCompra(
                            compra.getIdCompra()
                    );


            System.out.println();
            System.out.println(
                    "6) " + confirmar.getMensaje()
            );

            if (!confirmar.isExitoso()) {
                return;
            }


            // =================================================
            // 7. COMPROBAR STOCK
            // =================================================

            BigDecimal stockFinal =
                    stockDao.obtenerCantidad(
                            ID_PRODUCTO,
                            ID_DEPOSITO
                    );


            BigDecimal esperado =
                    stockInicial.add(
                            new BigDecimal("5000.000")
                    );


            System.out.println();
            System.out.println(
                    "Stock inicial:  "
                    + stockInicial
            );

            System.out.println(
                    "Compra:        +5000.000"
            );

            System.out.println(
                    "Esperado:       "
                    + esperado
            );

            System.out.println(
                    "Stock final:    "
                    + stockFinal
            );


            if (stockFinal.compareTo(
                    esperado
            ) != 0) {

                System.out.println(
                        "ERROR: STOCK FINAL INCORRECTO."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "OK: COMPRA MASIVA CORRECTA."
            );

            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "TEST FINALIZADO CORRECTAMENTE"
            );
            System.out.println(
                    "=============================================="
            );


        } catch (Exception ex) {

            System.out.println(
                    "ERROR: "
                    + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }
}