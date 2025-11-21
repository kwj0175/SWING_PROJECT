package manager;

import entity.FoodCategory;
import entity.Recipe;
import java.util.*;

public class RecipeManager {
    private final List<Recipe> recipes;
    private final Map<FoodCategory, List<Recipe>> recipesByCategory = new EnumMap<>(FoodCategory.class);
    private final Random random = new Random();

    public RecipeManager(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        sortByCategory();
    }

    public List<Recipe> getRecommendationsPerCategory() {
        return getRecommendationsByIngredients(null);
    }

    private void sortByCategory() {
        for (FoodCategory category : FoodCategory.values()) {
            recipesByCategory.put(category, new ArrayList<>());
        }
        for (Recipe recipe : recipes) {
            recipesByCategory.get(recipe.getCategory()).add(recipe);
        }
    }

    private Set<String> parseUserInput(String input) {
        Set<String> set = new HashSet<>();

        if (input == null || input.trim().isEmpty()) {
            return set;
        }

        String[] keywords = input.split("[,\\s]+");
        for (String key : keywords) {
            String norm = key.trim();
            if (!norm.isEmpty()) set.add(norm);
            System.out.println(norm);
        }
        return set;
    }

    // 입력 없음 OR 점수 0 -> 랜덤
    // 점수 가장 높은 레시피 pick
    // 같은 점수끼리는 랜덤
    private Recipe pickBestRecipe(List<Recipe> candidates, Set<String> userSet) {
        if (userSet.isEmpty()) {
            return candidates.get(random.nextInt(candidates.size()));
        }

        Recipe best = null;
        int bestScore = 0;

        for (Recipe recipe : candidates) {
            int score = recipe.matchScore(userSet);
            if (score > bestScore) {
                bestScore = score;
                best = recipe;
            } else if (score == bestScore && score > 0) {
                if (random.nextBoolean()) {
                    best = recipe;
                }
            }
            System.out.println("Score: " + score + "재료: " + recipe.getDetails());
        }

        if (best != null) {
            return best;
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    public List<Recipe> getRecommendationsByIngredients(String input) {
        Set<String> userSet = parseUserInput(input);
        List<Recipe> results = new ArrayList<>();

        for (FoodCategory cat : FoodCategory.values()) {
            List<Recipe> catRecipes = recipesByCategory.get(cat);
            if (catRecipes.isEmpty()) continue;

            Recipe recipe = pickBestRecipe(catRecipes, userSet);
            results.add(recipe);
        }
        return results;
    }
}