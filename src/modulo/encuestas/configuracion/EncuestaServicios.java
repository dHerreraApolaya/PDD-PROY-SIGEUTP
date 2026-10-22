package modulo.encuestas.configuracion;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import modulo.participantes.Participante;
import modulo.preguntas.PreguntaDAO;
import modulo.preguntas.ListaPregunta;
import modulo.preguntas.NodoPregunta;

public class EncuestaServicios {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EncuestaDAO encuestaDAO = EncuestaDAO.getInstancia();
    private static final PreguntaDAO preguntaDAO = PreguntaDAO.getInstancia();

    public static void registrarEncuesta() {
        try {
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) {
                System.out.println("Error: El titulo no puede estar vacio.");
                return;
            }
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine().trim();
            if (descripcion.isEmpty()) {
                System.out.println("Error: La descripcion no puede estar vacia.");
                return;
            }
            System.out.println("Seleccione el estado de la encuesta:");
            System.out.println("1. ACTIVA");
            System.out.println("2. INACTIVA");
            System.out.print("Opcion: ");
            int opcionEstado;
            try {
                opcionEstado = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero valido (1 o 2).");
                return;
            }
            String estado;
            if (opcionEstado == 1) {
                estado = "ACTIVA";
            } else if (opcionEstado == 2) {
                estado = "INACTIVA";
            } else {
                System.out.println("Error: Opcion invalida, ingrese 1 (ACTIVA) o 2 (INACTIVA).");
                return;
            }
            System.out.println("Seleccione el tipo de perfil que crea la encuesta:");
            System.out.println("1. Administrador");
            System.out.println("2. Auditor");
            System.out.print("Opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();
            String perfilCreador;
            switch (opcion) {
                case 1:
                    perfilCreador = "Administrador";
                    break;
                case 2:
                    perfilCreador = "Auditor";
                    break;
                default:
                    System.out.println("Error: Solo los perfiles Administrador o Auditor pueden crear encuestas.");
                    return;
            }
            
                      System.out.println("\n=== CONFIGURAR PERFIL DE PARTICIPANTE (OBLIGATORIO) ===");
            
            PerfilParticipante perfilParticipante = new PerfilParticipante();
            
            System.out.println("\n--- Configuracion de edad ---");
            System.out.print("Deseas establecer edad minima? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.print("Edad minima (0-120): ");
                try {
                    int edadMin = Integer.parseInt(scanner.nextLine().trim());
                    if (edadMin >= 0 && edadMin <= 120) {
                        perfilParticipante.setEdadMinima(edadMin);
                    } else {
                        System.out.println("Edad fuera de rango, se omite");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se omite");
                }
            }
            
            System.out.print("Deseas establecer edad maxima? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.print("Edad maxima (0-120): ");
                try {
                    int edadMax = Integer.parseInt(scanner.nextLine().trim());
                    if (edadMax >= 0 && edadMax <= 120) {
                        perfilParticipante.setEdadMaxima(edadMax);
                    } else {
                        System.out.println("Edad fuera de rango, se omite");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se omite");
                }
            }
            
            System.out.println("\n--- Configuracion de sexo ---");
            System.out.print("Deseas especificar sexo permitido? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.println("1. Masculino");
                System.out.println("2. Femenino");
                System.out.println("3. Todos");
                System.out.print("Opcion: ");
                int opcionSexo;
                try {
                    opcionSexo = Integer.parseInt(scanner.nextLine().trim());
                    switch (opcionSexo) {
                        case 1:
                            perfilParticipante.setSexoPermitido("Masculino");
                            break;
                        case 2:
                            perfilParticipante.setSexoPermitido("Femenino");
                            break;
                        case 3:
                            perfilParticipante.setSexoPermitido("Todos");
                            break;
                        default:
                            System.out.println("Opcion invalida, se omite");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se omite");
                }
            }

         
            System.out.println("\n--- Configuracion de grupos de edad ---");
            System.out.print("Deseas especificar grupos de edad permitidos? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.println("Grupos disponibles:");
                System.out.println("1. Nino(0-11)");
                System.out.println("2. Adolescente(12-17)");
                System.out.println("3. Joven(18-29)");
                System.out.println("4. Adulto(30-59)");
                System.out.println("5. Adulto Mayor(60+)");
                System.out.println("Ingrese numeros separados por coma (ej: 1,3,5 para Ninos, Jovenes y Mayores): ");
                String gruposInput = scanner.nextLine().trim();
                if (!gruposInput.isEmpty()) {
                    perfilParticipante.setGruposEdadPermitidos(gruposInput);
                }
            }

            System.out.println("\n--- Configuracion de estado civil ---");
            System.out.print("Deseas especificar estado civil permitido? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.println("1. Soltero");
                System.out.println("2. Casado");
                System.out.println("3. Separado");
                System.out.println("4. Divorciado");
                System.out.println("5. Viudo");
                System.out.println("6. Todos");
                System.out.print("Opcion: ");
                int opcionEstCivil;
                try {
                    opcionEstCivil = Integer.parseInt(scanner.nextLine().trim());
                    switch (opcionEstCivil) {
                        case 1:
                            perfilParticipante.setEstadoCivilPermitido("Soltero");
                            break;
                        case 2:
                            perfilParticipante.setEstadoCivilPermitido("Casado");
                            break;
                        case 3:
                            perfilParticipante.setEstadoCivilPermitido("Separado");
                            break;
                        case 4:
                            perfilParticipante.setEstadoCivilPermitido("Divorciado");
                            break;
                        case 5:
                            perfilParticipante.setEstadoCivilPermitido("Viudo");
                            break;
                        case 6:
                            perfilParticipante.setEstadoCivilPermitido("Todos");
                            break;
                        default:
                            System.out.println("Opcion invalida, se omite");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se omite");
                }
            }
            
            System.out.println("\n--- Configuracion de sueldo minimo ---");
            System.out.print("Deseas establecer sueldo minimo? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.print("Sueldo minimo: ");
                try {
                    double sueldoMin = Double.parseDouble(scanner.nextLine().trim());
                    if (sueldoMin >= 0) {
                        perfilParticipante.setSueldoMinimo(sueldoMin);
                    } else {
                        System.out.println("Sueldo invalido, se omite");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se omite");
                }
            }
            
            System.out.println("\n--- Configuracion de ubicaciones permitidas ---");
            System.out.print("Deseas especificar ubicaciones permitidas? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                List<String> ubicaciones = new ArrayList<>();
                System.out.println("Ingresa ubicaciones separadas por coma (ej: Lima, Arequipa, Cusco):");
                String ubicacionesInput = scanner.nextLine().trim();
                if (!ubicacionesInput.isEmpty()) {
                    String[] ubicacionesArray = ubicacionesInput.split(",");
                    for (String ubicacion : ubicacionesArray) {
                        ubicaciones.add(ubicacion.trim());
                    }
                    perfilParticipante.setUbicacionesPermitidas(ubicaciones);
                }
            }
           
            Encuesta encuesta = new Encuesta(titulo, descripcion, estado, perfilCreador);
            encuesta.setPerfilParticipante(perfilParticipante);
            
            encuestaDAO.registrarEncuesta(encuesta);
            System.out.println("\nEncuesta registrada correctamente por perfil: " + perfilCreador);
            System.out.println("ID asignado: " + encuesta.getIdEncuesta());
            
                       System.out.println("  PERFIL DE PARTICIPANTE CONFIGURADO             ");
                     System.out.println("Edad minima:        " + (perfilParticipante.getEdadMinima() != null ? perfilParticipante.getEdadMinima() : "No especificada"));
            System.out.println("Edad maxima:        " + (perfilParticipante.getEdadMaxima() != null ? perfilParticipante.getEdadMaxima() : "No especificada"));
            System.out.println("Grupos de edad:     " + (perfilParticipante.getGruposEdadPermitidos() != null && !perfilParticipante.getGruposEdadPermitidos().isEmpty() ? 
                    perfilParticipante.getGruposEdadPermitidos() : "Todos"));
            System.out.println("Sexo permitido:     " + (perfilParticipante.getSexoPermitido() != null ? perfilParticipante.getSexoPermitido() : "No especificado"));
            System.out.println("Estado civil:       " + (perfilParticipante.getEstadoCivilPermitido() != null ? perfilParticipante.getEstadoCivilPermitido() : "No especificado"));
            System.out.println("Sueldo minimo:      " + (perfilParticipante.getSueldoMinimo() != null ? perfilParticipante.getSueldoMinimo() : "No especificado"));
            System.out.println("Ubicaciones:        " + (perfilParticipante.getUbicacionesPermitidas() != null && !perfilParticipante.getUbicacionesPermitidas().isEmpty() ? 
                    String.join(", ", perfilParticipante.getUbicacionesPermitidas()) : "No especificadas"));
                        
            EncuestaController.agregarEncuesta(encuesta);
        } catch (Exception e) {
            System.out.println("Error al registrar encuesta: " + e.getMessage());
        }
    }

    public static void editarEncuesta() {
        try {
            System.out.print("ID de la encuesta a editar: ");
            int id = scanner.nextInt();
            scanner.nextLine();
       
            EncuestaDAO dao = EncuestaDAO.getInstancia();
            Encuesta encuestaActual = dao.buscarPorId(id);
            
            if (encuestaActual == null) {
                System.out.println("Error: Encuesta no encontrada.");
                return;
            }
            
            System.out.println("\n=== EDITAR ENCUESTA ID " + id + " ===");
            System.out.println("Informacion actual:");
            System.out.println("Titulo: " + encuestaActual.getTitulo());
            System.out.println("Descripcion: " + encuestaActual.getDescripcion());
            System.out.println("Estado: " + encuestaActual.getEstado());
          
            System.out.print("\nDeseas editar el titulo? (s/n): ");
            String titulo = encuestaActual.getTitulo();
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.print("Nuevo titulo: ");
                titulo = scanner.nextLine().trim();
                if (titulo.isEmpty()) {
                    System.out.println("Titulo no valido, se mantiene el anterior.");
                    titulo = encuestaActual.getTitulo();
                }
            }
            
            System.out.print("Deseas editar la descripcion? (s/n): ");
            String descripcion = encuestaActual.getDescripcion();
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.print("Nueva descripcion: ");
                descripcion = scanner.nextLine().trim();
                if (descripcion.isEmpty()) {
                    System.out.println("Descripcion no valida, se mantiene la anterior.");
                    descripcion = encuestaActual.getDescripcion();
                }
            }
            
            System.out.print("Deseas editar el estado? (s/n): ");
            String estado = encuestaActual.getEstado();
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.println("1. ACTIVA");
                System.out.println("2. INACTIVA");
                System.out.print("Opcion: ");
                int opcionEstado;
                try {
                    opcionEstado = Integer.parseInt(scanner.nextLine().trim());
                    if (opcionEstado == 1) {
                        estado = "ACTIVA";
                    } else if (opcionEstado == 2) {
                        estado = "INACTIVA";
                    } else {
                        System.out.println("Opcion invalida, se mantiene el estado anterior.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida, se mantiene el estado anterior.");
                }
            }
            
              System.out.println("\n=== EDITAR PERFIL DE PARTICIPANTE ===");
            
            PerfilParticipante perfilActual = encuestaActual.getPerfilParticipante();
            PerfilParticipante perfilNuevo = new PerfilParticipante();
            
                     if (perfilActual != null) {
                if (perfilActual.getEdadMinima() != null) {
                    perfilNuevo.setEdadMinima(perfilActual.getEdadMinima());
                }
                if (perfilActual.getEdadMaxima() != null) {
                    perfilNuevo.setEdadMaxima(perfilActual.getEdadMaxima());
                }
                if (perfilActual.getSexoPermitido() != null) {
                    perfilNuevo.setSexoPermitido(perfilActual.getSexoPermitido());
                }
                if (perfilActual.getSueldoMinimo() != null) {
                    perfilNuevo.setSueldoMinimo(perfilActual.getSueldoMinimo());
                }
                if (perfilActual.getUbicacionesPermitidas() != null) {
                    perfilNuevo.setUbicacionesPermitidas(perfilActual.getUbicacionesPermitidas());
                }
            }
            
            System.out.print("Deseas editar el perfil de participante? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                System.out.println("\n--- Configuracion de edad ---");
                System.out.print("Deseas establecer/modificar edad minima? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                    System.out.print("Edad minima (0-120): ");
                    try {
                        int edadMin = Integer.parseInt(scanner.nextLine().trim());
                        if (edadMin >= 0 && edadMin <= 120) {
                            perfilNuevo.setEdadMinima(edadMin);
                        } else {
                            System.out.println("Edad fuera de rango, se omite");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada invalida, se omite");
                    }
                }
                
                System.out.print("Deseas establecer/modificar edad maxima? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                    System.out.print("Edad maxima (0-120): ");
                    try {
                        int edadMax = Integer.parseInt(scanner.nextLine().trim());
                        if (edadMax >= 0 && edadMax <= 120) {
                            perfilNuevo.setEdadMaxima(edadMax);
                        } else {
                            System.out.println("Edad fuera de rango, se omite");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada invalida, se omite");
                    }
                }
                
                System.out.println("\n--- Configuracion de sexo ---");
                System.out.print("Deseas establecer/modificar sexo permitido? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                    System.out.println("1. Masculino");
                    System.out.println("2. Femenino");
                    System.out.println("3. Todos");
                    System.out.print("Opcion: ");
                    int opcionSexo;
                    try {
                        opcionSexo = Integer.parseInt(scanner.nextLine().trim());
                        switch (opcionSexo) {
                            case 1:
                                perfilNuevo.setSexoPermitido("Masculino");
                                break;
                            case 2:
                                perfilNuevo.setSexoPermitido("Femenino");
                                break;
                            case 3:
                                perfilNuevo.setSexoPermitido("Todos");
                                break;
                            default:
                                System.out.println("Opcion invalida, se omite");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada invalida, se omite");
                    }
                }
                
                System.out.println("\n--- Configuracion de sueldo minimo ---");
                System.out.print("Deseas establecer/modificar sueldo minimo? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                    System.out.print("Sueldo minimo: ");
                    try {
                        double sueldoMin = Double.parseDouble(scanner.nextLine().trim());
                        if (sueldoMin >= 0) {
                            perfilNuevo.setSueldoMinimo(sueldoMin);
                        } else {
                            System.out.println("Sueldo invalido, se omite");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada invalida, se omite");
                    }
                }
                
                System.out.println("\n--- Configuracion de ubicaciones permitidas ---");
                System.out.print("Deseas establecer/modificar ubicaciones permitidas? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                    List<String> ubicaciones = new ArrayList<>();
                    System.out.println("Ingresa ubicaciones separadas por coma (ej: Lima, Arequipa, Cusco):");
                    String ubicacionesInput = scanner.nextLine().trim();
                    if (!ubicacionesInput.isEmpty()) {
                        String[] ubicacionesArray = ubicacionesInput.split(",");
                        for (String ubicacion : ubicacionesArray) {
                            ubicaciones.add(ubicacion.trim());
                        }
                        perfilNuevo.setUbicacionesPermitidas(ubicaciones);
                    }
                }
            }
           
            encuestaActual.setTitulo(titulo);
            encuestaActual.setDescripcion(descripcion);
            encuestaActual.setEstado(estado);
            encuestaActual.setPerfilParticipante(perfilNuevo);
            
            encuestaDAO.editarEncuesta(encuestaActual);
            System.out.println("\n Encuesta editada correctamente.");
           
            System.out.println("\n=== PERFIL DE PARTICIPANTE ACTUALIZADO ===");
            System.out.println("Edad minima: " + (perfilNuevo.getEdadMinima() != null ? perfilNuevo.getEdadMinima() : "No especificada"));
            System.out.println("Edad maxima: " + (perfilNuevo.getEdadMaxima() != null ? perfilNuevo.getEdadMaxima() : "No especificada"));
            System.out.println("Sexo permitido: " + (perfilNuevo.getSexoPermitido() != null ? perfilNuevo.getSexoPermitido() : "No especificado"));
            System.out.println("Sueldo minimo: " + (perfilNuevo.getSueldoMinimo() != null ? perfilNuevo.getSueldoMinimo() : "No especificado"));
            System.out.println("Ubicaciones: " + (perfilNuevo.getUbicacionesPermitidas() != null && !perfilNuevo.getUbicacionesPermitidas().isEmpty() ? 
                    String.join(", ", perfilNuevo.getUbicacionesPermitidas()) : "No especificadas"));
            
        } catch (Exception e) {
            System.out.println("Error al editar encuesta: " + e.getMessage());
        }
    }

    public static void eliminarEncuesta() {
        try {
            System.out.print("ID de la encuesta a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            encuestaDAO.eliminarEncuesta(id);
            System.out.println("Encuesta eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar encuesta: " + e.getMessage());
        }
    }

    public static void listarEncuestas() {
        try {
            System.out.println("\n=== LISTADO DE ENCUESTAS ===");
            encuestaDAO.listarEncuestas();
        } catch (Exception e) {
            System.out.println("Error al listar encuestas: " + e.getMessage());
        }
    }

    public static void listarEncuestasPorFecha() {
        try {
            System.out.println("\n=== ENCUESTAS ORDENADAS POR FECHA DE CREACION ===");
            
            System.out.println("Como desea ordenar?");
            System.out.println("1. De mas reciente a mas antigua (descendente)");
            System.out.println("2. De mas antigua a mas reciente (ascendente)");
            System.out.print("Seleccione opcion (1-2): ");
            
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido (1 o 2).");
                return;
            }
            
            if (opcion < 1 || opcion > 2) {
                System.out.println("Error: Opcion invalida. Seleccione 1 o 2.");
                return;
            }
            
            String orden = (opcion == 1) ? "DESC" : "ASC";
            encuestaDAO.listarEncuestasPorFecha(orden);
            
        } catch (Exception e) {
            System.out.println("Error al listar encuestas por fecha: " + e.getMessage());
        }
    }

    public static void configurarPerfilParticipante() {
        try {
            System.out.print("ID de la encuesta: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Deseas configurar un perfil detallado? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();

            String tipo = "No especificado";
            String sexo = "No especificado";
            Integer edad = null;
            String grupoEdad = "No especificado";
            String nivelEducativo = "No especificado";

            if (respuesta.equals("s")) {
                System.out.print("Tipo de participante (Administrador/Encuestador/Participante): ");
                tipo = scanner.nextLine().trim();
                if (tipo.isEmpty()) {
                    tipo = "No especificado";
                }

                System.out.print("Sexo (Masculino/Femenino): ");
                sexo = scanner.nextLine().trim().toLowerCase();
                if (!sexo.equals("masculino") && !sexo.equals("femenino")) {
                    sexo = "No especificado";
                }

                System.out.print("Edad (0 a 120, o dejar vacio): ");
                String edadInput = scanner.nextLine().trim();
                if (!edadInput.isEmpty()) {
                    try {
                        edad = Integer.parseInt(edadInput);
                        if (edad < 0 || edad > 120) {
                            System.out.println("Edad invalida. Se omitira.");
                            edad = null;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Edad no valida. Se omitira.");
                    }
                }

                if (edad != null) {
                    if (edad <= 11) {
                        grupoEdad = "0-11 anios";
                    } else if (edad <= 17) {
                        grupoEdad = "12-17 anios";
                    } else if (edad <= 29) {
                        grupoEdad = "18-29 anios";
                    } else if (edad <= 59) {
                        grupoEdad = "30-59 anios";
                    } else {
                        grupoEdad = "60 anios a mas";
                    }
                }

                System.out.print("Nivel educativo (Primaria/Secundaria/Superior, o dejar vacio): ");
                nivelEducativo = scanner.nextLine().trim();
                if (nivelEducativo.isEmpty()) {
                    nivelEducativo = "No especificado";
                }
            }

                     System.out.println("\nPerfil configurado para la encuesta ID " + id + ":");
            System.out.println("Tipo: " + tipo);
            System.out.println("Sexo: " + sexo);
            System.out.println("Edad: " + (edad != null ? edad : "No especificada"));
            System.out.println("Grupo de edad: " + grupoEdad);
            System.out.println("Nivel educativo: " + nivelEducativo);
        } catch (Exception e) {
            System.out.println("Error al configurar perfil: " + e.getMessage());
        }
    }

    public boolean puedeParticipar(Encuesta encuesta, Participante participante) {
        PerfilParticipante perfil = encuesta.getPerfilParticipante();
        if (perfil == null) {
            return true; 
        }
        return perfil.cumplePerfil(participante);
    }

    public void iniciarEncuesta(Encuesta encuesta, Participante participante) throws IllegalAccessException {
        if (!puedeParticipar(encuesta, participante)) {
            throw new IllegalAccessException("El participante no cumple con el perfil definido para esta encuesta.");
        }
    }

    public static void visualizarEncuestaConPreguntas() {
        try {
            System.out.print("ID de la encuesta a visualizar: ");
            int idEncuesta = scanner.nextInt();
            scanner.nextLine();

            Encuesta encuesta = encuestaDAO.buscarPorId(idEncuesta);
            if (encuesta == null) {
                System.out.println("Error: Encuesta no encontrada.");
                return;
            }

            System.out.println("\n=== DETALLES DE LA ENCUESTA ===");
            System.out.println("ID: " + encuesta.getIdEncuesta());
            System.out.println("Titulo: " + encuesta.getTitulo());
            System.out.println("Descripcion: " + encuesta.getDescripcion());
            System.out.println("Estado: " + encuesta.getEstado());
            System.out.println("Perfil Creador: " + encuesta.getPerfilCreador());
            System.out.println("\n=== PREGUNTAS ASOCIADAS ===");

      
            ListaPregunta preguntasEncuesta = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
            
            if (preguntasEncuesta == null || preguntasEncuesta.getCabeza() == null) {
                System.out.println("No hay preguntas asociadas a esta encuesta.");
            } else {
                int contador = 1;
                NodoPregunta nodo = preguntasEncuesta.getCabeza();
                while (nodo != null) {
                    System.out.println(contador + ". " + nodo.getPregunta().getTexto() + 
                    " (ID: " + nodo.getPregunta().getIdPregunta() + ")");
                    contador++;
                    nodo = nodo.getSiguiente();
                }
            }

        } catch (Exception e) {
            System.out.println("Error al visualizar encuesta: " + e.getMessage());
        }
    }
}
