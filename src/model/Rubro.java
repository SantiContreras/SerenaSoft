package model;

/**
 * Representa un rubro de productos dentro del sistema.
 *
 * Ejemplos:
 * BEBIDAS
 * ALMACÉN
 * LIMPIEZA
 * PERFUMERÍA
 */
public class Rubro {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idRubro;
    private String nombre;
    private String descripcion;
    private boolean activo;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Rubro() {
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR PARA CREAR UN RUBRO NUEVO
    // =========================================================
    // No recibe idRubro porque MySQL lo genera automáticamente.

    public Rubro(
            String nombre,
            String descripcion
    ) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================
    // Principalmente lo utilizará RubroDao cuando lea
    // registros existentes desde la base de datos.

    public Rubro(
            int idRubro,
            String nombre,
            String descripcion,
            boolean activo
    ) {

        this.idRubro = idRubro;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(int idRubro) {
        this.idRubro = idRubro;
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