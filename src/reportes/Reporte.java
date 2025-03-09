package reportes;

import java.sql.Connection;

public interface Reporte {
    void generarReporte(Connection conn);
}
