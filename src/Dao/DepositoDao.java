package Dao;

import Configuracion.conexion;
import model.Deposito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepositoDao {


    // =========================================================
    // GUARDAR
    // =========================================================

    public boolean guardar(Deposito deposito) {

        String sql =
                "INSERT INTO deposito "
                + "(codigo, nombre, descripcion, es_principal, activo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(
                    1,
                    deposito.getCodigo()
            );

            ps.setString(
                    2,
                    deposito.getNombre()
            );

            ps.setString(
                    3,
                    deposito.getDescripcion()
            );

            ps.setBoolean(
                    4,
                    deposito.isEsPrincipal()
            );

            ps.setBoolean(
                    5,
                    deposito.isActivo()
            );

            int filas =
                    ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs =
                             ps.getGeneratedKeys()) {

                    if (rs.next()) {

                        deposito.setIdDeposito(
                                rs.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar depósito:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(
            Deposito deposito) {

        String sql =
                "UPDATE deposito SET "
                + "codigo = ?, "
                + "nombre = ?, "
                + "descripcion = ?, "
                + "es_principal = ?, "
                + "activo = ? "
                + "WHERE id_deposito = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    deposito.getCodigo()
            );

            ps.setString(
                    2,
                    deposito.getNombre()
            );

            ps.setString(
                    3,
                    deposito.getDescripcion()
            );

            ps.setBoolean(
                    4,
                    deposito.isEsPrincipal()
            );

            ps.setBoolean(
                    5,
                    deposito.isActivo()
            );

            ps.setInt(
                    6,
                    deposito.getIdDeposito()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar depósito:"
            );

            System.err.println(
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Deposito buscarPorId(
            int idDeposito) {

        String sql =
                "SELECT * "
                + "FROM deposito "
                + "WHERE id_deposito = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idDeposito
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearDeposito(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar depósito por ID:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public Deposito buscarPorCodigo(
            String codigo) {

        String sql =
                "SELECT * "
                + "FROM deposito "
                + "WHERE codigo = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    codigo
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearDeposito(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar depósito por código:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // BUSCAR DEPÓSITO PRINCIPAL
    // =========================================================

    public Deposito buscarPrincipal() {

        String sql =
                "SELECT * "
                + "FROM deposito "
                + "WHERE es_principal = 1 "
                + "AND activo = 1 "
                + "LIMIT 1";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            if (rs.next()) {

                return mapearDeposito(rs);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar depósito principal:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // EXISTE CÓDIGO
    // =========================================================

    public boolean existeCodigo(
            String codigo) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM deposito "
                + "WHERE codigo = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    codigo
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar código de depósito:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // EXISTE CÓDIGO EN OTRO DEPÓSITO
    // =========================================================

    public boolean existeCodigoEnOtroDeposito(
            String codigo,
            int idDeposito) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM deposito "
                + "WHERE codigo = ? "
                + "AND id_deposito <> ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    codigo
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar código duplicado:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Deposito> listarTodos() {

        List<Deposito> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM deposito "
                + "ORDER BY nombre";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapearDeposito(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar depósitos:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Deposito> listarActivos() {

        List<Deposito> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM deposito "
                + "WHERE activo = 1 "
                + "ORDER BY es_principal DESC, nombre";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapearDeposito(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar depósitos activos:"
            );

            System.err.println(
                    e.getMessage()
            );
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            int idDeposito,
            boolean activo) {

        String sql =
                "UPDATE deposito "
                + "SET activo = ? "
                + "WHERE id_deposito = ?";

        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setBoolean(
                    1,
                    activo
            );

            ps.setInt(
                    2,
                    idDeposito
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado del depósito:"
            );

            System.err.println(
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAPEAR RESULTSET
    // =========================================================

    private Deposito mapearDeposito(
            ResultSet rs)
            throws SQLException {

        return new Deposito(

                rs.getInt(
                        "id_deposito"
                ),

                rs.getString(
                        "codigo"
                ),

                rs.getString(
                        "nombre"
                ),

                rs.getString(
                        "descripcion"
                ),

                rs.getBoolean(
                        "es_principal"
                ),

                rs.getBoolean(
                        "activo"
                )
        );
    }
}