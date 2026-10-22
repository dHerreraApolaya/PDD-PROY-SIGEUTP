package modulo.encuestas.configuracion;

import modulo.respuestas.ListaRespuesta;

public interface repositorioEncuesta {

    void guardarRespuestas(String dniParticipante, ListaRespuesta respuestas);
}
