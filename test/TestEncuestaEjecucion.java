public class TestEncuestaEjecucion {
    public static void main(String[] args) {
        // Cargar instancias
        modulo.encuestas.configuracion.EncuestaDAO encuestaDAO = modulo.encuestas.configuracion.EncuestaDAO.getInstancia();
        modulo.preguntas.PreguntaDAO preguntaDAO = modulo.preguntas.PreguntaDAO.getInstancia();
        
        System.out.println("=== ENCUESTAS CARGADAS ===");
        java.util.List<modulo.encuestas.configuracion.Encuesta> encuestas = encuestaDAO.listarEncuestasActivas();
        
        if (encuestas == null || encuestas.isEmpty()) {
            System.out.println("No hay encuestas disponibles.");
        } else {
            for (modulo.encuestas.configuracion.Encuesta e : encuestas) {
                System.out.println("- ID: " + e.getIdEncuesta() + ", Título: " + e.getTitulo());
            }
        }
        
        System.out.println("\n=== PREGUNTAS DE ENCUESTA 1 ===");
        modulo.preguntas.ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(1);
        if (preguntas == null || preguntas.getCabeza() == null) {
            System.out.println("No hay preguntas.");
        } else {
            modulo.preguntas.NodoPregunta nodo = preguntas.getCabeza();
            int i = 1;
            while (nodo != null) {
                System.out.println(i + ". " + nodo.getPregunta().getTexto());
                i++;
                nodo = nodo.getSiguiente();
            }
        }
    }
}
