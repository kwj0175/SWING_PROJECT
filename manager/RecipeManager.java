package manager;
import entity.Recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//임시
public class RecipeManager {

    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private Random random = new Random();

    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "main_side_dishes.txt", "메인 반찬",
            "rice_dishes.txt", "밥류",
            "side_dishes.txt", "반찬",
            "soups.txt", "국/찌개류"
    );

    public RecipeManager() {
        loadRecipesFromAllFiles();
    }


    public List<Recipe> getRecommendationsPerCategory() {
        ArrayList<Recipe> recommendations = new ArrayList<>();

        for (String category : CATEGORY_MAP.values()) {

            List<Recipe> filteredList = new ArrayList<>();
            for (Recipe r : allRecipes) {
                if (r.getCategory() != null && r.getCategory().equals(category)) {
                    filteredList.add(r);
                }
            }

            if (!filteredList.isEmpty()) {
                Recipe picked = filteredList.get(random.nextInt(filteredList.size()));
                recommendations.add(picked);
            }
        }
        return recommendations;
    }


    private void loadRecipesFromAllFiles() {
        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            String fileName = entry.getKey();
            String category = entry.getValue();

            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("경고: " + fileName + " 파일을 찾을 수 없습니다.");
                continue;
            }

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    Recipe r = new Recipe();
                    r.read(sc);
                    if (r.getName() != null) {
                        r.setCategory(category);
                        allRecipes.add(r);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("레시피 파일 로드 중 오류: " + e.getMessage());
            }
        }
        System.out.println("총 " + allRecipes.size() + "개의 레시피를 로드했습니다.");
    }
}