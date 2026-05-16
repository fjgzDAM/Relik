@echo off
REM Script para empaquetar Relik v1.0 para entrega
REM Crea un ZIP limpio sin archivos innecesarios

setlocal enabledelayedexpansion

REM Variables
set PROJECT_DIR=%cd%
set PACKAGE_NAME=Relik-v1.0-release
set ZIP_FILE=!PACKAGE_NAME!.zip

REM Verificar que estamos en la carpeta correcta
if not exist "pom.xml" (
    echo ERROR: pom.xml no encontrado. Ejecuta este script desde la raiz del proyecto.
    exit /b 1
)

echo ======================================================
echo   EMPAQUETADOR DE RELIK v1.0
echo ======================================================
echo.
echo Proyecto: %PROJECT_DIR%
echo.

REM Limpiar si existe ZIP previo
if exist "!ZIP_FILE!" (
    echo [*] Eliminando ZIP previo...
    del "!ZIP_FILE!"
)

REM Crear carpeta temporal
set TEMP_DIR=%temp%\!PACKAGE_NAME!
if exist "!TEMP_DIR!" rmdir /s /q "!TEMP_DIR!"
mkdir "!TEMP_DIR!"

echo [*] Copiando archivos de proyecto...

REM Copiar archivos root
copy pom.xml "!TEMP_DIR!\"
copy mvnw.cmd "!TEMP_DIR!\"
copy mvnw "!TEMP_DIR!\"
copy README.md "!TEMP_DIR!\"
copy MEMORIA.md "!TEMP_DIR!\"
copy TESTS.md "!TEMP_DIR!\"
copy INSTALACIÓN.md "!TEMP_DIR!\"
copy RELEASE_NOTES.md "!TEMP_DIR!\"
copy CHECKLIST_ENTREGA.md "!TEMP_DIR!\"
copy INSTRUCCIONES_ENTREGA.txt "!TEMP_DIR!\"

REM Copiar src
xcopy src "!TEMP_DIR!\src" /E /I /Y >nul

REM Copiar scripts
xcopy scripts "!TEMP_DIR!\scripts" /E /I /Y >nul

REM Copiar target (JAR compilado)
if exist target\Arquealia-0.0.1-SNAPSHOT.jar (
    mkdir "!TEMP_DIR!\target"
    copy target\Arquealia-0.0.1-SNAPSHOT.jar "!TEMP_DIR!\target\"
    echo [+] JAR ejecutable incluido
) else (
    echo [!] JAR no encontrado. Considera compilar antes: mvnw.cmd -DskipTests package
)

echo [+] Copias completadas

REM Crear ZIP usando PowerShell (más portable que 7z)
echo [*] Creando archivo ZIP...

powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::CreateFromDirectory('%TEMP_DIR%', '%ZIP_FILE%')"

if exist "!ZIP_FILE!" (
    echo.
    echo ======================================================
    echo   EXITO: Archivo de entrega creado
    echo ======================================================
    echo.
    echo Nombre: !ZIP_FILE!
    echo Ubicacion: %PROJECT_DIR%
    echo.
    for /f %%A in ('wc -c < "!ZIP_FILE!"') do (
        set /a size=%%A / 1048576
        echo Tamano: aproximadamente !size! MB
    )
    echo.
    echo [+] Contiene:
    echo     - Código fuente (src/)
    echo     - Scripts SQL (scripts/)
    echo     - JAR compilado (si existe)
    echo     - Documentación (README, MEMORIA, TESTS, etc.)
    echo     - pom.xml y Maven wrapper
    echo.
    echo [+] Listo para descargar y entregar a plataforma
)

REM Limpiar
echo.
echo [*] Limpiando archivos temporales...
rmdir /s /q "!TEMP_DIR!"

echo.
echo ======================================================
echo   EMPAQUETAMIENTO COMPLETADO
echo ======================================================

endlocal

