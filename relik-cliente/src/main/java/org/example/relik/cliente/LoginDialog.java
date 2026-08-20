package org.example.relik.cliente;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginDialog extends JDialog {
    private String serverUrl;
    private boolean autenticado = false;
    private HttpClient httpClient;

    public LoginDialog(Frame owner, String serverUrl) {
        super(owner, "Relik - Autenticación y Registro", true);
        this.serverUrl = serverUrl;
        this.httpClient = HttpClient.newHttpClient();

        setSize(480, 440);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);
        tabbedPane.setBackground(UITheme.COLOR_PARCHMENT_BG);
        tabbedPane.addTab("Iniciar Sesión", createPanelLogin());
        tabbedPane.addTab("Registrarse", createPanelRegistro());

        add(tabbedPane);
    }

    private JPanel createPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Acceso a Relik", SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_HEADER_BG);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(UITheme.FONT_BOLD);
        lblCorreo.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblCorreo, gbc);

        JTextField txtCorreo = new JTextField(18);
        txtCorreo.setFont(UITheme.FONT_REGULAR);
        txtCorreo.setText("prueba@example.com");
        gbc.gridx = 1;
        panel.add(txtCorreo, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(UITheme.FONT_BOLD);
        lblPass.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(18);
        txtPass.setFont(UITheme.FONT_REGULAR);
        txtPass.setText("1234");
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        JButton btnLogin = UITheme.createButton("Iniciar Sesión", UITheme.COLOR_BTN_PRIMARY);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        JLabel lblHelp = new JLabel("<html><small style='color: #4A3525;'>Admin por defecto: <b>admin@relik.com</b> / <b>admin</b></small></html>", SwingConstants.CENTER);
        gbc.gridy = 4;
        panel.add(lblHelp, gbc);

        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (correo.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor introduce correo y contraseña.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                JsonObject json = new JsonObject();
                json.addProperty("correo", correo);
                json.addProperty("contrasena", pass);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject resObj = JsonParser.parseString(response.body()).getAsJsonObject();
                    SessionManager.getInstance().iniciarSesion(resObj);
                    autenticado = true;
                    JOptionPane.showMessageDialog(this, "Bienvenido " + SessionManager.getInstance().getNombre() + " (" + SessionManager.getInstance().getRol() + ")", "Inicio de sesión correcto", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error de autenticación: " + response.body(), "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar con el servidor: " + ex.getMessage(), "Error de red", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Registro de Arqueólogo", SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_HEADER_BG);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(UITheme.FONT_BOLD);
        lblNombre.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblNombre, gbc);

        JTextField txtNombre = new JTextField(18);
        txtNombre.setFont(UITheme.FONT_REGULAR);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(UITheme.FONT_BOLD);
        lblCorreo.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblCorreo, gbc);

        JTextField txtCorreo = new JTextField(18);
        txtCorreo.setFont(UITheme.FONT_REGULAR);
        gbc.gridx = 1;
        panel.add(txtCorreo, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(UITheme.FONT_BOLD);
        lblPass.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(18);
        txtPass.setFont(UITheme.FONT_REGULAR);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(UITheme.FONT_BOLD);
        lblRol.setForeground(UITheme.COLOR_TEXT_DARK);
        panel.add(lblRol, gbc);

        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"ARQUEOLOGO", "ADMIN"});
        cmbRol.setFont(UITheme.FONT_REGULAR);
        gbc.gridx = 1;
        panel.add(cmbRol, gbc);

        JButton btnRegister = UITheme.createButton("Registrar Usuario", UITheme.COLOR_BTN_ACTION);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        btnRegister.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String rol = (String) cmbRol.getSelectedItem();

            if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos requeridos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                JsonObject json = new JsonObject();
                json.addProperty("nombre", nombre);
                json.addProperty("correo", correo);
                json.addProperty("contrasena", pass);
                json.addProperty("rol", rol);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/auth/register"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject resObj = JsonParser.parseString(response.body()).getAsJsonObject();
                    SessionManager.getInstance().iniciarSesion(resObj);
                    autenticado = true;
                    JOptionPane.showMessageDialog(this, "Registro completado con éxito. Sesión iniciada como " + nombre + " (" + rol + ").", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar: " + response.body(), "Error de registro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de comunicación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    public boolean isAutenticado() {
        return autenticado;
    }
}

