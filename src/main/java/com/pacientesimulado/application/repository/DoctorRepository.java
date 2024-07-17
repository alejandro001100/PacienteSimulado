package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Optional<Doctor> findByCorreo(String correo);
    void deleteByCorreo(String correo);  
}
