package com.pacientesimulado.application.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Document(collection = "reservas")
public class Reserva {
    @Id
    private String id;
    private String correoDoctor;
    private String carrera;
    private String tipo;
    private String caso;
    private String actividad;
    private String aula;
    private Integer numeroPacientes;
    private String formaRequerimiento;
    private LocalDate fechaEntrenamiento;
    private List<String> horasEntrenamiento;
    private LocalDate fechaSeccion;
    private List<String> horasSeccion;
    private List<Paciente> pacientes;
    private String estado;
    private List<Actor> actoresAsignados;
    private String actorAsignadoId;
    private String tipoReserva;
    private List<Disponibilidad> disponibilidadEntrenamiento;
    private String modulo; // Nuevo atributo para módulo
    private String horaInicio; // Nuevo atributo para hora de inicio
    private boolean moulage;
    private LocalTime tiempoLlegadaMoulage;
    private String detalleMoulage;
    private String observacionesGenerales;

    public Reserva() {
    }

    // Constructor para disponibilidad
    public Reserva(String correoDoctor, String carrera, Map<LocalDate, List<String>> disponibilidad) {
        this.correoDoctor = correoDoctor;
        this.carrera = carrera;
        this.estado = "pendiente";
    }

    // Constructor completo
    public Reserva(String correoDoctor, String carrera, String tipo, String caso) {
        this.correoDoctor = correoDoctor;
        this.carrera = carrera;
        this.tipo = tipo;
        this.caso = caso;
        this.estado = "pendiente";
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreoDoctor() {
        return correoDoctor;
    }

    public void setCorreoDoctor(String correoDoctor) {
        this.correoDoctor = correoDoctor;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCaso() {
        return caso;
    }

    public void setCaso(String caso) {
        this.caso = caso;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Integer getNumeroPacientes() {
        return numeroPacientes;
    }

    public void setNumeroPacientes(Integer numeroPacientes) {
        this.numeroPacientes = numeroPacientes;
    }

    public String getFormaRequerimiento() {
        return formaRequerimiento;
    }

    public void setFormaRequerimiento(String formaRequerimiento) {
        this.formaRequerimiento = formaRequerimiento;
    }

    public LocalDate getFechaEntrenamiento() {
        return fechaEntrenamiento;
    }

    public void setFechaEntrenamiento(LocalDate fechaEntrenamiento) {
        this.fechaEntrenamiento = fechaEntrenamiento;
    }

    public List<String> getHorasEntrenamiento() {
        return horasEntrenamiento;
    }

    public void setHorasEntrenamiento(List<String> horasEntrenamiento) {
        this.horasEntrenamiento = horasEntrenamiento;
    }

    public LocalDate getFechaSeccion() {
        return fechaSeccion;
    }

    public void setFechaSeccion(LocalDate fechaSeccion) {
        this.fechaSeccion = fechaSeccion;
    }

    public List<String> getHorasSeccion() {
        return horasSeccion;
    }

    public void setHorasSeccion(List<String> horasSeccion) {
        this.horasSeccion = horasSeccion;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Actor> getActoresAsignados() {
        return actoresAsignados;
    }

    public void setActoresAsignados(List<Actor> actoresAsignados) {
        this.actoresAsignados = actoresAsignados;
    }

    public String getActorAsignadoId() {
        return actorAsignadoId;
    }

    public void setActorAsignadoId(String actorAsignadoId) {
        this.actorAsignadoId = actorAsignadoId;
    }

    public String getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public List<Disponibilidad> getDisponibilidadEntrenamiento() {
        return disponibilidadEntrenamiento;
    }

    public void setDisponibilidadEntrenamiento(List<Disponibilidad> disponibilidadEntrenamiento) {
        this.disponibilidadEntrenamiento = disponibilidadEntrenamiento;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public boolean isMoulage() {
        return moulage;
    }

    public void setMoulage(boolean moulage) {
        this.moulage = moulage;
    }

    public LocalTime getTiempoLlegadaMoulage() {
        return tiempoLlegadaMoulage;
    }

    public void setTiempoLlegadaMoulage(LocalTime tiempoLlegadaMoulage) {
        this.tiempoLlegadaMoulage = tiempoLlegadaMoulage;
    }

    public String getDetalleMoulage() {
        return detalleMoulage;
    }

    public void setDetalleMoulage(String detalleMoulage) {
        this.detalleMoulage = detalleMoulage;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }
}
