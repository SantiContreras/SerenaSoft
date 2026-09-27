package util;

import model.Marca;
import services.MarcaService;
import services.ResultadoOperacion;

import java.util.List;

public class TestMarca {

    public static void main(String[] args) {

        MarcaService marcaService =
                new MarcaService();


        System.out.println(
                "========================================"
        );

        System.out.println(
                "       PRUEBA MODULO MARCA"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // 1. CREAR MARCA
        // =====================================================

        System.out.println();
        System.out.println(
                "1. CREANDO MARCA COCA-COLA..."
        );

        ResultadoOperacion resultado =
                marcaService.crearMarca(
                        "COCA-COLA",
                        "Marca de bebidas"
                );

        System.out.println(
                resultado.getMensaje()
        );


        // =====================================================
        // 2. BUSCAR MARCA
        // =====================================================

        System.out.println();
        System.out.println(
                "2. BUSCANDO MARCA COCA-COLA..."
        );

        Marca marca =
                marcaService.buscarPorNombre(
                        "COCA-COLA"
                );

        if (marca != null) {

            System.out.println(
                    "ID: " + marca.getIdMarca()
            );

            System.out.println(
                    "Nombre: " + marca.getNombre()
            );

            System.out.println(
                    "Descripcion: "
                    + marca.getDescripcion()
            );

            System.out.println(
                    "Activo: "
                    + marca.isActivo()
            );

            System.out.println(
                    "Fecha creacion: "
                    + marca.getFechaCreacion()
            );

            System.out.println(
                    "Fecha modificacion: "
                    + marca.getFechaModificacion()
            );

        } else {

            System.out.println(
                    "No se encontro la marca."
            );
        }


        // =====================================================
        // 3. LISTAR TODAS
        // =====================================================

        System.out.println();
        System.out.println(
                "3. LISTADO DE MARCAS"
        );

        List<Marca> marcas =
                marcaService.listarTodas();

        for (Marca m : marcas) {

            System.out.println(
                    m.getIdMarca()
                    + " | "
                    + m.getNombre()
                    + " | "
                    + m.getDescripcion()
                    + " | Activo: "
                    + m.isActivo()
            );
        }


        // =====================================================
        // 4. LISTAR ACTIVAS
        // =====================================================

        System.out.println();
        System.out.println(
                "4. MARCAS ACTIVAS"
        );

        List<Marca> activas =
                marcaService.listarActivas();

        for (Marca m : activas) {

            System.out.println(
                    m.getIdMarca()
                    + " | "
                    + m.getNombre()
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
                "       FIN PRUEBA MARCA"
        );

        System.out.println(
                "========================================"
        );
    }
}
