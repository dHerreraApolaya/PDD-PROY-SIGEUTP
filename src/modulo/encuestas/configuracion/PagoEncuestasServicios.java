package modulo.encuestas.configuracion;

import modulo.participantes.Participante;
import modulo.participantes.ParticipanteDAO;
import modulo.preguntas.PreguntaDAO;
import modulo.respuestas.Respuesta;
import modulo.respuestas.RespuestaDAO;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Servicio para gestionar pagos y generar reportes de encuestas respondidas
 * Implementa REQMS-020, REQMS-021, REQMS-022
 */
public class PagoEncuestasServicios {

    private static final ConfiguracionCostoDAO costosDAO = ConfiguracionCostoDAO.getInstancia();
    private static final RegistroPagoDAO pagosDAO = RegistroPagoDAO.getInstancia();
    private static final RespuestaDAO respuestaDAO = new RespuestaDAO(PreguntaDAO.getInstancia());
    private static final ParticipanteDAO participanteDAO = new ParticipanteDAO();
    private static final EncuestaDAO encuestaDAO = EncuestaDAO.getInstancia();

    // ============ REQMS-020: CONFIGURAR COSTO POR ENCUESTA ============

    public static void configurarCostoPorEncuesta(Scanner scanner) {
        try {
            System.out.println("\n=== CONFIGURAR COSTO POR ENCUESTA RESPONDIDA ===");
            System.out.println("(Admin only - Requiere permisos de administrador)");
            
            ListaEncuesta encuestas = encuestaDAO.getListaEncuestas();
            if (encuestas == null || encuestas.getCabeza() == null) {
                System.out.println("No hay encuestas disponibles.");
                return;
            }
            
            // Mostrar encuestas disponibles
            System.out.println("\nEncuestas disponibles:");
            NodoEncuesta nodo = encuestas.getCabeza();
            int contador = 1;
            while (nodo != null) {
                Encuesta e = nodo.getEncuesta();
                ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(e.getIdEncuesta());
                String costo = config != null ? String.format("S/. %.2f", config.getCostoPorRespuesta()) : "No configurado";
                System.out.println(contador + ". " + e.getTitulo() + " (ID: " + e.getIdEncuesta() + ") - Costo actual: " + costo);
                nodo = nodo.getSiguiente();
                contador++;
            }
            
            System.out.print("\nSelecciona ID de encuesta: ");
            int idEncuesta = Integer.parseInt(scanner.nextLine().trim());
            
            Encuesta encuesta = encuestaDAO.buscarPorId(idEncuesta);
            if (encuesta == null) {
                System.out.println("Error: Encuesta no encontrada");
                return;
            }
            
            System.out.print("Ingresa costo por respuesta (S/.): ");
            double costo = Double.parseDouble(scanner.nextLine().trim());
            
            if (costo < 0) {
                System.out.println("Error: El costo no puede ser negativo");
                return;
            }
            
            ConfiguracionCosto configExistente = costosDAO.obtenerConfiguracionPorEncuesta(idEncuesta);
            if (configExistente != null) {
                costosDAO.actualizarConfiguracion(idEncuesta, costo);
            } else {
                ConfiguracionCosto config = new ConfiguracionCosto(idEncuesta, costo);
                config.setFechaConfiguracion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                costosDAO.agregarConfiguracion(config);
            }
            
            System.out.println("\n Configuracion exitosa!");
            System.out.println("Encuesta: " + encuesta.getTitulo());
            System.out.println("Costo por respuesta: S/. " + String.format("%.2f", costo));
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa valores validos");
        } catch (Exception e) {
            System.out.println("Error al configurar costo: " + e.getMessage());
        }
    }

    // ============ REQMS-021: GENERAR REPORTE DE ENCUESTAS RESPONDIDAS ============

    public static void generarReporteEncuestasRespondidas(Scanner scanner) {
        try {
            System.out.println("\n=== REPORTE DE ENCUESTAS RESPONDIDAS POR PARTICIPANTE ===");
            
            System.out.print("Ingresa ID del participante (o 0 para todos): ");
            int idParticipante = Integer.parseInt(scanner.nextLine().trim());
            
            boolean exportarCSV = false;
            System.out.print("Deseas exportar a CSV? (S/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();
            exportarCSV = respuesta.equals("S");
            
            if (idParticipante > 0) {
                generarReporteParticipanteUnico(idParticipante, exportarCSV);
            } else {
                generarReporteTodosParticipantes(exportarCSV);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un numero valido");
        } catch (Exception e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }

    private static void generarReporteParticipanteUnico(int idParticipante, boolean exportar) {
        Participante p = participanteDAO.obtenerParticipantePorId(idParticipante);
        if (p == null) {
            System.out.println("Error: Participante no encontrado");
            return;
        }
        
        List<Respuesta> respuestas = respuestaDAO.obtenerRespuestasParticipanteEncuesta(idParticipante, -1);
        if (respuestas == null || respuestas.isEmpty()) {
            System.out.println("No hay encuestas respondidas por este participante");
            return;
        }
        
        // Agrupar por encuesta
        Map<Integer, Integer> respuestasPorEncuesta = new java.util.HashMap<>();
        for (Respuesta r : respuestas) {
            int idEnc = r.getIdEncuesta();
            respuestasPorEncuesta.put(idEnc, respuestasPorEncuesta.getOrDefault(idEnc, 0) + 1);
        }
        
        System.out.println("\n=== REPORTE INDIVIDUAL ===");
        System.out.println("Participante: " + p.getNombre());
        System.out.println("ID: " + p.getId_participante());
        System.out.println("Correo: " + p.getCorreo());
        System.out.println("Telefono: " + p.getTelefono());
        System.out.println("\n--- ENCUESTAS RESPONDIDAS ---");
        
        double totalIngresos = 0;
        int contador = 1;
        
        for (Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
            int idEnc = entry.getKey();
            int cantRespuestas = entry.getValue();
            
            Encuesta encuesta = encuestaDAO.buscarPorId(idEnc);
            ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(idEnc);
            
            double costo = config != null ? config.getCostoPorRespuesta() : 0;
            double monto = cantRespuestas * costo;
            totalIngresos += monto;
            
            System.out.printf("%d. %s: %d preguntas  S/. %.2f = S/. %.2f\n",
                contador, 
                encuesta != null ? encuesta.getTitulo() : "Encuesta " + idEnc,
                cantRespuestas, costo, monto);
            contador++;
        }
        
        System.out.println("\n--- RESUMEN ECONOMICO ---");
        System.out.println("Total de encuestas respondidas: " + respuestasPorEncuesta.size());
        System.out.println("Total de preguntas respondidas: " + respuestas.size());
        System.out.printf("TOTAL A PAGAR: S/. %.2f\n", totalIngresos);
        
        if (exportar) {
            exportarReporteParticipanteCSV(idParticipante, p, respuestasPorEncuesta, totalIngresos);
        }
    }

    private static void generarReporteTodosParticipantes(boolean exportar) {
        System.out.println("\n=== REPORTE CONSOLIDADO - TODOS LOS PARTICIPANTES ===");
        
        List<RegistroPago> pagos = pagosDAO.obtenerTodos();
        if (pagos.isEmpty()) {
            System.out.println("No hay registros de pago en.");
        } else {
            double totalGlobal = 0;
            System.out.println("\n--- PAGOS REGISTRADOS ---");
            for (RegistroPago pago : pagos) {
                Participante p = participanteDAO.obtenerParticipantePorId(pago.getIdParticipante());
                System.out.printf("- %s: S/. %.2f (%s)\n", 
                    p != null ? p.getNombre() : "ID " + pago.getIdParticipante(),
                    pago.getMontoTotal(), pago.getEstado());
                totalGlobal += pago.getMontoTotal();
            }
            System.out.printf("\nTOTAL GLOBAL: S/. %.2f\n", totalGlobal);
        }
        
        if (exportar) {
            exportarReporteTodosCSV(pagos);
        }
    }

    private static void exportarReporteParticipanteCSV(int idParticipante, Participante p, 
                                                       Map<Integer, Integer> respuestasPorEncuesta, 
                                                       double totalIngresos) {
        try {
            String nombreArchivo = "reporte_encuestas_participante_" + idParticipante + "_" + 
                                 System.currentTimeMillis() + ".csv";
            File archivo = new File(nombreArchivo);
            PrintWriter writer = new PrintWriter(archivo);
            
            writer.println("\"REPORTE DE ENCUESTAS RESPONDIDAS POR PARTICIPANTE\"");
            writer.println("\"Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\"");
            writer.println();
            writer.println("\"DATOS DEL PARTICIPANTE\"");
            writer.println("\"Nombre\",\"" + p.getNombre() + "\"");
            writer.println("\"ID\",\"" + p.getId_participante() + "\"");
            writer.println("\"Correo\",\"" + p.getCorreo() + "\"");
            writer.println("\"Telefono\",\"" + p.getTelefono() + "\"");
            writer.println();
            
            writer.println("\"ENCUESTAS RESPONDIDAS\"");
            writer.println("\"ID_ENCUESTA\",\"TITULO_ENCUESTA\",\"PREGUNTAS_RESPONDIDAS\",\"COSTO_UNITARIO\",\"MONTO_TOTAL\"");
            
            for (Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
                int idEnc = entry.getKey();
                int cantRespuestas = entry.getValue();
                Encuesta encuesta = encuestaDAO.buscarPorId(idEnc);
                ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(idEnc);
                
                double costo = config != null ? config.getCostoPorRespuesta() : 0;
                double monto = cantRespuestas * costo;
                
                String titulo = encuesta != null ? encuesta.getTitulo() : "Encuesta " + idEnc;
                writer.printf("%d,\"%s\",%d,%.2f,%.2f\n", idEnc, titulo, cantRespuestas, costo, monto);
            }
            
            writer.println();
            writer.printf("\"TOTAL A PAGAR:\",\"S/. %.2f\"\n", totalIngresos);
            writer.close();
            
            System.out.println("\n Reporte exportado: " + nombreArchivo);
            
        } catch (Exception e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    private static void exportarReporteTodosCSV(List<RegistroPago> pagos) {
        try {
            String nombreArchivo = "reporte_pagos_consolidado_" + System.currentTimeMillis() + ".csv";
            File archivo = new File(nombreArchivo);
            PrintWriter writer = new PrintWriter(archivo);
            
            writer.println("\"REPORTE CONSOLIDADO DE PAGOS POR ENCUESTAS\"");
            writer.println("\"Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\"");
            writer.println();
            writer.println("\"NUMERO_RECIBO\",\"ID_PARTICIPANTE\",\"NOMBRE_PARTICIPANTE\",\"MONTO\",\"ESTADO\",\"FECHA_PAGO\"");
            
            double totalGlobal = 0;
            for (RegistroPago pago : pagos) {
                Participante p = participanteDAO.obtenerParticipantePorId(pago.getIdParticipante());
                String nombre = p != null ? p.getNombre() : "";
                writer.printf("\"%s\",%d,\"%s\",%.2f,\"%s\",\"%s\"\n",
                    pago.getNumeroRecibo(), pago.getIdParticipante(), nombre, 
                    pago.getMontoTotal(), pago.getEstado(), pago.getFechaPago());
                totalGlobal += pago.getMontoTotal();
            }
            
            writer.println();
            writer.printf("\"TOTAL PAGOS:\",\"\",\"\",\"%.2f\"\n", totalGlobal);
            writer.close();
            
            System.out.println("\n Reporte consolidado exportado: " + nombreArchivo);
            
        } catch (Exception e) {
            System.out.println("Error al exportar CSV consolidado: " + e.getMessage());
        }
    }

    // ============ REQMS-022: GENERAR RECIBO DE PAGO (ESTILO BOLETA SUNAT) ============

    public static void generarReciboPago(Scanner scanner) {
        try {
            System.out.println("\n=== GENERAR RECIBO DE PAGO ===");
            System.out.println("(Similar a Boleta Electronica SUNAT Peru)");
            
            System.out.print("Ingresa ID del participante: ");
            int idParticipante = Integer.parseInt(scanner.nextLine().trim());
            
            Participante p = participanteDAO.obtenerParticipantePorId(idParticipante);
            if (p == null) {
                System.out.println("Error: Participante no encontrado");
                return;
            }
            
            // Obtener encuestas respondidas
            List<Respuesta> respuestas = respuestaDAO.obtenerRespuestasParticipanteEncuesta(idParticipante, -1);
            if (respuestas == null || respuestas.isEmpty()) {
                System.out.println("Este participante no ha respondido encuestas aun");
                return;
            }
            
            // Agrupar por encuesta
            Map<Integer, Integer> respuestasPorEncuesta = new java.util.HashMap<>();
            for (Respuesta r : respuestas) {
                int idEnc = r.getIdEncuesta();
                respuestasPorEncuesta.put(idEnc, respuestasPorEncuesta.getOrDefault(idEnc, 0) + 1);
            }
            
            // Calcular total
            double totalMonto = 0;
            for (Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
                ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(entry.getKey());
                if (config != null) {
                    totalMonto += entry.getValue() * config.getCostoPorRespuesta();
                }
            }
            
            if (totalMonto == 0) {
                System.out.println("No hay encuestas con configuracion de costo");
                return;
            }
            
            System.out.print("Confirmar pago de S/. " + String.format("%.2f", totalMonto) + "? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (!confirmacion.equals("S")) {
                System.out.println("Operacion cancelada");
                return;
            }
            
            // Crear registro de pago
            RegistroPago pago = new RegistroPago(idParticipante, 0, totalMonto);
            pago.setMontoTotal(totalMonto);
            pago.setTotalEncuestasRespondidas(respuestasPorEncuesta.size());
            pago.setEstado("PAGADO");
            
            System.out.print("Metodo de pago (EFECTIVO/TRANSFERENCIA): ");
            String metodo = scanner.nextLine().trim().toUpperCase();
            if (metodo.isEmpty()) metodo = "EFECTIVO";
            pago.setMetodoPago(metodo);
            
            pagosDAO.registrarPago(pago);
            
            // Mostrar recibo
            mostrarReciboBoleta(p, respuestasPorEncuesta, pago);
            
            // Opcion de exportar
            System.out.print("\nDeseas exportar recibo a PDF/TXT? (S/N): ");
            String exportar = scanner.nextLine().trim().toUpperCase();
            if (exportar.equals("S")) {
                exportarReciboBoleta(p, respuestasPorEncuesta, pago);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un numero valido");
        } catch (Exception e) {
            System.out.println("Error al generar recibo: " + e.getMessage());
        }
    }

    private static void mostrarReciboBoleta(Participante p, Map<Integer, Integer> respuestasPorEncuesta, 
                                           RegistroPago pago) {
        System.out.println("\n");
        System.out.println("               BOLETA ELECTRONICA DE PAGO");
        System.out.println("         SISTEMA INTEGRADO DE GESTION DE ENCUESTAS (SIGEUTP)");
        System.out.println("===========================================================================");
        System.out.println();
        System.out.println("NUMERO DE RECIBO:  " + pago.getNumeroRecibo());
        System.out.println("FECHA DE EMISION:  " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        System.out.println();
        System.out.println("===========================================================================");
        System.out.println("DATOS DEL PARTICIPANTE");
        System.out.println("===========================================================================");
        System.out.println("NOMBRE:            " + p.getNombre());
        System.out.println("DOCUMENTO:         " + (p.getId_participante() > 0 ? String.format("%08d", p.getId_participante()) : "N/A"));
        System.out.println("CORREO:            " + p.getCorreo());
        System.out.println("TELEFONO:          " + p.getTelefono());
        System.out.println();
        System.out.println("===========================================================================");
        System.out.println("DETALLE DE ENCUESTAS RESPONDIDAS");
        System.out.println("===========================================================================");
        System.out.println(String.format("%-40s %10s %12s %12s", "DESCRIPCION", "CANTIDAD", "V.UNITARIO", "SUBTOTAL"));
        System.out.println("===========================================================================");
        
        double totalNeto = 0;
        for (Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
            int idEnc = entry.getKey();
            int cantRespuestas = entry.getValue();
            
            Encuesta encuesta = encuestaDAO.buscarPorId(idEnc);
            ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(idEnc);
            
            double costo = config != null ? config.getCostoPorRespuesta() : 0;
            double subtotal = cantRespuestas * costo;
            totalNeto += subtotal;
            
            String titulo = encuesta != null ? encuesta.getTitulo() : "Encuesta " + idEnc;
            if (titulo.length() > 40) {
                titulo = titulo.substring(0, 37) + "...";
            }
            
            System.out.printf("%-40s %10d S/. %9.2f S/. %9.2f\n", titulo, cantRespuestas, costo, subtotal);
        }
        
        System.out.println("=========================================================================");
        System.out.println(String.format("%-40s %33s S/. %9.2f", "TOTAL NETO", "", totalNeto));
        System.out.println(String.format("%-40s %33s S/. %9.2f", "IGV (18%)", "", totalNeto * 0.18));
        System.out.println("=========================================================================");
        System.out.println(String.format("%-40s %33s S/. %9.2f", "TOTAL A PAGAR", "", pago.getMontoTotal()));
        System.out.println("=========================================================================");
        System.out.println();
        System.out.println("METODO DE PAGO:    " + pago.getMetodoPago());
        System.out.println("ESTADO:            " + pago.getEstado());
        System.out.println("FECHA DE PAGO:     " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        System.out.println();
        System.out.println("=========================================================================");
        System.out.println("Esta boleta es un comprobante de pago valido por las encuestas");
        System.out.println("respondidas en el periodo indicado. Conserve para sus registros.");
        System.out.println("=========================================================================");
    }

    private static void exportarReciboBoleta(Participante p, Map<Integer, Integer> respuestasPorEncuesta, 
                                            RegistroPago pago) {
        try {
            String nombreArchivo = "recibo_pago_" + pago.getNumeroRecibo().replace("-", "_") + ".txt";
            File archivo = new File(nombreArchivo);
            PrintWriter writer = new PrintWriter(archivo);
            
            writer.println("==============================================================================");
            writer.println("               BOLETA ELECTRONICA DE PAGO");
            writer.println("         SISTEMA INTEGRADO DE GESTION DE ENCUESTAS (SIGEUTP)");
            writer.println("==============================================================================");
            writer.println();
            writer.println("NUMERO DE RECIBO:  " + pago.getNumeroRecibo());
            writer.println("FECHA DE EMISION:  " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println();
            writer.println("==============================================================================");
            writer.println("DATOS DEL PARTICIPANTE");
            writer.println("==============================================================================");
            writer.println("NOMBRE:            " + p.getNombre());
            writer.println("DOCUMENTO:         " + (p.getId_participante() > 0 ? String.format("%08d", p.getId_participante()) : "N/A"));
            writer.println("CORREO:            " + p.getCorreo());
            writer.println("TELEFONO:          " + p.getTelefono());
            writer.println();
            writer.println("==============================================================================");
            writer.println("DETALLE DE ENCUESTAS RESPONDIDAS");
            writer.println("==============================================================================");
            writer.println(String.format("%-40s %10s %12s %12s", "DESCRIPCION", "CANTIDAD", "V.UNITARIO", "SUBTOTAL"));
            writer.println("==============================================================================");
            
            double totalNeto = 0;
            for (Map.Entry<Integer, Integer> entry : respuestasPorEncuesta.entrySet()) {
                int idEnc = entry.getKey();
                int cantRespuestas = entry.getValue();
                
                Encuesta encuesta = encuestaDAO.buscarPorId(idEnc);
                ConfiguracionCosto config = costosDAO.obtenerConfiguracionPorEncuesta(idEnc);
                
                double costo = config != null ? config.getCostoPorRespuesta() : 0;
                double subtotal = cantRespuestas * costo;
                totalNeto += subtotal;
                
                String titulo = encuesta != null ? encuesta.getTitulo() : "Encuesta " + idEnc;
                if (titulo.length() > 40) {
                    titulo = titulo.substring(0, 37) + "...";
                }
                
                writer.printf("%-40s %10d S/. %9.2f S/. %9.2f\n", titulo, cantRespuestas, costo, subtotal);
            }
            
            writer.println("============================================================================");
            writer.println(String.format("%-40s %33s S/. %9.2f", "TOTAL NETO", "", totalNeto));
            writer.println(String.format("%-40s %33s S/. %9.2f", "IGV (18%)", "", totalNeto * 0.18));
            writer.println("============================================================================");
            writer.println(String.format("%-40s %33s S/. %9.2f", "TOTAL A PAGAR", "", pago.getMontoTotal()));
            writer.println("============================================================================");
            writer.println();
            writer.println("METODO DE PAGO:    " + pago.getMetodoPago());
            writer.println("ESTADO:            " + pago.getEstado());
            writer.println("FECHA DE PAGO:     " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println();
            writer.println("============================================================================");
            writer.println("Esta boleta es un comprobante de pago valido por las encuestas");
            writer.println("respondidas en el periodo indicado. Conserve para sus registros.");
            writer.println("============================================================================");
            writer.close();
            
            System.out.println(" Recibo exportado: " + nombreArchivo);
            
        } catch (Exception e) {
            System.out.println("Error al exportar recibo: " + e.getMessage());
        }
    }
}
