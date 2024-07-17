package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MateriaRepository extends MongoRepository<Materia, String> {
    List<Materia> findByCarrera(String carrera);
}
