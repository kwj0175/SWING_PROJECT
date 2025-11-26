package src.screen.home;

import src.entity.Recipe;
import src.entity.User;
import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.utils.IconHelper;
import src.screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends JPanel {
    private final MainScreen mainScreen;
    private final RecipeManager recipeManager;
    private final HomePresenter homePresenter;

    private User currentUser;
    private List<Recipe> recommendedRecipes;
    private JLabel welcomeLabel;
    private JPanel recommendListPanel;

    public HomeScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        this.mainScreen = mainScreen;
        this.recipeManager = recipeManager;
        this.homePresenter = new HomePresenter(this, recipeManager);
        this.recommendedRecipes = new ArrayList<>();

        currentUser = null;
        setOpaque(false);
        welcomeLabel = ScreenHelper.setText(" ", 16);

        setLayout(new BorderLayout());

        JPanel form = buildForm();
        add(form, BorderLayout.CENTER);
        homePresenter.loadInitialRecommendations();
    }

    public void showRecommendations(List<Recipe> recipes) {
        this.recommendedRecipes = recipes;
        updateRecommendList();
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private JPanel buildForm() {
        JPanel infoPanel = createInfoPanel();
        JPanel recommendPanel = createRecommendPanel();

        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BorderLayout(0, 25));

        root.add(infoPanel, BorderLayout.NORTH);
        root.add(recommendPanel, BorderLayout.CENTER);

        return root;
    }

    private JPanel createInfoPanel() {
        JLabel fridgeBtn = new JLabel("");
        ImageIcon icon = IconHelper.getFridgeOnIcon();

        fridgeBtn.setIcon(icon);

        fridgeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fridgeBtn.setToolTipText("냉장고 재료 입력하기");

        JPanel infoPanel = ScreenHelper.noColorCardPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0));

        infoPanel.add(fridgeBtn);
        infoPanel.add(welcomeLabel);

        fridgeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new IngredientInputDialog(HomeScreen.this, homePresenter::onIngredientsSubmitted).open();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fridgeBtn.setIcon(IconHelper.getFridgeOffIcon());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fridgeBtn.setIcon(IconHelper.getFridgeOnIcon());
            }
        });

        return infoPanel;
    }

    private JPanel createRecommendPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JLabel title = ScreenHelper.setText("오늘의 추천 메뉴", 16);
        title.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        recommendListPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        recommendListPanel.setOpaque(false);

        updateRecommendList();

        container.add(title, BorderLayout.NORTH);
        container.add(recommendListPanel, BorderLayout.CENTER);

        return container;
    }

    private void updateRecommendList() {
        recommendListPanel.removeAll();

        if (recommendedRecipes != null) {
            for (Recipe r : recommendedRecipes) {
                RecipeCardPanel card = new RecipeCardPanel(r, mainScreen::displayRecipeDetail);
                recommendListPanel.add(card);
            }
        }

        for (int i = recommendListPanel.getComponentCount(); i < 4; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            recommendListPanel.add(empty);
        }

        recommendListPanel.revalidate();
        recommendListPanel.repaint();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        String userName = (currentUser != null) ? currentUser.getName() : "사용자";
        welcomeLabel.setText("<html><b>" + userName + "</b>님,<br>오늘도 맛있는 하루 되세요!</html>");
        revalidate();
        repaint();
    }
}