package src.screen.navigation;

import src.screen.utils.IconHelper;

import javax.swing.*;
import java.awt.*;

public class BottomNavigation extends JPanel{
    private final NavigationHandler handler;
    private final JPanel navigationPanel;

    private JButton homeButton;
    private JButton menuButton;
    private JButton plannerButton;
    private JButton favoriteButton;

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

    private void hoverIcon(JButton button, ImageIcon out, ImageIcon hover) {
        button.setIcon(out);
        button.setRolloverIcon(hover);
        button.setRolloverEnabled(true);
    }

    private JPanel createNavigationPanel() {
        homeButton = buildButton(IconHelper.getHomeOnIcon());
        menuButton = buildButton(IconHelper.getMenuOnIcon());
        plannerButton = buildButton(IconHelper.getCalendarOnIcon());
        favoriteButton = buildButton(IconHelper.getFavoriteOnIcon());

        hoverIcon(homeButton, (ImageIcon)homeButton.getIcon(), IconHelper.getHomeOffIcon());
        hoverIcon(menuButton, (ImageIcon)menuButton.getIcon(), IconHelper.getMenuOffIcon());
        hoverIcon(plannerButton, (ImageIcon)plannerButton.getIcon(), IconHelper.getCalendarOffIcon());
        hoverIcon(favoriteButton, (ImageIcon)favoriteButton.getIcon(), IconHelper.getFavoriteOffIcon());

        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));

        panel.add(homeButton);
        panel.add(menuButton);
        panel.add(plannerButton);
        panel.add(favoriteButton);

        homeButton.addActionListener(e -> handler.displayHomeScreen());
        menuButton.addActionListener(e -> handler.displayCategoryScreen());
        plannerButton.addActionListener(e -> handler.displayPlannerScreen());
        favoriteButton.addActionListener(e -> handler.displayFavoriteScreen());

        return panel;
    }
}
