package modulo.encuestas.ejecucion;

import modulo.encuestas.configuracion.Encuesta;

public class NodoEncuestaCola {

    Encuesta encuesta;
    NodoEncuestaCola siguiente;

    public NodoEncuestaCola(Encuesta encuesta) {
        this.encuesta = encuesta;
        this.siguiente = null;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public NodoEncuestaCola getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoEncuestaCola siguiente) {
        this.siguiente = siguiente;
    }
}
