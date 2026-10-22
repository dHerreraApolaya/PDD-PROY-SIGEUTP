package modulo.encuestas.configuracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexionSQL.ConexionSQL;

public class EncuestaDAO {

    private static EncuestaDAO instancia;
    private ListaEncuesta listaEncuestas;
    private int contadorId = 1;

    public EncuestaDAO() {
        this.listaEncuestas = new ListaEncuesta();
  
        cargarEncuestasDesdeDB();
    }

    public static EncuestaDAO getInstancia() {
        if (instancia == null) {
            instancia = new EncuestaDAO();
        }
        return instancia;
    }

    public static List<Encuesta> consultarEncuestasDisponibles() throws SQLException {
        List<Encuesta> encuestas = new ArrayList<>();
        String consulta = "SELECT id_encuesta, titulo, descripcion, estado FROM Encuestas "
                + "WHERE estado = 'ACTIVA' ORDER BY id_encuesta";

        try (Connection conexion = ConexionSQL.getConnection()) {
            if (conexion == null) {
                throw new SQLException("No se pudo conectar con SQL Server.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
                    ResultSet resultados = sentencia.executeQuery()) {
                while (resultados.next()) {
                    Encuesta encuesta = new Encuesta(resultados.getString("titulo"),
                            resultados.getString("descripcion"), resultados.getString("estado"), "Administrador");
                    encuesta.setIdEncuesta(resultados.getInt("id_encuesta"));
                    encuestas.add(encuesta);
                }
            }
        }
        return encuestas;
    }

    public ListaEncuesta getListaEncuestas() {
        return listaEncuestas;
    }
    
    public void registrarEncuesta(Encuesta encuesta) {
        encuesta.setIdEncuesta(contadorId++);
        if (encuesta.getFechaCreacion() == null) {
            encuesta.setFechaCreacion(LocalDate.now());
        }
        listaEncuestas.agregar(encuesta);
        
        guardarEnBD(encuesta);
        
        System.out.println("Encuesta registrada: " + encuesta.getTitulo() 
                + " (ID: " + encuesta.getIdEncuesta() + ")");
    }
    
    public void editarEncuesta(Encuesta encuestaEditada) {
        Encuesta existente = listaEncuestas.buscarPorId(encuestaEditada.getIdEncuesta());
        if (existente != null) {
            existente.setTitulo(encuestaEditada.getTitulo());
            existente.setDescripcion(encuestaEditada.getDescripcion());
            existente.setEstado(encuestaEditada.getEstado());
            existente.setPerfilCreador(encuestaEditada.getPerfilCreador());
            existente.setFechaCreacion(encuestaEditada.getFechaCreacion());
            System.out.println("Encuesta ID " + existente.getIdEncuesta() + " actualizada.");
        } else {
            System.out.println("Encuesta no encontrada con ID: " + encuestaEditada.getIdEncuesta());
        }
    }
    
    public void eliminarEncuesta(int idEncuesta) {
        boolean eliminado = listaEncuestas.eliminarPorId(idEncuesta);
        if (eliminado) {
            System.out.println("Encuesta ID " + idEncuesta + " eliminada.");
        } else {
            System.out.println("Encuesta no encontrada con ID: " + idEncuesta);
        }
    }
    
    public void listarEncuestas() {
        listaEncuestas.listar();
    }
    
    public void listarEncuestasPorFecha(String orden) {
        System.out.println("\n" + (orden.equals("DESC") ? " ORDENADO DE MAS RECIENTE A MAS ANTIGUA " :
                " ORDENADO DE MAS ANTIGUA A MAS RECIENTE "));
        System.out.println("================================================================?");
        
        NodoEncuesta actual = listaEncuestas.getCabeza();
        List<Encuesta> encuestas = new ArrayList<>();
        
        while (actual != null) {
            encuestas.add(actual.getEncuesta());
            actual = actual.getSiguiente();
        }
        if (orden.equals("DESC")) {
        
            encuestas.sort((e1, e2) -> {
                if (e1.getFechaCreacion() == null || e2.getFechaCreacion() == null) return 0;
                return e2.getFechaCreacion().compareTo(e1.getFechaCreacion());
            });
        } else {
            encuestas.sort((e1, e2) -> {
                if (e1.getFechaCreacion() == null || e2.getFechaCreacion() == null) return 0;
                return e1.getFechaCreacion().compareTo(e2.getFechaCreacion());
            });
        }
        
        if (encuestas.isEmpty()) {
            System.out.println("No hay encuestas registradas.");
        } else {
            int contador = 1;
            for (Encuesta e : encuestas) {
                System.out.printf("%2d. [ID: %3d] %s%n", contador, e.getIdEncuesta(), e.getTitulo());
                System.out.printf("    Descripcion: %s%n", e.getDescripcion());
                System.out.printf("    Estado: %s | Fecha: %s%n", e.getEstado(), e.getFechaCreacion());
                System.out.println("===============================================================");
                contador++;
            }
        }
    }
    
    public ListaEncuesta filtrarEncuestas(String estado, String perfil, LocalDate desde, LocalDate hasta) {
        ListaEncuesta filtradas = new ListaEncuesta();
        NodoEncuesta actual = listaEncuestas.getCabeza();
        while (actual != null) {
            Encuesta e = actual.getEncuesta();
            boolean coincideEstado = e.getEstado() != null && e.getEstado().equalsIgnoreCase(estado);
            boolean coincidePerfil = e.getPerfilCreador() != null && e.getPerfilCreador().equalsIgnoreCase(perfil);
            boolean dentroRango = e.getFechaCreacion() != null &&
                    ( !e.getFechaCreacion().isBefore(desde) && !e.getFechaCreacion().isAfter(hasta) );

            if (coincideEstado && coincidePerfil && dentroRango) {
                filtradas.agregar(e);
            }
            actual = actual.getSiguiente();
        }
        return filtradas;
    }
    
    public Encuesta buscarPorId(int idEncuesta) {
        return listaEncuestas.buscarPorId(idEncuesta);
    }
    
    public int contarEncuestas() {
        int count = 0;
        NodoEncuesta actual = listaEncuestas.getCabeza();
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        return count;
    }
    
    public List<Encuesta> listarEncuestasActivas() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Encuesta> encuestasActivas = new ArrayList<>();
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexion a BD.");
                return encuestasActivas;
            }
            
            String sql = "SELECT id_encuesta, titulo, descripcion, estado, fecha_creacion, " +
                        "edad_minima, edad_maxima, sexo_permitido " +
                        "FROM Encuestas WHERE estado = 'ACTIVA' ORDER BY id_encuesta";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_encuesta");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String estado = rs.getString("estado");
                
                Encuesta e = new Encuesta(titulo, descripcion, estado, "Administrador");
                e.setIdEncuesta(id);
                
                Integer edadMinima = (Integer) rs.getObject("edad_minima");
                Integer edadMaxima = (Integer) rs.getObject("edad_maxima");
                String sexoPermitido = rs.getString("sexo_permitido");
                
                if (edadMinima != null || edadMaxima != null || sexoPermitido != null) {
                    PerfilParticipante perfil = new PerfilParticipante(
                        edadMinima, 
                        edadMaxima, 
                        null, 
                        sexoPermitido, 
                        null, 
                        null, 
                        null   
                    );
                    e.setPerfilParticipante(perfil);
                }
                
                encuestasActivas.add(e);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar encuestas activas: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        
        return encuestasActivas;
    }
    
      public void cargarEncuestasDesdeDB() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexion a BD. Usando datos en memoria.");
                return;
            }
            
            String sql = "SELECT id_encuesta, titulo, descripcion, estado, fecha_creacion, " +
                        "edad_minima, edad_maxima, grupo_edad_permitido, sexo_permitido, estado_civil_permitido " +
                        "FROM Encuestas";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_encuesta");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String estado = rs.getString("estado");
                java.sql.Date fechaSQL = rs.getDate("fecha_creacion");
                
                LocalDate fechaCreacion = (fechaSQL != null) ? fechaSQL.toLocalDate() : LocalDate.now();
             
                Encuesta e = new Encuesta(titulo, descripcion, estado, "Administrador");
                e.setIdEncuesta(id);
                e.setFechaCreacion(fechaCreacion);
           
                Integer edadMinima = (Integer) rs.getObject("edad_minima");
                Integer edadMaxima = (Integer) rs.getObject("edad_maxima");
                String grupoEdad = rs.getString("grupo_edad_permitido");
                String sexoPermitido = rs.getString("sexo_permitido");
                String estadoCivil = rs.getString("estado_civil_permitido");
                
                if (edadMinima != null || edadMaxima != null || grupoEdad != null || 
                    sexoPermitido != null || estadoCivil != null) {
                    PerfilParticipante perfil = new PerfilParticipante(
                        edadMinima, 
                        edadMaxima, 
                        grupoEdad, 
                        sexoPermitido, 
                        estadoCivil, 
                        null, 
                        null   
                    );
                    e.setPerfilParticipante(perfil);
                }
                
              
                listaEncuestas.agregar(e);
                
                              if (id >= contadorId) {
                    contadorId = id + 1;
                }
            }
            
            System.out.println("Encuestas cargadas desde la base de datos.");
            
        } catch (SQLException e) {
            System.out.println("Error al cargar encuestas desde BD: " + e.getMessage());
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

    private void guardarEnBD(Encuesta encuesta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("No hay conexion a BD. Encuesta guardada solo en memoria.");
                return;
            }
            
            String sql = "INSERT INTO Encuestas " +
                    "(titulo, descripcion, estado, id_creador, edad_minima, edad_maxima, " +
                    "grupo_edad_permitido, sexo_permitido, estado_civil_permitido, " +
                    "fecha_creacion, cantidad_preguntas, numero_participantes, fecha_actualizacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, ?, GETDATE())";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, encuesta.getTitulo());
            pstmt.setString(2, encuesta.getDescripcion());
            pstmt.setString(3, encuesta.getEstado());
            pstmt.setNull(4, java.sql.Types.INTEGER);  
            
            PerfilParticipante perfil = encuesta.getPerfilParticipante();
            if (perfil != null) {
                pstmt.setObject(5, perfil.getEdadMinima());
                pstmt.setObject(6, perfil.getEdadMaxima());
                pstmt.setString(7, perfil.getGruposEdadPermitidos());
                pstmt.setString(8, perfil.getSexoPermitido());
                pstmt.setString(9, perfil.getEstadoCivilPermitido());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
                pstmt.setNull(6, java.sql.Types.INTEGER);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.VARCHAR);
                pstmt.setNull(9, java.sql.Types.VARCHAR);
            }
            
            pstmt.setInt(10, 0);  
            pstmt.setInt(11, 0);  
            
            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println(" Encuesta guardada completamente en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar encuesta en BD: " + e.getMessage());
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
