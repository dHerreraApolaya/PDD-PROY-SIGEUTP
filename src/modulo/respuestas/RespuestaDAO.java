package modulo.respuestas;

import modulo.preguntas.Pregunta;
import modulo.preguntas.PreguntaDAO;
import conexionSQL.ConexionSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RespuestaDAO {

    private final ListaRespuesta listaRespuestas = new ListaRespuesta();
    private final PreguntaDAO preguntaDAO;
    private int contadorId = 1;

    public RespuestaDAO(PreguntaDAO preguntaDAO) {
        this.preguntaDAO = preguntaDAO;
    }

    public static int guardarRespuestasEncuesta(List<Respuesta> respuestas) throws SQLException {
        validarRespuestasEncuesta(respuestas);
        List<Integer> identificadores = new ArrayList<>();
        try (Connection conexion = ConexionSQL.getConnection()) {
            if (conexion == null) {
                throw new SQLException("No se pudo conectar con SQL Server. Las respuestas no se guardaron.");
            }
            conexion.setAutoCommit(false);
            try {
                for (Respuesta respuesta : respuestas) {
                    int idRespuesta = insertarRespuesta(conexion, respuesta);
                    verificarRespuestaGuardada(conexion, idRespuesta, respuesta);
                    identificadores.add(idRespuesta);
                }
                conexion.commit();
            } catch (SQLException excepcion) {
                try {
                    conexion.rollback();
                } catch (SQLException errorReversion) {
                    excepcion.addSuppressed(errorReversion);
                }
                throw excepcion;
            }
        }
        for (int indice = 0; indice < respuestas.size(); indice++) {
            respuestas.get(indice).setIdRespuesta(identificadores.get(indice));
        }
        return respuestas.size();
    }

    private static void validarRespuestasEncuesta(List<Respuesta> respuestas) {
        if (respuestas == null || respuestas.isEmpty() || respuestas.get(0) == null) {
            throw new IllegalArgumentException("No hay respuestas para guardar.");
        }
        int idParticipante = respuestas.get(0).getIdParticipante();
        int idEncuesta = respuestas.get(0).getIdEncuesta();
        Set<Integer> preguntasRespondidas = new HashSet<>();
        for (Respuesta respuesta : respuestas) {
            if (respuesta == null || idParticipante <= 0 || idEncuesta <= 0
                    || respuesta.getIdParticipante() != idParticipante || respuesta.getIdEncuesta() != idEncuesta
                    || respuesta.getIdPregunta() <= 0 || respuesta.getContenido() == null
                    || respuesta.getContenido().isBlank() || !preguntasRespondidas.add(respuesta.getIdPregunta())) {
                throw new IllegalArgumentException("Las respuestas deben pertenecer al mismo participante y encuesta, sin preguntas repetidas.");
            }
        }
    }

    private static int insertarRespuesta(Connection conexion, Respuesta respuesta) throws SQLException {
        String consultaPregunta = "SELECT id_pregunta FROM Preguntas WHERE id_pregunta = ? AND id_encuesta = ? AND estado = 'ACTIVO'";
        try (PreparedStatement sentencia = conexion.prepareStatement(consultaPregunta)) {
            sentencia.setInt(1, respuesta.getIdPregunta());
            sentencia.setInt(2, respuesta.getIdEncuesta());
            try (ResultSet resultados = sentencia.executeQuery()) {
                if (!resultados.next()) {
                    throw new SQLException("La pregunta " + respuesta.getIdPregunta() + " no esta activa o no pertenece a la encuesta seleccionada.");
                }
            }
        }
        String insercion = "INSERT INTO Respuestas (id_pregunta, id_participante, id_encuesta, contenido, tiempo_respuesta, fecha_respuesta) "
                + "VALUES (?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement sentencia = conexion.prepareStatement(insercion, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, respuesta.getIdPregunta());
            sentencia.setInt(2, respuesta.getIdParticipante());
            sentencia.setInt(3, respuesta.getIdEncuesta());
            sentencia.setString(4, respuesta.getContenido());
            sentencia.setInt(5, 0);
            if (sentencia.executeUpdate() != 1) {
                throw new SQLException("No se pudo guardar la respuesta de la pregunta " + respuesta.getIdPregunta());
            }
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (!claves.next() || claves.getInt(1) <= 0) {
                    throw new SQLException("SQL Server no devolvio el identificador de la respuesta.");
                }
                return claves.getInt(1);
            }
        }
    }

    private static void verificarRespuestaGuardada(Connection conexion, int idRespuesta, Respuesta respuesta) throws SQLException {
        String consulta = "SELECT id_pregunta, id_participante, id_encuesta, contenido FROM Respuestas WHERE id_respuesta = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idRespuesta);
            try (ResultSet resultados = sentencia.executeQuery()) {
                if (!resultados.next() || resultados.getInt("id_pregunta") != respuesta.getIdPregunta()
                        || resultados.getInt("id_participante") != respuesta.getIdParticipante()
                        || resultados.getInt("id_encuesta") != respuesta.getIdEncuesta()
                        || !respuesta.getContenido().equals(resultados.getString("contenido"))) {
                    throw new SQLException("No se pudo verificar la respuesta " + idRespuesta + " en SQL Server.");
                }
            }
        }
    }

    public void registrarRespuesta(Respuesta respuesta) {
        Pregunta p = preguntaDAO.buscarPorId(respuesta.getIdPregunta());
        if (p == null) {
            System.out.println("No existe una pregunta con ID "
                    + respuesta.getIdPregunta());
            return;
        }

        respuesta.setIdRespuesta(contadorId++);
        listaRespuestas.agregar(respuesta);
   
        guardarEnBD(respuesta);
        
        System.out.println("Respuesta registrada para pregunta ID "
                + respuesta.getIdPregunta());
    }

    private void guardarEnBD(Respuesta respuesta) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println(" No hay conexion a BD. Respuesta guardada solo en memoria.");
                return;
            }

            String sql = "INSERT INTO Respuestas (id_pregunta, id_participante, id_encuesta, contenido, tiempo_respuesta, fecha_respuesta) " +
                        "VALUES (?, ?, ?, ?, ?, GETDATE())";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, respuesta.getIdPregunta());
            pstmt.setInt(2, respuesta.getIdParticipante());
            pstmt.setInt(3, respuesta.getIdEncuesta());
            pstmt.setString(4, respuesta.getContenido());
            pstmt.setInt(5, 0); 

            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("� Respuesta guardada en BD: Pregunta " + respuesta.getIdPregunta());
            }

        } catch (SQLException e) {
            System.out.println(" Error al guardar respuesta en BD: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
              
            }
        }
    }

    public void mostrarRespuestas() {
        listaRespuestas.mostrar();
    }

    public int contarRespuestas() {
        return listaRespuestas.contar();
    }

    public Respuesta buscarPorPregunta(int idPregunta) {
        return listaRespuestas.buscarPorPregunta(idPregunta);
    }

    public ListaRespuesta getListaRespuesta() {
     
        cargarTodasRespuestasDesdeDB();
        return listaRespuestas;
    }

    private void cargarTodasRespuestasDesdeDB() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println(" No hay conexión a BD. Usando respuestas en memoria.");
                return;
            }

            String sql = "SELECT id_respuesta, id_pregunta, id_participante, id_encuesta, contenido, fecha_respuesta " +
                        "FROM Respuestas ORDER BY fecha_respuesta DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Respuesta respuesta = new Respuesta();
                respuesta.setIdRespuesta(rs.getInt("id_respuesta"));
                respuesta.setIdPregunta(rs.getInt("id_pregunta"));
                respuesta.setIdParticipante(rs.getInt("id_participante"));
                respuesta.setIdEncuesta(rs.getInt("id_encuesta"));
                respuesta.setContenido(rs.getString("contenido"));
                
                java.sql.Timestamp timestamp = rs.getTimestamp("fecha_respuesta");
                if (timestamp != null) {
                    respuesta.setFechaRespuesta(timestamp.toLocalDateTime());
                }

                if (!existeEnLista(respuesta.getIdRespuesta())) {
                    listaRespuestas.agregar(respuesta);
                }
            }

        } catch (SQLException e) {
            System.out.println(" Error al cargar respuestas desde BD: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
         
            }
        }
    }

    private boolean existeEnLista(int idRespuesta) {
        NodoRespuesta actual = listaRespuestas.getCabeza();
        while (actual != null) {
            if (actual.getRespuesta().getIdRespuesta() == idRespuesta) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public ListaRespuesta listarHistorialPorEncuesta(int idEncuesta) {
     
        cargarRespuestasDesdeDB(idEncuesta);
       
        return listaRespuestas.filtrarPorEncuesta(idEncuesta);
    }

    private void cargarRespuestasDesdeDB(int idEncuesta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println(" No hay conexion a BD. Usando respuestas en memoria.");
                return;
            }

            String sql = "SELECT id_respuesta, id_pregunta, id_participante, id_encuesta, contenido, fecha_respuesta " +
                        "FROM Respuestas WHERE id_encuesta = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEncuesta);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Respuesta respuesta = new Respuesta();
                respuesta.setIdRespuesta(rs.getInt("id_respuesta"));
                respuesta.setIdPregunta(rs.getInt("id_pregunta"));
                respuesta.setIdParticipante(rs.getInt("id_participante"));
                respuesta.setIdEncuesta(rs.getInt("id_encuesta"));
                respuesta.setContenido(rs.getString("contenido"));
                
                java.sql.Timestamp timestamp = rs.getTimestamp("fecha_respuesta");
                if (timestamp != null) {
                    respuesta.setFechaRespuesta(timestamp.toLocalDateTime());
                }

                listaRespuestas.agregar(respuesta);
            }

        } catch (SQLException e) {
            System.out.println(" Error al cargar respuestas desde BD: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
              
            }
        }
    }

    // ============================================================================
    // NUEVOS METODOS PARA ACTUALIZACION Y VALIDACION GRANULAR
    // ============================================================================

    /**
     * Obtiene TODAS las respuestas de un participante para una encuesta específica
     * @param idParticipante ID del participante
     * @param idEncuesta ID de la encuesta
     * @return Lista de respuestas del participante en esa encuesta
     */
    public java.util.List<Respuesta> obtenerRespuestasParticipanteEncuesta(
            int idParticipante, int idEncuesta) {
        java.util.List<Respuesta> resultado = new java.util.ArrayList<>();
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.getIdParticipante() == idParticipante && 
                r.getIdEncuesta() == idEncuesta) {
                resultado.add(r);
            }
            nodo = nodo.getSiguiente();
        }
        
        return resultado;
    }

    /**
     * Verifica si existe una respuesta para una pregunta específica de un participante
     * @param idParticipante ID del participante
     * @param idEncuesta ID de la encuesta
     * @param idPregunta ID de la pregunta
     * @return true si existe respuesta, false si no existe
     */
    public boolean existeRespuestaPregunta(int idParticipante, int idEncuesta, int idPregunta) {
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.getIdParticipante() == idParticipante && 
                r.getIdEncuesta() == idEncuesta && 
                r.getIdPregunta() == idPregunta) {
                return true;
            }
            nodo = nodo.getSiguiente();
        }
        
        return false;
    }

    /**
     * Obtiene la respuesta específica de un participante para una pregunta
     * @param idParticipante ID del participante
     * @param idEncuesta ID de la encuesta
     * @param idPregunta ID de la pregunta
     * @return Respuesta si existe, null si no existe
     */
    public Respuesta obtenerRespuestaPregunta(int idParticipante, int idEncuesta, int idPregunta) {
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.getIdParticipante() == idParticipante && 
                r.getIdEncuesta() == idEncuesta && 
                r.getIdPregunta() == idPregunta) {
                return r;
            }
            nodo = nodo.getSiguiente();
        }
        
        return null;
    }

    /**
     * Actualiza una respuesta existente en la BD
     * @param respuesta La respuesta actualizada
     */
    public void actualizarRespuesta(Respuesta respuesta) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println(" No hay conexion a BD. Respuesta no actualizada.");
                return;
            }

            // UPDATE usando (participante, encuesta, pregunta) como clave
            String sql = "UPDATE Respuestas SET contenido = ?, fecha_respuesta = GETDATE() " +
                        "WHERE id_participante = ? AND id_encuesta = ? AND id_pregunta = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, respuesta.getContenido());
            pstmt.setInt(2, respuesta.getIdParticipante());
            pstmt.setInt(3, respuesta.getIdEncuesta());
            pstmt.setInt(4, respuesta.getIdPregunta());

            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("✓ Respuesta actualizada: Pregunta " + respuesta.getIdPregunta());
            }

        } catch (SQLException e) {
            System.out.println(" Error al actualizar respuesta en BD: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
    
    // ============ METODOS DE FILTRADO AVANZADO (Fase 3) ============
    
    public java.util.List<Respuesta> obtenerRespuestasFiltradasPorTipo(String tipo) {
        java.util.List<Respuesta> respuestasFiltradasPorTipo = new java.util.ArrayList<>();
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            Pregunta p = preguntaDAO.buscarPorId(r.getIdPregunta());
            if (p != null && p.getTipo().toString().equals(tipo)) {
                respuestasFiltradasPorTipo.add(r);
            }
            nodo = nodo.getSiguiente();
        }
        
        return respuestasFiltradasPorTipo;
    }
    
    public java.util.List<Respuesta> obtenerRespuestasFiltradasPorEncuesta(int idEncuesta) {
        java.util.List<Respuesta> respuestasFiltradasPorEncuesta = new java.util.ArrayList<>();
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.getIdEncuesta() == idEncuesta) {
                respuestasFiltradasPorEncuesta.add(r);
            }
            nodo = nodo.getSiguiente();
        }
        
        return respuestasFiltradasPorEncuesta;
    }
    
    public java.util.List<Respuesta> obtenerRespuestasValidas() {
        java.util.List<Respuesta> respuestasValidas = new java.util.ArrayList<>();
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.isEsValida()) {
                respuestasValidas.add(r);
            }
            nodo = nodo.getSiguiente();
        }
        
        return respuestasValidas;
    }
    
    public int contarRespuestasParticipante(int idParticipante) {
        int contador = 0;
        NodoRespuesta nodo = listaRespuestas.getCabeza();
        
        while (nodo != null) {
            if (nodo.getRespuesta().getIdParticipante() == idParticipante) {
                contador++;
            }
            nodo = nodo.getSiguiente();
        }
        
        return contador;
    }
}

