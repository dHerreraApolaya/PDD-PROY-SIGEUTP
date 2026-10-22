package modulo.preguntas;

import conexionSQL.ConexionSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private static CategoriaDAO instancia;

    private CategoriaDAO() {
    }

    public static CategoriaDAO getInstancia() {
        if (instancia == null) {
            instancia = new CategoriaDAO();
        }
        return instancia;
    }

    public List<Categoria> obtenerCategoriasRaiz() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, id_padre, nivel, estado, fecha_creacion " +
                     "FROM Categorias WHERE id_padre IS NULL ORDER BY id_categoria";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categori­as rai­z: " + e.getMessage());
        }
        
        return categorias;
    }

    public List<Categoria> obtenerSubcategorias(int idPadre) {
        List<Categoria> subcategorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, id_padre, nivel, estado, fecha_creacion " +
                     "FROM Categorias WHERE id_padre = ? ORDER BY id_categoria";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPadre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subcategorias.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener subcategori­as: " + e.getMessage());
        }
        
        return subcategorias;
    }

    public Categoria obtenerPorId(int idCategoria) {
        String sql = "SELECT id_categoria, nombre, descripcion, id_padre, nivel, estado, fecha_creacion " +
                     "FROM Categorias WHERE id_categoria = ?";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categori­a: " + e.getMessage());
        }
        
        return null;
    }

  
    public Categoria obtenerPorNombre(String nombre) {
        String sql = "SELECT id_categoria, nombre, descripcion, id_padre, nivel, estado, fecha_creacion " +
                     "FROM Categorias WHERE nombre = ? AND estado = 'ACTIVO'";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categori­a por nombre: " + e.getMessage());
        }
        
        return null;
    }

    public int crearCategoriaRaiz(String nombre, String descripcion) {
        String sql = "INSERT INTO Categorias (nombre, descripcion, id_padre, nivel, estado, fecha_creacion) " +
                     "VALUES (?, ?, NULL, 0, 'ACTIVO', GETDATE())";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear categori­a rai­z: " + e.getMessage());
        }
        
        return -1;
    }

    public int crearSubcategoria(String nombre, String descripcion, int idPadre) {
  
        Categoria padre = obtenerPorId(idPadre);
        if (padre == null) {
            System.out.println("Error: Categori­a padre no existe.");
            return -1;
        }
        
        int nuevoNivel = padre.getNivel() + 1;
        String sql = "INSERT INTO Categorias (nombre, descripcion, id_padre, nivel, estado, fecha_creacion) " +
                     "VALUES (?, ?, ?, ?, 'ACTIVO', GETDATE())";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, idPadre);
            ps.setInt(4, nuevoNivel);
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear subcategori­a: " + e.getMessage());
        }
        
        return -1;
    }

    public void actualizar(Categoria categoria) {
        String sql = "UPDATE Categorias SET nombre = ?, descripcion = ?, estado = ? WHERE id_categoria = ?";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getEstado());
            ps.setInt(4, categoria.getIdCategoria());
            ps.executeUpdate();
            System.out.println("Categori­a actualizada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar categorÃ­a: " + e.getMessage());
        }
    }

    public void desactivar(int idCategoria) {
        String sql = "UPDATE Categorias SET estado = 'INACTIVO' WHERE id_categoria = ?";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
            System.out.println("Categori­a desactivada.");
        } catch (SQLException e) {
            System.out.println("Error al desactivar categori­a: " + e.getMessage());
        }
    }

    public List<Categoria> obtenerActivas() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, id_padre, nivel, estado, fecha_creacion " +
                     "FROM Categorias WHERE estado = 'ACTIVO' ORDER BY id_padre, nombre";
        
        try (Connection conn = ConexionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categorÃ­as activas: " + e.getMessage());
        }
        
        return categorias;
    }

    private Categoria mapearResultSet(ResultSet rs) throws SQLException {
        int idCategoria = rs.getInt("id_categoria");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        Object idPadreObj = rs.getObject("id_padre");
        Integer idPadre = idPadreObj != null ? rs.getInt("id_padre") : null;
        int nivel = rs.getInt("nivel");
        String estado = rs.getString("estado");
        Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
        LocalDateTime fechaCreacion = tsCreacion != null ? tsCreacion.toLocalDateTime() : LocalDateTime.now();
        
        return new Categoria(idCategoria, nombre, descripcion, idPadre, nivel, estado, fechaCreacion);
    }
}
