package com.pacientesimulado.application.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "actores")
public class Actor {
    @Id
    private String id;
    private String nombre;
    private String correo;
    private int edad;
    private String sexo;
    private double peso;
    private double altura;
    private List<Disponibilidad> disponibilidades = new ArrayList<>();  // Inicializar la lista
    private List<SesionAsignada> sesionesAsignadas = new ArrayList<>(); // Inicializar la lista

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public List<SesionAsignada> getSesionesAsignadas() {
        return sesionesAsignadas;
    }

    public void setSesionesAsignadas(List<SesionAsignada> sesionesAsignadas) {
        this.sesionesAsignadas = sesionesAsignadas;
    }
}
