package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class Page extends JPanel {

    private static class ScrollablePanel extends JPanel implements Scrollable {
        ScrollablePanel(LayoutManager lm) { super(lm); }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 96; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    private final JPanel body = new JPanel();

    protected Page(String breadcrumb, boolean showSearch) {
        super(new BorderLayout());
        setBackground(Theme.BG);
        add(topBar(breadcrumb, showSearch), BorderLayout.NORTH);

        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        ScrollablePanel wrap = new ScrollablePanel(new BorderLayout());
        wrap.setBackground(Theme.BG);
        wrap.setBorder(new EmptyBorder(4, 28, 28, 28));
        wrap.add(body, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(wrap, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.getViewport().setBackground(Theme.BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        add(sp, BorderLayout.CENTER);
    }

    protected void put(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(c);
    }

    protected void gap(int h) {
        body.add(Box.createVerticalStrut(h));
    }

    private JComponent topBar(String crumb, boolean showSearch) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG);
        bar.setBorder(new EmptyBorder(18, 28, 14, 28));

        JLabel bc = new JLabel("<html><span style='color:" + Theme.hex(Theme.MUTED) + "'>Mi negocio / </span><b>"
                + crumb + "</b></html>");
        bc.setFont(Theme.font(Font.PLAIN, 12));
        bc.setForeground(Theme.TEXT);
        bar.add(bc, BorderLayout.WEST);

        JPanel account = Ui.transparent(new GridLayout(2, 1));
        account.add(Ui.label("Administración", Font.BOLD, 12, Theme.TEXT));
        account.add(Ui.label("Mi cuenta", Font.PLAIN, 10, Theme.MUTED));

        JLabel bell = new JLabel(Icons.of("bell-dot", 22, Theme.TEXT));
        JLabel chevron = new JLabel(Icons.of("chevron", 14, Theme.MUTED));

        JComponent right = showSearch
                ? Ui.row(16, new Ui.SearchField("Buscar en tu negocio...", 240), bell, account, chevron)
                : Ui.row(16, bell, account, chevron);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }
}