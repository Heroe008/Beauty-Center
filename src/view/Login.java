package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

public class Login extends JFrame {

    private static final Color FONDO       = new Color(0xF4, 0xF8, 0xF5);
    private static final Color CARD_BG     = new Color(0xE3, 0xF3, 0xEC);
    private static final Color LINEA       = new Color(0x8F, 0xB5, 0xA0);
    private static final Color PLACEHOLDER = new Color(0x7B, 0x9E, 0x8D);
    private static final Color VERDE       = new Color(0x1F, 0x6B, 0x4E);
    private static final Color VERDE_OSC   = new Color(0x1A, 0x57, 0x40);
    private static final Color TEXTO       = new Color(0x14, 0x1F, 0x1B);
    private static final Color GRIS        = new Color(0x5E, 0x7A, 0x6D);

    public Login() {
        super("Beauty Center - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(crearContenido());
        setSize(1100, 760);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    private static class Fondo extends JPanel {
        Fondo() {
            super(new BorderLayout());
            setBackground(FONDO);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double w = getWidth(), h = getHeight();

            Path2D clara = new Path2D.Double();
            clara.moveTo(w * 0.172, 0);
            clara.lineTo(w * 0.302, 0);
            clara.lineTo(0, h * 0.66);
            clara.lineTo(0, h * 0.37);
            clara.closePath();
            g2.setColor(VERDE);
            g2.fill(clara);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine((int) (w * 0.302), 0, 0, (int) (h * 0.66));

            Path2D oscura = new Path2D.Double();
            oscura.moveTo(0, 0);
            oscura.lineTo(w * 0.172, 0);
            oscura.lineTo(0, h * 0.37);
            oscura.closePath();
            g2.setColor(VERDE_OSC);
            g2.fill(oscura);
            g2.dispose();
        }
    }

    public static JPanel crearContenido() {
        Fondo root = new Fondo();
        root.add(barraSuperior(), BorderLayout.NORTH);
        root.add(centro(), BorderLayout.CENTER);
        root.add(pie(), BorderLayout.SOUTH);
        return root;
    }

    private static JPanel barraSuperior() {
        JPanel top = transparente(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        top.setBorder(new EmptyBorder(26, 0, 0, 40));

        RoundedPanel badge = new RoundedPanel(26, Color.WHITE, new Color(0x3E, 0x5C, 0x52));
        badge.setLayout(new BorderLayout(10, 0));
        badge.setBorder(new EmptyBorder(8, 10, 8, 20));

        Icon logo = cargarLogo("/beauty_center/icon/logo.png", 24);
        if (logo != null) {
            RoundedPanel cuadro = new RoundedPanel(12, VERDE_OSC, null);
            cuadro.setLayout(new BorderLayout());
            cuadro.setBorder(new EmptyBorder(6, 6, 6, 6));
            cuadro.add(new JLabel(logo), BorderLayout.CENTER);

            JPanel envoltorio = transparente(new GridBagLayout());
            envoltorio.add(cuadro);
            badge.add(envoltorio, BorderLayout.WEST);
        }

        JPanel textos = transparente(new GridLayout(2, 1, 0, 0));
        textos.add(etiqueta("Beauty Center", Font.BOLD, 13, TEXTO));
        textos.add(etiqueta("Gestion & Bienestar", Font.BOLD, 10, VERDE));
        badge.add(textos, BorderLayout.CENTER);

        top.add(badge);
        return top;
    }

    private static JComponent centro() {
        JPanel wrap = transparente(new GridBagLayout());
        JPanel col = transparente(null);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        JLabel titulo = etiqueta("Log in", Font.BOLD, 38, TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        col.add(titulo);
        col.add(Box.createVerticalStrut(4));

        RoundedPanel barra = new RoundedPanel(6, VERDE, null);
        Dimension db = new Dimension(130, 5);
        barra.setPreferredSize(db);
        barra.setMaximumSize(db);
        barra.setAlignmentX(Component.CENTER_ALIGNMENT);
        col.add(barra);
        col.add(Box.createVerticalStrut(10));

        JLabel sub = new JLabel("<html>Bienvenido al panel administrativo de "
                + "<b><font color='#000000'>Beauty Center</font></b></html>");        sub.setFont(fuente(Font.PLAIN, 12));
        sub.setForeground(GRIS);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, sub.getPreferredSize().height));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        col.add(sub);
        col.add(Box.createVerticalStrut(22));

        col.add(tarjeta());
        wrap.add(col);
        return wrap;
    }

    private static JComponent tarjeta() {
        RoundedPanel card = new RoundedPanel(60, CARD_BG, null) {
            @Override public Dimension getPreferredSize() {
                return new Dimension(420, super.getPreferredSize().height);
            }
            @Override public Dimension getMaximumSize() { return getPreferredSize(); }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 44, 30, 44));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(campo("username", false, icono(true)));
        card.add(Box.createVerticalStrut(24));
        card.add(campo("password", true, icono(false)));
        card.add(Box.createVerticalStrut(6));

        JPanel cuenta = transparente(new BorderLayout());
        JLabel txt = new JLabel("<html>you don't have an account? <b><u>Create account.</u></b></html>");
        txt.setFont(fuente(Font.PLAIN, 10));
        txt.setForeground(GRIS);
        cuenta.add(txt, BorderLayout.WEST);
        cuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(cuenta);
        card.add(Box.createVerticalStrut(28));

        JButton login = new JButton("Log in") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_OSC : VERDE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.setColor(new Color(0x0F, 0x2A, 0x20));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        login.setFont(fuente(Font.BOLD, 16));
        login.setForeground(Color.WHITE);
        login.setContentAreaFilled(false);
        login.setBorderPainted(false);
        login.setFocusPainted(false);
        login.setOpaque(false);
        login.setRolloverEnabled(true);
        login.setPreferredSize(new Dimension(130, 40));
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel filaBoton = transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filaBoton.add(login);
        filaBoton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(filaBoton);
        card.add(Box.createVerticalStrut(16));

        JPanel filaAyuda = transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filaAyuda.add(etiqueta("Rellenar acceso  (@beautycenter.com)", Font.BOLD, 10, GRIS));
        filaAyuda.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(filaAyuda);
        return card;
    }

    private static JPanel pie() {
        JPanel p = transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setBorder(new EmptyBorder(0, 0, 30, 0));
        p.add(etiqueta("© 2026 Beauty Center · Sistema de Gestión Estética y Bienestar",
                Font.BOLD, 11, new Color(0x6E, 0x8A, 0x7C)));
        return p;
    }

    private static JComponent campo(String placeholder, boolean clave, Icon icono) {
        JTextField tf = clave ? new CampoClave(placeholder) : new CampoTexto(placeholder);
        tf.setBorder(null);
        tf.setOpaque(false);
        tf.setFont(fuente(Font.BOLD, 17));
        tf.setForeground(TEXTO);
        tf.setCaretColor(TEXTO);

        JPanel fila = transparente(new BorderLayout(8, 0));
        fila.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, LINEA), new EmptyBorder(0, 2, 6, 2)));
        fila.add(tf, BorderLayout.CENTER);
        fila.add(new JLabel(icono), BorderLayout.EAST);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fila;
    }

    private static void dibujarPlaceholder(JTextComponent c, Graphics g, String texto) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(PLACEHOLDER);
        g2.setFont(c.getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, c.getInsets().left, (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }

    private static class CampoTexto extends JTextField {
        private final String ph;
        CampoTexto(String ph) { this.ph = ph; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) dibujarPlaceholder(this, g, ph);
        }
    }

    private static class CampoClave extends JPasswordField {
        private final String ph;
        CampoClave(String ph) { this.ph = ph; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0) dibujarPlaceholder(this, g, ph);
        }
    }

    private static Icon icono(final boolean usuario) {
        return new Icon() {
            @Override public int getIconWidth() { return 18; }
            @Override public int getIconHeight() { return 18; }
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXTO);
                if (usuario) {
                    g2.fillOval(x, y, 18, 18);
                    g2.setColor(CARD_BG);
                    g2.fillOval(x + 6, y + 3, 6, 6);
                    g2.fill(new Arc2D.Float(x + 4, y + 10, 10, 10, 0, 180, Arc2D.PIE));
                } else {
                    g2.setStroke(new BasicStroke(2f));
                    g2.draw(new Arc2D.Float(x + 4, y + 1, 10, 10, 0, 180, Arc2D.OPEN));
                    g2.drawLine(x + 4, y + 6, x + 4, y + 9);
                    g2.drawLine(x + 14, y + 6, x + 14, y + 9);
                    g2.fillRoundRect(x + 2, y + 8, 14, 10, 4, 4);
                    g2.setColor(CARD_BG);
                    g2.fillOval(x + 8, y + 11, 3, 3);
                }
                g2.dispose();
            }
        };
    }

    private static Icon cargarLogo(String ruta, int alto) {
        java.net.URL url = Login.class.getResource(ruta);
        if (url == null) return null;
        ImageIcon original = new ImageIcon(url);
        int ancho = original.getIconWidth() * alto / original.getIconHeight();
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    private static Font fuente(int estilo, int tam) { return Theme.font(estilo, tam); }

    private static JLabel etiqueta(String texto, int estilo, int tam, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(estilo, tam));
        l.setForeground(color);
        return l;
    }

    private static JPanel transparente(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setOpaque(false);
        return p;
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
