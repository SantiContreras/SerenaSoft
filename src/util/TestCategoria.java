package util;

import model.Categoria;
import model.Rubro;

import services.CategoriaService;
import services.ResultadoOperacion;
import services.RubroService;

import java.util.List;


public class TestCategoria {

    public static void main(String[] args) {


        // =====================================================
        // SERVICES
        // =====================================================

        CategoriaService categoriaService
                = new CategoriaService();

        RubroService rubroService
                = new RubroService();


        System.out.println(
                "=================================="
        );

        System.out.println(
                "PRUEBA MODULO CATEGORIA"
        );

        System.out.println(
                "=================================="
        );


        // =====================================================
        // 1. BUSCAR RUBRO BEBIDAS
        // =====================================================

        System.out.println();

        System.out.println(
                "1. BUSCANDO RUBRO BEBIDAS..."
        );


        Rubro bebidas
                = rubroService.buscarPorNombre(
                        "BEBIDAS"
                );


        if (bebidas == null) {

            System.out.println(
                    "ERROR: No existe el rubro BEBIDAS."
            );

            System.out.println(
                    "Primero debe existir el rubro BEBIDAS."
            );

            return;
        }


        System.out.println(
                "Rubro encontrado."
        );

        System.out.println(
                "ID Rubro: "
                + bebidas.getIdRubro()
        );

        System.out.println(
                "Nombre Rubro: "
                + bebidas.getNombre()
        );


        // =====================================================
        // 2. CREAR CATEGORIA GASEOSAS
        // =====================================================

        System.out.println();

        System.out.println(
                "2. CREANDO CATEGORIA GASEOSAS..."
        );


        ResultadoOperacion resultado
                = categoriaService.crearCategoria(
                        bebidas.getIdRubro(),
                        "GASEOSAS",
                        "Gaseosas y bebidas carbonatadas"
                );


        System.out.println(
                resultado.getMensaje()
        );


        // =====================================================
        // 3. BUSCAR CATEGORIA POR NOMBRE
        // =====================================================

        System.out.println();

        System.out.println(
                "3. BUSCANDO CATEGORIA..."
        );


        Categoria gaseosas
                = categoriaService.buscarPorNombre(
                        "GASEOSAS"
                );


        if (gaseosas != null) {

            System.out.println(
                    "ID Categoria: "
                    + gaseosas.getIdCategoria()
            );

            System.out.println(
                    "Nombre: "
                    + gaseosas.getNombre()
            );

            System.out.println(
                    "Descripcion: "
                    + gaseosas.getDescripcion()
            );

            System.out.println(
                    "Activo: "
                    + gaseosas.isActivo()
            );


            // =================================================
            // MOSTRAR RUBRO RELACIONADO
            // =================================================

            if (gaseosas.getRubro() != null) {

                System.out.println(
                        "ID Rubro: "
                        + gaseosas
                                .getRubro()
                                .getIdRubro()
                );

                System.out.println(
                        "Rubro: "
                        + gaseosas
                                .getRubro()
                                .getNombre()
                );

            } else {

                System.out.println(
                        "Rubro: SIN RUBRO"
                );
            }

        } else {

            System.out.println(
                    "No se encontro la categoria."
            );
        }


        // =====================================================
        // 4. LISTAR TODAS LAS CATEGORIAS
        // =====================================================

        System.out.println();

        System.out.println(
                "4. LISTADO DE CATEGORIAS"
        );


        List<Categoria> categorias
                = categoriaService.listarTodas();


        for (Categoria categoria : categorias) {

            String nombreRubro;


            if (categoria.getRubro() != null) {

                nombreRubro
                        = categoria
                                .getRubro()
                                .getNombre();

            } else {

                nombreRubro
                        = "SIN RUBRO";
            }


            System.out.println(
                    categoria.getIdCategoria()
                    + " | "
                    + categoria.getNombre()
                    + " | Rubro: "
                    + nombreRubro
                    + " | Activo: "
                    + categoria.isActivo()
            );
        }


        // =====================================================
        // 5. LISTAR CATEGORIAS DEL RUBRO BEBIDAS
        // =====================================================

        System.out.println();

        System.out.println(
                "5. CATEGORIAS DEL RUBRO BEBIDAS"
        );


        List<Categoria> categoriasBebidas
                = categoriaService.listarPorRubro(
                        bebidas.getIdRubro()
                );


        for (Categoria categoria : categoriasBebidas) {

            System.out.println(
                    categoria.getIdCategoria()
                    + " | "
                    + categoria.getNombre()
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