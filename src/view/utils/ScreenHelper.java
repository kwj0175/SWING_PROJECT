package src.view.utils;

import javax.swing.*;
import java.awt.*;

public class ScreenHelper {

    private static final Color borderColor = UIManager.getColor("Panel.borderColor");

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

    public static JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.decode("#CCE2CB"));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        return panel;
    }

    public static JPanel noColorCardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.decode("#F9FFFD"));
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
