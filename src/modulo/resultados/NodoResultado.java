package modulo.resultados;

public class NodoResultado {

    private ResultadoPregunta resultado;
    private NodoResultado siguiente;

    public NodoResultado(ResultadoPregunta resultado) {
        this.resultado = resultado;
    }

    public ResultadoPregunta getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoPregunta resultado) {
        this.resultado = resultado;
    }

    public NodoResultado getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoResultado siguiente) {
        this.siguiente = siguiente;
    }
}
