package org.example.relik.dto;

public class MuseoDTO {
    private Long idMuseo;
    private String nombre;
    private String localizacion;
    private String direccion;
    private String contacto;

    public MuseoDTO() {}

    public MuseoDTO(Long idMuseo, String nombre, String localizacion, String direccion, String contacto) {
        this.idMuseo = idMuseo;
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.direccion = direccion;
        this.contacto = contacto;
    }

    public Long getIdMuseo() {
        return idMuseo;
    }

    public void setIdMuseo(Long idMuseo) {
        this.idMuseo = idMuseo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    // Alias methods for compatibility
    public String getCiudad() { return localizacion; }
    public void setCiudad(String ciudad) { this.localizacion = ciudad; }

    public String getPais() { return direccion; }
    public void setPais(String pais) { this.direccion = pais; }

    public String getEspecialidad() { return contacto; }
    public void setEspecialidad(String especialidad) { this.contacto = especialidad; }
    public String getEpocaEspecializada() { return contacto; }
    public void setEpocaEspecializada(String epoca) { this.contacto = epoca; }

    @Override
    public String toString() {
        return nombre;
    }
}

