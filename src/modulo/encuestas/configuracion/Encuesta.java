package modulo.encuestas.configuracion;

import java.time.LocalDate;
import modulo.preguntas.Pregunta;
import modulo.preguntas.TipoPregunta;

public class Encuesta {

    private int idEncuesta;
    private String titulo;
    private String descripcion;
    private String estado;
    private String perfilCreador;   
    private int idUsuario;        
    private PerfilParticipante perfilParticipante;
    private LocalDate fechaCreacion;
    private Pregunta[] preguntas;

    public Encuesta() {
        this.preguntas = new Pregunta[20];
        this.fechaCreacion = LocalDate.now();
    }

    public Encuesta(String titulo, String descripcion, String estado, String perfilCreador) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.perfilCreador = perfilCreador;
    }

    public Encuesta(int idEncuesta, String titulo, String descripcion, String estado, int idUsuario) {
        this();
        this.idEncuesta = idEncuesta;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    public int getIdEncuesta() { return idEncuesta; }
    public void setIdEncuesta(int idEncuesta) { this.idEncuesta = idEncuesta; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPerfilCreador() { return perfilCreador; }
    public void setPerfilCreador(String perfilCreador) { this.perfilCreador = perfilCreador; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public PerfilParticipante getPerfilParticipante() { return perfilParticipante; }
    public void setPerfilParticipante(PerfilParticipante perfil) { this.perfilParticipante = perfil; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public void crearPregunta(int indice, TipoPregunta tipo) {
        if (indice >= 0 && indice < preguntas.length) {
            preguntas[indice] = new Pregunta(tipo);
            System.out.println("Pregunta creada en posicion " + indice);
        }
    }

    public void editarPregunta(int indice, TipoPregunta nuevoTipo) {
        if (preguntas[indice] != null) {
            preguntas[indice].setTipo(nuevoTipo);
            System.out.println("Pregunta en posicion " + indice + " editada.");
        }
    }

    public void eliminarPregunta(int indice) {
        if (preguntas[indice] != null) {
            preguntas[indice] = null;
            System.out.println("Pregunta eliminada en posicion " + indice);
        }
    }

    public void inactivarPregunta(int indice) {
        if (preguntas[indice] != null) {
            preguntas[indice].inactivar();
            System.out.println("Pregunta en posicion " + indice + " inactivada.");
        }
    }

    public void mostrarPreguntas() {
        for (int i = 0; i < preguntas.length; i++) {
            System.out.println("Posicion " + i + ": "
                    + (preguntas[i] != null ? preguntas[i] : "[VACIA]"));
        }
    }

    public Pregunta[] getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(Pregunta[] preguntas) {
        this.preguntas = preguntas;
    }
}
