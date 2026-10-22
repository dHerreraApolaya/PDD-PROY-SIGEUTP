package modulo.respuestas;

public class registroRespuesta {

    private repositorioRespuesta repositorio;

    public registroRespuesta(repositorioRespuesta repositorio) {
        this.repositorio = repositorio;
    }

    public void registrar(String dniParticipante, ListaRespuesta respuestas) {
        repositorio.guardarRespuestas(dniParticipante, respuestas);
    }
}
