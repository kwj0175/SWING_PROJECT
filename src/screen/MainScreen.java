package src.screen;

import src.entity.Recipe;
import src.entity.User;
import src.manager.RecipeManager;
import src.manager.UserManager;
import src.repository.FileRecipeRepository;
import src.repository.FileUserRepository;
import src.repository.RecipeRepository;
import src.repository.UserRepository;
import src.screen.category.CategoryScreen;
import src.screen.favorite.FavoriteScreen;
import src.screen.home.HomeScreen;
import src.screen.login.LoginScreen;
import src.screen.navigation.BottomNavigation;
import src.screen.navigation.NavigationHandler;
import src.screen.planner.PlannerScreen;
import src.screen.recipe.RecipeScreen;

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
    private final FavoriteScreen favoriteScreen;

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
        favoriteScreen = new FavoriteScreen(this, recipeManager);
    }

    public void run() {
        cardPanel.add(loginScreen, "Login");
        cardPanel.add(homeScreen, "Home");
        cardPanel.add(plannerScreen, "Planner");
        cardPanel.add(categoryScreen, "Category");
        cardPanel.add(recipeScreen, "Recipe");
        cardPanel.add(favoriteScreen, "Favorite");

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

    public void displayFavoriteScreen() {
        cardLayout.show(cardPanel, "Favorite");
    }

}