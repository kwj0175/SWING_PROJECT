package src.screen.category;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.utils.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CategoryScreen extends JPanel {
    private JTextField inputField;
    private JPanel cards;
    private CardLayout cardLayout;

    private final CategoryPresenter categoryPresenter;
    private final MainScreen mainScreen;

    public CategoryScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        setLayout(new BorderLayout());
        this.mainScreen = mainScreen;
        this.categoryPresenter = new CategoryPresenter(this, recipeManager);
        initComponents();
    }

    private void initComponents() {
        // 상단바
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
        inputField = new JTextField();
        JButton searchButton = new JButton("검색");//검색버튼

        ActionListener searchAction = e -> searchCurrentCard(inputField.getText().trim());
        inputField.addActionListener(searchAction);
        searchButton.addActionListener(searchAction);

        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // 카테고리 버튼
        JPanel categoryPanel = new JPanel(
                new GridLayout(1, FoodCategory.values().length, 5, 5)
        );

        for(FoodCategory cat : FoodCategory.values()) {//카테고리 버튼 생성
            JButton btn = new JButton(cat.getDisplayName());
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            categoryPanel.add(btn);
            btn.addActionListener(e -> {
                cardLayout.show(cards, cat.name());
                inputField.setText("");
                searchCurrentCard("");
            });
            
        }

        // 헤더 패널
        JPanel headerPanel = new JPanel();//
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));//
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);//왼쪽 정렬
        headerPanel.add(topPanel);//

        // 카테고리 패널을 헤더에 추가 --> 카테고리 버튼 화면 위쪽으로 오게 배치
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);//
        headerPanel.add(categoryPanel);//
        headerPanel.add(Box.createVerticalStrut(2));//간격 띄우기

        add(headerPanel, BorderLayout.NORTH);//

        // 중앙 카드 영역
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        for (FoodCategory cat : FoodCategory.values()) {
            JScrollPane scrollPanel = createScrollPanel(getRecipesByCategory(cat));
            scrollPanel.setName(cat.name());
            cards.add(scrollPanel, cat.name());
        }

        add(cards, BorderLayout.CENTER);
    }

    private List<Recipe> getRecipesByCategory(FoodCategory category) {
        return categoryPresenter.loadRecipesByCategory(category);
    }

    private JScrollPane createScrollPanel(List<Recipe> recipes) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // 메뉴 카드 생성
        for(Recipe recipe : recipes) {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBackground(Color.WHITE);
            menuPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            menuPanel.setPreferredSize(new Dimension(150, 160));

            String path = recipe.getImagePath();
            Component imgComp;

            if (path != null) {
                imgComp = new ImagePanel(path);
            } else {
                JLabel noImg = new JLabel("🍽️");
                noImg.setFont(new Font("SansSerif", Font.PLAIN, 40));
                noImg.setHorizontalAlignment(SwingConstants.CENTER);
                noImg.setOpaque(true);
                noImg.setBackground(new Color(240, 240, 240));
                imgComp = noImg;
            }

            JLabel label = new JLabel(recipe.getName());//메뉴 이름 라벨
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);

            menuPanel.add(imgComp, BorderLayout.CENTER);
            menuPanel.add(label, BorderLayout.SOUTH);

            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 2));//
                    menuPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) {
                    menuPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
                }
                public void mouseClicked(MouseEvent e) {
                    mainScreen.displayRecipeDetail(recipe);
                }
            });

            panel.add(menuPanel);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//가로 스크롤바 안 보이게 설정
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);//세로 스크롤바 안 보이게 설정//
        return scrollPane;
    }

    private void searchCurrentCard(String text) {
        String cardName = getCurrentCardName();
        Component current = getCurrentCard();

        if(cardName == null || !(current instanceof JScrollPane)) return;

        FoodCategory currentCategory;
        try{
            currentCategory = FoodCategory.valueOf(cardName);
        } catch(IllegalArgumentException e){
            System.err.println("Invalid card name: " + cardName);
            return;
        }

        List<Recipe> allRecipes = categoryPresenter.loadRecipesByCategory(currentCategory);
        List<Recipe> filteredRecipes = categoryPresenter.filterRecipes(allRecipes, text);

        JScrollPane newScrollPane = createScrollPanel(filteredRecipes);
        newScrollPane.setName(cardName);

        cards.remove(current);
        cards.add(newScrollPane, cardName);

        cardLayout.show(cards, cardName);
        cards.revalidate();
        cards.repaint();
    }

    private Component getCurrentCard() {
        for(Component comp : cards.getComponents()) {
            if(comp.isVisible()) return comp;
        }
        return null;
    }

    private String getCurrentCardName(){
        Component current = getCurrentCard();
        if(current == null) return null;

        return current.getName();
    }
}