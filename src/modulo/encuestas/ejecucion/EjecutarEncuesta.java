package modulo.encuestas.ejecucion;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import modulo.encuestas.configuracion.Encuesta;
import modulo.encuestas.configuracion.repositorioEncuesta;
import modulo.encuestas.configuracion.FiltroEvaluador;
import modulo.participantes.ParticipanteDAO;
import modulo.preguntas.Pregunta;
import modulo.respuestas.ListaRespuesta;
import modulo.respuestas.Respuesta;
import java.time.LocalDateTime;

public class EjecutarEncuesta {

    private Encuesta encuesta;
    private ListaRespuesta respuestas;
    private repositorioEncuesta repositorio;
    private controlTiempo controlTiempo;
    private Map<Integer, String> respuestasAnterior;  

    public EjecutarEncuesta(Encuesta encuesta, repositorioEncuesta repositorio, controlTiempo controlTiempo) {
        this.encuesta = encuesta;
        this.respuestas = new ListaRespuesta();
        this.repositorio = repositorio;
        this.controlTiempo = controlTiempo;
        this.respuestasAnterior = new HashMap<>();
    }

    public void ejecutar(String dniParticipante) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);

            ParticipanteDAO participanteDAO = new ParticipanteDAO();
            int idParticipante = participanteDAO.obtenerIdPorDNI(dniParticipante);
            if (idParticipante == -1) {
                System.out.println("No se puede continuar: participante no encontrado.");
                return;
            }

            controlTiempo.tiempoDisponible();

            System.out.println("\n=== INICIANDO ENCUESTA ===");
            System.out.println("Encuesta: " + encuesta.getTitulo());
            System.out.println("Descripcion: " + encuesta.getDescripcion());
            System.out.println("============================================\n");

            ejecutarConFiltros(scanner, idParticipante);

            repositorio.guardarRespuestas(dniParticipante, respuestas);
            System.out.println("\n Encuesta completada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero valido.");
        } catch (Exception e) {
            System.out.println("Error al ejecutar encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void ejecutarConFiltros(Scanner scanner, int idParticipante) {
        Pregunta[] preguntas = encuesta.getPreguntas();
        
        for (int i = 0; i < preguntas.length; i++) {
            Pregunta pregunta = preguntas[i];
            
            if (pregunta == null || !pregunta.isActiva()) {
                continue;
            }
         
            if (pregunta.isFiltro() && pregunta.getIdPreguntaPadre() != null) {
          
                if (!debeVisualizar(pregunta)) {
                 
                    registrarRespuestaNoAplicable(pregunta, idParticipante);
                    continue; 
                }
            }
         
            System.out.println("\n[" + (i + 1) + "/" + preguntas.length + "] " + pregunta.getTexto());
            
            pregunta.generarOpcionesSegunTipo();
            String[] opciones = pregunta.getOpciones();
            
            if (opciones != null && opciones.length > 0) {
                for (int j = 0; j < opciones.length; j++) {
                    System.out.println("  " + (j + 1) + ") " + opciones[j]);
                }
            }
          
            String respuesta = obtenerRespuesta(scanner, opciones != null ? opciones.length : -1);
          
            Respuesta r = new Respuesta();
            r.setIdParticipante(idParticipante);
            r.setIdEncuesta(encuesta.getIdEncuesta());
            r.setIdPregunta(pregunta.getIdPregunta());
            r.setContenido(respuesta);
            r.setFechaRespuesta(LocalDateTime.now());
            r.setEsValida(true); 
            
            respuestas.agregar(r);
            
            respuestasAnterior.put(pregunta.getIdPregunta(), respuesta);
        }
    }

    private boolean debeVisualizar(Pregunta pregunta) {
        Integer idPadre = pregunta.getIdPreguntaPadre();
        String operador = pregunta.getOperadorFiltro();
        String valorEsperado = pregunta.getValorFiltro();
 
        String respuestaPadre = respuestasAnterior.get(idPadre);
        
        if (respuestaPadre == null) {
         
            return false;
        }
        
        return FiltroEvaluador.debeVisualizar(respuestaPadre, operador, valorEsperado);
    }

    private void registrarRespuestaNoAplicable(Pregunta pregunta, int idParticipante) {
        Respuesta r = new Respuesta();
        r.setIdParticipante(idParticipante);
        r.setIdEncuesta(encuesta.getIdEncuesta());
        r.setIdPregunta(pregunta.getIdPregunta());
        r.setContenido("N/A");  
        r.setFechaRespuesta(LocalDateTime.now());
        r.setEsValida(false);  
        
        respuestas.agregar(r);
    }

    private String obtenerRespuesta(Scanner scanner, int numOpciones) {
        String respuesta;
        
        if (numOpciones > 0) {
          
            while (true) {
                System.out.print("Ingrese su respuesta (1-" + numOpciones + "): ");
                try {
                    int opcion = Integer.parseInt(scanner.nextLine());
                    if (opcion >= 1 && opcion <= numOpciones) {
                        respuesta = String.valueOf(opcion);
                        break;
                    }
                } catch (NumberFormatException e) {
                
                }
                System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } else {
          
            System.out.print("Ingrese su respuesta: ");
            respuesta = scanner.nextLine();
        }
        
        return respuesta;
    }
}

