package org.example.relik.dto;

public class RestoMaterialDTO {
    private Long idResto;
    private String tipo;
    private String material;
    private String descripcion;
    private String periodo;
    private Long idMuseo;
    private String nombreMuseo;

    public RestoMaterialDTO() {}

    public RestoMaterialDTO(Long idResto, String tipo, String material, String descripcion, String periodo, Long idMuseo, String nombreMuseo) {
        this.idResto = idResto;
        this.tipo = tipo;
        this.material = material;
        this.descripcion = descripcion;
        this.periodo = periodo;
        this.idMuseo = idMuseo;
        this.nombreMuseo = nombreMuseo;
    }

    public Long getIdResto() {
        return idResto;
    }

    public void setIdResto(Long idResto) {
        this.idResto = idResto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Long getIdMuseo() {
        return idMuseo;
    }

    public void setIdMuseo(Long idMuseo) {
        this.idMuseo = idMuseo;
    }

    public String getNombreMuseo() {
        return nombreMuseo;
    }

    public void setNombreMuseo(String nombreMuseo) {
        this.nombreMuseo = nombreMuseo;
    }

    @Override
    public String toString() {
        return tipo + " (" + material + ")";
    }
}


