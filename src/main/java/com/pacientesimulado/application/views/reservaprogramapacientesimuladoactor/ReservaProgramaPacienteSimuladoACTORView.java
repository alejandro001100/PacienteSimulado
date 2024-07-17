package com.pacientesimulado.application.views.reservaprogramapacientesimuladoactor;

import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Reserva Programa Paciente Simulado (ACTOR)")
@Route(value = "my-view2", layout = MainLayout.class)
@AnonymousAllowed
public class ReservaProgramaPacienteSimuladoACTORView extends Composite<VerticalLayout> {

    public ReservaProgramaPacienteSimuladoACTORView() {
        Paragraph textSmall = new Paragraph();
        Anchor link = new Anchor();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        RadioButtonGroup radioGroup = new RadioButtonGroup();
        ComboBox comboBox = new ComboBox();
        Button buttonPrimary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        textSmall.setText(
                "Si necesita consultar el caso que requiere antes de llenar el formulario por favor de click en el siguiente Link para que pueda revisarlo.");
        textSmall.setWidth("100%");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        link.setText(
                "https://udlaec-my.sharepoint.com/:f:/g/personal/rocio_paredes_udla_edu_ec/EojckZ4-1wdIhAYnTu6HYf4B-BJwp_aYFdc_p58HG11-Qw?e=efvbdR");
        link.setHref("#");
        link.setWidth("100%");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        radioGroup.setLabel("Seleccione el caso adecuado para su práctica acorde a su carrera");
        radioGroup.setWidth("min-content");
        radioGroup.setItems("Order ID", "Product Name", "Customer", "Status");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        comboBox.setLabel("Combo Box");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        buttonPrimary.setText("Siguiente");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(textSmall);
        getContent().add(link);
        getContent().add(layoutColumn2);
        layoutColumn2.add(radioGroup);
        layoutColumn2.add(comboBox);
        layoutColumn2.add(buttonPrimary);
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
