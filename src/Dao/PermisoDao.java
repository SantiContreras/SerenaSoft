package Dao;

import Configuracion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Permiso;

public class PermisoDao {

    public List<Permiso> listarPorRol(int idRol)
            throws SQLException {

        List<Permiso> lista = new ArrayList<>();

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
            ORDER BY p.modulo, p.nombre
            """;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRol);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Permiso permiso = new Permiso();

                    permiso.setIdPermiso(
                            rs.getInt("id_permiso"));

                    permiso.setCodigo(
                            rs.getString("codigo"));

                    permiso.setNombre(
                            rs.getString("nombre"));

                    permiso.setModulo(
                            rs.getString("modulo"));

                    permiso.setDescripcion(
                            rs.getString("descripcion"));

                    permiso.setActivo(
                            rs.getBoolean("activo"));

                    lista.add(permiso);
                }
            }
        }

        return lista;
    }
}