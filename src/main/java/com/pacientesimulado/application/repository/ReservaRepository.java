package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Reserva;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends MongoRepository<Reserva, String> {
    List<Reserva> findByCorreoDoctor(String correoDoctor);
}
