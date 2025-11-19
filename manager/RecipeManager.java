package manager;

import entity.FoodCategory;
import entity.Recipe;
import java.io.File;
import java.util.*;

public class RecipeManager {
    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private Random random = new Random();

    private static final String FILE_PATH = "datasets/texts/";

    // 파일명 -> Enum 매핑
    private static final Map<String, FoodCategory> FILE_TO_ENUM_MAP = Map.of(
            "main_side_dishes.txt", FoodCategory.MAIN_SIDE_DISH,
            "rice_dishes.txt", FoodCategory.RICE_DISH,
            "side_dishes.txt", FoodCategory.SIDE_DISH,
            "soups.txt", FoodCategory.SOUP
    );

    public RecipeManager() {
        loadRecipesFromAllFiles();
    }

    public List<Recipe> getRecommendationsPerCategory() {
        return getRecommendationsByIngredients(null);
    }

    // 재료 기반 추천 로직 (Enum 사용)
    public List<Recipe> getRecommendationsByIngredients(String userIngredients) {
        List<Recipe> results = new ArrayList<>();
        FoodCategory[] categories = FoodCategory.values();

        List<String> inputIngredients = new ArrayList<>();
        if (userIngredients != null && !userIngredients.trim().isEmpty()) {
            String[] split = userIngredients.split("[,\\s]+");
            inputIngredients.addAll(Arrays.asList(split));
        }

        for (FoodCategory cat : categories) {
            List<Recipe> catRecipes = new ArrayList<>();
            for (Recipe r : allRecipes) {
                if (r.checkCat(cat)) {
                    catRecipes.add(r);
                }
            }

            if (catRecipes.isEmpty()) continue;

            List<Recipe> matchedRecipes = new ArrayList<>();
            if (!inputIngredients.isEmpty()) {
                for (Recipe r : catRecipes) {
                    // details 배열을 돌며 확인
                    for (String ing : inputIngredients) {
                        for (String detail : r.getDetails()) {
                            if (detail.contains(ing)) {
                                matchedRecipes.add(r);
                                break; // 해당 재료 찾음
                            }
                        }
                        if (!matchedRecipes.isEmpty() && matchedRecipes.get(matchedRecipes.size()-1) == r) break;
                    }
                }
            }

            if (!matchedRecipes.isEmpty()) {
                results.add(matchedRecipes.get(random.nextInt(matchedRecipes.size())));
            } else {
                results.add(catRecipes.get(random.nextInt(catRecipes.size())));
            }
        }
        return results;
    }

    public ArrayList<Recipe> getAllRecipes() {
        return allRecipes;
    }

    private void loadRecipesFromAllFiles() {
        for (Map.Entry<String, FoodCategory> entry : FILE_TO_ENUM_MAP.entrySet()) {
            File file = new File(FILE_PATH + entry.getKey());

            if (!file.exists()) continue;

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    Recipe r = new Recipe();
                    r.read(sc); // 텍스트 파싱 (이미지 경로도 여기서 자동 설정됨)

                    if (r.getTitle() != null) {
                        r.setCategory(entry.getValue()); // Enum 카테고리 설정
                        allRecipes.add(r);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("총 " + allRecipes.size() + "개의 레시피 로드 완료.");
    }
}