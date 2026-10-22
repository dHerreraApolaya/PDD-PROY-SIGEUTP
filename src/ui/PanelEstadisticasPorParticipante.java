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
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelEstadisticasPorParticipante extends JPanel {
    private JTextField txtDNI;
    private JTextArea txtDatosParticipante;
    private JTable tablaEncuestasParticipante;
    private JLabel lblMensaje;
    private JButton btnBuscar;
    private JButton btnExportarCSV;
    private JButton btnExportarTXT;
    private JButton btnVolver;
    private JButton btnCerrar;

    public PanelEstadisticasPorParticipante(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Estadistica POR PARTICIPANTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelBusqueda.setBackground(new Color(30, 60, 100));

        JLabel lblDNI = new JLabel("DNI del Participante:");
        lblDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDNI.setForeground(Color.WHITE);
        panelBusqueda.add(lblDNI);

        txtDNI = new JTextField(12);
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        panelBusqueda.add(txtDNI);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParticipante();
            }
        });
        panelBusqueda.add(btnBuscar);

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Panel contenido con scroll
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        int fila = 0;

        // Datos del participante
        JLabel lblDatos = new JLabel("DATOS DEL PARTICIPANTE:");
        lblDatos.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDatos.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblDatos, gbc);
        fila++;

        txtDatosParticipante = new JTextArea(5, 80);
        txtDatosParticipante.setFont(new Font("Arial", Font.PLAIN, 11));
        txtDatosParticipante.setLineWrap(true);
        txtDatosParticipante.setWrapStyleWord(true);
        txtDatosParticipante.setEditable(false);
        txtDatosParticipante.setBackground(new Color(30, 60, 100));
        txtDatosParticipante.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = fila;
        gbc.weighty = 0.2;
        panelContenido.add(new JScrollPane(txtDatosParticipante), gbc);
        fila++;

        // Separador
        fila++;

        // Encuestas en las que participo
        JLabel lblEncuestas = new JLabel("ENCUESTAS EN LAS QUE participo:");
        lblEncuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEncuestas.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblEncuestas, gbc);
        fila++;

        String[] columnasEncuestas = { "ID Encuesta", "Nombre Encuesta", "Fecha Participacion" };
        Object[][] datosEncuestas = {
                { "001", "Satisfaccion del Servicio", "01/12/2025" },
                { "002", "Calidad de Atencion", "02/12/2025" }
        };

        tablaEncuestasParticipante = new JTable(new DefaultTableModel(datosEncuestas, columnasEncuestas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tablaEncuestasParticipante.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaEncuestasParticipante.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEncuestasParticipante.setRowHeight(25);

        gbc.gridy = fila;
        gbc.weighty = 0.3;
        panelContenido.add(new JScrollPane(tablaEncuestasParticipante), gbc);
        fila++;

        // Separador
        fila++;

        // Preguntas y respuestas
        JLabel lblRespuestas = new JLabel("PREGUNTAS Y RESPUESTAS:");
        lblRespuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRespuestas.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblRespuestas, gbc);
        fila++;

        String[] columnasRespuestas = { "ID Encuesta", "Pregunta No.", "Texto de Pregunta", "Respuesta Dada" };
        Object[][] datosRespuestas = {
                { "001", "1", "Como calificaria nuestro servicio?", "Muy bueno" },
                { "001", "2", "Recomendaria nuestro servicio?", "Si" },
                { "002", "1", "Nivel de Satisfaccion", "De acuerdo" }
        };

        JTable tablaRespuestas = new JTable(new DefaultTableModel(datosRespuestas, columnasRespuestas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tablaRespuestas.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaRespuestas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaRespuestas.setRowHeight(25);

        gbc.gridy = fila;
        gbc.weighty = 0.5;
        panelContenido.add(new JScrollPane(tablaRespuestas), gbc);

        JScrollPane scrollContenido = new JScrollPane(panelContenido);
        panelCentral.add(scrollContenido, BorderLayout.CENTER);

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMensaje.setBackground(new Color(30, 60, 100));
        panelMensaje.add(lblMensaje);
        panelCentral.add(panelMensaje, BorderLayout.SOUTH);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnExportarCSV = new JButton("Exportar CSV");
        btnExportarCSV.setPreferredSize(new Dimension(130, 40));
        btnExportarCSV.setFont(new Font("Arial", Font.PLAIN, 12));
        btnExportarCSV.setBackground(new Color(34, 139, 34));
        btnExportarCSV.setForeground(Color.WHITE);
        btnExportarCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarCSV();
            }
        });

        btnExportarTXT = new JButton("Exportar TXT");
        btnExportarTXT.setPreferredSize(new Dimension(130, 40));
        btnExportarTXT.setFont(new Font("Arial", Font.PLAIN, 12));
        btnExportarTXT.setBackground(new Color(34, 139, 34));
        btnExportarTXT.setForeground(Color.WHITE);
        btnExportarTXT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarTXT();
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
                cardLayout.show(panelPrincipal, "AnalisisInformes");
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

        panelInferior.add(btnExportarCSV);
        panelInferior.add(btnExportarTXT);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void buscarParticipante() {
        String dni = txtDNI.getText().trim();
        if (dni.isEmpty()) {
            lblMensaje.setText("Ingrese un DNI valido");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Cargar datos desde BD
        txtDatosParticipante.setText("DNI: " + dni + "\n" +
                "Nombre: Juan Perez Garcia\n" +
                "Email: juan.perez@example.com\n" +
                "Sexo: Masculino\n" +
                "Edad: 28");

        lblMensaje.setText("Participante encontrado");
        lblMensaje.setForeground(new Color(0, 150, 0));
    }

    private void exportarCSV() {
        try {
            String nombreArchivo = "estadisticas_participante_" + txtDNI.getText() + "_" + System.currentTimeMillis()
                    + ".csv";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("Estadisticas POR PARTICIPANTE\n\n");
            writer.write(txtDatosParticipante.getText());
            writer.close();

            lblMensaje.setText("Archivo CSV exportado: " + nombreArchivo);
            lblMensaje.setForeground(new Color(0, 150, 0));
        } catch (IOException e) {
            lblMensaje.setText("Error al exportar: " + e.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }

    private void exportarTXT() {
        try {
            String nombreArchivo = "estadisticas_participante_" + txtDNI.getText() + "_" + System.currentTimeMillis()
                    + ".txt";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("=".repeat(70) + "\n");
            writer.write("Estadisticas POR PARTICIPANTE\n");
            writer.write("=".repeat(70) + "\n\n");
            writer.write(txtDatosParticipante.getText());
            writer.close();

            lblMensaje.setText("Archivo TXT exportado: " + nombreArchivo);
            lblMensaje.setForeground(new Color(0, 150, 0));
        } catch (IOException e) {
            lblMensaje.setText("Error al exportar: " + e.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }
}

