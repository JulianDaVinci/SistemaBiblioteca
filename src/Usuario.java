import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private String nombre;
    private int dni;
    private String email;

    // Constructor
    public Usuario(String nombre, int dni, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
    }

    // Método para crear un usuario
    public void crearUsuario() {
        Connection conn = null;
        ResultSet rs = null;
        try {
            // Obtener la conexión
            conn = ConexionBD.obtenerConexion();

            // Consulta para verificar si ya existe un usuario con el mismo DNI
            String verificarSQL = "SELECT COUNT(*) FROM usuario WHERE dni = ?";
            PreparedStatement stmtVerificar = conn.prepareStatement(verificarSQL);
            stmtVerificar.setInt(1, this.dni);

            // Ejecutamos la consulta y obtenemos el ResultSet
            rs = stmtVerificar.executeQuery();

            // Si rs.next() devuelve true, es que hay un resultado
            if (rs.next()) {
                int count = rs.getInt(1); // Obtiene el valor de la primera columna (COUNT(*))

                // Si count > 0, significa que ya existe un usuario con ese DNI
                if (count > 0) {
                    System.out.println("Ya existe un usuario con ese DNI.");
                    return;
                }
            }

            // Si no existe, insertamos el nuevo usuario
            String insertarSQL = "INSERT INTO usuario (nombre, dni, email) VALUES (?, ?, ?)";
            PreparedStatement stmtInsertar = conn.prepareStatement(insertarSQL);
            stmtInsertar.setString(1, this.nombre);
            stmtInsertar.setInt(2, this.dni);
            stmtInsertar.setString(3, this.email);
            stmtInsertar.executeUpdate(); // Ejecutamos el INSERT

            System.out.println("Usuario creado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear el usuario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar recursos.");
            }
        }
    }
}
