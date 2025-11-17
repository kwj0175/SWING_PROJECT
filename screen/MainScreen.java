package screen;

import entity.User;
import screen.category.CategoryScreen;
import screen.home.HomeScreen;
import screen.login.LoginScreen;
import screen.planner.PlannerScreen;
import screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {
    public static final int DISPLAY_WIDTH = 360;
    public static final int DISPLAY_HEIGHT = 640;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private JButton homeButton;
    private JPanel navigationPanel;
    private JButton viewMenuButton;
    private JButton viewFavoritesButton;
    private JButton viewPlannerButton;

    private final HomeScreen homeScreen;
    private final PlannerScreen plannerScreen;
    private final LoginScreen loginScreen;
    private final CategoryScreen categoryScreen;

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel stage = new JPanel(new BorderLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        stage.add(cardPanel, BorderLayout.CENTER);


        navigationPanel = createNavigationPanel();
        stage.add(navigationPanel, BorderLayout.SOUTH);
        navigationPanel.setVisible(false);

        loginScreen = new LoginScreen(this);
        homeScreen = new HomeScreen(this);
        plannerScreen = new PlannerScreen(this);
        categoryScreen = new CategoryScreen();

        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
    }
    private JPanel createNavigationPanel() {
        homeButton = new JButton("홈");
        viewMenuButton = new JButton("메뉴보기");
        viewPlannerButton = new JButton("플래너");
        viewFavoritesButton = new JButton("즐겨찾기");

        //폰트크기 11pt로 조정
        Font buttonFont = new Font("SansSerif", Font.BOLD, 11);
        homeButton.setFont(buttonFont);
        viewMenuButton.setFont(buttonFont);
        viewFavoritesButton.setFont(buttonFont);
        viewPlannerButton.setFont(buttonFont);

        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // (작은 여백)

        panel.add(homeButton);
        panel.add(viewMenuButton);
        panel.add(viewPlannerButton);
        panel.add(viewFavoritesButton);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayHomeScreen();
            }
        });
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCategoryScreen();
            }
        });

        viewPlannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlannerScreen();
            }
        });

        viewFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainScreen.this, "즐겨찾기 화면 (미구현)");
            }
        });

        return panel;
    }

    public void run() {

    }

    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        navigationPanel.setVisible(true); // ❗️네비게이션 바 보이기
        cardLayout.show(cardPanel, "Home");
    }

    public void displayHomeScreen() {
        navigationPanel.setVisible(true); // ❗️(수정) 네비 바 켜기
        cardLayout.show(cardPanel, "Home");
    }

    public void displayPlannerScreen() {
        navigationPanel.setVisible(true); // ❗️(수정) 네비 바 켜기
        cardLayout.show(cardPanel, "Planner");
    }

    public void displayCategoryScreen() {
        navigationPanel.setVisible(true); // ❗️(수정) 네비 바 켜기
        cardLayout.show(cardPanel, "Category");
    }


//    private void setBackgroundIMG(JLayeredPane layeredPane) {
//        // --- 배경 이미지 로드 및 크기 조정 ---
//        ImageIcon foodIcon = new ImageIcon(getClass().getResource("/HomeImage.jpg"));
//        imageLabel = new JLabel();
//        int frameWidth = 800;
//        int frameHeight = 500;
//
//        if (foodIcon.getIconWidth() > 0) {
//            Image scaledImage = foodIcon.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
//            imageLabel.setIcon(new ImageIcon(scaledImage));
//        } else {
//            imageLabel.setText("이미지 로드 실패! /HomeImage.jpg 확인");
//            imageLabel.setOpaque(true);
//            imageLabel.setBackground(Color.WHITE);
//            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        }
//
//        imageLabel.setBounds(0, 0, frameWidth, frameHeight);
//        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
//    }
}
