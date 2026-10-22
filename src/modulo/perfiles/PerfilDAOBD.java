package modulo.perfiles;

import conexionSQL.ConexionSQL;
import java.sql.*;
import java.util.*;

public class PerfilDAOBD {

    public List<Perfil> cargarTodosPerfiles() {
        List<Perfil> perfiles = new ArrayList<>();
        String sql = "SELECT id_perfil, nombre FROM Perfiles WHERE estado = 'ACTIVO' ORDER BY id_perfil";
        
        try (Connection conn = ConexionSQL.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idPerfil = rs.getInt("id_perfil");
                String nombre = rs.getString("nombre");
                
                Perfil perfil = new Perfil(nombre);
                cargarPermisosDelPerfil(perfil, idPerfil);
                perfiles.add(perfil);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar perfiles desde BD: " + e.getMessage());
        }
        
        return perfiles;
    }

    private void cargarPermisosDelPerfil(Perfil perfil, int idPerfil) {
        String sql = "SELECT pm.nombre FROM Perfil_Permiso pp " +
                     "JOIN Permisos pm ON pp.id_permiso = pm.id_permiso " +
                     "WHERE pp.id_perfil = ? AND pm.estado = 'ACTIVO'";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPerfil);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String permiso = rs.getString("nombre");
                perfil.agregarPermiso(permiso);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al cargar permisos del perfil: " + e.getMessage());
        }
    }

    public Perfil obtenerPerfilPorNombre(String nombre) {
        String sql = "SELECT id_perfil, nombre FROM Perfiles " +
                     "WHERE LOWER(nombre) = LOWER(?) AND estado = 'ACTIVO'";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int idPerfil = rs.getInt("id_perfil");
                String nombrePerfil = rs.getString("nombre");
                
                Perfil perfil = new Perfil(nombrePerfil);
                cargarPermisosDelPerfil(perfil, idPerfil);
                rs.close();
                return perfil;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener perfil: " + e.getMessage());
        }
        
        return null;
    }

    public boolean crearPerfilEnBD(String nombrePerfil, String descripcion) {
        String sql = "INSERT INTO Perfiles (nombre, descripcion, estado, fecha_creacion) " +
                     "VALUES (?, ?, 'ACTIVO', GETDATE())";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombrePerfil);
            pstmt.setString(2, descripcion != null ? descripcion : "Perfil personalizado de usuario externo");
            
            int resultado = pstmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("Violation of PRIMARY KEY constraint") || 
                e.getMessage().contains("UNIQUE")) {
                return true; 
            }
            System.out.println("Error al crear perfil en BD: " + e.getMessage());
        }
        
        return false;
    }

    public boolean asignarPermisoAPerfil(String nombrePerfil, String nombrePermiso) {
        String sql = "{CALL sp_AsignarPermisoAUsuarioExterno(?, ?)}";
        
        try (Connection conn = ConexionSQL.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, nombrePerfil);
            cstmt.setString(2, nombrePermiso);
            
            cstmt.execute();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error al asignar permiso: " + e.getMessage());
            return false;
        }
    }

    public boolean removerPermisoDelPerfil(String nombrePerfil, String nombrePermiso) {
        String sqlPerfil = "SELECT id_perfil FROM Perfiles WHERE LOWER(nombre) = LOWER(?)";
        String sqlPermiso = "SELECT id_permiso FROM Permisos WHERE nombre = ?";
        String sqlRemover = "DELETE FROM Perfil_Permiso WHERE id_perfil = ? AND id_permiso = ?";
        
        try (Connection conn = ConexionSQL.getConnection()) {
            PreparedStatement pstmtPerfil = conn.prepareStatement(sqlPerfil);
            pstmtPerfil.setString(1, nombrePerfil);
            ResultSet rsPerfil = pstmtPerfil.executeQuery();
            
            if (!rsPerfil.next()) {
                rsPerfil.close();
                pstmtPerfil.close();
                return false;
            }
            int idPerfil = rsPerfil.getInt("id_perfil");
            rsPerfil.close();
            pstmtPerfil.close();
            
            PreparedStatement pstmtPermiso = conn.prepareStatement(sqlPermiso);
            pstmtPermiso.setString(1, nombrePermiso);
            ResultSet rsPermiso = pstmtPermiso.executeQuery();
            
            if (!rsPermiso.next()) {
                rsPermiso.close();
                pstmtPermiso.close();
                return false;
            }
            int idPermiso = rsPermiso.getInt("id_permiso");
            rsPermiso.close();
            pstmtPermiso.close();
            
            PreparedStatement pstmtRemover = conn.prepareStatement(sqlRemover);
            pstmtRemover.setInt(1, idPerfil);
            pstmtRemover.setInt(2, idPermiso);
            pstmtRemover.executeUpdate();
            pstmtRemover.close();
            
            return true;
        } catch (SQLException e) {
            System.out.println("Error al remover permiso: " + e.getMessage());
            return false;
        }
    }

    public List<String> listarPermisosDelPerfil(String nombrePerfil) {
        List<String> permisos = new ArrayList<>();
        String sql = "SELECT pm.nombre FROM Perfil_Permiso pp " +
                     "JOIN Permisos pm ON pp.id_permiso = pm.id_permiso " +
                     "JOIN Perfiles p ON pp.id_perfil = p.id_perfil " +
                     "WHERE LOWER(p.nombre) = LOWER(?) AND pm.estado = 'ACTIVO'";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombrePerfil);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                permisos.add(rs.getString("nombre"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al listar permisos: " + e.getMessage());
        }
        
        return permisos;
    }
}
