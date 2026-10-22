package modulo.participantes;

import java.time.LocalDateTime;

public class Participante {

    private int id_participante;
    private String dni;
    private String nombre;
    private String correo;
    private int idperfil;
    private int edad;
    private String sexo;
    private String nivelEducativo;
    private String grupoEdad;
    private double sueldo;
    private String ubicacion;  
    private String departamento;
    private LocalDateTime fechaRegistro;
    private String estadoCivil;
    private String telefono;
    private String empresa;
    private String estado;
    private LocalDateTime fechaActualizacion;

    public Participante(String dni, String nombre, String correo) {
        this.dni = dni;
        this.nombre = nombre;
        this.correo = correo;
        this.nivelEducativo = "No especificado";
        this.grupoEdad = "No especificado";
        this.estadoCivil = "No especificado";
        this.estado = "ACTIVO";
        this.fechaRegistro = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
              if (edad <= 11) {
            this.grupoEdad = "Nino(0-11)";
        } else if (edad <= 17) {
            this.grupoEdad = "Adolescente(12-17)";
        } else if (edad <= 29) {
            this.grupoEdad = "Joven(18-29)";
        } else if (edad <= 59) {
            this.grupoEdad = "Adulto(30-59)";
        } else {
            this.grupoEdad = "Adulto Mayor(60+)";
        }
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPerfil() {
        return sexo; 
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public String getGrupoEdad() {
        return grupoEdad;
    }

    public void setGrupoEdad(String grupoEdad) {
        this.grupoEdad = grupoEdad;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getCiudad() {
        return ubicacion;
    }

    public void setCiudad(String ciudad) {
        this.ubicacion = ciudad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getId_participante() {
        return id_participante;
    }

    public void setId_participante(int id_participante) {
        this.id_participante = id_participante;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(int idperfil) {
        this.idperfil = idperfil;
    }

    @Override
    public String toString() {
        return "Participante{" +
                "id_participante=" + id_participante +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", edad=" + edad +
                ", grupoEdad='" + grupoEdad + '\'' +
                ", sexo='" + sexo + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", departamento='" + departamento + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
