package src.screen.navigation;

import src.screen.utils.IconHelper;

import javax.swing.*;
import java.awt.*;

public class BottomNavigation extends JPanel{
    private final NavigationHandler handler;
    private final JPanel navigationPanel;

    private JButton homeButton;
    private JButton viewMenuButton;
    private JButton viewFavoritesButton;
    private JButton viewPlannerButton;

    public BottomNavigation(NavigationHandler handler) {
        this.handler = handler;
        setLayout(new BorderLayout());

        navigationPanel = createNavigationPanel();
        navigationPanel.setVisible(false);
        navigationPanel.setPreferredSize(new Dimension(0, 50));

        add(navigationPanel, BorderLayout.CENTER);
    }

    public void showBar() {
        navigationPanel.setVisible(true);
    }

    private JButton customizeButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    private JButton buildButton(ImageIcon icon) {
        if (icon != null && icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(32, 32));
        return button;
    }

    private JPanel createNavigationPanel() {
        homeButton = buildButton(IconHelper.getHomeIcon());
        viewMenuButton = buildButton(IconHelper.getMenuIcon());
        viewPlannerButton = buildButton(IconHelper.getCalendarIcon());
        viewFavoritesButton = buildButton(IconHelper.getFavorite());

        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));

        panel.add(customizeButton(homeButton));
        panel.add(customizeButton(viewMenuButton));
        panel.add(customizeButton(viewPlannerButton));
        panel.add(customizeButton(viewFavoritesButton));

        homeButton.addActionListener(e -> handler.displayHomeScreen());
        viewMenuButton.addActionListener(e -> handler.displayCategoryScreen());
        viewPlannerButton.addActionListener(e -> handler.displayPlannerScreen());
//        viewFavoritesButton.addActionListener(e -> handler.());

        return panel;
    }
}
