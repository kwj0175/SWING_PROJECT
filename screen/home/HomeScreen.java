package screen.home;

import entity.Recipe;
import entity.User;
import manager.RecipeManager;
import screen.MainScreen;
import screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.Normalizer; // 한글 자모음 합치기용
import java.util.List;

public class HomeScreen extends JPanel {

    private User currentUser;
    private RecipeManager recipeManager;
    private List<Recipe> recommendedRecipes;
    private MainScreen mainScreen;
    private JLabel welcomeLabel;
    private JPanel recommendListPanel;

    public HomeScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        currentUser = null;
        setOpaque(false);
        welcomeLabel = ScreenHelper.setText(" ", 18);

        setLayout(new BorderLayout());
        this.recipeManager = new RecipeManager();
        this.recommendedRecipes = recipeManager.getRecommendationsPerCategory();

        JPanel form = buildForm();
        add(form, BorderLayout.CENTER);
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
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
            this.recommendedRecipes = recipeManager.getRecommendationsByIngredients(input);
            updateRecommendList();
            JOptionPane.showMessageDialog(this, "추천 메뉴가 갱신되었습니다!");
        }
    }

    private JPanel recommendPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setOpaque(false);

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
        recommendListPanel.revalidate();
        recommendListPanel.repaint();
    }

    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel imgLabel = new JLabel();

        File imgFile = ScreenHelper.findRecipeImage(recipe.getImageName());
        if (imgFile != null) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            } else {
                imgLabel.setText("X");
            }
        } else {
            imgLabel.setText("🍽️");
            imgLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgLabel.setPreferredSize(new Dimension(60, 60));
        }

        // 2. 텍스트 정보
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("<html><b>[" + recipe.getCategory() + "]</b> " + recipe.getDisplayName() + "</html>");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel infoLabel = new JLabel(recipe.getInfo());
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(infoLabel);

        card.add(imgLabel, BorderLayout.WEST);
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