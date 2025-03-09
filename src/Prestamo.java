import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class Prestamo {
    private final int idUsuario;
    private final int idLibro;
    private final Date fechaPrestamo;
    private final Date fechaDevolucion;

    // Constructor para inicializar el préstamo
    public Prestamo(int idUsuario, int idLibro, Date fechaPrestamo, Date fechaDevolucion) {
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Método para crear un préstamo en la base de datos
    public void crearPrestamo(Connection conn) {
        String sql = "INSERT INTO usuario_prestamo (idusuario, idlibro, fechaPrestamo, fechaDevolucion) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecemos los valores en el PreparedStatement
            stmt.setInt(1, this.idUsuario);
            stmt.setInt(2, this.idLibro);
            stmt.setDate(3, this.fechaPrestamo);
            stmt.setDate(4, this.fechaDevolucion);

            // Ejecutamos la inserción
            stmt.executeUpdate();
            System.out.println("Préstamo creado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear el préstamo: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "idUsuario=" + idUsuario +
                ", idLibro=" + idLibro +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }
}
