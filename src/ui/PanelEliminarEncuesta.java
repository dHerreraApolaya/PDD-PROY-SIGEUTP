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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelEliminarEncuesta extends JPanel {
    private JComboBox<String> cbxIdEncuesta;
    private JTextArea txtDetalles;
    private JButton btnEliminar;
    private JButton btnVolver;
    private JButton btnCerrar;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public PanelEliminarEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("ELIMINAR ENCUESTA EXISTENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridBagLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Label para seleccionar encuesta
        JLabel lblSeleccionar = new JLabel("Seleccionar Encuesta (ID):");
        lblSeleccionar.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSeleccionar.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelCentral.add(lblSeleccionar, gbc);

        // ComboBox con IDs de encuestas
        cbxIdEncuesta = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" }); // Placeholder
        cbxIdEncuesta.setFont(new Font("Arial", Font.PLAIN, 12));
        cbxIdEncuesta.setPreferredSize(new Dimension(200, 30));
        cbxIdEncuesta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cargarDetallesEncuesta();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = fila;
        panelCentral.add(cbxIdEncuesta, gbc);
        fila++;

        // Separador
        fila++;

        // TextArea para mostrar detalles
        JLabel lblDetalles = new JLabel("Detalles de la Encuesta:");
        lblDetalles.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDetalles.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(lblDetalles, gbc);
        gbc.gridwidth = 1;
        fila++;

        txtDetalles = new JTextArea(6, 40);
        txtDetalles.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDetalles.setLineWrap(true);
        txtDetalles.setWrapStyleWord(true);
        txtDetalles.setEditable(false);
        txtDetalles.setBackground(new Color(220, 220, 220));
        JScrollPane scrollDetalles = new JScrollPane(txtDetalles);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelCentral.add(scrollDetalles, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Separador
        fila++;

        // Label de mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentral.add(lblMensaje, gbc);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(new Dimension(140, 45));
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEncuesta();
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
                limpiarFormulario();
                cardLayout.show(panelPrincipal, "ConfiguracionEncuesta");
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

        panelInferior.add(btnEliminar);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDetallesEncuesta() {
        String idSeleccionado = (String) cbxIdEncuesta.getSelectedItem();
        // TODO: Cargar detalles de la encuesta desde la base de datos
        txtDetalles.setText("ID: " + idSeleccionado + "\n\n"
                + "Titulo: Encuesta Ejemplo\n"
                + "Descripcion: Esta es una encuesta de ejemplo para demostracion\n"
                + "Estado: Activa");
        lblMensaje.setText("");
    }

    private void eliminarEncuesta() {
        String idSeleccionado = (String) cbxIdEncuesta.getSelectedItem();

        if (idSeleccionado == null || idSeleccionado.isEmpty()) {
            lblMensaje.setText("Error: Debe seleccionar una encuesta para eliminar");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Eliminar de la base de datos
        lblMensaje.setText("Encuesta ID " + idSeleccionado + " eliminada exitosamente");
        lblMensaje.setForeground(new Color(0, 150, 0));

        txtDetalles.setText("");
    }

    private void limpiarFormulario() {
        cbxIdEncuesta.setSelectedIndex(0);
        txtDetalles.setText("");
        lblMensaje.setText("");
    }
}





