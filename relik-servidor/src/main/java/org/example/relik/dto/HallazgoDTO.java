package org.example.relik.dto;

public class HallazgoDTO {
    private Long idHallazgo;
    private Long idArqueologo;
    private String nombreArqueologo;
    private Long idYacimiento;
    private String nombreYacimiento;
    private Long idResto;
    private String tipoResto;
    private String campana;
    private String cuadricula;
    private String coordenadaX;
    private String coordenadaY;
    private String cotaZ;
    private String unidadEstratigrafica;
    private String fechaHallazgo;
    private String descripcion;

    public HallazgoDTO() {}

    public HallazgoDTO(Long idHallazgo, Long idArqueologo, String nombreArqueologo, Long idYacimiento, String nombreYacimiento, Long idResto, String tipoResto, String campana, String cuadricula, String coordenadaX, String coordenadaY, String cotaZ, String unidadEstratigrafica, String fechaHallazgo, String descripcion) {
        this.idHallazgo = idHallazgo;
        this.idArqueologo = idArqueologo;
        this.nombreArqueologo = nombreArqueologo;
        this.idYacimiento = idYacimiento;
        this.nombreYacimiento = nombreYacimiento;
        this.idResto = idResto;
        this.tipoResto = tipoResto;
        this.campana = campana;
        this.cuadricula = cuadricula;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.cotaZ = cotaZ;
        this.unidadEstratigrafica = unidadEstratigrafica;
        this.fechaHallazgo = fechaHallazgo;
        this.descripcion = descripcion;
    }

    public Long getIdHallazgo() {
        return idHallazgo;
    }

    public void setIdHallazgo(Long idHallazgo) {
        this.idHallazgo = idHallazgo;
    }

    public Long getIdArqueologo() {
        return idArqueologo;
    }

    public void setIdArqueologo(Long idArqueologo) {
        this.idArqueologo = idArqueologo;
    }

    public String getNombreArqueologo() {
        return nombreArqueologo;
    }

    public void setNombreArqueologo(String nombreArqueologo) {
        this.nombreArqueologo = nombreArqueologo;
    }

    public Long getIdYacimiento() {
        return idYacimiento;
    }

    public void setIdYacimiento(Long idYacimiento) {
        this.idYacimiento = idYacimiento;
    }

    public String getNombreYacimiento() {
        return nombreYacimiento;
    }

    public void setNombreYacimiento(String nombreYacimiento) {
        this.nombreYacimiento = nombreYacimiento;
    }

    public Long getIdResto() {
        return idResto;
    }

    public void setIdResto(Long idResto) {
        this.idResto = idResto;
    }

    public String getTipoResto() {
        return tipoResto;
    }

    public void setTipoResto(String tipoResto) {
        this.tipoResto = tipoResto;
    }

    public String getCampana() {
        return campana;
    }

    public void setCampana(String campana) {
        this.campana = campana;
    }

    public String getCuadricula() {
        return cuadricula;
    }

    public void setCuadricula(String cuadricula) {
        this.cuadricula = cuadricula;
    }

    public String getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(String coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public String getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(String coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public String getCotaZ() {
        return cotaZ;
    }

    public void setCotaZ(String cotaZ) {
        this.cotaZ = cotaZ;
    }

    public String getUnidadEstratigrafica() {
        return unidadEstratigrafica;
    }

    public void setUnidadEstratigrafica(String unidadEstratigrafica) {
        this.unidadEstratigrafica = unidadEstratigrafica;
    }

    public String getFechaHallazgo() {
        return fechaHallazgo;
    }

    public void setFechaHallazgo(String fechaHallazgo) {
        this.fechaHallazgo = fechaHallazgo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Hallazgo: " + tipoResto + " (" + fechaHallazgo + ")";
    }
}

