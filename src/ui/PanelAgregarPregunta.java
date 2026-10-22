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
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import conexionSQL.ConexionSQL;

public class PanelAgregarPregunta extends JPanel {
    private static final int NUM_PREGUNTAS = 10;

    private JTextArea[] txtPregunta = new JTextArea[NUM_PREGUNTAS];
    private JComboBox<String>[] cbxTipoPregunta = new JComboBox[NUM_PREGUNTAS];
    private JComboBox<String>[] cbxCategoria = new JComboBox[NUM_PREGUNTAS];
    private JCheckBox[] chkEsPreguntaFiltro = new JCheckBox[NUM_PREGUNTAS];

    private JButton btnGuardar;
    private JButton btnAgregarMas;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelAgregarPregunta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("AGREGAR PREGUNTA");
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

        for (int i = 0; i < NUM_PREGUNTAS; i++) {
            // Label Pregunta
            JLabel lblPregunta = new JLabel("Pregunta " + (i + 1) + ":");
            lblPregunta.setFont(new Font("Arial", Font.BOLD, 11));
            lblPregunta.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = i * 3;
            gbc.gridwidth = 1;
            panelFormulario.add(lblPregunta, gbc);

            // TextArea Pregunta
            txtPregunta[i] = new JTextArea(2, 30);
            txtPregunta[i].setFont(new Font("Arial", Font.PLAIN, 11));
            txtPregunta[i].setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(txtPregunta[i]);
            gbc.gridx = 1;
            gbc.gridy = i * 3;
            gbc.gridwidth = 2;
            panelFormulario.add(scrollPane, gbc);

            // Tipo de pregunta
            JLabel lblTipo = new JLabel("Tipo:");
            lblTipo.setFont(new Font("Arial", Font.PLAIN, 11));
            lblTipo.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = i * 3 + 1;
            gbc.gridwidth = 1;
            panelFormulario.add(lblTipo, gbc);

            cbxTipoPregunta[i] = new JComboBox<>(
                    new String[] { "Dicotomica", "Respuesta Simple", "Escala de Valoracion", "Numerico" });
            gbc.gridx = 1;
            gbc.gridy = i * 3 + 1;
            panelFormulario.add(cbxTipoPregunta[i], gbc);

            // Categoria
            JLabel lblCategoria = new JLabel("Categoria:");
            lblCategoria.setFont(new Font("Arial", Font.PLAIN, 11));
            lblCategoria.setForeground(Color.WHITE);
            gbc.gridx = 2;
            gbc.gridy = i * 3 + 1;
            panelFormulario.add(lblCategoria, gbc);

            cbxCategoria[i] = new JComboBox<>(
                    new String[] { "Salud", "Educacion", "Tecnologia", "Consumo y Habitos", "Medio Ambiente",
                            "Otros" });
            gbc.gridx = 3;
            gbc.gridy = i * 3 + 1;
            panelFormulario.add(cbxCategoria[i], gbc);

            // Filtro
            chkEsPreguntaFiltro[i] = new JCheckBox("Es pregunta filtro");
            chkEsPreguntaFiltro[i].setBackground(new Color(30, 60, 100));
            chkEsPreguntaFiltro[i].setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = i * 3 + 2;
            gbc.gridwidth = 4;
            panelFormulario.add(chkEsPreguntaFiltro[i], gbc);
        }

        JScrollPane scrollPanel = new JScrollPane(panelFormulario);
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelInferior.setBackground(new Color(30, 60, 100));

        btnGuardar = new JButton("Guardar Preguntas");
        btnGuardar.setPreferredSize(new Dimension(160, 45));
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPreguntas();
            }
        });

        btnAgregarMas = new JButton("Agregar Mas");
        btnAgregarMas.setPreferredSize(new Dimension(160, 45));
        btnAgregarMas.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAgregarMas.setBackground(new Color(50, 160, 50));
        btnAgregarMas.setForeground(Color.WHITE);
        btnAgregarMas.setOpaque(true);
        btnAgregarMas.setBorderPainted(false);
        btnAgregarMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                lblMensaje.setText("Formulario limpio. Ingrese nuevas preguntas.");
                lblMensaje.setForeground(new Color(0, 100, 150));
            }
        });

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(60, 150, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
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
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                cardLayout.show(panelPrincipal, "Home");
            }
        });

        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);

        panelInferior.add(btnGuardar);
        panelInferior.add(btnAgregarMas);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        JPanel panelConMensaje = new JPanel();
        panelConMensaje.setLayout(new BorderLayout());
        panelConMensaje.setBackground(new Color(30, 60, 100));
        panelConMensaje.add(lblMensaje, BorderLayout.NORTH);
        panelConMensaje.add(panelInferior, BorderLayout.SOUTH);

        add(panelConMensaje, BorderLayout.SOUTH);
    }

    private void guardarPreguntas() {
        int preguntasValidas = 0;

        for (int i = 0; i < NUM_PREGUNTAS; i++) {
            if (!txtPregunta[i].getText().trim().isEmpty()) {
                preguntasValidas++;
            }
        }

        if (preguntasValidas == 0) {
            lblMensaje.setText("Error: Debe ingresar al menos una pregunta");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        int preguntasGuardadas = 0;
        int numeroPregunta = 1;
        for (int i = 0; i < NUM_PREGUNTAS; i++) {
            if (!txtPregunta[i].getText().trim().isEmpty()) {
                String texto = txtPregunta[i].getText().trim();
                String tipo = (String) cbxTipoPregunta[i].getSelectedItem();
                String categoria = (String) cbxCategoria[i].getSelectedItem();
                boolean filtro = chkEsPreguntaFiltro[i].isSelected();

                if (guardarPreguntaEnBD(texto, tipo, categoria, filtro, numeroPregunta)) {
                    preguntasGuardadas++;
                }
                numeroPregunta++;
            }
        }

        if (preguntasGuardadas > 0) {
            lblMensaje.setText("Guardadas: " + preguntasGuardadas + " pregunta(s) exitosamente");
            lblMensaje.setForeground(new Color(0, 150, 0));
        } else {
            lblMensaje.setText("Error al guardar preguntas en BD");
            lblMensaje.setForeground(Color.RED);
        }
    }

    private boolean guardarPreguntaEnBD(String texto, String tipo, String categoria, boolean filtro,
            int numeroPregunta) {
        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO Preguntas (texto, tipo, categoria, es_pregunta_filtro, numero_pregunta) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, texto);
                pstmt.setString(2, tipo);
                pstmt.setString(3, categoria);
                pstmt.setBoolean(4, filtro);
                pstmt.setInt(5, numeroPregunta);

                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
                System.out.println("Pregunta guardada en BD: " + texto);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al guardar pregunta en BD: " + e.getMessage());
        }
        return false;
    }

    private void limpiarFormulario() {
        for (int i = 0; i < NUM_PREGUNTAS; i++) {
            txtPregunta[i].setText("");
            cbxTipoPregunta[i].setSelectedIndex(0);
            cbxCategoria[i].setSelectedIndex(0);
            chkEsPreguntaFiltro[i].setSelected(false);
        }
        lblMensaje.setText("");
    }
}
