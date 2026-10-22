package modulo.encuestas.configuracion;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase para registrar pagos de encuestas respondidas
 * Genera recibos similares a boletas SUNAT
 */
public class RegistroPago {
    private int idRegistro;
    private int idParticipante;
    private int idEncuesta;
    private double montoEncuesta;
    private double montoTotal;
    private String estado; // PENDIENTE, PAGADO
    private String fechaPago;
    private String numeroRecibo;
    private String metodoPago; // EFECTIVO, TRANSFERENCIA, etc
    private int totalEncuestasRespondidas;

    public RegistroPago() {
        this.estado = "PENDIENTE";
        this.fechaPago = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.numeroRecibo = generarNumeroRecibo();
        this.metodoPago = "EFECTIVO";
    }

    public RegistroPago(int idParticipante, int idEncuesta, double monto) {
        this();
        this.idParticipante = idParticipante;
        this.idEncuesta = idEncuesta;
        this.montoEncuesta = monto;
        this.montoTotal = monto;
    }

    private String generarNumeroRecibo() {
        // Formato: RCP-YYYYMMDD-HHMMSS-XXXX
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String timestamp = df.format(new Date());
        int random = (int) (Math.random() * 10000);
        return "RCP-" + timestamp + "-" + String.format("%04d", random);
    }

    // Getters y Setters
    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public double getMontoEncuesta() {
        return montoEncuesta;
    }

    public void setMontoEncuesta(double montoEncuesta) {
        this.montoEncuesta = montoEncuesta;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getTotalEncuestasRespondidas() {
        return totalEncuestasRespondidas;
    }

    public void setTotalEncuestasRespondidas(int totalEncuestasRespondidas) {
        this.totalEncuestasRespondidas = totalEncuestasRespondidas;
    }

    @Override
    public String toString() {
        return "Recibo: " + numeroRecibo + " | Participante: " + idParticipante + 
               " | Monto: S/. " + String.format("%.2f", montoTotal) + " | Estado: " + estado;
    }
}
