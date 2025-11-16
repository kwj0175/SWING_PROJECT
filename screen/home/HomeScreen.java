package screen.home;

import entity.User;
import screen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {
    private User currentUser;
    // private RecipeManager recipeManager;
    // private List<Recipe> recommendedRecipes;

    private JLabel welcomeLabel;
    private JButton viewMenuButton;
    private JButton viewPlannerButton;
    private JButton viewFavoritesButton;

    public HomeScreen(MainScreen mainScreen) {
        currentUser = null;
        setOpaque(false);
        setLayout(new GridBagLayout());
        JPanel form = buildForm();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        add(form, c);
        // this.recipeManager = new RecipeManager(); // (RecipeManager 구현 후 주석 해제)
        // (RecipeManager 구현 후)
        // recommendedRecipes = recipeManager.getRandomRecipes(3); // 3개 랜덤 추출

        //  '오늘의 추천 메뉴' 1번 클릭 시
//        recipeLabel1.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                // (RecipeManager 구현 후)
//                // Recipe clickedRecipe = recommendedRecipes.get(0);
//                // new RecipeDetailDialog(HomeFrame.this, clickedRecipe).setVisible(true);
//                JOptionPane.showMessageDialog(HomeDisplay.this, "레시피 상세 창 띄우기 (미구현)");
//            }
//        });

        // '메뉴 보기' 버튼 클릭 시
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.displayCategoryScreen();
            }
        });

        // '플래너' 버튼 클릭 시
        viewPlannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainScreen.displayPlannerScreen();
            }
        });
    }

    private JPanel buildForm() {
        JPanel infoPanel = infoPanel();
        JPanel recommendPanel = recommendPanel();
        JPanel menuPanel = menuPanel();

        JPanel root = new JPanel();
        GroupLayout formLayout = new GroupLayout(root);
        root.setLayout(formLayout);

        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(false);

        int TOP_BOTTOM_GAP = 10;
        int PANEL_GAP = 30;

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(infoPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(recommendPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(menuPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addGap(TOP_BOTTOM_GAP)
                        .addComponent(infoPanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(PANEL_GAP)
                        .addComponent(recommendPanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(PANEL_GAP)
                        .addComponent(menuPanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(TOP_BOTTOM_GAP)
        );

        formLayout.linkSize(SwingConstants.HORIZONTAL, infoPanel, recommendPanel, menuPanel);
        return root;
    }

    private JPanel infoPanel() {
        JComponent profile = new ProfilePanel(50);
        welcomeLabel = setLabelWithFont(new JLabel(""), "SansSerif", Font.BOLD, 18);

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GroupLayout formLayout = new GroupLayout(infoPanel);
        infoPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(true);

        int HORIZONTAL_GAP = 30;

        formLayout.setHorizontalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(profile,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(HORIZONTAL_GAP)
                        .addComponent(welcomeLabel)
        );

        formLayout.setVerticalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(profile)
                        .addComponent(welcomeLabel)
        );

        return infoPanel;
    }

    private JPanel recommendPanel() {
        // 2. "오늘의 추천 메뉴" 영역
        JLabel recommendTitle = setLabelWithFont(new JLabel("오늘의 추천 메뉴"), "SansSerif", Font.BOLD, 16);

        // (임시) 추천 메뉴 1
        JLabel recipeLabel1 = new JLabel("추천메뉴 1 (클릭)"); // + recommendedRecipes.get(0).getName()
        recipeLabel1.setBounds(70, 190, 200, 25);
        recipeLabel1.setForeground(Color.BLUE);

        // (임시) 추천 메뉴 2
        JLabel recipeLabel2 = new JLabel("추천메뉴 2 (클릭)"); // + recommendedRecipes.get(1).getName()
        recipeLabel2.setBounds(70, 220, 200, 25);
        recipeLabel2.setForeground(Color.BLUE);

        int TITLE_GAP = 30;
        int RECIPE_GAP = 10;

        JPanel recommendPanel = new JPanel();
        recommendPanel.setOpaque(true);
        recommendPanel.setBackground(Color.DARK_GRAY);
        recommendPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GroupLayout formLayout = new GroupLayout(recommendPanel);
        recommendPanel.setLayout(formLayout);

        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(true);

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(recommendTitle)
                        .addComponent(recipeLabel1)
                        .addComponent(recipeLabel2)
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(recommendTitle)
                        .addGap(TITLE_GAP)
                        .addComponent(recipeLabel1)
                        .addGap(RECIPE_GAP)
                        .addComponent(recipeLabel2)
        );

        return recommendPanel;
    }

    private JPanel menuPanel() {
        // 3. 버튼 영역 (스케치 기반)
        viewMenuButton = new JButton("메뉴 보기");
        viewMenuButton.setBounds(50, 300, 180, 100);

        viewFavoritesButton = new JButton("즐겨찾기");
        viewFavoritesButton.setBounds(270, 300, 180, 50);

        viewPlannerButton = new JButton("플래너");
        viewPlannerButton.setBounds(50, 370, 180, 50);

        int HORIZONTAL_GAP = 50;
        int VERTICAL_GAP = 20;

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(true);
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GroupLayout formLayout = new GroupLayout(menuPanel);
        menuPanel.setLayout(formLayout);

        formLayout.setAutoCreateGaps(true);
        formLayout.setAutoCreateContainerGaps(true);

        formLayout.setHorizontalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(viewMenuButton)
                        .addGap(HORIZONTAL_GAP)
                        .addGroup(
                                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(viewFavoritesButton)
                                        .addComponent(viewPlannerButton)
                        )
        );

        formLayout.setVerticalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(viewMenuButton)
                        .addGroup(
                                formLayout.createSequentialGroup()
                                        .addComponent(viewFavoritesButton)
                                        .addGap(VERTICAL_GAP)
                                        .addComponent(viewPlannerButton)
                        )
        );

        return menuPanel;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        welcomeLabel.setText(currentUser.getName() + "님, 환영합니다.");
        revalidate();
        repaint();
    }

    private JLabel setLabelWithFont(JLabel label, String font, int style, int size) {
        label.setFont(new Font(font, style, size));
        return label;
    }

}