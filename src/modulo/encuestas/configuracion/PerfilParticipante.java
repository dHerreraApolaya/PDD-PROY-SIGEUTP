package modulo.encuestas.configuracion;

import java.util.List;
import modulo.participantes.Participante;

public class PerfilParticipante {

    private Integer edadMinima;
    private Integer edadMaxima;
    private String gruposEdadPermitidos; 
    private String sexoPermitido;
    private String estadoCivilPermitido;      
    private Double sueldoMinimo;
    private List<String> ubicacionesPermitidas;

    public PerfilParticipante() {
    }

    public PerfilParticipante(Integer edadMinima, Integer edadMaxima, String gruposEdadPermitidos,
            String sexoPermitido, String estadoCivilPermitido, Double sueldoMinimo, 
            List<String> ubicacionesPermitidas) {
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.gruposEdadPermitidos = gruposEdadPermitidos;
        this.sexoPermitido = sexoPermitido;
        this.estadoCivilPermitido = estadoCivilPermitido;
        this.sueldoMinimo = sueldoMinimo;
        this.ubicacionesPermitidas = ubicacionesPermitidas;
    }

    public boolean cumplePerfil(Participante participante) {
        return cumpleEdad(participante.getEdad())
                && cumpleGrupoEdad(participante.getGrupoEdad())
                && cumpleSexo(participante.getSexo())
                && cumpleEstadoCivil(participante.getEstadoCivil())
                && cumpleSueldo(participante.getSueldo())
                && cumpleUbicacion(participante.getUbicacion());
    }

    private boolean cumpleEdad(int edad) {
        if (edadMinima != null && edad < edadMinima) {
            return false;
        }
        if (edadMaxima != null && edad > edadMaxima) {
            return false;
        }
        return true;
    }

    private boolean cumpleGrupoEdad(String grupoEdad) {
        if (gruposEdadPermitidos == null || gruposEdadPermitidos.isEmpty()) {
            return true;
        }
        return gruposEdadPermitidos.contains(grupoEdad);
    }

    private boolean cumpleSexo(String sexo) {
        if (sexoPermitido == null || sexoPermitido.equalsIgnoreCase("Todos")) {
            return true;
        }
        return sexoPermitido.equalsIgnoreCase(sexo);
    }

    private boolean cumpleEstadoCivil(String estadoCivil) {
        if (estadoCivilPermitido == null || estadoCivilPermitido.equalsIgnoreCase("Todos")) {
            return true;
        }
        return estadoCivilPermitido.contains(estadoCivil);
    }

    private boolean cumpleSueldo(double sueldo) {
        if (sueldoMinimo == null) {
            return true;
        }
        return sueldo >= sueldoMinimo;
    }

    private boolean cumpleUbicacion(String ubicacion) {
        if (ubicacionesPermitidas == null || ubicacionesPermitidas.isEmpty()) {
            return true;
        }
        return ubicacionesPermitidas.contains(ubicacion);
    }

    // Getters y Setters
    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public String getGruposEdadPermitidos() {
        return gruposEdadPermitidos;
    }

    public void setGruposEdadPermitidos(String gruposEdadPermitidos) {
        this.gruposEdadPermitidos = gruposEdadPermitidos;
    }

    public String getSexoPermitido() {
        return sexoPermitido;
    }

    public void setSexoPermitido(String sexoPermitido) {
        this.sexoPermitido = sexoPermitido;
    }

    public String getEstadoCivilPermitido() {
        return estadoCivilPermitido;
    }

    public void setEstadoCivilPermitido(String estadoCivilPermitido) {
        this.estadoCivilPermitido = estadoCivilPermitido;
    }

    public Double getSueldoMinimo() {
        return sueldoMinimo;
    }

    public void setSueldoMinimo(Double sueldoMinimo) {
        this.sueldoMinimo = sueldoMinimo;
    }

    public List<String> getUbicacionesPermitidas() {
        return ubicacionesPermitidas;
    }

    public void setUbicacionesPermitidas(List<String> ubicacionesPermitidas) {
        this.ubicacionesPermitidas = ubicacionesPermitidas;
    }
}
