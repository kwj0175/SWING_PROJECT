package screen.recipe;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final Image backgroundImage;

    public ImagePanel(String imagePath) {
        System.out.println("Image path: " + imagePath);
        ImageIcon icon = new ImageIcon(imagePath);
        backgroundImage = icon.getImage().getScaledInstance(320, 200, Image.SCALE_SMOOTH);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 패널 크기에 맞게 이미지 늘려서 그림
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
