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
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import conexionSQL.ConexionSQL;
import modulo.participantes.Participante;
import modulo.participantes.ParticipanteDAO;

public class PanelRegistroParticipante extends JPanel {
    private JTextField txtNombre;
    private JTextField txtDNI;
    private JTextField txtEmail;
    private JComboBox<String> cbxSexo;
    private JTextField txtEdad;
    private JComboBox<String> cbxNivelEducativo;
    private JButton btnGuardar;
    private JButton btnContinuar;
    private JButton btnVolver;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private PanelEjecutarEncuesta panelEjecutarEncuesta;

    public PanelRegistroParticipante(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("REGISTRO DE PARTICIPANTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con formulario (dentro de un scroll para que quepa todo)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Label Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombre.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblNombre, gbc);

        // TextField Nombre
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtNombre, gbc);
        fila++;

        // Label DNI
        JLabel lblDNI = new JLabel("DNI:");
        lblDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDNI.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblDNI, gbc);

        // TextField DNI
        txtDNI = new JTextField(20);
        txtDNI.setName("dniParticipante");
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtDNI, gbc);
        fila++;

        // Label Email
        JLabel lblEmail = new JLabel("Correo Electronico:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEmail.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblEmail, gbc);

        // TextField Email
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtEmail, gbc);
        fila++;

        // Label Sexo
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSexo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblSexo, gbc);

        // ComboBox Sexo
        cbxSexo = new JComboBox<>(new String[] { "Masculino", "Femenino" });
        cbxSexo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(cbxSexo, gbc);
        fila++;

        // Label Edad
        JLabel lblEdad = new JLabel("Edad (0-120):");
        lblEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEdad.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblEdad, gbc);

        // TextField Edad (solo numeros)
        txtEdad = new JTextField(20);
        txtEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        // Permitir solo numeros
        ((javax.swing.text.AbstractDocument) txtEdad.getDocument()).setDocumentFilter(
                new javax.swing.text.DocumentFilter() {
                    public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String text,
                            javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                        if (text == null)
                            return;
                        if (text.matches("[0-9]*")) {
                            fb.insertString(offset, text, attr);
                        }
                    }

                    public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length,
                            String text,
                            javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                        if (text == null)
                            return;
                        if (text.matches("[0-9]*")) {
                            fb.replace(offset, length, text, attr);
                        }
                    }
                });
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtEdad, gbc);
        fila++;

        // Label Nivel Educativo
        JLabel lblNivelEducativo = new JLabel("Nivel Educativo:");
        lblNivelEducativo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNivelEducativo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblNivelEducativo, gbc);

        // ComboBox Nivel Educativo
        cbxNivelEducativo = new JComboBox<>(new String[] { "Sin estudios", "Primaria", "Secundaria", "Superior" });
        cbxNivelEducativo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(cbxNivelEducativo, gbc);
        fila++;

        // Label de mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblMensaje, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(30, 60, 100));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(140, 45));
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarParticipante();
            }
        });

        btnContinuar = new JButton("Continuar");
        btnContinuar.setPreferredSize(new Dimension(140, 45));
        btnContinuar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnContinuar.setBackground(new Color(34, 139, 34));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuarEncuesta();
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
                cardLayout.show(panelPrincipal, "GestionParticipante");
            }
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnContinuar);
        panelBotones.add(btnVolver);

        // Panel contenedor para centralizar
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBackground(new Color(30, 60, 100));
        panelContenedor.add(Box.createVerticalGlue());
        panelContenedor.add(panelFormulario);
        panelContenedor.add(panelBotones);
        panelContenedor.add(Box.createVerticalGlue());

        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);
    }

    private void guardarParticipante() {
        String nombre = txtNombre.getText().trim();
        String dni = txtDNI.getText().trim();
        String email = txtEmail.getText().trim();
        String sexo = (String) cbxSexo.getSelectedItem();
        String edadStr = txtEdad.getText().trim();
        String nivelEducativo = (String) cbxNivelEducativo.getSelectedItem();

        // Validar Nombre
        if (nombre.isEmpty()) {
            lblMensaje.setText("Error: El nombre no puede estar vacio");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        if (nombre.length() > 100) {
            lblMensaje.setText("Error: El nombre no puede exceder 100 caracteres");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Validar DNI
        if (!dni.matches("\\d{8}")) {
            lblMensaje.setText("Error: DNI debe contener exactamente 8 digitos");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Validar DNI prohibidos
        String[] dniProhibidos = { "00000000", "11111111", "22222222", "33333333", "44444444",
                "55555555", "66666666", "77777777", "88888888", "12345678", "87654321" };
        for (String prohibido : dniProhibidos) {
            if (dni.equals(prohibido)) {
                lblMensaje.setText("Error: DNI prohibido. Intente con otro");
                lblMensaje.setForeground(Color.RED);
                return;
            }
        }

        // Validar Email
        if (email.isEmpty()) {
            lblMensaje.setText("Error: El correo no puede estar vacio");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        if (!email.contains("@")) {
            lblMensaje.setText("Error: El correo debe contener '@'");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Validar Edad
        if (edadStr.isEmpty()) {
            lblMensaje.setText("Error: La edad no puede estar vacia");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 0 || edad > 120) {
                lblMensaje.setText("Error: La edad debe estar entre 0 y 120");
                lblMensaje.setForeground(Color.RED);
                return;
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("Error: Ingrese una edad valida");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Guardar en BD
        if (guardarParticipanteEnBD(nombre, dni, email, sexo, edad, nivelEducativo)) {
            String confirmacion = String.format(
                    "Participante guardado exitosamente: %s | DNI: %s",
                    nombre, dni);
            lblMensaje.setText(confirmacion);
            lblMensaje.setForeground(new Color(0, 150, 0));
        } else {
            lblMensaje.setText("Error al guardar participante en BD");
            lblMensaje.setForeground(Color.RED);
        }
    }

    private boolean guardarParticipanteEnBD(String nombre, String dni, String email, String sexo, int edad,
            String nivelEducativo) {
        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO Participantes (dni, nombre, correo, sexo, edad, nivel_educativo, fecha_registro, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, dni);
                pstmt.setString(2, nombre);
                pstmt.setString(3, email);
                pstmt.setString(4, sexo);
                pstmt.setInt(5, edad);
                pstmt.setString(6, nivelEducativo);
                pstmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.setString(8, "ACTIVO");

                int resultado = pstmt.executeUpdate();
                pstmt.close();
                conn.close();

                if (resultado > 0) {
                    System.out.println("Participante guardado en BD: " + nombre + " (" + dni + ")");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al guardar participante en BD: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDNI.setText("");
        txtEmail.setText("");
        txtEdad.setText("");
        cbxSexo.setSelectedIndex(0);
        cbxNivelEducativo.setSelectedIndex(0);
        lblMensaje.setText("");
    }

    public void establecerPanelEjecucion(PanelEjecutarEncuesta panelEjecutarEncuesta) {
        this.panelEjecutarEncuesta = panelEjecutarEncuesta;
    }

    protected Participante consultarParticipante(String dni) throws SQLException {
        return new ParticipanteDAO().consultarParticipanteParaEncuesta(dni);
    }

    private void continuarEncuesta() {
        String dni = txtDNI.getText().trim();
        if (!dni.matches("\\d{8}")) {
            lblMensaje.setText("Ingrese el DNI de 8 digitos de un participante registrado.");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        if (panelEjecutarEncuesta == null) {
            lblMensaje.setText("No se encontro el panel de ejecucion de encuestas.");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        try {
            Participante participante = consultarParticipante(dni);
            if (participante == null) {
                lblMensaje.setText("Participante no registrado. Guarde sus datos antes de continuar.");
                lblMensaje.setForeground(Color.RED);
                return;
            }
            if (!panelEjecutarEncuesta.cargarEncuestas(participante)) {
                lblMensaje.setText(panelEjecutarEncuesta.obtenerMensaje());
                lblMensaje.setForeground(Color.RED);
                return;
            }
            lblMensaje.setText("Participante identificado: " + participante.getNombre());
            lblMensaje.setForeground(new Color(0, 150, 0));
            cardLayout.show(panelPrincipal, "EjecutarEncuesta");
        } catch (SQLException excepcion) {
            lblMensaje.setText("No se pudo identificar al participante: " + excepcion.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }
}
