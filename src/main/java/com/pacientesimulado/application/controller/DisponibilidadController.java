package com.pacientesimulado.application.controller;

import com.pacientesimulado.application.data.DisponibilidadActor;
import com.pacientesimulado.application.services.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @Autowired
    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping("/actor/{actorId}")
    public List<DisponibilidadActor> obtenerDisponibilidadesPorActorId(@PathVariable String actorId) {
        return disponibilidadService.obtenerDisponibilidadesPorActorId(actorId);
    }

    @PostMapping("/guardar")
    public DisponibilidadActor guardarDisponibilidad(@RequestBody DisponibilidadActor disponibilidadActor) {
        return disponibilidadService.guardarDisponibilidad(disponibilidadActor);
    }

    @DeleteMapping("/{id}")
    public void eliminarDisponibilidad(@PathVariable String id) {
        disponibilidadService.eliminarDisponibilidad(id);
    }
}
