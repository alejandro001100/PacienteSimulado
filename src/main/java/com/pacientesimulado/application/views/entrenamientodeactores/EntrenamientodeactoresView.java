package com.pacientesimulado.application.views.entrenamientodeactores;

import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Entrenamiento de actores")
@Route(value = "entrenamiento-de-actores", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EntrenamientodeactoresView extends Composite<VerticalLayout> {

    public EntrenamientodeactoresView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        MultiSelectComboBox multiSelectComboBox = new MultiSelectComboBox();
        DatePicker datePicker = new DatePicker();
        MultiSelectComboBox multiSelectComboBox2 = new MultiSelectComboBox();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonPrimary2 = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        multiSelectComboBox.setLabel("Modalidad");
        multiSelectComboBox.setWidth("288px");
        setMultiSelectComboBoxSampleData(multiSelectComboBox);
        datePicker.setLabel("Seleccione la fecha para el entrenamiento");
        datePicker.setWidth("288px");
        multiSelectComboBox2.setLabel("Seleccione los modulos");
        multiSelectComboBox2.setWidth("288px");
        setMultiSelectComboBoxSampleData(multiSelectComboBox2);
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setAlignItems(Alignment.START);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);
        buttonPrimary.setText("Regresar");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary2.setText("Agendar");
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutColumn2);
        layoutColumn2.add(multiSelectComboBox);
        layoutColumn2.add(datePicker);
        layoutColumn2.add(multiSelectComboBox2);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonPrimary2);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setMultiSelectComboBoxSampleData(MultiSelectComboBox multiSelectComboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        multiSelectComboBox.setItems(sampleItems);
        multiSelectComboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }
}
