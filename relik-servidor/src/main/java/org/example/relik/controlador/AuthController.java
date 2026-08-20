package org.example.relik.controlador;

import org.example.relik.dominio.Arqueologo;
import org.example.relik.dto.ArqueologoDTO;
import org.example.relik.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private ModeloInterface modeloService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String correo = payload.get("correo");
        String contrasena = payload.get("contrasena");

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest().body("Correo y contraseña requeridos.");
        }

        Arqueologo a = modeloService.consultarArqueologoPorCorreo(correo.trim());
        if (a == null || !a.getContrasena().equals(contrasena.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        ArqueologoDTO dto = new ArqueologoDTO(
                (long) a.getIdArqueologo(),
                a.getNombre(),
                "",
                "General",
                a.getCorreo(),
                a.getRol()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String nombre = payload.get("nombre");
        String correo = payload.get("correo");
        String contrasena = payload.get("contrasena");
        String rol = payload.getOrDefault("rol", "ARQUEOLOGO");

        if (nombre == null || nombre.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Todos los campos (nombre, correo, contraseña) son obligatorios.");
        }

        Arqueologo existente = modeloService.consultarArqueologoPorCorreo(correo.trim());
        if (existente != null && existente.getIdArqueologo() > 0) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está registrado.");
        }

        Arqueologo nuevo = new Arqueologo(nombre.trim(), correo.trim(), contrasena.trim(), rol);
        Arqueologo guardado = modeloService.insertar(nuevo);

        ArqueologoDTO dto = new ArqueologoDTO(
                (long) guardado.getIdArqueologo(),
                guardado.getNombre(),
                "",
                "General",
                guardado.getCorreo(),
                guardado.getRol()
        );
        return ResponseEntity.ok(dto);
    }
}

