package org.example.arquealia.dominio;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 *
 * @author fjgza
 */
@Entity
@Table(name = "tmuseo")
public class Museo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_museo")
    private int idMuseo;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "pais")
    private String pais;

    @Basic(optional = false)
    @Column(name = "epoca_especializada")
    private String epocaEspecializada;

    // mappedBy debe referirse al nombre del atributo en RestoMaterial que apunta a Museo
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "museo", fetch = FetchType.EAGER)
    private List<RestoMaterial> restoMaterialList;

    public Museo() {
    }

    public Museo(String nombre, String epocaEspecializada) {
        this.nombre = nombre;
        this.epocaEspecializada = epocaEspecializada;
    }

    public Museo(String nombre, String ciudad, String pais, String epocaEspecializada) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.epocaEspecializada = epocaEspecializada;
    }

    public int getIdMuseo() {
        return idMuseo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEpocaEspecializada() {
        return epocaEspecializada;
    }

    public void setEpocaEspecializada(String epocaEspecializada) {
        this.epocaEspecializada = epocaEspecializada;
    }

    public List<RestoMaterial> getRestoMaterialList() {
        return restoMaterialList;
    }

    public void setRestoMaterialList(List<RestoMaterial> restoMaterialList) {
        this.restoMaterialList = restoMaterialList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Museo museo = (Museo) o;
        return idMuseo == museo.idMuseo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idMuseo);
    }

    @Override
    public String toString() {
        return "Museo{" +
                "idMuseo=" + idMuseo +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", epocaEspecializada='" + epocaEspecializada + '\'' +
                '}';
    }
}
