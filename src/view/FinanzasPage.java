package view;
import javax.swing.*;
import java.awt.*;

public class FinanzasPage extends Page {

    public FinanzasPage() {
        super("Finanzas", false);

        JButton nuevo = Ui.primary("+ Nuevo movimiento");
        put(Ui.pageHeader("Finanzas", null, Ui.row(10, Ui.outline("Exportar"), nuevo)));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(Ui.statCard("Ingresos del mes", "RD$ 128,500", "+12.8 % respecto a octubre",
                Theme.PRIMARY, null, false, 22));
        stats.add(Ui.statCard("Gastos del mes", "RD$ 42,800", "33.3 % de los ingresos",
                Theme.PRIMARY, null, false, 22));
        stats.add(Ui.statCard("Beneficio neto", "RD$ 85,700", "Margen de beneficio: 66.7 %",
                Theme.PRIMARY, null, false, 22));
        stats.add(Ui.statCard("Por cobrar", "RD$ 8,500", "3 pagos pendientes",
                Theme.AMBER, null, false, 22));
        put(stats);
        gap(16);

        JPanel mid = Ui.transparent(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridx = 0; gc.weightx = 0.62; gc.insets = new Insets(0, 0, 0, 8);
        mid.add(chartCard(), gc);
        gc.gridx = 1; gc.weightx = 0.38; gc.insets = new Insets(0, 8, 0, 0);
        mid.add(categoriasCard(), gc);
        put(mid);
        gap(16);

        put(movimientosCard());
    }

    private JComponent chartCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 8));
        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 8));
        north.add(Ui.cardHeader(Ui.label("Ingresos y gastos", Font.BOLD, 15, Theme.TEXT),
                Ui.label("Jun – Nov 2026", Font.PLAIN, 10, Theme.MUTED)));
        north.add(Ui.row(14, Ui.legend(Theme.PRIMARY, "Ingresos"), Ui.legend(Theme.BAR_LIGHT, "Gastos")));
        c.add(north, BorderLayout.NORTH);
        c.add(new Ui.BarChart(
                new String[]{"Jun", "Jul", "Ago", "Sep", "Oct", "Nov"},
                new double[]{95, 105, 100, 112, 118, 128.5},
                new double[]{40, 46, 41, 47, 44, 42.8},
                150, new String[]{"0", "50 mil", "100 mil", "150 mil"}), BorderLayout.CENTER);
        return c;
    }

    private static class SegmentBar extends JComponent {
        private final double[] parts;
        private final Color[] colors;

        SegmentBar(double[] parts, Color[] colors) {
            this.parts = parts;
            this.colors = colors;
            setPreferredSize(new Dimension(100, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double total = 0;
            for (double p : parts) total += p;
            int gap = 3;
            int usable = getWidth() - gap * (parts.length - 1);
            int x = 0;
            for (int i = 0; i < parts.length; i++) {
                int w = (int) Math.round(usable * parts[i] / total);
                g2.setColor(colors[i]);
                g2.fillRoundRect(x, 0, w, getHeight(), 8, 8);
                x += w + gap;
            }
            g2.dispose();
        }
    }

    private JComponent categoriasCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 14));

        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 4));
        north.add(Ui.label("Gastos por categoría", Font.BOLD, 15, Theme.TEXT));
        north.add(Ui.label("<html><b style='font-size:18px'>RD$ 42,800</b> &nbsp;<span style='color:"
                + Theme.hex(Theme.MUTED) + "'>este mes</span></html>", Font.PLAIN, 11, Theme.TEXT));
        c.add(north, BorderLayout.NORTH);

        Color[] cols = {Theme.PRIMARY_DARK, new Color(0x6E, 0x9B, 0x7B), new Color(0xD3, 0xE6, 0xA4), new Color(0xE3, 0xE7, 0xE0)};
        String[] nombres = {"Nómina", "Alquiler", "Productos", "Servicios"};
        String[] montos = {"RD$ 24,000", "RD$ 10,000", "RD$ 6,000", "RD$ 2,800"};
        String[] pct = {"56.1 %", "23.4 %", "14.0 %", "6.5 %"};
        double[] valores = {24000, 10000, 6000, 2800};

        JPanel center = Ui.transparent(new BorderLayout(0, 12));
        center.add(new SegmentBar(valores, cols), BorderLayout.NORTH);
        JPanel list = Ui.transparent(new GridLayout(0, 1, 0, 6));
        for (int i = 0; i < nombres.length; i++) {
            JPanel r = Ui.transparent(new BorderLayout());
            r.add(Ui.legend(cols[i], nombres[i]), BorderLayout.WEST);
            r.add(Ui.label(montos[i] + "   " + pct[i], Font.PLAIN, 11, Theme.TEXT), BorderLayout.EAST);
            list.add(r);
        }
        center.add(list, BorderLayout.CENTER);
        c.add(center, BorderLayout.CENTER);
        return c;
    }

    private JComponent movimientosCard() {
        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 12));

        JButton mes = Ui.outline("Noviembre");
        mes.setFont(Theme.font(Font.PLAIN, 12));
        mes.setIcon(Icons.of("chevron", 12, Theme.MUTED));
        mes.setHorizontalTextPosition(SwingConstants.LEFT);

        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 10));
        north.add(Ui.cardHeader(
                Ui.row(10, Ui.label("Últimos movimientos", Font.BOLD, 15, Theme.TEXT),
                        Ui.badge("48 movimientos", Theme.BG, Theme.MUTED)),
                Ui.row(10, new Ui.SearchField("Buscar movimiento...", 240), mes)));
        north.add(Ui.tabs("Todos (48)", "Ingresos (36)", "Gastos (12)", "Por cobrar (3)"));
        c.add(north, BorderLayout.NORTH);

        Ui.DataTable t = new Ui.DataTable(
                new String[]{"CONCEPTO", "FECHA", "MÉTODO DE PAGO", "IMPORTE", "ESTADO", "ACCIÓN"},
                new double[]{2.4, 1.2, 1.4, 1.2, 1.0, 1.2});
        t.addRow(Ui.twoLine("Limpieza facial · María González", "MOV-048"), "19 nov 2026", "Tarjeta",
                Ui.label("+ RD$ 2,500", Font.BOLD, 12, Theme.PRIMARY), Ui.estado("Cobrado"),
                Ui.action("Ver detalle →", null));
        t.addRow(Ui.twoLine("Compra de productos · Beauty Pro", "MOV-047"), "19 nov 2026", "Transferencia",
                Ui.label("− RD$ 6,000", Font.BOLD, 12, Theme.RED), Ui.estado("Pagado"),
                Ui.action("Ver detalle →", null));
        t.addRow(Ui.twoLine("Masaje relajante · Carlos Ramírez", "MOV-046"), "18 nov 2026", "Efectivo",
                Ui.label("+ RD$ 1,800", Font.BOLD, 12, Theme.PRIMARY), Ui.estado("Cobrado"),
                Ui.action("Ver detalle →", null));
        t.addRow(Ui.twoLine("Tratamiento facial · Ana Martínez", "MOV-045"), "17 nov 2026", "Transferencia",
                Ui.label("RD$ 3,500", Font.BOLD, 12, Theme.TEXT), Ui.estado("Pendiente"),
                Ui.action("Registrar pago →", null));
        c.add(t, BorderLayout.CENTER);
        c.add(Ui.pager("Mostrando 1-4 de 48 movimientos", "<", "1", "2", "3", "...", "12", ">"), BorderLayout.SOUTH);
        return c;
    }
}