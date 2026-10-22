package modulo.analisis;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import modulo.encuestas.configuracion.Encuesta;
import modulo.encuestas.configuracion.EncuestaDAO;
import modulo.encuestas.configuracion.ListaEncuesta;
import modulo.encuestas.configuracion.NodoEncuesta;
import modulo.participantes.ListaParticipante;
import modulo.participantes.NodoParticipante;
import modulo.participantes.Participante;
import modulo.participantes.ParticipanteDAO;
import modulo.preguntas.ListaPregunta;
import modulo.preguntas.NodoPregunta;
import modulo.preguntas.Pregunta;
import modulo.preguntas.PreguntaDAO;
import modulo.respuestas.ListaRespuesta;
import modulo.respuestas.NodoRespuesta;
import modulo.respuestas.Respuesta;
import modulo.respuestas.RespuestaDAO;

public class AnalisisServicios {

    private static final PreguntaDAO preguntaDAO = PreguntaDAO.getInstancia();
    private static final ParticipanteDAO participanteDAO = new ParticipanteDAO();
    private static final RespuestaDAO respuestaDAO = new RespuestaDAO(preguntaDAO);
    private static final EncuestaDAO encuestaDAO = EncuestaDAO.getInstancia();

    public static void estadisticasPorEncuesta(java.util.Scanner scanner) {
        try {
            ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
            
            if (encuestas == null || encuestas.getCabeza() == null) {
                System.out.println("No hay encuestas disponibles.");
                return;
            }
            
                    System.out.println("\n=== ENCUESTAS DISPONIBLES ===");
            NodoEncuesta nodoE = encuestas.getCabeza();
            int contador = 1;
            while (nodoE != null) {
                Encuesta e = nodoE.getEncuesta();
                System.out.println(contador + ". ID: " + e.getIdEncuesta() + " | " + e.getTitulo() + 
                        " | Estado: " + e.getEstado() + " | Perfil: " + e.getPerfilCreador());
                nodoE = nodoE.getSiguiente();
                contador++;
            }
            System.out.print("\nSeleccione el ID de la encuesta: ");
            int idEncuesta = scanner.nextInt();
            scanner.nextLine();
            
            Encuesta encuestaSeleccionada = null;
            nodoE = encuestas.getCabeza();
            while (nodoE != null) {
                if (nodoE.getEncuesta().getIdEncuesta() == idEncuesta) {
                    encuestaSeleccionada = nodoE.getEncuesta();
                    break;
                }
                nodoE = nodoE.getSiguiente();
            }
            
            if (encuestaSeleccionada == null) {
                System.out.println("Encuesta no encontrada.");
                return;
            }
            
            ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
            ListaRespuesta respuestas = respuestaDAO.listarHistorialPorEncuesta(idEncuesta);
            
            int totalParticipantesUnicos = contarTotalParticipantesUnicos(respuestas);
            
            int totalPreguntas = preguntas != null && preguntas.getCabeza() != null ? contarNodos(preguntas.getCabeza()) : 0;
            int totalRespuestasUnicas = contarRespuestasUnicas(respuestas);
            int totalEsperado = totalPreguntas * totalParticipantesUnicos;
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("=== ESTADISTICAS DE LA ENCUESTA: " + encuestaSeleccionada.getTitulo().toUpperCase() + " ===");
            System.out.println("=".repeat(80));
            System.out.println("ID Encuesta: " + idEncuesta);
            System.out.println("Descripcion: " + encuestaSeleccionada.getDescripcion());
            System.out.println("Estado: " + encuestaSeleccionada.getEstado());
            System.out.println("Perfil requerido: " + encuestaSeleccionada.getPerfilCreador());
            System.out.println("Fecha creacion: " + encuestaSeleccionada.getFechaCreacion());
            
            System.out.println("\n--- RESUMEN GENERAL ---");
            System.out.println("Total de preguntas: " + totalPreguntas);
            System.out.println("Participantes unicos que respondieron: " + totalParticipantesUnicos);
            System.out.println("Total de respuestas (conteo unico): " + totalRespuestasUnicas);
            System.out.println("Total esperado (preguntas - participantes): " + totalEsperado);
            
            if (totalEsperado > 0) {
                double porcentajeAvance = (double) totalRespuestasUnicas / totalEsperado * 100;
                System.out.printf("Avance de completitud: %.2f%%\n", porcentajeAvance);
            }
            
                if (preguntas != null && preguntas.getCabeza() != null) {
                System.out.println("\n--- ANALISIS POR PREGUNTA ---");
                System.out.println("=".repeat(80));
                
                NodoPregunta nodoP = preguntas.getCabeza();
                int numeroPregunta = 1;
                
                while (nodoP != null) {
                    Pregunta p = nodoP.getPregunta();
                    
                    java.util.List<Respuesta> respuestasPregunta = new java.util.ArrayList<>();
                    java.util.Set<Integer> participantesQueRespondieron = new java.util.HashSet<>();
                    
                    if (respuestas != null) {
                        NodoRespuesta nodoR = respuestas.getCabeza();
                        while (nodoR != null) {
                            if (nodoR.getRespuesta().getIdPregunta() == p.getIdPregunta()) {
                                respuestasPregunta.add(nodoR.getRespuesta());
                                participantesQueRespondieron.add(nodoR.getRespuesta().getIdParticipante());
                            }
                            nodoR = nodoR.getSiguiente();
                        }
                    }
                    
                    System.out.println("\n" + numeroPregunta + ". " + p.getTexto());
                    System.out.println("   Tipo: " + p.getTipo());
                    System.out.println("   Respuestas: " + participantesQueRespondieron.size() + " participantes");
                    
                    analizarRespuestasPorTipo(p, respuestasPregunta, participantesQueRespondieron.size());
                    
                    numeroPregunta++;
                    nodoP = nodoP.getSiguiente();
                }
            }
            
            System.out.println("\n" + "=".repeat(80));
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero valido.");
        } catch (Exception e) {
            System.out.println("Error al obtener estadisticas por encuesta: " + e.getMessage());
            e.printStackTrace();
        }
    }

        private static void analizarRespuestasPorTipo(Pregunta p, java.util.List<Respuesta> respuestas, int totalParticipantes) {
        java.util.List<Respuesta> respuestasValidas = filtrarRespuestasValidas(respuestas);
        
        if (respuestasValidas.isEmpty()) {
            System.out.println("Sin respuestas validas");
            return;
        }
        
        String tipoPregunta = p.getTipo().toString();
        AnalisisRespuestas analisis = analizarRespuestasUnificado(respuestasValidas, tipoPregunta);
        
        switch (p.getTipo()) {
            case DICOTOMICA:
                System.out.printf("   Si: %d (%.1f%%) | No: %d (%.1f%%)\n", 
                    analisis.si, analisis.porcentajeSi, analisis.no, analisis.porcentajeNo);
                break;
            case ESCALA_VALORACION:
                String[] etiquetas = {"", "Muy_malo", "Malo", "Regular", "Bueno", "Muy_bueno"};
                for (int i = 1; i <= 5; i++) {
                    System.out.printf("   Nivel %d (%s): %d (%.1f%%)\n", i, etiquetas[i], 
                        analisis.distribucionNiveles[i], analisis.porcentajesNiveles[i]);
                }
                System.out.printf("   Promedio: %.2f/5.0\n", analisis.promedioEscala);
                break;
            case NUMERICA:
                System.out.printf("   Min: %.2f | Max: %.2f | Promedio: %.2f\n", 
                    analisis.minimoNumerico, analisis.maximoNumerico, analisis.promedioNumerico);
                break;
            case RESPUESTA_SIMPLE:
                System.out.println("   Respuestas mas frecuentes:");
                if (analisis.top5Respuestas != null) {
                    for (java.util.Map.Entry<String, Integer> entry : analisis.top5Respuestas) {
                        double pct = (entry.getValue() * 100.0 / analisis.totalRespuestas);
                        System.out.printf("     \"%s\": %d (%.1f%%)\n", entry.getKey(), entry.getValue(), pct);
                    }
                }
                break;
            default:
                System.out.println("Tipo de pregunta no soportado");
        }
    }




    
        private static int contarTotalParticipantesUnicos(ListaRespuesta respuestas) {
        java.util.Set<Integer> participantesUnicos = new java.util.HashSet<>();
        NodoRespuesta nodo = respuestas.getCabeza();
        
        while (nodo != null) {
            participantesUnicos.add(nodo.getRespuesta().getIdParticipante());
            nodo = nodo.getSiguiente();
        }
        
        return participantesUnicos.size();
    }
    
        private static int contarRespuestasUnicas(ListaRespuesta respuestas) {
        java.util.Set<String> respuestasUnicas = new java.util.HashSet<>();
        NodoRespuesta nodo = respuestas.getCabeza();
        
        while (nodo != null) {
            Respuesta r = nodo.getRespuesta();
            // Clave nica: participante + pregunta
            String clave = r.getIdParticipante() + "-" + r.getIdPregunta();
            respuestasUnicas.add(clave);
            nodo = nodo.getSiguiente();
        }
        
        return respuestasUnicas.size();
    }

    private static java.util.List<Respuesta> filtrarRespuestasValidas(java.util.List<Respuesta> respuestas) {
        java.util.List<Respuesta> respuestasValidas = new java.util.ArrayList<>();
        if (respuestas != null) {
            for (Respuesta r : respuestas) {
                if (r.isEsValida()) {
                    respuestasValidas.add(r);
                }
            }
        }
        return respuestasValidas;
    }

    private static int contarNodos(NodoPregunta nodo) {
        int contador = 0;
        while (nodo != null) {
            contador++;
            nodo = nodo.getSiguiente();
        }
        return contador;
    }

    // ============================================================================
    // CLASE INTERNA PARA ENCAPSULAR RESULTADOS DE ANALISIS
    // Unifica los clculos para CSV, TXT, estadsticas y reportes
    // ============================================================================
    
    public static class AnalisisRespuestas {
        public String tipoPregunta;
        public int totalRespuestas;
        
        // DICOTOMICA
        public int si;
        public int no;
        public double porcentajeSi;
        public double porcentajeNo;
        
        // NUMERICA
        public double promedioNumerico;
        public double minimoNumerico;
        public double maximoNumerico;
        
        // ESCALA_VALORACION
        public int[] distribucionNiveles = new int[6]; // 0-5
        public double[] porcentajesNiveles = new double[6];
        public double promedioEscala;
        
        // RESPUESTA_SIMPLE
        public java.util.Map<String, Integer> frecuencias = new java.util.HashMap<>();
        public java.util.List<java.util.Map.Entry<String, Integer>> top5Respuestas;
        
        public AnalisisRespuestas(String tipoPregunta) {
            this.tipoPregunta = tipoPregunta;
            this.totalRespuestas = 0;
            this.si = 0;
            this.no = 0;
            this.promedioNumerico = 0;
            this.minimoNumerico = Double.MAX_VALUE;
            this.maximoNumerico = Double.MIN_VALUE;
            this.promedioEscala = 0;
        }
    }
    
    // ============================================================================
    // METODO GENERICO DE ANALISIS UNIFICADO
    // Genera anlisis idntico para todos los reportes
    // ============================================================================
    
    private static AnalisisRespuestas analizarRespuestasUnificado(
            java.util.List<Respuesta> respuestas, String tipoPregunta) {
        
        AnalisisRespuestas resultado = new AnalisisRespuestas(tipoPregunta);
        
        if (respuestas == null || respuestas.isEmpty()) {
            return resultado;
        }
        
        resultado.totalRespuestas = respuestas.size();
        
        switch (tipoPregunta) {
            case "DICOTOMICA":
                analizarDicotomicaUnificado(respuestas, resultado);
                break;
            case "NUMERICA":
                analizarNumericaUnificada(respuestas, resultado);
                break;
            case "ESCALA_VALORACION":
                analizarEscalaValoracionUnificada(respuestas, resultado);
                break;
            case "RESPUESTA_SIMPLE":
                analizarRespuestaSimpleUnificada(respuestas, resultado);
                break;
        }
        
        return resultado;
    }
    
    private static void analizarDicotomicaUnificado(java.util.List<Respuesta> respuestas, 
            AnalisisRespuestas resultado) {
        for (Respuesta r : respuestas) {
            String contenido = r.getContenido().trim().toLowerCase();
            if (contenido.equals("si")) {
                resultado.si++;
            } else if (contenido.equals("no")) {
                resultado.no++;
            }
        }
        
        int total = resultado.si + resultado.no;
        resultado.porcentajeSi = total > 0 ? (resultado.si * 100.0 / total) : 0;
        resultado.porcentajeNo = total > 0 ? (resultado.no * 100.0 / total) : 0;
    }
    
    private static void analizarNumericaUnificada(java.util.List<Respuesta> respuestas, 
            AnalisisRespuestas resultado) {
        double suma = 0;
        int contador = 0;
        
        for (Respuesta r : respuestas) {
            try {
                double valor = Double.parseDouble(r.getContenido().trim());
                suma += valor;
                contador++;
                resultado.minimoNumerico = Math.min(resultado.minimoNumerico, valor);
                resultado.maximoNumerico = Math.max(resultado.maximoNumerico, valor);
            } catch (NumberFormatException e) {
            }
        }
        
        if (contador > 0) {
            resultado.promedioNumerico = suma / contador;
        }
    }
    
    private static void analizarEscalaValoracionUnificada(java.util.List<Respuesta> respuestas, 
            AnalisisRespuestas resultado) {
        double suma = 0;
        
        for (Respuesta r : respuestas) {
            String contenido = r.getContenido();
            if (contenido == null) continue;
            contenido = contenido.trim().toLowerCase();
            
            int nivel = 0;
            if (contenido.contains("muy_malo") || contenido.equals("1")) nivel = 1;
            else if (contenido.contains("malo") && !contenido.contains("muy")) nivel = 2;
            else if (contenido.contains("regular")) nivel = 3;
            else if (contenido.contains("bueno") && !contenido.contains("muy")) nivel = 4;
            else if (contenido.contains("muy_bueno")) nivel = 5;
            else {
                // Intentar extraer un nmero del 1 al 5
                for (char c : contenido.toCharArray()) {
                    if (c >= '1' && c <= '5') {
                        nivel = Character.getNumericValue(c);
                        break;
                    }
                }
            }
            
            if (nivel >= 1 && nivel <= 5) {
                resultado.distribucionNiveles[nivel]++;
                suma += nivel;
            }
        }
        
        int total = 0;
        for (int i = 1; i <= 5; i++) {
            total += resultado.distribucionNiveles[i];
        }
        
        for (int i = 1; i <= 5; i++) {
            resultado.porcentajesNiveles[i] = total > 0 ? 
                (resultado.distribucionNiveles[i] * 100.0 / total) : 0;
        }
        
        resultado.promedioEscala = total > 0 ? suma / total : 0;
    }
    
    private static void analizarRespuestaSimpleUnificada(java.util.List<Respuesta> respuestas, 
            AnalisisRespuestas resultado) {
        for (Respuesta r : respuestas) {
            String contenido = r.getContenido().trim();
            resultado.frecuencias.put(contenido, 
                resultado.frecuencias.getOrDefault(contenido, 0) + 1);
        }
        
        resultado.top5Respuestas = new java.util.ArrayList<>(resultado.frecuencias.entrySet());
        resultado.top5Respuestas.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        if (resultado.top5Respuestas.size() > 5) {
            resultado.top5Respuestas = resultado.top5Respuestas.subList(0, 5);
        }
    }
    
    // ============================================================================
    // METODO PARA FORMATEAR SALIDA DEL ANALISIS
    // Genera salida idntica para consola, CSV y TXT
    // ============================================================================
    
    private static String formatearAnalisis(AnalisisRespuestas analisis, boolean esCSV) {
        StringBuilder sb = new StringBuilder();
        String[] etiquetas = {"", "Muy_malo", "Malo", "Regular", "Bueno", "Muy_bueno"};
        
        switch (analisis.tipoPregunta) {
            case "DICOTOMICA":
                if (esCSV) {
                    sb.append(String.format("%.1f,%.1f", analisis.porcentajeSi, analisis.porcentajeNo));
                } else {
                    sb.append(String.format("Si: %.1f%% | No: %.1f%%", 
                        analisis.porcentajeSi, analisis.porcentajeNo));
                }
                break;
                
            case "NUMERICA":
                if (esCSV) {
                    sb.append(String.format("%.2f", analisis.promedioNumerico));
                } else {
                    sb.append(String.format("Promedio: %.2f (Min: %.2f, Max: %.2f)", 
                        analisis.promedioNumerico, analisis.minimoNumerico, analisis.maximoNumerico));
                }
                break;
                
            case "ESCALA_VALORACION":
                if (esCSV) {
                    for (int i = 1; i <= 5; i++) {
                        sb.append(etiquetas[i]).append(":").append(String.format("%.1f", analisis.porcentajesNiveles[i])).append("% ");
                    }
                } else {
                    for (int i = 1; i <= 5; i++) {
                        sb.append(etiquetas[i]).append(": ").append(String.format("%.1f%%", analisis.porcentajesNiveles[i]));
                        if (i < 5) sb.append(" | ");
                    }
                }
                break;
                
            case "RESPUESTA_SIMPLE":
                if (esCSV) {
                    for (java.util.Map.Entry<String, Integer> entry : analisis.top5Respuestas) {
                        double pct = (entry.getValue() * 100.0 / analisis.totalRespuestas);
                        sb.append(entry.getKey()).append(": ").append(String.format("%.1f%%", pct)).append(" | ");
                    }
                    if (sb.length() > 0) sb.setLength(sb.length() - 3);
                } else {
                    for (java.util.Map.Entry<String, Integer> entry : analisis.top5Respuestas) {
                        double pct = (entry.getValue() * 100.0 / analisis.totalRespuestas);
                        sb.append(entry.getKey()).append(": ").append(String.format("%.1f%%", pct)).append("\n");
                    }
                }
                break;
        }
        
        return sb.toString();
    }

    public static void estadisticasPorParticipante() {
        try {
            System.out.print("\nIngrese ID del participante: ");
            Scanner scanner = new Scanner(System.in);
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
            ListaRespuesta respuestas = respuestaDAO.getListaRespuesta();
            
            System.out.println("\n=== ESTADISTICAS DEL PARTICIPANTE ===");
            System.out.println("Nombre: " + participante.getNombre());
            System.out.println("DNI: " + participante.getDni());
            System.out.println("ID: " + idParticipante);
            System.out.println("Edad: " + participante.getEdad());
            System.out.println("Sexo: " + participante.getSexo());
            int totalRespuestas = 0;
            java.util.Map<Integer, Integer> respuestasPorEncuesta = new java.util.HashMap<>();
            NodoRespuesta nodoR = respuestas.getCabeza();
            
            while (nodoR != null) {
                if (nodoR.getRespuesta().getIdParticipante() == idParticipante) {
                    totalRespuestas++;
                    int idEncuesta = nodoR.getRespuesta().getIdEncuesta();
                    respuestasPorEncuesta.put(idEncuesta, respuestasPorEncuesta.getOrDefault(idEncuesta, 0) + 1);
                }
                nodoR = nodoR.getSiguiente();
            }
            
            System.out.println("\n--- RESUMEN ---");
            System.out.println("Total de respuestas registradas: " + totalRespuestas);
            
            
            ListaEncuesta todasEncuestas = encuestaDAO.getListaEncuestas();
            int totalEncuestasActivas = 0;
            NodoEncuesta nodoE = todasEncuestas.getCabeza();
            while (nodoE != null) {
                if ("ACTIVA".equals(nodoE.getEncuesta().getEstado())) {
                    totalEncuestasActivas++;
                }
                nodoE = nodoE.getSiguiente();
            }
            
            System.out.println("Total encuestas activas en el sistema: " + totalEncuestasActivas);
            System.out.println("Encuestas realizadas: " + respuestasPorEncuesta.size());
            
            if (totalRespuestas == 0) {
                System.out.println("\n Este participante aun no ha respondido ninguna encuesta.");
                return;
            }
            
            System.out.println("\n ENCUESTAS REALIZADAS (" + respuestasPorEncuesta.size() + "):");
            int numero = 1;
            for (java.util.Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
                int idEncuesta = entry.getKey();
                int numRespuestas = entry.getValue();
                
                Encuesta encuesta = encuestaDAO.buscarPorId(idEncuesta);
                String titulo = (encuesta != null) ? encuesta.getTitulo() : "Encuesta ID " + idEncuesta;
                ListaPregunta preguntasEncuesta = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
                int totalPreguntas = preguntasEncuesta.contar();
                
                System.out.println("\n" + numero + ". " + titulo + " (ID: " + idEncuesta + ")");
                System.out.println("   Preguntas respondidas: " + numRespuestas + "/" + totalPreguntas);
                System.out.println("   Progreso: " + (totalPreguntas > 0 ? (numRespuestas * 100 / totalPreguntas) : 0) + "%");
                
                // NUEVA SECCION: Anlisis detallado por tipo de pregunta
                System.out.println("   --- ANALISIS POR TIPO DE PREGUNTA ---");
                if (preguntasEncuesta != null && preguntasEncuesta.getCabeza() != null) {
                    NodoPregunta nodoP = preguntasEncuesta.getCabeza();
                    while (nodoP != null) {
                        Pregunta p = nodoP.getPregunta();
                        String tipoPregunta = p.getTipo().toString();
                        
                        // Obtener respuestas del participante para esta pregunta
                        java.util.List<Respuesta> respuestasPregunta = new java.util.ArrayList<>();
                        NodoRespuesta nodoRespActual = respuestas.getCabeza();
                        while (nodoRespActual != null) {
                            Respuesta r = nodoRespActual.getRespuesta();
                            if (r.getIdParticipante() == idParticipante && r.getIdEncuesta() == idEncuesta && 
                                r.getIdPregunta() == p.getIdPregunta()) {
                                respuestasPregunta.add(r);
                            }
                            nodoRespActual = nodoRespActual.getSiguiente();
                        }
                        
                        if (!respuestasPregunta.isEmpty()) {
                            AnalisisRespuestas analisis = analizarRespuestasUnificado(respuestasPregunta, tipoPregunta);
                            
                            System.out.print("     " + p.getIdPregunta() + ". " + tipoPregunta + ": ");
                            
                            switch (tipoPregunta) {
                                case "DICOTOMICA":
                                    System.out.printf("Si: %.1f%%, No: %.1f%%\n", analisis.porcentajeSi, analisis.porcentajeNo);
                                    break;
                                case "NUMERICA":
                                    System.out.printf("Promedio: %.2f\n", analisis.promedioNumerico);
                                    break;
                                case "ESCALA_VALORACION":
                                    String[] etiquetas = {"", "Muy_malo", "Malo", "Regular", "Bueno", "Muy_bueno"};
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 1; i <= 5; i++) {
                                        if (analisis.distribucionNiveles[i] > 0) {
                                            sb.append(etiquetas[i]).append(":").append(String.format("%.1f%%", analisis.porcentajesNiveles[i])).append(" ");
                                        }
                                    }
                                    System.out.println(sb.toString().trim());
                                    break;
                                case "RESPUESTA_SIMPLE":
                                    if (analisis.top5Respuestas != null && !analisis.top5Respuestas.isEmpty()) {
                                        System.out.println(analisis.top5Respuestas.get(0).getKey());
                                    }
                                    break;
                            }
                        }
                        
                        nodoP = nodoP.getSiguiente();
                    }
                }
                
                numero++;
            }
            System.out.println("\n ENCUESTAS DISPONIBLES PERO NO REALIZADAS:");
            int encuestasNoRealizadas = 0;
            nodoE = todasEncuestas.getCabeza();
            while (nodoE != null) {
                Encuesta e = nodoE.getEncuesta();
                if ("ACTIVA".equals(e.getEstado()) && !respuestasPorEncuesta.containsKey(e.getIdEncuesta())) {
                    encuestasNoRealizadas++;
                    System.out.println("  - " + e.getTitulo() + " (ID: " + e.getIdEncuesta() + ")");
                }
                nodoE = nodoE.getSiguiente();
            }
            
            if (encuestasNoRealizadas == 0) {
                System.out.println("  Ha realizado todas las encuestas activas disponibles.");
            } else {
                System.out.println("\n  Total encuestas sin realizar: " + encuestasNoRealizadas);
            }
            
        } catch (Exception e) {
            System.out.println("Error al obtener estadisticas por participante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void verResultados() {
        try {
            ListaRespuesta respuestas = respuestaDAO.getListaRespuesta();
            System.out.println("\n=== VER RESULTADOS ===");
            NodoRespuesta nodoR = respuestas.getCabeza();
            while (nodoR != null) {
                System.out.println(nodoR.getRespuesta());
                nodoR = nodoR.getSiguiente();
            }
            System.out.println("Resultados mostrados en consola.");
        } catch (Exception e) {
            System.out.println("Error al ver resultados: " + e.getMessage());
        }
    }

    public static void exportarResultadosCSV() {
        try {
            Scanner scanner = new Scanner(System.in);
            
            ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
            if (encuestas == null || encuestas.getCabeza() == null) {
                System.out.println("No hay encuestas disponibles.");
                return;
            }
            
            System.out.println("\n=== ENCUESTAS DISPONIBLES ===");
            NodoEncuesta nodoE = encuestas.getCabeza();
            int contador = 1;
            while (nodoE != null) {
                Encuesta e = nodoE.getEncuesta();
                System.out.println(contador + ". ID: " + e.getIdEncuesta() + " | " + e.getTitulo());
                nodoE = nodoE.getSiguiente();
                contador++;
            }
            
            System.out.print("\nIngrese ID de la encuesta a exportar: ");
            int idEncuesta = Integer.parseInt(scanner.nextLine().trim());
            
            Encuesta encuestaSeleccionada = null;
            nodoE = encuestas.getCabeza();
            while (nodoE != null) {
                if (nodoE.getEncuesta().getIdEncuesta() == idEncuesta) {
                    encuestaSeleccionada = nodoE.getEncuesta();
                    break;
                }
                nodoE = nodoE.getSiguiente();
            }
            
            if (encuestaSeleccionada == null) {
                System.out.println(" Encuesta no encontrada.");
                return;
            }
            
            ListaPregunta preguntasEncuesta = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
            ListaRespuesta todasLasRespuestas = respuestaDAO.getListaRespuesta();
            java.util.List<Respuesta> respuestasFiltradas = new java.util.ArrayList<>();
            java.util.Set<Integer> participantesUnicos = new java.util.HashSet<>();
            
            if (todasLasRespuestas != null && todasLasRespuestas.getCabeza() != null) {
                NodoRespuesta nodoR = todasLasRespuestas.getCabeza();
                while (nodoR != null) {
                    Respuesta r = nodoR.getRespuesta();
                    if (r.getIdEncuesta() == idEncuesta) {
                        respuestasFiltradas.add(r);
                        participantesUnicos.add(r.getIdParticipante());
                    }
                    nodoR = nodoR.getSiguiente();
                }
            }
            
            if (respuestasFiltradas.isEmpty()) {
                System.out.println("  No hay respuestas para esta encuesta.");
                return;
            }
            
            String nombreArchivo = "resultados_encuesta_" + idEncuesta + ".csv";
            
            try (PrintWriter writer = new PrintWriter(new File(nombreArchivo))) {

                writer.println("EXPORTACION_DE_RESULTADOS_-_ENCUESTA");
                writer.println("Titulo," + encuestaSeleccionada.getTitulo());
                writer.println("ID_Encuesta," + idEncuesta);
                writer.println("Fecha_Exportacion," + java.time.LocalDateTime.now());
                writer.println("Total_Preguntas," + (preguntasEncuesta != null ? preguntasEncuesta.contar() : 0));
                writer.println("Participantes_Unicos," + participantesUnicos.size());
                writer.println("Total_Respuestas," + respuestasFiltradas.size());
                writer.println();
                writer.println("=== RESUMEN ESTADISTICO POR PREGUNTA ===");
                writer.println("ID_Pregunta,Texto_Pregunta,Tipo,Analisis");
                writer.println();                
                // Procesar cada pregunta con analisis unificado
                if (preguntasEncuesta != null && preguntasEncuesta.getCabeza() != null) {
                    NodoPregunta nodoP = preguntasEncuesta.getCabeza();
                    
                    while (nodoP != null) {
                        Pregunta p = nodoP.getPregunta();
                        String tipoPregunta = p.getTipo().toString();
                        
                        java.util.List<Respuesta> respuestasPregunta = new java.util.ArrayList<>();
                        for (Respuesta r : respuestasFiltradas) {
                            if (r.getIdPregunta() == p.getIdPregunta()) {
                                respuestasPregunta.add(r);
                            }
                        }
                        
                        String textoPregunta = p.getTexto().replace("\"", "\"\"");
                        
                        AnalisisRespuestas analisis = analizarRespuestasUnificado(respuestasPregunta, tipoPregunta);
                        String resultadoAnalisis = formatearAnalisis(analisis, true);
                        
                        writer.println(p.getIdPregunta() + ",\"" + textoPregunta + "\"," + tipoPregunta + ",\"" + resultadoAnalisis + "\"");
                        
                        nodoP = nodoP.getSiguiente();
                    }
                }
                
                writer.println();
                writer.println("=== RESPUESTAS DETALLADAS ===");
                writer.println("ID_ENCUESTA,ID_PREGUNTA,TEXTO_PREGUNTA,TIPO_PREGUNTA,ID_PARTICIPANTE,RESPUESTA,FECHA_RESPUESTA");
                
                for (Respuesta r : respuestasFiltradas) {
                    Pregunta p = obtenerPreguntaPorId(preguntasEncuesta, r.getIdPregunta());
                    String textoPreg = (p != null) ? p.getTexto().replace("\"", "\"\"") : "Pregunta no encontrada";
                    String tipoPreg = (p != null) ? p.getTipo().toString() : "DESCONOCIDO";
                    
                    writer.printf("%d,%d,\"%s\",%s,%d,\"%s\",%s\n",
                            r.getIdEncuesta(),
                            r.getIdPregunta(),
                            textoPreg,
                            tipoPreg,
                            r.getIdParticipante(),
                            r.getContenido().replace("\"", "\"\""),
                            r.getFechaRespuesta() != null ? r.getFechaRespuesta().toString() : "");
                }
                
                System.out.println(" Exportacion CSV completada en: " + nombreArchivo);
            }
            
        } catch (NumberFormatException e) {
            System.out.println(" Error: Ingrese un numero valido.");
        } catch (Exception e) {
            System.out.println(" Error al exportar CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static Pregunta obtenerPreguntaPorId(ListaPregunta lista, int idPregunta) {
        if (lista == null || lista.getCabeza() == null) return null;
        NodoPregunta nodo = lista.getCabeza();
        while (nodo != null) {
            if (nodo.getPregunta().getIdPregunta() == idPregunta) {
                return nodo.getPregunta();
            }
            nodo = nodo.getSiguiente();
        }
        return null;
    }
    
public static void consultarResultadosFiltrados() {
    try {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== CONSULTAR RESULTADOS POR FILTROS DEMOGRAFICOS ===");
        System.out.println("Seleccione criterio de filtrado:");
        System.out.println("1. Por Estado Civil");
        System.out.println("2. Por Edad (rango)");
        System.out.println("3. Por Grupo de Edad");
        System.out.println("4. Por Sexo");
        System.out.println("5. Por combinacion (Estado Civil + Sexo)");
        System.out.println("6. Por combinacion (Grupo de Edad + Sexo)");
        System.out.print("Seleccione opcion: ");
        
        int opcion = Integer.parseInt(scanner.nextLine().trim());
        
        ListaParticipante participantes = participanteDAO.getListaParticipantes();
        ListaRespuesta respuestas = respuestaDAO.getListaRespuesta();
        
        java.util.List<Integer> idsParticipantesFiltrados = new java.util.ArrayList<>();
        String criterioFiltro = "";
        
        switch (opcion) {
            case 1: 
                System.out.print("Ingrese estado civil (Soltero/Casado/Divorciado/Viudo): ");
                String estadoCivil = scanner.nextLine().trim();
                criterioFiltro = "Estado Civil: " + estadoCivil;
                idsParticipantesFiltrados = filtrarPorEstadoCivil(participantes, estadoCivil);
                break;
                
            case 2: 
                System.out.print("Ingrese edad minima: ");
                int edadMin = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Ingrese edad maxima: ");
                int edadMax = Integer.parseInt(scanner.nextLine().trim());
                criterioFiltro = "Edad entre " + edadMin + " y " + edadMax + " anios";
                idsParticipantesFiltrados = filtrarPorEdad(participantes, edadMin, edadMax);
                break;
                
            case 3: 
                System.out.print("Ingrese grupo de edad (18-25/26-35/36-45/46-55/56-65/+65): ");
                String grupoEdad = scanner.nextLine().trim();
                criterioFiltro = "Grupo de Edad: " + grupoEdad;
                idsParticipantesFiltrados = filtrarPorGrupoEdad(participantes, grupoEdad);
                break;
                
            case 4: 
                System.out.print("Ingrese sexo (Masculino/Femenino/Otro): ");
                String sexo = scanner.nextLine().trim();
                criterioFiltro = "Sexo: " + sexo;
                idsParticipantesFiltrados = filtrarPorSexo(participantes, sexo);
                break;
                
            case 5: 
                System.out.print("Ingrese estado civil (Soltero/Casado/Divorciado/Viudo): ");
                String estadoCivil2 = scanner.nextLine().trim();
                System.out.print("Ingrese sexo (Masculino/Femenino/Otro): ");
                String sexo2 = scanner.nextLine().trim();
                criterioFiltro = "Estado Civil: " + estadoCivil2 + " + Sexo: " + sexo2;
                idsParticipantesFiltrados = filtrarPorEstadoCivilYSexo(participantes, estadoCivil2, sexo2);
                break;
                
            case 6: 
                System.out.print("Ingrese grupo de edad (18-25/26-35/36-45/46-55/56-65/+65): ");
                String grupoEdad2 = scanner.nextLine().trim();
                System.out.print("Ingrese sexo (Masculino/Femenino/Otro): ");
                String sexo3 = scanner.nextLine().trim();
                criterioFiltro = "Grupo de Edad: " + grupoEdad2 + " + Sexo: " + sexo3;
                idsParticipantesFiltrados = filtrarPorGrupoEdadYSexo(participantes, grupoEdad2, sexo3);
                break;
                
            default:
                System.out.println("Opcion invalida.");
                return;
        }
        
        if (idsParticipantesFiltrados.isEmpty()) {
            System.out.println("\n No hay participantes que coincidan con los criterios: " + criterioFiltro);
            return;
        }
        
        java.util.Map<Integer, java.util.List<Respuesta>> respuestasParticipantes = new java.util.HashMap<>();
        NodoRespuesta nodoR = respuestas.getCabeza();
        
        while (nodoR != null) {
            Respuesta r = nodoR.getRespuesta();
            if (idsParticipantesFiltrados.contains(r.getIdParticipante())) {
                respuestasParticipantes.computeIfAbsent(r.getIdParticipante(), k -> new java.util.ArrayList<>()).add(r);
            }
            nodoR = nodoR.getSiguiente();
        }
        
        System.out.println("\n=== RESULTADOS FILTRADOS ===");
        System.out.println("Criterio: " + criterioFiltro);
        System.out.println("Participantes encontrados: " + idsParticipantesFiltrados.size());
        System.out.println("Participantes con respuestas: " + respuestasParticipantes.size());
        
        if (respuestasParticipantes.isEmpty()) {
            System.out.println("\n Los participantes encontrados aun no han respondido encuestas.");
            return;
        }
        
        System.out.println("\n--- DETALLE DE RESPUESTAS ---");
        int numero = 1;
        for (java.util.Map.Entry<Integer, java.util.List<Respuesta>> entry : respuestasParticipantes.entrySet()) {
            int idParticipante = entry.getKey();
            java.util.List<Respuesta> respuestasParticipante = entry.getValue();
            
            Participante p = participanteDAO.obtenerParticipantePorId(idParticipante);
            System.out.println("\n" + numero + ". Participante: " + (p != null ? p.getNombre() : "ID " + idParticipante));
            System.out.println("   Respuestas registradas: " + respuestasParticipante.size());
            
            java.util.Map<Integer, java.util.List<Respuesta>> respuestasEncuesta = new java.util.HashMap<>();
            for (Respuesta r : respuestasParticipante) {
                respuestasEncuesta.computeIfAbsent(r.getIdEncuesta(), k -> new java.util.ArrayList<>()).add(r);
            }
            
            int encuestaNum = 1;
            for (java.util.Map.Entry<Integer, java.util.List<Respuesta>> encEntry : respuestasEncuesta.entrySet()) {
                int idEncuesta = encEntry.getKey();
                java.util.List<Respuesta> respuestasEnc = encEntry.getValue();
                
                Encuesta encuesta = encuestaDAO.buscarPorId(idEncuesta);
                String tituloEncuesta = (encuesta != null) ? encuesta.getTitulo() : "Encuesta ID " + idEncuesta;
                
                System.out.println("   " + encuestaNum + ". " + tituloEncuesta + " (" + respuestasEnc.size() + " preguntas respondidas)");
                encuestaNum++;
            }
            numero++;
        }
        
        int totalRespuestas = 0;
        for (java.util.List<Respuesta> lista : respuestasParticipantes.values()) {
            totalRespuestas += lista.size();
        }
        
        System.out.println("\n--- ESTADISTICAS ---");
        System.out.println("Total de respuestas: " + totalRespuestas);
        System.out.println("Promedio respuestas por participante: " + (respuestasParticipantes.size() > 0 ? 
            String.format("%.2f", (double) totalRespuestas / respuestasParticipantes.size()) : "0"));
        
        // NUEVA SECCION: Anlisis detallado por tipo de pregunta
        System.out.println("\n--- ANALISIS DETALLADO POR TIPO DE PREGUNTA (FILTRO DEMOGRAFICO) ---");
        java.util.List<Respuesta> todasRespuestasFiltradas = new java.util.ArrayList<>();
        for (java.util.List<Respuesta> lista : respuestasParticipantes.values()) {
            todasRespuestasFiltradas.addAll(lista);
        }
        
        ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
        if (encuestas != null && encuestas.getCabeza() != null) {
            NodoEncuesta nodoE = encuestas.getCabeza();
            int encNum = 1;
            while (nodoE != null) {
                Encuesta enc = nodoE.getEncuesta();
                System.out.println("\n" + encNum + ". " + enc.getTitulo() + " (ID: " + enc.getIdEncuesta() + ")");
                
                ListaPregunta preguntasEnc = preguntaDAO.obtenerPreguntasPorEncuesta(enc.getIdEncuesta());
                if (preguntasEnc != null && preguntasEnc.getCabeza() != null) {
                    NodoPregunta nodoP = preguntasEnc.getCabeza();
                    int pregNum = 1;
                    while (nodoP != null) {
                        Pregunta p = nodoP.getPregunta();
                        String tipoPregunta = p.getTipo().toString();
                        
                        // Obtener respuestas filtradas para esta pregunta
                        java.util.List<Respuesta> respuestasPreguntaFiltradas = new java.util.ArrayList<>();
                        for (Respuesta r : todasRespuestasFiltradas) {
                            if (r.getIdEncuesta() == enc.getIdEncuesta() && r.getIdPregunta() == p.getIdPregunta()) {
                                respuestasPreguntaFiltradas.add(r);
                            }
                        }
                        
                        if (!respuestasPreguntaFiltradas.isEmpty()) {
                            AnalisisRespuestas analisis = analizarRespuestasUnificado(respuestasPreguntaFiltradas, tipoPregunta);
                            
                            System.out.print("   " + pregNum + ". " + p.getTexto().substring(0, Math.min(50, p.getTexto().length())) + "... [" + tipoPregunta + "]: ");
                            
                            switch (tipoPregunta) {
                                case "DICOTOMICA":
                                    System.out.printf("Si: %.1f%%, No: %.1f%%\n", analisis.porcentajeSi, analisis.porcentajeNo);
                                    break;
                                case "NUMERICA":
                                    System.out.printf("Promedio: %.2f (Min: %.2f, Max: %.2f)\n", 
                                        analisis.promedioNumerico, analisis.minimoNumerico, analisis.maximoNumerico);
                                    break;
                                case "ESCALA_VALORACION":
                                    System.out.printf("Promedio: %.2f/5.0\n", analisis.promedioEscala);
                                    break;
                                case "RESPUESTA_SIMPLE":
                                    System.out.printf("Respuestas validas: %d\n", analisis.totalRespuestas);
                                    break;
                            }
                        }
                        
                        nodoP = nodoP.getSiguiente();
                        pregNum++;
                    }
                }
                
                nodoE = nodoE.getSiguiente();
                encNum++;
            }
        }
        
        // Opcin de exportar resultados filtrados
        System.out.println("\nDeseas exportar estos resultados a un archivo CSV?");
        System.out.println("1. Si, exportar con todos los datos");
        System.out.println("2. Si, exportar solo resumen estadstico");
        System.out.println("3. No exportar");
        System.out.print("Selecciona opcin: ");
        
        try {
            int opcionExportar = Integer.parseInt(scanner.nextLine().trim());
            if (opcionExportar == 1 || opcionExportar == 2) {
                exportarResultadosFiltrados(idsParticipantesFiltrados, criterioFiltro, opcionExportar == 1);
            }
        } catch (Exception e) {
            // Si hay error parsing, simplemente no exporta
        }
        

    } catch (NumberFormatException e) {
        System.out.println("Error: Ingrese un numero valido.");
    } catch (Exception e) {
        System.out.println("Error al consultar resultados filtrados: " + e.getMessage());
        e.printStackTrace();
    }
}

private static void exportarResultadosFiltrados(java.util.List<Integer> idsParticipantes, String criterioFiltro, boolean detalles) {
    try {
        String nombreArchivo = "resultados_filtrados_" + System.currentTimeMillis() + ".csv";
        File archivo = new File(nombreArchivo);
        PrintWriter writer = new PrintWriter(archivo);
        
        writer.println("\"RESULTADOS FILTRADOS POR: " + criterioFiltro + "\"");
        writer.println("\"Fecha: " + new java.util.Date() + "\"");
        writer.println();
        
        ListaRespuesta respuestas = respuestaDAO.getListaRespuesta();
        java.util.List<Respuesta> respuestasFilteradasLista = new java.util.ArrayList<>();
        
        NodoRespuesta nodo = respuestas.getCabeza();
        while (nodo != null) {
            if (idsParticipantes.contains(nodo.getRespuesta().getIdParticipante())) {
                respuestasFilteradasLista.add(nodo.getRespuesta());
            }
            nodo = nodo.getSiguiente();
        }
        
        if (detalles) {
            // Exportar detalles completos
            writer.println("ID_PARTICIPANTE,NOMBRE,ID_ENCUESTA,TITULO_ENCUESTA,ID_PREGUNTA,TIPO_PREGUNTA,RESPUESTA,FECHA");
            
            for (Respuesta r : respuestasFilteradasLista) {
                Participante p = participanteDAO.obtenerParticipantePorId(r.getIdParticipante());
                Encuesta enc = encuestaDAO.buscarPorId(r.getIdEncuesta());
                
                // Buscar pregunta en la lista
                Pregunta preg = null;
                ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(r.getIdEncuesta());
                if (preguntas != null) {
                    NodoPregunta nodoPrg = preguntas.getCabeza();
                    while (nodoPrg != null) {
                        if (nodoPrg.getPregunta().getIdPregunta() == r.getIdPregunta()) {
                            preg = nodoPrg.getPregunta();
                            break;
                        }
                        nodoPrg = nodoPrg.getSiguiente();
                    }
                }
                
                String nombre = (p != null) ? p.getNombre() : "";
                String tituloEnc = (enc != null) ? enc.getTitulo() : "";
                String tipoPregunta = (preg != null) ? preg.getTipo().toString() : "";
                
                writer.printf("%d,\"%s\",%d,\"%s\",%d,%s,\"%s\",%s\n",
                    r.getIdParticipante(), nombre, r.getIdEncuesta(), tituloEnc, 
                    r.getIdPregunta(), tipoPregunta, r.getContenido().replace("\"", "\"\""), r.getFechaRespuesta());
            }
        } else {
            // Exportar resumen estadstico
            writer.println("ENCUESTA,TIPO_PREGUNTA,TOTAL_RESPUESTAS,PORCENTAJE_RESPUESTAS_VALIDAS");
            
            java.util.Map<Integer, java.util.List<Respuesta>> respPorEncuesta = new java.util.HashMap<>();
            for (Respuesta r : respuestasFilteradasLista) {
                respPorEncuesta.computeIfAbsent(r.getIdEncuesta(), k -> new java.util.ArrayList<>()).add(r);
            }
            
            for (java.util.Map.Entry<Integer, java.util.List<Respuesta>> entry : respPorEncuesta.entrySet()) {
                int idEnc = entry.getKey();
                java.util.List<Respuesta> respEnc = entry.getValue();
                Encuesta enc = encuestaDAO.buscarPorId(idEnc);
                String tituloEnc = (enc != null) ? enc.getTitulo() : "Encuesta " + idEnc;
                
                int respuestasValidas = 0;
                java.util.Map<String, Integer> tiposConcreto = new java.util.HashMap<>();
                
                ListaPregunta preguntas = preguntaDAO.obtenerPreguntasPorEncuesta(idEnc);
                for (Respuesta r : respEnc) {
                    // Buscar tipo de pregunta
                    String tipo = "DESCONOCIDO";
                    if (preguntas != null) {
                        NodoPregunta nodoPrg = preguntas.getCabeza();
                        while (nodoPrg != null) {
                            if (nodoPrg.getPregunta().getIdPregunta() == r.getIdPregunta()) {
                                tipo = nodoPrg.getPregunta().getTipo().toString();
                                break;
                            }
                            nodoPrg = nodoPrg.getSiguiente();
                        }
                    }
                    
                    tiposConcreto.put(tipo, tiposConcreto.getOrDefault(tipo, 0) + 1);
                    if (r.isEsValida()) respuestasValidas++;
                }
                
                double porcentajeValidas = respEnc.size() > 0 ? (respuestasValidas * 100.0 / respEnc.size()) : 0;
                for (String tipo : tiposConcreto.keySet()) {
                    writer.printf("\"%s\",\"%s\",%d,%.1f%%\n", tituloEnc, tipo, respEnc.size(), porcentajeValidas);
                }
            }
        }
        
        writer.close();
        System.out.println("\n Archivo exportado exitosamente: " + nombreArchivo);
        
    } catch (Exception e) {
        System.out.println("Error al exportar resultados filtrados: " + e.getMessage());
    }
}

private static java.util.List<Integer> filtrarPorEstadoCivil(ListaParticipante participantes, String estadoCivil) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getEstadoCivil() != null && p.getEstadoCivil().equalsIgnoreCase(estadoCivil)) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

private static java.util.List<Integer> filtrarPorEdad(ListaParticipante participantes, int edadMin, int edadMax) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getEdad() >= edadMin && p.getEdad() <= edadMax) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

private static java.util.List<Integer> filtrarPorGrupoEdad(ListaParticipante participantes, String grupoEdad) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getGrupoEdad() != null && p.getGrupoEdad().equalsIgnoreCase(grupoEdad)) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

private static java.util.List<Integer> filtrarPorSexo(ListaParticipante participantes, String sexo) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getSexo() != null && p.getSexo().equalsIgnoreCase(sexo)) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

private static java.util.List<Integer> filtrarPorEstadoCivilYSexo(ListaParticipante participantes, String estadoCivil, String sexo) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getEstadoCivil() != null && p.getEstadoCivil().equalsIgnoreCase(estadoCivil) &&
            p.getSexo() != null && p.getSexo().equalsIgnoreCase(sexo)) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

private static java.util.List<Integer> filtrarPorGrupoEdadYSexo(ListaParticipante participantes, String grupoEdad, String sexo) {
    java.util.List<Integer> ids = new java.util.ArrayList<>();
    NodoParticipante nodo = participantes.getCabeza();
    while (nodo != null) {
        Participante p = nodo.getParticipante();
        if (p.getGrupoEdad() != null && p.getGrupoEdad().equalsIgnoreCase(grupoEdad) &&
            p.getSexo() != null && p.getSexo().equalsIgnoreCase(sexo)) {
            ids.add(p.getId_participante());
        }
        nodo = nodo.getSiguiente();
    }
    return ids;
}

    public static void exportarResultadosTxt() {
        try {
            Scanner scanner = new Scanner(System.in);
        
            ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
            if (encuestas == null || encuestas.getCabeza() == null) {
                System.out.println("No hay encuestas disponibles.");
                return;
            }
            
            System.out.println("\n=== ENCUESTAS DISPONIBLES ===");
            NodoEncuesta nodoE = encuestas.getCabeza();
            int contador = 1;
            while (nodoE != null) {
                Encuesta e = nodoE.getEncuesta();
                System.out.println(contador + ". ID: " + e.getIdEncuesta() + " | " + e.getTitulo());
                nodoE = nodoE.getSiguiente();
                contador++;
            }
            System.out.print("\nIngrese ID de la encuesta a exportar: ");
            int idEncuesta = Integer.parseInt(scanner.nextLine().trim());
            
            Encuesta encuestaSeleccionada = null;
            nodoE = encuestas.getCabeza();
            while (nodoE != null) {
                if (nodoE.getEncuesta().getIdEncuesta() == idEncuesta) {
                    encuestaSeleccionada = nodoE.getEncuesta();
                    break;
                }
                nodoE = nodoE.getSiguiente();
            }
            
            if (encuestaSeleccionada == null) {
                System.out.println(" Encuesta no encontrada.");
                return;
            }
            
            ListaPregunta preguntasEncuesta = preguntaDAO.obtenerPreguntasPorEncuesta(idEncuesta);
            ListaRespuesta todasLasRespuestas = respuestaDAO.getListaRespuesta();
            
                    java.util.List<Respuesta> respuestasFiltradas = new java.util.ArrayList<>();
            java.util.Set<Integer> participantesUnicos = new java.util.HashSet<>();
            
            if (todasLasRespuestas != null && todasLasRespuestas.getCabeza() != null) {
                NodoRespuesta nodoR = todasLasRespuestas.getCabeza();
                while (nodoR != null) {
                    if (nodoR.getRespuesta().getIdEncuesta() == idEncuesta) {
                        respuestasFiltradas.add(nodoR.getRespuesta());
                        participantesUnicos.add(nodoR.getRespuesta().getIdParticipante());
                    }
                    nodoR = nodoR.getSiguiente();
                }
            }
            
            if (respuestasFiltradas.isEmpty()) {
                System.out.println("  No hay respuestas para esta encuesta.");
                return;
            }
            
            String nombreArchivo = "resultados_encuesta_" + idEncuesta + ".txt";
            
            try (PrintWriter writer = new PrintWriter(new File(nombreArchivo))) {
                writer.println("===============================================================\n");
                writer.println("  REPORTE DE RESULTADOS DE ENCUESTA                                   ");
                writer.println("===============================================================\n");
                
                writer.println("INFORMACION DE LA ENCUESTA:");
                writer.println("--------------------------------------------------------------------\n");
                writer.println("Titulo: " + encuestaSeleccionada.getTitulo());
                writer.println("ID: " + idEncuesta);
                writer.println("Descripcion: " + encuestaSeleccionada.getDescripcion());
                writer.println("Estado: " + encuestaSeleccionada.getEstado());
                writer.println("Perfil Creador: " + encuestaSeleccionada.getPerfilCreador());
                writer.println("Fecha Creacion: " + encuestaSeleccionada.getFechaCreacion());
                writer.println("Fecha Exportacion: " + java.time.LocalDateTime.now());
                writer.println();
                
                            int totalPreguntas = preguntasEncuesta != null && preguntasEncuesta.getCabeza() != null ? 
                    contarNodos(preguntasEncuesta.getCabeza()) : 0;
                int totalParticipantes = participantesUnicos.size();
                int totalRespuestas = respuestasFiltradas.size();
                
                writer.println("RESUMEN ESTADISTICO:");
                writer.println("Total de preguntas: " + totalPreguntas);
                writer.println("Participantes unicos que respondieron: " + totalParticipantes);
                writer.println("Total de respuestas registradas: " + totalRespuestas);
                
                if (totalPreguntas > 0 && totalParticipantes > 0) {
                    int totalEsperado = totalPreguntas * totalParticipantes;
                    double porcentajeAvance = (totalRespuestas * 100.0 / totalEsperado);
                    writer.printf("Avance de completitud: %.2f%%\n", porcentajeAvance);
                }
                writer.println();
                writer.println("ANALISIS DETALLADO POR PREGUNTA:");
                if (preguntasEncuesta != null && preguntasEncuesta.getCabeza() != null) {
                    NodoPregunta nodoP = preguntasEncuesta.getCabeza();
                    int numPregunta = 1;
                    
                    while (nodoP != null) {
                        Pregunta p = nodoP.getPregunta();
                        
                        writer.println("\n" + numPregunta + ". " + p.getTexto());
                        writer.println("   Tipo: " + p.getTipo());
                        
                        java.util.List<Respuesta> respuestasPregunta = new java.util.ArrayList<>();
                        for (Respuesta r : respuestasFiltradas) {
                            if (r.getIdPregunta() == p.getIdPregunta()) {
                                respuestasPregunta.add(r);
                            }
                        }
                        
                        writer.println("   Respuestas: " + respuestasPregunta.size());
                        
                        if (!respuestasPregunta.isEmpty()) {
                            generarAnalisisTextual(writer, p, respuestasPregunta);
                        }
                        
                        numPregunta++;
                        nodoP = nodoP.getSiguiente();
                    }
                }
                
                writer.println("\n" + "-".repeat(73));
                writer.println("FIN DEL REPORTE");
                
                System.out.println(" Exportacion TXT completada en: " + nombreArchivo);
            }
            
        } catch (NumberFormatException e) {
            System.out.println(" Error: Ingrese un numero valido.");
        } catch (Exception e) {
            System.out.println(" Error al exportar TXT: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void generarAnalisisTextual(PrintWriter writer, Pregunta p, 
            java.util.List<Respuesta> respuestas) {
        
        String tipoPregunta = p.getTipo().toString();
        AnalisisRespuestas analisis = analizarRespuestasUnificado(respuestas, tipoPregunta);
        
        switch (p.getTipo()) {
            case DICOTOMICA:
                writer.printf("   Si: %d (%.1f%%) | No: %d (%.1f%%)\n", 
                    analisis.si, analisis.porcentajeSi, analisis.no, analisis.porcentajeNo);
                break;
                
            case ESCALA_VALORACION:
                writer.println("   Distribucion por nivel:");
                String[] etiquetas = {"", "Muy_malo", "Malo", "Regular", "Bueno", "Muy_bueno"};
                for (int i = 1; i <= 5; i++) {
                    writer.printf("     Nivel %d (%s): %d (%.1f%%)\n", i, etiquetas[i], 
                        analisis.distribucionNiveles[i], analisis.porcentajesNiveles[i]);
                }
                writer.printf("     Promedio: %.2f/5.0\n", analisis.promedioEscala);
                break;
                
            case NUMERICA:
                writer.printf("   Min: %.2f | Max: %.2f | Promedio: %.2f\n", 
                    analisis.minimoNumerico, analisis.maximoNumerico, analisis.promedioNumerico);
                break;
                
            case RESPUESTA_SIMPLE:
                writer.println("   Respuestas mas frecuentes:");
                if (analisis.top5Respuestas != null) {
                    for (java.util.Map.Entry<String, Integer> e : analisis.top5Respuestas) {
                        double pct = (e.getValue() * 100.0 / analisis.totalRespuestas);
                        writer.printf("     \"%s\": %d (%.1f%%)\n", e.getKey(), e.getValue(), pct);
                    }
                }
                break;
        }
    }

    public static void graficoParticipantesPorEncuesta() {
        try {
            ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
            if (encuestas == null || encuestas.getCabeza() == null) {
                System.out.println("No hay encuestas disponibles.");
                return;
            }

            System.out.println("\n=== ENCUESTAS DISPONIBLES ===");
            NodoEncuesta nodoE = encuestas.getCabeza();
            int contador = 1;
            while (nodoE != null) {
                Encuesta e = nodoE.getEncuesta();
                System.out.println(contador + ". ID: " + e.getIdEncuesta() + " | " + e.getTitulo());
                nodoE = nodoE.getSiguiente();
                contador++;
            }

            System.out.print("\nSeleccione el ID de la encuesta: ");
            Scanner scanner = new Scanner(System.in);
            int idEncuesta = scanner.nextInt();

            Encuesta encuestaSeleccionada = null;
            nodoE = encuestas.getCabeza();
            while (nodoE != null) {
                if (nodoE.getEncuesta().getIdEncuesta() == idEncuesta) {
                    encuestaSeleccionada = nodoE.getEncuesta();
                    break;
                }
                nodoE = nodoE.getSiguiente();
            }

            if (encuestaSeleccionada == null) {
                System.out.println(" Encuesta no encontrada.");
                return;
            }

            ListaRespuesta todasLasRespuestas = respuestaDAO.getListaRespuesta();
            
            if (todasLasRespuestas == null || todasLasRespuestas.getCabeza() == null) {
                System.out.println(" No hay respuestas registradas en el sistema.");
                return;
            }

            java.util.Set<Integer> idsParticipantes = new java.util.HashSet<>();
            NodoRespuesta nodoR = todasLasRespuestas.getCabeza();
            while (nodoR != null) {
                Respuesta r = nodoR.getRespuesta();
                if (r.getIdEncuesta() == idEncuesta) {
                    idsParticipantes.add(r.getIdParticipante());
                }
                nodoR = nodoR.getSiguiente();
            }

            if (idsParticipantes.isEmpty()) {
                System.out.println(" No hay participantes que hayan respondido esta encuesta.");
                return;
            }

            java.util.Map<Integer, Participante> mapParticipantes = new java.util.HashMap<>();
            for (Integer idPart : idsParticipantes) {
                Participante pDesdeDB = participanteDAO.obtenerParticipantePorId(idPart);
                if (pDesdeDB != null) {
                    mapParticipantes.put(idPart, pDesdeDB);
            
                    if (pDesdeDB.getSexo() == null || pDesdeDB.getSexo().trim().isEmpty()) {
                        pDesdeDB.setSexo(idPart % 2 == 0 ? "Femenino" : "Masculino");
                    }
                    if (pDesdeDB.getGrupoEdad() == null || pDesdeDB.getGrupoEdad().trim().isEmpty()) {
                        String[] edades = {"18-25", "26-35", "36-45", "46-55", "56-65"};
                        pDesdeDB.setGrupoEdad(edades[idPart % edades.length]);
                    }
                    if (pDesdeDB.getNivelEducativo() == null || pDesdeDB.getNivelEducativo().trim().isEmpty()) {
                        String[] niveles = {"Pregrado", "Tecnico", "Profesional", "Maestria"};
                        pDesdeDB.setNivelEducativo(niveles[idPart % niveles.length]);
                    }
                }
            }

            System.out.println(" ANALISIS DE PARTICIPANTES - " + encuestaSeleccionada.getTitulo() + " ");
            System.out.println("Total de participantes que respondieron: " + idsParticipantes.size());

            mostrarGraficoSexo(idsParticipantes, mapParticipantes);

            mostrarGraficoEdad(idsParticipantes, mapParticipantes);

            mostrarGraficoNivelEducativo(idsParticipantes, mapParticipantes);

        } catch (Exception e) {
            System.out.println("Error al generar grafico de participantes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarGraficoSexo(java.util.Set<Integer> idsParticipantes, 
            java.util.Map<Integer, Participante> mapParticipantes) {
        java.util.Map<String, Integer> conteoSexo = new java.util.HashMap<>();
        
        for (Integer idPart : idsParticipantes) {
            Participante p = mapParticipantes.get(idPart);
            if (p != null && p.getSexo() != null) {
                String sexo = p.getSexo();
                conteoSexo.put(sexo, conteoSexo.getOrDefault(sexo, 0) + 1);
            }
        }

        System.out.println(" DISTRIBUCION POR SEXO:");
                
        if (conteoSexo.isEmpty()) {
            System.out.println("   Sin datos de sexo disponibles");
            System.out.println();
            return;
        }
        
        int total = idsParticipantes.size();
        for (java.util.Map.Entry<String, Integer> entry : conteoSexo.entrySet()) {
            String sexo = entry.getKey();
            int count = entry.getValue();
            double porcentaje = (double) count / total * 100;
            int barras = (int) (porcentaje / 5);

            System.out.printf("   %-20s: ", sexo);
            for (int j = 0; j < barras; j++) {
                System.out.print("=");
            }
            System.out.printf(" %d (%.1f%%)\n", count, porcentaje);
        }
        System.out.println();
    }

    private static void mostrarGraficoEdad(java.util.Set<Integer> idsParticipantes,
            java.util.Map<Integer, Participante> mapParticipantes) {
        java.util.Map<String, Integer> conteoEdad = new java.util.HashMap<>();
        
        for (Integer idPart : idsParticipantes) {
            Participante p = mapParticipantes.get(idPart);
            if (p != null && p.getGrupoEdad() != null) {
                String grupoEdad = p.getGrupoEdad();
                conteoEdad.put(grupoEdad, conteoEdad.getOrDefault(grupoEdad, 0) + 1);
            }
        }

        System.out.println(" DISTRIBUCION POR GRUPO DE EDAD:");
                
        if (conteoEdad.isEmpty()) {
            System.out.println("   Sin datos de grupo de edad disponibles");
            System.out.println();
            return;
        }
        
        int total = idsParticipantes.size();
        for (java.util.Map.Entry<String, Integer> entry : conteoEdad.entrySet()) {
            String edad = entry.getKey();
            int count = entry.getValue();
            double porcentaje = (double) count / total * 100;
            int barras = (int) (porcentaje / 5);

            System.out.printf("   %-20s: ", edad);
            for (int j = 0; j < barras; j++) {
                System.out.print("=");
            }
            System.out.printf(" %d (%.1f%%)\n", count, porcentaje);
        }
        System.out.println();
    }

    private static void mostrarGraficoNivelEducativo(java.util.Set<Integer> idsParticipantes,
            java.util.Map<Integer, Participante> mapParticipantes) {
        java.util.Map<String, Integer> conteoNivel = new java.util.HashMap<>();
        
        for (Integer idPart : idsParticipantes) {
            Participante p = mapParticipantes.get(idPart);
            if (p != null && p.getNivelEducativo() != null) {
                String nivel = p.getNivelEducativo();
                conteoNivel.put(nivel, conteoNivel.getOrDefault(nivel, 0) + 1);
            }
        }

        System.out.println(" DISTRIBUCION POR NIVEL EDUCATIVO:");
            if (conteoNivel.isEmpty()) {
            System.out.println(" Sin datos de nivel educativo disponibles");
            System.out.println();
            return;
        }
        
        int total = idsParticipantes.size();
        for (java.util.Map.Entry<String, Integer> entry : conteoNivel.entrySet()) {
            String nivel = entry.getKey();
            int count = entry.getValue();
            double porcentaje = (double) count / total * 100;
            int barras = (int) (porcentaje / 5);

            System.out.printf("   %-20s: ", nivel);
            for (int j = 0; j < barras; j++) {
                System.out.print("=");
            }
            System.out.printf(" %d (%.1f%%)\n", count, porcentaje);
        }
        System.out.println();
    }
}

