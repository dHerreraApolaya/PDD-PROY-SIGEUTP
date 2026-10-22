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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PanelListarParticipantes extends JPanel {
    private JTable tablaParticipantes;
    private JTable tablaParticipantesPorEncuesta;
    private JComboBox<String> cmbEncuesta;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelListarParticipantes(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("LISTA DE PARTICIPANTES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central dividido en 2 secciones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // ========== Seccion 1: TODOS LOS PARTICIPANTES ==========
        JLabel lblTodosParticipantes = new JLabel("TODOS LOS PARTICIPANTES:");
        lblTodosParticipantes.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTodosParticipantes.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        panelCentral.add(lblTodosParticipantes, gbc);

        // Tabla de todos los participantes
        String[] columnasParticipantes = { "ID", "Nombre", "DNI", "Email", "Sexo", "Edad", "Nivel Educativo" };
        Object[][] datosParticipantes = {}; // TODO: Cargar desde BD
        tablaParticipantes = new JTable(datosParticipantes, columnasParticipantes);
        tablaParticipantes.setEnabled(false);
        tablaParticipantes.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaParticipantes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaParticipantes.setRowHeight(25);
        JScrollPane scrollPane1 = new JScrollPane(tablaParticipantes);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.5;
        panelCentral.add(scrollPane1, gbc);

        // Separador
        JLabel lblSeparador = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;
        panelCentral.add(lblSeparador, gbc);

        // ========== Seccion 2: PARTICIPANTES POR ENCUESTA ==========
        JLabel lblParticipantesPorEncuesta = new JLabel("PARTICIPANTES POR ENCUESTA:");
        lblParticipantesPorEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblParticipantesPorEncuesta.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0;
        panelCentral.add(lblParticipantesPorEncuesta, gbc);

        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltro.setBackground(new Color(30, 60, 100));

        JLabel lblFiltro = new JLabel("Seleccionar Encuesta:");
        lblFiltro.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFiltro.setForeground(Color.WHITE);
        panelFiltro.add(lblFiltro);

        cmbEncuesta = new JComboBox<String>();
        cmbEncuesta.setPreferredSize(new Dimension(250, 30));
        cmbEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbEncuesta.addItem("-- Seleccione una encuesta --");
        // TODO: Cargar IDs de encuestas desde BD
        cmbEncuesta.addItem("Encuesta #001");
        cmbEncuesta.addItem("Encuesta #002");
        cmbEncuesta.addItem("Encuesta #003");
        cmbEncuesta.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Cargar datos cuando el combo se abre
                cargarParticipantesPorEncuesta();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                cargarParticipantesPorEncuesta();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        panelFiltro.add(cmbEncuesta);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 0;
        panelCentral.add(panelFiltro, gbc);

        // Tabla de participantes por encuesta
        String[] columnasParticipantesPorEncuesta = { "ID", "Nombre", "DNI", "Email", "Sexo", "Edad", "Estado" };
        Object[][] datosParticipantesPorEncuesta = {}; // TODO: Cargar desde BD
        tablaParticipantesPorEncuesta = new JTable(datosParticipantesPorEncuesta,
                columnasParticipantesPorEncuesta);
        tablaParticipantesPorEncuesta.setEnabled(false);
        tablaParticipantesPorEncuesta.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaParticipantesPorEncuesta.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaParticipantesPorEncuesta.setRowHeight(25);
        JScrollPane scrollPane2 = new JScrollPane(tablaParticipantesPorEncuesta);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 0.5;
        panelCentral.add(scrollPane2, gbc);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(34, 139, 34));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "GestionParticipante");
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

        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarParticipantesPorEncuesta() {
        String encuestaSeleccionada = (String) cmbEncuesta.getSelectedItem();

        // TODO: Cargar participantes de la encuesta seleccionada desde BD
        // Por ahora es solo un placeholder
        if (encuestaSeleccionada != null && !encuestaSeleccionada.equals("-- Seleccione una encuesta --")) {
            // Logica para cargar participantes de la encuesta seleccionada
            // Actualizar tablaParticipantesPorEncuesta con datos filtrados
        }
    }
}




