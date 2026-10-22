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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class PanelEstadisticasPorEncuesta extends JPanel {
    private JComboBox<String> cmbEncuestas;
    private JTextArea txtEstadisticas;
    private JTable tablaRespuestas;
    private JLabel lblMensaje;
    private JButton btnExportarCSV;
    private JButton btnExportarTXT;
    private JButton btnVolver;
    private JButton btnCerrar;

    public PanelEstadisticasPorEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("Estadistica POR ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de seleccion
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelSeleccion.setBackground(new Color(30, 60, 100));

        JLabel lblIDEncuesta = new JLabel("ID de Encuesta:");
        lblIDEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIDEncuesta.setForeground(Color.WHITE);
        panelSeleccion.add(lblIDEncuesta);

        cmbEncuestas = new JComboBox<String>();
        cmbEncuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbEncuestas.setPreferredSize(new Dimension(300, 30));
        cmbEncuestas.addItem("-- Seleccione una encuesta --");
        cmbEncuestas.addItem("001 - Satisfaccion del Servicio");
        cmbEncuestas.addItem("002 - Calidad de Atencion");
        cmbEncuestas.addItem("003 - Producto y Empaques");
        cmbEncuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarEstadisticas();
            }
        });
        panelSeleccion.add(cmbEncuestas);

        panelCentral.add(panelSeleccion, BorderLayout.NORTH);

        // Panel contenido con scroll
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        int fila = 0;

        // Estadisticas generales
        JLabel lblEstadisticas = new JLabel("Estadisticas GENERALES:");
        lblEstadisticas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstadisticas.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblEstadisticas, gbc);
        fila++;

        txtEstadisticas = new JTextArea(8, 80);
        txtEstadisticas.setFont(new Font("Arial", Font.PLAIN, 11));
        txtEstadisticas.setLineWrap(true);
        txtEstadisticas.setWrapStyleWord(true);
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setBackground(new Color(30, 60, 100));
        txtEstadisticas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = fila;
        gbc.weighty = 0.3;
        panelContenido.add(new JScrollPane(txtEstadisticas), gbc);
        fila++;

        // Separador
        fila++;

        // Tabla de respuestas por pregunta
        JLabel lblRespuestas = new JLabel("RESUMEN ESTADISTICO POR PREGUNTA:");
        lblRespuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRespuestas.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblRespuestas, gbc);
        fila++;

        String[] columnas = { "Pregunta No.", "Texto", "Tipo", "Estadistica de Respuestas" };
        Object[][] datos = {
                { "1", "Como calificaria nuestro servicio?", "Numerica", "Promedio: 4.2" },
                { "2", "Recomendaria nuestro servicio?", "Dicotomica", "Si: 85% | No: 15%" },
                { "3", "Nivel de Satisfaccion", "Escala",
                        "De acuerdo: 60% | Ni de acuerdo ni en desacuerdo: 25% | En desacuerdo: 15%" }
        };

        tablaRespuestas = new JTable(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tablaRespuestas.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaRespuestas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaRespuestas.setRowHeight(30);
        tablaRespuestas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaRespuestas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaRespuestas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaRespuestas.getColumnModel().getColumn(3).setPreferredWidth(300);

        gbc.gridy = fila;
        gbc.weighty = 0.4;
        panelContenido.add(new JScrollPane(tablaRespuestas), gbc);
        fila++;

        // Separador
        fila++;

        // Historial de respuestas
        JLabel lblHistorial = new JLabel("HISTORIAL DE TODAS LAS RESPUESTAS:");
        lblHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
        lblHistorial.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.weighty = 0;
        panelContenido.add(lblHistorial, gbc);
        fila++;

        String[] columnasHistorial = { "Pregunta No.", "Texto", "Respuesta", "Participante DNI", "Fecha" };
        Object[][] datosHistorial = {
                { "1", "Como calificaria nuestro servicio?", "Muy bueno", "12345678", "01/12/2025" },
                { "2", "Recomendaria nuestro servicio?", "Si", "12345678", "01/12/2025" },
                { "3", "Nivel de Satisfaccion", "De acuerdo", "12345678", "01/12/2025" }
        };

        JTable tablaHistorial = new JTable(new DefaultTableModel(datosHistorial, columnasHistorial) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaHistorial.setRowHeight(25);

        gbc.gridy = fila;
        gbc.weighty = 0.3;
        panelContenido.add(new JScrollPane(tablaHistorial), gbc);

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

    private void cargarEstadisticas() {
        String encuestaSeleccionada = (String) cmbEncuestas.getSelectedItem();
        if (encuestaSeleccionada != null && !encuestaSeleccionada.equals("-- Seleccione una encuesta --")) {
            // TODO: Cargar Estadisticas desde BD
            txtEstadisticas.setText("ENCUESTA: Satisfaccion del Servicio\n\n" +
                    "ID: 001\n" +
                    "Descripcion: Evalue nuestro servicio\n" +
                    "Fecha de Estadistica: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "\n\n" +
                    "Total de Preguntas: 3\n" +
                    "Participantes Unicos: 5\n" +
                    "Total de Respuestas: 15");
        }
    }

    private void exportarCSV() {
        try {
            String nombreArchivo = "estadisticas_encuesta_" + System.currentTimeMillis() + ".csv";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("Estadisticas POR ENCUESTA\n\n");
            writer.write(txtEstadisticas.getText());
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
            String nombreArchivo = "estadisticas_encuesta_" + System.currentTimeMillis() + ".txt";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("=".repeat(70) + "\n");
            writer.write("Estadisticas POR ENCUESTA\n");
            writer.write("=".repeat(70) + "\n\n");
            writer.write(txtEstadisticas.getText());
            writer.close();

            lblMensaje.setText("Archivo TXT exportado: " + nombreArchivo);
            lblMensaje.setForeground(new Color(0, 150, 0));
        } catch (IOException e) {
            lblMensaje.setText("Error al exportar: " + e.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }
}

