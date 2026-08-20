package org.example.relik.controlador;

import org.example.relik.dominio.Museo;
import org.example.relik.dto.MuseoDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/museos")
@CrossOrigin(origins = "*")
public class MuseoController {

    @Autowired
    private ModeloInterface modeloService;

    // --- TRADUCTORES ---
    private MuseoDTO convertirADTO(Museo m) {
        return new MuseoDTO(
                (long) m.getIdMuseo(),
                m.getNombre(),
                m.getCiudad() != null ? m.getCiudad() : "",
                m.getPais() != null ? m.getPais() : "",
                m.getEpocaEspecializada() != null ? m.getEpocaEspecializada() : ""
        );
    }

    private Museo convertirAEntidad(MuseoDTO dto) {
        Museo m = new Museo();
        m.setNombre(dto.getNombre());
        m.setCiudad(dto.getCiudad());
        m.setPais(dto.getPais());
        m.setEpocaEspecializada(dto.getEspecialidad());
        return m;
    }

    @GetMapping
    public List<MuseoDTO> listarTodos() {
        return modeloService.listarMuseos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuseoDTO> obtenerPorId(@PathVariable int id) {
        Museo m = modeloService.consultarMuseo(id);
        return (m != null && m.getIdMuseo() > 0) ? ResponseEntity.ok(convertirADTO(m)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MuseoDTO dto) {
        try {
            Museo m = convertirAEntidad(dto);
            Museo guardado = modeloService.insertar(m);
            return ResponseEntity.ok(convertirADTO(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable int id, @RequestBody MuseoDTO dto) {
        try {
            Museo existente = modeloService.consultarMuseo(id);
            if (existente == null || existente.getIdMuseo() == 0) {
                return ResponseEntity.notFound().build();
            }
            if (dto.getNombre() != null) existente.setNombre(dto.getNombre());
            if (dto.getCiudad() != null) existente.setCiudad(dto.getCiudad());
            if (dto.getPais() != null) existente.setPais(dto.getPais());
            if (dto.getEspecialidad() != null) existente.setEpocaEspecializada(dto.getEspecialidad());

            Museo modificado = modeloService.modificar(existente);
            return ResponseEntity.ok(convertirADTO(modificado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            Museo m = modeloService.consultarMuseo(id);
            if (m != null && m.getIdMuseo() > 0) {
                modeloService.eliminar(m);
                return ResponseEntity.ok("Eliminado");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
