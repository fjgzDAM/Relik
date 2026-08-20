package org.example.relik.controlador;

import org.example.relik.dominio.Museo;
import org.example.relik.dominio.RestoMaterial;
import org.example.relik.dto.RestoMaterialDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restos")
@CrossOrigin(origins = "*")
public class RestoMaterialController {

    @Autowired
    private ModeloInterface modeloService;

    // --- TRADUCTORES ---
    private RestoMaterialDTO convertirADTO(RestoMaterial r) {
        return new RestoMaterialDTO(
                (long) r.getIdResto(),
                r.getNombre(),
                r.getTipologia() != null ? r.getTipologia() : "Desconocido",
                "Resto material de época " + r.getEpoca(),
                r.getEpoca(),
                r.getMuseo() != null ? (long) r.getMuseo().getIdMuseo() : null,
                r.getMuseo() != null ? r.getMuseo().getNombre() : "Sin museo"
        );
    }

    private RestoMaterial convertirAEntidad(RestoMaterialDTO dto) {
        RestoMaterial r = new RestoMaterial();
        r.setNombre(dto.getTipo());
        r.setEpoca(dto.getPeriodo());
        r.setTipologia(dto.getMaterial() != null ? dto.getMaterial() : "General");
        return r;
    }

    @GetMapping
    public List<RestoMaterialDTO> listarTodos() {
        return modeloService.listarRestosMateriales().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestoMaterialDTO> obtenerPorId(@PathVariable int id) {
        RestoMaterial r = modeloService.consultarRestoMaterial(id);
        return (r != null && r.getIdResto() > 0) ? ResponseEntity.ok(convertirADTO(r)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RestoMaterialDTO dto) {
        try {
            RestoMaterial r = convertirAEntidad(dto);
            r = modeloService.asignarMuseo(r);
            if (r.getMuseo() == null) {
                List<Museo> museos = modeloService.listarMuseos();
                if (!museos.isEmpty()) {
                    r.setMuseo(museos.get(0));
                }
            }
            RestoMaterial guardado = modeloService.insertar(r);
            return ResponseEntity.ok(convertirADTO(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable int id, @RequestBody RestoMaterialDTO dto) {
        try {
            RestoMaterial existente = modeloService.consultarRestoMaterial(id);
            if (existente == null || existente.getIdResto() == 0) {
                return ResponseEntity.notFound().build();
            }
            if (dto.getTipo() != null) existente.setNombre(dto.getTipo());
            if (dto.getPeriodo() != null) {
                existente.setEpoca(dto.getPeriodo());
                existente = modeloService.asignarMuseo(existente);
            }
            if (dto.getMaterial() != null) existente.setTipologia(dto.getMaterial());

            RestoMaterial modificado = modeloService.modificar(existente);
            return ResponseEntity.ok(convertirADTO(modificado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            RestoMaterial r = modeloService.consultarRestoMaterial(id);
            if (r != null && r.getIdResto() > 0) {
                modeloService.eliminar(r);
                return ResponseEntity.ok("Eliminado");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
