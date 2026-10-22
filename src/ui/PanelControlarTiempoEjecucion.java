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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelControlarTiempoEjecucion extends JPanel {
    private JComboBox<String> cmbEncuestas;
    private JLabel lblNumeroPreguntas;
    private JTextField txtTiempoMinutos;
    private JLabel lblTiempoTotal;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelControlarTiempoEjecucion(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("ASIGNAR TIEMPO POR PREGUNTA DE LA ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // ID de Encuesta
        JLabel lblIDEncuesta = new JLabel("ID de Encuesta:");
        lblIDEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIDEncuesta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelCentral.add(lblIDEncuesta, gbc);

        cmbEncuestas = new JComboBox<String>();
        cmbEncuestas.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbEncuestas.setPreferredSize(new Dimension(300, 35));
        // TODO: Cargar todas las encuestas registradas desde BD
        cmbEncuestas.addItem("-- Seleccione una encuesta --");
        cmbEncuestas.addItem("001 - Satisfaccion del Servicio");
        cmbEncuestas.addItem("002 - Calidad de Atencion");
        cmbEncuestas.addItem("003 - Producto y Empaques");
        cmbEncuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNumeroPreguntas();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(cmbEncuestas, gbc);
        fila++;

        // Separador
        fila++;

        // Numero de Preguntas
        JLabel lblNumPreguntas = new JLabel("Numero de Preguntas:");
        lblNumPreguntas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNumPreguntas.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelCentral.add(lblNumPreguntas, gbc);

        lblNumeroPreguntas = new JLabel("0");
        lblNumeroPreguntas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNumeroPreguntas.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(lblNumeroPreguntas, gbc);
        fila++;

        // Separador
        fila++;

        // Tiempo Asignado por Pregunta
        JLabel lblTiempoAsignado = new JLabel("Tiempo Asignado por Pregunta (minutos):");
        lblTiempoAsignado.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTiempoAsignado.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelCentral.add(lblTiempoAsignado, gbc);

        txtTiempoMinutos = new JTextField(10);
        txtTiempoMinutos.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTiempoMinutos.setPreferredSize(new Dimension(150, 35));
        txtTiempoMinutos.setText("1");
        // Filtrar solo numeros
        ((javax.swing.text.AbstractDocument) txtTiempoMinutos.getDocument())
                .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String text,
                            javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                        if (text.matches("[0-9]*")) {
                            fb.insertString(offset, text, attr);
                            actualizarTiempoTotal();
                        }
                    }

                    @Override
                    public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length,
                            String text, javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        if (text.matches("[0-9]*")) {
                            fb.replace(offset, length, text, attr);
                            actualizarTiempoTotal();
                        }
                    }
                });
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(txtTiempoMinutos, gbc);
        fila++;

        // Separador
        fila++;

        // Tiempo Total
        JLabel lblTotalEncuesta = new JLabel("Tiempo Total de la Encuesta:");
        lblTotalEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTotalEncuesta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelCentral.add(lblTotalEncuesta, gbc);

        lblTiempoTotal = new JLabel("0 minutos");
        lblTiempoTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiempoTotal.setForeground(new Color(0, 100, 0));
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(lblTiempoTotal, gbc);

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
                guardarTiempo();
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
                cardLayout.show(panelPrincipal, "RealizacionEncuesta");
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
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarNumeroPreguntas() {
        // TODO: Cargar Numero de preguntas desde BD segun encuesta seleccionada
        String encuestaSeleccionada = (String) cmbEncuestas.getSelectedItem();
        if (encuestaSeleccionada != null && !encuestaSeleccionada.equals("-- Seleccione una encuesta --")) {
            // Placeholder: simulamos 5 preguntas
            lblNumeroPreguntas.setText("5");
            actualizarTiempoTotal();
        } else {
            lblNumeroPreguntas.setText("0");
            lblTiempoTotal.setText("0 minutos");
        }
    }

    private void actualizarTiempoTotal() {
        try {
            int numPreguntas = Integer.parseInt(lblNumeroPreguntas.getText());
            int tiempoMinuto = Integer
                    .parseInt(txtTiempoMinutos.getText().isEmpty() ? "0" : txtTiempoMinutos.getText());
            int tiempoTotal = numPreguntas * tiempoMinuto;
            lblTiempoTotal.setText(tiempoTotal + " minutos");
        } catch (NumberFormatException e) {
            lblTiempoTotal.setText("0 minutos");
        }
    }

    private void guardarTiempo() {
        // TODO: Guardar Configuracion de tiempo en BD
        cardLayout.show(panelPrincipal, "RealizacionEncuesta");
    }
}



