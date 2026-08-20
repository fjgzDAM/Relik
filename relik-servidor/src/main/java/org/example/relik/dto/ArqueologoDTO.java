package org.example.relik.dto;

public class ArqueologoDTO {
    private Long idArqueologo;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String email;
    private String rol;

    public ArqueologoDTO() {}

    public ArqueologoDTO(Long idArqueologo, String nombre, String apellidos, String especialidad, String email) {
        this(idArqueologo, nombre, apellidos, especialidad, email, "ARQUEOLOGO");
    }

    public ArqueologoDTO(Long idArqueologo, String nombre, String apellidos, String especialidad, String email, String rol) {
        this.idArqueologo = idArqueologo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.email = email;
        this.rol = rol != null ? rol : "ARQUEOLOGO";
    }

    public Long getIdArqueologo() {
        return idArqueologo;
    }

    public void setIdArqueologo(Long idArqueologo) {
        this.idArqueologo = idArqueologo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}


