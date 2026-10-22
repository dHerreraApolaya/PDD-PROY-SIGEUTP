package modulo.respuestas;

import java.time.LocalDateTime;

public class Respuesta {

    private int idRespuesta;
    private int idEncuesta;
    private int idPregunta;
    private String contenido;
    private int idParticipante;
    private LocalDateTime fechaRespuesta;
    private boolean esValida;  

    public Respuesta() {
    }

    public Respuesta(int idRespuesta, int idEncuesta, int idPregunta, String contenido, 
        int idParticipante, LocalDateTime fechaRespuesta) {
        this.idRespuesta = idRespuesta;
        this.idEncuesta = idEncuesta;
        this.idPregunta = idPregunta;
        this.contenido = contenido;
        this.idParticipante = idParticipante;
        this.fechaRespuesta = fechaRespuesta;
        this.esValida = true;  
    }

    public Respuesta(int idRespuesta, int idEncuesta, int idPregunta, String contenido, 
        int idParticipante, LocalDateTime fechaRespuesta, boolean esValida) {
        this.idRespuesta = idRespuesta;
        this.idEncuesta = idEncuesta;
        this.idPregunta = idPregunta;
        this.contenido = contenido;
        this.idParticipante = idParticipante;
        this.fechaRespuesta = fechaRespuesta;
        this.esValida = esValida;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public boolean isEsValida() {
        return esValida;
    }

    public void setEsValida(boolean esValida) {
        this.esValida = esValida;
    }

    @Override
    public String toString() {
        return "Respuesta ID: " + idRespuesta
                + " | Encuesta ID: " + idEncuesta
                + " | Pregunta ID: " + idPregunta
                + " | Contenido: " + contenido
                + " | Participante ID: " + idParticipante
                + " | Fecha: " + (fechaRespuesta != null ? fechaRespuesta.toString() : "Sin fecha");
    }
}
