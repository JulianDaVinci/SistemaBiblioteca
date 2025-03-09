package Dialog;
import reportes.LibrosPrestados;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class VerLibrosPrestadosDialog extends JDialog {
    private JTextField campoDniUsuario;
    private JTextArea areaResultados;
    private Connection conexion;

    public VerLibrosPrestadosDialog(Frame parent, Connection conexion) {
        super(parent, "Ver Libros Prestados", true);
        this.conexion = conexion;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("DNI del Usuario:"));
        campoDniUsuario = new JTextField();
        panel.add(campoDniUsuario);

        areaResultados = new JTextArea(10, 30);
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);

        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dniUsuarioText = campoDniUsuario.getText();
                if (dniUsuarioText != null && !dniUsuarioText.trim().isEmpty()) {
                    try {
                        int dniUsuario = Integer.parseInt(dniUsuarioText);
                        LibrosPrestados librosPrestados = new LibrosPrestados(dniUsuario);
                        List<String> resultados = librosPrestados.buscarLibrosPrestados(conexion);
                        mostrarResultados(resultados);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(VerLibrosPrestadosDialog.this, "Ingresa un DNI válido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(VerLibrosPrestadosDialog.this, "Ingresa el DNI del usuario.");
                }
            }
        });

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botonBuscar, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void mostrarResultados(List<String> resultados) {
        areaResultados.setText("");
        for (String resultado : resultados) {
            areaResultados.append(resultado + "\n");
        }
    }
}