package src.screen.category;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryPresenter {

    private final CategoryScreen view;
    private final RecipeManager recipeManager;

    public CategoryPresenter(CategoryScreen view, RecipeManager recipeManager) {
        this.view = view;
        this.recipeManager = recipeManager;
    }

    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeManager.getRecipesByCategory(category);
    }

    public List<Recipe> filterRecipes(List<Recipe> recipes, String searchText) {
        // 1. 검색어가 비어 있거나 null인 경우, 원본 목록 전체를 반환합니다.
        if (searchText == null || searchText.trim().isEmpty()) {
            return recipes;
        }

        // 2. 검색을 위해 소문자 문자열을 준비합니다.
        final String search = searchText.trim().toLowerCase();

        // 3. Java Stream API를 사용하여 필터링을 수행합니다.
        return recipes.stream()
                // 레시피 이름이 검색어를 포함하는지 확인 (대소문자 무시)
                .filter(recipe -> recipe.getName().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }
}
