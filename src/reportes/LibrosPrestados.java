package reportes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrosPrestados implements Reporte {
    private final int dniUsuario;

    public LibrosPrestados(int dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    @Override
    public String generarReporte(Connection conn) {
        List<String> resultados = buscarLibrosPrestados(conn);
        return mostrarResultados(resultados);
    }

    public List<String> buscarLibrosPrestados(Connection conn) {
        List<String> resultados = new ArrayList<>();
        String sql = "SELECT l.idLibro, l.nombre, l.autor, l.categoria, up.fechaPrestamo " +
                "FROM libro l " +
                "JOIN usuario_prestamo up ON l.idLibro = up.idLibro " +
                "JOIN usuario u ON up.idUsuario = u.idUsuario " +
                "WHERE u.dni = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dniUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idLibro = rs.getInt("idLibro");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String categoria = rs.getString("categoria");
                String fechaPrestamo = rs.getString("fechaPrestamo");

                String resultado = String.format("Libro: %s\nAutor: %s\nCategoría: %s\nID: %d\nFecha de Préstamo: %s\n",
                        nombre, autor, categoria, idLibro, fechaPrestamo);
                resultados.add(resultado);
            }

            if (resultados.isEmpty()) {
                resultados.add("No se encontraron libros prestados para el usuario, DNI: " + dniUsuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultados.add("Error al buscar libros prestados: " + e.getMessage());
        }
        return resultados;
    }

    public String mostrarResultados(List<String> resultados) {
        StringBuilder reporte = new StringBuilder();
        for (String resultado : resultados) {
            reporte.append(resultado).append("\n");
        }
        return reporte.toString();
    }
}