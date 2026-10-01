package model;

import java.time.LocalDateTime;

public class Proveedor {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idProveedor;
    private String razonSocial;
    private String nombreComercial;
    private String cuit;
    private String condicionIva;
    private String direccion;
    private String localidad;
    private String provincia;
    private String telefono;
    private String email;
    private String personaContacto;
    private String telefonoContacto;
    private String observaciones;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Proveedor() {

        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Proveedor(
            int idProveedor,
            String razonSocial,
            String nombreComercial,
            String cuit,
            String condicionIva,
            String direccion,
            String localidad,
            String provincia,
            String telefono,
            String email,
            String personaContacto,
            String telefonoContacto,
            String observaciones,
            boolean activo,
            LocalDateTime fechaRegistro,
            LocalDateTime fechaModificacion) {

        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.cuit = cuit;
        this.condicionIva = condicionIva;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.telefono = telefono;
        this.email = email;
        this.personaContacto = personaContacto;
        this.telefonoContacto = telefonoContacto;
        this.observaciones = observaciones;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }


    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }


    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }


    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }


    public String getCondicionIva() {
        return condicionIva;
    }

    public void setCondicionIva(String condicionIva) {
        this.condicionIva = condicionIva;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }


    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }


    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(
            LocalDateTime fechaModificacion) {

        this.fechaModificacion = fechaModificacion;
    }


    // =========================================================
    // TO STRING
    //
    // Esto después nos sirve directamente en JComboBox,
    // búsquedas y pantallas de Compra.
    // =========================================================

    @Override
    public String toString() {

        if (nombreComercial != null
                && !nombreComercial.trim().isEmpty()) {

            return nombreComercial;
        }

        return razonSocial;
    }
}