package Dao;

import Configuracion.conexion;
import model.Rubro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;


/**
 * DAO encargado de realizar las operaciones
 * de base de datos relacionadas con la tabla rubro.
 */
public class RubroDao {


    // =========================================================
    // GUARDAR RUBRO
    // =========================================================

    public boolean guardar(Rubro rubro) {

        String sql
                = "INSERT INTO rubro "
                + "(nombre, descripcion, activo) "
                + "VALUES (?, ?, ?)";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setString(
                    1,
                    rubro.getNombre()
            );

            ps.setString(
                    2,
                    rubro.getDescripcion()
            );

            ps.setBoolean(
                    3,
                    rubro.isActivo()
            );


            int filasAfectadas
                    = ps.executeUpdate();


            if (filasAfectadas > 0) {

                try (
                        ResultSet rs
                        = ps.getGeneratedKeys()
                ) {

                    if (rs.next()) {

                        rubro.setIdRubro(
                                rs.getInt(1)
                        );
                    }
                }

                return true;
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar rubro: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR RUBRO
    // =========================================================

    public boolean actualizar(Rubro rubro) {

        String sql
                = "UPDATE rubro "
                + "SET nombre = ?, "
                + "descripcion = ?, "
                + "activo = ? "
                + "WHERE id_rubro = ?";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    rubro.getNombre()
            );

            ps.setString(
                    2,
                    rubro.getDescripcion()
            );

            ps.setBoolean(
                    3,
                    rubro.isActivo()
            );

            ps.setInt(
                    4,
                    rubro.getIdRubro()
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar rubro: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR RUBRO POR ID
    // =========================================================

    public Rubro buscarPorId(int idRubro) {

        String sql
                = "SELECT "
                + "id_rubro, "
                + "nombre, "
                + "descripcion, "
                + "activo "
                + "FROM rubro "
                + "WHERE id_rubro = ?";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idRubro
            );


            try (
                    ResultSet rs
                    = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapearRubro(rs);
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar rubro por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR RUBRO POR NOMBRE
    // =========================================================

    public Rubro buscarPorNombre(String nombre) {

        String sql
                = "SELECT "
                + "id_rubro, "
                + "nombre, "
                + "descripcion, "
                + "activo "
                + "FROM rubro "
                + "WHERE nombre = ?";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );


            try (
                    ResultSet rs
                    = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapearRubro(rs);
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar rubro por nombre: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // VERIFICAR SI EXISTE UN NOMBRE
    // =========================================================

    public boolean existeNombre(String nombre) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM rubro "
                + "WHERE nombre = ?";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );


            try (
                    ResultSet rs
                    = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar nombre de rubro: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // VERIFICAR NOMBRE AL EDITAR
    // =========================================================
    /*
     * Sirve cuando editamos un rubro.
     *
     * Ejemplo:
     *
     * id 1 = BEBIDAS
     * id 2 = LIMPIEZA
     *
     * Si editamos BEBIDAS, no debe considerar
     * su propio nombre como duplicado.
     */

    public boolean existeNombreEnOtroRubro(
            String nombre,
            int idRubro
    ) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM rubro "
                + "WHERE nombre = ? "
                + "AND id_rubro <> ?";

        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );

            ps.setInt(
                    2,
                    idRubro
            );


            try (
                    ResultSet rs
                    = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar nombre "
                    + "en otro rubro: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // LISTAR TODOS LOS RUBROS
    // =========================================================

    public List<Rubro> listarTodos() {

        List<Rubro> lista
                = new ArrayList<>();


        String sql
                = "SELECT "
                + "id_rubro, "
                + "nombre, "
                + "descripcion, "
                + "activo "
                + "FROM rubro "
                + "ORDER BY nombre ASC";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql);
                ResultSet rs
                = ps.executeQuery()
        ) {

            while (rs.next()) {

                Rubro rubro
                        = mapearRubro(rs);

                lista.add(
                        rubro
                );
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al listar rubros: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // LISTAR SOLAMENTE RUBROS ACTIVOS
    // =========================================================

    public List<Rubro> listarActivos() {

        List<Rubro> lista
                = new ArrayList<>();


        String sql
                = "SELECT "
                + "id_rubro, "
                + "nombre, "
                + "descripcion, "
                + "activo "
                + "FROM rubro "
                + "WHERE activo = 1 "
                + "ORDER BY nombre ASC";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql);
                ResultSet rs
                = ps.executeQuery()
        ) {

            while (rs.next()) {

                Rubro rubro
                        = mapearRubro(rs);

                lista.add(
                        rubro
                );
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al listar rubros activos: "
                    + e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO DEL RUBRO
    // =========================================================
    /*
     * No eliminamos físicamente el rubro.
     *
     * activo = true  -> rubro activo
     * activo = false -> rubro inactivo
     */

    public boolean cambiarEstado(
            int idRubro,
            boolean activo
    ) {

        String sql
                = "UPDATE rubro "
                + "SET activo = ? "
                + "WHERE id_rubro = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps
                = cn.prepareStatement(sql)
        ) {

            ps.setBoolean(
                    1,
                    activo
            );

            ps.setInt(
                    2,
                    idRubro
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado del rubro: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAPEAR RESULTSET A OBJETO RUBRO
    // =========================================================
    /*
     * Convierte una fila obtenida de MySQL
     * en un objeto Rubro de Java.
     */

    private Rubro mapearRubro(
            ResultSet rs
    ) throws SQLException {

        Rubro rubro
                = new Rubro();


        rubro.setIdRubro(
                rs.getInt("id_rubro")
        );

        rubro.setNombre(
                rs.getString("nombre")
        );

        rubro.setDescripcion(
                rs.getString("descripcion")
        );

        rubro.setActivo(
                rs.getBoolean("activo")
        );


        return rubro;
    }
}