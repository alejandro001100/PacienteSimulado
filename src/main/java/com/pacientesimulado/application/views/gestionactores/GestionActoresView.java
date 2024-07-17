package com.pacientesimulado.application.views.gestionactores;

import com.pacientesimulado.application.data.Actor;
import com.pacientesimulado.application.services.ActorService;
import com.pacientesimulado.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Route(value = "gestion-actores", layout = MainLayout.class)
@PageTitle("Gestión de Actores")
public class GestionActoresView extends VerticalLayout {

    private final ActorService actorService;
    private final Grid<Actor> grid;

    @Autowired
    public GestionActoresView(ActorService actorService) {
        this.actorService = actorService;
        this.grid = new Grid<>(Actor.class, false);

        configureGrid();
        add(grid, createDownloadButton());
        listActores();
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Actor::getNombre).setHeader("Nombre").setSortable(true).setAutoWidth(true);
        grid.addColumn(Actor::getCorreo).setHeader("Correo").setSortable(true).setAutoWidth(true);
        grid.addColumn(Actor::getEdad).setHeader("Edad").setSortable(true).setAutoWidth(true);
        grid.addColumn(Actor::getSexo).setHeader("Sexo").setSortable(true).setAutoWidth(true);
        grid.addColumn(Actor::getPeso).setHeader("Peso").setSortable(true).setAutoWidth(true);
        grid.addColumn(Actor::getAltura).setHeader("Altura").setSortable(true).setAutoWidth(true);
    }

    private void listActores() {
        List<Actor> actores = actorService.obtenerTodosLosActores();
        if (actores != null && !actores.isEmpty()) {
            grid.setItems(actores);
        }
    }

    private Button createDownloadButton() {
        Button downloadButton = new Button("Descargar Actores en Excel");
        downloadButton.addClickListener(event -> {
            StreamResource resource = createExcelResource();
            if (resource != null) {
                Anchor downloadLink = new Anchor(resource, "");
                downloadLink.getElement().setAttribute("download", "actores.xlsx");
                downloadLink.add(downloadButton);
                add(downloadLink);
                downloadLink.getElement().callJsFunction("click");
            }
        });
        return downloadButton;
    }

    private StreamResource createExcelResource() {
        return new StreamResource("actores.xlsx", () -> {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Actores");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Nombre");
                headerRow.createCell(1).setCellValue("Correo");
                headerRow.createCell(2).setCellValue("Edad");
                headerRow.createCell(3).setCellValue("Sexo");
                headerRow.createCell(4).setCellValue("Peso");
                headerRow.createCell(5).setCellValue("Altura");

                List<Actor> actores = actorService.obtenerTodosLosActores();
                int rowIndex = 1;
                for (Actor actor : actores) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(actor.getNombre());
                    row.createCell(1).setCellValue(actor.getCorreo());
                    row.createCell(2).setCellValue(actor.getEdad());
                    row.createCell(3).setCellValue(actor.getSexo());
                    row.createCell(4).setCellValue(actor.getPeso());
                    row.createCell(5).setCellValue(actor.getAltura());
                }

                workbook.write(out);
                workbook.close();
                return new ByteArrayInputStream(out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
