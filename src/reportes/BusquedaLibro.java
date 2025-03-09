package reportes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusquedaLibro implements Reporte {
    private String nombreLibro;

    public BusquedaLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    @Override
    public void generarReporte(Connection conn) {
        // Consulta para buscar libros con nombre similar al proporcionado
        String sql = "SELECT l.idLibro, l.nombre, l.autor, l.categoria, l.disponible " +
                "FROM libro l " +
                "WHERE l.nombre LIKE CONCAT('%', ?, '%')";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreLibro);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;

            while (rs.next()) {
                int idLibro = rs.getInt("idLibro");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String categoria = rs.getString("categoria");
                boolean disponible = rs.getBoolean("disponible");

                found = true;

                System.out.println("Libro: " + nombre);
                System.out.println("Autor: " + autor);
                System.out.println("Categoría: " + categoria);
                System.out.println("ID: " + idLibro);
                System.out.println("Disponible: " + (disponible ? "Sí" : "No"));
                System.out.println("---------------------------------------------------");
            }

            if (!found) {
                System.out.println("No se encontraron libros con el nombre: " + nombreLibro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al generar el reporte.");
        }
    }
}
