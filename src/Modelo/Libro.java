package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Libro {
    private String nombre;
    private String autor;
    private String categoria;
    private boolean disponible;

    public Libro(String nombre, String autor, String categoria, boolean disponible) {
        this.nombre = nombre;
        this.autor = autor;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public void crearLibro(Connection conn) {
        String sql = "INSERT INTO libro (nombre, autor, categoria, disponible) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, autor);
            stmt.setString(3, categoria);
            stmt.setBoolean(4, disponible);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}