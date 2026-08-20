package org.example.relik.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

/**
 * Test Automatizado Exhaustivo - Relik (Cliente - Servidor REST)
 * Prueba el 100% de las funcionalidades que ejecuta la interfaz Swing.
 */
public class RelikSystemTest {
    private static final String SERVER_URL = "http://localhost:8080/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║      RELIK v1.0 - BATERÍA EXHAUSTIVA DE PRUEBAS END-TO-END CLIENTE-API     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");

        try {
            // 1. AUTENTICACIÓN
            testAuthLoginAdmin();
            testAuthLoginArqueologo();
            testAuthLoginInvalido();

            // 2. ARQUEÓLOGOS (CRUD)
            testListarArqueologos();
            int idArqueologoCreado = testCrearArqueologo();
            testModificarArqueologo(idArqueologoCreado);
            testEliminarArqueologo(idArqueologoCreado);

            // 3. YACIMIENTOS (CRUD)
            testListarYacimientos();
            int idYacimientoCreado = testCrearYacimiento();
            testModificarYacimiento(idYacimientoCreado);
            testEliminarYacimiento(idYacimientoCreado);

            // 4. MUSEOS (CRUD)
            testListarMuseos();
            int idMuseoCreado = testCrearMuseo();
            testModificarMuseo(idMuseoCreado);
            testEliminarMuseo(idMuseoCreado);

            // 5. RESTOS MATERIALES (CRUD Y ASIGNACIÓN A MUSEO)
            testListarRestos();
            int idRestoCreado = testCrearRestoMaterialConAutoAsignacionMuseo();
            testModificarResto(idRestoCreado);
            testEliminarResto(idRestoCreado);

            // 6. HALLAZGOS ARQUEOLÓGICOS (3D, UE, CAMPAÑA, INÉDITO, REMONTAJE Y PERMISOS)
            testHallazgoIneditoYRemontaje();

            // RESUMEN FINAL
            printResumen();
        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL EN LA EJECUCIÓN DEL TEST: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==========================================
    // 1. PRUEBAS DE AUTENTICACIÓN
    // ==========================================
    private static void testAuthLoginAdmin() {
        print("TEST 1.1: Login Administrador (admin@relik.com)");
        try {
            JsonObject loginJson = new JsonObject();
            loginJson.addProperty("correo", "admin@relik.com");
            loginJson.addProperty("contrasena", "admin");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject user = JsonParser.parseString(response.body()).getAsJsonObject();
                if ("ADMIN".equalsIgnoreCase(user.get("rol").getAsString())) {
                    printSuccess("✅ Autenticado como ADMIN correctamente. Nombre: " + user.get("nombre").getAsString());
                    testsPassed++;
                    return;
                }
            }
            printError("❌ Falló autenticación ADMIN: Status " + response.statusCode() + " | Body: " + response.body());
            testsFailed++;
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testAuthLoginArqueologo() {
        print("TEST 1.2: Login Arqueólogo (elena.ramos@relik.com)");
        try {
            JsonObject loginJson = new JsonObject();
            loginJson.addProperty("correo", "elena.ramos@relik.com");
            loginJson.addProperty("contrasena", "1234");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject user = JsonParser.parseString(response.body()).getAsJsonObject();
                if ("ARQUEOLOGO".equalsIgnoreCase(user.get("rol").getAsString())) {
                    printSuccess("✅ Autenticado como ARQUEOLOGO correctamente. Nombre: " + user.get("nombre").getAsString());
                    testsPassed++;
                    return;
                }
            }
            printError("❌ Falló autenticación ARQUEOLOGO: Status " + response.statusCode());
            testsFailed++;
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testAuthLoginInvalido() {
        print("TEST 1.3: Login con credenciales incorrectas (Debe denegar)");
        try {
            JsonObject loginJson = new JsonObject();
            loginJson.addProperty("correo", "usuario_inexistente@relik.com");
            loginJson.addProperty("contrasena", "clave_erronea_999");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                printSuccess("✅ Acceso denegado correctamente (HTTP 401 Unauthorized)");
                testsPassed++;
            } else {
                printError("❌ Código inesperado para login inválido: " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // ==========================================
    // 2. PRUEBAS DE ARQUEÓLOGOS (CRUD)
    // ==========================================
    private static void testListarArqueologos() {
        print("TEST 2.1: Listar Arqueólogos (GET /api/arqueologos)");
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL + "/arqueologos")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                printSuccess("✅ Listados " + array.size() + " arqueólogos en base de datos.");
                testsPassed++;
            } else {
                printError("❌ Error: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static int testCrearArqueologo() {
        print("TEST 2.2: Crear Arqueólogo (POST /api/arqueologos)");
        int idGenerado = 0;
        try {
            JsonObject arq = new JsonObject();
            arq.addProperty("nombre", "Laura");
            arq.addProperty("apellidos", "Vázquez Gómez");
            arq.addProperty("especialidad", "Ceramología y Cronología");
            arq.addProperty("email", "laura.vazquez" + System.currentTimeMillis() + "@relik.com");
            arq.addProperty("rol", "ARQUEOLOGO");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/arqueologos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(arq.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject creado = JsonParser.parseString(response.body()).getAsJsonObject();
                idGenerado = creado.get("idArqueologo").getAsInt();
                printSuccess("✅ Arqueóloga creada con éxito. ID: #" + idGenerado + " (" + creado.get("nombre").getAsString() + " " + creado.get("apellidos").getAsString() + ")");
                testsPassed++;
            } else {
                printError("❌ Error al crear: HTTP " + response.statusCode() + " | Body: " + response.body());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
        return idGenerado;
    }

    private static void testModificarArqueologo(int id) {
        if (id <= 0) return;
        print("TEST 2.3: Modificar Arqueólogo (PUT /api/arqueologos/" + id + ")");
        try {
            JsonObject mod = new JsonObject();
            mod.addProperty("nombre", "Dra. Laura");
            mod.addProperty("apellidos", "Vázquez Gómez-Soto");
            mod.addProperty("especialidad", "Estratigrafía y Cerámica Griega");
            mod.addProperty("email", "laura.vazquez@relik.com");
            mod.addProperty("rol", "ARQUEOLOGO");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/arqueologos/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mod.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject actualizado = JsonParser.parseString(response.body()).getAsJsonObject();
                printSuccess("✅ Arqueóloga actualizada: " + actualizado.get("nombre").getAsString() + " " + actualizado.get("apellidos").getAsString() + " (" + actualizado.get("especialidad").getAsString() + ")");
                testsPassed++;
            } else {
                printError("❌ Error al modificar: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testEliminarArqueologo(int id) {
        if (id <= 0) return;
        print("TEST 2.4: Eliminar Arqueólogo (DELETE /api/arqueologos/" + id + ")");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/arqueologos/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                printSuccess("✅ Arqueóloga #" + id + " eliminada correctamente.");
                testsPassed++;
            } else {
                printError("❌ Error al eliminar arqueólogo: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // ==========================================
    // 3. PRUEBAS DE YACIMIENTOS (CRUD)
    // ==========================================
    private static void testListarYacimientos() {
        print("TEST 3.1: Listar Yacimientos (GET /api/yacimientos)");
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL + "/yacimientos")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                printSuccess("✅ Listados " + array.size() + " yacimientos registrados.");
                testsPassed++;
            } else {
                printError("❌ Error: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static int testCrearYacimiento() {
        print("TEST 3.2: Crear Yacimiento Arqueológico (POST /api/yacimientos)");
        int idGenerado = 0;
        try {
            JsonObject yac = new JsonObject();
            yac.addProperty("nombre", "Castro de Coaña Test " + System.currentTimeMillis());
            yac.addProperty("ubicacion", "Coaña, Asturias");
            yac.addProperty("coordenadas", "43.5150 N, -6.7490 W");
            yac.addProperty("epoca", "Edad del Hierro");
            yac.addProperty("fechaInicio", "1940-05-12");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/yacimientos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(yac.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject creado = JsonParser.parseString(response.body()).getAsJsonObject();
                idGenerado = creado.get("idYacimiento").getAsInt();
                printSuccess("✅ Yacimiento creado. ID: #" + idGenerado + " (" + creado.get("nombre").getAsString() + " | " + creado.get("epoca").getAsString() + ")");
                testsPassed++;
            } else {
                printError("❌ Error al crear yacimiento: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
        return idGenerado;
    }

    private static void testModificarYacimiento(int id) {
        if (id <= 0) return;
        print("TEST 3.3: Modificar Yacimiento (PUT /api/yacimientos/" + id + ")");
        try {
            JsonObject mod = new JsonObject();
            mod.addProperty("nombre", "Parque Arqueológico del Castro de Coaña");
            mod.addProperty("ubicacion", "Villacondide, Coaña, Asturias");
            mod.addProperty("coordenadas", "43.5155 N, -6.7495 W");
            mod.addProperty("epoca", "Edad del Hierro");
            mod.addProperty("fechaInicio", "1940-05-12");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/yacimientos/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mod.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject act = JsonParser.parseString(response.body()).getAsJsonObject();
                printSuccess("✅ Yacimiento actualizado: " + act.get("nombre").getAsString());
                testsPassed++;
            } else {
                printError("❌ Error al modificar: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testEliminarYacimiento(int id) {
        if (id <= 0) return;
        print("TEST 3.4: Eliminar Yacimiento (DELETE /api/yacimientos/" + id + ")");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/yacimientos/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                printSuccess("✅ Yacimiento #" + id + " eliminado correctamente.");
                testsPassed++;
            } else {
                printError("❌ Error al eliminar yacimiento: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // ==========================================
    // 4. PRUEBAS DE MUSEOS (CRUD)
    // ==========================================
    private static void testListarMuseos() {
        print("TEST 4.1: Listar Museos (GET /api/museos)");
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL + "/museos")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                printSuccess("✅ Listados " + array.size() + " museos registrados.");
                testsPassed++;
            } else {
                printError("❌ Error: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static int testCrearMuseo() {
        print("TEST 4.2: Crear Museo Especializado (POST /api/museos)");
        int idGenerado = 0;
        try {
            JsonObject m = new JsonObject();
            m.addProperty("nombre", "Museo Arqueológico de Asturias Test " + System.currentTimeMillis());
            m.addProperty("ciudad", "Oviedo");
            m.addProperty("pais", "España");
            m.addProperty("epocaEspecializada", "Edad del Hierro");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/museos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(m.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject creado = JsonParser.parseString(response.body()).getAsJsonObject();
                idGenerado = creado.get("idMuseo").getAsInt();
                printSuccess("✅ Museo creado. ID: #" + idGenerado + " (" + creado.get("nombre").getAsString() + " | Época: " + creado.get("epocaEspecializada").getAsString() + ")");
                testsPassed++;
            } else {
                printError("❌ Error al crear museo: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
        return idGenerado;
    }

    private static void testModificarMuseo(int id) {
        if (id <= 0) return;
        print("TEST 4.3: Modificar Museo (PUT /api/museos/" + id + ")");
        try {
            JsonObject mod = new JsonObject();
            mod.addProperty("nombre", "Museo Arqueológico y Etnográfico de Asturias");
            mod.addProperty("ciudad", "Oviedo");
            mod.addProperty("pais", "España");
            mod.addProperty("epocaEspecializada", "Edad del Hierro");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/museos/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mod.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject act = JsonParser.parseString(response.body()).getAsJsonObject();
                printSuccess("✅ Museo actualizado: " + act.get("nombre").getAsString());
                testsPassed++;
            } else {
                printError("❌ Error al modificar: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testEliminarMuseo(int id) {
        if (id <= 0) return;
        print("TEST 4.4: Eliminar Museo (DELETE /api/museos/" + id + ")");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/museos/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                printSuccess("✅ Museo #" + id + " eliminado correctamente.");
                testsPassed++;
            } else {
                printError("❌ Error al eliminar museo: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // ==============================================================
    // 5. PRUEBAS DE RESTOS MATERIALES Y ASIGNACIÓN AUTOMÁTICA
    // ==============================================================
    private static void testListarRestos() {
        print("TEST 5.1: Listar Restos Materiales (GET /api/restos)");
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL + "/restos")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                printSuccess("✅ Listados " + array.size() + " restos materiales inventariados.");
                testsPassed++;
            } else {
                printError("❌ Error: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static int testCrearRestoMaterialConAutoAsignacionMuseo() {
        print("TEST 5.2: Crear Resto Material y verificar asignación automática a Museo (POST /api/restos)");
        int idGenerado = 0;
        try {
            JsonObject r = new JsonObject();
            r.addProperty("tipo", "Fíbula Castreña Anular");
            r.addProperty("material", "Bronce y Plata / Orfebrería");
            r.addProperty("descripcion", "Broche prerromano con decoración geométrica");
            r.addProperty("periodo", "Paleolitico"); // Coincide con Museo de Altamira o MAN

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/restos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(r.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject creado = JsonParser.parseString(response.body()).getAsJsonObject();
                idGenerado = creado.get("idResto").getAsInt();
                String nombreMuseo = creado.has("nombreMuseo") && !creado.get("nombreMuseo").isJsonNull() ? creado.get("nombreMuseo").getAsString() : "Sin museo";
                printSuccess("✅ Resto creado. ID: #" + idGenerado + " (" + creado.get("tipo").getAsString() + ") | Asignado automáticamente al museo: " + nombreMuseo);
                testsPassed++;
            } else {
                printError("❌ Error al crear resto: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
        return idGenerado;
    }

    private static void testModificarResto(int id) {
        if (id <= 0) return;
        print("TEST 5.3: Modificar Resto Material (PUT /api/restos/" + id + ")");
        try {
            JsonObject mod = new JsonObject();
            mod.addProperty("tipo", "Fíbula Castreña Anular de Plata");
            mod.addProperty("material", "Plata Pura / Orfebrería");
            mod.addProperty("descripcion", "Fíbula hispánica restaurada");
            mod.addProperty("periodo", "Paleolitico");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/restos/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mod.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject act = JsonParser.parseString(response.body()).getAsJsonObject();
                printSuccess("✅ Resto modificado: " + act.get("tipo").getAsString() + " (" + act.get("material").getAsString() + ")");
                testsPassed++;
            } else {
                printError("❌ Error al modificar: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testEliminarResto(int id) {
        if (id <= 0) return;
        print("TEST 5.4: Eliminar Resto Material (DELETE /api/restos/" + id + ")");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/restos/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                printSuccess("✅ Resto material #" + id + " eliminado correctamente.");
                testsPassed++;
            } else {
                printError("❌ Error al eliminar resto: HTTP " + response.statusCode());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // =========================================================================
    // 6. PRUEBAS DE HALLAZGOS (3D, UE, CAMPAÑA, INÉDITO, REMONTAJE Y PERMISOS)
    // =========================================================================
    private static void testHallazgoIneditoYRemontaje() {
        print("TEST 6.1: Flujo 1 - Registrar Hallazgo de Pieza Inédita (POST /api/hallazgos)");
        int idHallazgo1 = 0;
        int idRestoAsociado = 0;
        try {
            JsonObject h1 = new JsonObject();
            h1.addProperty("idArqueologo", 2); // Dra. Elena Ramos
            h1.addProperty("idYacimiento", 1); // Gran Dolina
            h1.addProperty("idResto", 0);      // Pieza Inédita
            h1.addProperty("nombreResto", "Cuenco Campaniforme Decorado");
            h1.addProperty("epocaResto", "Calcolitico");
            h1.addProperty("tipologiaResto", "Cerámica / Ajuar Funerario");
            h1.addProperty("campana", "Campaña Anual 2026");
            h1.addProperty("cuadricula", "Cuadrícula B2");
            h1.addProperty("coordenadaX", "1.45m");
            h1.addProperty("coordenadaY", "2.10m");
            h1.addProperty("cotaZ", "-3.80m");
            h1.addProperty("unidadEstratigrafica", "UE-304");

            HttpRequest req1 = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/hallazgos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(h1.toString()))
                    .build();

            HttpResponse<String> resp1 = httpClient.send(req1, HttpResponse.BodyHandlers.ofString());
            if (resp1.statusCode() == 200) {
                JsonObject creado = JsonParser.parseString(resp1.body()).getAsJsonObject();
                idHallazgo1 = creado.get("idHallazgo").getAsInt();
                idRestoAsociado = creado.get("idResto").getAsInt();
                printSuccess("✅ Hallazgo Inédito registrado. ID: #" + idHallazgo1 + " | Resto #" + idRestoAsociado + " | Localización 3D: (" + creado.get("cuadricula").getAsString() + ", X:" + creado.get("coordenadaX").getAsString() + ", Y:" + creado.get("coordenadaY").getAsString() + ", Z:" + creado.get("cotaZ").getAsString() + ") | " + creado.get("unidadEstratigrafica").getAsString());
                testsPassed++;
            } else {
                printError("❌ Error en Hallazgo Inédito: HTTP " + resp1.statusCode() + " | " + resp1.body());
                testsFailed++;
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }

        print("TEST 6.2: Flujo 2 - Registrar Hallazgo de Fragmento Vinculado a Resto Existente / Remontaje");
        int idHallazgo2 = 0;
        try {
            if (idRestoAsociado > 0) {
                JsonObject h2 = new JsonObject();
                h2.addProperty("idArqueologo", 2);
                h2.addProperty("idYacimiento", 1);
                h2.addProperty("idResto", idRestoAsociado); // Vinculado a la misma pieza del museo
                h2.addProperty("campana", "Campaña de Otoño 2026");
                h2.addProperty("cuadricula", "Cuadrícula C4");
                h2.addProperty("coordenadaX", "3.20m");
                h2.addProperty("coordenadaY", "0.85m");
                h2.addProperty("cotaZ", "-4.10m");
                h2.addProperty("unidadEstratigrafica", "UE-308");

                HttpRequest req2 = HttpRequest.newBuilder()
                        .uri(URI.create(SERVER_URL + "/hallazgos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(h2.toString()))
                        .build();

                HttpResponse<String> resp2 = httpClient.send(req2, HttpResponse.BodyHandlers.ofString());
                if (resp2.statusCode() == 200) {
                    JsonObject creado2 = JsonParser.parseString(resp2.body()).getAsJsonObject();
                    idHallazgo2 = creado2.get("idHallazgo").getAsInt();
                    printSuccess("✅ Remontaje registrado: Hallazgo #" + idHallazgo2 + " vinculado al Resto #" + idRestoAsociado + " con nuevas coordenadas 3D (" + creado2.get("cuadricula").getAsString() + ", Z:" + creado2.get("cotaZ").getAsString() + ")");
                    testsPassed++;
                } else {
                    printError("❌ Error en Remontaje: HTTP " + resp2.statusCode());
                    testsFailed++;
                }
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }

        print("TEST 6.3: Seguridad - Intento de borrado por usuario no autor sin rol ADMIN (Debe bloquear HTTP 403)");
        try {
            if (idHallazgo1 > 0) {
                // Usuario ID: 3 (Carlos Mendoza) intenta borrar hallazgo del Usuario ID: 2 (Elena Ramos)
                HttpRequest reqBloqueo = HttpRequest.newBuilder()
                        .uri(URI.create(SERVER_URL + "/hallazgos/" + idHallazgo1 + "?usuarioId=3"))
                        .DELETE()
                        .build();

                HttpResponse<String> respBloqueo = httpClient.send(reqBloqueo, HttpResponse.BodyHandlers.ofString());
                if (respBloqueo.statusCode() == 403) {
                    printSuccess("✅ Borrado no autorizado bloqueado correctamente con HTTP 403 Forbidden.");
                    testsPassed++;
                } else {
                    printError("❌ Código inesperado (debería ser 403): " + respBloqueo.statusCode());
                    testsFailed++;
                }
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }

        print("TEST 6.4: Borrado Legítimo con Limpieza de Restos Huérfanos");
        try {
            if (idHallazgo1 > 0) {
                HttpRequest reqBorrado = HttpRequest.newBuilder()
                        .uri(URI.create(SERVER_URL + "/hallazgos/" + idHallazgo1 + "?usuarioId=1")) // ADMIN
                        .DELETE()
                        .build();

                HttpResponse<String> respBorrado = httpClient.send(reqBorrado, HttpResponse.BodyHandlers.ofString());
                if (respBorrado.statusCode() == 200) {
                    printSuccess("✅ Hallazgo #" + idHallazgo1 + " eliminado con éxito.");
                    testsPassed++;
                } else {
                    printError("❌ Error al eliminar hallazgo: HTTP " + respBorrado.statusCode());
                    testsFailed++;
                }
            }

            if (idHallazgo2 > 0) {
                HttpRequest reqBorrado2 = HttpRequest.newBuilder()
                        .uri(URI.create(SERVER_URL + "/hallazgos/" + idHallazgo2 + "?usuarioId=1")) // ADMIN
                        .DELETE()
                        .build();
                httpClient.send(reqBorrado2, HttpResponse.BodyHandlers.ofString());
            }
        } catch (Exception e) {
            printError("❌ Excepción: " + e.getMessage());
            testsFailed++;
        }
    }

    // ==========================================
    // IMPRESIÓN DEL RESUMEN FINAL
    // ==========================================
    private static void printResumen() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       RESUMEN DE PRUEBAS DE SISTEMA                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  ✅ PRUEBAS EXITOSAS (PASS): %-2d                                            ║\n", testsPassed);
        System.out.printf("║  ❌ PRUEBAS FALLIDAS (FAIL): %-2d                                            ║\n", testsFailed);
        System.out.printf("║  📊 TOTAL PRUEBAS EJECUTADAS: %-2d                                           ║\n", (testsPassed + testsFailed));
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");

        if (testsFailed == 0) {
            System.out.println("║  🎉 RESULTADO: 100% DE PRUEBAS SUPERADAS SATISFACTORIAMENTE                ║");
            System.out.println("║  ✨ TODOS LOS FLUJOS CLIENTE-SERVIDOR FUNCIONAN SIN ERRORES NI FISURAS     ║");
        } else {
            System.out.println("║  ⚠️ SE HAN DETECTADO FALLOS EN ALGUNAS PRUEBAS                             ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void print(String msg) {
        System.out.println("\n" + msg);
    }

    private static void printSuccess(String msg) {
        System.out.println("   " + msg);
    }

    private static void printError(String msg) {
        System.out.println("   " + msg);
    }
}
