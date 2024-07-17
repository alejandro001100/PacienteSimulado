package com.pacientesimulado.application.controller;

import com.pacientesimulado.application.data.DisponibilidadActor;
import com.pacientesimulado.application.services.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final DisponibilidadService disponibilidadService;

    @Autowired
    public UsuarioController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping("/{actorId}/disponibilidad")
    public List<DisponibilidadActor> obtenerDisponibilidadPorActorId(@PathVariable String actorId) {
        return disponibilidadService.obtenerDisponibilidadesPorActorId(actorId);
    }

    @PostMapping("/disponibilidad")
    public DisponibilidadActor guardarDisponibilidad(@RequestBody DisponibilidadActor disponibilidadActor) {
        return disponibilidadService.guardarDisponibilidad(disponibilidadActor);
    }

    @DeleteMapping("/disponibilidad/{id}")
    public void eliminarDisponibilidad(@PathVariable String id) {
        disponibilidadService.eliminarDisponibilidad(id);
    }
}
