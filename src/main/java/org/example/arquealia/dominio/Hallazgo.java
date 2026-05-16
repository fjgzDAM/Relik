package org.example.arquealia.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHallazgo;

    @JoinColumn(name = "id_arqueologo", referencedColumnName = "id_arqueologo")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Arqueologo arqueologo;

    @JoinColumn(name = "id_yacimiento", referencedColumnName = "id_yacimiento")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Yacimiento yacimiento;

    @JoinColumn(name = "id_resto", referencedColumnName = "id_resto")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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
