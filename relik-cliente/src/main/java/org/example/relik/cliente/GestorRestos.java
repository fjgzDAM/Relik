package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.*;

public class GestorRestos extends GestorBase {
    private HttpClient httpClient;
    private Gson gson;

    private static final String[] EPOCAS_DISPONIBLES = new String[]{
        "Paleolitico", "Neolitico", "Calcolitico", "Edad del Bronce", "Edad del Hierro", "Romana", "Medieval", "General"
    };

    public GestorRestos(String serverUrl) {
        super(serverUrl, "Gestor de Restos Materiales", 1000, 600);
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

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        botonesPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JButton btnAgregar = UITheme.createButton("[+ Agregar Resto]", UITheme.COLOR_BTN_ACTION);
        JButton btnEditar = UITheme.createButton("[Editar Seleccionado]", UITheme.COLOR_BTN_SECONDARY);
        JButton btnEliminar = UITheme.createButton("[Eliminar Seleccionado]", UITheme.COLOR_BTN_DANGER);
        JButton btnRecargar = UITheme.createButton("[Recargar]", UITheme.COLOR_BTN_PRIMARY);

        btnAgregar.addActionListener(e -> agregarRegistro());
        btnEditar.addActionListener(e -> editarRegistro());
        btnEliminar.addActionListener(e -> eliminarRegistro());
        btnRecargar.addActionListener(e -> cargarDatos());

        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnRecargar);

        crearTabla(new String[]{"ID", "Objeto/Resto", "Tipología/Material", "Descripción", "Época/Período", "Museo Asignado"});
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
                    .uri(URI.create(serverUrl + "/restos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                tableModel.setRowCount(0);
                JsonArray restos = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement element : restos) {
                    JsonObject resto = element.getAsJsonObject();

                    String tipo = resto.has("tipo") && !resto.get("tipo").isJsonNull() ? resto.get("tipo").getAsString() :
                                 (resto.has("nombre") && !resto.get("nombre").isJsonNull() ? resto.get("nombre").getAsString() : "");

                    String material = resto.has("material") && !resto.get("material").isJsonNull() ? resto.get("material").getAsString() :
                                     (resto.has("tipologia") && !resto.get("tipologia").isJsonNull() ? resto.get("tipologia").getAsString() : "");

                    String desc = resto.has("descripcion") && !resto.get("descripcion").isJsonNull() ? resto.get("descripcion").getAsString() : "";

                    String periodo = resto.has("periodo") && !resto.get("periodo").isJsonNull() ? resto.get("periodo").getAsString() :
                                    (resto.has("epoca") && !resto.get("epoca").isJsonNull() ? resto.get("epoca").getAsString() : "");

                    String museo = resto.has("nombreMuseo") && !resto.get("nombreMuseo").isJsonNull() ? resto.get("nombreMuseo").getAsString() :
                                  (resto.has("museo") && resto.get("museo").isJsonObject() && resto.getAsJsonObject("museo").has("nombre") ? resto.getAsJsonObject("museo").get("nombre").getAsString() : "Sin museo");

                    tableModel.addRow(new Object[]{
                            resto.has("idResto") && !resto.get("idResto").isJsonNull() ? resto.get("idResto").getAsLong() : 0L,
                            tipo,
                            material,
                            desc,
                            periodo,
                            museo
                    });
                }
            }
        } catch (Exception e) {
            mostrarError("Error al cargar restos: " + e.getMessage());
        }
    }

    @Override
    protected void agregarRegistro() {
        JDialog dialog = new JDialog(this, "Nuevo Resto Material", true);
        dialog.setSize(440, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtTipo = new JTextField();
        JTextField txtMaterial = new JTextField("Herramienta / Cerámica / Metal");
        JTextField txtDesc = new JTextField();
        JComboBox<String> cmbPeriodo = new JComboBox<>(EPOCAS_DISPONIBLES);

        panel.add(new JLabel("Nombre Objeto/Resto:"));
        panel.add(txtTipo);
        panel.add(new JLabel("Tipología / Material:"));
        panel.add(txtMaterial);
        panel.add(new JLabel("Descripción detallada:"));
        panel.add(txtDesc);
        panel.add(new JLabel("Época / Período:"));
        panel.add(cmbPeriodo);

        JButton btnGuardar = UITheme.createButton("Guardar", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                String tipo = txtTipo.getText().trim();
                if (tipo.isEmpty()) {
                    mostrarError("Introduce el nombre del resto material.");
                    return;
                }

                JsonObject resto = new JsonObject();
                resto.addProperty("tipo", tipo);
                resto.addProperty("material", txtMaterial.getText().trim());
                resto.addProperty("descripcion", txtDesc.getText().trim());
                resto.addProperty("periodo", (String) cmbPeriodo.getSelectedItem());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/restos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(resto.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Resto material creado y asignado al museo de su época.");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error al crear resto: " + response.body());
                }
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    @Override
    protected void editarRegistro() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un resto material de la tabla");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);
        String tipo = (String) tableModel.getValueAt(fila, 1);
        String material = (String) tableModel.getValueAt(fila, 2);
        String desc = (String) tableModel.getValueAt(fila, 3);
        String periodo = (String) tableModel.getValueAt(fila, 4);

        JDialog dialog = new JDialog(this, "Editar Resto Material", true);
        dialog.setSize(440, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtTipo = new JTextField(tipo);
        JTextField txtMaterial = new JTextField(material);
        JTextField txtDesc = new JTextField(desc);
        JComboBox<String> cmbPeriodo = new JComboBox<>(EPOCAS_DISPONIBLES);
        cmbPeriodo.setSelectedItem(periodo);

        panel.add(new JLabel("Nombre Objeto/Resto:"));
        panel.add(txtTipo);
        panel.add(new JLabel("Tipología / Material:"));
        panel.add(txtMaterial);
        panel.add(new JLabel("Descripción detallada:"));
        panel.add(txtDesc);
        panel.add(new JLabel("Época / Período:"));
        panel.add(cmbPeriodo);

        JButton btnGuardar = UITheme.createButton("Guardar Cambios", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject resto = new JsonObject();
                resto.addProperty("tipo", txtTipo.getText().trim());
                resto.addProperty("material", txtMaterial.getText().trim());
                resto.addProperty("descripcion", txtDesc.getText().trim());
                resto.addProperty("periodo", (String) cmbPeriodo.getSelectedItem());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/restos/" + id))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(resto.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Resto material actualizado correctamente.");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error al actualizar: " + response.body());
                }
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    @Override
    protected void eliminarRegistro() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un resto material");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);

        if (mostrarConfirmacion("¿Eliminar este resto material?") == JOptionPane.YES_OPTION) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/restos/" + id))
                        .DELETE()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Resto material eliminado correctamente.");
                    cargarDatos();
                } else {
                    mostrarError("Error al eliminar: " + response.body());
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }
}
