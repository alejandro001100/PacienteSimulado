package com.pacientesimulado.application.views.gestionmaterias;

import com.pacientesimulado.application.data.Materia;
import com.pacientesimulado.application.services.MateriaService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "gestion-materias", layout = MainLayout.class)
@PageTitle("Gestión de Materias")
public class GestionMateriasView extends VerticalLayout {

    private final MateriaService materiaService;
    private final Grid<Materia> grid = new Grid<>(Materia.class);
    private final Binder<Materia> binder = new Binder<>(Materia.class);

    @Autowired
    public GestionMateriasView(MateriaService materiaService) {
        this.materiaService = materiaService;

        addClassName("gestion-materias-view");
        setSizeFull();
        configureGrid();

        Button addButton = new Button("Añadir Materia", e -> showFormDialog(new Materia()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(addButton, grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("materia-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addComponentColumn(materia -> {
            Button editButton = new Button("Editar");
            editButton.addClickListener(e -> showFormDialog(materia));
            return editButton;
        }).setHeader("Acciones").setAutoWidth(true);

        grid.addColumn(Materia::getCarrera).setHeader("Carrera").setAutoWidth(true);
        grid.addColumn(materia -> String.join(", ", materia.getTiposYCasos().keySet()))
                .setHeader("Tipos").setAutoWidth(true);
        grid.addColumn(materia ->
                        materia.getTiposYCasos().entrySet().stream()
                                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                                .collect(Collectors.joining(", ")))
                .setHeader("Casos").setAutoWidth(true);

        // Limitar la expansión horizontal
        grid.getColumns().forEach(col -> col.setFlexGrow(0));
    }

    private void updateList() {
        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        grid.setItems(materias);
    }

    private void showFormDialog(Materia materia) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        FormLayout formLayout = new FormLayout();
        TextField carrera = new TextField("Carrera");
        ComboBox<String> tipoComboBox = new ComboBox<>("Tipo");
        TextArea casosTextArea = new TextArea("Casos");
        TextField nuevoTipo = new TextField("Nuevo Tipo");
        Button agregarTipoButton = new Button("Agregar Tipo");

        // Obtener tipos solo de la materia seleccionada
        List<String> tiposDeMateria = new ArrayList<>(materia.getTiposYCasos().keySet());
        tipoComboBox.setItems(tiposDeMateria);
        tipoComboBox.setPlaceholder("Seleccione un tipo");
        tipoComboBox.setWidthFull();
        casosTextArea.setWidthFull();
        casosTextArea.setPlaceholder("Ingrese los casos separados por comas");

        tipoComboBox.addValueChangeListener(event -> {
            String tipoSeleccionado = event.getValue();
            if (tipoSeleccionado != null && !tipoSeleccionado.isEmpty()) {
                String casos = String.join(", ", materia.getTiposYCasos().getOrDefault(tipoSeleccionado, Collections.emptyList()));
                casosTextArea.setValue(casos);
            }
        });

        agregarTipoButton.addClickListener(event -> {
            String tipo = nuevoTipo.getValue();
            if (tipo != null && !tipo.isEmpty() && !tiposDeMateria.contains(tipo)) {
                tiposDeMateria.add(tipo);
                tipoComboBox.setItems(tiposDeMateria); // Recargar elementos
                tipoComboBox.setValue(tipo);
                casosTextArea.clear();
                nuevoTipo.clear();
            } else {
                Notification.show("El tipo ya existe o es inválido");
            }
        });

        formLayout.add(carrera, tipoComboBox, nuevoTipo, agregarTipoButton, casosTextArea);
        dialog.add(formLayout);

        binder.forField(carrera).bind(Materia::getCarrera, Materia::setCarrera);
        binder.readBean(materia);

        Button saveButton = new Button("Guardar", e -> {
            if (binder.writeBeanIfValid(materia)) {
                String tipoSeleccionado = tipoComboBox.getValue();
                if (tipoSeleccionado != null && !tipoSeleccionado.isEmpty()) {
                    List<String> casos = Arrays.stream(casosTextArea.getValue().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                    materia.getTiposYCasos().put(tipoSeleccionado, casos);
                }
                materiaService.guardarMateria(materia);
                updateList();
                dialog.close();
            }
        });

        Button cancelButton = new Button("Cancelar", e -> dialog.close());

        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private List<String> getAllTipos() {
        Set<String> allTipos = new HashSet<>();
        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        for (Materia materia : materias) {
            allTipos.addAll(materia.getTiposYCasos().keySet());
        }
        return new ArrayList<>(allTipos);
    }
}
