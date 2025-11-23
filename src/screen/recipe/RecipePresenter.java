package src.screen.recipe;

import src.entity.Recipe;

import java.util.List;

public class RecipePresenter {
    private final RecipeScreen view;

    public RecipePresenter(RecipeScreen view) {
        this.view = view;
    }

    public void showRecipe(Recipe recipe) {
        if (recipe == null) {
            view.updateRecipeView(
                    "",
                    null,
                    "",
                    "",
                    List.of(),
                    new String[0]
            );
            return;
        }

        view.updateRecipeView(
                recipe.getName(),
                recipe.getImagePath(),
                recipe.getAmount(),
                recipe.getTime(),
                recipe.getDetails(),
                recipe.getSteps()
        );
    }
}
