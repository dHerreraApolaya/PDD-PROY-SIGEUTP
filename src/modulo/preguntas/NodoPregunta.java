package modulo.preguntas;

public class NodoPregunta {
    private Pregunta pregunta;
    private NodoPregunta siguiente;

    public NodoPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
        this.siguiente = null;
    }
    public Pregunta getPregunta() {
        return pregunta;
    }
    
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    
    public NodoPregunta getSiguiente() {
        return siguiente;
    }
    
    public void setSiguiente(NodoPregunta siguiente) {
        this.siguiente = siguiente;
    }
}
