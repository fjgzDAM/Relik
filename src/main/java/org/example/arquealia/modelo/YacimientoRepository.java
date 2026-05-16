package org.example.arquealia.modelo;

import org.example.arquealia.dominio.Yacimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface YacimientoRepository extends JpaRepository<Yacimiento, Integer> {

    Optional<Yacimiento> findByNombre(String nombre);

}
