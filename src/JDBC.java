import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "julian";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws Exception {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new Exception("Error al conectar con la bd", e);
        }
    }
}