package modulo.participantes;

public interface repositorioParticipante {

    boolean porDNI(String dni);

    void guardar(Participante participante);
}
