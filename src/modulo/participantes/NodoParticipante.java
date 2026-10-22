package modulo.participantes;

public class NodoParticipante {

    private Participante participante;
    private NodoParticipante siguiente;

    public NodoParticipante(Participante participante) {
        this.participante = participante;
        this.siguiente = null;
    }

    public Participante getParticipante() {
        return participante;
    }

    public NodoParticipante getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoParticipante siguiente) {
        this.siguiente = siguiente;
    }
}
