# Verificación del flujo principal de encuestas

## Alcance y estado

Se corrigieron selección de encuesta, carga de preguntas, captura de respuestas y envío mediante JDBC. Se conservaron Java 21, Swing, CardLayout, SQL Server, Ant/NetBeans y las dependencias existentes. No se agregaron patrones ni se modificaron pagos, estadísticas, exportaciones o los métodos del temporizador.

La compilación y las pruebas de interfaz pueden ejecutarse sin SQL Server. La persistencia real, las claves y la compatibilidad con un esquema físico permanecen pendientes porque no hay una instancia ni un script de creación disponibles. Los datos usados por la prueba automatizada son simulados y no se muestran como encuestas reales en la aplicación.

## Recorrido anterior y causa

`PanelRegistroParticipante → Continuar → PanelEjecutarEncuesta → Ejecutar → PanelRegistrarRespuestas → SQL directo`

Continuar solo validaba los campos y no recuperaba el ID del participante. Ejecutar cambiaba de tarjeta sin cargar la encuesta. El formulario se construía al iniciar la aplicación con preguntas de ejemplo. Guardar buscaba paneles anidados aunque los controles eran hijos directos; por ello no capturaba respuestas. También intentaba guardar el número ordinal de la pregunta y empleaba columnas distintas de las usadas por RespuestaDAO.

## Recorrido actual

1. `PanelRegistroParticipante.continuarEncuesta()` consulta el DNI con `ParticipanteDAO.consultarParticipanteParaEncuesta()` y obtiene un `Participante` con su ID real. Un participante nuevo debe guardarse antes de continuar. Un participante existente puede continuar con su DNI.
2. `PanelEjecutarEncuesta.cargarEncuestas()` utiliza `EncuestaDAO.consultarEncuestasDisponibles()` y llena la tabla con objetos `Encuesta` obtenidos de la base.
3. El botón de la fila termina la edición de la tabla y llama a `ejecutarEncuesta()`. Se pasan el participante y la encuesta seleccionada a `PanelRegistrarRespuestas.prepararEncuesta()`.
4. `PreguntaDAO.consultarPreguntasPorEncuesta()` consulta las preguntas activas por `id_encuesta`, en lugar de utilizar la caché inicial del DAO.
5. Cada pregunta se asocia con un campo mediante su `id_pregunta`. Texto y números utilizan JTextField; dicotómicas y escalas utilizan JComboBox. El formulario muestra título, descripción e ID de la encuesta seleccionada.
6. Finalizar llama a `capturarRespuestas()`. Primero valida todas las entradas y crea objetos `Respuesta` con los IDs de participante, encuesta y pregunta.
7. `RespuestaDAO.guardarRespuestasEncuesta()` abre una transacción, comprueba la pertenencia de cada pregunta, inserta las respuestas y consulta cada registro por la clave devuelta. Solo confirma el lote cuando todas las operaciones terminan. Una SQLException provoca rollback.
8. Tras el guardado confirmado se desactiva Finalizar y se vuelve a la tabla con un mensaje. Ante un error se conserva el formulario y sus respuestas.

El recorrido usa `Interfaz → evento → DAO → JDBC → SQL Server`. No se agregó una capa de servicio.

## Contrato SQL provisional

Se reutilizaron nombres presentes en las consultas de los DAO anteriores. No se crearon tablas, columnas, relaciones ni datos.

| Tabla | Columnas utilizadas por el flujo |
|---|---|
| Participantes | id_participante, dni, nombre, correo |
| Encuestas | id_encuesta, titulo, descripcion, estado |
| Preguntas | id_pregunta, id_encuesta, id_categoria, texto, tipo, opciones, estado |
| Respuestas | id_respuesta, id_pregunta, id_participante, id_encuesta, contenido, tiempo_respuesta, fecha_respuesta |

Las relaciones lógicas utilizadas son Preguntas.id_encuesta con Encuestas.id_encuesta, y los tres identificadores de Respuestas con sus respectivas entidades. Esto no acredita que existan restricciones PRIMARY KEY o FOREIGN KEY en una base real.

El contrato heredado considera encuestas con estado ACTIVA y preguntas con estado ACTIVO. Los tipos reconocidos son DICOTOMICA, RESPUESTA_SIMPLE, ESCALA_VALORACION y NUMERICA. Las opciones persistidas se separan con `|`; cuando no existen se utilizan las opciones del modelo Pregunta. Un tipo desconocido produce un error y no se sustituye por preguntas de ejemplo.

El guardado espera que SQL Server genere id_respuesta y lo entregue mediante getGeneratedKeys(). Esta característica no pudo contrastarse con una definición física de la tabla. tiempo_respuesta sigue guardándose en cero, como en el DAO anterior; no se implementó medición del tiempo.

## Prueba automatizada sin base de datos

Ejecutar `ui.PruebaFlujoEncuesta`, ubicado en `test/ui/PruebaFlujoEncuesta.java`, desde IntelliJ como una aplicación Java. No requiere JUnit ni bibliotecas nuevas. Los métodos de consulta y envío de los paneles se sustituyen en las clases de prueba por implementaciones locales, sin reflexión.

Comprueba selección con el botón real de JTable, identidad del participante, preguntas con IDs no consecutivos, cuatro tipos de entrada, validaciones, cambio de encuesta y participante, errores de consulta y envío, conservación de entradas, finalización y bloqueo del segundo clic. También comprueba el rechazo de lotes vacíos y preguntas repetidas antes de acceder a JDBC.

Esta prueba no ejecuta los INSERT, SELECT de verificación, commit o rollback contra SQL Server.

## Prueba manual con SQL Server disponible

1. Configurar SIGEUTP_BD_URL, SIGEUTP_BD_USUARIO y SIGEUTP_BD_CLAVE en el entorno de ejecución para una instancia accesible con la base esperada, siguiendo README.md.
2. Confirmar el contrato SQL anterior y preparar al menos dos encuestas activas, cada una con preguntas asociadas mediante id_encuesta. Esta etapa no corrige el editor o creador de preguntas; no se debe asumir que esos paneles secundarios ya generan relaciones válidas.
3. Ejecutar ui.VentanaPrincipal desde IntelliJ.
4. Pulsar Clic si eres Participante.
5. Para un participante nuevo, completar sus datos y pulsar Guardar; después pulsar Continuar. Para uno existente, ingresar su DNI y pulsar Continuar. La identidad se toma del registro de base de datos, no del nombre escrito sin guardar.
6. Comprobar el nombre del participante en la tabla y pulsar Ejecutar en la fila elegida.
7. Comprobar ID, título, descripción y las preguntas de esa encuesta.
8. Pulsar Finalizar encuesta con un campo vacío. Debe mantenerse el formulario con una indicación y no enviarse el lote.
9. Completar todas las preguntas y pulsar Finalizar encuesta. Debe volver a la tabla solo tras recibir confirmación del DAO.
10. Consultar Respuestas en SQL Server desde otra conexión para confirmar que la transacción quedó visible fuera de la aplicación.
11. Ejecutar una segunda encuesta y comprobar que no aparecen las preguntas ni las respuestas anteriores.

Sin SQL Server, Continuar debe informar del fallo de conexión; no existe un modo de demostración con datos inventados en producción.

## Registros a comprobar

Reemplazar DNI_REAL e ID_ENCUESTA_REAL por los valores utilizados. Primero ejecutar las consultas de lectura previas y anotar los id_respuesta existentes. No ejecutar el marcador ID_ENCUESTA_REAL sin sustituirlo por un número.

```sql
SELECT id_participante, dni, nombre
FROM Participantes
WHERE dni = 'DNI_REAL';

SELECT id_encuesta, titulo, estado
FROM Encuestas
WHERE id_encuesta = ID_ENCUESTA_REAL;

SELECT id_pregunta, id_encuesta, texto, tipo, estado
FROM Preguntas
WHERE id_encuesta = ID_ENCUESTA_REAL
  AND estado = 'ACTIVO'
ORDER BY id_pregunta;
```

Ejecutar esta consulta antes y después de finalizar, desde una conexión independiente. Debe aparecer un nuevo registro por cada pregunta mostrada, con sus IDs correctos, contenido ingresado y fecha de respuesta. Repetir deliberadamente una encuesta es un nuevo envío: esta etapa bloquea el doble clic del mismo formulario, pero no implementa una política de actualización o prohibición de múltiples participaciones.

```sql
SELECT r.id_respuesta,
       r.id_participante,
       p.dni,
       r.id_encuesta,
       r.id_pregunta,
       q.texto,
       r.contenido,
       r.fecha_respuesta
FROM Respuestas r
JOIN Participantes p ON p.id_participante = r.id_participante
JOIN Preguntas q ON q.id_pregunta = r.id_pregunta
               AND q.id_encuesta = r.id_encuesta
WHERE p.dni = 'DNI_REAL'
  AND r.id_encuesta = ID_ENCUESTA_REAL
ORDER BY r.id_respuesta;
```

Si una inserción falla, el formulario debe conservar las respuestas y mostrar el error. Comprobar desde SQL Server que ese intento no dejó filas parciales. Esta comprobación sigue pendiente de ejecución real.
