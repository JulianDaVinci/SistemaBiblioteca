package reportes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusquedaLibro implements Reporte {
    private String nombreLibro;

    public BusquedaLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    @Override
    public String generarReporte(Connection conn) {
        List<String> resultados = buscarLibros(conn);
        return mostrarResultados(resultados);
    }

    public List<String> buscarLibros(Connection conn) {
        List<String> resultados = new ArrayList<>();
        String sqlLibro = "SELECT idLibro, nombre, autor, categoria, disponible FROM libro WHERE nombre LIKE ?";
        String sqlUsuario = "SELECT u.dni FROM usuario_prestamo up JOIN usuario u ON up.idUsuario = u.idUsuario WHERE up.idLibro = ? ORDER BY up.idPrestamo DESC LIMIT 1";

        try (PreparedStatement stmtLibro = conn.prepareStatement(sqlLibro)) {
            stmtLibro.setString(1, "%" + nombreLibro + "%");
            ResultSet rsLibro = stmtLibro.executeQuery();

            while (rsLibro.next()) {
                int idLibro = rsLibro.getInt("idLibro");
                String nombre = rsLibro.getString("nombre");
                String autor = rsLibro.getString("autor");
                String categoria = rsLibro.getString("categoria");
                boolean disponible = rsLibro.getBoolean("disponible");

                StringBuilder resultado = new StringBuilder(String.format("Libro: %s\nAutor: %s\nCategoría: %s\nID: %d\nDisponible: %s\n",
                        nombre, autor, categoria, idLibro, disponible ? "Sí" : "No"));

                try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {
                    stmtUsuario.setInt(1, idLibro);
                    ResultSet rsUsuario = stmtUsuario.executeQuery();
                    if (rsUsuario.next()) {
                        int dniUsuario = rsUsuario.getInt("dni");
                        resultado.append(String.format("DNI del último solicitante: %d\n", dniUsuario));
                    }
                }

                resultados.add(resultado.toString());
            }

            if (resultados.isEmpty()) {
                resultados.add("No se encontraron libros con el nombre: " + nombreLibro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultados.add("Error al buscar libros: " + e.getMessage());
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