package modulo.perfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PerfilController {

    private GestorPerfiles gestor;
    private Scanner scanner = new Scanner(System.in);

    public PerfilController(GestorPerfiles gestor) {
        this.gestor = gestor;
    }

    public void crearPerfil() {
        System.out.print("Ingrese nombre del perfil: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("Error: El nombre del perfil no puede estar vacio.");
            return;
        }

        if (nombre.length() > 50) {
            System.out.println("Error: El nombre del perfil no puede exceder los 50 caracteres.");
            return;
        }

        if (gestor.obtenerPerfil(nombre) != null) {
            System.out.println("Error: El perfil ya existe.");
            return;
        }

        if (List.of("ADMIN", "PARTICIPANTE", "SUPERVISOR").contains(nombre.toUpperCase())) {
            System.out.println("Este perfil ya esta predefinido en el sistema.");
            return;
        }

        List<String> permisosSeleccionados = new ArrayList<>();
        System.out.println("Seleccione permisos para el nuevo perfil (escriba 'fin' para terminar):");
        for (String permiso : gestor.getPermisosDisponibles()) {
            System.out.print("Agregar permiso '" + permiso + "'? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) {
                permisosSeleccionados.add(permiso);
            }
        }

        boolean creado = gestor.crearPerfilPersonalizado(nombre, permisosSeleccionados);
        if (creado) {
            System.out.println("Perfil personalizado creado correctamente.");
        } else {
            System.out.println("No se pudo crear el perfil.");
        }
    }

    public void asignarPermiso() {
      
        System.out.println("\n=== ASIGNAR PERMISOS A UN PERFIL ===");
        System.out.println("Opciones:");
        System.out.println("1. Crear nuevo usuario EXTERNO con permisos personalizados");
        System.out.println("2. Asignar permisos a un perfil existente");
        System.out.print("Seleccione opcion (1-2): ");
        
        String opcion = scanner.nextLine().trim();
        
        if (opcion.equals("1")) {
            crearExternoConPermisos();
        } else if (opcion.equals("2")) {
            asignarPermisosAPerfilExistente();
        } else {
            System.out.println("Opcion invalida.");
        }
    }
    
    private void crearExternoConPermisos() {
        PerfilDAOBD perfilDAO = new PerfilDAOBD();
        
        System.out.println("\n=== CREAR NUEVO USUARIO EXTERNO ===");
        System.out.print("Ingrese nombre/identificacion del usuario externo: ");
        String nombreExterno = scanner.nextLine().trim();
        
        if (nombreExterno.isEmpty()) {
            System.out.println("Error: El nombre no puede estar vacÃ­o.");
            return;
        }
        
        System.out.println("\nCreando perfil '" + nombreExterno + "' en base de datos...");
        if (!perfilDAO.crearPerfilEnBD(nombreExterno, "Usuario externo: " + nombreExterno)) {
            System.out.println("Error: No se pudo crear el perfil en BD.");
            return;
        }
        
        if (!gestor.crearPerfilPersonalizado(nombreExterno, new ArrayList<>())) {
            System.out.println("Nota: Perfil ya existe en memoria.");
        }
        
        System.out.println("\nSeleccione los permisos que desea asignar al usuario '" + nombreExterno + "':");
        System.out.println("=================================\n");
        
        List<String> permisosDisponibles = gestor.getPermisosDisponibles();
        List<String> permisosSeleccionados = new ArrayList<>();
        
        for (String permiso : permisosDisponibles) {
            System.out.print("Asignar permiso '" + permiso + "'? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) {
                permisosSeleccionados.add(permiso);
            }
        }
        
        System.out.println("\nGuardando permisos en BD...");
        int permisosGuardados = 0;
        for (String permiso : permisosSeleccionados) {
            boolean resultado = perfilDAO.asignarPermisoAPerfil(nombreExterno, permiso);
            if (resultado) {
                permisosGuardados++;
            }
        }
        
        System.out.println("\nâœ… Usuario externo '" + nombreExterno + "' creado correctamente.");
        System.out.println("Permisos asignados: " + permisosGuardados + "/" + permisosSeleccionados.size());
        
        if (permisosSeleccionados.size() > 0) {
            System.out.println("\n Permisos asignados a '" + nombreExterno + "':");
            for (String p : permisosSeleccionados) {
                System.out.println("  âœ“ " + p);
            }
        }
    }
  
    private void asignarPermisosAPerfilExistente() {
        System.out.println("\n=== ASIGNAR PERMISOS A PERFIL EXISTENTE ===");
        System.out.println("Perfiles disponibles:");
        
        List<String> nombresPerfiles = new ArrayList<>(gestor.listarNombresPerfiles());
        for (int i = 0; i < nombresPerfiles.size(); i++) {
            System.out.println((i + 1) + ". " + nombresPerfiles.get(i));
        }
        
        System.out.print("Seleccione el numero del perfil: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            if (opcion < 1 || opcion > nombresPerfiles.size()) {
                System.out.println("Opcion invalida.");
                return;
            }
            String nombrePerfil = nombresPerfiles.get(opcion - 1);
            Perfil perfil = gestor.obtenerPerfil(nombrePerfil);
            
            if (perfil == null) {
                System.out.println("Error: Perfil no encontrado.");
                return;
            }
          
            if (List.of("Administrador", "Auditor", "Participante").contains(nombrePerfil)) {
                System.out.println("\n  Advertencia: '" + nombrePerfil + "' es un perfil predefinido.");
                System.out.println("Los cambios que realice pueden afectar la funcionalidad del sistema.");
                System.out.print(" Desea continuar? (s/n): ");
                if (!scanner.nextLine().trim().toLowerCase().equals("s")) {
                    System.out.println("Operacion cancelada.");
                    return;
                }
            }
            
            asignarPermisosAlPerfil(perfil, nombrePerfil);
            
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Debe ingresar un numero.");
        }
    }
  
    private void asignarPermisosAlPerfil(Perfil perfil, String nombrePerfil) {
        PerfilDAOBD perfilDAO = new PerfilDAOBD();
        
        System.out.println("\n=== ASIGNAR PERMISOS AL PERFIL: " + nombrePerfil.toUpperCase() + " ===");
        System.out.println("Seleccione los permisos que desea asignar:\n");
        
        List<String> permisosDisponibles = gestor.getPermisosDisponibles();
        List<String> permisosAsignados = new ArrayList<>();
        
        for (String permiso : permisosDisponibles) {
            System.out.print("Asignar permiso '" + permiso + "'? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) {
              
                perfil.agregarPermiso(permiso);
            
                boolean resultado = perfilDAO.asignarPermisoAPerfil(nombrePerfil, permiso);
                if (resultado) {
                    permisosAsignados.add(permiso);
                    System.out.println("  Permiso '" + permiso + "' asignado y guardado en BD.");
                } else {
                    System.out.println("  - Error al guardar permiso en BD.");
                }
            }
        }
        
        System.out.println("\n… Configuracion completada.");
        if (permisosAsignados.isEmpty()) {
            System.out.println("El perfil '" + nombrePerfil + "' no tiene permisos nuevos asignados.");
        } else {            
            System.out.println("Permisos asignados al perfil '" + nombrePerfil + "':");
            for (String p : permisosAsignados) {
                System.out.println("  - " + p);
            }
        }
    }

    public void verPermisos() {
        System.out.println("\n=== VER PERMISOS DE UN PERFIL ===");
        System.out.println("Perfiles disponibles:");
        
        List<String> nombresPerfiles = new ArrayList<>(gestor.listarNombresPerfiles());
        for (int i = 0; i < nombresPerfiles.size(); i++) {
            System.out.println((i + 1) + ". " + nombresPerfiles.get(i));
        }
        
        System.out.print("Seleccione el numero del perfil: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            if (opcion < 1 || opcion > nombresPerfiles.size()) {
                System.out.println("Opcion invalida.");
                return;
            }
            String nombrePerfil = nombresPerfiles.get(opcion - 1);
            
            PerfilDAOBD perfilDAO = new PerfilDAOBD();
            Perfil perfil = perfilDAO.obtenerPerfilPorNombre(nombrePerfil);
            
            if (perfil == null) {
                perfil = gestor.obtenerPerfil(nombrePerfil);
            }
            
            if (perfil != null) {
                System.out.println("\n Permisos del perfil '" + nombrePerfil + "':");
                System.out.println("======================================");
                
              
                ListaPermiso permisos = perfil.getPermisos();
                if (permisos.getCabeza() == null) {
                    System.out.println(" Este perfil no tiene permisos asignados.");
                } else {
                    System.out.println("Permisos asignados:");
                    permisos.mostrar();
                }
            } else {
                System.out.println("Perfil no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Debe ingresar un numero.");
        }
    }

    public void listarPerfiles() {
        System.out.println("\n====================================================");
        System.out.println("             LISTADO DE TODOS LOS PERFILES Y PERMISOS        ");
        System.out.println("=====================================================\n");
        
        List<String> nombresPerfiles = new ArrayList<>(gestor.listarNombresPerfiles());
        int contador = 1;
        
        for (String nombre : nombresPerfiles) {
            Perfil perfil = gestor.obtenerPerfil(nombre);
            System.out.println(contador + ". ðŸ“‹ PERFIL: " + nombre.toUpperCase());
            System.out.println("   ==================================================");
            System.out.println("   Permisos asignados:");
           
            ListaPermiso permisos = perfil.getPermisos();
            if (permisos.getCabeza() == null) {
                System.out.println(" (sin permisos asignados)");
            } else {
                permisos.mostrar();
            }
            System.out.println();
            contador++;
        }
        
        System.out.println("==========================================================");
        System.out.println("Total de perfiles: " + nombresPerfiles.size());
    }
}

