package modulo.perfiles;

import conexionSQL.ConexionSQL;
import java.sql.*;
import java.util.*;

public class UsuarioExternoDAO {

    public boolean crearUsuarioExterno(String nombreUsuario, String nombrePerfil, String username, String password) {
        String sqlPerfil = "SELECT id_perfil FROM Perfiles WHERE LOWER(nombre) = LOWER(?)";
        String sqlInsertarUsuario = "INSERT INTO Usuarios (nombre, nombre_usuario, contrasena, id_perfil, estado, fecha_creacion) "
                +
                "VALUES (?, ?, ?, ?, 'ACTIVO', GETDATE())";

        try (Connection conn = ConexionSQL.getConnection()) {
            PreparedStatement pstmtPerfil = conn.prepareStatement(sqlPerfil);
            pstmtPerfil.setString(1, nombrePerfil);
            ResultSet rsPerfil = pstmtPerfil.executeQuery();

            if (!rsPerfil.next()) {
                rsPerfil.close();
                pstmtPerfil.close();
                System.out.println("Perfil no encontrado: " + nombrePerfil);
                return false;
            }
            int idPerfil = rsPerfil.getInt("id_perfil");
            rsPerfil.close();
            pstmtPerfil.close();

            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertarUsuario);
            pstmtInsert.setString(1, nombreUsuario);
            pstmtInsert.setString(2, username);
            pstmtInsert.setString(3, password);
            pstmtInsert.setInt(4, idPerfil);

            int resultado = pstmtInsert.executeUpdate();
            pstmtInsert.close();

            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear usuario externo: " + e.getMessage());
            return false;
        }
    }

    public List<String> listarUsuariosExternos() {
        List<String> usuarios = new ArrayList<>();
        String sql = "SELECT nombre, nombre_usuario FROM Usuarios WHERE estado = 'ACTIVO' ORDER BY nombre";

        try (Connection conn = ConexionSQL.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String nombreUsuario = rs.getString("nombre_usuario");
                usuarios.add(nombre + " (" + nombreUsuario + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios externos: " + e.getMessage());
        }

        return usuarios;
    }

    public int obtenerIdPerfilDelUsuario(String nombreUsuarioDisplay) {
        String nombreUsuario = extraerNombreUsuario(nombreUsuarioDisplay);
        String sql = "SELECT id_perfil FROM Usuarios WHERE nombre_usuario = ? AND estado = 'ACTIVO'";

        try (Connection conn = ConexionSQL.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int idPerfil = rs.getInt("id_perfil");
                rs.close();
                return idPerfil;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener id perfil del usuario: " + e.getMessage());
        }

        return -1;
    }

    public String obtenerNombrePerfilDelUsuario(String nombreUsuarioDisplay) {
        String nombreUsuario = extraerNombreUsuario(nombreUsuarioDisplay);
        String sql = "SELECT p.nombre FROM Usuarios u " +
                "JOIN Perfiles p ON u.id_perfil = p.id_perfil " +
                "WHERE u.nombre_usuario = ? AND u.estado = 'ACTIVO'";

        try (Connection conn = ConexionSQL.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nombrePerfil = rs.getString("nombre");
                rs.close();
                return nombrePerfil;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener nombre perfil del usuario: " + e.getMessage());
        }

        return null;
    }

    private String extraerNombreUsuario(String nombreUsuarioDisplay) {
        if (nombreUsuarioDisplay.contains("(") && nombreUsuarioDisplay.contains(")")) {
            return nombreUsuarioDisplay.substring(nombreUsuarioDisplay.indexOf("(") + 1,
                    nombreUsuarioDisplay.indexOf(")"));
        }
        return nombreUsuarioDisplay;
    }

    public List<String> listarPermisosDisponibles() {
        List<String> permisos = new ArrayList<>();
        String sql = "SELECT nombre FROM Permisos WHERE estado = 'ACTIVO' ORDER BY nombre";

        try (Connection conn = ConexionSQL.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                permisos.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar permisos disponibles: " + e.getMessage());
        }

        return permisos;
    }
}
