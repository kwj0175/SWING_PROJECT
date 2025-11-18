package screen;

import entity.Recipe;

import javax.swing.*;
import java.awt.*;

public class RecipeDetailPanel extends JPanel {

    private final MainScreen mainScreen;

    private JLabel nameLabel;
    private JLabel imgLabel;
    private JTextArea ingredientsArea;
    private JTextArea stepsArea;

    public RecipeDetailPanel(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== 상단(제목 + X 버튼) =====
        nameLabel = new JLabel("레시피 이름", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JButton closeBtn = new JButton("X");
        closeBtn.setMargin(new Insets(2, 8, 2, 8));
        closeBtn.addActionListener(e -> {
            clear();
            mainScreen.displayCategoryScreen();   // 카테고리로 돌아가기
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(nameLabel, BorderLayout.CENTER);
        titlePanel.add(closeBtn, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // ===== 이미지 영역 (가운데 정렬) =====
        imgLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(320, 200));

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.add(imgLabel);

        // ===== 재료 =====
        ingredientsArea = new JTextArea();
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        ingredientsArea.setEditable(false);

        JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);
        ingredientsScroll.setBorder(BorderFactory.createTitledBorder("재료"));
        ingredientsScroll.setPreferredSize(new Dimension(320, 80)); // 높이 고정
        ingredientsScroll.setMaximumSize(new Dimension(320, 80));

        // ===== 조리 방법 (길어도 스크롤) =====
        stepsArea = new JTextArea();
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        stepsArea.setEditable(false);

        JScrollPane stepsScroll = new JScrollPane(stepsArea);
        stepsScroll.setBorder(BorderFactory.createTitledBorder("조리 방법"));
        stepsScroll.setPreferredSize(new Dimension(320, 220)); // 화면에 딱 맞게 높이
        stepsScroll.setMaximumSize(new Dimension(320, 220));

        // ===== 가운데 내용들을 세로로 쌓기 =====
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));

        centerContent.add(imagePanel);                  // 사진
        centerContent.add(Box.createVerticalStrut(5));
        centerContent.add(ingredientsScroll);           // 재료(스크롤)
        centerContent.add(Box.createVerticalStrut(5));
        centerContent.add(stepsScroll);                 // 조리방법(스크롤)

        add(centerContent, BorderLayout.CENTER);
    }

    public void setRecipe(Recipe recipe) {
        if (recipe == null) {
            clear();
            return;
        }

        nameLabel.setText(recipe.getName());
        ingredientsArea.setText(recipe.getIngredients());
        stepsArea.setText(recipe.getSteps());

        ingredientsArea.setCaretPosition(0);
        stepsArea.setCaretPosition(0);

        // 이미지 로딩 (파일 경로 기준)
        String imagePath = recipe.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            java.io.File imgFile = new java.io.File(imagePath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getIconWidth() > 0) {
                    Image img = icon.getImage()
                            .getScaledInstance(320, 200, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                    imgLabel.setText(null);
                } else {
                    System.out.println("상세화면에서 이미지 로드 실패: " + imagePath);
                    imgLabel.setIcon(null);
                    imgLabel.setText("이미지 없음");
                }
            } else {
                System.out.println("상세화면에서 이미지 파일 없음: " + imagePath);
                imgLabel.setIcon(null);
                imgLabel.setText("이미지 없음");
            }
        } else {
            imgLabel.setIcon(null);
            imgLabel.setText("이미지 없음");
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
