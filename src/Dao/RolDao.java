package Dao;

import Configuracion.conexion;
import model.Rol;
import model.Permiso;
import java.util.Set;
import java.util.HashSet;
import java.sql.Statement;

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
    /** Catálogo de permisos activos. */
    public List<Permiso> listarPermisos() throws SQLException {
        List<Permiso> salida = new ArrayList<>();
        String sql = "SELECT id_permiso,codigo,nombre,modulo,descripcion,activo FROM permiso WHERE activo=TRUE ORDER BY modulo,nombre";
        try(Connection cn=conexion.getConexion(); PreparedStatement ps=cn.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                Permiso p=new Permiso(); p.setIdPermiso(rs.getInt("id_permiso"));p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));p.setModulo(rs.getString("modulo"));
                p.setDescripcion(rs.getString("descripcion"));p.setActivo(rs.getBoolean("activo"));salida.add(p);
            }
        }
        return salida;
    }
    public Set<Integer> permisosDeRol(int idRol) throws SQLException {
        Set<Integer> ids=new HashSet<>();
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement("SELECT id_permiso FROM rol_permiso WHERE id_rol=?")) {
            ps.setInt(1,idRol);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())ids.add(rs.getInt(1));}
        }
        return ids;
    }
    /** Actualización atómica de permisos. Nunca se altera el rol administrador desde esta pantalla. */
    public void guardarPermisos(int idRol, Set<Integer> ids) throws SQLException {
        try(Connection cn=conexion.getConexion()) {
            cn.setAutoCommit(false);
            try {
                String nombre;
                try(PreparedStatement ps=cn.prepareStatement("SELECT nombre FROM rol WHERE id_rol=? FOR UPDATE")) {
                    ps.setInt(1,idRol);try(ResultSet rs=ps.executeQuery()) {if(!rs.next())throw new SQLException("Rol inexistente");nombre=rs.getString(1);}
                }
                if("ADMINISTRADOR".equalsIgnoreCase(nombre)) throw new SQLException("Los permisos del administrador están protegidos.");
                try(PreparedStatement ps=cn.prepareStatement("DELETE FROM rol_permiso WHERE id_rol=?")) {ps.setInt(1,idRol);ps.executeUpdate();}
                try(PreparedStatement ps=cn.prepareStatement("INSERT INTO rol_permiso (id_rol,id_permiso) SELECT ?,id_permiso FROM permiso WHERE id_permiso=? AND activo=TRUE")) {
                    for(Integer id:ids){ps.setInt(1,idRol);ps.setInt(2,id);if(ps.executeUpdate()!=1)throw new SQLException("Permiso inválido: "+id);}
                }
                cn.commit();
            } catch(SQLException | RuntimeException ex){cn.rollback();throw ex;} finally {cn.setAutoCommit(true);}
        }
    }
}