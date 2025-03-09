import reportes.Reporte;
import reportes.LibrosPrestados;
import reportes.BusquedaLibro;
import reportes.LibrosMasPrestados;
import reportes.ReporteCompuesto;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Obtener la conexión a la base de datos (asumiendo que ya tienes una clase ConexionBD)
        Connection conn = ConexionBD.obtenerConexion();

        // Crear el reporte de búsqueda de libros
        BusquedaLibro reporteBusqueda = new BusquedaLibro("pepito");

        // Generar el reporte de libros que coincidan con "pepito"
        reporteBusqueda.generarReporte(conn);

        // Cerrar la conexión (si es necesario)
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();  // Cerrar la conexión al final
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la conexión.");
        }
    }
}
