package src.view.category;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.service.RecipeService;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryPresenter {

    protected final RecipeService recipeService;

    public CategoryPresenter(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeService.getRecipesByCategory(category);
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
