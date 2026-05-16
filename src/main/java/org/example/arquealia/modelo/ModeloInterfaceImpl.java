package org.example.arquealia.modelo;

import org.example.arquealia.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeloInterfaceImpl implements ModeloInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloInterfaceImpl.class);

    @Autowired
    private MuseoRepository museoRepository;
    @Autowired
    private ArqueologoRespository arqueologoRespository;
    @Autowired
    private HallazgoRepository hallazgoRepository;
    @Autowired
    private RestoMaterialRepository restoMaterialRepository;
    @Autowired
    private YacimientoRepository yacimientoRepository;


    @Override
    public Arqueologo insertar(Arqueologo arqueologo) {
        try {
            return arqueologoRespository.save(arqueologo);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("No se pudo insertar arqueólogo (posible duplicado): {} - {}", arqueologo.getNombre(), arqueologo.getCorreo());
            return arqueologo;
        }
    }

    @Override
    public Arqueologo modificar(Arqueologo arqueologo) {
        return arqueologoRespository.save(arqueologo);
    }

    @Override
    public void eliminar(Integer id) {
        arqueologoRespository.deleteById(id);
    }

    @Override
    public Arqueologo consultarArqueologoPorId(int idArqueologo) {
        return arqueologoRespository.findById(idArqueologo).orElse(new Arqueologo());
    }

    @Override
    public Arqueologo consultarArqueologoPorCorreo(String correo) {
        return arqueologoRespository.findByCorreo(correo).orElse(null);
    }

    @Override
    public ArrayList<Arqueologo> listarArqueologos() {
        return (ArrayList<Arqueologo>) arqueologoRespository.findAll();
    }

    @Override
    public Museo insertar(Museo museo) {
        try {
            return museoRepository.save(museo);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("No se pudo insertar museo (posible duplicado): {}", museo.getNombre());
            return museo;
        }
    }

    @Override
    public Museo modificar(Museo museo) {
        return museoRepository.save(museo);
    }

    @Override
    public void eliminar(Museo museo) {
        museoRepository.delete(museo);
    }

    @Override
    public Museo consultarMuseo(int idMuseo) {
        return museoRepository.findById(idMuseo).orElse(new Museo());
    }

    @Override
    public ArrayList<Museo> listarMuseos() {
        return (ArrayList<Museo>) museoRepository.findAll();
    }

    @Override
    public Hallazgo insertar(Hallazgo hallazgo) {
        try {
            return hallazgoRepository.save(hallazgo);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("No se pudo insertar hallazgo: {}", hallazgo);
            return hallazgo;
        }
    }

    @Override
    public Hallazgo modificar(Hallazgo hallazgo) {
        return hallazgoRepository.save(hallazgo);
    }

    @Override
    public void eliminar(Hallazgo hallazgo) {
        hallazgoRepository.delete(hallazgo);
    }

    @Override
    public Hallazgo consultarHallazgo(int idHallazgo) {
        return hallazgoRepository.findById(idHallazgo).orElse(new Hallazgo());
    }

    @Override
    public ArrayList<Hallazgo> listarHallazgos() {
        return (ArrayList<Hallazgo>) hallazgoRepository.findAll();
    }

    @Override
    public RestoMaterial insertar(RestoMaterial restoMaterial) {
        try {
            return restoMaterialRepository.save(restoMaterial);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("No se pudo insertar resto material (posible duplicado): {}", restoMaterial.getNombre());
            return restoMaterial;
        }
    }

    @Override
    public RestoMaterial modificar(RestoMaterial restoMaterial) {
        return restoMaterialRepository.save(restoMaterial);
    }

    @Override
    public void eliminar(RestoMaterial restoMaterial) {
        restoMaterialRepository.delete(restoMaterial);
    }

    @Override
    public RestoMaterial consultarRestoMaterial(int idResto) {
        return restoMaterialRepository.findById(idResto).orElse(new RestoMaterial());
    }

    @Override
    public ArrayList<RestoMaterial> listarRestosMateriales() {
        return (ArrayList<RestoMaterial>) restoMaterialRepository.findAll();
    }

    public RestoMaterial asignarMuseo(RestoMaterial restoMaterial) {
        ArrayList<Museo> museos = museoRepository.findByEpoca(restoMaterial.getEpoca());
        if (!museos.isEmpty()) {
            restoMaterial.setMuseo(museos.get(0)); // Asigna el primer museo disponible
        }
        return restoMaterial;
    }

    @Override
    public Yacimiento insertar(Yacimiento yacimiento) {
        try {
            return yacimientoRepository.save(yacimiento);
        } catch (DataIntegrityViolationException ex) {
            LOG.warn("No se pudo insertar yacimiento (posible duplicado): {}", yacimiento.getNombre());
            return yacimiento;
        }
    }

    @Override
    public Yacimiento modificar(Yacimiento yacimiento) {
        return yacimientoRepository.save(yacimiento);
    }

    @Override
    public void eliminar(Yacimiento yacimiento) {
        yacimientoRepository.delete(yacimiento);
    }

    @Override
    public Yacimiento consultarYacimientoPorId(int idYacimiento) {
        return yacimientoRepository.findById(idYacimiento).orElse(new Yacimiento());
    }

    @Override
    public Yacimiento consultarYacimientoPorNombre(String nombre) {
        return yacimientoRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public ArrayList<Yacimiento> listarYacimientos() {
        return (ArrayList<Yacimiento>) yacimientoRepository.findAll();
    }

    @Override
    public String topArqueologosPorHallazgos() {
        List<Arqueologo> arqueologos = arqueologoRespository.findAll();
        arqueologos.sort((a1, a2) -> Integer.compare(a2.getHallazgoList().size(), a1.getHallazgoList().size()));

        StringBuilder resultado = new StringBuilder();
        arqueologos.subList(0, Math.min(arqueologos.size(), 10)).forEach(arqueologo -> {
            resultado.append(arqueologo.getNombre()).append(": ").append(arqueologo.getHallazgoList().size()).append(" hallazgos\n");

    });
        return resultado.toString();
    }

    @Override
    public ArrayList<Hallazgo> resumenHallazgosYacimientoRangoFecha(int idYacimiento, LocalDate fechaInicio, LocalDate fechaFin) {
        return (ArrayList<Hallazgo>) hallazgoRepository.findAll().stream()
                .filter(h -> h.getYacimiento().getIdYacimiento() == idYacimiento)
                .filter(h -> !h.getFechaHallazgo().toLocalDate().isBefore(fechaInicio) && !h.getFechaHallazgo().toLocalDate().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Hallazgo> resumenHallazgosFecha(LocalDate fecha) {
        return (ArrayList<Hallazgo>) hallazgoRepository.findAll().stream()
                .filter(h -> h.getFechaHallazgo().toLocalDate().isEqual(fecha))
                .collect(Collectors.toList());
    }

    public ArrayList<Hallazgo> resumenHallazgosYacimiento(int idYacimiento){
        return (ArrayList<Hallazgo>) hallazgoRepository.findAll().stream()
                .filter(h -> h.getYacimiento().getIdYacimiento() == idYacimiento)
                .collect(Collectors.toList());
    }
}
