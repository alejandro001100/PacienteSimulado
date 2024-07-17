package com.pacientesimulado.application.views.restablecerpassword;

import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.services.UsuarioService;
import com.pacientesimulado.application.views.login.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Restablecer Contraseña")
@Route(value = "restablecer-password")
public class RestablecerPasswordView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private Usuario usuarioActual;

    public RestablecerPasswordView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        EmailField emailField = new EmailField("Correo electrónico");
        PasswordField newPasswordField = new PasswordField("Nueva contraseña");
        PasswordField confirmNewPasswordField = new PasswordField("Confirmar nueva contraseña");
        Button resetButton = new Button("Restablecer contraseña");
        Button changePasswordButton = new Button("Cambiar Contraseña");
        Button backButton = new Button("Regresar");

        resetButton.addClickListener(event -> {
            String correo = emailField.getValue();
            usuarioActual = usuarioService.obtenerUsuarioPorCorreo(correo).orElse(null);
            if (usuarioActual != null) {
                Notification.show("Bienvenido, " + usuarioActual.getNombre() + " " + usuarioActual.getApellido(), 3000, Notification.Position.MIDDLE);
                emailField.setVisible(false);
                resetButton.setVisible(false);
                newPasswordField.setVisible(true);
                confirmNewPasswordField.setVisible(true);
                changePasswordButton.setVisible(true);
                backButton.setVisible(true);
            } else {
                Notification.show("No se encontró una cuenta con ese correo.", 3000, Notification.Position.MIDDLE);
            }
        });

        changePasswordButton.addClickListener(event -> {
            String nuevaContrasena = newPasswordField.getValue();
            String confirmarNuevaContrasena = confirmNewPasswordField.getValue();
            if (nuevaContrasena.isEmpty() || confirmarNuevaContrasena.isEmpty()) {
                Notification.show("Los campos de contraseña no pueden estar vacíos.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (nuevaContrasena.equals(confirmarNuevaContrasena)) {
                usuarioActual.setContraseña(nuevaContrasena);
                usuarioService.actualizarUsuario(usuarioActual.getId(), usuarioActual);
                Notification.show("Contraseña cambiada exitosamente!", 3000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            } else {
                Notification.show("Las contraseñas no coinciden.", 3000, Notification.Position.MIDDLE);
            }
        });

        backButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });

        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        newPasswordField.setVisible(false);
        confirmNewPasswordField.setVisible(false);
        changePasswordButton.setVisible(false);
        backButton.setVisible(false);

        add(new H3("Restablecer tu contraseña"), emailField, resetButton, newPasswordField, confirmNewPasswordField, changePasswordButton, backButton);
    }
}
