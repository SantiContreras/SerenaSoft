package util;

import Dao.RolDao;
import model.Rol;
import services.ResultadoOperacion;
import services.UsuarioService;

public class TestCrearUsuario {

    public static void main(String[] args) {

        // =====================================================
        // 1. CREAR SERVICIOS
        // =====================================================

        RolDao rolDao =
                new RolDao();

        UsuarioService usuarioService =
                new UsuarioService();


        // =====================================================
        // 2. BUSCAR ROL CAJERO
        // =====================================================

        Rol rolCajero =
                rolDao.buscarPorNombre(
                        "CAJERO"
                );

        if (rolCajero == null) {

            System.out.println(
                    "ERROR: No existe el rol CAJERO."
            );

            return;
        }


        System.out.println(
                "Rol encontrado: "
                + rolCajero.getNombre()
        );


        // =====================================================
        // 3. CREAR USUARIO
        // =====================================================

        ResultadoOperacion resultado =
                usuarioService.crearUsuario(
                        "Juan Pérez",
                        "juan",
                        "Caja1234",
                        "Caja1234",
                        "juan@serenasoft.local",
                        rolCajero
                );


        // =====================================================
        // 4. MOSTRAR RESULTADO
        // =====================================================

        if (resultado.isExitoso()) {

            System.out.println(
                    "USUARIO CREADO CORRECTAMENTE"
            );

        } else {

            System.out.println(
                    "NO SE PUDO CREAR EL USUARIO"
            );
        }

        System.out.println(
                resultado.getMensaje()
        );
    }
}