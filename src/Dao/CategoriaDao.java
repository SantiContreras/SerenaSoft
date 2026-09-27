package Dao;

import Configuracion.conexion;
import model.Categoria;
import model.Rubro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {


    // =========================================================
    // GUARDAR
    // =========================================================

    public boolean guardar(Categoria categoria) {

        String sql
                = "INSERT INTO categoria "
                + "(id_rubro, nombre, descripcion, activo) "
                + "VALUES (?, ?, ?, ?)";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            // -------------------------------------------------
            // RUBRO
            // -------------------------------------------------

            if (categoria.getRubro() != null) {

                ps.setInt(
                        1,
                        categoria.getRubro().getIdRubro()
                );

            } else {

                ps.setNull(
                        1,
                        java.sql.Types.INTEGER
                );
            }


            ps.setString(
                    2,
                    categoria.getNombre()
            );

            ps.setString(
                    3,
                    categoria.getDescripcion()
            );

            ps.setBoolean(
                    4,
                    categoria.isActivo()
            );


            int filas
                    = ps.executeUpdate();


            if (filas > 0) {

                try (
                        ResultSet rs
                        = ps.getGeneratedKeys()
                ) {

                    if (rs.next()) {

                        categoria.setIdCategoria(
                                rs.getInt(1)
                        );
                    }
                }

                return true;
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar categoría: "
                    + e.getMessage()
            );
        }


        return false;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(Categoria categoria) {

        String sql
                = "UPDATE categoria "
                + "SET id_rubro = ?, "
                + "nombre = ?, "
                + "descripcion = ?, "
                + "activo = ? "
                + "WHERE id_categoria = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            if (categoria.getRubro() != null) {

                ps.setInt(
                        1,
                        categoria.getRubro().getIdRubro()
                );

            } else {

                ps.setNull(
                        1,
                        java.sql.Types.INTEGER
                );
            }


            ps.setString(
                    2,
                    categoria.getNombre()
            );

            ps.setString(
                    3,
                    categoria.getDescripcion()
            );

            ps.setBoolean(
                    4,
                    categoria.isActivo()
            );

            ps.setInt(
                    5,
                    categoria.getIdCategoria()
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar categoría: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Categoria buscarPorId(int idCategoria) {

        String sql
                = "SELECT "
                + "c.id_categoria, "
                + "c.nombre AS categoria_nombre, "
                + "c.descripcion AS categoria_descripcion, "
                + "c.activo AS categoria_activo, "
                + "r.id_rubro, "
                + "r.nombre AS rubro_nombre, "
                + "r.descripcion AS rubro_descripcion, "
                + "r.activo AS rubro_activo "
                + "FROM categoria c "
                + "LEFT JOIN rubro r "
                + "ON c.id_rubro = r.id_rubro "
                + "WHERE c.id_categoria = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idCategoria
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapearCategoria(rs);
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar categoría por ID: "
                    + e.getMessage()
            );
        }


        return null;
    }


    // =========================================================
    // BUSCAR POR NOMBRE
    // =========================================================

    public Categoria buscarPorNombre(String nombre) {

        String sql
                = "SELECT "
                + "c.id_categoria, "
                + "c.nombre AS categoria_nombre, "
                + "c.descripcion AS categoria_descripcion, "
                + "c.activo AS categoria_activo, "
                + "r.id_rubro, "
                + "r.nombre AS rubro_nombre, "
                + "r.descripcion AS rubro_descripcion, "
                + "r.activo AS rubro_activo "
                + "FROM categoria c "
                + "LEFT JOIN rubro r "
                + "ON c.id_rubro = r.id_rubro "
                + "WHERE c.nombre = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapearCategoria(rs);
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar categoría por nombre: "
                    + e.getMessage()
            );
        }


        return null;
    }


    // =========================================================
    // EXISTE NOMBRE
    // =========================================================

    public boolean existeNombre(String nombre) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM categoria "
                + "WHERE nombre = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar categoría: "
                    + e.getMessage()
            );
        }


        return false;
    }


    // =========================================================
    // EXISTE NOMBRE EN OTRA CATEGORÍA
    // =========================================================

    public boolean existeNombreEnOtraCategoria(
            String nombre,
            int idCategoria
    ) {

        String sql
                = "SELECT COUNT(*) "
                + "FROM categoria "
                + "WHERE nombre = ? "
                + "AND id_categoria <> ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nombre
            );

            ps.setInt(
                    2,
                    idCategoria
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar nombre de categoría: "
                    + e.getMessage()
            );
        }


        return false;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Categoria> listarTodos() {

        List<Categoria> lista
                = new ArrayList<>();


        String sql
                = "SELECT "
                + "c.id_categoria, "
                + "c.nombre AS categoria_nombre, "
                + "c.descripcion AS categoria_descripcion, "
                + "c.activo AS categoria_activo, "
                + "r.id_rubro, "
                + "r.nombre AS rubro_nombre, "
                + "r.descripcion AS rubro_descripcion, "
                + "r.activo AS rubro_activo "
                + "FROM categoria c "
                + "LEFT JOIN rubro r "
                + "ON c.id_rubro = r.id_rubro "
                + "ORDER BY c.nombre ASC";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        mapearCategoria(rs)
                );
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al listar categorías: "
                    + e.getMessage()
            );
        }


        return lista;
    }


    // =========================================================
    // LISTAR ACTIVAS
    // =========================================================

    public List<Categoria> listarActivas() {

        List<Categoria> lista
                = new ArrayList<>();


        String sql
                = "SELECT "
                + "c.id_categoria, "
                + "c.nombre AS categoria_nombre, "
                + "c.descripcion AS categoria_descripcion, "
                + "c.activo AS categoria_activo, "
                + "r.id_rubro, "
                + "r.nombre AS rubro_nombre, "
                + "r.descripcion AS rubro_descripcion, "
                + "r.activo AS rubro_activo "
                + "FROM categoria c "
                + "LEFT JOIN rubro r "
                + "ON c.id_rubro = r.id_rubro "
                + "WHERE c.activo = 1 "
                + "ORDER BY c.nombre ASC";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        mapearCategoria(rs)
                );
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al listar categorías activas: "
                    + e.getMessage()
            );
        }


        return lista;
    }


    // =========================================================
    // LISTAR POR RUBRO
    // =========================================================

    public List<Categoria> listarPorRubro(
            int idRubro
    ) {

        List<Categoria> lista
                = new ArrayList<>();


        String sql
                = "SELECT "
                + "c.id_categoria, "
                + "c.nombre AS categoria_nombre, "
                + "c.descripcion AS categoria_descripcion, "
                + "c.activo AS categoria_activo, "
                + "r.id_rubro, "
                + "r.nombre AS rubro_nombre, "
                + "r.descripcion AS rubro_descripcion, "
                + "r.activo AS rubro_activo "
                + "FROM categoria c "
                + "LEFT JOIN rubro r "
                + "ON c.id_rubro = r.id_rubro "
                + "WHERE c.id_rubro = ? "
                + "AND c.activo = 1 "
                + "ORDER BY c.nombre ASC";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idRubro
            );


            try (
                    ResultSet rs = ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            mapearCategoria(rs)
                    );
                }
            }


        } catch (SQLException e) {

            System.err.println(
                    "Error al listar categorías por rubro: "
                    + e.getMessage()
            );
        }


        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            int idCategoria,
            boolean activo
    ) {

        String sql
                = "UPDATE categoria "
                + "SET activo = ? "
                + "WHERE id_categoria = ?";


        try (
                Connection cn = conexion.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setBoolean(
                    1,
                    activo
            );

            ps.setInt(
                    2,
                    idCategoria
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado de categoría: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAPEAR CATEGORÍA
    // =========================================================

    private Categoria mapearCategoria(
            ResultSet rs
    ) throws SQLException {

        Categoria categoria
                = new Categoria();


        categoria.setIdCategoria(
                rs.getInt("id_categoria")
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


        // -----------------------------------------------------
        // RUBRO
        // -----------------------------------------------------

        int idRubro
                = rs.getInt("id_rubro");


        if (!rs.wasNull()) {

            Rubro rubro
                    = new Rubro();

            rubro.setIdRubro(
                    idRubro
            );

            rubro.setNombre(
                    rs.getString("rubro_nombre")
            );

            rubro.setDescripcion(
                    rs.getString("rubro_descripcion")
            );

            rubro.setActivo(
                    rs.getBoolean("rubro_activo")
            );


            categoria.setRubro(
                    rubro
            );

        } else {

            categoria.setRubro(
                    null
            );
        }


        return categoria;
    }
}