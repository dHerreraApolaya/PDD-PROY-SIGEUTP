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
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PanelEliminarUsuarioExterno extends JPanel {
    private JTextField txtBuscarDNI;
    private JTextArea txtDatosUsuario;
    private JLabel lblMensaje;

    public PanelEliminarUsuarioExterno(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(200, 100, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("ELIMINAR USUARIO EXTERNO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Panel de busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelBusqueda.setBackground(new Color(30, 60, 100));

        JLabel lblBuscar = new JLabel("Ingrese DNI del usuario externo para buscar:");
        lblBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBuscar.setForeground(Color.WHITE);
        panelBusqueda.add(lblBuscar);

        txtBuscarDNI = new JTextField(12);
        txtBuscarDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        panelBusqueda.add(txtBuscarDNI);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        panelBusqueda.add(btnBuscar);

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // TextArea con datos del usuario
        JLabel lblDatos = new JLabel("DATOS Y PERMISOS DEL USUARIO:");
        lblDatos.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDatos.setForeground(Color.WHITE);
        JPanel panelTituloDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTituloDatos.setBackground(new Color(30, 60, 100));
        panelTituloDatos.add(lblDatos);

        txtDatosUsuario = new JTextArea(12, 80);
        txtDatosUsuario.setFont(new Font("Arial", Font.PLAIN, 11));
        txtDatosUsuario.setEditable(false);
        txtDatosUsuario.setLineWrap(true);
        txtDatosUsuario.setWrapStyleWord(true);
        txtDatosUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel panelDatos = new JPanel(new BorderLayout());
        panelDatos.setBackground(new Color(30, 60, 100));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelDatos.add(panelTituloDatos, BorderLayout.NORTH);
        panelDatos.add(new JScrollPane(txtDatosUsuario), BorderLayout.CENTER);

        panelCentral.add(panelDatos, BorderLayout.CENTER);

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMensaje.setBackground(new Color(30, 60, 100));
        panelMensaje.add(lblMensaje);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(30, 60, 100));

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(new Dimension(140, 45));
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(34, 139, 34));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
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

        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);
        panelBotones.add(btnSalir);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.add(panelMensaje, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        panelInferior.setPreferredSize(new Dimension(0, 100));

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void buscarUsuario() {
        String dni = txtBuscarDNI.getText().trim();
        if (dni.isEmpty()) {
            lblMensaje.setText("Ingrese un DNI valido");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Cargar datos desde BD
        StringBuilder datos = new StringBuilder();
        datos.append("DNI: ").append(dni).append("\n");
        datos.append("Nombre: Carlos Lopez\n");
        datos.append("Edad: 32\n");
        datos.append("Usuario: carlos_lopez\n");
        datos.append("Email: carlos@example.com\n\n");
        datos.append("PERMISOS ASIGNADOS:\n");
        datos.append("Ver Encuestas\n");
        datos.append("Crear Encuestas\n");
        datos.append("Ver Resultados\n");
        datos.append("Exportar Resultados\n");

        txtDatosUsuario.setText(datos.toString());
        lblMensaje.setText("Usuario encontrado");
        lblMensaje.setForeground(new Color(0, 150, 0));
    }

    private void eliminarUsuario() {
        if (txtBuscarDNI.getText().isEmpty()) {
            lblMensaje.setText("Busca un usuario primero");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        if (txtDatosUsuario.getText().isEmpty()) {
            lblMensaje.setText("El usuario no existe");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Eliminar de BD
        lblMensaje.setText("Usuario externo eliminado exitosamente");
        lblMensaje.setForeground(new Color(0, 150, 0));
        txtDatosUsuario.setText("");
        txtBuscarDNI.setText("");
    }
}





