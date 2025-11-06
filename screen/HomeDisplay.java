package screen;

import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeDisplay extends JFrame {
    private User currentUser;
    // private RecipeManager recipeManager;
    // private List<Recipe> recommendedRecipes;

    private JLabel welcomeLabel;
    private JButton viewMenuButton;
    private JButton viewPlannerButton;
    private JButton viewFavoritesButton;

    public HomeDisplay(User user) {
        this.currentUser = user;
        // this.recipeManager = new RecipeManager(); // (RecipeManager 구현 후 주석 해제)

        setTitle("MySmartRecipe - 홈");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. "OOO님 환영합니다"
        welcomeLabel = new JLabel(currentUser.getName() + "님, 환영합니다!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setBounds(50, 30, 400, 30);
        add(welcomeLabel);

        // 2. "오늘의 추천 메뉴" 영역
        JLabel recommendTitle = new JLabel("오늘의 추천 메뉴");
        recommendTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        recommendTitle.setBounds(50, 150, 200, 30);
        add(recommendTitle);

        // (RecipeManager 구현 후)
        // recommendedRecipes = recipeManager.getRandomRecipes(3); // 3개 랜덤 추출

        // (임시) 추천 메뉴 1
        JLabel recipeLabel1 = new JLabel("추천메뉴 1 (클릭)"); // + recommendedRecipes.get(0).getName()
        recipeLabel1.setBounds(70, 190, 200, 25);
        recipeLabel1.setForeground(Color.BLUE);
        add(recipeLabel1);

        // (임시) 추천 메뉴 2
        JLabel recipeLabel2 = new JLabel("추천메뉴 2 (클릭)"); // + recommendedRecipes.get(1).getName()
        recipeLabel2.setBounds(70, 220, 200, 25);
        recipeLabel2.setForeground(Color.BLUE);
        add(recipeLabel2);

        // 3. 버튼 영역 (스케치 기반)
        viewMenuButton = new JButton("메뉴 보기");
        viewMenuButton.setBounds(50, 300, 180, 50);
        add(viewMenuButton);

        viewFavoritesButton = new JButton("즐겨찾기한 메뉴");
        viewFavoritesButton.setBounds(270, 300, 180, 50);
        add(viewFavoritesButton);

        viewPlannerButton = new JButton("플래너");
        viewPlannerButton.setBounds(50, 370, 180, 50);
        add(viewPlannerButton);


        //  '오늘의 추천 메뉴' 1번 클릭 시
        recipeLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // (RecipeManager 구현 후)
                // Recipe clickedRecipe = recommendedRecipes.get(0);
                // new RecipeDetailDialog(HomeFrame.this, clickedRecipe).setVisible(true);
                JOptionPane.showMessageDialog(HomeDisplay.this, "레시피 상세 창 띄우기 (미구현)");
            }
        });

        // '메뉴 보기' 버튼 클릭 시
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new CategoryFrame().setVisible(true); // (CategoryFrame 구현 후 주석 해제)
                JOptionPane.showMessageDialog(HomeDisplay.this, "메뉴 카테고리 창 띄우기 (미구현)");
                dispose();
            }
        });

        // '플래너' 버튼 클릭 시
        viewPlannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // (PlannerFrame 구현 후 주석 해제)
                // new PlannerFrame(currentUser).setVisible(true);
                JOptionPane.showMessageDialog(HomeDisplay.this, "플래너 창 띄우기 (미구현)");
                dispose();
            }
        });
    }
}
