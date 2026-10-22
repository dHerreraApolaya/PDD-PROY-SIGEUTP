package modulo.participantes;

public class ListaParticipante {

    private NodoParticipante cabeza;

    public ListaParticipante() {
        this.cabeza = null;
    }

    public NodoParticipante getCabeza() {
        return cabeza;
    }
    
    public boolean contiene(Participante nuevo) {
        NodoParticipante actual = cabeza;
        while (actual != null) {
            Participante existente = actual.getParticipante();
            if (existente.getDni().equals(nuevo.getDni())
                    || existente.getCorreo().equalsIgnoreCase(nuevo.getCorreo())) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    public boolean agregar(Participante nuevo) {
        if (contiene(nuevo)) {
            System.out.println("El participante ya se encuentra registrado.");
            return false;
        }

        NodoParticipante nuevoNodo = new NodoParticipante(nuevo);

        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoParticipante actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        return true;
    }
    
    public Participante buscarPorDni(String dni) {
        NodoParticipante actual = cabeza;
        while (actual != null) {
            if (actual.getParticipante().getDni().equals(dni)) {
                return actual.getParticipante();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void mostrar() {
        NodoParticipante actual = cabeza;
        System.out.println("=== LISTA DE PARTICIPANTES ===");
        while (actual != null) {
            System.out.println(actual.getParticipante().toString());
            actual = actual.getSiguiente();
        }
        System.out.println("==============================");
    }

    public int contar() {
        int contador = 0;
        NodoParticipante actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }
}
