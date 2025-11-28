package src.view.favorite;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.service.RecipeService;
import src.view.category.CategoryPresenter;

import java.util.List;

public class FavoritePresenter extends CategoryPresenter {

    public FavoritePresenter(RecipeService recipeService) {
        super(recipeService);
    }

    @Override
    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeService.getFavoriteRecipesByCategory(category);
    }
}
