![Relik](https://img.shields.io/badge/Relik-v1.0--release-blue)
![Java](https://img.shields.io/badge/Java-17%2F21-green)
![Spring%20Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange)
![License](https://img.shields.io/badge/Licencia-CC%20BY--SA%204.0-lightgrey)

# Relik – Sistema de Registro y Gestión de Hallazgos Arqueológicos

**Proyecto Fin de Ciclo (PFC) - Formación Profesional de Grado Superior**  
**Ciclo Formativo:** Desarrollo de Aplicaciones Multiplataforma (DAM)  
**Centro Educativo:** IES José Luis Martínez Palomo  

---

## ¿Qué es Relik?

**Relik** es una solución software multiplataforma diseñada para la automatización, documentación y registro en tiempo real de excavaciones arqueológicas a pie de campo. Sustituye el registro manual tradicional en papel por una plataforma digital centralizada y desacoplada mediante arquitectura **Cliente-Servidor (API REST HTTP)**.

### Características Principales:
- ✅ **Micro-Localización Espacial 3D y Estratigrafía:** Registro tridimensional exacto de hallazgos mediante **Cuadrícula/Sector**, **Coordenadas Planas Eje X y Eje Y**, **Cota Z de Profundidad** (nivel topográfico) y **Unidad Estratigráfica (UE)**.
- ✅ **Asignación Automática de Museos:** Clasificación de restos materiales con asignación automatizada al museo receptor según la **Época Histórica** del resto y la especialización del museo.
- ✅ **Control de Acceso y Permisos por Rol:** Sistema de autenticación con diferenciación de roles (`ADMIN` para administración total y `ARQUEOLOGO` para técnicos de campo).
- ✅ **Interfaz Gráfica de Alto Contraste:** UI Swing con diseño propio en tonos arqueológicos (`UITheme`), optimizada para pantallas portátiles en condiciones de campo.
- ✅ **Arquitectura Desacoplada:** Servidor Spring Boot 3 con servidor Tomcat embebido y persistencia en MySQL 8.0.

---

## 📋 Requisitos del Sistema

- **Java Runtime / JDK:** Java 17 o 21.
- **Base de Datos:** MySQL 8.0+ (Servidor independiente o vía XAMPP).
- **Herramienta de Compilación:** Maven 3.6+ (Incluido wrapper `mvnw.cmd`).
- **Sistema Operativo:** Windows, macOS, Linux.

---

## 🚀 Inicio Rápido

### 1. Preparar la Base de Datos MySQL
En tu gestor de base de datos (phpMyAdmin o consola MySQL), ejecuta los scripts ubicados en la carpeta `scripts/`:
```sql
SOURCE scripts/schema.sql;
SOURCE scripts/seed.sql;
```

### 2. Compilar y Empaquetar Ambos Módulos
Desde el directorio raíz del proyecto:
```powershell
.\mvnw.cmd clean package -DskipTests
```

### 3. Arrancar el Servidor REST (`relik-servidor`)
Ejecuta el paquete ejecutable del servidor:
```powershell
java -jar relik-servidor\target\relik-servidor-0.0.1-SNAPSHOT.jar
```
*El servidor iniciará Tomcat embebido en el puerto 8080 (`http://localhost:8080/api`).*

### 4. Arrancar la Aplicación Cliente (`relik-cliente`)
En otra terminal, ejecuta el cliente de escritorio independiente:
```powershell
java -jar relik-cliente\target\relik-cliente-0.0.1-SNAPSHOT.jar
```

---

## 🔑 Credenciales de Acceso por Defecto (Auto-sembradas)

- **Administrador:**  
  - Email: `admin@relik.com`  
  - Contraseña: `admin`  
  - Rol: `ADMIN` (Acceso total a creación, edición y borrado)

- **Arqueólogo de Campo:**  
  - Email: `prueba@example.com` (o `elena.ramos@relik.com`)  
  - Contraseña: `1234`  
  - Rol: `ARQUEOLOGO` (Permiso para crear hallazgos y borrar únicamente los creados por sí mismo)

---

## 📂 Estructura del Proyecto (Maven Multimódulo)

```
Relik/ (directorio raíz)
├── pom.xml                               <-- POM Padre agregador (<packaging>pom</packaging>)
├── mvnw / mvnw.cmd / .mvn/               <-- Maven Wrapper raíz para compilar todo
├── scripts/                              <-- Scripts SQL (schema.sql, seed.sql)
│
├── relik-servidor/                       <-- MÓDULO BACKEND REST API (Spring Boot)
│   ├── pom.xml                           <-- Dependencias de Spring Boot, JPA, MySQL
│   └── src/
│       ├── main/
│       │   ├── resources/application.properties
│       │   └── java/org/example/relik/
│       │       ├── RelikApplication.java <-- Main del microservicio
│       │       ├── controlador/          <-- Controladores REST (@RestController)
│       │       ├── dominio/              <-- Entidades JPA (@Entity)
│       │       ├── modelo/               <-- Repositorios y Servicios (@Service)
│       │       └── dto/                  <-- Objetos de transferencia (DTO)
│       └── test/java/org/example/relik/test/RelikSystemTest.java
│
└── relik-cliente/                        <-- MÓDULO FRONTEND DESKTOP (Java Swing)
    ├── pom.xml                           <-- Solo dependencias de cliente (Gson)
    └── src/main/java/org/example/relik/cliente/
        ├── ClienteRelik.java             <-- Main Class del cliente
        ├── SessionManager.java           <-- Manejo de sesión y rol en memoria
        ├── UITheme.java                  <-- Paleta temática arqueológica
        ├── LoginDialog.java              <-- Ventana modal de login
        └── Gestor*.java                  <-- Ventanas de gestión (Arqueólogos, Hallazgos, etc.)
```

---

## 📄 Licencia

Este proyecto está bajo la licencia **Creative Commons Atribución-CompartirIgual 4.0 Internacional (CC BY-SA 4.0)**.


