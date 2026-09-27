package util;

import model.UnidadMedida;
import services.ResultadoOperacion;
import services.UnidadMedidaService;

import java.util.List;

public class TestUnidadMedida {

    public static void main(String[] args) {

        UnidadMedidaService service =
                new UnidadMedidaService();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "     PRUEBA MODULO UNIDAD DE MEDIDA"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // 1. CREAR UNIDAD
        // =====================================================

        System.out.println();
        System.out.println(
                "1. CREANDO UNIDAD..."
        );

        ResultadoOperacion resultadoUnidad =
                service.crearUnidad(
                        "UN",
                        "Unidad",
                        false
                );

        System.out.println(
                resultadoUnidad.getMensaje()
        );


        // =====================================================
        // 2. CREAR KILOGRAMO
        // =====================================================

        System.out.println();
        System.out.println(
                "2. CREANDO KILOGRAMO..."
        );

        ResultadoOperacion resultadoKg =
                service.crearUnidad(
                        "KG",
                        "Kilogramo",
                        true
                );

        System.out.println(
                resultadoKg.getMensaje()
        );


        // =====================================================
        // 3. BUSCAR KG
        // =====================================================

        System.out.println();
        System.out.println(
                "3. BUSCANDO KG..."
        );

        UnidadMedida kg =
                service.buscarPorCodigo("KG");

        if (kg != null) {

            System.out.println(
                    "ID: " + kg.getIdUnidad()
            );

            System.out.println(
                    "Código: " + kg.getCodigo()
            );

            System.out.println(
                    "Nombre: " + kg.getNombre()
            );

            System.out.println(
                    "Permite decimales: "
                    + kg.isPermiteDecimales()
            );

            System.out.println(
                    "Activo: "
                    + kg.isActivo()
            );
        }


        // =====================================================
        // 4. LISTAR TODAS
        // =====================================================

        System.out.println();
        System.out.println(
                "4. LISTADO DE UNIDADES"
        );

        List<UnidadMedida> unidades =
                service.listarTodas();

        for (UnidadMedida u : unidades) {

            System.out.println(
                    u.getIdUnidad()
                    + " | "
                    + u.getCodigo()
                    + " | "
                    + u.getNombre()
                    + " | Decimales: "
                    + u.isPermiteDecimales()
                    + " | Activo: "
                    + u.isActivo()
            );
        }


        // =====================================================
        // FIN
        // =====================================================

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "        FIN PRUEBA UNIDAD"
        );

        System.out.println(
                "========================================"
        );
    }
}
