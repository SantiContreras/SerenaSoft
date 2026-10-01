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

public class TestCrearJamonCrudo {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST CREAR JAMÓN CRUDO - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );


        final String CODIGO_PRODUCTO =
                "JAM-CRUDO-001";

        final int ID_DEPOSITO = 1;


        ProductoService productoService =
                new ProductoService();

        UnidadMedidaService unidadService =
                new UnidadMedidaService();

        StockProductoDao stockDao =
                new StockProductoDao();


        try {

            // =================================================
            // 1. BUSCAR KG
            // =================================================

            System.out.println();
            System.out.println(
                    "1) BUSCANDO UNIDAD KG..."
            );


            UnidadMedida kg =
                    unidadService.buscarPorCodigo("KG");


            if (kg == null) {

                System.out.println(
                        "ERROR: No existe la unidad KG."
                );

                return;
            }


            if (!kg.isActivo()) {

                System.out.println(
                        "ERROR: KG está inactiva."
                );

                return;
            }


            if (!kg.isPermiteDecimales()) {

                System.out.println(
                        "ERROR: KG debe permitir decimales."
                );

                return;
            }


            System.out.println(
                    "KG encontrada. ID: "
                    + kg.getIdUnidad()
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
                        "El producto no existe. Creando..."
                );


                // =============================================
                // 3. CREAR PRODUCTO
                // =============================================

                ResultadoOperacion resultado =
                        productoService.crearProducto(

                                // Código
                                CODIGO_PRODUCTO,

                                // Código interno
                                "INT-JAM-001",

                                // Código de barras
                                null,

                                // Nombre
                                "Jamón Crudo",

                                // Descripción
                                "Jamón crudo vendido por kilogramo",

                                // Ubicación
                                "Sector Fiambrería",

                                // Rubro
                                null,

                                // Categoría
                                null,

                                // Marca
                                null,

                                // Unidad compra
                                kg.getIdUnidad(),

                                // Unidad venta
                                kg.getIdUnidad(),

                                // Factor conversión
                                new BigDecimal("1"),

                                // Precio compra por KG
                                new BigDecimal("12000.00"),

                                // Precio venta por KG
                                new BigDecimal("18000.00"),

                                // Margen %
                                new BigDecimal("50.00"),

                                // IVA
                                null,

                                // Stock mínimo
                                new BigDecimal("2.000"),

                                // Stock máximo
                                new BigDecimal("50.000"),

                                // Controla stock
                                true,

                                // Permite venta sin stock
                                false,

                                // Es pesable
                                true,

                                // Permite descuento
                                true,

                                // Observaciones
                                "Producto de prueba para stock por peso"
                        );


                System.out.println(
                        resultado.getMensaje()
                );


                if (!resultado.isExitoso()) {

                    System.out.println(
                            "ERROR: No se pudo crear "
                            + "Jamón Crudo."
                    );

                    return;
                }


                jamon =
                        productoService.buscarPorCodigo(
                                CODIGO_PRODUCTO
                        );

            } else {

                System.out.println(
                        "Jamón Crudo ya existe."
                );
            }


            // =================================================
            // 4. VALIDAR PRODUCTO
            // =================================================

            if (jamon == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "el producto."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "3) DATOS DEL PRODUCTO"
            );

            System.out.println(
                    "ID: "
                    + jamon.getIdProducto()
            );

            System.out.println(
                    "Código: "
                    + jamon.getCodigo()
            );

            System.out.println(
                    "Nombre: "
                    + jamon.getNombre()
            );

            System.out.println(
                    "Unidad compra: "
                    + jamon.getUnidadCompra()
            );

            System.out.println(
                    "Unidad venta: "
                    + jamon.getUnidadVenta()
            );

            System.out.println(
                    "Factor conversión: "
                    + jamon.getFactorConversion()
            );

            System.out.println(
                    "Controla stock: "
                    + jamon.isControlaStock()
            );

            System.out.println(
                    "Pesable: "
                    + jamon.isPesable()
            );

            System.out.println(
                    "Permite venta sin stock: "
                    + jamon.isPermiteVentaSinStock()
            );


            // =================================================
            // 5. VALIDACIONES
            // =================================================

            if (!jamon.isControlaStock()) {

                System.out.println(
                        "ERROR: Debe controlar stock."
                );

                return;
            }


            if (!jamon.isPesable()) {

                System.out.println(
                        "ERROR: Debe ser pesable."
                );

                return;
            }


            if (jamon.getFactorConversion()
                    .compareTo(
                            BigDecimal.ONE
                    ) != 0) {

                System.out.println(
                        "ERROR: El factor debe ser 1."
                );

                return;
            }


            if (!jamon.getUnidadCompra()
                    .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: La unidad de compra "
                        + "debe admitir decimales."
                );

                return;
            }


            if (!jamon.getUnidadVenta()
                    .isPermiteDecimales()) {

                System.out.println(
                        "ERROR: La unidad de venta "
                        + "debe admitir decimales."
                );

                return;
            }


            // =================================================
            // 6. COMPROBAR STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "4) VERIFICANDO STOCK..."
            );


            BigDecimal stock =
                    stockDao.obtenerCantidad(
                            jamon.getIdProducto(),
                            ID_DEPOSITO
                    );


            System.out.println(
                    "Stock actual: "
                    + stock
                    + " KG"
            );


            /*
             * IMPORTANTE:
             *
             * No creamos stock_producto acá.
             *
             * Queremos que la primera compra confirmada
             * sea la que cree la fila automáticamente.
             */


            if (stock.compareTo(
                    BigDecimal.ZERO
            ) != 0) {

                System.out.println(
                        "ATENCIÓN: El producto ya tiene stock."
                );

                System.out.println(
                        "Para nuestra prueba esperábamos 0."
                );

                return;
            }


            System.out.println(
                    "OK: Jamón todavía no tiene stock."
            );


            // =================================================
            // RESULTADO FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "JAMÓN CRUDO PREPARADO CORRECTAMENTE"
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
