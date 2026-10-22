package modulo.preguntas;

public class Pregunta {

    private int idPregunta;
    private String texto;
    private String[] opciones;
    private boolean esFiltro;
    private Integer idPreguntaPadre;   
    private String operadorFiltro;     
    private String valorFiltro;         
    private String categoria;
    private Integer idCategoria;  
    private TipoPregunta tipo;
    private boolean activa;
    private int idEncuesta;

    public int getIdEncuesta() {
    return idEncuesta;
}

public void setIdEncuesta(int idEncuesta) {
    this.idEncuesta = idEncuesta;
}
    public String getCategoria() {
        return categoria;
    }

    public void setFiltro(boolean esFiltro) {
        this.esFiltro = esFiltro;
    }

    public Pregunta(int idPregunta, int idEncuesta, String texto, TipoPregunta tipo, String categoria, boolean esFiltro) {
    this.idPregunta = idPregunta;
    this.idEncuesta = idEncuesta;
    this.texto = texto;
    this.tipo = tipo;
    this.categoria = categoria;
    this.esFiltro = esFiltro;
    this.activa = true;
    this.opciones = new String[0];
}

    public Pregunta(int idPregunta, int idEncuesta, Integer idCategoria, String texto, TipoPregunta tipo, boolean esFiltro) {
        this.idPregunta = idPregunta;
        this.idEncuesta = idEncuesta;
        this.idCategoria = idCategoria;
        this.texto = texto;
        this.tipo = tipo;
        this.esFiltro = esFiltro;
        this.activa = true;
        this.opciones = new String[0];
        this.categoria = "";  
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public String getTexto() {
        return texto;
    }

    public String[] getOpciones() {
        return opciones;
    }
    
    public void setOpciones(String[] opciones) {
        this.opciones = opciones;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Pregunta(TipoPregunta tipo) {
        this.tipo = tipo;
        this.activa = true;
    }

    public TipoPregunta getTipo() {
        return tipo;
    }

    public void setTipo(TipoPregunta tipo) {
        this.tipo = tipo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void inactivar() {
        this.activa = false;
    }

    public boolean isFiltro() {
        return esFiltro;
    }


    public Integer getIdPreguntaPadre() {
        return idPreguntaPadre;
    }

    public void setIdPreguntaPadre(Integer idPreguntaPadre) {
        this.idPreguntaPadre = idPreguntaPadre;
    }

    public String getOperadorFiltro() {
        return operadorFiltro;
    }

    public void setOperadorFiltro(String operadorFiltro) {
        this.operadorFiltro = operadorFiltro;
    }

    public String getValorFiltro() {
        return valorFiltro;
    }

    public void setValorFiltro(String valorFiltro) {
        this.valorFiltro = valorFiltro;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return (activa ? "[ACTIVA] " : "[INACTIVA] ") + tipo;
    }

    public void generarOpcionesSegunTipo() {
        switch (this.tipo) {
            case DICOTOMICA:
                
                this.opciones = new String[]{"Si", "No"};
                break;
            case RESPUESTA_SIMPLE:
              
                this.opciones = new String[]{"(Ingrese su respuesta)"};
                break;
            case ESCALA_VALORACION:
               
                this.opciones = new String[]{
                    "1Totalmente en desacuerdo",
                    "2En desacuerdo",
                    "3Neutral",
                    "4De acuerdo",
                    "5Totalmente de acuerdo"
                };
                break;
            case NUMERICA:
           
                this.opciones = new String[]{"(Ingrese un numero)"};
                break;
            default:
                this.opciones = new String[]{};
        }
    }

}
