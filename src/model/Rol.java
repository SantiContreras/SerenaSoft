package model;

import java.util.ArrayList;
import java.util.List;

public class Rol {

    private int idRol;
    private String nombre;
    private String descripcion;
    private boolean activo;

    private List<Permiso> permisos;

    // =========================================================
    // CONSTRUCTOR VACIO
    // =========================================================

    public Rol() {
        this.permisos = new ArrayList<>();
    }

    // =========================================================
    // CONSTRUCTOR BASICO
    // =========================================================

    public Rol(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.permisos = new ArrayList<>();
    }

    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Rol(
            int idRol,
            String nombre,
            String descripcion,
            boolean activo
    ) {

        this.idRol = idRol;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;

        this.permisos = new ArrayList<>();
    }

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
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

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {

        if (permisos == null) {
            this.permisos = new ArrayList<>();
        } else {
            this.permisos = permisos;
        }
    }

    // =========================================================
    // PERMISOS
    // =========================================================

    public boolean tienePermiso(String codigoPermiso) {

        if (codigoPermiso == null || permisos == null) {
            return false;
        }

        for (Permiso permiso : permisos) {

            if (permiso != null
                    && permiso.getCodigo() != null
                    && permiso.getCodigo()
                            .equalsIgnoreCase(codigoPermiso)) {

                return true;
            }
        }

        return false;
    }

    public void agregarPermiso(Permiso permiso) {

        if (permiso == null) {
            return;
        }

        if (this.permisos == null) {
            this.permisos = new ArrayList<>();
        }

        this.permisos.add(permiso);
    }

    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {
        return nombre;
    }
}