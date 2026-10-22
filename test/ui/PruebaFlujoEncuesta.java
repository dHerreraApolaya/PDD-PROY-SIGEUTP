package ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import modulo.encuestas.configuracion.Encuesta;
import modulo.participantes.Participante;
import modulo.preguntas.Pregunta;
import modulo.preguntas.TipoPregunta;
import modulo.respuestas.Respuesta;
import modulo.respuestas.RespuestaDAO;

public class PruebaFlujoEncuesta {
    private static int comprobaciones;

    private static class SeleccionDePrueba extends PanelEjecutarEncuesta {
        private boolean fallarConsulta;
        private int consultas;

        SeleccionDePrueba(CardLayout distribucion, JPanel principal) {
            super(distribucion, principal);
        }

        @Override
        protected List<Encuesta> consultarEncuestasDisponibles() throws SQLException {
            consultas++;
            if (fallarConsulta) {
                throw new SQLException("Conexion de prueba no disponible");
            }
            List<Encuesta> encuestas = new ArrayList<>();
            encuestas.add(new Encuesta(11, "Primera encuesta", "Cuatro tipos de pregunta", "ACTIVA", 1));
            encuestas.add(new Encuesta(28, "Segunda encuesta", "Pregunta independiente", "ACTIVA", 1));
            return encuestas;
        }
    }

    private static class RegistroDePrueba extends PanelRegistroParticipante {
        private Participante participante;
        private boolean fallarConsulta;

        RegistroDePrueba(CardLayout distribucion, JPanel principal, Participante participante) {
            super(distribucion, principal);
            this.participante = participante;
        }

        @Override
        protected Participante consultarParticipante(String dni) throws SQLException {
            if (fallarConsulta) {
                throw new SQLException("Conexion de prueba no disponible");
            }
            return participante.getDni().equals(dni) ? participante : null;
        }
    }

    private static class RespuestasDePrueba extends PanelRegistrarRespuestas {
        private boolean fallarCarga;
        private boolean sinPreguntas;
        private boolean preguntaAjena;
        private boolean fallarGuardado;
        private int cargas;
        private int intentosGuardado;
        private int ultimoIdEncuesta;
        private List<Respuesta> guardadas = new ArrayList<>();

        RespuestasDePrueba(CardLayout distribucion, JPanel principal, PanelEjecutarEncuesta seleccion) {
            super(distribucion, principal, seleccion);
        }

        @Override
        protected List<Pregunta> consultarPreguntas(int idEncuesta) throws SQLException {
            cargas++;
            ultimoIdEncuesta = idEncuesta;
            if (fallarCarga) {
                throw new SQLException("Consulta de preguntas fallida");
            }
            List<Pregunta> preguntas = new ArrayList<>();
            if (sinPreguntas) {
                return preguntas;
            }
            if (idEncuesta == 11) {
                preguntas.add(new Pregunta(101, 11, "Comentario", TipoPregunta.RESPUESTA_SIMPLE, "General", false));
                Pregunta dicotomica = new Pregunta(305, 11, "Recomendaria el servicio", TipoPregunta.DICOTOMICA, "General", false);
                dicotomica.generarOpcionesSegunTipo();
                preguntas.add(dicotomica);
                preguntas.add(new Pregunta(470, 11, "Cantidad", TipoPregunta.NUMERICA, "General", false));
                Pregunta escala = new Pregunta(980, 11, "Satisfaccion", TipoPregunta.ESCALA_VALORACION, "General", false);
                escala.generarOpcionesSegunTipo();
                preguntas.add(escala);
            } else {
                preguntas.add(new Pregunta(2005, preguntaAjena ? 11 : 28, "Otra pregunta", TipoPregunta.RESPUESTA_SIMPLE, "General", false));
            }
            return preguntas;
        }

        @Override
        protected int registrarRespuestas(List<Respuesta> respuestas) throws SQLException {
            intentosGuardado++;
            if (fallarGuardado) {
                throw new SQLException("Insercion de prueba rechazada");
            }
            guardadas = new ArrayList<>(respuestas);
            return guardadas.size();
        }
    }

    private static void comprobar(boolean condicion, String descripcion) {
        if (!condicion) {
            throw new AssertionError(descripcion);
        }
        comprobaciones++;
    }

    private static JComponent buscarCampo(Container contenedor, String nombre) {
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JComponent && nombre.equals(componente.getName())) {
                return (JComponent) componente;
            }
            if (componente instanceof Container) {
                JComponent encontrado = buscarCampo((Container) componente, nombre);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    private static JButton buscarBoton(Container contenedor, String texto) {
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JButton && texto.equals(((JButton) componente).getText())) {
                return (JButton) componente;
            }
            if (componente instanceof Container) {
                JButton encontrado = buscarBoton((Container) componente, texto);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    private static JTable buscarTabla(Container contenedor) {
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JTable) {
                return (JTable) componente;
            }
            if (componente instanceof Container) {
                JTable encontrada = buscarTabla((Container) componente);
                if (encontrada != null) {
                    return encontrada;
                }
            }
        }
        return null;
    }

    private static void completarPrimeraEncuesta(RespuestasDePrueba respuestas) {
        ((JTextField) buscarCampo(respuestas, "respuesta_101")).setText("Atencion correcta");
        ((JComboBox<?>) buscarCampo(respuestas, "respuesta_305")).setSelectedIndex(1);
        ((JTextField) buscarCampo(respuestas, "respuesta_470")).setText("12.5");
        ((JComboBox<?>) buscarCampo(respuestas, "respuesta_980")).setSelectedIndex(4);
    }

    private static void comprobarValidacionDAO() throws SQLException {
        List<Respuesta> respuestas = new ArrayList<>();
        try {
            RespuestaDAO.guardarRespuestasEncuesta(respuestas);
            throw new AssertionError("El DAO acepto una lista vacia");
        } catch (IllegalArgumentException esperada) {
            comprobaciones++;
        }
        Respuesta respuesta = new Respuesta();
        respuesta.setIdParticipante(37);
        respuesta.setIdEncuesta(11);
        respuesta.setIdPregunta(101);
        respuesta.setContenido("Contenido");
        respuestas.add(respuesta);
        respuestas.add(respuesta);
        try {
            RespuestaDAO.guardarRespuestasEncuesta(respuestas);
            throw new AssertionError("El DAO acepto preguntas repetidas");
        } catch (IllegalArgumentException esperada) {
            comprobaciones++;
        }
    }

    private static void ejecutarPruebas() throws SQLException {
        CardLayout distribucion = new CardLayout();
        JPanel principal = new JPanel(distribucion);
        Participante participante = new Participante("45671239", "Participante de prueba", "prueba@example.com");
        participante.setId_participante(37);
        SeleccionDePrueba seleccion = new SeleccionDePrueba(distribucion, principal);
        RegistroDePrueba registro = new RegistroDePrueba(distribucion, principal, participante);
        RespuestasDePrueba respuestas = new RespuestasDePrueba(distribucion, principal, seleccion);
        principal.add(registro, "RegistroParticipante");
        principal.add(seleccion, "EjecutarEncuesta");
        principal.add(respuestas, "RegistrarRespuestas");
        registro.establecerPanelEjecucion(seleccion);
        seleccion.establecerPanelRespuestas(respuestas);
        JButton finalizar = buscarBoton(respuestas, "Finalizar encuesta");
        JButton continuar = buscarBoton(registro, "Continuar");
        JTextField dni = (JTextField) buscarCampo(registro, "dniParticipante");
        try {
            comprobar(seleccion.consultas == 0 && respuestas.cargas == 0, "Se consulto SQL durante la construccion");
            comprobar(!finalizar.isEnabled(), "Se puede finalizar sin encuesta");
            distribucion.show(principal, "EjecutarEncuesta");
            seleccion.ejecutarEncuesta(0);
            comprobar(seleccion.isVisible() && respuestas.cargas == 0, "Se ejecuto sin participante");

            distribucion.show(principal, "RegistroParticipante");
            dni.setText("99998888");
            continuar.doClick();
            comprobar(registro.isVisible() && seleccion.consultas == 0, "Un participante no registrado pudo continuar");
            dni.setText(participante.getDni());
            registro.fallarConsulta = true;
            continuar.doClick();
            comprobar(registro.isVisible(), "Se navego tras fallar la consulta de participante");
            registro.fallarConsulta = false;
            seleccion.fallarConsulta = true;
            continuar.doClick();
            comprobar(registro.isVisible(), "Se navego tras fallar la consulta de encuestas");
            seleccion.fallarConsulta = false;
            continuar.doClick();
            JTable tabla = buscarTabla(seleccion);
            comprobar(seleccion.isVisible() && tabla.getRowCount() == 2, "No se cargaron las encuestas del participante");
            comprobar(tabla.editCellAt(0, 4), "No se pudo pulsar Ejecutar en la tabla");
            ((JButton) tabla.getEditorComponent()).doClick();
            comprobar(!tabla.isEditing(), "El editor de la tabla quedo activo");
            comprobar(respuestas.isVisible() && respuestas.ultimoIdEncuesta == 11, "No se cargo la encuesta elegida");
            comprobar(seleccion.getIdEncuestaSeleccionada() == 11, "Se perdio el ID de encuesta");
            comprobar(buscarCampo(respuestas, "respuesta_101") instanceof JTextField, "Falta la respuesta de texto");
            comprobar(buscarCampo(respuestas, "respuesta_305") instanceof JComboBox<?>, "Falta la respuesta dicotomica");
            comprobar(buscarCampo(respuestas, "respuesta_470") instanceof JTextField, "Falta la respuesta numerica");
            comprobar(buscarCampo(respuestas, "respuesta_980") instanceof JComboBox<?>, "Falta la escala");

            finalizar.doClick();
            comprobar(respuestas.intentosGuardado == 0 && respuestas.isVisible(), "Se guardaron respuestas vacias");
            completarPrimeraEncuesta(respuestas);
            ((JComboBox<?>) buscarCampo(respuestas, "respuesta_305")).setSelectedIndex(0);
            finalizar.doClick();
            comprobar(respuestas.intentosGuardado == 0, "Se guardo la opcion Seleccione");
            completarPrimeraEncuesta(respuestas);
            ((JTextField) buscarCampo(respuestas, "respuesta_470")).setText("NaN");
            finalizar.doClick();
            comprobar(respuestas.intentosGuardado == 0, "Se guardo un numero no finito");
            completarPrimeraEncuesta(respuestas);
            List<Respuesta> capturadas = respuestas.capturarRespuestas();
            int[] identificadores = { 101, 305, 470, 980 };
            comprobar(capturadas.size() == 4, "No se capturaron las cuatro preguntas");
            for (int indice = 0; indice < capturadas.size(); indice++) {
                Respuesta respuesta = capturadas.get(indice);
                comprobar(respuesta.getIdParticipante() == 37 && respuesta.getIdEncuesta() == 11
                        && respuesta.getIdPregunta() == identificadores[indice], "Se mezclaron los identificadores");
            }
            comprobar("Si".equals(capturadas.get(1).getContenido()), "Se perdio la opcion dicotomica");
            respuestas.fallarGuardado = true;
            finalizar.doClick();
            comprobar(respuestas.isVisible() && finalizar.isEnabled(), "Un error de guardado abandono el formulario");
            comprobar("Atencion correcta".equals(((JTextField) buscarCampo(respuestas, "respuesta_101")).getText()), "Se perdio el texto al fallar");
            respuestas.fallarGuardado = false;
            finalizar.doClick();
            comprobar(seleccion.isVisible() && respuestas.guardadas.size() == 4, "No se finalizo la encuesta");
            comprobar(!finalizar.isEnabled(), "El boton permite repetir el mismo envio");
            int intentos = respuestas.intentosGuardado;
            finalizar.doClick();
            comprobar(respuestas.intentosGuardado == intentos, "Se repitio el mismo guardado");

            respuestas.fallarCarga = true;
            seleccion.ejecutarEncuesta(1);
            comprobar(seleccion.isVisible() && !finalizar.isEnabled(), "Se abrio una encuesta tras fallar su consulta");
            comprobar(seleccion.getIdEncuestaSeleccionada() == -1, "Quedo un ID seleccionado tras fallar la carga");
            comprobar(buscarCampo(respuestas, "respuesta_101") == null, "Quedaron preguntas de la encuesta anterior");
            respuestas.fallarCarga = false;
            respuestas.sinPreguntas = true;
            seleccion.ejecutarEncuesta(1);
            comprobar(seleccion.isVisible() && !finalizar.isEnabled(), "Se inventaron preguntas de ejemplo");
            respuestas.sinPreguntas = false;
            respuestas.preguntaAjena = true;
            seleccion.ejecutarEncuesta(1);
            comprobar(seleccion.isVisible() && !finalizar.isEnabled(), "Se admitio una pregunta de otra encuesta");
            respuestas.preguntaAjena = false;
            seleccion.ejecutarEncuesta(1);
            comprobar(respuestas.isVisible() && respuestas.ultimoIdEncuesta == 28, "No se cambio a la segunda encuesta");
            comprobar(buscarCampo(respuestas, "respuesta_101") == null && buscarCampo(respuestas, "respuesta_2005") != null,
                    "Se mezclaron los campos de las dos encuestas");
            ((JTextField) buscarCampo(respuestas, "respuesta_2005")).setText("Respuesta independiente");
            finalizar.doClick();
            comprobar(respuestas.guardadas.size() == 1 && respuestas.guardadas.get(0).getIdPregunta() == 2005
                    && respuestas.guardadas.get(0).getIdEncuesta() == 28, "Se guardo la segunda encuesta con IDs anteriores");
            Participante otroParticipante = new Participante("56473829", "Segundo participante", "otro@example.com");
            otroParticipante.setId_participante(82);
            comprobar(seleccion.cargarEncuestas(otroParticipante), "No se actualizo el participante");
            seleccion.ejecutarEncuesta(1);
            ((JTextField) buscarCampo(respuestas, "respuesta_2005")).setText("Otro participante");
            comprobar(respuestas.capturarRespuestas().get(0).getIdParticipante() == 82,
                    "Se conservaron los datos del participante anterior");
            comprobarValidacionDAO();
        } finally {
            buscarBoton(respuestas, "Volver").doClick();
        }
    }

    public static void main(String[] argumentos) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    ejecutarPruebas();
                } catch (SQLException excepcion) {
                    throw new IllegalStateException(excepcion);
                }
            }
        });
        System.out.println("Pruebas del flujo: " + comprobaciones + " comprobaciones correctas.");
        System.out.println("Consultas y guardado simulados. No se utilizo SQL Server.");
    }
}
