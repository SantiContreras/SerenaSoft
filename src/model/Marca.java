package model;

import java.time.LocalDateTime;

public class Marca {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idMarca;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Marca() {
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Marca(
            int idMarca,
            String nombre,
            String descripcion,
            boolean activo,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaModificacion) {

        this.idMarca = idMarca;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }


    // =========================================================
    // REPRESENTACIÓN
    // =========================================================

    @Override
    public String toString() {
        return nombre;
    }
}
