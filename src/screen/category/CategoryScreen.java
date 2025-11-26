package src.screen.category;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.utils.IconHelper;
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
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputField = new JTextField();
        JButton searchButton = new JButton(IconHelper.getSearchOffIcon());

        ActionListener searchAction = e -> searchCurrentCard(inputField.getText().trim());
        inputField.addActionListener(searchAction);
        searchButton.addActionListener(searchAction);

        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel categoryPanel = buildCategoryPanel();

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(topPanel);

        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(categoryPanel);
        headerPanel.add(Box.createVerticalStrut(2));

        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        for (FoodCategory cat : FoodCategory.values()) {
            JScrollPane scrollPanel = createScrollPanel(getRecipesByCategory(cat));
            scrollPanel.setName(cat.name());
            cards.add(scrollPanel, cat.name());
        }

        add(cards, BorderLayout.CENTER);
    }

    private JPanel buildCategoryPanel() {
        JPanel categoryPanel = new JPanel(
                new GridLayout(1, FoodCategory.values().length, 10, 10)
        );

        categoryPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 15, 10)
        );

        for(FoodCategory cat : FoodCategory.values()) {
            JButton btn = new JButton(cat.getDisplayName());
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));

            categoryPanel.add(btn);
            btn.addActionListener(e -> {
                cardLayout.show(cards, cat.name());
                inputField.setText("");
                searchCurrentCard("");
            });
        }
        return categoryPanel;
    }

    private List<Recipe> getRecipesByCategory(FoodCategory category) {
        return categoryPresenter.loadRecipesByCategory(category);
    }

    private JScrollPane createScrollPanel(List<Recipe> recipes) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        Color borderColor = UIManager.getColor("Panel.borderColor");

        for(Recipe recipe : recipes) {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setPreferredSize(new Dimension(150, 160));

            Component imgComp = buildImgComp(recipe);

            JLabel label = new JLabel(recipe.getName());
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            label.setOpaque(true);

            menuPanel.add(imgComp, BorderLayout.CENTER);
            menuPanel.add(label, BorderLayout.SOUTH);

            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
                    menuPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) {
                    menuPanel.setBorder(BorderFactory.createEmptyBorder());
                }
                public void mouseClicked(MouseEvent e) {
                    mainScreen.displayRecipeDetail(recipe);
                }
            });

            panel.add(menuPanel);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(null);

        return scrollPane;
    }

    private static Component buildImgComp(Recipe recipe) {
        String path = recipe.getImagePath();
        return new ImagePanel(path);
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