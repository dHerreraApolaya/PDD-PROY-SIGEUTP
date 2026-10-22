package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelAnalisisInformes extends JPanel {
    private JButton btnEstadisticasEncuesta;
    private JButton btnEstadisticasParticipante;
    private JButton btnGraficosEncuesta;
    private JButton btnVolver;
    private JButton btnCerrar;

    public PanelAnalisisInformes(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(41, 128, 185));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Analisis E INFORMES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Seleccione una opcion:");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblSubtitulo);

        // Panel central con botones (grid 2x2)
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 30, 30));
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Boton: Estadisticas por Encuesta
        btnEstadisticasEncuesta = new JButton("Estadisticas por Encuesta");
        btnEstadisticasEncuesta.setBackground(new Color(30, 60, 100));
        btnEstadisticasEncuesta.setForeground(Color.WHITE);
        btnEstadisticasEncuesta.setFont(new Font("Arial", Font.BOLD, 16));
        btnEstadisticasEncuesta.setPreferredSize(new Dimension(200, 120));
        btnEstadisticasEncuesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EstadisticasPorEncuesta");
            }
        });
        btnEstadisticasEncuesta.setOpaque(true);
        btnEstadisticasEncuesta.setBorderPainted(false);
        btnEstadisticasEncuesta.setFocusPainted(false);

        // Boton: Estadisticas por Participante
        btnEstadisticasParticipante = new JButton("Estadisticas por Participante");
        btnEstadisticasParticipante.setBackground(new Color(76, 175, 80));
        btnEstadisticasParticipante.setForeground(Color.WHITE);
        btnEstadisticasParticipante.setFont(new Font("Arial", Font.BOLD, 16));
        btnEstadisticasParticipante.setPreferredSize(new Dimension(200, 120));
        btnEstadisticasParticipante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EstadisticasParticipante");
            }
        });
        btnEstadisticasParticipante.setOpaque(true);
        btnEstadisticasParticipante.setBorderPainted(false);
        btnEstadisticasParticipante.setFocusPainted(false);

        // Boton: Graficos por Encuesta
        btnGraficosEncuesta = new JButton("Graficos por Encuesta");
        btnGraficosEncuesta.setBackground(new Color(153, 102, 204));
        btnGraficosEncuesta.setForeground(Color.WHITE);
        btnGraficosEncuesta.setFont(new Font("Arial", Font.BOLD, 16));
        btnGraficosEncuesta.setPreferredSize(new Dimension(200, 120));
        btnGraficosEncuesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "GraficosPorEncuesta");
            }
        });
        btnGraficosEncuesta.setOpaque(true);
        btnGraficosEncuesta.setBorderPainted(false);
        btnGraficosEncuesta.setFocusPainted(false);

        // Boton: Volver (placeholder en grid)
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(60, 150, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setPreferredSize(new Dimension(200, 120));
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "MenuPrincipal");
            }
        });
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);

        panelCentral.add(btnEstadisticasEncuesta);
        panelCentral.add(btnEstadisticasParticipante);
        panelCentral.add(btnGraficosEncuesta);
        panelCentral.add(btnVolver);

        // Panel inferior con Boton Cerrar
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 90));

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(140, 45));
        btnCerrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrar.setBackground(new Color(34, 139, 34));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}


