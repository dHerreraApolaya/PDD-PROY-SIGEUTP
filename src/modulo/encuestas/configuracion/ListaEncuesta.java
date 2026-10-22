package modulo.encuestas.configuracion;

public class ListaEncuesta {

    private NodoEncuesta cabeza;
    private NodoEncuesta cola;

    public ListaEncuesta() {
        this.cabeza = null;
        this.cola = null;
    }
    
    public NodoEncuesta getCabeza() {
    return cabeza;
}

    public void agregar(Encuesta encuesta) {
        NodoEncuesta nuevo = new NodoEncuesta(encuesta);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }
    }

    public boolean eliminarPorId(int idEncuesta) {
        if (cabeza == null) {
            return false;
        }
        if (cabeza.getEncuesta().getIdEncuesta() == idEncuesta) {
            cabeza = cabeza.getSiguiente();
            return true;
        }
        NodoEncuesta actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getEncuesta().getIdEncuesta() == idEncuesta) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Encuesta buscarPorId(int idEncuesta) {
        NodoEncuesta actual = cabeza;
        while (actual != null) {
            if (actual.getEncuesta().getIdEncuesta() == idEncuesta) {
                return actual.getEncuesta();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void listarDesdeInicio() {
        NodoEncuesta actual = cabeza;
        while (actual != null) {
            imprimirEncuesta(actual.getEncuesta());
            actual = actual.getSiguiente();
        }
    }

    public void listarDesdeFinal() {
        NodoEncuesta actual = cola;
        while (actual != null) {
            imprimirEncuesta(actual.getEncuesta());
            actual = actual.getAnterior();
        }
    }

    private void imprimirEncuesta(Encuesta e) {
        System.out.println("ID: " + e.getIdEncuesta() + " | Titulo: " + e.getTitulo()
                + " | Estado: " + e.getEstado() + " | Usuario: " + e.getIdUsuario());
    }

    public void listarPorEstado(String estadoBuscado) {
        NodoEncuesta actual = cabeza;
        while (actual != null) {
            Encuesta e = actual.getEncuesta();
            if (e.getEstado().equalsIgnoreCase(estadoBuscado)) {
                imprimirEncuesta(e);
            }
            actual = actual.getSiguiente();
        }
    }

    public void ordenarPorEstado() {
        if (cabeza == null || cabeza.getSiguiente() == null) {
            return;
        }
        boolean huboIntercambio;
        do {
            NodoEncuesta actual = cabeza;
            huboIntercambio = false;

            while (actual.getSiguiente() != null) {
                Encuesta e1 = actual.getEncuesta();
                Encuesta e2 = actual.getSiguiente().getEncuesta();
                if (e1.getEstado().compareToIgnoreCase(e2.getEstado()) > 0) {
                    actual.setEncuesta(e2);
                    actual.getSiguiente().setEncuesta(e1);
                    huboIntercambio = true;
                }
                actual = actual.getSiguiente();
            }
        } while (huboIntercambio);
        System.out.println("\n=== ENCUESTAS ORDENADAS POR ESTADO ===");
        listarDesdeInicio();
    }

    public void ordenarPorFecha() {
        if (cabeza == null || cabeza.getSiguiente() == null) {
            return;
        }

        boolean huboIntercambio;
        do {
            NodoEncuesta actual = cabeza;
            huboIntercambio = false;

            while (actual.getSiguiente() != null) {
                Encuesta e1 = actual.getEncuesta();
                Encuesta e2 = actual.getSiguiente().getEncuesta();

                if (e1.getFechaCreacion().isAfter(e2.getFechaCreacion())) {
                    actual.setEncuesta(e2);
                    actual.getSiguiente().setEncuesta(e1);
                    huboIntercambio = true;
                }
                actual = actual.getSiguiente();
            }
        } while (huboIntercambio);

        System.out.println("\n=== ENCUESTAS ORDENADAS POR FECHA DE CREACION ===");
        listar();
    }

    public void listar() {
        NodoEncuesta actual = cabeza;
        while (actual != null) {
            Encuesta e = actual.getEncuesta();
            System.out.println("ID: " + e.getIdEncuesta() + " | Titulo: " + e.getTitulo()
                    + " | Estado: " + e.getEstado() + " | Usuario: " + e.getIdUsuario() + " | Fecha: " + e.getFechaCreacion());
            actual = actual.getSiguiente();
        }
    }
}

