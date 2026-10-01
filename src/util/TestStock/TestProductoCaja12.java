/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.TestStock;


import Dao.StockProductoDao;

import model.Producto;
import model.UnidadMedida;

import services.ProductoService;
import services.ResultadoOperacion;
import services.UnidadMedidaService;

import java.math.BigDecimal;

public class TestProductoCaja12 {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST PRODUCTO CAJA x12 - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );

        final String CODIGO_PRODUCTO =
                "AGU-0001";

        final int ID_DEPOSITO = 1;


        UnidadMedidaService unidadService =
                new UnidadMedidaService();

        ProductoService productoService =
                new ProductoService();

        StockProductoDao stockDao =
                new StockProductoDao();


        try {

            // =================================================
            // 1. BUSCAR / CREAR UNIDAD CAJA
            // =================================================

            System.out.println();
            System.out.println(
                    "1) VERIFICANDO UNIDAD CAJA..."
            );


            UnidadMedida caja =
                    unidadService.buscarPorCodigo(
                            "CAJA"
                    );


            if (caja == null) {

                System.out.println(
                        "CAJA no existe. Creando..."
                );


                ResultadoOperacion resultadoCaja =
                        unidadService.crearUnidad(
                                "CAJA",
                                "Caja",
                                false
                        );


                System.out.println(
                        resultadoCaja.getMensaje()
                );


                if (!resultadoCaja.isExitoso()) {
                    return;
                }


                caja =
                        unidadService.buscarPorCodigo(
                                "CAJA"
                        );
            }


            if (caja == null) {

                System.out.println(
                        "ERROR: No se pudo obtener CAJA."
                );

                return;
            }


            if (!caja.isActivo()) {

                System.out.println(
                        "ERROR: CAJA está inactiva."
                );

                return;
            }


            if (caja.isPermiteDecimales()) {

                System.out.println(
                        "ERROR: CAJA no debe permitir decimales."
                );

                return;
            }


            System.out.println(
                    "CAJA ID: "
                    + caja.getIdUnidad()
            );

            System.out.println(
                    "Permite decimales: "
                    + caja.isPermiteDecimales()
            );


            // =================================================
            // 2. BUSCAR UNIDAD UN
            // =================================================

            System.out.println();
            System.out.println(
                    "2) VERIFICANDO UNIDAD UN..."
            );


            UnidadMedida unidad =
                    unidadService.buscarPorCodigo(
                            "UN"
                    );


            if (unidad == null) {

                System.out.println(
                        "ERROR: No existe UN."
                );

                return;
            }


            if (!unidad.isActivo()) {

                System.out.println(
                        "ERROR: UN está inactiva."
                );

                return;
            }


            System.out.println(
                    "UN ID: "
                    + unidad.getIdUnidad()
            );


            // =================================================
            // 3. BUSCAR PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "3) BUSCANDO PRODUCTO..."
            );


            Producto producto =
                    productoService.buscarPorCodigo(
                            CODIGO_PRODUCTO
                    );


            if (producto == null) {

                System.out.println(
                        "Agua Mineral no existe. Creando..."
                );


                // =============================================
                // CREAR PRODUCTO
                // =============================================

                ResultadoOperacion resultado =
                        productoService.crearProducto(

                                // Código
                                CODIGO_PRODUCTO,

                                // Código interno
                                "INT-AGU-0001",

                                // Código de barras
                                null,

                                // Nombre
                                "Agua Mineral 500 ml",

                                // Descripción
                                "Producto de prueba comprado "
                                + "por caja de 12 unidades",

                                // Ubicación
                                "Depósito general",

                                // Rubro
                                null,

                                // Categoría
                                null,

                                // Marca
                                null,

                                // Unidad compra
                                caja.getIdUnidad(),

                                // Unidad venta
                                unidad.getIdUnidad(),

                                // Factor:
                                // 1 CAJA = 12 UN
                                new BigDecimal("12"),

                                // Precio compra POR CAJA
                                new BigDecimal("6000.00"),

                                // Precio venta POR UNIDAD
                                new BigDecimal("800.00"),

                                // Margen
                                new BigDecimal("60.00"),

                                // IVA
                                null,

                                // Stock mínimo en UN
                                new BigDecimal("24.000"),

                                // Stock máximo en UN
                                new BigDecimal("600.000"),

                                // Controla stock
                                true,

                                // Permite venta sin stock
                                false,

                                // Pesable
                                false,

                                // Permite descuento
                                true,

                                // Observaciones
                                "Compra CAJA x12 / venta UN"
                        );


                System.out.println(
                        resultado.getMensaje()
                );


                if (!resultado.isExitoso()) {
                    return;
                }


                producto =
                        productoService.buscarPorCodigo(
                                CODIGO_PRODUCTO
                        );

            } else {

                System.out.println(
                        "El producto ya existe."
                );
            }


            // =================================================
            // 4. VALIDAR PRODUCTO
            // =================================================

            if (producto == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "el producto."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "4) CONFIGURACIÓN DEL PRODUCTO"
            );

            System.out.println(
                    "ID: "
                    + producto.getIdProducto()
            );

            System.out.println(
                    "Código: "
                    + producto.getCodigo()
            );

            System.out.println(
                    "Producto: "
                    + producto.getNombre()
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
            // 5. VALIDAR CAJA -> UN
            // =================================================

            if (producto.getUnidadCompra() == null
                    || !"CAJA".equalsIgnoreCase(
                            producto
                                    .getUnidadCompra()
                                    .getCodigo()
                    )) {

                System.out.println(
                        "ERROR: La unidad de compra "
                        + "debe ser CAJA."
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
                        "ERROR: La unidad de venta "
                        + "debe ser UN."
                );

                return;
            }


            if (producto.getFactorConversion()
                    .compareTo(
                            new BigDecimal("12")
                    ) != 0) {

                System.out.println(
                        "ERROR: El factor debe ser 12."
                );

                return;
            }


            if (producto.getUnidadCompra()
                    .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: CAJA no debe admitir "
                        + "cantidades decimales."
                );

                return;
            }


            if (!producto.isControlaStock()) {

                System.out.println(
                        "ERROR: El producto debe "
                        + "controlar stock."
                );

                return;
            }


            // =================================================
            // 6. VERIFICAR STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "5) VERIFICANDO STOCK INICIAL..."
            );


            BigDecimal stock =
                    stockDao.obtenerCantidad(
                            producto.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock actual: "
                    + stock
                    + " UN"
            );


            if (stock.compareTo(
                    BigDecimal.ZERO
            ) != 0) {

                System.out.println(
                        "ATENCIÓN: Este producto "
                        + "ya tiene stock."
                );

                return;
            }


            System.out.println(
                    "OK: Stock inicial = 0 UN"
            );


            // =================================================
            // RESULTADO
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "PRODUCTO CAJA x12 PREPARADO CORRECTAMENTE"
            );

            System.out.println(
                    "1 CAJA = 12 UN"
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
