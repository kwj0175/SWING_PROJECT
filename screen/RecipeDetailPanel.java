package screen;

import javax.swing.*;
import java.awt.*;

import entity.Recipe;

public class RecipeDetailPanel extends JPanel {

    private JLabel nameLabel;
    private JLabel imgLabel;
    private JTextArea ingredientsArea;
    private JTextArea stepsArea;

    public RecipeDetailPanel() {
        initUI();
    }

    public RecipeDetailPanel(Recipe recipe) {
        initUI();
        setRecipe(recipe);
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== 제목 =====
        nameLabel = new JLabel("레시피 이름", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // ===== 이미지 =====
        imgLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
        // 360x640 기준: 좌우 여백 감안해서 320x200 정도
        imgLabel.setPreferredSize(new Dimension(320, 200));

        // ===== 재료 =====
        ingredientsArea = new JTextArea();
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        ingredientsArea.setEditable(false);

        JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);
        ingredientsScroll.setBorder(BorderFactory.createTitledBorder("재료"));
        // 재료는 얇게
        ingredientsScroll.setPreferredSize(new Dimension(320, 60));

        // ===== 조리 방법 =====
        stepsArea = new JTextArea();
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        stepsArea.setEditable(false);

        JScrollPane stepsScroll = new JScrollPane(stepsArea);
        stepsScroll.setBorder(BorderFactory.createTitledBorder("조리 방법"));

        // ===== 아래쪽(재료 + 조리방법) 배치 =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // 세로로 쌓기

        bottomPanel.add(ingredientsScroll);          // 위에 재료
        bottomPanel.add(Box.createVerticalStrut(5)); // 간격 조금
        bottomPanel.add(stepsScroll);                // 아래에 조리 방법 크게

        // ===== 전체 배치 =====
        add(nameLabel, BorderLayout.NORTH);
        add(imgLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setRecipe(Recipe recipe) {
        if (recipe == null) {
            clear();
            return;
        }

        nameLabel.setText(recipe.getName());
        ingredientsArea.setText(recipe.getIngredients());
        stepsArea.setText(recipe.getSteps());

        // 스크롤 맨 위로
        ingredientsArea.setCaretPosition(0);
        stepsArea.setCaretPosition(0);

        // 이미지 로딩
        String imagePath = recipe.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(320, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
            imgLabel.setText(null);
        } else {
            imgLabel.setIcon(null);
            imgLabel.setText("이미지가 없습니다");
        }
    }

    public void clear() {
        nameLabel.setText("레시피를 선택해주세요");
        imgLabel.setIcon(null);
        imgLabel.setText("이미지 없음");
        ingredientsArea.setText("");
        stepsArea.setText("");
    }
}
