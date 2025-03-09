package Dialog;
import Modelo.Libro;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class CrearLibroDialog extends JDialog {
    private JTextField campoNombre;
    private JTextField campoAutor;
    private JTextField campoCategoria;
    private JCheckBox checkDisponible;
    private Connection conexion;

    public CrearLibroDialog(Frame owner, Connection conexion) {
        super(owner, "Crear Libro", true);
        this.conexion = conexion;
        agregarComponentes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void agregarComponentes() {
        campoNombre = new JTextField(20);
        campoAutor = new JTextField(20);
        campoCategoria = new JTextField(20);
        checkDisponible = new JCheckBox("Disponible");

        JButton botonCrear = new JButton("Crear");
        botonCrear.addActionListener(e -> crearLibro());

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Autor:"));
        panel.add(campoAutor);
        panel.add(new JLabel("Categoría:"));
        panel.add(campoCategoria);
        panel.add(new JLabel("Disponible:"));
        panel.add(checkDisponible);
        panel.add(botonCrear);

        add(panel, BorderLayout.CENTER);
    }

    private void crearLibro() {
        String nombre = campoNombre.getText();
        String autor = campoAutor.getText();
        String categoria = campoCategoria.getText();
        boolean disponible = checkDisponible.isSelected();

        Libro libro = new Libro(nombre, autor, categoria, disponible);
        libro.crearLibro(conexion);
        JOptionPane.showMessageDialog(this, "Libro creado correctamente.");
        dispose();
    }
}