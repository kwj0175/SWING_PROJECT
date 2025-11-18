package screen.recipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import entity.Recipe;
import screen.MainScreen;
import screen.utils.ScreenHelper;
import screen.utils.VerticalScrollPanel;

public class RecipeScreen extends JPanel {

    private JLabel nameLabel;
    private JPanel imgPanel;
    private JLabel amountLabel;
    private JLabel timeLabel;
    private JPanel detailsPanel;
    private JPanel stepsPanel;

    public RecipeScreen(MainScreen mainScreen) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel form = buildForm();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
//        c.gridwidth = c.gridheight = 1;
//        c.fill = GridBagConstraints.BOTH;
        add(form, c);
    }

    private JPanel buildNamePanel() {
        nameLabel = ScreenHelper.setText("", 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel namePanel = ScreenHelper.darkCardPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.add(nameLabel, BorderLayout.CENTER);
        return namePanel;
    }

    private void buildImgPanel() {
        imgPanel = ScreenHelper.noColorCardPanel();
//        imgPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        imgPanel.setBorder(null);
        imgPanel.setPreferredSize(new Dimension(320, 200));
        imgPanel.setLayout(new BorderLayout());
    }

    private JPanel buildAmountTimePanel() {
        amountLabel = ScreenHelper.setText("", 16);
        timeLabel = ScreenHelper.setText("", 16);

        JPanel amountTimePanel = ScreenHelper.darkCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(amountTimePanel);

        int HORIZONTAL_GAP = 15;

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
        detailsPanel.setOpaque(false);
    }

    private void buildStepsPanel() {
        stepsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        stepsPanel.setOpaque(false);
    }

    private JPanel buildForm() {
        JPanel root = ScreenHelper.darkCardPanel();
        root.setPreferredSize(new Dimension(320, 560));
        root.setMaximumSize(new Dimension(320, 560));
        root.setLayout(new BorderLayout(0, 15));

        JPanel header = new JPanel();
        header.setOpaque(false);
        GroupLayout headerLayout = ScreenHelper.groupLayout(header);

        JPanel namePanel = buildNamePanel();
        buildImgPanel();
        JPanel amountTimePanel = buildAmountTimePanel();

        headerLayout.setHorizontalGroup(
                headerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(namePanel)
                        .addComponent(imgPanel)
                        .addComponent(amountTimePanel)
        );

        headerLayout.setVerticalGroup(
                headerLayout.createSequentialGroup()
                        .addComponent(namePanel)
                        .addGap(15)
                        .addComponent(imgPanel)
                        .addGap(15)
                        .addComponent(amountTimePanel)
        );

        buildDetailsPanel();
        buildStepsPanel();

        JPanel body = new VerticalScrollPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(detailsPanel);
        body.add(Box.createVerticalStrut(20));
        body.add(stepsPanel);

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);

        JViewport viewport = scrollPane.getViewport();
        viewport.setOpaque(true);
        viewport.setBackground(root.getBackground());

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        root.add(header, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);

        return root;
    }

    public void setRecipe(Recipe recipe) {
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
            JLabel label = ScreenHelper.setText(data);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            detailsPanel.add(label);
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
