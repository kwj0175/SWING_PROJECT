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
import java.io.File;
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

        // ⭐️ [수정 1] JScrollPane 삭제하고 바로 패널 추가
        JPanel form = buildForm();
        add(form, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel infoPanel = infoPanel();
        JPanel recommendPanel = recommendPanel();

        // ⭐️ [수정 2] BorderLayout 사용
        // NORTH: 상단 정보 (높이 고정)
        // CENTER: 추천 메뉴 (남은 공간 전부 차지 -> 자동으로 늘어나고 줄어듦)
        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BorderLayout(0, 15)); // 상하 간격 15

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
            this.recommendedRecipes = recipeManager.getRecommendationsByIngredients(input);
            updateRecommendList();
            JOptionPane.showMessageDialog(this, "추천 메뉴가 갱신되었습니다!");
        }
    }

    private JPanel recommendPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // 하단 여백

        JLabel title = ScreenHelper.setText("오늘의 추천 메뉴", 16);
        title.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        // ⭐️ [수정 3] GridLayout(4, 1) 사용
        // 무조건 4행 1열로 공간을 꽉 채우게 합니다.
        // 화면이 작아지면 각 행의 높이도 같이 작아집니다.
        recommendListPanel = new JPanel(new GridLayout(4, 1, 0, 10)); // 카드 간격 10
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
        // 만약 추천 메뉴가 4개 미만일 경우 빈칸을 채워 모양 유지 (선택사항)
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
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // 내부 여백 약간 줄임
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ⭐️ [수정 4] 카드 크기 고정 코드(setPreferredSize) 삭제!
        // GridLayout이 알아서 크기를 결정하도록 둡니다.

        // 1. 이미지 (왼쪽)
        Component imgComp;
        String path = recipe.getImagePath();

        if (path == null || path.isEmpty()) {
            File f = ScreenHelper.findRecipeImage(recipe.getName());
            if (f != null) path = f.getAbsolutePath();
        }

        if (path != null) {
            // ⭐️ ImagePanel은 부모 패널 크기에 맞춰 알아서 줄어듭니다.
            ImagePanel imgPanel = new ImagePanel(path);
            // 가로 길이는 고정하고 싶다면 설정 (세로는 자동 축소)
            imgPanel.setPreferredSize(new Dimension(110, 0));
            imgComp = imgPanel;
        } else {
            JLabel noImg = new JLabel("🍽️");
            noImg.setFont(new Font("SansSerif", Font.PLAIN, 30));
            noImg.setHorizontalAlignment(SwingConstants.CENTER);
            noImg.setPreferredSize(new Dimension(110, 0));
            imgComp = noImg;
        }

        // 2. 텍스트 정보 (중앙)
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 0)); // 간격 최소화
        textPanel.setOpaque(false);

        String catName = (recipe.getCategory() != null) ? recipe.getCategory().getDisplayName() : "기타";
        JLabel nameLabel = new JLabel("<html><b>[" + catName + "]</b><br>" + recipe.getTitle() + "</html>");
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