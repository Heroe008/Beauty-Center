package view;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public final class Icons {
    private Icons() {}

    public static Icon of(final String name, final int size, final Color color) {
        return new Icon() {
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                g2.translate(x, y);
                g2.scale(size / 24.0, size / 24.0);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                draw(g2, name);
                g2.dispose();
            }
        };
    }

    public static Icon dot(final Color color, final int size) {
        return new Icon() {
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(x, y, size, size);
                g2.dispose();
            }
        };
    }

    private static void user(Graphics2D g) {
        g.draw(new Ellipse2D.Float(5, 3, 8, 8));
        g.draw(new Arc2D.Float(1, 14, 16, 16, 0, 180, Arc2D.OPEN));
    }

    private static void draw(Graphics2D g, String name) {
        switch (name) {
            case "grid":
                g.draw(new RoundRectangle2D.Float(3, 3, 7, 7, 2, 2));
                g.draw(new RoundRectangle2D.Float(14, 3, 7, 7, 2, 2));
                g.draw(new RoundRectangle2D.Float(3, 14, 7, 7, 2, 2));
                g.draw(new RoundRectangle2D.Float(14, 14, 7, 7, 2, 2));
                break;
            case "calendar":
                g.draw(new RoundRectangle2D.Float(3, 5, 18, 16, 4, 4));
                g.drawLine(3, 10, 21, 10);
                g.drawLine(8, 3, 8, 7);
                g.drawLine(16, 3, 16, 7);
                break;
            case "chart":
                g.drawLine(3, 3, 3, 21);
                g.drawLine(3, 21, 21, 21);
                g.drawPolyline(new int[]{7, 11, 15, 20}, new int[]{15, 11, 13, 6}, 4);
                break;
            case "box": {
                Path2D p = new Path2D.Float();
                p.moveTo(12, 3); p.lineTo(20, 7); p.lineTo(20, 17);
                p.lineTo(12, 21); p.lineTo(4, 17); p.lineTo(4, 7); p.closePath();
                g.draw(p);
                g.drawLine(4, 7, 12, 11);
                g.drawLine(20, 7, 12, 11);
                g.drawLine(12, 11, 12, 21);
                break;
            }
            case "users":
                g.draw(new Ellipse2D.Float(4, 4, 8, 8));
                g.draw(new Arc2D.Float(1, 15, 14, 14, 0, 180, Arc2D.OPEN));
                g.draw(new Arc2D.Float(13, 4, 7, 7, -90, 180, Arc2D.OPEN));
                g.draw(new Arc2D.Float(15, 15, 8, 8, 0, 100, Arc2D.OPEN));
                break;
            case "user":
                user(g);
                break;
            case "user-x":
                user(g);
                g.drawLine(17, 8, 22, 13);
                g.drawLine(22, 8, 17, 13);
                break;
            case "user-check": {
                user(g);
                Path2D p = new Path2D.Float();
                p.moveTo(17, 11); p.lineTo(19.5f, 13.5f); p.lineTo(23, 8.5f);
                g.draw(p);
                break;
            }
            case "user-minus":
                user(g);
                g.drawLine(17, 11, 23, 11);
                break;
            case "search":
                g.draw(new Ellipse2D.Float(3, 3, 13, 13));
                g.drawLine(14, 14, 21, 21);
                break;
            case "bell":
            case "bell-dot":
                g.draw(new Arc2D.Float(6, 4, 12, 12, 0, 180, Arc2D.OPEN));
                g.drawLine(6, 10, 6, 17);
                g.drawLine(18, 10, 18, 17);
                g.drawLine(4, 17, 20, 17);
                g.drawLine(10, 21, 14, 21);
                if (name.equals("bell-dot")) {
                    g.setColor(Theme.RED);
                    g.fill(new RoundRectangle2D.Float(14, 0, 9, 9, 3, 3));
                }
                break;
            case "eye":
                g.draw(new Ellipse2D.Float(2, 6, 20, 12));
                g.fill(new Ellipse2D.Float(9, 9, 6, 6));
                break;
            case "edit":
                g.drawPolygon(new int[]{3, 6, 17, 21, 10}, new int[]{21, 14, 3, 7, 18}, 5);
                break;
            case "trash":
                g.drawLine(4, 6, 20, 6);
                g.draw(new RoundRectangle2D.Float(6, 6, 12, 15, 3, 3));
                g.drawLine(9, 3, 15, 3);
                g.drawLine(10, 10, 10, 17);
                g.drawLine(14, 10, 14, 17);
                break;
            case "wallet":
                g.draw(new RoundRectangle2D.Float(3, 6, 18, 14, 4, 4));
                g.drawLine(3, 10, 21, 10);
                g.fill(new Ellipse2D.Float(15, 13, 3, 3));
                break;
            case "arrow-up-right":
                g.drawLine(7, 17, 17, 7);
                g.drawPolyline(new int[]{8, 17, 17}, new int[]{7, 7, 16}, 3);
                break;
            case "chevron":
                g.drawPolyline(new int[]{6, 12, 18}, new int[]{9, 15, 9}, 3);
                break;
            case "star": {
                Path2D p = new Path2D.Double();
                for (int i = 0; i < 10; i++) {
                    double r = (i % 2 == 0) ? 11 : 4.8;
                    double a = -Math.PI / 2 + i * Math.PI / 5;
                    double px = 12 + r * Math.cos(a), py = 12 + r * Math.sin(a);
                    if (i == 0) p.moveTo(px, py); else p.lineTo(px, py);
                }
                p.closePath();
                g.fill(p);
                break;
            }
            default:
                break;
        }
    }
}
