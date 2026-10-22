package modulo.perfiles;

public class PerfilAdministrador extends Perfil {

    public PerfilAdministrador(int idPerfil, ListaPermiso permisos) {
        super(idPerfil, "Administrador", permisos);
    }
}
