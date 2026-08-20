# CHECKLIST DE ENTREGA FINAL - Relik v1.0

**Proyecto Fin de Ciclo (PFC) - Desarrollo de Aplicaciones Multiplataforma (DAM)**  
**Centro Educativo:** IES José Luis Martínez Palomo  
**Curso Académico:** 2024 / 2025  

---

## 📅 Hitos de Seguimiento del Tutor (Cumplimiento de Entregas)

| Hito / Entrega | Fecha Oficial | Canal | Estado | Observaciones |
|---|---|---|---|---|
| **Primer Seguimiento** | 15 de marzo | Plataforma | ✅ **Completado** | Memoria (Objetivo, Gantt, Viabilidad) y Git inicial con prototipo navegeable |
| **Segundo Seguimiento** | 25 de abril | Plataforma | ✅ **Completado** | Diseño completo, MVP funcional con persistencia en BD y Plan de pruebas inicial |
| **Borrador Final** | 10 de mayo | Entrega al Tutor | ✅ **Completado** | Memoria completa con todos los apartados redactados y paquete de entrega preliminar |
| **Entrega Final** | 21 de mayo | Plataforma | ✅ **COMPLETADA** | Entrega digital empaquetada, ejecutable JAR, memoria pulida y evidencias 100% PASS |

---

## ✅ VERIFICACIÓN DE CONTENIDOS Y ENTREGABLES

### 1. Memoria Técnica (`MEMORIA.md`)
- [x] Ajustada a los 9 epígrafes normativos del IES José Luis Martínez Palomo.
- [x] Licencia **Creative Commons CC BY-SA 4.0** especificada.
- [x] Estudio comparativo de soluciones similares (*ArcGIS*, *KoboToolbox* vs *Relik*).
- [x] Diagrama de Gantt y Análisis de Costes / Viabilidad Económica.
- [x] Matriz de Requisitos Funcionales (RF) y No Funcionales (RNF).
- [x] Modelo Entidad-Relación y Modelo Relacional Físico en MySQL (5 tablas).
- [x] Justificación de arquitectura Cliente-Servidor REST (Spring Boot 3 + Tomcat Embebido 8080 + MySQL 8.0).
- [x] Sistema de diseño UI Swing (`UITheme`) con paleta de color de alto contraste arqueológico.
- [x] Mapeo de **Micro-Localización 3D** (Cuadrícula, Ejes X, Y, Cota Z de profundidad) y **Unidades Estratigráficas (UE)**.
- [x] Control de permisos y roles (`ADMIN` vs `ARQUEOLOGO`).

### 2. Base de Datos y Scripts (`scripts/`)
- [x] `scripts/schema.sql`: Creación de la base de datos `BRelik` y definición DDL de las 5 tablas con restricciones de clave foránea e índices únicos.
- [x] `scripts/seed.sql`: Datos de prueba iniciales ricos para arqueólogos, yacimientos con época, museos, restos y hallazgos 3D.
- [x] Auto-sembrado automático implementado en el backend si se inicia con base de datos vacía.

### 3. Código Fuente y Arquitectura (Maven Multimódulo)
- [x] Estructura Maven Multimódulo limpia con `relik-servidor` y `relik-cliente` bajo `org.example.relik`.
- [x] Entidades JPA (`Arqueologo`, `Yacimiento`, `Museo`, `RestoMaterial`, `Hallazgo`) anotadas correctamente.
- [x] Repositorios Spring Data JPA para cada entidad.
- [x] DTOs de transferencia JSON (`ArqueologoDTO`, `YacimientoDTO`, `HallazgoDTO`, etc.) para independencia del modelo de red.
- [x] Controladores REST (`AuthController`, `ArqueologoController`, `YacimientoController`, `MuseoController`, `RestoMaterialController`, `HallazgoController`).
- [x] Cliente gráfico Swing (`ClienteRelik`, `LoginDialog`, `GestorHallazgos`, `GestorYacimientos`, etc.) con soporte para Resto Inédito y Remontaje.

### 4. Empaquetado y Pruebas
- [x] `pom.xml` agregador con módulos `relik-servidor` y `relik-cliente`.
- [x] Maven Wrapper (`mvnw.cmd` / `mvnw`) incluido para compilación universal.
- [x] Generación exitosa de los ejecutables autónomos: `relik-servidor-0.0.1-SNAPSHOT.jar` y `relik-cliente-0.0.1-SNAPSHOT.jar` (Reactor Build Success).
- [x] `TESTS.md`: Plan de pruebas completo con 23 casos ejecutados y 100% de éxito (PASS).
- [x] `README.md`: Guía de inicio rápido, arquitectura y credenciales de acceso predeterminadas.

---

## 🎯 ESTADO FINAL
- **Aplicación Entregable:** ✅ SÍ
- **Servidor y BD Funcionales:** ✅ SÍ
- **Memoria Conforme a Normativa:** ✅ SÍ
- **Pruebas Superadas (100% PASS):** ✅ SÍ

**Estado:** ✅ **LISTO PARA PRESENTACIÓN Y DEFENSA ANTE EL TRIBUNAL**


