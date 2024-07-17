package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.DisponibilidadActor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadActorRepository extends MongoRepository<DisponibilidadActor, String> {
    List<DisponibilidadActor> findByActorId(String actorId);
}
