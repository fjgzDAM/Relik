package org.example.arquealia.modelo;

import org.example.arquealia.dominio.Arqueologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArqueologoRespository extends JpaRepository<Arqueologo, Integer> {
    Optional<Arqueologo> findByCorreo(String correo);
}
