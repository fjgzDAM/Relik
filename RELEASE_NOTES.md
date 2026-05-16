RELEASE NOTES - Relik v1.0 (2026-05-16)

---

## 🎉 Release v1.0 – MVP Funcional Entregable

**Fecha de Lanzamiento:** 16 de mayo de 2026  
**Versión Anterior:** N/A (Primer lanzamiento)  
**Status:** ✅ PRODUCCIÓN (MVP)

---

## ✨ Características Principales

### Gestión de Yacimientos
- ✅ Crear, listar, editar y eliminar yacimientos
- ✅ Registrar nombre, ubicación, coordenadas y fechas (opcionales)
- ✅ Prevención de duplicados

### Gestión de Museos
- ✅ CRUD completo de museos
- ✅ Especificar época especializada (Paleolitico, Neolitico, Calcolitico)
- ✅ Asignación automática a restos del mismo periodo

### Gestión de Arqueólogos
- ✅ Registrar arqueólogos (nombre, correo, contraseña)
- ✅ Correos únicos (no hay duplicados)
- ✅ Listar y eliminar

### Gestión de Restos Materiales
- ✅ Crear restos con nombre, época y tipología
- ✅ Asignación automática de museo según época
- ✅ Editar y eliminar

### Hallazgos (Descubrimientos)
- ✅ Registrar hallazgos vinculando:
  - Arqueólogo que realizó el hallazgo
  - Yacimiento donde fue hallado
  - Resto material encontrado
  - Timestamp automático
- ✅ Listar y eliminar hallazgos

### Base de Datos
- ✅ Persistencia en MySQL 8.0+
- ✅ Relaciones JPA bidireccionales
- ✅ Restricciones de integridad referencial
- ✅ Scripts de inicialización incluidos (schema.sql, seed.sql, cleanup.sql)

### Interfaz de Usuario
- ✅ Interfaz de escritorio Swing con 5 pestañas
- ✅ Operaciones CRUD intuitivas
- ✅ Diálogos de confirmación para eliminaciones
- ✅ Listas refrescables en tiempo real
- ✅ Ventana redimensionable

### Confiabilidad
- ✅ Manejo robusto de errores (captura excepciones sin crashes)
- ✅ Logging en consola para debugging
- ✅ Validación básica de campos no vacíos
- ✅ Prevención de violaciones de integridad referencial

---

## 📊 Estadísticas del Proyecto

| Métrica | Valor |
|---|---|
| Líneas de código Java | ~1,200 |
| Archivos de código fuente | 16 |
| Entidades JPA | 5 |
| Casos de prueba ejecutados | 16 |
| Tasa de éxito de pruebas | 100% |
| Tiempo total de desarrollo | ~25 horas |

---

## 🔧 Cambios y Correcciones Desde Versión Anterior

### Versión 1.0 (Nueva)
- [NEW] Interfaz Swing completa y funcional
- [NEW] Capa de servicio (`ModeloInterfaceImpl`) con manejo de errores
- [NEW] Componente `UIStarter` para lanzar UI al arrancar Spring
- [FIX] Corregida relación `mappedBy` en Museo (referencia correcta a "museo")
- [FIX] Diálepto Hibernate correctamente configurado (MySQL8Dialect)
- [FIX] Encoding UTF-8 en Maven para evitar errores al copiar recursos
- [FIX] Prevención de duplicados en arranque (`ArquealiaApplication`)
- [IMPROVED] Logging granular con SLF4J
- [DOCS] Documentación completa (MEMORIA.md, TESTS.md, INSTALACIÓN.md, README.md)

---

## 🧪 Pruebas

### Resumen de Pruebas
- **Casos ejecutados:** 16
- **Casos PASS:** 16 (100%)
- **Casos FAIL:** 0
- **Cobertura funcional:** 100%

### Áreas Probadas
✅ CRUD Yacimientos  
✅ CRUD Museos  
✅ CRUD Arqueólogos  
✅ CRUD Restos Materiales  
✅ CRUD Hallazgos  
✅ Prevención de duplicados  
✅ Asignación automática de museo  
✅ Persistencia en BD  
✅ Interfaz y navegación  
✅ Manejo de errores  

Ver `TESTS.md` para detalles de ejecución y evidencias.

---

## 📦 Contenido de la Distribución

```
Arquealia-v1.0-release/
├── README.md ........................... Guía de uso rápido
├── MEMORIA.md .......................... Documentación técnica completa
├── TESTS.md ............................ Plan de pruebas y resultados
├── INSTALACIÓN.md ...................... Guía paso a paso de instalación
├── RELEASE_NOTES.md .................... Este documento
├── pom.xml ............................. Configuración Maven
├── mvnw.cmd ............................ Maven wrapper Windows
├── mvnw ................................ Maven wrapper Unix
├── src/
│   ├── main/
│   │   ├── java/org/example/arquealia/
│   │   │   ├── ArquealiaApplication.java
│   │   │   ├── dominio/ ............... Entidades JPA
│   │   │   ├── modelo/ ............... Repositorios y servicio
│   │   │   └── ui/ .................  UI Swing
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/.../ArquealiaApplicationTests.java
├── scripts/
│   ├── schema.sql ..................... Creación de BD y tablas
│   ├── seed.sql ........................ Datos iniciales
│   └── cleanup.sql .................... Limpieza de duplicados
└── target/
    └── Arquealia-0.0.1-SNAPSHOT.jar ... Ejecutable JAR

```

---

## 🚀 Instrucciones de Ejecución

### Requisitos Mínimos
- Java 17 o superior
- MySQL 5.7 o superior (ej. XAMPP)
- 200 MB espacio en disco

### Ejecución Rápida
1. Crear BD ejecutando `scripts/schema.sql`
2. Compilar: `mvnw.cmd -DskipTests package`
3. Ejecutar: `java -jar target/Arquealia-0.0.1-SNAPSHOT.jar`

Ver `INSTALACIÓN.md` para instrucciones detalladas.

---

## ⚠️ Limitaciones Conocidas

1. **UI en Swing:** No es la herramienta más moderna. Versión 1.1 migrará a JavaFX.
2. **Sin autenticación:** Todos los usuarios ven todos los datos. Implementar en v1.1.
3. **Sin búsqueda avanzada:** Solo listas simples. Filtros en v1.2.
4. **Base de datos local:** No hay replicación ni backups automáticos. Implementar servidor remoto en v2.0.
5. **Validación básica:** No hay validación de email, longitud de campos, etc. Mejorar en v1.1.

---

## 🔮 Roadmap (Versiones Futuras)

### v1.1 (Q3 2026)
- Migrar UI a JavaFX (mejor apariencia y UX)
- Añadir validación avanzada (email, longitudes, formatos)
- Implementar búsqueda básica por nombre
- Mejorar mensajes de error en UI

### v1.2 (Q4 2026)
- Exportación a CSV
- Importación de datos desde CSV
- Filtros y reportes

### v2.0 (2027)
- API REST completa
- Cliente web (Angular/React)
- Autenticación JWT + roles (arqueólogo, conservador, admin)
- Base de datos en servidor remoto

### v3.0 (2027-2028)
- Sincronización en-línea
- Aplicación móvil (Android/iOS con Flutter)
- Integración con APIs cartográficas
- Análisis de datos y predicciones

---

## 🤝 Contribuciones y Reporte de Bugs

Para reportar un bug o sugerir mejoras:
1. Documenta el problema con pasos para reproducirlo
2. Incluye versión de Java, MySQL y sistema operativo
3. Adjunta logs de la consola si es posible

---

## 📄 Licencia

Relik v1.0 se distribuye bajo licencia **MIT**.  
Siéntete libre de usarlo, modificarlo y distribuirlo, mencionando la autoría original.

---

## 🎓 Notas Académicas

**Proyecto Final de Ciclo Superior en Desarrollo de Aplicaciones Multiplataforma**

Este proyecto demuestra el dominio de:
- ✅ Bases de datos relacionales (diseño, normalización, SQL)
- ✅ Desarrollo backend con Spring Boot y JPA
- ✅ Interfaz gráfica de escritorio (Swing)
- ✅ Control de versiones y documentación
- ✅ Pruebas funcionales y validación
- ✅ Arquitectura en capas y patrones de diseño
- ✅ Manejo de errores y excepciones
- ✅ Acceso a datos ORM

---

## 📞 Información de Contacto

**Desarrollador:** Alumno de DAM  
**Institución:** IES [Centro educativo]  
**Email:** [tu email]  
**Fecha de Entrega:** 16 de mayo de 2026

---

**Versión:** 1.0  
**Release Type:** Stable  
**Status:** ✅ READY FOR PRODUCTION (MVP Phase)
