package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ResumenPage extends Page {

    public ResumenPage() {
        super("Resumen", true);

        JButton fecha = Ui.outline("Noviembre 2026");
        fecha.setIcon(Icons.of("calendar", 14, Theme.TEXT));
        fecha.setIconTextGap(8);
        JPopupMenu meses = new JPopupMenu();
        meses.setBackground(Color.WHITE);
        meses.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        String[] listaMeses = {
            "Noviembre 2026", "Octubre 2026", "Septiembre 2026", "Agosto 2026",
            "Julio 2026", "Junio 2026", "Mayo 2026", "Abril 2026"
        };
        for (String mes : listaMeses) {
            JMenuItem item = new JMenuItem(mes);
            item.setFont(Theme.font(Font.PLAIN, 12));
            item.setForeground(Theme.TEXT);
            item.setBackground(Color.WHITE);
            item.setBorder(new EmptyBorder(8, 14, 8, 14));
            meses.add(item);
        }
        fecha.addActionListener(e -> {

        	meses.setPopupSize(fecha.getWidth(), meses.getPreferredSize().height);

        	meses.show(fecha, 0, fecha.getHeight() + 4);

        	});

        put(Ui.pageHeader("Resumen del día", null, Ui.row(10, fecha)));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(Ui.statCard("Ingresos del mes", "$48,250",
                "<html>" + Ui.colored("↗ 12.8%", Theme.PRIMARY) + " vs. mes anterior</html>",
                Theme.MUTED, "wallet", false, 24));
        stats.add(Ui.statCard("Gastos del mes", "$18,600",
                "<html>" + Ui.colored("↘ 4.2%", Theme.PRIMARY) + " vs. mes anterior</html>",
                Theme.MUTED, "chart", false, 24));
        stats.add(Ui.statCard("Ganancia neta", "$29,650",
                "<html>" + Ui.colored("↗ 18.4%", Theme.PRIMARY) + " vs. mes anterior</html>",
                Theme.MUTED, "arrow-up-right", true, 24));
        stats.add(Ui.statCard("Citas de hoy", "8 citas",
                "<html>" + Ui.colored("5 confirmadas", Theme.PRIMARY) + " &nbsp;3 por confirmar</html>",
                Theme.MUTED, "calendar", false, 24));
        put(stats);
        gap(16);

        JPanel mid = Ui.transparent(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridx = 0; gc.weightx = 0.58; gc.insets = new Insets(0, 0, 0, 8);
        mid.add(chartCard(), gc);
        gc.gridx = 1; gc.weightx = 0.42; gc.insets = new Insets(0, 8, 0, 0);
        mid.add(citasCard(), gc);
        put(mid);
        gap(16);

        put(inventarioCard());
    }

    private JComponent chartCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 8));

        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 8));
        north.add(Ui.cardHeader(Ui.label("Ganancias y pérdidas", Font.BOLD, 15, Theme.TEXT),
                Ui.action("Ver reporte ↗", null)));
        north.add(Ui.cardHeader(
                Ui.row(14, Ui.legend(Theme.PRIMARY, "Ingresos"), Ui.legend(Theme.BAR_LIGHT, "Gastos")),
                Ui.label("DOP · 1–18 nov", Font.PLAIN, 10, Theme.MUTED)));
        c.add(north, BorderLayout.NORTH);

        c.add(new Ui.BarChart(
                new String[]{"1–5 nov", "6–10 nov", "11–15 nov", "16–19 nov"},
                new double[]{10.5, 13.0, 11.5, 13.25},
                new double[]{4.0, 5.0, 4.2, 5.4},
                15, new String[]{"$0", "$5 mil", "$10 mil", "$15 mil"}), BorderLayout.CENTER);

        JPanel foot = Ui.transparent(new BorderLayout());
        foot.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Theme.LINE), new EmptyBorder(10, 0, 0, 0)));
        foot.add(Ui.label("Tu margen de ganancia es del 61.5%", Font.PLAIN, 11, Theme.TEXT), BorderLayout.WEST);
        foot.add(Ui.badge("Balance positivo", Theme.GREEN_BG, Theme.PRIMARY), BorderLayout.EAST);
        c.add(foot, BorderLayout.SOUTH);
        return c;
    }

    private JComponent citasCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 10));
        c.add(Ui.cardHeader(Ui.label("Próximas citas", Font.BOLD, 15, Theme.TEXT),
                Ui.badge("Hoy · 8 citas", Theme.GREEN_BG, Theme.PRIMARY)), BorderLayout.NORTH);

        String[][] citas = {
                {"10:00", "María González", "Servicio de pedicure", "Confirmada"},
                {"11:30", "Carlos Ramírez", "Servicio de manicure", "Confirmada"},
                {"13:00", "Ana Martínez", "Servicio de uñas", "Pendiente"},
                {"15:30", "Luis Hernández", "Servicio de Tratamiento capilar", "Confirmada"},
        };
        JPanel list = Ui.transparent(new GridLayout(0, 1));
        for (String[] r : citas) {
            JPanel row = Ui.transparent(new BorderLayout(14, 0));
            row.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Theme.LINE), new EmptyBorder(8, 0, 8, 0)));
            JPanel hora = Ui.twoLine(r[0], "60 min");
            hora.setPreferredSize(new Dimension(48, 30));
            JPanel info = Ui.transparent(new GridLayout(2, 1, 0, 1));
            info.add(Ui.label(r[1], Font.BOLD, 12, Theme.TEXT));
            info.add(Ui.label(r[2], Font.PLAIN, 10, Theme.MUTED));
            row.add(hora, BorderLayout.WEST);
            row.add(info, BorderLayout.CENTER);
            row.add(Ui.center(Ui.estado(r[3])), BorderLayout.EAST);
            list.add(row);
        }
        c.add(list, BorderLayout.CENTER);

        JPanel foot = Ui.transparent(new BorderLayout());
        foot.add(Ui.label("4 citas más programadas", Font.PLAIN, 10, Theme.MUTED), BorderLayout.WEST);
        foot.add(Ui.action("Ver agenda →", null), BorderLayout.EAST);
        c.add(foot, BorderLayout.SOUTH);
        return c;
    }

    private JComponent inventarioCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 12));

        JButton ver = Ui.outline("Ver inventario ↗");
        ver.setFont(Theme.font(Font.PLAIN, 11));
        c.add(Ui.cardHeader(
                Ui.row(10, Ui.label("Inventario que necesita atención", Font.BOLD, 15, Theme.TEXT),
                        Ui.badge("4 productos", Theme.AMBER_BG, Theme.AMBER)),
                ver), BorderLayout.NORTH);

        Ui.DataTable t = new Ui.DataTable(
                new String[]{"PRODUCTO", "CATEGORÍA", "STOCK ACTUAL", "STOCK MÍNIMO", "ESTADO", "ACCIÓN"},
                new double[]{2.2, 1.6, 1.2, 1.2, 1.2, 1.0});
        Object[][] filas = {
                {"Aceite esencial · Lavanda", "Consumibles", "0 unidades", "10 unidades", "Agotado"},
                {"Guantes · M", "Material de trabajo", "3 unidades", "20 unidades", "Stock bajo"},
                {"Alcohol isopropílico · 1 L", "Higiene", "2 unidades", "8 unidades", "Stock bajo"},
                {"Toallas desechables", "Consumibles", "5 unidades", "15 unidades", "Stock bajo"},
        };
        for (Object[] f : filas) {
            boolean agotado = f[4].equals("Agotado");
            t.addRow(Ui.label((String) f[0], Font.BOLD, 12, Theme.TEXT), f[1],
                    Ui.label((String) f[2], Font.BOLD, 12, agotado ? Theme.RED : Theme.AMBER),
                    f[3], Ui.estado((String) f[4]),
                    Ui.action("Reponer →", null));
        }
        c.add(t, BorderLayout.CENTER);
        return c;
    }
}