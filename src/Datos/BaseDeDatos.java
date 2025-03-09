package Datos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDatos {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "julian";
    private static final String PASSWORD = "1234";
    private static final String DB_NAME = "SistemaBiblioteca";

    public static void crearBaseDeDatos() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();

        String createDBSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        stmt.executeUpdate(createDBSQL);
        System.out.println("Base de datos '" + DB_NAME + "' creada.");

        conn.close();

        conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        stmt = conn.createStatement();

        crearTablas(stmt);
        insertarDatosSiVacio(stmt);

        conn.close();
    }

    private static void crearTablas(Statement stmt) throws SQLException {
        String sqlUsuario = "" +
                "CREATE TABLE IF NOT EXISTS `usuario` (\n" +
                "`idUsuario` INT NOT NULL AUTO_INCREMENT,\n" +
                "`nombre` VARCHAR(150) NULL,\n" +
                "`dni` INT NULL,\n" +
                "`email` VARCHAR(255) NULL,\n" +
                "PRIMARY KEY (`idUsuario`)) ENGINE = InnoDB;";

        String sqlLibro = "" +
                "CREATE TABLE IF NOT EXISTS `libro` (\n" +
                "`idLibro` INT NOT NULL AUTO_INCREMENT,\n" +
                "`nombre` VARCHAR(150) NULL,\n" +
                "`autor` VARCHAR(150) NULL,\n" +
                "`categoria` VARCHAR(150) NULL,\n" +
                "`disponible` TINYINT NULL,\n" +
                "PRIMARY KEY (`idLibro`)) ENGINE = InnoDB;";

        String sqlPrestamo = "" +
                "CREATE TABLE IF NOT EXISTS `usuario_prestamo` (\n" +
                "`idPrestamo` INT NOT NULL AUTO_INCREMENT,\n" +
                "`idUsuario` INT NOT NULL,\n" +
                "`idLibro` INT NOT NULL,\n" +
                "`fechaPrestamo` DATE NULL,\n" +
                "`fechaDevolucion` DATE NULL,\n" +
                "PRIMARY KEY (`idPrestamo`),\n" +
                "FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`),\n" +
                "FOREIGN KEY (`idLibro`) REFERENCES `libro` (`idLibro`));";

        stmt.executeUpdate(sqlUsuario);
        stmt.executeUpdate(sqlLibro);
        stmt.executeUpdate(sqlPrestamo);

        System.out.println("Tablas creadas.");
    }

    private static void insertarDatosSiVacio(Statement stmt) throws SQLException {
        // Check if tables are empty
        String checkUsuario = "SELECT COUNT(*) AS count FROM usuario";
        ResultSet rsUsuario = stmt.executeQuery(checkUsuario);
        rsUsuario.next();
        int countUsuario = rsUsuario.getInt("count");

        String checkLibro = "SELECT COUNT(*) AS count FROM libro";
        ResultSet rsLibro = stmt.executeQuery(checkLibro);
        rsLibro.next();
        int countLibro = rsLibro.getInt("count");

        String checkPrestamo = "SELECT COUNT(*) AS count FROM usuario_prestamo";
        ResultSet rsPrestamo = stmt.executeQuery(checkPrestamo);
        rsPrestamo.next();
        int countPrestamo = rsPrestamo.getInt("count");

        if (countUsuario == 0 && countLibro == 0 && countPrestamo == 0) {
            String insertUsuarios = "INSERT INTO usuario (nombre, dni, email) VALUES\n";
            for (int i = 1; i <= 20; i++) {
                insertUsuarios += String.format("('Usuario %d', %d, 'usuario%d@example.com')", i, 10000000 + i, i);
                if (i != 20) {
                    insertUsuarios += ",\n";
                }
            }
            stmt.executeUpdate(insertUsuarios);

            String insertLibros = "INSERT INTO libro (nombre, autor, categoria, disponible) VALUES\n";
            for (int i = 1; i <= 50; i++) {
                insertLibros += String.format("('Libro %d', 'Autor %d', 'Categoria %d', 1)", i, i, i);
                if (i != 50) {
                    insertLibros += ",\n";
                }
            }
            stmt.executeUpdate(insertLibros);

            String insertPrestamos = "INSERT INTO usuario_prestamo (idUsuario, idLibro, fechaPrestamo, fechaDevolucion) VALUES\n";
            for (int i = 1; i <= 100; i++) {
                int usuarioId = (int) (Math.random() * 20) + 1; // Usuario aleatorio entre 1 y 20
                int libroId = (int) (Math.random() * 50) + 1;  // Libro aleatorio entre 1 y 50
                String fechaPrestamo = String.format("2025-03-%02d", (int) (Math.random() * 28) + 1); // Fecha aleatoria en marzo de 2025
                String fechaDevolucion = String.format("2025-03-%02d", (int) (Math.random() * 28) + 1); // Fecha aleatoria en marzo de 2025

                insertPrestamos += String.format("(%d, %d, '%s', '%s')", usuarioId, libroId, fechaPrestamo, fechaDevolucion);
                if (i != 100) {
                    insertPrestamos += ",\n";
                }
            }
            stmt.executeUpdate(insertPrestamos);

            System.out.println("Datos insertados.");
        } else {
            System.out.println("Los datos ya existen, no se insertaron nuevos datos.");
        }
    }
}