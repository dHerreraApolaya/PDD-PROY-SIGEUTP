package modulo.encuestas.configuracion;

public class NodoEncuesta {

    private Encuesta encuesta;

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }
    private NodoEncuesta siguiente;
    private NodoEncuesta anterior;

    public NodoEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
        this.siguiente = null;
        this.anterior = null;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public NodoEncuesta getSiguiente() {
        return siguiente;
    }

    public NodoEncuesta getAnterior() {
        return anterior;
    }

    public void setSiguiente(NodoEncuesta siguiente) {
        this.siguiente = siguiente;
    }

    public void setAnterior(NodoEncuesta anterior) {
        this.anterior = anterior;
    }
}

