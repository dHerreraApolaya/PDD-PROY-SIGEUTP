package modulo.perfiles;

import java.util.List;

public class PerfilDAO {
    private ListaPerfiles listaPerfiles;

    public PerfilDAO() {
        this.listaPerfiles = new ListaPerfiles();
    }

    public void registrarPerfil(Perfil perfil) {
        listaPerfiles.agregar(perfil);
    }

    public Perfil obtenerPerfilPorId(int idPerfil) {
        return listaPerfiles.buscarPorId(idPerfil);
    }

    public List<Perfil> obtenerTodos() {
        return listaPerfiles.obtenerTodos();
    }

    public void mostrarPerfiles() {
        listaPerfiles.mostrar();
    }
}
