package src.view.planner;

import src.entity.Recipe;
import src.view.utils.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Area;

public class PlannerOverlay extends JPanel {
    private Recipe recipe;
    private final ActionListener onCancelListener;

    private Rectangle highlightRect;

    private JLabel titleLabel;
    private JPanel imagePanel;
    private JPanel recipeCard;

    public PlannerOverlay(Recipe recipe, ActionListener onCancelListener) {
        this.recipe = recipe;
        this.onCancelListener = onCancelListener;

        setOpaque(false);
        setLayout(new BorderLayout());

        initComponents();
    }

    public void setHighlightRect(Rectangle rect) {
        this.highlightRect = rect;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color base = UIManager.getColor("Panel.background");
        if (base == null) {
            base = Color.BLACK;
        }
        Color overlayColor = new Color(
                base.getRed(),
                base.getGreen(),
                base.getBlue(),
                120
        );

        Rectangle full = new Rectangle(0, 0, getWidth(), getHeight());
        Area dimArea = new Area(full);

        if (highlightRect != null) {
            java.awt.geom.Area hole = new java.awt.geom.Area(highlightRect);
            dimArea.subtract(hole);
        }

        g2d.setColor(overlayColor);
        g2d.fill(dimArea);

        g2d.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        return recipeCard != null && recipeCard.getBounds().contains(x, y);
    }

    private void initComponents() {
        createRecipeCard();

        JPanel space = new JPanel();
        space.setOpaque(false);

        add(space, BorderLayout.CENTER);
        add(recipeCard, BorderLayout.SOUTH);
    }

    private void createRecipeCard() {
        recipeCard = new JPanel();
        recipeCard.setLayout(new BorderLayout(10, 10));

        Color borderColor = UIManager.getColor("Panel.borderColor");
        recipeCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3),
                BorderFactory.createEmptyBorder(5, 15, 15, 15)
        ));
        recipeCard.setPreferredSize(new Dimension(340, 300));
        recipeCard.setMaximumSize(new Dimension(340, 300));

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(280, 200));

        updateRecipeView();

        JLabel guideLabel = new JLabel("원하는 칸을 클릭하세요", SwingConstants.CENTER);
        guideLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        cancelBtn.addActionListener(onCancelListener);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(guideLabel, BorderLayout.NORTH);
        bottomPanel.add(cancelBtn, BorderLayout.SOUTH);

        // 레이아웃 구성
        recipeCard.add(titleLabel, BorderLayout.NORTH);
        recipeCard.add(imagePanel, BorderLayout.CENTER);
        recipeCard.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateRecipeView() {
        if (titleLabel != null) {
            titleLabel.setText(recipe != null ? recipe.getName() : "");
        }

        if (imagePanel != null) {
            imagePanel.removeAll();
            if (recipe.getImagePath() != null) {
                imagePanel.add(new ImagePanel(recipe.getImagePath()));
            } else {
                JLabel noImg = new JLabel("🍽️", SwingConstants.CENTER);
                noImg.setFont(new Font("SansSerif", Font.PLAIN, 60));
                imagePanel.add(noImg);
            }
            imagePanel.revalidate();
            imagePanel.repaint();
        }
    }

    public Recipe getCurrentRecipe() {
        return recipe;
    }

    public void setCurrentRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateRecipeView();
    }
}