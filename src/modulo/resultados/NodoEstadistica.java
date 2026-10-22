package modulo.resultados;

public class NodoEstadistica {

    private EstadisticaPregunta estadistica;
    private NodoEstadistica siguiente;

    public NodoEstadistica(EstadisticaPregunta estadistica) {
        this.estadistica = estadistica;
    }

    public EstadisticaPregunta getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(EstadisticaPregunta estadistica) {
        this.estadistica = estadistica;
    }

    public NodoEstadistica getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoEstadistica siguiente) {
        this.siguiente = siguiente;
    }
}
