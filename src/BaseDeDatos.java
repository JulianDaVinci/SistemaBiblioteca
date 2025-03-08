import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

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

        stmt.executeUpdate("USE " + DB_NAME);

        crearTablas(stmt);
    }

    private static void crearTablas(Statement stmt) throws SQLException {
        String sqlUsuario = "" +
                "CREATE TABLE IF NOT EXISTS `usuario` (\n" +
                "`idusuario` INT NOT NULL AUTO_INCREMENT,\n" +
                "`nombre` VARCHAR(150) NULL,\n" +
                "`dni` INT NULL,\n" +
                "`email` VARCHAR(255) NULL,\n" +
                "PRIMARY KEY (`idusuario`)) ENGINE = InnoDB;";

        String sqlLibro = "" +
                "CREATE TABLE IF NOT EXISTS `libro` (\n" +
                "`idlibro` INT NOT NULL AUTO_INCREMENT,\n" +
                "`nombre` VARCHAR(150) NULL,\n" +
                "`autor` VARCHAR(150) NULL,\n" +
                "`categoria` VARCHAR(150) NULL,\n" +
                "`disponible` TINYINT NULL,\n" +
                "PRIMARY KEY (`idlibro`)) ENGINE = InnoDB;";

        String sqlPrestamo = "" +
                "CREATE TABLE IF NOT EXISTS `usuario_prestamo` (\n" +
                "`idPrestamo` INT NOT NULL AUTO_INCREMENT,\n" +
                "`idusuario` INT NOT NULL,\n" +
                "`idlibro` INT NOT NULL,\n" +
                "`fechaPrestamo` DATE NULL,\n" +
                "`fechaDevolucion` VARCHAR(45) NULL,\n" +
                "PRIMARY KEY (`idPrestamo`, `idusuario`, `idlibro`),\n" +
                "FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`),\n" +
                "FOREIGN KEY (`idlibro`) REFERENCES `libro` (`idlibro`));";

        stmt.executeUpdate(sqlUsuario);
        stmt.executeUpdate(sqlLibro);
        stmt.executeUpdate(sqlPrestamo);

        System.out.println("BD Creada");
    }
}
