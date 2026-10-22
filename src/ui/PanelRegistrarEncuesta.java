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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import conexionSQL.ConexionSQL;

public class PanelRegistrarEncuesta extends JPanel {
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JCheckBox chkEstado;
    private JCheckBox chkPerfilDetallado;

    private JComboBox<String> cbxSexo;
    private JComboBox<String> cbxGrupoEdad;
    private JComboBox<String> cbxNivelEducativo;

    private JPanel panelPerfilDetallado;
    private JTextField txtRetribucion;
    private JLabel lblIdEncuesta;

    private JButton btnGuardar;
    private JButton btnBancoPreguntas;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelRegistrarEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTituloPrincipal = new JLabel("REGISTRAR ENCUESTA");
        lblTituloPrincipal.setFont(new Font("Arial", Font.BOLD, 28));
        lblTituloPrincipal.setForeground(Color.WHITE);
        panelSuperior.add(lblTituloPrincipal);

        // Panel central con scroll para los campos
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Titulo
        JLabel lblTitulo = new JLabel("Titulo:");
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblTitulo, gbc);

        txtTitulo = new JTextField(30);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtTitulo, gbc);
        fila++;

        // Descripcion
        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblDescripcion, gbc);

        txtDescripcion = new JTextArea(4, 30);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(scrollDesc, gbc);
        fila++;

        // Estado (Activa/Inactiva)
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstado.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblEstado, gbc);

        chkEstado = new JCheckBox("Activa");
        chkEstado.setBackground(new Color(30, 60, 100));
        chkEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(chkEstado, gbc);
        fila++;

        // Separador visual
        fila++;

        // SubTitulo Perfil del Participante
        JLabel lblPerfilSubtitulo = new JLabel("PERFIL DEL PARTICIPANTE");
        lblPerfilSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPerfilSubtitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(lblPerfilSubtitulo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Checkbox Perfil Detallado
        chkPerfilDetallado = new JCheckBox("ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿Requiere perfil detallado?");
        chkPerfilDetallado.setBackground(new Color(30, 60, 100));
        chkPerfilDetallado.setFont(new Font("Arial", Font.PLAIN, 12));
        chkPerfilDetallado.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                activarDesactivarPerfilDetallado(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(chkPerfilDetallado, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Panel para los campos de perfil detallado (inicialmente desactivado)
        panelPerfilDetallado = new JPanel();
        panelPerfilDetallado.setLayout(new GridBagLayout());
        panelPerfilDetallado.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbcPerfil = new GridBagConstraints();
        gbcPerfil.insets = new Insets(8, 10, 8, 10);
        gbcPerfil.anchor = GridBagConstraints.WEST;
        gbcPerfil.fill = GridBagConstraints.HORIZONTAL;

        int filaPerfil = 0;

        // Sexo
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSexo.setForeground(Color.WHITE);
        gbcPerfil.gridx = 0;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(lblSexo, gbcPerfil);

        cbxSexo = new JComboBox<>(new String[] { "Ignorar", "Masculino", "Femenino" });
        cbxSexo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcPerfil.gridx = 1;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(cbxSexo, gbcPerfil);
        filaPerfil++;

        // Grupo de Edad
        JLabel lblEdad = new JLabel("Grupo de Edad:");
        lblEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEdad.setForeground(Color.WHITE);
        gbcPerfil.gridx = 0;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(lblEdad, gbcPerfil);

        cbxGrupoEdad = new JComboBox<>(new String[] {
                "Ignorar",
                "Nino (0-11)",
                "Adolescente (12-17)",
                "Joven (18-29)",
                "Adulto (30-59)",
                "Adulto Mayor (60+)"
        });
        cbxGrupoEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcPerfil.gridx = 1;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(cbxGrupoEdad, gbcPerfil);
        filaPerfil++;

        // Nivel Educativo
        JLabel lblNivelEducativo = new JLabel("Nivel Educativo:");
        lblNivelEducativo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNivelEducativo.setForeground(Color.WHITE);
        gbcPerfil.gridx = 0;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(lblNivelEducativo, gbcPerfil);

        cbxNivelEducativo = new JComboBox<>(new String[] {
                "Ignorar",
                "Sin estudios",
                "Primaria",
                "Secundaria",
                "Superior"
        });
        cbxNivelEducativo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcPerfil.gridx = 1;
        gbcPerfil.gridy = filaPerfil;
        panelPerfilDetallado.add(cbxNivelEducativo, gbcPerfil);
        filaPerfil++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelPerfilDetallado.setEnabled(false);
        panelFormulario.add(panelPerfilDetallado, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Separador visual
        fila++;

        // SubTitulo Pago por Responder Encuesta
        JLabel lblPagoSubtitulo = new JLabel("PAGO POR RESPONDER ENCUESTA");
        lblPagoSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPagoSubtitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(lblPagoSubtitulo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Retribucion
        JLabel lblRetribucion = new JLabel("Retribucion (Soles):");
        lblRetribucion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRetribucion.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblRetribucion, gbc);

        txtRetribucion = new JTextField("5");
        txtRetribucion.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(txtRetribucion, gbc);
        fila++;

        // Separador visual
        fila++;

        // ID de la Encuesta (solo lectura, se muestra despues de guardar)
        JLabel lblIdLabel = new JLabel("ID Encuesta:");
        lblIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblIdLabel, gbc);

        lblIdEncuesta = new JLabel("Se asignara automaticamente");
        lblIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdEncuesta.setForeground(new Color(0, 100, 0));
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(lblIdEncuesta, gbc);
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

        // Envolver en scroll
        JScrollPane scrollPane = new JScrollPane(panelFormulario);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

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
                guardarEncuesta();
            }
        });

        btnBancoPreguntas = new JButton("Banco de Preguntas");
        btnBancoPreguntas.setPreferredSize(new Dimension(150, 40));
        btnBancoPreguntas.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBancoPreguntas.setBackground(new Color(34, 139, 34));
        btnBancoPreguntas.setForeground(Color.WHITE);
        btnBancoPreguntas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "BancoPreguntas");
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
                cardLayout.show(panelPrincipal, "ConfiguracionEncuesta");
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
        panelInferior.add(btnBancoPreguntas);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Desactivar inicialmente los campos de perfil detallado
        activarDesactivarPerfilDetallado(false);
    }

    private void activarDesactivarPerfilDetallado(boolean activar) {
        cbxSexo.setEnabled(activar);
        cbxGrupoEdad.setEnabled(activar);
        cbxNivelEducativo.setEnabled(activar);
        panelPerfilDetallado.setEnabled(activar);
    }

    private void guardarEncuesta() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String retribucionStr = txtRetribucion.getText().trim();
        boolean estado = chkEstado.isSelected();
        boolean perfilDetallado = chkPerfilDetallado.isSelected();

        // Validaciones
        if (titulo.isEmpty()) {
            lblMensaje.setText("Error: El Titulo no puede estar vacio");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        if (descripcion.isEmpty()) {
            lblMensaje.setText("Error: La Descripcion no puede estar vacia");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        if (retribucionStr.isEmpty()) {
            lblMensaje.setText("Error: La Retribucion no puede estar vacia");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Validar que Retribucion sea un Numero valido
        try {
            Double.parseDouble(retribucionStr);
        } catch (NumberFormatException e) {
            lblMensaje.setText("Error: La Retribucion debe ser un Numero valido");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Obtener valores del perfil detallado si esta habilitado
        String sexo = perfilDetallado ? (String) cbxSexo.getSelectedItem() : "No especificado";
        String grupoEdad = perfilDetallado ? (String) cbxGrupoEdad.getSelectedItem() : "No especificado";
        String nivelEducativo = perfilDetallado ? (String) cbxNivelEducativo.getSelectedItem() : "No especificado";
        double retribucion = Double.parseDouble(retribucionStr);

        // Guardar en la base de datos
        int idEncuestaGenerado = guardarEncuestaEnBD(titulo, descripcion, estado, sexo, grupoEdad, nivelEducativo,
                retribucion);

        if (idEncuestaGenerado > 0) {
            lblIdEncuesta.setText("ID: " + idEncuestaGenerado);
            lblMensaje.setText(
                    "ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã¢â‚¬Â¦ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã¢â‚¬Å“ Encuesta guardada exitosamente (Retribucion: S/. " + String.format("%.2f", retribucion) + ")");
            lblMensaje.setForeground(new Color(0, 150, 0));
        } else {
            lblMensaje.setText("Error al guardar encuesta en BD");
            lblMensaje.setForeground(Color.RED);
        }
    }

    private int guardarEncuestaEnBD(String titulo, String descripcion, boolean estado, String sexo, String grupoEdad,
            String nivelEducativo, double retribucion) {
        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO Encuestas (titulo, descripcion, estado, sexo, grupo_edad, nivel_educativo, retribucion)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, titulo);
                pstmt.setString(2, descripcion);
                pstmt.setBoolean(3, estado);
                pstmt.setString(4, sexo);
                pstmt.setString(5, grupoEdad);
                pstmt.setString(6, nivelEducativo);
                pstmt.setDouble(7, retribucion);

                pstmt.executeUpdate();
                java.sql.ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idEncuesta = rs.getInt(1);
                    rs.close();
                    pstmt.close();
                    conn.close();
                    System.out.println("Encuesta guardada en BD con ID: " + idEncuesta);
                    return idEncuesta;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al guardar encuesta en BD: " + e.getMessage());
        }
        return 0;
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtRetribucion.setText("5");
        chkEstado.setSelected(false);
        chkPerfilDetallado.setSelected(false);
        cbxSexo.setSelectedIndex(0);
        cbxGrupoEdad.setSelectedIndex(0);
        cbxNivelEducativo.setSelectedIndex(0);
        lblIdEncuesta.setText("Se asignara automaticamente");
        lblMensaje.setText("");
        activarDesactivarPerfilDetallado(false);
    }
}

