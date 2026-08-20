package org.example.relik.cliente;

import javax.swing.*;
import java.awt.*;

public class ClienteRelik extends JFrame {
    private static final String SERVER_URL = "http://localhost:8080/api";
    private JLabel lblUsuario;

    public ClienteRelik() {
        setTitle("Relik - Sistema de Registro Arqueológico de Campo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Mostrar diálogo de Autenticación / Registro
        LoginDialog loginDialog = new LoginDialog(this, SERVER_URL);
        loginDialog.setVisible(true);

        if (!loginDialog.isAutenticado() || !SessionManager.getInstance().isLogged()) {
            System.exit(0);
        }

        inicializarUI();
    }

    private void inicializarUI() {
        // Panel principal con menú
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        // Panel superior con título e información de usuario (Terracota)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UITheme.COLOR_HEADER_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("RELIK - Registro y Gestión de Hallazgos");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.COLOR_HEADER_TEXT);
        titlePanel.add(title, BorderLayout.WEST);

        lblUsuario = new JLabel("Usuario: " + SessionManager.getInstance().getNombre() + " (" + SessionManager.getInstance().getRol() + ")");
        lblUsuario.setFont(UITheme.FONT_BOLD);
        lblUsuario.setForeground(new Color(255, 230, 200)); // Oro suave
        titlePanel.add(lblUsuario, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Panel central con botones (Temática Arqueológica)
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        centerPanel.setBackground(UITheme.COLOR_PARCHMENT_BG);

        // Botones estilizados con alto contraste
        JButton btnYacimientos = UITheme.createButton("Gestionar Yacimientos", UITheme.COLOR_BTN_PRIMARY);
        JButton btnMuseos = UITheme.createButton("Gestionar Museos", UITheme.COLOR_BTN_PRIMARY);
        JButton btnArqueologos = UITheme.createButton("Gestionar Arqueólogos", UITheme.COLOR_BTN_PRIMARY);
        JButton btnRestos = UITheme.createButton("Gestionar Restos Materiales", UITheme.COLOR_BTN_PRIMARY);
        JButton btnHallazgos = UITheme.createButton("[+] Registrar Hallazgos", UITheme.COLOR_BTN_ACTION); // Verde Oliva
        JButton btnCerrarSesion = UITheme.createButton("[X] Cerrar Sesión / Salir", UITheme.COLOR_BTN_DANGER); // Rojo Óxido

        // Acciones de los botones
        btnYacimientos.addActionListener(e -> abrirGestorYacimientos());
        btnMuseos.addActionListener(e -> abrirGestorMuseos());
        btnArqueologos.addActionListener(e -> abrirGestorArqueologos());
        btnRestos.addActionListener(e -> abrirGestorRestos());
        btnHallazgos.addActionListener(e -> abrirGestorHallazgos());
        btnCerrarSesion.addActionListener(e -> reloguear());

        centerPanel.add(btnYacimientos);
        centerPanel.add(btnMuseos);
        centerPanel.add(btnArqueologos);
        centerPanel.add(btnRestos);
        centerPanel.add(btnHallazgos);
        centerPanel.add(btnCerrarSesion);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(230, 220, 205));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JLabel info = new JLabel("Servidor: " + SERVER_URL + " | Conectado a BRelik");
        info.setFont(UITheme.FONT_REGULAR);
        info.setForeground(UITheme.COLOR_TEXT_DARK);
        infoPanel.add(info, BorderLayout.WEST);

        JLabel lblPermisos = new JLabel(SessionManager.getInstance().isAdmin() ? "Permisos: Administrador Total" : "Permisos: Arqueólogo de campo (Crear y gestionar hallazgos propios)");
        lblPermisos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPermisos.setForeground(SessionManager.getInstance().isAdmin() ? new Color(140, 40, 40) : new Color(40, 90, 40));
        infoPanel.add(lblPermisos, BorderLayout.EAST);

        panel.add(infoPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void abrirGestorYacimientos() {
        new GestorYacimientos(SERVER_URL);
    }

    private void abrirGestorMuseos() {
        new GestorMuseos(SERVER_URL);
    }

    private void abrirGestorArqueologos() {
        new GestorArqueologos(SERVER_URL);
    }

    private void abrirGestorRestos() {
        new GestorRestos(SERVER_URL);
    }

    private void abrirGestorHallazgos() {
        new GestorHallazgos(SERVER_URL);
    }

    private void reloguear() {
        SessionManager.getInstance().cerrarSesion();
        dispose();
        new ClienteRelik();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new ClienteRelik());
    }
}

