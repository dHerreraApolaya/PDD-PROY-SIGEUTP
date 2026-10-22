package modulo.encuestas.configuracion;

public class EncuestaController {

    private static final ListaEncuesta listaEncuesta = new ListaEncuesta();

    public static void agregarEncuesta(Encuesta encuesta) {
        listaEncuesta.agregar(encuesta);
    }

    public static void ordenarPorFecha() {
        listaEncuesta.ordenarPorFecha();
    }

    public static void listar() {
        listaEncuesta.listar();
    }

    public static void ordenarPorEstado() {
        listaEncuesta.ordenarPorEstado();
    }

    public static void listarPorEstado(String estado) {
        listaEncuesta.listarPorEstado(estado);
    }
}
