package screen.home;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ProfilePanel extends JComponent {
    private final Image image;
    private final int diameter;

    public ProfilePanel(int diameter) {
        this(null, diameter);
    }

    public ProfilePanel(Image image, int diameter) {
        this.image = image;
        this.diameter = diameter;
        setOpaque(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(diameter, diameter);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(x, y, diameter, diameter);

        if (image != null) {
            Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);
            g2.setClip(circle);
            g2.drawImage(image, x, y, diameter, diameter, null);
            g2.setClip(null);
        }

        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(x, y, diameter, diameter);

        g2.dispose();
    }
}
