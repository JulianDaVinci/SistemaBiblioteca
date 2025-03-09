package reportes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroEstado implements Reporte {
    private int idLibro;

    public LibroEstado(int idLibro) {
        this.idLibro = idLibro;
    }

    @Override
    public String generarReporte(Connection conn) {
        return obtenerEstado(conn);
    }

    public String obtenerEstado(Connection conn) {
        StringBuilder estado = new StringBuilder();
        String sqlLibro = "SELECT nombre, disponible FROM libro WHERE idLibro = ?";
        String sqlPrestamo = "SELECT u.nombre, up.fechaPrestamo, up.fechaDevolucion " +
                "FROM usuario_prestamo up " +
                "JOIN usuario u ON up.idUsuario = u.idUsuario " +
                "WHERE up.idLibro = ?";

        try (PreparedStatement pstmtLibro = conn.prepareStatement(sqlLibro);
             PreparedStatement pstmtPrestamo = conn.prepareStatement(sqlPrestamo)) {

            pstmtLibro.setInt(1, idLibro);
            ResultSet rsLibro = pstmtLibro.executeQuery();
            if (rsLibro.next()) {
                String nombreLibro = rsLibro.getString("nombre");
                boolean disponible = rsLibro.getBoolean("disponible");
                estado.append("Libro: ").append(nombreLibro).append("\nDisponible: ").append(disponible ? "Sí" : "No").append("\n");

                if (!disponible) {
                    pstmtPrestamo.setInt(1, idLibro);
                    ResultSet rsPrestamo = pstmtPrestamo.executeQuery();
                    if (rsPrestamo.next()) {
                        String nombreUsuario = rsPrestamo.getString("nombre");
                        String fechaPrestamo = rsPrestamo.getString("fechaPrestamo");
                        String fechaDevolucion = rsPrestamo.getString("fechaDevolucion");
                        estado.append("Prestado a: ").append(nombreUsuario)
                                .append("\nFecha de Préstamo: ").append(fechaPrestamo)
                                .append("\nFecha de Devolución: ").append(fechaDevolucion).append("\n");
                    }
                }
            } else {
                estado.append("No se encontró un libro con el ID proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al obtener el estado del libro: " + e.getMessage();
        }

        return estado.toString();
    }
}