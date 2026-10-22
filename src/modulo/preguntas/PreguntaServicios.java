package modulo.preguntas;

import java.util.List;
import java.util.Scanner;
import modulo.preguntas.arbol.ArbolCategorias;
import modulo.preguntas.arbol.NodoCategoria;

public class PreguntaServicios {

    private static Scanner scanner;
    private static final PreguntaDAO preguntaDAO = PreguntaDAO.getInstancia();
    private static final CategoriaDAO categoriaDAO = CategoriaDAO.getInstancia();
    private static ArbolCategorias arbolCategorias = new ArbolCategorias("Categorias de Preguntas");
    
    static {
        try {
      
            Class<?> scannerGlobalClass = Class.forName("ScannerGlobal");
            scanner = (Scanner) scannerGlobalClass.getMethod("getScanner").invoke(null);
        } catch (Exception e) {
         
            scanner = new Scanner(System.in);
        }
    }

    public static void agregarPregunta() {
        try {
            System.out.print("ID de la encuesta a la que pertenece esta pregunta: ");
            int idEncuesta = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Texto de la pregunta: ");
            String texto = scanner.nextLine();
            
            System.out.println("Seleccione el tipo de pregunta:");
            for (TipoPregunta tipo : TipoPregunta.values()) {
                System.out.println(tipo.getCodigo() + ". " + tipo.getDescripcion());
            }

            System.out.print("Opcion: ");
            TipoPregunta tipo = null;
            try {
                int opcionTipo = Integer.parseInt(scanner.nextLine().trim());
                tipo = TipoPregunta.desdeCodigo(opcionTipo);
                if (tipo == null) {
                    System.out.println("Error: Opcion invalida");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Opcion invalida");
                return;
            }
            
            System.out.println("\nSeleccione la categoria de la pregunta:");
            List<Categoria> categoriasRaiz = categoriaDAO.obtenerCategoriasRaiz();
            
            if (categoriasRaiz.isEmpty()) {
                System.out.println("Error: No hay categorias disponibles en la BD.");
                return;
            }
            
            int opcionCategoria = 0;
            Categoria categoriaSeleccionada = null;
            
            for (int i = 0; i < categoriasRaiz.size(); i++) {
                System.out.println((i + 1) + ". " + categoriasRaiz.get(i).getNombre());
            }
            System.out.println((categoriasRaiz.size() + 1) + ". Crear una nueva subcategoria");
            
            System.out.print("Opcion (1-" + (categoriasRaiz.size() + 1) + "): ");
            
            try {
                opcionCategoria = Integer.parseInt(scanner.nextLine().trim());
                
                if (opcionCategoria >= 1 && opcionCategoria <= categoriasRaiz.size()) {
                    categoriaSeleccionada = categoriasRaiz.get(opcionCategoria - 1);
                } else if (opcionCategoria == categoriasRaiz.size() + 1) {
                
                    System.out.print("Bajo que categoria padre? (1-" + categoriasRaiz.size() + "): ");
                    int opcionPadre = Integer.parseInt(scanner.nextLine().trim());
                    if (opcionPadre < 1 || opcionPadre > categoriasRaiz.size()) {
                        System.out.println("Error: Opcion invalida.");
                        return;
                    }
                    
                    Categoria categoriaPadre = categoriasRaiz.get(opcionPadre - 1);
                    System.out.print("Nombre de la nueva subcategoria: ");
                    String nombreSub = scanner.nextLine().trim();
                    System.out.print("Descripcion (opcional): ");
                    String descripcionSub = scanner.nextLine().trim();
                    
                    int idSubcategoria = categoriaDAO.crearSubcategoria(nombreSub, descripcionSub, categoriaPadre.getIdCategoria());
                    if (idSubcategoria > 0) {
                        categoriaSeleccionada = categoriaDAO.obtenerPorId(idSubcategoria);
                        System.out.println("Subcategoria creada exitosamente.");
                    } else {
                        System.out.println("Error al crear la subcategoria.");
                        return;
                    }
                } else {
                    System.out.println("Error: Opcion invalida.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero valido.");
                return;
            }
            
            System.out.println("Categoria seleccionada: " + categoriaSeleccionada.getNombre());

            boolean esFiltro = false;
            while (true) {
                System.out.print("Es pregunta de filtro? (true/false): ");
                String filtroStr = scanner.nextLine().trim().toLowerCase();
                if (filtroStr.equals("true")) {
                    esFiltro = true;
                    break;
                } else if (filtroStr.equals("false")) {
                    esFiltro = false;
                    break;
                } else {
                    System.out.println("Error: Solo se admite 'true' o 'false'. Intente nuevamente.");
                }
            }
            
            Integer idPreguntaPadre = null;
            String operadorFiltro = null;
            String valorFiltro = null;
            
            if (esFiltro) {
                System.out.println("\n=== CONFIGURAR PREGUNTA FILTRO ===");
                System.out.print("ID de la pregunta de la cual depende esta pregunta: ");
                try {
                    idPreguntaPadre = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Error: ID invlido.");
                    return;
                }
                
                System.out.println("\nSeleccione el operador de comparacion:");
                System.out.println("1. IGUAL");
                System.out.println("2. DISTINTO");
                System.out.println("3. MAYOR");
                System.out.println("4. MENOR");
                System.out.println("5. MAYOR_IGUAL");
                System.out.println("6. MENOR_IGUAL");
                System.out.println("7. CONTIENE");
                System.out.print("Opcion (1-7): ");
                
                try {
                    int opcionOperador = Integer.parseInt(scanner.nextLine().trim());
                    switch (opcionOperador) {
                        case 1: operadorFiltro = "IGUAL"; break;
                        case 2: operadorFiltro = "DISTINTO"; break;
                        case 3: operadorFiltro = "MAYOR"; break;
                        case 4: operadorFiltro = "MENOR"; break;
                        case 5: operadorFiltro = "MAYOR_IGUAL"; break;
                        case 6: operadorFiltro = "MENOR_IGUAL"; break;
                        case 7: operadorFiltro = "CONTIENE"; break;
                        default:
                            System.out.println("Error: Opcion invalida.");
                            return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Opcion invalida.");
                    return;
                }
                
                System.out.print("Valor esperado (respuesta que dispara la pregunta): ");
                valorFiltro = scanner.nextLine().trim();
                
                System.out.println("\nPregunta filtro configurada:");
                System.out.println("  - Depende de pregunta ID: " + idPreguntaPadre);
                System.out.println("  - Operador: " + operadorFiltro);
                System.out.println("  - Valor esperado: " + valorFiltro);
            }
    
            Pregunta pregunta = new Pregunta(0, idEncuesta, categoriaSeleccionada.getIdCategoria(), texto, tipo, esFiltro);
        
            if (esFiltro) {
                pregunta.setIdPreguntaPadre(idPreguntaPadre);
                pregunta.setOperadorFiltro(operadorFiltro);
                pregunta.setValorFiltro(valorFiltro);
            }
            
            preguntaDAO.agregarPregunta(pregunta, idEncuesta);
            System.out.println("Pregunta agregada correctamente a la encuesta ID " + idEncuesta);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void editarPregunta() {
        try {
            System.out.print("ID de la encuesta a la que pertenece esta pregunta a editar: ");
            int idEncuesta = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("ID de la pregunta a editar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Pregunta existente = preguntaDAO.buscarPorId(id);
            if (existente == null) {
                System.out.println("No existe una pregunta con ese ID.");
                return;
            }

            System.out.print("Nuevo texto: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: El texto no puede estar vacio.");
                return;
            }
            
            System.out.println("Seleccione el nuevo tipo de pregunta:");
            for (TipoPregunta tipo : TipoPregunta.values()) {
                System.out.println(tipo.getCodigo() + ". " + tipo.getDescripcion());
            }
            
            TipoPregunta tipo = null;
            try {
                System.out.print("Opcion (1-4): ");
                int opcionTipo = Integer.parseInt(scanner.nextLine().trim());
                tipo = TipoPregunta.desdeCodigo(opcionTipo);
                if (tipo == null) {
                    System.out.println("Error: Opcion invalida.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Opcion invalida.");
                return;
            }
         
            System.out.println("\nSeleccione la nueva categoria:");
            List<Categoria> categoriasRaiz = categoriaDAO.obtenerCategoriasRaiz();
            
            if (categoriasRaiz.isEmpty()) {
                System.out.println("Error: No hay categorias disponibles en la BD.");
                return;
            }
            
            Categoria categoriaSeleccionada = null;
            
            for (int i = 0; i < categoriasRaiz.size(); i++) {
                System.out.println((i + 1) + ". " + categoriasRaiz.get(i).getNombre());
            }
            
            System.out.print("Opcion (1-" + categoriasRaiz.size() + "): ");
            
            try {
                int opcionCategoria = Integer.parseInt(scanner.nextLine().trim());
                
                if (opcionCategoria >= 1 && opcionCategoria <= categoriasRaiz.size()) {
                    categoriaSeleccionada = categoriasRaiz.get(opcionCategoria - 1);
                } else {
                    System.out.println("Error: Opcion invlida.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero valido.");
                return;
            }
            
            boolean esFiltro = false;
            while (true) {
                System.out.print("Es pregunta de filtro? (true/false): ");
                String filtroStr = scanner.nextLine().trim().toLowerCase();
                if (filtroStr.equals("true")) {
                    esFiltro = true;
                    break;
                } else if (filtroStr.equals("false")) {
                    esFiltro = false;
                    break;
                } else {
                    System.out.println("Error: Solo se admite 'true' o 'false'. Intente nuevamente.");
                }
            }
         
            existente.setIdCategoria(categoriaSeleccionada.getIdCategoria());
            existente.setFiltro(esFiltro);
            preguntaDAO.editarPregunta(existente);
            System.out.println("Pregunta ID " + id + " editada correctamente en Encuesta ID " + idEncuesta);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void eliminarPregunta() {
        System.out.print("ID de la pregunta a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        preguntaDAO.eliminarPregunta(id);
        System.out.println("Pregunta ID " + id + " eliminada.");
    }

    public static void modificarVerTipo() {
        System.out.println("Seleccione el tipo a filtrar:");
        for (TipoPregunta tipo : TipoPregunta.values()) {
            System.out.println(tipo.getCodigo() + ". " + tipo.getDescripcion());
        }
        
        TipoPregunta tipo = null;
        try {
            System.out.print("Opcion (1-4): ");
            int opcionTipo = Integer.parseInt(scanner.nextLine().trim());
            tipo = TipoPregunta.desdeCodigo(opcionTipo);
            if (tipo == null) {
                System.out.println("Error: Opcion invalida.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Opcion invalida.");
            return;
        }
        
        ListaPregunta lista = preguntaDAO.obtenerPorTipo(tipo.name());
        if (lista.getCabeza() == null) {
            System.out.println("No hay preguntas de ese tipo.");
            return;
        }
        
        System.out.println("\n=== Preguntas tipo " + tipo.getDescripcion() + " ===");
        NodoPregunta actual = lista.getCabeza();
        while (actual != null) {
            Pregunta p = actual.getPregunta();
            System.out.println("ID: " + p.getIdPregunta() + " | Texto: " + p.getTexto());
            actual = actual.getSiguiente();
        }
    }

    public static void VerPorCategoria() {
        System.out.print("Ingrese la categora a filtrar: ");
        String categoria = scanner.nextLine().trim();
        
        ListaPregunta lista = preguntaDAO.obtenerPorCategoria(categoria);
        if (lista.getCabeza() == null) {
            System.out.println("No hay preguntas en esta categoria.");
            return;
        }
        
        System.out.println("\n=== Preguntas en categora " + categoria + " ===");
        NodoPregunta actual = lista.getCabeza();
        while (actual != null) {
            Pregunta p = actual.getPregunta();
            System.out.println("ID: " + p.getIdPregunta() + " | Texto: " + p.getTexto());
            actual = actual.getSiguiente();
        }
    }

    public static void modificarTipoFiltro() {
        System.out.print("ID de la pregunta a marcar como filtro: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        preguntaDAO.marcarComoFiltro(id);
        System.out.println("Pregunta ID " + id + " marcada como filtro.");
    }

    public static void verPreguntas() {
        ListaPregunta lista = preguntaDAO.obtenerTodas();
        NodoPregunta actual = lista.getCabeza();
        
        if (actual == null) {
            System.out.println("No hay preguntas registradas.");
            return;
        }

        System.out.println("\n=== Lista de preguntas registradas ===");
        while (actual != null) {
            Pregunta p = actual.getPregunta();
            System.out.println("ID: " + p.getIdPregunta());
            System.out.println("Texto: " + p.getTexto());
            System.out.println("Tipo: " + p.getTipo().getDescripcion());
            System.out.println("Categoria: " + p.getCategoria());
            System.out.println("Filtro: " + (p.isFiltro() ? "Si" : "No"));
            System.out.println("Estado: " + (p.isActiva() ? "Activa" : "Inactiva"));
            System.out.println("--------------------------------------");
            actual = actual.getSiguiente();
        }
    }

    public static void agregarCategoria() {
        System.out.println("Nombre de la nueva categoria: ");
        String nombre = scanner.nextLine().trim();
        NodoCategoria nueva = new NodoCategoria(nombre);
        arbolCategorias.getRaiz().agregarSubcategoria(nueva);
        System.out.println("Categoria agregada");
    }

    public static void agregarSubcategoria() {
        try {
            System.out.print("Nombre de la categoria padre: ");
            String padre = scanner.nextLine().trim();
            System.out.print("Nombre de la subcategoria: ");
            String sub = scanner.nextLine().trim();

            NodoCategoria nodoPadre = buscarNodo(arbolCategorias.getRaiz(), padre);
            if (nodoPadre != null) {
                nodoPadre.agregarSubcategoria(new NodoCategoria(sub));
                System.out.println("Subcategoria agregada.");
            } else {
                System.out.println("Categoria padre no encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static NodoCategoria buscarNodo(NodoCategoria actual, String nombre) {
        if (actual.getNombre().equalsIgnoreCase(nombre)) {
            return actual;
        }
        for (NodoCategoria sub : actual.getSubcategorias()) {
            NodoCategoria encontrado = buscarNodo(sub, nombre);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    public static void mostrarCategorias() {
        System.out.println("\n=== ARBOL DE CATEGORIAS ===");
        arbolCategorias.mostrar();
    }
}
