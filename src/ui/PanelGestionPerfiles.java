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

public class PanelGestionPerfiles extends JPanel {

    public PanelGestionPerfiles(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Gestion DE USUARIOS EXTERNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Seleccione una opcion");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblSubtitulo);

        // Panel central con botones (grid 2x3)
        JPanel panelCentral = new JPanel(new GridLayout(2, 3, 30, 30));
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Boton: Crear Usuario Externo
        JButton btnCrear = new JButton("Crear Usuario Externo");
        btnCrear.setBackground(new Color(100, 150, 200));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrear.setPreferredSize(new Dimension(200, 120));
        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "CrearUsuarioExterno");
            }
        });
        btnCrear.setOpaque(true);
        btnCrear.setBorderPainted(false);
        btnCrear.setFocusPainted(false);

        // Boton: Editar Usuario Externo
        JButton btnEditar = new JButton("Editar Usuario Externo");
        btnEditar.setBackground(new Color(100, 180, 150));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditar.setPreferredSize(new Dimension(200, 120));
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EditarUsuarioExterno");
            }
        });
        btnEditar.setOpaque(true);
        btnEditar.setBorderPainted(false);
        btnEditar.setFocusPainted(false);

        // Boton: Eliminar Usuario Externo
        JButton btnEliminar = new JButton("Eliminar Usuario Externo");
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminar.setPreferredSize(new Dimension(200, 120));
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EliminarUsuarioExterno");
            }
        });
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);

        // Boton: Listar Usuarios Externos
        JButton btnListar = new JButton("Listar Usuarios Externos");
        btnListar.setBackground(new Color(150, 120, 180));
        btnListar.setForeground(Color.WHITE);
        btnListar.setFont(new Font("Arial", Font.BOLD, 14));
        btnListar.setPreferredSize(new Dimension(200, 120));
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "ListarUsuariosExternos");
            }
        });
        btnListar.setOpaque(true);
        btnListar.setBorderPainted(false);
        btnListar.setFocusPainted(false);

        // Boton: Volver
        JButton btnVolver = new JButton("Volver");
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

        // Boton: Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBackground(new Color(150, 150, 150));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setPreferredSize(new Dimension(200, 120));
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnSalir.setOpaque(true);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);

        panelCentral.add(btnCrear);
        panelCentral.add(btnEditar);
        panelCentral.add(btnEliminar);
        panelCentral.add(btnListar);
        panelCentral.add(btnVolver);
        panelCentral.add(btnSalir);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }
}




