package modulo.perfiles;

public class ListaPermiso {

    private NodoPermiso cabeza;

    public ListaPermiso() {
        this.cabeza = null;
    }

    public void agregar(Permiso permiso) {
        if (!existe(permiso.getNombre())) {
            NodoPermiso nuevo = new NodoPermiso(permiso);
            nuevo.setSiguiente(cabeza);
            cabeza = nuevo;
        }
    }

    public boolean existe(String nombrePermiso) {
        NodoPermiso actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getNombre().equalsIgnoreCase(nombrePermiso)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void mostrar() {
        if (cabeza == null) {
            System.out.println("Este perfil no tiene permisos asignados.");
            return;
        }

        System.out.println("Permisos: ");
        NodoPermiso actual = cabeza;
        while (actual != null) {
            System.out.println("- " + actual.getDato().getNombre());
            actual = actual.getSiguiente();
        }
    }

    public NodoPermiso getCabeza() {
        return cabeza;
    }
}
