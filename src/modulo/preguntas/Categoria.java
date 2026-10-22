package modulo.preguntas;

import java.time.LocalDateTime;

public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private Integer idPadre;  
    private int nivel;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Categoria(String nombre, String descripcion, Integer idPadre, int nivel, String estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idPadre = idPadre;
        this.nivel = nivel;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Categoria(int idCategoria, String nombre, String descripcion, Integer idPadre, int nivel, String estado, LocalDateTime fechaCreacion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idPadre = idPadre;
        this.nivel = nivel;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Integer idPadre) {
        this.idPadre = idPadre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idPadre=" + idPadre +
                ", nivel=" + nivel +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
