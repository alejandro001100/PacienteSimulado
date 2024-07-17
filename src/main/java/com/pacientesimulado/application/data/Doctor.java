package com.pacientesimulado.application.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctores")
public class Doctor {
    @Id
    private String id;
    private String correo;
    private String nombre;
    private String apellido;
    private List<Disponibilidad> disponibilidades;

    public Doctor() {}

    public Doctor(String correo, String nombre, String apellido, List<Disponibilidad> disponibilidades) {
        this.correo = correo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.disponibilidades = disponibilidades;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
}
