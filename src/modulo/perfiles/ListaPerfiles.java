package modulo.perfiles;

import java.util.ArrayList;
import java.util.List;

public class ListaPerfiles {

    private NodoPerfil cabeza;

    public ListaPerfiles() {
        this.cabeza = null;
    }

    public void agregar(Perfil perfil) {
        NodoPerfil nuevo = new NodoPerfil(perfil);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public Perfil buscarPorId(int idPerfil) {
        NodoPerfil actual = cabeza;
        while (actual != null) {
            if (actual.dato.getIdPerfil() == idPerfil) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public List<Perfil> obtenerTodos() {
        List<Perfil> lista = new ArrayList<>();
        NodoPerfil actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }

    public void mostrar() {
        NodoPerfil actual = cabeza;
        while (actual != null) {
            System.out.println("- Perfil: " + actual.dato.getNombrePerfil());
            actual = actual.siguiente;
        }
    }
}
