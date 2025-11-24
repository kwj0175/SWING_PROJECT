package src.screen.recipe;

import entity.Recipe;
import screen.MainScreen;
import screen.utils.IconHelper;
import screen.utils.ScreenHelper;
import screen.utils.VerticalScrollPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.List;

public class RecipeScreen extends JPanel {

    private final RecipePresenter recipePresenter;
    private final MainScreen mainScreen;

    private JLabel nameLabel;
    private JPanel imgPanel;
    private JLabel amountLabel;
    private JLabel timeLabel;
    private JPanel detailsPanel;
    private JPanel stepsPanel;
    private JScrollPane scrollPane;

    // 제목 왼쪽 즐겨찾기 / 화면 오른쪽 상단 추가 버튼
    private JToggleButton favoriteButton;
    private JButton addRecipeButton;

    public RecipeScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        this.recipePresenter = new RecipePresenter(this);

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel form = buildForm();
        JPanel topBar = buildTopBar();   // 화면 오른쪽 상단 추가 버튼

        add(topBar, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
    }

    /* ---------- 상단 오른쪽 추가 버튼 ---------- */

    private JPanel buildTopBar() {
        addRecipeButton = new JButton();
        ImageIcon addIcon = IconHelper.getAddRecipe();
        if (addIcon != null) {
            addRecipeButton.setIcon(addIcon);
        
        }
        addRecipeButton.setMargin(new Insets(0, 0, 0, 0));
        addRecipeButton.setBorderPainted(false);
        addRecipeButton.setContentAreaFilled(false);
        addRecipeButton.setFocusPainted(false);
        addRecipeButton.setPreferredSize(new Dimension(34, 34));

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrap.setOpaque(false);
        rightWrap.add(addRecipeButton);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 10));
        bar.add(rightWrap, BorderLayout.CENTER);

        return bar;
    }

    /* ---------- 제목 / 이미지 / 인분·시간 ---------- */

    private JPanel buildNamePanel() {
        nameLabel = ScreenHelper.setText("", 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        favoriteButton = new JToggleButton();
        ImageIcon favIcon = IconHelper.getFavorite();
        if (favIcon != null) {
            favoriteButton.setIcon(favIcon);
        
        }
        favoriteButton.setMargin(new Insets(0, 0, 0, 0));
        favoriteButton.setBorderPainted(false);
        favoriteButton.setContentAreaFilled(false);
        favoriteButton.setFocusPainted(false);
        favoriteButton.setPreferredSize(new Dimension(30, 30));

        JPanel namePanel = ScreenHelper.darkCardPanel();
        namePanel.setLayout(new BorderLayout());

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrapper.setOpaque(false);
        leftWrapper.add(favoriteButton);

        namePanel.add(leftWrapper, BorderLayout.WEST);
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
        amountTimePanel.setBorder(
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        );
        GroupLayout layout = ScreenHelper.groupLayout(amountTimePanel);

        int HORIZONTAL_GAP = 15;

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(amountLabel)
                        .addGap(HORIZONTAL_GAP)
                        .addComponent(timeLabel)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(amountLabel)
                        .addComponent(timeLabel)
        );

        return amountTimePanel;
    }

    /* ---------- 재료 / 조리방법 패널 ---------- */

    private void buildDetailsPanel() {
        detailsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        detailsPanel.setOpaque(false);
    }

    private void buildStepsPanel() {
        stepsPanel = new JPanel();
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

    /* ---------- 전체 폼 + 스크롤 ---------- */

    private JPanel buildForm() {
        JPanel root = ScreenHelper.darkCardPanel();
        root.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        root.setLayout(new BorderLayout());

        // 헤더 (제목 + 이미지 + 인분/시간)
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

        // 재료 / 조리방법
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

        // 스크롤 전체 적용
        scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JViewport viewport = scrollPane.getViewport();
        viewport.setOpaque(true);
        viewport.setBackground(root.getBackground());

        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setOpaque(false);
        vBar.setUnitIncrement(16);
        vBar.setPreferredSize(new Dimension(0, 0)); // 안 보이게

        vBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 0, 0, 0);
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

    /* ---------- 외부에서 레시피 세팅 ---------- */

    public void setRecipe(Recipe recipe) {
        recipePresenter.showRecipe(recipe);

        // 레시피 바뀔 때마다 맨 위로 스크롤
        SwingUtilities.invokeLater(() -> {
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }

    /* ---------- 뷰 업데이트 ---------- */

    void updateRecipeView(String name, String imagePath,
                          String amount, String time,
                          List<String> details, String[] steps) {

        // 제목
        nameLabel.setText(name != null ? name : "");

        // 이미지
        imgPanel.removeAll();
        if (imagePath != null && !imagePath.isEmpty()) {
            imgPanel.add(new ImagePanel(imagePath), BorderLayout.CENTER);
        }
        imgPanel.revalidate();
        imgPanel.repaint();

        // 인분/시간
        amountLabel.setText(amount != null ? amount : "");
        timeLabel.setText(time != null ? time : "");

        // 재료
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

        // 조리방법
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
                    comp.setAlignmentX(Component.LEFT_ALIGNMENT);

                    if (!first) {
                        stepsPanel.add(Box.createVerticalStrut(4));
                    }
                    stepsPanel.add(comp);

                    first = false;
                    index++;
                }
            }
        }
        stepsPanel.revalidate();
        stepsPanel.repaint();
    }
}
