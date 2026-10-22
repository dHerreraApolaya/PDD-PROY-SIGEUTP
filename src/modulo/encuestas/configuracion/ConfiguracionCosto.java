package modulo.encuestas.configuracion;

/**
 * Clase para configurar el costo de retribucion por encuesta respondida
 * Permite al administrador establecer montos para cada encuesta
 */
public class ConfiguracionCosto {
    private int idEncuesta;
    private double costoPorRespuesta;
    private String tipoMoneda; // PEN, USD, etc
    private boolean estaActivo;
    private String fechaConfiguracion;

    public ConfiguracionCosto() {
        this.tipoMoneda = "PEN";
        this.estaActivo = true;
        this.costoPorRespuesta = 0.0;
    }

    public ConfiguracionCosto(int idEncuesta, double costoPorRespuesta) {
        this();
        this.idEncuesta = idEncuesta;
        this.costoPorRespuesta = costoPorRespuesta;
    }

    // Getters y Setters
    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public double getCostoPorRespuesta() {
        return costoPorRespuesta;
    }

    public void setCostoPorRespuesta(double costoPorRespuesta) {
        this.costoPorRespuesta = costoPorRespuesta;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public boolean isEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }

    public String getFechaConfiguracion() {
        return fechaConfiguracion;
    }

    public void setFechaConfiguracion(String fechaConfiguracion) {
        this.fechaConfiguracion = fechaConfiguracion;
    }

    @Override
    public String toString() {
        return "Encuesta ID: " + idEncuesta + " | Costo: S/. " + String.format("%.2f", costoPorRespuesta) + 
               " | Activo: " + estaActivo;
    }
}
