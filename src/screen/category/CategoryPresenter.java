package src.screen.category;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryPresenter {

    private final CategoryScreen view;
    protected final RecipeManager recipeManager;

    public CategoryPresenter(CategoryScreen view, RecipeManager recipeManager) {
        this.view = view;
        this.recipeManager = recipeManager;
    }

    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeManager.getRecipesByCategory(category);
    }

    public List<Recipe> filterRecipes(List<Recipe> recipes, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return recipes;
        }

        final String search = searchText.trim().toLowerCase();

        return recipes.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }
}
