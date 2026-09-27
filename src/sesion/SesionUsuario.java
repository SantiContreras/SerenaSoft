package sesion;

import model.Usuario;

public final class SesionUsuario {

    // =========================================================
    // USUARIO ACTUAL
    // =========================================================

    /*
     * Guarda en memoria el usuario que inició sesión.
     *
     * Mientras Serena Soft esté abierto,
     * podemos consultar este objeto desde cualquier módulo.
     */
    private static Usuario usuarioActual;


    // =========================================================
    // CONSTRUCTOR PRIVADO
    // =========================================================

    /*
     * No queremos crear objetos:
     *
     * new SesionUsuario();
     *
     * La sesión pertenece a toda la aplicación.
     */
    private SesionUsuario() {
    }


    // =========================================================
    // INICIAR SESIÓN
    // =========================================================

    public static void iniciarSesion(
            Usuario usuario) {

        usuarioActual = usuario;
    }


    // =========================================================
    // OBTENER USUARIO ACTUAL
    // =========================================================

    public static Usuario getUsuarioActual() {

        return usuarioActual;
    }


    // =========================================================
    // VERIFICAR SI HAY SESIÓN
    // =========================================================

    public static boolean haySesion() {

        return usuarioActual != null;
    }


    // =========================================================
    // CERRAR SESIÓN
    // =========================================================

    public static void cerrarSesion() {

        usuarioActual = null;
    }


    // =========================================================
    // OBTENER ID DEL USUARIO
    // =========================================================

    public static int getIdUsuario() {

        if (usuarioActual == null) {

            return 0;
        }

        return usuarioActual.getIdUsuario();
    }


    // =========================================================
    // OBTENER NOMBRE COMPLETO
    // =========================================================

    public static String getNombreUsuario() {

        if (usuarioActual == null) {

            return "";
        }

        return usuarioActual.getNombreCompleto();
    }


    // =========================================================
    // OBTENER USERNAME
    // =========================================================

    public static String getUsername() {

        if (usuarioActual == null) {

            return "";
        }

        return usuarioActual.getUsername();
    }


    // =========================================================
    // OBTENER NOMBRE DEL ROL
    // =========================================================

    public static String getNombreRol() {

        if (usuarioActual == null) {

            return "";
        }

        if (usuarioActual.getRol() == null) {

            return "";
        }

        return usuarioActual
                .getRol()
                .getNombre();
    }


    // =========================================================
    // VERIFICAR SI ES ADMINISTRADOR
    // =========================================================

    public static boolean esAdministrador() {

        return "ADMINISTRADOR".equalsIgnoreCase(
                getNombreRol()
        );
    }


    // =========================================================
    // VERIFICAR PERMISO
    // =========================================================

    public static boolean tienePermiso(
            String codigoPermiso) {

        // Sin usuario no existe autorización.
        if (usuarioActual == null) {

            return false;
        }


        // Sin rol tampoco existe autorización.
        if (usuarioActual.getRol() == null) {

            return false;
        }


        // Código inválido.
        if (codigoPermiso == null
                || codigoPermiso.isBlank()) {

            return false;
        }


        /*
         * El Rol ya tiene cargada su lista de permisos
         * cuando UsuarioDao.buscarPorUsername()
         * recupera al usuario durante el login.
         */
        return usuarioActual
                .getRol()
                .tienePermiso(
                        codigoPermiso
                );
    }
}