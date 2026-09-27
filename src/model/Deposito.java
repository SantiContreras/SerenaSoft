package model;

public class Deposito {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idDeposito;
    private String codigo;
    private String nombre;
    private String descripcion;
    private boolean esPrincipal;
    private boolean activo;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Deposito() {
        this.esPrincipal = false;
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Deposito(
            int idDeposito,
            String codigo,
            String nombre,
            String descripcion,
            boolean esPrincipal,
            boolean activo) {

        this.idDeposito = idDeposito;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.esPrincipal = esPrincipal;
        this.activo = activo;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {
        return nombre;
    }
}