package src.view.favorite;

import src.entity.FoodCategory;
import src.entity.Recipe;
import src.service.RecipeService;
import src.view.MainScreen;
import src.view.category.CategoryView;

import javax.swing.*;

public class FavoriteView extends CategoryView {

    public FavoriteView(MainScreen mainScreen, RecipeService recipeService) {
        super(mainScreen, recipeService);
    }

    @Override
    protected FavoritePresenter createPresenter(RecipeService recipeService) {
        return new FavoritePresenter(this, recipeService);
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
