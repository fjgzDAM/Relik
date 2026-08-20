package org.example.relik.dto;

public class YacimientoDTO {
    private Long idYacimiento;
    private String nombre;
    private String ubicacion;
    private String localizacion;
    private String coordenadas;
    private String descripcion;
    private String epoca;
    private String periodo;
    private String fechaInicio;

    public YacimientoDTO() {}

    public YacimientoDTO(Long idYacimiento, String nombre, String ubicacion, String coordenadas, String epoca, String fechaInicio) {
        this.idYacimiento = idYacimiento;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.localizacion = ubicacion;
        this.coordenadas = coordenadas;
        this.descripcion = coordenadas;
        this.epoca = epoca;
        this.periodo = fechaInicio;
        this.fechaInicio = fechaInicio;
    }

    public Long getIdYacimiento() {
        return idYacimiento;
    }

    public void setIdYacimiento(Long idYacimiento) {
        this.idYacimiento = idYacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion != null ? ubicacion : localizacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        this.localizacion = ubicacion;
    }

    public String getLocalizacion() {
        return localizacion != null ? localizacion : ubicacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
        this.ubicacion = localizacion;
    }

    public String getCoordenadas() {
        return coordenadas != null ? coordenadas : descripcion;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
        this.descripcion = coordenadas;
    }

    public String getDescripcion() {
        return descripcion != null ? descripcion : coordenadas;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.coordenadas = descripcion;
    }

    public String getEpoca() {
        return epoca;
    }

    public void setEpoca(String epoca) {
        this.epoca = epoca;
    }

    public String getPeriodo() {
        return periodo != null ? periodo : fechaInicio;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
        this.fechaInicio = periodo;
    }

    public String getFechaInicio() {
        return fechaInicio != null ? fechaInicio : periodo;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
        this.periodo = fechaInicio;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

