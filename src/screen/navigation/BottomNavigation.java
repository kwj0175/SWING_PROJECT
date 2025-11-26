package src.screen.navigation;

import src.screen.utils.IconHelper;

import javax.swing.*;
import java.awt.*;

public class BottomNavigation extends JPanel{
    private final NavigationHandler handler;
    private final JPanel navigationPanel;

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

    private JButton buildButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    private JPanel createNavigationPanel() {
        JButton homeButton = buildButton(IconHelper.getHomeOnIcon());
        JButton viewMenuButton = buildButton(IconHelper.getMenuOnIcon());
        JButton viewPlannerButton = buildButton(IconHelper.getCalendarOnIcon());
        JButton viewFavoritesButton = buildButton(IconHelper.getFavoriteOnIcon());

        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));

        panel.add(homeButton);
        panel.add(viewMenuButton);
        panel.add(viewPlannerButton);
        panel.add(viewFavoritesButton);

        homeButton.addActionListener(e -> handler.displayHomeScreen());
        viewMenuButton.addActionListener(e -> handler.displayCategoryScreen());
        viewPlannerButton.addActionListener(e -> handler.displayPlannerScreen());
//        viewFavoritesButton.addActionListener(e -> handler.());

        return panel;
    }
}
