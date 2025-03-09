package Modelo;
import Datos.BaseDeDatos;
import Datos.ConexionBD;
import Dialog.*;
import reportes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Menu extends JFrame {
    private Connection conn;

    public Menu() {
        setTitle("Sistema de Biblioteca");
        setSize(400, 300);
        setLayout(new GridLayout(8, 1));

        try {
            BaseDeDatos.crearBaseDeDatos();
            conn = ConexionBD.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la bd: " + e.getMessage());
            System.exit(1);
        }

        JButton botonCrearUsuario = new JButton("Crear Usuario");
        botonCrearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearUsuarioDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonCrearPrestamo = new JButton("Crear Préstamo");
        botonCrearPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearPrestamoDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonMarcarDevolucion = new JButton("Marcar Devolución");
        botonMarcarDevolucion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MarcarDevolucionDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonCrearLibro = new JButton("Crear Libro");
        botonCrearLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearLibroDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonBuscarLibro = new JButton("Buscar Libro");
        botonBuscarLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarLibroDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonVerLibrosPrestados = new JButton("Ver Libros Prestados");
        botonVerLibrosPrestados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VerLibrosPrestadosDialog(Menu.this, conn).setVisible(true);
            }
        });

        JButton botonGenerarReporte = new JButton("Generar Reporte Último Libro");
        botonGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteUltimoLibro();
            }
        });

        add(botonCrearUsuario);
        add(botonCrearPrestamo);
        add(botonMarcarDevolucion);
        add(botonCrearLibro);
        add(botonBuscarLibro);
        add(botonVerLibrosPrestados);
        add(botonGenerarReporte);
    }

    private void generarReporteUltimoLibro() {
        try {
            int idLibro = obtenerIdUltimoLibro();
            String nombreLibro = obtenerNombreUltimoLibro(idLibro);

            // Crear el reporte composite
            ReporteCompuesto reporteCompuesto = new ReporteCompuesto();
            reporteCompuesto.agregarReporte(new BusquedaLibro(nombreLibro));
            reporteCompuesto.agregarReporte(new LibroEstado(idLibro));

            String reporte = reporteCompuesto.generarReporte(conn);
            JOptionPane.showMessageDialog(this, reporte, "Reporte del Último Libro", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerIdUltimoLibro() throws SQLException {
        String sql = "SELECT idLibro FROM libro ORDER BY idLibro DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idLibro");
            } else {
                throw new SQLException("No se encontraron libros en la bd.");
            }
        }
    }

    private String obtenerNombreUltimoLibro(int idLibro) throws SQLException {
        String sql = "SELECT nombre FROM libro WHERE idLibro = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            } else {
                throw new SQLException("No se encontró un libro con el ID.");
            }
        }
    }
}