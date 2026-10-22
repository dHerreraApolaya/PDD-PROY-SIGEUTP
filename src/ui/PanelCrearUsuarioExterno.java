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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PanelCrearUsuarioExterno extends JPanel {
    private JTextField txtNombre;
    private JTextField txtDNI;
    private JTextField txtEdad;
    private JTextField txtUsuario;
    private JTextField txtContrasena;
    private JComboBox<String> cmbPermisos;
    private JTextArea txtPermisosAsignados;
    private JTextArea txtResumenUsuario;
    private JLabel lblMensaje;

    public PanelCrearUsuarioExterno(CardLayout cardLayout, JPanel panelPrincipal) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(100, 150, 200));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("CREAR USUARIO EXTERNO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int fila = 0;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombre.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panelFormulario.add(lblNombre, gbc);

        txtNombre = new JTextField(15);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(txtNombre, gbc);
        fila++;

        // DNI
        JLabel lblDNI = new JLabel("DNI:");
        lblDNI.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDNI.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panelFormulario.add(lblDNI, gbc);

        txtDNI = new JTextField(15);
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(txtDNI, gbc);
        fila++;

        // Edad
        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEdad.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panelFormulario.add(lblEdad, gbc);

        txtEdad = new JTextField(15);
        txtEdad.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(txtEdad, gbc);
        fila++;

        // Nombre de Usuario
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panelFormulario.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(txtUsuario, gbc);
        fila++;

        // Contrasena
        JLabel lblContrasena = new JLabel("Contrasena:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContrasena.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panelFormulario.add(lblContrasena, gbc);

        txtContrasena = new JTextField(15);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(txtContrasena, gbc);
        fila++;

        // Separador
        fila++;

        // Asignar Permisos
        JLabel lblAsignarPermisos = new JLabel("ASIGNAR PERMISOS:");
        lblAsignarPermisos.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAsignarPermisos.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelFormulario.add(lblAsignarPermisos, gbc);
        fila++;

        // ComboBox Permisos
        JLabel lblPermiso = new JLabel("Seleccione Permiso:");
        lblPermiso.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPermiso.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panelFormulario.add(lblPermiso, gbc);

        cmbPermisos = new JComboBox<String>();
        cmbPermisos.addItem("-- Seleccione un permiso --");
        cmbPermisos.addItem("Ver Encuestas");
        cmbPermisos.addItem("Crear Encuestas");
        cmbPermisos.addItem("Editar Encuestas");
        cmbPermisos.addItem("Eliminar Encuestas");
        cmbPermisos.addItem("Ver Preguntas");
        cmbPermisos.addItem("Crear Preguntas");
        cmbPermisos.addItem("Editar Preguntas");
        cmbPermisos.addItem("Eliminar Preguntas");
        cmbPermisos.addItem("Ver Participantes");
        cmbPermisos.addItem("Crear Participantes");
        cmbPermisos.addItem("Editar Participantes");
        cmbPermisos.addItem("Eliminar Participantes");
        cmbPermisos.addItem("Ver Resultados");
        cmbPermisos.addItem("Exportar Resultados");
        cmbPermisos.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelFormulario.add(cmbPermisos, gbc);

        fila++;

        // Boton Agregar Permiso
        JButton btnAgregarPermiso = new JButton("Agregar Permiso");
        btnAgregarPermiso.setFont(new Font("Arial", Font.PLAIN, 11));
        btnAgregarPermiso.setPreferredSize(new Dimension(130, 25));
        gbc.gridy = fila;
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(btnAgregarPermiso, gbc);
        fila++;

        // TextArea Permisos Asignados
        JLabel lblPermisosAsignados = new JLabel("Permisos Asignados:");
        lblPermisosAsignados.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPermisosAsignados.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelFormulario.add(lblPermisosAsignados, gbc);
        fila++;

        txtPermisosAsignados = new JTextArea(4, 50);
        txtPermisosAsignados.setFont(new Font("Arial", Font.PLAIN, 10));
        txtPermisosAsignados.setEditable(false);
        txtPermisosAsignados.setLineWrap(true);
        txtPermisosAsignados.setWrapStyleWord(true);
        txtPermisosAsignados.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        panelFormulario.add(new JScrollPane(txtPermisosAsignados), gbc);
        fila++;

        // Separador
        fila++;

        // TextArea Resumen
        JLabel lblResumen = new JLabel("RESUMEN DE DATOS Y PERMISOS:");
        lblResumen.setFont(new Font("Arial", Font.PLAIN, 12));
        lblResumen.setForeground(Color.WHITE);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panelFormulario.add(lblResumen, gbc);
        fila++;

        txtResumenUsuario = new JTextArea(6, 50);
        txtResumenUsuario.setFont(new Font("Arial", Font.PLAIN, 10));
        txtResumenUsuario.setEditable(false);
        txtResumenUsuario.setLineWrap(true);
        txtResumenUsuario.setWrapStyleWord(true);
        txtResumenUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.3;
        panelFormulario.add(new JScrollPane(txtResumenUsuario), gbc);

        // Agregar action listener para Boton agregar permiso
        btnAgregarPermiso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPermiso();
            }
        });

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        panelCentral.add(scrollFormulario, BorderLayout.CENTER);

        // Mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.WHITE);
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMensaje.setBackground(new Color(30, 60, 100));
        panelMensaje.add(lblMensaje);
        panelCentral.add(panelMensaje, BorderLayout.SOUTH);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setPreferredSize(new Dimension(0, 90));

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setPreferredSize(new Dimension(160, 45));
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarUsuario();
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

        panelInferior.add(btnGuardar);
        panelInferior.add(btnVolver);
        panelInferior.add(btnSalir);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void agregarPermiso() {
        if (cmbPermisos.getSelectedIndex() > 0) {
            String permiso = (String) cmbPermisos.getSelectedItem();
            String contenido = txtPermisosAsignados.getText();
            if (contenido.isEmpty()) {
                txtPermisosAsignados.setText("ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ " + permiso);
            } else {
                txtPermisosAsignados.setText(contenido + "\nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ " + permiso);
            }
            actualizarResumen();
            lblMensaje.setText("ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã¢â‚¬Â¦ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã¢â‚¬Å“ Permiso agregado");
            lblMensaje.setForeground(new Color(0, 150, 0));
        }
    }

    private void actualizarResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("DATOS DEL USUARIO:\n");
        resumen.append("Nombre: ").append(txtNombre.getText()).append("\n");
        resumen.append("DNI: ").append(txtDNI.getText()).append("\n");
        resumen.append("Edad: ").append(txtEdad.getText()).append("\n");
        resumen.append("Usuario: ").append(txtUsuario.getText()).append("\n");
        resumen.append("Contrasena: ").append(txtContrasena.getText()).append("\n\n");
        resumen.append("PERMISOS ASIGNADOS:\n");
        resumen.append(txtPermisosAsignados.getText());

        txtResumenUsuario.setText(resumen.toString());
    }

    private void guardarUsuario() {
        if (txtNombre.getText().isEmpty() || txtDNI.getText().isEmpty() || txtEdad.getText().isEmpty() ||
                txtUsuario.getText().isEmpty() || txtContrasena.getText().isEmpty()) {
            lblMensaje.setText("Completa todos los campos");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        if (txtPermisosAsignados.getText().isEmpty()) {
            lblMensaje.setText("Asigna al menos un permiso");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // TODO: Guardar en BD
        lblMensaje.setText("Usuario externo creado exitosamente");
        lblMensaje.setForeground(new Color(0, 150, 0));
    }
}





