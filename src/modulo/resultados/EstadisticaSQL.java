package modulo.resultados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class EstadisticaSQL implements servicioEstadistica {

    private final Connection conexion;

    public EstadisticaSQL(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void calcularYGuardar(int idEncuesta) {
        int totalPreguntas = obtenerCantidadPreguntas(idEncuesta);
        double[] promedios = new double[totalPreguntas];
        NodoEstadistica cabeza = null;
        String sql = """
            SELECT idPregunta, AVG(CAST(respuesta AS FLOAT)) AS promedio
            FROM Respuestas
            WHERE idPregunta IN (SELECT id FROM Preguntas WHERE idEncuesta = ?)
            GROUP BY idPregunta
        """;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEncuesta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idPregunta = rs.getInt("idPregunta");
                    double promedio = rs.getDouble("promedio");
                    promedios[idPregunta - 1] = promedio;
                    EstadisticaPregunta ep = new EstadisticaPregunta(idPregunta, promedio);
                    NodoEstadistica nuevoNodo = new NodoEstadistica(ep);
                    nuevoNodo.setSiguiente(cabeza);
                    cabeza = nuevoNodo;
                    guardarEstadisticaEnBD(idEncuesta, idPregunta, promedio);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular estadisticas", e);
        }
    }

    private void guardarEstadisticaEnBD(int idEncuesta, int idPregunta, double promedio) {
        String sql = "INSERT INTO Estadisticas (idEncuesta, idPregunta, promedio) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEncuesta);
            stmt.setInt(2, idPregunta);
            stmt.setDouble(3, promedio);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar estadistica", e);
        }
    }

    private int obtenerCantidadPreguntas(int idEncuesta) {
        String sql = "SELECT COUNT(*) FROM Preguntas WHERE idEncuesta = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEncuesta);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar preguntas", e);
        }
    }
}
