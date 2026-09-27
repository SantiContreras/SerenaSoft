package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {

    private int idProducto;

    private String codigo;
    private String codigoInterno;
    private String codigoBarra;

    private String nombre;
    private String descripcion;
    private String ubicacion;

    private Rubro rubro;
    private Categoria categoria;
    private Marca marca;

    private UnidadMedida unidadCompra;
    private UnidadMedida unidadVenta;

    private BigDecimal factorConversion;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal margenPorcentaje;

    private TipoIva tipoIva;

    private BigDecimal stockMinimo;
    private BigDecimal stockMaximo;

    private boolean controlaStock;
    private boolean permiteVentaSinStock;
    private boolean pesable;
    private boolean permiteDescuento;
    private boolean activo;

    private String observaciones;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;


    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================

    public Producto() {

        this.factorConversion = BigDecimal.ONE;

        this.precioCompra = BigDecimal.ZERO;
        this.precioVenta = BigDecimal.ZERO;
        this.margenPorcentaje = BigDecimal.ZERO;

        this.stockMinimo = BigDecimal.ZERO;

        this.controlaStock = true;
        this.permiteVentaSinStock = false;
        this.pesable = false;
        this.permiteDescuento = false;
        this.activo = true;
    }


    // =========================================================
    // CONSTRUCTOR COMPLETO
    // =========================================================

    public Producto(
            int idProducto,
            String codigo,
            String codigoInterno,
            String codigoBarra,
            String nombre,
            String descripcion,
            String ubicacion,
            Rubro rubro,
            Categoria categoria,
            Marca marca,
            UnidadMedida unidadCompra,
            UnidadMedida unidadVenta,
            BigDecimal factorConversion,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            BigDecimal margenPorcentaje,
            TipoIva tipoIva,
            BigDecimal stockMinimo,
            BigDecimal stockMaximo,
            boolean controlaStock,
            boolean permiteVentaSinStock,
            boolean pesable,
            boolean permiteDescuento,
            boolean activo,
            String observaciones,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaModificacion) {

        this.idProducto = idProducto;
        this.codigo = codigo;
        this.codigoInterno = codigoInterno;
        this.codigoBarra = codigoBarra;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.rubro = rubro;
        this.categoria = categoria;
        this.marca = marca;
        this.unidadCompra = unidadCompra;
        this.unidadVenta = unidadVenta;
        this.factorConversion = factorConversion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.margenPorcentaje = margenPorcentaje;
        this.tipoIva = tipoIva;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.controlaStock = controlaStock;
        this.permiteVentaSinStock = permiteVentaSinStock;
        this.pesable = pesable;
        this.permiteDescuento = permiteDescuento;
        this.activo = activo;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public UnidadMedida getUnidadCompra() {
        return unidadCompra;
    }

    public void setUnidadCompra(UnidadMedida unidadCompra) {
        this.unidadCompra = unidadCompra;
    }

    public UnidadMedida getUnidadVenta() {
        return unidadVenta;
    }

    public void setUnidadVenta(UnidadMedida unidadVenta) {
        this.unidadVenta = unidadVenta;
    }

    public BigDecimal getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(BigDecimal factorConversion) {
        this.factorConversion = factorConversion;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getMargenPorcentaje() {
        return margenPorcentaje;
    }

    public void setMargenPorcentaje(BigDecimal margenPorcentaje) {
        this.margenPorcentaje = margenPorcentaje;
    }

    public TipoIva getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(BigDecimal stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public boolean isControlaStock() {
        return controlaStock;
    }

    public void setControlaStock(boolean controlaStock) {
        this.controlaStock = controlaStock;
    }

    public boolean isPermiteVentaSinStock() {
        return permiteVentaSinStock;
    }

    public void setPermiteVentaSinStock(boolean permiteVentaSinStock) {
        this.permiteVentaSinStock = permiteVentaSinStock;
    }

    public boolean isPesable() {
        return pesable;
    }

    public void setPesable(boolean pesable) {
        this.pesable = pesable;
    }

    public boolean isPermiteDescuento() {
        return permiteDescuento;
    }

    public void setPermiteDescuento(boolean permiteDescuento) {
        this.permiteDescuento = permiteDescuento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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


    @Override
    public String toString() {
        return nombre;
    }
}