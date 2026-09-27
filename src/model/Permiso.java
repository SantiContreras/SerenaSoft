package model;

public class Permiso {

    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String modulo;
    private String descripcion;
    private boolean activo;

    public Permiso() {
    }

    public Permiso(Integer idPermiso,
                   String codigo,
                   String nombre,
                   String modulo,
                   String descripcion,
                   boolean activo) {

        this.idPermiso = idPermiso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.modulo = modulo;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
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

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
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

    @Override
    public String toString() {
        return nombre;
    }
}