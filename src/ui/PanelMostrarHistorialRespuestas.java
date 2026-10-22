package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import conexionSQL.ConexionSQL;

public class PanelMostrarHistorialRespuestas extends JPanel {
    private JComboBox<String> cmbEncuestas;
    private JTable tablaRespuestas;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;

    public PanelMostrarHistorialRespuestas(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("VER RESPUESTAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelFiltro.setBackground(new Color(30, 60, 100));

        JLabel lblIDEncuesta = new JLabel("ID de Encuesta:");
        lblIDEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIDEncuesta.setForeground(Color.WHITE);
        panelFiltro.add(lblIDEncuesta);

        cmbEncuestas = new JComboBox<>();
        cmbEncuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbEncuestas.setPreferredSize(new Dimension(300, 30));
        cargarEncuestasEnCombo();
        cmbEncuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarRespuestas();
            }
        });
        panelFiltro.add(cmbEncuestas);

        panelCentral.add(panelFiltro, BorderLayout.NORTH);

        // Tabla de respuestas
        String[] columnas = { "Pregunta No.", "Texto de Pregunta", "Respuesta" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaRespuestas = new JTable(modeloTabla);
        tablaRespuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaRespuestas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaRespuestas.setRowHeight(25);
        tablaRespuestas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaRespuestas.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaRespuestas.getColumnModel().getColumn(2).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tablaRespuestas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(60, 150, 60));
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
        btnCerrar.setBackground(new Color(34, 139, 34));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarEncuestasEnCombo() {
        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT id_encuesta, titulo FROM Encuestas";
                ResultSet rs = stmt.executeQuery(sql);

                cmbEncuestas.addItem("-- Seleccione una encuesta --");
                while (rs.next()) {
                    cmbEncuestas.addItem(rs.getInt("id_encuesta") + " - " + rs.getString("titulo"));
                }

                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar encuestas en combo: " + e.getMessage());
            cmbEncuestas.addItem("-- Seleccione una encuesta --");
        }
    }

    private void cargarRespuestas() {
        try {
            modeloTabla.setRowCount(0);
            String selectedItem = (String) cmbEncuestas.getSelectedItem();
            if (selectedItem != null && !selectedItem.equals("-- Seleccione una encuesta --")) {
                String[] parts = selectedItem.split(" - ");
                if (parts.length >= 1) {
                    int idEncuesta = Integer.parseInt(parts[0]);

                    Connection conn = ConexionSQL.getConnection();
                    if (conn != null) {
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT r.id_pregunta, p.texto, r.respuesta FROM Respuestas r " +
                                "JOIN Preguntas p ON r.id_pregunta = p.id_pregunta " +
                                "WHERE p.id_encuesta = " + idEncuesta;
                        ResultSet rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            Object[] fila = {
                                    rs.getInt("id_pregunta"),
                                    rs.getString("texto"),
                                    rs.getString("respuesta")
                            };
                            modeloTabla.addRow(fila);
                        }

                        rs.close();
                        stmt.close();
                        conn.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar respuestas: " + e.getMessage());
        }
    }
}


