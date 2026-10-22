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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelEliminarPregunta extends JPanel {
    private JComboBox<String> cbxIdEncuesta;
    private JComboBox<String> cbxIdPregunta;

    private JTextArea txtPregunta;
    private JLabel lblTipo;
    private JLabel lblCategoria;
    private JCheckBox chkEsPreguntaFiltro;

    private JButton btnEliminar;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelEliminarPregunta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("ELIMINAR PREGUNTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // ID de Encuesta
        JLabel lblIdEncuesta = new JLabel("ID de Encuesta:");
        lblIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdEncuesta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblIdEncuesta, gbc);

        cbxIdEncuesta = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }); // Placeholder
        cbxIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        cbxIdEncuesta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cargarPreguntasEncuesta();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(cbxIdEncuesta, gbc);
        fila++;

        // Pregunta a Eliminar
        JLabel lblIdPregunta = new JLabel("Pregunta a Eliminar:");
        lblIdPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdPregunta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblIdPregunta, gbc);

        cbxIdPregunta = new JComboBox<>(new String[] { "1", "2", "3" }); // Placeholder
        cbxIdPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        cbxIdPregunta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cargarPreguntaSeleccionada();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(cbxIdPregunta, gbc);
        fila++;

        // Separador
        fila++;

        // Texto de pregunta (solo lectura)
        JLabel lblTextoPregunta = new JLabel("Texto de Pregunta:");
        lblTextoPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTextoPregunta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblTextoPregunta, gbc);

        txtPregunta = new JTextArea(3, 40);
        txtPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPregunta.setLineWrap(true);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setEditable(false);
        txtPregunta.setBackground(new Color(220, 220, 220));
        JScrollPane scrollTextoPregunta = new JScrollPane(txtPregunta);
        scrollTextoPregunta.setPreferredSize(new Dimension(300, 60));
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(scrollTextoPregunta, gbc);
        fila++;

        // Tipo de pregunta (solo lectura)
        JLabel lblTipoLabel = new JLabel("Tipo de Pregunta:");
        lblTipoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTipoLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(lblTipoLabel, gbc);

        lblTipo = new JLabel("No especificado");
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTipo.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(lblTipo, gbc);
        fila++;

        // Categoria (solo lectura)
        JLabel lblCategoriaLabel = new JLabel("Categoria:");
        lblCategoriaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCategoriaLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblCategoriaLabel, gbc);

        lblCategoria = new JLabel("No especificada");
        lblCategoria.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCategoria.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(lblCategoria, gbc);
        fila++;

        // Es pregunta filtro (solo lectura)
        JLabel lblFiltroLabel = new JLabel("Es Pregunta Filtro?:");
        lblFiltroLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFiltroLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblFiltroLabel, gbc);

        chkEsPreguntaFiltro = new JCheckBox();
        chkEsPreguntaFiltro.setBackground(new Color(30, 60, 100));
        chkEsPreguntaFiltro.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(chkEsPreguntaFiltro, gbc);
        fila++;

        // Separador
        fila++;

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblMensaje, gbc);

        JScrollPane scrollPane = new JScrollPane(panelFormulario);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnEliminar = new JButton("Eliminar Pregunta");
        btnEliminar.setPreferredSize(new Dimension(160, 45));
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPregunta();
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

        panelInferior.add(btnEliminar);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarPreguntasEncuesta() {
        // TODO: Cargar preguntas de la encuesta seleccionada desde BD
        String idEncuesta = (String) cbxIdEncuesta.getSelectedItem();
        lblMensaje.setText("Cargando preguntas de encuesta ID: " + idEncuesta);
        lblMensaje.setForeground(new Color(0, 100, 150));
    }

    private void cargarPreguntaSeleccionada() {
        // TODO: Cargar datos de la pregunta seleccionada desde BD (solo lectura)
        String idPregunta = (String) cbxIdPregunta.getSelectedItem();
        lblMensaje.setText("Cargando pregunta ID: " + idPregunta);
        lblMensaje.setForeground(new Color(0, 100, 150));
    }

    private void eliminarPregunta() {
        String idPregunta = (String) cbxIdPregunta.getSelectedItem();
        String textoPregunta = txtPregunta.getText().trim();

        if (textoPregunta.isEmpty()) {
            lblMensaje.setText("Error: Debe seleccionar una pregunta para eliminar");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Eliminar la pregunta de la BD
        lblMensaje.setText("Pregunta ID: " + idPregunta + " eliminada exitosamente");
        lblMensaje.setForeground(new Color(0, 150, 0));
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtPregunta.setText("");
        lblTipo.setText("No especificado");
        lblCategoria.setText("No especificada");
        chkEsPreguntaFiltro.setSelected(false);
        lblMensaje.setText("");
    }
}





