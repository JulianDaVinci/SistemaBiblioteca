import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private final String nombre;
    private final int dni;
    private final String email;

    public Usuario(String nombre, int dni, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
    }

    public void crearUsuario(Connection conn) {
        ResultSet rs = null;
        try {
            String verificarDNI = "SELECT COUNT(*) FROM usuario WHERE dni = ?";
            PreparedStatement stmtVerificar = conn.prepareStatement(verificarDNI);
            stmtVerificar.setInt(1, this.dni);

            rs = stmtVerificar.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("Ya existe un usuario con ese DNI.");
                    return;
                }
            }

            String insertarSQL = "INSERT INTO usuario (nombre, dni, email) VALUES (?, ?, ?)";
            PreparedStatement stmtInsertar = conn.prepareStatement(insertarSQL);
            stmtInsertar.setString(1, this.nombre);
            stmtInsertar.setInt(2, this.dni);
            stmtInsertar.setString(3, this.email);
            stmtInsertar.executeUpdate();

            System.out.println("Usuario creado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear el usuario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar recursos.");
            }
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", dni=" + dni +
                ", email='" + email + '\'' +
                '}';
    }
}
