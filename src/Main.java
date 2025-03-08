import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        try {
            BaseDeDatos.crearBaseDeDatos();
        } catch (SQLException e) {
            System.out.println("Error al verificar o crear la base de datos: " + e.getMessage());
        }
        Usuario usuario = new Usuario("Juan Pérez", 123456782, "juan.perez@example.com");

        // Insertar el usuarisso en la base aade datos
        usuario.crearUsuario();

        // Imprimir los detalles del usuario
        System.out.println(usuario);
    }
}
