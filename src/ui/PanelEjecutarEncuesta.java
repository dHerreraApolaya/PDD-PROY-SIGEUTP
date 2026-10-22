package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import modulo.encuestas.configuracion.Encuesta;
import modulo.encuestas.configuracion.EncuestaDAO;
import modulo.participantes.Participante;

public class PanelEjecutarEncuesta extends JPanel {
    private JTable tablaEncuestas;
    private JButton btnFinalizarPago;
    private JButton btnVolver;
    private JButton btnCerrar;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;
    private int idEncuestaSeleccionada = -1;
    private Participante participanteActual;
    private List<Encuesta> encuestasDisponibles = new ArrayList<>();
    private PanelRegistrarRespuestas panelRegistrarRespuestas;
    private JLabel lblMensaje;

    public PanelEjecutarEncuesta(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 60, 100));

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 60, 100));
        panelSuperior.setPreferredSize(new Dimension(0, 100));

        JLabel lblTitulo = new JLabel("EJECUTAR ENCUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(30, 60, 100));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Label informativo
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(30, 60, 100));
        lblMensaje = new JLabel("Primero identifique al participante en Registro de Participante.");
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        lblMensaje.setForeground(Color.WHITE);
        panelInfo.add(lblMensaje);
        panelCentral.add(panelInfo, BorderLayout.NORTH);

        // Tabla de encuestas disponibles
        String[] columnas = { "ID Encuesta", "Nombre Encuesta", "Descripcion", "Tiempo (min)", "Accion" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo columna de boton editable
            }
        };

        tablaEncuestas = new JTable(modeloTabla);
        tablaEncuestas.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaEncuestas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEncuestas.setRowHeight(35);
        tablaEncuestas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // Renderer y editor para botones
        tablaEncuestas.getColumn("Accion").setCellRenderer(new BotonTableRenderer());
        tablaEncuestas.getColumn("Accion").setCellEditor(new BotonTableEditor(this));

        JScrollPane scrollPane = new JScrollPane(tablaEncuestas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(30, 60, 100));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnFinalizarPago = new JButton("Finalizar e Ir a Pago");
        btnFinalizarPago.setPreferredSize(new Dimension(160, 40));
        btnFinalizarPago.setFont(new Font("Arial", Font.PLAIN, 12));
        btnFinalizarPago.setBackground(new Color(34, 139, 34));
        btnFinalizarPago.setForeground(Color.WHITE);
        btnFinalizarPago.setOpaque(true);
        btnFinalizarPago.setBorderPainted(false);
        btnFinalizarPago.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "PagoPorParticipante");
            }
        });

        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(60, 150, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelPrincipal, "RegistroParticipante");
            }
        });

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(140, 45));
        btnCerrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrar.setBackground(new Color(34, 139, 34));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelInferior.add(btnFinalizarPago);
        panelInferior.add(btnVolver);
        panelInferior.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public void establecerPanelRespuestas(PanelRegistrarRespuestas panelRegistrarRespuestas) {
        this.panelRegistrarRespuestas = panelRegistrarRespuestas;
    }

    public boolean cargarEncuestas(Participante participante) {
        participanteActual = null;
        idEncuestaSeleccionada = -1;
        modeloTabla.setRowCount(0);
        encuestasDisponibles.clear();
        if (participante == null || participante.getId_participante() <= 0) {
            mostrarError("Debe seleccionar un participante registrado en la base de datos.");
            return false;
        }
        try {
            encuestasDisponibles = consultarEncuestasDisponibles();
            participanteActual = participante;
            for (Encuesta encuesta : encuestasDisponibles) {
                modeloTabla.addRow(new Object[] { encuesta.getIdEncuesta(), encuesta.getTitulo(),
                        encuesta.getDescripcion(), 15, "Ejecutar" });
            }
            lblMensaje.setText("Participante: " + participante.getNombre() + " | Encuestas disponibles: "
                    + encuestasDisponibles.size());
            lblMensaje.setForeground(Color.WHITE);
            return true;
        } catch (SQLException excepcion) {
            mostrarError("No se pudieron cargar las encuestas: " + excepcion.getMessage());
            return false;
        }
    }

    protected List<Encuesta> consultarEncuestasDisponibles() throws SQLException {
        return EncuestaDAO.consultarEncuestasDisponibles();
    }

    public void ejecutarEncuesta(int fila) {
        if (participanteActual == null) {
            mostrarError("Primero identifique un participante registrado antes de ejecutar una encuesta.");
            return;
        }
        if (fila < 0 || fila >= tablaEncuestas.getRowCount()) {
            mostrarError("Seleccione una encuesta disponible.");
            return;
        }
        if (panelRegistrarRespuestas == null) {
            mostrarError("No se encontro el formulario de respuestas.");
            return;
        }
        Encuesta encuestaSeleccionada = encuestasDisponibles.get(tablaEncuestas.convertRowIndexToModel(fila));
        idEncuestaSeleccionada = -1;
        if (panelRegistrarRespuestas.prepararEncuesta(participanteActual, encuestaSeleccionada)) {
            idEncuestaSeleccionada = encuestaSeleccionada.getIdEncuesta();
            cardLayout.show(panelPrincipal, "RegistrarRespuestas");
        } else {
            mostrarError(panelRegistrarRespuestas.obtenerMensaje());
        }
    }

    public void mostrarResultado(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(Color.WHITE);
    }

    public String obtenerMensaje() {
        return lblMensaje.getText();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(Color.YELLOW);
    }

    public int getIdEncuestaSeleccionada() {
        return idEncuestaSeleccionada;
    }

    // Renderer para mostrar botones en la tabla
    private static class BotonTableRenderer extends JButton implements TableCellRenderer {
        public BotonTableRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(34, 139, 34));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 11));
            return this;
        }
    }

    // Editor para manejar clicks en botones de la tabla
    private static class BotonTableEditor extends javax.swing.DefaultCellEditor {
        private JButton boton;
        private PanelEjecutarEncuesta panel;
        private int filaActual = -1;

        public BotonTableEditor(PanelEjecutarEncuesta panel) {
            super(new javax.swing.JCheckBox());
            this.panel = panel;
            this.boton = new JButton("Ejecutar");
            boton.setBackground(new Color(34, 139, 34));
            boton.setForeground(Color.WHITE);
            boton.setOpaque(true);
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    panel.ejecutarEncuesta(filaActual);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            boton.setText((value == null) ? "" : value.toString());
            this.filaActual = row;
            return boton;
        }
    }
}
