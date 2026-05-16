package org.example.arquealia.modelo;

import org.example.arquealia.dominio.*;

import java.time.LocalDate;
import java.util.ArrayList;

public interface ModeloInterface {

    Arqueologo insertar(Arqueologo arqueologo);
    Arqueologo modificar(Arqueologo arqueologo);
    void eliminar(Integer id);
    Arqueologo consultarArqueologoPorId(int idArqueologo);
    Arqueologo consultarArqueologoPorCorreo(String Correo);
    ArrayList<Arqueologo> listarArqueologos();

    Museo insertar(Museo museo);
    Museo modificar(Museo museo);
    void eliminar(Museo museo);
    Museo consultarMuseo(int idMuseo);
    ArrayList<Museo> listarMuseos();

    Hallazgo insertar(Hallazgo hallazgo);
    Hallazgo modificar(Hallazgo hallazgo);
    void eliminar(Hallazgo hallazgo);
    Hallazgo consultarHallazgo(int idHallazgo);
    ArrayList<Hallazgo> listarHallazgos();

    RestoMaterial insertar(RestoMaterial restoMaterial);
    RestoMaterial modificar(RestoMaterial restoMaterial);
    void eliminar(RestoMaterial restoMaterial);
    RestoMaterial consultarRestoMaterial(int idResto);
    ArrayList<RestoMaterial> listarRestosMateriales();

    Yacimiento insertar(Yacimiento yacimiento);
    Yacimiento modificar(Yacimiento yacimiento);
    void eliminar(Yacimiento yacimiento);
    Yacimiento consultarYacimientoPorId(int idYacimiento);
    Yacimiento consultarYacimientoPorNombre(String nombre);
    ArrayList<Yacimiento> listarYacimientos();

    String topArqueologosPorHallazgos();
    ArrayList<Hallazgo> resumenHallazgosYacimiento(int idYacimiento);
    ArrayList<Hallazgo> resumenHallazgosYacimientoRangoFecha(int idYacimiento, LocalDate fechaInicio, LocalDate fechaFin);
    ArrayList<Hallazgo> resumenHallazgosFecha(LocalDate fecha);
}
