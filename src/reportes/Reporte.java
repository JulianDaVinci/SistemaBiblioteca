package reportes;
import java.sql.Connection;

public interface Reporte {
    String generarReporte(Connection conn);
}