package Dialog;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarcarDevolucionDialog extends JDialog {
    private JTextField campoDni;
    private JTextField campoIdLibro;
    private Connection conexion;

    public MarcarDevolucionDialog(Frame owner, Connection conexion) {
        super(owner, "Marcar Devolución", true);
        this.conexion = conexion;
        agregarComponentes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void agregarComponentes() {
        campoDni = new JTextField(20);
        campoIdLibro = new JTextField(20);
        JButton botonDevolver = new JButton("Devolver");
        botonDevolver.addActionListener(e -> marcarDevolucion());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("DNI Usuario:"));
        panel.add(campoDni);
        panel.add(new JLabel("ID Libro:"));
        panel.add(campoIdLibro);
        panel.add(botonDevolver);

        add(panel, BorderLayout.CENTER);
    }

    private void marcarDevolucion() {
        try {
            int dni = Integer.parseInt(campoDni.getText());
            int idLibro = Integer.parseInt(campoIdLibro.getText());
            System.out.println("DNI: " + dni);
            System.out.println("ID Libro: " + idLibro);

            if (marcarDevolucionEnBaseDeDatos(dni, idLibro)) {
                JOptionPane.showMessageDialog(this, "Devolución marcada.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al marcar la devolución. verifica los datos.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido para el DNI y el ID del libro.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al marcar la devolución: " + e.getMessage());
        }
    }

    private boolean marcarDevolucionEnBaseDeDatos(int dni, int idLibro) throws SQLException {
        String sqlObtenerIdUsuario = "SELECT idUsuario FROM usuario WHERE dni = ?";
        String sqlVerificarDisponibilidadLibro = "SELECT disponible FROM libro WHERE idLibro = ?";
        String sqlActualizarDevolucion = "UPDATE usuario_prestamo SET fechaDevolucion = CURRENT_DATE WHERE idUsuario = ? AND idLibro = ?";
        String sqlActualizarDisponibilidadLibro = "UPDATE libro SET disponible = 1 WHERE idLibro = ?";

        try (PreparedStatement stmtObtenerIdUsuario = conexion.prepareStatement(sqlObtenerIdUsuario);
             PreparedStatement stmtVerificarDisponibilidadLibro = conexion.prepareStatement(sqlVerificarDisponibilidadLibro);
             PreparedStatement stmtActualizarDevolucion = conexion.prepareStatement(sqlActualizarDevolucion);
             PreparedStatement stmtActualizarDisponibilidadLibro = conexion.prepareStatement(sqlActualizarDisponibilidadLibro)) {

            //  buscamos el id del usuario a partir del dni
            stmtObtenerIdUsuario.setInt(1, dni);
            ResultSet rsIdUsuario = stmtObtenerIdUsuario.executeQuery();
            if (!rsIdUsuario.next()) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                return false;
            }
            int idUsuario = rsIdUsuario.getInt("idUsuario");
            System.out.println("ID Usuario: " + idUsuario);

            // Verificamos si el libro ya está disponible
            stmtVerificarDisponibilidadLibro.setInt(1, idLibro);
            ResultSet rsDisponibilidadLibro = stmtVerificarDisponibilidadLibro.executeQuery();
            if (rsDisponibilidadLibro.next() && rsDisponibilidadLibro.getInt("disponible") == 1) {
                JOptionPane.showMessageDialog(this, "El libro ya está disponible en la biblioteca.");
                return false;
            }

            // Marcar la devolución en la base de datos
            stmtActualizarDevolucion.setInt(1, idUsuario);
            stmtActualizarDevolucion.setInt(2, idLibro);
            int filasActualizadas = stmtActualizarDevolucion.executeUpdate();
           // System.out.println("Filas actualizadas en usuario_prestamo: " + filasActualizadas);
            if (filasActualizadas > 0) {
                // Actualizar disponibilidad del libro
                stmtActualizarDisponibilidadLibro.setInt(1, idLibro);
                int filasActualizadasLibro = stmtActualizarDisponibilidadLibro.executeUpdate();
               // System.out.println("Filas actualizadas en libro: " + filasActualizadasLibro);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Error al marcar la devolución.");
                return false;
            }
        }
    }
}