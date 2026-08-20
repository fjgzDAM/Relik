package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.*;

public class GestorMuseos extends GestorBase {
    private HttpClient httpClient;
    private Gson gson;

    private static final String[] EPOCAS_DISPONIBLES = new String[]{
        "Paleolitico", "Neolitico", "Calcolitico", "Edad del Bronce", "Edad del Hierro", "Romana", "Medieval", "General"
    };

    public GestorMuseos(String serverUrl) {
        super(serverUrl, "Gestor de Museos", 900, 600);
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

        JButton btnAgregar = UITheme.createButton("[+ Agregar Museo]", UITheme.COLOR_BTN_ACTION);
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

        crearTabla(new String[]{"ID", "Nombre Museo", "Ciudad", "País", "Época Especializada"});
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
                    .uri(URI.create(serverUrl + "/museos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                tableModel.setRowCount(0);
                JsonArray museos = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement element : museos) {
                    JsonObject museo = element.getAsJsonObject();

                    String ciudad = museo.has("ciudad") && !museo.get("ciudad").isJsonNull() ? museo.get("ciudad").getAsString() :
                                   (museo.has("localizacion") && !museo.get("localizacion").isJsonNull() ? museo.get("localizacion").getAsString() : "");

                    String pais = museo.has("pais") && !museo.get("pais").isJsonNull() ? museo.get("pais").getAsString() :
                                 (museo.has("direccion") && !museo.get("direccion").isJsonNull() ? museo.get("direccion").getAsString() : "");

                    String epoca = museo.has("epocaEspecializada") && !museo.get("epocaEspecializada").isJsonNull() ? museo.get("epocaEspecializada").getAsString() :
                                  (museo.has("especialidad") && !museo.get("especialidad").isJsonNull() ? museo.get("especialidad").getAsString() :
                                  (museo.has("contacto") && !museo.get("contacto").isJsonNull() ? museo.get("contacto").getAsString() : "General"));

                    tableModel.addRow(new Object[]{
                            museo.has("idMuseo") && !museo.get("idMuseo").isJsonNull() ? museo.get("idMuseo").getAsLong() : 0L,
                            museo.has("nombre") && !museo.get("nombre").isJsonNull() ? museo.get("nombre").getAsString() : "",
                            ciudad,
                            pais,
                            epoca
                    });
                }
            } else {
                mostrarError("Error cargando museos: " + response.statusCode());
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @Override
    protected void agregarRegistro() {
        if (!SessionManager.getInstance().isAdmin()) {
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede crear nuevos museos.");
            return;
        }
        JDialog dialog = new JDialog(this, "Nuevo Museo", true);
        dialog.setSize(440, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtNombre = new JTextField();
        JTextField txtCiudad = new JTextField();
        JTextField txtPais = new JTextField("España");
        JComboBox<String> cmbEpoca = new JComboBox<>(EPOCAS_DISPONIBLES);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ciudad:"));
        panel.add(txtCiudad);
        panel.add(new JLabel("País:"));
        panel.add(txtPais);
        panel.add(new JLabel("Época especializada:"));
        panel.add(cmbEpoca);

        JButton btnGuardar = UITheme.createButton("Guardar", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    mostrarError("Introduce el nombre del museo.");
                    return;
                }

                JsonObject museo = new JsonObject();
                museo.addProperty("nombre", nombre);
                museo.addProperty("ciudad", txtCiudad.getText().trim());
                museo.addProperty("pais", txtPais.getText().trim());
                museo.addProperty("epocaEspecializada", (String) cmbEpoca.getSelectedItem());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/museos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(museo.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Museo creado correctamente.");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error: " + response.body());
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
        if (!SessionManager.getInstance().isAdmin()) {
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede editar museos.");
            return;
        }
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un museo");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);
        String nombre = (String) tableModel.getValueAt(fila, 1);
        String ciudad = (String) tableModel.getValueAt(fila, 2);
        String pais = (String) tableModel.getValueAt(fila, 3);
        String epoca = (String) tableModel.getValueAt(fila, 4);

        JDialog dialog = new JDialog(this, "Editar Museo", true);
        dialog.setSize(440, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtNombre = new JTextField(nombre);
        JTextField txtCiudad = new JTextField(ciudad);
        JTextField txtPais = new JTextField(pais);
        JComboBox<String> cmbEpoca = new JComboBox<>(EPOCAS_DISPONIBLES);
        cmbEpoca.setSelectedItem(epoca);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ciudad:"));
        panel.add(txtCiudad);
        panel.add(new JLabel("País:"));
        panel.add(txtPais);
        panel.add(new JLabel("Época especializada:"));
        panel.add(cmbEpoca);

        JButton btnGuardar = UITheme.createButton("Guardar Cambios", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject museo = new JsonObject();
                museo.addProperty("nombre", txtNombre.getText().trim());
                museo.addProperty("ciudad", txtCiudad.getText().trim());
                museo.addProperty("pais", txtPais.getText().trim());
                museo.addProperty("epocaEspecializada", (String) cmbEpoca.getSelectedItem());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/museos/" + id))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(museo.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Museo actualizado correctamente.");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error: " + response.body());
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
        if (!SessionManager.getInstance().isAdmin()) {
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede eliminar museos.");
            return;
        }
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un museo");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);

        if (mostrarConfirmacion("¿Eliminar este museo?") == JOptionPane.YES_OPTION) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/museos/" + id))
                        .DELETE()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Museo eliminado correctamente.");
                    cargarDatos();
                } else {
                    mostrarError("Error: " + response.body());
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }
}
