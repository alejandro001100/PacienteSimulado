package com.pacientesimulado.application.data;

public class Paciente {
    private String genero;
    private String rangoEdad;
    private boolean requiereMoulage;
    private String detalleMoulage;

    public Paciente() {
    }

    public Paciente(String genero, String rangoEdad, boolean requiereMoulage, String detalleMoulage) {
        this.genero = genero;
        this.rangoEdad = rangoEdad;
        this.requiereMoulage = requiereMoulage;
        this.detalleMoulage = detalleMoulage;
    }

    // Getters y Setters
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getRangoEdad() {
        return rangoEdad;
    }

    public void setRangoEdad(String rangoEdad) {
        this.rangoEdad = rangoEdad;
    }

    public boolean isRequiereMoulage() {
        return requiereMoulage;
    }

    public void setRequiereMoulage(boolean requiereMoulage) {
        this.requiereMoulage = requiereMoulage;
    }

    public String getDetalleMoulage() {
        return detalleMoulage;
    }

    public void setDetalleMoulage(String detalleMoulage) {
        this.detalleMoulage = detalleMoulage;
    }
}
