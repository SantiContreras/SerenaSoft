package Dao;

import Configuracion.conexion;
import model.Permiso;
import model.Rol;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDao {

    // =========================================================
    // BUSCAR USUARIO POR USERNAME
    // Se utiliza principalmente para LOGIN
    // =========================================================

    public Usuario buscarPorUsername(String username) {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombre_completo,
                    u.username,
                    u.password_hash,
                    u.email,
                    u.estado,
                    u.intentos_fallidos,

                    r.id_rol,
                    r.nombre AS rol_nombre,
                    r.descripcion AS rol_descripcion,
                    r.activo AS rol_activo

                FROM usuario u

                INNER JOIN rol r
                    ON r.id_rol = u.id_rol

                WHERE LOWER(u.username) = LOWER(?)

                LIMIT 1
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Rol rol = new Rol();

                    rol.setIdRol(
                            rs.getInt("id_rol")
                    );

                    rol.setNombre(
                            rs.getString("rol_nombre")
                    );

                    rol.setDescripcion(
                            rs.getString("rol_descripcion")
                    );

                    rol.setActivo(
                            rs.getBoolean("rol_activo")
                    );

                    // Cargar permisos del rol
                    rol.setPermisos(
                            cargarPermisosRol(
                                    cn,
                                    rol.getIdRol()
                            )
                    );


                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(
                            rs.getInt("id_usuario")
                    );

                    usuario.setNombreCompleto(
                            rs.getString("nombre_completo")
                    );

                    usuario.setUsername(
                            rs.getString("username")
                    );

                    usuario.setPasswordHash(
                            rs.getString("password_hash")
                    );

                    usuario.setEmail(
                            rs.getString("email")
                    );

                    usuario.setEstado(
                            rs.getString("estado")
                    );

                    usuario.setIntentosFallidos(
                            rs.getInt("intentos_fallidos")
                    );

                    usuario.setRol(rol);

                    return usuario;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar usuario por username: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // CARGAR PERMISOS DEL ROL
    // =========================================================

    private List<Permiso> cargarPermisosRol(
            Connection cn,
            int idRol)
            throws SQLException {

        List<Permiso> permisos =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.id_permiso,
                    p.codigo,
                    p.nombre,
                    p.modulo,
                    p.descripcion,
                    p.activo

                FROM permiso p

                INNER JOIN rol_permiso rp
                    ON rp.id_permiso = p.id_permiso

                WHERE rp.id_rol = ?
                  AND p.activo = TRUE

                ORDER BY
                    p.modulo,
                    p.codigo
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idRol
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    Permiso permiso =
                            new Permiso();

                    permiso.setIdPermiso(
                            rs.getInt("id_permiso")
                    );

                    permiso.setCodigo(
                            rs.getString("codigo")
                    );

                    permiso.setNombre(
                            rs.getString("nombre")
                    );

                    permiso.setModulo(
                            rs.getString("modulo")
                    );

                    permiso.setDescripcion(
                            rs.getString("descripcion")
                    );

                    permiso.setActivo(
                            rs.getBoolean("activo")
                    );

                    permisos.add(
                            permiso
                    );
                }
            }
        }

        return permisos;
    }


    // =========================================================
    // BUSCAR USUARIO POR ID
    // =========================================================

    public Usuario buscarPorId(
            int idUsuario) {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombre_completo,
                    u.username,
                    u.password_hash,
                    u.email,
                    u.estado,
                    u.intentos_fallidos,

                    r.id_rol,
                    r.nombre AS rol_nombre,
                    r.descripcion AS rol_descripcion,
                    r.activo AS rol_activo

                FROM usuario u

                INNER JOIN rol r
                    ON r.id_rol = u.id_rol

                WHERE u.id_usuario = ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearUsuario(
                            rs,
                            false,
                            cn
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar usuario por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // VERIFICAR SI EXISTE USERNAME
    // Utilizado al crear un usuario
    // =========================================================

    public boolean existeUsername(
            String username) {

        String sql = """
                SELECT COUNT(*)
                FROM usuario
                WHERE LOWER(username) = LOWER(?)
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    username
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar username: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // VERIFICAR USERNAME EN OTRO USUARIO
    // Utilizado al editar
    // =========================================================

    public boolean existeUsernameEnOtroUsuario(
            String username,
            int idUsuario) {

        String sql = """
                SELECT COUNT(*)
                FROM usuario
                WHERE LOWER(username) = LOWER(?)
                  AND id_usuario <> ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    username
            );

            ps.setInt(
                    2,
                    idUsuario
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar username "
                    + "en otro usuario: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // GUARDAR USUARIO
    // =========================================================

    public boolean guardar(
            Usuario usuario) {

        String sql = """
                INSERT INTO usuario
                (
                    id_rol,
                    nombre_completo,
                    username,
                    password_hash,
                    email,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(
                    1,
                    usuario.getRol().getIdRol()
            );

            ps.setString(
                    2,
                    usuario.getNombreCompleto()
            );

            ps.setString(
                    3,
                    usuario.getUsername()
            );

            ps.setString(
                    4,
                    usuario.getPasswordHash()
            );

            ps.setString(
                    5,
                    usuario.getEmail()
            );

            ps.setString(
                    6,
                    usuario.getEstado()
            );

            int filas =
                    ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet keys =
                             ps.getGeneratedKeys()) {

                    if (keys.next()) {

                        usuario.setIdUsuario(
                                keys.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar usuario: "
                    + e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // ACTUALIZAR USUARIO
    // =========================================================

    public boolean actualizar(
            Usuario usuario) {

        String sql = """
                UPDATE usuario
                SET
                    id_rol = ?,
                    nombre_completo = ?,
                    username = ?,
                    email = ?
                WHERE id_usuario = ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    usuario.getRol().getIdRol()
            );

            ps.setString(
                    2,
                    usuario.getNombreCompleto()
            );

            ps.setString(
                    3,
                    usuario.getUsername()
            );

            ps.setString(
                    4,
                    usuario.getEmail()
            );

            ps.setInt(
                    5,
                    usuario.getIdUsuario()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar usuario: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // CAMBIAR ESTADO
    // ACTIVO / INACTIVO / BLOQUEADO
    // =========================================================

    public boolean cambiarEstado(
            int idUsuario,
            String estado) {

        String sql = """
                UPDATE usuario
                SET estado = ?
                WHERE id_usuario = ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );

            ps.setInt(
                    2,
                    idUsuario
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar estado del usuario: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // CAMBIAR CONTRASEÑA
    // Recibe el HASH, no la contraseña sin cifrar
    // =========================================================

    public boolean cambiarPassword(
            int idUsuario,
            String nuevoHash) {

        String sql = """
                UPDATE usuario
                SET
                    password_hash = ?,
                    intentos_fallidos = 0,
                    bloqueado_hasta = NULL
                WHERE id_usuario = ?
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    nuevoHash
            );

            ps.setInt(
                    2,
                    idUsuario
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al cambiar contraseña: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // LISTAR USUARIOS
    // =========================================================

    public List<Usuario> listar() {

        List<Usuario> usuarios =
                new ArrayList<>();

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombre_completo,
                    u.username,
                    u.password_hash,
                    u.email,
                    u.estado,
                    u.intentos_fallidos,

                    r.id_rol,
                    r.nombre AS rol_nombre,
                    r.descripcion AS rol_descripcion,
                    r.activo AS rol_activo

                FROM usuario u

                INNER JOIN rol r
                    ON r.id_rol = u.id_rol

                ORDER BY u.nombre_completo
                """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps =
                     cn.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario =
                        mapearUsuario(
                                rs,
                                false,
                                cn
                        );

                usuarios.add(
                        usuario
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar usuarios: "
                    + e.getMessage()
            );
        }

        return usuarios;
    }


    // =========================================================
    // MAPEAR RESULTSET → USUARIO
    // =========================================================

    private Usuario mapearUsuario(
            ResultSet rs,
            boolean cargarPermisos,
            Connection cn)
            throws SQLException {

        Rol rol =
                new Rol();

        rol.setIdRol(
                rs.getInt("id_rol")
        );

        rol.setNombre(
                rs.getString("rol_nombre")
        );

        rol.setDescripcion(
                rs.getString("rol_descripcion")
        );

        rol.setActivo(
                rs.getBoolean("rol_activo")
        );

        if (cargarPermisos) {

            rol.setPermisos(
                    cargarPermisosRol(
                            cn,
                            rol.getIdRol()
                    )
            );
        }


        Usuario usuario =
                new Usuario();

        usuario.setIdUsuario(
                rs.getInt("id_usuario")
        );

        usuario.setNombreCompleto(
                rs.getString("nombre_completo")
        );

        usuario.setUsername(
                rs.getString("username")
        );

        usuario.setPasswordHash(
                rs.getString("password_hash")
        );

        usuario.setEmail(
                rs.getString("email")
        );

        usuario.setEstado(
                rs.getString("estado")
        );

        usuario.setIntentosFallidos(
                rs.getInt("intentos_fallidos")
        );

        usuario.setRol(rol);

        return usuario;
    }
    
    // =========================================================
// REGISTRAR INTENTO FALLIDO
// =========================================================

public boolean registrarIntentoFallido(
        int idUsuario,
        int intentos) {

    String sql = """
            UPDATE usuario
            SET intentos_fallidos = ?
            WHERE id_usuario = ?
            """;

    try (Connection cn = conexion.getConexion();
         PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        ps.setInt(
                1,
                intentos
        );

        ps.setInt(
                2,
                idUsuario
        );

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al registrar intento fallido: "
                + e.getMessage()
        );

        return false;
    }
}

// =========================================================
// REGISTRAR ACCESO CORRECTO
// =========================================================

public boolean registrarAccesoCorrecto(
        int idUsuario,
        LocalDateTime fechaAcceso) {

    String sql = """
            UPDATE usuario
            SET
                intentos_fallidos = 0,
                bloqueado_hasta = NULL,
                ultimo_acceso = ?
            WHERE id_usuario = ?
            """;

    try (Connection cn = conexion.getConexion();
         PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        ps.setTimestamp(
                1,
                Timestamp.valueOf(
                        fechaAcceso
                )
        );

        ps.setInt(
                2,
                idUsuario
        );

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al registrar acceso correcto: "
                + e.getMessage()
        );

        return false;
    }
}

// =========================================================
// DESBLOQUEAR USUARIO
// =========================================================

public boolean desbloquearUsuario(
        int idUsuario) {

    String sql = """
            UPDATE usuario
            SET
                estado = 'ACTIVO',
                intentos_fallidos = 0,
                bloqueado_hasta = NULL
            WHERE id_usuario = ?
            """;

    try (Connection cn = conexion.getConexion();
         PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        ps.setInt(
                1,
                idUsuario
        );

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al desbloquear usuario: "
                + e.getMessage()
        );

        return false;
    }
}
// =========================================================
// BLOQUEAR USUARIO TEMPORALMENTE
// =========================================================

public boolean bloquearUsuario(
        int idUsuario,
        int intentos,
        LocalDateTime bloqueadoHasta) {

    String sql = """
            UPDATE usuario
            SET
                estado = 'BLOQUEADO',
                intentos_fallidos = ?,
                bloqueado_hasta = ?
            WHERE id_usuario = ?
            """;

    try (Connection cn = conexion.getConexion();
         PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        ps.setInt(
                1,
                intentos
        );

        ps.setTimestamp(
                2,
                Timestamp.valueOf(
                        bloqueadoHasta
                )
        );

        ps.setInt(
                3,
                idUsuario
        );

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al bloquear usuario: "
                + e.getMessage()
        );

        return false;
    }
}
}