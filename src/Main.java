import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Obtener la conexión a la base de datos
        Connection conn = ConexionBD.obtenerConexion();

        // Crear un nuevo usuario
        Usuario usuario = new Usuario("Juan Pérez", 123456782, "juan.perez@example.com");
        usuario.crearUsuario(conn);  // Crear usuario en la base de datos, pasando la conexión

        // Imprimir los detalles del usuario
        System.out.println(usuario);

        // Crear un nuevo libro
        Libro libro = new Libro("El señor de los anillos", "J.R.R. Tolkien", "Fantasía", true);
        libro.crearLibro(conn);  // Crear libro en la base de datos, pasando la conexión

        // Imprimir los detalles del libro
        System.out.println(libro);

        // Cerrar la conexión (si es necesario)
    }
}
