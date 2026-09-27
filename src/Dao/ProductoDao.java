package Dao;

import Configuracion.conexion;

import model.Categoria;
import model.Marca;
import model.Producto;
import model.Rubro;
import model.TipoIva;
import model.UnidadMedida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    private static final String SELECT_COMPLETO =
            "SELECT " +
            "p.*, " +

            "r.nombre AS rubro_nombre, " +
            "r.descripcion AS rubro_descripcion, " +
            "r.activo AS rubro_activo, " +

            "c.nombre AS categoria_nombre, " +
            "c.descripcion AS categoria_descripcion, " +
            "c.activo AS categoria_activo, " +

            "m.nombre AS marca_nombre, " +
            "m.descripcion AS marca_descripcion, " +
            "m.activo AS marca_activo, " +

            "uc.codigo AS uc_codigo, " +
            "uc.nombre AS uc_nombre, " +
            "uc.permite_decimales AS uc_decimales, " +
            "uc.activo AS uc_activo, " +

            "uv.codigo AS uv_codigo, " +
            "uv.nombre AS uv_nombre, " +
            "uv.permite_decimales AS uv_decimales, " +
            "uv.activo AS uv_activo, " +

            "iva.codigo AS iva_codigo, " +
            "iva.nombre AS iva_nombre, " +
            "iva.porcentaje AS iva_porcentaje, " +
            "iva.activo AS iva_activo " +

            "FROM producto p " +

            "LEFT JOIN rubro r " +
            "ON p.id_rubro = r.id_rubro " +

            "LEFT JOIN categoria c " +
            "ON p.id_categoria = c.id_categoria " +

            "LEFT JOIN marca m " +
            "ON p.id_marca = m.id_marca " +

            "LEFT JOIN unidad_medida uc " +
            "ON p.id_unidad_compra = uc.id_unidad " +

            "LEFT JOIN unidad_medida uv " +
            "ON p.id_unidad_venta = uv.id_unidad " +

            "LEFT JOIN tipo_iva iva " +
            "ON p.id_iva = iva.id_iva ";


    // =========================================================
    // GUARDAR
    // =========================================================

    public boolean guardar(Producto p) {

        String sql =
                "INSERT INTO producto (" +
                "codigo, codigo_interno, codigo_barra, " +
                "nombre, descripcion, ubicacion, " +
                "id_rubro, id_categoria, id_marca, " +
                "id_unidad_compra, id_unidad_venta, " +
                "factor_conversion, precio_compra, precio_venta, " +
                "margen_porcentaje, id_iva, " +
                "stock_minimo, stock_maximo, " +
                "controla_stock, permite_venta_sin_stock, " +
                "es_pesable, permite_descuento, activo, " +
                "observaciones" +
                ") VALUES (" +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
                ")";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            cargarParametros(ps, p);

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        p.setIdProducto(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar producto: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(Producto p) {

        String sql =
                "UPDATE producto SET " +
                "codigo=?, codigo_interno=?, codigo_barra=?, " +
                "nombre=?, descripcion=?, ubicacion=?, " +
                "id_rubro=?, id_categoria=?, id_marca=?, " +
                "id_unidad_compra=?, id_unidad_venta=?, " +
                "factor_conversion=?, precio_compra=?, precio_venta=?, " +
                "margen_porcentaje=?, id_iva=?, " +
                "stock_minimo=?, stock_maximo=?, " +
                "controla_stock=?, permite_venta_sin_stock=?, " +
                "es_pesable=?, permite_descuento=?, activo=?, " +
                "observaciones=? " +
                "WHERE id_producto=?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            cargarParametros(ps, p);

            ps.setInt(
                    25,
                    p.getIdProducto()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Producto buscarPorId(int idProducto) {

        String sql =
                SELECT_COMPLETO +
                "WHERE p.id_producto = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar producto por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public Producto buscarPorCodigo(String codigo) {

        String sql =
                SELECT_COMPLETO +
                "WHERE p.codigo = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar producto por código: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CÓDIGO DE BARRAS
    // =========================================================

    public Producto buscarPorCodigoBarra(
            String codigoBarra) {

        String sql =
                SELECT_COMPLETO +
                "WHERE p.codigo_barra = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoBarra);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar producto por código de barras: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // VERIFICAR CÓDIGO
    // =========================================================

    public boolean existeCodigo(String codigo) {

        return existeValor(
                "codigo",
                codigo,
                null
        );
    }


    public boolean existeCodigoEnOtroProducto(
            String codigo,
            int idProducto) {

        return existeValor(
                "codigo",
                codigo,
                idProducto
        );
    }


    public boolean existeCodigoBarra(
            String codigoBarra) {

        return existeValor(
                "codigo_barra",
                codigoBarra,
                null
        );
    }


    public boolean existeCodigoBarraEnOtroProducto(
            String codigoBarra,
            int idProducto) {

        return existeValor(
                "codigo_barra",
                codigoBarra,
                idProducto
        );
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Producto> listarTodos() {

        List<Producto> lista =
                new ArrayList<>();

        String sql =
                SELECT_COMPLETO +
                "ORDER BY p.nombre";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar productos: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Producto> listarActivos() {

        List<Producto> lista =
                new ArrayList<>();

        String sql =
                SELECT_COMPLETO +
                "WHERE p.activo = 1 " +
                "ORDER BY p.nombre";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar productos activos: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            int idProducto,
            boolean activo) {

        String sql =
                "UPDATE producto " +
                "SET activo=? " +
                "WHERE id_producto=?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado del producto: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // CARGAR PARÁMETROS
    // =========================================================

    private void cargarParametros(
            PreparedStatement ps,
            Producto p)
            throws SQLException {

        ps.setString(1, p.getCodigo());
        ps.setString(2, p.getCodigoInterno());
        ps.setString(3, p.getCodigoBarra());

        ps.setString(4, p.getNombre());
        ps.setString(5, p.getDescripcion());
        ps.setString(6, p.getUbicacion());

        setIdNullable(
                ps,
                7,
                p.getRubro() == null
                        ? null
                        : p.getRubro().getIdRubro()
        );

        setIdNullable(
                ps,
                8,
                p.getCategoria() == null
                        ? null
                        : p.getCategoria().getIdCategoria()
        );

        setIdNullable(
                ps,
                9,
                p.getMarca() == null
                        ? null
                        : p.getMarca().getIdMarca()
        );

        setIdNullable(
                ps,
                10,
                p.getUnidadCompra() == null
                        ? null
                        : p.getUnidadCompra().getIdUnidad()
        );

        setIdNullable(
                ps,
                11,
                p.getUnidadVenta() == null
                        ? null
                        : p.getUnidadVenta().getIdUnidad()
        );

        ps.setBigDecimal(
                12,
                p.getFactorConversion()
        );

        ps.setBigDecimal(
                13,
                p.getPrecioCompra()
        );

        ps.setBigDecimal(
                14,
                p.getPrecioVenta()
        );

        ps.setBigDecimal(
                15,
                p.getMargenPorcentaje()
        );

        setIdNullable(
                ps,
                16,
                p.getTipoIva() == null
                        ? null
                        : p.getTipoIva().getIdIva()
        );

        ps.setBigDecimal(
                17,
                p.getStockMinimo()
        );

        if (p.getStockMaximo() == null) {

            ps.setNull(
                    18,
                    Types.DECIMAL
            );

        } else {

            ps.setBigDecimal(
                    18,
                    p.getStockMaximo()
            );
        }

        ps.setBoolean(
                19,
                p.isControlaStock()
        );

        ps.setBoolean(
                20,
                p.isPermiteVentaSinStock()
        );

        ps.setBoolean(
                21,
                p.isPesable()
        );

        ps.setBoolean(
                22,
                p.isPermiteDescuento()
        );

        ps.setBoolean(
                23,
                p.isActivo()
        );

        ps.setString(
                24,
                p.getObservaciones()
        );
    }


    // =========================================================
    // ID NULLABLE
    // =========================================================

    private void setIdNullable(
            PreparedStatement ps,
            int indice,
            Integer id)
            throws SQLException {

        if (id == null) {

            ps.setNull(
                    indice,
                    Types.INTEGER
            );

        } else {

            ps.setInt(
                    indice,
                    id
            );
        }
    }


    // =========================================================
    // VERIFICAR VALORES ÚNICOS
    // =========================================================

    private boolean existeValor(
            String campo,
            String valor,
            Integer excluirId) {

        String sql =
                "SELECT COUNT(*) FROM producto " +
                "WHERE " + campo + " = ?";

        if (excluirId != null) {
            sql += " AND id_producto <> ?";
        }

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, valor);

            if (excluirId != null) {
                ps.setInt(2, excluirId);
            }

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar producto duplicado: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // MAPEAR PRODUCTO
    // =========================================================

    private Producto mapearProducto(
            ResultSet rs)
            throws SQLException {

        Producto p = new Producto();

        p.setIdProducto(
                rs.getInt("id_producto")
        );

        p.setCodigo(
                rs.getString("codigo")
        );

        p.setCodigoInterno(
                rs.getString("codigo_interno")
        );

        p.setCodigoBarra(
                rs.getString("codigo_barra")
        );

        p.setNombre(
                rs.getString("nombre")
        );

        p.setDescripcion(
                rs.getString("descripcion")
        );

        p.setUbicacion(
                rs.getString("ubicacion")
        );

        p.setFactorConversion(
                rs.getBigDecimal("factor_conversion")
        );

        p.setPrecioCompra(
                rs.getBigDecimal("precio_compra")
        );

        p.setPrecioVenta(
                rs.getBigDecimal("precio_venta")
        );

        p.setMargenPorcentaje(
                rs.getBigDecimal("margen_porcentaje")
        );

        p.setStockMinimo(
                rs.getBigDecimal("stock_minimo")
        );

        p.setStockMaximo(
                rs.getBigDecimal("stock_maximo")
        );

        p.setControlaStock(
                rs.getBoolean("controla_stock")
        );

        p.setPermiteVentaSinStock(
                rs.getBoolean("permite_venta_sin_stock")
        );

        p.setPesable(
                rs.getBoolean("es_pesable")
        );

        p.setPermiteDescuento(
                rs.getBoolean("permite_descuento")
        );

        p.setActivo(
                rs.getBoolean("activo")
        );

        p.setObservaciones(
                rs.getString("observaciones")
        );


        // RUBRO

        int idRubro =
                rs.getInt("id_rubro");

        if (!rs.wasNull()) {

            Rubro rubro = new Rubro();

            rubro.setIdRubro(idRubro);
            rubro.setNombre(
                    rs.getString("rubro_nombre")
            );
            rubro.setDescripcion(
                    rs.getString("rubro_descripcion")
            );
            rubro.setActivo(
                    rs.getBoolean("rubro_activo")
            );

            p.setRubro(rubro);
        }


        // CATEGORÍA

        int idCategoria =
                rs.getInt("id_categoria");

        if (!rs.wasNull()) {

            Categoria categoria =
                    new Categoria();

            categoria.setIdCategoria(
                    idCategoria
            );

            categoria.setNombre(
                    rs.getString("categoria_nombre")
            );

            categoria.setDescripcion(
                    rs.getString("categoria_descripcion")
            );

            categoria.setActivo(
                    rs.getBoolean("categoria_activo")
            );

            p.setCategoria(categoria);
        }


        // MARCA

        int idMarca =
                rs.getInt("id_marca");

        if (!rs.wasNull()) {

            Marca marca = new Marca();

            marca.setIdMarca(idMarca);

            marca.setNombre(
                    rs.getString("marca_nombre")
            );

            marca.setDescripcion(
                    rs.getString("marca_descripcion")
            );

            marca.setActivo(
                    rs.getBoolean("marca_activo")
            );

            p.setMarca(marca);
        }


        // UNIDAD COMPRA

        int idUnidadCompra =
                rs.getInt("id_unidad_compra");

        if (!rs.wasNull()) {

            UnidadMedida unidad =
                    new UnidadMedida();

            unidad.setIdUnidad(
                    idUnidadCompra
            );

            unidad.setCodigo(
                    rs.getString("uc_codigo")
            );

            unidad.setNombre(
                    rs.getString("uc_nombre")
            );

            unidad.setPermiteDecimales(
                    rs.getBoolean("uc_decimales")
            );

            unidad.setActivo(
                    rs.getBoolean("uc_activo")
            );

            p.setUnidadCompra(unidad);
        }


        // UNIDAD VENTA

        int idUnidadVenta =
                rs.getInt("id_unidad_venta");

        if (!rs.wasNull()) {

            UnidadMedida unidad =
                    new UnidadMedida();

            unidad.setIdUnidad(
                    idUnidadVenta
            );

            unidad.setCodigo(
                    rs.getString("uv_codigo")
            );

            unidad.setNombre(
                    rs.getString("uv_nombre")
            );

            unidad.setPermiteDecimales(
                    rs.getBoolean("uv_decimales")
            );

            unidad.setActivo(
                    rs.getBoolean("uv_activo")
            );

            p.setUnidadVenta(unidad);
        }


        // IVA

        int idIva =
                rs.getInt("id_iva");

        if (!rs.wasNull()) {

            TipoIva iva = new TipoIva();

            iva.setIdIva(idIva);

            iva.setCodigo(
                    rs.getString("iva_codigo")
            );

            iva.setNombre(
                    rs.getString("iva_nombre")
            );

            iva.setPorcentaje(
                    rs.getBigDecimal("iva_porcentaje")
            );

            iva.setActivo(
                    rs.getBoolean("iva_activo")
            );

            p.setTipoIva(iva);
        }


        // FECHAS

        Timestamp creacion =
                rs.getTimestamp("fecha_creacion");

        if (creacion != null) {

            p.setFechaCreacion(
                    creacion.toLocalDateTime()
            );
        }

        Timestamp modificacion =
                rs.getTimestamp("fecha_modificacion");

        if (modificacion != null) {

            p.setFechaModificacion(
                    modificacion.toLocalDateTime()
            );
        }

        return p;
    }
}