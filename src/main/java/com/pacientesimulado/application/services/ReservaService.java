package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.Actor;
import com.pacientesimulado.application.data.Reserva;
import com.pacientesimulado.application.repository.ReservaRepository;
import com.pacientesimulado.application.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    public void guardarReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public void asignarActor(Reserva reserva, Actor actor) {
        if (reserva.getActoresAsignados() == null) {
            reserva.setActoresAsignados(new ArrayList<>());
        }
        reserva.getActoresAsignados().add(actor);
        if (reserva.getActoresAsignados().size() >= reserva.getNumeroPacientes()) {
            reserva.setEstado("asignado");
        }
        guardarReserva(reserva);
    }

    public Actor obtenerActorAsignado(String idReserva) {
        Reserva reserva = obtenerReservaPorId(idReserva);
        if (reserva != null && !reserva.getActoresAsignados().isEmpty()) {
            return reserva.getActoresAsignados().get(0);
        }
        return null;
    }

    private Reserva obtenerReservaPorId(String idReserva) {
        return reservaRepository.findById(idReserva).orElse(null);
    }
}
