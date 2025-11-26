package screen;

import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import manager.UserManager;
import repository.FileRecipeRepository;
import repository.FileUserRepository;
import repository.RecipeRepository;
import repository.UserRepository;
import screen.category.CategoryScreen;
import screen.home.HomeScreen;
import screen.login.LoginScreen;
import screen.navigation.BottomNavigation;
import screen.navigation.NavigationHandler;
import screen.planner.PlannerScreen;
import screen.recipe.RecipeScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainScreen extends JFrame implements NavigationHandler {
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

        UserRepository userRepository = new FileUserRepository("users.txt");
        UserManager userManager = new UserManager(userRepository);

        RecipeRepository recipeRepository = new FileRecipeRepository("datasets/texts");
        RecipeManager recipeManager = new RecipeManager(recipeRepository);

        JPanel stage = new JPanel(new BorderLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        stage.add(cardPanel, BorderLayout.CENTER);

        bottomNavigation = new BottomNavigation(this);
        stage.add(bottomNavigation, BorderLayout.SOUTH);

        loginScreen = new LoginScreen(this, userManager);
        homeScreen = new HomeScreen(this, recipeManager);
        plannerScreen = new PlannerScreen(this, recipeManager);
        categoryScreen = new CategoryScreen(this, recipeManager);
        recipeScreen = new RecipeScreen(this);
    }

    public void run() {
        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");
        cardPanel.add(recipeScreen, "Recipe");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
    }

    public void displayHomeScreen(User loggedInUser) {
        homeScreen.setCurrentUser(loggedInUser);
        bottomNavigation.showBar();
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

    public void displayPlannerScreenWithRecipe(Recipe recipe) {
        plannerScreen.enterRecipeAddMode(recipe);
        cardLayout.show(cardPanel, "Planner");
    }

}