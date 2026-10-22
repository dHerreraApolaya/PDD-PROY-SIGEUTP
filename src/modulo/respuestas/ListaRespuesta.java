package modulo.respuestas;

public class ListaRespuesta {

    private NodoRespuesta cabeza;

    public ListaRespuesta() {
        this.cabeza = null;
    }

    public void agregar(Respuesta respuesta) {
        NodoRespuesta nuevoNodo = new NodoRespuesta(respuesta);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoRespuesta actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
    }

    public void mostrar() {
        NodoRespuesta actual = cabeza;
        if (actual == null) {
            System.out.println("No hay respuestas registradas.");
            return;
        }

        System.out.println("\n=== LISTA DE RESPUESTAS ===");
        while (actual != null) {
            System.out.println(actual.getRespuesta());
            actual = actual.getSiguiente();
        }

        System.out.println("");
    }

    public int contar() {
        int contador = 0;
        NodoRespuesta actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public Respuesta buscarPorPregunta(int idPregunta) {
        NodoRespuesta actual = cabeza;
        while (actual != null) {
            if (actual.getRespuesta().getIdPregunta() == idPregunta) {
                return actual.getRespuesta();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public ListaRespuesta filtrarPorEncuesta(int idEncuesta) {
        ListaRespuesta filtrada = new ListaRespuesta();
        NodoRespuesta actual = cabeza;

        while (actual != null) {
            if (actual.getRespuesta().getIdEncuesta() == idEncuesta) {
                filtrada.agregar(actual.getRespuesta());
            }
            actual = actual.getSiguiente();
        }
        return filtrada;
    }

    public NodoRespuesta getCabeza() {
        return cabeza;
    }
    
    
}
