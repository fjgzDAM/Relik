package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

public class GestorHallazgos extends GestorBase {
    private HttpClient httpClient;
    private Gson gson;

    private class YacimientoItem {
        long id;
        String nombre;
        public YacimientoItem(long id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return nombre; }
    }

    private class RestoItem {
        long id;
        String nombre;
        String epoca;
        String tipologia;
        public RestoItem(long id, String nombre, String epoca, String tipologia) {
            this.id = id;
            this.nombre = nombre;
            this.epoca = epoca;
            this.tipologia = tipologia;
        }
        @Override public String toString() {
            return "#" + id + " - " + nombre + " (" + epoca + " / " + tipologia + ")";
        }
    }

    public GestorHallazgos(String serverUrl) {
        super(serverUrl, "Gestor de Hallazgos Arqueológicos de Campo (Campaña, Micro-localización 3D y UE)", 1400, 680);
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();

        inicializarUI();
        cargarDatos();
        setVisible(true);
    }

    private void inicializarUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        // Barra de botones con 2 acciones de registro independientes
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        botonesPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JButton btnNuevoInedito = UITheme.createButton("[+ Registrar Resto Inédito]", UITheme.COLOR_BTN_ACTION);
        JButton btnVincularExistente = UITheme.createButton("[+ Vincular a Resto Existente]", UITheme.COLOR_BTN_SECONDARY);
        JButton btnEliminar = UITheme.createButton("[Eliminar Hallazgo]", UITheme.COLOR_BTN_DANGER);
        JButton btnRecargar = UITheme.createButton("[Recargar Lista]", UITheme.COLOR_BTN_PRIMARY);

        btnNuevoInedito.addActionListener(e -> registrarHallazgoInedito());
        btnVincularExistente.addActionListener(e -> vincularFragmentoExistente());
        btnEliminar.addActionListener(e -> eliminarRegistro());
        btnRecargar.addActionListener(e -> cargarDatos());

        botonesPanel.add(btnNuevoInedito);
        botonesPanel.add(btnVincularExistente);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnRecargar);

        crearTabla(new String[]{"ID", "ID Arq", "Arqueólogo", "Yacimiento", "Campaña", "Resto Material", "Cuadrícula / Ejes X, Y", "Profundidad (Cota Z)", "Unidad Estratigráfica (UE)", "Fecha y Hora", "Info y Museo"});
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(botonesPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    @Override
    protected void cargarDatos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/hallazgos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                tableModel.setRowCount(0);
                JsonArray hallazgos = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement element : hallazgos) {
                    JsonObject h = element.getAsJsonObject();

                    long id = h.has("idHallazgo") && !h.get("idHallazgo").isJsonNull() ? h.get("idHallazgo").getAsLong() : 0;
                    
                    long idArq = h.has("idArqueologo") && !h.get("idArqueologo").isJsonNull() ? h.get("idArqueologo").getAsLong() :
                                (h.has("arqueologo") && h.get("arqueologo").isJsonObject() && h.getAsJsonObject("arqueologo").has("idArqueologo") ? h.getAsJsonObject("arqueologo").get("idArqueologo").getAsLong() : 0);

                    String arqueologo = h.has("nombreArqueologo") && !h.get("nombreArqueologo").isJsonNull() ? h.get("nombreArqueologo").getAsString() :
                                       (h.has("arqueologo") && h.get("arqueologo").isJsonObject() && h.getAsJsonObject("arqueologo").has("nombre") ? h.getAsJsonObject("arqueologo").get("nombre").getAsString() : "N/A");

                    String yacimiento = h.has("nombreYacimiento") && !h.get("nombreYacimiento").isJsonNull() ? h.get("nombreYacimiento").getAsString() :
                                       (h.has("yacimiento") && h.get("yacimiento").isJsonObject() && h.getAsJsonObject("yacimiento").has("nombre") ? h.getAsJsonObject("yacimiento").get("nombre").getAsString() : "N/A");

                    String resto = h.has("tipoResto") && !h.get("tipoResto").isJsonNull() ? h.get("tipoResto").getAsString() :
                                  (h.has("restoMaterial") && h.get("restoMaterial").isJsonObject() && h.getAsJsonObject("restoMaterial").has("nombre") ? h.getAsJsonObject("restoMaterial").get("nombre").getAsString() : "N/A");

                    String campana = h.has("campana") && !h.get("campana").isJsonNull() ? h.get("campana").getAsString() : "Campaña 2026";
                    String cuadricula = h.has("cuadricula") && !h.get("cuadricula").isJsonNull() ? h.get("cuadricula").getAsString() : "Cuadrícula A1";
                    String coordX = h.has("coordenadaX") && !h.get("coordenadaX").isJsonNull() ? h.get("coordenadaX").getAsString() : "0.5m";
                    String coordY = h.has("coordenadaY") && !h.get("coordenadaY").isJsonNull() ? h.get("coordenadaY").getAsString() : "0.5m";
                    String cotaZ = h.has("cotaZ") && !h.get("cotaZ").isJsonNull() ? h.get("cotaZ").getAsString() : "-1.2m";
                    String ue = h.has("unidadEstratigrafica") && !h.get("unidadEstratigrafica").isJsonNull() ? h.get("unidadEstratigrafica").getAsString() : "UE-101";

                    String posicionPlana = cuadricula + " (X: " + coordX + ", Y: " + coordY + ")";
                    String fecha = h.has("fechaHallazgo") && !h.get("fechaHallazgo").isJsonNull() ? h.get("fechaHallazgo").getAsString() : "N/A";
                    String desc = h.has("descripcion") && !h.get("descripcion").isJsonNull() ? h.get("descripcion").getAsString() : "";

                    tableModel.addRow(new Object[]{
                            id,
                            idArq,
                            arqueologo,
                            yacimiento,
                            campana,
                            resto,
                            posicionPlana,
                            cotaZ,
                            ue,
                            fecha,
                            desc
                    });
                }
            }
        } catch (Exception e) {
            mostrarError("Error al cargar hallazgos: " + e.getMessage());
        }
    }

    @Override
    protected void agregarRegistro() {
        registrarHallazgoInedito();
    }

    // ==========================================
    // INTERFAZ 1: REGISTRAR RESTO INÉDITO (NUEVO)
    // ==========================================
    private void registrarHallazgoInedito() {
        List<YacimientoItem> yacimientos = obtenerYacimientosServidor();
        if (yacimientos.isEmpty()) {
            mostrarError("No hay yacimientos disponibles. Un administrador debe crear al menos un yacimiento.");
            return;
        }

        JDialog dialog = new JDialog(this, "Registrar Hallazgo de Pieza Inédita (Nuevo Resto)", true);
        dialog.setSize(520, 480);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(10, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JComboBox<YacimientoItem> cmbYacimientos = new JComboBox<>(yacimientos.toArray(new YacimientoItem[0]));
        JTextField txtCampana = new JTextField("Campaña Anual 2026");
        JTextField txtNombreResto = new JTextField();
        JComboBox<String> cmbEpoca = new JComboBox<>(new String[]{"Paleolitico", "Neolitico", "Calcolitico", "Edad del Bronce", "Edad del Hierro", "Romana", "Medieval", "General"});
        JTextField txtTipologia = new JTextField("Herramienta / Cerámica / Orfebrería");

        JTextField txtCuadricula = new JTextField("Cuadrícula A1");
        JTextField txtCoordX = new JTextField("0.85m");
        JTextField txtCoordY = new JTextField("1.40m");
        JTextField txtCotaZ = new JTextField("-2.10m");
        JTextField txtUE = new JTextField("UE-104");

        panel.add(new JLabel("Yacimiento:"));
        panel.add(cmbYacimientos);
        panel.add(new JLabel("Campaña de Excavación:"));
        panel.add(txtCampana);
        panel.add(new JLabel("Nombre de la Nueva Pieza:"));
        panel.add(txtNombreResto);
        panel.add(new JLabel("Época Histórica:"));
        panel.add(cmbEpoca);
        panel.add(new JLabel("Tipología / Material:"));
        panel.add(txtTipologia);
        panel.add(new JLabel("Cuadrícula / Sector:"));
        panel.add(txtCuadricula);
        panel.add(new JLabel("Coordenadas Eje X:"));
        panel.add(txtCoordX);
        panel.add(new JLabel("Coordenadas Eje Y:"));
        panel.add(txtCoordY);
        panel.add(new JLabel("Cota Profundidad Z:"));
        panel.add(txtCotaZ);
        panel.add(new JLabel("Unidad Estratigráfica (UE):"));
        panel.add(txtUE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JButton btnGuardar = UITheme.createButton("Guardar Pieza y Hallazgo", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            String nombreResto = txtNombreResto.getText().trim();
            if (nombreResto.isEmpty()) {
                mostrarError("Introduce el nombre de la pieza inédita.");
                return;
            }

            YacimientoItem yac = (YacimientoItem) cmbYacimientos.getSelectedItem();
            if (yac == null) {
                mostrarError("Selecciona un yacimiento.");
                return;
            }

            try {
                JsonObject json = new JsonObject();
                json.addProperty("idArqueologo", SessionManager.getInstance().getIdArqueologo());
                json.addProperty("idYacimiento", yac.id);
                json.addProperty("idResto", 0); // Indica crear nueva pieza
                json.addProperty("nombreResto", nombreResto);
                json.addProperty("epocaResto", (String) cmbEpoca.getSelectedItem());
                json.addProperty("tipologiaResto", txtTipologia.getText().trim().isEmpty() ? "General" : txtTipologia.getText().trim());
                json.addProperty("campana", txtCampana.getText().trim().isEmpty() ? "Campaña 2026" : txtCampana.getText().trim());
                json.addProperty("cuadricula", txtCuadricula.getText().trim().isEmpty() ? "S/C" : txtCuadricula.getText().trim());
                json.addProperty("coordenadaX", txtCoordX.getText().trim().isEmpty() ? "0.0m" : txtCoordX.getText().trim());
                json.addProperty("coordenadaY", txtCoordY.getText().trim().isEmpty() ? "0.0m" : txtCoordY.getText().trim());
                json.addProperty("cotaZ", txtCotaZ.getText().trim().isEmpty() ? "0.0m" : txtCotaZ.getText().trim());
                json.addProperty("unidadEstratigrafica", txtUE.getText().trim().isEmpty() ? "UE-100" : txtUE.getText().trim());
                json.addProperty("fechaHallazgo", LocalDateTime.now().toString());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/hallazgos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Pieza inédita catalogada y asignada al museo con éxito.");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error al registrar hallazgo: " + response.body());
                }
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ==============================================================
    // INTERFAZ 2: VINCULAR HALLAZGO A RESTO EXISTENTE (REMONTAJE)
    // ==============================================================
    private void vincularFragmentoExistente() {
        List<RestoItem> restosExistentes = obtenerRestosServidor();
        if (restosExistentes.isEmpty()) {
            mostrarError("No hay restos materiales registrados en el catálogo. Registra primero una pieza inédita.");
            return;
        }

        List<YacimientoItem> yacimientos = obtenerYacimientosServidor();
        if (yacimientos.isEmpty()) {
            mostrarError("No hay yacimientos disponibles.");
            return;
        }

        JDialog dialog = new JDialog(this, "Vincular Hallazgo a Resto Material Existente (Remontaje / Fragmento)", true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JComboBox<RestoItem> cmbRestos = new JComboBox<>(restosExistentes.toArray(new RestoItem[0]));
        JComboBox<YacimientoItem> cmbYacimientos = new JComboBox<>(yacimientos.toArray(new YacimientoItem[0]));
        JTextField txtCampana = new JTextField("Campaña Anual 2026");

        JTextField txtCuadricula = new JTextField("Cuadrícula B2");
        JTextField txtCoordX = new JTextField("1.20m");
        JTextField txtCoordY = new JTextField("0.90m");
        JTextField txtCotaZ = new JTextField("-1.80m");
        JTextField txtUE = new JTextField("UE-102");

        panel.add(new JLabel("Pieza Catalogada (Resto):"));
        panel.add(cmbRestos);
        panel.add(new JLabel("Yacimiento:"));
        panel.add(cmbYacimientos);
        panel.add(new JLabel("Campaña de Excavación:"));
        panel.add(txtCampana);
        panel.add(new JLabel("Cuadrícula / Sector:"));
        panel.add(txtCuadricula);
        panel.add(new JLabel("Coordenadas Eje X:"));
        panel.add(txtCoordX);
        panel.add(new JLabel("Coordenadas Eje Y:"));
        panel.add(txtCoordY);
        panel.add(new JLabel("Cota Profundidad Z:"));
        panel.add(txtCotaZ);
        panel.add(new JLabel("Unidad Estratigráfica (UE):"));
        panel.add(txtUE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JButton btnGuardar = UITheme.createButton("Vincular Hallazgo", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            RestoItem restoSeleccionado = (RestoItem) cmbRestos.getSelectedItem();
            YacimientoItem yac = (YacimientoItem) cmbYacimientos.getSelectedItem();

            if (restoSeleccionado == null || yac == null) {
                mostrarError("Selecciona el resto catalogado y el yacimiento.");
                return;
            }

            try {
                JsonObject json = new JsonObject();
                json.addProperty("idArqueologo", SessionManager.getInstance().getIdArqueologo());
                json.addProperty("idYacimiento", yac.id);
                json.addProperty("idResto", restoSeleccionado.id); // Vincula al resto existente
                json.addProperty("campana", txtCampana.getText().trim().isEmpty() ? "Campaña 2026" : txtCampana.getText().trim());
                json.addProperty("cuadricula", txtCuadricula.getText().trim().isEmpty() ? "S/C" : txtCuadricula.getText().trim());
                json.addProperty("coordenadaX", txtCoordX.getText().trim().isEmpty() ? "0.0m" : txtCoordX.getText().trim());
                json.addProperty("coordenadaY", txtCoordY.getText().trim().isEmpty() ? "0.0m" : txtCoordY.getText().trim());
                json.addProperty("cotaZ", txtCotaZ.getText().trim().isEmpty() ? "0.0m" : txtCotaZ.getText().trim());
                json.addProperty("unidadEstratigrafica", txtUE.getText().trim().isEmpty() ? "UE-100" : txtUE.getText().trim());
                json.addProperty("fechaHallazgo", LocalDateTime.now().toString());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/hallazgos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Hallazgo vinculado con éxito al resto #" + restoSeleccionado.id + " (" + restoSeleccionado.nombre + ").");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error al vincular hallazgo: " + response.body());
                }
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private List<YacimientoItem> obtenerYacimientosServidor() {
        List<YacimientoItem> lista = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/yacimientos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                for (JsonElement elem : array) {
                    JsonObject obj = elem.getAsJsonObject();
                    long id = obj.get("idYacimiento").getAsLong();
                    String nombre = obj.get("nombre").getAsString();
                    lista.add(new YacimientoItem(id, nombre));
                }
            }
        } catch (Exception ignored) {}
        return lista;
    }

    private List<RestoItem> obtenerRestosServidor() {
        List<RestoItem> lista = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/restos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                for (JsonElement elem : array) {
                    JsonObject obj = elem.getAsJsonObject();
                    long id = obj.has("idResto") ? obj.get("idResto").getAsLong() : 0L;
                    String tipo = obj.has("tipo") && !obj.get("tipo").isJsonNull() ? obj.get("tipo").getAsString() : (obj.has("nombre") ? obj.get("nombre").getAsString() : "Sin nombre");
                    String epoca = obj.has("periodo") && !obj.get("periodo").isJsonNull() ? obj.get("periodo").getAsString() : (obj.has("epoca") ? obj.get("epoca").getAsString() : "General");
                    String tipologia = obj.has("material") && !obj.get("material").isJsonNull() ? obj.get("material").getAsString() : "General";
                    lista.add(new RestoItem(id, tipo, epoca, tipologia));
                }
            }
        } catch (Exception ignored) {}
        return lista;
    }

    @Override
    protected void editarRegistro() {
        mostrarError("Para modificar un hallazgo, elimínalo y vuelve a registrarlo si dispones de permisos.");
    }

    @Override
    protected void eliminarRegistro() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un hallazgo de la tabla");
            return;
        }

        Long idHallazgo = (Long) tableModel.getValueAt(fila, 0);
        Long idArqueologoCreador = (Long) tableModel.getValueAt(fila, 1);
        long idUsuarioLogueado = SessionManager.getInstance().getIdArqueologo();
        boolean isAdmin = SessionManager.getInstance().isAdmin();

        if (!isAdmin && idArqueologoCreador != idUsuarioLogueado) {
            mostrarError("No tienes permiso para borrar este hallazgo.\nSolo puedes borrar los hallazgos que hayas creado tú mismo (ID: " + idUsuarioLogueado + ").");
            return;
        }

        if (mostrarConfirmacion("¿Eliminar hallazgo #" + idHallazgo + "?") == JOptionPane.YES_OPTION) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/hallazgos/" + idHallazgo + "?usuarioId=" + idUsuarioLogueado))
                        .DELETE()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Hallazgo eliminado correctamente.");
                    cargarDatos();
                } else {
                    mostrarError("Error del servidor: " + response.body());
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }
}
