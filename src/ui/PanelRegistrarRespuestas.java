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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import modulo.encuestas.configuracion.Encuesta;
import modulo.participantes.Participante;
import modulo.preguntas.Pregunta;
import modulo.preguntas.PreguntaDAO;
import modulo.preguntas.TipoPregunta;
import modulo.respuestas.Respuesta;
import modulo.respuestas.RespuestaDAO;

public class PanelRegistrarRespuestas extends JPanel {
    private JLabel lblEncuestaID;
    private JLabel lblEncuestaTitulo;
    private JTextArea txtEncuestaDescripcion;
    private JPanel panelPreguntas;
    private JLabel lblTiempoRestante;
    private Timer timerContador;
    private int tiempoRestante = 900; // 15 minutos en segundos por defecto
    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private PanelEjecutarEncuesta panelEjecutarEncuesta;
    private Participante participanteActual;
    private Encuesta encuestaActual;
    private List<Pregunta> preguntasActuales = new ArrayList<>();
    private Map<Integer, JComponent> camposRespuestas = new LinkedHashMap<>();
    private JLabel lblMensaje;

    public PanelRegistrarRespuestas(CardLayout cardLayout, JPanel panelPrincipal) {
        this(cardLayout, panelPrincipal, null);
    }

    public PanelRegistrarRespuestas(CardLayout cardLayout, JPanel panelPrincipal,
            PanelEjecutarEncuesta panelEjecutarEncuesta) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;
        this.panelEjecutarEncuesta = panelEjecutarEncuesta;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setBackground(new Color(30, 60, 100));
        panelTitulo.add(lblTitulo);

        // Temporizador arriba a la derecha
        JPanel panelTimer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTimer.setBackground(new Color(30, 60, 100));
        lblTiempoRestante = new JLabel("15:00");
        lblTiempoRestante.setFont(new Font("Arial", Font.BOLD, 24));
        lblTiempoRestante.setForeground(Color.WHITE);
        panelTimer.add(lblTiempoRestante);

        panelSuperior.add(panelTitulo, BorderLayout.WEST);
        panelSuperior.add(panelTimer, BorderLayout.EAST);

        // Panel central con scroll
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel con informacion de encuesta
        JPanel panelInfoEncuesta = new JPanel(new GridBagLayout());
        panelInfoEncuesta.setBackground(new Color(30, 60, 100));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // ID Encuesta
        JLabel lblID = new JLabel("ID Encuesta:");
        lblID.setFont(new Font("Arial", Font.PLAIN, 12));
        lblID.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelInfoEncuesta.add(lblID, gbc);

        lblEncuestaID = new JLabel("--");
        lblEncuestaID.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEncuestaID.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelInfoEncuesta.add(lblEncuestaID, gbc);
        fila++;

        // Titulo Encuesta
        JLabel lblTituloEnc = new JLabel("Titulo:");
        lblTituloEnc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTituloEnc.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelInfoEncuesta.add(lblTituloEnc, gbc);

        lblEncuestaTitulo = new JLabel("Seleccione una encuesta");
        lblEncuestaTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEncuestaTitulo.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelInfoEncuesta.add(lblEncuestaTitulo, gbc);
        fila++;

        // Descripcion
        JLabel lblDesc = new JLabel("Descripcion:");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelInfoEncuesta.add(lblDesc, gbc);

        txtEncuestaDescripcion = new JTextArea(3, 50);
        txtEncuestaDescripcion.setFont(new Font("Arial", Font.PLAIN, 11));
        txtEncuestaDescripcion.setLineWrap(true);
        txtEncuestaDescripcion.setWrapStyleWord(true);
        txtEncuestaDescripcion.setEditable(false);
        txtEncuestaDescripcion.setText("");
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelInfoEncuesta.add(new JScrollPane(txtEncuestaDescripcion), gbc);
        fila++;

        // Separador
        fila++;

        // Seccion de preguntas
        JLabel lblPreguntas = new JLabel("PREGUNTAS:");
        lblPreguntas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPreguntas.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        panelInfoEncuesta.add(lblPreguntas, gbc);
        fila++;

        // Panel para preguntas con scroll
        panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new GridBagLayout());
        panelPreguntas.setBackground(new Color(30, 60, 100));

        JScrollPane scrollPreguntas = new JScrollPane(panelPreguntas);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelInfoEncuesta.add(scrollPreguntas, gbc);

        JScrollPane scrollCentral = new JScrollPane(panelInfoEncuesta);
        panelCentral.add(scrollCentral, BorderLayout.CENTER);
        lblMensaje = new JLabel("Seleccione un participante y una encuesta antes de responder.");
        lblMensaje.setForeground(Color.YELLOW);
        panelCentral.add(lblMensaje, BorderLayout.SOUTH);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnGuardar = new JButton("Finalizar encuesta");
        btnGuardar.setEnabled(false);
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestas();
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
                detenerTemporizador();
                cardLayout.show(panelPrincipal, "EjecutarEncuesta");
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
                detenerTemporizador();
                System.exit(0);
            }
        });

        panelInferior.add(btnGuardar);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        iniciarTemporizador();
    }

    public boolean prepararEncuesta(Participante participante, Encuesta encuesta) {
        participanteActual = null;
        encuestaActual = null;
        preguntasActuales.clear();
        camposRespuestas.clear();
        panelPreguntas.removeAll();
        btnGuardar.setEnabled(false);
        lblEncuestaID.setText("--");
        lblEncuestaTitulo.setText("Seleccione una encuesta");
        txtEncuestaDescripcion.setText("");
        try {
            if (participante == null || participante.getId_participante() <= 0
                    || encuesta == null || encuesta.getIdEncuesta() <= 0) {
                throw new IllegalArgumentException("Seleccione un participante y una encuesta registrados.");
            }
            List<Pregunta> preguntas = consultarPreguntas(encuesta.getIdEncuesta());
            if (preguntas.isEmpty()) {
                throw new IllegalArgumentException("La encuesta seleccionada no tiene preguntas activas.");
            }
            for (Pregunta pregunta : preguntas) {
                if (pregunta == null || pregunta.getIdPregunta() <= 0
                        || pregunta.getIdEncuesta() != encuesta.getIdEncuesta() || pregunta.getTipo() == null
                        || pregunta.getTexto() == null || pregunta.getTexto().isBlank()
                        || camposRespuestas.containsKey(pregunta.getIdPregunta())) {
                    throw new IllegalArgumentException("Las preguntas cargadas no son validas para esta encuesta.");
                }
                agregarPregunta(pregunta, preguntasActuales.size());
                preguntasActuales.add(pregunta);
            }
            participanteActual = participante;
            encuestaActual = encuesta;
            lblEncuestaID.setText(String.valueOf(encuesta.getIdEncuesta()));
            lblEncuestaTitulo.setText(encuesta.getTitulo());
            txtEncuestaDescripcion.setText(encuesta.getDescripcion());
            mostrarMensaje("Participante: " + participante.getNombre() + " | ID: "
                    + participante.getId_participante(), false);
            btnGuardar.setEnabled(true);
            return true;
        } catch (SQLException | IllegalArgumentException excepcion) {
            preguntasActuales.clear();
            camposRespuestas.clear();
            panelPreguntas.removeAll();
            mostrarMensaje("No se pudo cargar la encuesta: " + excepcion.getMessage(), true);
            return false;
        } finally {
            panelPreguntas.revalidate();
            panelPreguntas.repaint();
        }
    }

    protected List<Pregunta> consultarPreguntas(int idEncuesta) throws SQLException {
        return PreguntaDAO.consultarPreguntasPorEncuesta(idEncuesta);
    }

    private void agregarPregunta(Pregunta pregunta, int indice) {
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.insets = new Insets(10, 0, 10, 0);
        posicion.anchor = GridBagConstraints.WEST;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        posicion.weightx = 1.0;
        posicion.gridx = 0;
        posicion.gridy = indice * 2;
        JLabel etiqueta = new JLabel((indice + 1) + ". " + pregunta.getTexto());
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPreguntas.add(etiqueta, posicion);

        JComponent campo;
        switch (pregunta.getTipo()) {
            case RESPUESTA_SIMPLE:
            case NUMERICA:
                campo = new JTextField(30);
                break;
            case DICOTOMICA:
            case ESCALA_VALORACION:
                JComboBox<String> opciones = new JComboBox<>();
                opciones.addItem("-- Seleccione --");
                for (String opcion : pregunta.getOpciones()) {
                    opciones.addItem(opcion);
                }
                campo = opciones;
                break;
            default:
                throw new IllegalArgumentException("Tipo de pregunta no soportado.");
        }
        campo.setName("respuesta_" + pregunta.getIdPregunta());
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        etiqueta.setLabelFor(campo);
        posicion.gridy = indice * 2 + 1;
        panelPreguntas.add(campo, posicion);
        camposRespuestas.put(pregunta.getIdPregunta(), campo);
    }

    private void iniciarTemporizador() {
        timerContador = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                actualizarTiempo();

                if (tiempoRestante <= 0) {
                    detenerTemporizador();
                    // TODO: Guardar automaticamente y navegar
                }
            }
        });
        timerContador.start();
    }

    private void actualizarTiempo() {
        int minutos = tiempoRestante / 60;
        int segundos = tiempoRestante % 60;
        lblTiempoRestante.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void detenerTemporizador() {
        if (timerContador != null) {
            timerContador.stop();
        }
    }

    private void guardarRespuestas() {
        if (!btnGuardar.isEnabled()) {
            return;
        }
        try {
            List<Respuesta> respuestas = capturarRespuestas();
            btnGuardar.setEnabled(false);
            int cantidadGuardada = registrarRespuestas(respuestas);
            if (cantidadGuardada != respuestas.size()) {
                throw new SQLException("No se confirmo el guardado de todas las respuestas.");
            }
            detenerTemporizador();
            for (JComponent campo : camposRespuestas.values()) {
                campo.setEnabled(false);
            }
            String mensaje = "Encuesta " + encuestaActual.getIdEncuesta() + " finalizada. "
                    + cantidadGuardada + " respuestas guardadas para el participante "
                    + participanteActual.getId_participante() + ".";
            mostrarMensaje(mensaje, false);
            if (panelEjecutarEncuesta != null) {
                panelEjecutarEncuesta.mostrarResultado(mensaje);
            }
            cardLayout.show(panelPrincipal, "EjecutarEncuesta");
        } catch (SQLException | IllegalArgumentException excepcion) {
            btnGuardar.setEnabled(true);
            mostrarMensaje("No se finalizo la encuesta: " + excepcion.getMessage(), true);
        }
    }

    List<Respuesta> capturarRespuestas() {
        if (participanteActual == null || encuestaActual == null || preguntasActuales.isEmpty()) {
            throw new IllegalArgumentException("No hay una encuesta preparada para responder.");
        }
        List<Respuesta> respuestas = new ArrayList<>();
        for (Pregunta pregunta : preguntasActuales) {
            JComponent campo = camposRespuestas.get(pregunta.getIdPregunta());
            String contenido = obtenerContenido(pregunta, campo);
            Respuesta respuesta = new Respuesta();
            respuesta.setIdParticipante(participanteActual.getId_participante());
            respuesta.setIdEncuesta(encuestaActual.getIdEncuesta());
            respuesta.setIdPregunta(pregunta.getIdPregunta());
            respuesta.setContenido(contenido);
            respuesta.setFechaRespuesta(LocalDateTime.now());
            respuesta.setEsValida(true);
            respuestas.add(respuesta);
        }
        return respuestas;
    }

    private String obtenerContenido(Pregunta pregunta, JComponent campo) {
        String contenido;
        if (campo instanceof JTextField) {
            contenido = ((JTextField) campo).getText().trim();
        } else if (campo instanceof JComboBox<?>) {
            JComboBox<?> opciones = (JComboBox<?>) campo;
            contenido = opciones.getSelectedIndex() <= 0 ? "" : opciones.getSelectedItem().toString();
        } else {
            throw new IllegalArgumentException("No se encontro el campo de la pregunta " + pregunta.getIdPregunta());
        }
        if (contenido.isBlank()) {
            throw new IllegalArgumentException("Responda la pregunta " + pregunta.getIdPregunta() + ".");
        }
        if (pregunta.getTipo() == TipoPregunta.RESPUESTA_SIMPLE && contenido.length() > 500) {
            throw new IllegalArgumentException("La respuesta de texto no puede superar 500 caracteres.");
        }
        if (pregunta.getTipo() == TipoPregunta.NUMERICA) {
            try {
                double numero = Double.parseDouble(contenido);
                if (!Double.isFinite(numero)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException excepcion) {
                throw new IllegalArgumentException("Ingrese un numero valido para la pregunta " + pregunta.getIdPregunta());
            }
        }
        return contenido;
    }

    protected int registrarRespuestas(List<Respuesta> respuestas) throws SQLException {
        return RespuestaDAO.guardarRespuestasEncuesta(respuestas);
    }

    public String obtenerMensaje() {
        return lblMensaje.getText();
    }

    private void mostrarMensaje(String mensaje, boolean error) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(error ? Color.YELLOW : Color.WHITE);
    }
}
