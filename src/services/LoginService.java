package services;

import Dao.UsuarioDao;
import java.time.LocalDateTime;
import model.Usuario;
import security.PasswordUtil;
import sesion.SesionUsuario;

public class LoginService {

    private final UsuarioDao usuarioDao;

    // =========================================================
    // CONFIGURACIÓN DE SEGURIDAD
    // =========================================================

    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 15;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginService() {

        this.usuarioDao =
                new UsuarioDao();
    }


    // =========================================================
    // LOGIN
    // =========================================================

    public ResultadoLogin login(
            String username,
            String password) {

        // -----------------------------------------------------
        // VALIDAR USERNAME
        // -----------------------------------------------------

        if (username == null
                || username.isBlank()) {

            return ResultadoLogin.error(
                    "Ingrese el usuario."
            );
        }


        // -----------------------------------------------------
        // VALIDAR PASSWORD
        // -----------------------------------------------------

        if (password == null
                || password.isBlank()) {

            return ResultadoLogin.error(
                    "Ingrese la contraseña."
            );
        }


        username =
                username.trim();


        // -----------------------------------------------------
        // BUSCAR USUARIO
        // -----------------------------------------------------

        Usuario usuario =
                usuarioDao.buscarPorUsername(
                        username
                );


        if (usuario == null) {

            return ResultadoLogin.error(
                    "Usuario o contraseña incorrectos."
            );
        }


        // =====================================================
        // VERIFICAR BLOQUEO TEMPORAL
        // =====================================================

        if ("BLOQUEADO".equalsIgnoreCase(
                usuario.getEstado())) {

            LocalDateTime bloqueadoHasta =
                    usuario.getBloqueadoHasta();


            // Si el tiempo de bloqueo ya terminó,
            // desbloqueamos automáticamente.

            if (bloqueadoHasta != null
                    && !bloqueadoHasta.isAfter(
                            LocalDateTime.now())) {

                usuarioDao.desbloquearUsuario(
                        usuario.getIdUsuario()
                );


                // Volvemos a buscar el usuario
                // para obtener los datos actualizados.

                usuario =
                        usuarioDao.buscarPorUsername(
                                username
                        );
            }
        }


        // =====================================================
        // VERIFICAR USUARIO INACTIVO
        // =====================================================

        if ("INACTIVO".equalsIgnoreCase(
                usuario.getEstado())) {

            return ResultadoLogin.error(
                    "El usuario se encuentra inactivo."
            );
        }


        // =====================================================
        // VERIFICAR SI SIGUE BLOQUEADO
        // =====================================================

        if ("BLOQUEADO".equalsIgnoreCase(
                usuario.getEstado())) {

            return ResultadoLogin.error(
                    "Usuario bloqueado temporalmente. "
                    + "Intente nuevamente más tarde."
            );
        }


        // =====================================================
        // VERIFICAR CONTRASEÑA
        // =====================================================

        boolean passwordCorrecta =
                PasswordUtil.verificar(
                        password,
                        usuario.getPasswordHash()
                );


        // =====================================================
        // CONTRASEÑA INCORRECTA
        // =====================================================

        if (!passwordCorrecta) {

            int nuevosIntentos =
                    usuario.getIntentosFallidos()
                    + 1;


            // -------------------------------------------------
            // BLOQUEAR DESPUÉS DE 5 INTENTOS
            // -------------------------------------------------

            if (nuevosIntentos >= MAX_INTENTOS) {

                LocalDateTime bloqueadoHasta =
                        LocalDateTime.now()
                                .plusMinutes(
                                        MINUTOS_BLOQUEO
                                );


                usuarioDao.bloquearUsuario(
                        usuario.getIdUsuario(),
                        nuevosIntentos,
                        bloqueadoHasta
                );


                return ResultadoLogin.error(
                        "Usuario bloqueado por "
                        + MINUTOS_BLOQUEO
                        + " minutos."
                );
            }


            // -------------------------------------------------
            // REGISTRAR INTENTO FALLIDO
            // -------------------------------------------------

            usuarioDao.registrarIntentoFallido(
                    usuario.getIdUsuario(),
                    nuevosIntentos
            );


            int intentosRestantes =
                    MAX_INTENTOS
                    - nuevosIntentos;


            return ResultadoLogin.error(
                    "Usuario o contraseña incorrectos. "
                    + "Intentos restantes: "
                    + intentosRestantes
            );
        }


        // =====================================================
        // VERIFICAR ROL
        // =====================================================

        if (usuario.getRol() == null) {

            return ResultadoLogin.error(
                    "El usuario no tiene un rol asignado."
            );
        }


        if (!usuario.getRol().isActivo()) {

            return ResultadoLogin.error(
                    "El rol del usuario se encuentra inactivo."
            );
        }


        // =====================================================
        // LOGIN CORRECTO
        // =====================================================

        LocalDateTime ahora =
                LocalDateTime.now();


        usuarioDao.registrarAccesoCorrecto(
                usuario.getIdUsuario(),
                ahora
        );


        // Actualizamos también el objeto Java.

        usuario.setIntentosFallidos(0);

        usuario.setBloqueadoHasta(
                null
        );

        usuario.setUltimoAcceso(
                ahora
        );


        // =====================================================
        // GUARDAR USUARIO EN SESIÓN
        // =====================================================

        SesionUsuario.iniciarSesion(
                usuario
        );


        // =====================================================
        // DEVOLVER RESULTADO
        // =====================================================

        return ResultadoLogin.correcto(
                usuario
        );
    }
}