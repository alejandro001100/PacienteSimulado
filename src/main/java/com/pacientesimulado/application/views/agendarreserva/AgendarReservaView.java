package com.pacientesimulado.application.views.agendarreserva;

import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H6;
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

@PageTitle("Agendar Reserva")
@Route(value = "my-view3", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AgendarReservaView extends Composite<VerticalLayout> {

    public AgendarReservaView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        H6 h6 = new H6();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        DatePicker datePicker = new DatePicker();
        MultiSelectComboBox multiSelectComboBox = new MultiSelectComboBox();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonPrimary2 = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        h6.setText(
                "Semana de la reserva. Por favor seleccione la semana en la que requiere utilizar a nuestro actor. Se debe seleccionar el inicio de la semana en que se requiere su clase.");
        h6.setWidth("max-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        datePicker.setLabel("Seleccione la fecha para la practica");
        datePicker.setWidth("250px");
        multiSelectComboBox.setLabel("Seleccione los modulos");
        multiSelectComboBox.setWidth("min-content");
        setMultiSelectComboBoxSampleData(multiSelectComboBox);
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutRow2.setAlignItems(Alignment.START);
        layoutRow2.setJustifyContentMode(JustifyContentMode.END);
        buttonPrimary.setText("Regresar");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary2.setText("Siguiente");
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutRow);
        layoutRow.add(h6);
        getContent().add(layoutColumn2);
        layoutColumn2.add(datePicker);
        layoutColumn2.add(multiSelectComboBox);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(buttonPrimary);
        layoutRow2.add(buttonPrimary2);
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
