package services;

public class ResultadoOperacion {

    private final boolean exitoso;
    private final String mensaje;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ResultadoOperacion(
            boolean exitoso,
            String mensaje) {

        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }


    // =========================================================
    // RESULTADO EXITOSO
    // =========================================================

    public static ResultadoOperacion ok(
            String mensaje) {

        return new ResultadoOperacion(
                true,
                mensaje
        );
    }


    // =========================================================
    // RESULTADO CON ERROR
    // =========================================================

    public static ResultadoOperacion error( String mensaje) {

        return new ResultadoOperacion(
                false,
                mensaje
        );
    }
}