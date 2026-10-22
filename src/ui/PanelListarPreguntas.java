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
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import conexionSQL.ConexionSQL;

public class PanelListarPreguntas extends JPanel {
    private JComboBox<String> cbxIdEncuesta;
    private JTable tablaPreguntas;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;

    public PanelListarPreguntas(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("LISTA DE PREGUNTAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel de seleccion de encuesta
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setBackground(new Color(30, 60, 100));
        panelSeleccion.setLayout(new GridBagLayout());
        panelSeleccion.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblIdEncuesta = new JLabel("ID de la Encuesta:");
        lblIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdEncuesta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSeleccion.add(lblIdEncuesta, gbc);

        cbxIdEncuesta = new JComboBox<>();
        cbxIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        cbxIdEncuesta.setPreferredSize(new Dimension(100, 30));
        cargarEncuestasEnCombo();
        cbxIdEncuesta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cargarPreguntasEncuesta();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelSeleccion.add(cbxIdEncuesta, gbc);

        // Panel central con tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabla de preguntas
        String[] columnas = { "ID", "Pregunta", "Tipo", "Categoria", "Filtro" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPreguntas = new JTable(modeloTabla);
        tablaPreguntas.setEnabled(false);
        tablaPreguntas.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaPreguntas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaPreguntas.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaPreguntas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelCentral.add(panelSeleccion, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

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
                cardLayout.show(panelPrincipal, "BancoPreguntas");
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

                while (rs.next()) {
                    cbxIdEncuesta.addItem(rs.getInt("id_encuesta") + " - " + rs.getString("titulo"));
                }

                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar encuestas en combo: " + e.getMessage());
        }
    }

    private void cargarPreguntasEncuesta() {
        try {
            modeloTabla.setRowCount(0);
            String selectedItem = (String) cbxIdEncuesta.getSelectedItem();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                String idEncuestaStr = selectedItem.split(" - ")[0];
                int idEncuesta = Integer.parseInt(idEncuestaStr);

                Connection conn = ConexionSQL.getConnection();
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT id_pregunta, texto, tipo, categoria, es_pregunta_filtro FROM Preguntas WHERE id_encuesta = "
                            + idEncuesta;
                    ResultSet rs = stmt.executeQuery(sql);

                    while (rs.next()) {
                        Object[] fila = {
                                rs.getInt("id_pregunta"),
                                rs.getString("texto"),
                                rs.getString("tipo"),
                                rs.getString("categoria"),
                                rs.getBoolean("es_pregunta_filtro") ? "Si" : "No"
                        };
                        modeloTabla.addRow(fila);
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar preguntas de la encuesta: " + e.getMessage());
        }
    }
}


