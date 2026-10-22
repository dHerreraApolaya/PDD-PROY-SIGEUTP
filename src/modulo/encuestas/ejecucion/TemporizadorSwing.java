package modulo.encuestas.ejecucion;

import java.util.Timer;
import java.util.TimerTask;

public class TemporizadorSwing {

    private Timer timer;
    private int contador;

    public TemporizadorSwing(int segundosTotales) {
        this.contador = segundosTotales;
        this.timer = new Timer();
    }

    public void iniciar() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (contador > 0) {
                    int horas = contador / 3600;
                    int minutos = (contador % 3600) / 60;
                    int segundos = contador % 60;
                    System.out.println("Tiempo restante: " + horas + " Hr " + minutos + " Min " + segundos + " Seg");
                    contador--;
                } else {
                    System.out.println(" Tiempo agotado");
                    detener();
                }
            }
        }, 0, 1000);
    }

    public void detener() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public int getTiempoRestante() {
        return contador;
    }

    public boolean hayTiempoDisponible() {
        return contador > 0;
    }
}
