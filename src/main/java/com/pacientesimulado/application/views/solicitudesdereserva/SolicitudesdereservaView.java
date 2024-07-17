package com.pacientesimulado.application.views.solicitudesdereserva;

import com.pacientesimulado.application.data.*;
import com.pacientesimulado.application.services.*;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Route(value = "solicitudes-de-reserva", layout = MainLayout.class)
@PageTitle("Solicitudes de Reserva")
public class SolicitudesdereservaView extends VerticalLayout {

    private final ReservaService reservaService;
    private final ActorService actorService;
    private final UsuarioService usuarioService;
    private final DoctorService doctorService;
    private final Grid<Reserva> grid;
    private static final Logger LOGGER = Logger.getLogger(SolicitudesdereservaView.class.getName());

    @Autowired
    public SolicitudesdereservaView(ReservaService reservaService, ActorService actorService, UsuarioService usuarioService, DoctorService doctorService) {
        this.reservaService = reservaService;
        this.actorService = actorService;
        this.usuarioService = usuarioService;
        this.doctorService = doctorService;
        this.grid = new Grid<>(Reserva.class, false);

        configureGrid();
        add(grid, createExportButton());

        listReservas();
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Reserva::getEstado).setHeader("Estado").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getTipoReserva).setHeader("Tipo de Sección").setSortable(true).setAutoWidth(true);
        grid.addColumn(reserva -> {
            Usuario doctor = usuarioService.obtenerUsuarioPorCorreo(reserva.getCorreoDoctor()).orElse(null);
            return doctor != null ? doctor.getNombre() + " " + doctor.getApellido() : reserva.getCorreoDoctor();
        }).setHeader("Doctor").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getActividad).setHeader("Actividad").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getCaso).setHeader("Caso").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getModulo).setHeader("Módulo").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getHorasSeccion).setHeader("Hora/Inicio").setSortable(true).setAutoWidth(true);
        grid.addColumn(Reserva::getAula).setHeader("Aula").setSortable(true).setAutoWidth(true);

        grid.addItemClickListener(event -> showAssignActorDialog(event.getItem(), 1));
    }

    private void listReservas() {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        if (reservas != null && !reservas.isEmpty()) {
            grid.setItems(reservas);
            Notification.show("Reservas cargadas correctamente: " + reservas.size());
        } else {
            Notification.show("No se encontraron reservas.");
        }
    }

    private void showAssignActorDialog(Reserva reserva, int actorIndex) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("auto");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        ComboBox<Actor> actorComboBox = new ComboBox<>("Seleccione Actor");
        actorComboBox.setItemLabelGenerator(actor -> {
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(actor.getCorreo()).orElse(null);
            return (usuario != null) ? usuario.getNombre() + " " + usuario.getApellido() : "Sin nombre";
        });

        List<Actor> todosLosActores = actorService.obtenerTodosLosActores();
        actorComboBox.setItems(todosLosActores);

        // Preseleccionar el actor asignado
        if (reserva.getActoresAsignados() != null && !reserva.getActoresAsignados().isEmpty()) {
            Actor actorAsignado = reserva.getActoresAsignados().get(actorIndex - 1);
            actorComboBox.setValue(actorAsignado);
        }

        ComboBox<String> tipoSeccionComboBox = new ComboBox<>("Tipo de Sección");
        tipoSeccionComboBox.setItems("Tipo A", "Tipo B");

        ComboBox<String> generoComboBox = new ComboBox<>("Género del paciente");
        generoComboBox.setItems("Femenino", "Masculino", "No relevante");
        generoComboBox.setWidth("min-content");

        ComboBox<String> edadComboBox = new ComboBox<>("Rango de edad del paciente simulado");
        edadComboBox.setItems("Joven (18-29 años)", "Adulto (30-39 años)", "Adulto medio (40-49 años)", "Adulto mayor (50 años en adelante)", "No relevante");
        edadComboBox.setWidth("260px");

        Paciente paciente = reserva.getPacientes().get(actorIndex - 1);
        generoComboBox.setValue(paciente.getGenero());
        edadComboBox.setValue(paciente.getRangoEdad());
        tipoSeccionComboBox.setValue(reserva.getTipoReserva());

        ComboBox<LocalDate> fechaPracticaComboBox = new ComboBox<>("Fecha de Práctica");
        ComboBox<String> horaPracticaComboBox = new ComboBox<>("Hora de Práctica");

        fechaPracticaComboBox.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                filterHorasDisponibles(reserva.getCorreoDoctor(), selectedDate, horaPracticaComboBox);
                if (horaPracticaComboBox.getValue() != null) {
                    filterActors(actorComboBox, generoComboBox.getValue(), edadComboBox.getValue(), selectedDate, horaPracticaComboBox.getValue());
                }
            }
        });

        horaPracticaComboBox.addValueChangeListener(event -> {
            if (fechaPracticaComboBox.getValue() != null && event.getValue() != null) {
                filterActors(actorComboBox, generoComboBox.getValue(), edadComboBox.getValue(), fechaPracticaComboBox.getValue(), event.getValue());
            }
        });

        filterFechasYHorasDisponibles(reserva.getCorreoDoctor(), fechaPracticaComboBox, horaPracticaComboBox);

        TextField aulaTextField = new TextField("Aula");
        aulaTextField.setValue(reserva.getAula() != null ? reserva.getAula() : "");

        if (reserva.getFechaEntrenamiento() == null || reserva.getHorasEntrenamiento() == null || reserva.getHorasEntrenamiento().isEmpty()) {
            dialogLayout.add(new HorizontalLayout(new Span("Fecha de Práctica: "), fechaPracticaComboBox));
            dialogLayout.add(new HorizontalLayout(new Span("Hora de Práctica: "), horaPracticaComboBox));
        } else {
            dialogLayout.add(
                    new Span("Fecha de Práctica: " + reserva.getFechaEntrenamiento()),
                    new Span("Hora de Práctica: " + String.join(", ", reserva.getHorasEntrenamiento()))
            );
        }

        Button assignButton = new Button("Asignar");
        assignButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        assignButton.addClickListener(event -> {
            if (actorComboBox.getValue() != null) {
                Actor actorSeleccionado = actorComboBox.getValue();
                String tipoSeccion = tipoSeccionComboBox.getValue();
                LocalDate fechaPractica = fechaPracticaComboBox.getValue();
                String horaPractica = horaPracticaComboBox.getValue();

                SesionAsignada sesionAsignada = new SesionAsignada();
                sesionAsignada.setIdReserva(reserva.getId());
                sesionAsignada.setTipoSeccion(tipoSeccion);
                actorSeleccionado.getSesionesAsignadas().add(sesionAsignada);

                actualizarDisponibilidadActor(actorSeleccionado, reserva, horaPractica);
                actualizarDisponibilidadDoctor(reserva.getCorreoDoctor(), actorSeleccionado, reserva, horaPractica);

                actorService.guardarActor(actorSeleccionado);
                reserva.setTipoReserva(tipoSeccion);
                reserva.setFechaEntrenamiento(fechaPractica);
                reserva.setHorasEntrenamiento(Collections.singletonList(horaPractica));
                reserva.setAula(aulaTextField.getValue());
                reservaService.asignarActor(reserva, actorSeleccionado);

                Notification.show("Actor " + actorIndex + " asignado correctamente.");
                dialog.close();
                listReservas();
            } else {
                Notification.show("Por favor, seleccione un actor.");
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialogLayout.add(
                new Span("Estado: " + reserva.getEstado()),
                new Span("Tipo de Sección: " + (reserva.getTipoReserva() != null ? reserva.getTipoReserva() : "No asignada")),
                new Span("Doctor: " + (usuarioService.obtenerUsuarioPorCorreo(reserva.getCorreoDoctor()).map(doc -> doc.getNombre() + " " + doc.getApellido()).orElse("Desconocido"))),
                new Span("Correo del Doctor: " + reserva.getCorreoDoctor()),
                new Span("Actividad: " + reserva.getActividad()),
                new Span("Carrera: " + reserva.getCarrera()),
                new Span("Caso: " + reserva.getCaso()),
                new Span("Fecha de Sección: " + (reserva.getFechaSeccion() != null ? reserva.getFechaSeccion() : "No asignada")),
                new Span("Hora de Sección: " + (reserva.getHorasSeccion() != null ? String.join(", ", reserva.getHorasSeccion()) : "No asignada")),
                generoComboBox, edadComboBox, actorComboBox, tipoSeccionComboBox, aulaTextField,
                new HorizontalLayout(assignButton, cancelButton)
        );

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void filterFechasYHorasDisponibles(String correoDoctor, ComboBox<LocalDate> fechaPracticaComboBox, ComboBox<String> horaPracticaComboBox) {
        Optional<Doctor> doctorOptional = doctorService.obtenerDoctorPorCorreo(correoDoctor);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            List<Disponibilidad> disponibilidades = doctor.getDisponibilidades();

            Set<LocalDate> fechasDisponibles = disponibilidades.stream()
                    .map(Disponibilidad::getFecha)
                    .collect(Collectors.toSet());
            fechaPracticaComboBox.setItems(fechasDisponibles);

            fechaPracticaComboBox.addValueChangeListener(event -> {
                LocalDate selectedDate = event.getValue();
                if (selectedDate != null) {
                    List<String> horasDisponibles = disponibilidades.stream()
                            .filter(disponibilidad -> disponibilidad.getFecha().equals(selectedDate))
                            .flatMap(disponibilidad -> disponibilidad.getHoras().stream())
                            .filter(horaDisponibilidad -> horaDisponibilidad.getEstado().equals("libre"))
                            .map(Disponibilidad.HoraDisponibilidad::getHora)
                            .collect(Collectors.toList());
                    horaPracticaComboBox.setItems(horasDisponibles);
                }
            });
        }
    }

    private void filterHorasDisponibles(String correoDoctor, LocalDate selectedDate, ComboBox<String> horaPracticaComboBox) {
        Optional<Doctor> doctorOptional = doctorService.obtenerDoctorPorCorreo(correoDoctor);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            List<String> horasDisponibles = doctor.getDisponibilidades().stream()
                    .filter(disponibilidad -> disponibilidad.getFecha().equals(selectedDate))
                    .flatMap(disponibilidad -> disponibilidad.getHoras().stream())
                    .filter(horaDisponibilidad -> horaDisponibilidad.getEstado().equals("libre"))
                    .map(Disponibilidad.HoraDisponibilidad::getHora)
                    .collect(Collectors.toList());
            horaPracticaComboBox.setItems(horasDisponibles);
        }
    }

    private void actualizarDisponibilidadActor(Actor actor, Reserva reserva, String horaSeleccionada) {
        String nombreDoctor = usuarioService.obtenerUsuarioPorCorreo(reserva.getCorreoDoctor())
                .map(doc -> doc.getNombre() + " " + doc.getApellido())
                .orElse("Desconocido");

        actor.getDisponibilidades().forEach(disponibilidad -> {
            if (disponibilidad.getFecha().equals(reserva.getFechaEntrenamiento())) {
                disponibilidad.getHoras().forEach(horaDisponibilidad -> {
                    if (horaDisponibilidad.getHora().equals(horaSeleccionada)) {
                        horaDisponibilidad.setEstado("Entrenamiento con " + nombreDoctor + " para la sección " + reserva.getCaso());
                    }
                });
            }
        });
    }

    private void actualizarDisponibilidadDoctor(String correoDoctor, Actor actor, Reserva reserva, String horaSeleccionada) {
        Optional<Doctor> doctorOptional = doctorService.obtenerDoctorPorCorreo(correoDoctor);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            String nombreActor = usuarioService.obtenerUsuarioPorCorreo(actor.getCorreo())
                    .map(act -> act.getNombre() + " " + act.getApellido())
                    .orElse("Desconocido");

            doctor.getDisponibilidades().forEach(disponibilidad -> {
                if (disponibilidad.getFecha().equals(reserva.getFechaEntrenamiento())) {
                    disponibilidad.getHoras().forEach(horaDisponibilidad -> {
                        if (horaDisponibilidad.getHora().equals(horaSeleccionada)) {
                            horaDisponibilidad.setEstado("Entrenamiento con " + nombreActor + " para la sección " + reserva.getCaso());
                        }
                    });
                }
            });
            doctorService.guardarDoctor(doctor);
        }
    }

    private void filterActors(ComboBox<Actor> actorComboBox, String genero, String edad, LocalDate fechaEntrenamiento, String horaEntrenamiento) {
        List<Actor> todosLosActores = actorService.obtenerTodosLosActores();

        List<Actor> actoresFiltrados = todosLosActores.stream()
                .filter(actor -> {
                    boolean generoCoincide = genero.equals("No relevante") || genero.equals(actor.getSexo());
                    boolean edadCoincide = false;

                    if (edad.equals("No relevante")) {
                        edadCoincide = true;
                    } else {
                        switch (edad) {
                            case "Joven (18-29 años)":
                                edadCoincide = actor.getEdad() >= 18 && actor.getEdad() <= 29;
                                break;
                            case "Adulto (30-39 años)":
                                edadCoincide = actor.getEdad() >= 30 && actor.getEdad() <= 39;
                                break;
                            case "Adulto medio (40-49 años)":
                                edadCoincide = actor.getEdad() >= 40 && actor.getEdad() <= 49;
                                break;
                            case "Adulto mayor (50 años en adelante)":
                                edadCoincide = actor.getEdad() >= 50;
                                break;
                        }
                    }

                    boolean disponibilidadCoincide = actor.getDisponibilidades().stream()
                            .anyMatch(disponibilidad -> disponibilidad.getFecha().equals(fechaEntrenamiento) &&
                                    disponibilidad.getHoras().stream()
                                            .anyMatch(horaDisponibilidad -> horaDisponibilidad.getHora().equals(horaEntrenamiento) &&
                                                    horaDisponibilidad.getEstado().equals("libre")));

                    return generoCoincide && edadCoincide && disponibilidadCoincide;
                })
                .collect(Collectors.toList());

        actorComboBox.setItems(actoresFiltrados);
        actorComboBox.setItemLabelGenerator(actor -> {
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(actor.getCorreo()).orElse(null);
            return (usuario != null) ? usuario.getNombre() + " " + usuario.getApellido() : "Sin nombre";
        });
    }

    private Button createExportButton() {
        Button exportButton = new Button("Exportar a Excel");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportButton.addClickListener(event -> {
            StreamResource resource = new StreamResource("reservas.xlsx", this::exportToExcel);
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
        });
        return exportButton;
    }

    private String getModulo(LocalDate date, String hora) {
        String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();
        Map<String, String> moduloMap = new HashMap<>();
        moduloMap.put("LUNES 07h00", "101");
        moduloMap.put("LUNES 08h05", "102");
        moduloMap.put("LUNES 09h10", "103");
        moduloMap.put("LUNES 10h15", "104");
        moduloMap.put("LUNES 11h20", "105");
        moduloMap.put("LUNES 12h25", "106");
        moduloMap.put("LUNES 13h30", "107");
        moduloMap.put("LUNES 14h35", "108");
        moduloMap.put("LUNES 15h40", "109");
        moduloMap.put("LUNES 16h45", "110");
        moduloMap.put("LUNES 17h50", "111");
        moduloMap.put("LUNES 18h50", "112");
        moduloMap.put("MARTES 07h00", "201");
        moduloMap.put("MARTES 08h05", "202");
        moduloMap.put("MARTES 09h10", "203");
        moduloMap.put("MARTES 10h15", "204");
        moduloMap.put("MARTES 11h20", "205");
        moduloMap.put("MARTES 12h25", "206");
        moduloMap.put("MARTES 13h30", "207");
        moduloMap.put("MARTES 14h35", "208");
        moduloMap.put("MARTES 15h40", "209");
        moduloMap.put("MARTES 16h45", "210");
        moduloMap.put("MARTES 17h50", "211");
        moduloMap.put("MARTES 18h50", "212");
        moduloMap.put("MIÉRCOLES 07h00", "301");
        moduloMap.put("MIÉRCOLES 08h05", "302");
        moduloMap.put("MIÉRCOLES 09h10", "303");
        moduloMap.put("MIÉRCOLES 10h15", "304");
        moduloMap.put("MIÉRCOLES 11h20", "305");
        moduloMap.put("MIÉRCOLES 12h25", "306");
        moduloMap.put("MIÉRCOLES 13h30", "307");
        moduloMap.put("MIÉRCOLES 14h35", "308");
        moduloMap.put("MIÉRCOLES 15h40", "309");
        moduloMap.put("MIÉRCOLES 16h45", "310");
        moduloMap.put("MIÉRCOLES 17h50", "311");
        moduloMap.put("MIÉRCOLES 18h50", "312");
        moduloMap.put("JUEVES 07h00", "401");
        moduloMap.put("JUEVES 08h05", "402");
        moduloMap.put("JUEVES 09h10", "403");
        moduloMap.put("JUEVES 10h15", "404");
        moduloMap.put("JUEVES 11h20", "405");
        moduloMap.put("JUEVES 12h25", "406");
        moduloMap.put("JUEVES 13h30", "407");
        moduloMap.put("JUEVES 14h35", "408");
        moduloMap.put("JUEVES 15h40", "409");
        moduloMap.put("JUEVES 16h45", "410");
        moduloMap.put("JUEVES 17h50", "411");
        moduloMap.put("JUEVES 18h50", "412");
        moduloMap.put("VIERNES 07h00", "501");
        moduloMap.put("VIERNES 08h05", "502");
        moduloMap.put("VIERNES 09h10", "503");
        moduloMap.put("VIERNES 10h15", "504");
        moduloMap.put("VIERNES 11h20", "505");
        moduloMap.put("VIERNES 12h25", "506");
        moduloMap.put("VIERNES 13h30", "507");
        moduloMap.put("VIERNES 14h35", "508");
        moduloMap.put("VIERNES 15h40", "509");
        moduloMap.put("VIERNES 16h45", "510");
        moduloMap.put("VIERNES 17h50", "511");
        moduloMap.put("VIERNES 18h50", "512");
        moduloMap.put("SÁBADO 07h00", "601");
        moduloMap.put("SÁBADO 08h05", "602");
        moduloMap.put("SÁBADO 09h10", "603");
        moduloMap.put("SÁBADO 10h15", "604");
        moduloMap.put("SÁBADO 11h20", "605");
        moduloMap.put("SÁBADO 12h25", "606");
        moduloMap.put("SÁBADO 13h30", "607");
        moduloMap.put("SÁBADO 14h35", "608");
        moduloMap.put("SÁBADO 15h40", "609");
        moduloMap.put("SÁBADO 16h45", "610");
        moduloMap.put("SÁBADO 17h50", "611");
        moduloMap.put("SÁBADO 18h50", "612");

        return moduloMap.getOrDefault(day + " " + hora, "");
    }

    private ByteArrayInputStream exportToExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reservas");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Fecha");
            headerRow.createCell(1).setCellValue("Módulo");
            headerRow.createCell(2).setCellValue("Hora/Inicio");
            headerRow.createCell(3).setCellValue("Laboratorio");
            headerRow.createCell(4).setCellValue("Docente");
            headerRow.createCell(5).setCellValue("Correo Docente");
            headerRow.createCell(6).setCellValue("Escuela");
            headerRow.createCell(7).setCellValue("Materia");
            headerRow.createCell(8).setCellValue("Caso");
            headerRow.createCell(9).setCellValue("Fecha Entrenamiento");
            headerRow.createCell(10).setCellValue("Hora Entrenamiento");
            headerRow.createCell(11).setCellValue("Docente del entrenamiento");
            headerRow.createCell(12).setCellValue("Forma del Entrenamiento");
            headerRow.createCell(13).setCellValue("Género");
            headerRow.createCell(14).setCellValue("Rango solicitado de Edad");
            headerRow.createCell(15).setCellValue("Paciente Asignado");
            headerRow.createCell(16).setCellValue("Correo Paciente");
            headerRow.createCell(17).setCellValue("Edad real del Paciente");
            headerRow.createCell(18).setCellValue("Moulage");
            headerRow.createCell(19).setCellValue("Tiempo de llegada al Moulage");
            headerRow.createCell(20).setCellValue("Detalle del moulage");
            headerRow.createCell(21).setCellValue("Categoria");
            headerRow.createCell(22).setCellValue("Actividad");
            headerRow.createCell(23).setCellValue("# Pacientes");
            headerRow.createCell(24).setCellValue("Link del Caso Clínico");
            headerRow.createCell(25).setCellValue("Observaciones Generales");

            List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
            int rowIdx = 1;
            for (Reserva reserva : reservas) {
                String modulo = getModulo(reserva.getFechaSeccion(), reserva.getHorasSeccion() != null && !reserva.getHorasSeccion().isEmpty() ? reserva.getHorasSeccion().get(0) : "");
                reserva.setModulo(modulo);

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(reserva.getFechaSeccion() != null ? reserva.getFechaSeccion().toString() : "");
                row.createCell(1).setCellValue(reserva.getModulo());
                row.createCell(2).setCellValue(reserva.getHorasSeccion() != null ? String.join(", ", reserva.getHorasSeccion()) : "");
                row.createCell(3).setCellValue(reserva.getAula());
                Usuario doctor = usuarioService.obtenerUsuarioPorCorreo(reserva.getCorreoDoctor()).orElse(null);
                row.createCell(4).setCellValue(doctor != null ? doctor.getNombre() + " " + doctor.getApellido() : reserva.getCorreoDoctor());
                row.createCell(5).setCellValue(reserva.getCorreoDoctor());
                row.createCell(6).setCellValue(reserva.getCarrera());
                row.createCell(7).setCellValue(reserva.getTipo());
                row.createCell(8).setCellValue(reserva.getCaso());
                row.createCell(9).setCellValue(reserva.getFechaEntrenamiento() != null ? reserva.getFechaEntrenamiento().toString() : "");
                row.createCell(10).setCellValue(reserva.getHorasEntrenamiento() != null ? String.join(", ", reserva.getHorasEntrenamiento()) : "");
                row.createCell(11).setCellValue(doctor != null ? doctor.getNombre() + " " + doctor.getApellido() : reserva.getCorreoDoctor());
                row.createCell(12).setCellValue(reserva.getFormaRequerimiento());
                row.createCell(13).setCellValue(reserva.getPacientes() != null ? reserva.getPacientes().stream().map(Paciente::getGenero).collect(Collectors.joining(", ")) : "");
                row.createCell(14).setCellValue(reserva.getPacientes() != null ? reserva.getPacientes().stream().map(Paciente::getRangoEdad).collect(Collectors.joining(", ")) : "");
                row.createCell(15).setCellValue(reserva.getActoresAsignados() != null ? reserva.getActoresAsignados().stream().map(Actor::getNombre).collect(Collectors.joining(", ")) : "");
                row.createCell(16).setCellValue(reserva.getActoresAsignados() != null ? reserva.getActoresAsignados().stream().map(Actor::getCorreo).collect(Collectors.joining(", ")) : "");
                row.createCell(17).setCellValue(reserva.getActoresAsignados() != null ? reserva.getActoresAsignados().stream().map(Actor::getEdad).map(String::valueOf).collect(Collectors.joining(", ")) : "");
                row.createCell(18).setCellValue(reserva.getPacientes() != null ? reserva.getPacientes().stream().anyMatch(Paciente::isRequiereMoulage) ? "Sí" : "No" : "");
                row.createCell(19).setCellValue(""); // Campo vacío para el tiempo de llegada al moulage
                row.createCell(20).setCellValue(reserva.getPacientes() != null ? reserva.getPacientes().stream().map(Paciente::getDetalleMoulage).filter(Objects::nonNull).collect(Collectors.joining(", ")) : "");
                row.createCell(21).setCellValue(reserva.getTipoReserva());
                row.createCell(22).setCellValue(reserva.getActividad());
                row.createCell(23).setCellValue(reserva.getNumeroPacientes() != null ? reserva.getNumeroPacientes().toString() : "");
                row.createCell(24).setCellValue("https://udlaec-my.sharepoint.com/personal/rocio_paredes_udla_edu_ec/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Frocio%5Fparedes%5Fudla%5Fedu%5Fec%2FDocuments%2FBANCO%20DE%20CASOS%20PPS%202022&ga=1");
                row.createCell(25).setCellValue(reserva.getObservacionesGenerales() != null ? reserva.getObservacionesGenerales() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            LOGGER.severe("Error al exportar las reservas a Excel: " + e.getMessage());
            return null;
        }
    }
}
