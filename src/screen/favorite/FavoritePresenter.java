package src.screen.favorite;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;
import src.screen.category.CategoryPresenter;
import src.screen.category.CategoryScreen;

import java.util.List;

public class FavoritePresenter extends CategoryPresenter {

    public FavoritePresenter(CategoryScreen view, RecipeManager recipeManager) {
        super(view, recipeManager);
    }

    @Override
    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeManager.getFavoriteRecipesByCategory(category);
    }
}
