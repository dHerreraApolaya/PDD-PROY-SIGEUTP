package modulo.preguntas;

public enum TipoPregunta {
    DICOTOMICA(1, "Dicotomica (Si/No)"),
    RESPUESTA_SIMPLE(2, "Respuesta simple (una opcion)"),
    ESCALA_VALORACION(3, "Escala de valoracion (1 a 5)"),
    NUMERICA(4, "Respuesta numerica");

    private final int codigo;
    private final String descripcion;

    TipoPregunta(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoPregunta desdeCodigo(int codigo) {
        for (TipoPregunta tipo : TipoPregunta.values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        return null;
    }
}
