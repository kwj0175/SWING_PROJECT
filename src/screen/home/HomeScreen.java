package screen.home;

import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import screen.MainScreen;
import screen.recipe.ImagePanel;
import screen.utils.ScreenHelper;

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
        welcomeLabel = ScreenHelper.setText(" ", 18);

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
        JPanel infoPanel = infoPanel();
        JPanel recommendPanel = recommendPanel();

        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BorderLayout(0, 15));

        root.add(infoPanel, BorderLayout.NORTH);
        root.add(recommendPanel, BorderLayout.CENTER);

        return root;
    }

    private JPanel infoPanel() {
        JLabel fridgeBtn = new JLabel("🧊");
        ImageIcon icon = new ImageIcon("src/fridge.png");
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            fridgeBtn.setIcon(new ImageIcon(img));
            fridgeBtn.setText("");
        } else {
            fridgeBtn.setFont(new Font("SansSerif", Font.PLAIN, 40));
        }

        fridgeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fridgeBtn.setToolTipText("냉장고 재료 입력하기");

        fridgeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showIngredientDialog();
            }
        });

        JPanel infoPanel = ScreenHelper.noColorCardPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.setLayout(new BorderLayout(20, 0));

        infoPanel.add(fridgeBtn, BorderLayout.WEST);
        infoPanel.add(welcomeLabel, BorderLayout.CENTER);

        return infoPanel;
    }

    private void showIngredientDialog() {
        String input = JOptionPane.showInputDialog(this,
                "냉장고 재료를 입력하세요 (쉼표 구분)\n예: 두부, 계란, 대파",
                "재료 기반 추천",
                JOptionPane.QUESTION_MESSAGE);

        if (input != null) {
            homePresenter.onIngredientsSubmitted(input);
        }
    }

    private JPanel recommendPanel() {
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
                recommendListPanel.add(createRecipeCard(r));
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

    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Component imgComp;
        String path = recipe.getImagePath();

        if (path != null) {
            ImagePanel imgPanel = new ImagePanel(path);
            imgPanel.setPreferredSize(new Dimension(110, 0));
            imgComp = imgPanel;
        } else {
            JLabel noImg = new JLabel("🍽️");
            noImg.setFont(new Font("SansSerif", Font.PLAIN, 30));
            noImg.setHorizontalAlignment(SwingConstants.CENTER);
            noImg.setPreferredSize(new Dimension(110, 0));
            imgComp = noImg;
        }

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        textPanel.setOpaque(false);

        String catName = (recipe.getCategory() != null) ? recipe.getCategory().getDisplayName() : "기타";
        JLabel nameLabel = new JLabel("<html><b>[" + catName + "]</b><br>" + recipe.getName() + "</html>");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        String infoText = recipe.getAmount() + " | " + recipe.getTime();
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        textPanel.add(nameLabel);
        textPanel.add(infoLabel);

        card.add(imgComp, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainScreen.displayRecipeDetail(recipe);
            }
        });

        return card;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        welcomeLabel.setText("<html><b>" + currentUser.getName() + "</b>님,<br>오늘도 맛있는 하루 되세요!</html>");
        revalidate();
        repaint();
    }
}