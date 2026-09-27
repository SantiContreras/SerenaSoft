package util;

import Configuracion.conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println(" PRUEBA DE CONEXION - SERENA SOFT");
        System.out.println("================================");

        try (Connection cn = conexion.getConexion()) {

            if (cn != null && !cn.isClosed()) {

                System.out.println("CONEXION EXITOSA");
                System.out.println("Base de datos: "
                        + cn.getCatalog());

                System.out.println("MySQL: "
                        + cn.getMetaData()
                                .getDatabaseProductVersion());

                System.out.println("Usuario MySQL: "
                        + cn.getMetaData()
                                .getUserName());

            } else {

                System.out.println(
                        "No se pudo establecer la conexion."
                );
            }

        } catch (SQLException e) {

            System.err.println("ERROR DE CONEXION");
            System.err.println(
                    "Mensaje: " + e.getMessage()
            );

            System.err.println(
                    "Codigo SQL: " + e.getErrorCode()
            );

            System.err.println(
                    "SQL State: " + e.getSQLState()
            );
        }
    }
}