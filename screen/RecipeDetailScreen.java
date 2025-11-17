package screen;

import entity.Recipe;
import screen.MainScreen;
import screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;

/**
 * [임시] 레시피 상세 보기 팝업 (UI 위치 확인용)
 * (나중에 담당자가 JTextArea 등으로 상세 내용을 채워야 함)
 */
public class RecipeDetailScreen extends JDialog {

    public RecipeDetailScreen(MainScreen parent, Recipe recipe) {
        super(parent, "레시피 상세 보기 (임시)", true); // Modal 팝업

        JLabel titleLabel = ScreenHelper.setText("'" + recipe.getName() + "'", 20);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel infoLabel = ScreenHelper.setText("상세 내용 (구현 예정)", 16);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = ScreenHelper.primaryButton("닫기", 14);
        backButton.addActionListener(e -> dispose());

        JPanel contentPanel = new JPanel(new BorderLayout(10, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(infoLabel, BorderLayout.CENTER);
        contentPanel.add(backButton, BorderLayout.SOUTH);

        setContentPane(contentPanel);
        setSize(300, 200); // 임시 팝업 크기
        setLocationRelativeTo(parent);
    }
}