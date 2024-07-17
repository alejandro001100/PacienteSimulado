package com.pacientesimulado.application.views.reservaprogramapacientesimuladoactor;

import com.pacientesimulado.application.data.Materia;
import com.pacientesimulado.application.data.Paciente;
import com.pacientesimulado.application.data.Reserva;
import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.MateriaService;
import com.pacientesimulado.application.services.ReservaService;
import com.pacientesimulado.application.services.UsuarioService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Reserva Programa Paciente Simulado (ACTOR)")
@Route(value = "reserva-programa-paciente-simulado-actor", layout = MainLayout.class)
public class ReservaProgramaPacienteSimuladoACTORView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private final MateriaService materiaService;
    private final ReservaService reservaService;
    private VerticalLayout mainLayout;
    private Map<String, Map<String, Checkbox>> disponibilidadMap;
    private ComboBox<String> comboBoxActividad;
    private ComboBox<Integer> comboBoxNumeroPacientes;
    private ComboBox<String> comboBoxRequerimiento;
    private VerticalLayout datesLayoutSimulado;
    private Map<LocalDate, List<String>> disponibilidadSimulado = new HashMap<>();
    private Paragraph welcomeMessage = new Paragraph();
    private LocalDate fechaSeccion;
    private List<String> horasSeccion = new ArrayList<>();

    @Autowired
    public ReservaProgramaPacienteSimuladoACTORView(UsuarioService usuarioService, MateriaService materiaService, ReservaService reservaService) {
        this.usuarioService = usuarioService;
        this.materiaService = materiaService;
        this.reservaService = reservaService;

        disponibilidadMap = new HashMap<>();

        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);

        getContent().add(new H2("Reserva Programa Paciente Simulado (ACTOR)"));
        welcomeMessage.setText("Bienvenido, " + currentUser.getNombre() + " " + currentUser.getApellido() + ". Crea tu solicitud.");
        getContent().add(welcomeMessage);

        mainLayout = new VerticalLayout();
        getContent().add(mainLayout);

        showForm(currentUser.getCorreo());
    }

    private void showForm(String correoDoctor) {
        mainLayout.removeAll();

        // Conozca su caso
        Paragraph textSmall = new Paragraph("Si necesita consultar el caso que requiere antes de llenar el formulario por favor de click en el Consultar Casos.");
        textSmall.setWidth("100%");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        //abrir en una nueva pestaña
        Anchor link = new Anchor("https://udlaec-my.sharepoint.com/:f:/g/personal/rocio_paredes_udla_edu_ec/EojckZ4-1wdIhAYnTu6HYf4B-BJwp_aYFdc_p58HG11-Qw?e=efvbdR", "Consultar casos");
        link.setWidth("100%");
        link.setTarget("_blank");
        mainLayout.add(textSmall, link);

        // Selección de Carrera, Tipo y Caso
        ComboBox<String> carreraComboBox = new ComboBox<>("Seleccione la carrera");
        ComboBox<String> tipoComboBox = new ComboBox<>("Seleccione el tipo");
        ComboBox<String> casoComboBox = new ComboBox<>("Seleccione el caso");
        carreraComboBox.setItems(materiaService.obtenerTodasLasCarreras());
        mainLayout.add(carreraComboBox);

        carreraComboBox.addValueChangeListener(event -> {
            String carreraSeleccionada = event.getValue();
            if (carreraSeleccionada != null) {
                tipoComboBox.clear();
                casoComboBox.clear();
                List<Materia> materias = materiaService.obtenerMateriasPorCarrera(carreraSeleccionada);
                List<String> tipos = materias.stream()
                        .flatMap(m -> m.getTiposYCasos().keySet().stream())
                        .distinct()
                        .collect(Collectors.toList());
                tipoComboBox.setItems(tipos);
            }
        });

        tipoComboBox.addValueChangeListener(event -> {
            String tipoSeleccionado = event.getValue();
            if (tipoSeleccionado != null) {
                String carreraSeleccionada = carreraComboBox.getValue();
                List<Materia> materias = materiaService.obtenerMateriasPorCarrera(carreraSeleccionada);
                List<String> casos = materias.stream()
                        .filter(m -> m.getTiposYCasos().containsKey(tipoSeleccionado))
                        .flatMap(m -> m.getTiposYCasos().get(tipoSeleccionado).stream())
                        .distinct()
                        .collect(Collectors.toList());
                casoComboBox.setItems(casos);
            }
        });

        mainLayout.add(tipoComboBox, casoComboBox);

        // Fecha de la Sección de Paciente Simulado
        datesLayoutSimulado = new VerticalLayout();
        datesLayoutSimulado.setSpacing(true);
        datesLayoutSimulado.setPadding(true);

        DatePicker datePickerSimulado = new DatePicker("Seleccione fechas para la sesión de paciente simulado");
        datePickerSimulado.setPlaceholder("Seleccione una o varias fechas");
        datePickerSimulado.setWidth("500px");
        datePickerSimulado.setClearButtonVisible(true);
        datePickerSimulado.setEnabled(true);
        datePickerSimulado.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                fechaSeccion = selectedDate;  // Asignamos la fecha seleccionada a fechaSeccion
                addDateWithHours(selectedDate, datesLayoutSimulado, disponibilidadSimulado);
                datePickerSimulado.clear();
            }
        });

        mainLayout.add(datePickerSimulado, datesLayoutSimulado);

        // Actividad y Número de Pacientes
        comboBoxActividad = new ComboBox<>("Seleccione para que actividad necesita");
        comboBoxActividad.setItems("Clase", "Evaluación", "Examen complexivo", "Capacitación", "Talleres");
        comboBoxActividad.setWidth("270px");

        comboBoxNumeroPacientes = new ComboBox<>("Número de pacientes");
        comboBoxNumeroPacientes.setItems(1, 2, 3, 4, 5, 6);
        comboBoxNumeroPacientes.setWidth("min-content");

        Button siguienteButton = new Button("Siguiente");
        siguienteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        siguienteButton.addClickListener(event -> {
            Integer numberOfPatients = comboBoxNumeroPacientes.getValue();
            if (numberOfPatients != null) {
                updatePatientFields(numberOfPatients, correoDoctor, carreraComboBox.getValue(), tipoComboBox.getValue(), casoComboBox.getValue());
            } else {
                Notification.show("Por favor, seleccione el número de pacientes.");
            }
        });

        mainLayout.add(comboBoxActividad, comboBoxNumeroPacientes, siguienteButton);
    }

    private void addDateWithHours(LocalDate date, VerticalLayout datesLayout, Map<LocalDate, List<String>> disponibilidadMap) {
        VerticalLayout dateLayout = new VerticalLayout();
        String formattedDate = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")) + " " +
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dateLayout.add(new H2(formattedDate));

        MultiSelectComboBox<String> hoursComboBox = new MultiSelectComboBox<>("Seleccione una o varias horas");
        hoursComboBox.setItems(
                "07h00", "08h05", "09h10", "10h15", "11h20",
                "12h25", "13h30", "14h35", "15h40", "16h45",
                "17h45", "18h50", "19h50"
        );
        hoursComboBox.addValueChangeListener(event -> {
            List<String> hours = new ArrayList<>(hoursComboBox.getSelectedItems());
            disponibilidadMap.put(date, hours);
            if (datesLayout == datesLayoutSimulado) {
                horasSeccion = hours;  // Asignamos las horas seleccionadas a horasSeccion
            }
        });

        dateLayout.add(hoursComboBox);
        datesLayout.add(dateLayout);
    }

    private void updatePatientFields(int numberOfPatients, String correoDoctor, String carrera, String tipo, String caso) {
        mainLayout.removeAll();
        mainLayout.add(comboBoxActividad, comboBoxNumeroPacientes);

        List<Paciente> pacientes = new ArrayList<>();
        List<Map<String, Object>> patientDataList = new ArrayList<>();

        for (int i = 1; i <= numberOfPatients; i++) {
            VerticalLayout patientLayout = new VerticalLayout();
            patientLayout.add(new com.vaadin.flow.component.html.Hr());
            patientLayout.add(new com.vaadin.flow.component.html.H6("Paciente " + i));

            ComboBox<String> comboBoxGenero = new ComboBox<>("Género del paciente");
            comboBoxGenero.setItems("Femenino", "Masculino", "No relevante");
            comboBoxGenero.setWidth("min-content");

            ComboBox<String> comboBoxEdad = new ComboBox<>("Rango de edad del paciente simulado");
            comboBoxEdad.setItems("Joven (18-29 años)", "Adulto (30-39 años)", "Adulto medio (40-49 años)", "Adulto mayor (50 años en adelante)", "No relevante");
            comboBoxEdad.setWidth("260px");

            ComboBox<String> comboBoxMoulage = new ComboBox<>("¿Requiere moulage?");
            comboBoxMoulage.setItems("Si", "No");
            comboBoxMoulage.setWidth("min-content");

            com.vaadin.flow.component.textfield.TextField textFieldMoulage = new com.vaadin.flow.component.textfield.TextField("Especifique el moulage");
            textFieldMoulage.setWidth("min-content");
            textFieldMoulage.setVisible(false);

            comboBoxMoulage.addValueChangeListener(event -> {
                String selectedMoulage = event.getValue();
                textFieldMoulage.setVisible("Si".equals(selectedMoulage));
            });

            patientLayout.add(comboBoxGenero, comboBoxEdad, comboBoxMoulage, textFieldMoulage);
            mainLayout.add(patientLayout);

            Map<String, Object> patientData = new HashMap<>();
            patientData.put("genero", comboBoxGenero);
            patientData.put("edad", comboBoxEdad);
            patientData.put("moulage", comboBoxMoulage);
            patientData.put("detalleMoulage", textFieldMoulage);
            patientDataList.add(patientData);
        }

        comboBoxRequerimiento = new ComboBox<>("Forma de requerimiento de su Paciente Para la práctica");
        comboBoxRequerimiento.setItems("Presencial", "Virtual");
        comboBoxRequerimiento.setWidth("500px");

        Button guardarButton = new Button("Reservar para su práctica");
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarButton.addClickListener(event -> guardarReserva(pacientes, correoDoctor, carrera, tipo, caso, patientDataList));
        Button retrocederButton = new Button("Retroceder");
        retrocederButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        retrocederButton.addClickListener(event -> showForm(correoDoctor));

        mainLayout.add(comboBoxRequerimiento, new com.vaadin.flow.component.html.Hr(), guardarButton, retrocederButton);
    }

    private void guardarReserva(List<Paciente> pacientes, String correoDoctor, String carrera, String tipo, String caso, List<Map<String, Object>> patientDataList) {
        for (Map<String, Object> patientData : patientDataList) {
            Paciente paciente = new Paciente();
            paciente.setGenero(((ComboBox<String>) patientData.get("genero")).getValue());
            paciente.setRangoEdad(((ComboBox<String>) patientData.get("edad")).getValue());
            paciente.setRequiereMoulage("Si".equals(((ComboBox<String>) patientData.get("moulage")).getValue()));
            paciente.setDetalleMoulage(((com.vaadin.flow.component.textfield.TextField) patientData.get("detalleMoulage")).getValue());

            pacientes.add(paciente);
        }

        Reserva reserva = new Reserva(correoDoctor, carrera, tipo, caso);
        reserva.setActividad(comboBoxActividad.getValue());
        reserva.setNumeroPacientes(comboBoxNumeroPacientes.getValue());
        reserva.setFormaRequerimiento(comboBoxRequerimiento.getValue());
        reserva.setFechaSeccion(fechaSeccion);
        reserva.setHorasSeccion(horasSeccion);
        reserva.setPacientes(pacientes);
        reserva.setEstado("pendiente");

        reservaService.guardarReserva(reserva);
        Notification.show("Reserva guardada exitosamente.");

        mainLayout.removeAll();
        showForm(correoDoctor);
    }
}

