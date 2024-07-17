package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Disponibilidad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadRepository extends MongoRepository<Disponibilidad, String> {
    List<Disponibilidad> findByActorId(String actorId);
}
