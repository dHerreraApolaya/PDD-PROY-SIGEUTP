package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelMenuPrincipal extends JPanel {
    private JButton btnPerfiles;
    private JButton btnConfiguracion;
    private JButton btnParticipantes;
    private JButton btnEncuestas;
    private JButton btnAnalisis;
    private JButton btnSalir;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelMenuPrincipal(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("MENU PRINCIPAL");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con opciones (6 botones en GridLayout 2x3)
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 3, 20, 20));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelCentral.setBackground(new Color(30, 60, 100));

        btnPerfiles = crearBoton("Gestion de Perfiles");
        btnPerfiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "GestionPerfiles");
            }
        });

        btnConfiguracion = crearBoton("Configuracion de Encuesta");
        btnConfiguracion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "ConfiguracionEncuesta");
            }
        });

        btnParticipantes = crearBoton("Gestion del Participante");
        btnParticipantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "GestionParticipante");
            }
        });

        btnEncuestas = crearBoton("Realizacion de Encuesta");
        btnEncuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RealizacionEncuesta");
            }
        });

        btnAnalisis = crearBoton("Analisis e Informes");
        btnAnalisis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "AnalisisInformes");
            }
        });

        btnSalir = crearBoton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "Home");
            }
        });

        panelCentral.add(btnPerfiles);
        panelCentral.add(btnConfiguracion);
        panelCentral.add(btnParticipantes);
        panelCentral.add(btnEncuestas);
        panelCentral.add(btnAnalisis);
        panelCentral.add(btnSalir);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setBackground(new Color(100, 150, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }
}



