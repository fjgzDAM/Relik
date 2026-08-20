package org.example.relik.modelo;

import org.example.relik.dominio.Arqueologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArqueologoRespository extends JpaRepository<Arqueologo, Integer> {
    Optional<Arqueologo> findByCorreo(String correo);
    Optional<Arqueologo> findByCorreoIgnoreCase(String correo);
}

