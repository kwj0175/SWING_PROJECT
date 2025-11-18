package screen.recipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import entity.Recipe;
import screen.MainScreen;
import screen.utils.ScreenHelper;

public class RecipeScreen extends JPanel {

    private JLabel nameLabel;
    private JPanel imgPanel;
    private JLabel amountLabel;
    private JLabel timeLabel;
    private JPanel detailsPanel;
    private JPanel stepsPanel;

    private Recipe recipe;

    public RecipeScreen(MainScreen mainScreen) {
        setLayout(new GridBagLayout());
        JPanel form = buildForm();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.gridwidth = c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        add(form, c);
    }

    private JPanel buildNamePanel() {
        nameLabel = ScreenHelper.setText("", 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel namePanel = ScreenHelper.noColorCardPanel();
//        namePanel.setLayout(new BorderLayout());
        namePanel.add(nameLabel, BorderLayout.CENTER);
        return namePanel;
    }

    private void buildImgPanel() {
        imgPanel = ScreenHelper.noColorCardPanel();
        imgPanel.setPreferredSize(new Dimension(320, 200));
        imgPanel.setLayout(new BorderLayout());
    }

    private JPanel buildAmountTimePanel() {
        amountLabel = ScreenHelper.setText("", 20);
        timeLabel = ScreenHelper.setText("", 20);

        JPanel amountTimePanel = ScreenHelper.noColorCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(amountTimePanel);

        int HORIZONTAL_GAP = 30;

        formLayout.setHorizontalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(amountLabel)
                        .addGap(HORIZONTAL_GAP)
                        .addComponent(timeLabel)
        );

        formLayout.setVerticalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(amountLabel)
                        .addComponent(timeLabel)
        );

        return amountTimePanel;
    }

    private void buildDetailsPanel() {
        detailsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
    }

    private void buildStepsPanel() {
        stepsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
    }

    private JPanel buildForm() {
        JPanel namePanel = buildNamePanel();
        buildImgPanel();
        JPanel amountTimePanel = buildAmountTimePanel();
        buildDetailsPanel();
        buildStepsPanel();

        JPanel root = ScreenHelper.noColorCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(root, false, false);

        int GAP = 20;
        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(namePanel)
                        .addComponent(imgPanel)
                        .addComponent(amountTimePanel)
                        .addComponent(detailsPanel)
                        .addComponent(stepsPanel)
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(namePanel)
                        .addGap(GAP)
                        .addComponent(imgPanel)
                        .addGap(GAP)
                        .addComponent(amountTimePanel)
                        .addGap(GAP)
                        .addComponent(detailsPanel)
                        .addGap(GAP)
                        .addComponent(stepsPanel)
        );

        formLayout.linkSize(SwingConstants.HORIZONTAL, namePanel, imgPanel, amountTimePanel, detailsPanel, stepsPanel);
        return root;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        nameLabel.setText(recipe.getName());

        imgPanel.removeAll();
        String imagePath = recipe.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            imgPanel.add(new ImagePanel(imagePath), BorderLayout.CENTER);
        }
        imgPanel.revalidate();
        imgPanel.repaint();

        amountLabel.setText(recipe.getAmount());
        timeLabel.setText(recipe.getTime());

        detailsPanel.removeAll();
        for (String data: recipe.getDetails()) {
            detailsPanel.add(ScreenHelper.setText(data, 10));
        }
        detailsPanel.revalidate();
        detailsPanel.repaint();

        stepsPanel.removeAll();
        for (String data: recipe.getSteps()) {
            stepsPanel.add(ScreenHelper.setText(data));
        }
        stepsPanel.revalidate();
        stepsPanel.repaint();
    }

}
