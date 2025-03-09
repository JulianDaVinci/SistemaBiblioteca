package reportes;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReporteCompuesto implements Reporte {
    private List<Reporte> reportes = new ArrayList<>();

    public void agregarReporte(Reporte reporte) {
        reportes.add(reporte);
    }

    @Override
    public String generarReporte(Connection conn) {
        StringBuilder reporteCompleto = new StringBuilder();
        for (Reporte reporte : reportes) {
            reporteCompleto.append(reporte.generarReporte(conn)).append("\n");
        }
        return reporteCompleto.toString();
    }
}