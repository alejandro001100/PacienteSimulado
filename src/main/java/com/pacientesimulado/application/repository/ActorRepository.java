package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends MongoRepository<Actor, String> {
    Optional<Actor> findByCorreo(String correo);
    Optional<Actor> findById(String id);
    void deleteByCorreo(String correo);
}
