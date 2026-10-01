package Dao;

import Configuracion.conexion;

import model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ProveedorDao {

    // =========================================================
    // SQL BASE
    // =========================================================

    private static final String SQL_BASE =
            "SELECT "
            + "id_proveedor, "
            + "razon_social, "
            + "nombre_comercial, "
            + "cuit, "
            + "condicion_iva, "
            + "direccion, "
            + "localidad, "
            + "provincia, "
            + "telefono, "
            + "email, "
            + "persona_contacto, "
            + "telefono_contacto, "
            + "observaciones, "
            + "activo, "
            + "fecha_registro, "
            + "fecha_modificacion "
            + "FROM proveedor ";


    // =========================================================
    // GUARDAR
    // =========================================================

    public int guardar(Proveedor proveedor)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return guardar(
                    proveedor,
                    cn
            );
        }
    }


    // =========================================================
    // GUARDAR - CONEXIÓN EXTERNA
    // =========================================================

    public int guardar(
            Proveedor proveedor,
            Connection cn)
            throws SQLException {

        String sql =
                "INSERT INTO proveedor ("
                + "razon_social, "
                + "nombre_comercial, "
                + "cuit, "
                + "condicion_iva, "
                + "direccion, "
                + "localidad, "
                + "provincia, "
                + "telefono, "
                + "email, "
                + "persona_contacto, "
                + "telefono_contacto, "
                + "observaciones, "
                + "activo"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement ps =
                     cn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setString(
                    1,
                    proveedor.getRazonSocial()
            );

            ps.setString(
                    2,
                    proveedor.getNombreComercial()
            );

            ps.setString(
                    3,
                    proveedor.getCuit()
            );

            ps.setString(
                    4,
                    proveedor.getCondicionIva()
            );

            ps.setString(
                    5,
                    proveedor.getDireccion()
            );

            ps.setString(
                    6,
                    proveedor.getLocalidad()
            );

            ps.setString(
                    7,
                    proveedor.getProvincia()
            );

            ps.setString(
                    8,
                    proveedor.getTelefono()
            );

            ps.setString(
                    9,
                    proveedor.getEmail()
            );

            ps.setString(
                    10,
                    proveedor.getPersonaContacto()
            );

            ps.setString(
                    11,
                    proveedor.getTelefonoContacto()
            );

            ps.setString(
                    12,
                    proveedor.getObservaciones()
            );

            ps.setBoolean(
                    13,
                    proveedor.isActivo()
            );


            int filas =
                    ps.executeUpdate();


            if (filas == 0) {

                return 0;
            }


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    int idProveedor =
                            rs.getInt(1);

                    proveedor.setIdProveedor(
                            idProveedor
                    );

                    return idProveedor;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(
            Proveedor proveedor)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return actualizar(
                    proveedor,
                    cn
            );
        }
    }


    // =========================================================
    // ACTUALIZAR - CONEXIÓN EXTERNA
    // =========================================================

    public boolean actualizar(
            Proveedor proveedor,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE proveedor SET "
                + "razon_social = ?, "
                + "nombre_comercial = ?, "
                + "cuit = ?, "
                + "condicion_iva = ?, "
                + "direccion = ?, "
                + "localidad = ?, "
                + "provincia = ?, "
                + "telefono = ?, "
                + "email = ?, "
                + "persona_contacto = ?, "
                + "telefono_contacto = ?, "
                + "observaciones = ?, "
                + "activo = ? "
                + "WHERE id_proveedor = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    proveedor.getRazonSocial()
            );

            ps.setString(
                    2,
                    proveedor.getNombreComercial()
            );

            ps.setString(
                    3,
                    proveedor.getCuit()
            );

            ps.setString(
                    4,
                    proveedor.getCondicionIva()
            );

            ps.setString(
                    5,
                    proveedor.getDireccion()
            );

            ps.setString(
                    6,
                    proveedor.getLocalidad()
            );

            ps.setString(
                    7,
                    proveedor.getProvincia()
            );

            ps.setString(
                    8,
                    proveedor.getTelefono()
            );

            ps.setString(
                    9,
                    proveedor.getEmail()
            );

            ps.setString(
                    10,
                    proveedor.getPersonaContacto()
            );

            ps.setString(
                    11,
                    proveedor.getTelefonoContacto()
            );

            ps.setString(
                    12,
                    proveedor.getObservaciones()
            );

            ps.setBoolean(
                    13,
                    proveedor.isActivo()
            );

            ps.setInt(
                    14,
                    proveedor.getIdProveedor()
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Proveedor buscarPorId(
            int idProveedor)
            throws SQLException {

        String sql =
                SQL_BASE
                + "WHERE id_proveedor = ?";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProveedor
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearProveedor(
                            rs
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR ID - CONEXIÓN EXTERNA
    // =========================================================

    public Proveedor buscarPorId(
            int idProveedor,
            Connection cn)
            throws SQLException {

        String sql =
                SQL_BASE
                + "WHERE id_proveedor = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProveedor
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearProveedor(
                            rs
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR CUIT
    // =========================================================

    public Proveedor buscarPorCuit(
            String cuit)
            throws SQLException {

        String sql =
                SQL_BASE
                + "WHERE cuit = ?";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    cuit
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearProveedor(
                            rs
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Proveedor> listarTodos()
            throws SQLException {

        List<Proveedor> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE
                + "ORDER BY razon_social ASC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {


            while (rs.next()) {

                lista.add(
                        mapearProveedor(
                                rs
                        )
                );
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR ACTIVOS
    //
    // Este método va a ser importante para Compra:
    // el JComboBox/buscador solamente debería ofrecer
    // proveedores activos.
    // =========================================================

    public List<Proveedor> listarActivos()
            throws SQLException {

        List<Proveedor> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE
                + "WHERE activo = 1 "
                + "ORDER BY razon_social ASC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {


            while (rs.next()) {

                lista.add(
                        mapearProveedor(
                                rs
                        )
                );
            }
        }

        return lista;
    }


    // =========================================================
    // BUSCAR
    //
    // Permite buscar por:
    //
    // - Razón social
    // - Nombre comercial
    // - CUIT
    //
    // Esto nos servirá después para DialogoBuscarProveedor.
    // =========================================================

    public List<Proveedor> buscar(
            String texto)
            throws SQLException {

        List<Proveedor> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE
                + "WHERE razon_social LIKE ? "
                + "OR nombre_comercial LIKE ? "
                + "OR cuit LIKE ? "
                + "ORDER BY razon_social ASC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            String filtro =
                    "%"
                    + texto
                    + "%";


            ps.setString(
                    1,
                    filtro
            );

            ps.setString(
                    2,
                    filtro
            );

            ps.setString(
                    3,
                    filtro
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearProveedor(
                                    rs
                            )
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // BUSCAR ACTIVOS
    //
    // Variante especialmente útil para Compra.
    // =========================================================

    public List<Proveedor> buscarActivos(
            String texto)
            throws SQLException {

        List<Proveedor> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE
                + "WHERE activo = 1 "
                + "AND ("
                + "razon_social LIKE ? "
                + "OR nombre_comercial LIKE ? "
                + "OR cuit LIKE ?"
                + ") "
                + "ORDER BY razon_social ASC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            String filtro =
                    "%"
                    + texto
                    + "%";


            ps.setString(
                    1,
                    filtro
            );

            ps.setString(
                    2,
                    filtro
            );

            ps.setString(
                    3,
                    filtro
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearProveedor(
                                    rs
                            )
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    //
    // No eliminamos físicamente proveedores.
    // =========================================================

    public boolean cambiarEstado(
            int idProveedor,
            boolean activo)
            throws SQLException {

        String sql =
                "UPDATE proveedor "
                + "SET activo = ? "
                + "WHERE id_proveedor = ?";


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
                    idProveedor
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // EXISTE CUIT
    // =========================================================

    public boolean existeCuit(
            String cuit)
            throws SQLException {

        String sql =
                "SELECT 1 "
                + "FROM proveedor "
                + "WHERE cuit = ? "
                + "LIMIT 1";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    cuit
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // =========================================================
    // EXISTE CUIT EN OTRO PROVEEDOR
    //
    // Lo usaremos al editar.
    //
    // Ejemplo:
    // estamos editando proveedor 5 y conserva su propio CUIT.
    // Eso es válido.
    // =========================================================

    public boolean existeCuitEnOtroProveedor(
            String cuit,
            int idProveedor)
            throws SQLException {

        String sql =
                "SELECT 1 "
                + "FROM proveedor "
                + "WHERE cuit = ? "
                + "AND id_proveedor <> ? "
                + "LIMIT 1";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    cuit
            );

            ps.setInt(
                    2,
                    idProveedor
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // =========================================================
    // MAPEAR PROVEEDOR
    // =========================================================

    private Proveedor mapearProveedor(
            ResultSet rs)
            throws SQLException {

        Proveedor proveedor =
                new Proveedor();


        proveedor.setIdProveedor(
                rs.getInt(
                        "id_proveedor"
                )
        );


        proveedor.setRazonSocial(
                rs.getString(
                        "razon_social"
                )
        );


        proveedor.setNombreComercial(
                rs.getString(
                        "nombre_comercial"
                )
        );


        proveedor.setCuit(
                rs.getString(
                        "cuit"
                )
        );


        proveedor.setCondicionIva(
                rs.getString(
                        "condicion_iva"
                )
        );


        proveedor.setDireccion(
                rs.getString(
                        "direccion"
                )
        );


        proveedor.setLocalidad(
                rs.getString(
                        "localidad"
                )
        );


        proveedor.setProvincia(
                rs.getString(
                        "provincia"
                )
        );


        proveedor.setTelefono(
                rs.getString(
                        "telefono"
                )
        );


        proveedor.setEmail(
                rs.getString(
                        "email"
                )
        );


        proveedor.setPersonaContacto(
                rs.getString(
                        "persona_contacto"
                )
        );


        proveedor.setTelefonoContacto(
                rs.getString(
                        "telefono_contacto"
                )
        );


        proveedor.setObservaciones(
                rs.getString(
                        "observaciones"
                )
        );


        proveedor.setActivo(
                rs.getBoolean(
                        "activo"
                )
        );


        // =====================================================
        // FECHA REGISTRO
        // =====================================================

        Timestamp fechaRegistro =
                rs.getTimestamp(
                        "fecha_registro"
                );


        if (fechaRegistro != null) {

            proveedor.setFechaRegistro(
                    fechaRegistro.toLocalDateTime()
            );
        }


        // =====================================================
        // FECHA MODIFICACIÓN
        // =====================================================

        Timestamp fechaModificacion =
                rs.getTimestamp(
                        "fecha_modificacion"
                );


        if (fechaModificacion != null) {

            proveedor.setFechaModificacion(
                    fechaModificacion.toLocalDateTime()
            );
        }


        return proveedor;
    }
}