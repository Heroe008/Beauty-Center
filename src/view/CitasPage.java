package view;

import javax.swing.*;
import java.awt.*;

public class CitasPage extends Page {

    public CitasPage() {
        super("Citas", false);

        JButton exportar = Ui.outline("Exportar");
        JButton nueva = Ui.primary("+ Nueva cita");
        put(Ui.pageHeader("Citas", "calendar", Ui.row(10, exportar, nueva)));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(Ui.miniStat("Clientes registrados", "30", "user", Theme.TEXT));
        stats.add(Ui.miniStat("Citas canceladas", "3", "user-x", Theme.RED));
        stats.add(Ui.miniStat("Citas confirmadas", "15", "user-check", Theme.PRIMARY));
        stats.add(Ui.miniStat("Citas pendientes", "5", "user-minus", new Color(0xD4, 0xC4, 0x2E)));
        put(stats);
        gap(16);

        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 12));

        JButton filtrar = Ui.outline("Filtrar");
        filtrar.setFont(Theme.font(Font.PLAIN, 12));
        filtrar.setIcon(Icons.of("chevron", 12, Theme.MUTED));
        filtrar.setHorizontalTextPosition(SwingConstants.LEFT);
        c.add(Ui.cardHeader(
                Ui.row(10, Ui.label("Lista de citas", Font.BOLD, 15, Theme.TEXT),
                        Ui.badge("30 personas", Theme.BG, Theme.MUTED)),
                Ui.row(10, new Ui.SearchField("Buscar clientes...", 200), filtrar)), BorderLayout.NORTH);

        Ui.DataTable t = new Ui.DataTable(
                new String[]{"CLIENTE", "SERVICIO", "FECHA", "HORA", "ESTADO", "ACCIONES"},
                new double[]{1.6, 1.8, 1.2, 1.0, 1.2, 1.3});
        String[][] filas = {
                {"Karla Pilier", "Manicure", "20/11/2026", "9:00 AM", "Cancelada"},
                {"Ana Rodríguez", "Pedicure", "20/11/2026", "10:30 AM", "Pendiente"},
                {"Laura Díaz", "Uñas acrílicas", "21/11/2026", "2:00 PM", "Pendiente"},
                {"Daniela Gómez", "Manicure", "21/11/2026", "3:15 PM", "Pendiente"},
                {"Sofía Martínez", "Tratamiento facial", "21/11/2026", "11:00 AM", "Confirmada"},
                {"Margaret Familia", "Tratamiento capilar completo", "22/11/2026", "1:30 PM", "Confirmada"},
        };
        for (final String[] f : filas) {
            JButton ver = Ui.iconButton("eye", "Ver");
            JButton edit = Ui.iconButton("edit", "Editar");
            JButton del = Ui.iconButton("trash", "Eliminar");
            t.addRow(Ui.label(f[0], Font.BOLD, 12, Theme.TEXT), f[1], f[2], f[3],
                    Ui.estado(f[4]), Ui.row(8, ver, edit, del));
        }
        c.add(t, BorderLayout.CENTER);
        c.add(Ui.pager("Mostrando 1-6 de 30 citas", "<", "1", "2", ">"), BorderLayout.SOUTH);
        put(c);
    }
}