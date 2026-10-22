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

public class PanelEditarPregunta extends JPanel {
    private static final int NUM_PREGUNTAS = 10;

    private JComboBox<String> cbxIdEncuesta;
    private JComboBox<String> cbxIdPregunta;

    private JTextArea[] txtPregunta = new JTextArea[NUM_PREGUNTAS];
    private JComboBox<String>[] cbxTipoPregunta = new JComboBox[NUM_PREGUNTAS];
    private JComboBox<String>[] cbxCategoria = new JComboBox[NUM_PREGUNTAS];
    private JCheckBox[] chkEsPreguntaFiltro = new JCheckBox[NUM_PREGUNTAS];

    private JButton btnGuardarCambios;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelEditarPregunta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("EDITAR PREGUNTA EXISTENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
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

        // Pregunta a Editar
        JLabel lblIdPregunta = new JLabel("Pregunta a Editar:");
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

        // Tipos de pregunta y Categorias
        String[] tiposPreguntas = { "Dicotomica", "Respuesta Simple", "Escala de Valoracion", "Numerico" };
        String[] categorias = { "Salud", "Educacion", "Tecnologia", "Consumo y Habitos", "Medio Ambiente", "Otros" };

        // Crear campos para editar (solo para 1 pregunta seleccionada, pero usaremos
        // los arrays)
        for (int i = 0; i < NUM_PREGUNTAS; i++) {
            // Numero de pregunta
            JLabel lblNumPregunta = new JLabel("Pregunta " + (i + 1) + ":");
            lblNumPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNumPregunta.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 4;
            panelFormulario.add(lblNumPregunta, gbc);
            fila++;

            // Texto de pregunta
            JLabel lblTextoPregunta = new JLabel("Texto de Pregunta:");
            lblTextoPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTextoPregunta.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(lblTextoPregunta, gbc);

            txtPregunta[i] = new JTextArea(2, 40);
            txtPregunta[i].setFont(new Font("Arial", Font.PLAIN, 11));
            txtPregunta[i].setLineWrap(true);
            txtPregunta[i].setWrapStyleWord(true);
            JScrollPane scrollTextoPregunta = new JScrollPane(txtPregunta[i]);
            scrollTextoPregunta.setPreferredSize(new Dimension(300, 40));
            gbc.gridx = 1;
            gbc.gridy = fila;
            gbc.gridwidth = 3;
            panelFormulario.add(scrollTextoPregunta, gbc);
            fila++;

            // Tipo de pregunta
            JLabel lblTipo = new JLabel("Tipo de Pregunta:");
            lblTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTipo.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(lblTipo, gbc);

            cbxTipoPregunta[i] = new JComboBox<>(tiposPreguntas);
            cbxTipoPregunta[i].setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 1;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(cbxTipoPregunta[i], gbc);

            // Categoria
            JLabel lblCat = new JLabel("Categoria:");
            lblCat.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCat.setForeground(Color.WHITE);
            gbc.gridx = 2;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(lblCat, gbc);

            cbxCategoria[i] = new JComboBox<>(categorias);
            cbxCategoria[i].setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 3;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(cbxCategoria[i], gbc);
            fila++;

            // Es pregunta filtro
            JLabel lblFiltro = new JLabel("ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿Es Pregunta Filtro?:");
            lblFiltro.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFiltro.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(lblFiltro, gbc);

            chkEsPreguntaFiltro[i] = new JCheckBox();
            chkEsPreguntaFiltro[i].setBackground(new Color(30, 60, 100));
            gbc.gridx = 1;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            panelFormulario.add(chkEsPreguntaFiltro[i], gbc);

            // Separador
            fila++;
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 4;
            JLabel lblSeparador = new JLabel("==================================");
            panelFormulario.add(lblSeparador, gbc);
            fila++;
        }

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblMensaje, gbc);

        JScrollPane scrollPane = new JScrollPane(panelFormulario);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setPreferredSize(new Dimension(130, 40));
        btnGuardarCambios.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardarCambios.setBackground(new Color(34, 139, 34));
        btnGuardarCambios.setForeground(Color.WHITE);

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setPreferredSize(new Dimension(160, 45));
        btnGuardarCambios.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardarCambios.setBackground(new Color(34, 139, 34));
        btnGuardarCambios.setForeground(Color.WHITE);
        btnGuardarCambios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
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

        panelInferior.add(btnGuardarCambios);
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
        // TODO: Cargar datos de la pregunta seleccionada desde BD
        String idPregunta = (String) cbxIdPregunta.getSelectedItem();
        lblMensaje.setText("Cargando pregunta ID: " + idPregunta);
        lblMensaje.setForeground(new Color(0, 100, 150));
    }

    private void guardarCambios() {
        String idEncuesta = (String) cbxIdEncuesta.getSelectedItem();
        String idPregunta = (String) cbxIdPregunta.getSelectedItem();
        String textoPregunta = txtPregunta[0].getText().trim();

        if (textoPregunta.isEmpty()) {
            lblMensaje.setText("Error: El texto de la pregunta no puede estar vacio");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Guardar cambios en la BD con: idEncuesta, idPregunta, textoPregunta,
        // tipo, Categoria, filtro
        lblMensaje.setText("Cambios guardados exitosamente para pregunta ID: " + idPregunta);
        lblMensaje.setForeground(new Color(0, 150, 0));
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




