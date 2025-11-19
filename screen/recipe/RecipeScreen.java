package screen.recipe;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import entity.Recipe;
import screen.MainScreen;
import screen.utils.ScreenHelper;

public class RecipeScreen extends JPanel {

    private JLabel nameLabel;
    private JLabel imgLabel;
    private JLabel infoLabel;
    private JPanel detailsPanel;
    private JPanel stepsPanel;
    private MainScreen mainScreen; // 뒤로가기 등 필요시 사용

    public RecipeScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel form = buildForm();

        // 스크롤 기능 추가
        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel root = ScreenHelper.noColorCardPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 이름
        nameLabel = ScreenHelper.setText("", 20);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 이미지
        imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setPreferredSize(new Dimension(200, 150)); // 이미지 크기 지정

        // info (인분/시간)
        infoLabel = ScreenHelper.setText("", 14);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 재료 패널
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        // 조리법 패널
        stepsPanel = new JPanel();
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
        stepsPanel.setOpaque(false);

        root.add(nameLabel);
        root.add(Box.createVerticalStrut(10));
        root.add(imgLabel);
        root.add(Box.createVerticalStrut(10));
        root.add(infoLabel);
        root.add(Box.createVerticalStrut(20));

        root.add(ScreenHelper.setText("--- [ 재 료 ] ---", 16));
        root.add(Box.createVerticalStrut(5));
        root.add(detailsPanel);

        root.add(Box.createVerticalStrut(20));

        root.add(ScreenHelper.setText("--- [ 조리 순서 ] ---", 16));
        root.add(Box.createVerticalStrut(5));
        root.add(stepsPanel);

        return root;
    }

    public void setRecipe(Recipe recipe) {
        nameLabel.setText(recipe.getDisplayName());

        // 이미지 설정
        File imgFile = ScreenHelper.findRecipeImage(recipe.getImageName());
        if (imgFile != null) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
            imgLabel.setText("");
        } else {
            imgLabel.setIcon(null);
            imgLabel.setText("이미지 없음");
        }

        infoLabel.setText(recipe.getInfo());

        // 재료 파싱
        detailsPanel.removeAll();
        String[] ingredients = recipe.getIngredients().split(",");
        for (String ing : ingredients) {
            JLabel l = ScreenHelper.setText("• " + ing.trim(), 13);
            detailsPanel.add(l);
        }

        // 조리법 파싱
        stepsPanel.removeAll();
        String[] steps = recipe.getSteps().split("/");
        int count = 1;
        for (String step : steps) {
            // 텍스트가 길면 HTML로 줄바꿈 처리
            JLabel l = new JLabel("<html><body style='width: 250px'>" + count + ". " + step.trim() + "</body></html>");
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            stepsPanel.add(l);
            stepsPanel.add(Box.createVerticalStrut(5));
            count++;
        }

        revalidate();
        repaint();
    }
}