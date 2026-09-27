package services;

import model.Usuario;

public class ResultadoLogin {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private final boolean correcto;
    private final String mensaje;
    private final Usuario usuario;


    // =========================================================
    // CONSTRUCTOR PRIVADO
    // =========================================================

    private ResultadoLogin(
            boolean correcto,
            String mensaje,
            Usuario usuario) {

        this.correcto = correcto;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }


    // =========================================================
    // RESULTADO CORRECTO
    // =========================================================

    public static ResultadoLogin correcto(
            Usuario usuario) {

        return new ResultadoLogin(
                true,
                "Inicio de sesión correcto.",
                usuario
        );
    }


    // =========================================================
    // RESULTADO CON ERROR
    // =========================================================

    public static ResultadoLogin error(
            String mensaje) {

        return new ResultadoLogin(
                false,
                mensaje,
                null
        );
    }


    // =========================================================
    // SABER SI EL LOGIN FUE CORRECTO
    // =========================================================

    public boolean isCorrecto() {

        return correcto;
    }


    // =========================================================
    // OBTENER MENSAJE
    // =========================================================

    public String getMensaje() {

        return mensaje;
    }


    // =========================================================
    // OBTENER USUARIO
    // =========================================================

    public Usuario getUsuario() {

        return usuario;
    }
}