import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Libro {
    private final String nombre;
    private final String autor;
    private final String categoria;
    private final boolean disponible;

    public Libro(String nombre, String autor, String categoria, boolean disponible) {
        this.nombre = nombre;
        this.autor = autor;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public void crearLibro(Connection conn) {
        PreparedStatement stmtInsertar = null;
        try {
            String insertarSQL = "INSERT INTO libro (nombre, autor, categoria, disponible) VALUES (?, ?, ?, ?)";
            stmtInsertar = conn.prepareStatement(insertarSQL);
            stmtInsertar.setString(1, this.nombre);
            stmtInsertar.setString(2, this.autor);
            stmtInsertar.setString(3, this.categoria);
            stmtInsertar.setInt(4, this.disponible ? 1 : 0);
            stmtInsertar.executeUpdate();

            System.out.println("Libro creado");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear el libro: " + e.getMessage());
        } finally {
            try {
                if (stmtInsertar != null) {
                    stmtInsertar.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar stmt.");
            }
        }
    }

    @Override
    public String toString() {
        return "Libro{" +
                "nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", categoria='" + categoria + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
