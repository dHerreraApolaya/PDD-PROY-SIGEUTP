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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import conexionSQL.ConexionSQL;

public class PanelListarEncuesta extends JPanel {
    private JTable tablaEncuestas;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;

    public PanelListarEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("LISTA DE ENCUESTAS REGISTRADAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central con tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabla de encuestas con las columnas solicitadas
        String[] columnas = { "ID", "Titulo", "Descripcion", "Estado", "Fecha Creacion" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEncuestas = new JTable(modeloTabla);
        tablaEncuestas.setEnabled(false);
        tablaEncuestas.setFont(new Font("Arial", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(tablaEncuestas);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Cargar encuestas desde BD
        cargarEncuestasDesdeBD();

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 90));
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
                cardLayout.show(panelPrincipal, "ConfiguracionEncuesta");
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

    private void cargarEncuestasDesdeBD() {
        try {
            Connection conn = ConexionSQL.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT id_encuesta, titulo, descripcion, estado, fecha_creacion FROM Encuestas";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Object[] fila = {
                            rs.getInt("id_encuesta"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion")
                    };
                    modeloTabla.addRow(fila);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar encuestas desde BD: " + e.getMessage());
        }
    }
}
