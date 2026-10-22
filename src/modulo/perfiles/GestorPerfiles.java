package modulo.perfiles;

import java.util.*;

public class GestorPerfiles {

    private Map<String, Perfil> perfiles = new HashMap<>();
    private List<String> permisosDisponibles = Arrays.asList(
            "CREAR_ENCUESTA",
            "EDITAR_ENCUESTA",
            "ELIMINAR_ENCUESTA",
            "VER_ENCUESTA",
            "CREAR_PREGUNTAS",
            "EDITAR_PREGUNTAS",
            "ELIMINAR_PREGUNTAS",
            "VER_PREGUNTAS",
            "CREAR_PARTICIPANTES",
            "EDITAR_PARTICIPANTES",
            "ELIMINAR_PARTICIPANTES",
            "VER_PARTICIPANTES",
            "EJECUTAR_ENCUESTA",
            "VER_RESULTADOS",
            "EXPORTAR_RESULTADOS",
            "GESTIONAR_PERFILES",
            "GESTIONAR_PERMISOS"
    );

    public GestorPerfiles() {
        inicializarPerfilesPredeterminados();
        cargarUsuariosExternosDesdebd();
    }

    private void inicializarPerfilesPredeterminados() {
      
        Perfil admin = new Perfil("Administrador");
        admin.agregarPermiso("CREAR_ENCUESTA");
        admin.agregarPermiso("EDITAR_ENCUESTA");
        admin.agregarPermiso("ELIMINAR_ENCUESTA");
        admin.agregarPermiso("VER_ENCUESTA");
        admin.agregarPermiso("CREAR_PREGUNTAS");
        admin.agregarPermiso("EDITAR_PREGUNTAS");
        admin.agregarPermiso("ELIMINAR_PREGUNTAS");
        admin.agregarPermiso("VER_PREGUNTAS");
        admin.agregarPermiso("CREAR_PARTICIPANTES");
        admin.agregarPermiso("EDITAR_PARTICIPANTES");
        admin.agregarPermiso("ELIMINAR_PARTICIPANTES");
        admin.agregarPermiso("VER_PARTICIPANTES");
        admin.agregarPermiso("EJECUTAR_ENCUESTA");
        admin.agregarPermiso("VER_RESULTADOS");
        admin.agregarPermiso("EXPORTAR_RESULTADOS");
        admin.agregarPermiso("GESTIONAR_PERFILES");
        admin.agregarPermiso("GESTIONAR_PERMISOS");
        perfiles.put(admin.getNombrePerfil(), admin);

        Perfil auditor = new Perfil("Auditor");
        auditor.agregarPermiso("VER_ENCUESTA");
        auditor.agregarPermiso("VER_PREGUNTAS");
        auditor.agregarPermiso("VER_PARTICIPANTES");
        auditor.agregarPermiso("VER_RESULTADOS");
        auditor.agregarPermiso("EXPORTAR_RESULTADOS");
        perfiles.put(auditor.getNombrePerfil(), auditor);

        Perfil externo = new Perfil("Externo");
        perfiles.put(externo.getNombrePerfil(), externo);

        Perfil participante = new Perfil("Participante");
        participante.agregarPermiso("EJECUTAR_ENCUESTA");
        perfiles.put(participante.getNombrePerfil(), participante);
    }

    public boolean crearPerfilPersonalizado(String nombre, List<String> permisosSeleccionados) {
        if (nombre == null || nombre.trim().isEmpty() || perfiles.containsKey(nombre)) {
            return false;
        }

        Perfil nuevo = new Perfil(nombre.trim());

        for (String permiso : permisosSeleccionados) {
            nuevo.agregarPermiso(permiso);
        }

        perfiles.put(nuevo.getNombrePerfil(), nuevo);
        return true;
    }

    public Perfil obtenerPerfil(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        
        String nombreTrim = nombre.trim();
        
        if (perfiles.containsKey(nombreTrim)) {
            return perfiles.get(nombreTrim);
        }
        
        for (String key : perfiles.keySet()) {
            if (key.equalsIgnoreCase(nombreTrim)) {
                return perfiles.get(key);
            }
        }
        
        return null;
    }

    public Set<String> listarNombresPerfiles() {
        return perfiles.keySet();
    }

    public List<String> getPermisosDisponibles() {
        return permisosDisponibles;
    }

    public boolean perfilTienePermiso(String nombrePerfil, String permiso) {
        Perfil perfil = perfiles.get(nombrePerfil);
        return perfil != null && perfil.tienePermiso(permiso);
    }

    public void mostrarTodosLosPerfiles() {
        for (Perfil perfil : perfiles.values()) {
            System.out.println("Perfil: " + perfil.getNombrePerfil());
            perfil.mostrarPermisos();
            System.out.println();
        }
    }

    private void cargarUsuariosExternosDesdebd() {
        PerfilDAOBD perfilDAO = new PerfilDAOBD();
        List<Perfil> perfilesDesdebd = perfilDAO.cargarTodosPerfiles();
        
        for (Perfil perfil : perfilesDesdebd) {
            String nombre = perfil.getNombrePerfil();
           
            if (!nombre.equals("Administrador") && 
                !nombre.equals("Auditor") && 
                !nombre.equals("Participante") && 
                !nombre.equals("Externo")) {
                
                if (!perfiles.containsKey(nombre)) {
                    perfiles.put(nombre, perfil);
                }
            }
        }
    }
}
