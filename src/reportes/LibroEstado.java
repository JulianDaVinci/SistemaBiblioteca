package reportes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroEstado {
    private int idLibro;

    public LibroEstado(int idLibro) {
        this.idLibro = idLibro;
    }

    public void generarReporte(Connection conn) {
        String sql = "SELECT u.nombre, u.dni, up.fechaPrestamo, up.fechaDevolucion " +
                "FROM usuario_prestamo up " +
                "JOIN usuario u ON up.idusuario = u.id " +
                "WHERE up.idLibro = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);  // Establecemos el id del libro que queremos buscar
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreUsuario = rs.getString("nombre");
                String dniUsuario = rs.getString("dni");
                java.sql.Date fechaPrestamo = rs.getDate("fechaPrestamo");
                java.sql.Date fechaDevolucion = rs.getDate("fechaDevolucion");

                System.out.println("📚 Detalles del libro con ID: " + idLibro);
                System.out.println("Usuario que tiene el libro: " + nombreUsuario);
                System.out.println("DNI: " + dniUsuario);
                System.out.println("Fecha de préstamo: " + fechaPrestamo);
                System.out.println("Fecha de devolución: " + fechaDevolucion);
            } else {
                System.out.println("No se encontró ningún préstamo para el libro con ID: " + idLibro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al generar el reporte de estado del libro.");
        }
    }
}
