package manager;

import entity.Recipe;
import java.io.File;
import java.util.*;

public class RecipeManager {
    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private Random random = new Random();

    private static final String FILE_PATH = "datasets/texts/";

    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "main_side_dishes.txt", "메인 반찬",
            "rice_dishes.txt", "밥류",
            "side_dishes.txt", "반찬",
            "soups.txt", "국/찌개"
    );

    public RecipeManager() {
        loadRecipesFromAllFiles();
    }

    public List<Recipe> getRecommendationsPerCategory() {
        return getRecommendationsByIngredients(null);
    }

    // 재료 기반 추천 로직
    public List<Recipe> getRecommendationsByIngredients(String userIngredients) {
        List<Recipe> results = new ArrayList<>();
        String[] categories = {"메인 반찬", "밥류", "반찬", "국/찌개"};

        List<String> inputIngredients = new ArrayList<>();
        if (userIngredients != null && !userIngredients.trim().isEmpty()) {
            String[] split = userIngredients.split("[,\\s]+");
            inputIngredients.addAll(Arrays.asList(split));
        }

        for (String cat : categories) {
            List<Recipe> catRecipes = new ArrayList<>();
            for (Recipe r : allRecipes) {
                if (cat.equals(r.getCategory())) {
                    catRecipes.add(r);
                }
            }

            if (catRecipes.isEmpty()) continue;

            List<Recipe> matchedRecipes = new ArrayList<>();
            if (!inputIngredients.isEmpty()) {
                for (Recipe r : catRecipes) {
                    for (String ing : inputIngredients) {
                        if (r.getIngredients().contains(ing)) {
                            matchedRecipes.add(r);
                            break;
                        }
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
        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            File file = new File(FILE_PATH + entry.getKey());

            if (!file.exists()) {
                System.out.println("파일을 찾을 수 없습니다: " + file.getAbsolutePath());
                continue;
            }

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    Recipe r = new Recipe();
                    r.read(sc);
                    if (r.getName() != null) {
                        r.setCategory(entry.getValue());
                        allRecipes.add(r);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("총 " + allRecipes.size() + "개의 레시피를 로드했습니다.");
    }
}