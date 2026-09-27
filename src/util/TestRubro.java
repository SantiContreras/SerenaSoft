package util;

import model.Rubro;
import services.ResultadoOperacion;
import services.RubroService;

import java.util.List;


public class TestRubro {

    public static void main(String[] args) {


        // =====================================================
        // SERVICE
        // =====================================================

        RubroService rubroService
                = new RubroService();


        System.out.println(
                "=================================="
        );

        System.out.println(
                "PRUEBA MODULO RUBRO"
        );

        System.out.println(
                "=================================="
        );


        // =====================================================
        // 1. CREAR RUBRO
        // =====================================================

        System.out.println();
        System.out.println(
                "1. CREANDO RUBRO..."
        );


        ResultadoOperacion resultado
                = rubroService.crearRubro(
                        "BEBIDAS",
                        "Bebidas, gaseosas, aguas y jugos"
                );


        System.out.println(
                resultado.getMensaje()
        );


        // =====================================================
        // 2. BUSCAR RUBRO
        // =====================================================

        System.out.println();
        System.out.println(
                "2. BUSCANDO RUBRO..."
        );


        Rubro bebidas
                = rubroService.buscarPorNombre(
                        "BEBIDAS"
                );


        if (bebidas != null) {

            System.out.println(
                    "ID: "
                    + bebidas.getIdRubro()
            );

            System.out.println(
                    "Nombre: "
                    + bebidas.getNombre()
            );

            System.out.println(
                    "Descripcion: "
                    + bebidas.getDescripcion()
            );

            System.out.println(
                    "Activo: "
                    + bebidas.isActivo()
            );

        } else {

            System.out.println(
                    "No se encontro el rubro."
            );
        }


        // =====================================================
        // 3. LISTAR RUBROS
        // =====================================================

        System.out.println();
        System.out.println(
                "3. LISTADO DE RUBROS"
        );


        List<Rubro> rubros
                = rubroService.listarTodos();


        for (Rubro rubro : rubros) {

            System.out.println(
                    rubro.getIdRubro()
                    + " | "
                    + rubro.getNombre()
                    + " | "
                    + rubro.getDescripcion()
                    + " | Activo: "
                    + rubro.isActivo()
            );
        }


        // =====================================================
        // FIN
        // =====================================================

        System.out.println();

        System.out.println(
                "=================================="
        );

        System.out.println(
                "FIN DE LA PRUEBA"
        );

        System.out.println(
                "=================================="
        );
    }
}