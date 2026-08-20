package org.example.relik.controlador;

import org.example.relik.dominio.*;
import org.example.relik.dto.HallazgoDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hallazgos")
@CrossOrigin(origins = "*")
public class HallazgoController {

    @Autowired
    private ModeloInterface modeloService;

    // --- TRADUCTORES ---
    private HallazgoDTO convertirADTO(Hallazgo h) {
        String nombreMuseo = "Sin museo";
        if (h.getRestoMaterial() != null && h.getRestoMaterial().getMuseo() != null) {
            nombreMuseo = h.getRestoMaterial().getMuseo().getNombre();
        }

        String desc = "Resto: " + (h.getRestoMaterial() != null ? h.getRestoMaterial().getNombre() : "N/A") +
                      " | Época: " + (h.getRestoMaterial() != null ? h.getRestoMaterial().getEpoca() : "N/A") +
                      " | Museo asignado: " + nombreMuseo;

        return new HallazgoDTO(
                (long) h.getIdHallazgo(),
                h.getArqueologo() != null ? (long) h.getArqueologo().getIdArqueologo() : null,
                h.getArqueologo() != null ? h.getArqueologo().getNombre() : "N/A",
                h.getYacimiento() != null ? (long) h.getYacimiento().getIdYacimiento() : null,
                h.getYacimiento() != null ? h.getYacimiento().getNombre() : "N/A",
                h.getRestoMaterial() != null ? (long) h.getRestoMaterial().getIdResto() : null,
                h.getRestoMaterial() != null ? h.getRestoMaterial().getNombre() : "N/A",
                h.getCampana() != null ? h.getCampana() : "Campaña 2026",
                h.getCuadricula() != null ? h.getCuadricula() : "S/C",
                h.getCoordenadaX() != null ? h.getCoordenadaX() : "0.0m",
                h.getCoordenadaY() != null ? h.getCoordenadaY() : "0.0m",
                h.getCotaZ() != null ? h.getCotaZ() : "0.0m",
                h.getUnidadEstratigrafica() != null ? h.getUnidadEstratigrafica() : "UE-100",
                h.getFechaHallazgo() != null ? h.getFechaHallazgo().toString() : "Fecha Desconocida",
                desc
        );
    }

    @GetMapping
    public List<HallazgoDTO> listarTodos() {
        return modeloService.listarHallazgos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        Hallazgo h = modeloService.consultarHallazgo(id);
        if (h != null && h.getIdHallazgo() > 0) {
            return ResponseEntity.ok(convertirADTO(h));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        try {
            int idArqueologo = Integer.parseInt(payload.get("idArqueologo").toString());
            int idYacimiento = Integer.parseInt(payload.get("idYacimiento").toString());
            
            // Obtener resto existente o crear uno nuevo según elección del usuario
            RestoMaterial resto = null;
            if (payload.containsKey("idResto") && payload.get("idResto") != null && !payload.get("idResto").toString().isBlank() && !"0".equals(payload.get("idResto").toString())) {
                int idResto = Integer.parseInt(payload.get("idResto").toString());
                resto = modeloService.consultarRestoMaterial(idResto);
            }

            if (resto == null || resto.getIdResto() == 0) {
                String nombreResto = payload.containsKey("nombreResto") && payload.get("nombreResto") != null ? payload.get("nombreResto").toString().trim() : "Resto Inédito";
                String epocaResto = payload.containsKey("epocaResto") && payload.get("epocaResto") != null ? payload.get("epocaResto").toString() : "Paleolitico";
                String tipologiaResto = payload.containsKey("tipologiaResto") && payload.get("tipologiaResto") != null ? payload.get("tipologiaResto").toString().trim() : "Herramienta / Cerámica";

                resto = new RestoMaterial(nombreResto, epocaResto, tipologiaResto);
                resto = modeloService.asignarMuseo(resto);
                
                if (resto.getMuseo() == null) {
                    List<Museo> museos = modeloService.listarMuseos();
                    if (!museos.isEmpty()) {
                        resto.setMuseo(museos.get(0));
                    } else {
                        return ResponseEntity.badRequest().body("No hay ningún museo registrado en la base de datos para asignar.");
                    }
                }
                
                resto = modeloService.insertar(resto);
            }

            String campana = payload.containsKey("campana") && payload.get("campana") != null ? payload.get("campana").toString() : "Campaña Anual 2026";
            String cuadricula = payload.containsKey("cuadricula") && payload.get("cuadricula") != null ? payload.get("cuadricula").toString() : "Cuadrícula A1";
            String coordX = payload.containsKey("coordenadaX") && payload.get("coordenadaX") != null ? payload.get("coordenadaX").toString() : "1.00m";
            String coordY = payload.containsKey("coordenadaY") && payload.get("coordenadaY") != null ? payload.get("coordenadaY").toString() : "1.00m";
            String cotaZ = payload.containsKey("cotaZ") && payload.get("cotaZ") != null ? payload.get("cotaZ").toString() : "-1.50m";
            String ue = payload.containsKey("unidadEstratigrafica") && payload.get("unidadEstratigrafica") != null ? payload.get("unidadEstratigrafica").toString() : "UE-101";

            Arqueologo arqueologo = modeloService.consultarArqueologoPorId(idArqueologo);
            if (arqueologo == null || arqueologo.getIdArqueologo() == 0) {
                return ResponseEntity.badRequest().body("Arqueólogo no encontrado.");
            }

            Yacimiento yacimiento = modeloService.consultarYacimientoPorId(idYacimiento);
            if (yacimiento == null || yacimiento.getIdYacimiento() == 0) {
                return ResponseEntity.badRequest().body("Yacimiento no encontrado.");
            }

            // Crear el hallazgo con localización espacial 3D y Unidad Estratigráfica
            LocalDateTime fecha = LocalDateTime.now();
            if (payload.containsKey("fechaHallazgo") && payload.get("fechaHallazgo") != null) {
                try {
                    fecha = LocalDateTime.parse(payload.get("fechaHallazgo").toString());
                } catch (Exception ignored) {}
            }

            Hallazgo hallazgo = new Hallazgo(fecha, arqueologo, yacimiento, resto);
            hallazgo.setCampana(campana);
            hallazgo.setCuadricula(cuadricula);
            hallazgo.setCoordenadaX(coordX);
            hallazgo.setCoordenadaY(coordY);
            hallazgo.setCotaZ(cotaZ);
            hallazgo.setUnidadEstratigrafica(ue);

            hallazgo = modeloService.insertar(hallazgo);

            return ResponseEntity.ok(convertirADTO(hallazgo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear hallazgo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id, @RequestParam(required = false, defaultValue = "0") int usuarioId) {
        try {
            Hallazgo h = modeloService.consultarHallazgo(id);
            if (h == null || h.getIdHallazgo() == 0) {
                return ResponseEntity.notFound().build();
            }

            // Verificar permisos
            if (usuarioId > 0) {
                Arqueologo solicitante = modeloService.consultarArqueologoPorId(usuarioId);
                if (solicitante != null && !"ADMIN".equalsIgnoreCase(solicitante.getRol())) {
                    if (h.getArqueologo() == null || h.getArqueologo().getIdArqueologo() != solicitante.getIdArqueologo()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para eliminar este hallazgo (solo el autor o admin pueden eliminarlo).");
                    }
                }
            }

            modeloService.eliminar(h);
            return ResponseEntity.ok("Eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

