package src.view.navigation;

import src.view.utils.IconHelper;

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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void hoverIcon(JButton button, ImageIcon out, ImageIcon hover) {
        button.setIcon(out);
        button.setRolloverIcon(hover);
        button.setRolloverEnabled(true);
    }

    private JPanel createNavigationPanel() {
        JButton homeButton = buildButton(IconHelper.getHomeOnIcon());
        JButton menuButton = buildButton(IconHelper.getMenuOnIcon());
        JButton plannerButton = buildButton(IconHelper.getCalendarOnIcon());
        JButton favoriteButton = buildButton(IconHelper.getFavoriteOnIcon());

        hoverIcon(homeButton, (ImageIcon) homeButton.getIcon(), IconHelper.getHomeOffIcon());
        hoverIcon(menuButton, (ImageIcon) menuButton.getIcon(), IconHelper.getMenuOffIcon());
        hoverIcon(plannerButton, (ImageIcon) plannerButton.getIcon(), IconHelper.getCalendarOffIcon());
        hoverIcon(favoriteButton, (ImageIcon) favoriteButton.getIcon(), IconHelper.getFavoriteOffIcon());

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
