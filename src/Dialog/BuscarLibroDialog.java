package Dialog;
import reportes.BusquedaLibro;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class BuscarLibroDialog extends JDialog {
    private JTextField campoNombreLibro;
    private JTextArea areaResultados;
    private Connection conexion;

    public BuscarLibroDialog(Frame parent, Connection conexion) {
        super(parent, "Buscar Libro", true);
        this.conexion = conexion;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nombre del Libro:"));
        campoNombreLibro = new JTextField();
        panel.add(campoNombreLibro);

        areaResultados = new JTextArea(10, 30);
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);

        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreLibro = campoNombreLibro.getText();
                if (nombreLibro != null && !nombreLibro.trim().isEmpty()) {
                    BusquedaLibro busquedaLibro = new BusquedaLibro(nombreLibro);
                    List<String> resultados = busquedaLibro.buscarLibros(conexion);
                    mostrarResultados(resultados);
                } else {
                    JOptionPane.showMessageDialog(BuscarLibroDialog.this, "Ingresa el nombre del libro.");
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