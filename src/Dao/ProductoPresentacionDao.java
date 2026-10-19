package Dao;

import Configuracion.conexion;
import model.ProductoPresentacion;
import model.UnidadMedida;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a producto_presentacion. No modifica producto ni el stock. */
public class ProductoPresentacionDao {
    private static final String SELECT =
        "SELECT pp.*, um.codigo AS unidad_codigo, um.nombre AS unidad_nombre, " +
        "um.permite_decimales AS unidad_decimales, um.activo AS unidad_activa " +
        "FROM producto_presentacion pp " +
        "JOIN unidad_medida um ON um.id_unidad = pp.id_unidad_compra ";

    public List<ProductoPresentacion> listarActivasPorProducto(int idProducto) throws SQLException {
        if (idProducto <= 0) throw new IllegalArgumentException("Producto inválido.");
        String sql = SELECT + "WHERE pp.id_producto=? AND pp.activo=1 " +
                "ORDER BY pp.es_predeterminada DESC, pp.factor_conversion, pp.nombre";
        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                List<ProductoPresentacion> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public ProductoPresentacion buscarPorId(int idPresentacion) throws SQLException {
        String sql = SELECT + "WHERE pp.id_presentacion=?";
        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPresentacion);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    /** Valida presentación por producto + código + factor, sin depender del nombre. */
    public boolean existeActiva(int idProducto, String codigoUnidad, BigDecimal factor, Connection cn)
            throws SQLException {
        String sql = "SELECT 1 FROM producto_presentacion pp " +
            "JOIN unidad_medida um ON um.id_unidad=pp.id_unidad_compra " +
            "WHERE pp.id_producto=? AND pp.activo=1 AND um.activo=1 " +
            "AND UPPER(um.codigo)=UPPER(?) AND pp.factor_conversion=? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setString(2, codigoUnidad);
            ps.setBigDecimal(3, factor);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public int guardar(ProductoPresentacion p) throws SQLException {
        validar(p);
        String sql = "INSERT INTO producto_presentacion " +
            "(id_producto,nombre,id_unidad_compra,factor_conversion,es_predeterminada,activo) " +
            "VALUES (?,?,?,?,?,?)";
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,p.getIdProducto()); ps.setString(2,p.getNombre().trim());
            ps.setInt(3,p.getUnidadCompra().getIdUnidad());
            ps.setBigDecimal(4,p.getFactorConversion());
            ps.setBoolean(5,p.isPredeterminada()); ps.setBoolean(6,p.isActivo());
            if (ps.executeUpdate()!=1) throw new SQLException("No se guardó la presentación.");
            try (ResultSet rs=ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("No se obtuvo el ID generado.");
                int id=rs.getInt(1); p.setIdPresentacion(id); return id;
            }
        }
    }

    public boolean desactivar(int idPresentacion) throws SQLException {
        try (Connection cn=conexion.getConexion();
             PreparedStatement ps=cn.prepareStatement(
                "UPDATE producto_presentacion SET activo=0 WHERE id_presentacion=? AND es_predeterminada=0")) {
            ps.setInt(1,idPresentacion); return ps.executeUpdate()==1;
        }
    }

    private static void validar(ProductoPresentacion p) {
        if (p==null || p.getIdProducto()<=0 || p.getUnidadCompra()==null ||
            p.getUnidadCompra().getIdUnidad()<=0 || p.getNombre()==null ||
            p.getNombre().trim().isEmpty() || p.getNombre().trim().length()>100 ||
            p.getFactorConversion()==null || p.getFactorConversion().signum()<=0 ||
            p.getFactorConversion().scale()>4)
            throw new IllegalArgumentException("Datos de presentación inválidos.");
    }

    private static ProductoPresentacion mapear(ResultSet rs) throws SQLException {
        ProductoPresentacion p=new ProductoPresentacion();
        p.setIdPresentacion(rs.getInt("id_presentacion"));
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setFactorConversion(rs.getBigDecimal("factor_conversion"));
        p.setPredeterminada(rs.getBoolean("es_predeterminada"));
        p.setActivo(rs.getBoolean("activo"));
        Timestamp fecha=rs.getTimestamp("fecha_creacion");
        if(fecha!=null) p.setFechaCreacion(fecha.toLocalDateTime());
        UnidadMedida u=new UnidadMedida();
        u.setIdUnidad(rs.getInt("id_unidad_compra"));
        u.setCodigo(rs.getString("unidad_codigo"));
        u.setNombre(rs.getString("unidad_nombre"));
        u.setPermiteDecimales(rs.getBoolean("unidad_decimales"));
        u.setActivo(rs.getBoolean("unidad_activa"));
        p.setUnidadCompra(u);
        return p;
    }
}
