package services;

import Dao.RolDao;
import Dao.UsuarioDao;

import model.Rol;
import model.Usuario;

import security.PasswordUtil;
import sesion.SesionUsuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDao usuarioDao;
    private final RolDao rolDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UsuarioService() {

        usuarioDao =
                new UsuarioDao();

        rolDao =
                new RolDao();
    }


    // =========================================================
    // CREAR USUARIO
    // =========================================================

    public ResultadoOperacion crearUsuario(
            String nombreCompleto,
            String username,
            String password,
            String confirmarPassword,
            String email,
            Rol rol) {

        nombreCompleto =
                limpiar(nombreCompleto);

        username =
                limpiar(username);

        email =
                limpiar(email);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombreCompleto.isBlank()) {

            return ResultadoOperacion.error(
                    "Debe ingresar el nombre completo."
            );
        }


        // -----------------------------------------------------
        // VALIDAR USERNAME
        // -----------------------------------------------------

        if (username.isBlank()) {

            return ResultadoOperacion.error(
                    "Debe ingresar un nombre de usuario."
            );
        }

        if (username.length() < 3) {

            return ResultadoOperacion.error(
                    "El usuario debe tener al menos 3 caracteres."
            );
        }

        if (usuarioDao.existeUsername(
                username)) {

            return ResultadoOperacion.error(
                    "El nombre de usuario ya existe."
            );
        }


        // -----------------------------------------------------
        // VALIDAR PASSWORD
        // -----------------------------------------------------

        ResultadoOperacion validacionPassword =
                validarPassword(
                        password,
                        confirmarPassword
                );

        if (!validacionPassword.isExitoso()) {

            return validacionPassword;
        }


        // -----------------------------------------------------
        // VALIDAR ROL
        // -----------------------------------------------------

        if (rol == null) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un rol."
            );
        }

        if (!rol.isActivo()) {

            return ResultadoOperacion.error(
                    "El rol seleccionado está inactivo."
            );
        }


        // -----------------------------------------------------
        // GENERAR HASH BCrypt
        // -----------------------------------------------------

        String passwordHash =
                PasswordUtil.generarHash(
                        password
                );


        // -----------------------------------------------------
        // CREAR USUARIO
        // -----------------------------------------------------

        Usuario usuario =
                new Usuario();

        usuario.setNombreCompleto(
                nombreCompleto
        );

        usuario.setUsername(
                username
        );

        usuario.setPasswordHash(
                passwordHash
        );

        usuario.setEmail(
                email.isBlank()
                        ? null
                        : email
        );

        usuario.setRol(
                rol
        );

        usuario.setEstado(
                "ACTIVO"
        );


        // -----------------------------------------------------
        // GUARDAR
        // -----------------------------------------------------

        if (usuarioDao.guardar(
                usuario)) {

            return ResultadoOperacion.ok(
                    "Usuario creado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear el usuario."
        );
    }


    // =========================================================
    // EDITAR USUARIO
    // =========================================================

    public ResultadoOperacion editarUsuario(
            int idUsuario,
            String nombreCompleto,
            String username,
            String email,
            Rol rol) {

        Usuario usuario =
                usuarioDao.buscarPorId(
                        idUsuario
                );

        if (usuario == null) {

            return ResultadoOperacion.error(
                    "El usuario no existe."
            );
        }


        nombreCompleto =
                limpiar(nombreCompleto);

        username =
                limpiar(username);

        email =
                limpiar(email);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombreCompleto.isBlank()) {

            return ResultadoOperacion.error(
                    "Debe ingresar el nombre completo."
            );
        }


        // -----------------------------------------------------
        // VALIDAR USERNAME
        // -----------------------------------------------------

        if (username.isBlank()) {

            return ResultadoOperacion.error(
                    "Debe ingresar un nombre de usuario."
            );
        }

        if (username.length() < 3) {

            return ResultadoOperacion.error(
                    "El usuario debe tener al menos 3 caracteres."
            );
        }


        // Ignora al propio usuario que estamos editando.

        if (usuarioDao.existeUsernameEnOtroUsuario(
                username,
                idUsuario)) {

            return ResultadoOperacion.error(
                    "Ese nombre de usuario pertenece "
                    + "a otro usuario."
            );
        }


        // -----------------------------------------------------
        // VALIDAR ROL
        // -----------------------------------------------------

        if (rol == null) {

            return ResultadoOperacion.error(
                    "Debe seleccionar un rol."
            );
        }

        if (!rol.isActivo()) {

            return ResultadoOperacion.error(
                    "El rol seleccionado está inactivo."
            );
        }


        // -----------------------------------------------------
        // ACTUALIZAR OBJETO
        // -----------------------------------------------------

        usuario.setNombreCompleto(
                nombreCompleto
        );

        usuario.setUsername(
                username
        );

        usuario.setEmail(
                email.isBlank()
                        ? null
                        : email
        );

        usuario.setRol(
                rol
        );


        // -----------------------------------------------------
        // ACTUALIZAR BASE DE DATOS
        // -----------------------------------------------------

        if (usuarioDao.actualizar(
                usuario)) {

            return ResultadoOperacion.ok(
                    "Usuario actualizado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar el usuario."
        );
    }


    // =========================================================
    // CAMBIAR CONTRASEÑA
    // =========================================================

    public ResultadoOperacion cambiarPassword(
            int idUsuario,
            String password,
            String confirmarPassword) {

        Usuario usuario =
                usuarioDao.buscarPorId(
                        idUsuario
                );

        if (usuario == null) {

            return ResultadoOperacion.error(
                    "El usuario no existe."
            );
        }


        ResultadoOperacion validacion =
                validarPassword(
                        password,
                        confirmarPassword
                );

        if (!validacion.isExitoso()) {

            return validacion;
        }


        String nuevoHash =
                PasswordUtil.generarHash(
                        password
                );


        if (usuarioDao.cambiarPassword(
                idUsuario,
                nuevoHash)) {

            return ResultadoOperacion.ok(
                    "Contraseña modificada correctamente."
            );
        }


        return ResultadoOperacion.error(
                "No se pudo modificar la contraseña."
        );
    }


    // =========================================================
    // ACTIVAR USUARIO
    // =========================================================

    public ResultadoOperacion activarUsuario(
            int idUsuario) {

        Usuario usuario =
                usuarioDao.buscarPorId(
                        idUsuario
                );

        if (usuario == null) {

            return ResultadoOperacion.error(
                    "El usuario no existe."
            );
        }


        if ("ACTIVO".equalsIgnoreCase(
                usuario.getEstado())) {

            return ResultadoOperacion.error(
                    "El usuario ya se encuentra activo."
            );
        }


        if (usuarioDao.cambiarEstado(
                idUsuario,
                "ACTIVO")) {

            return ResultadoOperacion.ok(
                    "Usuario activado correctamente."
            );
        }


        return ResultadoOperacion.error(
                "No se pudo activar el usuario."
        );
    }


    // =========================================================
    // DESACTIVAR USUARIO
    // =========================================================

    public ResultadoOperacion desactivarUsuario(
            int idUsuario) {

        Usuario usuario =
                usuarioDao.buscarPorId(
                        idUsuario
                );

        if (usuario == null) {

            return ResultadoOperacion.error(
                    "El usuario no existe."
            );
        }


        // -----------------------------------------------------
        // EVITAR QUE EL USUARIO LOGUEADO
        // SE DESACTIVE A SÍ MISMO
        // -----------------------------------------------------

        Usuario usuarioLogueado =
                SesionUsuario.getUsuarioActual();

        if (usuarioLogueado != null
                && usuarioLogueado.getIdUsuario()
                == idUsuario) {

            return ResultadoOperacion.error(
                    "No puede desactivar el usuario "
                    + "con el que inició sesión."
            );
        }


        if ("INACTIVO".equalsIgnoreCase(
                usuario.getEstado())) {

            return ResultadoOperacion.error(
                    "El usuario ya se encuentra inactivo."
            );
        }


        if (usuarioDao.cambiarEstado(
                idUsuario,
                "INACTIVO")) {

            return ResultadoOperacion.ok(
                    "Usuario desactivado correctamente."
            );
        }


        return ResultadoOperacion.error(
                "No se pudo desactivar el usuario."
        );
    }


    // =========================================================
    // LISTAR USUARIOS
    // =========================================================

    public List<Usuario> listarUsuarios() {

        return usuarioDao.listar();
    }


    // =========================================================
    // LISTAR ROLES ACTIVOS
    // =========================================================

    public List<Rol> listarRoles() {

        return rolDao.listarActivos();
    }


    // =========================================================
    // BUSCAR USUARIO POR ID
    // =========================================================

    public Usuario buscarPorId(
            int idUsuario) {

        return usuarioDao.buscarPorId(
                idUsuario
        );
    }


    // =========================================================
    // VALIDAR CONTRASEÑA
    // =========================================================

    private ResultadoOperacion validarPassword(
            String password,
            String confirmarPassword) {

        if (password == null
                || password.isBlank()) {

            return ResultadoOperacion.error(
                    "Debe ingresar una contraseña."
            );
        }


        if (password.length() < 8) {

            return ResultadoOperacion.error(
                    "La contraseña debe tener "
                    + "al menos 8 caracteres."
            );
        }


        if (confirmarPassword == null
                || !password.equals(
                        confirmarPassword)) {

            return ResultadoOperacion.error(
                    "Las contraseñas no coinciden."
            );
        }


        return ResultadoOperacion.ok(
                "Contraseña válida."
        );
    }


    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(
            String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }
}