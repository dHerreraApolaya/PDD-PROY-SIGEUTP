package modulo.respuestas;

public class NodoRespuesta {

    private Respuesta respuesta;
    private NodoRespuesta siguiente;

    public NodoRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
        this.siguiente = null;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public NodoRespuesta getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoRespuesta siguiente) {
        this.siguiente = siguiente;
    }

}
