package src.screen.home;

import src.entity.Recipe;
import src.entity.User;
import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.recipe.ImagePanel;
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
        JPanel infoPanel = infoPanel();
        JPanel recommendPanel = recommendPanel();

        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BorderLayout(0, 25));

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

        infoPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0));
        infoPanel.add(fridgeBtn);
        infoPanel.add(welcomeLabel);

        return infoPanel;
    }

    private void showIngredientDialog() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame) parentWindow, "재료 기반 추천", true);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel rootPanel = new JPanel(new BorderLayout(0, 15));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rootPanel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0)); // 아이콘과 텍스트 사이 간격
        contentPanel.setOpaque(false);

        // 아이콘 설정
        ImageIcon icon = null;
        try {
            java.net.URL imgURL = getClass().getResource("/src/fridge(icon).png");
            if (imgURL == null) imgURL = getClass().getResource("/fridge(icon).png");
            if (imgURL == null) {
                ImageIcon temp = new ImageIcon("src/fridge(icon).png");
                if(temp.getIconWidth() > 0) icon = temp;
            } else {
                icon = new ImageIcon(imgURL);
            }

            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
        } catch (Exception e) { e.printStackTrace(); }

        JLabel iconLabel = new JLabel();
        if (icon != null) iconLabel.setIcon(icon);
        iconLabel.setVerticalAlignment(SwingConstants.TOP);
        contentPanel.add(iconLabel, BorderLayout.WEST);

        JPanel rightGroupPanel = new JPanel(new BorderLayout(0, 8));
        rightGroupPanel.setOpaque(false);

        JLabel textLabel = new JLabel("<html>냉장고 재료를 입력하세요 (쉼표 구분)<br>예: 두부, 계란, 대파</html>");
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField inputField = new JTextField();
        //입력 창 크기
        inputField.setPreferredSize(new Dimension(220, 30)); // 너비 220, 높이 30

        // 입력창이 늘어나지 않게 잡아주는 래퍼 패널
        JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inputWrapper.setOpaque(false);
        inputWrapper.add(inputField);

        rightGroupPanel.add(textLabel, BorderLayout.NORTH);
        rightGroupPanel.add(inputWrapper, BorderLayout.CENTER);

        contentPanel.add(rightGroupPanel, BorderLayout.CENTER);

        //하단 버튼 영역
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFocusPainted(false);
        confirmBtn.setBackground(new Color(240, 240, 240));

        buttonPanel.add(confirmBtn);
        rootPanel.add(contentPanel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(rootPanel);

        // 4. 이벤트 처리
        java.awt.event.ActionListener action = e -> {
            String input = inputField.getText();
            dialog.dispose();
            if (input != null && !input.trim().isEmpty()) {
                homePresenter.onIngredientsSubmitted(input);
            }
        };
        confirmBtn.addActionListener(action);
        inputField.addActionListener(action);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        textPanel.setOpaque(false);

        textPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

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