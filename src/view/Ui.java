package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public final class Ui {
    private Ui() {}

    public static JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(style, size));
        l.setForeground(color);
        return l;
    }

    public static JPanel transparent(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setOpaque(false);
        return p;
    }

    public static JPanel row(int gap, Component... items) {
        JPanel p = transparent(null);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        for (int i = 0; i < items.length; i++) {
            if (i > 0) p.add(Box.createRigidArea(new Dimension(gap, 0)));
            ((JComponent) items[i]).setAlignmentY(Component.CENTER_ALIGNMENT);
            p.add(items[i]);
        }
        return p;
    }

    public static RoundedPanel card(int pad) {
        RoundedPanel p = new RoundedPanel(16, Theme.CARD, Theme.BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(pad, pad + 2, pad, pad + 2));
        return p;
    }

    public static JPanel center(Component c) {
        JPanel p = transparent(new GridBagLayout());
        p.add(c);
        return p;
    }

    public static String colored(String text, Color c) {
        return "<font color='" + Theme.hex(c) + "'>" + text + "</font>";
    }

    public static void info(Component parent, String msg) {
    }

    public static class RoundedButton extends JButton {
        private final Color bg, hover, border;

        public RoundedButton(String text, Color fg, Color bg, Color hover, Color border) {
            super(text);
            this.bg = bg; this.hover = hover; this.border = border;
            setForeground(fg);
            setFont(Theme.font(Font.BOLD, 12));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setBorder(new EmptyBorder(9, 16, 9, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : bg);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            if (border != null) {
                g2.setColor(border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JButton primary(String text) {
        return new RoundedButton(text, Color.WHITE, Theme.PRIMARY, Theme.PRIMARY_DARK, null);
    }

    public static JButton outline(String text) {
        return new RoundedButton(text, Theme.TEXT, Color.WHITE, Theme.BG, Theme.BORDER);
    }

    public static JButton link(String text) {
        JButton b = new JButton(text);
        b.setFont(Theme.font(Font.BOLD, 11));
        b.setForeground(Theme.PRIMARY);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(null);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        return b;
    }

    public static JButton action(String text, final String message) {
        return link(text);
    }

    public static JButton iconButton(String icon, String tooltip) {
        JButton b = new JButton(Icons.of(icon, 13, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Theme.PRIMARY : new Color(0x3E, 0x5C, 0x52));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setRolloverEnabled(true);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(null);
        b.setToolTipText(tooltip);
        Dimension d = new Dimension(26, 26);
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);
        return b;
    }

    public static class Badge extends JLabel {
        private final Color bg;

        public Badge(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            setForeground(fg);
            setFont(Theme.font(Font.PLAIN, 11));
            setBorder(new EmptyBorder(3, 10, 3, 10));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static Badge badge(String text, Color bg, Color fg) {
        return new Badge(text, bg, fg);
    }

    public static Badge estado(String e) {
        switch (e) {
            case "Confirmada": case "Cobrado": case "Pagado": case "Disponible": case "Activo":
                return new Badge(e, Theme.GREEN_BG, Theme.PRIMARY);
            case "Pendiente": case "Stock bajo": case "Inactivo":
                return new Badge(e, Theme.AMBER_BG, Theme.AMBER);
            default: // Cancelada, Agotado
                return new Badge(e, Theme.RED_BG, Theme.RED);
        }
    }

    public static RoundedPanel statCard(String title, String value, String sub, Color subColor,
                                        String icon, boolean highlight, int valueSize) {
        RoundedPanel p = new RoundedPanel(14, highlight ? Theme.HIGHLIGHT : Theme.CARD,
                highlight ? new Color(0xC9, 0xDD, 0xB3) : Theme.BORDER);
        p.setLayout(new BorderLayout(0, 6));
        p.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel top = transparent(new BorderLayout());
        top.add(label(title, Font.PLAIN, 11, Theme.MUTED), BorderLayout.WEST);
        if (icon != null) top.add(new JLabel(Icons.of(icon, 14, Theme.MUTED)), BorderLayout.EAST);
        p.add(top, BorderLayout.NORTH);

        JPanel body = transparent(new GridLayout(2, 1, 0, 2));
        body.add(label(value, Font.BOLD, valueSize, Theme.TEXT));
        body.add(label(sub, Font.PLAIN, 10, subColor));
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    public static RoundedPanel miniStat(String title, String number, String icon, Color iconColor) {
        RoundedPanel p = new RoundedPanel(14, Theme.CARD, Theme.BORDER);
        p.setLayout(new BorderLayout(0, 4));
        p.setBorder(new EmptyBorder(12, 16, 12, 16));
        p.add(label(title, Font.PLAIN, 11, Theme.MUTED), BorderLayout.NORTH);
        p.add(label(number, Font.BOLD, 26, Theme.TEXT), BorderLayout.WEST);
        p.add(new JLabel(Icons.of(icon, 30, iconColor)), BorderLayout.EAST);
        return p;
    }

    public static JPanel pageHeader(String title, String icon, JComponent actions) {
        JPanel p = transparent(new BorderLayout());
        JLabel t = label(title, Font.BOLD, 24, Theme.TEXT);
        if (icon != null) {
            t.setIcon(Icons.of(icon, 26, Theme.PRIMARY_DARK));
            t.setIconTextGap(10);
        }
        p.add(t, BorderLayout.WEST);
        p.add(actions, BorderLayout.EAST);
        p.setBorder(new EmptyBorder(0, 0, 16, 0));
        return p;
    }

    public static JPanel cardHeader(JComponent left, JComponent right) {
        JPanel p = transparent(new BorderLayout());
        p.add(left, BorderLayout.WEST);
        if (right != null) p.add(right, BorderLayout.EAST);
        return p;
    }

    public static JComponent legend(Color c, String text) {
        JLabel l = label(text, Font.PLAIN, 11, Theme.MUTED);
        l.setIcon(Icons.dot(c, 7));
        l.setIconTextGap(6);
        return l;
    }

    public static JPanel twoLine(String title, String sub) {
        JPanel p = transparent(new GridLayout(2, 1, 0, 1));
        p.add(label(title, Font.BOLD, 12, Theme.TEXT));
        p.add(label(sub, Font.PLAIN, 10, Theme.MUTED));
        return p;
    }

    public static class SearchField extends RoundedPanel {
        public SearchField(final String placeholder, int width) {
            super(12, new Color(0xF1, 0xF3, 0xEF), null);
            setLayout(new BorderLayout(8, 0));
            setBorder(new EmptyBorder(7, 12, 7, 12));
            JTextField tf = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().isEmpty()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setColor(Theme.MUTED);
                        g2.setFont(getFont());
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(placeholder, getInsets().left,
                                (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                        g2.dispose();
                    }
                }
            };
            tf.setBorder(null);
            tf.setOpaque(false);
            tf.setFont(Theme.font(Font.PLAIN, 12));
            tf.setForeground(Theme.TEXT);
            add(new JLabel(Icons.of("search", 14, Theme.MUTED)), BorderLayout.WEST);
            add(tf, BorderLayout.CENTER);
            setPreferredSize(new Dimension(width, 34));
        }
    }

    public static class TabButton extends JToggleButton {
        public TabButton(String text) {
            super(text);
            setFont(Theme.font(Font.PLAIN, 11));
            setForeground(Theme.MUTED);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(new EmptyBorder(5, 12, 5, 12));
            addItemListener(e -> setForeground(isSelected() ? Theme.PRIMARY_DARK : Theme.MUTED));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isSelected()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.HIGHLIGHT);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    public static JPanel tabs(String... names) {
        JPanel p = transparent(new FlowLayout(FlowLayout.LEFT, 6, 0));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < names.length; i++) {
            TabButton b = new TabButton(names[i]);
            group.add(b);
            p.add(b);
            if (i == 0) {
                b.setSelected(true);
                b.setForeground(Theme.PRIMARY_DARK);
            }
        }
        return p;
    }

    public static JPanel pager(String info, String... pages) {
        JPanel p = transparent(new BorderLayout());
        p.add(label(info, Font.PLAIN, 10, Theme.MUTED), BorderLayout.WEST);
        JPanel btns = transparent(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        for (String t : pages) {
            boolean sel = t.equals("1");
            JButton b = new RoundedButton(t, sel ? Theme.PRIMARY_DARK : Theme.MUTED,
                    sel ? Theme.HIGHLIGHT : Color.WHITE, Theme.BG, Theme.BORDER);
            b.setFont(Theme.font(Font.PLAIN, 11));
            b.setBorder(new EmptyBorder(0, 0, 0, 0));
            b.setPreferredSize(new Dimension(30, 28));
            btns.add(b);
        }
        p.add(btns, BorderLayout.EAST);
        return p;
    }

    public static class DataTable extends JPanel {
        private final double[] weights;
        private int nextRow = 1;

        public DataTable(String[] headers, double[] weights) {
            super(new GridBagLayout());
            this.weights = weights;
            setOpaque(false);
            for (int c = 0; c < headers.length; c++) {
                JPanel cell = new JPanel(new GridBagLayout());
                cell.setBackground(new Color(0xF3, 0xF5, 0xF1));
                cell.setBorder(new EmptyBorder(8, 10, 8, 10));
                GridBagConstraints in = new GridBagConstraints();
                in.anchor = GridBagConstraints.WEST;
                in.weightx = 1;
                cell.add(label(headers[c], Font.PLAIN, 10, Theme.MUTED), in);
                add(cell, constraints(c, 0));
            }
        }

        private GridBagConstraints constraints(int col, int row) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridx = col;
            gc.gridy = row;
            gc.weightx = weights[col];
            gc.fill = GridBagConstraints.BOTH;
            return gc;
        }

        public void addRow(Object... cells) {
            for (int c = 0; c < cells.length; c++) {
                Component comp = (cells[c] instanceof Component)
                        ? (Component) cells[c]
                        : label(String.valueOf(cells[c]), Font.PLAIN, 12, Theme.MUTED);
                JPanel cell = transparent(new GridBagLayout());
                cell.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Theme.LINE),
                        new EmptyBorder(11, 10, 11, 10)));
                GridBagConstraints in = new GridBagConstraints();
                in.anchor = GridBagConstraints.WEST;
                in.weightx = 1;
                cell.add(comp, in);
                add(cell, constraints(c, nextRow));
            }
            nextRow++;
        }
    }

    public static class BarChart extends JComponent {
        private final String[] labels, yTicks;
        private final double[] a, b;
        private final double max;

        public BarChart(String[] labels, double[] a, double[] b, double max, String[] yTicks) {
            this.labels = labels; this.a = a; this.b = b; this.max = max; this.yTicks = yTicks;
            setPreferredSize(new Dimension(420, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(Theme.font(Font.PLAIN, 10));
            FontMetrics fm = g2.getFontMetrics();

            int left = 52, right = 8, top = 8, bottom = 26;
            int ph = getHeight() - top - bottom;
            int pw = getWidth() - left - right;

            for (int i = 0; i < yTicks.length; i++) {
                int y = top + ph - (int) Math.round(ph * i / (double) (yTicks.length - 1));
                g2.setColor(Theme.LINE);
                g2.drawLine(left, y, left + pw, y);
                g2.setColor(Theme.MUTED);
                g2.drawString(yTicks[i], left - 8 - fm.stringWidth(yTicks[i]), y + fm.getAscent() / 2 - 1);
            }

            double group = pw / (double) labels.length;
            int barW = (int) Math.min(34, group * 0.28);
            for (int k = 0; k < labels.length; k++) {
                int cx = (int) (left + group * (k + 0.5));
                int ha = (int) Math.round(a[k] / max * ph);
                int hb = (int) Math.round(b[k] / max * ph);
                g2.setColor(Theme.PRIMARY);
                g2.fillRoundRect(cx - barW - 2, top + ph - ha, barW, ha, 5, 5);
                g2.setColor(Theme.BAR_LIGHT);
                g2.fillRoundRect(cx + 2, top + ph - hb, barW, hb, 5, 5);
                g2.setColor(Theme.MUTED);
                g2.drawString(labels[k], cx - fm.stringWidth(labels[k]) / 2, getHeight() - 8);
            }
            g2.dispose();
        }
    }
}