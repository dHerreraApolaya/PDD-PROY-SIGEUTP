package modulo.participantes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoParticipantesDAO {
    private static final String ARCHIVO_DATOS = "participantes.dat";
  
    public static void guardarParticipantes(List<Participante> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(new ArrayList<>(lista));
            System.out.println("Participantes guardados en archivo: " + ARCHIVO_DATOS);
        } catch (IOException e) {
            System.out.println("Error al guardar archivo de participantes: " + e.getMessage());
        }
    }
  
    @SuppressWarnings("unchecked")
    public static List<Participante> cargarParticipantes() {
        List<Participante> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_DATOS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de participantes no encontrado. Iniciando con lista vaci­a.");
            return lista;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            lista = (List<Participante>) ois.readObject();
            System.out.println("Participantes cargados desde archivo: " + lista.size() + " registros");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar archivo de participantes: " + e.getMessage());
        }
        
        return lista;
    }
  
    public static boolean archivoExiste() {
        return new File(ARCHIVO_DATOS).exists();
    }
  
    public static void limpiarArchivo() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists() && archivo.delete()) {
            System.out.println("Archivo de participantes eliminado.");
        }
    }
}
