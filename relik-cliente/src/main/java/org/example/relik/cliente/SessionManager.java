package org.example.relik.cliente;

import com.google.gson.JsonObject;

public class SessionManager {
    private static SessionManager instance;

    private long idArqueologo;
    private String nombre;
    private String email;
    private String rol; // "ADMIN" or "ARQUEOLOGO"

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void iniciarSesion(JsonObject usuarioJson) {
        if (usuarioJson.has("idArqueologo") && !usuarioJson.get("idArqueologo").isJsonNull()) {
            this.idArqueologo = usuarioJson.get("idArqueologo").getAsLong();
        }
        if (usuarioJson.has("nombre") && !usuarioJson.get("nombre").isJsonNull()) {
            this.nombre = usuarioJson.get("nombre").getAsString();
        }
        if (usuarioJson.has("email") && !usuarioJson.get("email").isJsonNull()) {
            this.email = usuarioJson.get("email").getAsString();
        } else if (usuarioJson.has("correo") && !usuarioJson.get("correo").isJsonNull()) {
            this.email = usuarioJson.get("correo").getAsString();
        }
        if (usuarioJson.has("rol") && !usuarioJson.get("rol").isJsonNull()) {
            this.rol = usuarioJson.get("rol").getAsString().toUpperCase();
        } else {
            this.rol = "ARQUEOLOGO";
        }
    }

    public void cerrarSesion() {
        this.idArqueologo = 0;
        this.nombre = null;
        this.email = null;
        this.rol = null;
    }

    public boolean isLogged() {
        return idArqueologo > 0;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.rol);
    }

    public long getIdArqueologo() {
        return idArqueologo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol != null ? rol : "ARQUEOLOGO";
    }
}

