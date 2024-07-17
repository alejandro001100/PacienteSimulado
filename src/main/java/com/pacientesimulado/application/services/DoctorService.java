package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.Doctor;
import com.pacientesimulado.application.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Optional<Doctor> obtenerDoctorPorCorreo(String correo) {
        return doctorRepository.findByCorreo(correo);
    }

    public void guardarDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }
}
