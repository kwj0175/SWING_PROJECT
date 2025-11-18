package screen;

import entity.FoodCategory;
import entity.User;
import entity.Recipe;  
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

    private final ArrayList<Recipe> recipes = new ArrayList<>();

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //일단 창닫으면 종료
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        readFile();

        JPanel stage = new JPanel(new GridBagLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        stage.add(cardPanel, c);

        loginScreen = new LoginScreen(this);
        homeScreen = new HomeScreen(this);
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

    public void run() {

    }

    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        cardLayout.show(cardPanel, "Home");
    }

    public void displayHomeScreen() {
        cardLayout.show(cardPanel, "Home");
    }

    public void displayPlannerScreen() {
        cardLayout.show(cardPanel, "Planner");
    }

    public void displayCategoryScreen() {
        cardLayout.show(cardPanel, "Category");
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
        switch (foodCategory) {
            case MAIN_SIDE_DISH:  // 메인반찬
                return "main_side_dishes";
            case SIDE_DISH:       // 밑반찬
                return "side_dishes";
            case SOUP:            // 국/탕/찌개 묶어서 쓴다면
                return "soups";
            case RICE_DISH:       // 밥/죽/떡
                return "rice_dishes";
            default:
                // 디폴트는 그냥 category 이름 쓰거나, 에러 로그만 남기고 null
                return foodCategory.name().toLowerCase();
        }
    }

    private Recipe parseData(String line, FoodCategory foodCategory) {
        if (line == null) return null;
        String[] items = line.split("\\|");
        if (items.length != 6) {
            // 로그만 찍고 버리든지
            System.err.println("잘못된 형식의 라인: " + line);
            return null;
        }

        String name = items[0].trim();
        String title    = items[1].trim();
        String details  = items[2].trim();
        String steps = items[3].trim();
        String amount   = items[4].trim();
        String time     = items[5].trim();


        String imgDir = resolveImageDir(foodCategory);
        String imagePath = "datasets/imgs/" + imgDir + "/" + title + ".jpg";

        return new Recipe(
                name,
                title,
                foodCategory,
                details.split(","),
                steps.split("/"),
                amount,
                time,
                imagePath
        );
    }
}
