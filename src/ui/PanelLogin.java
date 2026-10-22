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
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

public class PanelLogin extends JPanel {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnVolver;
    private JLabel lblMensaje;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    // Credenciales predeterminadas del administrador
    private static final String USUARIO_ADMIN = "admin";
    private static final String CONTRASENA_ADMIN = "123";

    // Base de datos de usuarios (simulada)
    // En produccion, se cargaria desde BD
    private Map<String, String> usuariosRegistrados;

    public PanelLogin(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        // Inicializar base de datos de usuarios
        usuariosRegistrados = new HashMap<>();
        cargarUsuariosExternos();

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTituloLogin = new JLabel("LOGIN");
        lblTituloLogin.setFont(new Font("Arial", Font.BOLD, 32));
        lblTituloLogin.setForeground(Color.WHITE);
        panelSuperior.add(lblTituloLogin);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(new Color(30, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Label Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblUsuario, gbc);

        // TextField Usuario
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelFormulario.add(txtUsuario, gbc);

        // Label Contrasena
        JLabel lblContrasena = new JLabel("Contrasena:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContrasena.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblContrasena, gbc);

        // PasswordField Contrasena
        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContrasena.setBackground(Color.WHITE);
        txtContrasena.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelFormulario.add(txtContrasena, gbc);

        // Label de mensaje
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(lblMensaje, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(30, 60, 100));

        btnLogin = new JButton("Ingresar");
        btnLogin.setPreferredSize(new Dimension(140, 45));
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLogin.setBackground(new Color(34, 139, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
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
                cardLayout.show(panelPrincipal, "Home");
            }
        });

        panelBotones.add(btnLogin);
        panelBotones.add(btnVolver);

        // Panel contenedor para centralizar
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBackground(new Color(30, 60, 100));
        panelContenedor.add(Box.createVerticalGlue());
        panelContenedor.add(panelFormulario);
        panelContenedor.add(panelBotones);
        panelContenedor.add(Box.createVerticalGlue());

        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor complete todos los campos");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        // Validar credenciales del administrador
        if (usuario.equals(USUARIO_ADMIN) && contrasena.equals(CONTRASENA_ADMIN)) {
            lblMensaje.setText("LOGIN ADMIN EXITOSO");
            lblMensaje.setForeground(new Color(0, 150, 0));

            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    limpiarFormulario();
                    cardLayout.show(panelPrincipal, "MenuPrincipal");
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        // Validar credenciales de usuarios externos
        else if (usuariosRegistrados.containsKey(usuario) &&
                usuariosRegistrados.get(usuario).equals(contrasena)) {
            lblMensaje.setText("LOGIN USUARIO EXTERNO EXITOSO");
            lblMensaje.setForeground(new Color(0, 150, 0));

            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    limpiarFormulario();
                    cardLayout.show(panelPrincipal, "MenuPrincipal");
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMensaje.setText("Usuario o contrasena incorrectos");
            lblMensaje.setForeground(Color.RED);
            txtContrasena.setText("");
        }
    }

    private void cargarUsuariosExternos() {
        // Primero agregar usuarios de ejemplo
        usuariosRegistrados.put("carlos_lopez", "pass123");
        usuariosRegistrados.put("maria_garcia", "pass456");
        usuariosRegistrados.put("juan_rodri", "pass789");

        // Nota: UsuariosExternos no tiene columna de contraseña en BD
        // Se utilizan credenciales admin hardcodeadas para el acceso
        // Los usuarios externos se validan por nombre_usuario en BD pero sin contraseña
    }

    private void limpiarFormulario() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        lblMensaje.setText("");
    }
}
