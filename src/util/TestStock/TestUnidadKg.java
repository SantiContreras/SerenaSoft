package util.TestStock;

import model.UnidadMedida;

import services.ResultadoOperacion;
import services.UnidadMedidaService;

public class TestUnidadKg {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );
        System.out.println(
                "TEST UNIDAD KG - SERENA SOFT"
        );
        System.out.println(
                "=============================================="
        );


        UnidadMedidaService unidadService =
                new UnidadMedidaService();


        try {

            // =================================================
            // 1. VERIFICAR SI KG YA EXISTE
            // =================================================

            System.out.println();
            System.out.println(
                    "1) BUSCANDO UNIDAD KG..."
            );


            UnidadMedida kg =
                    unidadService.buscarPorCodigo("KG");


            if (kg == null) {

                // =============================================
                // 2. CREAR KG
                // =============================================

                System.out.println(
                        "KG no existe. Creando..."
                );


                ResultadoOperacion resultado =
                        unidadService.crearUnidad(
                                "KG",
                                "Kilogramo",
                                true
                        );


                System.out.println(
                        resultado.getMensaje()
                );


                if (!resultado.isExitoso()) {

                    System.out.println(
                            "ERROR: No se pudo crear KG."
                    );

                    return;
                }


                kg = unidadService.buscarPorCodigo(
                        "KG"
                );

            } else {

                System.out.println(
                        "KG ya existe."
                );
            }


            // =================================================
            // 3. VALIDAR DATOS
            // =================================================

            if (kg == null) {

                System.out.println(
                        "ERROR: KG no pudo recuperarse."
                );

                return;
            }


            System.out.println();
            System.out.println(
                    "2) DATOS DE LA UNIDAD"
            );

            System.out.println(
                    "ID: "
                    + kg.getIdUnidad()
            );

            System.out.println(
                    "Código: "
                    + kg.getCodigo()
            );

            System.out.println(
                    "Nombre: "
                    + kg.getNombre()
            );

            System.out.println(
                    "Permite decimales: "
                    + kg.isPermiteDecimales()
            );

            System.out.println(
                    "Activo: "
                    + kg.isActivo()
            );


            if (!kg.isPermiteDecimales()) {

                System.out.println();
                System.out.println(
                        "ERROR: KG debe permitir decimales."
                );

                return;
            }


            if (!kg.isActivo()) {

                System.out.println();
                System.out.println(
                        "ERROR: KG debe estar activa."
                );

                return;
            }


            // =================================================
            // 4. PROBAR DUPLICADO
            // =================================================

            System.out.println();
            System.out.println(
                    "3) INTENTANDO CREAR KG DUPLICADO..."
            );


            ResultadoOperacion duplicado =
                    unidadService.crearUnidad(
                            "KG",
                            "Kilogramo",
                            true
                    );


            System.out.println(
                    duplicado.getMensaje()
            );


            if (duplicado.isExitoso()) {

                System.out.println(
                        "ERROR: El sistema permitió "
                        + "duplicar KG."
                );

                return;
            }


            System.out.println(
                    "OK: Código KG duplicado rechazado."
            );


            // =================================================
            // RESULTADO
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST UNIDAD KG FINALIZADO CORRECTAMENTE"
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