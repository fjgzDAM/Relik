package org.example.relik.modelo;

import org.example.relik.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public void eliminar(Integer id) {
        if (id == null || id == 0) return;
        arqueologoRespository.deleteById(id);
        arqueologoRespository.flush();
    }

    @Override
    public Arqueologo consultarArqueologoPorId(int idArqueologo) {
        return arqueologoRespository.findById(idArqueologo).orElse(new Arqueologo());
    }

    @jakarta.annotation.PostConstruct
    public void inicializarDatosEjemplo() {
        if (arqueologoRespository.count() == 0) {
            try {
                Arqueologo admin = arqueologoRespository.save(new Arqueologo("Administrador", "General", "Sistemas y Dirección", "admin@relik.com", "admin", "ADMIN"));
                Arqueologo elena = arqueologoRespository.save(new Arqueologo("Elena", "Ramos", "Estratigrafía y Cerámica", "elena.ramos@relik.com", "1234", "ARQUEOLOGO"));
                Arqueologo carlos = arqueologoRespository.save(new Arqueologo("Carlos", "Mendoza", "Antropología Física", "carlos.mendoza@relik.com", "1234", "ARQUEOLOGO"));
                Arqueologo prueba = arqueologoRespository.save(new Arqueologo("Usuario", "Prueba", "Prospección de Campo", "prueba@example.com", "1234", "ARQUEOLOGO"));

                Museo m1 = museoRepository.save(new Museo("Museo Arqueológico Nacional", "Madrid", "España", "Paleolitico"));
                Museo m2 = museoRepository.save(new Museo("Museo Nacional de Altamira", "Santillana del Mar", "España", "Paleolitico"));
                Museo m3 = museoRepository.save(new Museo("Museo Arqueológico de Sevilla", "Sevilla", "España", "Romana"));
                Museo m4 = museoRepository.save(new Museo("Museo de la Prehistoria de Valencia", "Valencia", "España", "Neolitico"));
                Museo m5 = museoRepository.save(new Museo("Museo Monográfico de Atapuerca", "Ibeas de Juarros", "España", "Calcolitico"));

                Yacimiento y1 = new Yacimiento("Gran Dolina (Atapuerca)", "42.3514 N, -3.5182 W", LocalDate.of(1978, 7, 10));
                y1.setUbicacion("Burgos, Castilla y León");
                y1.setEpoca("Paleolitico");
                y1 = yacimientoRepository.save(y1);

                Yacimiento y2 = new Yacimiento("Cueva de Altamira", "43.3772 N, -4.1225 W", LocalDate.of(1879, 11, 20));
                y2.setUbicacion("Santillana del Mar, Cantabria");
                y2.setEpoca("Paleolitico");
                y2 = yacimientoRepository.save(y2);

                Yacimiento y3 = new Yacimiento("Conjunto Arqueológico de Itálica", "37.4442 N, -6.0441 W", LocalDate.of(1781, 4, 12));
                y3.setUbicacion("Santiponce, Sevilla");
                y3.setEpoca("Romana");
                y3 = yacimientoRepository.save(y3);

                Yacimiento y4 = new Yacimiento("Yacimiento de Los Millares", "36.9664 N, -2.5273 W", LocalDate.of(1891, 9, 15));
                y4.setUbicacion("Santa Fe de Mondújar, Almería");
                y4.setEpoca("Calcolitico");
                y4 = yacimientoRepository.save(y4);

                RestoMaterial r1 = restoMaterialRepository.save(new RestoMaterial("Bifaz Acheliense Excalibur", "Paleolitico", "Herramienta de piedra cuarcita", m1));
                RestoMaterial r2 = restoMaterialRepository.save(new RestoMaterial("Pigmento Rupestre de Bizonte", "Paleolitico", "Arte parietal con pigmentos", m2));
                RestoMaterial r3 = restoMaterialRepository.save(new RestoMaterial("Mosaico Polícromo de Neptuno", "Romana", "Pavimento de tesserae romanas", m3));

                Hallazgo h1 = new Hallazgo(LocalDateTime.now().minusDays(10), elena, y1, r1);
                h1.setCampana("Campaña Anual 2026");
                h1.setCuadricula("Cuadrícula A1");
                h1.setCoordenadaX("0.45m");
                h1.setCoordenadaY("1.20m");
                h1.setCotaZ("-1.85m");
                h1.setUnidadEstratigrafica("UE-102");
                hallazgoRepository.save(h1);

                Hallazgo h2 = new Hallazgo(LocalDateTime.now().minusDays(5), carlos, y2, r2);
                h2.setCampana("Campaña Verano 2026");
                h2.setCuadricula("Cuadrícula B3");
                h2.setCoordenadaX("0.90m");
                h2.setCoordenadaY("0.65m");
                h2.setCotaZ("-2.40m");
                h2.setUnidadEstratigrafica("UE-105");
                hallazgoRepository.save(h2);

                Hallazgo h3 = new Hallazgo(LocalDateTime.now().minusDays(2), elena, y3, r3);
                h3.setCampana("Campaña Anual 2026");
                h3.setCuadricula("Sector C2");
                h3.setCoordenadaX("1.15m");
                h3.setCoordenadaY("2.05m");
                h3.setCotaZ("-0.90m");
                h3.setUnidadEstratigrafica("UE-201");
                hallazgoRepository.save(h3);

                LOG.info("Base de datos sembrada automáticamente con datos de prueba ricos.");
            } catch (Exception e) {
                LOG.warn("Error al inicializar datos de prueba: {}", e.getMessage());
            }
        }
    }

    @Override
    public Arqueologo consultarArqueologoPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) return null;
        return arqueologoRespository.findByCorreoIgnoreCase(correo.trim()).orElse(null);
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

    @Transactional
    @Override
    public void eliminar(Museo museo) {
        if (museo == null || museo.getIdMuseo() == 0) return;
        museoRepository.deleteById(museo.getIdMuseo());
        museoRepository.flush();
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

    @Transactional
    @Override
    public void eliminar(Hallazgo hallazgo) {
        if (hallazgo == null || hallazgo.getIdHallazgo() == 0) return;
        int idHallazgo = hallazgo.getIdHallazgo();
        RestoMaterial resto = hallazgo.getRestoMaterial();

        hallazgoRepository.deleteById(idHallazgo);
        hallazgoRepository.flush();

        if (resto != null && resto.getIdResto() > 0) {
            int idResto = resto.getIdResto();
            boolean tieneOtrosHallazgos = hallazgoRepository.findAll().stream()
                    .anyMatch(h -> h.getRestoMaterial() != null && h.getRestoMaterial().getIdResto() == idResto);
            if (!tieneOtrosHallazgos) {
                try {
                    restoMaterialRepository.deleteById(idResto);
                    restoMaterialRepository.flush();
                } catch (Exception ex) {
                    LOG.warn("No se pudo eliminar el resto material huérfano #{}: {}", idResto, ex.getMessage());
                }
            }
        }
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

    @Transactional
    @Override
    public void eliminar(RestoMaterial restoMaterial) {
        if (restoMaterial == null || restoMaterial.getIdResto() == 0) return;
        restoMaterialRepository.deleteById(restoMaterial.getIdResto());
        restoMaterialRepository.flush();
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

    @Transactional
    @Override
    public void eliminar(Yacimiento yacimiento) {
        if (yacimiento == null || yacimiento.getIdYacimiento() == 0) return;
        yacimientoRepository.deleteById(yacimiento.getIdYacimiento());
        yacimientoRepository.flush();
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

