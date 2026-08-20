package org.example.relik.controlador;

import org.example.relik.dominio.Arqueologo;
import org.example.relik.dto.ArqueologoDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/arqueologos")
@CrossOrigin(origins = "*")
public class ArqueologoController {

    @Autowired
    private ModeloInterface modeloService;

    // --- TRADUCTORES ---
    private ArqueologoDTO convertirADTO(Arqueologo a) {
        return new ArqueologoDTO(
                (long) a.getIdArqueologo(),
                a.getNombre(),
                a.getApellidos() != null ? a.getApellidos() : "",
                a.getEspecialidad() != null ? a.getEspecialidad() : "Arqueología General",
                a.getCorreo(),
                a.getRol()
        );
    }

    private Arqueologo convertirAEntidad(ArqueologoDTO dto) {
        Arqueologo a = new Arqueologo();
        a.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : "");
        a.setApellidos(dto.getApellidos() != null ? dto.getApellidos().trim() : "");
        a.setEspecialidad(dto.getEspecialidad() != null && !dto.getEspecialidad().trim().isEmpty() ? dto.getEspecialidad().trim() : "Arqueología General");
        a.setCorreo(dto.getEmail() != null ? dto.getEmail().trim() : "");
        a.setContrasena("1234");
        a.setRol(dto.getRol() != null && !dto.getRol().trim().isEmpty() ? dto.getRol().trim() : "ARQUEOLOGO");
        return a;
    }

    // --- ENDPOINTS ---
    @GetMapping
    public List<ArqueologoDTO> listarTodos() {
        return modeloService.listarArqueologos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArqueologoDTO> obtenerPorId(@PathVariable int id) {
        Arqueologo a = modeloService.consultarArqueologoPorId(id);
        return (a != null && a.getIdArqueologo() > 0) ? ResponseEntity.ok(convertirADTO(a)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ArqueologoDTO dto) {
        try {
            Arqueologo nuevo = modeloService.insertar(convertirAEntidad(dto));
            return ResponseEntity.ok(convertirADTO(nuevo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable int id, @RequestBody ArqueologoDTO dto) {
        try {
            Arqueologo existente = modeloService.consultarArqueologoPorId(id);
            if (existente == null || existente.getIdArqueologo() == 0) {
                return ResponseEntity.notFound().build();
            }
            if (dto.getNombre() != null) {
                existente.setNombre(dto.getNombre().trim());
            }
            if (dto.getApellidos() != null) {
                existente.setApellidos(dto.getApellidos().trim());
            }
            if (dto.getEspecialidad() != null) {
                existente.setEspecialidad(dto.getEspecialidad().trim());
            }
            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                existente.setCorreo(dto.getEmail().trim());
            }
            if (dto.getRol() != null && !dto.getRol().trim().isEmpty()) {
                existente.setRol(dto.getRol().trim());
            }

            Arqueologo modificado = modeloService.modificar(existente);
            return ResponseEntity.ok(convertirADTO(modificado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            modeloService.eliminar(id);
            return ResponseEntity.ok("Eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
