package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelHome extends JPanel {
    private JButton btnParticipante;
    private JButton btnAdministrador;
    private JButton btnCerrar;
    private JLabel lblTitulo;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelHome(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;
        
        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior con Titulo
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 100));
        panelTitulo.setPreferredSize(new Dimension(0, 120));
        
        lblTitulo = new JLabel("ENCUESTAS UTP");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // Panel central con botones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 50));
        panelCentral.setBackground(new Color(30, 60, 100));

        btnParticipante = new JButton("Clic si eres Participante");
        btnParticipante.setPreferredSize(new Dimension(200, 60));
        btnParticipante.setFont(new Font("Arial", Font.PLAIN, 12));
        btnParticipante.setBackground(new Color(34, 139, 34));
        btnParticipante.setForeground(Color.WHITE);
        btnParticipante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistroParticipante");
            }
        });

        btnAdministrador = new JButton(" Clic si eres Admin o Externo");
        btnAdministrador.setPreferredSize(new Dimension(200, 60));
        btnAdministrador.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAdministrador.setBackground(new Color(34, 139, 34));
        btnAdministrador.setForeground(Color.WHITE);
        btnAdministrador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "Login");
            }
        });

        panelCentral.add(btnParticipante);
        panelCentral.add(btnAdministrador);

        // Panel inferior con Boton cerrar
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 100));

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(150, 40));
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

        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}




