package screen.category;
import entity.FoodCategory;
import entity.Recipe;
import screen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CategoryScreen extends JPanel {
    private JTextField inputField;
    private JPanel cards; // CardLayout 영역
    private CardLayout cardLayout;
    private final ArrayList<Recipe> recipes;

    public CategoryScreen(MainScreen mainScreen, ArrayList<Recipe> recipes) {
        setLayout(new BorderLayout());
        this.recipes = recipes;
        initComponents();
    }

    private void initComponents() {
        // 상단 입력바
        JPanel topPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton searchButton = new JButton("검색");
        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 카테고리 버튼
        JPanel categoryPanel = new JPanel(
                new GridLayout(1,FoodCategory.values().length,5,5)
        );

        for(FoodCategory  cat : FoodCategory.values()) {
            JButton btn = new JButton(cat.getDisplayName());
            categoryPanel.add(btn);

            btn.addActionListener(e -> cardLayout.show(cards, cat.name()));
        }
        add(categoryPanel, BorderLayout.AFTER_LAST_LINE);

        // CardLayout 영역
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        for (FoodCategory cat : FoodCategory.values()) {
            cards.add(createScrollPanel(getRecipesByCategory(cat)), cat.name());
        }

        add(cards, BorderLayout.CENTER);

        // 검색 버튼 이벤트
        searchButton.addActionListener(e -> searchCurrentCard(inputField.getText().trim()));
    }

    private ArrayList<Recipe> getRecipesByCategory(FoodCategory category) {
        ArrayList<Recipe> r = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.checkCat(category))
                r.add(recipe);
        }
        return r;
    }

    // 스크롤 패널 생성
    private JScrollPane createScrollPanel(ArrayList<Recipe> recipes) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1,5,5));

        for(Recipe recipe : recipes) {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            menuPanel.setBackground(new Color(245,245,245));

            JLabel label = new JLabel(recipe.getTitle());
            label.setFont(new Font("맑은 고딕", Font.BOLD, 10));
            menuPanel.add(label, BorderLayout.CENTER);

            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBackground(new Color(220,220,250));
                }
                public void mouseExited(MouseEvent e) {
                    menuPanel.setBackground(new Color(245,245,245));
                }
            });

            panel.add(menuPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // 현재 보이는 카드에서 검색
    private void searchCurrentCard(String text) {
        Component current = getCurrentCard();
        if(current instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) current;
            JPanel panel = (JPanel) sp.getViewport().getView();
            for(Component comp : panel.getComponents()) {
                if(comp instanceof JPanel) {
                    JLabel label = (JLabel) ((JPanel) comp).getComponent(0);
                    String title = label.getText();
                    comp.setVisible(title.contains(text));
                }
            }
            panel.revalidate();
            panel.repaint();
        }
    }

    // 현재 보이는 카드 가져오기
    private Component getCurrentCard() {
        for(Component comp : cards.getComponents()) {
            if(comp.isVisible()) return comp;
        }
        return null;
    }
}