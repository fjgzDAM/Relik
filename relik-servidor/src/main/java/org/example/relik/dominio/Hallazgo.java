package org.example.relik.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author fjgza
 */
@Entity
@Table(name = "thallazgo")
public class Hallazgo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hallazgo")
    private int idHallazgo;

    @Basic(optional = false)
    @Column(name = "fecha_hallazgo")
    private LocalDateTime fechaHallazgo;

    @Column(name = "campana")
    private String campana;

    @Column(name = "cuadricula")
    private String cuadricula;

    @Column(name = "coordenada_x")
    private String coordenadaX;

    @Column(name = "coordenada_y")
    private String coordenadaY;

    @Column(name = "cota_z")
    private String cotaZ;

    @Column(name = "unidad_estratigrafica")
    private String unidadEstratigrafica;

    @JoinColumn(name = "id_arqueologo", referencedColumnName = "id_arqueologo")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("hallazgoList")
    private Arqueologo arqueologo;

    @JoinColumn(name = "id_yacimiento", referencedColumnName = "id_yacimiento")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("hallazgoList")
    private Yacimiento yacimiento;

    @JoinColumn(name = "id_resto", referencedColumnName = "id_resto")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("hallazgoList")
    private RestoMaterial restoMaterial;

    public Hallazgo() {
    }

    public Hallazgo(LocalDateTime fechaHallazgo, Arqueologo arqueologo, Yacimiento yacimiento, RestoMaterial restoMaterial) {
        this.fechaHallazgo = fechaHallazgo;
        this.arqueologo = arqueologo;
        this.yacimiento = yacimiento;
        this.restoMaterial = restoMaterial;
    }

    public int getIdHallazgo() {
        return idHallazgo;
    }

    public LocalDateTime getFechaHallazgo() {
        return fechaHallazgo;
    }

    public void setFechaHallazgo(LocalDateTime fechaHallazgo) {
        this.fechaHallazgo = fechaHallazgo;
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

    public Arqueologo getArqueologo() {
        return arqueologo;
    }

    public void setArqueologo(Arqueologo arqueologo) {
        this.arqueologo = arqueologo;
    }

    public Yacimiento getYacimiento() {
        return yacimiento;
    }

    public void setYacimiento(Yacimiento yacimiento) {
        this.yacimiento = yacimiento;
    }

    public RestoMaterial getRestoMaterial() {
        return restoMaterial;
    }

    public void setRestoMaterial(RestoMaterial restoMaterial) {
        this.restoMaterial = restoMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hallazgo hallazgo = (Hallazgo) o;
        return idHallazgo == hallazgo.idHallazgo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idHallazgo);
    }

    @Override
    public String toString() {
        return "Hallazgo{" +
                "idHallazgo=" + idHallazgo +
                ", fechaHallazgo=" + fechaHallazgo +
                ", arqueologo=" + arqueologo +
                ", yacimiento=" + yacimiento +
                ", restoMaterial=" + restoMaterial +
                '}';
    }
}

