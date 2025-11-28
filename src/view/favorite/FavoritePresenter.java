package src.view.favorite;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.service.RecipeService;
import src.view.category.CategoryPresenter;
import src.view.category.CategoryView;

import java.util.List;

public class FavoritePresenter extends CategoryPresenter {

    public FavoritePresenter(CategoryView view, RecipeService recipeService) {
        super(view, recipeService);
    }

    @Override
    public List<Recipe> loadRecipesByCategory(FoodCategory category) {
        return recipeService.getFavoriteRecipesByCategory(category);
    }
}
