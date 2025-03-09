package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/SistemaBiblioteca";
    private static final String USER = "julian";
    private static final String PASSWORD = "1234";

    private static Connection conexion;

    public static Connection getConnection() throws SQLException {
        if (conexion == null) {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }
}
