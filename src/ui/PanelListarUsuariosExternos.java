package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelListarUsuariosExternos extends JPanel {
    private JTable tablaUsuarios;

    public PanelListarUsuariosExternos(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(100, 140, 180));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("LISTAR USUARIOS EXTERNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabla de usuarios
        String[] columnas = { "DNI", "Nombre", "Edad", "Usuario", "Email", "Permisos" };
        Object[][] datos = {
                { "12345678", "Carlos Lopez", "32", "carlos_lopez", "carlos@example.com",
                        "Ver Encuestas, Crear Encuestas" },
                { "87654321", "Maria Garcia", "28", "maria_garcia", "maria@example.com",
                        "Ver Preguntas, Editar Preguntas" },
                { "11111111", "Juan Rodriguez", "45", "juan_rodri", "juan@example.com",
                        "Ver Participantes, Ver Resultados" }
        };

        tablaUsuarios = new JTable(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 90));

        JButton btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "GestionPerfiles");
            }
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(140, 45));
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.setBackground(new Color(34, 139, 34));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelInferior.add(btnVolver);
        panelInferior.add(btnSalir);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}



