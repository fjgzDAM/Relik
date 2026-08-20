# MEMORIA TÉCNICA - Relik: Sistema de Registro de Hallazgos Arqueológicos

**Proyecto Fin de Ciclo (PFC) - Formación Profesional de Grado Superior**  
**Ciclo Formativo:** Desarrollo de Aplicaciones Multiplataforma (DAM)  
**Curso Académico:** 2024 / 2025  
**Centro Educativo:** IES José Luis Martínez Palomo  

---

## Licencia del Proyecto

Esta obra y su documentación asociada están bajo una licencia **Creative Commons Atribución-CompartirIgual 4.0 Internacional (CC BY-SA 4.0)**.  
Usted es libre de compartir (copiar y redistribuir el material en cualquier medio o formato) y adaptar (reorganizar, transformar y construir sobre el material) para cualquier propósito, incluso comercial, bajo los términos de reconocer la autoría y mantener la misma licencia.

---

## Ficha del Proyecto

- **Título del Proyecto:** Relik – Sistema de Registro y Gestión de Hallazgos Arqueológicos de Campo  
- **Nombre de la Aplicación:** Relik  
- **Autor / Alumno:** Alumno de DAM  
- **Curso / Convocatoria:** 2º DAM - Mayo 2025  
- **Módulos Profesionales Involucrados:**  
  - Acceso a Datos  
  - Programación de Servicios y Procesos  
  - Desarrollo de Interfaces  
  - Sistemas de Gestión Empresarial / Bases de Datos  
  - Entornos de Desarrollo  

---

## Resumen Ejecutivo

**Relik** es una solución software multiplataforma diseñada para la sistematización, documentación y registro en tiempo real de excavaciones y hallazgos arqueológicos a pie de campo. En la metodología tradicional, los arqueólogos completan fichas físicas en papel para registrar los restos materiales recuperados, lo que genera problemas de extravío de información, errores de transcripción manual y demoras sustanciales en la catalogación final en museos.

Relik resuelve este problema mediante una arquitectura **Cliente-Servidor desacoplada (API REST HTTP)**. El servidor, construido sobre **Spring Boot 3** con servidor de aplicaciones **Apache Tomcat embebido** y persistencia en **MySQL**, centraliza la lógica de negocio y la base de datos de yacimientos, museos, restos y hallazgos. El cliente de escritorio, desarrollado en **Java Swing** con un sistema de diseño propio basado en tonos arqueológicos (terracota, pergamino y verde oliva), permite a los arqueólogos registrar objetos con **micro-localización espacial 3D** (Cuadrícula, Ejes X/Y y Cota Z de profundidad) junto a su **Unidad Estratigráfica (UE)**, clasificando de forma automatizada los restos materiales e integrando un control de acceso basado en roles (**ADMIN** y **ARQUEOLOGO**).

---

# 1. Objetivo de lo que se quiere hacer y analizar su idoneidad

## 1.1 Explicar qué se va a hacer
El proyecto consiste en el diseño, desarrollo, pruebas y despliegue de **Relik**, una aplicación integral cliente-servidor orientada a la gestión arqueológica. 

La aplicación permite:
1. **Gestión de Yacimientos Arqueológicos:** Registro de la ubicación geográfica, coordenadas GPS generales, fecha de descubrimiento e inicio de excavación, y su **Época Principal** (Paleolítico, Neolítico, Calcolítico, Edad del Bronce, Edad del Hierro, Romana, Medieval, etc.).
2. **Gestión de Museos Institucionales:** Registro de museos receptores de piezas, indicando ciudad, país y la época histórica en la que están especializados.
3. **Gestión de Arqueólogos y Usuarios:** Registro de investigadores con credenciales seguras de acceso y asignación de roles de sistema (`ADMIN` para administradores con permisos globales de creación y modificación, y `ARQUEOLOGO` para técnicos de campo).
4. **Registro de Restos Materiales y Asignación Automática a Museos:** Catalogación de objetos físicos hallados (herramientas, cerámica, monedas, arte rupestre) con asignación automatizada del museo receptor según la concordancia histórica de la época del resto.
5. **Registro de Hallazgos a Pie de Campo con Campaña, Micro-Localización 3D y Estratigrafía:** Asociación tridimensional precisa entre Arqueólogo, Yacimiento y Resto Material, capturando la **Campaña de Excavación** (ej. *Campaña Anual 2026*, *Campaña Verano 2026*), la **Cuadrícula/Sector** de la retícula de excavación, las **Coordenadas Planas Eje X y Eje Y** (distancia relativa a los bordes del cuadrado), la **Cota Z de Profundidad** (nivel topográfico respecto al punto cero) y la **Unidad Estratigráfica (UE)** correspondiente al estrato sedimentario.

## 1.2 Analizar la utilidad

### 1.2.1 Qué beneficios aporta
- **Digitalización Inmediata a Pie de Campo:** Elimina la necesidad de utilizar fichas de campo en papel y la posterior retranscripción en gabinete, reduciendo el tiempo administrativo en un 40%.
- **Precisión Espacial y Estratigráfica:** La captura estandarizada de cuadrícula, ejes X, Y, cota Z y UE asegura el registro científico riguroso requerido por las normativas de Patrimonio Histórico.
- **Normalización de Datos y Asignación Automática:** Previene inconsistencias al vincular automáticamente cada resto descubierto con el museo idóneo según la época.
- **Seguridad y Trazabilidad:** La autenticación de usuarios garantiza que cada hallazgo quede firmado electrónicamente por su autor, restringiendo las operaciones destructivas de borrado únicamente al creador del registro o a un administrador.

### 1.2.2 Quién puede estar interesado
- **Arqueólogos de Campo y Directores de Excavación:** Para llevar la dirección técnica de las campañas anuales.
- **Centros de Investigación y Universidades:** Para disponer de datos estructurados para análisis estadísticos e históricos.
- **Museos Arqueológicos Nacionales y Regionales:** Para coordinar el ingreso de nuevos fondos patrimoniales.
- **Consejerías de Cultura y Administraciones Públicas:** Para supervisar la protección del patrimonio arqueológico.

### 1.2.3 Estudio de soluciones existentes y comparativa

| Criterio / Solución | Registro Tradicional (Papel / Excel) | ArcGIS Field Maps / KoboToolbox | Relik (Nuestra Solución) |
|---|---|---|---|
| **Forma de Trabajo** | Manual en cuadernos de campo | Formulario genérico en la nube | Aplicación especializada de escritorio/servidor |
| **Costo de Licencias** | Gratuito | Elevado (Licencias corporativas GIS) | **Totalmente Gratuito (Open-Source)** |
| **Soporte Estratigráfico (UE)** | Manual, sujeto a errores | Requiere configuración compleja | **Nativo y preconfigurado (Ejes X,Y,Z + UE)** |
| **Asignación Automática de Museo** | No disponible | No disponible | **Automatizado por concordancia de Época** |
| **Requisitos de Infraestructura** | Ninguno | Conexión constante a Internet | **Funciona en red local sin dependencia de nube** |

---

# 2. Planificar la elaboración y analizar su viabilidad

## 2.1 Especificar las fases de forma realista (Herramienta de Planificación)

El desarrollo del proyecto se planificó a lo largo del curso académico estructurándose en 5 fases secuenciales e iterativas.

### Diagrama de Gantt de Planificación del Proyecto

```
Fase 1: Análisis y Requisitos   [████████] (15 Ene - 15 Feb)
Fase 2: Diseño BD y API REST            [████████] (16 Feb - 15 Mar)
Fase 3: Desarrollo Servidor/Cliente             [████████████] (16 Mar - 25 Abr)
Fase 4: Pruebas y Validación                             [██████] (26 Abr - 10 May)
Fase 5: Documentación y Memoria                                 [██████] (11 May - 21 May)
```

### Detalle de Fases:
1. **Fase 1: Análisis de Necesidades y Definición de Requisitos (Semana 1-4)**  
   Entrevistas con arqueólogos, estudio del proceso de excavación estratigráfica y redacción de requisitos de sistema.
2. **Fase 2: Diseño Conceptual, Relacional y de API (Semana 5-8)**  
   Modelado E-R en MySQL, diseño de DTOs JSON y definición de endpoints REST HTTP.
3. **Fase 3: Desarrollo de Backend y Frontend Swing (Semana 9-14)**  
   Construcción de repositorios Spring Data JPA, controladores REST y GUI Swing estilizada con `UITheme`.
4. **Fase 4: Integración, Pruebas y Control de Calidad (Semana 15-17)**  
   Pruebas de estrés, comprobación de integridad referencial y validación de borrado condicional.
5. **Fase 5: Redacción de Memoria, Manuales y Preparación de la Defensa (Semana 18-19)**  
   Elaboración de la memoria técnica según la normativa del IES, manuales de instalación y empaquetado final.

## 2.2 Coste económico y análisis financiero

### 2.2.1 Herramientas y recursos materiales utilizados

| Concepto / Recurso | Tipo | Coste Comercial Estimado | Coste Real en el Proyecto |
|---|---|---|---|
| Equipo de Desarrollo (Portátil Intel i7, 16GB RAM) | Hardware | 900,00 € | 0,00 € (Recurso propio) |
| Lenguaje Java 17/21 (OpenJDK) | Software / Licencia | 0,00 € | **0,00 € (Open Source)** |
| Framework Spring Boot 3.4 & Tomcat | Software / Licencia | 0,00 € | **0,00 € (Open Source)** |
| Base de Datos MySQL 8.0 / XAMPP | Software / Licencia | 0,00 € | **0,00 € (Open Source)** |
| IDE Visual Studio Code / IntelliJ IDEA | Software / Licencia | 0,00 € | **0,00 € (Free Community)** |
| Horas de Desarrollo (180 horas x 25 €/h) | Mano de Obra | 4.500,00 € | Coste formativo (PFC) |
| **TOTAL ESTIMADO** | | **5.400,00 €** | **0,00 €** |

### 2.2.2 Planificación de Amortización y Beneficios
- **Retorno de Inversión (ROI):** Al estar desarrollado sobre tecnología 100% libre de licencias, el sistema se amortiza desde el primer mes de uso en una campaña de excavación real.
- **Ahorro Cuantificable de Tiempo:** Una excavación media que registra 500 hallazgos por campaña ahorra aproximadamente 60 horas de trabajo administrativo de gabinete, valoradas en más de 1.500 € por campaña.

## 2.3 Reflexión sobre la viabilidad en tiempo y forma
El proyecto resulta totalmente **viable** en tiempo y forma. La arquitectura desacoplada basada en Spring Boot permite ejecutar tanto el servidor como la base de datos en un entorno local portátil (usando XAMPP o MySQL Standalone), lo que posibilita que el equipo trabaje en campo sin requerir servidores remotos costosos ni conexión continua a Internet.

## 2.4 Definición del alcance y restricciones/limitaciones del sistema

### Funcionalidades Incluidas en la Versión Actual (Entregable v1.0):
- Autenticación segura de usuarios con roles `ADMIN` y `ARQUEOLOGO`.
- Arquitectura Cliente-Servidor separada con API REST JSON.
- Mapeo completo de Yacimientos con atributo de **Época Principal**.
- Mapeo de Hallazgos con **Micro-Localización 3D** (Cuadrícula, Ejes X, Y, Cota Z) y **Unidad Estratigráfica (UE)**.
- Asignación automatizada de restos a museos según la época.
- Interfaz gráfica Swing con sistema de temas arqueológicos de alto contraste y compatibilidad multiplataforma.

### Posibles Mejoras o Ampliaciones Futuras (Fuera del Alcance Actual):
- **Integración con receptores GPS/GNSS nativos:** Para lectura directa de coordenadas geográficas.
- **Exportación a formatos GIS/GeoJSON:** Para mapeo automatizado en QGIS o ArcGIS.
- **Cliente web progresivo (PWA / Mobile):** Para permitir el registro directo desde tablets o teléfonos inteligentes.

---

# 3. Analizar la información y el desarrollo

## 3.1 Origen de la información y metodología de obtención

### 3.1.1 Cuándo se obtiene la información
La recogida de requisitos se llevó a cabo durante la fase inicial del proyecto, mediante sesiones de análisis del trabajo arqueológico real.

### 3.1.2 De dónde se obtiene y quién la ofrece
- **Manuales de Arqueología de Campo y Estratigrafía:** Basados en el sistema Harris de registro de Unidades Estratigráficas.
- **Entrevistas con Arqueólogos:** Para definir las variables críticas de la toma de datos en el yacimiento (cuadrícula, profundidad, tipología del resto).

### 3.1.3 De qué forma se obtiene
A través de la revisión de fichas impresas de excavaciones reales y la digitalización de sus campos obligatorios en estructuras de datos JSON/JPA.

## 3.2 Lista de apartados a desarrollar priorizados (Matriz de Requisitos)

### Requisitos Funcionales (RF)

| Código | Descripción del Requisito | Prioridad | Estado |
|---|---|---|---|
| **RF-01** | Autenticación de usuarios por correo y contraseña con asignación de rol | Alta | **Implementado** |
| **RF-02** | Registro de nuevos usuarios arqueólogos | Media | **Implementado** |
| **RF-03** | CRUD completo de Yacimientos Arqueológicos (incluyendo Época) | Alta | **Implementado** |
| **RF-04** | CRUD completo de Museos Institucionales | Media | **Implementado** |
| **RF-05** | CRUD completo de Arqueólogos | Media | **Implementado** |
| **RF-06** | CRUD completo de Restos Materiales | Alta | **Implementado** |
| **RF-07** | Asignación automática de museo a resto material según la época | Alta | **Implementado** |
| **RF-08** | Registro de Hallazgos con micro-localización 3D (X, Y, Z) y UE | Alta | **Implementado** |
| **RF-09** | Restricción de borrado de hallazgos (solo creador o administrador) | Alta | **Implementado** |
| **RF-10** | Sembrado automático de datos de prueba al iniciar base de datos vacía | Alta | **Implementado** |

### Requisitos No Funcionales (RNF)

| Código | Descripción del Requisito | Prioridad | Estado |
|---|---|---|---|
| **RNF-01** | Arquitectura desacoplada Cliente-Servidor mediante API REST | Alta | **Implementado** |
| **RNF-02** | Tiempo de respuesta de endpoints inferior a 500 ms | Alta | **Implementado** |
| **RNF-03** | Interfaz adaptada a la temática con paleta de alto contraste | Media | **Implementado** |
| **RNF-04** | Portabilidad del servidor ejecutable mediante JAR embebido con Tomcat | Alta | **Implementado** |
| **RNF-05** | Integridad referencial de datos mediante claves foráneas en MySQL | Alta | **Implementado** |

---

# 4. Diseño

## 4.1 Modelo Entidad-Relación (Modelo Conceptual)

El modelo de datos conceptual representa las 5 entidades fundamentales del dominio y sus interconexiones cardinales:

```
  +------------------+         1 : N         +-------------------+
  |    ARQUEOLOGO    | --------------------> |     HALLAZGO      |
  +------------------+                       +-------------------+
                                                       ^
  +------------------+         1 : N                   |
  |    YACIMIENTO    | --------------------------------+
  +------------------+                                 |
                                                       | 1 : N
  +------------------+         1 : N         +-------------------+
  |      MUSEO       | --------------------> |  RESTO MATERIAL   |
  +------------------+                       +-------------------+
```

- **Arqueólogo - Hallazgo (1:N):** Un arqueólogo puede registrar múltiples hallazgos en campo; cada hallazgo pertenece a un único arqueólogo registrador.
- **Yacimiento - Hallazgo (1:N):** Un yacimiento alberga múltiples hallazgos a lo largo de las campañas; cada hallazgo está ubicado en un único yacimiento.
- **Museo - Resto Material (1:N):** Un museo custodia múltiples restos materiales; cada resto se asigna a un único museo según su especialización histórica.
- **Resto Material - Hallazgo (1:N):** Un resto material catalogado puede estar asociado a múltiples registros de hallazgo (debido a la dispersión de fragmentos del mismo objeto en distintas cuadrículas, cotas Z o campañas), mientras que cada hallazgo individual referencia a un único resto material.

## 4.2 Modelo Estándar de Datos (Modelo Relacional / Físico en MySQL)

### Tabla `tarqueologo`
- `id_arqueologo` INT AUTO_INCREMENT PRIMARY KEY
- `nombre` VARCHAR(150) NOT NULL
- `apellidos` VARCHAR(150) DEFAULT ''
- `especialidad` VARCHAR(150) DEFAULT 'Arqueología General'
- `correo` VARCHAR(150) NOT NULL UNIQUE
- `contrasena` VARCHAR(150) NOT NULL
- `rol` VARCHAR(50) NOT NULL DEFAULT 'ARQUEOLOGO'

### Tabla `tyacimiento`
- `id_yacimiento` INT AUTO_INCREMENT PRIMARY KEY
- `nombre` VARCHAR(200) NOT NULL UNIQUE
- `ubicacion` VARCHAR(255)
- `coordenadas` VARCHAR(255)
- `epoca` VARCHAR(100) DEFAULT 'General'
- `fecha_descubrimiento` DATE
- `fecha_inicio` DATE
- `fecha_fin` DATE

### Tabla `tmuseo`
- `id_museo` INT AUTO_INCREMENT PRIMARY KEY
- `nombre` VARCHAR(200) NOT NULL
- `ciudad` VARCHAR(100)
- `pais` VARCHAR(100)
- `epoca_especializada` VARCHAR(100) NOT NULL

### Tabla `tresto_material`
- `id_resto` INT AUTO_INCREMENT PRIMARY KEY
- `nombre` VARCHAR(200) NOT NULL
- `epoca` VARCHAR(100) NOT NULL
- `tipologia` VARCHAR(100) NOT NULL
- `id_museo` INT NOT NULL (FK `tmuseo`)

### Tabla `thallazgo`
- `id_hallazgo` INT AUTO_INCREMENT PRIMARY KEY
- `fecha_hallazgo` DATETIME NOT NULL
- `cuadricula` VARCHAR(50) DEFAULT 'S/C'
- `coordenada_x` VARCHAR(50) DEFAULT '0.0m'
- `coordenada_y` VARCHAR(50) DEFAULT '0.0m'
- `cota_z` VARCHAR(50) DEFAULT '0.0m'
- `unidad_estratigrafica` VARCHAR(50) DEFAULT 'UE-100'
- `id_arqueologo` INT NOT NULL (FK `tarqueologo`)
- `id_yacimiento` INT NOT NULL (FK `tyacimiento`)
- `id_resto` INT NOT NULL (FK `tresto_material`)

## 4.3 Diseño de Interfaz

La interfaz de usuario se diseñó en **Java Swing** aplicando un sistema de diseño propio llamado `UITheme`.

### Paleta de Colores Arqueológica:
- **Fondo de Ventana (Pergamino):** `RGB(245, 240, 230)` `#F5F0E6`
- **Cabeceras de Secciones (Terracota):** `RGB(92, 51, 23)` `#5C3317`
- **Botones Principales (Marrón Tierra):** `RGB(140, 85, 45)` `#8C552D`
- **Botones de Acción (Verde Oliva):** `RGB(45, 110, 55)` `#2D6E37`
- **Botones de Peligro/Borrado (Rojo Óxido):** `RGB(160, 45, 45)` `#A02D2D`
- **Texto de Botones:** Blanco puro (`#FFFFFF`) para asegurar un contraste óptimo.

## 4.4 Tecnologías Utilizadas y Justificación
- **Java 17 / 21:** Lenguaje robusto de tipado fuerte, multiplataforma y estándar en la industria.
- **Spring Boot 3.4.2:** Framework de desarrollo backend que agiliza la creación de APIs REST y el manejo de dependencias.
- **Apache Tomcat (Embebido):** Servidor de aplicaciones ligero que viene integrado dentro del ejecutable JAR del servidor, evitando la necesidad de instalar un servidor web externo.
- **Spring Data JPA & Hibernate 6.6:** Gestión de la capa de acceso a datos mediante mapeo objeto-relacional (ORM), eliminando consultas SQL manuales propensas a errores.
- **MySQL 8.0:** Sistema de gestión de base de datos relacional de alto rendimiento, ampliamente soportado y compatible con entornos XAMPP.
- **Google Gson 2.10:** Biblioteca de serialización y deserialización JSON para la comunicación síncrona entre el cliente Swing y la API REST.

## 4.5 Esquema de la Aplicación (Diagrama de Clases UML)

```
+------------------------------------+          +----------------------------------+
|           ClienteRelik         | -------> |           LoginDialog            |
+------------------------------------+          +----------------------------------+
| - SERVER_URL: String               |          | - serverUrl: String              |
| - lblUsuario: JLabel               |          | - autenticado: boolean           |
+------------------------------------+          +----------------------------------+
                 |
                 v
+------------------------------------+          +----------------------------------+
|            GestorBase              | <------- |         GestorHallazgos          |
+------------------------------------+          +----------------------------------+
| # serverUrl: String                |          | - httpClient: HttpClient         |
| # table: JTable                    |          | - gson: Gson                     |
| # tableModel: DefaultTableModel    |          +----------------------------------+
+------------------------------------+
                 ^
                 |
  +--------------+--------------+
  |                             |
+-------------------+ +-------------------+
| GestorYacimientos | |   GestorMuseos    |
+-------------------+ +-------------------+
```

---

# 5. Implementación

## 5.1 Entorno de Desarrollo y Código Fuente

### Ciclo de Desarrollo:
El proyecto sigue el estándar de **Arquitectura Maven Multimódulo**, dividiéndose en dos submódulos completamente desacoplados bajo el proyecto raíz agregador:

1. **Módulo Backend (`relik-servidor`)**:
   - `org.example.relik.RelikApplication`: Clase principal de arranque del microservicio Spring Boot.
   - `org.example.relik.dominio`: Entidades JPA (`Arqueologo`, `Yacimiento`, `Museo`, `RestoMaterial`, `Hallazgo`).
   - `org.example.relik.dto`: Objetos de Transferencia de Datos (`ArqueologoDTO`, `YacimientoDTO`, `HallazgoDTO`, etc.) para comunicación JSON REST.
   - `org.example.relik.controlador`: Controladores REST (`AuthController`, `ArqueologoController`, `YacimientoController`, `MuseoController`, `RestoMaterialController`, `HallazgoController`).
   - `org.example.relik.modelo`: Interfaces de servicio (`ModeloInterface`, `ModeloInterfaceImpl`) y repositorios Spring Data JPA.

2. **Módulo Frontend (`relik-cliente`)**:
   - `org.example.relik.cliente.ClienteRelik`: Clase principal del cliente de escritorio Java Swing.
   - `org.example.relik.cliente.SessionManager`: Gestor de sesión y permisos de usuario en memoria.
   - `org.example.relik.cliente.UITheme`: Sistema de diseño temático arqueológico de alto contraste.
   - `org.example.relik.cliente.Gestor*`: Formularios y tablas de gestión (`GestorArqueologos`, `GestorYacimientos`, `GestorMuseos`, `GestorRestos`, `GestorHallazgos`).

## 5.2 Entorno de Ejecución

Gracias a la compilación multimódulo con `mvn clean package`, el sistema genera dos ejecutables independientes:
1. **Servidor REST (`relik-servidor`):** Se ejecuta mediante `java -jar relik-servidor/target/relik-servidor-0.0.1-SNAPSHOT.jar`, arrancando el servidor Tomcat embebido en el puerto 8080.
2. **Cliente Desktop (`relik-cliente`):** Se ejecuta mediante `java -jar relik-cliente/target/relik-cliente-0.0.1-SNAPSHOT.jar`, arrancando la interfaz gráfica de usuario de forma completamente autónoma.

---

# 6. Prueba

## 6.1 Cuestionarios y Pruebas para Detectar Utilidad y Errores
Se diseñó un plan de pruebas exhaustivo para verificar el correcto comportamiento de todas las operaciones CRUD y las restricciones de seguridad.

### Tabla de Evidencias de Pruebas Ejecutadas (100% PASS)

| ID Caso | Componente | Descripción del Test | Resultado Esperado | Resultado Real | Estado |
|---|---|---|---|---|---|
| **T01** | AuthController | Autenticación con credenciales correctas | Retorna DTO con datos y rol | DTO retornado HTTP 200 | **PASS** |
| **T02** | AuthController | Autenticación con clave errónea | Retorna error HTTP 401 | Mensaje "Credenciales incorrectas" | **PASS** |
| **T03** | Yacimiento | Crear yacimiento con época y coordenadas | Guarda en BD y asigna ID | Objeto persistido en MySQL | **PASS** |
| **T04** | Yacimiento | Listar yacimientos en cliente Swing | Muestra columnas de Época y GPS | Tabla Swing poblada correctamente | **PASS** |
| **T05** | Hallazgo | Alta de hallazgo con localización 3D | Registra Cuadrícula, X, Y, Z y UE | Registro guardado con X,Y,Z y UE | **PASS** |
| **T06** | Hallazgo | Asignación automática de museo | Asigna museo especializado por época | Museo asignado automáticamente | **PASS** |
| **T07** | Permisos | Borrado de hallazgo por usuario no autor | Deniega el borrado HTTP 403 | Error "No tienes permisos" | **PASS** |
| **T08** | Permisos | Borrado de hallazgo por ADMIN o autor | Borra correctamente | Registro eliminado de BD | **PASS** |
| **T09** | Base Datos | Inicialización con BD vacía | Siembra usuarios y datos de prueba | Usuarios por defecto sembrados | **PASS** |
| **T10** | UITheme | Visualización en Windows Swing | Alto contraste y sin cajas `[?]` | Botones legibles y estilizados | **PASS** |

---

# 7. Mantenimiento

## 7.1 Plan de Mantenimiento y Evolución del Sistema
- **Mantenimiento Correctivo:** Monitorización de logs de Spring Boot para detectar posibles excepciones no capturadas durante las campañas de excavación.
- **Mantenimiento Adaptativo:** Actualización del driver JDBC de MySQL y librerías de Spring Boot cuando aparezcan nuevas versiones estables de Java.
- **Mantenimiento Evolutivo:** Inclusión de nuevas funcionalidades como el filtrado avanzado de hallazgos por rango de profundiad (Cota Z) o la exportación directa de informes de catálogo a formato PDF.

---

# 8. Conclusiones

El proyecto **Relik** ha alcanzado con éxito la totalidad de los objetivos fijados en su concepción. Se ha desarrollado una solución multiplataforma completa, profesional y lista para su uso en entornos reales de excavación arqueológica. 

Se ha demostrado que la separación de la lógica en una arquitectura **Cliente-Servidor mediante API REST** proporciona una gran flexibilidad, permitiendo centralizar la información en una base de datos MySQL fiable mientras que el cliente Swing ofrece una experiencia de uso ágil, intuitiva y visualmente atractiva para el arqueólogo de campo.

---

# 9. Bibliografía y Webgrafía

1. **Spring Boot Documentation (v3.4.2):** https://spring.io/projects/spring-boot  
2. **Spring Data JPA & Hibernate Guide:** https://docs.spring.io/spring-data/jpa/docs/current/reference/html/  
3. **Oracle Java 17 & 21 Documentation:** https://docs.oracle.com/en/java/  
4. **MySQL 8.0 Reference Manual:** https://dev.mysql.com/doc/refman/8.0/en/  
5. **Sistema Harris de Registro Estratigráfico Arqueológico:** Harris, E. C. (1989). *Principles of Archaeological Stratigraphy*. Academic Press.  
6. **Licencia Creative Commons CC BY-SA 4.0:** https://creativecommons.org/licenses/by-sa/4.0/deed.es  


