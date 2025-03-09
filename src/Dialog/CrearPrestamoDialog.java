package Dialog;
import Modelo.Prestamo;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CrearPrestamoDialog extends JDialog {
    private JTextField campoIdLibro;
    private JTextField campoDniUsuario;
    private Connection conn;

    public CrearPrestamoDialog(Frame owner, Connection conn) {
        super(owner, "Crear Préstamo", true);
        this.conn = conn;
        agregarComponentes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void agregarComponentes() {
        campoIdLibro = new JTextField(20);
        campoDniUsuario = new JTextField(20);

        JButton botonCrear = new JButton("Crear");
        botonCrear.addActionListener(e -> crearPrestamo());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID Libro:"));
        panel.add(campoIdLibro);
        panel.add(new JLabel("DNI Usuario:"));
        panel.add(campoDniUsuario);
        panel.add(botonCrear);

        add(panel, BorderLayout.CENTER);
    }

    private void crearPrestamo() {
        try {
            int idLibro = Integer.parseInt(campoIdLibro.getText());
            int dni = Integer.parseInt(campoDniUsuario.getText());

            if (libroDisponible(idLibro)) {
                int idUsuario = obtenerIdUsuarioPorDni(dni);
                if (idUsuario != -1) {
                    Date fechaPrestamo = Date.valueOf(LocalDate.now());
                    Date fechaDevolucion = Date.valueOf(LocalDate.now().plusDays(15));

                    Prestamo prestamo = new Prestamo(idLibro, idUsuario, fechaPrestamo, fechaDevolucion);
                    prestamo.crearPrestamo(conn);
                    JOptionPane.showMessageDialog(this, "Préstamo creado correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El libro no está disponible.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido para el ID del libro y el DNI del usuario.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el préstamo: " + e.getMessage());
        }
    }

    private boolean libroDisponible(int idLibro) throws SQLException {
        String sql = "SELECT disponible FROM libro WHERE idLibro = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("disponible");
                }
            }
        }
        return false;
    }

    private int obtenerIdUsuarioPorDni(int dni) throws SQLException {
        String sql = "SELECT idUsuario FROM usuario WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idUsuario");
                }
            }
        }
        return -1;
    }
}