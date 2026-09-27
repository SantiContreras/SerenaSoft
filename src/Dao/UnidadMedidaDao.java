package Dao;

import Configuracion.conexion;
import model.UnidadMedida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class UnidadMedidaDao {

    // =========================================================
    // GUARDAR
    // =========================================================

    public boolean guardar(UnidadMedida unidad) {

        String sql =
                "INSERT INTO unidad_medida " +
                "(codigo, nombre, permite_decimales, activo) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, unidad.getCodigo());
            ps.setString(2, unidad.getNombre());
            ps.setBoolean(3, unidad.isPermiteDecimales());
            ps.setBoolean(4, unidad.isActivo());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        unidad.setIdUnidad(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar unidad de medida: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(UnidadMedida unidad) {

        String sql =
                "UPDATE unidad_medida " +
                "SET codigo = ?, " +
                "nombre = ?, " +
                "permite_decimales = ?, " +
                "activo = ? " +
                "WHERE id_unidad = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, unidad.getCodigo());
            ps.setString(2, unidad.getNombre());
            ps.setBoolean(3, unidad.isPermiteDecimales());
            ps.setBoolean(4, unidad.isActivo());
            ps.setInt(5, unidad.getIdUnidad());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar unidad de medida: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public UnidadMedida buscarPorId(int idUnidad) {

        String sql =
                "SELECT * FROM unidad_medida " +
                "WHERE id_unidad = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idUnidad);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearUnidad(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar unidad por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public UnidadMedida buscarPorCodigo(String codigo) {

        String sql =
                "SELECT * FROM unidad_medida " +
                "WHERE codigo = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearUnidad(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar unidad por código: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // VERIFICAR CÓDIGO
    // =========================================================

    public boolean existeCodigo(String codigo) {

        String sql =
                "SELECT COUNT(*) FROM unidad_medida " +
                "WHERE codigo = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar código: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // VERIFICAR CÓDIGO AL EDITAR
    // =========================================================

    public boolean existeCodigoEnOtraUnidad(
            String codigo,
            int idUnidad) {

        String sql =
                "SELECT COUNT(*) FROM unidad_medida " +
                "WHERE codigo = ? " +
                "AND id_unidad <> ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setInt(2, idUnidad);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar código duplicado: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // LISTAR TODAS
    // =========================================================

    public List<UnidadMedida> listarTodas() {

        List<UnidadMedida> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM unidad_medida " +
                "ORDER BY nombre";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUnidad(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar unidades de medida: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // LISTAR ACTIVAS
    // =========================================================

    public List<UnidadMedida> listarActivas() {

        List<UnidadMedida> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM unidad_medida " +
                "WHERE activo = 1 " +
                "ORDER BY nombre";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUnidad(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar unidades activas: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            int idUnidad,
            boolean activo) {

        String sql =
                "UPDATE unidad_medida " +
                "SET activo = ? " +
                "WHERE id_unidad = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idUnidad);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado de unidad: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAPEAR RESULTSET
    // =========================================================

    private UnidadMedida mapearUnidad(ResultSet rs)
            throws SQLException {

        UnidadMedida unidad = new UnidadMedida();

        unidad.setIdUnidad(
                rs.getInt("id_unidad")
        );

        unidad.setCodigo(
                rs.getString("codigo")
        );

        unidad.setNombre(
                rs.getString("nombre")
        );

        unidad.setPermiteDecimales(
                rs.getBoolean("permite_decimales")
        );

        unidad.setActivo(
                rs.getBoolean("activo")
        );

        return unidad;
    }
}