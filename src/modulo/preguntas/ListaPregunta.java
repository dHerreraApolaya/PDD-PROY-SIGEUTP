package modulo.preguntas;

import java.util.ArrayList;
import java.util.List;

public class ListaPregunta {

    private NodoPregunta cabeza;

    public ListaPregunta() {
        this.cabeza = null;
    }

    public void agregar(Pregunta pregunta) {
        NodoPregunta nuevo = new NodoPregunta(pregunta);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoPregunta actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
    }

    public void eliminar(int id) {
        if (cabeza == null) {
            return;
        }

        if (cabeza.getPregunta().getIdPregunta() == id) {
            cabeza = cabeza.getSiguiente();
            return;
        }

        NodoPregunta actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getPregunta().getIdPregunta() == id) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    public Pregunta buscarPorId(int id) {
        NodoPregunta actual = cabeza;
        while (actual != null) {
            if (actual.getPregunta().getIdPregunta() == id) {
                return actual.getPregunta();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void mostrar() {
        NodoPregunta actual = cabeza;
        if (actual == null) {
            System.out.println("No hay preguntas registradas.");
            return;
        }

        System.out.println("\n=== LISTA DE PREGUNTAS ===");
        while (actual != null) {
            System.out.println(actual.getPregunta());
            actual = actual.getSiguiente();
        }
        System.out.println("");
    }

    public int contar() {
        int contador = 0;
        NodoPregunta actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public List<Pregunta> toList() {
        List<Pregunta> lista = new ArrayList<>();
        NodoPregunta actual = cabeza;
        while (actual != null) {
            lista.add(actual.getPregunta());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    public NodoPregunta getCabeza() {
        return cabeza;
    }
}
