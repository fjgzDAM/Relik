package org.example.arquealia.controlador;

import org.example.arquealia.dominio.Arqueologo;
import org.example.arquealia.modelo.ModeloInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arqueologos")
@CrossOrigin(origins = "*") // Permite que NetBeans se conecte sin bloqueos de seguridad
public class ArqueologoController {

    @Autowired
    private ModeloInterface modeloService;

    // Cuando NetBeans haga un GET a http://localhost:8080/api/arqueologos, devolverá la lista
    @GetMapping
    public List<Arqueologo> listarTodos() {
        return modeloService.listarArqueologos();
    }

    // Cuando NetBeans haga un POST para registrar un nuevo arqueólogo
    @PostMapping
    public ResponseEntity<Arqueologo> registrar(@RequestBody Arqueologo arqueologo) {
        Arqueologo nuevo = modeloService.insertar(arqueologo);
        return ResponseEntity.ok(nuevo);
    }
}