package model;

public class UnidadMedida {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idUnidad;
    private String codigo;
    private String nombre;
    private boolean permiteDecimales;
    private boolean activo;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public UnidadMedida() {
        this.permiteDecimales = false;
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public UnidadMedida(
            int idUnidad,
            String codigo,
            String nombre,
            boolean permiteDecimales,
            boolean activo) {

        this.idUnidad = idUnidad;
        this.codigo = codigo;
        this.nombre = nombre;
        this.permiteDecimales = permiteDecimales;
        this.activo = activo;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
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

    public boolean isPermiteDecimales() {
        return permiteDecimales;
    }

    public void setPermiteDecimales(boolean permiteDecimales) {
        this.permiteDecimales = permiteDecimales;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    // =========================================================
    // REPRESENTACIÓN
    // =========================================================

    @Override
    public String toString() {
        return nombre;
    }
}