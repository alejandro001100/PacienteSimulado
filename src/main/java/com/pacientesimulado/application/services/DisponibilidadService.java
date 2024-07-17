package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.DisponibilidadActor;
import com.pacientesimulado.application.repository.DisponibilidadActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadService {

    private final DisponibilidadActorRepository disponibilidadActorRepository;

    @Autowired
    public DisponibilidadService(DisponibilidadActorRepository disponibilidadActorRepository) {
        this.disponibilidadActorRepository = disponibilidadActorRepository;
    }

    public List<DisponibilidadActor> obtenerDisponibilidadesPorActorId(String actorId) {
        return disponibilidadActorRepository.findByActorId(actorId);
    }

    public DisponibilidadActor guardarDisponibilidad(DisponibilidadActor disponibilidadActor) {
        return disponibilidadActorRepository.save(disponibilidadActor);
    }

    public void eliminarDisponibilidad(String id) {
        disponibilidadActorRepository.deleteById(id);
    }
}
