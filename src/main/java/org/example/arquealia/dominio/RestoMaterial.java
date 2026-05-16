package org.example.arquealia.dominio;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author fjgza
 */
@Entity
@Table(name = "tresto_material")
@NamedQueries({
        @NamedQuery(name = "RestoMaterial.findAll", query = "SELECT r FROM RestoMaterial r")})
public class RestoMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resto")
    private int idResto;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @Basic(optional = false)
    @Column(name = "epoca")
    private String epoca;

    @Basic(optional = false)
    @Column(name = "tipologia")
    private String tipologia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restoMaterial", fetch = FetchType.EAGER)
    private List<Hallazgo> hallazgoList;

    @JoinColumn(name = "id_museo", referencedColumnName = "id_museo")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Museo museo;

    public RestoMaterial() {
    }

    public RestoMaterial(String nombre, String epoca, String tipologia) {
        this.nombre = nombre;
        this.epoca = epoca;
        this.tipologia = tipologia;
    }

    public int getIdResto() {
        return idResto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEpoca() {
        return epoca;
    }

    public void setEpoca(String epoca) {
        this.epoca = epoca;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public List<Hallazgo> getHallazgoList() {
        return hallazgoList;
    }

    public void setHallazgoList(List<Hallazgo> hallazgoList) {
        this.hallazgoList = hallazgoList;
    }

    public Museo getMuseo() {
        return museo;
    }

    public void setMuseo(Museo museo) {
        this.museo = museo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestoMaterial that = (RestoMaterial) o;
        return idResto == that.idResto;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idResto);
    }

    @Override
    public String toString() {
        return "RestoMaterial{" +
                "idResto=" + idResto +
                ", nombre='" + nombre + '\'' +
                ", epoca='" + epoca + '\'' +
                '}';
    }
}

