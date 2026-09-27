package util;

import model.TipoIva;
import services.ResultadoOperacion;
import services.TipoIvaService;

import java.math.BigDecimal;
import java.util.List;

public class TestTipoIva {

    public static void main(String[] args) {

        TipoIvaService service =
                new TipoIvaService();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "         PRUEBA MODULO TIPO IVA"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // 1. CREAR IVA 21%
        // =====================================================

        System.out.println();
        System.out.println(
                "1. CREANDO IVA 21%..."
        );

        ResultadoOperacion resultado21 =
                service.crearTipoIva(
                        "IVA21",
                        "IVA 21%",
                        new BigDecimal("21.000")
                );

        System.out.println(
                resultado21.getMensaje()
        );


        // =====================================================
        // 2. CREAR IVA 10.5%
        // =====================================================

        System.out.println();
        System.out.println(
                "2. CREANDO IVA 10.5%..."
        );

        ResultadoOperacion resultado105 =
                service.crearTipoIva(
                        "IVA105",
                        "IVA 10,5%",
                        new BigDecimal("10.500")
                );

        System.out.println(
                resultado105.getMensaje()
        );


        // =====================================================
        // 3. BUSCAR IVA 21
        // =====================================================

        System.out.println();
        System.out.println(
                "3. BUSCANDO IVA21..."
        );

        TipoIva iva21 =
                service.buscarPorCodigo("IVA21");

        if (iva21 != null) {

            System.out.println(
                    "ID: " + iva21.getIdIva()
            );

            System.out.println(
                    "Código: " + iva21.getCodigo()
            );

            System.out.println(
                    "Nombre: " + iva21.getNombre()
            );

            System.out.println(
                    "Porcentaje: "
                    + iva21.getPorcentaje()
                    + "%"
            );

            System.out.println(
                    "Activo: "
                    + iva21.isActivo()
            );
        }


        // =====================================================
        // 4. LISTAR TODOS
        // =====================================================

        System.out.println();
        System.out.println(
                "4. LISTADO DE TIPOS DE IVA"
        );

        List<TipoIva> tipos =
                service.listarTodos();

        for (TipoIva iva : tipos) {

            System.out.println(
                    iva.getIdIva()
                    + " | "
                    + iva.getCodigo()
                    + " | "
                    + iva.getNombre()
                    + " | "
                    + iva.getPorcentaje()
                    + "%"
                    + " | Activo: "
                    + iva.isActivo()
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
                "          FIN PRUEBA TIPO IVA"
        );

        System.out.println(
                "========================================"
        );
    }
}