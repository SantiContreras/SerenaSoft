package model;

public class Categoria {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idCategoria;
    private Rubro rubro;
    private String nombre;
    private String descripcion;
    private boolean activo;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Categoria() {
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Categoria(
            int idCategoria,
            Rubro rubro,
            String nombre,
            String descripcion,
            boolean activo
    ) {

        this.idCategoria = idCategoria;
        this.rubro = rubro;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }


    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
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


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {
        return nombre;
    }
}