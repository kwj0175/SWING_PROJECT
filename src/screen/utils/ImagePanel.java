package src.screen.utils;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final Image backgroundImage;

    public ImagePanel(String imagePath) {
//        System.out.println("Image path: " + imagePath);
        ImageIcon icon = new ImageIcon(imagePath);
        backgroundImage = icon.getImage().getScaledInstance(320, 200, Image.SCALE_SMOOTH);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
