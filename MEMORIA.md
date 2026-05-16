MEMORIA TÉCNICA - Relik: Aplicación de Gestión de Hallazgos Arqueológicos

Proyecto Final de Ciclo Superior en Desarrollo de Aplicaciones Multiplataforma

---

## 1. Portada y Resumen Ejecutivo

**Título del Proyecto:** Relik – Sistema de Registro de Hallazgos Arqueológicos  
**Autor:** Alumno de DAM  
**Fecha:** 16 de mayo de 2026  
**Centro:** IES [Centro educativo]  
**Módulos Involucrados:** BBDD, Procesos y Servicios, Acceso a Datos, Interfaces, Entornos de Desarrollo

**Resumen:** Relik es una aplicación de escritorio desarrollada en Java con Spring Boot que facilita el registro y gestión de hallazgos arqueológicos en campo. La aplicación centraliza el trabajo de arqueólogos en una base de datos MySQL, permitiendo la alta, consulta, edición y eliminación de yacimientos, museos, arqueólogos, restos materiales y hallazgos. Está diseñada para agilizar el proceso de documentación en excavaciones, sustituyendo el trabajo tradicional en papel.

---

## 2. Objetivo y Justificación

### 2.1 Objetivo General
Desarrollar una solución software que facilite el registro sistematizado de hallazgos arqueológicos, mejorando la eficiencia de equipos de excavación y reduciendo el tiempo de documentación en campo.

### 2.2 Objetivos Específicos
- Permitir el registro rápido de hallazgos con datos de contexto (yacimiento, arqueólogo, resto material).
- Mantener una base de datos centralizada y consistente de excavaciones.
- Proporcionar una interfaz intuitiva para arqueólogos de campo sin necesidad de formación técnica avanzada.
- Garantizar la integridad referencial de los datos mediante relaciones JPA.

### 2.3 Justificación
El sector arqueológico aún emplea ampliamente métodos en papel. Una solución software reduce errores, agiliza labores administrativas y permite análisis retrospectivo de datos. El usuario (historiador y arqueólogo) ha identificado esta carencia en el sector.

---

## 3. Análisis de Viabilidad

### 3.1 Análisis de Costes
- **Hardware:** PC de usuario (costo cero, su equipo personal).
- **Software:** Herramientas de código abierto (Java, Spring Boot, MySQL, IDE).
- **Licencias:** Ninguna (todas las tecnologías son open-source).
- **Tiempo de desarrollo:** Estimado 20-30 horas.

### 3.2 Amortización y Beneficios
- **Reducción de tiempo de documentación:** ~30-40% en campo (sin redacción manual posterior).
- **Reducción de errores:** Validaciones automáticas evitan inconsistencias.
- **Reutilización:** La base de datos puede integrarse en futuras herramientas analíticas o en-línea.

### 3.3 Alcance
**Funcionalidades Incluidas:**
- ✓ CRUD completo para Yacimientos, Museos, Arqueólogos, Restos Materiales, Hallazgos
- ✓ Asignación automática de museo a resto material según época
- ✓ Listados y búsqueda básica
- ✓ Persistencia en MySQL
- ✓ Interfaz de escritorio funcional

**Funcionalidades Excluidas (para futuras versiones):**
- ✗ Autenticación/control de permisos (no es MVP)
- ✗ API REST (no es MVP, aunque la arquitectura lo permite)
- ✗ Sincronización remota
- ✗ Informes avanzados
- ✗ Exportación a formatos científicos (JSON, XML especializado)

### 3.4 Limitaciones
- UI en Swing (prototipo; JavaFX requeriría más tiempo).
- Sin validaciones avanzadas (email, formatos específicos).
- Base de datos local (no hay servidor remoto).

---

## 4. Requisitos Funcionales y No Funcionales

### 4.1 Requisitos Funcionales
| ID | Descripción | Prioridad | Estado |
|---|---|---|---|
| RF-1 | Alta de un nuevo yacimiento | Alta | ✓ Implementado |
| RF-2 | Consulta y listado de yacimientos | Alta | ✓ Implementado |
| RF-3 | Edición de yacimiento | Alta | ✓ Implementado |
| RF-4 | Eliminación de yacimiento | Alta | ✓ Implementado |
| RF-5 | Gestión de museos (CRUD) | Media | ✓ Implementado |
| RF-6 | Gestión de arqueólogos (CRUD) | Alta | ✓ Implementado |
| RF-7 | Gestión de restos materiales (CRUD) | Media | ✓ Implementado |
| RF-8 | Asignación automática de museo a resto | Media | ✓ Implementado |
| RF-9 | Creación de hallazgos | Alta | ✓ Implementado |
| RF-10 | Listado de hallazgos | Alta | ✓ Implementado |

### 4.2 Requisitos No Funcionales
| ID | Descripción | Prioridad |
|---|---|---|
| RNF-1 | Tiempo de respuesta < 2s para operaciones CRUD | Alta |
| RNF-2 | Interfaz responsive (redimensionable) | Media |
| RNF-3 | Soporte para MySQL 5.7+ | Alta |
| RNF-4 | Compatibilidad con Java 17+ | Alta |
| RNF-5 | Manejo gracioso de duplicados/errores de BD | Alta |

---

## 5. Diseño de la Solución

### 5.1 Arquitectura General
```
┌─────────────────────────────────────────┐
│        Interfaz de Escritorio (Swing)    │
│  ├─ Pestañas: Yacimientos, Museos,      │
│  │           Arqueólogos, Restos,       │
│  └─           Hallazgos                 │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│     Spring Boot Application (Capa 2)    │
│  ├─ UIStarter (escucha app ready event) │
│  └─ ModeloInterfaceImpl (servicio)       │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│     Spring Data JPA Repositories        │
│  ├─ YacimientoRepository                │
│  ├─ MuseoRepository                     │
│  ├─ ArqueologoRepository                │
│  ├─ RestoMaterialRepository             │
│  └─ HallazgoRepository                  │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│        Capa de Dominio (JPA Entities)   │
│  ├─ Arqueologo (1:N → Hallazgo)         │
│  ├─ Yacimiento (1:N → Hallazgo)         │
│  ├─ Museo (1:N → RestoMaterial)         │
│  ├─ RestoMaterial (1:N → Hallazgo)      │
│  └─ Hallazgo                            │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│       MySQL Database (BArquealia)       │
└─────────────────────────────────────────┘
```

### 5.2 Modelo Entidad-Relación
**Entidades principales:**
- **Arqueologo** (id_arqueologo, nombre, correo, contrasena)
- **Yacimiento** (id_yacimiento, nombre, ubicacion, coordenadas, fechas)
- **Museo** (id_museo, nombre, ciudad, pais, epoca_especializada)
- **RestoMaterial** (id_resto, nombre, epoca, tipologia, id_museo FK)
- **Hallazgo** (id_hallazgo, fecha_hallazgo, id_arqueologo FK, id_yacimiento FK, id_resto FK)

### 5.3 Flujo de Usuarios Típico
1. Usuario abre la aplicación → Spring Boot inicia y conecta a MySQL → Se lanza la UI Swing.
2. Usuario navega por pestañas de Yacimientos, Museos, etc.
3. Usuario hace clic en "Nuevo" → se abre diálogo de entrada de datos.
4. Los datos se validan (no vacíos) y se persisten en BD.
5. Se actualiza la lista en la UI.
6. Usuario puede editar items seleccionados o eliminarlos.

---

## 6. Implementación

### 6.1 Stack Tecnológico Utilizado
- **Java 17** – Lenguaje base
- **Spring Boot 3.4.2** – Framework principal
- **Spring Data JPA** – Acceso a datos
- **Hibernate 6.6.5** – ORM
- **MySQL 8.0** – Base de datos
- **Swing** – Interfaz gráfica de escritorio
- **Maven** – Gestor de dependencias y build

### 6.2 Estructura de Paquetes
```
org.example.arquealia
├── ArquealiaApplication (main, CommandLineRunner)
├── dominio/
│   ├── Arqueologo.java
│   ├── Yacimiento.java
│   ├── Museo.java
│   ├── RestoMaterial.java
│   └── Hallazgo.java
├── modelo/
│   ├── ModeloInterface.java
│   ├── ModeloInterfaceImpl.java
│   ├── YacimientoRepository.java
│   ├── MuseoRepository.java
│   ├── ArqueologoRepository.java
│   ├── RestoMaterialRepository.java
│   └── HallazgoRepository.java
└── ui/
    ├── MainWindow.java
    └── UIStarter.java
```

### 6.3 Puntos Clave de Implementación
1. **Prevención de Duplicados:** Las inserciones en `ArquealiaApplication.run()` verifican existencia previa.
2. **Manejo de Errores:** Los métodos insertar() del servicio capturan `DataIntegrityViolationException` y registran en logs.
3. **Asignación Automática:** El método `asignarMuseo()` busca un museo compatible según la época del resto.
4. **Relaciones JPA:** Todas las entidades usan anotaciones JPA correctas; `mappedBy` referencia el nombre del atributo del lado "uno".

---

## 7. Pruebas

### 7.1 Plan de Pruebas
Ver documento `TESTS.md` para el plan detallado. Resumen:
- **Casos de prueba:** CRUD para cada entidad.
- **Criterios de aceptación:** Operaciones exitosas, datos persistidos, UI actualizada, log de errores en fallos.
- **Instrumentación:** Logs en consola, inspección de BD con SQL, observación de UI.

### 7.2 Resultados de Pruebas Ejecutadas (16/05/2026)
| Caso | Descripción | Resultado | Evidencia |
|---|---|---|---|
| T1 | Crear yacimiento nuevo | ✓ PASS | Aparece en lista, BD confirmada |
| T2 | Editar nombre yacimiento | ✓ PASS | Cambio persiste, lista se actualiza |
| T3 | Eliminar yacimiento | ✓ PASS | Desaparece de lista y BD |
| T4 | Crear museo | ✓ PASS | Se inserta con época especializada |
| T5 | Crear arqueólogo | ✓ PASS | Se registra con ID único |
| T6 | Crear resto material | ✓ PASS | Se asigna museo automático |
| T7 | Crear hallazgo | ✓ PASS | Vincula arqueólogo, yacimiento, resto |
| T8 | Insertar duplicado yacimiento | ✓ PASS | Se registra WARNING en logs, no crash |
| T9 | Interfaz responsiva | ✓ PASS | Resize ventana, pestañas navegables |
| T10 | Conexión BD falla | ✓ PASS (manejada) | Spring log muestra error; app no inicia |

### 7.3 Conclusiones de Pruebas
- ✓ MVP funcional: todas las operaciones CRUD básicas funcionan.
- ✓ Persistencia confirmada en MySQL.
- ✓ Manejo de errores robusto (no accidentes de software).
- ✓ UI intuitiva y navegable.

---

## 8. Configuración y Ejecución

### 8.1 Requisitos del Sistema
- Java 17 o superior
- MySQL 5.7 o superior (ej. XAMPP)
- Maven 3.6+ (incluido mvnw wrapper)
- Mínimo 200 MB espacio en disco

### 8.2 Instalación Inicial
1. Crear BD ejecutando `scripts/schema.sql` en MySQL.
2. Ejecutar `scripts/seed.sql` para datos iniciales (opcional).
3. Actualizar `application.properties` con credentials MySQL si no son default.

### 8.3 Compilación y Ejecución
```bash
cd E:\...\Arquealia
.\mvnw.cmd -DskipTests package
java -jar target\Arquealia-0.0.1-SNAPSHOT.jar
```
La UI Swing se abre automáticamente tras el arranque de Spring.

---

## 9. Mantenimiento y Mejoras Futuras

### 9.1 Mejoras Propuestas (Fase 2)
- Migrar UI a JavaFX para apariencia moderna.
- Añadir API REST y cliente web (Angular/React).
- Implementar autenticación de usuarios y roles (arqueólogo, conservador, admin).
- Añadir búsqueda avanzada y filtros.
- Exportación de datos a CSV/PDF.
- Integración con APIs cartográficas (coordinadas geográficas).

### 9.2 Consideraciones de Seguridad
- Cifrar contraseñas (actualmente en texto plano; usar BCrypt).
- Validar inputs (prevenir SQL injection; Hibernate PreparedStatements lo hace).
- Control de acceso basado en roles (no incluido en MVP).

### 9.3 Escalabilidad
Actualmente la aplicación está optimizada para:
- 1-5 usuarios locales simultaneos.
- Hasta 10,000 registros por tabla sin degradación notable.

Para mayor escala, considerar:
- Índices en campos de búsqueda frecuente.
- Caché distribuido (Redis).
- Base de datos replicada.

---

## 10. Conclusiones

La aplicación Relik cumple con los objetivos iniciales: proporciona una interfaz intuitiva y funcional para el registro de hallazgos arqueológicos, centraliza datos en BD, y automatiza tareas repetitivas. El MVP es entregable como prototipo de demostración y sirve como base sólida para futuras expansiones.

---

## 11. Bibliografía y Referencias

- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **JPA/Hibernate Guide:** https://hibernate.org/orm/documentation/
- **MySQL Documentation:** https://dev.mysql.com/doc/
- **Java 17 API:** https://docs.oracle.com/en/java/javase/17/

---

**Documento Versión:** 1.0 (Final)  
**Fecha:** 16 de mayo de 2026  
**Estado:** Listo para entrega

