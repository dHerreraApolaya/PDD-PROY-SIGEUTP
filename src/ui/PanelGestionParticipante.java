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

public class PanelGestionParticipante extends JPanel {
    private JButton btnRegistrar;
    private JButton btnBuscar;
    private JButton btnListar;
    private JButton btnPago;
    private JButton btnSalir;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelGestionParticipante(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Gestion DE PARTICIPANTES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con botones en grid 2x3
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setLayout(new GridLayout(2, 3, 20, 20));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        btnRegistrar = new JButton("Registrar Participante");
        btnRegistrar.setPreferredSize(new Dimension(150, 80));
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistrar.setBackground(new Color(34, 139, 34));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistroParticipante");
            }
        });

        btnBuscar = new JButton("Buscar Participante");
        btnBuscar.setPreferredSize(new Dimension(150, 80));
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBuscar.setBackground(new Color(34, 139, 34));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "BuscarParticipante");
            }
        });

        btnListar = new JButton("Listar Participantes");
        btnListar.setPreferredSize(new Dimension(150, 80));
        btnListar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnListar.setBackground(new Color(34, 139, 34));
        btnListar.setForeground(Color.WHITE);
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "ListarParticipantes");
            }
        });

        btnPago = new JButton("Pago por Participante");
        btnPago.setPreferredSize(new Dimension(150, 80));
        btnPago.setFont(new Font("Arial", Font.PLAIN, 12));
        btnPago.setBackground(new Color(34, 139, 34));
        btnPago.setForeground(Color.WHITE);
        btnPago.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "PagoPorParticipante");
            }
        });

        btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(150, 80));
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.setBackground(new Color(34, 139, 34));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel panelVolver = new JPanel();
        panelVolver.setBackground(new Color(30, 60, 100));

        panelCentral.add(btnRegistrar);
        panelCentral.add(btnBuscar);
        panelCentral.add(btnListar);
        panelCentral.add(btnPago);
        panelCentral.add(btnSalir);
        panelCentral.add(panelVolver); // Espacio vacio

        // Panel inferior con Boton Volver
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnVolver = new JButton("Volver");
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




