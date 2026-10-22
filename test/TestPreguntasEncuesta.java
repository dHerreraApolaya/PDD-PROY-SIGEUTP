public class TestPreguntasEncuesta {
    public static void main(String[] args) {
        // Cargar instancia de PreguntaDAO
        modulo.preguntas.PreguntaDAO preguntaDAO = modulo.preguntas.PreguntaDAO.getInstancia();
        
        // Obtener preguntas de la encuesta 1
        modulo.preguntas.ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(1);
        
        System.out.println("=== PREGUNTAS ASOCIADAS A ENCUESTA 1 ===");
        
        if (preguntas == null || preguntas.getCabeza() == null) {
            System.out.println("No hay preguntas asociadas a esta encuesta.");
        } else {
            int contador = 1;
            modulo.preguntas.NodoPregunta nodo = preguntas.getCabeza();
            while (nodo != null) {
                System.out.println(contador + ". " + nodo.getPregunta().getTexto() + 
                " (ID: " + nodo.getPregunta().getIdPregunta() + 
                ", ID Encuesta: " + nodo.getPregunta().getIdEncuesta() + ")");
                contador++;
                nodo = nodo.getSiguiente();
            }
        }
    }
}
