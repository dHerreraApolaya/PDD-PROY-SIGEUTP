package modulo.encuestas.configuracion;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar configuracion de costos por encuesta
 * Permite crear, editar, listar configuraciones
 */
public class ConfiguracionCostoDAO {
    private List<ConfiguracionCosto> configuraciones = new ArrayList<>();
    private static ConfiguracionCostoDAO instancia;

    private ConfiguracionCostoDAO() {
    }

    public static ConfiguracionCostoDAO getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionCostoDAO();
        }
        return instancia;
    }

    /**
     * Agregar nueva configuracion de costo
     */
    public void agregarConfiguracion(ConfiguracionCosto config) {
        if (config == null) {
            System.out.println("Error: Configuracin nula");
            return;
        }
        
        // Validar que no exista configuracion previa
        for (ConfiguracionCosto c : configuraciones) {
            if (c.getIdEncuesta() == config.getIdEncuesta()) {
                System.out.println("Error: Ya existe configuracion para esta encuesta");
                return;
            }
        }
        
        configuraciones.add(config);
        System.out.println(" Configuracion agregada: Encuesta " + config.getIdEncuesta() + 
                         " | Costo: S/. " + String.format("%.2f", config.getCostoPorRespuesta()));
    }

    /**
     * Obtener configuracion por ID de encuesta
     */
    public ConfiguracionCosto obtenerConfiguracionPorEncuesta(int idEncuesta) {
        for (ConfiguracionCosto c : configuraciones) {
            if (c.getIdEncuesta() == idEncuesta) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualizar configuracion existente
     */
    public void actualizarConfiguracion(int idEncuesta, double nuevoCosto) {
        ConfiguracionCosto config = obtenerConfiguracionPorEncuesta(idEncuesta);
        if (config != null) {
            config.setCostoPorRespuesta(nuevoCosto);
            System.out.println(" Configuracion actualizada: Encuesta " + idEncuesta + 
                             " | Nuevo costo: S/. " + String.format("%.2f", nuevoCosto));
        } else {
            System.out.println("Error: No existe configuracion para encuesta " + idEncuesta);
        }
    }

    /**
     * Listar todas las configuraciones activas
     */
    public void listarConfiguraciones() {
        if (configuraciones.isEmpty()) {
            System.out.println("No hay configuraciones de costo registradas.");
            return;
        }
        
        System.out.println("\n=== CONFIGURACIONES DE COSTO POR ENCUESTA ===");
        int numero = 1;
        for (ConfiguracionCosto c : configuraciones) {
            System.out.println(numero + ". " + c);
            numero++;
        }
    }

    /**
     * Obtener todas las configuraciones
     */
    public List<ConfiguracionCosto> obtenerTodas() {
        return new ArrayList<>(configuraciones);
    }

    /**
     * Desactivar configuracion
     */
    public void desactivarConfiguracion(int idEncuesta) {
        ConfiguracionCosto config = obtenerConfiguracionPorEncuesta(idEncuesta);
        if (config != null) {
            config.setEstaActivo(false);
            System.out.println(" Configuracion desactivada: Encuesta " + idEncuesta);
        }
    }

    /**
     * Eliminar configuracion
     */
    public void eliminarConfiguracion(int idEncuesta) {
        configuraciones.removeIf(c -> c.getIdEncuesta() == idEncuesta);
        System.out.println(" Configuracion eliminada: Encuesta " + idEncuesta);
    }

    /**
     * Contar configuraciones activas
     */
    public int contarConfiguracionesActivas() {
        return (int) configuraciones.stream().filter(ConfiguracionCosto::isEstaActivo).count();
    }
}
