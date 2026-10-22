package modulo.encuestas.ejecucion;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import modulo.encuestas.configuracion.Encuesta;

public class ColaEncuesta implements Queue<Encuesta> {

    private NodoEncuestaCola frente;
    private NodoEncuestaCola fin;
    private int tamano;

    public ColaEncuesta() {
        frente = null;
        fin = null;
        tamano = 0;
    }

    @Override
    public boolean offer(Encuesta encuesta) {
        NodoEncuestaCola nuevo = new NodoEncuestaCola(encuesta);
        if (frente == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }
        tamano++;
        return true;
    }

    @Override
    public Encuesta poll() {
        if (frente == null) {
            return null;
        }
        Encuesta encuesta = frente.getEncuesta();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamano--;
        return encuesta;
    }

    @Override
    public Encuesta peek() {
        return (frente != null) ? frente.getEncuesta() : null;
    }

    @Override
    public boolean isEmpty() {
        return tamano == 0;
    }

    @Override
    public int size() {
        return tamano;
    }

    public void mostrarCola() {
        System.out.println("\n=== COLA DE EJECUCION DE ENCUESTAS ===");
        NodoEncuestaCola actual = frente;
        while (actual != null) {
            Encuesta e = actual.getEncuesta();
            System.out.println("ID: " + e.getIdEncuesta() + " | Titulo: " + e.getTitulo() + " | Estado: " + e.getEstado());
            actual = actual.getSiguiente();
        }
    }

    @Override
    public boolean add(Encuesta e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Encuesta remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Encuesta element() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Encuesta> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Encuesta> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        frente = null;
        fin = null;
        tamano = 0;
    }
}
