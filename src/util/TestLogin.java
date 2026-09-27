package util;

import model.Permiso;
import model.Usuario;
import services.LoginService;
import services.ResultadoLogin;
import sesion.SesionUsuario;

public class TestLogin {

    public static void main(String[] args) {

        // =====================================================
        // DATOS DE PRUEBA
        // =====================================================

        String username = "juan";
        String password = "Caja1234";


        // =====================================================
        // LOGIN
        // =====================================================

        LoginService loginService =
                new LoginService();

        ResultadoLogin resultado =
                loginService.login(
                        username,
                        password
                );


        // =====================================================
        // VERIFICAR RESULTADO
        // =====================================================

        if (!resultado.isCorrecto()) {

            System.out.println(
                    "LOGIN INCORRECTO"
            );

            System.out.println(
                    resultado.getMensaje()
            );

            return;
        }


        System.out.println(
                "======================================"
        );

        System.out.println(
                "LOGIN CORRECTO"
        );

        System.out.println(
                "======================================"
        );


        // =====================================================
        // OBTENER USUARIO DE LA SESION
        // =====================================================

        Usuario usuario =
                SesionUsuario.getUsuarioActual();


        if (usuario == null) {

            System.out.println(
                    "ERROR: La sesión no contiene usuario."
            );

            return;
        }


        // =====================================================
        // MOSTRAR DATOS DEL USUARIO
        // =====================================================

        System.out.println(
                "ID: "
                + usuario.getIdUsuario()
        );

        System.out.println(
                "Nombre: "
                + usuario.getNombreCompleto()
        );

        System.out.println(
                "Username: "
                + usuario.getUsername()
        );

        System.out.println(
                "Estado: "
                + usuario.getEstado()
        );


        // =====================================================
        // MOSTRAR ROL
        // =====================================================

        if (usuario.getRol() == null) {

            System.out.println(
                    "ERROR: El usuario no tiene rol."
            );

            return;
        }


        System.out.println(
                "Rol: "
                + usuario.getRol().getNombre()
        );


        // =====================================================
        // MOSTRAR PERMISOS
        // =====================================================

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "PERMISOS DEL USUARIO"
        );

        System.out.println(
                "======================================"
        );


        if (usuario.getRol().getPermisos() == null
                || usuario.getRol().getPermisos().isEmpty()) {

            System.out.println(
                    "El rol no tiene permisos cargados."
            );

        } else {

            System.out.println(
                    "Cantidad de permisos: "
                    + usuario.getRol()
                            .getPermisos()
                            .size()
            );

            System.out.println();


            for (Permiso permiso
                    : usuario.getRol().getPermisos()) {

                System.out.println(
                        permiso.getModulo()
                        + " | "
                        + permiso.getCodigo()
                        + " | "
                        + permiso.getNombre()
                );
            }
        }


        // =====================================================
        // COMPROBAR SESION
        // =====================================================

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "SESION"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Usuario en sesión: "
                + SesionUsuario.getNombreUsuario()
        );

        System.out.println(
                "Rol en sesión: "
                + SesionUsuario.getNombreRol()
        );
    }
}