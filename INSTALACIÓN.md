INSTRUCCIONES DE INSTALACIÓN DETALLADAS - Relik v1.0

Fecha: 16 de mayo de 2026

---

## Paso 1: Verificar Requisitos Previos

### Windows 10/11
```powershell
java -version    # Debe mostrar java 17 o superior
mysql --version    # Si está en PATH; sino, verificar XAMPP
```

### Confirmar XAMPP está ejecutándose
- Abre C:\xampp\xampp-control.exe (o donde esté tu XAMPP)
- Verifica que MySQL esté iniciado (botón "Start" en fila MySQL)

---

## Paso 2: Crear la Base de Datos

### Opción A: phpMyAdmin (recomendada para usuarios sin experiencia)

1. **Abre phpMyAdmin:**
   - En navegador: `http://localhost/phpmyadmin`
   - Usuario: `root`
   - Contraseña: (vacío, o tu contraseña si la configuraste)

2. **Crea la BD ejecutando el script schema.sql:**
   - Click en pestaña "SQL"
   - Abre el archivo `scripts/schema.sql` (copiar contenido completo)
   - Pega el contenido en la caja de texto SQL de phpMyAdmin
   - Click en "Ejecutar" (o presiona Ctrl+Enter)
   - Verifica que aparece mensaje de éxito

3. **Ejecuta el script de datos iniciales (opcional):**
   - Repite los pasos anteriores con `scripts/seed.sql`

4. **Verifica que las tablas se crearon:**
   - Lado izquierdo: expande BD "BArquealia"
   - Deberías ver: tarqueologo, tyacimiento, tmuseo, tresto_material, thallazgo

### Opción B: Cliente MySQL en línea de comandos

```powershell
# Abre PowerShell / CMD y navega a:
cd "C:\xampp\mysql\bin"

# Conecta a MySQL
.\mysql.exe -u root

# En el prompt "mysql>":
SOURCE "E:/Nubes/Profe/OneDrive - Consejería de Educación, Formación Profesional y Empleo/DAM/Proyecto/Arquealia/scripts/schema.sql";
SOURCE "E:/Nubes/Profe/OneDrive - Consejería de Educación, Formación Profesional y Empleo/DAM/Proyecto/Arquealia/scripts/seed.sql";

# Verifica (opcional):
USE BArquealia;
SHOW TABLES;
SELECT COUNT(*) FROM tyacimiento;

# Sale:
EXIT;
```

### Opción C: HeidiSQL (si lo tienes instalado)
- Clic derecho en servidor → "New Database" → nombre: `BArquealia` → OK
- File → "Open SQL file" → selecciona `schema.sql` → Click "Execute"
- Repite con `seed.sql`

---

## Paso 3: Configurar la Aplicación

### Verifica aplicación.properties

Abre en editor:
```
E:\Nubes\Profe\...\Arquealia\src\main\resources\application.properties
```

Asegúrate de que contiene (ajusta con tu entorno):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/BArquealia
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=0
```

**Nota sobre la contraseña:**
- Si tu usuario MySQL es `root` **sin contraseña**: déjalo vacío (como está)
- Si lo creaste **con contraseña**: cambia `spring.datasource.password=` a `spring.datasource.password=tuContraseña`

---

## Paso 4: Compilar la Aplicación

Abre PowerShell en la carpeta del proyecto:

```powershell
cd "E:\Nubes\Profe\OneDrive - Consejería de Educación, Formación Profesional y Empleo\DAM\Proyecto\Arquealia"

# Compila y genera el JAR
.\mvnw.cmd -DskipTests package
```

**Esto puede tardar 2-5 minutos la primera vez** (descarga dependencias).

Si todo va bien, le veras:
```
[INFO] BUILD SUCCESS
[INFO] Total time:  X.XXX s
```

El archivo JAR estará en:
```
target\Arquealia-0.0.1-SNAPSHOT.jar
```

---

## Paso 5: Ejecutar la Aplicación

### Opción A: Desde PowerShell (recomendada)
```powershell
cd "E:\Nubes\Profe\OneDrive - Consejería de Educación, Formación Profesional y Empleo\DAM\Proyecto\Arquealia"
java -jar target\Arquealia-0.0.1-SNAPSHOT.jar
```

### Opción B: Desde Maven directamente
```powershell
.\mvnw.cmd -DskipTests spring-boot:run
```

### Esperado:
1. Verás muchas líneas de logs de Spring Boot (esto es normal)
2. Después de ~5 segundos, **aparecerá una ventana Swing** con el título "Relik - Gestión de hallazgos (prototipo)"
3. La ventana tiene 5 pestañas: Yacimientos, Museos, Arqueólogos, Restos, Hallazgos

---

## Paso 6: Probar la Aplicación

Una vez abierta la UI:

1. **Pestaña Yacimientos:**
   - Click "Nuevo"
   - Escribe: "Mi Yacimiento Test"
   - OK
   - Debería aparecer en la lista

2. **Pestaña Museos:**
   - Click "Nuevo"
   - Nombre: "Mi Museo"
   - Época: "Paleolitico"
   - OK
   - Debería aparecer en la lista

3. **Pestaña Arqueólogos:**
   - Click "Nuevo"
   - Nombre: "Dr. Test"
   - Correo: "test@example.com"
   - Contraseña: "1234"
   - OK

Si todo aparece sin errores, **¡la instalación fue exitosa!**

---

## Solución de Problemas

### ❌ Error: "Connection refused" o "Cannot connect to database"
**Causa:** MySQL no está en marcha o conexión incorrecta
**Solución:**
1. Verifica que XAMPP MySQL está iniciado
2. Revisa usuario/contraseña en `application.properties`
3. Asegúrate de que BD "BArquealia" existe (verifica en phpMyAdmin)

### ❌ Error: "Port 8080 is already in use" (puede ocurrir, pero está manejado)
**Causa:** Otro proceso usa puerto 8080
**Solución:** Está configurado para usar puerto random (port=0), así que debería iniciarse sin problemas. Si no:
- Cambia `server.port=8080` a `server.port=8081` en `application.properties`

### ❌ Error: "java: command not found"
**Causa:** Java no está en PATH
**Solución:**
```powershell
# Usa la ruta completa de Java:
"C:\Program Files\Java\jdk-17.X.X\bin\java.exe" -jar target\Arquealia-0.0.1-SNAPSHOT.jar
```

### ❌ No aparece la ventana Swing
**Causa:** Entorno gráfico no disponible (en Linux VM sin X11, por ejemplo)
**Solución:** 
- En Windows/macOS no debería ocurrir
- En Linux, configura X11 forwarding si estás en SSH

### ⚠️ Aparecen WARNINGs en los logs pero la app funciona
**Esto es normal.** Algunos warnings esperados:
```
HHH90000025: MySQL8Dialect does not need to be specified explicitly
HHH90000026: MySQL8Dialect has been deprecated
spring.jpa.open-in-view is enabled by default
```
No son errores críticos; son solo avisos.

### 🔧 Error: "Duplicate entry X for key 'nombre'"
**Causa:** Ya existe un registro con ese nombre
**Solución:** La app lo maneja; verás WARNING en logs pero no crashea. Intenta con otro nombre.

---

## Limpiar Datos Duplicados (si es necesario)

Si la BD acumula muchos duplicados:

1. **Haz backup:**
   ```powershell
   # En phpMyAdmin, derecha clic en BD BArquealia → "Export" → Download
   ```

2. **Ejecuta limpieza:**
   ```
   scripts/cleanup.sql
   ```
   (Repite los pasos del Paso 2 para ejecutar este script)

3. **Comprueba:**
   ```sql
   SELECT nombre, COUNT(*) FROM tyacimiento GROUP BY nombre HAVING COUNT(*) > 1;
   ```
   Debería devolver 0 filas (sin duplicados).

---

## Verificación Final

Checklist antes de considerar la instalación completa:

- [ ] Java 17+ instalado: `java -version`
- [ ] MySQL en marcha (XAMPP)
- [ ] BD BArquealia creada (phpMyAdmin)
- [ ] application.properties actualizado (si aplicable)
- [ ] `mvnw.cmd package` ejecutado exitosamente
- [ ] JAR se generó en `target/`
- [ ] La aplicación arranca sin errores
- [ ] Pestaña Yacimientos muestra tablero
- [ ] Botón "Nuevo" abre un diálogo
- [ ] Datos creados aparecen en la lista

Si todos los items están marcados, **¡estás listo para usar Relik!**

---

**Versión:** 1.0  
**Última actualización:** 16 de mayo de 2026  
**Estado:** Final
