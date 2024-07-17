package com.pacientesimulado.application.views.disponibilidadpracticadoctor;

import com.pacientesimulado.application.data.Disponibilidad;
import com.pacientesimulado.application.data.Doctor;
import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.DoctorService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "disponibilidad-practica-doctor", layout = MainLayout.class)
@PageTitle("Disponibilidad Práctica Doctor")
public class DisponibilidadPracticaDoctorView extends VerticalLayout {

    private final DoctorService doctorService;
    private DatePicker datePicker;
    private VerticalLayout datesLayout;
    private Button saveButton;
    private Button viewDisponibilitiesButton;
    private Paragraph welcomeMessage = new Paragraph();

    @Autowired
    public DisponibilidadPracticaDoctorView(DoctorService doctorService) {
        this.doctorService = doctorService;

        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);

        add(new H2("Disponibilidad Práctica Doctor"));
        welcomeMessage.setText("Bienvenido, " + currentUser.getNombre() + " " + currentUser.getApellido() + ". Seleccione las fechas y horas de disponibilidad para los entrenamientos con el actor.");
        add(welcomeMessage);

        datePicker = new DatePicker("Seleccione una o varias fechas");
        datePicker.setWidth("500px");
        datePicker.setPlaceholder("Seleccione fechas");
        datePicker.setClearButtonVisible(true);
        datePicker.setEnabled(true);

        datesLayout = new VerticalLayout();
        datesLayout.setSpacing(true);
        datesLayout.setPadding(true);

        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                // Verificar si la fecha ya existe
                Usuario currentUser1 = VaadinSession.getCurrent().getAttribute(Usuario.class);
                Optional<Doctor> optionalDoctor = doctorService.obtenerDoctorPorCorreo(currentUser1.getCorreo());
                if (optionalDoctor.isPresent()) {
                    Doctor doctor = optionalDoctor.get();
                    if (doctor.getDisponibilidades() == null) {
                        doctor.setDisponibilidades(new ArrayList<>());
                    }
                    Optional<Disponibilidad> existingDisponibilidad = doctor.getDisponibilidades().stream()
                            .filter(d -> d.getFecha().equals(selectedDate))
                            .findFirst();

                    if (existingDisponibilidad.isPresent()) {
                        Notification.show("La fecha ya existe. Cargando disponibilidad existente.");
                        addDateWithHours(existingDisponibilidad.get());
                    } else {
                        addDateWithHours(new Disponibilidad(currentUser1.getId(), selectedDate, new ArrayList<>()));
                    }
                }
                datePicker.clear();
            }
        });

        saveButton = new Button("Guardar Disponibilidad", event -> guardarDisponibilidad());
        saveButton.setEnabled(true);

        viewDisponibilitiesButton = new Button("Ver Disponibilidades", event -> mostrarDisponibilidades());
        viewDisponibilitiesButton.setEnabled(true);

        add(datePicker, datesLayout, saveButton, viewDisponibilitiesButton);
    }

    private void addDateWithHours(Disponibilidad disponibilidad) {
        VerticalLayout dateLayout = new VerticalLayout();
        String formattedDate = disponibilidad.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                disponibilidad.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dateLayout.add(new H2(formattedDate));

        MultiSelectComboBox<String> hoursComboBox = new MultiSelectComboBox<>("Seleccione una o varias horas");
        hoursComboBox.setWidth("500px");
        hoursComboBox.setItems(
                "07h00", "08h05", "09h10", "10h15", "11h20",
                "12h25", "13h30", "14h35", "15h40", "16h45",
                "17h45", "18h50", "19h50"
        );

        // Establecer las horas seleccionadas si ya existen
        if (disponibilidad.getHoras() != null) {
            hoursComboBox.setValue(disponibilidad.getHoras().stream().map(Disponibilidad.HoraDisponibilidad::getHora).collect(Collectors.toSet()));
        }

        dateLayout.add(hoursComboBox);
        datesLayout.add(dateLayout);
    }

    private void guardarDisponibilidad() {
        if (datesLayout.getComponentCount() < 2) {
            Notification.show("Debe seleccionar al menos dos fechas.");
            return;
        }

        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);
        Optional<Doctor> optionalDoctor = doctorService.obtenerDoctorPorCorreo(currentUser.getCorreo());

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            List<Disponibilidad> nuevasDisponibilidades = new ArrayList<>();
            for (int i = 0; i < datesLayout.getComponentCount(); i++) {
                VerticalLayout dateLayout = (VerticalLayout) datesLayout.getComponentAt(i);
                String dateText = ((H2) dateLayout.getComponentAt(0)).getText();
                MultiSelectComboBox<String> hoursComboBox = (MultiSelectComboBox<String>) dateLayout.getComponentAt(1);

                LocalDate date = LocalDate.parse(dateText.split(" ")[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Disponibilidad disponibilidad = new Disponibilidad(currentUser.getId(), date, new ArrayList<>());

                List<Disponibilidad.HoraDisponibilidad> horas = new ArrayList<>();
                for (String hour : hoursComboBox.getSelectedItems()) {
                    Disponibilidad.HoraDisponibilidad horaDisponibilidad = new Disponibilidad.HoraDisponibilidad();
                    horaDisponibilidad.setHora(hour);
                    horaDisponibilidad.setEstado("libre");
                    horas.add(horaDisponibilidad);
                }
                disponibilidad.setHoras(horas);
                nuevasDisponibilidades.add(disponibilidad);
            }

            // Agregar nuevas disponibilidades a las existentes
            if (doctor.getDisponibilidades() == null) {
                doctor.setDisponibilidades(new ArrayList<>());
            }

            doctor.getDisponibilidades().addAll(nuevasDisponibilidades);
            doctorService.guardarDoctor(doctor);
            Notification.show("Disponibilidad guardada");
        } else {
            Notification.show("Doctor no encontrado");
        }
        datesLayout.removeAll();
    }

    private void mostrarDisponibilidades() {
        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);
        Optional<Doctor> optionalDoctor = doctorService.obtenerDoctorPorCorreo(currentUser.getCorreo());

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            Dialog dialog = new Dialog();
            dialog.setWidth("700px");
            dialog.setHeight("400px");

            Grid<Disponibilidad> disponibilidadGrid = new Grid<>(Disponibilidad.class, false);
            disponibilidadGrid.addColumn(d -> d.getFecha().format(DateTimeFormatter.ofPattern("EEEE dd-MM-yyyy", new Locale("es")))).setHeader("Fecha");
            disponibilidadGrid.addColumn(d -> d.getHoras().stream()
                            .map(h -> h.getHora() + " (" + h.getEstado() + ")")
                            .collect(Collectors.joining(", ")))
                    .setHeader("Horas");
            disponibilidadGrid.addComponentColumn(disponibilidad -> {
                Button editButton = new Button("Editar", event -> {
                    datePicker.setValue(disponibilidad.getFecha());
                    addDateWithHours(disponibilidad);
                    dialog.close();
                });
                Button deleteButton = new Button("Eliminar", event -> {
                    doctor.getDisponibilidades().remove(disponibilidad);
                    doctorService.guardarDoctor(doctor);
                    disponibilidadGrid.setItems(doctor.getDisponibilidades());
                });
                HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
                return actions;
            }).setHeader("Acciones");

            disponibilidadGrid.setItems(doctor.getDisponibilidades());
            dialog.add(disponibilidadGrid);
            dialog.open();
        } else {
            Notification.show("Doctor no encontrado");
        }
    }
}
