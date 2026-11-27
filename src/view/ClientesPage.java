package view;

import javax.swing.*;
import java.awt.*;

public class ClientesPage extends Page {

    public ClientesPage() {
        super("Clientes", false);

        JButton nuevo = Ui.primary("+ Nuevo cliente");
        put(Ui.pageHeader("Clientes", "users", Ui.row(10, Ui.outline("Exportar"), nuevo)));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(Ui.statCard("Clientes registrados", "24", "Tu base de clientes", Theme.MUTED, null, false, 24));
        stats.add(Ui.statCard("Clientes activos", "20 clientes", "Con visitas en los últimos 90 días", Theme.PRIMARY, null, false, 22));
        stats.add(Ui.statCard("Nuevos este mes", "3 clientes", "Registrados en noviembre", Theme.AMBER, null, false, 22));
        stats.add(Ui.statCard("Clientes inactivos", "4 clientes", "Sin visitas en más de 90 días", Theme.RED, null, false, 22));
        put(stats);
        gap(16);

        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 12));

        JButton filtro = Ui.outline("Todos");
        filtro.setFont(Theme.font(Font.PLAIN, 12));
        filtro.setIcon(Icons.of("chevron", 12, Theme.MUTED));
        filtro.setHorizontalTextPosition(SwingConstants.LEFT);

        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 10));
        north.add(Ui.cardHeader(
                Ui.row(10, Ui.label("Todos los clientes", Font.BOLD, 15, Theme.TEXT),
                        Ui.badge("24 clientes", Theme.BG, Theme.MUTED)),
                Ui.row(10, new Ui.SearchField("Buscar nombre o teléfono...", 240), filtro)));
        north.add(Ui.tabs("Todos (24)", "Activos (20)", "Inactivos (4)"));
        c.add(north, BorderLayout.NORTH);

        Ui.DataTable t = new Ui.DataTable(
                new String[]{"CLIENTE", "CONTACTO", "ÚLTIMA VISITA", "VISITAS", "ESTADO", "ACCIÓN"},
                new double[]{2.0, 1.6, 1.4, 0.8, 1.0, 1.0});
        String[][] filas = {
                {"María González", "CLI-001", "829-123-4567", "19 nov 2026", "12", "Activo"},
                {"Carlos Ramírez", "CLI-002", "829-765-4321", "18 nov 2026", "8", "Activo"},
                {"Ana Martínez", "CLI-003", "829-111-2222", "17 nov 2026", "6", "Activo"},
                {"Luis Hernández", "CLI-004", "829-333-4444", "15 nov 2026", "10", "Activo"},
                {"Sofía López", "CLI-005", "829-555-6666", "12 nov 2026", "1", "Activo"},
                {"Laura Torres", "CLI-006", "829-597-2069", "10 jul 2026", "4", "Inactivo"},
        };
        for (String[] f : filas) {
            t.addRow(Ui.twoLine(f[0], f[1]), f[2],
                    Ui.label(f[3], Font.BOLD, 12, Theme.TEXT), f[4], Ui.estado(f[5]),
                    Ui.action("Ver ficha →", null));
        }
        c.add(t, BorderLayout.CENTER);
        c.add(Ui.pager("Mostrando 1-6 de 24 clientes", "<", "1", "2", "3", "4", ">"), BorderLayout.SOUTH);
        put(c);
    }
}
