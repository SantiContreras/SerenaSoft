package Dao;

import Configuracion.conexion;
import model.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class RolDao {

    // =========================================================
    // LISTAR SOLO ROLES ACTIVOS
    // =========================================================

    public List<Rol> listarActivos() {

        List<Rol> roles =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_rol,
                    nombre,
                    descripcion,
                    activo
                FROM rol
                WHERE activo = TRUE
                ORDER BY nombre
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                roles.add(
                        mapearRol(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar roles activos: "
                    + e.getMessage()
            );
        }

        return roles;
    }


    // =========================================================
    // LISTAR TODOS LOS ROLES
    // =========================================================

    public List<Rol> listarTodos() {

        List<Rol> roles =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_rol,
                    nombre,
                    descripcion,
                    activo
                FROM rol
                ORDER BY nombre
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                roles.add(
                        mapearRol(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar todos los roles: "
                    + e.getMessage()
            );
        }

        return roles;
    }


    // =========================================================
    // BUSCAR ROL POR ID
    // =========================================================

    public Rol buscarPorId(
            int idRol) {

        String sql = """
                SELECT
                    id_rol,
                    nombre,
                    descripcion,
                    activo
                FROM rol
                WHERE id_rol = ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idRol
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return mapearRol(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar rol por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR ROL POR NOMBRE
    // =========================================================

    public Rol buscarPorNombre(
            String nombre) {

        String sql = """
                SELECT
                    id_rol,
                    nombre,
                    descripcion,
                    activo
                FROM rol
                WHERE UPPER(nombre) = UPPER(?)
                LIMIT 1
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    nombre
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return mapearRol(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar rol por nombre: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // MAPEAR RESULTSET A OBJETO ROL
    // =========================================================

    private Rol mapearRol(
            ResultSet rs)
            throws SQLException {

        Rol rol =
                new Rol();

        rol.setIdRol(
                rs.getInt("id_rol")
        );

        rol.setNombre(
                rs.getString("nombre")
        );

        rol.setDescripcion(
                rs.getString("descripcion")
        );

        rol.setActivo(
                rs.getBoolean("activo")
        );

        return rol;
    }
}