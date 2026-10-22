package modulo.perfiles;

import modulo.respuestas.Usuario;

public class VistaPermisos {

    public void mostrarPermisos(Usuario usuario) {
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Perfil: " + usuario.getPerfil().getNombrePerfil());
        System.out.println("Permisos asignados:");

        ListaPermiso lista = usuario.getPerfil().getPermisos();
        NodoPermiso actual = lista.getCabeza();

        if (actual == null) {
            System.out.println(" - No tiene permisos asignados.");
        } else {
            while (actual != null) {
                System.out.println(" - " + actual.getDato().getNombre());
                actual = actual.getSiguiente();
            }
        }

        System.out.println();
    }
}
