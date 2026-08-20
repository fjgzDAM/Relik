package org.example.relik.controlador;

import org.example.relik.dominio.Yacimiento;
import org.example.relik.dto.YacimientoDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/yacimientos")
@CrossOrigin(origins = "*")
public class YacimientoController {

    @Autowired
    private ModeloInterface modeloService;

    // --- TRADUCTORES ---
    private YacimientoDTO convertirADTO(Yacimiento y) {
        return new YacimientoDTO(
                (long) y.getIdYacimiento(),
                y.getNombre(),
                y.getUbicacion() != null ? y.getUbicacion() : "",
                y.getCoordenadas() != null ? y.getCoordenadas() : "",
                y.getEpoca() != null ? y.getEpoca() : "General",
                y.getFechaDescubrimiento() != null ? y.getFechaDescubrimiento().toString() : ""
        );
    }

    private Yacimiento convertirAEntidad(YacimientoDTO dto) {
        Yacimiento y = new Yacimiento();
        y.setNombre(dto.getNombre());
        y.setUbicacion(dto.getUbicacion() != null ? dto.getUbicacion() : dto.getLocalizacion());
        y.setCoordenadas(dto.getCoordenadas() != null ? dto.getCoordenadas() : dto.getDescripcion());
        y.setEpoca(dto.getEpoca() != null ? dto.getEpoca() : "General");
        String fecha = dto.getFechaInicio() != null ? dto.getFechaInicio() : dto.getPeriodo();
        if (fecha != null && !fecha.isEmpty()) {
            try {
                y.setFechaDescubrimiento(LocalDate.parse(fecha));
            } catch (Exception e) {
                y.setFechaDescubrimiento(LocalDate.now());
            }
        } else {
            y.setFechaDescubrimiento(LocalDate.now());
        }
        return y;
    }

    @GetMapping
    public List<YacimientoDTO> listarTodos() {
        return modeloService.listarYacimientos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<YacimientoDTO> obtenerPorId(@PathVariable int id) {
        Yacimiento y = modeloService.consultarYacimientoPorId(id);
        return (y != null && y.getIdYacimiento() > 0) ? ResponseEntity.ok(convertirADTO(y)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody YacimientoDTO dto) {
        try {
            Yacimiento y = convertirAEntidad(dto);
            Yacimiento guardado = modeloService.insertar(y);
            return ResponseEntity.ok(convertirADTO(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable int id, @RequestBody YacimientoDTO dto) {
        try {
            Yacimiento existente = modeloService.consultarYacimientoPorId(id);
            if (existente == null || existente.getIdYacimiento() == 0) {
                return ResponseEntity.notFound().build();
            }
            if (dto.getNombre() != null) existente.setNombre(dto.getNombre());
            
            if (dto.getUbicacion() != null) existente.setUbicacion(dto.getUbicacion());
            else if (dto.getLocalizacion() != null) existente.setUbicacion(dto.getLocalizacion());

            if (dto.getCoordenadas() != null) existente.setCoordenadas(dto.getCoordenadas());
            else if (dto.getDescripcion() != null) existente.setCoordenadas(dto.getDescripcion());

            if (dto.getEpoca() != null) existente.setEpoca(dto.getEpoca());
            
            Yacimiento modificado = modeloService.modificar(existente);
            return ResponseEntity.ok(convertirADTO(modificado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            Yacimiento y = modeloService.consultarYacimientoPorId(id);
            if (y != null && y.getIdYacimiento() > 0) {
                modeloService.eliminar(y);
                return ResponseEntity.ok("Eliminado");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

