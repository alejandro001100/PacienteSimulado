package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.Actor;
import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.repository.ActorRepository;
import com.pacientesimulado.application.repository.UsuarioRepository;
//import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ActorService(ActorRepository actorRepository, UsuarioService usuarioService) {
        this.actorRepository = actorRepository;
        this.usuarioService = usuarioService;
    }

    public List<Actor> obtenerTodosLosActores() {
        List<Actor> actores = actorRepository.findAll();
        actores.forEach(actor -> {
            usuarioService.obtenerUsuarioPorCorreoOptional(actor.getCorreo()).ifPresent(usuario -> {
                actor.setNombre(usuario.getNombre() + " " + usuario.getApellido());
            });
        });
        return actores;
    }

    public Optional<Actor> obtenerActorPorId(String id) {
        return actorRepository.findById(id);
    }

    public Optional<Actor> obtenerActorPorCorreo(String correo) {
        Optional<Actor> actorOptional = actorRepository.findByCorreo(correo);
        actorOptional.ifPresent(actor -> {
            usuarioService.obtenerUsuarioPorCorreoOptional(correo).ifPresent(usuario -> {
                actor.setNombre(usuario.getNombre() + " " + usuario.getApellido());
            });
        });
        return actorOptional;
    }

    public void eliminarActor(String id) {
        actorRepository.deleteById(id);
    }

    public Actor guardarActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Actor actualizarActor(String id, Actor actorActualizado) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if (optionalActor.isPresent()) {
            Actor actor = optionalActor.get();
            actor.setNombre(actorActualizado.getNombre());
            actor.setCorreo(actorActualizado.getCorreo());
            actor.setEdad(actorActualizado.getEdad());
            actor.setSexo(actorActualizado.getSexo());
            actor.setPeso(actorActualizado.getPeso());
            actor.setAltura(actorActualizado.getAltura());
            return actorRepository.save(actor);
        } else {
            return null;
        }
    }

    public Actor obtenerPorId(String id) {
        return actorRepository.findById(id).orElse(null);
    }

    // @PostConstruct
    // public void cargarActoresEstáticos() {
    //     List<String[]> datosActores = Arrays.asList(
    //             new String[]{"Alexander Jefferson Cabascango Valencia", "alexitocv22@gmail.com", "1718600115"},
    //             new String[]{"Andrea Elizabeth Brito Cruz", "andreabritogye@gmail.com", "919560441"},
    //             new String[]{"Danae Fernanda Calle Avilés", "danaefernanda@gmail.com", "1725268161"},
    //             new String[]{"Dayana Gisselle Delgado Andrade", "dagdelgado5@gmail.com", "1724529720"},
    //             new String[]{"Diana María Madrigal Arango", "dmadrigalarango@gmail.com", "1720464344"},
    //             new String[]{"Diego Daniel Cerda Hidalgo", "ddchddch@gmail.com", "1721592705"},
    //             new String[]{"Diego Tomas Flores Cabezas", "tomas.flores.art@gmail.com", "1717553414"},
    //             new String[]{"Esteban Mauricio Pulupa Tiaguaro", "underpe1695@gmail.com", "1724745276"},
    //             new String[]{"Guillermo Sotomayor Estrugo", "guillermo.sotomayor.estrugo@gmail.com", "1717236242"},
    //             new String[]{"Isabel del Carmen Gamboa Mejia", "Isabelgamboamejia6@gmail.com", "1703830842"},
    //             new String[]{"José Roberto Chucuma Lara", "jrchucuma@gmail.com", "1712388121"},
    //             new String[]{"Juan Carlos Benítez Vargas", "benomon20@gmail.com", "1716399041"},
    //             new String[]{"Laura Cristina Oviedo Navarrete", "laura.oviedocn@gmail.com", "1715486195"},
    //             new String[]{"Maira Eliana Tiaguaro Males", "mtiaguaro123@hotmail.com", "1714649728"},
    //             new String[]{"Marco Antonio Lagla Corrales", "marco.lagla14@gmail.com", "1713507380"},
    //             new String[]{"Maria Esther Cevallos Jimenez", "esthercevallosjimenez@gmail.com", "1712382728"},
    //             new String[]{"Maria Fernanda Auz Ceron", "ne.gr.azul8.5@gmail.com", "1718360173"},
    //             new String[]{"Mirian Jenny Gahona Calderón", "mily_g2676@hotmail.com", "1712372943"},
    //             new String[]{"Rommel Mauricio Albuja Proaño", "albujarommel@gmail.com", "1717047193"},
    //             new String[]{"Ruben Dario Gabela Cordoba", "rubengabela@gmail.com", "1715857254"},
    //             new String[]{"Sara Elena Alban Landeta", "sarironia@gmail.com", "1722711494"},
    //             new String[]{"Sara Elizabeth Solis Gamboa", "sarasolis2013@gmail.com", "1600320400"},
    //             new String[]{"Susana Carolina Balseca Gamboa", "carolinabalseca93@gmail.com", "1715180749"},
    //             new String[]{"Tamiana Yanandi Naranjo Bonilla", "tamiananaranjo@gmail.com", "1715239735"}
    //     );

    //     datosActores.forEach(datos -> {
    //         String nombres = datos[0];
    //         String correo = datos[1];
    //         String cedula = datos[2];

    //         Usuario usuario = new Usuario();
    //         usuario.setCorreo(correo);
    //         usuario.setNombre(nombres.split(" ")[0]);
    //         usuario.setApellido(nombres.substring(nombres.indexOf(" ") + 1));
    //         usuario.setContraseña(cedula);  // Usar la cédula como contraseña
    //         usuario.setRol("Actor");

    //         usuarioRepository.save(usuario);

    //         Actor actor = new Actor();
    //         actor.setCorreo(correo);
    //         actorRepository.save(actor);
    //     });
    // }
}
