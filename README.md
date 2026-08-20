![Relik](https://img.shields.io/badge/Relik-v1.0--release-blue)
![Java](https://img.shields.io/badge/Java-17+-green)
![Spring%20Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange)

# Relik – Aplicación de Gestión de Hallazgos Arqueológicos

**Proyecto Final de Ciclo Superior en Desarrollo de Aplicaciones Multiplataforma**

Una aplicación de escritorio intuitiva que facilita el registro y gestión centralizada de hallazgos arqueológicos en excavaciones de campo.

---

## ¿Qué es Relik?

Relik es una solución software desarrollada con **Spring Boot** y una interfaz gráfica en **Swing** que permite a arqueólogos:

- Registrar nuevos hallazgos con contexto completo (arqueólogo, yacimiento, resto material)  
- Gestionar yacimientos, museos y catálogos de restos materiales  
- Mantener una base de datos centralizada y consistente  
- Reemplazar el trabajo tradicional en papel por un flujo digital eficiente  

---

## Requisitos del Sistema

- **Java:** 17 o superior
- **MySQL:** 5.7 o superior (ej. XAMPP)
- **RAM:** 512 MB mínimo, 1 GB recomendado
- **Disco:** 200 MB
- **S.O.:** Windows, macOS, Linux
- **Maven:** (incluido en el proyecto como wrapper mvnw)

---

## 🚀 Inicio Rápido (5 minutos)

### 1. Preparar la Base de Datos

#### Opción A: phpMyAdmin (XAMPP)
1. Abre XAMPP Control Panel → inicia MySQL
2. Abre navegador → `localhost/phpmyadmin`
3. Copia el contenido de `scripts/schema.sql` y ejecútalo en la pestaña SQL
4. (Opcional) ejecuta `scripts/seed.sql` para datos iniciales

#### Opción B: Línea de Comandos
```powershell
# Abre el cliente MySQL
cd "C:\xampp\mysql\bin"
.\mysql.exe -u root

# En el prompt de MySQL:
SOURCE "E:/Nubes/Profe/.../scripts/schema.sql";
SOURCE "E:/Nubes/Profe/.../scripts/seed.sql";
EXIT;
```

### 2. Configurar Credenciales (si es necesario)

Abre `src/main/resources/application.properties` y verifica:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/BArquealia
spring.datasource.username=root
spring.datasource.password=
```
Si tu MySQL usa contraseña, actualiza `spring.datasource.password`.

### 3. Compilar y Ejecutar

```powershell
cd "E:\Nubes\Profe\OneDrive - Consejería de Educación, Formación Profesional y Empleo\DAM\Proyecto\Arquealia"

# Compilar
.\mvnw.cmd -DskipTests package

# Ejecutar
java -jar target\Arquealia-0.0.1-SNAPSHOT.jar
```

**¡Listo!** La interfaz Swing se abre automáticamente. 

---

## Guía de Uso

### Interfaz Gráfica

La aplicación tiene 5 pestañas principales:

#### 1️ **Yacimientos** (lugares de excavación)
- **Nuevo:** Añade un yacimiento con nombre
- **Editar:** Modifica el nombre de uno seleccionado
- **Eliminar:** Borra el yacimiento de la BD
- **Refrescar:** Recarga la lista

#### 2️ **Museos** (destino de restos)
- **Nuevo:** Crea un museo especificando nombre y época especializada
- **Editar:** Modifica nombre/época
- **Eliminar:** Elimina el museo
- **Refrescar:** Actualiza lista

#### 3️ **Arqueólogos** (profesionales registrados)
- **Nuevo:** Registra un arqueólogo (nombre, correo, contraseña)
- **Eliminar:** Borra del registro
- **Refrescar:** Recarga

#### 4️ **Restos** (objetos hallados)
- **Nuevo:** Crea un resto material (nombre, época, tipología)
- **Nota:** Se asigna automáticamente al museo de su época
- **Editar/Eliminar:** Operaciones estándar

#### 5️ **Hallazgos** (registros de descubrimientos)
- **Nuevo:** Crea un hallazgo seleccionando:
  - Arqueólogo que realizó el hallazgo
  - Yacimiento donde fue hallado
  - Resto material encontrado
- El timestamp se registra automáticamente

---

## Estructura del Proyecto

```
Arquealia/
├── src/
│   ├── main/
│   │   ├── java/org/example/arquealia/
│   │   │   ├── ArquealiaApplication.java (main + CommandLineRunner)
│   │   │   ├── dominio/
│   │   │   │   ├── Arqueologo.java
│   │   │   │   ├── Hallazgo.java
│   │   │   │   ├── Museo.java
│   │   │   │   ├── RestoMaterial.java
│   │   │   │   └── Yacimiento.java
│   │   │   ├── modelo/
│   │   │   │   ├── ModeloInterface.java
│   │   │   │   ├── ModeloInterfaceImpl.java
│   │   │   │   ├── YacimientoRepository.java
│   │   │   │   ├── MuseoRepository.java
│   │   │   │   ├── ArqueologoRespository.java
│   │   │   │   ├── RestoMaterialRepository.java
│   │   │   │   └── HallazgoRepository.java
│   │   │   └── ui/
│   │   │       ├── MainWindow.java (ventana principal Swing)
│   │   │       └── UIStarter.java (listener de arranque)
│   │   └── resources/
│   │       └── application.properties (configuración)
│   └── test/
│       └── java/.../ArquealiaApplicationTests.java
├── scripts/
│   ├── schema.sql (crear BD y tablas)
│   ├── seed.sql (datos iniciales de prueba)
│   └── cleanup.sql (limpiar duplicados si los hay)
├── pom.xml (dependencias Maven)
├── README.md (este archivo)
├── MEMORIA.md (documentación técnica del proyecto)
├── TESTS.md (plan de pruebas y resultados)
└── mvnw.cmd / mvnw (Maven Wrapper para Windows/Unix)
```

---

##  Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.4.2 | Framework web/aplicación |
| Spring Data JPA | - | Acceso a datos ORM |
| Hibernate | 6.6.5 | ORM de mapeo objeto-relacional |
| MySQL Connector | 8.0+ | Driver MySQL |
| Swing | - | UI de escritorio |
| Maven | 3.6+ | Build tool |

---

## Pruebas

Consulta `TESTS.md` para ver:
- Plan detallado de pruebas (16 casos)
- Resultados ejecutados (100% PASS)
- Observaciones y recomendaciones

Resumen: La aplicación ha sido testeada en:
- ✓ Operaciones CRUD de todas las entidades
- ✓ Persistencia en BD
- ✓ Manejo de errores y duplicados
- ✓ Usabilidad de interfaz

---

## Documentación

- **MEMORIA.md** – Documento académico completo del proyecto (requisitos, diseño, implementación)
- **TESTS.md** – Plan de pruebas, casos y resultados
- **README.md** – Este archivo (guía de uso rápido)

---

## Solución de Problemas

### "No se puede conectar a MySQL"
- Verifica que MySQL esté en marcha (XAMPP Control Panel)
- Revisa credenciales en `application.properties`
- Asegúrate de que `BArquealia` BD existe

### "Duplicado entry" en logs
- Ejecuta `scripts/cleanup.sql` para limpiar duplicados previos
- La app no crashea; solo registra un WARNING

### "Puerto 8080 en uso"
- Por diseño, la app usa puerto aleatorio (0) en desarrollo
- En producción, cambia `server.port=8080` en `application.properties`

### La UI no aparece
- Verifica que Java tiene soporte para Swing (Windows sí; en Linux/VM puede necesitar configuración X11)
- Reintenta ejecutar con: `java -Djava.awt.headless=false -jar ...jar`

---

## Soporte y Contacto

Para reportar bugs o sugerencias:
- Abre un issue en el repositorio
- O contacta al desarrollador: [tu email si lo deseas incluir]

---

## 📄 Licencia

Este proyecto es de código abierto desarrollado como trabajo académico.  
Licencia: GNU 3.0

---

## 🎉 Próximas Versiones (Roadmap)

- **v1.1** – Mejoras en UI
- **v1.2** – API REST + cliente web
- **v2.0** – Autenticación y control de roles

---

**Versión Actual:** 1.0 (Release)  
**Fecha:** 16 de mayo de 2026  
**Estado:** Producción (MVP)

