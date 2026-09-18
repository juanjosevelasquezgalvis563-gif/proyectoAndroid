1.Nombre de la Aplicación: TaskManager App

Descripción: Solución móvil para la administración segura de tareas personales en la nube con aislamiento por usuario (ownerId). Incluye persistencia local mediante Room para la gestión de borradores sin conexión a internet y su posterior sincronización con Cloud Firestore.

Integrantes del Equipo:

Juan Jose Velasquez Galvis


2.Tecnologías y Arquitectura Implementadas
Tecnologías
Lenguaje: Kotlin

Interfaz de Usuario: Jetpack Compose & Navigation Compose

Gestión de Estado & Concurrencia: ViewModel, StateFlow y Corrutinas

Backend (BaaS): Firebase Authentication & Cloud Firestore (Firebase Android BoM)

Persistencia Local: Room Database & KSP

Control de Versiones: Git / GitHub

Arquitectura MVVM
UI: Capa visual encargada de renderizar componentes de Compose y consumir estados (StateFlow).

ViewModel: Capa intermediaria que gestiona la lógica de interfaz, procesa eventos y ejecuta casos de uso.

Domain: Modelos de dominio puros, contratos de repositorios e interfaces de casos de uso desacoplados de librerías externas.

Data: Implementación de repositorios, fuentes de datos remotas (Firestore) y locales (Room), junto con sus respectivos mapeadores.

3.Instrucciones para Configurar y Ejecutar el Proyecto
Clonar el repositorio:

Bash
git clone <url-del-repositorio>
Configurar Firebase:

Crear un proyecto en la consola de Firebase.

Registrar la aplicación Android utilizando el applicationId exacto del proyecto.

Descargar el archivo google-services.json y ubicarlo en la ruta del módulo: app/google-services.json.

Habilitar Firebase Authentication (Correo/Contraseña) y Cloud Firestore.

Sincronizar y Ejecutar:

Abrir el proyecto en Android Studio.

Ejecutar la sincronización de archivos Gradle (Sync Project with Gradle Files).

Conectar un dispositivo físico o iniciar un emulador y presionar Run.

4.Explicación de la Estructura de Paquetes
Plaintext
com.sena.taskmanager
├── data
│    ├── local              # DAOs, Base de datos Room y entidades locales
│    ├── remote             # Modelos de datos para Cloud Firestore
│    ├── mapper             # Transformadores entre entidades/documentos y modelos de dominio
│    └── repository         # Implementaciones de los repositorios de datos
├── domain
│    ├── model              # Modelos puros de dominio
│    ├── repository         # Interfaces o contratos de los repositorios
│    └── usecase            # Casos de uso de negocio (Auth, Tareas, Borradores)
├── ui
│    ├── navigation         # Definición de rutas y navegación con Compose
│    ├── screen             # Pantallas principales (Login, Registro, Listado, Formulario, Borradores)
│    ├── component          # Elementos reutilizables de UI
│    └── state              # Clases de estado para la interfaz
├── di                      # Módulos de provisión de dependencias
├── MainActivity.kt
└── TaskManagerApplication.kt

5.Funcionalidades Terminadas y Errores Conocidos
Funcionalidades Terminadas
RF01 a RF03: Registro, inicio de sesión y cierre de sesión seguro mediante Firebase Authentication con validaciones de campos y manejo de estados.

RF04 a RF07: Creación, lectura, actualización y eliminación (CRUD) de tareas en Cloud Firestore filtradas por el ID del usuario autenticado (ownerId).

RF08 a RF09: Almacenamiento local de borradores utilizando Room Database para operación offline y sincronización hacia la nube.

Errores Conocidos
No se reportan errores críticos en la compilación ni en la ejecución de los flujos principales de autenticación, sincronización local y remota.

6.Capturas de pantalla principales

