package modulo.encuestas.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para gestionar registros de pago por encuestas respondidas
 */
public class RegistroPagoDAO {
    private List<RegistroPago> registrosPago = new ArrayList<>();
    private static RegistroPagoDAO instancia;
    private int contadorId = 1;

    private RegistroPagoDAO() {
    }

    public static RegistroPagoDAO getInstancia() {
        if (instancia == null) {
            instancia = new RegistroPagoDAO();
        }
        return instancia;
    }

    /**
     * Registrar nuevo pago
     */
    public void registrarPago(RegistroPago pago) {
        if (pago == null) {
            System.out.println("Error: Registro de pago nulo");
            return;
        }
        
        pago.setIdRegistro(contadorId++);
        registrosPago.add(pago);
        System.out.println(" Pago registrado: " + pago.getNumeroRecibo() + 
                         " | Monto: S/. " + String.format("%.2f", pago.getMontoTotal()));
    }

    /**
     * Obtener registro por ID
     */
    public RegistroPago obtenerRegistroPorId(int idRegistro) {
        for (RegistroPago r : registrosPago) {
            if (r.getIdRegistro() == idRegistro) {
                return r;
            }
        }
        return null;
    }

    /**
     * Obtener registros de pago por participante
     */
    public List<RegistroPago> obtenerRegistrosPorParticipante(int idParticipante) {
        List<RegistroPago> registrosParticipante = new ArrayList<>();
        for (RegistroPago r : registrosPago) {
            if (r.getIdParticipante() == idParticipante) {
                registrosParticipante.add(r);
            }
        }
        return registrosParticipante;
    }

    /**
     * Cambiar estado de pago a PAGADO
     */
    public void marcarComoPagado(int idRegistro) {
        RegistroPago registro = obtenerRegistroPorId(idRegistro);
        if (registro != null) {
            registro.setEstado("PAGADO");
            System.out.println(" Pago marcado como pagado: " + registro.getNumeroRecibo());
        }
    }

    /**
     * Obtener total de ingresos por participante
     */
    public double obtenerTotalIngresoParticipante(int idParticipante) {
        double total = 0;
        for (RegistroPago r : registrosPago) {
            if (r.getIdParticipante() == idParticipante) {
                total += r.getMontoTotal();
            }
        }
        return total;
    }

    /**
     * Obtener total de encuestas respondidas por participante
     */
    public int obtenerTotalEncuestasRespondidas(int idParticipante) {
        int total = 0;
        for (RegistroPago r : registrosPago) {
            if (r.getIdParticipante() == idParticipante) {
                total += r.getTotalEncuestasRespondidas();
            }
        }
        return total;
    }

    /**
     * Listar todos los registros de pago
     */
    public void listarRegistrosPago() {
        if (registrosPago.isEmpty()) {
            System.out.println("No hay registros de pago.");
            return;
        }
        
        System.out.println("\n=== REGISTROS DE PAGO ===");
        int numero = 1;
        for (RegistroPago r : registrosPago) {
            System.out.println(numero + ". " + r);
            numero++;
        }
    }

    /**
     * Obtener todos los registros
     */
    public List<RegistroPago> obtenerTodos() {
        return new ArrayList<>(registrosPago);
    }

    /**
     * Contar registros pendientes
     */
    public int contarRegistrosPendientes() {
        return (int) registrosPago.stream().filter(r -> r.getEstado().equals("PENDIENTE")).count();
    }

    /**
     * Contar registros pagados
     */
    public int contarRegistrosPagados() {
        return (int) registrosPago.stream().filter(r -> r.getEstado().equals("PAGADO")).count();
    }

    /**
     * Obtener reporte resumido por participante
     */
    public Map<Integer, Double> obtenerReporteIngresoPorParticipante() {
        Map<Integer, Double> reporte = new HashMap<>();
        for (RegistroPago r : registrosPago) {
            int idPart = r.getIdParticipante();
            double monto = r.getMontoTotal();
            reporte.put(idPart, reporte.getOrDefault(idPart, 0.0) + monto);
        }
        return reporte;
    }
}
