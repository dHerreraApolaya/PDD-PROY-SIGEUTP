package modulo.perfiles;

public class NodoPermiso {

    Permiso dato;
    NodoPermiso siguiente;

    public NodoPermiso(Permiso dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public Permiso getDato() {
        return dato;
    }

    public void setDato(Permiso dato) {
        this.dato = dato;
    }

    public NodoPermiso getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPermiso siguiente) {
        this.siguiente = siguiente;
    }

}
