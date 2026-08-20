package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.*;

public class GestorArqueologos extends GestorBase {
    private HttpClient httpClient;
    private Gson gson;

    public GestorArqueologos(String serverUrl) {
        super(serverUrl, "Gestor de Arqueólogos", 900, 600);
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

        JButton btnAgregar = UITheme.createButton("[+ Agregar Arqueólogo]", UITheme.COLOR_BTN_ACTION);
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

        crearTabla(new String[]{"ID", "Nombre", "Apellidos", "Especialidad", "Email"});
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
                    .uri(URI.create(serverUrl + "/arqueologos"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                tableModel.setRowCount(0);
                JsonArray arqueologos = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement element : arqueologos) {
                    JsonObject arq = element.getAsJsonObject();
                    tableModel.addRow(new Object[]{
                            arq.get("idArqueologo").getAsLong(),
                            arq.get("nombre").getAsString(),
                            arq.get("apellidos") != null && !arq.get("apellidos").isJsonNull() ? arq.get("apellidos").getAsString() : "",
                            arq.get("especialidad") != null && !arq.get("especialidad").isJsonNull() ? arq.get("especialidad").getAsString() : "",
                            arq.get("email") != null && !arq.get("email").isJsonNull() ? arq.get("email").getAsString() : ""
                    });
                }
            }else{
                mostrarError("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @Override
    protected void agregarRegistro() {
        JDialog dialog = new JDialog(this, "Nuevo Arqueólogo", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblApellidos = new JLabel("Apellidos:");
        JTextField txtApellidos = new JTextField();
        JLabel lblEspecialidad = new JLabel("Especialidad:");
        JTextField txtEspecialidad = new JTextField();
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellidos);
        panel.add(txtApellidos);
        panel.add(lblEspecialidad);
        panel.add(txtEspecialidad);
        panel.add(lblEmail);
        panel.add(txtEmail);

        JButton btnGuardar = UITheme.createButton("Guardar", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject arq = new JsonObject();
                arq.addProperty("nombre", txtNombre.getText().trim());
                arq.addProperty("apellidos", txtApellidos.getText().trim());
                arq.addProperty("especialidad", txtEspecialidad.getText().trim());
                arq.addProperty("email", txtEmail.getText().trim());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/arqueologos"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(arq.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Arqueólogo creado correctamente.");
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
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un arqueólogo");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);
        String nombre = (String) tableModel.getValueAt(fila, 1);
        String apellidos = (String) tableModel.getValueAt(fila, 2);
        String especialidad = (String) tableModel.getValueAt(fila, 3);
        String email = (String) tableModel.getValueAt(fila, 4);

        JDialog dialog = new JDialog(this, "Editar Arqueólogo", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(nombre);
        JLabel lblApellidos = new JLabel("Apellidos:");
        JTextField txtApellidos = new JTextField(apellidos);
        JLabel lblEspecialidad = new JLabel("Especialidad:");
        JTextField txtEspecialidad = new JTextField(especialidad);
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(email);

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellidos);
        panel.add(txtApellidos);
        panel.add(lblEspecialidad);
        panel.add(txtEspecialidad);
        panel.add(lblEmail);
        panel.add(txtEmail);

        JButton btnGuardar = UITheme.createButton("Guardar Cambios", UITheme.COLOR_BTN_ACTION);
        JButton btnCancelar = UITheme.createButton("Cancelar", UITheme.COLOR_BTN_DANGER);

        btnGuardar.addActionListener(e -> {
            try {
                JsonObject arq = new JsonObject();
                arq.addProperty("nombre", txtNombre.getText().trim());
                arq.addProperty("apellidos", txtApellidos.getText().trim());
                arq.addProperty("especialidad", txtEspecialidad.getText().trim());
                arq.addProperty("email", txtEmail.getText().trim());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/arqueologos/" + id))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(arq.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Arqueólogo actualizado correctamente.");
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
        int fila = table.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un arqueólogo");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);

        if (mostrarConfirmacion("¿Eliminar este arqueólogo?") == JOptionPane.YES_OPTION) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/arqueologos/" + id))
                        .DELETE()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    mostrarExito("Arqueólogo eliminado");
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


