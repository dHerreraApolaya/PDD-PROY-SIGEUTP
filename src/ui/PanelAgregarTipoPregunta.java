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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelAgregarTipoPregunta extends JPanel {
    private JTextField txtTituloTipo;
    private JComboBox<String> cbxTipoExistente;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelAgregarTipoPregunta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("AGREGAR TIPO DE PREGUNTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Titulo del tipo de pregunta
        JLabel lblTituloTipoLabel = new JLabel("Titulo del Tipo de Pregunta:");
        lblTituloTipoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTituloTipoLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblTituloTipoLabel, gbc);

        txtTituloTipo = new JTextField(30);
        txtTituloTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtTituloTipo, gbc);
        fila++;

        // Separador
        fila++;

        // Seleccionar tipo existente para referencia
        JLabel lblSeleccionar = new JLabel("Seleccionar Tipo de Referencia:");
        lblSeleccionar.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSeleccionar.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblSeleccionar, gbc);

        cbxTipoExistente = new JComboBox<>(
                new String[] { "Dicotomica", "Respuesta Simple", "Escala de Valoracion", "Numerico" });
        cbxTipoExistente.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(cbxTipoExistente, gbc);
        fila++;

        // Separador
        fila++;

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblMensaje, gbc);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(140, 45));
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarTipo();
            }
        });

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                cardLayout.show(panelPrincipal, "BancoPreguntas");
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

        panelInferior.add(btnGuardar);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void guardarTipo() {
        String titulo = txtTituloTipo.getText().trim();

        if (titulo.isEmpty()) {
            lblMensaje.setText("Error: El Titulo del tipo no puede estar vacio");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Guardar en BD el nuevo tipo de pregunta
        // El nuevo tipo debe agregarse a las opciones en PanelAgregarPregunta y
        // PanelEditarPregunta
        lblMensaje.setText("Tipo de pregunta '" + titulo + "' guardado exitosamente");
        lblMensaje.setForeground(new Color(0, 150, 0));
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtTituloTipo.setText("");
        cbxTipoExistente.setSelectedIndex(0);
        lblMensaje.setText("");
    }
}




