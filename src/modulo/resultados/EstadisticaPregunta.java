package modulo.resultados;

public class EstadisticaPregunta {

    private int idPregunta;
    private double promedio;

    public EstadisticaPregunta(int idPregunta, double promedio) {
        this.idPregunta = idPregunta;
        this.promedio = promedio;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
}
