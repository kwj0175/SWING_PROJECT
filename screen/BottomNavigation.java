package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BottomNavigation extends JPanel{
    private final MainScreen mainScreen;
    private final JPanel navigationPanel;

    private JButton homeButton;
    private JButton viewMenuButton;
    private JButton viewFavoritesButton;
    private JButton viewPlannerButton;

    public BottomNavigation(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
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

        // --- 버튼 리스너 연결 ---
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.displayHomeScreen();
            }
        });

        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.displayCategoryScreen();
            }
        });

        viewPlannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.displayPlannerScreen();
            }
        });

        viewFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainScreen, "즐겨찾기 화면 (미구현)");
            }
        });

        return panel;
    }
}
