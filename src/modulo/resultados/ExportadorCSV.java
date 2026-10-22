package modulo.resultados;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import modulo.preguntas.*;
import modulo.respuestas.*;

public class ExportadorCSV {

    private static final String Ruta_Base = "D:/DISCO D/Documentos/NetBeansProjects/SIGEUTP1234/test/ExportadosCSV/";

    public static void exportarPreguntas(ListaPregunta lista, String nombreArchivo) {
        if (lista == null || lista.getCabeza() == null) {
            System.out.println("No hay preguntas para exportar.");
            return;
        }

        String archivo = nombreArchivo.endsWith(".csv") ? nombreArchivo : nombreArchivo + ".csv";
        String rutaCompleta = Ruta_Base + archivo;

        new File(Ruta_Base).mkdirs();

        try (FileWriter writer = new FileWriter(rutaCompleta)) {
            writer.append("ID;Texto;Tipo;Categoria;Filtro;Activa\n");

            NodoPregunta actual = lista.getCabeza();
            while (actual != null) {
                Pregunta p = actual.getPregunta();
                writer.append(p.getIdPregunta() + ";");
                writer.append(escapar(p.getTexto()) + ";");
                writer.append(p.getTipo().name() + ";");
                writer.append(p.getCategoria() + ";");
                writer.append((p.isFiltro() ? "Si" : "No") + ";");
                writer.append((p.isActiva() ? "Activa" : "Inactiva") + "\n");
                actual = actual.getSiguiente();
            }
            writer.flush();
            System.out.println("Preguntas exportadas correctamente en: " + rutaCompleta);
        } catch (IOException e) {
            System.out.println("Error al exportar preguntas: " + e.getMessage());
        }
    }

    public static void exportarRespuestas(ListaRespuesta lista, String nombreArchivo) {
        if (lista == null || lista.getCabeza() == null) {
            System.out.println("No hay respuestas para exportar.");
            return;
        }

        String archivo = nombreArchivo.endsWith(".csv") ? nombreArchivo : nombreArchivo + ".csv";

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.append("ID Respuesta;ID Pregunta;Contenido\n");

            NodoRespuesta actual = lista.getCabeza();
            while (actual != null) {
                Respuesta r = actual.getRespuesta();
                writer.append(r.getIdRespuesta() + ";");
                writer.append(r.getIdPregunta() + ";");
                writer.append(escapar(r.getContenido()) + "\n");
                actual = actual.getSiguiente();
            }

            writer.flush();
            System.out.println("Respuestas exportadas correctamente en: " + archivo);
        } catch (IOException e) {
            System.out.println("Error al exportar respuestas: " + e.getMessage());
        }
    }

    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        texto = texto.replace(";", ",");
        if (texto.contains("\"") || texto.contains(",") || texto.contains("\n")) {
            texto = "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
