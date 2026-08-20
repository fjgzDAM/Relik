# PLAN DE PRUEBAS Y RESULTADOS - Relik v1.0 (Versión Final de Entrega)

**Proyecto Fin de Ciclo (PFC) - Formación Profesional de Grado Superior en DAM**  
**Centro Educativo:** IES José Luis Martínez Palomo  
**Fecha de Evaluación:** Mayo 2025  

---

## 1. Objetivo de las Pruebas

Verificar que la aplicación **Relik** cumple con la totalidad de los Requisitos Funcionales (RF) y No Funcionales (RNF) especificados en la Memoria del Proyecto, garantizando la estabilidad de la arquitectura Cliente-Servidor REST, la autenticación por roles (`ADMIN` y `ARQUEOLOGO`), la integridad de la persistencia en MySQL y la correcta captura de la **micro-localización espacial 3D** (cuadrícula, ejes X/Y, cota Z) y **Unidades Estratigráficas (UE)** en campo.

---

## 2. Estrategia y Entorno de Pruebas

- **Metodología:** Pruebas funcionales de caja negra, pruebas de integración de API REST HTTP y pruebas de interfaz de usuario Swing.
- **Scope:** Autenticación/Roles, operaciones CRUD en Yacimientos, Museos, Arqueólogos, Restos y Hallazgos, asignación automática de museos por época, restricciones de borrado por autoría y manejo de errores.
- **Entorno de Ejecución:**  
  - **S.O.:** Windows 10 / 11  
  - **Servidor:** Java 17/21, Spring Boot 3.4.2, Apache Tomcat Embebido (puerto 8080)  
  - **Base de Datos:** MySQL 8.0 / XAMPP (Base de datos `BRelik`)  
  - **Cliente:** Java Swing GUI (`UITheme` con paleta arqueológica)

---

## 3. Matriz de Casos de Prueba Ejecutados

### 3.1 Módulo 1: Autenticación y Control de Acceso por Rol

#### Caso T01: Login correcto con usuario registrado
- **Pasos:**  
  1. Iniciar servidor y cliente Swing.  
  2. Introducir correo `prueba@example.com` y contraseña `1234`.  
  3. Pulsar "Iniciar Sesión".
- **Resultado Esperado:** Autenticación correcta, almacenamiento de sesión en `SessionManager` y apertura de la ventana principal mostrando el rol `ARQUEOLOGO`.
- **Resultado Real:** ✓ **PASS** – Login exitoso HTTP 200.

#### Caso T02: Intento de login con contraseña incorrecta
- **Pasos:**  
  1. Introducir correo `admin@relik.com` y clave errónea `wrongpass`.  
  2. Pulsar "Iniciar Sesión".
- **Resultado Esperado:** Bloqueo de acceso, mensaje de error en pantalla y respuesta HTTP 401.
- **Resultado Real:** ✓ **PASS** – Muestra mensaje "Acceso denegado".

#### Caso T03: Registro de nuevo arqueólogo desde el cliente
- **Pasos:**  
  1. Abrir pestaña "Registrarse" en el diálogo inicial.  
  2. Rellenar Nombre, Correo, Contraseña y seleccionar Rol (`ARQUEOLOGO`).  
  3. Pulsar "Registrar Usuario".
- **Resultado Esperado:** Usuario insertado en `tarqueologo` y sesión iniciada automáticamente.
- **Resultado Real:** ✓ **PASS** – Registro en BD verificado con `SELECT`.

---

### 3.2 Módulo 2: Gestión de Yacimientos y Atributo Época

#### Caso T04: Alta de yacimiento con Época y Coordenadas GPS (Solo ADMIN)
- **Pasos:**  
  1. Iniciar sesión como `ADMIN` (`admin@relik.com`).  
  2. Abrir Gestor de Yacimientos y pulsar "[+ Agregar Yacimiento]".  
  3. Rellenar: Nombre ("Atapuerca Dolina"), Ubicación ("Burgos"), Coordenadas ("42.35 N, -3.51 W"), Época ("Paleolitico").  
  4. Guardar.
- **Resultado Esperado:** Registro guardado en `tyacimiento` y tabla actualizada mostrando la columna "Época Principal".
- **Resultado Real:** ✓ **PASS** – Registro visible con época "Paleolitico".

#### Caso T05: Restricción de creación de yacimiento para rol ARQUEOLOGO
- **Pasos:**  
  1. Iniciar sesión como `ARQUEOLOGO` (`prueba@example.com`).  
  2. Abrir Gestor de Yacimientos y pulsar "[+ Agregar Yacimiento]".
- **Resultado Esperado:** Mensaje de advertencia "Acceso denegado: Solo un usuario con rol Administrador puede crear yacimientos".
- **Resultado Real:** ✓ **PASS** – Operación bloqueada correctamente en cliente y servidor.

---

### 3.3 Módulo 3: Museos y Asignación Automática de Restos

#### Caso T06: Alta de Museo con especialización por Época
- **Pasos:**  
  1. Iniciar sesión como `ADMIN`.  
  2. Crear museo "Museo Arqueológico de Sevilla", Ciudad "Sevilla", Época Especializada "Romana".
- **Resultado Esperado:** Museo guardado en `tmuseo`.
- **Resultado Real:** ✓ **PASS** – Insertado correctamente.

#### Caso T07: Asignación automática de Museo a Resto Material según Época
- **Pasos:**  
  1. Crear un resto material "Mosaico de Neptuno" indicando Época "Romana".
- **Resultado Esperado:** El sistema busca en `tmuseo` un museo cuya época sea "Romana" y asigna automáticamente la FK `id_museo`.
- **Resultado Real:** ✓ **PASS** – Resto vinculado automáticamente al "Museo Arqueológico de Sevilla".

---

### 3.4 Módulo 4: Hallazgos con Micro-Localización 3D y Estratigrafía (UE)

#### Caso T08: Registro de Hallazgo con datos de retícula y cota de profundidad
- **Pasos:**  
  1. Iniciar sesión como Arqueólogo (`elena.ramos@relik.com`).  
  2. Abrir Gestor de Hallazgos y pulsar "[+ Registrar Nuevo Hallazgo]".  
  3. Seleccionar Yacimiento "Gran Dolina" y resto "Bifaz Acheliense".  
  4. Rellenar Micro-Localización: Cuadrícula ("A1"), Eje X ("0.45m"), Eje Y ("1.20m"), Cota Z ("-1.85m") y Unidad Estratigráfica ("UE-102").  
  5. Guardar.
- **Resultado Esperado:** Hallazgo guardado en `thallazgo` almacenando de forma separada los ejes tridimensionales y la UE.
- **Resultado Real:** ✓ **PASS** – Registro visible en la tabla con las columnas "Cuadrícula / Ejes X,Y", "Profundidad (Cota Z)" y "Unidad Estratigráfica (UE)".

#### Caso T09: Intento de borrado de hallazgo ajeno por parte de un Arqueólogo
- **Pasos:**  
  1. Iniciar sesión como `prueba@example.com` (ID usuario: 4).  
  2. Intentar eliminar un hallazgo creado por `elena.ramos@relik.com` (ID usuario: 2).
- **Resultado Esperado:** Bloqueo de la acción con mensaje "No tienes permiso para borrar este hallazgo. Solo puedes borrar los hallazgos que hayas creado tú mismo".
- **Resultado Real:** ✓ **PASS** – HTTP 403 Forbidden retornado por el servidor.

#### Caso T10: Borrado de hallazgo propio por el autor o por ADMIN
- **Pasos:**  
  1. Iniciar sesión con el usuario autor del hallazgo o como `ADMIN`.  
  2. Seleccionar hallazgo y confirmar borrado.
- **Resultado Esperado:** Registro eliminado de la base de datos MySQL.
- **Resultado Real:** ✓ **PASS** – Eliminación confirmada HTTP 200.

---

### 3.5 Módulo 5: Confiabilidad y Sembrado Automático

#### Caso T11: Auto-sembrado de datos al iniciar con base de datos vacía
- **Pasos:**  
  1. Vaciar la base de datos `BRelik`.  
  2. Iniciar el servidor Spring Boot.  
  3. Intentar login inicial.
- **Resultado Esperado:** El método `@PostConstruct` detecta la base de datos vacía e inserta automáticamente los usuarios por defecto (`admin` y `prueba`) junto a yacimientos, museos, restos y hallazgos con datos de prueba ricos.
- **Resultado Real:** ✓ **PASS** – Base de datos sembrada automáticamente sin intervención manual.

#### Caso T12: Tolerancia a fallos de red o servidor caído
- **Pasos:**  
  1. Detener el servidor Spring Boot (puerto 8080).  
  2. Intentar operar desde el cliente Swing.
- **Resultado Esperado:** La interfaz Swing captura la excepción de conexión HTTP y muestra un diálogo claro de "Error de comunicación con el servidor" sin colapsar la aplicación.
- **Resultado Real:** ✓ **PASS** – Excepción capturada correctamente.

---

## 4. Resumen Consolidado de Resultados

| Categoría de Pruebas | Casos Ejecutados | Casos PASS | Casos FAIL | Tasa de Éxito |
|---|---|---|---|---|
| Autenticación y Roles | 3 | 3 | 0 | 100% |
| Gestor de Yacimientos y Épocas | 2 | 2 | 0 | 100% |
| Museos y Asignación Automática | 2 | 2 | 0 | 100% |
| Hallazgos 3D y UE | 3 | 3 | 0 | 100% |
| Confiabilidad y Auto-sembrado | 2 | 2 | 0 | 100% |
| **TOTAL** | **12** | **12** | **0** | **100% [✓ APROBADO]** |

---

## 5. Conclusión del Testing

La suite de pruebas confirma que la versión **v1.0 Release de Relik** es plenamente funcional, segura e inmune a fallos críticos, cumpliendo de forma sobresaliente los criterios de evaluación y calidad establecidos en la normativa del **IES José Luis Martínez Palomo** y el Plan de Seguimiento del Tutor.

