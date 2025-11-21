package src.screen.navigation;

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

    private JPanel createNavigationPanel() {
        homeButton = new JButton("홈");
        viewMenuButton = new JButton("메뉴보기");
        viewFavoritesButton = new JButton("즐겨찾기");
        viewPlannerButton = new JButton("플래너");

        Font buttonFont = new Font("SansSerif", Font.BOLD, 11);
        homeButton.setFont(buttonFont);
        viewMenuButton.setFont(buttonFont);
        viewFavoritesButton.setFont(buttonFont);
        viewPlannerButton.setFont(buttonFont);

        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));
        panel.setBackground(Color.LIGHT_GRAY);

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
