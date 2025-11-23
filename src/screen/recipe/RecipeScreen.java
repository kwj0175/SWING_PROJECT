package src.screen.recipe;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.SwingUtilities;
import java.awt.Insets;

import src.entity.Recipe;
import src.screen.MainScreen;
import src.screen.utils.ScreenHelper;
import src.screen.utils.VerticalScrollPanel;

public class RecipeScreen extends JPanel {
    private final RecipePresenter recipePresenter;

    private JLabel nameLabel;
    private JPanel imgPanel;
    private JLabel amountLabel;
    private JLabel timeLabel;
    private JPanel detailsPanel;
    private JPanel stepsPanel;
    private JScrollPane scrollPane; 

    public RecipeScreen(MainScreen mainScreen) {
        this.recipePresenter = new RecipePresenter(this);

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel form = buildForm();
        add(form, BorderLayout.CENTER);
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
        imgPanel.setBorder(null);
        imgPanel.setPreferredSize(new Dimension(320, 200));
        imgPanel.setLayout(new BorderLayout());
    }

    private JPanel buildAmountTimePanel() {
        amountLabel = ScreenHelper.setText("", 16);
        timeLabel = ScreenHelper.setText("", 16);

        JPanel amountTimePanel = ScreenHelper.darkCardPanel();
        amountTimePanel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
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
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
    }
    
    private JComponent createMultiLineText(String text) {
        JTextArea area = new JTextArea(text);
        area.setLineWrap(true);          
        area.setWrapStyleWord(true);    
        area.setEditable(false);        
        area.setOpaque(false);         
        area.setBorder(null);          
        
        area.setMargin(new Insets(0, 0, 0, 0));

       
        JLabel tmp = ScreenHelper.setText("");
        area.setFont(tmp.getFont());

        return area;
    }

    private JPanel buildForm() {
        JPanel root = ScreenHelper.darkCardPanel();
        root.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        root.setLayout(new BorderLayout());

        
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
                        .addGap(5)
                        .addComponent(amountTimePanel)
        );

        
        buildDetailsPanel();
        buildStepsPanel();

        
        JPanel content = new VerticalScrollPanel(); 
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(header);
        content.add(Box.createVerticalStrut(15));  
        content.add(detailsPanel);
        content.add(Box.createVerticalStrut(8));   
        content.add(stepsPanel);

        
        scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);

        JViewport viewport = scrollPane.getViewport();
        viewport.setOpaque(true);
        viewport.setBackground(root.getBackground());

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

       
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setOpaque(false);
        vBar.setUnitIncrement(16);
        vBar.setPreferredSize(new Dimension(8, 0));
        
        vBar.setPreferredSize(new Dimension(0, 0));

        vBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 255, 255, 80);
                this.trackColor = new Color(0, 0, 0, 0);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

       
        root.add(scrollPane, BorderLayout.CENTER);

        return root;
    }
  

    public void setRecipe(Recipe recipe) {
        recipePresenter.showRecipe(recipe);
    }

    void updateRecipeView(String name, String imagePath,
                          String amount, String time,
                          List<String> details, String[] steps) {

        nameLabel.setText(name != null ? name : "");

        imgPanel.removeAll();
        if (imagePath != null && !imagePath.isEmpty()) {
            imgPanel.add(new ImagePanel(imagePath), BorderLayout.CENTER);
        }
        imgPanel.revalidate();
        imgPanel.repaint();

        amountLabel.setText(amount != null ? amount : "");
        timeLabel.setText(time != null ? time : "");

        detailsPanel.removeAll();
        if (details != null) {
            for (String data : details) {
                JLabel label = ScreenHelper.setText(data);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                detailsPanel.add(label);
            }
        }
        detailsPanel.revalidate();
        detailsPanel.repaint();

        stepsPanel.removeAll();
        if (steps != null) {
        	int index = 1;
        	boolean first = true;

            for (String raw : steps) {
                if (raw == null) continue;

                
                String[] parts = raw.split("/");

                for (String part : parts) {
                	if (part == null) continue;
                    
                    String cleaned = part.stripLeading();

                    if (cleaned.isEmpty()) continue;

                    String numbered = index + ". " + cleaned;   
                    JComponent comp = createMultiLineText(numbered);
                    
                    
                    
                    if (!first) {
                        stepsPanel.add(Box.createVerticalStrut(4)); 
                    }
                    stepsPanel.add(comp);
                    first = false;
                    index++;
            }
        }
        stepsPanel.revalidate();
        stepsPanel.repaint();
        
        SwingUtilities.invokeLater(() -> {
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
        
    }

}
}