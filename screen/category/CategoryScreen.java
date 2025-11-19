package screen.category;

import entity.FoodCategory;
import entity.Recipe;
import screen.MainScreen;
import screen.utils.ScreenHelper; // ⭐️ ScreenHelper import 필수

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class CategoryScreen extends JPanel {
    private JTextField inputField;
    private JPanel cards; // CardLayout 영역
    private CardLayout cardLayout;
    private final ArrayList<Recipe> recipes;
    private final MainScreen mainScreen;

    public CategoryScreen(MainScreen mainScreen, ArrayList<Recipe> recipes) {
        setLayout(new BorderLayout());
        this.recipes = recipes;
        this.mainScreen = mainScreen;
        initComponents();
    }

    private void initComponents() {
        // 1. 상단 검색바
        JPanel topPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton searchButton = new JButton("검색");

        // 엔터키 이벤트
        inputField.addActionListener(e -> searchCurrentCard(inputField.getText().trim()));
        searchButton.addActionListener(e -> searchCurrentCard(inputField.getText().trim()));

        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 2. 카테고리 버튼 패널
        JPanel categoryPanel = new JPanel(
                new GridLayout(1, FoodCategory.values().length, 5, 5)
        );

        for(FoodCategory cat : FoodCategory.values()) {
            JButton btn = new JButton(cat.getDisplayName());
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            categoryPanel.add(btn);

            // 버튼 클릭 시 해당 카테고리 화면으로 전환
            btn.addActionListener(e -> cardLayout.show(cards, cat.name()));
        }
        add(categoryPanel, BorderLayout.AFTER_LAST_LINE);

        // 3. 중앙 리스트 영역
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // 각 카테고리별로 패널 생성해서 카드에 추가
        for (FoodCategory cat : FoodCategory.values()) {
            cards.add(createScrollPanel(getRecipesByCategory(cat)), cat.name());
        }

        add(cards, BorderLayout.CENTER);
    }

    // 카테고리에 맞는 레시피 필터링
    private ArrayList<Recipe> getRecipesByCategory(FoodCategory category) {
        ArrayList<Recipe> r = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCategory() != null &&
                    recipe.getCategory().equals(category.getDisplayName())) {
                r.add(recipe);
            }
        }
        return r;
    }

    private JScrollPane createScrollPanel(ArrayList<Recipe> recipes) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for(Recipe recipe : recipes) {
            // 개별 카드 패널
            JPanel menuPanel = new JPanel(new BorderLayout(10, 0));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)), // 하단 구분선
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) // 여백
            ));

            // 높이 고정
            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            // 이미지 추가 (ScreenHelper 사용)
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
                imgLabel.setText("X"); // 이미지 없으면 X
                imgLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imgLabel.setPreferredSize(new Dimension(60, 60));
            }

            // 2. 텍스트 추가 (HTML 태그로 줄바꿈 지원)
            String htmlTitle = "<html><body style='width: 200px'><b>" + recipe.getName() + "</b></body></html>";
            JLabel textLabel = new JLabel(htmlTitle);
            textLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

            menuPanel.add(imgLabel, BorderLayout.WEST);
            menuPanel.add(textLabel, BorderLayout.CENTER);

            // 3. 마우스 이벤트 (상세페이지 이동)
            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBackground(new Color(240, 245, 255));
                    menuPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) {
                    menuPanel.setBackground(Color.WHITE);
                }
                public void mouseClicked(MouseEvent e) {
                    mainScreen.displayRecipeDetail(recipe);
                }
            });

            contentPanel.add(menuPanel);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(contentPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // 현재 보이는 카드에서 검색 기능
    private void searchCurrentCard(String text) {
        Component current = getCurrentCard();
        if(current instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) current;
            JPanel wrapper = (JPanel) sp.getViewport().getView();
            JPanel contentPanel = (JPanel) wrapper.getComponent(0);

            for(Component comp : contentPanel.getComponents()) {
                if(comp instanceof JPanel) {
                    JPanel card = (JPanel) comp;
                    Component centerComp = ((BorderLayout)card.getLayout()).getLayoutComponent(BorderLayout.CENTER);

                    if(centerComp instanceof JLabel) {
                        String htmlText = ((JLabel)centerComp).getText();
                        // HTML 태그 제거 후 검색어 비교
                        String rawText = htmlText.replaceAll("<[^>]*>", "");
                        comp.setVisible(rawText.contains(text));
                    }
                }
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private Component getCurrentCard() {
        for(Component comp : cards.getComponents()) {
            if(comp.isVisible()) return comp;
        }
        return null;
    }
}