package modulo.perfiles;

public class NodoPerfil {
    Perfil dato;
    NodoPerfil siguiente;

    public NodoPerfil(Perfil dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
