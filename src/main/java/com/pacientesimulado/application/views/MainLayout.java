package com.pacientesimulado.application.views;

import com.pacientesimulado.application.data.User;
import com.pacientesimulado.application.security.AuthenticatedUser;
import com.pacientesimulado.application.views.agendarreserva.AgendarReservaView;
import com.pacientesimulado.application.views.disponibilidadsemanadeclases.DisponibilidadsemanadeClasesView;
import com.pacientesimulado.application.views.entrenamientodeactores.EntrenamientodeactoresView;
import com.pacientesimulado.application.views.gestionusuarios.GestionUsuariosView;
import com.pacientesimulado.application.views.personform.PersonFormView;
import com.pacientesimulado.application.views.reservaprogramapacientesimuladoactor.ReservaProgramaPacienteSimuladoACTORView;
import com.pacientesimulado.application.views.seleccionarpacientes.SeleccionarpacientesView;
import com.pacientesimulado.application.views.solicitudesdereserva.SolicitudesdereservaView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("Paciente Simulado");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(PersonFormView.class)) {
            nav.addItem(new SideNavItem("Person Form", PersonFormView.class, LineAwesomeIcon.USER.create()));

        }
        if (accessChecker.hasAccess(DisponibilidadsemanadeClasesView.class)) {
            nav.addItem(new SideNavItem("Disponibilidad semana de Clases", DisponibilidadsemanadeClasesView.class,
                    LineAwesomeIcon.CALENDAR_ALT_SOLID.create()));

        }
        if (accessChecker.hasAccess(ReservaProgramaPacienteSimuladoACTORView.class)) {
            nav.addItem(new SideNavItem("Reserva Programa Paciente Simulado (ACTOR)",
                    ReservaProgramaPacienteSimuladoACTORView.class, LineAwesomeIcon.PENCIL_RULER_SOLID.create()));

        }
        if (accessChecker.hasAccess(AgendarReservaView.class)) {
            nav.addItem(new SideNavItem("Agendar Reserva", AgendarReservaView.class,
                    LineAwesomeIcon.PENCIL_RULER_SOLID.create()));

        }
        if (accessChecker.hasAccess(SeleccionarpacientesView.class)) {
            nav.addItem(new SideNavItem("Seleccionar pacientes", SeleccionarpacientesView.class,
                    LineAwesomeIcon.PENCIL_RULER_SOLID.create()));

        }
        if (accessChecker.hasAccess(EntrenamientodeactoresView.class)) {
            nav.addItem(new SideNavItem("Entrenamiento de actores", EntrenamientodeactoresView.class,
                    LineAwesomeIcon.PENCIL_RULER_SOLID.create()));

        }
        if (accessChecker.hasAccess(SolicitudesdereservaView.class)) {
            nav.addItem(new SideNavItem("Solicitudes de reserva", SolicitudesdereservaView.class,
                    LineAwesomeIcon.TH_SOLID.create()));

        }
        if (accessChecker.hasAccess(GestionUsuariosView.class)) {
            nav.addItem(new SideNavItem("Gestion Usuarios", GestionUsuariosView.class,
                    LineAwesomeIcon.COLUMNS_SOLID.create()));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
