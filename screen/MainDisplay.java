package screen;

import java.awt.BorderLayout;
import java.util.List;               
import javax.swing.*;

import entity.Recipe;
import manager.RecipeDatasetLoader;  

public class MainDisplay extends JFrame {

    private RecipeDetailPanel recipeDetailPanel;

    public MainDisplay() {
        setTitle("레시피 메인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
       
        //리스트 읽어오기  
        List<Recipe> soups       = RecipeDatasetLoader.loadSoups();          // 국/찌개
        List<Recipe> sideDishes  = RecipeDatasetLoader.loadSideDishes();     // 반찬
        List<Recipe> riceDishes  = RecipeDatasetLoader.loadRiceDishes();     // 밥요리
        List<Recipe> mainSides   = RecipeDatasetLoader.loadMainSideDishes(); // 메인반찬

        // 2)레시피 보여주기
        if (!riceDishes.isEmpty()) {
            recipeDetailPanel = new RecipeDetailPanel(riceDishes.get(20));
        } else {
            recipeDetailPanel = new RecipeDetailPanel();
        }

        add(recipeDetailPanel, BorderLayout.CENTER);
    }
}
