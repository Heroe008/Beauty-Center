package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private static class NavItem extends JPanel {
        private final JLabel text;
        private final JLabel badge;
        private final String icon;
        private boolean active, hover;

        NavItem(String title, String icon, String badgeText, final Runnable onClick) {
            super(new BorderLayout(10, 0));
            this.icon = icon;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 12, 10, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            text = Ui.label(title, Font.PLAIN, 13, new Color(0xD5, 0xE2, 0xDC));
            text.setIcon(Icons.of(icon, 16, new Color(0xD5, 0xE2, 0xDC)));
            text.setIconTextGap(10);
            add(text, BorderLayout.CENTER);

            badge = Ui.label(badgeText == null ? "" : badgeText, Font.BOLD, 10, Theme.NAV_ACTIVE);
            add(badge, BorderLayout.EAST);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        void setActive(boolean a) {
            active = a;
            Color fg = a ? Theme.SIDEBAR : new Color(0xD5, 0xE2, 0xDC);
            text.setForeground(fg);
            text.setFont(Theme.font(a ? Font.BOLD : Font.PLAIN, 13));
            text.setIcon(Icons.of(icon, 16, fg));
            badge.setForeground(a ? Theme.PRIMARY_DARK : Theme.NAV_ACTIVE);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (active || hover) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(active ? Theme.NAV_ACTIVE : Theme.SIDEBAR_SOFT);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, NavItem> nav = new LinkedHashMap<>();

    public MainFrame() {
        super("Beauty Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        

        add(buildSidebar(), BorderLayout.WEST);

        content.add(new ResumenPage(), "Resumen");
        content.add(new CitasPage(), "Citas");
        content.add(new FinanzasPage(), "Finanzas");
        content.add(new InventarioPage(), "Inventario");
        content.add(new ClientesPage(), "Clientes");
        add(content, BorderLayout.CENTER);

        show("Resumen");
        setMinimumSize(new Dimension(1100, 680));
        setSize(1360, 860);
        setLocationRelativeTo(null);
    }

    private void show(String key) {
        cards.show(content, key);
        for (Map.Entry<String, NavItem> e : nav.entrySet()) {
            e.getValue().setActive(e.getKey().equals(key));
        }
    }
    private static Icon cargarLogo(String ruta, int alto) {
        java.net.URL url = MainFrame.class.getResource(ruta);
        if (url == null) {
            return null; 
        }
        ImageIcon original = new ImageIcon(url);
        int ancho = original.getIconWidth() * alto / original.getIconHeight();
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
    private JComponent buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.SIDEBAR);
        side.setBorder(new EmptyBorder(22, 12, 16, 12));
        side.setPreferredSize(new Dimension(236, 0));

        JLabel logo = new JLabel("<html><div style='line-height:1.05'>Beauty<br>Center</div></html>",
                cargarLogo("/beauty_center/icon/logo.png", 90), SwingConstants.LEFT);
        logo.setFont(Theme.font(Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setIconTextGap(2);
        logo.setBorder(new EmptyBorder(0, 6, 0, 0));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);
        side.add(Box.createVerticalStrut(22));

        RoundedPanel negocio = new RoundedPanel(12, Theme.SIDEBAR_SOFT, null);
        negocio.setLayout(new BorderLayout());
        negocio.setBorder(new EmptyBorder(11, 12, 11, 12));
        negocio.add(Ui.label("Mi negocio", Font.PLAIN, 12, Color.WHITE), BorderLayout.WEST);
        negocio.add(new JLabel(Icons.of("chevron", 14, new Color(0xB5, 0xC8, 0xBF))), BorderLayout.EAST); 
        negocio.setAlignmentX(Component.LEFT_ALIGNMENT);
        negocio.setMaximumSize(new Dimension(Integer.MAX_VALUE, negocio.getPreferredSize().height));
        side.add(negocio);
        side.add(Box.createVerticalStrut(20));

        JPanel opciones = new JPanel();
        opciones.setLayout(new BoxLayout(opciones, BoxLayout.Y_AXIS));
        opciones.setOpaque(false);
        opciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        opciones.setVisible(true); 

        JLabel principal = Ui.label("PRINCIPAL", Font.PLAIN, 10, new Color(0x8F, 0xA6, 0x9C));
        principal.setBorder(new EmptyBorder(0, 6, 0, 0));
        principal.setAlignmentX(Component.LEFT_ALIGNMENT);
        opciones.add(principal);
        opciones.add(Box.createVerticalStrut(8));

        addNav(opciones, "Resumen", "grid", null);
        addNav(opciones, "Citas", "calendar", "08");
        addNav(opciones, "Finanzas", "chart", null);
        addNav(opciones, "Inventario", "box", "04");
        addNav(opciones, "Clientes", "users", null);

        side.add(opciones);
        side.add(Box.createVerticalGlue());

        negocio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        negocio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                opciones.setVisible(!opciones.isVisible());
                side.revalidate();
                side.repaint();
            }
        });

        return side;}

    private void addNav(JPanel side, final String key, String icon, String badge) {
        NavItem item = new NavItem(key, icon, badge, () -> show(key));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
        nav.put(key, item);
        side.add(item);
        side.add(Box.createVerticalStrut(4));
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
