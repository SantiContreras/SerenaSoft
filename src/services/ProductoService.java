package services;

import Dao.CategoriaDao;
import Dao.MarcaDao;
import Dao.ProductoDao;
import Dao.RubroDao;
import Dao.TipoIvaDao;
import Dao.UnidadMedidaDao;

import model.Categoria;
import model.Marca;
import model.Producto;
import model.Rubro;
import model.TipoIva;
import model.UnidadMedida;

import java.math.BigDecimal;
import java.util.List;

public class ProductoService {

    private final ProductoDao productoDao;
    private final RubroDao rubroDao;
    private final CategoriaDao categoriaDao;
    private final MarcaDao marcaDao;
    private final UnidadMedidaDao unidadDao;
    private final TipoIvaDao tipoIvaDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProductoService() {

        productoDao = new ProductoDao();
        rubroDao = new RubroDao();
        categoriaDao = new CategoriaDao();
        marcaDao = new MarcaDao();
        unidadDao = new UnidadMedidaDao();
        tipoIvaDao = new TipoIvaDao();
    }


    // =========================================================
    // CREAR PRODUCTO
    // =========================================================

    public ResultadoOperacion crearProducto(
            String codigo,
            String codigoInterno,
            String codigoBarra,
            String nombre,
            String descripcion,
            String ubicacion,
            Integer idRubro,
            Integer idCategoria,
            Integer idMarca,
            Integer idUnidadCompra,
            Integer idUnidadVenta,
            BigDecimal factorConversion,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            BigDecimal margenPorcentaje,
            Integer idIva,
            BigDecimal stockMinimo,
            BigDecimal stockMaximo,
            boolean controlaStock,
            boolean permiteVentaSinStock,
            boolean esPesable,
            boolean permiteDescuento,
            String observaciones) {

        codigo = limpiar(codigo);
        codigoInterno = limpiar(codigoInterno);
        codigoBarra = limpiar(codigoBarra);
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
        ubicacion = limpiar(ubicacion);
        observaciones = limpiar(observaciones);


        // =====================================================
        // VALIDAR DATOS BÁSICOS
        // =====================================================

        ResultadoOperacion validacion =
                validarDatosBasicos(
                        codigo,
                        codigoInterno,
                        codigoBarra,
                        nombre,
                        descripcion,
                        ubicacion,
                        factorConversion,
                        precioCompra,
                        precioVenta,
                        margenPorcentaje,
                        stockMinimo,
                        stockMaximo
                );

        if (!validacion.isExitoso()) {
            return validacion;
        }


        // =====================================================
        // VALIDAR DUPLICADOS
        // =====================================================

        if (productoDao.existeCodigo(codigo)) {

            return ResultadoOperacion.error(
                    "Ya existe un producto con el código "
                    + codigo + "."
            );
        }

        if (codigoBarra != null
                && productoDao.existeCodigoBarra(codigoBarra)) {

            return ResultadoOperacion.error(
                    "Ya existe un producto con el código de barras "
                    + codigoBarra + "."
            );
        }


        // =====================================================
        // OBTENER Y VALIDAR RELACIONES
        // =====================================================

        Rubro rubro = obtenerRubro(idRubro);

        if (idRubro != null && rubro == null) {

            return ResultadoOperacion.error(
                    "El rubro seleccionado no existe o está inactivo."
            );
        }


        Categoria categoria =
                obtenerCategoria(idCategoria);

        if (idCategoria != null && categoria == null) {

            return ResultadoOperacion.error(
                    "La categoría seleccionada no existe o está inactiva."
            );
        }


        ResultadoOperacion validacionCategoria =
                validarCategoriaRubro(
                        rubro,
                        categoria
                );

        if (!validacionCategoria.isExitoso()) {
            return validacionCategoria;
        }


        Marca marca = obtenerMarca(idMarca);

        if (idMarca != null && marca == null) {

            return ResultadoOperacion.error(
                    "La marca seleccionada no existe o está inactiva."
            );
        }


        UnidadMedida unidadCompra =
                obtenerUnidad(idUnidadCompra);

        if (idUnidadCompra != null
                && unidadCompra == null) {

            return ResultadoOperacion.error(
                    "La unidad de compra no existe o está inactiva."
            );
        }


        UnidadMedida unidadVenta =
                obtenerUnidad(idUnidadVenta);

        if (idUnidadVenta != null
                && unidadVenta == null) {

            return ResultadoOperacion.error(
                    "La unidad de venta no existe o está inactiva."
            );
        }


        TipoIva tipoIva =
                obtenerIva(idIva);

        if (idIva != null && tipoIva == null) {

            return ResultadoOperacion.error(
                    "El tipo de IVA no existe o está inactivo."
            );
        }


        // =====================================================
        // CREAR OBJETO
        // =====================================================

        Producto producto = new Producto();

        cargarProducto(
                producto,
                codigo,
                codigoInterno,
                codigoBarra,
                nombre,
                descripcion,
                ubicacion,
                rubro,
                categoria,
                marca,
                unidadCompra,
                unidadVenta,
                factorConversion,
                precioCompra,
                precioVenta,
                margenPorcentaje,
                tipoIva,
                stockMinimo,
                stockMaximo,
                controlaStock,
                permiteVentaSinStock,
                esPesable,
                permiteDescuento,
                observaciones
        );

        producto.setActivo(true);


        // =====================================================
        // GUARDAR
        // =====================================================

        if (productoDao.guardar(producto)) {

            return ResultadoOperacion.ok(
                    "Producto creado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear el producto."
        );
    }


    // =========================================================
    // EDITAR PRODUCTO
    // =========================================================

    public ResultadoOperacion editarProducto(
            int idProducto,
            String codigo,
            String codigoInterno,
            String codigoBarra,
            String nombre,
            String descripcion,
            String ubicacion,
            Integer idRubro,
            Integer idCategoria,
            Integer idMarca,
            Integer idUnidadCompra,
            Integer idUnidadVenta,
            BigDecimal factorConversion,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            BigDecimal margenPorcentaje,
            Integer idIva,
            BigDecimal stockMinimo,
            BigDecimal stockMaximo,
            boolean controlaStock,
            boolean permiteVentaSinStock,
            boolean esPesable,
            boolean permiteDescuento,
            String observaciones) {


        // =====================================================
        // VALIDAR ID
        // =====================================================

        if (idProducto <= 0) {

            return ResultadoOperacion.error(
                    "El ID del producto no es válido."
            );
        }


        // =====================================================
        // BUSCAR PRODUCTO ACTUAL
        // =====================================================

        Producto producto =
                productoDao.buscarPorId(idProducto);

        if (producto == null) {

            return ResultadoOperacion.error(
                    "El producto no existe."
            );
        }


        // =====================================================
        // LIMPIAR DATOS
        // =====================================================

        codigo = limpiar(codigo);
        codigoInterno = limpiar(codigoInterno);
        codigoBarra = limpiar(codigoBarra);
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
        ubicacion = limpiar(ubicacion);
        observaciones = limpiar(observaciones);


        // =====================================================
        // VALIDACIONES GENERALES
        // =====================================================

        ResultadoOperacion validacion =
                validarDatosBasicos(
                        codigo,
                        codigoInterno,
                        codigoBarra,
                        nombre,
                        descripcion,
                        ubicacion,
                        factorConversion,
                        precioCompra,
                        precioVenta,
                        margenPorcentaje,
                        stockMinimo,
                        stockMaximo
                );

        if (!validacion.isExitoso()) {
            return validacion;
        }


        // =====================================================
        // VALIDAR CÓDIGO DUPLICADO
        // =====================================================

        if (productoDao.existeCodigoEnOtroProducto(
                codigo,
                idProducto)) {

            return ResultadoOperacion.error(
                    "Ya existe otro producto con el código "
                    + codigo + "."
            );
        }


        // =====================================================
        // VALIDAR CÓDIGO DE BARRAS DUPLICADO
        // =====================================================

        if (codigoBarra != null
                && productoDao.existeCodigoBarraEnOtroProducto(
                        codigoBarra,
                        idProducto)) {

            return ResultadoOperacion.error(
                    "Ya existe otro producto con el código de barras "
                    + codigoBarra + "."
            );
        }


        // =====================================================
        // RUBRO
        // =====================================================

        Rubro rubro =
                obtenerRubro(idRubro);

        if (idRubro != null
                && rubro == null) {

            return ResultadoOperacion.error(
                    "El rubro seleccionado no existe o está inactivo."
            );
        }


        // =====================================================
        // CATEGORÍA
        // =====================================================

        Categoria categoria =
                obtenerCategoria(idCategoria);

        if (idCategoria != null
                && categoria == null) {

            return ResultadoOperacion.error(
                    "La categoría seleccionada no existe o está inactiva."
            );
        }


        ResultadoOperacion validacionCategoria =
                validarCategoriaRubro(
                        rubro,
                        categoria
                );

        if (!validacionCategoria.isExitoso()) {
            return validacionCategoria;
        }


        // =====================================================
        // MARCA
        // =====================================================

        Marca marca =
                obtenerMarca(idMarca);

        if (idMarca != null
                && marca == null) {

            return ResultadoOperacion.error(
                    "La marca seleccionada no existe o está inactiva."
            );
        }


        // =====================================================
        // UNIDAD COMPRA
        // =====================================================

        UnidadMedida unidadCompra =
                obtenerUnidad(idUnidadCompra);

        if (idUnidadCompra != null
                && unidadCompra == null) {

            return ResultadoOperacion.error(
                    "La unidad de compra no existe o está inactiva."
            );
        }


        // =====================================================
        // UNIDAD VENTA
        // =====================================================

        UnidadMedida unidadVenta =
                obtenerUnidad(idUnidadVenta);

        if (idUnidadVenta != null
                && unidadVenta == null) {

            return ResultadoOperacion.error(
                    "La unidad de venta no existe o está inactiva."
            );
        }


        // =====================================================
        // IVA
        // =====================================================

        TipoIva tipoIva =
                obtenerIva(idIva);

        if (idIva != null
                && tipoIva == null) {

            return ResultadoOperacion.error(
                    "El tipo de IVA no existe o está inactivo."
            );
        }


        // =====================================================
        // ACTUALIZAR OBJETO
        // =====================================================

        cargarProducto(
                producto,
                codigo,
                codigoInterno,
                codigoBarra,
                nombre,
                descripcion,
                ubicacion,
                rubro,
                categoria,
                marca,
                unidadCompra,
                unidadVenta,
                factorConversion,
                precioCompra,
                precioVenta,
                margenPorcentaje,
                tipoIva,
                stockMinimo,
                stockMaximo,
                controlaStock,
                permiteVentaSinStock,
                esPesable,
                permiteDescuento,
                observaciones
        );


        // =====================================================
        // ACTUALIZAR BASE DE DATOS
        // =====================================================

        if (productoDao.actualizar(producto)) {

            return ResultadoOperacion.ok(
                    "Producto actualizado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar el producto."
        );
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Producto buscarPorId(int idProducto) {

        if (idProducto <= 0) {
            return null;
        }

        return productoDao.buscarPorId(
                idProducto
        );
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public Producto buscarPorCodigo(
            String codigo) {

        codigo = limpiar(codigo);

        if (codigo == null) {
            return null;
        }

        return productoDao.buscarPorCodigo(
                codigo
        );
    }


    // =========================================================
    // BUSCAR POR CÓDIGO DE BARRAS
    // =========================================================

    public Producto buscarPorCodigoBarra(
            String codigoBarra) {

        codigoBarra =
                limpiar(codigoBarra);

        if (codigoBarra == null) {
            return null;
        }

        return productoDao.buscarPorCodigoBarra(
                codigoBarra
        );
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Producto> listarTodos() {

        return productoDao.listarTodos();
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Producto> listarActivos() {

        return productoDao.listarActivos();
    }


    // =========================================================
    // DESACTIVAR
    // =========================================================

    public ResultadoOperacion desactivarProducto(
            int idProducto) {

        Producto producto =
                productoDao.buscarPorId(idProducto);

        if (producto == null) {

            return ResultadoOperacion.error(
                    "El producto no existe."
            );
        }

        if (!producto.isActivo()) {

            return ResultadoOperacion.error(
                    "El producto ya está inactivo."
            );
        }

        if (productoDao.cambiarEstado(
                idProducto,
                false)) {

            return ResultadoOperacion.ok(
                    "Producto desactivado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo desactivar el producto."
        );
    }


    // =========================================================
    // ACTIVAR
    // =========================================================

    public ResultadoOperacion activarProducto(
            int idProducto) {

        Producto producto =
                productoDao.buscarPorId(idProducto);

        if (producto == null) {

            return ResultadoOperacion.error(
                    "El producto no existe."
            );
        }

        if (producto.isActivo()) {

            return ResultadoOperacion.error(
                    "El producto ya está activo."
            );
        }

        if (productoDao.cambiarEstado(
                idProducto,
                true)) {

            return ResultadoOperacion.ok(
                    "Producto activado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo activar el producto."
        );
    }


    // =========================================================
    // CARGAR DATOS EN PRODUCTO
    // =========================================================

    private void cargarProducto(
            Producto producto,
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
            boolean esPesable,
            boolean permiteDescuento,
            String observaciones) {

        producto.setCodigo(codigo);
        producto.setCodigoInterno(codigoInterno);
        producto.setCodigoBarra(codigoBarra);

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setUbicacion(ubicacion);

        producto.setRubro(rubro);
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        producto.setUnidadCompra(unidadCompra);
        producto.setUnidadVenta(unidadVenta);

        producto.setFactorConversion(
                factorConversion
        );

        producto.setPrecioCompra(
                precioCompra
        );

        producto.setPrecioVenta(
                precioVenta
        );

        producto.setMargenPorcentaje(
                margenPorcentaje
        );

        producto.setTipoIva(tipoIva);

        producto.setStockMinimo(
                stockMinimo
        );

        producto.setStockMaximo(
                stockMaximo
        );

        producto.setControlaStock(
                controlaStock
        );

        producto.setPermiteVentaSinStock(
                permiteVentaSinStock
        );

        producto.setPesable(
                esPesable
        );

        producto.setPermiteDescuento(
                permiteDescuento
        );

        producto.setObservaciones(
                observaciones
        );
    }


    // =========================================================
    // VALIDAR DATOS BÁSICOS
    // =========================================================

    private ResultadoOperacion validarDatosBasicos(
            String codigo,
            String codigoInterno,
            String codigoBarra,
            String nombre,
            String descripcion,
            String ubicacion,
            BigDecimal factorConversion,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            BigDecimal margenPorcentaje,
            BigDecimal stockMinimo,
            BigDecimal stockMaximo) {

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código del producto es obligatorio."
            );
        }

        if (codigo.length() > 50) {

            return ResultadoOperacion.error(
                    "El código no puede superar los 50 caracteres."
            );
        }

        if (codigoInterno != null
                && codigoInterno.length() > 50) {

            return ResultadoOperacion.error(
                    "El código interno no puede superar los 50 caracteres."
            );
        }

        if (codigoBarra != null
                && codigoBarra.length() > 100) {

            return ResultadoOperacion.error(
                    "El código de barras no puede superar los 100 caracteres."
            );
        }

        if (nombre == null) {

            return ResultadoOperacion.error(
                    "El nombre del producto es obligatorio."
            );
        }

        if (nombre.length() > 180) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 180 caracteres."
            );
        }

        if (descripcion != null
                && descripcion.length() > 500) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar los 500 caracteres."
            );
        }

        if (ubicacion != null
                && ubicacion.length() > 150) {

            return ResultadoOperacion.error(
                    "La ubicación no puede superar los 150 caracteres."
            );
        }

        if (factorConversion == null
                || factorConversion.compareTo(
                        BigDecimal.ZERO) <= 0) {

            return ResultadoOperacion.error(
                    "El factor de conversión debe ser mayor que cero."
            );
        }

        if (precioCompra == null
                || precioCompra.compareTo(
                        BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El precio de compra no puede ser negativo."
            );
        }

        if (precioVenta == null
                || precioVenta.compareTo(
                        BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El precio de venta no puede ser negativo."
            );
        }

        if (margenPorcentaje == null) {

            return ResultadoOperacion.error(
                    "El margen es obligatorio."
            );
        }

        if (stockMinimo == null
                || stockMinimo.compareTo(
                        BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El stock mínimo no puede ser negativo."
            );
        }

        if (stockMaximo != null
                && stockMaximo.compareTo(
                        stockMinimo) < 0) {

            return ResultadoOperacion.error(
                    "El stock máximo no puede ser menor que el stock mínimo."
            );
        }

        return ResultadoOperacion.ok(
                "Datos válidos."
        );
    }


    // =========================================================
    // VALIDAR RELACIÓN CATEGORÍA / RUBRO
    // =========================================================

    private ResultadoOperacion validarCategoriaRubro(
            Rubro rubro,
            Categoria categoria) {

        if (rubro == null
                || categoria == null) {

            return ResultadoOperacion.ok(
                    "Relación válida."
            );
        }

        if (categoria.getRubro() != null
                && categoria.getRubro().getIdRubro()
                != rubro.getIdRubro()) {

            return ResultadoOperacion.error(
                    "La categoría seleccionada no pertenece al rubro indicado."
            );
        }

        return ResultadoOperacion.ok(
                "Relación válida."
        );
    }


    // =========================================================
    // OBTENER RUBRO
    // =========================================================

    private Rubro obtenerRubro(Integer id) {

        if (id == null) {
            return null;
        }

        Rubro rubro =
                rubroDao.buscarPorId(id);

        if (rubro == null
                || !rubro.isActivo()) {

            return null;
        }

        return rubro;
    }


    // =========================================================
    // OBTENER CATEGORÍA
    // =========================================================

    private Categoria obtenerCategoria(
            Integer id) {

        if (id == null) {
            return null;
        }

        Categoria categoria =
                categoriaDao.buscarPorId(id);

        if (categoria == null
                || !categoria.isActivo()) {

            return null;
        }

        return categoria;
    }


    // =========================================================
    // OBTENER MARCA
    // =========================================================

    private Marca obtenerMarca(Integer id) {

        if (id == null) {
            return null;
        }

        Marca marca =
                marcaDao.buscarPorId(id);

        if (marca == null
                || !marca.isActivo()) {

            return null;
        }

        return marca;
    }


    // =========================================================
    // OBTENER UNIDAD
    // =========================================================

    private UnidadMedida obtenerUnidad(
            Integer id) {

        if (id == null) {
            return null;
        }

        UnidadMedida unidad =
                unidadDao.buscarPorId(id);

        if (unidad == null
                || !unidad.isActivo()) {

            return null;
        }

        return unidad;
    }


    // =========================================================
    // OBTENER IVA
    // =========================================================

    private TipoIva obtenerIva(
            Integer id) {

        if (id == null) {
            return null;
        }

        TipoIva iva =
                tipoIvaDao.buscarPorId(id);

        if (iva == null
                || !iva.isActivo()) {

            return null;
        }

        return iva;
    }


    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        return texto.isEmpty()
                ? null
                : texto;
    }
}