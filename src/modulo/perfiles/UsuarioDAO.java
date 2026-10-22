package modulo.perfiles;

import modulo.respuestas.Usuario;

public class UsuarioDAO {

    private static PerfilAdministrador perfilAdmin;
    private static PerfilEncuestador perfilEncuestador;
    private static Externo perfilExterno;

    static {
        ListaPermiso permisosAdmin = new ListaPermiso();
        permisosAdmin.agregar(new Permiso("Crear Encuestas"));
        permisosAdmin.agregar(new Permiso("Eliminar Usuarios"));
        permisosAdmin.agregar(new Permiso("Ver Reportes"));

        ListaPermiso permisosEncuestador = new ListaPermiso();
        permisosEncuestador.agregar(new Permiso("Realizar Encuestas"));
        permisosEncuestador.agregar(new Permiso("Ver Encuestas Propias"));

        ListaPermiso permisosExterno = new ListaPermiso();
        permisosExterno.agregar(new Permiso("Responder Encuestas"));

        perfilAdmin = new PerfilAdministrador(1, permisosAdmin);
        perfilEncuestador = new PerfilEncuestador(2, permisosEncuestador);
        perfilExterno = new Externo(3, permisosExterno);
    }

    public Usuario validarUsuario(String username, String password) {
        
        if (username.equalsIgnoreCase("admin") && password.equals("123")) {
            return new Usuario("Administrador del Sistema", perfilAdmin);
        }

        if (username.equalsIgnoreCase("encuestador") && password.equals("123")) {
            return new Usuario("Operador Encuestador", perfilEncuestador);
        }

        if (username.equalsIgnoreCase("externo") && password.equals("123")) {
            return new Usuario("Usuario Externo", perfilExterno);
        }

        return null;
    }
}
