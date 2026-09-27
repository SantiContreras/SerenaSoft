package Dao;

import Configuracion.conexion;
import model.TipoIva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class TipoIvaDao {

    // =========================================================
    // GUARDAR
    // =========================================================

    public boolean guardar(TipoIva tipoIva) {

        String sql =
                "INSERT INTO tipo_iva " +
                "(codigo, nombre, porcentaje, activo) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tipoIva.getCodigo());
            ps.setString(2, tipoIva.getNombre());
            ps.setBigDecimal(3, tipoIva.getPorcentaje());
            ps.setBoolean(4, tipoIva.isActivo());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        tipoIva.setIdIva(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar tipo de IVA: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(TipoIva tipoIva) {

        String sql =
                "UPDATE tipo_iva " +
                "SET codigo = ?, " +
                "nombre = ?, " +
                "porcentaje = ?, " +
                "activo = ? " +
                "WHERE id_iva = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, tipoIva.getCodigo());
            ps.setString(2, tipoIva.getNombre());
            ps.setBigDecimal(3, tipoIva.getPorcentaje());
            ps.setBoolean(4, tipoIva.isActivo());
            ps.setInt(5, tipoIva.getIdIva());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar tipo de IVA: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public TipoIva buscarPorId(int idIva) {

        String sql =
                "SELECT * FROM tipo_iva " +
                "WHERE id_iva = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idIva);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearTipoIva(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar tipo de IVA por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public TipoIva buscarPorCodigo(String codigo) {

        String sql =
                "SELECT * FROM tipo_iva " +
                "WHERE codigo = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearTipoIva(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar tipo de IVA por código: "
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
                "SELECT COUNT(*) FROM tipo_iva " +
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
                    "Error al verificar código de IVA: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // VERIFICAR CÓDIGO AL EDITAR
    // =========================================================

    public boolean existeCodigoEnOtroTipo(
            String codigo,
            int idIva) {

        String sql =
                "SELECT COUNT(*) FROM tipo_iva " +
                "WHERE codigo = ? " +
                "AND id_iva <> ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setInt(2, idIva);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar código duplicado de IVA: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<TipoIva> listarTodos() {

        List<TipoIva> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM tipo_iva " +
                "ORDER BY porcentaje";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTipoIva(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar tipos de IVA: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<TipoIva> listarActivos() {

        List<TipoIva> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM tipo_iva " +
                "WHERE activo = 1 " +
                "ORDER BY porcentaje";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTipoIva(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar tipos de IVA activos: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            int idIva,
            boolean activo) {

        String sql =
                "UPDATE tipo_iva " +
                "SET activo = ? " +
                "WHERE id_iva = ?";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idIva);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado del tipo de IVA: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAPEAR RESULTSET
    // =========================================================

    private TipoIva mapearTipoIva(ResultSet rs)
            throws SQLException {

        TipoIva tipoIva = new TipoIva();

        tipoIva.setIdIva(
                rs.getInt("id_iva")
        );

        tipoIva.setCodigo(
                rs.getString("codigo")
        );

        tipoIva.setNombre(
                rs.getString("nombre")
        );

        tipoIva.setPorcentaje(
                rs.getBigDecimal("porcentaje")
        );

        tipoIva.setActivo(
                rs.getBoolean("activo")
        );

        return tipoIva;
    }
}