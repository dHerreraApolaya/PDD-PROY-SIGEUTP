package modulo.encuestas.ejecucion;

public class LanzadorTemporizador {
    
    private static Thread threadTemporizador = null;
    private static volatile boolean detener = false;
   
    public static void abrirTemporizadorEnVentana(int segundos) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            
            if (osName.contains("win")) {
              
                String[] comando = {"cmd.exe", "/c", "start cmd /k \"java -cp . TemporizadorConsola " + segundos + "\""};
                Runtime.getRuntime().exec(comando);
            } else if (osName.contains("linux")) {
           
                String[] comando = {"bash", "-c", "gnome-terminal -- java -cp . TemporizadorConsola " + segundos};
                Runtime.getRuntime().exec(comando);
            } else if (osName.contains("mac")) {
           
                String[] comando = {"open", "-a", "Terminal", "java -cp . TemporizadorConsola " + segundos};
                Runtime.getRuntime().exec(comando);
            } else {
                System.out.println("Sistema operativo no reconocido.");
                return;
            }
            
            System.out.println("Temporizador abierto en ventana separada (" + segundos + " segundos)");
            
        } catch (Exception e) {
            System.out.println("Error al abrir temporizador en ventana: " + e.getMessage());
        }
    }
  
    public static void iniciarTemporizadorEnThread(int segundos) {
        detener = false; 
        threadTemporizador = new Thread(() -> {
            try {
                System.out.println("\n  TEMPORIZADOR: " + segundos + " segundos");
                for (int i = segundos; i >= 0 && !detener; i--) {
                    int minutos = i / 60;
                    int secs = i % 60;
                    System.out.print("\rTiempo restante: " + minutos + ":" + String.format("%02d", secs) + "   ");
                    Thread.sleep(1000);
                }
                if (!detener) {
                    System.out.println("\nTIEMPO AGOTADO!");
                } else {
                    System.out.print("\n");
                }
            } catch (InterruptedException e) {
                System.out.println("\nTemporizador detenido.");
            }
        });
        threadTemporizador.start();
    }
   
    public static void detenerTemporizador() {
        detener = true;  
        if (threadTemporizador != null && threadTemporizador.isAlive()) {
            threadTemporizador.interrupt();
            try {
                threadTemporizador.join(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
