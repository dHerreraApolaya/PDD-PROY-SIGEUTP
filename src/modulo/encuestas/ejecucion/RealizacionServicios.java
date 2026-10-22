package modulo.encuestas.ejecucion;

import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import modulo.encuestas.configuracion.Encuesta;
import modulo.encuestas.configuracion.EncuestaDAO;
import modulo.encuestas.configuracion.PerfilParticipante;
import modulo.participantes.Participante;
import modulo.participantes.ParticipanteDAO;
import modulo.preguntas.ListaPregunta;
import modulo.preguntas.NodoPregunta;
import modulo.preguntas.Pregunta;
import modulo.preguntas.PreguntaDAO;
import modulo.preguntas.TipoPregunta;
import modulo.respuestas.ListaRespuesta;
import modulo.respuestas.Respuesta;
import modulo.respuestas.RespuestaDAO;

public class RealizacionServicios {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PreguntaDAO preguntaDAO = PreguntaDAO.getInstancia();
    private static final EncuestaDAO encuestaDAO = EncuestaDAO.getInstancia();
    private static final ParticipanteDAO participanteDAO = new ParticipanteDAO();
    private static final RespuestaDAO respuestaDAO = new RespuestaDAO(preguntaDAO);
    private static final Stack<Respuesta> pilaRespuestas = new Stack<>();
    private static final Stack<Pregunta> pilaPreguntas = new Stack<>();

    public static void ejecutarEncuesta() {
        System.out.println("\n=== EJECUCION DE ENCUESTA ===");
        
        System.out.print("Ingrese el ID del participante: ");
        int idParticipante;
        try {
            idParticipante = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un numero valido.");
            return;
        }
    
        Participante participante = participanteDAO.obtenerParticipantePorId(idParticipante);
        if (participante == null) {
            System.out.println("Error: No se encontro participante con ID " + idParticipante);
            return;
        }
        
        System.out.println(" Participante encontrado: " + participante.getNombre());
        System.out.println("  Sexo: " + participante.getSexo());
        System.out.println("  Perfil: " + participante.getPerfil());
        
        List<Encuesta> encuestas = encuestaDAO.listarEncuestasActivas();
        if (encuestas.isEmpty()) {
            System.out.println("No hay encuestas disponibles.");
            return;
        }
        
        List<Encuesta> encuestasValidas = new ArrayList<>();
        for (Encuesta e : encuestas) {
            if (validarAccesoEncuesta(participante, e)) {
                encuestasValidas.add(e);
            }
        }
        
        if (encuestasValidas.isEmpty()) {
            System.out.println("\n No hay encuestas disponibles para tu perfil (" + 
                             participante.getPerfil() + ")");
            System.out.println("Espera a que se creen encuestas compatibles con tu perfil.");
            return;
        }
        
        System.out.println("\n Encuestas disponibles para tu perfil:");
        for (int i = 0; i < encuestasValidas.size(); i++) {
            Encuesta e = encuestasValidas.get(i);
            System.out.println((i + 1) + ". " + e.getTitulo() + " (ID: " + e.getIdEncuesta() + ")");
        }
        
        System.out.print("Selecciona una encuesta: ");
        try {
            int seleccion = Integer.parseInt(scanner.nextLine().trim());
            if (seleccion < 1 || seleccion > encuestasValidas.size()) {
                System.out.println("Seleccion invalida.");
                return;
            }
            
            Encuesta encuestaActual = encuestasValidas.get(seleccion - 1);
            
            boolean yaRespondio = verificarSiYaRespondio(idParticipante, encuestaActual.getIdEncuesta());
            
            if (yaRespondio) {
                System.out.println("\n YA HAS RESPONDIDO ESTA ENCUESTA");
                System.out.println("Que deseas hacer?");
                System.out.println("1. Actualizar tus respuestas");
                System.out.println("2. Ver tus respuestas anteriores");
                System.out.println("3. Volver atras");
                System.out.print("Selecciona opcion: ");
                
                try {
                    int opcionDuplicado = Integer.parseInt(scanner.nextLine().trim());
                    
                    switch (opcionDuplicado) {
                        case 1:
                            actualizarRespuestasEncuesta(idParticipante, encuestaActual);
                            return;
                        case 2:
                            mostrarRespuestasAnteriores(idParticipante, encuestaActual.getIdEncuesta());
                            return;
                        case 3:
                            System.out.println("Volviendo atras...");
                            return;
                        default:
                            System.out.println("Opcion invalida");
                            return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Ingresa un numero valido.");
                    return;
                }
            }
            
            System.out.println("\nEncuesta seleccionada: " + encuestaActual.getTitulo());
            System.out.println("Descripcion: " + encuestaActual.getDescripcion());

            ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(encuestaActual.getIdEncuesta());
            NodoPregunta actual = preguntas.getCabeza();

            int respuestasCount = 0;

            while (actual != null) {
                Pregunta p = actual.getPregunta();
               
                if (p.getOpciones() == null || p.getOpciones().length == 0) {
                    p.generarOpcionesSegunTipo();
                }
                
                pilaPreguntas.push(p);

                System.out.println("\n" + p.getTexto());
                
                if (p.getTipo() == TipoPregunta.DICOTOMICA || p.getTipo() == TipoPregunta.ESCALA_VALORACION) {
                    String[] opciones = p.getOpciones();
                    for (int i = 0; i < opciones.length; i++) {
                        System.out.println((i + 1) + ". " + opciones[i]);
                    }
                } else if (p.getTipo() == TipoPregunta.RESPUESTA_SIMPLE) {
                    System.out.println("(Ingrese su respuesta de texto)");
                } else if (p.getTipo() == TipoPregunta.NUMERICA) {
                    System.out.println("(Ingrese un numero)");
                }

                LanzadorTemporizador.iniciarTemporizadorEnThread(180);

                System.out.print("Respuesta (o 'd' para deshacer, 'b' para volver): ");
                String entrada = scanner.nextLine().trim();

                if (entrada.equalsIgnoreCase("d")) {
                    if (!pilaRespuestas.isEmpty()) {
                        Respuesta deshecha = pilaRespuestas.pop();
                        pilaPreguntas.pop();
                        System.out.println("Respuesta eliminada: " + deshecha.getContenido());
                        respuestasCount--;

                        if (pilaPreguntas.isEmpty()) {
                            actual = preguntas.getCabeza();
                        } else {
                            actual = buscarNodoPorPregunta(preguntas, pilaPreguntas.peek()).getSiguiente();
                        }
                    } else {
                        System.out.println("No hay respuestas para deshacer.");
                    }
                    continue;
                }

                if (entrada.equalsIgnoreCase("b")) {
                    if (pilaPreguntas.size() > 1) {
                        pilaPreguntas.pop();
                        if (!pilaRespuestas.isEmpty()) {
                            pilaRespuestas.pop();
                            respuestasCount--;
                        }
                        actual = buscarNodoPorPregunta(preguntas, pilaPreguntas.peek());
                    } else {
                        System.out.println("Ya estas en la primera pregunta.");
                    }
                    continue;
                }

                try {
               
                    String contenidoRespuesta = validarRespuestaSegunTipo(p, entrada);
                    
                    if (contenidoRespuesta == null) {
                    
                        continue;
                    }

                    Respuesta respuesta = new Respuesta();
                    respuesta.setIdEncuesta(encuestaActual.getIdEncuesta());
                    respuesta.setIdPregunta(p.getIdPregunta());
                    respuesta.setContenido(contenidoRespuesta);
                    respuesta.setIdParticipante(participante.getId_participante());
                    respuesta.setFechaRespuesta(java.time.LocalDateTime.now());

                    pilaRespuestas.push(respuesta);
                    respuestaDAO.registrarRespuesta(respuesta);
                    respuestasCount++;
                   
                    pilaPreguntas.pop();
                    actual = actual.getSiguiente();

                } catch (Exception e) {
                    System.out.println("Error procesando respuesta: " + e.getMessage());
                    continue;
                }
            }

            System.out.println("\n Encuesta completada!");
            System.out.println("Se registraron " + respuestasCount + " respuestas.");
            
           
            LanzadorTemporizador.detenerTemporizador();
            
            pilaRespuestas.clear();
            pilaPreguntas.clear();
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un numero valido.");
            LanzadorTemporizador.detenerTemporizador();
        } finally {
  
            LanzadorTemporizador.detenerTemporizador();
        }
    }

    private static NodoPregunta buscarNodoPorPregunta(ListaPregunta lista, Pregunta preguntaBuscada) {
        NodoPregunta actual = lista.getCabeza();
        while (actual != null) {
            if (actual.getPregunta().getIdPregunta() == preguntaBuscada.getIdPregunta()) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public static void registrarRespuestas() {
    System.out.print("ID de la encuesta: ");
    int idEncuesta = scanner.nextInt();
    scanner.nextLine();
        System.out.print("ID de la pregunta: ");
    int idPregunta = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Texto de la respuesta: ");
    String texto = scanner.nextLine();

    Respuesta respuesta = new Respuesta();
    respuesta.setIdEncuesta(idEncuesta); 
    respuesta.setIdPregunta(idPregunta);
    respuesta.setContenido(texto);
    respuesta.setIdParticipante(1); 
    respuesta.setFechaRespuesta(java.time.LocalDateTime.now());

    respuestaDAO.registrarRespuesta(respuesta);
    System.out.println("Respuesta registrada en Encuesta ID " + idEncuesta);
}

    public static void verPerfilParticipanteEncuestas() {
        System.out.println("\n=== VER PERFIL REQUERIDO PARA ENCUESTA ===");
        
        System.out.print("Ingrese ID de la encuesta: ");
        int idEncuesta;
        try {
            idEncuesta = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un numero valido.");
            return;
        }
        
        EncuestaDAO encuestaDAO = EncuestaDAO.getInstancia();
        Encuesta encuesta = null;
        List<Encuesta> todas = encuestaDAO.listarEncuestasActivas();
        for (Encuesta e : todas) {
            if (e.getIdEncuesta() == idEncuesta) {
                encuesta = e;
                break;
            }
        }
        
        if (encuesta == null) {
            System.out.println("Error: No se encontro encuesta con ID " + idEncuesta);
            return;
        }
        
        System.out.println("\n=== INFORMACION DE LA ENCUESTA ===");
        System.out.println("Ti�tulo: " + encuesta.getTitulo());
        System.out.println("ID: " + encuesta.getIdEncuesta());
        System.out.println("Descripcion: " + encuesta.getDescripcion());
        System.out.println("Estado: " + encuesta.getEstado());
        
        PerfilParticipante perfilRequerido = encuesta.getPerfilParticipante();
        
        System.out.println("\n=== PERFIL REQUERIDO PARA HACER ESTA ENCUESTA ===");
        if (perfilRequerido == null) {
            System.out.println("PERFIL ABIERTO: Todos pueden hacer esta encuesta");
        } else {
            System.out.println("Requisitos que debe cumplir:");
            
            if (perfilRequerido.getEdadMinima() != null) {
                System.out.println("  Edad minima: " + perfilRequerido.getEdadMinima() + " anios");
            }
            if (perfilRequerido.getEdadMaxima() != null) {
                System.out.println("   Edad maxima: " + perfilRequerido.getEdadMaxima() + " anios");
            }
            if (perfilRequerido.getSexoPermitido() != null) {
                String sexo = perfilRequerido.getSexoPermitido();
                if (sexo.equalsIgnoreCase("Ambos")) {
                    System.out.println("  Sexo requerido: Masculino y Femenino");
                } else if (!sexo.equalsIgnoreCase("Todos")) {
                    System.out.println("  Sexo requerido: " + sexo);
                }
            }
            if (perfilRequerido.getEstadoCivilPermitido() != null && 
                !perfilRequerido.getEstadoCivilPermitido().equalsIgnoreCase("Todos")) {
                System.out.println("  Estado civil: " + perfilRequerido.getEstadoCivilPermitido());
            }
            if (perfilRequerido.getSueldoMinimo() != null) {
                System.out.println("   Sueldo minimo: " + perfilRequerido.getSueldoMinimo());
            }
            if (perfilRequerido.getUbicacionesPermitidas() != null && 
                !perfilRequerido.getUbicacionesPermitidas().isEmpty()) {
                System.out.println("  Ubicaciones permitidas: " + String.join(", ", perfilRequerido.getUbicacionesPermitidas()));
            }
            if (perfilRequerido.getGruposEdadPermitidos() != null && 
                !perfilRequerido.getGruposEdadPermitidos().isEmpty()) {
                System.out.println("  Grupos de edad permitidos: " + perfilRequerido.getGruposEdadPermitidos());
            }
        }
    }


    public static void controlarTiempo() {
        System.out.print("Ingrese tiempo limite en segundos: ");
        int segundos = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Temporizador iniciado...");
        try {
            for (int i = segundos; i > 0; i--) {
                System.out.println("Tiempo restante: " + i + " segundos\r");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Temporizador interrumpido.");
        }
        System.out.println("\nTiempo finalizado.");
    }

    public static void temporizadorSwing() {
        System.out.print("Ingrese duracion del temporizador en minutos (default 3): ");
        String entrada = scanner.nextLine().trim();
        int minutos = 3;
        
        if (!entrada.isEmpty()) {
            try {
                minutos = Integer.parseInt(entrada);
                if (minutos <= 0) {
                    System.out.println("Usando valor por defecto: 3 minutos");
                    minutos = 3;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Usando valor por defecto: 3 minutos");
                minutos = 3;
            }
        }
        
        int segundosTotales = minutos * 60;
        
        System.out.println("\n  Temporizador iniciado: " + minutos + " minuto(s) (" + segundosTotales + " segundos)");
        System.out.println("===============================================================?");
        
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            int contador = segundosTotales;

            @Override
            public void run() {
                if (contador > 0) {
                    int minutosRestantes = contador / 60;
                    int segundosRestantes = contador % 60;
                    
                    if (contador <= 30) {
                        System.out.println("  TIEMPO RESTANTE: " + minutosRestantes + ":" + 
                        String.format("%02d", segundosRestantes) + " seg");
                    } else {
                        System.out.println("�?� Tiempo restante: " + minutosRestantes + ":" + 
                        String.format("%02d", segundosRestantes) + " seg");
                    }
                    contador--;
                } else {
                    System.out.println("\n=======================================================");
                    System.out.println(" TIEMPO AGOTADO! Finalizar respuesta.");
                    System.out.println("=========================================================");
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }
   
    public static java.util.Timer iniciarTemporizadorEncuesta(int minutosPorPregunta) {
        int segundosTotales = minutosPorPregunta * 60;
        
        System.out.println("  Tiempo disponible: " + minutosPorPregunta + " minuto(s)");
        
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            int contador = segundosTotales;

            @Override
            public void run() {
                if (contador > 0) {
                    if (contador == 60) {
                        System.out.println("\n  AVISO: Queda 1 minuto");
                    } else if (contador == 30) {
                        System.out.println("\n  AVISO: Queda 30 segundos");
                    }
                    contador--;
                } else {
                    System.out.println("\n TIEMPO AGOTADO PARA ESTA PREGUNTA!");
                    timer.cancel();
                }
            }
        }, 0, 1000);
        
        return timer;
    }

    public static void mostrarHistorialRespuestas() {
        System.out.print("Ingrese el ID de la encuesta para ver su historial: ");
        int idEncuesta = scanner.nextInt();
        scanner.nextLine();

        ListaRespuesta historial = respuestaDAO.listarHistorialPorEncuesta(idEncuesta);

        if (historial.getCabeza() == null) {
            System.out.println("No hay respuestas registradas para la encuesta ID " 
                    + idEncuesta);
            return;
        }

        System.out.println("\n=== HISTORIAL DE RESPUESTAS DE ENCUESTA ID " 
                + idEncuesta + " ===");
        historial.mostrar();
    }

    private static boolean validarAccesoEncuesta(Participante participante, Encuesta encuesta) {
        
        PerfilParticipante perfilRequerido = encuesta.getPerfilParticipante();
        
        if (perfilRequerido == null) {
            return true;
        }
        
        return perfilRequerido.cumplePerfil(participante);
    }

    private static boolean verificarSiYaRespondio(int idParticipante, int idEncuesta) {
        ListaRespuesta respuestas = respuestaDAO.getListaRespuesta();
        
        if (respuestas == null || respuestas.getCabeza() == null) {
            return false; 
        }
        
        modulo.respuestas.NodoRespuesta nodo = respuestas.getCabeza();
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            if (r.getIdParticipante() == idParticipante && 
                r.getIdEncuesta() == idEncuesta) {
                return true; 
            }
            nodo = nodo.getSiguiente();
        }
        
        return false; 
    }

    private static void mostrarRespuestasAnteriores(int idParticipante, int idEncuesta) {
        System.out.println("\n=== TUS RESPUESTAS ANTERIORES ===");
        
        ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
        ListaRespuesta todasRespuestas = respuestaDAO.getListaRespuesta();
        
        if (preguntas == null || preguntas.getCabeza() == null) {
            System.out.println("Sin preguntas disponibles");
            return;
        }
        
        NodoPregunta nodoP = preguntas.getCabeza();
        int numero = 1;
        
        while (nodoP != null) {
            Pregunta p = nodoP.getPregunta();
        
            String respuestaAnterior = "[Sin respuesta]";
            
            if (todasRespuestas != null) {
                modulo.respuestas.NodoRespuesta nodoR = todasRespuestas.getCabeza();
                while (nodoR != null) {
                    Respuesta r = nodoR.getRespuesta();
                    if (r.getIdEncuesta() == idEncuesta &&
                        r.getIdPregunta() == p.getIdPregunta() &&
                        r.getIdParticipante() == idParticipante) {
                        respuestaAnterior = r.getContenido();
                        break;
                    }
                    nodoR = nodoR.getSiguiente();
                }
            }
            
            System.out.println(numero + ". " + p.getTexto());
            System.out.println("   Tu respuesta anterior: " + respuestaAnterior);
            
            numero++;
            nodoP = nodoP.getSiguiente();
        }
    }

    private static String validarRespuestaSegunTipo(Pregunta pregunta, String entrada) {
        if (entrada == null || entrada.isEmpty()) {
            System.out.println("�?� La respuesta no puede estar vaci�a");
            return null;
        }
        
        try {
            switch (pregunta.getTipo()) {
                case DICOTOMICA:
                    return validarDicotomica(pregunta, entrada);
                    
                case ESCALA_VALORACION:
                    return validarEscalaValoracion(pregunta, entrada);
                    
                case NUMERICA:
                    return validarNumerica(entrada);
                    
                case RESPUESTA_SIMPLE:
                    return validarRespuestaSimple(entrada);
                    
                default:
                    System.out.println(" Tipo de pregunta no soportado");
                    return null;
            }
        } catch (Exception e) {
            System.out.println(" Error validando respuesta: " + e.getMessage());
            return null;
        }
    }

    private static String validarDicotomica(Pregunta pregunta, String entrada) {
        try {
            int opcion = Integer.parseInt(entrada);
            
            if (opcion < 1 || opcion > 2) {
                System.out.println(" Opcion invalida. Ingresa 1 (Si�) o 2 (No)");
                return null;
            }
            
            String[] opciones = pregunta.getOpciones();
            if (opciones == null || opciones.length < opcion) {
                System.out.println(" Las opciones no estan disponibles");
                return null;
            }
            
            System.out.println("� Respuesta registrada: " + opciones[opcion - 1]);
            return opciones[opcion - 1];
            
        } catch (NumberFormatException e) {
            System.out.println(" Debes ingresar 1 (Si�) o 2 (No)");
            return null;
        }
    }

    private static String validarEscalaValoracion(Pregunta pregunta, String entrada) {
        try {
            int opcion = Integer.parseInt(entrada);
            
            if (opcion < 1 || opcion > 5) {
                System.out.println(" Opcion invalida. Ingresa un numero del 1 al 5");
                return null;
            }
            
            String[] opciones = pregunta.getOpciones();
            if (opciones == null || opciones.length < opcion) {
                System.out.println(" Las opciones no estan disponibles");
                return null;
            }
            
            System.out.println("� Respuesta registrada: " + opcion + " - " + opciones[opcion - 1]);
            return opciones[opcion - 1];
            
        } catch (NumberFormatException e) {
            System.out.println(" Debes ingresar un numero entre 1 y 5");
            return null;
        }
    }

    private static String validarNumerica(String entrada) {
        try {
            Double valor = Double.parseDouble(entrada);
            System.out.println("� Respuesta registrada: " + valor);
            return String.valueOf(valor);
        } catch (NumberFormatException e) {
            System.out.println(" Debes ingresar un numero valido");
            return null;
        }
    }

    private static String validarRespuestaSimple(String entrada) {
        if (entrada.length() > 500) {
            System.out.println(" Maximo 500 caracteres. Tu respuesta tiene " + entrada.length());
            return null;
        }
        
        System.out.println("✓ Respuesta registrada");
        return entrada;
    }

    // ============================================================================
    // NUEVOS METODOS PARA ACTUALIZACION DE RESPUESTAS Y DETECCION DE PROGRESO
    // ============================================================================

    /**
     * Actualiza TODAS las respuestas de un participante en una encuesta
     * Permite al participante modificar sus respuestas previas
     */
    private static void actualizarRespuestasEncuesta(int idParticipante, Encuesta encuesta) {
        System.out.println("\n=== ACTUALIZAR TUS RESPUESTAS ===");
        System.out.println("Encuesta: " + encuesta.getTitulo());
        
        ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(encuesta.getIdEncuesta());
        java.util.List<Respuesta> respuestasAnteriores = 
            respuestaDAO.obtenerRespuestasParticipanteEncuesta(idParticipante, encuesta.getIdEncuesta());
        
        if (respuestasAnteriores.isEmpty()) {
            System.out.println("No hay respuestas previas para actualizar.");
            return;
        }
        
        int progreso = obtenerProgreso(idParticipante, encuesta.getIdEncuesta());
        System.out.println("\nProgreso actual: " + respuestasAnteriores.size() + "/" + preguntas.contar() + " preguntas");
        System.out.println("Completitud: " + progreso + "%\n");
        
        System.out.println("Selecciona opcion:");
        System.out.println("1. Actualizar respuestas completamente");
        System.out.println("2. Ver respuestas anteriores");
        System.out.println("3. Volver atras");
        System.out.print("Selecciona opcion: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcion) {
                case 1:
                    responderEncuestaParaActualizar(idParticipante, encuesta);
                    break;
                case 2:
                    mostrarRespuestasAnteriores(idParticipante, encuesta.getIdEncuesta());
                    break;
                case 3:
                    System.out.println("Volviendo atras...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un numero valido.");
        }
    }

    /**
     * Responde la encuesta permitiendo actualizar respuestas existentes
     * Guarda/actualiza cada respuesta en BD
     */
    private static void responderEncuestaParaActualizar(int idParticipante, Encuesta encuesta) {
        System.out.println("\n=== RESPONDIENDO ENCUESTA (MODO ACTUALIZACION) ===");
        System.out.println("Instrucciones:");
        System.out.println("- Ingresa tus nuevas respuestas (o repite las anteriores para mantenerlas)");
        System.out.println("- Todas las respuestas serán actualizadas\n");
        
        ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(encuesta.getIdEncuesta());
        NodoPregunta actual = preguntas.getCabeza();
        
        int preguntasActualizadas = 0;
        
        while (actual != null) {
            Pregunta p = actual.getPregunta();
            
            if (p.getOpciones() == null || p.getOpciones().length == 0) {
                p.generarOpcionesSegunTipo();
            }
            
            System.out.println("\n" + p.getTexto());
            
            // Mostrar respuesta anterior si existe
            Respuesta respuestaAnterior = respuestaDAO.obtenerRespuestaPregunta(
                idParticipante, encuesta.getIdEncuesta(), p.getIdPregunta());
            
            if (respuestaAnterior != null) {
                System.out.println("[Respuesta anterior: " + respuestaAnterior.getContenido() + "]");
            }
            
            String nuevaRespuesta = null;
            
            // Solicitar nueva respuesta segun tipo
            if (p.getTipo() == TipoPregunta.DICOTOMICA || p.getTipo() == TipoPregunta.ESCALA_VALORACION) {
                String[] opciones = p.getOpciones();
                for (int i = 0; i < opciones.length; i++) {
                    System.out.println((i + 1) + ". " + opciones[i]);
                }
                System.out.print("Selecciona opcion (1-" + opciones.length + "): ");
                nuevaRespuesta = validarRespuestaSegunTipo(p, scanner.nextLine().trim());
            } else if (p.getTipo() == TipoPregunta.RESPUESTA_SIMPLE) {
                System.out.print("Ingresa tu respuesta: ");
                nuevaRespuesta = validarRespuestaSegunTipo(p, scanner.nextLine().trim());
            } else if (p.getTipo() == TipoPregunta.NUMERICA) {
                System.out.print("Ingresa un numero: ");
                nuevaRespuesta = validarRespuestaSegunTipo(p, scanner.nextLine().trim());
            }
            
            if (nuevaRespuesta != null) {
                // Crear/actualizar respuesta
                Respuesta respuestaActualizada = new Respuesta();
                respuestaActualizada.setIdParticipante(idParticipante);
                respuestaActualizada.setIdEncuesta(encuesta.getIdEncuesta());
                respuestaActualizada.setIdPregunta(p.getIdPregunta());
                respuestaActualizada.setContenido(nuevaRespuesta);
                respuestaActualizada.setEsValida(true);
                
                // Actualizar en BD (INSERT o UPDATE segun exista)
                if (respuestaAnterior != null) {
                    // Ya existe: ACTUALIZAR
                    respuestaDAO.actualizarRespuesta(respuestaActualizada);
                } else {
                    // No existe: CREAR NUEVA
                    respuestaDAO.registrarRespuesta(respuestaActualizada);
                }
                
                preguntasActualizadas++;
            }
            
            actual = actual.getSiguiente();
        }
        
        System.out.println("\n✓ Actualizacion completa!");
        System.out.println("Preguntas actualizadas: " + preguntasActualizadas);
    }

    /**
     * Calcula el progreso de respuestas del participante en una encuesta
     * @param idParticipante ID del participante
     * @param idEncuesta ID de la encuesta
     * @return Porcentaje de progreso (0-100)
     */
    private static int obtenerProgreso(int idParticipante, int idEncuesta) {
        ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
        java.util.List<Respuesta> respuestas = 
            respuestaDAO.obtenerRespuestasParticipanteEncuesta(idParticipante, idEncuesta);
        
        int totalPreguntas = preguntas.contar();
        int respuestasValidas = 0;
        
        for (Respuesta r : respuestas) {
            if (r.isEsValida()) {
                respuestasValidas++;
            }
        }
        
        return totalPreguntas > 0 ? (respuestasValidas * 100 / totalPreguntas) : 0;
    }
}

