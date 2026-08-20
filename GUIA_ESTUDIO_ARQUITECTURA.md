# GUÍA COMPLETA DE ESTUDIO Y ARQUITECTURA - PROYECTO RELIK

**Dirigido a:** Estudiante de DAM (Desarrollo de Aplicaciones Multiplataforma)  
**Objetivo:** Comprender en profundidad el funcionamiento, flujo de datos, arquitectura y estructura archivo por archivo de la aplicación **Relik** para preparar el repaso del código y la defensa del Proyecto Fin de Ciclo (PFC).

---

## 1. El "Big Picture": ¿Cómo funciona Relik?

**Relik** es una aplicación de arquitectura **Cliente-Servidor desacoplada**. Esto significa que el proyecto está dividido en dos partes totalmente independientes que se comunican a través de Internet o una Red Local usando el protocolo **HTTP** y mensajes en formato **JSON**.

```
┌─────────────────────────────────────────────────────────────────┐
│                      CLIENTE (FRONTEND SWING)                   │
│  - Formulario de Login, Pestañas de Gestión y Tablas Swing      │
│  - No toca la Base de Datos directamente                        │
│  - Usa HttpClient de Java para enviar solicitudes HTTP/JSON     │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                     Peticiones HTTP (GET, POST, PUT, DELETE)
                     Respuestas HTTP con datos JSON
                                 │
┌────────────────────────────────▼────────────────────────────────┐
│                     SERVIDOR (BACKEND SPRING BOOT)              │
│  - Servidor Apache Tomcat Embebido (Escucha en puerto 8080)     │
│  - Controladores REST (@RestController) convierten JSON <-> Java│
│  - Servicio de Negocio (ModeloInterfaceImpl)                    │
│  - Persistencia con Spring Data JPA / Hibernate (ORM)           │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                     Consultas SQL (JDBC / HikariCP)
                                 │
┌────────────────────────────────▼────────────────────────────────┐
│                      BASE DE DATOS (MYSQL 8.0)                  │
│  - Tablas: tarqueologo, tyacimiento, tmuseo, tresto, thallazgo  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Flujo de Ejecución Paso a Paso: Desde que le das a "Play"

### Paso 1: Arranque del Servidor (`RelikApplication.java`)
1. Ejecutas `java -jar target/Relik-0.0.1-SNAPSHOT.jar`.
2. Spring Boot inicia su contenedor de inyección de dependencias (`ApplicationContext`).
3. Inicializa el servidor web **Apache Tomcat** en el puerto `8080`.
4. Conecta con MySQL usando el pool de conexiones **HikariCP** (`application.properties`).
5. **Hibernate** escanea el paquete `dominio/` y valida las tablas relacionales.
6. Se ejecuta el método `@PostConstruct inicializarDatosEjemplo()` en `ModeloInterfaceImpl.java`. Si la base de datos está vacía, crea automáticamente los usuarios por defecto (`admin` y `prueba`) junto a museos, yacimientos y hallazgos iniciales.

### Paso 2: Arranque del Cliente (`ClienteRelik.java`)
1. Ejecutas `java -cp "target\classes;target\lib\*" org.example.relik.cliente.ClienteRelik`.
2. Se establece el *Look and Feel* multiplataforma de Swing mediante `UITheme.java`.
3. Se instancia y muestra el diálogo modal de autenticación `LoginDialog`.

### Paso 3: Autenticación y Gestión de Sesión (`LoginDialog` ➔ `SessionManager`)
1. El usuario introduce `prueba@example.com` / `1234` y hace clic en "Iniciar Sesión".
2. `LoginDialog` crea una petición **HTTP POST** a `http://localhost:8080/api/auth/login` enviando un cuerpo JSON:
   ```json
   { "correo": "prueba@example.com", "contrasena": "1234" }
   ```
3. El servidor procesa la petición en `AuthController.java` y llama al servicio `ModeloInterfaceImpl`.
4. Si la clave es válida, el servidor responde con un objeto **DTO JSON** con los datos del usuario y su rol (`ADMIN` o `ARQUEOLOGO`).
5. El cliente Swing recibe el JSON, lo parsea con **Gson** e inicia la sesión en el patrón Singleton `SessionManager.getInstance()`.

### Paso 4: Visualización y Operaciones en Campo (Ej: Gestor de Hallazgos)
1. El usuario abre el `GestorHallazgos`.
2. El gestor envía una petición **HTTP GET** a `/api/hallazgos`.
3. `HallazgoController.java` obtiene la lista de hallazgos en la BD, los convierte a `HallazgoDTO` (incluyendo **micro-localización 3D: Cuadrícula, X, Y, Z y Unidad Estratigráfica UE**) y los devuelve en un array JSON.
4. `GestorHallazgos` vacía su `DefaultTableModel` y añade cada fila a la tabla `JTable` estilizada con la paleta de colores arqueológica.

---

## 3. Desglose Archivo por Archivo (Capa a Capa)

---

### 📦 CAPA 1: DOMINIO / ENTIDADES JPA (`org.example.relik.dominio`)

Estas clases representan las tablas de MySQL en forma de objetos Java.

#### 1. [Arqueologo.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/dominio/Arqueologo.java)
- **Propósito:** Mapea la tabla `tarqueologo`.
- **Campos principales:**
  - `idArqueologo` (`@Id`, `@GeneratedValue`): Identificador único entero autoincremental.
  - `nombre`, `correo`, `contrasena`, `rol`: Atributos del investigador. El rol puede ser `"ADMIN"` o `"ARQUEOLOGO"`.
  - `hallazgoList` (`@OneToMany`): Relación de 1 a N con la entidad `Hallazgo`.
- **Anotación Clave:** `@JsonIgnore` en `hallazgoList` para evitar bucles infinitos de serialización JSON al consultar un arqueólogo.

#### 2. [Yacimiento.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/dominio/Yacimiento.java)
- **Propósito:** Mapea la tabla `tyacimiento`.
- **Campos principales:**
  - `idYacimiento`, `nombre`, `ubicacion`, `coordenadas` (GPS generales), `epoca` (Época prehistórica/histórica principal), `fechaDescubrimiento`, `fechaInicio`.

#### 3. [Museo.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/dominio/Museo.java)
- **Propósito:** Mapea la tabla `tmuseo`.
- **Campos principales:**
  - `idMuseo`, `nombre`, `ciudad`, `pais`, `epocaEspecializada`.
  - `restoMaterialList` (`@OneToMany`): Lista de piezas custodiadas en la institución.

#### 4. [RestoMaterial.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/dominio/RestoMaterial.java)
- **Propósito:** Mapea la tabla `tresto_material`.
- **Campos principales:**
  - `idResto`, `nombre`, `epoca`, `tipologia`.
  - `museo` (`@ManyToOne`, `@JoinColumn(name = "id_museo")`): Museo al que ha sido asignado el resto.
  - `hallazgoList` (`@OneToMany`): Lista de registros de hallazgos asociados a esta pieza (relación 1 a N, un resto material puede tener múltiples hallazgos/fragmentos registrados).

#### 5. [Hallazgo.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/dominio/Hallazgo.java)
- **Propósito:** Mapea la tabla `thallazgo` (Entidad central que une Arqueólogo, Yacimiento, Resto y Ubicación Espacial 3D).
- **Campos principales:**
  - `idHallazgo`, `fechaHallazgo`.
  - **Micro-Localización 3D y Estratigrafía:**
    - `cuadricula`: Identificador de retícula (ej. `"Cuadrícula A1"`).
    - `coordenadaX`: Distancia plana X (ej. `"0.45m"`).
    - `coordenadaY`: Distancia plana Y (ej. `"1.20m"`).
    - `cotaZ`: Nivel de profundidad Z (ej. `"-1.85m"`).
    - `unidadEstratigrafica`: Código UE de capa sedimentaria (ej. `"UE-102"`).
  - `arqueologo`, `yacimiento`, `restoMaterial`: Relaciones `@ManyToOne`.

---

### 📦 CAPA 2: DTOs / TRANSFERENCIA DE DATOS (`org.example.relik.dto`)

**¿Por qué existen los DTOs?**  
Si enviáramos las entidades JPA directamente por la red en formato JSON, Jackson entraría en un bucle infinito (Arqueólogo contiene Hallazgos, que contiene Arqueólogo, etc.). Además, los DTOs ocultan campos sensibles como contraseñas y permiten enviar alias limpios de variables al cliente Swing.

- **`HallazgoDTO.java`**: Transporta el ID, nombre del arqueólogo, nombre del yacimiento, tipo de resto material, la **cuadrícula, ejes X, Y, cota Z y UE**, y la fecha serializada como String.
- **`YacimientoDTO.java`**: Transporta los datos del yacimiento con alias cruzados (`ubicacion`/`localizacion`, `coordenadas`/`descripcion`, `epoca`).
- **`ArqueologoDTO.java`**, **`MuseoDTO.java`**, **`RestoMaterialDTO.java`**: DTOs equivalentes para el resto de entidades.

---

### 📦 CAPA 3: MODELO Y REPOSITORIOS (`org.example.relik.modelo`)

#### 1. Interfaces Repositorio (`*Repository.java`)
- **`ArqueologoRespository`**, **`YacimientoRepository`**, **`MuseoRepository`**, **`RestoMaterialRepository`**, **`HallazgoRepository`**.
- **Cómo funcionan:** Extienden de `JpaRepository<Entidad, Integer>`. Spring Data JPA genera automáticamente las consultas SQL de base (`findAll()`, `findById()`, `save()`, `deleteById()`) sin necesidad de escribir una sola línea de SQL.
- **Métodos personalizados:**
  - `findByCorreoIgnoreCase(String correo)` en `ArqueologoRespository`.
  - `findByEpoca(String epoca)` en `MuseoRepository`.

#### 2. [ModeloInterfaceImpl.java](file:///e:/Nubes/Profe/OneDrive%20-%20Consejer%C3%ADa%20de%20Educaci%C3%B3n,%20Formaci%C3%B3n%20Profesional%20y%20Empleo/DAM/Proyecto/Arquealia%20ag/src/main/java/org/example/arquealia/modelo/ModeloInterfaceImpl.java)
- **Propósito:** Clase de servicio annotada con `@Service` que concentra la lógica de negocio del servidor.
- **Método Destacado `asignarMuseo(RestoMaterial resto)`:** Busca en la BD si existe un museo especializado en la misma época que el resto material; si lo encuentra, se lo asigna automáticamente.
- **Método Destacado `@PostConstruct inicializarDatosEjemplo()`:** Se ejecuta automáticamente tras iniciar Spring. Si la base de datos está vacía, la puebla con usuarios, yacimientos, museos, restos y hallazgos con localización 3D.

---

### 📦 CAPA 4: CONTROLADORES API REST (`org.example.relik.controlador`)

Reciben las peticiones HTTP desde el cliente Swing, invocan el servicio de negocio y devuelven respuestas HTTP (200 OK, 401 Unauthorized, 403 Forbidden, 404 Not Found).

- **`AuthController.java`**:
  - `POST /api/auth/login`: Valida usuario y contraseña.
  - `POST /api/auth/register`: Registra un nuevo arqueólogo.
- **`HallazgoController.java`**:
  - `GET /api/hallazgos`: Retorna el listado completo en DTOs.
  - `POST /api/hallazgos`: Recibe el JSON del cliente, crea el resto material, le asigna museo, registra la fecha, la **micro-localización 3D y UE** e inserta el hallazgo.
  - `DELETE /api/hallazgos/{id}?usuarioId=X`: Verifica si el usuario que solicita el borrado es `ADMIN` o el autor original del hallazgo. Si no lo es, retorna HTTP 403 Forbidden.
- **`YacimientoController.java`**, **`MuseoController.java`**, **`ArqueologoController.java`**, **`RestoMaterialController.java`**: Controladores CRUD correspondientes.

---

### 📦 CAPA 5: CLIENTE SWING (`org.example.relik.cliente`)

- **`ClienteRelik.java`**: Ventana principal (`JFrame`). Muestra la barra superior con el usuario logueado, su rol y los 5 botones para abrir los gestores de entidad.
- **`LoginDialog.java`**: Diálogo de acceso modal (`JDialog`). Ofrece pestañas para Iniciar Sesión y Registrarse.
- **`SessionManager.java`**: Clase Singleton que guarda la información de la sesión activa en el cliente (`idArqueologo`, `nombre`, `correo`, `rol`).
- **`UITheme.java`**: Sistema de temas visuales. Define los colores globales (Terracota, Pergamino, Verde Oliva, Marrón Tierra), las fuentes y la función `createButton` para renderizar botones de alto contraste.
- **`GestorBase.java`**: Clase base abstracta de la que heredan todos los gestores. Define la estructura de la ventana, la creación de tablas `JTable` estilizadas y los métodos abstractos `cargarDatos()`, `agregarRegistro()`, `editarRegistro()` y `eliminarRegistro()`.
- **`GestorHallazgos.java`**: Ventana de gestión de hallazgos. Incluye el formulario modal con los campos de **Cuadrícula, Coordenada X, Coordenada Y, Cota Z y Unidad Estratigráfica UE**, y valida los permisos de borrado antes de enviar la petición DELETE al servidor.

---

## 4. Conceptos Clave para la Defensa ante el Tribunal

| Concepto | Explicación sencilla para el tribunal |
|---|---|
| **Arquitectura Cliente-Servidor REST** | El cliente (interfaz Swing) y el servidor (Spring Boot) están separados. Se comunican mediante HTTP y JSON. |
| **ORM (Hibernate)** | Mapeador Objeto-Relacional. Convierte tablas de MySQL en clases Java (`@Entity`) automáticamente. |
| **Spring Data JPA** | Capa que abstrae las consultas SQL. Al extender `JpaRepository`, Spring genera las operaciones CRUD sin código manual. |
| **Tomcat Embebido** | El servidor web va dentro del ejecutable JAR. No hace falta instalar un servidor externo en la máquina. |
| **Patrón DTO (Data Transfer Object)** | Objetos ligeros usados únicamente para transmitir datos por la red sin enviar las entidades de BD directas. |
| **Patrón Singleton (`SessionManager`)** | Garantiza que solo existe una instancia de la sesión en memoria para toda la aplicación cliente. |
| **Micro-Localización 3D y UE** | Método científico de excavación que registra la posición espacial por retícula (X,Y), cota de profundidad (Z) y estrato sedimentario (UE). |


