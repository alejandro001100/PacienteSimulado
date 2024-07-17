package com.pacientesimulado.application.views;

import com.pacientesimulado.application.data.Usuario;

import com.pacientesimulado.application.views.disponibilidadpracticadoctor.DisponibilidadPracticaDoctorView;
import com.pacientesimulado.application.views.disponibilidadsemanadeclases.DisponibilidadSemanaDeClasesView;
import com.pacientesimulado.application.views.gestionactores.GestionActoresView;
import com.pacientesimulado.application.views.gestionmaterias.GestionMateriasView;
import com.pacientesimulado.application.views.gestionusuarios.GestionUsuariosView;
import com.pacientesimulado.application.views.reservaprogramapacientesimuladoactor.ReservaProgramaPacienteSimuladoACTORView;
import com.pacientesimulado.application.views.personform.PersonFormView;
import com.pacientesimulado.application.views.solicitudesdereserva.SolicitudesdereservaView;
import com.pacientesimulado.application.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        // Cambia la ruta de la imagen por la ruta de tu logo
        Image logoImage = new Image("icons/recurso.png", "Logo");
        logoImage.setHeight("50px");

        H1 logoText = new H1("Sistema de Gestión de Pacientes Simulados");
        logoText.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Cerrar Sesión", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate(LoginView.class);
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logoImage, logoText, logoutButton);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logoText);
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        Usuario user = VaadinSession.getCurrent().getAttribute(Usuario.class);
        if (user != null) {
            if ("Administrador".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Gestión de Usuarios", GestionUsuariosView.class),
                        new RouterLink("Gestión de Materias", GestionMateriasView.class),
                        new RouterLink("Solicitudes de Reserva", SolicitudesdereservaView.class),
                        new RouterLink("Actores", GestionActoresView.class),
                        new RouterLink("Creditos", AcercaDeView.class)
                ));
            } else if ("Actor".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Disponibilidad Semana de Clases", DisponibilidadSemanaDeClasesView.class),
                        new RouterLink("Actualizar Datos del Actor", PersonFormView.class),
                        new RouterLink("Creditos", AcercaDeView.class)
                ));
            } else if ("Doctor".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Reserva Programa Paciente Simulado (ACTOR)", ReservaProgramaPacienteSimuladoACTORView.class),
                        new RouterLink("Disponibilidad Práctica Doctor", DisponibilidadPracticaDoctorView.class),
                        new RouterLink("Creditos", AcercaDeView.class)
                ));
            }
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute(Usuario.class) == null) {
            event.rerouteTo(LoginView.class);
        }
    }
}
