import java.sql.*;
import conexionSQL.ConexionSQL;

public class VerificarBD {
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
            
            System.out.println("\n=== ENCUESTAS ===");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Encuestas");
            if (rs.next()) System.out.println("Total: " + rs.getInt("total"));
            rs.close();
            
            rs = stmt.executeQuery("SELECT id_encuesta, titulo FROM Encuestas");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id_encuesta") + " | " + rs.getString("titulo"));
            }
            rs.close();
            
            System.out.println("\n=== PREGUNTAS ===");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Preguntas");
            if (rs.next()) System.out.println("Total: " + rs.getInt("total"));
            rs.close();
            
            rs = stmt.executeQuery("SELECT id_pregunta, id_encuesta, texto FROM Preguntas");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id_pregunta") + " | Encuesta: " + rs.getInt("id_encuesta") + " | " + rs.getString("texto"));
            }
            rs.close();
            
            System.out.println("\n=== PARTICIPANTES ===");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Participantes");
            if (rs.next()) System.out.println("Total: " + rs.getInt("total"));
            rs.close();
            
            rs = stmt.executeQuery("SELECT id_participante, nombre FROM Participantes");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id_participante") + " | " + rs.getString("nombre"));
            }
            rs.close();
            
            System.out.println("\n=== RESPUESTAS ===");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Respuestas");
            if (rs.next()) System.out.println("Total: " + rs.getInt("total"));
            rs.close();
            
            rs = stmt.executeQuery("SELECT id_respuesta, id_encuesta, id_pregunta, id_participante, contenido FROM Respuestas");
            while (rs.next()) {
                System.out.println("  ID: " + rs.getInt("id_respuesta") + 
                " | Encuesta: " + rs.getInt("id_encuesta") +
                " | Pregunta: " + rs.getInt("id_pregunta") +
                " | Participante: " + rs.getInt("id_participante") +
                " | Respuesta: " + rs.getString("contenido"));
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
