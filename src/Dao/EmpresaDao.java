package Dao;

import Configuracion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Configuración de un único comercio por instalación/base de datos. */
public final class EmpresaDao {
    public static final class Empresa {
        public int id;
        public String nombreComercial, razonSocial, cuit, condicionIva, direccion,
                localidad, provincia, telefono, email, logoRuta;
    }

    public Empresa obtener() throws SQLException {
        try (Connection c = conexion.getConexion()) {
            return consultar(c);
        }
    }

    private Empresa consultar(Connection c) throws SQLException {
        String sql = "SELECT id_empresa,nombre_comercial,razon_social,cuit,condicion_iva,direccion," +
                "localidad,provincia,telefono,email,logo_ruta FROM empresa WHERE activo=1 ORDER BY id_empresa LIMIT 1";
        try (PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
            if (!rs.next()) return null;
            Empresa d = new Empresa();
            d.id = rs.getInt(1); d.nombreComercial = rs.getString(2); d.razonSocial = rs.getString(3);
            d.cuit = rs.getString(4); d.condicionIva = rs.getString(5); d.direccion = rs.getString(6);
            d.localidad = rs.getString(7); d.provincia = rs.getString(8); d.telefono = rs.getString(9);
            d.email = rs.getString(10); d.logoRuta = rs.getString(11);
            return d;
        }
    }

    public void guardar(Empresa d) throws SQLException {
        try (Connection c = conexion.getConexion()) {
            c.setAutoCommit(false);
            try {
                // Bloquea la tabla lógica mediante la primera fila existente; instalación monocomercio.
                Empresa actual = consultar(c);
                if (actual == null) {
                    String sql = "INSERT INTO empresa(nombre_comercial,razon_social,cuit,condicion_iva,direccion," +
                            "localidad,provincia,telefono,email,logo_ruta,activo) VALUES(?,?,?,?,?,?,?,?,?,?,1)";
                    try (PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        valores(p, d, 1);
                        p.executeUpdate();
                    }
                } else {
                    String sql = "UPDATE empresa SET nombre_comercial=?,razon_social=?,cuit=?,condicion_iva=?," +
                            "direccion=?,localidad=?,provincia=?,telefono=?,email=?,logo_ruta=? WHERE id_empresa=? AND activo=1";
                    try (PreparedStatement p = c.prepareStatement(sql)) {
                        valores(p, d, 1); p.setInt(11, actual.id);
                        if (p.executeUpdate() != 1) throw new SQLException("No se actualizó la empresa.");
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException ex) {
                c.rollback(); throw ex;
            } finally { c.setAutoCommit(true); }
        }
    }

    private void valores(PreparedStatement p, Empresa d, int inicio) throws SQLException {
        p.setString(inicio, d.nombreComercial);
        p.setString(inicio+1, d.razonSocial);
        p.setString(inicio+2, vacioANull(d.cuit));
        p.setString(inicio+3, d.condicionIva);
        p.setString(inicio+4, d.direccion);
        p.setString(inicio+5, d.localidad);
        p.setString(inicio+6, d.provincia);
        p.setString(inicio+7, d.telefono);
        p.setString(inicio+8, vacioANull(d.email));
        p.setString(inicio+9, vacioANull(d.logoRuta));
    }
    private static String vacioANull(String s) { return s == null || s.isBlank() ? null : s; }
}
