package com.pacientesimulado.application.views.login;

import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.UsuarioService;
import com.pacientesimulado.application.views.disponibilidadsemanadeclases.DisponibilidadSemanaDeClasesView;
import com.pacientesimulado.application.views.gestionusuarios.GestionUsuariosView;
import com.pacientesimulado.application.views.registro.RegistroView;
import com.pacientesimulado.application.views.reservaprogramapacientesimuladoactor.ReservaProgramaPacienteSimuladoACTORView;
import com.pacientesimulado.application.views.personform.PersonFormView;
import com.pacientesimulado.application.views.restablecerpassword.RestablecerPasswordView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

@Route(value = "")
@PageTitle("Login | Sistema de Gestión de Pacientes Simulados")
public class LoginView extends VerticalLayout {

    private final UsuarioService usuarioService;

    public LoginView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setWidth("100%");
        getStyle().set("flex-grow", "1");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        H3 title = new H3("¡Le damos la bienvenida a la plataforma de reserva paciente simulado!");
        H5 subtitle = new H5("Inicie sesión en su cuenta");

        // Añadir imágenes
        Image recursoImage = new Image("icons/recurso.png", "Recurso");
        recursoImage.setWidth("200px");
        Image imagenesImage = new Image("icons/images.jpg", "Imagenes");
        imagenesImage.setWidth("50px");

        HorizontalLayout imagesLayout = new HorizontalLayout(recursoImage, imagenesImage);
        imagesLayout.setSpacing(true);
        imagesLayout.setAlignItems(Alignment.CENTER);

        EmailField emailField = new EmailField("Correo electrónico");
        emailField.setWidthFull();

        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidthFull();

        RouterLink forgotPasswordLink = new RouterLink("¿Olvidó su contraseña?", RestablecerPasswordView.class);
        forgotPasswordLink.getElement().getStyle().set("margin-top", "10px");

        Button loginButton = new Button("Iniciar Sesión");
        loginButton.setWidth("100%");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.getStyle().set("margin-top", "10px");
        loginButton.addClickListener(event -> {
            String correo = emailField.getValue();
            String contraseña = passwordField.getValue();
            Optional<Usuario> userOptional = usuarioService.obtenerUsuarioPorCorreo(correo);
            if (userOptional.isPresent()) {
                Usuario user = userOptional.get();
                if (user.getContraseña().equals(contraseña)) {
                    VaadinSession.getCurrent().setAttribute(Usuario.class, user);
                    Notification.show("Inicio de sesión exitoso!", 3000, Notification.Position.MIDDLE);

                    // Redirigir según el rol del usuario
                    if ("Administrador".equals(user.getRol())) {
                        getUI().ifPresent(ui -> ui.navigate(GestionUsuariosView.class));
                    } else if ("Actor".equals(user.getRol())) {
                        getUI().ifPresent(ui -> ui.navigate(DisponibilidadSemanaDeClasesView.class));
                    } else if ("Doctor".equals(user.getRol())) {
                        getUI().ifPresent(ui -> ui.navigate(ReservaProgramaPacienteSimuladoACTORView.class));
                    }
                } else {
                    Notification.show("Correo o contraseña incorrectos.", 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Correo o contraseña incorrectos.", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout actions = new HorizontalLayout(loginButton);
        actions.setWidthFull();
        actions.setJustifyContentMode(JustifyContentMode.CENTER);
        actions.setAlignItems(Alignment.CENTER);

        Paragraph noAccountText = new Paragraph("¿No tiene una cuenta?");
        RouterLink registerLink = new RouterLink("Regístrese", RegistroView.class);
        registerLink.getElement().getStyle().set("margin-left", "0.5em");

        HorizontalLayout registerLayout = new HorizontalLayout(noAccountText, registerLink);
        registerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        registerLayout.setAlignItems(Alignment.CENTER);

        add(imagesLayout, title, subtitle, emailField, passwordField, forgotPasswordLink, actions, registerLayout);
    }
}
