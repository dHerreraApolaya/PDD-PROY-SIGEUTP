package modulo.perfiles;

public class Perfil {

    private int idPerfil;
    private String nombrePerfil;
    private ListaPermiso permisos;

    public Perfil(int idPerfil, String nombrePerfil, ListaPermiso permisos) {
        this.idPerfil = idPerfil;
        this.nombrePerfil = nombrePerfil;
        this.permisos = (permisos != null) ? permisos : new ListaPermiso();
    }

    public Perfil(String nombrePerfil) {
        this.idPerfil = 0;
        this.nombrePerfil = nombrePerfil;
        this.permisos = new ListaPermiso();
    }

    public void agregarPermiso(String nombrePermiso) {
        permisos.agregar(new Permiso(nombrePermiso));
    }

    public boolean tienePermiso(String nombrePermiso) {
        return permisos.existe(nombrePermiso);
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public ListaPermiso getPermisos() {
        return permisos;
    }

    public void mostrarPermisos() {
        System.out.println("Permisos asignados al perfil " + nombrePerfil + ":");
        permisos.mostrar();
    }
}
