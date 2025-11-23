package screen.utils;

import javax.swing.*;
import java.awt.*;

public class ScreenHelper {

    private ScreenHelper() {}

    public static JLabel setText(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        return label;
    }

    public static JLabel setText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    public static JButton primaryButton(String text, int size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, size));
        return btn;
    }

    public static JButton secondaryButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(50, 300, 180, 100);
        return button;
    }

    public static JPanel darkCardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        return panel;
    }

    public static JPanel noColorCardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        return panel;
    }

    public static GroupLayout groupLayout(JPanel panel, boolean autoGaps, boolean autoContainerGaps) {
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(autoGaps);
        layout.setAutoCreateContainerGaps(autoContainerGaps);
        return layout;
    }

    public static GroupLayout groupLayout(JPanel panel) {
        return groupLayout(panel, false, true);
    }
}
