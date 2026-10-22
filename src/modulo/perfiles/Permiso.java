package modulo.perfiles;

public class Permiso {

    private String nombre;

    public Permiso(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permiso) {
            return nombre.equalsIgnoreCase(((Permiso) obj).getNombre());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    public void ejecutar() {
        System.out.println("Ejecutando permiso: " + nombre);
    }

}
