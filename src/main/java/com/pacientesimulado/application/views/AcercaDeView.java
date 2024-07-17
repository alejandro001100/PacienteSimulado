package com.pacientesimulado.application.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@PageTitle("Acerca de")
@Route(value = "acerca-de", layout = MainLayout.class)
public class AcercaDeView extends Composite<VerticalLayout> {

    public AcercaDeView() {
        getContent().setSizeFull();
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H2 title = new H2("Acerca del Proyecto");
        title.getStyle().set("color", "#2E3B4E");

        Paragraph description = new Paragraph(
                "Bienvenido a mi proyecto de Gestión de Pacientes Simulados. " +
                        "Mi nombre es Alejandro Paqui, un apasionado estudiante de Ciberseguridad. " +
                        "Este proyecto fue desarrollado en colaboración con la UITEC con el objetivo de proporcionar una herramienta eficiente y confiable para la gestión y reserva de pacientes simulados. " +
                        "Espero que encuentren esta aplicación útil y fácil de usar."
        );
        description.getStyle().set("font-size", "1.2em")
                .set("color", "#2E3B4E")
                .set("max-width", "800px")
                .set("text-align", "center")
                .set("white-space", "pre-wrap"); // Permite saltos de línea

        H3 githubTitle = new H3("GitHub del Proyecto");
        githubTitle.getStyle().set("color", "#2E3B4E");

        Anchor githubLink = new Anchor("https://github.com/alejandro001100", "Visita mi GitHub");
        githubLink.setTarget("_blank");
        githubLink.getStyle().set("font-size", "1.1em").set("color", "#1E88E5");

        // Imagen de GitHub
        StreamResource githubImageResource = new StreamResource("github-logo.png",
                () -> getClass().getResourceAsStream("/META-INF/resources/icons/images.jpg"));
        Image githubImage = new Image(githubImageResource, "GitHub Logo");
        githubImage.setWidth("100px");
        githubImage.getStyle().set("margin-top", "20px");

        VerticalLayout contentLayout = new VerticalLayout(title, description, githubTitle, githubLink, githubImage);
        contentLayout.setSpacing(true);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        getContent().add(contentLayout);
    }
}
