package com.pacientesimulado.application.views.personform;

import com.pacientesimulado.application.data.Actor;
import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.ActorService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "person-form", layout = MainLayout.class)
@PageTitle("Actualizar Datos del Actor")
public class PersonFormView extends VerticalLayout {

    private final ActorService actorService;
    private Actor actor;

    private NumberField edadField = new NumberField("Edad");
    private NumberField pesoField = new NumberField("Peso en kg");
    private NumberField alturaField = new NumberField("Altura en cm");
    private Paragraph welcomeMessage = new Paragraph();

    private Binder<Actor> binder = new Binder<>(Actor.class);

    @Autowired
    public PersonFormView(ActorService actorService) {
        this.actorService = actorService;

        Usuario currentUser = VaadinSession.getCurrent().getAttribute(Usuario.class);

        add(new H2("Actualizar Datos del Actor"));

        welcomeMessage.setText("Bienvenido, " + currentUser.getNombre() +" "+ currentUser.getApellido () +". Edita tu información.");
        add(welcomeMessage);

        FormLayout formLayout = new FormLayout();
        formLayout.add(edadField, pesoField, alturaField);

        Button saveButton = new Button("Guardar Datos", e -> guardarDatosActor());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton);

        add(formLayout, buttonsLayout);

        binder.forField(edadField)
                .withConverter(new DoubleToIntegerConverter())
                .asRequired("Edad es requerida")
                .withValidator(p -> p != null && p >= 18 && p <= 90, "La edad debe estar entre 18 y 90 años")
                .bind(Actor::getEdad, Actor::setEdad);

        binder.forField(pesoField)
                .asRequired("Peso es requerido")
                .withValidator(p -> p != null && p >= 30 && p <= 250, "Peso debe estar entre 30 kg y 250 kg")
                .bind(Actor::getPeso, Actor::setPeso);

        binder.forField(alturaField)
                .asRequired("Altura es requerida")
                .withValidator(a -> a != null && a >= 100 && a <= 250, "Altura debe estar entre 1.0 m y 2.50 m")
                .bind(Actor::getAltura, Actor::setAltura);

        // Cargar actor por el correo del usuario actual
        actorService.obtenerActorPorCorreo(currentUser.getCorreo()).ifPresentOrElse(
                actor -> {
                    this.actor = actor;
                    binder.readBean(actor); // Vincular los datos del actor al formulario
                },
                () -> Notification.show("No se encontró actor con ese correo")
        );

        binder.bindInstanceFields(this);
    }

    private void guardarDatosActor() {
        if (actor == null) {
            Notification.show("Por favor, busque un actor primero.");
            return;
        }
        if (binder.writeBeanIfValid(actor)) {
            actorService.guardarActor(actor);
            Notification.show("Datos guardados correctamente.");
        } else {
            Notification.show("Por favor, complete todos los campos correctamente.");
        }
    }

    private class DoubleToIntegerConverter implements Converter<Double, Integer> {
        @Override
        public Result<Integer> convertToModel(Double value, ValueContext context) {
            return value == null ? Result.ok(null) : Result.ok(value.intValue());
        }

        @Override
        public Double convertToPresentation(Integer value, ValueContext context) {
            return value == null ? null : value.doubleValue();
        }
    }
}
