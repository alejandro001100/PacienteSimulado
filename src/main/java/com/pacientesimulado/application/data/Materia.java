package com.pacientesimulado.application.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "materias")
public class Materia {
    @Id
    private String id;
    private String carrera;
    private Map<String, List<String>> tiposYCasos;

    public Materia() {
    }

    public Materia(String carrera, Map<String, List<String>> tiposYCasos) {
        this.carrera = carrera;
        this.tiposYCasos = tiposYCasos;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Map<String, List<String>> getTiposYCasos() {
        return tiposYCasos;
    }

    public void setTiposYCasos(Map<String, List<String>> tiposYCasos) {
        this.tiposYCasos = tiposYCasos;
    }
}
