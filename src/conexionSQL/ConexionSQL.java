package conexionSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {

    public static Connection getConnection() {
        String direccion = System.getenv("SIGEUTP_BD_URL");
        String usuario = System.getenv("SIGEUTP_BD_USUARIO");
        String clave = System.getenv("SIGEUTP_BD_CLAVE");
        if (direccion == null || direccion.isBlank() || usuario == null || usuario.isBlank()
                || clave == null || clave.isBlank()) {
            System.out.println("Configure SIGEUTP_BD_URL, SIGEUTP_BD_USUARIO y SIGEUTP_BD_CLAVE para conectar con SQL Server.");
            return null;
        }
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(direccion, usuario, clave);
            System.out.println("Conexion exitosa a SQL Server");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error al conectar a SQL Server: " + e.getMessage());
        }
        return conn;
    }
}
