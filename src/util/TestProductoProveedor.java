package test;

import model.Producto;
import model.ProductoProveedor;
import model.Proveedor;

import services.ProductoProveedorService;
import services.ProductoService;
import services.ProveedorService;
import services.ResultadoOperacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class TestProductoProveedor {

    // =========================================================
    // SERVICES
    // =========================================================

    private static final ProductoService productoService =
            new ProductoService();

    private static final ProveedorService proveedorService =
            new ProveedorService();

    private static final ProductoProveedorService productoProveedorService =
            new ProductoProveedorService();


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println(
                "===================================================="
        );

        System.out.println(
                "TEST PRODUCTO - PROVEEDOR"
        );

        System.out.println(
                "===================================================="
        );


        // =====================================================
        // 1. BUSCAR PRODUCTO
        // =====================================================

        System.out.println();
        System.out.println(
                "1) BUSCANDO PRODUCTO"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        Producto producto =
                productoService.buscarPorCodigo(
                        "BEB-0002"
                );


        if (producto == null) {

            errorFatal(
                    "No se encontró el producto BEB-0002."
            );

            return;
        }


        System.out.println(
                "[OK] Producto: "
                + producto.getCodigo()
                + " - "
                + producto.getNombre()
        );


        // =====================================================
        // 2. BUSCAR PROVEEDORES
        // =====================================================

        System.out.println();
        System.out.println(
                "2) BUSCANDO PROVEEDORES"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        List<Proveedor> proveedores =
                proveedorService.listarTodos();


        if (proveedores == null
                || proveedores.size() < 3) {

            errorFatal(
                    "Se necesitan al menos 3 proveedores "
                    + "para ejecutar el test."
            );

            return;
        }


        /*
         * Intentamos utilizar proveedores concretos de la carga demo.
         *
         * Si alguno no aparece por nombre, utilizamos los primeros
         * proveedores disponibles para no depender de IDs.
         */
        Proveedor proveedor1 =
                buscarProveedorPorNombre(
                        proveedores,
                        "Bebidas Nordeste"
                );

        Proveedor proveedor2 =
                buscarProveedorPorNombre(
                        proveedores,
                        "Mayorista Norte"
                );

        Proveedor proveedor3 =
                buscarProveedorPorNombre(
                        proveedores,
                        "Distribuidora Litoral"
                );


        if (proveedor1 == null) {

            proveedor1 =
                    proveedores.get(0);
        }


        if (proveedor2 == null
                || proveedor2.getIdProveedor()
                == proveedor1.getIdProveedor()) {

            proveedor2 =
                    buscarProveedorDistinto(
                            proveedores,
                            proveedor1
                    );
        }


        if (proveedor3 == null
                || proveedor3.getIdProveedor()
                == proveedor1.getIdProveedor()
                || proveedor3.getIdProveedor()
                == proveedor2.getIdProveedor()) {

            proveedor3 =
                    buscarProveedorDistinto(
                            proveedores,
                            proveedor1,
                            proveedor2
                    );
        }


        if (proveedor1 == null
                || proveedor2 == null
                || proveedor3 == null) {

            errorFatal(
                    "No se pudieron obtener "
                    + "3 proveedores distintos."
            );

            return;
        }


        mostrarProveedor(
                "Proveedor 1",
                proveedor1
        );

        mostrarProveedor(
                "Proveedor 2",
                proveedor2
        );

        mostrarProveedor(
                "Proveedor 3",
                proveedor3
        );


        // =====================================================
        // 3. LIMPIAR RELACIONES ANTERIORES DEL TEST
        // =====================================================

        System.out.println();
        System.out.println(
                "3) PREPARANDO TEST"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        limpiarRelacion(
                producto,
                proveedor1
        );

        limpiarRelacion(
                producto,
                proveedor2
        );

        limpiarRelacion(
                producto,
                proveedor3
        );


        System.out.println(
                "[OK] Entorno preparado."
        );


        // =====================================================
        // 4. ASOCIAR PRIMER PROVEEDOR
        // =====================================================

        System.out.println();
        System.out.println(
                "4) ASOCIANDO PRIMER PROVEEDOR"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        ResultadoOperacion resultado =
                productoProveedorService.asociar(
                        producto.getIdProducto(),
                        proveedor1.getIdProveedor(),
                        "COCA-225-A",
                        new BigDecimal("2100.00"),
                        true
                );


        verificarExito(
                resultado,
                "Asociar proveedor 1"
        );


        ProductoProveedor principal =
                productoProveedorService.buscarPrincipal(
                        producto.getIdProducto()
                );


        if (principal == null) {

            errorFatal(
                    "No se encontró proveedor principal."
            );

            return;
        }


        if (principal.getProveedor().getIdProveedor()
                != proveedor1.getIdProveedor()) {

            errorFatal(
                    "El proveedor 1 debería ser el principal."
            );

            return;
        }


        System.out.println(
                "[OK] Proveedor 1 establecido como principal."
        );


        // =====================================================
        // 5. ASOCIAR OTROS DOS PROVEEDORES
        // =====================================================

        System.out.println();
        System.out.println(
                "5) ASOCIANDO MÁS PROVEEDORES"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.asociar(
                        producto.getIdProducto(),
                        proveedor2.getIdProveedor(),
                        "COCA-225-B",
                        new BigDecimal("2180.00"),
                        false
                );


        verificarExito(
                resultado,
                "Asociar proveedor 2"
        );


        resultado =
                productoProveedorService.asociar(
                        producto.getIdProducto(),
                        proveedor3.getIdProveedor(),
                        "COCA-225-C",
                        new BigDecimal("2150.00"),
                        false
                );


        verificarExito(
                resultado,
                "Asociar proveedor 3"
        );


        List<ProductoProveedor> relaciones =
                productoProveedorService.listarPorProducto(
                        producto.getIdProducto()
                );


        if (relaciones.size() != 3) {

            errorFatal(
                    "Se esperaban 3 proveedores asociados, "
                    + "pero se encontraron "
                    + relaciones.size()
                    + "."
            );

            return;
        }


        System.out.println(
                "[OK] El producto tiene 3 proveedores asociados."
        );


        mostrarRelaciones(
                relaciones
        );


        // =====================================================
        // 6. PROBAR DUPLICADO
        // =====================================================

        System.out.println();
        System.out.println(
                "6) PROBANDO RELACIÓN DUPLICADA"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.asociar(
                        producto.getIdProducto(),
                        proveedor1.getIdProveedor(),
                        "CODIGO-DUPLICADO",
                        new BigDecimal("9999.00"),
                        false
                );


        if (resultado.isExitoso()) {

            errorFatal(
                    "El sistema permitió una relación duplicada."
            );

            return;
        }


        System.out.println(
                "[OK] Relación duplicada rechazada."
        );

        System.out.println(
                "     Mensaje: "
                + resultado.getMensaje()
        );


        // =====================================================
        // 7. CAMBIAR PROVEEDOR PRINCIPAL
        // =====================================================

        System.out.println();
        System.out.println(
                "7) CAMBIANDO PROVEEDOR PRINCIPAL"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.establecerPrincipal(
                        producto.getIdProducto(),
                        proveedor2.getIdProveedor()
                );


        verificarExito(
                resultado,
                "Cambiar proveedor principal"
        );


        principal =
                productoProveedorService.buscarPrincipal(
                        producto.getIdProducto()
                );


        if (principal == null) {

            errorFatal(
                    "El producto quedó sin proveedor principal."
            );

            return;
        }


        if (principal.getProveedor().getIdProveedor()
                != proveedor2.getIdProveedor()) {

            errorFatal(
                    "El proveedor 2 debería ser "
                    + "el nuevo principal."
            );

            return;
        }


        /*
         * Además verificamos que solamente haya UNO marcado.
         */
        relaciones =
                productoProveedorService.listarPorProducto(
                        producto.getIdProducto()
                );


        int cantidadPrincipales = 0;


        for (ProductoProveedor relacion : relaciones) {

            if (relacion.isProveedorPrincipal()) {

                cantidadPrincipales++;
            }
        }


        if (cantidadPrincipales != 1) {

            errorFatal(
                    "Debe existir exactamente un proveedor "
                    + "principal. Encontrados: "
                    + cantidadPrincipales
            );

            return;
        }


        System.out.println(
                "[OK] Nuevo principal: "
                + proveedor2
        );

        System.out.println(
                "[OK] Existe exactamente un proveedor principal."
        );


        // =====================================================
        // 8. ACTUALIZAR CÓDIGO Y COSTO
        // =====================================================

        System.out.println();
        System.out.println(
                "8) ACTUALIZANDO RELACIÓN"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.actualizar(
                        producto.getIdProducto(),
                        proveedor3.getIdProveedor(),
                        "COCA-225-C-NUEVO",
                        new BigDecimal("2250.50")
                );


        verificarExito(
                resultado,
                "Actualizar relación"
        );


        ProductoProveedor relacionActualizada =
                productoProveedorService.buscar(
                        producto.getIdProducto(),
                        proveedor3.getIdProveedor()
                );


        if (relacionActualizada == null) {

            errorFatal(
                    "No se encontró la relación actualizada."
            );

            return;
        }


        if (!"COCA-225-C-NUEVO".equals(
                relacionActualizada.getCodigoProveedor())) {

            errorFatal(
                    "El código del proveedor no se actualizó."
            );

            return;
        }


        if (relacionActualizada
                .getCostoUltimo()
                .compareTo(
                        new BigDecimal("2250.50")
                ) != 0) {

            errorFatal(
                    "El costo de la relación no se actualizó."
            );

            return;
        }


        System.out.println(
                "[OK] Código actualizado: "
                + relacionActualizada.getCodigoProveedor()
        );

        System.out.println(
                "[OK] Costo actualizado: $"
                + relacionActualizada.getCostoUltimo()
        );


        // =====================================================
        // 9. ACTUALIZAR ÚLTIMO COSTO Y FECHA
        // =====================================================

        System.out.println();
        System.out.println(
                "9) ACTUALIZANDO ÚLTIMO COSTO DE COMPRA"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        LocalDateTime fechaCompra =
                LocalDateTime.now()
                        .withNano(0);


        resultado =
                productoProveedorService.actualizarUltimoCosto(
                        producto.getIdProducto(),
                        proveedor2.getIdProveedor(),
                        new BigDecimal("2050.75"),
                        fechaCompra
                );


        verificarExito(
                resultado,
                "Actualizar último costo"
        );


        ProductoProveedor relacionCosto =
                productoProveedorService.buscar(
                        producto.getIdProducto(),
                        proveedor2.getIdProveedor()
                );


        if (relacionCosto == null) {

            errorFatal(
                    "No se encontró la relación "
                    + "para verificar el costo."
            );

            return;
        }


        if (relacionCosto
                .getCostoUltimo()
                .compareTo(
                        new BigDecimal("2050.75")
                ) != 0) {

            errorFatal(
                    "El último costo no coincide."
            );

            return;
        }


        if (relacionCosto.getFechaUltimaCompra() == null) {

            errorFatal(
                    "No se registró fecha de última compra."
            );

            return;
        }


        System.out.println(
                "[OK] Último costo: $"
                + relacionCosto.getCostoUltimo()
        );

        System.out.println(
                "[OK] Fecha última compra: "
                + relacionCosto.getFechaUltimaCompra()
        );


        // =====================================================
        // 10. PROBAR COSTO NEGATIVO
        // =====================================================

        System.out.println();
        System.out.println(
                "10) PROBANDO COSTO NEGATIVO"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.actualizarUltimoCosto(
                        producto.getIdProducto(),
                        proveedor2.getIdProveedor(),
                        new BigDecimal("-100.00"),
                        LocalDateTime.now()
                );


        if (resultado.isExitoso()) {

            errorFatal(
                    "El sistema permitió un costo negativo."
            );

            return;
        }


        System.out.println(
                "[OK] Costo negativo rechazado."
        );

        System.out.println(
                "     Mensaje: "
                + resultado.getMensaje()
        );


        // =====================================================
        // 11. LISTAR PRODUCTOS DEL PROVEEDOR
        // =====================================================

        System.out.println();
        System.out.println(
                "11) CONSULTANDO POR PROVEEDOR"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        List<ProductoProveedor> productosProveedor =
                productoProveedorService.listarPorProveedor(
                        proveedor2.getIdProveedor()
                );


        boolean encontrado =
                false;


        for (ProductoProveedor relacion
                : productosProveedor) {

            if (relacion.getProducto() != null
                    && relacion.getProducto()
                            .getIdProducto()
                    == producto.getIdProducto()) {

                encontrado =
                        true;

                break;
            }
        }


        if (!encontrado) {

            errorFatal(
                    "El producto no apareció al consultar "
                    + "por proveedor."
            );

            return;
        }


        System.out.println(
                "[OK] Consulta Proveedor -> Productos correcta."
        );


        // =====================================================
        // 12. ELIMINAR UNA RELACIÓN
        // =====================================================

        System.out.println();
        System.out.println(
                "12) ELIMINANDO UNA RELACIÓN"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        resultado =
                productoProveedorService.eliminar(
                        producto.getIdProducto(),
                        proveedor3.getIdProveedor()
                );


        verificarExito(
                resultado,
                "Eliminar proveedor 3"
        );


        ProductoProveedor eliminada =
                productoProveedorService.buscar(
                        producto.getIdProducto(),
                        proveedor3.getIdProveedor()
                );


        if (eliminada != null) {

            errorFatal(
                    "La relación eliminada todavía existe."
            );

            return;
        }


        relaciones =
                productoProveedorService.listarPorProducto(
                        producto.getIdProducto()
                );


        if (relaciones.size() != 2) {

            errorFatal(
                    "Después de eliminar deberían quedar "
                    + "2 proveedores. Encontrados: "
                    + relaciones.size()
            );

            return;
        }


        System.out.println(
                "[OK] Relación eliminada correctamente."
        );

        System.out.println(
                "[OK] Permanecen 2 proveedores asociados."
        );


        // =====================================================
        // 13. VERIFICAR QUE EL PRINCIPAL SIGA CORRECTO
        // =====================================================

        System.out.println();
        System.out.println(
                "13) VERIFICANDO ESTADO FINAL"
        );

        System.out.println(
                "----------------------------------------------------"
        );


        principal =
                productoProveedorService.buscarPrincipal(
                        producto.getIdProducto()
                );


        if (principal == null
                || principal.getProveedor()
                        .getIdProveedor()
                != proveedor2.getIdProveedor()) {

            errorFatal(
                    "El proveedor principal final "
                    + "no coincide."
            );

            return;
        }


        relaciones =
                productoProveedorService.listarPorProducto(
                        producto.getIdProducto()
                );


        mostrarRelaciones(
                relaciones
        );


        // =====================================================
        // RESULTADO FINAL
        // =====================================================

        System.out.println();
        System.out.println(
                "===================================================="
        );

        System.out.println(
                "TEST PRODUCTO-PROVEEDOR COMPLETADO CORRECTAMENTE"
        );

        System.out.println(
                "===================================================="
        );

        System.out.println(
                "Múltiples proveedores: OK"
        );

        System.out.println(
                "Duplicados rechazados: OK"
        );

        System.out.println(
                "Proveedor principal único: OK"
        );

        System.out.println(
                "Cambio de principal: OK"
        );

        System.out.println(
                "Actualización de relación: OK"
        );

        System.out.println(
                "Último costo y fecha: OK"
        );

        System.out.println(
                "Costo negativo rechazado: OK"
        );

        System.out.println(
                "Consulta Producto -> Proveedores: OK"
        );

        System.out.println(
                "Consulta Proveedor -> Productos: OK"
        );

        System.out.println(
                "Eliminación de relación: OK"
        );

        System.out.println(
                "===================================================="
        );
    }


    // =========================================================
    // BUSCAR PROVEEDOR POR NOMBRE
    // =========================================================

    private static Proveedor buscarProveedorPorNombre(
            List<Proveedor> proveedores,
            String nombre) {

        for (Proveedor proveedor : proveedores) {

            String razonSocial =
                    proveedor.getRazonSocial();

            String nombreComercial =
                    proveedor.getNombreComercial();


            if (razonSocial != null
                    && razonSocial.equalsIgnoreCase(
                            nombre
                    )) {

                return proveedor;
            }


            if (nombreComercial != null
                    && nombreComercial.equalsIgnoreCase(
                            nombre
                    )) {

                return proveedor;
            }
        }


        return null;
    }


    // =========================================================
    // BUSCAR PROVEEDOR DISTINTO
    // =========================================================

    private static Proveedor buscarProveedorDistinto(
            List<Proveedor> proveedores,
            Proveedor... excluir) {

        for (Proveedor proveedor : proveedores) {

            boolean permitido =
                    true;


            for (Proveedor excluido : excluir) {

                if (excluido != null
                        && proveedor.getIdProveedor()
                        == excluido.getIdProveedor()) {

                    permitido =
                            false;

                    break;
                }
            }


            if (permitido) {

                return proveedor;
            }
        }


        return null;
    }


    // =========================================================
    // MOSTRAR PROVEEDOR
    // =========================================================

    private static void mostrarProveedor(
            String titulo,
            Proveedor proveedor) {

        System.out.println(
                "[OK] "
                + titulo
                + ": "
                + proveedor
                + " | ID: "
                + proveedor.getIdProveedor()
        );
    }


    // =========================================================
    // MOSTRAR RELACIONES
    // =========================================================

    private static void mostrarRelaciones(
            List<ProductoProveedor> relaciones) {

        System.out.println();

        System.out.println(
                "Proveedores asociados:"
        );


        for (ProductoProveedor relacion
                : relaciones) {

            String principal =
                    relacion.isProveedorPrincipal()
                            ? " [PRINCIPAL]"
                            : "";


            System.out.println(
                    " - "
                    + relacion.getProveedor()
                    + " | código: "
                    + relacion.getCodigoProveedor()
                    + " | costo: "
                    + relacion.getCostoUltimo()
                    + principal
            );
        }
    }


    // =========================================================
    // LIMPIAR RELACIÓN DEL TEST
    // =========================================================

    private static void limpiarRelacion(
            Producto producto,
            Proveedor proveedor) {

        ProductoProveedor existente =
                productoProveedorService.buscar(
                        producto.getIdProducto(),
                        proveedor.getIdProveedor()
                );


        if (existente != null) {

            productoProveedorService.eliminar(
                    producto.getIdProducto(),
                    proveedor.getIdProveedor()
            );
        }
    }


    // =========================================================
    // VERIFICAR RESULTADO EXITOSO
    // =========================================================

    private static void verificarExito(
            ResultadoOperacion resultado,
            String operacion) {

        if (resultado == null) {

            throw new RuntimeException(
                    operacion
                    + ": ResultadoOperacion es null."
            );
        }


        if (!resultado.isExitoso()) {

            throw new RuntimeException(
                    operacion
                    + ": "
                    + resultado.getMensaje()
            );
        }


        System.out.println(
                "[OK] "
                + operacion
                + ": "
                + resultado.getMensaje()
        );
    }


    // =========================================================
    // ERROR FATAL
    // =========================================================

    private static void errorFatal(
            String mensaje) {

        System.err.println();
        System.err.println(
                "[ERROR] "
                + mensaje
        );

        System.err.println();

        System.err.println(
                "===================================================="
        );

        System.err.println(
                "TEST PRODUCTO-PROVEEDOR FALLÓ"
        );

        System.err.println(
                "===================================================="
        );
    }
}