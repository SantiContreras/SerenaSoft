package Configuracion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class conexion {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream input
                = new FileInputStream("config.properties")) {

            PROPERTIES.load(input);

        } catch (IOException e) {

            throw new ExceptionInInitializerError(
                    "No se pudo cargar config.properties: "
                    + e.getMessage()
            );
        }
    }

    private conexion() { }

    public static Connection getConexion() throws SQLException {

        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.user");
        String password = PROPERTIES.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }

    public static boolean probarConexion() {

        try (Connection cn = getConexion()) {

            return cn != null && !cn.isClosed();

        } catch (SQLException e) {

            System.err.println(
                    "Error de conexión: " + e.getMessage()
            );

            return false;
        }
    }
}
