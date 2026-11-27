package view;

import javax.swing.*;
import java.awt.*;

public class InventarioPage extends Page {

    public InventarioPage() {
        super("Inventario", false);

        JButton nuevo = Ui.primary("+ Nuevo producto");
        put(Ui.pageHeader("Inventario", "box", Ui.row(10, Ui.outline("Exportar"), nuevo)));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(Ui.statCard("Productos registrados", "24", "En 4 categorías", Theme.MUTED, null, false, 24));
        stats.add(Ui.statCard("Stock disponible", "20 productos", "Por encima del mínimo", Theme.PRIMARY, null, false, 22));
        stats.add(Ui.statCard("Stock bajo", "3 productos", "Necesitan reposición", Theme.AMBER, null, false, 22));
        stats.add(Ui.statCard("Productos agotados", "1 producto", "Sin unidades disponibles", Theme.RED, null, false, 22));
        put(stats);
        gap(16);

        RoundedPanel c = Ui.card(18);
        c.setLayout(new BorderLayout(0, 12));

        JButton cat = Ui.outline("Todas las categorías");
        cat.setFont(Theme.font(Font.PLAIN, 12));
        cat.setIcon(Icons.of("chevron", 12, Theme.MUTED));
        cat.setHorizontalTextPosition(SwingConstants.LEFT);

        JPanel north = Ui.transparent(new GridLayout(2, 1, 0, 10));
        north.add(Ui.cardHeader(
                Ui.row(10, Ui.label("Todos los productos", Font.BOLD, 15, Theme.TEXT),
                        Ui.badge("24 productos", Theme.BG, Theme.MUTED)),
                Ui.row(10, new Ui.SearchField("Buscar producto o código...", 240), cat)));
        north.add(Ui.tabs("Todos (24)", "Stock bajo (3)", "Agotados (1)"));
        c.add(north, BorderLayout.NORTH);

        Ui.DataTable t = new Ui.DataTable(
                new String[]{"PRODUCTO", "CATEGORÍA", "STOCK ACTUAL", "STOCK MÍNIMO", "ESTADO", "ACCIÓN"},
                new double[]{2.2, 1.6, 1.2, 1.2, 1.2, 1.0});
        Object[][] filas = {
                {"Aceite esencial · Lavanda", "INV-001", "Consumibles", "0 unidades", "10 unidades", "Agotado"},
                {"Guantes · M", "INV-002", "Material de trabajo", "3 unidades", "20 unidades", "Stock bajo"},
                {"Alcohol isopropílico · 1 L", "INV-003", "Higiene", "2 unidades", "8 unidades", "Stock bajo"},
                {"Toallas desechables", "INV-004", "Consumibles", "5 unidades", "15 unidades", "Stock bajo"},
                {"Crema hidratante · 500 ml", "INV-005", "Cuidado facial", "18 unidades", "5 unidades", "Disponible"},
                {"Algodón · 100 piezas", "INV-006", "Consumibles", "32 unidades", "10 unidades", "Disponible"},
        };
        for (Object[] f : filas) {
            String estado = (String) f[5];
            Color col = estado.equals("Agotado") ? Theme.RED : estado.equals("Stock bajo") ? Theme.AMBER : Theme.PRIMARY;
            boolean bajo = !estado.equals("Disponible");
            t.addRow(Ui.twoLine((String) f[0], (String) f[1]), f[2],
                    Ui.label((String) f[3], Font.BOLD, 12, col), f[4], Ui.estado(estado),
                    Ui.action(bajo ? "Reponer →" : "Ver detalle →", null));
        }
        c.add(t, BorderLayout.CENTER);
        c.add(Ui.pager("Mostrando 1-6 de 24 productos", "<", "1", "2", "3", "4", ">"), BorderLayout.SOUTH);
        put(c);
    }
}