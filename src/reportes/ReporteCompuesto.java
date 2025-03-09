package reportes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReporteCompuesto implements Reporte {
    private List<Reporte> reportes = new ArrayList<>();

    public void agregarReporte(Reporte reporte) {
        reportes.add(reporte);
    }

    public void eliminarReporte(Reporte reporte) {
        reportes.remove(reporte);
    }

    @Override
    public void generarReporte(Connection conn) {
        for (Reporte reporte : reportes) {
            reporte.generarReporte(conn);
        }
    }
}
