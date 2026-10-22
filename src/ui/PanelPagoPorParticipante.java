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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import conexionSQL.ConexionSQL;

public class PanelPagoPorParticipante extends JPanel {
    private JTextField txtDNI;
    private JTextField txtNombreParticipante;
    private JLabel lblMensajeBusqueda;
    private JTable tablaEncuestasCompletadas;
    private JTextField txtTotalSoles;
    private JTextArea txtAreaComprobante;
    private JButton btnBuscar;
    private JButton btnExportarTXT;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelPagoPorParticipante(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("PAGO POR PARTICIPANTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central principal
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ========== Seccion 1: busqueda POR DNI ==========
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBackground(new Color(30, 60, 100));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "busqueda DE PARTICIPANTE", 0, 0, new Font("Arial", Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // DNI input
        JLabel lblDNI = new JLabel("Ingrese DNI del Participante:");
        lblDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDNI.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelBusqueda.add(lblDNI, gbc);

        txtDNI = new JTextField(15);
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelBusqueda.add(txtDNI, gbc);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBuscar.setBackground(new Color(34, 139, 34));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParticipante();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelBusqueda.add(btnBuscar, gbc);
        fila++;

        // Nombre participante (lectura)
        JLabel lblNombre = new JLabel("Nombre del Participante:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombre.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelBusqueda.add(lblNombre, gbc);

        txtNombreParticipante = new JTextField(20);
        txtNombreParticipante.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNombreParticipante.setEditable(false);
        txtNombreParticipante.setBackground(new Color(220, 220, 220));
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelBusqueda.add(txtNombreParticipante, gbc);
        fila++;

        // Mensaje
        lblMensajeBusqueda = new JLabel("");
        lblMensajeBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensajeBusqueda.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panelBusqueda.add(lblMensajeBusqueda, gbc);

        // ========== Seccion 2: ENCUESTAS COMPLETADAS ==========
        JPanel panelEncuestas = new JPanel(new BorderLayout());
        panelEncuestas.setBackground(new Color(30, 60, 100));
        panelEncuestas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "ENCUESTAS COMPLETADAS", 0, 0, new Font("Arial", Font.BOLD, 12)));
        panelEncuestas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla de encuestas completadas
        String[] columnasEncuestas = { "ID Encuesta", "Nombre Encuesta", "Fecha Completada", "Monto (S/.)" };
        Object[][] datosEncuestas = {}; // TODO: Cargar desde BD
        tablaEncuestasCompletadas = new JTable(datosEncuestas, columnasEncuestas);
        tablaEncuestasCompletadas.setEnabled(false);
        tablaEncuestasCompletadas.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaEncuestasCompletadas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEncuestasCompletadas.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaEncuestasCompletadas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelEncuestas.add(scrollPane, BorderLayout.CENTER);

        // Panel de total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelTotal.setBackground(new Color(30, 60, 100));

        JLabel lblTotal = new JLabel("TOTAL A PAGAR (SOLES):");
        lblTotal.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTotal.setForeground(Color.WHITE);
        panelTotal.add(lblTotal);

        txtTotalSoles = new JTextField(10);
        txtTotalSoles.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalSoles.setEditable(false);
        txtTotalSoles.setBackground(new Color(200, 255, 200));
        txtTotalSoles.setForeground(new Color(0, 100, 0));
        panelTotal.add(txtTotalSoles);

        JPanel panelEncuestasCompleto = new JPanel(new BorderLayout());
        panelEncuestasCompleto.add(panelEncuestas, BorderLayout.CENTER);
        panelEncuestasCompleto.add(panelTotal, BorderLayout.SOUTH);

        // ========== Seccion 3: COMPROBANTE DE PAGO (PREVIEW SUNAT) ==========
        JPanel panelComprobante = new JPanel(new BorderLayout());
        panelComprobante.setBackground(new Color(30, 60, 100));
        panelComprobante.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "PREVIEW DE COMPROBANTE DE PAGO", 0, 0, new Font("Arial", Font.BOLD, 12)));

        txtAreaComprobante = new JTextArea(8, 60);
        txtAreaComprobante.setFont(new Font("Courier New", Font.PLAIN, 10));
        txtAreaComprobante.setEditable(false);
        txtAreaComprobante.setBackground(new Color(30, 60, 100));
        txtAreaComprobante.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollComprobante = new JScrollPane(txtAreaComprobante);
        panelComprobante.add(scrollComprobante, BorderLayout.CENTER);

        // Separador central
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbcContenido = new GridBagConstraints();
        gbcContenido.fill = GridBagConstraints.BOTH;
        gbcContenido.weightx = 1.0;
        gbcContenido.insets = new Insets(10, 0, 10, 0);

        gbcContenido.gridy = 0;
        gbcContenido.weighty = 0.3;
        panelContenido.add(panelBusqueda, gbcContenido);

        gbcContenido.gridy = 1;
        gbcContenido.weighty = 0.4;
        panelContenido.add(panelEncuestasCompleto, gbcContenido);

        gbcContenido.gridy = 2;
        gbcContenido.weighty = 0.3;
        panelContenido.add(panelComprobante, gbcContenido);

        JScrollPane scrollCentral = new JScrollPane(panelContenido);
        panelCentral.add(scrollCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnExportarTXT = new JButton("Exportar a TXT");
        btnExportarTXT.setPreferredSize(new Dimension(160, 45));
        btnExportarTXT.setFont(new Font("Arial", Font.PLAIN, 12));
        btnExportarTXT.setBackground(new Color(34, 139, 34));
        btnExportarTXT.setForeground(Color.WHITE);
        btnExportarTXT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarComprobante();
            }
        });

        JButton btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistroParticipante");
            }
        });

        JButton btnCerrar = new JButton("Cerrar");
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
            lblMensajeBusqueda.setText("Error: Ingrese un DNI valido");
            lblMensajeBusqueda.setForeground(Color.RED);
            txtNombreParticipante.setText("");
            return;
        }

        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                // Buscar participante en BD
                String sqlParticipante = "SELECT nombre FROM Participantes WHERE dni = ?";
                PreparedStatement pstmt = conn.prepareStatement(sqlParticipante);
                pstmt.setString(1, dni);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String nombreParticipante = rs.getString("nombre");
                    txtNombreParticipante.setText(nombreParticipante);

                    // Buscar encuestas completadas del participante
                    String sqlRespuestas = "SELECT COUNT(DISTINCT id_pregunta) as cantidad FROM Respuestas WHERE 1=1";
                    Statement stmt = conn.createStatement();
                    ResultSet rsRespuestas = stmt.executeQuery(sqlRespuestas);

                    int cantidadEncuestas = 0;
                    if (rsRespuestas.next()) {
                        cantidadEncuestas = rsRespuestas.getInt("cantidad");
                    }

                    lblMensajeBusqueda
                            .setText("Participante encontrado - " + cantidadEncuestas + " encuestas completadas");
                    lblMensajeBusqueda.setForeground(new Color(0, 150, 0));

                    // Simular calculo de total (S/. 5.00 por encuesta)
                    double totalPago = cantidadEncuestas * 5.00;
                    txtTotalSoles.setText(String.format("%.2f", totalPago));

                    // Generar preview de comprobante
                    generarComprobantePreview(dni, nombreParticipante, cantidadEncuestas, totalPago);

                    rsRespuestas.close();
                    stmt.close();
                } else {
                    lblMensajeBusqueda.setText("Error: Participante no encontrado en la BD");
                    lblMensajeBusqueda.setForeground(Color.RED);
                    txtNombreParticipante.setText("");
                }

                rs.close();
                pstmt.close();
                conn.close();
            }
        } catch (Exception e) {
            lblMensajeBusqueda.setText("Error al buscar participante: " + e.getMessage());
            lblMensajeBusqueda.setForeground(Color.RED);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generarComprobantePreview(String dni, String nombreParticipante, int cantidadEncuestas,
            double totalPago) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaActual = sdf.format(new Date());
        String idComprobante = "001-" + System.currentTimeMillis();
        String codigoGiro = "GIRO-" + String.format("%08d", (long) (Math.random() * 100000000));

        StringBuilder comprobante = new StringBuilder();
        comprobante.append("====================================================\n");
        comprobante.append("           COMPROBANTE DE PAGO DE ENCUESTAS\n");
        comprobante.append("====================================================\n\n");
        comprobante.append("ID COMPROBANTE: ").append(idComprobante).append("\n");
        comprobante.append("FECHA DE EMISION: ").append(fechaActual).append("\n\n");

        comprobante.append("PARTICIPANTE:\n");
        comprobante.append("DNI: ").append(dni).append("\n");
        comprobante.append("NOMBRE: ").append(nombreParticipante).append("\n\n");

        comprobante.append("DETALLE DE PAGOS:\n");
        comprobante.append("----------------------------------------------------\n");

        // Mostrar encuestas completadas
        double montoPorEncuesta = 5.00;
        for (int i = 1; i <= cantidadEncuestas; i++) {
            comprobante.append(
                    String.format("Encuesta #%03d          %s          S/. %.2f\n", i, fechaActual, montoPorEncuesta));
        }

        comprobante.append("----------------------------------------------------\n");
        comprobante.append(String.format("TOTAL A PAGAR:                             S/. %.2f\n", totalPago));
        comprobante.append("====================================================\n\n");
        comprobante.append("INFORMACION DE PAGO POR GIRO:\n");
        comprobante.append("----------------------------------------------------\n");
        comprobante.append("PAGO POR GIRO EN 24 HORAS\n");
        comprobante.append("Codigo de Giro: ").append(codigoGiro).append("\n");
        comprobante.append("Banco: BANCO DE LA NACION\n");
        comprobante.append("Plazo: Maximo 24 horas habiles\n");
        comprobante.append("Requisitos: Llevar DNI fisico vigente\n");
        comprobante.append("====================================================\n\n");
        comprobante.append("Comprobante generado automaticamente por SIGEUTP\n");
        comprobante.append("valido como justificante de pago\n");

        txtAreaComprobante.setText(comprobante.toString());
    }

    private void exportarComprobante() {
        String contenido = txtAreaComprobante.getText();

        if (contenido.isEmpty()) {
            lblMensajeBusqueda.setText("Error: No hay comprobante para exportar");
            lblMensajeBusqueda.setForeground(Color.RED);
            return;
        }

        try {
            String nombreArchivo = "comprobante_" + txtDNI.getText() + "_" + System.currentTimeMillis() + ".txt";
            String rutaArchivo = System.getProperty("user.home") + "\\Desktop\\" + nombreArchivo;

            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write(contenido);
            writer.close();

            lblMensajeBusqueda.setText("Comprobante exportado a: " + nombreArchivo);
            lblMensajeBusqueda.setForeground(new Color(0, 150, 0));
        } catch (IOException e) {
            lblMensajeBusqueda.setText("Error al exportar: " + e.getMessage());
            lblMensajeBusqueda.setForeground(Color.RED);
        }
    }
}
