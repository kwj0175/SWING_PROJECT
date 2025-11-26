package src.screen.home;

import src.entity.Recipe;
import src.screen.recipe.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
//레시피 카드 UI
public class RecipeCardPanel extends JPanel {

    public RecipeCardPanel(Recipe recipe, Consumer<Recipe> onClickAction) {
        setLayout(new BorderLayout(15, 0));
//        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(createImageComponent(recipe.getImagePath()), BorderLayout.WEST);

        add(createTextPanel(recipe), BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickAction != null) {
                    onClickAction.accept(recipe);
                }
            }
        });
    }

    private Component createImageComponent(String path) {
        if (path != null) {
            ImagePanel imgPanel = new ImagePanel(path);
            imgPanel.setPreferredSize(new Dimension(110, 0));
            return imgPanel;
        } else {
            JLabel noImg = new JLabel("🍽️");
            noImg.setFont(new Font("SansSerif", Font.PLAIN, 30));
            noImg.setHorizontalAlignment(SwingConstants.CENTER);
            noImg.setPreferredSize(new Dimension(110, 0));
            return noImg;
        }
    }

    private JPanel createTextPanel(Recipe recipe) {
        JPanel textPanel = new JPanel(new BorderLayout(0, 0));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        // 상단: 카테고리
        String catName = (recipe.getCategory() != null) ? recipe.getCategory().getDisplayName() : "기타";
        JLabel catLabel = new JLabel("[" + catName + "]");
        catLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        catLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        textPanel.add(catLabel, BorderLayout.NORTH);

        // 중단: 메뉴 이름
        String name = recipe.getName();
        int fontSize = (name.length() >= 10) ? 13 : 14;

        JLabel nameLabel = new JLabel("<html>" + name + "</html>");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        nameLabel.setVerticalAlignment(SwingConstants.CENTER);
        textPanel.add(nameLabel, BorderLayout.CENTER);

        // 하단: 정보
        String infoText = recipe.getAmount() + " | " + recipe.getTime();
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);
        textPanel.add(infoLabel, BorderLayout.SOUTH);

        return textPanel;
    }
}