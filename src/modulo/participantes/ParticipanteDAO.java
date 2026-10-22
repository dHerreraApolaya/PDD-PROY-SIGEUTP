package modulo.participantes;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexionSQL.ConexionSQL;

public class ParticipanteDAO {
    private ListaParticipante listaParticipantes;
    private int contadorId = 1;

    public ParticipanteDAO() {
        this.listaParticipantes = new ListaParticipante();
    }

    public Participante consultarParticipanteParaEncuesta(String dni) throws SQLException {
        String consulta = "SELECT id_participante, nombre, dni, correo FROM Participantes WHERE dni = ?";
        try (Connection conexion = ConexionSQL.getConnection()) {
            if (conexion == null) {
                throw new SQLException("No se pudo conectar con SQL Server.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                sentencia.setString(1, dni);
                try (ResultSet resultados = sentencia.executeQuery()) {
                    if (resultados.next()) {
                        Participante participante = new Participante(resultados.getString("dni"),
                                resultados.getString("nombre"), resultados.getString("correo"));
                        participante.setId_participante(resultados.getInt("id_participante"));
                        return participante;
                    }
                }
            }
        }
        return null;
    }

    public void registrarParticipante(Participante participante) {
        if (listaParticipantes.contiene(participante)) {
            System.out.println("No se puede registrar: El participante "
                    + "ya existe (DNI o correo duplicado).");
            return;
        }

        participante.setId_participante(contadorId++);
        participante.setIdperfil(1);
        listaParticipantes.agregar(participante);
        
        guardarEnBD(participante);

        System.out.println("Participante registrado correctamente: "
                + participante.getNombre());
    }
    
    private void guardarEnBD(Participante participante) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("Error: No se pudo conectar a la base de datos.");
                return;
            }
            
            String sql = "INSERT INTO Participantes " +
                    "(nombre, dni, correo, id_perfil, edad, sexo, sueldo, ubicacion, " +
                    "nivel_educativo, estado_civil, telefono, departamento, grupoEdad) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, participante.getNombre());
            pstmt.setString(2, participante.getDni());
            pstmt.setString(3, participante.getCorreo());
            pstmt.setInt(4, participante.getIdperfil());
            pstmt.setInt(5, participante.getEdad());
            pstmt.setString(6, participante.getSexo());
            pstmt.setDouble(7, participante.getSueldo());
            pstmt.setString(8, participante.getUbicacion()); // ubicacion
            pstmt.setString(9, participante.getNivelEducativo());
            pstmt.setString(10, participante.getEstadoCivil());
            pstmt.setString(11, participante.getTelefono());
            pstmt.setString(12, participante.getDepartamento());
            pstmt.setString(13, participante.getGrupoEdad());
            
            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println(" Participante guardado en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar en BD: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }

    public boolean existeDocumento(String dni) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
      
                NodoParticipante actual = listaParticipantes.getCabeza();
                while (actual != null) {
                    if (actual.getParticipante().getDni().equalsIgnoreCase(dni)) {
                        return true;
                    }
                    actual = actual.getSiguiente();
                }
                return false;
            }
            
            String sql = "SELECT COUNT(*) as count FROM Participantes WHERE dni = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dni);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar DNI en BD: " + e.getMessage());
           
            NodoParticipante actual = listaParticipantes.getCabeza();
            while (actual != null) {
                if (actual.getParticipante().getDni().equalsIgnoreCase(dni)) {
                    return true;
                }
                actual = actual.getSiguiente();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    public void mostrarParticipantes() {
        listaParticipantes.mostrar();
    }

    public int obtenerIdPorDNI(String dni) {
        NodoParticipante actual = listaParticipantes.getCabeza();
        while (actual != null) {
            if (actual.getParticipante().getDni().equalsIgnoreCase(dni)) {
                return actual.getParticipante().getId_participante();
            }
            actual = actual.getSiguiente();
        }
        System.out.println("Participante no encontrado con DNI: " + dni);
        return -1;
    }

    public int contarParticipantes() {
        return listaParticipantes.contar();
    }
    
    public ListaParticipante getListaParticipantes() {
        return listaParticipantes;
    }

    public List<Participante> listarParticipantes() {
        List<Participante> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("Error: No se pudo conectar a la base de datos.");
                return lista;
            }
            
            String sql = "SELECT id_participante, nombre, dni, correo, id_perfil, edad, sexo, nivel_educativo, grupoEdad FROM Participantes";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Participante participante = new Participante(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("correo")
                );
                participante.setId_participante(rs.getInt("id_participante"));
                participante.setIdperfil(rs.getInt("id_perfil"));
                participante.setEdad(rs.getInt("edad"));
                participante.setSexo(rs.getString("sexo"));
                participante.setNivelEducativo(rs.getString("nivel_educativo"));
                participante.setGrupoEdad(rs.getString("grupoEdad"));
                lista.add(participante);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar BD: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return lista;
    }
   
    public Participante obtenerParticipantePorId(int idParticipante) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("Error: No se pudo conectar a la base de datos.");
                return null;
            }
            
            String sql = "SELECT id_participante, nombre, dni, correo, id_perfil, edad, sexo, nivel_educativo, grupoEdad FROM Participantes WHERE id_participante = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idParticipante);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Participante participante = new Participante(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("correo")
                );
                participante.setId_participante(rs.getInt("id_participante"));
                participante.setIdperfil(rs.getInt("id_perfil"));
                participante.setEdad(rs.getInt("edad"));
                participante.setSexo(rs.getString("sexo"));
                participante.setNivelEducativo(rs.getString("nivel_educativo"));
                participante.setGrupoEdad(rs.getString("grupoEdad"));
                return participante;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener participante: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return null;
    }
   
    public Participante obtenerParticipantePorDNI(String dni) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("Error: No se pudo conectar a la base de datos.");
                return null;
            }
            
            String sql = "SELECT id_participante, nombre, dni, correo, id_perfil, edad, sexo, nivel_educativo, grupoEdad FROM Participantes WHERE dni = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dni);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Participante participante = new Participante(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("correo")
                );
                participante.setId_participante(rs.getInt("id_participante"));
                participante.setIdperfil(rs.getInt("id_perfil"));
                participante.setEdad(rs.getInt("edad"));
                participante.setSexo(rs.getString("sexo"));
                participante.setNivelEducativo(rs.getString("nivel_educativo"));
                participante.setGrupoEdad(rs.getString("grupoEdad"));
                return participante;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener participante por DNI: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return null;
    }
   
    public boolean eliminarParticipante(String dni) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionSQL.getConnection();
            if (conn == null) {
                System.out.println("Error: No se pudo conectar a la base de datos.");
                return false;
            }
            
            // Primero obtener el id_participante
            String sqlObtenerID = "SELECT id_participante FROM Participantes WHERE dni = ?";
            pstmt = conn.prepareStatement(sqlObtenerID);
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("No se encontro participante con ese DNI.");
                return false;
            }
            
            int idParticipante = rs.getInt("id_participante");
            
            // Eliminar respuestas asociadas
            String sqlEliminarRespuestas = "DELETE FROM Respuestas WHERE id_participante = ?";
            pstmt = conn.prepareStatement(sqlEliminarRespuestas);
            pstmt.setInt(1, idParticipante);
            pstmt.executeUpdate();
            
            // Luego eliminar el participante
            String sqlEliminarParticipante = "DELETE FROM Participantes WHERE dni = ?";
            pstmt = conn.prepareStatement(sqlEliminarParticipante);
            pstmt.setString(1, dni);
            
            int filasEliminadas = pstmt.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Participante eliminado correctamente.");
                return true;
            } else {
                System.out.println("No se encontro participante con ese DNI.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar participante: " + e.getMessage());
            return false;
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
