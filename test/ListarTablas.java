import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conexionSQL.ConexionSQL;

public class ListarTablas {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexión a BD");
                return;
            }

            stmt = conn.createStatement();

            System.out.println("\n=== TABLAS EN LA BD ===");
            rs = stmt.executeQuery("SELECT name FROM sys.tables WHERE type='U' ORDER BY name");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("name"));
            }
            rs.close();

            System.out.println("\n=== COLUMNAS DE UsuariosExternos ===");
            rs = stmt.executeQuery(
                    "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'UsuariosExternos' ORDER BY ORDINAL_POSITION");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("COLUMN_NAME") + " (" + rs.getString("DATA_TYPE") + ")");
            }
            rs.close();

            System.out.println("\n=== COLUMNAS DE Participantes ===");
            rs = stmt.executeQuery(
                    "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Participantes' ORDER BY ORDINAL_POSITION");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("COLUMN_NAME") + " (" + rs.getString("DATA_TYPE") + ")");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
