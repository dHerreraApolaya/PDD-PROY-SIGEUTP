package modulo.participantes;

import java.util.List;
import java.util.Scanner;

public class ParticipanteServicios {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ParticipanteDAO participanteDAO = new ParticipanteDAO();

    public static void registrarParticipante() {
        try {
            String[] dniProhibidos = {"00000000", "11111111", "22222222", "33333333", "44444444", "55555555",
                "66666666", "77777777", "88888888", "12345678", "87654321"};

            String nombre;
            while (true) {
                System.out.print("Nombre completo: ");
                nombre = scanner.nextLine().trim();
                if (nombre.isEmpty()) {
                    System.out.println("Error: El nombre no puede estar vacio.");
                } else if (nombre.length() > 100) {
                    System.out.println("Error: El nombre no puede exceder los 100 caracteres.");
                } else {
                    break;
                }
            }

            String dni;
            while (true) {
                System.out.print("Documento de identidad (DNI): ");
                dni = scanner.nextLine().trim();
                if (!dni.matches("\\d{8}")) {
                    System.out.println("Error: El DNI debe contener exactamente 8 digitos numericos.");
                    continue;
                }
                boolean esProhibido = false;
                for (String prohibido : dniProhibidos) {
                    if (dni.equals(prohibido)) {
                        esProhibido = true;
                        break;
                    }
                }
                if (esProhibido) {
                    System.out.println("Error: DNI prohibido. Intente con otro.");
                    continue;
                }
                if (participanteDAO.existeDocumento(dni)) {
                    System.out.println("Ya existe un participante con ese DNI. Intente con otro.");
                    continue;
                }
                break;
            }

            String correo;
            while (true) {
                System.out.print("Correo electronico: ");
                correo = scanner.nextLine().trim();
                if (correo.isEmpty()) {
                    System.out.println("Error: El correo no puede estar vacio.");
                } else if (!correo.contains("@")) {
                    System.out.println("Error: El correo debe contener '@'.");
                } else {
                    break;
                }
            }

            int edad = 0;
            while (true) {
                System.out.print("Edad (0 a 120): ");
                String edadInput = scanner.nextLine().trim();
                if (edadInput.isEmpty()) {
                    System.out.println("Error: La edad no puede estar vacia.");
                    continue;
                }
                try {
                    edad = Integer.parseInt(edadInput);
                    if (edad < 0 || edad > 120) {
                        System.out.println("Error: La edad debe estar entre 0 y 120.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Ingrese un numero valido.");
                }
            }

            String sexo;
            while (true) {
                System.out.print("Sexo (Masculino/Femenino/Otro): ");
                sexo = scanner.nextLine().trim();
                if (sexo.isEmpty()) {
                    System.out.println("Error: El sexo no puede estar vacio.");
                } else if (!sexo.equalsIgnoreCase("masculino") && 
                !sexo.equalsIgnoreCase("femenino") && 
                !sexo.equalsIgnoreCase("otro")) {
                    System.out.println("Error: Ingrese Masculino, Femenino u Otro.");
                } else {
                    break;
                }
            }

            String estadoCivil;
            while (true) {
                System.out.println("Estado Civil:");
                System.out.println("1. Soltero");
                System.out.println("2. Casado");
                System.out.println("3. Separado");
                System.out.println("4. Divorciado");
                System.out.println("5. Viudo");
                System.out.print("OpciÃ³n: ");
                String opcionEstado = scanner.nextLine().trim();
                
                switch (opcionEstado) {
                    case "1":
                        estadoCivil = "Soltero";
                        break;
                    case "2":
                        estadoCivil = "Casado";
                        break;
                    case "3":
                        estadoCivil = "Separado";
                        break;
                    case "4":
                        estadoCivil = "Divorciado";
                        break;
                    case "5":
                        estadoCivil = "Viudo";
                        break;
                    default:
                        System.out.println("Error: OpciÃ³n invÃ¡lida.");
                        continue;
                }
                break;
            }

            // 7. TELEFONO
            String telefono;
            while (true) {
                System.out.print("Telefono (solo numeros): ");
                telefono = scanner.nextLine().trim();
                if (telefono.isEmpty()) {
                    System.out.println("Error: El telefono no puede estar vaci­o.");
                } else if (!telefono.matches("\\d{7,15}")) {
                    System.out.println("Error: El telefono debe contener entre 7 y 15 digitos.");
                } else {
                    break;
                }
            }

            double sueldo = 0;
            while (true) {
                System.out.print("Sueldo (ej: 2500.50) o presione Enter para omitir: ");
                String suelString = scanner.nextLine().trim();
                if (suelString.isEmpty()) {
                    sueldo = 0;
                    break;
                }
                try {
                    sueldo = Double.parseDouble(suelString);
                    if (sueldo < 0) {
                        System.out.println("Error: El sueldo no puede ser negativo.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Ingrese un nÃºmero vÃ¡lido.");
                }
            }

            String ciudad;
            while (true) {
                System.out.print("Ciudad/Ubicacion: ");
                ciudad = scanner.nextLine().trim();
                if (ciudad.isEmpty()) {
                    System.out.println("Error: La ciudad no puede estar vaci­a.");
                } else if (ciudad.length() > 100) {
                    System.out.println("Error: La ciudad no puede exceder 100 caracteres.");
                } else {
                    break;
                }
            }

            String departamento;
            while (true) {
                System.out.print("Departamento/Region: ");
                departamento = scanner.nextLine().trim();
                if (departamento.isEmpty()) {
                    System.out.println("Error: El departamento no puede estar vacio");
                } else if (departamento.length() > 100) {
                    System.out.println("Error: El departamento no puede exceder 100 caracteres.");
                } else {
                    break;
                }
            }

            String nivelEducativo;
            while (true) {
                System.out.println("Nivel Educativo:");
                System.out.println("1. Primaria");
                System.out.println("2. Secundaria");
                System.out.println("3. Superior");
                System.out.println("4. No especificado");
                System.out.print("Opcion: ");
                String opcionNivel = scanner.nextLine().trim();
                
                switch (opcionNivel) {
                    case "1":
                        nivelEducativo = "Primaria";
                        break;
                    case "2":
                        nivelEducativo = "Secundaria";
                        break;
                    case "3":
                        nivelEducativo = "Superior";
                        break;
                    case "4":
                        nivelEducativo = "No especificado";
                        break;
                    default:
                        System.out.println("Error: Opcion invalida.");
                        continue;
                }
                break;
            }

            Participante participante = new Participante(dni, nombre, correo);
            participante.setEdad(edad);  
            participante.setSexo(sexo);
            participante.setEstadoCivil(estadoCivil);
            participante.setTelefono(telefono);
            participante.setSueldo(sueldo);
            participante.setUbicacion(ciudad);  
            participante.setDepartamento(departamento);
            participante.setNivelEducativo(nivelEducativo);
            participante.setIdperfil(3); 
            
            participanteDAO.registrarParticipante(participante);
            
            System.out.println("\n==============================================================");
            System.out.println("          âœ“ REGISTRO COMPLETADO EXITOSAMENTE        ");
            System.out.println("================================================================");
            System.out.println("Nombre:           " + participante.getNombre());
            System.out.println("DNI:              " + participante.getDni());
            System.out.println("Correo:           " + participante.getCorreo());
            System.out.println("Edad:             " + participante.getEdad() + " anios (" + participante.getGrupoEdad() + ")");
            System.out.println("Sexo:             " + participante.getSexo());
            System.out.println("Estado Civil:     " + participante.getEstadoCivil());
            System.out.println("Telefono:         " + participante.getTelefono());
            System.out.println("Sueldo:           S/. " + (sueldo > 0 ? participante.getSueldo() : "No especificado"));
            System.out.println("Ubicacion:        " + participante.getUbicacion());
            System.out.println("Departamento:     " + participante.getDepartamento());
            System.out.println("Nivel Educativo:  " + participante.getNivelEducativo());
            System.out.println("================================================================");
            
        } catch (Exception e) {
            System.out.println("Error al registrar participante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void validarRegistroUnico() {
        try {
            String dni;
            while (true) {
                System.out.print("Ingrese DNI a validar: ");
                dni = scanner.nextLine().trim();
                if (!dni.matches("\\d{8}")) {
                    System.out.println("Error: El DNI debe contener exactamente 8 digitos.");
                } else {
                    break;
                }
            }

            if (participanteDAO.existeDocumento(dni)) {
                System.out.println("El DNI ya esta registrado.");
            } else {
                System.out.println("El DNI no esta registrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al validar DNI: " + e.getMessage());
        }
    }

    public static void listarParticipantes() {
        try {
            List<Participante> lista = participanteDAO.listarParticipantes();
            System.out.println("\n=== LISTADO DE PARTICIPANTES ===");
            if (lista.isEmpty()) {
                System.out.println("No hay participantes registrados.");
                return;
            }
            for (Participante p : lista) {
                System.out.println("ID: " + p.getId_participante()
                        + " | Nombre: " + p.getNombre()
                        + " | DNI: " + p.getDni()
                        + " | Correo: " + p.getCorreo()
                        + " | Edad: " + p.getEdad() + " (" + p.getGrupoEdad() + ")"
                        + " | Sexo: " + p.getSexo()
                        + " | Nivel Educativo: " + p.getNivelEducativo());
            }
        } catch (Exception e) {
            System.out.println("Error al listar participantes: " + e.getMessage());
        }
    }

    public static void buscarParticipantePorDNI() {
        try {
            String dni;
            while (true) {
                System.out.print("Ingrese el DNI del participante a buscar: ");
                dni = scanner.nextLine().trim();
                if (!dni.matches("\\d{8}")) {
                    System.out.println("Error: El DNI debe contener exactamente 8 digitos.");
                } else {
                    break;
                }
            }

            Participante participante = participanteDAO.obtenerParticipantePorDNI(dni);

            if (participante != null) {
                System.out.println("\n=== DATOS DEL PARTICIPANTE ===");
                System.out.println("ID: " + participante.getId_participante());
                System.out.println("Nombre: " + participante.getNombre());
                System.out.println("DNI: " + participante.getDni());
                System.out.println("Correo: " + participante.getCorreo());
                System.out.println("Edad: " + participante.getEdad() + " aÃ±os");
                System.out.println("Grupo de Edad: " + participante.getGrupoEdad());
                System.out.println("Sexo: " + participante.getSexo());
                System.out.println("Nivel Educativo: " + participante.getNivelEducativo());
                System.out.println("ID Perfil: " + participante.getIdperfil());
            } else {
                System.out.println("No se encontro participante con el DNI: " + dni);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar participante: " + e.getMessage());
        }
    }

    public static void eliminarParticipante() {
        try {
            String dni;
            while (true) {
                System.out.print("Ingrese el DNI del participante a eliminar: ");
                dni = scanner.nextLine().trim();
                if (!dni.matches("\\d{8}")) {
                    System.out.println("Error: El DNI debe contener exactamente 8 digitos.");
                } else {
                    break;
                }
            }

            Participante participante = participanteDAO.obtenerParticipantePorDNI(dni);
            if (participante == null) {
                System.out.println("No se encontro participante con ese DNI.");
                return;
            }

            System.out.println("\n=== PARTICIPANTE A ELIMINAR ===");
            System.out.println("Nombre: " + participante.getNombre());
            System.out.println("DNI: " + participante.getDni());
            System.out.println("Correo: " + participante.getCorreo());

            System.out.print("\nConfirma la eliminacion? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                boolean eliminado = participanteDAO.eliminarParticipante(dni);
                if (eliminado) {
                    System.out.println("Participante eliminado correctamente.");
                }
            } else {
                System.out.println("Eliminacion cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar participante: " + e.getMessage());
        }
    }
}

