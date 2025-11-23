package screen.category;

import entity.FoodCategory;
import entity.Recipe;
import manager.RecipeManager;

import java.util.List;

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
}
