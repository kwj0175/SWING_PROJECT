package screen;

import entity.FoodCategory;
import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import screen.category.CategoryScreen;
import screen.home.HomeScreen;
import screen.login.LoginScreen;
import screen.planner.PlannerScreen;
import screen.recipe.RecipeScreen;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainScreen extends JFrame {
    public static final int DISPLAY_WIDTH = 360;
    public static final int DISPLAY_HEIGHT = 640;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final HomeScreen homeScreen;
    private final PlannerScreen plannerScreen;
    private final LoginScreen loginScreen;
    private final CategoryScreen categoryScreen;
    private final RecipeScreen recipeScreen;
    private final BottomNavigation bottomNavigation;

    private final ArrayList<Recipe> recipes = new ArrayList<>();

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        readFile();

        JPanel stage = new JPanel(new BorderLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        stage.add(cardPanel, BorderLayout.CENTER);

        bottomNavigation = new BottomNavigation(this);
        stage.add(bottomNavigation, BorderLayout.SOUTH);

        loginScreen = new LoginScreen(this);
        homeScreen = new HomeScreen(this, recipes);
        plannerScreen = new PlannerScreen(this);
        categoryScreen = new CategoryScreen(this, recipes);
        recipeScreen = new RecipeScreen(this);

        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");
        cardPanel.add(recipeScreen, "Recipe");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
    }

    // 로그인 성공 시 호출 (유저 정보 전달 + 홈 화면 이동)
    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        bottomNavigation.showBar();
        cardLayout.show(cardPanel, "Home");
    }

    // '홈' 클릭 시
    public void displayHomeScreen() {
        cardLayout.show(cardPanel, "Home");
    }

    // '플래너' 클릭 시
    public void displayPlannerScreen() {
        cardLayout.show(cardPanel, "Planner");
    }

    // '메뉴보기' 클릭 시
    public void displayCategoryScreen() {
        cardLayout.show(cardPanel, "Category");
    }

    // 5. 로그아웃 시 (네비게이션 바 숨김 + 로그인 화면 이동)
    public void displayLoginScreen() {
//        navigationPanel.setVisible(false);
        cardLayout.show(cardPanel, "Login");
    }

    public void displayRecipeDetail(Recipe recipe) {
        recipeScreen.setRecipe(recipe);
        cardLayout.show(cardPanel, "Recipe");
    }

    private void readFile() {
        File dir = new File("datasets/texts");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("txt 파일이 없습니다.");
            return;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            FoodCategory foodCategory = FoodCategory.from(file.getName());
            if (foodCategory == null) {
                System.err.println("FoodCategory 매핑 실패: " + file.getName());
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().isEmpty()) continue;
                    Recipe recipe = parseData(line, foodCategory);
                    if (recipe != null)
                        recipes.add(recipe);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private String resolveImageDir(FoodCategory foodCategory) {
        return switch (foodCategory) {
            case MAIN_SIDE_DISH -> "main_side_dishes";
            case SIDE_DISH -> "side_dishes";
            case SOUP -> "soups";
            case RICE_DISH -> "rice_dishes";
            default -> foodCategory.name().toLowerCase();
        };
    }

    private Recipe parseData(String line, FoodCategory foodCategory) {
        if (line == null) return null;
        String[] items = line.split("\\|");
        if (items.length != 6) {
            System.err.println("잘못된 형식의 라인: " + line);
            return null;
        }

        String name = items[0].trim();
        String title    = items[1].trim();
        List<String> details  = Arrays.stream(items[2].split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        String[] steps = Arrays.stream(items[3].split("/"))
                .map(String::trim)
                .toArray(String[]::new);
        String amount   = items[4].trim();
        String time     = items[5].trim();


        String imgDir = resolveImageDir(foodCategory);
        String imagePath = "datasets/imgs/" + imgDir + "/" + title + ".jpg";

        return new Recipe(
                name,
                title,
                foodCategory,
                details,
                steps,
                amount,
                time,
                imagePath
        );
    }
}