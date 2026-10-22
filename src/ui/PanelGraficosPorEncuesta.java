package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelGraficosPorEncuesta extends JPanel {
    private JComboBox<String> cmbEncuestas;
    private JPanel panelGraficos;
    private JLabel lblMensaje;
    private JButton btnExportarCSV;
    private JButton btnExportarTXT;
    private JButton btnVolver;
    private JButton btnCerrar;

    public PanelGraficosPorEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(153, 102, 204));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("GraficoS POR ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel selector de encuesta
        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelSelector.setBackground(new Color(30, 60, 100));

        JLabel lblEncuesta = new JLabel("Seleccione Encuesta:");
        lblEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEncuesta.setForeground(Color.WHITE);
        panelSelector.add(lblEncuesta);

        cmbEncuestas = new JComboBox<String>();
        cmbEncuestas.addItem("-- Seleccione una encuesta --");
        cmbEncuestas.addItem("001 - Satisfaccion del Servicio");
        cmbEncuestas.addItem("002 - Calidad de Atencion");
        cmbEncuestas.addItem("003 - Evaluacion de Instalaciones");
        cmbEncuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbEncuestas.setPreferredSize(new Dimension(350, 30));
        cmbEncuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarGraficos();
            }
        });
        panelSelector.add(cmbEncuestas);

        // Panel central con Graficos
        panelGraficos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (cmbEncuestas.getSelectedIndex() > 0) {
                    dibujarGraficos((Graphics2D) g);
                }
            }
        };
        panelGraficos.setBackground(new Color(30, 60, 100));
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMensaje.setBackground(new Color(30, 60, 100));
        panelMensaje.add(lblMensaje);

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
        add(panelSelector, BorderLayout.NORTH);
        add(panelGraficos, BorderLayout.CENTER);
        add(panelMensaje, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(new Color(30, 60, 100));
        panelBotones.add(panelMensaje, BorderLayout.NORTH);
        panelBotones.add(panelInferior, BorderLayout.SOUTH);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarGraficos() {
        if (cmbEncuestas.getSelectedIndex() > 0) {
            panelGraficos.repaint();
        }
    }

    private void dibujarGraficos(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth() - 40;
        int alto = getHeight() - 200;

        // Grafico 1: DISTRIBUCION de respuestas (Pregunta 1)
        int x1 = 20;
        int y1 = 20;
        int anchoGrafico = ancho / 2 - 10;
        int altoGrafico = alto / 2 - 10;

        dibujarGraficoBarras(g2d, x1, y1, anchoGrafico, altoGrafico, "Pregunta 1: Satisfaccion General",
                new String[] { "Muy Bueno", "Bueno", "Regular", "Malo" },
                new int[] { 45, 35, 15, 5 },
                new Color[] { new Color(76, 175, 80), new Color(33, 150, 243), new Color(255, 152, 0),
                        new Color(244, 67, 54) });

        // Grafico 2: DISTRIBUCION Dicotomica (Pregunta 2)
        int x2 = x1 + anchoGrafico + 20;
        int y2 = y1;

        dibujarGraficoBarras(g2d, x2, y2, anchoGrafico, altoGrafico, "Pregunta 2: ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿Recomendaria?",
                new String[] { "Si", "No" },
                new int[] { 87, 13 },
                new Color[] { new Color(76, 175, 80), new Color(244, 67, 54) });

        // Grafico 3: DISTRIBUCION de escala (Pregunta 3)
        int x3 = x1;
        int y3 = y1 + altoGrafico + 20;

        dibujarGraficoBarras(g2d, x3, y3, anchoGrafico, altoGrafico, "Pregunta 3: Nivel de Acuerdo",
                new String[] { "1", "2", "3", "4", "5" },
                new int[] { 5, 10, 20, 35, 30 },
                new Color[] { new Color(244, 67, 54), new Color(255, 193, 7), new Color(255, 152, 0),
                        new Color(76, 175, 80), new Color(0, 150, 136) });

        // Grafico 4: Participacion por edad (Pregunta 4)
        int x4 = x2;
        int y4 = y3;

        dibujarGraficoBarras(g2d, x4, y4, anchoGrafico, altoGrafico, "Participantes por Edad",
                new String[] { "18-25", "26-35", "36-45", "46-55", "56+" },
                new int[] { 20, 35, 25, 15, 5 },
                new Color[] { new Color(156, 39, 176), new Color(103, 58, 183), new Color(63, 81, 181),
                        new Color(33, 150, 243), new Color(0, 188, 212) });
    }

    private void dibujarGraficoBarras(Graphics2D g2d, int x, int y, int ancho, int alto, String titulo,
            String[] etiquetas, int[] valores, Color[] colores) {
        // Fondo del Grafico
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, y, ancho, alto);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRect(x, y, ancho, alto);

        // Titulo
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(titulo, x + 10, y + 20);

        // Calcular Maximo valor
        int maxValor = 0;
        for (int v : valores) {
            if (v > maxValor)
                maxValor = v;
        }

        // Dibujar barras
        int espacioX = (ancho - 40) / etiquetas.length;
        int altoBarra = alto - 80;

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        for (int i = 0; i < etiquetas.length; i++) {
            int barraAlto = (valores[i] * altoBarra) / Math.max(maxValor, 100);
            int barraX = x + 20 + (i * espacioX) + 5;
            int barraY = y + alto - 40 - barraAlto;

            g2d.setColor(colores[i % colores.length]);
            g2d.fillRect(barraX, barraY, espacioX - 10, barraAlto);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(barraX, barraY, espacioX - 10, barraAlto);

            // Valor sobre la barra
            g2d.drawString(valores[i] + "%", barraX + (espacioX - 25) / 2, barraY - 5);

            // Etiqueta
            g2d.drawString(etiquetas[i], barraX - 5, y + alto - 20);
        }
    }

    private void exportarCSV() {
        try {
            String encuesta = cmbEncuestas.getSelectedItem().toString();
            String nombreArchivo = "graficos_encuesta_" + System.currentTimeMillis() + ".csv";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("GraficoS POR ENCUESTA\n\n");
            writer.write("Encuesta: " + encuesta + "\n");
            writer.write("Fecha de Generacion: "
                    + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) + "\n\n");
            writer.write("Pregunta 1,Satisfaccion General\n");
            writer.write("Muy Bueno,45%\n");
            writer.write("Bueno,35%\n");
            writer.write("Regular,15%\n");
            writer.write("Malo,5%\n\n");
            writer.write("Pregunta 2,ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿Recomendaria?\n");
            writer.write("Si,87%\n");
            writer.write("No,13%\n");
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
            String encuesta = cmbEncuestas.getSelectedItem().toString();
            String nombreArchivo = "graficos_encuesta_" + System.currentTimeMillis() + ".txt";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("=".repeat(70) + "\n");
            writer.write("GraficoS POR ENCUESTA\n");
            writer.write("=".repeat(70) + "\n\n");
            writer.write("Encuesta: " + encuesta + "\n");
            writer.write("Fecha de Generacion: "
                    + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) + "\n\n");
            writer.write("-".repeat(70) + "\n");
            writer.write("DISTRIBUCION DE RESPUESTAS\n");
            writer.write("-".repeat(70) + "\n\n");
            writer.write("Pregunta 1: Satisfaccion General\n");
            writer.write("Muy Bueno: 45%\n");
            writer.write("Bueno: 35%\n");
            writer.write("Regular: 15%\n");
            writer.write("Malo: 5%\n\n");
            writer.write("Pregunta 2: Recomendaria?\n");
            writer.write(" Si: 87%\n");
            writer.write(" No: 13%\n");
            writer.write("\n" + "=".repeat(70) + "\n");
            writer.close();

            lblMensaje.setText("Archivo TXT exportado: " + nombreArchivo);
            lblMensaje.setForeground(new Color(0, 150, 0));
        } catch (IOException e) {
            lblMensaje.setText("Error al exportar: " + e.getMessage());
            lblMensaje.setForeground(Color.RED);
        }
    }
}



