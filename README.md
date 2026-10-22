# PDD-PROY-SIGEUTP

Proyecto académico Java Desktop para gestionar encuestas, participantes, preguntas y respuestas. Utiliza Java 21, Swing, CardLayout, JDBC, SQL Server y Ant/NetBeans.

## Estado

El proyecto compila con Java 21. El flujo principal tiene pruebas de interfaz con consultas y guardado simulados. La persistencia real permanece pendiente de validación contra SQL Server; no se incluye una base de datos ni un script de creación confirmado.

Los módulos de pagos, estadísticas, usuarios y otras operaciones contienen funcionalidades incompletas o datos de ejemplo. Este repositorio describe un trabajo académico en desarrollo y no una aplicación preparada para producción.

## Preparación

1. Instalar y seleccionar JDK 21.
2. Colocar `mssql-jdbc-13.2.1.jre8.jar` en `lib`, siguiendo `lib/README.md`.
3. Abrir la carpeta del proyecto en IntelliJ o NetBeans.
4. En IntelliJ, marcar `src` como Sources Root y `test` como Test Sources Root. Agregar el JAR de `lib` como dependencia del módulo con alcance Compile. Seleccionar UTF-8 como codificación.
5. Ejecutar `ui.VentanaPrincipal`.

En Windows, `compile_project.cmd` compila las fuentes a `build/classes`; no crea un JAR ejecutable. En NetBeans se conserva la configuración Ant de `build.xml` y `nbproject`.

## Conexión con SQL Server

La conexión se configura exclusivamente mediante estas variables de entorno:

| Variable | Contenido |
|---|---|
| SIGEUTP_BD_URL | URL JDBC de la instancia y base de datos |
| SIGEUTP_BD_USUARIO | Usuario SQL de la aplicación |
| SIGEUTP_BD_CLAVE | Contraseña de ese usuario |

En IntelliJ, añadirlas en Run → Edit Configurations → configuración de VentanaPrincipal → Environment variables. Si se ejecuta desde una terminal, definirlas en el entorno del proceso Java.

`.env.example` es una plantilla sin credenciales. Java no carga archivos `.env` automáticamente: copiar la plantilla a `.env` no configura la aplicación por sí solo. No publicar valores reales ni incorporarlos a configuraciones compartidas del IDE. Usar una cuenta SQL con los permisos necesarios para la aplicación.

La plantilla exige validar el certificado del servidor. Si una instancia local de prácticas usa un certificado autofirmado, su configuración puede requerir `trustServerCertificate=true`; esa decisión corresponde al entorno local.

Sin configuración de base de datos, la interfaz puede iniciarse, pero las operaciones que consultan o guardan datos no estarán disponibles.

## Acceso de demostración

El login actual contiene cuentas de ejemplo: administrador `admin` con clave `123`, y usuarios externos ficticios. `UsuarioDAO` también contiene credenciales de demostración para consola. Estos valores son públicos, no representan cuentas SQL y no ofrecen autenticación ni autorización adecuadas para producción. No reutilizar contraseñas reales para estas cuentas.

## Pruebas

Ejecutar `ui.PruebaFlujoEncuesta`, dentro de `test/ui`, como una aplicación Java. Comprueba 41 condiciones con datos simulados, sin SQL Server, frameworks de prueba nuevos ni reflexión.

El recorrido manual, las consultas y los supuestos del esquema están en [PRUEBA_FLUJO_ENCUESTA.md](PRUEBA_FLUJO_ENCUESTA.md).

## Archivos excluidos de Git

Se excluyen credenciales locales, configuraciones privadas del IDE, resultados de compilación, archivos de datos, respaldos y exportaciones identificadas. Las carpetas de imágenes originales se conservan localmente y se excluyen porque no tienen una licencia documentada y el código fuente actual no las utiliza. El controlador JDBC se obtiene por separado.

No hay una licencia de distribución del código propio definida en este proyecto. Antes de asignarla, corresponde confirmar la autoría y el acuerdo de los integrantes. La publicación del repositorio no implica que sus recursos de terceros puedan redistribuirse sin revisar sus licencias.
