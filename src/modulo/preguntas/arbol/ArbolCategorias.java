package modulo.preguntas.arbol;

public class ArbolCategorias {

    private NodoCategoria raiz;

    public ArbolCategorias(String nombreRaiz) {
        this.raiz = new NodoCategoria(nombreRaiz);
    }

    public NodoCategoria getRaiz() {
        return raiz;
    }

    public void mostrar() {
        if (raiz != null) {
            raiz.mostrar("");
        }
    }
}
