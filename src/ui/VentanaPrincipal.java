package ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private PanelEjecutarEncuesta panelEjecutarEncuesta;
    private PanelRegistrarRespuestas panelRegistrarRespuestas;
    private PanelPagoPorParticipante panelPagoPorParticipante;

    public VentanaPrincipal() {
        setTitle("ENCUESTAS UTP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        // Crear CardLayout para gestionar los paneles
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Crear y agregar todos los paneles
        panelPrincipal.add(new PanelHome(cardLayout, panelPrincipal), "Home");
        panelPrincipal.add(new PanelLogin(cardLayout, panelPrincipal), "Login");
        panelPrincipal.add(new PanelMenuPrincipal(cardLayout, panelPrincipal), "MenuPrincipal");
        PanelRegistroParticipante panelRegistroParticipante = new PanelRegistroParticipante(cardLayout, panelPrincipal);
        panelPrincipal.add(panelRegistroParticipante, "RegistroParticipante");
        panelPrincipal.add(new PanelGestionPerfiles(cardLayout, panelPrincipal), "GestionPerfiles");
        panelPrincipal.add(new PanelConfiguracionEncuesta(cardLayout, panelPrincipal), "ConfiguracionEncuesta");
        panelPrincipal.add(new PanelGestionParticipante(cardLayout, panelPrincipal), "GestionParticipante");
        panelPrincipal.add(new PanelRealizacionEncuesta(cardLayout, panelPrincipal), "RealizacionEncuesta");
        panelPrincipal.add(new PanelAnalisisInformes(cardLayout, panelPrincipal), "AnalisisInformes");
        panelPrincipal.add(new PanelRegistrarEncuesta(cardLayout, panelPrincipal), "RegistrarEncuesta");
        panelPrincipal.add(new PanelEditarEncuesta(cardLayout, panelPrincipal), "EditarEncuesta");
        panelPrincipal.add(new PanelEliminarEncuesta(cardLayout, panelPrincipal), "EliminarEncuesta");
        panelPrincipal.add(new PanelListarEncuesta(cardLayout, panelPrincipal), "ListarEncuesta");
        panelPrincipal.add(new PanelBancoPreguntas(cardLayout, panelPrincipal), "BancoPreguntas");
        panelPrincipal.add(new PanelAgregarPregunta(cardLayout, panelPrincipal), "AgregarPregunta");
        panelPrincipal.add(new PanelEditarPregunta(cardLayout, panelPrincipal), "EditarPregunta");
        panelPrincipal.add(new PanelEliminarPregunta(cardLayout, panelPrincipal), "EliminarPregunta");
        panelPrincipal.add(new PanelListarPreguntas(cardLayout, panelPrincipal), "ListarPreguntas");
        panelPrincipal.add(new PanelAgregarTipoPregunta(cardLayout, panelPrincipal), "AgregarTipoPregunta");
        panelPrincipal.add(new PanelAgregarCategoria(cardLayout, panelPrincipal), "AgregarCategoria");
        panelPrincipal.add(new PanelBuscarParticipante(cardLayout, panelPrincipal), "BuscarParticipante");
        panelPrincipal.add(new PanelListarParticipantes(cardLayout, panelPrincipal), "ListarParticipantes");
        panelPagoPorParticipante = new PanelPagoPorParticipante(cardLayout, panelPrincipal);
        panelPrincipal.add(panelPagoPorParticipante, "PagoPorParticipante");
        panelEjecutarEncuesta = new PanelEjecutarEncuesta(cardLayout, panelPrincipal);
        panelPrincipal.add(panelEjecutarEncuesta, "EjecutarEncuesta");
        panelRegistrarRespuestas = new PanelRegistrarRespuestas(cardLayout, panelPrincipal, panelEjecutarEncuesta);
        panelPrincipal.add(panelRegistrarRespuestas, "RegistrarRespuestas");
        panelRegistroParticipante.establecerPanelEjecucion(panelEjecutarEncuesta);
        panelEjecutarEncuesta.establecerPanelRespuestas(panelRegistrarRespuestas);
        panelPrincipal.add(new PanelControlarTiempoEjecucion(cardLayout, panelPrincipal), "ControlarTiempoEjecucion");
        panelPrincipal.add(new PanelMostrarHistorialRespuestas(cardLayout, panelPrincipal),
                "MostrarHistorialRespuestas");
        panelPrincipal.add(new PanelEstadisticasPorEncuesta(cardLayout, panelPrincipal), "EstadisticasPorEncuesta");
        panelPrincipal.add(new PanelEstadisticasPorParticipante(cardLayout, panelPrincipal),
                "EstadisticasParticipante");
        panelPrincipal.add(new PanelGraficosPorEncuesta(cardLayout, panelPrincipal), "GraficosPorEncuesta");
        panelPrincipal.add(new PanelCrearUsuarioExterno(cardLayout, panelPrincipal), "CrearUsuarioExterno");
        panelPrincipal.add(new PanelEditarUsuarioExterno(cardLayout, panelPrincipal), "EditarUsuarioExterno");
        panelPrincipal.add(new PanelEliminarUsuarioExterno(cardLayout, panelPrincipal), "EliminarUsuarioExterno");
        panelPrincipal.add(new PanelListarUsuariosExternos(cardLayout, panelPrincipal), "ListarUsuariosExternos");

        add(panelPrincipal);

        // Mostrar el panel Home inicialmente
        cardLayout.show(panelPrincipal, "Home");

        setVisible(true);
    }

    private static void configurarApariencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException excepcion) {
            throw new IllegalStateException("No se pudo configurar la apariencia de la aplicacion", excepcion);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                configurarApariencia();
                new VentanaPrincipal();
            }
        });
    }
}
