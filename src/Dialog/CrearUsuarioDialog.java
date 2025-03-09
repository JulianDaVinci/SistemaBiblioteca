package Dialog;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrearUsuarioDialog extends JDialog {
    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoEmail;
    private Connection conexion;

    public CrearUsuarioDialog(Frame owner, Connection conexion) {
        super(owner, "Crear Usuario", true);
        this.conexion = conexion;
        agregarComponentes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void agregarComponentes() {
        campoDni = new JTextField(20);
        campoNombre = new JTextField(20);
        campoEmail = new JTextField(20);

        JButton botonCrear = new JButton("Crear");
        botonCrear.addActionListener(e -> crearUsuario());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("DNI:"));
        panel.add(campoDni);
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Email:"));
        panel.add(campoEmail);
        panel.add(botonCrear);

        add(panel, BorderLayout.CENTER);
    }

    private void crearUsuario() {
        try {
            int dni = Integer.parseInt(campoDni.getText());
            String nombre = campoNombre.getText();
            String email = campoEmail.getText();

            if (!usuarioExiste(dni, email)) {
                Usuario usuario = new Usuario(dni, nombre, email);
                usuario.crearUsuario(conexion);
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "El usuario con este DNI o email ya existe.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido para el DNI.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el usuario: " + e.getMessage());
        }
    }

    private boolean usuarioExiste(int dni, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE dni = ? OR email = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}