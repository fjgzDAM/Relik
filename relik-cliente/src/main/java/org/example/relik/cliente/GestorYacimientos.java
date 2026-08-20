package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.*;

public class GestorYacimientos extends GestorBase {
    private HttpClient httpClient;
    private Gson gson;

    public GestorYacimientos(String serverUrl) {
        super(serverUrl, "Gestor de Yacimientos Arqueológicos", 950, 600);
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

        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        botonesPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JButton btnAgregar = UITheme.createButton("[+ Agregar Yacimiento]", UITheme.COLOR_BTN_ACTION);
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

        // Tabla con columnas incluyendo Época Principal
        crearTabla(new String[]{"ID", "Nombre Yacimiento", "Ubicación", "Coordenadas GPS", "Época Principal", "Fecha Inicio"});
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
                    .uri(URI.create(serverUrl + "/yacimientos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                tableModel.setRowCount(0);
                JsonArray yacimientos = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement element : yacimientos) {
                    JsonObject yac = element.getAsJsonObject();
                    
                    String ubicacion = yac.has("ubicacion") && !yac.get("ubicacion").isJsonNull() ? yac.get("ubicacion").getAsString() :
                                      (yac.has("localizacion") && !yac.get("localizacion").isJsonNull() ? yac.get("localizacion").getAsString() : "");
                                      
                    String coordenadas = yac.has("coordenadas") && !yac.get("coordenadas").isJsonNull() ? yac.get("coordenadas").getAsString() :
                                         (yac.has("descripcion") && !yac.get("descripcion").isJsonNull() ? yac.get("descripcion").getAsString() : "");
                                         
                    String epoca = yac.has("epoca") && !yac.get("epoca").isJsonNull() ? yac.get("epoca").getAsString() : "General";

                    String fecha = yac.has("fechaInicio") && !yac.get("fechaInicio").isJsonNull() ? yac.get("fechaInicio").getAsString() :
                                  (yac.has("periodo") && !yac.get("periodo").isJsonNull() ? yac.get("periodo").getAsString() : "");

                    tableModel.addRow(new Object[]{
                            yac.has("idYacimiento") && !yac.get("idYacimiento").isJsonNull() ? yac.get("idYacimiento").getAsLong() : 0L,
                            yac.has("nombre") && !yac.get("nombre").isJsonNull() ? yac.get("nombre").getAsString() : "",
                            ubicacion,
                            coordenadas,
                            epoca,
                            fecha
                    });
                }
            } else {
                mostrarError("Error al cargar yacimientos: " + response.statusCode());
            }
        } catch (Exception e) {
            mostrarError("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void agregarRegistro() {
        if (!SessionManager.getInstance().isAdmin()) {
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede crear nuevos yacimientos.");
            return;
        }
        JDialog dialog = new JDialog(this, "Nuevo Yacimiento Arqueológico", true);
        dialog.setSize(440, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtNombre = new JTextField();
        JTextField txtUbic = new JTextField();
        JTextField txtCoord = new JTextField();
        JComboBox<String> cmbEpoca = new JComboBox<>(new String[]{"Paleolitico", "Neolitico", "Calcolitico", "Edad del Bronce", "Edad del Hierro", "Romana", "Medieval", "Indiferenciada"});
        JTextField txtFecha = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ubicación/Localidad:"));
        panel.add(txtUbic);
        panel.add(new JLabel("Coordenadas GPS:"));
        panel.add(txtCoord);
        panel.add(new JLabel("Época Principal:"));
        panel.add(cmbEpoca);
        panel.add(new JLabel("Fecha inicio (YYYY-MM-DD):"));
        panel.add(txtFecha);

        JButton btnGuardar = UITheme.createButton("Guardar", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject yacimiento = new JsonObject();
                yacimiento.addProperty("nombre", txtNombre.getText());
                yacimiento.addProperty("ubicacion", txtUbic.getText());
                yacimiento.addProperty("localizacion", txtUbic.getText());
                yacimiento.addProperty("coordenadas", txtCoord.getText());
                yacimiento.addProperty("descripcion", txtCoord.getText());
                yacimiento.addProperty("epoca", (String) cmbEpoca.getSelectedItem());
                if (!txtFecha.getText().isBlank()) {
                    yacimiento.addProperty("fechaInicio", txtFecha.getText());
                    yacimiento.addProperty("periodo", txtFecha.getText());
                }

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/yacimientos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(yacimiento.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Yacimiento creado correctamente");
                    dialog.dispose();
                    cargarDatos();
                } else {
                    mostrarError("Error al crear yacimiento: " + response.body());
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
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede editar yacimientos.");
            return;
        }
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un yacimiento para editar");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);
        String nombre = (String) tableModel.getValueAt(fila, 1);
        String ubic = (String) tableModel.getValueAt(fila, 2);
        String coord = (String) tableModel.getValueAt(fila, 3);
        String epoca = (String) tableModel.getValueAt(fila, 4);
        String fecha = (String) tableModel.getValueAt(fila, 5);

        JDialog dialog = new JDialog(this, "Editar Yacimiento", true);
        dialog.setSize(440, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JTextField txtNombre = new JTextField(nombre);
        JTextField txtUbic = new JTextField(ubic);
        JTextField txtCoord = new JTextField(coord);
        JComboBox<String> cmbEpoca = new JComboBox<>(new String[]{"Paleolitico", "Neolitico", "Calcolitico", "Edad del Bronce", "Edad del Hierro", "Romana", "Medieval", "Indiferenciada"});
        cmbEpoca.setSelectedItem(epoca);
        JTextField txtFecha = new JTextField(fecha);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ubicación/Localidad:"));
        panel.add(txtUbic);
        panel.add(new JLabel("Coordenadas GPS:"));
        panel.add(txtCoord);
        panel.add(new JLabel("Época Principal:"));
        panel.add(cmbEpoca);
        panel.add(new JLabel("Fecha inicio (YYYY-MM-DD):"));
        panel.add(txtFecha);

        JButton btnGuardar = UITheme.createButton("Guardar", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject yacimiento = new JsonObject();
                yacimiento.addProperty("nombre", txtNombre.getText());
                yacimiento.addProperty("ubicacion", txtUbic.getText());
                yacimiento.addProperty("localizacion", txtUbic.getText());
                yacimiento.addProperty("coordenadas", txtCoord.getText());
                yacimiento.addProperty("descripcion", txtCoord.getText());
                yacimiento.addProperty("epoca", (String) cmbEpoca.getSelectedItem());
                if (!txtFecha.getText().isBlank()) {
                    yacimiento.addProperty("fechaInicio", txtFecha.getText());
                    yacimiento.addProperty("periodo", txtFecha.getText());
                }

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/yacimientos/" + id))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(yacimiento.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Yacimiento actualizado correctamente");
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
        if (!SessionManager.getInstance().isAdmin()) {
            mostrarError("Acceso denegado: Solo un usuario con rol Administrador puede eliminar yacimientos.");
            return;
        }
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un yacimiento para eliminar");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);

        if (mostrarConfirmacion("¿Estás seguro de que deseas eliminar este yacimiento?") == JOptionPane.YES_OPTION) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/yacimientos/" + id))
                        .DELETE()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Yacimiento eliminado correctamente");
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

