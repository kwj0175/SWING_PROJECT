package src.view.home;

import src.view.utils.IconHelper;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class IngredientInputDialog extends JDialog {

    public IngredientInputDialog(Component parentComponent, Consumer<String> onConfirmed) {
        super(SwingUtilities.getWindowAncestor(parentComponent), "재료 기반 추천", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel rootPanel = buildUI(onConfirmed);
        add(rootPanel);

        pack();
        setLocationRelativeTo(parentComponent);
    }

    private JPanel buildUI(Consumer<String> onConfirmed) {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 15));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);

        ImageIcon icon = IconHelper.getFridgeOnIcon();
        JLabel iconLabel = new JLabel();
        if (icon != null) iconLabel.setIcon(icon);
        iconLabel.setVerticalAlignment(SwingConstants.TOP);
        contentPanel.add(iconLabel, BorderLayout.WEST);

        JPanel rightGroupPanel = new JPanel(new BorderLayout(0, 8));
        rightGroupPanel.setOpaque(false);

        JLabel textLabel = new JLabel("<html>냉장고 재료를 입력하세요 (쉼표 구분)<br>예: 두부, 계란, 대파</html>");
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(220, 30));

        JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inputWrapper.setOpaque(false);
        inputWrapper.add(inputField);

        rightGroupPanel.add(textLabel, BorderLayout.NORTH);
        rightGroupPanel.add(inputWrapper, BorderLayout.CENTER);

        contentPanel.add(rightGroupPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFocusPainted(false);
        buttonPanel.add(confirmBtn);

        rootPanel.add(contentPanel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        java.awt.event.ActionListener action = e -> {
            String input = inputField.getText();
            dispose();
            if (input != null && !input.trim().isEmpty() && onConfirmed != null) {
                onConfirmed.accept(input);
            }
        };
        confirmBtn.addActionListener(action);
        inputField.addActionListener(action);

        return rootPanel;
    }

    public void open() {
        setVisible(true);
    }
}