package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class Prestamo {
    private int idLibro;
    private int idUsuario;
    private Date fechaPrestamo;
    private Date fechaDevolucion;

    public Prestamo(int idLibro, int idUsuario, Date fechaPrestamo, Date fechaDevolucion) {
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public void crearPrestamo(Connection conn) {
        String sqlPrestamo = "INSERT INTO usuario_prestamo (idLibro, idUsuario, fechaPrestamo, fechaDevolucion) VALUES (?, ?, ?, ?)";
        String sqlActualizarLibro = "UPDATE libro SET disponible = 0 WHERE idLibro = ?";
        try (PreparedStatement stmtPrestamo = conn.prepareStatement(sqlPrestamo);
             PreparedStatement stmtActualizarLibro = conn.prepareStatement(sqlActualizarLibro)) {
            conn.setAutoCommit(false); // Iniciar transacción

            stmtPrestamo.setInt(1, idLibro);
            stmtPrestamo.setInt(2, idUsuario);
            stmtPrestamo.setDate(3, fechaPrestamo);
            stmtPrestamo.setDate(4, fechaDevolucion);
            stmtPrestamo.executeUpdate();

            stmtActualizarLibro.setInt(1, idLibro);
            stmtActualizarLibro.executeUpdate();

            conn.commit(); // Mandamo el commit
        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback si falla
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true); // Restaurar modo de confirmación automática
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}