package modulo.resultados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modulo.respuestas.ListaRespuesta;
import modulo.respuestas.NodoRespuesta;
import modulo.respuestas.Respuesta;
import modulo.respuestas.repositorioRespuesta;

public class RespuestaSQL implements repositorioRespuesta {

    private Connection conexion;

    public RespuestaSQL(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void guardarRespuestas(String dniParticipante, ListaRespuesta respuestas) {
        NodoRespuesta actual = respuestas.getCabeza();
        while (actual != null) {
            Respuesta r = actual.getRespuesta();
            String sql = "INSERT INTO Respuestas (dniParticipante, idPregunta, contenido) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, dniParticipante);
                stmt.setInt(2, r.getIdPregunta());
                stmt.setString(3, r.getContenido());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al guardar respuesta: " + e.getMessage());
            }
            actual = actual.getSiguiente();
        }
    }
}
