package org.example.arquealia.modelo;

import org.example.arquealia.dominio.Museo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MuseoRepository extends JpaRepository<Museo, Integer> {

    @Query("SELECT m FROM Museo m WHERE m.epocaEspecializada = :epoca")
    ArrayList<Museo> findByEpoca(@Param("epoca") String epoca);
}
