package screen.category;

import entity.FoodCategory;
import entity.Recipe;
import screen.MainScreen;
import screen.recipe.ImagePanel;
import screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class CategoryScreen extends JPanel {
    private JTextField inputField;
    private JPanel cards;
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
        // 상단바
        JPanel topPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton searchButton = new JButton("검색");

        ActionListener searchAction = e -> searchCurrentCard(inputField.getText().trim());
        inputField.addActionListener(searchAction);
        searchButton.addActionListener(searchAction);

        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 카테고리 버튼
        JPanel categoryPanel = new JPanel(
                new GridLayout(1, FoodCategory.values().length, 5, 5)
        );

        for(FoodCategory cat : FoodCategory.values()) {
            JButton btn = new JButton(cat.getDisplayName());
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            categoryPanel.add(btn);
            btn.addActionListener(e -> cardLayout.show(cards, cat.name()));
        }
        add(categoryPanel, BorderLayout.AFTER_LAST_LINE);

        // 중앙 카드 영역
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        for (FoodCategory cat : FoodCategory.values()) {
            cards.add(createScrollPanel(getRecipesByCategory(cat)), cat.name());
        }

        add(cards, BorderLayout.CENTER);
    }

    private ArrayList<Recipe> getRecipesByCategory(FoodCategory category) {
        ArrayList<Recipe> r = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.checkCat(category)) {
                r.add(recipe);
            }
        }
        return r;
    }

    private JScrollPane createScrollPanel(ArrayList<Recipe> recipes) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        for(Recipe recipe : recipes) {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBackground(Color.WHITE);
            menuPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            menuPanel.setPreferredSize(new Dimension(160, 160));

            String path = recipe.getImagePath();
            Component imgComp;

            if (path == null) {
                File f = ScreenHelper.findRecipeImage(recipe.getName());
                if (f != null) path = f.getAbsolutePath();
            }

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

            JLabel label = new JLabel(recipe.getTitle());
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);

            menuPanel.add(imgComp, BorderLayout.CENTER);
            menuPanel.add(label, BorderLayout.SOUTH);

            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 2));
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void searchCurrentCard(String text) {
        Component current = getCurrentCard();
        if(current instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) current;
            JPanel wrapper = (JPanel) sp.getViewport().getView();
            JPanel gridPanel = (JPanel) wrapper.getComponent(0);

            for(Component comp : gridPanel.getComponents()) {
                if(comp instanceof JPanel) {
                    JPanel card = (JPanel) comp;
                    Component southComp = ((BorderLayout)card.getLayout()).getLayoutComponent(BorderLayout.SOUTH);

                    if(southComp instanceof JLabel) {
                        String title = ((JLabel) southComp).getText();
                        comp.setVisible(title.contains(text));
                    }
                }
            }
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    private Component getCurrentCard() {
        for(Component comp : cards.getComponents()) {
            if(comp.isVisible()) return comp;
        }
        return null;
    }
}