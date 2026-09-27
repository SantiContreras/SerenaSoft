package util;

import model.Deposito;
import services.DepositoService;
import services.ResultadoOperacion;

import java.util.List;

public class TestDeposito {

    public static void main(String[] args) {

        DepositoService service =
                new DepositoService();


        System.out.println(
                "========================================"
        );

        System.out.println(
                "          PRUEBA MODULO DEPOSITO"
        );

        System.out.println(
                "========================================"
        );


        // =====================================================
        // 1. CREAR DEPÓSITO PRINCIPAL
        // =====================================================

        System.out.println();
        System.out.println(
                "1. CREANDO DEPOSITO PRINCIPAL..."
        );

        ResultadoOperacion resultado =
                service.crearDeposito(
                        "DEP-01",
                        "Depósito Principal",
                        "Depósito principal del negocio",
                        true
                );

        System.out.println(
                resultado.getMensaje()
        );


        // =====================================================
        // 2. BUSCAR POR CÓDIGO
        // =====================================================

        System.out.println();
        System.out.println(
                "2. BUSCANDO DEPOSITO..."
        );

        Deposito deposito =
                service.buscarPorCodigo(
                        "DEP-01"
                );

        if (deposito == null) {

            System.out.println(
                    "ERROR: No se encontró el depósito."
            );

            return;
        }


        System.out.println(
                "ID: "
                + deposito.getIdDeposito()
        );

        System.out.println(
                "Codigo: "
                + deposito.getCodigo()
        );

        System.out.println(
                "Nombre: "
                + deposito.getNombre()
        );

        System.out.println(
                "Descripcion: "
                + deposito.getDescripcion()
        );

        System.out.println(
                "Principal: "
                + deposito.isEsPrincipal()
        );

        System.out.println(
                "Activo: "
                + deposito.isActivo()
        );


        // =====================================================
        // 3. BUSCAR PRINCIPAL
        // =====================================================

        System.out.println();
        System.out.println(
                "3. BUSCANDO DEPOSITO PRINCIPAL..."
        );

        Deposito principal =
                service.buscarPrincipal();

        if (principal != null) {

            System.out.println(
                    "Principal encontrado: "
                    + principal.getNombre()
            );

        } else {

            System.out.println(
                    "No existe depósito principal."
            );
        }


        // =====================================================
        // 4. LISTAR
        // =====================================================

        System.out.println();
        System.out.println(
                "4. LISTADO DE DEPOSITOS"
        );

        List<Deposito> depositos =
                service.listarTodos();

        for (Deposito d : depositos) {

            System.out.println(
                    d.getIdDeposito()
                    + " | "
                    + d.getCodigo()
                    + " | "
                    + d.getNombre()
                    + " | Principal: "
                    + d.isEsPrincipal()
                    + " | Activo: "
                    + d.isActivo()
            );
        }


        // =====================================================
        // 5. EDITAR
        // =====================================================

        System.out.println();
        System.out.println(
                "5. EDITANDO DEPOSITO..."
        );

        ResultadoOperacion edicion =
                service.editarDeposito(
                        deposito.getIdDeposito(),
                        "DEP-01",
                        "Depósito Principal",
                        "Stock general del negocio",
                        true
                );

        System.out.println(
                edicion.getMensaje()
        );


        // =====================================================
        // 6. VERIFICAR EDICIÓN
        // =====================================================

        System.out.println();
        System.out.println(
                "6. VERIFICANDO EDICION..."
        );

        Deposito actualizado =
                service.buscarPorId(
                        deposito.getIdDeposito()
                );

        if (actualizado != null) {

            System.out.println(
                    "Nombre: "
                    + actualizado.getNombre()
            );

            System.out.println(
                    "Descripcion: "
                    + actualizado.getDescripcion()
            );

            System.out.println(
                    "Principal: "
                    + actualizado.isEsPrincipal()
            );

            System.out.println(
                    "Activo: "
                    + actualizado.isActivo()
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
                "       FIN PRUEBA MODULO DEPOSITO"
        );

        System.out.println(
                "========================================"
        );
    }
}