package util;

import model.Proveedor;

import services.ProveedorService;
import services.ResultadoOperacion;

import java.util.List;

public class TestProveedor {

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "TEST PROVEEDOR - SERENA SOFT"
        );

        System.out.println(
                "=============================================="
        );


        ProveedorService proveedorService =
                new ProveedorService();


        // =====================================================
        // 1. CREAR PROVEEDOR
        // =====================================================

        System.out.println();
        System.out.println(
                "1) CREANDO PROVEEDOR..."
        );


        Proveedor proveedor =
                new Proveedor();

        proveedor.setRazonSocial(
                "Distribuidora Norte S.R.L."
        );

        proveedor.setNombreComercial(
                "Distribuidora Norte"
        );

        proveedor.setCuit(
                "30-71234567-8"
        );

        proveedor.setCondicionIva(
                "Responsable Inscripto"
        );

        proveedor.setDireccion(
                "Av. Sarmiento 1500"
        );

        proveedor.setLocalidad(
                "Resistencia"
        );

        proveedor.setProvincia(
                "Chaco"
        );

        proveedor.setTelefono(
                "3624000000"
        );

        proveedor.setEmail(
                "ventas@distribuidoranorte.com"
        );

        proveedor.setPersonaContacto(
                "Carlos Gómez"
        );

        proveedor.setTelefonoContacto(
                "3624111111"
        );

        proveedor.setObservaciones(
                "Proveedor de bebidas - TEST SERENA SOFT"
        );


        ResultadoOperacion resultadoCrear =
                proveedorService.crear(
                        proveedor
                );


        System.out.println(
                resultadoCrear.getMensaje()
        );


        if (!resultadoCrear.isExitoso()) {

            System.out.println(
                    "ERROR: No se pudo continuar con el test."
            );

            return;
        }


        int idProveedor =
                proveedor.getIdProveedor();


        System.out.println(
                "ID generado: "
                + idProveedor
        );


        // =====================================================
        // 2. BUSCAR POR ID
        // =====================================================

        System.out.println();
        System.out.println(
                "2) BUSCANDO PROVEEDOR POR ID..."
        );


        Proveedor encontrado =
                proveedorService.buscarPorId(
                        idProveedor
                );


        if (encontrado == null) {

            System.out.println(
                    "ERROR: No se encontró el proveedor."
            );

            return;
        }


        System.out.println(
                "Razón social: "
                + encontrado.getRazonSocial()
        );

        System.out.println(
                "Nombre comercial: "
                + encontrado.getNombreComercial()
        );

        System.out.println(
                "CUIT: "
                + encontrado.getCuit()
        );

        System.out.println(
                "Activo: "
                + encontrado.isActivo()
        );

        System.out.println(
                "Fecha registro: "
                + encontrado.getFechaRegistro()
        );


        // =====================================================
        // 3. BUSCAR POR CUIT
        // =====================================================

        System.out.println();
        System.out.println(
                "3) BUSCANDO POR CUIT..."
        );


        Proveedor porCuit =
                proveedorService.buscarPorCuit(
                        "30-71234567-8"
                );


        if (porCuit == null) {

            System.out.println(
                    "ERROR: No se encontró por CUIT."
            );

            return;
        }


        System.out.println(
                "Encontrado: "
                + porCuit.getRazonSocial()
        );

        System.out.println(
                "OK: búsqueda por CUIT correcta."
        );


        // =====================================================
        // 4. BUSCAR POR TEXTO
        // =====================================================

        System.out.println();
        System.out.println(
                "4) BUSCANDO POR TEXTO 'Norte'..."
        );


        List<Proveedor> resultados =
                proveedorService.buscar(
                        "Norte"
                );


        System.out.println(
                "Resultados encontrados: "
                + resultados.size()
        );


        for (Proveedor p : resultados) {

            System.out.println(
                    "- "
                    + p.getIdProveedor()
                    + " | "
                    + p.getRazonSocial()
                    + " | "
                    + p.getCuit()
            );
        }


        if (resultados.isEmpty()) {

            System.out.println(
                    "ERROR: La búsqueda no devolvió resultados."
            );

            return;
        }


        // =====================================================
        // 5. ACTUALIZAR PROVEEDOR
        // =====================================================

        System.out.println();
        System.out.println(
                "5) ACTUALIZANDO PROVEEDOR..."
        );


        encontrado.setTelefono(
                "3624555555"
        );

        encontrado.setPersonaContacto(
                "María López"
        );

        encontrado.setObservaciones(
                "Proveedor principal de bebidas"
        );


        ResultadoOperacion resultadoActualizar =
                proveedorService.actualizar(
                        encontrado
                );


        System.out.println(
                resultadoActualizar.getMensaje()
        );


        if (!resultadoActualizar.isExitoso()) {

            return;
        }


        Proveedor actualizado =
                proveedorService.buscarPorId(
                        idProveedor
                );


        System.out.println(
                "Teléfono actualizado: "
                + actualizado.getTelefono()
        );

        System.out.println(
                "Contacto actualizado: "
                + actualizado.getPersonaContacto()
        );

        System.out.println(
                "Observaciones: "
                + actualizado.getObservaciones()
        );


        // =====================================================
        // 6. PROBAR CUIT DUPLICADO
        // =====================================================

        System.out.println();
        System.out.println(
                "6) PROBANDO CUIT DUPLICADO..."
        );


        Proveedor duplicado =
                new Proveedor();

        duplicado.setRazonSocial(
                "Otro Proveedor S.R.L."
        );

        duplicado.setNombreComercial(
                "Otro Proveedor"
        );

        duplicado.setCuit(
                "30-71234567-8"
        );


        ResultadoOperacion resultadoDuplicado =
                proveedorService.crear(
                        duplicado
                );


        System.out.println(
                resultadoDuplicado.getMensaje()
        );


        if (resultadoDuplicado.isExitoso()) {

            System.out.println(
                    "ERROR: Se permitió guardar "
                    + "un CUIT duplicado."
            );

            return;
        }


        System.out.println(
                "OK: CUIT duplicado rechazado."
        );


        // =====================================================
        // 7. DESACTIVAR PROVEEDOR
        // =====================================================

        System.out.println();
        System.out.println(
                "7) DESACTIVANDO PROVEEDOR..."
        );


        ResultadoOperacion resultadoDesactivar =
                proveedorService.cambiarEstado(
                        idProveedor,
                        false
                );


        System.out.println(
                resultadoDesactivar.getMensaje()
        );


        if (!resultadoDesactivar.isExitoso()) {

            return;
        }


        Proveedor inactivo =
                proveedorService.buscarPorId(
                        idProveedor
                );


        System.out.println(
                "Activo: "
                + inactivo.isActivo()
        );


        if (inactivo.isActivo()) {

            System.out.println(
                    "ERROR: El proveedor sigue activo."
            );

            return;
        }


        // =====================================================
        // 8. VERIFICAR QUE NO APARECE ENTRE LOS ACTIVOS
        // =====================================================

        System.out.println();
        System.out.println(
                "8) VERIFICANDO LISTADO DE ACTIVOS..."
        );


        List<Proveedor> activos =
                proveedorService.listarActivos();


        boolean apareceActivo =
                false;


        for (Proveedor p : activos) {

            if (p.getIdProveedor()
                    == idProveedor) {

                apareceActivo =
                        true;

                break;
            }
        }


        if (apareceActivo) {

            System.out.println(
                    "ERROR: El proveedor inactivo aparece "
                    + "en listarActivos()."
            );

            return;
        }


        System.out.println(
                "OK: proveedor inactivo no aparece "
                + "en la lista de activos."
        );


        // =====================================================
        // 9. REACTIVAR PROVEEDOR
        //
        // Lo dejamos activo porque lo vamos a utilizar
        // inmediatamente en las pruebas de Compra.
        // =====================================================

        System.out.println();
        System.out.println(
                "9) REACTIVANDO PROVEEDOR..."
        );


        ResultadoOperacion resultadoActivar =
                proveedorService.cambiarEstado(
                        idProveedor,
                        true
                );


        System.out.println(
                resultadoActivar.getMensaje()
        );


        if (!resultadoActivar.isExitoso()) {

            return;
        }


        Proveedor proveedorFinal =
                proveedorService.buscarPorId(
                        idProveedor
                );


        if (!proveedorFinal.isActivo()) {

            System.out.println(
                    "ERROR: No se reactivó el proveedor."
            );

            return;
        }


        System.out.println(
                "Activo: "
                + proveedorFinal.isActivo()
        );


        // =====================================================
        // 10. RESULTADO FINAL
        // =====================================================

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "TEST PROVEEDOR FINALIZADO CORRECTAMENTE"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println();
        System.out.println(
                "Proveedor preparado para Compra:"
        );

        System.out.println(
                proveedorFinal.getIdProveedor()
                + " | "
                + proveedorFinal.getRazonSocial()
                + " | "
                + proveedorFinal.getCuit()
        );
    }
}