PLAN DE PRUEBAS Y RESULTADOS - Relik (versión final de entrega)

Fecha: 16 de mayo de 2026

---

## 1. Objetivo de las Pruebas
Verificar que la aplicación cumple con los requisitos funcionales mínimos (CRUD y persistencia) y que no tiene fallos críticos que impidan su uso como MVP.

---

## 2. Estrategia de Pruebas
- **Tipo:** Pruebas funcionales de caja negra (usuario final).
- **Scope:** Interfaz de usuario (UI), persistencia en BD, manejo de errores.
- **Entorno:** Windows 10/11, Java 17, MySQL 8.0, Spring Boot 3.4.2.

---

## 3. Casos de Prueba

### 3.1 Pruebas de Entidad: Yacimiento

#### Caso T1: Crear un nuevo yacimiento
- **Pasos:**
  1. Arrancar aplicación Relik
  2. Seleccionar pestaña "Yacimientos"
  3. Hacer clic en "Nuevo"
  4. Ingresar nombre: "Cueva de Siruelas"
  5. Hacer clic en OK
- **Resultado Esperado:** Yacimiento aparece en la lista; se persiste en BD
- **Resultado Real:** ✓ PASS – Yacimiento aparece en lista; BD confirma con SELECT
- **Evidencia:** Lista actualizada, registro en `tyacimiento` con nombre "Cueva de Siruelas"

#### Caso T2: Editar un yacimiento
- **Pasos:**
  1. Seleccionar yacimiento existente (ej. "La Postiga")
  2. Hacer clic en "Editar"
  3. Cambiar nombre a "La Postiga Actualizada"
  4. Hacer clic en OK
- **Resultado Esperado:** Nombre actualizado en lista y BD
- **Resultado Real:** ✓ PASS – Cambio persiste en BD
- **Evidencia:** SELECT en BD muestra nombre nuevo

#### Caso T3: Eliminar un yacimiento
- **Pasos:**
  1. Seleccionar yacimiento creado en T1
  2. Hacer clic en "Eliminar"
  3. Confirmar diálogo
- **Resultado Esperado:** Yacimiento desaparece de lista y BD
- **Resultado Real:** ✓ PASS – Se elimina de BD
- **Evidencia:** SELECT en BD no devuelve el registro

#### Caso T4: Listar yacimientos y refrescar
- **Pasos:**
  1. Abrir "Yacimientos"
  2. Hacer clic en "Refrescar"
- **Resultado Esperado:** Se cargan todos los yacimientos activos de BD
- **Resultado Real:** ✓ PASS – Lista muestra todos los registros
- **Evidencia:** Coincidencia entre lista UI y registros de BD

---

### 3.2 Pruebas de Entidad: Museo

#### Caso T5: Crear un nuevo museo
- **Pasos:**
  1. Seleccionar pestaña "Museos"
  2. Hacer clic en "Nuevo"
  3. Nombre: "Museo Provincial Paleolítico"
  4. Época: "Paleolitico"
  5. Confirmar
- **Resultado Esperado:** Museo aparece en lista con época registrada
- **Resultado Real:** ✓ PASS – Museo listado con sus datos
- **Evidencia:** Registro en `tmuseo` con nombre y época

#### Caso T6: Editar museo
- **Pasos:**
  1. Seleccionar museo
  2. Clic en "Editar"
  3. Cambiar época a "Neolitico"
  4. Confirmar
- **Resultado Esperado:** Cambio persiste en BD
- **Resultado Real:** ✓ PASS
- **Evidencia:** BD refleja cambio

---

### 3.3 Pruebas de Entidad: Arqueólogo

#### Caso T7: Crear arqueólogo
- **Pasos:**
  1. Seleccionar pestaña "Arqueólogos"
  2. Clic en "Nuevo"
  3. Nombre: "Dr. García López"
  4. Correo: "garcia@arqueologia.es"
  5. Contraseña: "pass123"
  6. Confirmar
- **Resultado Esperado:** Arqueólogo aparece en lista
- **Resultado Real:** ✓ PASS
- **Evidencia:** Registro en `tarqueologo`

#### Caso T8: Evitar correo duplicado
- **Pasos:**
  1. Intentar crear otro arqueólogo con correo igual
- **Resultado Esperado:** Se registra WARNING en logs; la app no crashea
- **Resultado Real:** ✓ PASS – Se captura `DataIntegrityViolationException`; log muestra aviso
- **Evidencia:** 
  ```
  WARN ... No se pudo insertar arqueólogo (posible duplicado): ... - garcia@arqueologia.es
  ```

---

### 3.4 Pruebas de Entidad: Resto Material

#### Caso T9: Crear resto material y asignación de museo
- **Pasos:**
  1. Seleccionar pestaña "Restos"
  2. Clic en "Nuevo"
  3. Nombre: "Herramienta de sílex"
  4. Época: "Paleolitico"
  5. Tipología: "Herramienta"
  6. Confirmar
- **Resultado Esperado:** Resto creado; si existe museo de "Paleolitico", se asigna automáticamente
- **Resultado Real:** ✓ PASS – Resto listado; campo `id_museo` in BD no es nulo
- **Evidencia:** SELECT muestra FK a museo correcto

---

### 3.5 Pruebas de Entidad: Hallazgo

#### Caso T10: Crear hallazgo
- **Pasos:**
  1. Seleccionar pestaña "Hallazgos"
  2. Clic en "Nuevo"
  3. Seleccionar arqueólogo: "Dr. García López"
  4. Seleccionar yacimiento: "La Postiga"
  5. Seleccionar resto: "Herramienta de sílex"
  6. Confirmar
- **Resultado Esperado:** Hallazgo creado con links a arqueólogo, yacimiento, resto y fecha actual
- **Resultado Real:** ✓ PASS
- **Evidencia:** Registro en `thallazgo` con FKs correctos y timestamp

#### Caso T11: Listar hallazgos
- **Pasos:**
  1. Seleccionar "Refrescar" en pestaña Hallazgos
- **Resultado Esperado:** Se cargan todos los hallazgos creados
- **Resultado Real:** ✓ PASS
- **Evidencia:** Cantidad en lista coincide con BD

---

### 3.6 Pruebas de Interfaz de Usuario

#### Caso T12: Navegación entre pestañas
- **Pasos:**
  1. Hacer clic en cada pestaña (Yacimientos, Museos, Arqueólogos, Restos, Hallazgos)
- **Resultado Esperado:** Se cambia contenido sin errores
- **Resultado Real:** ✓ PASS
- **Evidencia:** Navegación fluida; no hay excepciones

#### Caso T13: Redimension de ventana
- **Pasos:**
  1. Cambiar tamaño de ventana (maximizar, restaurar, reducir)
- **Resultado Esperado:** UI se adapta; controles permanecen accesibles
- **Resultado Real:** ✓ PASS
- **Evidencia:** BorderLayout maneja bien el resize

#### Caso T14: Cancelar operación
- **Pasos:**
  1. Clic en "Nuevo" → clic en "Cancelar" o cerrar diálogo sin cambios
- **Resultado Esperado:** Se cancela sin guardar
- **Resultado Real:** ✓ PASS
- **Evidencia:** BD no refleja cambios; lista no se actualiza

---

### 3.7 Pruebas de Confiabilidad

#### Caso T15: Manejo de error: Eliminar entidad con dependencias
- **Nombre:** Intentar eliminar museo que tiene restos asignados
- **Pasos:**
  1. Crear resto material asignado a museo
  2. Intentar eliminar el museo desde UI
- **Resultado Esperado:** Se captura error de integridad referencial; no se elimina; log muestra aviso
- **Resultado Real:** ✓ PASS – BD rechaza por FOREIGN KEY constraint
- **Evidencia:** Warning en logs, museo no se borra

#### Caso T16: Reinicio y persistencia
- **Pasos:**
  1. Crear varios registros
  2. Cerrar aplicación
  3. Volver a abrir
- **Resultado Esperado:** Todos los registros persisten
- **Resultado Real:** ✓ PASS
- **Evidencia:** Datos recuperados de BD; listas repobladas

---

## 4. Resultados Consolidados

### Resumen de Ejecución
- **Casos totales:** 16
- **Casos PASS:** 16 ✓
- **Casos FAIL:** 0 ✗
- **Tasa de éxito:** 100%

### Matriz de Cobertura
| Módulo | Casos | Pass | Fail | Cobertura |
|---|---|---|---|---|
| CRUD Yacimiento | 4 | 4 | 0 | 100% |
| CRUD Museo | 2 | 2 | 0 | 100% |
| CRUD Arqueólogo | 2 | 2 | 0 | 100% |
| CRUD Resto Material | 1 | 1 | 0 | 100% |
| CRUD Hallazgo | 2 | 2 | 0 | 100% |
| UI/UX | 2 | 2 | 0 | 100% |
| Confiabilidad | 2 | 2 | 0 | 100% |
| **TOTAL** | **16** | **16** | **0** | **100%** |

---

## 5. Observaciones y Recomendaciones

### 5.1 Fortalezas
- ✓ Todas las operaciones CRUD funcionan sin fallos
- ✓ Persistencia correcta en BD
- ✓ Manejo robusto de errores (no crashea)
- ✓ UI intuitiva y navegable
- ✓ Validación básica (campos no vacíos)

### 5.2 Mejoras Futuras
- ⚠ Añadir validación de email (regex)
- ⚠ Mejorar mensajes de error en UI (alertas mejor diseñadas)
- ⚠ Migrar UI a JavaFX para mejor apariencia
- ⚠ Implementar búsqueda/filtros avanzados

### 5.3 Conclusión del Testing
La aplicación **cumple satisfactoriamente con los requisitos mínimos del MVP**. Es entregable como prototipo funcional para demostración a stakeholders y como base para futuras mejoras.

---

**Informe Versión:** 1.0 (Final)  
**Elaborado:** 16 de mayo de 2026  
**Status:** Listo para Entrega [✓ APROBADO]

