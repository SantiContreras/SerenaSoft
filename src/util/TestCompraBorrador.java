package util;

import Dao.StockProductoDao;

import model.Compra;
import model.CompraDetalle;
import model.Deposito;
import model.Producto;
import model.Proveedor;

import services.CompraService;
import services.ResultadoOperacion;

import java.math.BigDecimal;
import java.util.List;

import services.LoginService;
import services.ResultadoLogin;

public class TestCompraBorrador {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "TEST COMPRA BORRADOR - SERENA SOFT"
        );

        System.out.println(
                "=============================================="
        );

        CompraService compraService
                = new CompraService();

        StockProductoDao stockProductoDao
                = new StockProductoDao();

        // =========================================================
// INICIAR SESIÓN DE PRUEBA
// =========================================================
        System.out.println();
        System.out.println(
                "INICIANDO SESIÓN DE PRUEBA..."
        );

        LoginService loginService
                = new LoginService();

        ResultadoLogin resultadoLogin
                = loginService.login(
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

        System.out.println(
                "Rol: "
                + resultadoLogin
                        .getUsuario()
                        .getRol()
                        .getNombre()
        );

        // =====================================================
        // DATOS QUE YA TENEMOS EN LA BASE
        // =====================================================
        int idProveedor = 1;
        int idProducto = 1;
        int idDeposito = 1;

        try {

            // =================================================
            // 1. CONSULTAR STOCK INICIAL
            // =================================================
            System.out.println();
            System.out.println(
                    "1) CONSULTANDO STOCK INICIAL..."
            );

            BigDecimal stockInicial
                    = stockProductoDao.obtenerCantidad(
                            idProducto,
                            idDeposito
                    );

            System.out.println(
                    "Stock inicial: "
                    + stockInicial
            );

            // =================================================
            // 2. PREPARAR COMPRA
            // =================================================
            System.out.println();
            System.out.println(
                    "2) CREANDO COMPRA BORRADOR..."
            );

            Proveedor proveedor
                    = new Proveedor();

            proveedor.setIdProveedor(
                    idProveedor
            );

            Deposito deposito
                    = new Deposito();

            deposito.setIdDeposito(
                    idDeposito
            );

            Compra compra
                    = new Compra();

            compra.setProveedor(
                    proveedor
            );

            compra.setDeposito(
                    deposito
            );

            compra.setNumeroComprobante(
                    "A 0001-00000100"
            );

            compra.setOrigenCarga(
                    "MANUAL"
            );

            compra.setObservaciones(
                    "Compra de prueba - Serena Soft"
            );

            ResultadoOperacion resultadoCrear
                    = compraService.crearCompra(
                            compra
                    );

            System.out.println(
                    resultadoCrear.getMensaje()
            );

            if (!resultadoCrear.isExitoso()) {

                System.out.println();
                System.out.println(
                        "No se pudo continuar con el test."
                );

                return;
            }

            long idCompra
                    = compra.getIdCompra();

            System.out.println(
                    "ID compra: "
                    + idCompra
            );

            // =================================================
            // 3. AGREGAR COCA-COLA
            //
            // 10 UN x $850 = $8.500
            // =================================================
            System.out.println();
            System.out.println(
                    "3) AGREGANDO PRODUCTO..."
            );

            ResultadoOperacion resultadoAgregar
                    = compraService.agregarProducto(
                            idCompra,
                            idProducto,
                            "UN",
                            BigDecimal.ONE,
                            new BigDecimal("10"),
                            new BigDecimal("850")
                    );

            System.out.println(
                    resultadoAgregar.getMensaje()
            );

            if (!resultadoAgregar.isExitoso()) {

                return;
            }

            // =================================================
            // 4. CONSULTAR COMPRA
            // =================================================
            System.out.println();
            System.out.println(
                    "4) CONSULTANDO COMPRA..."
            );

            Compra compraGuardada
                    = compraService.buscarPorId(
                            idCompra
                    );

            if (compraGuardada == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar la compra."
                );

                return;
            }

            System.out.println(
                    "Compra #: "
                    + compraGuardada.getIdCompra()
            );

            System.out.println(
                    "Proveedor: "
                    + compraGuardada.getProveedor()
            );

            System.out.println(
                    "Depósito: "
                    + compraGuardada.getDeposito()
            );

            System.out.println(
                    "Comprobante: "
                    + compraGuardada.getNumeroComprobante()
            );

            System.out.println(
                    "Origen: "
                    + compraGuardada.getOrigenCarga()
            );

            System.out.println(
                    "Estado: "
                    + compraGuardada.getEstado()
            );

            System.out.println(
                    "Subtotal: $"
                    + compraGuardada.getSubtotal()
            );

            System.out.println(
                    "Descuento: $"
                    + compraGuardada.getDescuento()
            );

            System.out.println(
                    "Total: $"
                    + compraGuardada.getTotal()
            );

            // =================================================
            // 5. MOSTRAR DETALLES
            // =================================================
            System.out.println();
            System.out.println(
                    "5) DETALLE DE COMPRA..."
            );

            List<CompraDetalle> detalles
                    = compraGuardada.getDetalles();

            System.out.println(
                    "Cantidad de detalles: "
                    + detalles.size()
            );

            for (CompraDetalle detalle
                    : detalles) {

                Producto producto
                        = detalle.getProducto();

                System.out.println(
                        "----------------------------------------------"
                );

                System.out.println(
                        "Producto: "
                        + producto.getNombre()
                );

                System.out.println(
                        "Cantidad: "
                        + detalle.getCantidad()
                );

                System.out.println(
                        "Costo unitario: $"
                        + detalle.getCostoUnitario()
                );

                System.out.println(
                        "Subtotal: $"
                        + detalle.getSubtotal()
                );
            }

            // =================================================
            // 6. VALIDAR ESTADO
            // =================================================
            System.out.println();
            System.out.println(
                    "6) VALIDANDO ESTADO..."
            );

            if (!compraGuardada.estaEnBorrador()) {

                System.out.println(
                        "ERROR: La compra debería estar "
                        + "en BORRADOR."
                );

                return;
            }

            System.out.println(
                    "OK: compra permanece en BORRADOR."
            );

            // =================================================
            // 7. VALIDAR TOTAL
            // =================================================
            System.out.println();
            System.out.println(
                    "7) VALIDANDO TOTAL..."
            );

            BigDecimal totalEsperado
                    = new BigDecimal(
                            "8500.00"
                    );

            if (compraGuardada
                    .getTotal()
                    .compareTo(
                            totalEsperado
                    ) != 0) {

                System.out.println(
                        "ERROR: Total esperado $"
                        + totalEsperado
                        + " pero se obtuvo $"
                        + compraGuardada.getTotal()
                );

                return;
            }

            System.out.println(
                    "OK: total correcto = $"
                    + compraGuardada.getTotal()
            );

            // =================================================
            // 8. CONSULTAR STOCK DESPUÉS DEL BORRADOR
            // =================================================
            System.out.println();
            System.out.println(
                    "8) CONSULTANDO STOCK..."
            );

            BigDecimal stockFinal
                    = stockProductoDao.obtenerCantidad(
                            idProducto,
                            idDeposito
                    );

            System.out.println(
                    "Stock antes:   "
                    + stockInicial
            );

            System.out.println(
                    "Stock después: "
                    + stockFinal
            );

            // =================================================
            // 9. COMPROBAR QUE NO CAMBIÓ
            // =================================================
            if (stockInicial.compareTo(
                    stockFinal
            ) != 0) {

                System.out.println();
                System.out.println(
                        "ERROR: Una compra BORRADOR "
                        + "modificó el stock."
                );

                return;
            }

            System.out.println();
            System.out.println(
                    "OK: BORRADOR NO MODIFICÓ EL STOCK."
            );

            // =================================================
            // RESULTADO FINAL
            // =================================================
            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST COMPRA BORRADOR FINALIZADO CORRECTAMENTE"
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
