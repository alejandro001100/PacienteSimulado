package com.pacientesimulado.application.views.disponibilidadsemanadeclases;

import com.pacientesimulado.application.data.Disponibilidad;
import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.ActorService;
import com.pacientesimulado.application.services.DisponibilidadService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

@Route(value = "disponibilidad-semana-de-clases", layout = MainLayout.class)
@PageTitle("Disponibilidad Semana de Clases")
public class DisponibilidadSemanaDeClasesView extends VerticalLayout {

    private final DisponibilidadService disponibilidadService;
    private final ActorService actorService;
    private DatePicker datePicker;
    private VerticalLayout datesLayout;
    private Button saveButton;
    private Paragraph welcomeMessage = new Paragraph();
    private List<Disponibilidad> existingDisponibilidades;

    @Autowired
    public DisponibilidadSemanaDeClasesView(DisponibilidadService disponibilidadService, ActorService actorService) {
        this.disponibilidadService = disponibilidadService;
        this.actorService = actorService;

        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);

        add(new H2("Disponibilidad Semana de Clases"));
        welcomeMessage.setText("Bienvenido, " + currentUser.getNombre() + " " + currentUser.getApellido() + ". Seleccione las fechas y horas de disponibilidad.");
        add(welcomeMessage);

        datePicker = new DatePicker("Seleccione una o varias fechas");
        datePicker.setWidth("400px");
        datePicker.setPlaceholder("Seleccione fechas");
        datePicker.setClearButtonVisible(true);
        datePicker.setEnabled(true);

        datesLayout = new VerticalLayout();
        datesLayout.setSpacing(true);
        datesLayout.setPadding(true);

        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                if (isDateAlreadySelected(selectedDate)) {
                    Notification.show("Fecha ya seleccionada. Edite la fecha existente.");
                    loadExistingDateWithHours(selectedDate);
                } else {
                    addDateWithHours(selectedDate);
                }
                datePicker.clear();
            }
        });

        saveButton = new Button("Guardar Disponibilidad", event -> guardarDisponibilidad());
        saveButton.setEnabled(true);

        add(datePicker, datesLayout, saveButton);

        Button viewDisponibilidadesButton = new Button("Ver Disponibilidades", event -> showDisponibilidadesDialog());
        add(viewDisponibilidadesButton);

        loadExistingDisponibilidades(currentUser);
    }

    private boolean isDateAlreadySelected(LocalDate date) {
        return existingDisponibilidades.stream().anyMatch(d -> d.getFecha().equals(date));
    }

    private void loadExistingDisponibilidades(Usuario currentUser) {
        actorService.obtenerActorPorCorreo(currentUser.getCorreo()).ifPresent(actor -> {
            existingDisponibilidades = actor.getDisponibilidades();
        });
    }

    private void addDateWithHours(LocalDate date) {
        VerticalLayout dateLayout = new VerticalLayout();
        String formattedDate = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dateLayout.add(new H2(formattedDate));

        MultiSelectComboBox<String> hoursComboBox = new MultiSelectComboBox<>("Seleccione una o varias horas");
        hoursComboBox.setWidth("400px");
        hoursComboBox.setItems(
                "07h00", "08h05", "09h10", "10h15", "11h20",
                "12h25", "13h30", "14h35", "15h40", "16h45",
                "17h45", "18h50", "19h50"
        );

        Button selectAllButton = new Button("Seleccionar todas las horas", event -> {
            hoursComboBox.select("07h00", "08h05", "09h10", "10h15", "11h20",
                    "12h25", "13h30", "14h35", "15h40", "16h45",
                    "17h45", "18h50", "19h50");
        });

        Button deleteButton = new Button("Eliminar Fecha", event -> datesLayout.remove(dateLayout));
        deleteButton.getStyle().set("color", "red");

        dateLayout.add(hoursComboBox, selectAllButton, deleteButton);
        datesLayout.add(dateLayout);
    }

    private void loadExistingDateWithHours(LocalDate date) {
        existingDisponibilidades.stream()
                .filter(d -> d.getFecha().equals(date))
                .findFirst()
                .ifPresent(existingDisponibilidad -> {
                    VerticalLayout dateLayout = new VerticalLayout();
                    String formattedDate = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                            date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    dateLayout.add(new H2(formattedDate));

                    MultiSelectComboBox<String> hoursComboBox = new MultiSelectComboBox<>("Seleccione una o varias horas");
                    hoursComboBox.setWidth("400px");
                    hoursComboBox.setItems(
                            "07h00", "08h05", "09h10", "10h15", "11h20",
                            "12h25", "13h30", "14h35", "15h40", "16h45",
                            "17h45", "18h50", "19h50"
                    );

                    List<String> selectedHours = new ArrayList<>();
                    for (Disponibilidad.HoraDisponibilidad hora : existingDisponibilidad.getHoras()) {
                        selectedHours.add(hora.getHora());
                    }
                    hoursComboBox.setValue(selectedHours);

                    Button selectAllButton = new Button("Seleccionar todas las horas", event -> {
                        hoursComboBox.select("07h00", "08h05", "09h10", "10h15", "11h20",
                                "12h25", "13h30", "14h35", "15h40", "16h45",
                                "17h45", "18h50", "19h50");
                    });

                    Button deleteButton = new Button("Eliminar Fecha", event -> datesLayout.remove(dateLayout));
                    deleteButton.getStyle().set("color", "red");

                    dateLayout.add(hoursComboBox, selectAllButton, deleteButton);
                    datesLayout.add(dateLayout);
                });
    }

    private void guardarDisponibilidad() {
        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);
        actorService.obtenerActorPorCorreo(currentUser.getCorreo()).ifPresent(actor -> {
            List<Disponibilidad> nuevasDisponibilidades = new ArrayList<>();
            for (int i = 0; i < datesLayout.getComponentCount(); i++) {
                VerticalLayout dateLayout = (VerticalLayout) datesLayout.getComponentAt(i);
                String dateText = ((H2) dateLayout.getComponentAt(0)).getText();
                MultiSelectComboBox<String> hoursComboBox = (MultiSelectComboBox<String>) dateLayout.getComponentAt(1);

                LocalDate date = LocalDate.parse(dateText.split(" ")[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Disponibilidad disponibilidad = new Disponibilidad();
                disponibilidad.setFecha(date);

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

            if (nuevasDisponibilidades.size() < 2) {
                Notification.show("Debe seleccionar al menos dos fechas.");
                return;
            }

            List<Disponibilidad> disponibilidadesExistentes = actor.getDisponibilidades();
            List<Disponibilidad> disponibilidadesCombinadas = new ArrayList<>(disponibilidadesExistentes);

            for (Disponibilidad nueva : nuevasDisponibilidades) {
                Optional<Disponibilidad> existente = disponibilidadesExistentes.stream()
                        .filter(d -> d.getFecha().equals(nueva.getFecha()))
                        .findFirst();

                if (existente.isPresent()) {
                    List<Disponibilidad.HoraDisponibilidad> horasCombinadas = new ArrayList<>(existente.get().getHoras());
                    horasCombinadas.addAll(nueva.getHoras());
                    existente.get().setHoras(horasCombinadas);
                } else {
                    disponibilidadesCombinadas.add(nueva);
                }
            }

            actor.setDisponibilidades(disponibilidadesCombinadas);
            actorService.guardarActor(actor);
            Notification.show("Disponibilidad guardada");
        });
        datesLayout.removeAll();
    }

    private void showDisponibilidadesDialog() {
        Dialog disponibilidadesDialog = new Dialog();
        disponibilidadesDialog.setWidth("80%");
        disponibilidadesDialog.setHeight("80%");

        Grid<Disponibilidad> grid = new Grid<>(Disponibilidad.class, false);
        grid.addColumn(d -> d.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                d.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).setHeader("Fecha");
        grid.addColumn(d -> {
            StringBuilder horas = new StringBuilder();
            for (Disponibilidad.HoraDisponibilidad hora : d.getHoras()) {
                if (horas.length() > 0) horas.append(", ");
                horas.append(hora.getHora()).append(" (").append(hora.getEstado()).append(")");
            }
            return horas.toString();
        }).setHeader("Horas");

        grid.addComponentColumn(disponibilidad -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button editButton = new Button("Editar", event -> {
                disponibilidadesDialog.close();
                showEditDialog(disponibilidad);
            });
            editButton.getStyle().set("color", "green");

            Button deleteButton = new Button("Eliminar", event -> {
                deleteDisponibilidad(disponibilidad);
                disponibilidadesDialog.close();
            });
            deleteButton.getStyle().set("color", "red");

            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Acciones");

        grid.setItems(existingDisponibilidades);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        disponibilidadesDialog.add(grid);
        disponibilidadesDialog.open();
    }

    private void showEditDialog(Disponibilidad disponibilidad) {
        Dialog editDialog = new Dialog();
        editDialog.setWidth("50%");
        editDialog.setHeight("50%");

        VerticalLayout editLayout = new VerticalLayout();
        String formattedDate = disponibilidad.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                disponibilidad.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        editLayout.add(new H2(formattedDate));

        MultiSelectComboBox<String> hoursComboBox = new MultiSelectComboBox<>("Seleccione una o varias horas");
        hoursComboBox.setWidth("400px");
        hoursComboBox.setItems(
                "07h00", "08h05", "09h10", "10h15", "11h20",
                "12h25", "13h30", "14h35", "15h40", "16h45",
                "17h45", "18h50", "19h50"
        );

        List<String> selectedHours = new ArrayList<>();
        for (Disponibilidad.HoraDisponibilidad hora : disponibilidad.getHoras()) {
            selectedHours.add(hora.getHora());
        }
        hoursComboBox.setValue(selectedHours);

        Button saveButton = new Button("Guardar", event -> {
            List<Disponibilidad.HoraDisponibilidad> nuevasHoras = new ArrayList<>();
            for (String hour : hoursComboBox.getSelectedItems()) {
                Disponibilidad.HoraDisponibilidad horaDisponibilidad = new Disponibilidad.HoraDisponibilidad();
                horaDisponibilidad.setHora(hour);
                horaDisponibilidad.setEstado("libre");
                nuevasHoras.add(horaDisponibilidad);
            }
            disponibilidad.setHoras(nuevasHoras);
            actorService.guardarActor(VaadinSession.getCurrent().getAttribute(com.pacientesimulado.application.data.Actor.class));
            Notification.show("Disponibilidad actualizada");
            editDialog.close();
            showDisponibilidadesDialog(); // Refrescar el diálogo de disponibilidades
        });

        editLayout.add(hoursComboBox, saveButton);
        editDialog.add(editLayout);
        editDialog.open();
    }

    private void deleteDisponibilidad(Disponibilidad disponibilidad) {
        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);
        actorService.obtenerActorPorCorreo(currentUser.getCorreo()).ifPresent(actor -> {
            List<Disponibilidad> disponibilidadesExistentes = actor.getDisponibilidades();
            disponibilidadesExistentes.removeIf(d -> d.getFecha().equals(disponibilidad.getFecha()));
            actor.setDisponibilidades(disponibilidadesExistentes);
            actorService.guardarActor(actor);
            Notification.show("Disponibilidad eliminada");
            showDisponibilidadesDialog(); // Refrescar el diálogo de disponibilidades
        });
    }
}
