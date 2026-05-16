package org.example.arquealia.dominio;

import java.io.Serializable;
import java.time.LocalDate;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author fjgza
 */
@Entity
@Table(name = "tyacimiento")
public class Yacimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_yacimiento")
    private int idYacimiento;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "coordenadas")
    private String coordenadas;

    @Column(name = "fecha_descubrimiento")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaDescubrimiento;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "yacimiento", fetch = FetchType.EAGER)
    private List<Hallazgo> hallazgoList;

    public Yacimiento() {
    }

    public Yacimiento(String nombre, String coordenadas, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.fechaInicio = fechaInicio;
    }

    public Yacimiento(String nombre) {
        this.nombre = nombre;
    }

    public int getIdYacimiento() {
        return idYacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public LocalDate getFechaDescubrimiento() {
        return fechaDescubrimiento;
    }

    public void setFechaDescubrimiento(LocalDate fechaDescubrimiento) {
        this.fechaDescubrimiento = fechaDescubrimiento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
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
        Yacimiento that = (Yacimiento) o;
        return idYacimiento == that.idYacimiento;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idYacimiento);
    }

    @Override
    public String toString() {
        return "Yacimiento{" +
                "idYacimiento=" + idYacimiento +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}

