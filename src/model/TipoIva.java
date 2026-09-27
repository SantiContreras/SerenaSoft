package model;

import java.math.BigDecimal;

public class TipoIva {

    // =========================================================
    // ATRIBUTOS
    // =========================================================

    private int idIva;
    private String codigo;
    private String nombre;
    private BigDecimal porcentaje;
    private boolean activo;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public TipoIva() {
        this.porcentaje = BigDecimal.ZERO;
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public TipoIva(
            int idIva,
            String codigo,
            String nombre,
            BigDecimal porcentaje,
            boolean activo) {

        this.idIva = idIva;
        this.codigo = codigo;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.activo = activo;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdIva() {
        return idIva;
    }

    public void setIdIva(int idIva) {
        this.idIva = idIva;
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

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
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