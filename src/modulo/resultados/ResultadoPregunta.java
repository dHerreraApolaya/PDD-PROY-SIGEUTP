package modulo.resultados;

public class ResultadoPregunta {

    private int idPregunta;
    private String textoPregunta;
    private String[] respuestas;

    public ResultadoPregunta(int idPregunta, String textoPregunta, int maxRespuestas) {
        this.idPregunta = idPregunta;
        this.textoPregunta = textoPregunta;
        this.respuestas = new String[maxRespuestas];
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public String[] getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(String[] respuestas) {
        this.respuestas = respuestas;
    }
}
