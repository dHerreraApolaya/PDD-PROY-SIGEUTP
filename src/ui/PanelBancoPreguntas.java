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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelBancoPreguntas extends JPanel {
    private JButton btnAgregarPregunta;
    private JButton btnEditarPregunta;
    private JButton btnEliminarPregunta;
    private JButton btnListarPreguntas;
    private JButton btnAgregarTipo;
    private JButton btnAgregarCategoria;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelBancoPreguntas(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("BANCO DE PREGUNTAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con 6 botones en grid 2x3
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 3, 20, 30));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelCentral.setBackground(new Color(30, 60, 100));

        btnAgregarPregunta = crearBoton("Agregar Pregunta");
        btnAgregarPregunta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "AgregarPregunta");
            }
        });

        btnEditarPregunta = crearBoton("Editar Pregunta");
        btnEditarPregunta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EditarPregunta");
            }
        });

        btnEliminarPregunta = crearBoton("Eliminar Pregunta");
        btnEliminarPregunta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "EliminarPregunta");
            }
        });

        btnListarPreguntas = crearBoton("Listar Preguntas");
        btnListarPreguntas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "ListarPreguntas");
            }
        });

        btnAgregarTipo = crearBoton("Agregar Tipo de Pregunta");
        btnAgregarTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "AgregarTipoPregunta");
            }
        });

        btnAgregarCategoria = crearBoton("Agregar Categoria");
        btnAgregarCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "AgregarCategoria");
            }
        });

        panelCentral.add(btnAgregarPregunta);
        panelCentral.add(btnEditarPregunta);
        panelCentral.add(btnEliminarPregunta);
        panelCentral.add(btnListarPreguntas);
        panelCentral.add(btnAgregarTipo);
        panelCentral.add(btnAgregarCategoria);

        // Panel inferior con botones volver y cerrar
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 90));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistrarEncuesta");
            }
        });

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

        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(180, 60));
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBackground(new Color(100, 150, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }
}




