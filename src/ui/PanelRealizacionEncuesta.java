package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelRealizacionEncuesta extends JPanel {
    private JButton btnEjecutarEncuesta;
    private JButton btnRegistrarRespuestas;
    private JButton btnControlarTiempo;
    private JButton btnVerRespuestas;
    private JButton btnVolver;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelRealizacionEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Realizacion DE ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con botones (grid 2x2)
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 30, 30));
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Boton 1: Ejecutar Encuesta
        btnEjecutarEncuesta = new JButton("Ejecutar Encuesta");
        btnEjecutarEncuesta.setFont(new Font("Arial", Font.BOLD, 16));
        btnEjecutarEncuesta.setForeground(Color.WHITE);
        btnEjecutarEncuesta.setBackground(new Color(52, 152, 219));
        btnEjecutarEncuesta.setOpaque(true);
        btnEjecutarEncuesta.setBorderPainted(false);
        btnEjecutarEncuesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EjecutarEncuesta");
            }
        });
        panelCentral.add(btnEjecutarEncuesta);

        // Boton 2: Registrar Respuestas de Encuesta
        btnRegistrarRespuestas = new JButton("Registrar Respuestas de Encuesta");
        btnRegistrarRespuestas.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrarRespuestas.setForeground(Color.WHITE);
        btnRegistrarRespuestas.setBackground(new Color(46, 204, 113));
        btnRegistrarRespuestas.setOpaque(true);
        btnRegistrarRespuestas.setBorderPainted(false);
        btnRegistrarRespuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistrarRespuestas");
            }
        });
        panelCentral.add(btnRegistrarRespuestas);

        // Boton 3: Controlar Tiempo de Ejecucion
        btnControlarTiempo = new JButton("Controlar Tiempo de Ejecucion");
        btnControlarTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        btnControlarTiempo.setForeground(Color.WHITE);
        btnControlarTiempo.setBackground(new Color(155, 89, 182));
        btnControlarTiempo.setOpaque(true);
        btnControlarTiempo.setBorderPainted(false);
        btnControlarTiempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "ControlarTiempoEjecucion");
            }
        });
        panelCentral.add(btnControlarTiempo);

        // Boton 4: Mostrar Historial de Respuestas
        btnVerRespuestas = new JButton("Mostrar Historial de Respuestas");
        btnVerRespuestas.setFont(new Font("Arial", Font.BOLD, 16));
        btnVerRespuestas.setForeground(Color.WHITE);
        btnVerRespuestas.setBackground(new Color(230, 126, 34));
        btnVerRespuestas.setOpaque(true);
        btnVerRespuestas.setBorderPainted(false);
        btnVerRespuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "MostrarHistorialRespuestas");
            }
        });
        panelCentral.add(btnVerRespuestas);

        // Panel inferior con Boton Volver
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "MenuPrincipal");
            }
        });

        panelInferior.add(btnVolver);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}




