
package com.pacientesimulado.application.controller;

import com.pacientesimulado.application.data.Actor;
import com.pacientesimulado.application.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/actores")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping("/registro")
    public ResponseEntity<Actor> registrarActor(@RequestBody Actor actor) {
        Actor nuevoActor = actorService.guardarActor(actor);
        return ResponseEntity.ok(nuevoActor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> actualizarActor(@PathVariable String id, @RequestBody Actor actorActualizado) {
        Actor actor = actorService.actualizarActor(id, actorActualizado);
        return ResponseEntity.ok(actor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> obtenerActorPorId(@PathVariable String id) {
        Actor actor = actorService.obtenerPorId(id);
        return ResponseEntity.ok(actor);
    }

    @GetMapping
    public List<Actor> obtenerTodosLosActores() {
        return actorService.obtenerTodosLosActores();
    }

    @GetMapping("/correo/{correo}")
    public Optional<Actor> obtenerActorPorCorreo(@PathVariable String correo) {
        return actorService.obtenerActorPorCorreo(correo);
    }

    @DeleteMapping("/{id}")
    public void eliminarActor(@PathVariable String id) {
        actorService.eliminarActor(id);
    }
}

