package com.pacientesimulado.application.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "disponibilidad")
public class Disponibilidad {
    @Id
    private String id;
    private String actorId;
    private LocalDate fecha;
    private List<HoraDisponibilidad> horas;

    public Disponibilidad() {}

    public Disponibilidad(String actorId, LocalDate fecha, List<HoraDisponibilidad> horas) {
        this.actorId = actorId;
        this.fecha = fecha;
        this.horas = horas;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<HoraDisponibilidad> getHoras() {
        return horas;
    }

    public void setHoras(List<HoraDisponibilidad> horas) {
        this.horas = horas;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (this.horas != null && !this.horas.isEmpty()) {
            this.horas.get(0).setHora(horaInicio.toString());
        }
    }

    public void setHoraFin(LocalTime horaFin) {
        if (this.horas != null && this.horas.size() > 1) {
            this.horas.get(1).setHora(horaFin.toString());
        }
    }

    public LocalTime getHoraInicio() {
        if (this.horas != null && !this.horas.isEmpty()) {
            return LocalTime.parse(this.horas.get(0).getHora());
        }
        return null;
    }

    public LocalTime getHoraFin() {
        if (this.horas != null && this.horas.size() > 1) {
            return LocalTime.parse(this.horas.get(1).getHora());
        }
        return null;
    }

    public static class HoraDisponibilidad {
        private String hora;
        private String estado; // "libre", "ocupado" o nombre del caso clínico

        public String getHora() {
            return hora;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}
