package model;

import java.time.LocalDateTime;

public class Usuario {

    private Integer idUsuario;

    private Rol rol;

    private String nombreCompleto;
    private String username;
    private String passwordHash;
    private String email;

    private String estado;

    private int intentosFallidos;

    private LocalDateTime bloqueadoHasta;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Usuario() {
    }

    public Usuario(Integer idUsuario,
                   Rol rol,
                   String nombreCompleto,
                   String username,
                   String passwordHash,
                   String email,
                   String estado,
                   int intentosFallidos,
                   LocalDateTime bloqueadoHasta,
                   LocalDateTime ultimoAcceso,
                   LocalDateTime fechaCreacion,
                   LocalDateTime fechaModificacion) {

        this.idUsuario = idUsuario;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.estado = estado;
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.ultimoAcceso = ultimoAcceso;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
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

    public boolean estaActivo() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }

    public boolean tienePermiso(String codigo) {

        if (rol == null) {
            return false;
        }

        return rol.tienePermiso(codigo);
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}