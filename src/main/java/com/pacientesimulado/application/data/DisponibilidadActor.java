package com.pacientesimulado.application.data;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "disponibilidad_actor")
public class DisponibilidadActor {

    @Id
    private String id;
    private String actorId;
    private Map<LocalDate, List<String>> disponibilidad = new HashMap<>();

    // Constructor
    public DisponibilidadActor(String actorId) {
        this.actorId = actorId;
    }


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

    public Map<LocalDate, List<String>> getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Map<LocalDate, List<String>> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void addDisponibilidad(LocalDate date, List<String> hours) {
        disponibilidad.put(date, hours);
    }

    public void removeDisponibilidad(LocalDate date, String hour) {
        if (disponibilidad.containsKey(date)) {
            List<String> hours = disponibilidad.get(date);
            hours.remove(hour);
            if (hours.isEmpty()) {
                disponibilidad.remove(date);
            } else {
                disponibilidad.put(date, hours);
            }
        }
    }
}
