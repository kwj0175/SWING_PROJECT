package src.screen.favorite;

import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.category.CategoryPresenter;
import src.screen.category.CategoryScreen;

public class FavoriteScreen extends CategoryScreen {

    public FavoriteScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        super(mainScreen, recipeManager);
    }

    @Override
    protected CategoryPresenter createPresenter(RecipeManager recipeManager) {
        return new FavoritePresenter(this, recipeManager);
    }
}
