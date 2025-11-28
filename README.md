Evaluación 2 - PetsOnlineSPA

1. Caso elegido y alcance

Caso: PetsOnlineSPA (Cliente, Productos, Servicios Veterinarios, Reservas y Mascotas).

Alcance: Diseño/UI, validaciones, navegación, gestión de estado, persistencia local, recursos nativos, animaciones y consumo de API real.

2. Requisitos y ejecución

Stack:
- Kotlin + Android Studio (Jetpack Compose)
- ViewModel + StateFlow para gestión de estado
- DataStore para persistencia local
- Retrofit + Coroutines para consumo de API
- Coil para carga de imágenes
- Navigation Compose

Instalación:
1. Clonar el repositorio:
   git clone https://github.com/javiergiraudo/App.git
2. Abrir el proyecto en Android Studio.
3. Esperar la sincronización de Gradle.
4. Conectar un emulador.

Ejecución:
- Ejecutar con el botón Run app en Android Studio.
- La aplicación inicia en la pantalla de Login o Signup.

3. Arquitectura y flujo

Estructura de carpetas:

data/
 ┣ local/          
 ┣ remote/         
 ┣ repository/     
domain/
 ┗ model/          
navigation/
root/
ui/
 ┣ components/     
 ┣ screens/        
 ┗ viewmodels/     
utils/             

Gestión de estado:
Se utiliza StateFlow dentro de los ViewModel para manejar los estados de carga, éxito y error, actualizando la interfaz en tiempo real.

Navegación:
Implementada con NavHost de Compose.
Flujo principal: Login → Home → Perfil / Mascotas / Productos / Servicios / Reservas.

4. Funcionalidades

Formulario validado:
- Formularios de Login, Registro, Mascota y Reserva.
- Validaciones por campo (obligatorios, formato email, fechas, contraseñas y longitud).
- Mensajes visibles y bloqueo del envío si los datos son inválidos.

Navegación y backstack:
- Flujo entre pantallas controlado por backstack.
- Acceso protegido a pantallas internas (requieren sesión iniciada).

Gestión de estado:
- Estados de carga y error en peticiones a la API.
- Actualización dinámica de la interfaz según las respuestas.

Persistencia local:
- CRUD local para Mascotas, Productos, Servicios y Reservas.
- Token y datos del usuario guardados con DataStore.
- Imagen de perfil almacenada localmente (URI en DataStore, visible sin conexión).

Recursos nativos:
- Uso de galería y cámara para seleccionar o capturar foto de perfil.
- Manejo de permisos y fallback si el usuario los deniega.

Animaciones:
- Transiciones suaves entre pantallas y efectos visuales al cargar listas.
- Microinteracciones que mejoran la experiencia del usuario.

Consumo de API:
- Autenticación, login, registro y validación de token.
- Endpoint /auth/me para obtener datos del usuario autenticado.
- Manejo de errores y códigos HTTP (401, 403, 500).

5. Endpoints

Base URL:
https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW

Método: POST
Ruta: /auth/signup
Body: { email, password }
Respuesta: 201 { authToken, user: { id, email, ... } }
Errores: 400 (validación), 409 (ya existe), 500

Método: POST
Ruta: /auth/login
Body: { email, password }
Respuesta: 200 { authToken, user: { id, email, ... } }
Errores: 401 (inválido), 400, 500

Método: GET
Ruta: /auth/me
Body: - (requiere header Authorization)
Respuesta: 200 { id, email, name?, avatarUrl? }
Errores: 401, 403, 500

6. User flows

Flujo principal:
1. El usuario abre la app en la pantalla de Login.
2. Si no tiene cuenta, puede registrarse con validaciones activas.
3. Al iniciar sesión, se guarda el token y se redirige al Home.
4. Desde Home puede:
   - Entrar al perfil, ver, cambiar o agregar su foto (galería o cámara) y cerrar sesión.
   - Registrar o editar una mascota.
   - Consultar productos o servicios veterinarios.
   - Reservar una cita veterinaria.
5. Al cerrar sesión, la app limpia los datos guardados pero mantiene el token si el usuario no borra caché.

Flujo de error:
- Si falla la conexión con la API, se muestra un mensaje de error y la opción de reintento.
- Si el token expira, se redirige automáticamente a la pantalla de login.
