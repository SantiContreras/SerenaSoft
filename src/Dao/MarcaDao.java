package Dao;

import Configuracion.conexion;
import model.Marca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class MarcaDao {

    // =========================================================
    // GUARDAR
    // =========================================================
    public boolean guardar(Marca marca) {

        String sql
                = "INSERT INTO marca "
                + "(nombre, descripcion, activo) "
                + "VALUES (?, ?, ?)";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setBoolean(3, marca.isActivo());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        marca.setIdMarca(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar marca: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================
    public boolean actualizar(Marca marca) {

        String sql
                = "UPDATE marca "
                + "SET nombre = ?, "
                + "descripcion = ?, "
                + "activo = ? "
                + "WHERE id_marca = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setBoolean(3, marca.isActivo());
            ps.setInt(4, marca.getIdMarca());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar marca: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================
    public Marca buscarPorId(int idMarca) {

        String sql
                = "SELECT * "
                + "FROM marca "
                + "WHERE id_marca = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idMarca);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearMarca(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar marca por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // BUSCAR POR NOMBRE
    // =========================================================
    public Marca buscarPorNombre(String nombre) {

        String sql
                = "SELECT * "
                + "FROM marca "
                + "WHERE nombre = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearMarca(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar marca por nombre: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // VERIFICAR NOMBRE
    // =========================================================
    public boolean existeNombre(String nombre) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM marca "
                + "WHERE nombre = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar nombre de marca: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // =========================================================
    // VERIFICAR NOMBRE AL EDITAR
    // =========================================================
    public boolean existeNombreEnOtraMarca(String nombre, int idMarca) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM marca "
                + "WHERE nombre = ? "
                + "AND id_marca <> ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, idMarca);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar marca duplicada: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // =========================================================
    // LISTAR TODAS
    // =========================================================
    public List<Marca> listarTodas() {

        List<Marca> lista = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM marca "
                + "ORDER BY nombre";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMarca(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar marcas: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // LISTAR ACTIVAS
    // =========================================================
    public List<Marca> listarActivas() {
        List<Marca> lista = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM marca "
                + "WHERE activo = 1 "
                + "ORDER BY nombre";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMarca(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar marcas activas: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================
    public boolean cambiarEstado(int idMarca, boolean activo) {

        String sql
                = "UPDATE marca "
                + "SET activo = ? "
                + "WHERE id_marca = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idMarca);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado de marca: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // MAPEAR RESULTSET -> MARCA
    // =========================================================
    private Marca mapearMarca(ResultSet rs) throws SQLException {

        Marca marca = new Marca();

        marca.setIdMarca(rs.getInt("id_marca"));
        marca.setNombre(rs.getString("nombre"));
        marca.setDescripcion(rs.getString("descripcion"));
        marca.setActivo(rs.getBoolean("activo"));

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");

        if (fechaCreacion != null) {
            marca.setFechaCreacion(
                    fechaCreacion.toLocalDateTime()
            );
        }

        Timestamp fechaModificacion = rs.getTimestamp("fecha_modificacion");

        if (fechaModificacion != null) {

            marca.setFechaModificacion(
                    fechaModificacion.toLocalDateTime()
            );
        }

        return marca;
    }
}
