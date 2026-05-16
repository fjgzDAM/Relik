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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author fjgza
 */
@Entity
@Table(name = "tarqueologo")

public class Arqueologo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arqueologo")
    private int idArqueologo;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;

    @Basic(optional = false)
    @Column(name = "contrasena")
    private String contrasena;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arqueologo", fetch = FetchType.EAGER)
    private List<Hallazgo> hallazgoList;

    public Arqueologo() {
    }

    public Arqueologo(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public int getIdArqueologo() {
        return idArqueologo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Hallazgo> getHallazgoList() {
        return hallazgoList;
    }

    public void setHallazgoList(List<Hallazgo> hallazgoList) {
        this.hallazgoList = hallazgoList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Arqueologo that = (Arqueologo) o;
        return idArqueologo == that.idArqueologo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idArqueologo);
    }

    @Override
    public String toString() {
        return "Arqueologo{" +
                "idArqueologo=" + idArqueologo +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

