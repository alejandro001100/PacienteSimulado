package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.DisponibilidadActor;
import com.pacientesimulado.application.repository.DisponibilidadActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadActorService {

    private final DisponibilidadActorRepository repository;

    @Autowired
    public DisponibilidadActorService(DisponibilidadActorRepository repository) {
        this.repository = repository;
    }

    public List<DisponibilidadActor> obtenerDisponibilidadPorActorId(String actorId) {
        return repository.findByActorId(actorId);
    }

    public void guardarDisponibilidad(List<DisponibilidadActor> disponibilidades) {
        repository.saveAll(disponibilidades);
    }

    public void eliminarDisponibilidad(String id) {
        repository.deleteById(id);
    }
}
