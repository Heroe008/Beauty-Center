package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Theme {
    private Theme() {}

    public static final Color SIDEBAR      = new Color(0x18, 0x2F, 0x29);
    public static final Color SIDEBAR_SOFT = new Color(0x24, 0x40, 0x38);
    public static final Color NAV_ACTIVE   = new Color(0xD8, 0xEB, 0xA5);
    public static final Color PRIMARY      = new Color(0x2A, 0x7A, 0x58);
    public static final Color PRIMARY_DARK = new Color(0x1F, 0x5F, 0x45);
    public static final Color BAR_LIGHT    = new Color(0xC9, 0xDB, 0xB3);
    public static final Color BG           = new Color(0xF5, 0xF6, 0xF4);
    public static final Color CARD         = Color.WHITE;
    public static final Color BORDER       = new Color(0xDD, 0xE1, 0xDC);
    public static final Color LINE         = new Color(0xEC, 0xEE, 0xEA);
    public static final Color TEXT         = new Color(0x1A, 0x2B, 0x25);
    public static final Color MUTED        = new Color(0x6F, 0x7A, 0x75);
    public static final Color HIGHLIGHT    = new Color(0xE3, 0xEE, 0xD9);
    public static final Color GREEN_BG     = new Color(0xE2, 0xF1, 0xE7);
    public static final Color AMBER        = new Color(0xA0, 0x6A, 0x00);
    public static final Color AMBER_BG     = new Color(0xFD, 0xF0, 0xCF);
    public static final Color RED          = new Color(0xC0, 0x39, 0x2B);
    public static final Color RED_BG       = new Color(0xFB, 0xE4, 0xE1);

    private static final String FAMILY = pickFamily();

    private static String pickFamily() {
        List<String> preferidas = List.of("Inter", "Segoe UI", "SF Pro Text", "Helvetica Neue", "Arial");
        Set<String> disponibles = new HashSet<>(
                List.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String f : preferidas) {
            if (disponibles.contains(f)) {
                return f;
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font font(int style, int size) {
        return new Font(FAMILY, style, size);
    }

    public static String hex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }
}