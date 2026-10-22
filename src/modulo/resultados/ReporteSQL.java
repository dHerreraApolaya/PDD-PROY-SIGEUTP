package modulo.resultados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ReporteSQL implements GenerarReporte {

    private final Connection conexion;

    public ReporteSQL(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public NodoResultado generarReporte(int idEncuesta) {
        NodoResultado cabeza = null;
        String sqlPreguntas = "SELECT id, texto FROM Preguntas WHERE idEncuesta = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sqlPreguntas)) {
            stmt.setInt(1, idEncuesta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idPregunta = rs.getInt("id");
                    String texto = rs.getString("texto");
                    ResultadoPregunta rp = new ResultadoPregunta(idPregunta, texto, 100);
                    cargarRespuestas(idPregunta, rp);
                    NodoResultado nuevoNodo = new NodoResultado(rp);
                    nuevoNodo.setSiguiente(cabeza);
                    cabeza = nuevoNodo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar reporte de preguntas", e);
        }
        return cabeza;
    }

    private void cargarRespuestas(int idPregunta, ResultadoPregunta rp) {
        String sqlRespuestas = "SELECT respuesta FROM Respuestas WHERE idPregunta = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sqlRespuestas)) {
            stmt.setInt(1, idPregunta);
            try (ResultSet rs = stmt.executeQuery()) {
                int index = 0;
                while (rs.next() && index < rp.getRespuestas().length) {
                    rp.getRespuestas()[index++] = rs.getString("respuesta");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar respuestas", e);
        }
    }
}
