package modulo.preguntas.arbol;

import java.util.ArrayList;
import java.util.List;

public class NodoCategoria {

    private String nombre;
    private List<NodoCategoria> subcategorias;

    public NodoCategoria(String nombre) {
        this.nombre = nombre;
        this.subcategorias = new ArrayList<>();
    }

    public void agregarSubcategoria(NodoCategoria sub) {
        subcategorias.add(sub);
    }

    public String getNombre() {
        return nombre;
    }

    public List<NodoCategoria> getSubcategorias() {
        return subcategorias;
    }

    public void mostrar(String prefijo) {
        System.out.println(prefijo + "- " + nombre);
        for (NodoCategoria sub : subcategorias) {
            sub.mostrar(prefijo + "  ");
        }
    }
}
