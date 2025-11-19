package screen;

import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import screen.category.CategoryScreen;
import screen.home.HomeScreen;
import screen.login.LoginScreen;
import screen.planner.PlannerScreen;
import screen.recipe.RecipeScreen;
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

    private JPanel navigationPanel;
    private JButton homeButton;
    private JButton viewMenuButton;
    private JButton viewFavoritesButton;
    private JButton viewPlannerButton;

    private final HomeScreen homeScreen;
    private final PlannerScreen plannerScreen;
    private final LoginScreen loginScreen;
    private final CategoryScreen categoryScreen;
    private final RecipeScreen recipeScreen;

    private final RecipeManager recipeManager;

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        recipeManager = new RecipeManager();

        JPanel stage = new JPanel(new BorderLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        stage.add(cardPanel, BorderLayout.CENTER);

        // 하단 네비게이션 바 생성 및 배치 (초기엔 숨김)
        navigationPanel = createNavigationPanel();
        stage.add(navigationPanel, BorderLayout.SOUTH);
        navigationPanel.setVisible(false);

        loginScreen = new LoginScreen(this);
        homeScreen = new HomeScreen(this);
        plannerScreen = new PlannerScreen(this);

        categoryScreen = new CategoryScreen(this, recipeManager.getAllRecipes());
        recipeScreen = new RecipeScreen(this);

        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");
        cardPanel.add(recipeScreen, "Recipe");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
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
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(homeButton);
        panel.add(viewMenuButton);
        panel.add(viewPlannerButton); // (순서는 원하시는대로 배치)
        panel.add(viewFavoritesButton);

        // --- 버튼 리스너 연결 ---
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
    // 로그인 성공 시 호출 (유저 정보 전달 + 홈 화면 이동)
    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        navigationPanel.setVisible(true); // 네비게이션 바 보이기
        cardLayout.show(cardPanel, "Home");
    }

    // '홈' 클릭 시
    public void displayHomeScreen() {
        navigationPanel.setVisible(true);
        cardLayout.show(cardPanel, "Home");
    }

    // '플래너' 클릭 시
    public void displayPlannerScreen() {
        navigationPanel.setVisible(true);
        cardLayout.show(cardPanel, "Planner");
    }

    // '메뉴보기' 클릭 시
    public void displayCategoryScreen() {
        navigationPanel.setVisible(true);
        cardLayout.show(cardPanel, "Category");
    }

    // 5. 로그아웃 시 (네비게이션 바 숨김 + 로그인 화면 이동)
    public void displayLoginScreen() {
        navigationPanel.setVisible(false);
        cardLayout.show(cardPanel, "Login");
    }


    public void displayRecipeDetail(Recipe recipe) {
        recipeScreen.setRecipe(recipe);
        navigationPanel.setVisible(true);
        cardLayout.show(cardPanel, "Recipe");
    }
}