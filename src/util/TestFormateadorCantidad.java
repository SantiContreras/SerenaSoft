package util;

import java.math.BigDecimal;
import model.UnidadMedida;

public class TestFormateadorCantidad {

    public static void main(String[] args) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "     PRUEBA FORMATEADOR CANTIDADES"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // UNIDAD - NO PERMITE DECIMALES
        // =====================================================

        UnidadMedida unidad =
                new UnidadMedida();

        unidad.setCodigo("UN");
        unidad.setNombre("Unidad");
        unidad.setPermiteDecimales(false);
        unidad.setActivo(true);


        System.out.println();
        System.out.println(
                "1. PRODUCTOS POR UNIDAD"
        );

        System.out.println(
                "----------------------------------------"
        );


        probar(
                new BigDecimal("50.000"),
                unidad
        );

        probar(
                new BigDecimal("75.000"),
                unidad
        );

        probar(
                new BigDecimal("1250.000"),
                unidad
        );


        // =====================================================
        // KILOGRAMOS - PERMITE DECIMALES
        // =====================================================

        UnidadMedida kg =
                new UnidadMedida();

        kg.setCodigo("KG");
        kg.setNombre("Kilogramo");
        kg.setPermiteDecimales(true);
        kg.setActivo(true);


        System.out.println();
        System.out.println(
                "2. PRODUCTOS POR KILOGRAMO"
        );

        System.out.println(
                "----------------------------------------"
        );


        probar(
                new BigDecimal("12.350"),
                kg
        );

        probar(
                new BigDecimal("8.725"),
                kg
        );

        probar(
                new BigDecimal("0.500"),
                kg
        );


        // =====================================================
        // LITROS - PERMITE DECIMALES
        // =====================================================

        UnidadMedida litros =
                new UnidadMedida();

        litros.setCodigo("LT");
        litros.setNombre("Litro");
        litros.setPermiteDecimales(true);
        litros.setActivo(true);


        System.out.println();
        System.out.println(
                "3. PRODUCTOS POR LITRO"
        );

        System.out.println(
                "----------------------------------------"
        );


        probar(
                new BigDecimal("15.000"),
                litros
        );

        probar(
                new BigDecimal("15.500"),
                litros
        );

        probar(
                new BigDecimal("1250.750"),
                litros
        );


        // =====================================================
        // SIN UNIDAD
        // =====================================================

        System.out.println();
        System.out.println(
                "4. CANTIDAD SIN UNIDAD"
        );

        System.out.println(
                "----------------------------------------"
        );


        System.out.println(
                "BD: 75.000"
        );

        System.out.println(
                "Visible: "
                + FormateadorCantidad.formatear(
                        new BigDecimal("75.000"),
                        null
                )
        );


        // =====================================================
        // FIN
        // =====================================================

        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "       FIN PRUEBA FORMATEADOR"
        );

        System.out.println(
                "========================================"
        );
    }


    // =========================================================
    // MÉTODO AUXILIAR DE PRUEBA
    // =========================================================

    private static void probar(
            BigDecimal cantidad,
            UnidadMedida unidad) {

        System.out.println(
                "BD: "
                + cantidad
                + "  ->  Usuario: "
                + FormateadorCantidad.formatearConUnidad(
                        cantidad,
                        unidad
                )
        );
    }
}