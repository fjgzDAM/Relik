# GUÍA COMPLETA DE ESTUDIO Y ARQUITECTURA - PROYECTO RELIK

**Dirigido a:** Estudiante de DAM (Desarrollo de Aplicaciones Multiplataforma)  
**Objetivo:** Comprender en profundidad el funcionamiento, flujo de datos, arquitectura y estructura archivo por archivo de la aplicación **Relik** para preparar el repaso del código y la defensa del Proyecto Fin de Ciclo (PFC).

---

## 1. El "Big Picture": ¿Cómo funciona Relik?

**Relik** es una aplicación con **Arquitectura Maven Multimódulo (Cliente-Servidor desacoplado)**. Esto significa que el proyecto está dividido en dos aplicaciones independientes que se comunican mediante peticiones de red usando el protocolo **HTTP** y mensajes estructurados en **JSON**.

```
┌─────────────────────────────────────────────────────────────────┐
│              MÓDULO CLIENTE (FRONTEND SWING) - relik-cliente    │
│  - Formulario de Login, Pestañas de Gestión y Tablas Swing      │
│  - No toca la Base de Datos directamente                        │
│  - Usa HttpClient de Java para enviar solicitudes HTTP/JSON     │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                     Peticiones HTTP (GET, POST, PUT, DELETE)
                     Respuestas HTTP con datos JSON
                                 │
┌────────────────────────────────▼────────────────────────────────┐
│             MÓDULO SERVIDOR (BACKEND REST) - relik-servidor     │
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
│  - Base de Datos: BRelik                                        │
│  - Tablas: tarqueologo, tyacimiento, tmuseo, tresto_material,   │
│            thallazgo                                            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Flujo de Ejecución Paso a Paso: Desde que le das a "Play"

### Paso 1: Arranque del Servidor Backend (`RelikApplication.java`)
1. Ejecutas `java -jar relik-servidor/target/relik-servidor-0.0.1-SNAPSHOT.jar`.
2. Spring Boot inicia su contenedor de inyección de dependencias (`ApplicationContext`).
3. Inicializa el servidor web **Apache Tomcat** en el puerto `8080`.
4. Conecta con MySQL usando el pool de conexiones **HikariCP** (`application.properties`).
5. **Hibernate** escanea el paquete `dominio/` y valida las tablas relacionales de la base de datos `BRelik`.
6. Se ejecuta el método `@PostConstruct inicializarDatosEjemplo()` en `ModeloInterfaceImpl.java`. Si la base de datos está vacía, crea automáticamente los usuarios por defecto (`admin` y `prueba`) junto a museos, yacimientos y hallazgos iniciales.

### Paso 2: Arranque del Cliente Desktop (`ClienteRelik.java`)
1. Ejecutas `java -jar relik-cliente/target/relik-cliente-0.0.1-SNAPSHOT.jar`.
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
3. `HallazgoController.java` obtiene la lista de hallazgos en la BD, los convierte a `HallazgoDTO` (incluyendo **micro-localización 3D: Cuadrícula, X, Y, Z, Campaña y Unidad Estratigráfica UE**) y los devuelve en un array JSON.
4. `GestorHallazgos` vacía su `DefaultTableModel` y añade cada fila a la tabla `JTable` estilizada con la paleta de colores arqueológica.
5. Permite dos modos de alta:
   - **`[+ Registrar Resto Inédito]`**: Para piezas nunca antes descubiertas (crea el resto, asigna museo por época y registra el hallazgo 3D).
   - **`[+ Vincular a Resto Existente]`**: Para fragmentos o remontaje de una pieza previamente catalogada.

---

## 3. Desglose Archivo por Archivo (Capa a Capa)

---

### 📦 MÓDULO BACKEND (`relik-servidor`)

#### Capa de Dominio / Entidades JPA (`org.example.relik.dominio`)
Estas clases representan las tablas de MySQL en forma de objetos Java:

1. **`Arqueologo.java`**: Mapea la tabla `tarqueologo` (`idArqueologo`, `nombre`, `apellidos`, `especialidad`, `correo`, `contrasena`, `rol`).
2. **`Yacimiento.java`**: Mapea la tabla `tyacimiento` (`idYacimiento`, `nombre`, `ubicacion`, `coordenadas`, `epoca`, `fechaInicio`).
3. **`Museo.java`**: Mapea la tabla `tmuseo` (`idMuseo`, `nombre`, `ciudad`, `pais`, `epocaEspecializada`).
4. **`RestoMaterial.java`**: Mapea la tabla `tresto_material` (`idResto`, `nombre`, `epoca`, `tipologia`, `id_museo`).
5. **`Hallazgo.java`**: Mapea la tabla `thallazgo` (`idHallazgo`, `fechaHallazgo`, `campana`, `cuadricula`, `coordenadaX`, `coordenadaY`, `cotaZ`, `unidadEstratigrafica`, `idArqueologo`, `idYacimiento`, `idResto`).

#### Capa de Transferencia / DTOs (`org.example.relik.dto`)
Desacopla la estructura interna de la base de datos del formato JSON de la API REST:
- **`HallazgoDTO.java`**, **`YacimientoDTO.java`**, **`ArqueologoDTO.java`**, **`MuseoDTO.java`**, **`RestoMaterialDTO.java`**.

#### Capa de Servicio y Persistencia (`org.example.relik.modelo`)
- **Interfaces Repositorio**: `ArqueologoRespository`, `YacimientoRepository`, `MuseoRepository`, `RestoMaterialRepository`, `HallazgoRepository` (extienden de `JpaRepository`).
- **`ModeloInterfaceImpl.java`**: Lógica de negocio transaccional, auto-asignación de museos por época histórica y sembrado inicial de datos (`@PostConstruct inicializarDatosEjemplo`).

#### Capa de Controladores REST (`org.example.relik.controlador`)
- **`AuthController.java`**: Endpoints de login y registro.
- **`HallazgoController.java`**: Endpoints para gestión de hallazgos (modo inédito vs remontaje, y control de permisos de borrado).
- **`ArqueologoController.java`**, **`YacimientoController.java`**, **`MuseoController.java`**, **`RestoMaterialController.java`**: Controladores CRUD de cada entidad.

---

### 📦 MÓDULO FRONTEND SWING (`relik-cliente`)

- **`ClienteRelik.java`**: Ventana principal (`JFrame`) con menú de navegación temático.
- **`LoginDialog.java`**: Diálogo modal (`JDialog`) de autenticación y registro.
- **`SessionManager.java`**: Singleton en memoria que almacena la identidad del arqueólogo autenticado.
- **`UITheme.java`**: Paleta de color arqueológica de alto contraste y componentes estilizados.
- **`GestorBase.java`**: Clase abstracta base para las tablas y operaciones CRUD de la interfaz.
- **`GestorArqueologos.java`**: Gestión visual de investigadores, apellidos y especialidades.
- **`GestorYacimientos.java`**: Gestión visual de yacimientos, época principal y coordenadas GPS.
- **`GestorMuseos.java`**: Gestión de museos y épocas de especialización.
- **`GestorRestos.java`**: Inventario de piezas y visualización del museo asignado.
- **`GestorHallazgos.java`**: Registro 3D (X, Y, Z), Campaña, UE y remontaje de piezas.

---

## 4. Conceptos Clave para la Defensa ante el Tribunal

| Concepto | Explicación sencilla para el tribunal |
|---|---|
| **Arquitectura Maven Multimódulo** | El proyecto raíz compila y gestiona dos submódulos desacoplados: el servidor REST (`relik-servidor`) y el cliente Swing (`relik-cliente`). |
| **Arquitectura Cliente-Servidor REST** | El cliente (interfaz Swing) y el servidor (Spring Boot) están separados. Se comunican mediante HTTP y JSON de forma asíncrona o síncrona. |
| **ORM (Hibernate)** | Mapeador Objeto-Relacional. Convierte tablas de MySQL en clases Java (`@Entity`) automáticamente. |
| **Spring Data JPA** | Capa que abstrae las consultas SQL. Al extender `JpaRepository`, Spring genera las operaciones CRUD sin código manual. |
| **Tomcat Embebido** | El servidor web va dentro del ejecutable JAR del backend. No hace falta instalar un servidor externo en la máquina. |
| **Patrón DTO (Data Transfer Object)** | Objetos ligeros usados únicamente para transmitir datos por la red sin enviar las entidades de BD directas. |
| **Patrón Singleton (`SessionManager`)** | Garantiza que solo existe una instancia de la sesión en memoria para toda la aplicación cliente. |
| **Micro-Localización 3D y UE** | Método científico de excavación que registra la posición espacial por retícula (X,Y), cota de profundidad (Z), Campaña y estrato sedimentario (UE). |
