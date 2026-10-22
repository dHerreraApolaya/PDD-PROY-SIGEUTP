package modulo.respuestas;

import modulo.perfiles.Perfil;
import modulo.perfiles.ListaPermiso;
import modulo.perfiles.NodoPermiso;

public class Usuario {

    private String nombre;
    private Perfil perfil;

    public Usuario(String nombre, Perfil perfil) {
        this.nombre = nombre;
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void mostrarInfo() {
        System.out.println("Usuario: " + nombre);
        perfil.mostrarPermisos();
    }

    public void ejecutarPermisos() {
        ListaPermiso lista = perfil.getPermisos();
        NodoPermiso actual = lista.getCabeza();

        while (actual != null) {
            actual.getDato().ejecutar();
            actual = actual.getSiguiente();
        }
    }
}