package src.screen.planner;

import src.entity.Recipe;
import src.screen.recipe.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlannerOverlay extends JPanel {
    private Recipe recipe;
    private final ActionListener onCancelListener;

    private JLabel titleLabel;
    private JPanel imagePanel;

    public PlannerOverlay(Recipe recipe, ActionListener onCancelListener) {
        this.recipe = recipe;
        this.onCancelListener = onCancelListener;

        setOpaque(false);
        setLayout(new GridBagLayout());

        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 상단 부분만 반투명 처리
        g2d.setColor(new Color(0, 0, 0, 100)); // 더 연한 반투명
        g2d.fillRect(0, 0, getWidth(), getHeight() - 300); // 하단 300px는 투명하게

        g2d.dispose();
    }

    private void initComponents() {
        JPanel recipeCard = createRecipeCard();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1; // 하단에 배치
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // 하단에 고정
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0); // 하단 여백

        add(recipeCard, gbc);
    }

    private JPanel createRecipeCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 255), 3),
                BorderFactory.createEmptyBorder(5, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(340, 240));
        card.setMaximumSize(new Dimension(340, 240));

        // 제목
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        // 이미지
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(280, 120));

        updateRecipeView();

        // 안내 메시지 및 버튼
        JLabel guideLabel = new JLabel("원하는 칸을 클릭하세요", SwingConstants.CENTER);
        guideLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        guideLabel.setForeground(new Color(100, 150, 255));

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        cancelBtn.addActionListener(onCancelListener);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(guideLabel, BorderLayout.NORTH);
        bottomPanel.add(cancelBtn, BorderLayout.SOUTH);

        // 레이아웃 구성
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(imagePanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateRecipeView();
    }
}