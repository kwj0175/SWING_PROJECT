package src.view;

import src.entity.Recipe;
import src.entity.User;
import src.service.RecipeService;
import src.service.UserService;
import src.infrastructure.FileRecipeRepository;
import src.infrastructure.FileUserRepository;
import src.infrastructure.RecipeRepository;
import src.infrastructure.UserRepository;
import src.view.category.CategoryView;
import src.view.favorite.FavoriteView;
import src.view.home.HomeView;
import src.view.login.LoginView;
import src.view.navigation.BottomNavigation;
import src.view.navigation.NavigationHandler;
import src.view.planner.PlannerView;
import src.view.recipe.RecipeView;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame implements NavigationHandler {
    public static final int DISPLAY_WIDTH = 360;
    public static final int DISPLAY_HEIGHT = 640;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final HomeView homeView;
    private final PlannerView plannerView;
    private final LoginView loginView;
    private final CategoryView categoryView;
    private final RecipeView recipeView;
    private final BottomNavigation bottomNavigation;
    private final FavoriteView favoriteView;

    public MainScreen() {
        setTitle("MySmartRecipe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        UserRepository userRepository = new FileUserRepository("users.txt");
        UserService userService = new UserService(userRepository);

        RecipeRepository recipeRepository = new FileRecipeRepository("datasets/texts");
        RecipeService recipeService = new RecipeService(recipeRepository);

        JPanel stage = new JPanel(new BorderLayout());
        setContentPane(stage);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        stage.add(cardPanel, BorderLayout.CENTER);

        bottomNavigation = new BottomNavigation(this);
        stage.add(bottomNavigation, BorderLayout.SOUTH);

        loginView = new LoginView(this, userService);
        homeView = new HomeView(this, recipeService);
        plannerView = new PlannerView(this, recipeService);
        categoryView = new CategoryView(this, recipeService);
        recipeView = new RecipeView(this);
        favoriteView = new FavoriteView(this, recipeService);
    }

    public void run() {
        cardPanel.add(loginView, "Login");
        cardPanel.add(homeView, "Home");
        cardPanel.add(plannerView, "Planner");
        cardPanel.add(categoryView, "Category");
        cardPanel.add(recipeView, "Recipe");
        cardPanel.add(favoriteView, "Favorite");

        cardLayout.show(cardPanel, "Login");
        setVisible(true);
    }

    public void displayHomeScreen(User loggedInUser) {
        homeView.setCurrentUser(loggedInUser);
        bottomNavigation.showBar();
        cardLayout.show(cardPanel, "Home");
    }

    @Override
    public void displayHomeScreen() {
        cardLayout.show(cardPanel, "Home");
    }

    @Override
    public void displayPlannerScreen() {
        cardLayout.show(cardPanel, "Planner");
    }

    @Override
    public void displayCategoryScreen() {
        cardLayout.show(cardPanel, "Category");
    }

    @Override
    public void displayFavoriteScreen() {
        cardLayout.show(cardPanel, "Favorite");
    }

    public void displayRecipeDetail(Recipe recipe) {
        recipeView.setRecipe(recipe);
        cardLayout.show(cardPanel, "Recipe");
    }

    public void displayPlannerScreenWithRecipe(Recipe recipe) {
        plannerView.startOverlayAddMode(recipe);
        cardLayout.show(cardPanel, "Planner");
    }

    public void refreshFavoriteScreen() {
        favoriteView.refreshFavoriteList();
    }

}