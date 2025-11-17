package screen.home;

import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import screen.MainScreen;
import screen.RecipeDetailScreen;
import screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class HomeScreen extends JPanel {

    private User currentUser;
    private RecipeManager recipeManager;
    private List<Recipe> recommendedRecipes;
    private MainScreen mainScreen;

    private JLabel welcomeLabel;

    public HomeScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        currentUser = null;
        setOpaque(false);
        welcomeLabel = ScreenHelper.setText(" ", 18);

        setLayout(new BorderLayout());

        this.recipeManager = new RecipeManager();
        this.recommendedRecipes = recipeManager.getRecommendationsPerCategory();

        JPanel form = buildForm();
        add(form, BorderLayout.NORTH);
    }

    private JPanel buildForm() {
        JPanel infoPanel = infoPanel();
        JPanel recommendPanel = recommendPanel();

        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BorderLayout(0, 20));

        root.add(infoPanel, BorderLayout.NORTH);

        root.add(recommendPanel, BorderLayout.CENTER);

        return root;
    }

    private JPanel infoPanel() {
        JComponent profile = new ProfilePanel(50);

        JPanel infoPanel = ScreenHelper.noColorCardPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GroupLayout formLayout = ScreenHelper.groupLayout(infoPanel);

        int HORIZONTAL_GAP = 30;

        formLayout.setHorizontalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(profile,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(HORIZONTAL_GAP)
                        .addComponent(welcomeLabel)
        );

        formLayout.setVerticalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(profile)
                        .addComponent(welcomeLabel)
        );
        return infoPanel;
    }

    private JLabel createRecommendLabel(int index) {
        String labelText = "추천 메뉴 없음";
        Recipe recipe = null;

        if (recommendedRecipes != null && index < recommendedRecipes.size()) {
            recipe = recommendedRecipes.get(index);
            labelText = String.format("[%s] %s", recipe.getCategory(), recipe.getName());
        }

        JLabel recipeLabel = ScreenHelper.setText(labelText, 14);
        recipeLabel.setForeground(Color.BLUE);
        recipeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (recipe != null) {
            final Recipe finalRecipe = recipe;

            recipeLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    RecipeDetailScreen detailScreen = new RecipeDetailScreen(mainScreen, finalRecipe);
                    detailScreen.setVisible(true);
                }
            });
        }
        return recipeLabel;
    }

    private JPanel recommendPanel() {
        JLabel recommendTitle = ScreenHelper.setText("오늘의 추천 메뉴", 16);

        JLabel recipeLabel1 = createRecommendLabel(0);
        JLabel recipeLabel2 = createRecommendLabel(1);
        JLabel recipeLabel3 = createRecommendLabel(2);
        JLabel recipeLabel4 = createRecommendLabel(3);

        int TITLE_GAP = 30;
        int RECIPE_GAP = 10;

        JPanel recommendPanel = ScreenHelper.darkCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(recommendPanel);

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(recommendTitle)
                        .addComponent(recipeLabel1)
                        .addComponent(recipeLabel2)
                        .addComponent(recipeLabel3)
                        .addComponent(recipeLabel4)
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(recommendTitle)
                        .addGap(TITLE_GAP)
                        .addComponent(recipeLabel1)
                        .addGap(RECIPE_GAP)
                        .addComponent(recipeLabel2)
                        .addGap(RECIPE_GAP)
                        .addComponent(recipeLabel3)
                        .addGap(RECIPE_GAP)
                        .addComponent(recipeLabel4)
        );
        return recommendPanel;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        welcomeLabel.setText(currentUser.getName() + "님, 환영합니다.");
        revalidate();
        repaint();
    }
}