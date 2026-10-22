package modulo.preguntas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import conexionSQL.ConexionSQL;

public class PreguntaDAO {

    private static PreguntaDAO instancia;
    private final ListaPregunta listaPreguntas = new ListaPregunta();
    private int contadorId = 1;

    public PreguntaDAO() {
        
        cargarPreguntasDesdeDB();
    }

    public static PreguntaDAO getInstancia() {
        if (instancia == null) {
            instancia = new PreguntaDAO();
        }
        return instancia;
    }

    public static List<Pregunta> consultarPreguntasPorEncuesta(int idEncuesta) throws SQLException {
        List<Pregunta> preguntas = new ArrayList<>();
        String consulta = "SELECT id_pregunta, id_encuesta, id_categoria, texto, tipo, opciones FROM Preguntas "
                + "WHERE id_encuesta = ? AND estado = 'ACTIVO' ORDER BY id_pregunta";

        try (Connection conexion = ConexionSQL.getConnection()) {
            if (conexion == null) {
                throw new SQLException("No se pudo conectar con SQL Server.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                sentencia.setInt(1, idEncuesta);
                try (ResultSet resultados = sentencia.executeQuery()) {
                    while (resultados.next()) {
                        String nombreTipo = resultados.getString("tipo");
                        TipoPregunta tipo;
                        try {
                            tipo = TipoPregunta.valueOf(nombreTipo == null ? "" : nombreTipo.trim());
                        } catch (IllegalArgumentException excepcion) {
                            throw new SQLException("Tipo de pregunta no reconocido: " + nombreTipo, excepcion);
                        }
                        Integer idCategoria = resultados.getObject("id_categoria") == null
                                ? null : resultados.getInt("id_categoria");
                        Pregunta pregunta = new Pregunta(resultados.getInt("id_pregunta"),
                                resultados.getInt("id_encuesta"), idCategoria,
                                resultados.getString("texto"), tipo, false);
                        String opciones = resultados.getString("opciones");
                        if (opciones == null || opciones.isBlank()) {
                            pregunta.generarOpcionesSegunTipo();
                        } else {
                            pregunta.setOpciones(opciones.split("\\|"));
                        }
                        preguntas.add(pregunta);
                    }
                }
            }
        }
        return preguntas;
    }

public void agregarPregunta(Pregunta pregunta, int idEncuesta) {
    
    if (pregunta == null) {
        System.out.println("Error: la pregunta no puede ser nula.");
        return;
    }
    if (pregunta.getTexto() == null || pregunta.getTexto().trim().isEmpty()) {
        System.out.println("Error: el texto de la pregunta no puede estar vacio.");
        return;
    }
    if (pregunta.getTipo() == null) {
        System.out.println("Error: el tipo de la pregunta es obligatorio.");
        return;
    }
    if (pregunta.getIdCategoria() == null || pregunta.getIdCategoria() <= 0) {
        System.out.println("Error: la categoria es obligatoria.");
        return;
    }

    Pregunta nueva = new Pregunta(
        contadorId++,
        idEncuesta,
        pregunta.getIdCategoria(),
        pregunta.getTexto(),
        pregunta.getTipo(),
        pregunta.isFiltro()
    );

    nueva.setIdEncuesta(idEncuesta);
   
    nueva.generarOpcionesSegunTipo();

    listaPreguntas.agregar(nueva);
   
    guardarEnBD(nueva);

    System.out.println("Pregunta registrada con ID: " + nueva.getIdPregunta()
        + " en Encuesta ID: " + idEncuesta);
}


    public void editarPregunta(Pregunta preguntaEditada) {
        NodoPregunta actual = listaPreguntas.getCabeza();
        while (actual != null) {
            if (actual.getPregunta().getIdPregunta() == preguntaEditada.getIdPregunta()) {
                actual.setPregunta(preguntaEditada);
                System.out.println("Pregunta ID "
                        + preguntaEditada.getIdPregunta() + " actualizada.");
                return;
            }
            actual = actual.getSiguiente();
        }
        System.out.println("Pregunta no encontrada.");
    }

    public void eliminarPregunta(int id) {
        listaPreguntas.eliminar(id);
        System.out.println("Pregunta ID " + id + " eliminada (si existia).");
    }

    public Pregunta buscarPorId(int id) {
        return listaPreguntas.buscarPorId(id);
    }

    public ListaPregunta obtenerPorTipo(String tipo) {
        ListaPregunta resultado = new ListaPregunta();
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            Pregunta p = actual.getPregunta();
            if (p.getTipo().name().equalsIgnoreCase(tipo)) {
                resultado.agregar(p);
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public ListaPregunta obtenerPorCategoria(String categoria) {
        ListaPregunta resultado = new ListaPregunta();
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            Pregunta p = actual.getPregunta();
            if (p.getCategoria() != null && p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.agregar(p);
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public void marcarComoFiltro(int id) {
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            Pregunta p = actual.getPregunta();
            if (p.getIdPregunta() == id) {
                p.setFiltro(true);
                System.out.println("Pregunta ID " + id
                        + " marcada como filtro.");
                return;
            }
            actual = actual.getSiguiente();
        }
        System.out.println("Pregunta no encontrada para marcar como filtro.");
    }

    public ListaPregunta obtenerPreguntasActivas() {
        ListaPregunta activas = new ListaPregunta();
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            Pregunta p = actual.getPregunta();
            if (p.isActiva()) {
                activas.agregar(p);
            }
            actual = actual.getSiguiente();
        }
        return activas;
    }

    public ListaPregunta obtenerTodas() {
        ListaPregunta copia = new ListaPregunta();
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            copia.agregar(actual.getPregunta());
            actual = actual.getSiguiente();
        }

        return copia;
    }

    public ListaPregunta obtenerPreguntasPorEncuesta(int idEncuesta) {
        ListaPregunta resultado = new ListaPregunta();
        NodoPregunta actual = listaPreguntas.getCabeza();

        while (actual != null) {
            Pregunta p = actual.getPregunta();
            if (p.getIdEncuesta() == idEncuesta && p.isActiva()) {
                resultado.agregar(p);
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public void mostrarPreguntas() {
        listaPreguntas.mostrar();
    }

    public ListaPregunta getListaPregunta() {
        return listaPreguntas;
    }
   
    public void cargarPreguntasDesdeDB() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexion a BD. Usando datos en memoria.");
                return;
            }
            
            String sql = "SELECT id_pregunta, id_encuesta, id_categoria, texto, tipo, opciones FROM Preguntas WHERE estado = 'ACTIVO'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_pregunta");
                int idEncuesta = rs.getInt("id_encuesta");
                Object idCategoriaObj = rs.getObject("id_categoria");
                Integer idCategoria = idCategoriaObj != null ? rs.getInt("id_categoria") : null;
                String texto = rs.getString("texto");
                String tipo = rs.getString("tipo");
                String opcionesStr = rs.getString("opciones");
                
                Pregunta p = new Pregunta(id, idEncuesta, idCategoria, texto, TipoPregunta.valueOf(tipo), false);
              
                if (opcionesStr != null && !opcionesStr.isEmpty()) {
                    String[] opciones = opcionesStr.split("\\|");
                    p.setOpciones(opciones);
                } else {
                    p.generarOpcionesSegunTipo();
                }
                
                listaPreguntas.agregar(p);
                if (id >= contadorId) {
                    contadorId = id + 1;
                }
            }
            
            System.out.println("Preguntas cargadas desde la base de datos.");
            
        } catch (SQLException e) {
            System.out.println("Error al cargar preguntas desde BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
    
  
    private void guardarEnBD(Pregunta pregunta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexion a BD. Pregunta guardada solo en memoria.");
                return;
            }
           
            String opcionesStr = "";
            if (pregunta.getOpciones() != null && pregunta.getOpciones().length > 0) {
                opcionesStr = String.join("|", pregunta.getOpciones());
            }
            
            String sql = "INSERT INTO Preguntas (id_encuesta, id_categoria, texto, tipo, opciones, estado) " +
                        "VALUES (?, ?, ?, ?, ?, 'ACTIVO')";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pregunta.getIdEncuesta());
            pstmt.setInt(2, pregunta.getIdCategoria());
            pstmt.setString(3, pregunta.getTexto());
            pstmt.setString(4, pregunta.getTipo().name());
            pstmt.setString(5, opcionesStr);
            
            System.out.println("[DEBUG] Ejecutando INSERT: id_encuesta=" + pregunta.getIdEncuesta() + 
                             ", texto=" + pregunta.getTexto() + 
                             ", tipo=" + pregunta.getTipo().name());
            
            int filasInsertadas = pstmt.executeUpdate();
            System.out.println("[DEBUG] Filas insertadas: " + filasInsertadas);
            
            if (filasInsertadas > 0) {
                System.out.println("Pregunta guardada en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar pregunta en BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
