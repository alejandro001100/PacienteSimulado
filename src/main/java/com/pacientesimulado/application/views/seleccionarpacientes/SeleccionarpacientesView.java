package com.pacientesimulado.application.views.seleccionarpacientes;

import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Seleccionar pacientes")
@Route(value = "my-view4", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class SeleccionarpacientesView extends Composite<VerticalLayout> {

    public SeleccionarpacientesView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        H6 h6 = new H6();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        ComboBox comboBox = new ComboBox();
        ComboBox comboBox2 = new ComboBox();
        Hr hr = new Hr();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        ComboBox comboBox3 = new ComboBox();
        ComboBox comboBox4 = new ComboBox();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        ComboBox comboBox5 = new ComboBox();
        TextField textField = new TextField();
        Hr hr2 = new Hr();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonPrimary2 = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        h6.setText("Es necesario que indique el número, genero, edad del paciente que desea");
        h6.setWidth("max-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        comboBox.setLabel("Seleccione para que actividad necesita");
        comboBox.setWidth("270px");
        setComboBoxSampleData(comboBox);
        comboBox2.setLabel("Número de pacientes");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        comboBox3.setLabel("Género del paciente");
        comboBox3.setWidth("min-content");
        setComboBoxSampleData(comboBox3);
        comboBox4.setLabel("Rango de edad del paciente simulado");
        comboBox4.setWidth("260px");
        setComboBoxSampleData(comboBox4);
        layoutColumn3.setHeightFull();
        layoutRow2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        comboBox5.setLabel("Su caso requiere moulage?");
        comboBox5.setWidth("min-content");
        setComboBoxSampleData(comboBox5);
        textField.setLabel("Detalle el moulage");
        textField.setWidth("min-content");
        layoutRow3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        layoutRow3.setAlignItems(Alignment.START);
        layoutRow3.setJustifyContentMode(JustifyContentMode.END);
        buttonPrimary.setText("Regresar");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary2.setText("Siguiente");
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutRow);
        layoutRow.add(h6);
        getContent().add(layoutColumn2);
        layoutColumn2.add(comboBox);
        layoutColumn2.add(comboBox2);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(comboBox3);
        layoutRow2.add(comboBox4);
        layoutRow2.add(layoutColumn3);
        layoutColumn3.add(comboBox5);
        layoutColumn3.add(textField);
        layoutColumn2.add(hr2);
        layoutColumn2.add(layoutRow3);
        layoutRow3.add(buttonPrimary);
        layoutRow3.add(buttonPrimary2);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }
}
