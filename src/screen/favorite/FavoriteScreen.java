package src.screen.favorite;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.manager.RecipeManager;
import src.screen.MainScreen;
import src.screen.category.CategoryScreen;

import javax.swing.*;

public class FavoriteScreen extends CategoryScreen {

    public FavoriteScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        super(mainScreen, recipeManager);
    }

    @Override
    protected FavoritePresenter createPresenter(RecipeManager recipeManager) {
        return new FavoritePresenter(this, recipeManager);
    }

    public void refreshFavoriteList() {
        String currentCardName = getCurrentCardName();
        cards.removeAll();

        for (FoodCategory cat : FoodCategory.values()) {
            java.util.List<Recipe> recipes = getRecipesByCategory(cat);
            JScrollPane scrollPanel = createScrollPanel(recipes);
            scrollPanel.setName(cat.name());
            cards.add(scrollPanel, cat.name());
        }

        if (currentCardName != null) {
            cardLayout.show(cards, currentCardName);
        }

        cards.revalidate();
        cards.repaint();
    }

}
