package screen;

import entity.User;
import screen.category.CategoryScreen;
import screen.home.HomeScreen;
import screen.login.LoginScreen;
import screen.planner.PlannerScreen;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    public static final int DISPLAY_WIDTH = 360;
    public static final int DISPLAY_HEIGHT = 640;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final HomeScreen homeScreen;
    private final PlannerScreen plannerScreen;
    private final LoginScreen loginScreen;
    private final CategoryScreen categoryScreen;

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //일단 창닫으면 종료
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel stage = new JPanel(new GridBagLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
//        c.weightx = c.weighty = 1.0;
//        c.fill = GridBagConstraints.BOTH;
        stage.add(cardPanel, c);

        loginScreen = new LoginScreen(this);
        homeScreen = new HomeScreen(this);
        plannerScreen = new PlannerScreen();
        categoryScreen = new CategoryScreen();

        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
    }

    public void run() {

    }

    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        cardLayout.show(cardPanel, "Home");
    }

    public void displayPlannerScreen() {
        cardLayout.show(cardPanel, "Planner");
    }

    public void displayCategoryScreen() {
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
