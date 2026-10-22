package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PanelBuscarParticipante extends JPanel {
    private JTextField txtDNI;
    private JTextArea txtResultado;
    private JButton btnBuscar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelBuscarParticipante(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("BUSCAR PARTICIPANTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));
        panelFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // DNI a buscar - en la misma fila con Boton Buscar
        JLabel lblDNI = new JLabel("Ingrese DNI del Participante:");
        lblDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDNI.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblDNI, gbc);

        txtDNI = new JTextField(20);
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(txtDNI, gbc);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBuscar.setBackground(new Color(34, 139, 34));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParticipante();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(btnBuscar, gbc);
        fila++;

        // Separador
        fila++;

        // Resultado de busqueda
        JLabel lblResultado = new JLabel("Datos del Participante:");
        lblResultado.setFont(new Font("Arial", Font.PLAIN, 12));
        lblResultado.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        panelFormulario.add(lblResultado, gbc);
        fila++;

        txtResultado = new JTextArea(10, 50);
        txtResultado.setFont(new Font("Arial", Font.PLAIN, 11));
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setEditable(false);
        txtResultado.setBackground(new Color(220, 220, 220));
        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelFormulario.add(scrollResultado, gbc);
        fila++;

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(lblMensaje, gbc);

        // Panel inferior con botones
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
                limpiarFormulario();
                cardLayout.show(panelPrincipal, "GestionParticipante");
            }
        });

        JButton btnCerrar = new JButton("Cerrar");
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
        add(panelFormulario, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void buscarParticipante() {
        String dni = txtDNI.getText().trim();

        if (dni.isEmpty()) {
            lblMensaje.setText("Error: Ingrese un DNI valido");
            lblMensaje.setForeground(Color.RED);
            txtResultado.setText("");
            return;
        }

        // TODO: Buscar en la BD el participante con este DNI
        boolean encontrado = true; // Placeholder
        if (encontrado) {
            txtResultado.setText(
                    "PARTICIPANTE REGISTRADO\n\n" +
                            "DNI: " + dni + "\n" +
                            "Nombre: Juan Perez Garcia\n" +
                            "Email: juan.perez@example.com\n" +
                            "Sexo: Masculino\n" +
                            "Edad: 28\n" +
                            "Nivel Educativo: Superior\n" +
                            "Fecha de Registro: 15/11/2025\n" +
                            "Estado: Activo");
            lblMensaje.setText("Participante encontrado");
            lblMensaje.setForeground(new Color(0, 150, 0));
        } else {
            txtResultado.setText(
                    "NO REGISTRADO\n\nEl participante con DNI " + dni + " no se encuentra registrado en el sistema.");
            lblMensaje.setText("Participante no registrado");
            lblMensaje.setForeground(Color.ORANGE);
        }
    }

    private void limpiarFormulario() {
        txtDNI.setText("");
        txtResultado.setText("");
        lblMensaje.setText("");
    }
}





