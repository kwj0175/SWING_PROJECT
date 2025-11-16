package screen.home;

import entity.User;
import screen.MainScreen;
import screen.utils.ScreenHelper;

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
        c.gridwidth = c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
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

        JPanel root = ScreenHelper.noColorCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(root, false, false);

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
        welcomeLabel = ScreenHelper.setText("", 18);

        JPanel infoPanel = ScreenHelper.noColorCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(infoPanel);

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

    private JLabel recommendMenu() {
        JLabel recipeLabel  = ScreenHelper.setText("추천 메뉴 (클릭)");
        recipeLabel.setBounds(70, 190, 200, 25);
        recipeLabel.setForeground(Color.BLUE);
        return recipeLabel;
    }

    private JPanel recommendPanel() {
        // 2. "오늘의 추천 메뉴" 영역
        JLabel recommendTitle = ScreenHelper.setText("오늘의 추천 메뉴", 16);
        JLabel recipeLabel1 = recommendMenu();
        JLabel recipeLabel2 = recommendMenu();

        int TITLE_GAP = 30;
        int RECIPE_GAP = 10;

        JPanel recommendPanel = ScreenHelper.darkCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(recommendPanel);

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
        viewMenuButton = ScreenHelper.secondaryButton("메뉴보기", 50, 300, 180, 100);
        viewFavoritesButton = ScreenHelper.secondaryButton("즐겨찾기", 270, 300, 180, 50);
        viewPlannerButton = ScreenHelper.secondaryButton("플래너", 50, 370, 180, 50);

        int HORIZONTAL_GAP = 50;
        int VERTICAL_GAP = 20;

        JPanel menuPanel = ScreenHelper.darkCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(menuPanel, true, true);

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
}